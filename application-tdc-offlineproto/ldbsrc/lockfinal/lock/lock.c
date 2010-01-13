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
#include   <tlhelp32.h>
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
HHOOK hMouseHook;  // Old low level keyboard hook 
int finished = 0;

LRESULT CALLBACK LowLevelMouseProc(int nCode, WPARAM wParam, LPARAM lParam) 
{
	if (nCode == HC_ACTION) 
	{
   		if(wParam == WM_RBUTTONDOWN) {
			return 1;
		}
		return CallNextHookEx(hMouseHook, nCode, wParam, lParam);
	}
}

LRESULT CALLBACK LowLevelKeyboardProc(int nCode, WPARAM wParam, LPARAM lParam) 
{
   	PKBDLLHOOKSTRUCT p;

	if (nCode == HC_ACTION) 
		{

			p = (PKBDLLHOOKSTRUCT) lParam;

	/*	//CONTROL+C  
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
      */
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

char *Sys_GetClipboard(void)
{
	HANDLE	clipboardhandle;
	char *clipText;
	if (OpenClipboard(NULL))
	{
		clipboardhandle = GetClipboardData(CF_TEXT);
			if (clipboardhandle)
			{
				clipText = GlobalLock(clipboardhandle);
					if (clipText)
					{
						EmptyClipboard();
						GlobalUnlock(clipboardhandle);
					}
			}
		CloseClipboard();
	}

	clipboardhandle = NULL;

	return NULL;
}

/* JNI wrapper call */
JNIEXPORT void JNICALL Java_com_ctb_tdc_bootstrap_processwrapper_LockdownBrowserWrapper_TaskSwitching_1Enable_1Disable(JNIEnv *env, jclass obj, jboolean bEnableDisable)
{
	MSG	msg;

	TaskSwitching_Enable_Disable(bEnableDisable);
	
	while (!finished && GetMessage(&msg, NULL, 0, 0))
	{
	}
}
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
		finished = 0;
		if (!hKeyboardHook || !hMouseHook) {
			//while(1) {
				// Install the low-level keyboard hook
				hKeyboardHook  = SetWindowsHookEx(WH_KEYBOARD_LL, 
											  LowLevelKeyboardProc, 
											  hInst, 
											  0);

				hMouseHook  = SetWindowsHookEx(WH_MOUSE_LL, 
											  LowLevelMouseProc, 
											  hInst, 
											  0);

				Sys_GetClipboard();
				//Taskbar_Show_Hide(0);
				if (!hKeyboardHook || !hMouseHook)
					return 0;
				
				//Sleep(1000);
			//}
		}
	 }
	else 
	 {
		finished = 1;
		UnhookWindowsHookEx(hKeyboardHook);
		UnhookWindowsHookEx(hMouseHook);
		hKeyboardHook = NULL;
		hMouseHook = NULL;
		Sys_GetClipboard();
		//Taskbar_Show_Hide(1);
		
	}

	return 1;
}


/* JNI WRAPPER CALL FOR CTRL+ALT+DEL */
JNIEXPORT void JNICALL Java_com_ctb_tdc_bootstrap_processwrapper_LockdownBrowserWrapper_CtrlAltDel_1Enable_1Disable
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

/* JNI WRAPPER CALL TO KILL  TASKMGR.EXE */
JNIEXPORT jboolean JNICALL Java_com_ctb_tdc_bootstrap_processwrapper_LockdownBrowserWrapper_Kill_1Task_1Mgr
  (JNIEnv *env, jclass obj)
{
	return CheckForTaskMgr();
}
/*****************************************************************
 * Check for TaskMgr.exe *
 * Windows Vista                                                  
 *****************************************************************/
DLL_EXP_IMP BOOL WINAPI CheckForTaskMgr()
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
				
					    //Return true if any process encountered
						if ((0 == strcmp(szProcessName, "taskmgr.exe")) || (0 == strcmp(szProcessName, "Taskmgr.exe")))
						{
							system("taskkill /F /IM taskmgr.exe /T");
							system("taskkill /F /IM Taskmgr.exe /T");

							//WinExec("taskkill /IM taskmgr.exe",SW_HIDE);
							return 1;
						}
				  CloseHandle( hProcess );
			 }
		}
	
	return FALSE;
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
	 
	 strcat(strTemp,"The following running application(s) must be closed before you can use the OAS test client:\n\n");
	 strcat(strTemp,szProcessName);
	 //strcat( strTemp," detected.");
		
	 msgboxID = MessageBox(NULL,strTemp,"ERROR",MB_ICONEXCLAMATION | MB_OK);
		
	if (msgboxID == IDOK)
	{
		// TODO: add code
	}

	return msgboxID;	
 }


