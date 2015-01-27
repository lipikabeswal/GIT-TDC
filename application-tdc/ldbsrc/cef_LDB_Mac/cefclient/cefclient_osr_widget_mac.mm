// Copyright (c) 2013 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license
// that can be found in the LICENSE file.

#import <Cocoa/Cocoa.h>
#include <OpenGL/gl.h>

#include "cefclient/cefclient_osr_widget_mac.h"

#include "include/cef_browser.h"
#include "include/cef_client.h"
#include "cefclient/cefclient.h"
#include "cefclient/osrenderer.h"
#include "cefclient/resource_util.h"
#include "cefclient/util.h"

@interface ClientOpenGLView ()
- (float)getDeviceScaleFactor;
- (void)windowDidChangeBackingProperties:(NSNotification*)notification;

- (bool) isOverPopupWidgetX: (int) x andY: (int) y;
- (void) applyPopupOffsetToX: (int&) x andY: (int&) y;
- (int) getPopupXOffset;
- (int) getPopupYOffset;

- (void) sendMouseClick: (NSEvent *)event
                 button: (CefBrowserHost::MouseButtonType)type
                   isUp: (bool)isUp;

- (void) convertRects: (const CefRenderHandler::RectList&) rects
       toBackingRects: (CefRenderHandler::RectList*) scaled_rects
              andSize: (const NSSize) size
        toBackingSize: (NSSize*) scaled_size;

- (CefRect) convertRectToBackingInternal: (const CefRect&) rect;
- (CefRect) convertRectFromBackingInternal: (const CefRect&) rect;

@property (readwrite) bool was_last_mouse_down_on_view;
@end

namespace {

static CefRect convertRect(const NSRect& target, const NSRect& frame) {
  NSRect rect = target;
  rect.origin.y = NSMaxY(frame) - NSMaxY(target);
  return CefRect(rect.origin.x,
                 rect.origin.y,
                 rect.size.width,
                 rect.size.height);
}

}  // namespace

ClientOSRHandler::ClientOSRHandler(ClientOpenGLView* view,
                                   OSRBrowserProvider* browser_provider)
    : view_(view),
      painting_popup_(false) {
  [view_ retain];
  view_->browser_provider_ = browser_provider;

#if defined(__MAC_OS_X_VERSION_MAX_ALLOWED) && \
    __MAC_OS_X_VERSION_MAX_ALLOWED >= 1070
  // Do not override the delegate; the mac client already does that
  if ([view_ respondsToSelector:@selector(backingScaleFactor:)]) {
    [[NSNotificationCenter defaultCenter]
      addObserver:view_
         selector:@selector(windowDidChangeBackingProperties:)
             name:NSWindowDidChangeBackingPropertiesNotification
           object:[view_ window]];
  }
#endif
}

ClientOSRHandler:: ~ClientOSRHandler() {
#if defined(__MAC_OS_X_VERSION_MAX_ALLOWED) && \
    __MAC_OS_X_VERSION_MAX_ALLOWED >= 1070
  if ([view_ respondsToSelector:@selector(backingScaleFactor:)]) {
    [[NSNotificationCenter defaultCenter]
        removeObserver:view_
                  name:NSWindowDidChangeBackingPropertiesNotification
                object:[view_ window]];
  }
#endif
}

void ClientOSRHandler::Disconnect() {
  [view_ release];
  view_ = nil;
}

// CefRenderHandler methods
void ClientOSRHandler::OnBeforeClose(CefRefPtr<CefBrowser> browser) {
    
    if (view_)
    view_->browser_provider_ = NULL;
}

bool ClientOSRHandler::GetViewRect(CefRefPtr<CefBrowser> browser,
                                   CefRect& rect) {
  REQUIRE_UI_THREAD();

  if (!view_)
    return false;

  // The simulated screen and view rectangle are the same. This is necessary
  // for popup menus to be located and sized inside the view.
  const NSRect bounds = [view_ bounds];
  rect.x = rect.y = 0;
  rect.width = bounds.size.width;
  rect.height = bounds.size.height;
  return true;
}

