/*
 * Main Program to implement TABE 9&10 CAT.
 * Created: Q3 2010
 * Author: Lianghua Shu
 */

#include <stdio.h>
#include <string.h>

/* Includes required by the cat C API */
#include "cat.h"
#include "genRandomDis.h"   // for randomNoBetween, so it can be commented out

#define MAX_FILENAME_LENGTH 300

FILE *log_file;

/*
 *      main -- driver routine for cat prototype
 *      (1) setup_cat(content, level, testLength); 
 *      (2) itemID = adapt_n_item(n);
 *      (3) set_rwo(rwo); // rwo = 0,1 -- raw score of above item
 *      (4) theta = score(&SEM); // theta ability score with SEM after n item test;
 *      (5) repeat (2) to (4) until n = testLength;
 *      (6) print final theta ability score with SEM after n = testLength;
 *      (7) setoff_cat();
 */
int main() {
	int k;
 
	char log_filename[MAX_FILENAME_LENGTH];
    
    int n_students= 3;
	int n;
	int testLength = 50;
	int itemID;
	double theta, SEM=0.0;
	int rwo = 1;
	
	char versionInfo[]="TABE CAT version 1.0 (Build 001), 08/17/2010.";

	strcpy(log_filename, ".\\Data\\RD.log");
    
	log_file = fopen(log_filename, "w");
  
    if (log_file == NULL) {
        printf ("Error: Couldn't open log file: %s \n", log_filename);
        return 1;
    }

    fprintf(log_file, " %s \n", versionInfo);
    
	for ( k = 0; k < n_students; k++) {
       
       setup_cat("RD", 'M');
       testLength = getTestLength();
	   // printf(" ItemID,   rwo,   theta  \n");
	   for ( n = 0; n < testLength; n++) {
           itemID = adapt_n_item(n);

		   rwo = randomNoBetween(0,1); // get raw score for item n by comparing student answer with key.
		                               // if (right) rwo = 1 else rwo = 0;

		   set_rwo(rwo);  // rwo = 0 or 1
         
		   theta = score(); // student scale score after taking n item test.
	     // 	 printf(" %d,   %d,   %6.2lf,  %8.3lf \n", itemID, rwo, theta);
	   } 
	   // final theta with SEM print out there for student # k;
	   SEM = getSEM(theta);
       printf(" student #,   theta,    SEM   \n");
       printf(" %d,  %6.2lf,  %8.3lf \n", k, theta, SEM);  // report student scale score with SEM after test.

	   setoff_cat();  
	}

    printf(" \n");
    fclose(log_file);
    return 1;
}

