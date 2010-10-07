/*
 * Created: Q3 2010
 * Author: Lianghua Shu
 */
#include <windows.h>
#include <tlhelp32.h>
#include <tchar.h>

#include <stdio.h>
#include <stdlib.h>
#include <stddef.h>
#include <string.h>
#include <math.h>
#include <time.h>

#include "item.h"
#include "irt.h"
#include "getPar.h"
#include "utilityFun.h"
#include "array.h"
#include "cat.h"
#include "genRandomDis.h"
#include "sort_index.h"

#include "jni.h"

#define MAX_FILENAME_LENGTH 300
#define MAX_NUM_OBJ 10
#define Qs 101   // q points

HINSTANCE	hInst;		    // Instance handle

char par_filename[MAX_FILENAME_LENGTH]= "";
char ssByLvl_filename[MAX_FILENAME_LENGTH];
char ssByCnt_filename[MAX_FILENAME_LENGTH]="";   // NOT USE IT, IF USE 'M' level as default
static char _NoItemsEachObjByLvl_filename[MAX_FILENAME_LENGTH];

// char log_file[MAX_FILENAME_LENGTH];
static int _n_items; 
static double _loss; 
static double _hoss; 
static int _testLength = 20;
static int _1st_nItems = 5;
static int _2nd_nItems = 5;
// static int _3rd_nItems = 0;
double std;
double mean;
static double *_theta = NULL;
static struct item_info *_items = NULL;
static int _objID[MAX_NUM_OBJ];       
static int _n_obj_items[MAX_NUM_OBJ]; // total number of items in objID[j] in item bank
static int _n_adm_obj[MAX_NUM_OBJ];   // total number of items in objID[j] need to be adiministrated.
static int _n_obj_adapted[MAX_NUM_OBJ]; // total number of items in objID[j] has been adapted.
static int _n_obj = 0;
static int*** _pItems = NULL ;   /* in a 3D array */
static int** _nItems_a_c; /* number of items in a strata and content 2D array */
static int* _n_a;

static int _aStrata = 3;
static int _aStrata_o = 1;
static double _n_rate = 0.1;

static int* _o; // obj order
static int* _max_nItem_obj;
static int _max_pItems_diff;
static double* _itemInfo;
static int* _indx;
static int* _indx_a;
static int _idx1;
static int _idx2;
static int _iadm = 0;
static int _ik = 0;
struct item_info *_item_adm = NULL;
static double* _tq=NULL;
static double* _ft=NULL;
static int _seeds = 0;
static double ssSts[4];
static char _subTest[2];
int NEXT_ITEM_FAILURE = -999;
int END_ITEM = -1;

FILE *log_file;
char log_filename[MAX_FILENAME_LENGTH];

void set_pItems();
int adapt_aItem(int obj, int aSt, double theta);
int adapt_aItemFromAll(double theta);  // no obj and alpha stratificatoin constraint.
int adapt_aItemFromIdx(double theta, int idx1, int idx2);
int get_o_a(int *obj, int *aSt, int n);

int get_objID_idx();
void set_obj_order();
void set_testLength();

void update_iBank();

JNIEXPORT jint JNICALL Java_com_ctb_tdc_web_utils_CATEngineProxy_setup_1cat (JNIEnv * env, jclass theclass, jstring subtest) {
	const jchar* subtestS = (*env)->GetStringUTFChars( env, subtest, 0 );
	char* subtestString = (char*) subtestS;
	return setup_cat(subtestString);
}