bool ClientOSRHandler::GetScreenPoint(CefRefPtr<CefBrowser> browser,
                                      int viewX,
                                      int viewY,
                                      int& screenX,
                                      int& screenY) {
  REQUIRE_UI_THREAD();

  if (!view_)
    return false;

  // Convert the point from view coordinates to actual screen coordinates.
  NSRect bounds = [view_ bounds];
  NSPoint view_pt = NSMakePoint(viewX, bounds.size.height - viewY);
  NSPoint window_pt = [view_ convertPoint:view_pt toView:nil];
  NSPoint screen_pt = [[view_ window] convertBaseToScreen:window_pt];
  screenX = screen_pt.x;
  screenY = screen_pt.y;
  return true;
}

bool ClientOSRHandler::GetScreenInfo(CefRefPtr<CefBrowser> browser,
                                     CefScreenInfo& screen_info) {
  REQUIRE_UI_THREAD();

  if (!view_)
    return false;

  NSWindow* window = [view_ window];
  if (!window)
    return false;

  screen_info.device_scale_factor = [view_ getDeviceScaleFactor];

  NSScreen* screen = [window screen];
  if (!screen)
    screen = [NSScreen deepestScreen];

  screen_info.depth = NSBitsPerPixelFromDepth([screen depth]);
  screen_info.depth_per_component = NSBitsPerSampleFromDepth([screen depth]);
  screen_info.is_monochrome =
      [[screen colorSpace] colorSpaceModel] == NSGrayColorSpaceModel;
  // screen_info.is_monochrome = true;
  screen_info.rect = convertRect([screen frame], [screen frame]);
  screen_info.available_rect =
      convertRect([screen visibleFrame], [screen frame]);

  return true;
}

void ClientOSRHandler::OnPopupShow(CefRefPtr<CefBrowser> browser,
                                   bool show) {
  REQUIRE_UI_THREAD();

  if (!view_)
    return;

  if (!show) {
    CefRect client_popup_rect = view_->renderer_->popup_rect();

    // Clear the popup rectangles, so that the paint triggered by Invalidate
    // will not repaint the popup content over the OpenGL view.
    view_->renderer_->ClearPopupRects();
    CefRect scaled_rect =
        [view_ convertRectFromBackingInternal:client_popup_rect];
    browser->GetHost()->Invalidate(scaled_rect, PET_VIEW);
  }

  view_->renderer_->OnPopupShow(browser, show);
}

void ClientOSRHandler::OnPopupSize(CefRefPtr<CefBrowser> browser,
                                   const CefRect& rect) {
  REQUIRE_UI_THREAD();

  if (!view_)
    return;

  CefRect scaled_rect = [view_ convertRectToBackingInternal:rect];
  view_->renderer_->OnPopupSize(browser, scaled_rect);
}

void ClientOSRHandler::OnPaint(CefRefPtr<CefBrowser> browser,
                               PaintElementType type,
                               const RectList& dirtyRects,
                               const void* buffer,
                               int width, int height) {
  REQUIRE_UI_THREAD();

  if (!view_)
    return;

  if (painting_popup_) {
    RectList scaled_dirty_rects;
    NSSize scaled_size = NSMakeSize(0, 0);
    [view_ convertRects:dirtyRects
         toBackingRects:&scaled_dirty_rects
                andSize:NSMakeSize(width, height)
          toBackingSize:&scaled_size];
    view_->renderer_->OnPaint(browser, type, scaled_dirty_rects, buffer,
                              scaled_size.width, scaled_size.height);
    return;
  }

  NSOpenGLContext* context = [view_ openGLContext];
  [context makeCurrentContext];

  RectList scaled_dirty_rects;
  NSSize scaled_size = NSMakeSize(0, 0);
  [view_ convertRects:dirtyRects
       toBackingRects:&scaled_dirty_rects
              andSize:NSMakeSize(width, height)
        toBackingSize:&scaled_size];

  view_->renderer_->OnPaint(browser, type, scaled_dirty_rects, buffer,
                            scaled_size.width, scaled_size.height);

  if (type == PET_VIEW && !view_->renderer_->popup_rect().IsEmpty()) {
    painting_popup_ = true;
    CefRect client_popup_rect(0, 0,
                              view_->renderer_->popup_rect().width,
                              view_->renderer_->popup_rect().height);

    CefRect scaled_popup_rect =
        [view_ convertRectFromBackingInternal:client_popup_rect];

    browser->GetHost()->Invalidate(scaled_popup_rect, PET_POPUP);
    painting_popup_ = false;
  }

  view_->renderer_->Render();
  [context flushBuffer];
}

