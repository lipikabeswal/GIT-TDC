#include <stdio.h>
#include "jni.h"
#include "LockdownBrowserWrapper.h"
#include <stdlib.h>


JNIEXPORT jint JNICALL Java_com_ctb_tdc_bootstrap_processwrapper_LockdownBrowserWrapper_Get_1Blacklist_1Process_1No
  (JNIEnv *env, jclass obj) {

              int a;
              a = system("sh macpcheck.sh");
	      a= a/256;
              return a;    	
}