// For no obj and aStr case
// To switch from memo8 to memo9
// 1) _n_rate = 0.05; to 0.1
// 2) itemID = adapt_aItemFromAll(theta) to ...
//  
int DLL_EXP_IMP WINAPI setup_cat(char subTest[]) {
   int condition_code = 0;  /* Returned condition code  */
   int i;
   double *a;
   char versionInfo[]="TABE CAT version 1.0 (Build 001), 08/17/2010.";
 //  char tmpC[2] = "";

   strcpy(_subTest, subTest);

   strcpy(log_filename, "");
   strcat(log_filename, ".\\Data\\");
   strcat(log_filename, subTest);
//   tmpC[0] = '_';
//   tmpC[1] = testLevel;
//   tmpC[2] = '\0';
//   strcat(log_filename, tmpC);
   strcat(log_filename, ".log");

   log_file = fopen(log_filename, "w");
  
   if (log_file == NULL) {
        printf ("Error: Couldn't open log file: %s \n", log_filename);
        return SETUP_CAT_FAILURE;
   }

   fprintf(log_file, " %s \n", versionInfo);

   /*
   strcpy(ssByLvl_filename, ".\\Data\\SSstatsByLevel.csv");
 //  strcpy(ssByCnt_filename, ".\\Data\\SSstatsByContent.csv");

   condition_code = getSSstats (ssByLvl_filename, ssByCnt_filename, subTest, testLevel, ssSts);
   if (condition_code != GETSS_STATS_OK) {
	   fprintf(log_file, "Error: getSSstats failed for subTest %s and Level %c \n", subTest, testLevel);
	   return SETUP_CAT_FAILURE; 
   }
   
   mean = ssSts[0];
   std = ssSts[1];
   _loss = ssSts[2];
   _hoss = ssSts[3];
   */

   strcpy(_NoItemsEachObjByLvl_filename, ".\\Data\\NoItemsAnObjByLvl.csv");
   /*
   condition_code = getNadmObj(NoItemsEachObjByLvl_filename, subTest, testLevel, _n_adm_obj);
   if (condition_code != GETN_ADMOBJ_OK) {
	   fprintf(log_file, "Error: getNadmObj failed for subTest %s and Level %c \n", subTest, testLevel);
	   return SETUP_CAT_FAILURE; 
   }
   */

   if ( !strcmp("MC", subTest)) {  // set stratify and adapt an item from the rate of n items with the best info
       _aStrata = 3;    // for first n items only
       _n_rate = 0.1;   // for first n items only
   //    _n_rate = 0.05;   // memo 8

	   _aStrata_o = 1;  // after 10 item test
	   mean = 495;
       std = 130;
       _loss = 235;
       _hoss = 755;
	   // set testLength
       _testLength = 20;  //@todo should be max test length for all levels; then reset after 10 item test.
	   _1st_nItems = 5;   // from lowest 1/3 alpha strata
	   _2nd_nItems = 5;   // from 2nd 1/3 alpha strata
	//   _3rd_nItems = _testLength - _1st_nItems - _2nd_nItems;
   }

   strcpy(par_filename, "");
   strcat(par_filename, ".\\Data\\");
   strcat(par_filename, subTest);
   strcat(par_filename, ".csv");
   
   _n_items = get_n_items(par_filename);
   if (_n_items == GET_NITEM_FAILURE ) {
       fprintf(log_file, "Error: get_n_items failed in %s \n", par_filename);
	   return SETUP_CAT_FAILURE; 
   }

   _items = (struct item_info *) malloc(_n_items * sizeof(struct item_info));
	
    if (_items == NULL) {
		fprintf(log_file, "Error: malloc _items failed! \n");
        return  SETUP_CAT_FAILURE;
    }
    
	for ( i = 0; i < MAX_NUM_OBJ; i++) {
        _n_obj_items[i] = 0;    // initialize to 0
		_n_obj_adapted[i] = 0;
	}
	condition_code = getItems(par_filename, _items, _n_items, &_n_obj, 
			  _objID, _n_obj_items);
 // QA passed
	/* print out once for info
	j = 0;
    for ( i = 0; i < _n_obj; i++) {
        fprintf(log_file, " %d %s %d \n", _objID[i], _items[j].obj_title, _n_obj_items[i]);
		j = j + _n_obj_items[i]; 
	}
    */
    if (condition_code != GETPAR_OK) {
        fprintf(log_file, "Error: getItems failed %s \n", par_filename); 
        return SETUP_CAT_FAILURE;
	}

	 // MC
   /*
   for ( i = 0; i < _n_obj; i++) {
        _testLength = _testLength + _n_adm_obj[i];
   }
   */ 
    _theta = (double*) malloc((_testLength+1) * sizeof(double));
	if (_theta == NULL) {
		fprintf(log_file, "Error: malloc _theta failed! \n");
        return  SETUP_CAT_FAILURE;
    }
	_theta[0] = mean; // 0.5*(_loss+_hoss);
    
    // set Q points
    _tq = (double*) malloc((Qs+1) * sizeof(double));
    _ft = (double*) malloc((Qs+1) * sizeof(double));
    if ((_tq == NULL) | (_ft == NULL) ) {
		fprintf(log_file, "Error: malloc _tq or _ft failed! \n");
        return  SETUP_CAT_FAILURE;
    }

    for ( i = 0; i < Qs; i++) {
        _tq[i] = _loss + (double)i * (_hoss - _loss)/(double)(Qs-1) ;    // initialize to 0
	    _ft[i] = getNormDisDen(_tq[i], mean, std);
	}
    _item_adm = (struct item_info *) malloc( _testLength * sizeof(struct item_info));
    if (_item_adm == NULL) {
		fprintf(log_file, "Error: malloc _item_adm failed! \n");
        return  SETUP_CAT_FAILURE;
    }

	
	// item pool partition i=0, _n_obj - 1;j = 0, aStrata_o -1; k=0,1
    
    condition_code = new3DIntArray(_n_obj, _aStrata_o, 2, &_pItems);
    if (condition_code != ARRAY_MALLOC_OK) {
        fprintf(log_file, "Error:  malloc 3D array failed! \n");
	    return SETUP_CAT_FAILURE;	
	} 

    condition_code = new2DIntArray(_n_obj, _aStrata_o, &_nItems_a_c);
    if (condition_code != ARRAY_MALLOC_OK) {
       fprintf(log_file, "Error: malloc 2D array failed! \n");
	   return SETUP_CAT_FAILURE;	
	} 

	_n_a = (int *) malloc(_aStrata_o * sizeof(int));
    _max_nItem_obj = (int *) malloc(_aStrata_o * sizeof(int));
    if ( (_n_a == NULL) | (_max_nItem_obj == NULL) ) {
		fprintf(log_file, "Error: malloc _n_a or _max_nItem_obj failed! \n");
        return  SETUP_CAT_FAILURE;
    }

    set_pItems();   
   // print_pItems();
  

    _itemInfo = (double *) malloc(_n_items * sizeof(double));
    _indx = (int *) malloc(_n_items * sizeof(int));
    _indx_a = (int *) malloc(_n_items * sizeof(int));
	a = (double *) malloc(_n_items * sizeof(double));
    if ( (_itemInfo == NULL) | (_indx == NULL) |(_indx_a == NULL ) |(a == NULL )) {
		fprintf(log_file, "Error: malloc _itemInfo or _indx failed! \n");
        return  SETUP_CAT_FAILURE;
    }
	
	_seeds++;
    srand(((int)time(0)+_seeds)); // set seed at different value for different time.
    
    // sort item bank by a parameter value
    for (i = 0; i < _n_items; i++)
		a[i] = _items[i].parameters[0];

    indexx(_n_items, a, _indx_a);
	_idx1 = _n_items/_aStrata;   
	_idx2 = _n_items*2/_aStrata;
	free(a);
//	_o = (int *) malloc(_n_obj * sizeof(int));
	
//	set_obj_order(subTest, testLevel); // set randomly obj adapt order

	ran0(-1);  // for simulation

    _iadm = 0;
	return SETUP_CAT_OK;
}