void ClientOSRHandler::OnCursorChange(CefRefPtr<CefBrowser> browser,
                                      CefCursorHandle cursor) {
  REQUIRE_UI_THREAD();
  [cursor set];
}

void ClientOSRHandler::SetLoading(bool isLoading) {
}

@implementation ClientOpenGLView

@synthesize was_last_mouse_down_on_view = was_last_mouse_down_on_view_;

- (id)initWithFrame:(NSRect)frame andTransparency:(bool)transparency {
  NSOpenGLPixelFormat * pixelFormat =
      [[NSOpenGLPixelFormat alloc]
       initWithAttributes:(NSOpenGLPixelFormatAttribute[]) {
           NSOpenGLPFAWindow,
           NSOpenGLPFADoubleBuffer,
           NSOpenGLPFADepthSize,
           32,
           0}];
  [pixelFormat autorelease];

  self = [super initWithFrame:frame pixelFormat:pixelFormat];
  if (self) {
    renderer_ = new ClientOSRenderer(transparency);
    rotating_ = false;

    tracking_area_ =
        [[NSTrackingArea alloc] initWithRect:frame
                                     options:NSTrackingMouseMoved |
                                             NSTrackingActiveInActiveApp |
                                             NSTrackingInVisibleRect
                                       owner:self
                                    userInfo:nil];
    [self addTrackingArea:tracking_area_];
  }

#if defined(__MAC_OS_X_VERSION_MAX_ALLOWED) && \
    __MAC_OS_X_VERSION_MAX_ALLOWED >= 1070
  if ([self respondsToSelector:@selector(setWantsBestResolutionOpenGLSurface:)]) {
    // enable HiDPI buffer
    [self setWantsBestResolutionOpenGLSurface:YES];
  }
#endif

  return self;
}

- (void)dealloc {
  CefRefPtr<CefBrowser> browser = [self getBrowser];
  if (browser) {
    static_cast<ClientOSRHandler*>(
      browser->GetHost()->GetClient()->GetRenderHandler().get())->Disconnect();
    browser->GetHost()->CloseBrowser(true);
    browser = NULL;
  }
  if (renderer_)
    delete renderer_;

  [super dealloc];
}

- (CefRefPtr<CefBrowser>)getBrowser {
  return browser_provider_->GetBrowser();
}

- (void)setFrame:(NSRect)frameRect {
  CefRefPtr<CefBrowser> browser = [self getBrowser];
  if (!browser)
    return;

  [super setFrame:frameRect];
  browser->GetHost()->WasResized();
}

- (void) sendMouseClick:(NSEvent *)event
                 button: (CefBrowserHost::MouseButtonType)type
                   isUp: (bool)isUp {
  CefRefPtr<CefBrowser> browser = [self getBrowser];
  if (!browser)
    return;

  CefMouseEvent mouseEvent;
  [self getMouseEvent: mouseEvent forEvent: event];
  NSPoint point = [self getClickPointForEvent:event];
  if (!isUp)
    self.was_last_mouse_down_on_view = ![self isOverPopupWidgetX: point.x
                                                            andY: point.y];
  else if (self.was_last_mouse_down_on_view &&
           [self isOverPopupWidgetX:point.x andY: point.y] &&
           ([self getPopupXOffset] || [self getPopupYOffset])) {
    return;
  }

  browser->GetHost()->SendMouseClickEvent(mouseEvent,
                                           type,
                                           isUp,
                                           [event clickCount]);
}

