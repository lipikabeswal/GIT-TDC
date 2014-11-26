
#import "MyDocument.h"
#import <WebKit/WebKit.h>
#import "FullscreenWindow.h"

	int cmdIsDown = 0;
	int ctrlIsDown = 0;
	int shiftIsDown = 0;
	int optIsDown = 0;
	int fnIsDown = 0;

    CFMachPortRef eventTap = nil;//required globally
	
static CGEventRef MouseCallback(CGEventTapProxy proxy, CGEventType event_type, CGEventRef event, void *info)
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

@implementation MyDocument
@synthesize window;

-(void)dealloc {
	//NSLog(@"dealloc called...");
    //[webView close];
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

- (void)awakeFromNib {

	//NSLog(@"awakeFromNib....");   
    CGEventMask eventMask = 0;
    //CFMachPortRef eventTap = nil;
    CFRunLoopSourceRef runLoopSource = nil;
	CFRunLoopRef runLoop = nil;

    eventMask = CGEventMaskBit(kCGEventKeyDown)|
				CGEventMaskBit(kCGEventKeyUp)|
				CGEventMaskBit(kCGEventFlagsChanged)|
				CGEventMaskBit(kCGEventTapDisabledByTimeout);
	
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
								MouseCallback, 
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
}

-(void)windowControllerDidLoadNib:(NSWindowController *) aController {

    [super windowControllerDidLoadNib:aController];

	[webView setFrameLoadDelegate:self];
    [webView setUIDelegate:self];
	//[webView setDrawsBackground:NO];//to avoid default white background
	
	NSAutoreleasePool *pool;
	pool = [NSAutoreleasePool new]; 
    
	NSString *weburl;
    
    // Change for LASLINKS switch mechanism //
    
    NSError *err=nil;
    NSString *form=[NSString stringWithContentsOfFile:@"form.txt" encoding:NSUTF8StringEncoding error:&err];
    
    NSArray *temp =[form componentsSeparatedByString:@"\n"];
    form=[temp objectAtIndex:0];
    temp =[form componentsSeparatedByString:@":"];
    
    
    NSString *prodType=[temp objectAtIndex:0];
    form=[temp objectAtIndex:1];
    if(!err)
    {
        if ([prodType isEqualToString:@"LASLINKS"]) {
            
           if ([form isEqualToString:@"Form A/Form B/Espanol"]) {
                weburl = @"http://127.0.0.1:12345/login_swf.html";
                
            } else if ([form isEqualToString:@"Form C/Form D/Espanol2"] ) {
                weburl = @"http://127.0.0.1:12345/login.html";
            }
            else{
                weburl = @"http://127.0.0.1:12345/login.html";
               }
        }
        else{
            weburl = @"http://127.0.0.1:12345/login.html";
           
            
        }
        
        [[NSFileManager defaultManager] removeItemAtPath:@"form.txt" error:nil];
    }
    else
    {
        weburl = @"http://127.0.0.1:12345/login.html";
        
		}
   
    // Change for LASLINKS switch mechanism //

    //Dispatch thread with method checkForScreenShotsAfter and NSDate object with current time to check for screenshots taken after this point
    screenshotThread = [[NSThread alloc] initWithTarget:self selector:@selector(checkForScreenShotsAfter:)                                     object:[NSDate date]];
    
    [screenshotThread start];
    
	
    // Load the default URL
	NSURL *URL = [NSURL URLWithString:weburl];
    [self loadURL:URL];
	
	[self setFullScreenWindow];
	[self disableHotKeys];	
	[self disableEject];

	[pool drain];		
}

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

- (NSArray *)webView:(WebView *)sender contextMenuItemsForElement:(NSDictionary *)element
    defaultMenuItems:(NSArray *)defaultMenuItems
{
    // disable right-click context menu
    return nil;
}

/*will do it later 
- (void)webView:(WebView *)sender didFinishLoadForFrame:(WebFrame *)frame
{
	NSURL *url = [webView mainFrameURL];
	NSLog(@"didFinishLoadForFrame.....%@",url);
}*/

-(void)close {
    //[webView close];
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

	//[fullscreenWindow setBackgroundColor:[self colorWithHexColorString:@"6691B4"]];//settting blue background			
	[fullscreenWindow setLevel:windowLevel];
	[fullscreenWindow setContentView:[window contentView]];
	[fullscreenWindow setFrame:
						[fullscreenWindow frameRectForContentRect:[[window screen] frame]]
						display:YES
						animate:NO];
	[fullscreenWindow makeKeyAndOrderFront:nil];
	[window orderOut:nil];		
}

/*- (NSColor*)colorWithHexColorString:(NSString*)inColorString
{
	NSColor* result    = nil;
	unsigned colorCode = 0;
	unsigned char redByte, greenByte, blueByte;
 
	if (nil != inColorString)
	{
		NSScanner* scanner = [NSScanner scannerWithString:inColorString];
		(void) [scanner scanHexInt:&colorCode]; // ignore error
	}
	redByte   = (unsigned char)(colorCode >> 16);
	greenByte = (unsigned char)(colorCode >> 8);
	blueByte  = (unsigned char)(colorCode);     // masks off high bits
 
	result = [NSColor
		          colorWithCalibratedRed:(CGFloat)redByte    / 0xff
		          green:(CGFloat)greenByte / 0xff
		          blue:(CGFloat)blueByte   / 0xff
		          alpha:1.0];
	return result;
}*/
@end
