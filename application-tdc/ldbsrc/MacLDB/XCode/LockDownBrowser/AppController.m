
#import "AppController.h"

@implementation AppController

- (void)dealloc
{
    [super dealloc];
}

- (void)applicationDidFinishLaunching:(NSNotification *)aNotification {

	//NSLog(@"applicationDidFinishLaunching.......");
	[self hideAll];
	[self clearClipBoard];
}

- (NSApplicationTerminateReply) applicationShouldTerminate: (NSApplication*) sender { 
	
	//NSLog(@"applicationShouldTerminate....");
	[self clearClipBoard];
	[self showAll];
	return YES; 
} 

- (NSArray *)webView:(WebView *)sender contextMenuItemsForElement:(NSDictionary *)element 
		defaultMenuItems:(NSArray *)defaultMenuItems {
		
	//NSLog(@"contextMenuItemsForElement....");
	return NO;
} 

- (void) clearClipBoard {
	//NSLog(@"clearClipBoard...");
	system("echo '' | pbcopy");
}

- (void) hideAll {
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
	NSString *applescript = @"tell application \"System Events\" \n"
								//"say \"show all called\" \n"
								"set visible of (every process whose background only is false) to true \n" 
							"end tell";	
	NSString *result = [NSString stringWithFormat:@" %@ ",applescript]; 
	NSAppleScript *run = [[NSAppleScript alloc] initWithSource:result];
	[run executeAndReturnError:nil];
}

@end
