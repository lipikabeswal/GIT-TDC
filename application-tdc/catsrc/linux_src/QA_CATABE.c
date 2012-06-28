/*
 * Main Program to implement TABE 9&10 CAT.
 * Created: Q3 2010
 * Author: Lianghua Shu
 */

#include <stdio.h>
#include <string.h>
// #include "windows.h" // NOT necessary

/* Includes required by the cat C API */
#include "cat.h"
#include "genRandomDis.h"   // for randomNoBetween, so it can be commented out
// #pragma comment(lib, "CATABE.lib")  // Not necessary

/*
__declspec(dllimport) int setup_cat(char subTest[]);  // Not necessary
*/

#define MAX_FILENAME_LENGTH2 300

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
int main( int argc, char *argv[] ) {
		int i, k,j;
		 /* examinee response vector as a string and as an integer array */
    char *response_string = NULL;
	int *item_response = NULL;
	char subTest[3];
	char data_filename[MAX_FILENAME_LENGTH2];
	char output_filename[MAX_FILENAME_LENGTH2];
	FILE  *dat_file, *output_file;
	char datfile_line[35];
//	int getCurrentline(char *line, int max, FILE *fp);
//	char log_filename[MAX_FILENAME_LENGTH2];
    
    int n_students= 1;
	int n;
	int testLength = 50;
	int itemID;
	double theta, SEM=0.0;
	int rwo = 1;
	int condition_code = 0;

	int NEXT_ITEM_FAILURE = -999;
	int END_ITEM = -1;
	int SETUP_CATok = 0;

	int tot_n_objs = 0;
	int obj_id = 0;
	double obj_score = 0.0;
	double obj_SSsem = 0.0;
	char obj_lvl;
	int obj_masteryLvl;
	int obj_rs = 0;
	int totObj_rs = 0;

 //   HINSTANCE hInstLibrary = LoadLibrary(TEXT("CATABE.dll"));  // Not necessarystrcpy(par_filename, argv[1]);
// get par and loss/hoss from par here

	strcpy(subTest, argv[1]);
// get par and loss/hoss from par here
	strcpy(data_filename, argv[2]);
	strcpy(output_filename, argv[3]);

	dat_file = fopen(data_filename, "r");
    if (dat_file == NULL) {
        printf("Error: Couldn't open dat file %s. \n", data_filename);
        return 1;
    }
	output_file = fopen(output_filename, "w");
    if (output_file == NULL) {
            printf("Error: Couldn't open output file %s. \n", output_filename); 
            return 1;
	}
	
	k = 0;
    while ( getCurrentline(datfile_line, 35, dat_file) > 0 ) {
            
//	for ( k = 0; k < n_students; k++) {
       
       condition_code = setup_cat(subTest);

	   if (condition_code != SETUP_CATok) {
	       printf("Error: setup_cat failed! \n");
	       return 1; 
	   }

	   /*
	   condition_code = checkPsgID();  // to check duplicated passageID in item bank
	   if (condition_code != PSGID_OK) {
	       printf("Error: duplicated passageID found! \n");
	       return 1; 
	   }
	   */

	   testLength = getTestLength();
	   fprintf( output_file, "testLength = %d \n", testLength);
	   fprintf(output_file, "Item#,   ItemID,   rwo,   theta  \n");
	   item_response = (int *) malloc(testLength * sizeof(int));
       response_string = (char *) malloc((testLength+1) * sizeof(char));
       if (response_string == NULL || item_response == NULL) {
          printf("Allocation failed for response buffer.\n"); 
          return 1;
       }

	   // Extract the response from the line dat file 
	   strcpy(response_string, strtok(datfile_line," \n"));
	   // Convert character responses to integer responses 
        for (i = 0; i < testLength; i++) {	
                // Otherwise, assume the response character is
                // a digit character, and convert it to the
                // corresponding integer 
                item_response[i] = response_string[i]-'0';
        }

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
			//    rwo = randomNoBetween(0,1); // get raw score for item n by comparing student answer with key.
		                               // if (right) rwo = 1 else rwo = 0;
				rwo = item_response[n-1];
		        set_rwo(rwo);  // rwo = 0 or 1
		        theta = score();
				fprintf(output_file, "%d,   %d,   %d,   %6.2lf \n", n, itemID, rwo, theta);
			}
	    }
	    n--;
        // n is the test Length

	   // final theta with SEM print out there for student # k;
	   SEM = getSEM(theta);
       fprintf(output_file, " student #,   theta,    SEM   \n");
       fprintf(output_file, " %d,  %6.2lf,  %8.3lf \n", (k+1), theta, SEM);  // report student scale score with SEM after test.

	   tot_n_objs = get_nObj();  // number of collapsed objectives
	   fprintf(output_file, " Obj #, Obj_ID, rs, tot_rs, SS, sem, level, Mastery_Level   \n");
	   for ( j = 0; j < tot_n_objs; j++) {
		   obj_id = get_objID(j);
		   obj_lvl = get_objLvl(theta);  // E, M, D, A
		   obj_score = get_objScaleScore(obj_lvl, obj_id);
		   if (obj_score > 0) {
			   obj_SSsem = get_objSSsem(obj_score, obj_id);
		       obj_rs = get_objRS();
			   totObj_rs = get_totObjRS();
		       obj_masteryLvl = get_objMasteryLvl(obj_score, obj_id, obj_lvl); // 0 = Non-Mastery, 1=Partial-Mastery, 2=Mastery
               if (obj_masteryLvl < 0) 
				   fprintf(output_file, "Error: invalid objective level call. \n");
			   else
			       fprintf(output_file, " %d,  %d,  %d,  %d, %8.2lf, %8.2lf,  %c,  %d \n", (j+1), obj_id, obj_rs, totObj_rs, obj_score, obj_SSsem, obj_lvl, obj_masteryLvl);
		   }
		   else {}  // not report objective score
	   }
	   setoff_cat();  
	   free(item_response);
       free(response_string);
	   k++;
	}

	fclose(dat_file); 
    fclose(output_file);

    return 1;
}

/*
int getCurrentline(char *line, int max, FILE *fp) {
  if(fgets(line, max, fp) == NULL)
    return 0;
  else
    return strlen(line);
} */

