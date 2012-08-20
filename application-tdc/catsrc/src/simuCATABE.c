/*
 * Main Program to implement TABE 9&10 CAT.
 * Created: Q3 2010
 * Author: Lianghua Shu
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <time.h>
#include "windows.h"

#include "cat.h"
#include "item.h"
#include "genRandomDis.h"
#include "getPar.h"

#define MAX_FILENAME_LENGTH 300
#define MAX_N_COMMANDS 10
#define STUDENT_ID_LENGTH 26

#define n_students 300  // simulation
#define qpts 101

FILE *logSimu_file;
FILE *bias_file;
FILE *objLvl_file;
int level2i(char lvl);
char i2level(int i);
/*
 *      main -- driver routine for grid search prototype
 *      (1) Sets up fields, (2) calls setup_grid_components,
 *      (3) prints intermediate results,
 *      (4) calls compute_ml_scale_score, and (5) prints final results.
 */
int main( int argc, char *argv[] ) {
	long i, j;
       	
	int condition_code;  /* Returned condition code  */

	char log_filename[MAX_FILENAME_LENGTH];
    char bias_filename[MAX_FILENAME_LENGTH];
	char objLvl_filename[MAX_FILENAME_LENGTH];
	
	int n;
	int testLength = 50;
	int nItems = 400; // simulation, total items in the bank
	int itemID = 0;
	double theta, SEM=0.0;
    double t10, SEM10=0.0;

	int rwo = 1;
	
	/* timing variables */
	clock_t start_clock, end_clock;
	double total_time;

	char versionInfo[]="TABE CAT version 1.01 (Build 002), 12/15/2010.";

	int k, kq;   // simulation studies
	double th[qpts]; 
	double et[n_students];
    double theta0;
    struct item_info *items = NULL;
	int n_obj;	
	int n_obj_adm[10];
    int n_obj_adm1[10];

	int objID[10];
	double bias = 0.0;
	double abs_bias = 0.0;
	double mse = 0.0;
	double se = 0.0;
	double ttmean = 0.0;
	double etmean = 0.0;
    
	double bias10 = 0.0;
	double mse10 = 0.0;
	double se10 = 0.0;
	double et10mean = 0.0;

	double cor = 0;
	int *n_adm = NULL;
	double* exposureRate= NULL;
	int n05 = 0;
	int n00 = 0;
	int n_rwo = 0;
	double overlapRate;
	int n_overlap = 0;
	int *RWO1 = NULL;
	int *RWO2 = NULL; 
	int stud1, stud2; 
	int raw_score = 0;
    
	int NEXT_ITEM_FAILURE = -999;
	int END_ITEM = -1;
	int SETUP_CATok = 0;
  //  char tmpC[2] = "";

    double loss = 235;
	double hoss = 755;
	char cont[] = "RD";

	char lvl = 'E';
	double n_obj_byLevel[6][10];
	int t_n_obj_byLevel[6][10];
	int n_qs_byLevel[6][10];

   strcpy(log_filename, "");
   strcat(log_filename, ".\\Data\\");
   strcat(log_filename, cont);
 //  tmpC[0] = '_';
 //  tmpC[1] = lvl;
 //  tmpC[2] = '\0';
 //  strcat(log_filename, tmpC);

   strcpy(bias_filename,log_filename); 
   strcpy(objLvl_filename,log_filename); 

   strcat(log_filename, "_bias.log");
   strcat(bias_filename, "_bias.txt");
   strcat(objLvl_filename, "_objLvl.txt");

	logSimu_file = fopen(log_filename, "w");
    bias_file = fopen(bias_filename, "w");
    objLvl_file = fopen(objLvl_filename, "w");

    if (logSimu_file == NULL) {
           printf ("Error: Couldn't open log file: %s \n", log_filename);
           return 1;
    }

    fprintf(logSimu_file, " %s \n", versionInfo);

	for ( i = 0; i < 6; i++ ) 
		for ( j = 0; j < 10; j++ ) {
	        n_obj_byLevel[i][j] = 0.0;
	        n_qs_byLevel[i][j] = 0;
			t_n_obj_byLevel[i][j] = 0;
	}

   if ( !strcmp("MC", cont)) {  // set stratify and adapt an item from the rate of n items with the best info
   //    _n_rate = 0.05;   // memo 8
       loss = 235;
       hoss = 755;
       testLength = 20;  
   }
   else if ( !strcmp("RD", cont)) {
       loss = 175;
       hoss = 812;  
       testLength = 25;  
   }
   else if ( !strcmp("LA", cont)) {
       loss = 235;
       hoss = 826;   
       testLength = 20;  //@todo should be max test length for all levels; then reset after 10 item test.
   }
   else {} //@todo

    for ( kq = 0; kq < qpts; kq++ ) {
		th[kq] = loss + (double) kq * (hoss - loss)/((double)qpts - 1.0);
//		printf(" %lf \n", th[kq]);
	}

	for ( kq = 0; kq < qpts; kq++ ) {

        condition_code = setup_cat(cont);

	    if (condition_code != SETUP_CATok) {
	       printf("Error: setup_cat failed! \n");
	       return 1; 
		}

        testLength = getTestLength(); //@todo should be max test length for all levels
	    nItems = getNumItems();	
		n_obj = getNumObj();
//  loss = getLoss();
//	hoss = getHoss();
        items = (struct item_info *) malloc(nItems * sizeof(struct item_info));
        n_adm = (int *) malloc(nItems * sizeof(int));
        exposureRate = (double *) malloc(nItems * sizeof(double));
        RWO1 = (int *) malloc(nItems * sizeof(int));
	    RWO2 = (int *) malloc(nItems * sizeof(int));

  //  condition_code = simSS(n_students, th, mean, std, loss, hoss);

        srand((int)time(0));
	    stud1 = randomNoBetween(0, n_students/2);
	    stud2 = randomNoBetween((n_students/2 +1), n_students);
	    bias = 0.0;
	    abs_bias = 0.0;
	    mse = 0.0;
	    etmean = 0.0;

        bias10 = 0.0;
	    mse10 = 0.0;
	    et10mean = 0.0;

	    for ( i = 0; i < nItems; i++) {
               n_adm[i] = 0;    // number of item i administrated
               exposureRate[i] = 0.0;
        }

        for ( j = 0; j < n_obj; j++) 
                n_obj_adm[j] = 0;

	    for ( k = 0; k < n_students; k++) {
             theta0 = th[kq];
		     condition_code = setup_cat(cont);

	         if (condition_code != SETUP_CATok) {
	              printf("Error: setup_cat failed! \n");
	              return 1; 
			 }
	    //     for ( n = 0; n < testLength; n++) {
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
                    set_simuRWO(rwo, theta0); // simulation
		            theta = score();
					if (n == 10) {
					    t10 = theta;
						SEM10= getSEM(t10);
					}
				 }
	    //	 printf(" theta = %6.2lf, SEM = %8.3lf \n", theta, SEM);
	         }
			 n--;
			 // n is total number of adapted items.

             testLength = getTestLength(); // updated testLength
             if (n != testLength)
				 printf(" Error: n = %d, testLength = %d \n", n, testLength);
			 
	         SEM = getSEM(theta);
	         et[k] = theta;   // used in huaBias only
	         etmean = etmean + theta;
			 et10mean = et10mean + t10;
	//	 ttmean = ttmean + theta0;
	         abs_bias = abs_bias + fabs(theta0 - theta);
		     bias = bias + (theta0 - theta);
			 bias10 = bias10 + (theta0 - t10);
		     mse = mse + (theta0 - theta)*(theta0 - theta);
		     mse10 = mse10 + (theta0 - t10)*(theta0 - t10);

             getAdmItems(items, &n_obj, objID);    // get items after one student finishes test.

             n_rwo = 0;
		     raw_score = 0;
             for ( i = 0; i < nItems; i++) 
			     if (items[i].rwo == 1) raw_score ++;

		     if (k == 0) 
			     fprintf(logSimu_file, "Student No., Raw Score, True theta,   Est. theta,  SEM, theta10, SEM10  obj-items rwo  \n");

             fprintf(logSimu_file, " %d \t, %d, %6.2lf, %6.2lf, %8.3lf, %6.2lf, %8.3lf ", (k+1), raw_score, theta0, theta, SEM, t10, SEM10);    
             
			 // check number of obj, passed
		     for ( j = 0; j < n_obj; j++) 
                n_obj_adm1[j] = 0;

		     for ( i = 0; i < nItems; i++) {
		         if (items[i].adm_flag == 0) {
                     n_adm[i] ++;
		             for ( j = 0; j < n_obj; j++) {
                         if (items[i].obj_id == objID[j] ) {
				             n_obj_adm[j]++;
                             n_obj_adm1[j]++;
						 }
					 }
				 }
			 }
         
             for ( j = 0; j < n_obj; j++) 
                 fprintf(logSimu_file, " %d ", n_obj_adm1[j]) ;

		     for ( i = 0; i < nItems; i++) {
			      if (items[i].rwo != 9) n_rwo++;

			      fprintf(logSimu_file, "%d", items[i].rwo);

			      if (k == stud1) 
                      RWO1[i] = items[i].rwo;
			      if (k == stud2) 
                      RWO2[i] = items[i].rwo;	
			 }

		     fprintf(logSimu_file, "\n");

		     if (n_rwo != testLength) 
			     printf("Error: n_rwo not = testLength for student k = %d \n", k);

		     setoff_cat();  //todo
		}
   // ttmean = ttmean/(double)n_students;
	    etmean = etmean/(double)n_students;
		et10mean = et10mean/(double)n_students;

	    abs_bias = abs_bias/(double)n_students;
        bias = bias/(double)n_students;
        bias10 = bias10/(double)n_students;

	    mse = mse/(double)n_students;
        mse10 = mse10/(double)n_students;

		se = sqrt(mse - bias*bias);
		se10 = sqrt(mse10 - bias10*bias10);

        mse = sqrt(mse);  // rmsd
        mse10 = sqrt(mse10);  // rmsd

		fprintf(logSimu_file, "At 10th item:        bias = %6.3lf, rmsd = %6.3lf, se = %6.3lf, est_mean = %6.3lf \n "
		    , bias10, mse10, se10,  et10mean);

        fprintf(logSimu_file, " abs_bias = %6.3lf, bias = %6.3lf, rmsd = %6.3lf, se = %6.3lf, est_mean = %6.3lf \n "
		    ,abs_bias, bias, mse, se,  etmean);
    
  //  cor = corr(n_students, th, et, &ttmean, &etmean, &ttstd, &etstd, &rmsd);
  //  fprintf(log_file, "corr = %6.3lf, %6.3lf %6.3lf %6.3lf %6.3lf %6.3lf \n", cor, ttmean, etmean, ttstd, etstd, rmsd); 
	   n_overlap = 0;
	   for ( i = 0; i < nItems; i++) {
           if ((RWO1[i] != 9) && (RWO2[i] != 9))
                n_overlap++;
	   }
	
	   overlapRate = (double)n_overlap/testLength;
	   fprintf(logSimu_file, "Overlap Rate = %5.3lf between student no. %d and %d. \n", overlapRate, 
		  (stud1+1), (stud2+1));

	   fprintf(logSimu_file, " Item No.,  Adm No.,  Exposure Rate <= 0.05 \n");
	
	   n05 = 0;
	   n00 = 0;
	   for ( i = 0; i < nItems; i++) {
           exposureRate[i]= ((double) n_adm[i])/n_students;
		   if ( exposureRate[i] <= 0.05 ) {
   //            fprintf(logSimu_file, " %d, \t %d,  %4.2lf \n", (i+1), n_adm[i], exposureRate[i]);
		       n05 ++;
		   }
           if ( exposureRate[i] == 0.0 ) n00 ++;
	   }
    
	   fprintf(logSimu_file, "No. of items with exposure rate <= 0.05: = %d \n", n05);
       fprintf(logSimu_file, "No. of items with exposure rate = 0: = %d \n", n00);

       fprintf(bias_file, " %d \t %7.3lf %7.3lf %6.3lf %6.3lf %6.3lf  %7.3lf %6.3lf %6.3lf %6.3lf %d %d %6.3lf %d %d  ", 
	        kq, theta0, etmean, bias, mse, se, et10mean, bias10, mse10, se10,  n05, n00, overlapRate, (stud1+1), (stud2+1));
       
	   for ( j = 0; j < n_obj; j++)                    
           fprintf(bias_file, " %6.2lf", n_obj_adm[j]*100.0/(n_students*testLength)); 


       for ( j = 0; j < n_obj; j++)                    
           fprintf(bias_file, " %d", objID[j]); 
       
	   fprintf(bias_file, "\n");

       lvl = get_testLevel(theta0, cont);
	   for ( j = 0; j < n_obj; j++)   {
	      n_obj_byLevel[level2i(lvl)][j] += n_obj_adm[j];
	      n_qs_byLevel[level2i(lvl)][j] ++;
	   }
	   
       free(items); 
       free(n_adm); 
       free(exposureRate);
	   free(RWO1);
	   free(RWO2);
	}

    for ( j = 0; j < n_obj; j++)   {
        for ( i = 0; i < 6; i++) {
            n_obj_byLevel[i][j] = n_obj_byLevel[i][j]/(n_qs_byLevel[i][j]*n_students);
			fprintf(objLvl_file, "%5.2lf ", n_obj_byLevel[i][j]);
		}
		fprintf(objLvl_file, "\n");
	}
    //@todo
    for ( i = 0; i < 6; i++) {
	   getNadmObj(".\\Data\\NoItemsAnObjByLvl.csv", cont, i2level(i), n_obj_adm);
       for ( j = 0; j < n_obj; j++)   {
           t_n_obj_byLevel[i][j] = n_obj_adm[j];
	   }
    }

    for ( j = 0; j < n_obj; j++)   {
        for ( i = 0; i < 6; i++) {
			fprintf(objLvl_file, "%5d ", t_n_obj_byLevel[i][j]);
		}
		fprintf(objLvl_file, "\n");
	}

    for ( j = 0; j < n_obj; j++)   {
        for ( i = 0; i < 6; i++) {
			fprintf(objLvl_file, "%5.2lf ", (t_n_obj_byLevel[i][j]-n_obj_byLevel[i][j]));
		}
		fprintf(objLvl_file, "\n");
	}

    fclose(logSimu_file);
	fclose(bias_file);
	fclose(objLvl_file);
    return 1;
}

int level2i(char lvl) {
	int i;
	switch(lvl){
		 case 'L': i = 0; break;
		 case 'E': i = 1; break;
         case 'M': i = 2; break;
		 case 'D': i = 3; break;
		 case 'A': i = 4; break;
		 case 'T': i = 5; break;
		 default : i = 2; break;   //use 'M' if no testLevel  
	}
    return i;
}

char i2level(int i) {
	char lvl;
	switch(i){
		 case 0: lvl = 'L'; break;
		 case 1: lvl = 'E'; break;
         case 2: lvl = 'M'; break;
		 case 3: lvl = 'D'; break;
		 case 4: lvl = 'A'; break;
		 case 5: lvl = 'T'; break;
		 default : lvl = 'M'; break;   //use 'M' if no testLevel  
	}
    return lvl;
}
