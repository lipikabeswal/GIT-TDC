#include <stdio.h>

#include <stdlib.h>

#include <string.h>

#include <ctype.h>



char * modifyLine (char *,int);

char  * replace_str ( char  *, char  *, char  *);

int isContain (char *);

void writeFile (void);

char* hotKeys[] = {"F1","Control_R","Control_L","Alt_L","Alt_R","F5","F10","F12","Print Sys_Req","F3","Escape","Print Execute"};
char array2[400][400];

int main()

{

	static const char filename1[] = "xmodmap_original";



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