- (void)mouseDown:(NSEvent *)event {
  [self sendMouseClick: event button:MBT_LEFT isUp:false];
}

- (void)rightMouseDown:(NSEvent *)event {
  if ([event modifierFlags] & NSShiftKeyMask) {
    // Start rotation effect.
    last_mouse_pos_ = cur_mouse_pos_ = [self getClickPointForEvent:event];
    rotating_ = true;
    return;
  }

  [self sendMouseClick: event button:MBT_RIGHT isUp:false];
}

- (void)otherMouseDown:(NSEvent *)event {
  [self sendMouseClick: event button:MBT_MIDDLE isUp:false];
}

- (void)mouseUp:(NSEvent *)event {
  [self sendMouseClick: event button: MBT_LEFT isUp: true];
}

- (void)rightMouseUp:(NSEvent *)event {
  if (rotating_) {
    // End rotation effect.
    renderer_->SetSpin(0, 0);
    rotating_ = false;
    [self setNeedsDisplay:YES];
    return;
  }
  [self sendMouseClick: event button: MBT_RIGHT isUp: true];
}

- (void)otherMouseUp:(NSEvent *)event {
  [self sendMouseClick: event button: MBT_MIDDLE isUp: true];
}

- (void)mouseMoved:(NSEvent *)event {
  CefRefPtr<CefBrowser> browser = [self getBrowser];
  if (!browser)
    return;

  if (rotating_) {
    // Apply rotation effect.
    cur_mouse_pos_ = [self getClickPointForEvent:event];;
    renderer_->IncrementSpin((cur_mouse_pos_.x - last_mouse_pos_.x),
                             (cur_mouse_pos_.y - last_mouse_pos_.y));
    last_mouse_pos_ = cur_mouse_pos_;
    [self setNeedsDisplay:YES];
    return;
  }

  CefMouseEvent mouseEvent;
  [self getMouseEvent: mouseEvent forEvent: event];
  browser->GetHost()->SendMouseMoveEvent(mouseEvent, false);
}

- (void)mouseDragged:(NSEvent *)event {
  [self mouseMoved:event];
}

- (void)rightMouseDragged:(NSEvent *)event {
  [self mouseMoved:event];
}

- (void)otherMouseDragged:(NSEvent *)event {
  [self mouseMoved:event];
}

- (void)mouseEntered:(NSEvent *)event {
  [self mouseMoved:event];
}

- (void)mouseExited:(NSEvent *)event {
  CefRefPtr<CefBrowser> browser = [self getBrowser];
  if (!browser)
    return;

  CefMouseEvent mouseEvent;
  [self getMouseEvent: mouseEvent forEvent: event];
  browser->GetHost()->SendMouseMoveEvent(mouseEvent, true);
}

- (void)keyDown:(NSEvent *)event {
  CefRefPtr<CefBrowser> browser = [self getBrowser];
  if (!browser)
    return;

  CefKeyEvent keyEvent;
  [self getKeyEvent:keyEvent forEvent:event];

  keyEvent.type = KEYEVENT_KEYDOWN;
  browser->GetHost()->SendKeyEvent(keyEvent);

  if ([event modifierFlags] & (NSNumericPadKeyMask | NSFunctionKeyMask)) {
    // Don't send a Char event for non-char keys like arrows, function keys and
    // clear.
    switch (keyEvent.native_key_code) {
      case 81: // =
      case 75: // /
      case 67: // *
      case 78: // -
      case 69: // +
      case 76: // Enter
      case 65: // .
      case 82: // 0
      case 83: // 1
      case 84: // 2
      case 85: // 3
      case 86: // 4
      case 87: // 5
      case 88: // 6
      case 89: // 7
      case 91: // 8
      case 92: // 9
        break;
      default:
        return;
    }
  }

  keyEvent.type = KEYEVENT_CHAR;
  browser->GetHost()->SendKeyEvent(keyEvent);
}

