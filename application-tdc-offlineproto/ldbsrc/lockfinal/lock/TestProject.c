#include "lock.h"
int WINAPI WinMain(HINSTANCE hInstance, 
                   HINSTANCE hPrevInstance, 
                   LPSTR lpCmdLine, 
                   int nCmdShow)
{

	boolean flg = false;
	TaskSwitching_Enable_Disable(flg);
	return 0;
}
