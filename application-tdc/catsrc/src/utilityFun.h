/* Author: Lianghua Shu
 
 */

#ifndef UTLITYFUN_H
#define UTLITYFUN_H

/* Get number of lines in data file  */

char *getStrByIndex(char inStr[], int begin_index, int end_index);
char *changeFileNameExtension(char inFileName[], char newExtension[]);
char *getFileNameExtension(char inFileName[]);
char *toUpperStr(char *str);
#endif

