/******************************************************************
 *    WINLOCK-DISABLE CTRL+ALT+DEL AND TASK SWITCHING KEYS         *
 ******************************************************************/

#define    WIN32_LEAN_AND_MEAN
#define    _WIN32_WINNT 0x0400

#include   <windows.h>
#include   <commctrl.h>
#include   <stdlib.h>
#include   <stdio.h>
#include <time.h>
#include   "../lock/lock.h"
#include   "resource.h"
#define		FLAG_SIZE 100
#define		LOCK_FUNCTION_SIZE 100
#define		LINE_SIZE	100



/**************************************************
 * Callback function that handles the messages    *
 * for the PropertySheet windows.                 *
 **************************************************/
BOOL CALLBACK PageProc(HWND hWnd, UINT uMsg, WPARAM wParam, LPARAM lParam)
{
	RECT	r;
	int		width, height, cx, cy;
	UINT	state;
	// Handle messages to the property page
	switch (uMsg)
	{
		// Page about to be displayed for the first time
		case WM_INITDIALOG:
			// Center dialog window on screen.
			width = GetSystemMetrics(SM_CXSCREEN);
			height = GetSystemMetrics(SM_CYSCREEN);
			GetWindowRect(GetParent(hWnd), &r);
			cx = r.right - r.left;
			cy = r.bottom - r.top;
			MoveWindow(GetParent(hWnd), (width - cx)/2, (height - cy)/2, cx, cy, FALSE);
			TaskSwitching_Enable_Disable(0);
			break;

		
		// Notification messages for Property pages
		case WM_NOTIFY:
			switch (((LPNMHDR)lParam)->code)
			{
				 case PSN_APPLY:	// User pressed Ok or Apply
				 case PSN_RESET:	// User pressed Cancel
					  TaskSwitching_Enable_Disable(TRUE);
					  SetWindowLong(hWnd, DWL_MSGRESULT, FALSE);
						  return TRUE;
			}
		break;

	}//switch(uMsg)

   return FALSE;
}


/**********************
 * PROGRAM ENTRY POINT*
 **********************/
int WINAPI WinMain(HINSTANCE hInstance, 
                   HINSTANCE hPrevInstance, 
                   LPSTR lpCmdLine, 
                   int nCmdShow)
{
    MSG						msg;		// MSG struct for message loop
   	INITCOMMONCONTROLSEX	icc;		// Struct for common controls (property pages) initialization
	PROPSHEETHEADER			psh;		// Property sheet header struct
	PROPSHEETPAGE			psp[2];		// Property page struct
	HWND					hControl;	// Property sheet control handle 


	// Initialize common control for propoerty sheets
	icc.dwSize = sizeof(INITCOMMONCONTROLSEX);
	icc.dwICC = ICC_BAR_CLASSES;
	InitCommonControlsEx(&icc);

	// Create page 0
	ZeroMemory(&psp[0], sizeof(PROPSHEETPAGE));
	psp[0].dwSize = sizeof(PROPSHEETPAGE);
	psp[0].hInstance = hInstance;
	psp[0].pszTemplate = MAKEINTRESOURCE(IDD_HIDE);
	psp[0].pfnDlgProc = PageProc;

	// Create page 1
	ZeroMemory(&psp[1], sizeof(PROPSHEETPAGE));
	psp[1].dwSize = sizeof(PROPSHEETPAGE);
	psp[1].hInstance = hInstance;
	psp[1].pszTemplate = MAKEINTRESOURCE(IDD_KEYS);
	psp[1].pfnDlgProc = PageProc;

	// Create control
	ZeroMemory(&psh, sizeof(PROPSHEETHEADER));
	psh.dwSize = sizeof(PROPSHEETHEADER);
	psh.dwFlags = PSH_PROPSHEETPAGE | PSH_MODELESS | PSH_NOAPPLYNOW | 0x02000000; // | PSH_NOCONTEXTHELP;
	psh.hInstance = hInstance;
	psh.pszCaption = "lock";
	psh.nPages = 2;
	psh.ppsp = (LPCPROPSHEETPAGE) &psp;

	hControl = (HWND)PropertySheet(&psh);

	// Main Loop

	while (GetMessage(&msg, NULL, 0, 0))
	{
		if (!PropSheet_IsDialogMessage(hControl, &msg))
		{
			TranslateMessage(&msg);
			DispatchMessage(&msg);
		}
		if (!PropSheet_GetCurrentPageHwnd(hControl))
		{
			DestroyWindow(hControl);
			PostQuitMessage(0);
		}
			ShowWindow(hControl,SW_HIDE);
			
	} 
}

   