- (void)keyUp:(NSEvent *)event {
  CefRefPtr<CefBrowser> browser = [self getBrowser];
  if (!browser)
    return;

  CefKeyEvent keyEvent;
  [self getKeyEvent:keyEvent forEvent:event];

  keyEvent.type = KEYEVENT_KEYUP;
  browser->GetHost()->SendKeyEvent(keyEvent);
}

- (void)flagsChanged:(NSEvent *)event {
  if ([self isKeyUpEvent:event])
    [self keyUp:event];
  else
    [self keyDown:event];
}

- (void)scrollWheel:(NSEvent *)event {
  CefRefPtr<CefBrowser> browser = [self getBrowser];
  if (!browser)
    return;

  CGEventRef cgEvent = [event CGEvent];
  ASSERT(cgEvent);

  int deltaX =
      CGEventGetIntegerValueField(cgEvent, kCGScrollWheelEventPointDeltaAxis2);
  int deltaY =
      CGEventGetIntegerValueField(cgEvent, kCGScrollWheelEventPointDeltaAxis1);

  CefMouseEvent mouseEvent;
  [self getMouseEvent: mouseEvent forEvent: event];
  browser->GetHost()->SendMouseWheelEvent(mouseEvent, deltaX, deltaY);
}

- (BOOL)canBecomeKeyView {
  CefRefPtr<CefBrowser> browser = [self getBrowser];
  return (browser != NULL);
}

- (BOOL)acceptsFirstResponder {
  CefRefPtr<CefBrowser> browser = [self getBrowser];
  return (browser != NULL);
}

- (BOOL)becomeFirstResponder {
  CefRefPtr<CefBrowser> browser = [self getBrowser];
  if (browser) {
    browser->GetHost()->SendFocusEvent(true);
    return [super becomeFirstResponder];
  }

  return NO;
}

- (BOOL)resignFirstResponder {
  CefRefPtr<CefBrowser> browser = [self getBrowser];
  if (browser) {
    browser->GetHost()->SendFocusEvent(false);
    return [super resignFirstResponder];
  }

  return NO;
}



- (void)undo:(id)sender {
  CefRefPtr<CefBrowser> browser = [self getBrowser];
  if (browser)
    browser->GetFocusedFrame()->Undo();
}

- (void)redo:(id)sender {
  CefRefPtr<CefBrowser> browser = [self getBrowser];
  if (browser)
    browser->GetFocusedFrame()->Redo();
}

- (void)cut:(id)sender {
  CefRefPtr<CefBrowser> browser = [self getBrowser];
  if (browser)
    browser->GetFocusedFrame()->Cut();
}

- (void)copy:(id)sender {
  CefRefPtr<CefBrowser> browser = [self getBrowser];
  if (browser)
    browser->GetFocusedFrame()->Copy();
}

- (void)paste:(id)sender {
  CefRefPtr<CefBrowser> browser = [self getBrowser];
  if (browser)
    browser->GetFocusedFrame()->Paste();
}

- (void)delete:(id)sender {
  CefRefPtr<CefBrowser> browser = [self getBrowser];
  if (browser)
    browser->GetFocusedFrame()->Delete();
}

- (void)selectAll:(id)sender {
  CefRefPtr<CefBrowser> browser = [self getBrowser];
  if (browser)
    browser->GetFocusedFrame()->SelectAll();
}

- (NSPoint)getClickPointForEvent:(NSEvent*)event {
  NSPoint windowLocal = [event locationInWindow];
  NSPoint contentLocal = [self convertPoint:windowLocal fromView:nil];

  NSPoint point;
  point.x = contentLocal.x;
  point.y = [self frame].size.height - contentLocal.y;  // Flip y.
  return point;
}

