// Copyright (c) 2013 The Chromium Embedded Framework Authors.
// Portions copyright (c) 2010 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#import <Cocoa/Cocoa.h>
#import "Carbon/Carbon.h"
#import "cefclient/FullscreenWindow.h"
#include <sstream>
#include "cefclient/cefclient.h"
#include "include/cef_app.h"
#import "include/cef_application_mac.h"
#include "include/cef_browser.h"
#include "include/cef_frame.h"
#include "include/cef_runnable.h"
#include "cefclient/cefclient_osr_widget_mac.h"
#include "cefclient/client_handler.h"
#include "cefclient/client_switches.h"
#include "cefclient/resource_util.h"
#include "cefclient/scheme_test.h"
#include "cefclient/string_util.h"

#include <pwd.h>

// The global ClientHandler reference.
extern CefRefPtr<ClientHandler> g_handler;

class MainBrowserProvider : public OSRBrowserProvider {
  virtual CefRefPtr<CefBrowser> GetBrowser() {
    if (g_handler.get())
      return g_handler->GetBrowser();

    return NULL;
  }
} g_main_browser_provider;

char szWorkingDir[512];   // The current working directory

// Sizes for URL bar layout
//#define BUTTON_HEIGHT 22
//#define BUTTON_WIDTH 72
//#define BUTTON_MARGIN 8
//#define URLBAR_HEIGHT  32

// Content area size for newly created windows.
//const int kWindowWidth = 800;
//const int kWindowHeight = 600;

// Provide the CefAppProtocol implementation required by CEF.
@interface ClientApplication : NSApplication<CefAppProtocol> {
@private
  BOOL handlingSendEvent_;
    
}
@end


int cmdIsDown = 0;
int ctrlIsDown = 0;
int shiftIsDown = 0;
int optIsDown = 0;
int fnIsDown = 0;

CFMachPortRef eventTap = nil;//required globally

static CGEventRef KeyboardCallback(CGEventTapProxy proxy, CGEventType event_type, CGEventRef event, void *info)
{
    
	CGKeyCode keyCode = (CGKeyCode)CGEventGetIntegerValueField(event, kCGKeyboardEventKeycode);
	
	switch (event_type) {
            
		case kCGEventFlagsChanged:
			//NSLog(@"modifier key pressed....");
            
			switch (keyCode) {
                    
				case 55://command key
					cmdIsDown = (cmdIsDown == 1) ? 0 : 1;
                    break;
                    
				case 56://shift key
					shiftIsDown = (shiftIsDown == 1) ? 0 : 1;
                    break;
                    
				case 58://option key
					optIsDown = (optIsDown == 1) ? 0 : 1;
                    return NULL;
                    break;
                    
				case 59://control key
					ctrlIsDown = (ctrlIsDown == 1) ? 0 : 1;
                    break;
                    
				case 63://function key
					fnIsDown = (fnIsDown == 1) ? 0 : 1;
                    break;
                    
				default:
                    break;
			}
            
            break;
            
		case kCGEventKeyDown:
            
			switch (keyCode) {
                    
				case 96://F5
					//block voice over
					if(cmdIsDown) {
						return NULL;
					}
                    break;
                    
				case 53://Esc
					//block front row
					if(cmdIsDown) {
						return NULL;
					}
                    break;
                    
				case 20://3
					//block screen capture
					if(cmdIsDown && shiftIsDown) {
						return NULL;
					}
                    break;
                    
				case 21://4
					//block selection capture
					if(cmdIsDown && shiftIsDown) {
						return NULL;
					}
                    break;
                    
                case 12://Q
                    if (ctrlIsDown || optIsDown || cmdIsDown) {
                        return NULL;
                    }
                    
				default://if option key is down, ignore any normal key to avoid option+key combination, we will have to handle above cases also.
                    if(optIsDown) {
                        return NULL;
                    }
                    break;
			}
            
            break;
            
		case kCGEventTapDisabledByTimeout://event tab disabled
			NSLog(@"kCGEventTapDisabledByTimeout fired");
			CGEventTapEnable(eventTap, true);
            break;
            
		default:
            break;
	}
    
    return event;
}

	

@implementation ClientApplication
- (BOOL)isHandlingSendEvent {
  return handlingSendEvent_;
}

- (void)setHandlingSendEvent:(BOOL)handlingSendEvent {
  handlingSendEvent_ = handlingSendEvent;
}

- (void)sendEvent:(NSEvent*)event {
  CefScopedSendingEvent sendingEventScoper;
  [super sendEvent:event];
}
@end


// Receives notifications from controls and the browser window. Will delete
// itself when done.
@interface ClientWindowDelegate : NSObject <NSWindowDelegate>

//Add UI elements (IBActions) here if required
@end

@implementation ClientWindowDelegate





