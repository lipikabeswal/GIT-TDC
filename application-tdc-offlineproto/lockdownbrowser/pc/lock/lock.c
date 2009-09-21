/***********************************************************************
 * lock - Windows desktop lockdown library
 *
 * Adapted from the original code by A. Miguel Feijão
 ***********************************************************************/

#define     WIN32_LEAN_AND_MEAN
#define     _WIN32_WINNT 0x0400

#include    <windows.h>
#include    <stdlib.h>
#include	<stdio.h>
#include	<tchar.h>
#include	"psapi.h"
#include    "lock.h"
#include    "LockDownBrowserWrapper.h"
#include    "inject.h"

#define     PROGRAM_MANAGER "Program Manager"	// Program manager window name
#define     TASKBAR         "Shell_TrayWnd"		// Taskbar class name
#define     ID_STARTBUTTON  0x130				// Start button ID
#define     ID_TRAY         0x12F				// System tray ID
#define     ID_CLOCK        0x12F				// System clock ID

HINSTANCE	hInst;		    // Instance handle


/**************************************
 * Low Level Keyboard Hook procedure. *
 * (Win NT4SP3+)                      *
 **************************************/

HHOOK hKeyboardHook;  // Old low level keyboard hook 

LRESULT CALLBACK LowLevelKeyboardProc(int nCode, WPARAM wParam, LPARAM lParam) 
{
    PKBDLLHOOKSTRUCT p;

    if (nCode == HC_ACTION) 
    {
        p = (PKBDLLHOOKSTRUCT) lParam;

        if (
            // WIN key (for Start Menu)
            ((p->vkCode == VK_LWIN) || (p->vkCode == VK_RWIN)) ||       
            // ALT+TAB
            (p->vkCode == VK_TAB && p->flags & LLKHF_ALTDOWN) ||       
            // ALT+ESC
            (p->vkCode == VK_ESCAPE && p->flags & LLKHF_ALTDOWN) ||    
            // CTRL+ESC
            ((p->vkCode == VK_ESCAPE) && ((GetAsyncKeyState(VK_CONTROL) & 0x8000) != 0)) ||
            // CTRL+SHIFT+ESC
            ((p->vkCode == VK_ESCAPE) && ((GetAsyncKeyState(VK_CONTROL) & 0x8000) != 0) && ((GetAsyncKeyState(VK_SHIFT) & 0x8000) != 0)) ||
			// CTRL+ALT+DEL (Unfortunately doesn't work !)
            ((p->vkCode == VK_DELETE) && ( (p->flags & LLKHF_ALTDOWN) != 0 ) && ( (GetAsyncKeyState(VK_CONTROL) & 0x8000) != 0))
            )
            return 1;
   }
                    
   return CallNextHookEx(hKeyboardHook, nCode, wParam, lParam);
}

/* JNI wrapper call */
JNIEXPORT void JNICALL Java_com_ctb_tdc_bootstrap_processwrapper_LockdownBrowserWrapper_TaskSwitching_1Enable_1Disable(JNIEnv *env, jclass obj, jboolean bEnableDisable)
{
	TaskSwitching_Enable_Disable(bEnableDisable);
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
JNIEXPORT void JNICALL Java_com_ctb_tdc_bootstrap_processwrapper_LockdownBrowserWrapper_CtrlAltDel_1Enable_1Disable(JNIEnv *env, jclass obj, jboolean bEnableDisable)
{
	CtrlAltDel_Enable_Disable(bEnableDisable);
}

/*****************************************************************
 * Enable/Disable Ctrl+Alt+Del and Ctrl+Shift+Esc key sequences. *
 * TRUE=Enable, FALSE=Disable                                    *
 * (Win 2K).                                                     *
 *****************************************************************/
int DLL_EXP_IMP WINAPI CtrlAltDel_Enable_Disable(BOOL bEnableDisable)
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

int DisplayBlacklistMessageBox()
{
    int msgboxID = MessageBox(
        NULL,
        "Forbidden process detected.",
        "Error",
        MB_ICONEXCLAMATION | MB_OK
    );

    if (msgboxID == IDOK)
    {
        // TODO: add code
    }

    return msgboxID;	
}

int CheckProcessBlacklist( )
{
	DWORD aProcesses[1024], cbNeeded, cProcesses;
    unsigned int i;

    if ( !EnumProcesses( aProcesses, sizeof(aProcesses), &cbNeeded ) )
        return 0;

    // Calculate how many process identifiers were returned.

    cProcesses = cbNeeded / sizeof(DWORD);

    // Print the name and process identifier for each process.

	for ( i = 0; i < cProcesses; i++ ) {
		if( aProcesses[i] != 0 ) {
            

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

			//_tprintf( TEXT("%s  (PID: %u)\n"), szProcessName, aProcesses[i] );
			if(0 == strcmp(szProcessName, "iexplore.exe") || 0 == strcmp(szProcessName, "OUTLOOK.EXE")) {
				DisplayBlacklistMessageBox();
				return 1;
			}

			CloseHandle( hProcess );
		}
	}
	return 0;
}

/* JNI wrapper call */
JNIEXPORT void JNICALL Java_com_ctb_tdc_bootstrap_processwrapper_LockdownBrowserWrapper_Process_1Desktop(JNIEnv *env, jclass obj, jstring szDesktopName, jstring szPath)
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


/************************
 * Library entry point. *
 ************************/
BOOL WINAPI DllMain(HINSTANCE hInstDll, DWORD fdwReason, LPVOID lpReserved)
{
    hInst = hInstDll;

	return TRUE;
}
