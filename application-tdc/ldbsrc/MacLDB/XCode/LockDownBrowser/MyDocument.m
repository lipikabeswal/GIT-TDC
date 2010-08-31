
#import "MyDocument.h"
#import <WebKit/WebKit.h>
#import "FullscreenWindow.h"

@implementation MyDocument

@synthesize window;

-(void)dealloc {
	//NSLog(@"dealloc called...");
    [webView close];
    [window release];
    [super dealloc];
}

-(id)webView {
    return webView;
}

-(NSString *)windowNibName {
    return @"MyDocument";
}

-(void)loadURL:(NSURL *)URL {
    [[webView mainFrame] loadRequest:[NSURLRequest requestWithURL:URL]];
}

-(void)windowControllerDidLoadNib:(NSWindowController *) aController {
    [super windowControllerDidLoadNib:aController];

	NSAutoreleasePool *pool;
	pool = [NSAutoreleasePool new]; 
    
	NSString *weburl;
	//NSString *filepath = @"/Applications/LockDownBrowser.app/Contents/Resources/myfile"; //url of the properties file 

	//weburl = [self getURLfromFile:filepath];
	//weburl = @"http://google.com";
	weburl = @"http://127.0.0.1:12345/login.html";
		
    // Load the default URL
	NSURL *URL = [NSURL URLWithString:weburl];
    [self loadURL:URL];
	
	[self setFullScreenWindow];
	[self disableHotKeys];	
	[self disableEject];

	[pool drain];		
}

-(void)close {
    [webView close];
    [super close];
}

OSErr QuitAppleEventHandler (const AppleEvent *appleEvt, AppleEvent* reply, UInt32 refcon) {
	return userCanceledErr;
}

-(void) disableEject {

	//NSLog(@"disableEject........");
	OSErr oserr;
	oserr = AEInstallEventHandler (kCoreEventClass,kAEQuitApplication,NewAEEventHandlerUPP((AEEventHandlerProcPtr)QuitAppleEventHandler),0,false);

	if (oserr != noErr) {
		ExitToShell();
	}
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

//Reads the file line by line and returns one line at a time
-(NSString *)getURLfromFile:(NSString *)filePath 
{
	NSString *aFilePath = filePath;
	NSString *fileString = [NSString stringWithContentsOfFile:aFilePath];
	NSArray *lines = [fileString componentsSeparatedByString:@"\n"];

	return (NSString *)[lines objectAtIndex:0];
}

//Opens a full screen test window
-(void) setFullScreenWindow {

	int windowLevel;
	
	[window deminiaturize:nil];
	if ([[window screen] isEqual:[[NSScreen screens] objectAtIndex:0]]) {
		[NSMenu setMenuBarVisible:NO];
	}
		
	if(CGCaptureAllDisplays() != kCGErrorSuccess) {
		//NSLog(@"could not capture main display.....");
	}
	
	windowLevel = CGShieldingWindowLevel();
			
	fullscreenWindow = [[FullscreenWindow alloc]
		initWithContentRect:[window contentRectForFrameRect:[window frame]]
		styleMask:NSBorderlessWindowMask
		backing:NSBackingStoreBuffered
		defer:YES screen:[NSScreen mainScreen]];
			
	[fullscreenWindow setLevel:windowLevel];
	[fullscreenWindow setContentView:[window contentView]];
	[fullscreenWindow setFrame:
						[fullscreenWindow frameRectForContentRect:[[window screen] frame]]
						display:YES
						animate:NO];
	[fullscreenWindow makeKeyAndOrderFront:nil];
	[window orderOut:nil];		
}

@end