int getTestLength(){
	return _testLength;
}

int getNumItems(){
	return _n_items;
}

int getNumObj(){
	return _n_obj;
}

double getLoss(){
	return _loss;
}

double getHoss(){
	return _hoss;
}

void set_obj_order(char subTest[], char testLevel){   // set rand obj order
	 int i; //, j, k;
//	 int flag = 0;
     	 
	 if (!strcmp("MC", subTest))  {
		 if ((testLevel == 'L') | ( testLevel == 'E') |
			 (testLevel == 'M')) {   // downward order
             for (i = 0; i < _n_obj; i++) { 
				 _o[i] = i;    
			 }
		 }
		 else {
             for (i = 0; i < _n_obj; i++) { // upward order
				 _o[i] = _n_obj - i - 1;
			 }
		 }

	 }
	 else
         indexIntDescend( _n_obj, _n_adm_obj, _o); 
	 // set Descend order, let the highest number of obj item to be tested first.

	 /* set randomly obj order
     _o[0] = randomNoBetween(0,(_n_obj-1));

     for( i = 1; i < _n_obj; i++) {
	    do{
		    k = randomNoBetween(0,(_n_obj-1));
            flag = 1;
			for (j = 0; j < i; j++) {
               if (_o[j] == k) flag = 0;
			}
		} while ( !flag);
	    _o[i] = k;
     }
      
	 
     for( i = 0; i < _n_obj; i++) {
	   //  fprintf(log_file, " %d  %d \n", _o[i], _n_adm_obj[_o[i]]);
		 printf( " %d  %d \n", _o[i], _n_adm_obj[_o[i]]);
	 }	
	 */
}