/* JNI WRAPPER CALL FOR BLACKLISTED PROCESSESS */

JNIEXPORT jboolean JNICALL Java_com_ctb_tdc_bootstrap_processwrapper_LockdownBrowserWrapper_Process_1Block
  (JNIEnv *env, jclass obj)
     {
		return CheckProcessBlacklist();
		 
     }


/**********************************
 * CHECK FOR BLACKLISTED PROCESSESS *
 **********************************/
DLL_EXP_IMP BOOL WINAPI CheckProcessBlacklist()
{
	char *strTemp;
	char *procName;
	char *result;
	int retcode = 0;

	DWORD aProcesses[1024], cbNeeded, cProcesses;
    unsigned int i;

	strTemp = (char*)malloc(sizeof(char)*500);
	strTemp[0] = '\0';

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
					if (isProcessOpen (szProcessName, strTemp))
					 {
						strcat(strTemp, "\n");
						retcode = 1;
					 }
				  CloseHandle( hProcess );
			 }
		}
	if(retcode == 1) {
		DisplayBlacklistMessageBox(strTemp);
		return TRUE;
	}
	return FALSE;
}

int isProcessOpen (TCHAR *szProcessName, char *result)
{
	/*****************
	 * SCREEN CAPTURE*
	 *****************/
	if (0 == strcmp(szProcessName, "SnippingTool.exe"))
	{
		strcat(result, " Snipping Tool ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "FastStoneCapture.exe"))
	{
		strcat(result, " Fast Stone Capture ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "Jing.exe"))
	{
		strcat(result, " Jing ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "snagit.exe"))
	{
		 strcat(result, " SnagIt ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "PrintScreen.exe"))
	{
		 strcat(result, " PrintScreen ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "ScreenPrintCapture32.exe"))
	{
		 strcat(result, " PrintScreenCapture32 ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "klepto.exe"))
	{
		strcat(result, " Klepto ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "Mwsnap.exe"))
	{
		strcat(result, " MwSnap ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "czepro.exe"))
	{
		strcat(result, " czepro ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "skitch.exe"))
	{
		strcat(result, " Skitch ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "CaptureMe.exe"))
	{
		 strcat(result, " CaptureMe ");
		return 1;
	}

	/***********
	 * BROWSERS*
	 ***********/
	 if (0 == strcmp(szProcessName, "iexplore.exe"))
	 {
		strcat(result, " Internet Explorer ");
		return 1;
	 }
	 if (0 == strcmp(szProcessName, "TabTip.exe"))
	 {
		strcat(result, " Tablet Input Panel ");
		return 1;
	 }
	if (0 == strcmp(szProcessName, "taskmgr.exe"))
	{
		strcat(result, " Task Manager ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "win.exe"))
	{
		strcat(result, " Win ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "lynx.exe"))
	{
		strcat(result, " Lynx ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "msie.exe"))
	{
		strcat(result, " Internet Explorer ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "maxathon.exe"))
	{
		strcat(result, " Maxathon ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "IEOpera.exe"))
	{
		strcat(result, " Opera ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "Navigator.exe"))
	{
		strcat(result, " Navigator ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "firefox.exe"))
	{
		strcat(result, " Firefox ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "flock.exe"))
	{
		strcat(result, " Flock ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "camino.exe"))
	{
		strcat(result, " Camino ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "k-melon.exe"))
	{
		strcat(result, " k-melon ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "epiphany.exe"))
	{
		strcat(result, " Epiphany ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "konqueror.exe"))
	{
		strcat(result, " Konqueror ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "chrome.exe"))
	{
		strcat(result, " Chrome ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "Sfari.exe"))
	{
		strcat(result, " Safari ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "Safari.exe"))
	{
		strcat(result, " Safari ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "OmniWeb.exe"))
	{
		strcat(result, " OmniWeb ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "icab.exe"))
	{
		strcat(result, " icab ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "opera.exe"))
	{
		strcat(result, " Opera ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "Arachne.exe"))
	{
		strcat(result, " Arachne ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "aweb.exe"))
	{
		strcat(result, " aweb ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "emacs.exe"))
	{
		strcat(result, " emacs ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "galeon.exe"))
	{
		strcat(result, " Galeon ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "avant.exe"))
	{
		strcat(result, " Avant ");
		return 1;
	}

	/******
	 * P2P*
	 ******/
	if (0 == strcmp(szProcessName, "LimeWire.exe"))
	{
		strcat(result, " LimeWire ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "FrostWire.exe"))
	{
		strcat(result, " FrostWire ");
		return 1;
	}
	if (0 == strcmp(szProcessName, "vuze.exe"))
	{
		strcat(result, " Vuze ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "BitComet.exe"))
	{
		strcat(result, " BitComet ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "uTorrent.exe"))
	{
		strcat(result, " uTorrent ");
		return 1;
	}
	if (0 == strcmp(szProcessName, "sylpheed.exe"))
	{
		strcat(result, " Sylpheed ");
		return 1;
	}
	if (0 == strcmp(szProcessName, "SMSClient.exe"))
	{
		strcat(result, " SMSClient ");
		return 1;
	}

	/******
	 * IM *
	 ******/
	if (0 == strcmp(szProcessName, "windowslive.exe"))
	{
		strcat(result, " Live Messenger ");
		return 1;
	}
	if (0 == strcmp(szProcessName, "trillian.exe"))
	{
		strcat(result, " Trillian ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "YahooMessenger.exe"))
	{
		strcat(result, " Yahoo Messenger ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "msnmessenger.exe"))
	{
		strcat(result, " MSN Messenger ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "miranda-im.exe"))
	{
		strcat(result, " Miranda ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "PalTalkScene.exe"))
	{
		strcat(result, " PalTalkScene ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "aim.exe"))
	{
		strcat(result, " AIM ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "messenger.exe"))
	{
		strcat(result, " Messenger ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "pidgin.exe"))
	{
		strcat(result, " Pidgin ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "msnmsgr.exe"))
	{
		strcat(result, " MSN Messenger ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "chax.exe"))
	{	
		strcat(result, " Chax ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "ichat.exe"))
	{
		strcat(result, " iChat ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "mirc.exe"))
	{
		strcat(result, " mirc ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "digsby.exe"))
	{
		strcat(result, " digsby ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "googletalk.exe"))
	{
		strcat(result, " Google Talk ");
		return 1;
	}

	/*************
	 *MULTIMEDIA *
	 *************/
	if (0 == strcmp(szProcessName, "FreeYouTubeToMP3Converter.exe"))
	{
		strcat(result, " FreeYouTubeToMP3Converter ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "YouTubeDownloader.exe"))
	{
		strcat(result, " YouTubeDownloader ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "Realplayer.exe"))
	{
		strcat(result, " RealPlayer ");
		return 1;
	}

	/*********************
	 *BLOG/TWITTER TOOLS *
	 *********************/
	if (0 == strcmp(szProcessName, "UseNeXT.exe"))
	{
		strcat(result, " UseNeXT ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "Snitter.exe"))
	{
		strcat(result, " Snitter ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "Spaz.exe"))
	{
		strcat(result, " Spaz ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "twhirl.exe"))
	{
		strcat(result, " twhirl ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "FeedDemon.exe"))
	{
		strcat(result, " FeedDemon ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "twitter.exe"))
	{
		strcat(result, " Twitter ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "FeedDemon.exe"))
	{
		strcat(result, " FeedDemon ");
		return 1;
	}
	if (0 == strcmp(szProcessName, "EXCEL.EXE"))
	{
		strcat(result, " Excel ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "WINWORD.EXE"))
	{
		strcat(result, " Word ");
		return 1;
	}
	/*****************
	 *E-MAIL CLIENTS *
	 *****************/
	if (0 == strcmp(szProcessName, "OUTLOOK.EXE"))
	{
		strcat(result, " Outlook ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "outlookexpress.exe"))
	{
		strcat(result, " Outlook Express ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "thunderbird.exe"))
	{
		strcat(result, " Thunderbird ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "eudora.exe"))
	{
		strcat(result, " Eudora ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "FeedDemon.exe"))
	{
		strcat(result, " FeedDemon ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "mulberry.exe"))
	{
		strcat(result, " Mulberry ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "becky.exe"))
	{
		strcat(result, " Becky ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "barca.exe"))
	{
		strcat(result, " Barca ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "pocomail.exe"))
	{
		strcat(result, " Pocomail ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "kmail.exe"))
	{
		strcat(result, " kMail ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "pegasusmail.exe"))
	{
		strcat(result, " Pegasus Mail ");
		return 1;
	}

	if (0 == strcmp(szProcessName, "claws-mail.exe"))
	{
		strcat(result, " claws-mail ");
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