//window gained focus
- (void)windowDidBecomeKey:(NSNotification*)notification {
  if (g_handler.get() && g_handler->GetBrowserId()) {
    // Give focus to the browser window.
    g_handler->GetBrowser()->GetHost()->SetFocus(true);
  }
}

// Called when we are deactivated (when we lose focus).
- (void)windowDidResignKey:(NSNotification*)notification {
    if (g_handler.get() && g_handler->GetBrowserId()) {
        // Give focus to the browser window.
        g_handler->GetBrowser()->GetHost()->SetFocus(false);
    }
}

// Called when the window is about to close. Perform the self-destruction
// sequence by getting rid of the window. By returning YES, we allow the window
// to be removed from the screen.
- (BOOL)windowShouldClose:(id)window {
  if (g_handler.get() && !g_handler->IsClosing()) {
    CefRefPtr<CefBrowser> browser = g_handler->GetBrowser();
    if (browser.get()) {
      // Notify the browser window that we would like to close it. This
      // will result in a call to ClientHandler::DoClose() if the
      // JavaScript 'onbeforeunload' event handler allows it.
      browser->GetHost()->CloseBrowser(false);
    
      // Cancel the close.
      return NO;
    }
  }

  // Try to make the window go away.
  [window autorelease];

  // Clean ourselves up after clearing the stack of anything that might have the
  // window on it.
    [self performSelectorOnMainThread:@selector(cleanup:)
                           withObject:window
                        waitUntilDone:NO];
    

    
  // Allow the close.
  return YES;
}

// Deletes itself.
- (void)cleanup:(id)window {
    
    
   
  [self release];
}

@end


// Receives notifications from the application. Will delete itself when done.
@interface ClientAppDelegate : NSObject


- (void)createApp:(id)object;
- (void) clearScreenshots ;
- (void) clearClipBoard ;
- (void) checkForScreenShotsAfter: (NSDate *) startTime;



@end

@implementation ClientAppDelegate
void *oldHotKeyMode;


NSDate *startTime_;

NSThread *screenshotThread; //NSThread to check for screenshots

//Method to check for screenshots
- (void) checkForScreenShotsAfter: (NSDate *) startTime
{
    while(true)
    {
        CFStringRef loc = (CFStringRef) CFPreferencesCopyAppValue( CFSTR("location"), CFSTR("com.apple.screencapture") ); //get screenshot location
        NSString *location = (NSString *) loc;
        
        NSFileManager *fileManager = [NSFileManager defaultManager];
        
        NSArray *contents=[fileManager contentsOfDirectoryAtPath:location error:nil];
        
        
        for (NSString *entity in contents) {
            if([[entity pathExtension] isEqualToString:@"png"])
            {
                entity = [NSString stringWithFormat:@"%@/%@",location,entity];
                NSError *attributeserror = nil;
                NSDictionary * fileAttribs =[fileManager attributesOfItemAtPath:entity error:&attributeserror];
                NSDate *createDate = [fileAttribs objectForKey:NSFileCreationDate];
                
                if ([createDate compare:startTime]==NSOrderedDescending) {
                    //Screenshot detected!
                    [fileManager removeItemAtPath:entity error:nil]; //delete screenshot
                }
                
            }
        }
        [NSThread sleepForTimeInterval:1.0f];
    }
}






- (void) clearClipBoard {
	system("echo '' | pbcopy");
}

-(OSStatus)disableHotKeys {
	OSStatus err;
	SystemUIMode mode = kUIModeAllHidden;
	SystemUIOptions options = kUIOptionDisableAppleMenu |
	kUIOptionDisableProcessSwitch |
	kUIOptionDisableForceQuit |
	kUIOptionDisableSessionTerminate |
	kUIOptionDisableHide;
	err = SetSystemUIMode(mode,options);
	return err;
}

