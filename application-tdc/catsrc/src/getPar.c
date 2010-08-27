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

//extern FILE *log_file;

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
  int  i;
  double ad, b, c3;
  char *aLine;
  int inChar, lineLen_max = 0, lineLen = 0, n_line = 0;
//  int nHoss, nLoss;
  extern int hasLossHossComm;
//  char obs[5];
  int n_objs;

  
  fp = fopen(inPar, "r");	
  
  if ( fp  ==  NULL ) {
    //fprintf (log_file, "Couldn't open par file: %s \n", inPar);
    return  GETPAR_FAILURE;
  }
  
  /* get max line length from all lines in par file */
    
  do
  {
	inChar = getc(fp);
    lineLen++;

    if( inChar == '\n' || inChar == EOF ) {
      if( lineLen_max < lineLen ) lineLen_max = lineLen;
      lineLen = 0;
	  n_line ++;
    }
  } while( (inChar != EOF) || (n_line == 3) ) ;
  
  lineLen_max++; /* add 1 for \n */
  rewind(fp);

  aLine = (char *) malloc(lineLen_max * sizeof(char));
  if( aLine == NULL ) {
    //fprintf (log_file, "malloc aLine failed in getPar \n");
    return  GETPAR_FAILURE;
  }
    
  i = 0;
  n_objs = 0;
  fgets (aLine, lineLen_max,fp); // get the first line

  /* 
   * lineLen_max must be >= the line width of par files ***below read items@todo
   */
 	   
  while (getline (aLine, lineLen_max, fp) > 1 ) {
	
	items[i].item_no = i+1;

     /* for MC items */

	/* does not work
      sscanf(aLine, "%s,%d,%c,%d,%s,%lf,%lf,%lf", 
	                obs, &items[i].item_id, items[i].level_id, &items[i].answer_key, 
					&items[i].obj_id, items[i].obj_title, &ad,&b,&c3 );
 */
      strtok(aLine,",");  /* skip first obs */
      items[i].item_id = atoi(strtok(NULL,",")); 
      items[i].level_id = strtok(NULL,",")[1];
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
	  c3 = atof(strtok(NULL,"\n")); 
	  /* convert 3PL to 2PPC metric */
        ad = 1.7*ad;
        b = ad*b;
          
      items[i].parameters = (double *) malloc(3*sizeof(double));
      if (items[i].parameters == NULL) {
        return  GETPAR_FAILURE;
      }	    
      items[i].parameters[0] = ad;
      items[i].parameters[1] = b;
      items[i].parameters[2] = c3;
 	  
    i++; /* number of items from par file */
	  
  }  /* end while */

  *n_obj = n_objs + 1;
  fclose(fp);
  free(aLine);
      
  if (i != n_items) {
    //fprintf (log_file, "Error: Number of items in Par are different from that in Dat or Rwo: %s \n", inPar);
    return GETPAR_FAILURE;
  }

  return GETPAR_OK;
  /*  end of read Par file */              
}
