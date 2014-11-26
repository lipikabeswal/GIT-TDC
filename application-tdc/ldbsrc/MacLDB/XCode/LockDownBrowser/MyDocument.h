
#import <Cocoa/Cocoa.h>
#import <Carbon/Carbon.h>


@class WebView;

@interface MyDocument : NSDocument
{
    IBOutlet WebView *webView;
    IBOutlet NSWindow *window;
	IBOutlet NSWindow *fullscreenWindow;
    NSThread *screenshotThread; //NSThread to check for screenshots
	
}
@property (assign) IBOutlet NSWindow	*window;

- (NSString *)getURLfromFile:(NSString *)filePath; 
- (void)disableEject;
- (OSStatus)disableHotKeys;
- (void)setFullScreenWindow;
- (void) checkForScreenShotsAfter: (NSDate *) startTime; //NSThread to check for screenshots
//- (NSColor*)colorWithHexColorString:(NSString*)inColorString;
@end
