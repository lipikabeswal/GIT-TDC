// newdll.cpp : Defines the entry point for the DLL application.
//

#include "stdafx.h"
#include "newdll.h"
#include "LockdownBrowserWrapper.h"
#include <windows.h>
#include <stdlib.h>
#include <stdio.h>

HINSTANCE	hInst;	

BOOL APIENTRY DllMain( HINSTANCE hModule, 
                       DWORD  ul_reason_for_call, 
                       LPVOID lpReserved
					 )
{
    hInst = hModule;

    return TRUE;
}



/* JNI WRAPPER CALL FOR EXE */

JNIEXPORT void JNICALL Java_com_ctb_tdc_bootstrap_processwrapper_LockdownBrowserWrapper_Hot_1Keys_1Enable_1Disable
  (JNIEnv *env, jclass obj, jboolean bEnableDisable)
{
	if (!bEnableDisable) 
	{
	 WinExec(".\\WinLock-C.exe",SW_HIDE);
	} 
	else 
	{
		system("taskkill /IM WinLock-C.exe");
	/*	system("taskkill /IM LockdownBrowser.exe"); */
	}
 
}
