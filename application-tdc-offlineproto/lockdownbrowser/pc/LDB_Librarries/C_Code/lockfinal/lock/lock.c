/***********************************************************************
 * 
 *                 LOCK-WINDOWS DESKTOP LOCKDOWN LIBRARY              *
 * 
 ***********************************************************************/

#define     WIN32_LEAN_AND_MEAN
#define     _WIN32_WINNT 0x0400

#include    <windows.h>
#include    <stdlib.h>
#include	<stdio.h>
#include	<tchar.h>
#include	"psapi.h"
#include    "lock.h"
#include    "inject.h"
#include   "LockdownBrowserWrapper.h"
#include   "jni.h"
#define     PROGRAM_MANAGER "Program Manager"	// Program manager window name
#define     TASKBAR         "Shell_TrayWnd"		// Taskbar class name
#define     ID_STARTBUTTON  0x130				// Start button ID
#define     ID_TRAY         0x12F				// System tray ID
#define     ID_CLOCK        0x12F				// System clock ID


 
/**************************************
 * Low Level Keyboard Hook procedure. *
 * (Win NT4SP3+)                      *
 **************************************/

HINSTANCE	hInst;		    // Instance handle
HHOOK hKeyboardHook;  // Old low level keyboard hook 

LRESULT CALLBACK LowLevelKeyboardProc(int nCode, WPARAM wParam, LPARAM lParam) 
{
   	PKBDLLHOOKSTRUCT p;

	if (nCode == HC_ACTION) 
		{

			p = (PKBDLLHOOKSTRUCT) lParam;

		//CONTROL+C  
		if (p->vkCode == VkKeyScan('c') && (GetAsyncKeyState(VK_CONTROL) & 0x8000) != 0) 
		{
			return 1;
		}

		//CONTROL+V
		if (p->vkCode == VkKeyScan('v') && (GetAsyncKeyState(VK_CONTROL) & 0x8000) != 0) 
		{
			return 1;
		}

		//CONTROL+X 
		if(p->vkCode == VkKeyScan('x') && (GetAsyncKeyState(VK_CONTROL) & 0x8000) != 0) 
		{
		   return 1;
		}

		// ALT+TAB
		if((p->vkCode == VK_TAB && p->flags & LLKHF_ALTDOWN)) 
		{
		    return 1;
		}

		//Alt+F4
		if((p->vkCode == VK_F4 && p->flags & LLKHF_ALTDOWN)) 
		{
		    return 1;
		}

		// ALT+ESC
		if((p->vkCode == VK_ESCAPE && p->flags & LLKHF_ALTDOWN)) 
		{
		    return 1;
		}

		//ALT+F6
		if((p->vkCode == VK_SPACE) && ((p->flags & LLKHF_ALTDOWN) != 0 )) 
		{
		    return 1;
		}

		//ALT+ -
		if((p->vkCode == VK_SUBTRACT) && ((p->flags & LLKHF_ALTDOWN) != 0 )) 
		{
		    return 1;
		}

		// CTRL+SHIFT+ESC
		if((p->vkCode == VK_ESCAPE) && ((GetAsyncKeyState(VK_CONTROL) & 0x8000) != 0) && ((GetAsyncKeyState(VK_SHIFT) & 0x8000) != 0)) 
		{
		    return 1;
		}

		// CTRL+ALT+DEL 
		if((p->vkCode == VK_DELETE) && ((p->flags & LLKHF_ALTDOWN) != 0 ) && ((GetAsyncKeyState(VK_CONTROL) & 0x8000) != 0)) 
		{
		    return 1;
		}

		//ALT+ENTER
		if((p->vkCode == VK_RETURN) && ((p->flags & LLKHF_ALTDOWN) != 0 )) 
		{
		    return 1;
		}

		//ALT+SPACEBAR
		if((p->vkCode == VK_SPACE) && ((p->flags & LLKHF_ALTDOWN) != 0 )) 
		{
		    return 1;
		}

		//CONTROL+ESCAPE
		if((p->vkCode == VK_ESCAPE) && ((GetAsyncKeyState(VK_CONTROL) & 0x8000) != 0)) 
		{
		    return 1;
		}

		//CONTROL+TAB
		if((p->vkCode == VK_TAB) && ((GetAsyncKeyState(VK_CONTROL) & 0x8000) != 0)) 
		{
		    return 1;
		}

		//CONTROL+F4
		if((p->vkCode == VK_F4) && ((GetAsyncKeyState(VK_CONTROL) & 0x8000) != 0)) 
		{
		    return 1;
		}

		//F1
		if(p->vkCode == VK_F1)
		{
		   return 1;
		}

		//F5
		if(p->vkCode == VK_F5)
		{
		   return 1;
		}

		//F6
		if(p->vkCode == VK_F6)
		{
		   return 1;
		}

		//F10
		if(p->vkCode == VK_F10)
		{
		   return 1;
		}

		//SHIFT+F10
		if((p->vkCode == VK_F10) && ((GetAsyncKeyState(VK_SHIFT) & 0x8000) != 0))
		{
		    return 1;
		}

		//CONTROL+RIGHT ARROW
		if((p->vkCode == VK_RIGHT) && ((GetAsyncKeyState(VK_CONTROL) & 0x8000) != 0))
		{
		    return 1;
		}

		//CONTROL+LEFT ARROW
		if((p->vkCode == VK_LEFT) && ((GetAsyncKeyState(VK_CONTROL) & 0x8000) != 0))
		{
		    return 1;
		}

		//CONTROL+UP ARROW
		if((p->vkCode == VK_UP) && ((GetAsyncKeyState(VK_CONTROL) & 0x8000) != 0))
		{
		    return 1;
		}

		//CONTROL+DOWN ARROW
		if((p->vkCode == VK_DOWN) && ((GetAsyncKeyState(VK_CONTROL) & 0x8000) != 0))
		{
		    return 1;
		}

		//WINDOW KEY			
		if((p->vkCode == VK_LWIN) || (p->vkCode == VK_RWIN))
		{
		    return 1;
		}

		//WINDOW+U
		if((p->vkCode == VK_LWIN)&&(p->vkCode == VkKeyScan('u')))
		{
		    return 1;
		}

		//WINDOW+L
		if((p->vkCode == VK_LWIN)&&(p->vkCode == VkKeyScan('l')))
		{
		    return 1;
		}

		//WINDOW+E
		if((p->vkCode == VK_LWIN)&&(p->vkCode == VkKeyScan('e')))
		{
		    return 1;
		}

		//WINDOW+F
		if((p->vkCode == VK_LWIN)&&(p->vkCode == VkKeyScan('f')))
		{
		    return 1;
		}

		//WINDOW+R
		if((p->vkCode == VK_LWIN)&&(p->vkCode == VkKeyScan('r')))
		{
		    return 1;
		}

		//WINDOW+F1
		if((p->vkCode == VK_LWIN)&&(p->vkCode == VkKeyScan('f1')))
		{
		    return 1;
		}

		//ESCAPE KEY			
		if(p->vkCode == VK_ESCAPE)
		{
		   return 1;
		}

		//PRINTSCREEN			
		if(p->vkCode == VK_SNAPSHOT)
		{
		   return 1;
		}

		 return CallNextHookEx(hKeyboardHook, nCode, wParam, lParam);

		}

}

