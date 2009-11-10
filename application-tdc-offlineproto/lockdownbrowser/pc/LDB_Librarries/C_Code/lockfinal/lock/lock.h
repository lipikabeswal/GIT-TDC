#ifndef WINLOCKDLL_H
#define WINLOCKDLL_H

#ifdef  _DLL_
#define DLL_EXP_IMP __declspec(dllexport)
#else
#define DLL_EXP_IMP __declspec(dllimport)
#endif

DLL_EXP_IMP int WINAPI TaskSwitching_Enable_Disable(BOOL bEnableDisable);
DLL_EXP_IMP int WINAPI CTRLALTDEL_Enable_Disable(BOOL bEnableDisable);
DLL_EXP_IMP int WINAPI CheckProcessBlacklist( );
DLL_EXP_IMP int WINAPI Process_Desktop(char *szDesktopName, char *szPath);
#endif