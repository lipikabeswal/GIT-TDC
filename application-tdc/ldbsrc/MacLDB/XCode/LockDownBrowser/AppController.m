
#import "AppController.h"

@implementation AppController

- (void)dealloc
{
	//NSLog(@"dealloc AppController.......");
    [super dealloc];
}

- (void)applicationDidFinishLaunching:(NSNotification *)aNotification {

	//NSLog(@"applicationDidFinishLaunching.......");
	[self clearClipBoard];
	[self hideAll];
	[self disableScreenCapture];
	[self killFrontRowProcess];
}

- (NSApplicationTerminateReply) applicationShouldTerminate: (NSApplication*) sender { 
	
	NSLog(@"applicationShouldTerminate....");
	[self clearClipBoard];
	[self enableScreenCapture];
	[self showAll];
	return YES; 
} 

- (NSApplicationTerminateReply) applicationWillTerminate: (NSApplication*) sender { 
	
	NSLog(@"applicationWillTerminate....");

	return YES; 
} 

- (NSArray *)webView:(WebView *)sender contextMenuItemsForElement:(NSDictionary *)element 
		defaultMenuItems:(NSArray *)defaultMenuItems {
		
	NSLog(@"contextMenuItemsForElement....");
	return NO;
} 

- (void) disableScreenCapture {
	//NSLog(@"disableScreenCapture...");
	system("mkdir ~/Desktop/ScrCapture;"
			"defaults write com.apple.screencapture disable-shadow -bool true;"
			"defaults write com.apple.screencapture location ~/Desktop/ScrCapture;"
			"defaults write com.apple.screencapture name picture;"
			"killall SystemUIServer");
}

- (void) enableScreenCapture {
	//NSLog(@"enableScreenCapture...");
	system("rm -rf ~/Desktop/ScrCapture;"
		"defaults write com.apple.screencapture disable-shadow -bool false;"
		"defaults write com.apple.screencapture location ~/Desktop;"
		"defaults write com.apple.screencapture name picture;"
		"killall SystemUIServer");
}

- (void) clearClipBoard {
	NSLog(@"clearClipBoard...");
	system("echo '' | pbcopy");
}

- (void) hideAll {
	//NSLog(@"hideAll....");
	NSString *applescript = @"tell application \"System Events\" \n" 
								//"say \"hide all called\" \n"
								"set visible of (every process whose name is not \"LockDownBrowser\" and visible is true and frontmost is false) to false \n" 
								"set visible of process \"Finder\" to false \n" 
							"end tell";
	NSString *result = [NSString stringWithFormat:@" %@ ",applescript]; 
	NSAppleScript *run = [[NSAppleScript alloc] initWithSource:result];
	[run executeAndReturnError:nil];
}

- (void) showAll {
	//NSLog(@"showAll....");	
	NSString *applescript = @"tell application \"System Events\" \n"
								//"say \"show all called\" \n"
								"set visible of (every process whose background only is false) to true \n" 
							"end tell";	
	NSString *result = [NSString stringWithFormat:@" %@ ",applescript]; 
	NSAppleScript *run = [[NSAppleScript alloc] initWithSource:result];
	[run executeAndReturnError:nil];
}

- (void) killFrontRow {
	//NSLog(@"killFrontRow....");	
	NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init];
	system("rm -rf ~/Desktop/ScrCapture/*;"
		   "killall -9 \"Front Row\"");
	[NSThread sleepUntilDate:[NSDate dateWithTimeIntervalSinceNow:1.0]];
	[self killFrontRowProcess];
	[pool release];
}

- (void) killFrontRowProcess {
	//NSLog(@"killFrontRowProcess....");	
	[NSThread detachNewThreadSelector:@selector(killFrontRow) toTarget:self withObject:nil];
}

@end