JNIEXPORT jint JNICALL Java_com_ctb_tdc_web_utils_CATEngineProxy_adapt_1n_1item (JNIEnv * env, jclass theclass) {
	return next_item();
}

int DLL_EXP_IMP WINAPI next_item() {
	char lvl;
    int itemID;
/* // For obj and aSt case
    int obj, aSt; 
*/
	int objID_idx;
	int condition_code;
	double theta;

  //  if (n == 0 ) _iadm = 0;
    if ( _iadm >= _testLength)  return END_ITEM;
    theta = _theta[_iadm];

	/* // For obj, aSt case memo 1
	// determine n item at [obj, aSt]
	condition_code = get_o_a(&obj, &aSt, n);
//	fprintf(log_file, "n= %d %d %d \n", n, aSt, obj); 
	if (condition_code != GET_O_A_OK ) {
		fprintf(log_file, "Error: get_o_a failed at item # %d \n", n);
		return ADAPT_N_ITEM_FAILURE;
	}
		
	itemID = adapt_aItem(obj, aSt, theta);
	*/

	// memo 9
    if (_iadm < _1st_nItems )
          itemID = adapt_aItemFromIdx(theta, 0, _idx1);
	else if ((_iadm >= _1st_nItems) & ( _iadm < (_1st_nItems + _2nd_nItems ) ) )
		  itemID = adapt_aItemFromIdx(theta, _idx1, _idx2);
	else {
		if (_iadm == (_1st_nItems + _2nd_nItems ) ) {
			lvl = get_testLevel(theta, _subTest);
		//	get _n_adm_obj[]
            condition_code = getNadmObj(_NoItemsEachObjByLvl_filename, _subTest, lvl, _n_adm_obj);
            if (condition_code != GETN_ADMOBJ_OK) {
	             fprintf(log_file, "Error: getNadmObj failed for subTest %s and Level %c \n", _subTest, lvl);
	             return NEXT_ITEM_FAILURE; 
			}
			//set testLength
			set_testLength();
		}
        objID_idx = get_objID_idx();
		itemID = adapt_aItem(objID_idx, (_aStrata_o -1), theta); 
	}
    

	// memo 8
  //  itemID = adapt_aItemFromAll(theta);  // for no obj sSt case

    update_iBank();

	return itemID;
}

int get_objID_idx(){
    double r; 
	double r_max = -1;
    int i;
	int i_max = 0;
	
	for ( i = 0; i < _n_obj; i++) {
		if (_n_adm_obj[i] > _n_obj_adapted[i]) {
            r = (double)(_n_adm_obj[i] - _n_obj_adapted[i])/_n_adm_obj[i];
			if (r > r_max ) {
				r = r_max;
				i_max = i;
			}
		}
    }
	return i_max;
}

