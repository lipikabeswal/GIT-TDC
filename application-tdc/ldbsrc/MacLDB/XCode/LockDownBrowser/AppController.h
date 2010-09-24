
#import <Cocoa/Cocoa.h>
#import <Carbon/Carbon.h>
#import <WebKit/WebKit.h>

@interface AppController : NSObject
{

}

- (void)dealloc;
- (void)applicationDidFinishLaunching:(NSNotification *)aNotification;
- (void)clearClipBoard;
- (void)hideAll;
- (void)showAll;
@end