/* JNI wrapper call */
/*JNIEXPORT void JNICALL Java_com_ctb_tdc_bootstrap_processwrapper_LockdownBrowserWrapper_TaskSwitching_1Enable_1Disable(JNIEnv *env, jclass obj, jboolean bEnableDisable)
{
	TaskSwitching_Enable_Disable(bEnableDisable);
}*/
/*****************************************************
 * Enable/Disable task switching keys.               *
 * (Alt+Tab, Alt+Esc, Ctrl+Esc, Win, Ctrl+Shift+Esc) *
 * TRUE=Enable, FALSE=Disable                        *
 * (Win NT4SP3+, Win2K).                             *
 *****************************************************/
int DLL_EXP_IMP WINAPI TaskSwitching_Enable_Disable(BOOL bEnableDisable)
{
	if (!bEnableDisable) 
	 {

		if (!hKeyboardHook) 
		{

		// Install the low-level keyboard hook
			hKeyboardHook  = SetWindowsHookEx(WH_KEYBOARD_LL, 
										  LowLevelKeyboardProc, 
										  hInst, 
										  0);
			if (!hKeyboardHook)
			return 0;
		 }
	 }
	else 
	 {

		UnhookWindowsHookEx(hKeyboardHook);
		hKeyboardHook = NULL;
	}

	return 1;
}