- (void)getKeyEvent:(CefKeyEvent &)keyEvent forEvent:(NSEvent *)event {
  if ([event type] == NSKeyDown || [event type] == NSKeyUp) {
    NSString* s = [event characters];
    if ([s length] > 0)
      keyEvent.character = [s characterAtIndex:0];

    s = [event charactersIgnoringModifiers];
    if ([s length] > 0)
      keyEvent.unmodified_character = [s characterAtIndex:0];
  }

  if ([event type] == NSFlagsChanged) {
    keyEvent.character = 0;
    keyEvent.unmodified_character = 0;
  }

  keyEvent.native_key_code = [event keyCode];

  keyEvent.modifiers = [self getModifiersForEvent:event];
}

- (void)getMouseEvent:(CefMouseEvent&)mouseEvent forEvent:(NSEvent*)event {
  NSPoint point = [self getClickPointForEvent:event];
  mouseEvent.x = point.x;
  mouseEvent.y = point.y;

  if ([self isOverPopupWidgetX:mouseEvent.x andY: mouseEvent.y]) {
    [self applyPopupOffsetToX:mouseEvent.x andY: mouseEvent.y];
  }

  mouseEvent.modifiers = [self getModifiersForEvent:event];
}

- (int)getModifiersForEvent:(NSEvent*)event {
  int modifiers = 0;

  if ([event modifierFlags] & NSControlKeyMask)
    modifiers |= EVENTFLAG_CONTROL_DOWN;
  if ([event modifierFlags] & NSShiftKeyMask)
    modifiers |= EVENTFLAG_SHIFT_DOWN;
  if ([event modifierFlags] & NSAlternateKeyMask)
    modifiers |= EVENTFLAG_ALT_DOWN;
  if ([event modifierFlags] & NSCommandKeyMask)
    modifiers |= EVENTFLAG_COMMAND_DOWN;
  if ([event modifierFlags] & NSAlphaShiftKeyMask)
    modifiers |= EVENTFLAG_CAPS_LOCK_ON;

  if ([event type] == NSKeyUp ||
      [event type] == NSKeyDown ||
      [event type] == NSFlagsChanged) {
    // Only perform this check for key events
    if ([self isKeyPadEvent:event])
      modifiers |= EVENTFLAG_IS_KEY_PAD;
  }

  // OS X does not have a modifier for NumLock, so I'm not entirely sure how to
  // set EVENTFLAG_NUM_LOCK_ON;
  //
  // There is no EVENTFLAG for the function key either.

  // Mouse buttons
  switch ([event type]) {
    case NSLeftMouseDragged:
    case NSLeftMouseDown:
    case NSLeftMouseUp:
      modifiers |= EVENTFLAG_LEFT_MOUSE_BUTTON;
    break;
    case NSRightMouseDragged:
    case NSRightMouseDown:
    case NSRightMouseUp:
      modifiers |= EVENTFLAG_RIGHT_MOUSE_BUTTON;
    break;
    case NSOtherMouseDragged:
    case NSOtherMouseDown:
    case NSOtherMouseUp:
      modifiers |= EVENTFLAG_MIDDLE_MOUSE_BUTTON;
    break;
  }

  return modifiers;
}

- (BOOL)isKeyUpEvent:(NSEvent*)event {
  if ([event type] != NSFlagsChanged)
    return [event type] == NSKeyUp;

  // FIXME: This logic fails if the user presses both Shift keys at once, for
  // example: we treat releasing one of them as keyDown.
  switch ([event keyCode]) {
    case 54: // Right Command
    case 55: // Left Command
      return ([event modifierFlags] & NSCommandKeyMask) == 0;

    case 57: // Capslock
      return ([event modifierFlags] & NSAlphaShiftKeyMask) == 0;

    case 56: // Left Shift
    case 60: // Right Shift
      return ([event modifierFlags] & NSShiftKeyMask) == 0;

    case 58: // Left Alt
    case 61: // Right Alt
     return ([event modifierFlags] & NSAlternateKeyMask) == 0;

    case 59: // Left Ctrl
    case 62: // Right Ctrl
      return ([event modifierFlags] & NSControlKeyMask) == 0;

    case 63: // Function
      return ([event modifierFlags] & NSFunctionKeyMask) == 0;
  }
  return false;
}

