#include <stdio.h>
#include <jni.h>
#include "LockdownBrowserWrapper.h"


JNIEXPORT void JNICALL Java_com_ctb_tdc_bootstrap_processwrapper_LockdownBrowserWrapper_Hot_1Keys_1Enable_1Disable
  (JNIEnv *env, jclass obj, jboolean b_ENABLE_DISABLE) {


	if(!b_ENABLE_DISABLE)
		{
			system("gcc -g -o disablekeysobj disablekeys.c ");
			system("./disablekeysobj");
		}
	else
		{
			system("gcc -g -o enablekeysobj enablekeys.c");
			system("./enablekeysobj");
		}
}

JNIEXPORT jboolean JNICALL Java_com_ctb_tdc_bootstrap_processwrapper_LockdownBrowserWrapper_Process_1Check
  (JNIEnv *env, jclass obj) {
	
	int a; 
	a = system("sh processcheck.sh");
        a = a/256;
        return a;
      	
}

JNIEXPORT void JNICALL Java_com_ctb_tdc_bootstrap_processwrapper_LockdownBrowserWrapper_kill_1printscreen_1snapshot
  (JNIEnv *env, jclass obj) {


	system("sh disable_printscreen.sh");

}


