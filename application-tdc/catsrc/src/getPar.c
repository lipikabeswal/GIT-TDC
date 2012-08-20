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
#include "sort_index.h"

extern FILE *log_file;

/* getCurrentline: read aline, return length */
/*Renamed getline function to getCurrentline while removing windows dependency*/
int getCurrentline(char *line, int max, FILE *fp) {
  if(fgets(line, max, fp) == NULL)
    return 0;
  else
    return strlen(line);
}

/* read TABE item bank file added Q3 2010 */
int getItems (char inPar[], struct item_info items[], int n_items, int *n_obj, int objID[], 
			  int n_obj_items[], int *n_new_obj, int new_objID[]) {

  FILE *fp;

  /* for getPar file */
  int  i, j;
  double ad, b, c3;
  char aLine[lineLen_max];
  int lineLen = 0, n_line = 0;
  int n_objs;
  int n_new_objs; /* collpased obj  */
  int *indx;
  int *tmp;

  fp = fopen(inPar, "r");	
  
  if ( fp  ==  NULL ) {
    if (LOG_FILE_FLAG) fprintf (log_file, "Couldn't open par file: %s \n", inPar);
    return  GETPAR_FAILURE;
  }
  
  /* get max line length from all lines in par file */
    
  i = 0;
  n_objs = 0;
  n_new_objs = 0;
  fgets (aLine, lineLen_max,fp); /* get the first line */

  /* 
   * lineLen_max must be >= the line width of par files ***below read items@todo
   */
 	   
  while (getCurrentline (aLine, lineLen_max, fp) > 1 ) {
	
	items[i].item_no = i+1;

     /* for MC items */
      strtok(aLine,",");  /* skip first obs */
      items[i].item_id = atoi(strtok(NULL,",")); 

	  
	  ad = atof(strtok(NULL,",")); 
	  b = atof(strtok(NULL,",")); 
	  c3 = atof(strtok(NULL,",\n"));   /* note */
	  /* convert 3PL to 2PPC metric */
        ad = 1.7*ad;
        b = ad*b;
          
      items[i].parameters = (double *) malloc(3*sizeof(double));
      if (items[i].parameters == NULL) {
		if (LOG_FILE_FLAG) fprintf (log_file, "Error: malloc item[%d].parameters failed! \n", i);
        return  GETPAR_FAILURE;
      }	    
      items[i].parameters[0] = ad;
      items[i].parameters[1] = b;
      items[i].parameters[2] = c3;

      items[i].obj_id = atoi(strtok(NULL,",")); 
	  items[i].new_obj_id = atoi(strtok(NULL,","));  /* collapsed objective IDs */

	  items[i].org_psg_id = atoi(strtok(NULL,","));  /* read original CTB_Passage_ID for data QA purpose */

      items[i].psg_id = atoi(strtok(NULL,","));  /* read new ctb_passage_id */
	  items[i].item_order = atoi(strtok(NULL,","));
	  items[i].enemy = atoi(strtok(NULL,",")); /* read enemy item, put enemy after item order in item bank */
	  strcpy(items[i].obj_title,strtok(NULL,",")); 	  

	//  printf(" %d %d %d %d \n", items[i].item_id, items[i].obj_id, items[i].psg_id,  items[i].item_order ); 

	  items[i].adm_flag = 1;
      items[i].rwo = 9;  /* initialize rwo to 9 */

      if (i == 0) {    /* item bank must be sorted by obj_id */
		  objID[n_objs] = items[i].obj_id;
	  }
	  else if (( i > 0) && (items[i].obj_id != items[i-1].obj_id)){
		  n_objs++;
          objID[n_objs] = items[i].obj_id;
	  }
	  else {}

      n_obj_items[n_objs]++;

	  if (i == 0) {     /* note duplicated new_objID is possible as item bank was not sorted by new_ obj_id */
		  new_objID[n_new_objs] = items[i].new_obj_id;
	  }
	  else if (( i > 0) && (items[i].new_obj_id != items[i-1].new_obj_id)){
		  n_new_objs++;
          new_objID[n_new_objs] = items[i].new_obj_id;
	  }
	  else {}

    i++; /* number of items from par file */
	  
  }  /* end while */

  *n_obj = n_objs + 1;
  if (i != n_items) {
    if (LOG_FILE_FLAG) fprintf (log_file, "Error: Number of items in Par are different from that in : %s \n", inPar);
    return GETPAR_FAILURE;
  }

  n_new_objs++;

  rmdup(new_objID, &n_new_objs);   /* removed duplicated new_objID */
  indx = (int *) malloc(n_new_objs*sizeof(int));
  tmp = (int *) malloc(n_new_objs*sizeof(int));
  indInt(n_new_objs, new_objID, indx);  /* sort new obj ID */

  for (i = 0; i < n_new_objs; i++) {
	  tmp[i] = new_objID[indx[i]];
  }
  for (i = 0; i < n_new_objs; i++) {
	  new_objID[i] = tmp[i];
  }

  *n_new_obj = n_new_objs;

  /*
  for (i = 0; i < n_new_objs; i++) {
	  printf("%d   %d \n ", i, new_objID[i]) ;
  } */

  free(indx);
  free(tmp);
  fclose(fp);
  return GETPAR_OK;
  /*  end of read Par file */              
}

