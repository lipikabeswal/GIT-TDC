/* Author: Lianghua Shu
 * read par file.
 * Exhaustive Search for QA4irt Freedom version 1.0 (ESF). 
 * Created: 2-19-03
 * Modified: 2-21-03 to test Grid Search
 * Updated: April-May, 2003 for ESF 1.0
 * Modified: Aug. 2003 
 */

#include <stdio.h>
#include <string.h>
#include <stdlib.h> /* atof */
#include <ctype.h>


#include "getPar.h"

extern FILE *log_file;

/* getline: read aline, return length */
int getline(char *line, int max, FILE *fp) {
  if(fgets(line, max, fp) == NULL)
    return 0;
  else
    return strlen(line);
}

/* read TABE item bank file added Q3 2010 */
int getItems (char inPar[], struct item_info items[], int n_items, int *n_obj, int objID[], 
			  int n_obj_items[]) {

  FILE *fp;

  /* for getPar file */
  int  i, lineLen_max = 120;
  double ad, b, c3;
  char aLine[120];
  int lineLen = 0, n_line = 0;
  int n_objs;

  fp = fopen(inPar, "r");	
  
  if ( fp  ==  NULL ) {
    fprintf (log_file, "Couldn't open par file: %s \n", inPar);
    return  GETPAR_FAILURE;
  }
  
  /* get max line length from all lines in par file */
    
  i = 0;
  n_objs = 0;
  fgets (aLine, lineLen_max,fp); // get the first line

  /* 
   * lineLen_max must be >= the line width of par files ***below read items@todo
   */
 	   
  while (getline (aLine, lineLen_max, fp) > 1 ) {
	
	items[i].item_no = i+1;

     /* for MC items */
      strtok(aLine,",");  /* skip first obs */
      items[i].item_id = atoi(strtok(NULL,",")); 
      items[i].level_id = strtok(NULL,",")[1];  //note
	  items[i].answer_key = atoi(strtok(NULL,",")); 
      items[i].obj_id = atoi(strtok(NULL,",")); 

	  items[i].adm_flag = 1;
      items[i].rwo = 9;  // initialize rwo to 9

//	  if ( i == 398) {
//		  i = i;
//	  }

      if (i == 0) {
		  objID[n_objs] = items[i].obj_id;
	  }
	  else if (( i > 0) & (items[i].obj_id != items[i-1].obj_id)){
		  n_objs++;
          objID[n_objs] = items[i].obj_id;
	  }
	  else {}

      n_obj_items[n_objs]++;

      strcpy(items[i].obj_title,strtok(NULL,",")); 
	  ad = atof(strtok(NULL,",")); 
	  b = atof(strtok(NULL,",")); 
	  c3 = atof(strtok(NULL,",\n"));   // note
	  /* convert 3PL to 2PPC metric */
        ad = 1.7*ad;
        b = ad*b;
          
      items[i].parameters = (double *) malloc(3*sizeof(double));
      if (items[i].parameters == NULL) {
		fprintf (log_file, "Error: malloc item[%d].parameters failed! \n", i);
        return  GETPAR_FAILURE;
      }	    
      items[i].parameters[0] = ad;
      items[i].parameters[1] = b;
      items[i].parameters[2] = c3;
 	  
    i++; /* number of items from par file */
	  
  }  /* end while */

  *n_obj = n_objs + 1;
    
  if (i != n_items) {
    fprintf (log_file, "Error: Number of items in Par are different from that in Dat or Rwo: %s \n", inPar);
    return GETPAR_FAILURE;
  }

  fclose(fp);
  return GETPAR_OK;
  /*  end of read Par file */              
}

int getSSstats (char inFileByLvl[], char inFileByCnt[], char subTest[], char testLevel, double ssSts[]){
  FILE *fp;
//  FILE *fp2;
  int lineLen_max = 100;
  int  i;
  char aLine[100];  
  char content[2];
  char level;

  fp = fopen(inFileByLvl, "r");	
  
  if ( fp  ==  NULL ) {
    fprintf (log_file, "Couldn't open file: %s \n", inFileByLvl);
    return  GETPAR_FAILURE;
  }

  if (!((testLevel == 'L') | ( testLevel == 'E') |
	 ( testLevel == 'D') | (testLevel ==  'A') | (testLevel ==  'T')))
     testLevel = 'M';       // use 'M' if no testLevel inputs
  
  fgets (aLine, lineLen_max,fp); // get the first line
	   
  while (getline (aLine, lineLen_max, fp) > 1 ) {
     strcpy(content, strtok(aLine,","));  
     level = strtok(NULL,",")[0];  // note 
     if ( (!strcmp(content, subTest)) & ( level == testLevel )) {
       //  strtok(NULL,",");  // skip N
	     for (i = 0; i < 4; i++) 
		     ssSts[i] = atof(strtok(NULL,",\n")); 
		 fclose(fp);
	     return GETSS_STATS_OK;
	 }	 
  }
  
  /*
  // to do : may use 'M' if no testLevel
  fp2 = fopen(inFileByCnt, "r");	
  fgets (aLine, lineLen_max,fp2); // get the first line
	   
  while (getline (aLine, lineLen_max, fp2) > 1 ) {
     strcpy(content, strtok(aLine,","));  
     if ( !strcmp(content, subTest)) {
         strtok(NULL,",");  // skip N
	     for (i = 0; i < 4; i++) 
		     ssSts[i] = atof(strtok(NULL,",\n")); 
	 }	 
  }
  fclose(fp2); 
  */

  return GETSS_STATS_FAILURE;
}

int getNadmObj(char inFileNoItemObj[], char subTest[], char testLevel, int n_adm_obj[]){
  FILE *fp;
  int lineLen_max = 100;
  int  i, j, n_skip;
  char aLine[100];  
  char content[2];

  fp = fopen(inFileNoItemObj, "r");	
  
  if ( fp  ==  NULL ) {
    fprintf (log_file, "Error: Couldn't open file: %s \n", inFileNoItemObj);
    return  GETN_ADMOBJ_FAILURE;
  }

  for (i = 0; i < 6; i++) {
     fgets (aLine, lineLen_max,fp); // skip the first 6 lines
  }
  
  j = 0;
  while (getline (aLine, lineLen_max, fp) > 1 ) {
     strcpy(content, strtok(aLine,","));  
     if ( !strcmp(content, subTest)) {
		 switch(testLevel){
		 case 'L': n_skip = 2; break;
		 case 'E': n_skip = 3; break;
         case 'M': n_skip = 4; break;
		 case 'D': n_skip = 5; break;
		 case 'A': n_skip = 6; break;
		 case 'T': n_skip = 7; break;
		 default : n_skip = 4; break;   //use 'M' if no testLevel  
		 }
         for (i = 0; i < n_skip; i++) 
            strtok(NULL,",");  // skip N
         
		 n_adm_obj[j] = atoi(strtok(NULL,",\n"));
         j++;
	 }
	 else if (j >0) break;
  }

  fclose(fp);
  return GETN_ADMOBJ_OK;

}


int get_n_items(char inPar[]){

  FILE *fp;
  int lineLen_max = 150, n_line = 0;
  char aLine[150];
  
 
  fp = fopen(inPar, "r");	
  
  if ( fp  ==  NULL ) {
    fprintf (log_file, "Error: Couldn't open file: %s in get_n_items. \n", inPar);
    return  GET_NITEM_FAILURE;
  }

  fgets (aLine, lineLen_max,fp); // get the first line

  while (getline (aLine, lineLen_max, fp) > 1 ) 
	n_line ++;
  
  fclose(fp);
  return n_line;
}