void set_testLength() {
	int i;
	_testLength = 0;
	for ( i = 0; i < _n_obj; i++) {
        _testLength = _testLength + _n_adm_obj[i];
    }
}

char get_testLevel(double theta, char subTest[]){
	char lvl = 'M';
	
	if ( !strcmp("MC", subTest)) {
        if (theta <= 313) lvl = 'L';
        else if ((theta > 313) & (theta <= 441) ) lvl = 'E';
        else if ((theta > 441) & (theta <= 505) ) lvl = 'M';
        else if ((theta > 505) & (theta <= 565) ) lvl = 'D';
        else if ((theta > 565) & (theta <= 594) ) lvl = 'A';
        else lvl = 'T';
	}
	else{}
	return lvl;
}

int get_o_a(int *obj, int *aSt, int n){                                                         
   int nItems = 0;
   int i,j,k;
   int a1=_n_a[0], a2=a1;

   // to determine a strata
   if (n < _n_a[0]) *aSt = 0;
   else {
	      for ( j = 1; j < _aStrata; j++) {
              a2 = a2 + _n_a[j];
			  if ((n >= a1) && (n < a2)) {
				  *aSt = j;
				  break;
			  }
			  a1 = a2;
		  }
   }	   

   // to determine obj 
   for ( j = 0; j <= *aSt; j++) {
     for (k = 0; k < _max_nItem_obj[j]; k++) {  
        for ( i = 0; i < _n_obj; i++) {
            if (_nItems_a_c[_o[i]][j] >= (k+1))
			    nItems++;
            if (nItems == (n+1)) {
                *obj = _o[i];
				return GET_O_A_OK;
			}
		}
	 }
   }

//   fprintf(log_file, "n= %d %d %d \n", n, &aSt, &obj); 
   return GET_O_A_FAILURE;
}

int adapt_aItemFromIdx(double theta, int idx1, int idx2) {  // no obj and alpha stratificatoin constraint.
    // found best n items with highest item info at theta
	// randomly select one of above n items
	// return item ID and set it as adminitrated.
   int k, i=0;
   int n_cell_items = 0;
   
   int ns;  // from best 5 items
   int itemID;
   // theta = 478.5;
   
   for (k = idx1; k < idx2; k++) { 
	   if ( _items[_indx_a[k]].adm_flag ) {
	       _itemInfo[i] = fisherInfo(theta, _items[_indx_a[k]].parameters);  
	       n_cell_items ++;
	   }
	   else
		   _itemInfo[i] = -9.0;
	   i++;
   }

   k = idx2 - idx1;
   
   if (n_cell_items < 1) {
	   fprintf(log_file, "Error: run out of item in pool \n");
	   return NEXT_ITEM_FAILURE;
   }
   indexDescend( k, _itemInfo, _indx);  

   // set best 10% in the pool
   ns = (int)( _n_rate * n_cell_items );
   if (ns < 1 ) 
       ns = n_cell_items;

   // rand select one of first n _indx, available items are n_cell_items
   if (ns == 1 )
	  i = 0;
   else
      i = randomNoBetween(0, (ns -1));  // rand select from best ns items

   _ik = _indx_a[_indx[i] + idx1];
   
   itemID = _items[_ik].item_id;
   
   return itemID;
}