/* JNI wrapper call */
/*JNIEXPORT void JNICALL Java_com_ctb_tdc_bootstrap_processwrapper_LockdownBrowserWrapper_CtrlAltDel_1Enable_1Disable(JNIEnv *env, jclass obj, jboolean bEnableDisable)
{
	CtrlAltDel_Enable_Disable(bEnableDisable);
}*/

/* JNI WRAPPER CALL FOR CTRL+ALT+DEL */
JNIEXPORT void JNICALL Java_com_javaunit_LockdownBrowserWrapper_CtrlAltDel_1Enable_1Disable
  (JNIEnv *env, jclass obj, jboolean bEnableDisable)
{
  CTRLALTDEL_Enable_Disable(bEnableDisable);
}


 /*****************************************************************
 * Enable/Disable Ctrl+Alt+Del and Ctrl+Shift+Esc key sequences. *
 * TRUE=Enable, FALSE=Disable                                    *
 * (Win 2K).                                                     *
 *****************************************************************/
int DLL_EXP_IMP WINAPI CTRLALTDEL_Enable_Disable(BOOL bEnableDisable)
{
	static BOOL bInjected = FALSE;

		if (!bEnableDisable) 
		{
			if (!bInjected) 
			{
				bInjected = Inject();
				return bInjected;
			}
		}
		else 
		{
			if (bInjected) 
			{
				bInjected = !Eject();
				return !bInjected;
			}
		}

		return 0;
}


/****************
 * Run process. *
 ****************/
BOOL StartProcess(char *szDesktopName, char *szPath) 
{


    STARTUPINFO         si;
    PROCESS_INFORMATION pi;

    // Zero these structs
    ZeroMemory(&si, sizeof(si));
    si.cb = sizeof(si);
	si.lpTitle = szDesktopName;
	si.lpDesktop = szDesktopName;
    ZeroMemory(&pi, sizeof(pi));

    // Start the child process
    if (!CreateProcess(NULL,    // No module name (use command line). 
                       szPath,  // Command line. 
                       NULL,    // Process handle not inheritable. 
                       NULL,    // Thread handle not inheritable. 
                       FALSE,   // Set handle inheritance to FALSE. 
                       0,       // No creation flags. 
                       NULL,    // Use parent's environment block. 
                       NULL,    // Use parent's starting directory. 
                       &si,     // Pointer to STARTUPINFO structure.
                       &pi))    // Pointer to PROCESS_INFORMATION structure.
			{
				return FALSE;
			}

	// Wait until process exits
	WaitForSingleObject(pi.hProcess, INFINITE);

    // Close process and thread handles
    CloseHandle(pi.hProcess);
    CloseHandle(pi.hThread);

		return TRUE;
}

int DisplayBlacklistMessageBox(TCHAR *szProcessName)
{

	 char *strTemp;
     int msgboxID = 0;
	 strTemp = (char*)malloc(sizeof(char)*200);
	 strTemp[0] = '\0';
	 
	 strcat(strTemp,"Forbidden process ");
	 strcat(strTemp,szProcessName);
	 strcat( strTemp," detected.");
		
	 msgboxID = MessageBox(NULL,strTemp,"ERROR",MB_ICONEXCLAMATION | MB_OK);
		
	if (msgboxID == IDOK)
	{
		// TODO: add code
	}

	return msgboxID;	
 }


/* JNI WRAPPER CALL FOR BLACKLISTED PROCESSESS */

