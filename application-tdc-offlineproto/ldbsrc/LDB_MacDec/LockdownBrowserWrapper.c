#include <stdio.h>
#include "jni.h"
#include "LockdownBrowserWrapper.h"
#include <stdlib.h>

JNIEXPORT void JNICALL Java_com_ctb_tdc_bootstrap_processwrapper_LockdownBrowserWrapper_Hot_1Keys_1Enable_1Disable
  (JNIEnv *env, jclass obj, jboolean bEnable_Disable) {

	if(!bEnable_Disable)
	{
		system("osascript Disable_Control.scpt");
		system("osascript Disable_Command.scpt");
		system("osascript add_shortcut.scpt");

	}

	else
	{
		system("osascript Restore_Default.scpt");
		system("osascript Restoredefault_shortcut.scpt");
	}

}

JNIEXPORT jint JNICALL Java_com_ctb_tdc_bootstrap_processwrapper_LockdownBrowserWrapper_Get_1Blacklist_1Process_1No
  (JNIEnv *env, jclass obj) {

              int a;
              a = system("sh processcheck.sh");
	      a= a/256;
              return a;    	
}


