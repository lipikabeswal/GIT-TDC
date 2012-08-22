/*
 * Main Program to implement TABE 9&10 CAT.
 * Created: Q3 2010
 * Author: Lianghua Shu
 */

#include <stdio.h>
#include <string.h>
// #include "windows.h"

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
	int k,j;
 
//	char log_filename[MAX_FILENAME_LENGTH];
    
    int n_students= 3;
	int n;
	int testLength = 50;
	int itemID;
	double theta, SEM=0.0;
	int rwo = 1;
	int condition_code = 0;

	int NEXT_ITEM_FAILURE = -999;
	int END_ITEM = -1;
	int SETUP_CATok = 0;
	double UnReportableScore = -1.0;

	int tot_n_objs = 0;
	int obj_id = 0;
	double obj_score = 0.0;
	double obj_SSsem = 0.0;
	char obj_lvl;
	int obj_masteryLvl;
	int obj_rs = 0;
	int totObj_rs = 0;
	int psgID = 0;

 //   HINSTANCE hInstLibrary = LoadLibrary(TEXT("CATABE.dll"));

	for ( k = 0; k < n_students; k++) {
       
       condition_code = setup_cat("LA");
	   
	   if (condition_code != SETUP_CATok) {
	       printf("Error: setup_cat failed! \n");
	       return 1; 
	   }

//	   setoff_cat();  
	   
	   condition_code = checkPsgID();  // to check duplicated passageID in item bank
	   if (condition_code != PSGID_OK) {
	       printf("Error: duplicated passageID found! \n");
	       return 1; 
	   }

	   testLength = getTestLength();
	   printf( "testLength = %d \n", testLength);
	   printf("Item#,   ItemID,   rwo,   theta  \n");

	   n = 0;
	   itemID = 0;
	   while (itemID != END_ITEM) {
           itemID = next_item(); 
		//   psgID = get_psgID();
		   n++;
           if (itemID == NEXT_ITEM_FAILURE) {
                printf("Error: adapt item # %d failed! \n", n);
		        setoff_cat();  
	            return 1;
			}		

/*		   if (n == 12) {
		       setoff_cat();  
		   }
*/		 
            if (itemID != END_ITEM) {
			    rwo = randomNoBetween(0,1); // get raw score for item n by comparing student answer with key.
		                               // if (right) rwo = 1 else rwo = 0;
				if (n == 20) 
					rwo = -9;   // test stops 
		        
				set_rwo(rwo);  // rwo = 0 or 1
					
		        theta = score();
		//		printf("%d,   %d,  %d,  %d,   %6.2lf \n", n, itemID, psgID, rwo, theta);
				printf("%d,   %d,  %d,   %6.2lf \n", n, itemID, rwo, theta);
			}
	    }
	    n--;
        // n is the test Length
/*		if (n != testLength ){
			printf("Error: test length n = %d, \n", n);
			return 0;
		}
*/
       if ( theta != UnReportableScore ) { /* -1.0 */
	       // final theta with SEM print out there for student # k;
	       SEM = getSEM(theta);
           printf(" student #,   theta,    SEM   \n");
           printf(" %d,  %6.2lf,  %8.3lf \n", k, theta, SEM);  // report student scale score with SEM after test.

	       tot_n_objs = get_nObj();  // number of collapsed objectives
	       printf(" Obj #, Obj_ID, rs, tot_rs, SS, sem, Mastery_Level   \n");
	       for ( j = 0; j < tot_n_objs; j++) {
		       obj_id = get_objID(j);
		       //   obj_lvl = get_objLvl(theta);  // E, M, D, A
		       obj_score = get_objScaleScore(obj_id);
		       if (obj_score > 0) {
			       obj_SSsem = get_objSSsem(obj_score, obj_id);
		           obj_rs = get_objRS();
			       totObj_rs = get_totObjRS();
		           obj_masteryLvl = get_objMasteryLvl(obj_score, obj_id); // 0 = Non-Mastery, 1= begin Mastery, 2=Partial-Mastery, 3=Mastery
                   if (obj_masteryLvl < 0)                                // 4 = Advanced Mastery 
				       printf("Error: invalid objective level call. \n");
			       else
			           printf(" %d,  %d,  %d,  %d, %8.2lf, %8.2lf,  %d \n", (j+1), obj_id, obj_rs, totObj_rs, obj_score, obj_SSsem, obj_masteryLvl);
		       }
		       else {}  // not report objective score
	       }
	    }
	   else {} // not report scale score
	   setoff_cat();  
	}

    return 1;
}