- (BOOL)isKeyPadEvent:(NSEvent*)event {
  if ([event modifierFlags] & NSNumericPadKeyMask)
    return true;

  switch ([event keyCode]) {
    case 71: // Clear
    case 81: // =
    case 75: // /
    case 67: // *
    case 78: // -
    case 69: // +
    case 76: // Enter
    case 65: // .
    case 82: // 0
    case 83: // 1
    case 84: // 2
    case 85: // 3
    case 86: // 4
    case 87: // 5
    case 88: // 6
    case 89: // 7
    case 91: // 8
    case 92: // 9
      return true;
  }

  return false;
}

- (void)windowDidChangeBackingProperties:(NSNotification*)notification {
#if defined(__MAC_OS_X_VERSION_MAX_ALLOWED) && \
    __MAC_OS_X_VERSION_MAX_ALLOWED >= 1070
  // This delegate method is only called on 10.7 and later, so don't worry about
  // other backing changes calling it on 10.6 or earlier
  CGFloat newBackingScaleFactor = [self getDeviceScaleFactor];
  NSNumber* oldBackingScaleFactor =
      [[notification userInfo] objectForKey:NSBackingPropertyOldScaleFactorKey];
  if (newBackingScaleFactor != [oldBackingScaleFactor doubleValue]) {
    CefRefPtr<CefBrowser> browser = [self getBrowser];
    if (!browser)
      return;

    browser->GetHost()->NotifyScreenInfoChanged();
  }
#endif
}

- (void)drawRect: (NSRect) dirtyRect {
  // The Invalidate below fixes flicker when resizing
  if ([self inLiveResize]) {
    CefRefPtr<CefBrowser> browser = [self getBrowser];
    if (!browser)
      return;

    NSRect b = [self bounds];
    CefRect boundsRect = CefRect((int)b.origin.x,
                                 (int)b.origin.y,
                                 (int)b.size.width,
                                 (int)b.size.height);
    browser->GetHost()->Invalidate(boundsRect, PET_VIEW);
  }
}

// Utility - private
- (float)getDeviceScaleFactor {
  float deviceScaleFactor = 1;
  NSWindow* window = [self window];
  if (!window)
    return deviceScaleFactor;

#if defined(__MAC_OS_X_VERSION_MAX_ALLOWED) && \
    __MAC_OS_X_VERSION_MAX_ALLOWED >= 1070
  if ([window respondsToSelector:@selector(backingScaleFactor)])
    deviceScaleFactor = [window backingScaleFactor];
  else
#endif
    deviceScaleFactor = [window userSpaceScaleFactor];

  return deviceScaleFactor;
}

- (bool) isOverPopupWidgetX: (int) x andY: (int) y {
  CefRect rc = [self convertRectFromBackingInternal:renderer_->popup_rect()];
  int popup_right = rc.x + rc.width;
  int popup_bottom = rc.y + rc.height;
  return (x >= rc.x) && (x < popup_right) &&
         (y >= rc.y) && (y < popup_bottom);
}

- (int) getPopupXOffset {
  int original_x =
      [self convertRectFromBackingInternal:renderer_->original_popup_rect()].x;
  int popup_x =
      [self convertRectFromBackingInternal:renderer_->popup_rect()].x;

  return original_x - popup_x;
}

- (int) getPopupYOffset {
  int original_y =
      [self convertRectFromBackingInternal:renderer_->original_popup_rect()].y;
  int popup_y =
      [self convertRectFromBackingInternal:renderer_->popup_rect()].y;

  return original_y - popup_y;
}