JNIEXPORT jboolean JNICALL Java_com_javaunit_LockdownBrowserWrapper_Process_1Block
  (JNIEnv *env, jclass obj)
     {
		return CheckProcessBlacklist();
     }


/**********************************
 * CHECK FOR BLACKLISTED PROCESSESS *
 **********************************/
DLL_EXP_IMP BOOL WINAPI CheckProcessBlacklist()
{

	DWORD aProcesses[1024], cbNeeded, cProcesses;
    unsigned int i;

    if ( !EnumProcesses( aProcesses, sizeof(aProcesses), &cbNeeded ) )
        return FALSE;

    // Calculate how many process identifiers were returned.
		cProcesses = cbNeeded / sizeof(DWORD);

    // Print the name and process identifier for each process.
		for ( i = 0; i < cProcesses; i++ )
		 {
			if ( aProcesses[i] != 0 )
			 {
				TCHAR szProcessName[MAX_PATH] = TEXT("<unknown>");
					// Get a handle to the process.
				HANDLE hProcess = OpenProcess( PROCESS_QUERY_INFORMATION |
										   PROCESS_VM_READ,
										   FALSE, aProcesses[i] );
					// Get the process name.
					if (NULL != hProcess )
					 {
						HMODULE hMod;
						DWORD cbNeeded;
						if ( EnumProcessModules( hProcess, &hMod, sizeof(hMod), 
							&cbNeeded) )
						  {
							GetModuleBaseName( hProcess, hMod, szProcessName, 
							sizeof(szProcessName)/sizeof(TCHAR) );
						  }
					  }
				
					//Return true if any forbidden process encountered
					if (isProcessOpen (szProcessName))
					 {
						return TRUE;

					 }
				  CloseHandle( hProcess );
			 }
		}
	
	return FALSE;
}

