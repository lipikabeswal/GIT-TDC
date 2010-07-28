#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include "jni.h"
#include "LockdownBrowserWrapper.h"

	char * modifyLine (char *,int);

	char  * replace_str ( char  *, char  *, char  *);

	int isContain (char *);

	void writeFile (void);

	char* hotKeys[] = {"F1","Alt_L","Alt_R","F5","F10","F12","Print Sys_Req","F3","Escape","Print Execute","Print","Sys_Req","Execute"};
	char array2[400][400];

	static const char filename1[] = "xmodmap_original";

JNIEXPORT void JNICALL Java_com_ctb_tdc_bootstrap_processwrapper_LockdownBrowserWrapper_Hot_1Keys_1Enable_1Disable
  (JNIEnv *env, jclass obj, jboolean b_ENABLE_DISABLE) {


	if(!b_ENABLE_DISABLE)
		{
			//system("gcc -g -o disablekeysobj disablekeys.c ");
			//system("./disablekeysobj");
			
			system("chmod 777 wmctrl");
			system("xmodmap -pke > xmodmap_original");
			xmod();
			system("xmodmap xmodmap_modified");
			system("xmodmap -e \"pointer = 1 9 8 7 6 5 4 3 2\"");
			system("xmodmap -e \"keycode 0x09 = \"");
			system("xmodmap -e \"keycode 107 = \"");
			system("xmodmap -e \"keycode 218 = \"");
			system("killall -q -9 gnome-panel-screenshot");
			system("killall -q -9 gnome-screenshot");
			system("killall -q -9 evolution-exchange-storage");
			system("sh processcheck.sh");
			system("xset -r 107");
			//system("metacity --replace");
			//system("gconftool-2 -s -t int /apps/compiz/general/screen0/options/number_of_desktops 1");
		}
	else
		{
			//system("gcc -g -o enablekeysobj enablekeys.c");
			//system("./enablekeysobj");

			system("xmodmap xmodmap_original");
			system("xmodmap -e \"pointer = 1 2 3 4 5 6 7 8 9\"");
			system("xmodmap -e \"keycode 0x09 = Escape\"");
			system("xmodmap -e \"keycode 107 = Print Sys_Req\"");
			system("xmodmap -e \"keycode 218 = Print\"");
			//system("gconftool-2 -s -t int /apps/compiz/general/screen0/options/number_of_desktops 2");
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


	system("killall -q -9 gnome-panel-screenshot");
	system("killall -q -9 gnome-screenshot");

}


JNIEXPORT void JNICALL Java_com_ctb_tdc_bootstrap_processwrapper_LockdownBrowserWrapper_Rightclick_1Enable_1Disable
  (JNIEnv *env, jclass obj, jboolean b_ENABLE_DISABLE) {

	if (!b_ENABLE_DISABLE)
		{
			system("xmodmap -e \"pointer = 1 6 7 8 9\"");
		}
	
	else
		{
			system("xmodmap -e \"pointer = 1 2 3 4 5\"");
		}
}


int xmod() {



	FILE *file1 = fopen(filename1,"r");







	char line[400];

	char *findpattern;



	int i,j;

	for(i=0; i<400; i++)

	{

		for(j=0; j<400; j++)

		{

			array2[i][j] = '\0';

		}

	}



	for(i=0; i<400; i++)

	{

		line[i] = '\0';

	}



//if((file = fopen( "..\\Output\\sample.txt", "r" )) != NULL )

	if ( file1 != NULL )

	{

		i=0;



		while( fgets( line, sizeof line, file1 ) != NULL)

		{

			strcpy(array2[i],line);

            i++;

		}

		fclose(file1);



	 }



	for (i = 0; i < 400; i++)

	{

		if ((j = isContain (array2[i])) != -1)

		 {

			findpattern = modifyLine (array2[i],j);

			strcpy(array2[i],findpattern);

		 }



	}



	writeFile ();

	return 0;

}

/*

 * F1 XF86_Switch_VT_1,Control_R,Control_L,Tab,Alt_L Meta_L,

 * Alt_R Meta_R,F3 XF86_Switch_VT_3

 *
 */

char * modifyLine (char *lineArray,int j) {



		lineArray = replace_str (lineArray,hotKeys[j],"");

		return lineArray;



}



char *  replace_str ( char  *str, char  *orig, char  *rep)

{

  static   char  buffer[4096];

  char  *p;

   //printf("orig:%s\n",orig);

   //printf("str1:%s\n",str);

  if (!(p = strstr(str, orig)))   // Is 'orig' even in 'str'?

    return  str;



   strncpy(buffer, str, p-str); // Copy characters from 'str' start to 'orig' st$



  //printf("buffer1:%s\n",buffer);

   buffer[p-str] = '\0' ;



  return  buffer;

}



int isContain (char *lineArray)

{

	int i;

	for (i = 0; i < 7; i++)

	{

		if (strstr(lineArray,hotKeys[i]))

		{

			return i;

		}

	 }

	return -1;

}



void writeFile ()

{

	FILE *file2 = fopen("xmodmap_modified","w");

	int i;

	for (i = 0; i < 400;i++) {



		fprintf (file2,"\n%s\n",array2[i]);

	}

	fclose(file2);



}