- (void) applyPopupOffsetToX: (int&) x andY: (int&) y {
  if ([self isOverPopupWidgetX:x andY:y]) {
    x += [self getPopupXOffset];
    y += [self getPopupYOffset];
  }
}

- (void) convertRects: (const CefRenderHandler::RectList&) rects
       toBackingRects: (CefRenderHandler::RectList*) scaled_rects
              andSize: (const NSSize) size
        toBackingSize: (NSSize*) scaled_size {
  *scaled_rects = rects;
  *scaled_size = size;

#if defined(__MAC_OS_X_VERSION_MAX_ALLOWED) && \
    __MAC_OS_X_VERSION_MAX_ALLOWED >= 1070
  if ([self getDeviceScaleFactor] != 1 &&
      [self respondsToSelector:@selector(convertSizeToBacking:)] &&
      [self respondsToSelector:@selector(convertRectToBacking:)]) {
    CefRenderHandler::RectList scaled_dirty_rects;
    for (size_t i = 0; i < rects.size(); ++i) {
      scaled_dirty_rects.push_back([self convertRectToBackingInternal:rects[i]]);
    }

    *scaled_rects = scaled_dirty_rects;
    *scaled_size = [self convertSizeToBacking:size];
  }
#endif
}

- (CefRect) convertRectToBackingInternal: (const CefRect&) rect {
  if ([self getDeviceScaleFactor] == 1)
    return rect;

#if defined(__MAC_OS_X_VERSION_MAX_ALLOWED) && \
    __MAC_OS_X_VERSION_MAX_ALLOWED >= 1070
  if ([self respondsToSelector:@selector(convertRectToBacking:)]) {
    NSRect old_rect = NSMakeRect(rect.x, rect.y, rect.width, rect.height);
    NSRect scaled_rect = [self convertRectToBacking:old_rect];
    return CefRect((int)scaled_rect.origin.x,
                   (int)scaled_rect.origin.y,
                   (int)scaled_rect.size.width,
                   (int)scaled_rect.size.height);
  }
#endif

  return rect;
}

- (CefRect) convertRectFromBackingInternal: (const CefRect&) rect {
#if defined(__MAC_OS_X_VERSION_MAX_ALLOWED) && \
    __MAC_OS_X_VERSION_MAX_ALLOWED >= 1070
  if ([self respondsToSelector:@selector(convertRectFromBacking:)]) {
    NSRect old_rect = NSMakeRect(rect.x, rect.y, rect.width, rect.height);
    NSRect scaled_rect = [self convertRectFromBacking:old_rect];
    return CefRect((int)scaled_rect.origin.x,
                   (int)scaled_rect.origin.y,
                   (int)scaled_rect.size.width,
                   (int)scaled_rect.size.height);
  }
#endif

  return rect;
}

@end


CefRefPtr<OSRWindow> OSRWindow::Create(OSRBrowserProvider* browser_provider,
                                       bool transparent,
                                       CefWindowHandle parentView,
                                       const CefRect& frame) {
  return new OSRWindow(browser_provider, transparent, parentView, frame);
}

OSRWindow::OSRWindow(OSRBrowserProvider* browser_provider,
                     bool transparent,
                     CefWindowHandle parentView,
                     const CefRect& frame) {
  NSRect window_rect = NSMakeRect(frame.x, frame.y, frame.width, frame.height);
  ClientOpenGLView* view = [[ClientOpenGLView alloc] initWithFrame:window_rect
                                                   andTransparency:transparent];
  this->view_ = view;
  [parentView addSubview:view];
  [view setAutoresizingMask:(NSViewWidthSizable | NSViewHeightSizable)];
  [view setAutoresizesSubviews: true];

  this->render_client = new ClientOSRHandler(view, browser_provider);
}

OSRWindow::~OSRWindow() {
}