int adapt_aItemFromAll(double theta) {  // no obj and alpha stratificatoin constraint.
    // found best n items with highest item info at theta
	// randomly select one of above n items
	// return item ID and set it as adminitrated.
   int k, i=0;
   int n_cell_items = 0;
   
   int ns;  // from best 5 items
   int itemID;
   // theta = 478.5;
   
   for (k = 0; k < _n_items; k++) { 
	   if ( _items[k].adm_flag ) {
	       _itemInfo[i] = fisherInfo(theta, _items[k].parameters);  
	       n_cell_items ++;
	   }
	   else
		   _itemInfo[i] = -9.0;
	   i++;
   }

   if (n_cell_items < 1) {
	   fprintf(log_file, "Error: run out of item in pool \n");
	   return NEXT_ITEM_FAILURE;
   }
   indexDescend( _n_items, _itemInfo, _indx);  

      // set best 10% in the pool
   ns = (int)( _n_rate * n_cell_items );
   if (ns < 1 ) 
       ns = n_cell_items;

   // rand select one of first n _indx, available items are n_cell_items
   if (ns == 1 )
	  i = 0;
   else
      i = randomNoBetween(0, (ns -1));  // rand select from best ns items

   _ik = _indx[i];
   
   itemID = _items[_ik].item_id;
   
   return itemID;
}

int adapt_aItem(int obj, int aSt, double theta) {
    // found best n items with highest item info at theta
	// randomly select one of above n items
	// return item ID and set it as adminitrated.
   int k, i=0;
   int n_cell_items = 0;
   
 //  int ns;  // from best 5 items
   int itemID;
  
   for (k = _pItems[obj][aSt][0]; k <= _pItems[obj][aSt][1]; k++) { 
	   if ( _items[k].adm_flag ) {
	       _itemInfo[i] = fisherInfo(theta, _items[k].parameters);  
	       n_cell_items ++;
	   }
	   else
		   _itemInfo[i] = -9.0;
	   i++;
   }
   k = _pItems[obj][aSt][1] - _pItems[obj][aSt][0] + 1;
   
   if (n_cell_items < 1) {
	   fprintf(log_file, "Error: run out of item in pool \n");
	   return NEXT_ITEM_FAILURE;
   }
   
   indexDescend( k, _itemInfo, _indx);  //@todo
   i = 0;   // pick up the best one	
   _ik = _pItems[obj][aSt][0] + _indx[i];
   
   itemID = _items[_ik].item_id;
   
   return itemID;
}

void update_iBank(){
	int i;
   _items[_ik].adm_flag = 0;   // set this items as administrated

   // copy items to item_adm with continue index
   _item_adm[_iadm].item_id = _items[_ik].item_id;
   _item_adm[_iadm].item_no = _items[_ik].item_no;
   _item_adm[_iadm].obj_id = _items[_ik].obj_id;

   //trace number of items in obj[j] have been adapted
   for (i = 0; i < _n_obj; i++) {
	   if (_objID[i] == _item_adm[_iadm].obj_id)
		   _n_obj_adapted[i] ++;
   }

   _item_adm[_iadm].parameters = (double *) malloc( 3*sizeof(double) );
    if (_item_adm[_iadm].parameters == NULL)  {
		fprintf(log_file, "Error: malloc _item_adm[%d].parameters failed! \n", _iadm );
//		return ADAPT_N_ITEM_FAILURE;
    }
   _item_adm[_iadm].parameters[0] = _items[_ik].parameters[0];
   _item_adm[_iadm].parameters[1] = _items[_ik].parameters[1];
   _item_adm[_iadm].parameters[2] = _items[_ik].parameters[2];
   _iadm++;
}

// simulation
void set_simuRWO(int rwo, double theta0) {

   // simulated studies
   double p0;
  // double theta0 = 400.0;
   p0 = 1.0- respProb3PL(1, theta0, _item_adm[_iadm-1].parameters);
   if (p0 >= ran0(1)) rwo = 0;
   else rwo = 1;
   // end of simulation

   _item_adm[_iadm -1].rwo = rwo;
   _items[_ik].rwo = rwo;   // simulation

}

JNIEXPORT void JNICALL Java_com_ctb_tdc_web_utils_CATEngineProxy_set_1rwo (JNIEnv * env, jclass theclass, jint raw) {
	set_rwo(raw);
}

void DLL_EXP_IMP WINAPI set_rwo(int rwo) {
   _item_adm[_iadm -1].rwo = rwo;
}

JNIEXPORT jdouble JNICALL Java_com_ctb_tdc_web_utils_CATEngineProxy_score (JNIEnv * env, jclass theclass) {
	return score();
}

