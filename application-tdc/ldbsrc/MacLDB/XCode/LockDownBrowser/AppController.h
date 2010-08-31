
#import <Cocoa/Cocoa.h>
#import <WebKit/WebKit.h>

@interface AppController : NSObject
{
	IBOutlet NSWindow* mWindow; 
}
- (void)dealloc;
- (void)applicationDidFinishLaunching:(NSNotification *)aNotification;
- (void)disableScreenCapture;
- (void)enableScreenCapture; 
- (void)clearClipBoard;
- (void)hideAll;
- (void)showAll;
- (void)killFrontRow;
- (void)killFrontRowProcess;
@end
