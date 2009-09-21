#ifndef WINLOCKDLL_H
#define WINLOCKDLL_H

typedef struct _THREAD_DATA
{
	HDESK hDesk;
	char  szDesktopName[20];
} THREAD_DATA;

#ifdef  _DLL_
#define DLL_EXP_IMP __declspec(dllexport)
#else
#define DLL_EXP_IMP __declspec(dllimport)
#endif

DLL_EXP_IMP int WINAPI TaskSwitching_Enable_Disable(BOOL bEnableDisable);
DLL_EXP_IMP int WINAPI CtrlAltDel_Enable_Disable(BOOL bEnableDisable);
DLL_EXP_IMP int WINAPI Process_Desktop(char *szDesktopName, char *szPath);

#endif