double DLL_EXP_IMP WINAPI score(){
    double theta;
	double fpq, p, pq;
	double sfpq, tfpq;
	double max_pq = -1.0;
	int max_i, loss_i, hoss_i;
    int i, j;
	double final_theta, old_max_pq;

	sfpq = 0.0;
	tfpq = 0.0;

    for ( i = 0; i < Qs; i++) {
		pq = 1.0;
        for ( j = 0; j < _iadm; j++) {
            p = respProb3PL(1, _tq[i], _item_adm[j].parameters);
			if (_item_adm[j].rwo )
		           pq = pq*p;
			else
			       pq = pq*(1.0-p);
		}

		if (_iadm == _testLength) { // last item so use exhuasitive search
            if (max_pq <= pq) {
				max_pq = pq;
				max_i = i;
			}
		}
        fpq = _ft[i]*pq;
		sfpq = sfpq + fpq;
		tfpq = tfpq + _tq[i]*fpq;
	}
    
	theta = tfpq/sfpq;
    _theta[_iadm] = theta;
    
	if (_iadm == _testLength) { // final theta
		if ( max_i == 0 ) {
			loss_i = (int)_tq[max_i];
			hoss_i = (int)_tq[1];
		}
		else if ( max_i == (Qs - 1) ) {
			loss_i = (int)_tq[Qs -2];
			hoss_i = (int)_tq[max_i];
		}
		else {
			loss_i = (int)_tq[max_i - 1];
			hoss_i = (int)_tq[max_i + 1];
		}

		old_max_pq = max_pq;
        for ( i = loss_i; i <= hoss_i; i++) {
		   pq = 1.0;
           for ( j = 0; j < _iadm; j++) {
               p = respProb3PL(1, (double)i, _item_adm[j].parameters);
			   if (_item_adm[j].rwo )
		           pq = pq*p;
			   else
			       pq = pq*(1.0-p);
		   }
           if (max_pq < pq) {
			   max_pq = pq;
			   final_theta = (double) i;
		   }
		}

		if (old_max_pq == max_pq) final_theta = _tq[max_i];

        /*
		fprintf(log_file, " %6.2lf, %6.2lf \n", theta, final_theta);
		for ( j = 0; j < _iadm; j++) 
		   fprintf(log_file, "%d ", _item_adm[j].item_no);
        fprintf(log_file, "\n");

		for ( j = 0; j < _iadm; j++) 
		   fprintf(log_file, "%d", _item_adm[j].rwo);
		fprintf(log_file, "\n");
		*/

		theta = final_theta;  // final theta from ES.
	}

	return theta;
}
JNIEXPORT jdouble JNICALL Java_com_ctb_tdc_web_utils_CATEngineProxy_getSEM (JNIEnv * env, jclass theclass, jdouble theta) {
	return getSEM(theta);
}

double DLL_EXP_IMP WINAPI getSEM(double theta) {
	int j;
	double p = 0.0;
    for ( j = 0; j < _iadm; j++) {  // to do need to be changed to final_theta
         p = p + fisherInfo(theta, _item_adm[j].parameters);		
	} 
    
	return 1.0/sqrt(p);
}