int isProcessOpen (TCHAR *szProcessName)
{

	/*****************
	 * SCREEN CAPTURE*
	 *****************/
	if (0 == strcmp(szProcessName, "SnippingTool.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "FastStoneCapture.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "Jing.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "snagit.exe"))
	{
		 DisplayBlacklistMessageBox(szProcessName);
		 return 1;
	}

	if (0 == strcmp(szProcessName, "PrintScreen.exe"))
	{
		 DisplayBlacklistMessageBox(szProcessName);
		 return 1;
	}

	if (0 == strcmp(szProcessName, "PrintScreen.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		 return 1;
	 }

	if (0 == strcmp(szProcessName, "ScreenPrintCapture32.exe"))
	{
		 DisplayBlacklistMessageBox(szProcessName);
		 return 1;
	}

	if (0 == strcmp(szProcessName, "klepto.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		 return 1;
	}

	if (0 == strcmp(szProcessName, "Mwsnap.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		 return 1;
	}

	if (0 == strcmp(szProcessName, "czepro.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		 return 1;
	}

	if (0 == strcmp(szProcessName, "skitch.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		 return 1;
	}

	if (0 == strcmp(szProcessName, "CaptureMe.exe"))
	{
		 DisplayBlacklistMessageBox(szProcessName);
		 return 1;
	}

	/***********
	 * BROWSERS*
	 ***********/
	if (0 == strcmp(szProcessName, "win.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "lynx.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "msie.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "maxathon.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "IEOpera.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "Navigator.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "firefox.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "flock.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "camino.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "k-melon.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "epiphany.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "konqueror.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "chrome.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "Sfari.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "OmniWeb.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "icab.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "opera.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "Arachne.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "aweb.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "emacs.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "galeon.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "avant.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	/******
	 * P2P*
	 ******/
	if (0 == strcmp(szProcessName, "LimeWire.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "FrostWire.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}
	if (0 == strcmp(szProcessName, "vuze.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "BitComet.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "uTorrent.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}
	if (0 == strcmp(szProcessName, "sylpheed.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}
	if (0 == strcmp(szProcessName, "SMSClient.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	/******
	 * IM *
	 ******/
	if (0 == strcmp(szProcessName, "windowslive.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}
	if (0 == strcmp(szProcessName, "trillian.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "YahooMessenger.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "msnmessenger.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "miranda-im.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "PalTalkScene.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "aim.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "messenger.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "pidgin.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "msnmsgr.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "chax.exe"))
	{	
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "ichat.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "mirc.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "digsby.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "googletalk.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	/*************
	 *MULTIMEDIA *
	 *************/
	if (0 == strcmp(szProcessName, "FreeYouTubeToMP3Converter.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "YouTubeDownloader.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "Realplayer.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	/*********************
	 *BLOG/TWITTER TOOLS *
	 *********************/
	if (0 == strcmp(szProcessName, "UseNeXT.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "Snitter.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "Spaz.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "twhirl.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "FeedDemon.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "twitter.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "FeedDemon.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	/*****************
	 *E-MAIL CLIENTS *
	 *****************/
	if (0 == strcmp(szProcessName, "OUTLOOK.EXE"))
	{
		 DisplayBlacklistMessageBox(szProcessName);
		 return 1;
	}

	if (0 == strcmp(szProcessName, "outlookexpress.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "thunderbird.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "eudora.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "FeedDemon.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "mulberry.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "becky.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "barca.exe"))
	{
		 DisplayBlacklistMessageBox(szProcessName);
		 return 1;
	}

	if (0 == strcmp(szProcessName, "pocomail.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "kmail.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "pegasusmail.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}

	if (0 == strcmp(szProcessName, "claws-mail.exe"))
	{
		DisplayBlacklistMessageBox(szProcessName);
		return 1;
	}
	return 0;

}//END OF isProcessOpen

/* JNI wrapper call */
/*JNIEXPORT void JNICALL Java_com_ctb_tdc_bootstrap_processwrapper_LockdownBrowserWrapper_Process_1Desktop(JNIEnv *env, jclass obj, jstring szDesktopName, jstring szPath)
{
	char *pName = (*env)->GetStringUTFChars( env, szDesktopName, 0 );
	char *pPath = (*env)->GetStringUTFChars( env, szPath, 0 );

	Process_Desktop(pName, pPath);
}*/
/* JNI wrapper call */
JNIEXPORT void JNICALL Java_com_javaunit_LockdownBrowserWrapper_Process_1Desktop
  (JNIEnv *env, jclass obj, jstring szDesktopName, jstring szPath)
{
	char *pName = (*env)->GetStringUTFChars( env, szDesktopName, 0 );
	char *pPath = (*env)->GetStringUTFChars( env, szPath, 0 );

	Process_Desktop(pName, pPath);
}

/*************************************************
 * Create a new Desktop and run a Process in it. *
 * (Win NT+).                                    *
 *************************************************/
int DLL_EXP_IMP WINAPI Process_Desktop(char *szDesktopName, char *szPath)
{
	HDESK	hOriginalThread;
	HDESK	hOriginalInput;
	HDESK	hNewDesktop;

	if(!CheckProcessBlacklist()) {

		// Save original ...
		hOriginalThread = GetThreadDesktop(GetCurrentThreadId());
		hOriginalInput = OpenInputDesktop(0, FALSE, DESKTOP_SWITCHDESKTOP);

		// Create a new Desktop and switch to it
		hNewDesktop = CreateDesktop(szDesktopName, NULL, NULL, 0, GENERIC_ALL, NULL);
		SetThreadDesktop(hNewDesktop);
		SwitchDesktop(hNewDesktop);

		// Execute process in new desktop
		StartProcess(szDesktopName, szPath);

		// Restore original ...
		SwitchDesktop(hOriginalInput);
		SetThreadDesktop(hOriginalThread);

		// Close the Desktop
		CloseDesktop(hNewDesktop);
	}

	return 0;
}


/***********************
 * LIBRARY ENTRY POINT.*
 ***********************/
BOOL WINAPI DllMain(HINSTANCE hInstDll, DWORD fdwReason, LPVOID lpReserved)
{
    hInst = hInstDll;

	return TRUE;
}