// Create the application on the UI thread.
- (void)createApp:(id)object {
  [NSApplication sharedApplication];
  //[NSBundle loadNibNamed:@"MainMenu" owner:NSApp];

    //Old lockdown code : does not work on OSx 10.9 and above
   CGEventMask eventMask = 0;
   //CFMachPortRef eventTap = nil;
   CFRunLoopSourceRef runLoopSource = nil;
   CFRunLoopRef runLoop = nil;
 
   
   eventMask = CGEventMaskBit(kCGEventKeyDown)|CGEventMaskBit(kCGEventKeyUp)| CGEventMaskBit(kCGEventFlagsChanged);
   
   //eventMask |=CGEventMaskBit(kCGEventTapDisabledByTimeout);
	
   /* eventTap = CGEventTapCreate(kCGSessionEventTap,
    kCGHeadInsertEventTap,
    kCGEventTapOptionDefault,
    eventMask,
    MouseCallback,
    self); */
   
	eventTap = CGEventTapCreate(kCGSessionEventTap,
								kCGHeadInsertEventTap,
								0x00000000, //value of kCGEventTapOptionDefault taken from constants header file
								eventMask,
								KeyboardCallback,
								self);
   
   if (!eventTap) {
       
       fprintf(stderr, "Failed to create event tap\n");
       exit(1);
   }
   
   // Create a run loop source.
   runLoopSource = CFMachPortCreateRunLoopSource(NULL, eventTap, 0);
	if(runLoopSource == NULL) {
		
		//NSLog(@"no event in run loop");
	}
	
   CFRelease(eventTap);
	
	runLoop = CFRunLoopGetCurrent();
	if(runLoop == NULL) {
		
		//NSLog(@"no run loop");
	}
	
   // Add to the current run loop.
   CFRunLoopAddSource([[NSRunLoop currentRunLoop] getCFRunLoop], runLoopSource, kCFRunLoopCommonModes);
    
  // Enable the event tap.
  CGEventTapEnable(eventTap, true);
  CFRelease(runLoopSource);
  
    
  // Set the delegate for application events.
  [NSApp setDelegate:self];

  // Create the delegate for control and browser window events.
  ClientWindowDelegate* delegate = [[ClientWindowDelegate alloc] init];

  // Create the main application window.
  NSRect screen_rect = [[NSScreen mainScreen] frame];

  NSWindow* mainWnd = [[UnderlayOpenGLHostingWindow alloc]
                        initWithContentRect:screen_rect
                       styleMask:NSBorderlessWindowMask
                       backing:NSBackingStoreBuffered
                       defer:YES screen:[NSScreen mainScreen]];
    
    
  [mainWnd setTitle:@"cefclient"];
  [mainWnd setDelegate:delegate];
    

  // Rely on the window delegate to clean us up rather than immediately
  // releasing when the window gets closed. We use the delegate to do
  // everything from the autorelease pool so the window isn't on the stack
  // during cleanup (ie, a window close from javascript).
  [mainWnd setReleasedWhenClosed:NO];

  NSView* contentView = [mainWnd contentView];

  startTime_ = [[NSDate alloc] init];

  // Create the handler.
  g_handler = new ClientHandler();
  g_handler->SetMainHwnd(contentView);
  //g_handler->SetEditHwnd(editWnd);

  // Create the browser view.
  CefWindowInfo window_info;
  CefBrowserSettings settings;

  if (AppIsOffScreenRenderingEnabled()) {
    CefRefPtr<CefCommandLine> cmd_line = AppGetCommandLine();
    bool transparent =
        cmd_line->HasSwitch(cefclient::kTransparentPaintingEnabled);

    CefRefPtr<OSRWindow> osr_window =
        OSRWindow::Create(&g_main_browser_provider, transparent, contentView,
            CefRect(0, 0, screen_rect.size.width, screen_rect.size.height));
    window_info.SetAsOffScreen(osr_window->GetWindowHandle());
    window_info.SetTransparentPainting(transparent);
    g_handler->SetOSRHandler(osr_window->GetRenderHandler().get());
  } else {
    // Initialize window info to the defaults for a child window.
    window_info.SetAsChild(contentView, 0, 0, screen_rect.size.width, screen_rect.size.height);
  }

  CefBrowserHost::CreateBrowser(window_info, g_handler.get(),
                                g_handler->GetStartupURL(), settings);
    //int windowLevel;
	
//    if ([[mainWnd screen] isEqual:[[NSScreen screens] objectAtIndex:0]]) {
//		[NSMenu setMenuBarVisible:NO];
//	}
    
	if(CGCaptureAllDisplays() != kCGErrorSuccess) {
		//NSLog(@"could not capture main display.....");
	}
	
	/*windowLevel = CGShieldingWindowLevel();
    
	NSWindow *fullscreenWindow = [[FullscreenWindow alloc]
                        initWithContentRect:[mainWnd contentRectForFrameRect:[mainWnd frame]]
                        styleMask:NSBorderlessWindowMask
                        backing:NSBackingStoreBuffered
                        defer:YES screen:[NSScreen mainScreen]];
    //[fullscreenWindow setDelegate:delegate];
	[fullscreenWindow setLevel:windowLevel];
	[fullscreenWindow setContentView:[mainWnd contentView]];
	[fullscreenWindow setFrame:
    [fullscreenWindow frameRectForContentRect:[[mainWnd screen] frame]] display:YES animate:NO];
	[fullscreenWindow makeKeyAndOrderFront:nil];
    //[mainWnd orderOut:nil];
    [fullscreenWindow deminiaturize:nil];
    //[fullscreenWindow setOpaque:NO];
    //[fullscreenWindow setSharingType:NSWindowSharingNone]; //prevents other applications from taking screenshots
    */
    
    
    
    
    
    [mainWnd setFrame:[mainWnd frameRectForContentRect:screen_rect] display:YES];
    [mainWnd makeKeyAndOrderFront:nil];
    
    [mainWnd deminiaturize:nil];
    //[mainWnd setLevel:CGShieldingWindowLevel()];
    //[mainWnd setCollectionBehavior:NSWindowCollectionBehaviorFullScreenPrimary];
    
    [contentView enterFullScreenMode:[NSScreen mainScreen] withOptions:nil];
    
    
	oldHotKeyMode = PushSymbolicHotKeyMode(kHIHotKeyModeAllDisabled);
	[self disableHotKeys];
    [self clearClipBoard];
    
    
    
    
    

}

