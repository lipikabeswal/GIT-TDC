
#import "FullscreenWindow.h"


@implementation FullscreenWindow

- (BOOL)canBecomeKeyWindow {
	return YES;
}

- (BOOL)canBecomeMainWindow {
	return YES;
}

@end
