/* Author: Lianghua Shu
 * 
 * Aug, 2006
 */

#include <stdio.h>
#include <string.h>
#include <stdlib.h> /* atof */
#include <ctype.h>  /* toupper */

#include "utilityFun.h"


#define GET_LINELEN_FAILURE 1               /* Fatal errors  */


static int getCurrentline(char *line, int max, FILE *fp); 
static int get_maxLineLen (char inPar[]);

/* getline: read aline, return length */
static int getCurrentline(char *line, int max, FILE *fp) {
  if(fgets(line, max, fp) == NULL)
    return 0;
  else
    return strlen(line);
}

/* get max line length in par file */
static int get_maxLineLen (char inPar[]) {
  FILE *fp;
  int maxLineLen = 0;
  int inChar, lineLen = 0 ;
  
  fp = fopen(inPar, "r");	
  if (fp  ==  NULL) {
    printf ( "Error: Couldn't open par file. \n ");
    return GET_LINELEN_FAILURE;
  }

  do 
  {
    inChar = getc(fp);   
    lineLen++;
    if( inChar == '\n' || inChar == EOF ) {
      if( maxLineLen < lineLen ) maxLineLen = lineLen;
      lineLen = 0;
    }
 
  } while(inChar != EOF);    
  fclose(fp);

  return ++maxLineLen ; /* add 1 for \n  */
}

char *getStrByIndex(char inStr[], int begin_index, int end_index){

    int outStrLen = end_index - begin_index + 1;
	char *tmpStr=NULL;
	char *outStr=NULL;
	tmpStr = (char *) malloc(strlen(inStr) * sizeof(char));
    outStr = (char *) malloc(outStrLen * sizeof(char));

	strcpy(tmpStr, inStr);
	memmove(tmpStr, &tmpStr[begin_index], outStrLen ); /* move str to the beginning of tmpStr */
	strncpy(outStr,tmpStr,outStrLen);/* then copy it. */
    outStr[outStrLen] = '\0';
    
	return outStr;
}

/* change inFileName extension to newExtension */
char *changeFileNameExtension(char inFileName[], char newExtension[]){
 
	char *tmpStr = NULL;  
	tmpStr = (char *) malloc((strlen(inFileName) + 1) * sizeof(char));
    
	strcpy(tmpStr, inFileName); /* to keep inFileName after this call */
    strcpy(tmpStr, strtok(tmpStr,"."));
	if (tmpStr == NULL) {
		printf("Error: Invalid file name s% without . extension!", inFileName);
		exit(1);
	}

    return  strcat(tmpStr, newExtension); 
}

char *getFileNameExtension(char inFileName[]) {
 
	char *tmpStr = NULL;
    
	tmpStr = (char *) malloc((strlen(inFileName) + 1) * sizeof(char));    
	strcpy(tmpStr, inFileName); /* to keep inFileName after this call */

    strtok(tmpStr,".");
	if (tmpStr == NULL) {
		printf("Error: Invalid file name s% without . extension!", inFileName);
		exit(1);
	}

	return strtok(NULL," \0");
}

char *toUpperStr(char *str) {
  int i;
  int slen = strlen(str);

  for( i = 0; i < slen; i++) {
    str[ i ] = toupper( str[ i ] );
  }

  return str;
}