int getSSstats (char inFileByLvl[], char inFileByCnt[], char subTest[], char testLevel, double ssSts[]){
  FILE *fp;
  int  i;
  char aLine[lineLen_max];  
  char content[2];
  char level;

  fp = fopen(inFileByLvl, "r");	
  
  if ( fp  ==  NULL ) {
    if (LOG_FILE_FLAG) fprintf (log_file, "Couldn't open file: %s \n", inFileByLvl);
    return  GETPAR_FAILURE;
  }

  if (!((testLevel == 'L') || ( testLevel == 'E') ||
	 ( testLevel == 'D') || (testLevel ==  'A') || (testLevel ==  'T')))
     testLevel = 'M';       /* use 'M' if no testLevel inputs */
  
  fgets (aLine, lineLen_max,fp); /* get the first line */
	   
  while (getCurrentline (aLine, lineLen_max, fp) > 1 ) {
     strcpy(content, strtok(aLine,","));  
     level = strtok(NULL,",")[0];  
     if ( (!strcmp(content, subTest)) && ( level == testLevel )) {
       /*  strtok(NULL,",");  // skip N */
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
	   
  while (getCurrentline (aLine, lineLen_max, fp2) > 1 ) {
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
  int  i, j, n_skip;
  char aLine[lineLen_max];  
  char content[3];

  fp = fopen(inFileNoItemObj, "r");	
  
  if ( fp  ==  NULL ) {
    if (LOG_FILE_FLAG) fprintf (log_file, "Error: Couldn't open file: %s \n", inFileNoItemObj);
    return  GETN_ADMOBJ_FAILURE;
  }

  for (i = 0; i < 6; i++) {
     fgets (aLine, lineLen_max,fp); /* skip the first 6 lines */
  }
  
  j = 0;
  while (getCurrentline (aLine, lineLen_max, fp) > 1 ) {
     strcpy(content, strtok(aLine,","));  
     if ( !strcmp(content, subTest)) {
		 switch(testLevel){
		 case 'L': n_skip = 2; break;
		 case 'E': n_skip = 3; break;
         case 'M': n_skip = 4; break;
		 case 'D': n_skip = 5; break;
		 case 'A': n_skip = 6; break;
		 case 'T': n_skip = 7; break;
		 default : n_skip = 4; break;   /* use 'M' if no testLevel  */
		 }
         for (i = 0; i < n_skip; i++) 
            strtok(NULL,",");  /* skip N */
         
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
  int n_line = 0;
  char aLine[lineLen_max];
  
 
  fp = fopen(inPar, "r");	
  
  if ( fp  ==  NULL ) {
    if (LOG_FILE_FLAG) fprintf (log_file, "Error: Couldn't open file: %s in get_n_items. \n", inPar);
    return  GET_NITEM_FAILURE;
  }

  fgets (aLine, lineLen_max,fp); /* get the first line */

  while (getCurrentline (aLine, lineLen_max, fp) > 1 ) 
	n_line ++;
  
  fclose(fp);
  return n_line;
}

/* read TABE item bank file added Q3 2010 */
int getFT_Items (char inPar[], struct item_info items[], int n_items, int n_FTitemsLvl[]) {

  FILE *fp;

  /* for getPar file */
  int  i,j;
  double ad, b, c3;
  char aLine[lineLen_max];
  int lineLen = 0, n_line = 0;
  int n_objs;

  fp = fopen(inPar, "r");	
  
  if ( fp  ==  NULL ) {
    if (LOG_FILE_FLAG) fprintf (log_file, "Couldn't open par file: %s \n", inPar);
    return  GETPAR_FAILURE;
  }
  
  /* get max line length from all lines in par file */
    
  i = 0;
  n_objs = 0;
  fgets (aLine, lineLen_max,fp); /* get the first line */

  /* 
   * lineLen_max must be >= the line width of par files ***below read items@todo
   */
 	   
  while (getCurrentline (aLine, lineLen_max, fp) > 1 ) {
	
	items[i].item_no = i+1;

     /* for MC items */
      strtok(aLine,",");  /* skip first obs */
	  for (j = 0; j < 7; j++) {  /* skip another 7 obs */
         strtok(NULL,","); 
      }
      items[i].item_id = atoi(strtok(NULL,",")); 

	  /*
	  ad = atof(strtok(NULL,",")); 
	  b = atof(strtok(NULL,",")); 
	  c3 = atof(strtok(NULL,",\n"));   // note
	  
      items[i].obj_id = atoi(strtok(NULL,",")); 
	  strtok(NULL,",");  // skip CTB_Passage_ID 

      items[i].psg_id = atoi(strtok(NULL,","));  // read new ctb_passage_id 
	  items[i].item_order = atoi(strtok(NULL,","));
	  strcpy(items[i].obj_title,strtok(NULL,",")); 
	  items[i].level = strtok(NULL,",")[0];
	  */
	  items[i].obj_id = 9999;
	  items[i].psg_id = 0;   // non passage item
	  items[i].item_order = 0;  // non passage item

/*	  printf(" %d %d %d %d \n", items[i].item_id, items[i].obj_id, items[i].psg_id,  items[i].item_order ); */

	  items[i].adm_flag = 1;
      items[i].rwo = 9;  /* initialize rwo to 9 */

	  /*
	  switch(items[i].level){
		 case 'L': ++n_FTitemsLvl[0]; break;  //@todo to match the real levels in FT item bank
		 case 'E': ++n_FTitemsLvl[0]; break;  // current set up for level, E, M, D, A only.
         case 'M': ++n_FTitemsLvl[1]; break;  //                           0, 1, 2, 3 
		 case 'D': ++n_FTitemsLvl[2]; break;
		 case 'A': ++n_FTitemsLvl[3]; break;
		 case 'T': ++n_FTitemsLvl[3]; break;
		 default : fprintf (log_file, "Error: item level for item %d is NOT found in FT item bank: %s \n", i, inPar); break;    
		 }
		 */

    i++; /* number of items from par file */
	  
  }  /* end while */

  if (i != n_items) {
    if (LOG_FILE_FLAG) fprintf (log_file, "Error: Number of items in Par are different from that in Dat or Rwo: %s \n", inPar);
    return GETPAR_FAILURE;
  }

  fclose(fp);
  return GETPAR_OK;
  /*  end of read Par file */              
}

int get_LH4lvl(char inFile[], char subTest[], double loss[], double hoss[]){
	 FILE *fp;
	 char aLine[lineLen_max];
	 char content[3];
     int  i;
  
     fp = fopen(inFile, "r");	
  
    if ( fp  ==  NULL ) {
        if (LOG_FILE_FLAG) fprintf (log_file, "Couldn't open par file: %s \n", inFile);
        return  GETPAR_FAILURE;
     }
  
     i = 0;
     fgets (aLine, lineLen_max,fp); /* get the first line */

  /* 
   * lineLen_max must be >= the line width of par files 
   */
 	   
     while (getCurrentline (aLine, lineLen_max, fp) > 1 ) {
	     strcpy(content, strtok(aLine,","));  
         if ( !strcmp(content, subTest)) {
			      strtok(NULL,","); 
				  loss[i] = atof(strtok(NULL,","));
				  hoss[i] = atof(strtok(NULL,","));
	              i++;
		 }
		 if (i >= 4) break;
	 }
	 fclose(fp);
	 return GETPAR_OK;
}

int get_objLvlCut(char inFile[], int objID[], struct objSScut objSS_cut[]) {
	 FILE *fp;
	 char aLine[lineLen_max];
     int  i, j;
	 int obj_id;
//	 char lvl[]="EMDA";
  
     fp = fopen(inFile, "r");	
  
     if ( fp  ==  NULL ) {
        if (LOG_FILE_FLAG) fprintf (log_file, "Couldn't open par file: %s \n", inFile);
        return  GETPAR_FAILURE;
     }
  
     i = 0;
	 j = 0;
     fgets (aLine, lineLen_max,fp); /* get the first line */

	 while (getCurrentline (aLine, lineLen_max, fp) > 1 ) {
		 obj_id = atoi(strtok(aLine,","));
		 if (obj_id == objID[i]) {   /* objID[] are sorted by number already  */
			      for (j = 0; j < 4; j++) 
				      objSS_cut[i].s75[j] = atof(strtok(NULL,","));
					  objSS_cut[i].objID = obj_id;
					  i++;
		 }
		 else {
			  if (LOG_FILE_FLAG) fprintf (log_file, "Error: objective ID mismatch in: %s \n", inFile);
              return  GETPAR_FAILURE;
		 }
	 }

	 fclose(fp);
	 return GETPAR_OK;
}