- (void) clearScreenshots {
    struct passwd *pw = getpwuid(getuid());
    
    NSString *desktopDirPath = [NSString  stringWithFormat:@"%@/%@", [NSString stringWithUTF8String:pw->pw_dir],@"DeskTop"];
    
    NSArray* dirs = [[NSFileManager defaultManager] contentsOfDirectoryAtPath:desktopDirPath
                                                                        error:NULL];
    NSMutableArray *jpgFiles = [[NSMutableArray alloc] init];
    [dirs enumerateObjectsUsingBlock:^(id obj, NSUInteger idx, BOOL *stop) {
        NSString *filename = (NSString *)obj;
        NSString *extension = [[filename pathExtension] lowercaseString];
        if ([extension isEqualToString:@"png"]) {
            NSString *filePath = [desktopDirPath stringByAppendingPathComponent:filename];
            NSDictionary* attrs = [[NSFileManager defaultManager] attributesOfItemAtPath:filePath error:nil];
            NSDate *fileCreationdate = (NSDate*)[attrs objectForKey: NSFileCreationDate];
            if ([fileCreationdate compare:startTime_] != NSOrderedAscending ) {
                [jpgFiles addObject:filePath];
            }
        }
    }];
    
    for (NSString* path in jpgFiles) {
        NSError *error;
        if ([[NSFileManager defaultManager] isDeletableFileAtPath:path]) {
            [[NSFileManager defaultManager] removeItemAtPath:path error:&error];
        }
    }
    
}


// Called when the application's Quit menu item is selected.
- (NSApplicationTerminateReply)applicationShouldTerminate:
      (NSApplication *)sender {
    [self clearScreenshots];
  // Request that all browser windows close.
  if (g_handler.get())
    g_handler->CloseAllBrowsers(false);
    PopSymbolicHotKeyMode(oldHotKeyMode);

    //
  // Cancel the termination. The application will exit after all windows have
  // closed.
  return NSTerminateCancel;
}

// Sent immediately before the application terminates. This signal should not
// be called because we cancel the termination.
- (void)applicationWillTerminate:(NSNotification *)aNotification {
    ASSERT(false);  // Not reached.
}

@end


int main(int argc, char* argv[]) {
  CefMainArgs main_args(argc, argv);
  CefRefPtr<ClientApp> app(new ClientApp);

  // Execute the secondary process, if any.
  int exit_code = CefExecuteProcess(main_args, app.get());
  if (exit_code >= 0)
    return exit_code;

  // Retrieve the current working directory.
  getcwd(szWorkingDir, sizeof(szWorkingDir));

  // Initialize the AutoRelease pool.
  NSAutoreleasePool* autopool = [[NSAutoreleasePool alloc] init];

  // Initialize the ClientApplication instance.
  [ClientApplication sharedApplication];

  // Parse command line arguments.
  AppInitCommandLine(argc, argv);

  CefSettings settings;

  // Populate the settings based on command line arguments.
  AppGetSettings(settings);

  // Initialize CEF.
  CefInitialize(main_args, settings, app.get());

  
  // Create the application delegate and window.
  NSObject* delegate = [[ClientAppDelegate alloc] init];
    [delegate performSelectorOnMainThread:@selector(createApp:) withObject:delegate
                            waitUntilDone:NO];
   


   //Dispatch thread with method checkForScreenShotsAfter and NSDate object with current time to check for screenshots taken after this point
   screenshotThread = [[NSThread alloc] initWithTarget:delegate selector:@selector(checkForScreenShotsAfter:) object:[NSDate date]];
   [screenshotThread start];

  // Run the application message loop.
  CefRunMessageLoop();

//  [delegate performSelector:@selector(checkForScreenShotsAfter:) withObject:startTime_];
    
  //  [delegate performSelectorOnMainThread:@selector(clearScreenshots:) withObject:nil waitUntilDone:NO];
  // Shut down CEF.
  CefShutdown();

  // Release the handler.
  g_handler = NULL;

  // Release the delegate.
  [delegate release];

  // Release the AutoRelease pool.
  [autopool release];

  return 0;
}


std::string AppGetWorkingDirectory() {
  return szWorkingDir;
}

void AppQuitMessageLoop() {
  CefQuitMessageLoop();
}
