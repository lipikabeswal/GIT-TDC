
#import <Cocoa/Cocoa.h>
#import <Carbon/Carbon.h>


@class WebView;

@interface MyDocument : NSDocument
{
    IBOutlet WebView *webView;
    IBOutlet NSWindow *window;
	IBOutlet NSWindow *fullscreenWindow;
	
}
@property (assign) IBOutlet NSWindow	*window;

- (NSString *)getURLfromFile:(NSString *)filePath; 
- (void)disableEject;
- (OSStatus)disableHotKeys;
- (void)setFullScreenWindow;
//- (NSColor*)colorWithHexColorString:(NSString*)inColorString;
@end
