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

// #define MAX_FILENAME_LENGTH 300
// FILE *log_file;

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
 
//	char log_filename[MAX_FILENAME_LENGTH];
    
    int n_students= 2;
	int n;
	int testLength = 50;
	int itemID;
	double theta, SEM=0.0;
	int rwo = 1;
	int condition_code = 0;

	int NEXT_ITEM_FAILURE = -999;
	int END_ITEM = -1;
	int SETUP_CATok = 0;

	for ( k = 0; k < n_students; k++) {
       
       condition_code = setup_cat("MC");

	   if (condition_code != SETUP_CATok) {
	       printf("Error: setup_cat failed! \n");
	       return 1; 
	   }

	   printf("Item#,   ItemID,   rwo,   theta  \n");

	   n = 0;
	   itemID = 0;
	   while (itemID != END_ITEM) {
           itemID = next_item(); 
		   n++;
           if (itemID == NEXT_ITEM_FAILURE) {
                printf("Error: adapt item # %d failed! \n", n);
		        setoff_cat();  
	            return 1;
			}			
		 
            if (itemID != END_ITEM) {
			    rwo = randomNoBetween(0,1); // get raw score for item n by comparing student answer with key.
		                               // if (right) rwo = 1 else rwo = 0;

		        set_rwo(rwo);  // rwo = 0 or 1
		        theta = score();
				printf("%d,   %d,   %d,   %6.2lf \n", n, itemID, rwo, theta);
			}
	    }
	    n--;
        // n is the test Length

	   // final theta with SEM print out there for student # k;
	   SEM = getSEM(theta);
       printf(" student #,   theta,    SEM   \n");
       printf(" %d,  %6.2lf,  %8.3lf \n", k, theta, SEM);  // report student scale score with SEM after test.

	   setoff_cat();  
	}

    return 1;
}