void set_pItems(){
     int i, j, n_a, n_a_c;
     int n_ob = 0;
	 int pItems_diff; 
     _max_pItems_diff = -1;

	 for ( i = 0; i < _n_obj; i++) {
         n_a = _n_obj_items[i]/_aStrata_o;  // all items in an a strata and one obj 
         n_a_c = _n_adm_obj[i]/_aStrata_o;  //administraed items in an a strata and one obj 

		 for ( j = 0; j < _aStrata_o; j++) {
             _pItems[i][j][0] = j*n_a + n_ob;  
			 if (j == (_aStrata_o -1)) { 
		         _pItems[i][j][1] = _n_obj_items[i] -1 + n_ob;
                 _nItems_a_c[i][j] = _n_adm_obj[i] - j*n_a_c; 
			 }
			 else {
				 _pItems[i][j][1] = (j+1)*n_a -1 + n_ob;
                 _nItems_a_c[i][j] = n_a_c;
             }
			 pItems_diff = _pItems[i][j][1] - _pItems[i][j][0];
             if (_max_pItems_diff < pItems_diff ) _max_pItems_diff = pItems_diff;
		 }
         n_ob = n_ob + _n_obj_items[i]; 
	 }

	 _max_pItems_diff ++;

     for ( j = 0; j < _aStrata_o; j++) {
        _n_a[j] = 0;
        _max_nItem_obj[j] = 0;
        for ( i = 0; i < _n_obj; i++) {
		    _n_a[j] = _n_a[j] + _nItems_a_c[i][j]; // total number items administrated in an strata 
            if (_nItems_a_c[i][j] > _max_nItem_obj[j])
                _max_nItem_obj[j] = _nItems_a_c[i][j];  // max number items administrated in one obj at j
		}
	 }
}

void print_pItems(){
     int i, j;
	 for ( i = 0; i < _n_obj; i++) {
		 for ( j = 0; j < _aStrata_o; j++) {
             fprintf(log_file, "i= %d j= %d %d %d %d \n", i, j, _pItems[i][j][0], _pItems[i][j][1], 
				 _nItems_a_c[i][j]); 
		 }
	 }
      
	 fprintf(log_file, " _max_pItems_diff =  %d  \n", _max_pItems_diff);

     for ( j = 0; j < _aStrata_o; j++) {
             fprintf(log_file, "%d  %d  %d \n", j, _n_a[j], _max_nItem_obj[j]); 
	 }
 
}

JNIEXPORT void JNICALL Java_com_ctb_tdc_web_utils_CATEngineProxy_setoff_1cat (JNIEnv * env, jclass theclass) {
	setoff_cat();
}

void DLL_EXP_IMP WINAPI setoff_cat(){  // For no obj and aStr case

   fclose(log_file);
   free(_theta);
   free(_tq);
   free(_ft);
   free_items(_n_items, _items); 
   free_items(_testLength, _item_adm);
 //  free(_n_a);
   free(_max_nItem_obj);
   free(_itemInfo);
   free(_indx);
//   free3DIntArray(_pItems);
//   free2DIntArray(_nItems_a_c);
}

/*  // for obj and aStr case
void setoff_cat(){

   fclose(log_file);
   free(_theta);
   free(_tq);
   free(_ft);
   free_items(_n_items, _items); 
   free_items(_testLength, _item_adm);
   free(_n_a);
   free(_max_nItem_obj);
   free(_itemInfo);
   free(_indx);
   free3DIntArray(_pItems);
   free2DIntArray(_nItems_a_c);
}
*/
// simulate scale score in normal distribution
int simSS(int nStudents, double ss[], double mean, double std, double loss, double hoss) {
    int i;
	long init_ran = -1;

    srand((int)time(0));
  //  ran1(&init_ran);
	ran0(init_ran);
	for (i = 0; i < nStudents; i++) {	
       ss[i] = genNormDis(mean, std, loss, hoss);
	   // set ss in normal distribution from loss and hoss
	}
    return 0;
}

void getAdmItems(struct item_info *items, int *n_obj, int objID[]){
    int i, j;
    for (i = 0; i < _n_items; i++) {
	   items[i].item_no = _items[i].item_no;
       items[i].adm_flag = _items[i].adm_flag; 
       items[i].obj_id = _items[i].obj_id; 
   //    items[i].obj_n = _items[i].obj_n; 
	   items[i].rwo = _items[i].rwo; 
	}
    for (j = 0; j < _n_obj; j++) {
       objID[j] = _objID[j];    
	}
    *n_obj = _n_obj;
}


/***********************
 * LIBRARY ENTRY POINT.*
 ***********************/
BOOL WINAPI DllMain(HINSTANCE hInstDll, DWORD fdwReason, LPVOID lpReserved)
{
    hInst = hInstDll;

	return TRUE;
}