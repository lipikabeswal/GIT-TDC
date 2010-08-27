/*
 * Created: Q3 2010
 * Author: Lianghua Shu
 */

#include    <windows.h>
#include   <tlhelp32.h>
#include	<tchar.h>
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

char par_filename[MAX_FILENAME_LENGTH];
// char log_file[MAX_FILENAME_LENGTH];
int n_items; 
double loss; 
double hoss; 
static int _testLength = 0;
double std;
double mean;
static double *_theta = NULL;
static struct item_info *_items = NULL;
static int _objID[MAX_NUM_OBJ], _n_obj_items[MAX_NUM_OBJ], _n_adm_obj[MAX_NUM_OBJ];
static int _n_obj = 0;
static int*** _pItems = NULL ;   /* in a 3D array */
static int** _nItems_a_c; /* number of items in a strata and content 2D array */
static int* _n_a;

static int _aStrata = 3;
static double _n_rate = 0.3;

static int* _o; // obj order
static int* _max_nItem_obj;
static int _max_pItems_diff;
static double* _itemInfo;
static int* _indx;
static int _iadm = 0;
static int _ik = 0;
struct item_info *_item_adm = NULL;
static double* _tq=NULL;
static double* _ft=NULL;
static int _seeds = 0;

void set_obj_order();

JNIEXPORT jint JNICALL Java_com_ctb_tdc_web_utils_CATEngineProxy_setup_1cat (JNIEnv * env, jclass theclass, jstring subtest, jchar level) {
	const jchar* subtestS = (*env)->GetStringUTFChars( env, subtest, 0 );
	char* subtestString = (char*) subtestS;
	return setup_cat(subtestString, level);
}

int DLL_EXP_IMP WINAPI setup_cat(char subTest[], char testLevel)
{   
   int condition_code = 0;  /* Returned condition code  */
   int i;
   
   if (!strcmp(subTest, "RD")) {
	   n_items = 400;
	   strcpy(par_filename, "C:\\Program Files\\CTB\\Online Assessment\\data\\RD.csv");
   //    strcpy(log_file, changeFileNameExtension(par_filename, ".LOG"));
	   
	   if (testLevel == 'L') {
		   // set Loss, Hoss, std, theta0, etc
		   loss = 160;
		   hoss = 515;
	//	   _testLength = testLength;
		   std = 84.47;
		   mean = 424.35;
           // set number of items in each obj to be administrated from Tabel 5, TABE 10 
           _n_adm_obj[0] = 12;    //Words in Context
           _n_adm_obj[1] = 4;    //Interpret Graphic Information 
           _n_adm_obj[2] = 9;   //Recall Information
		   _n_adm_obj[3] = 7;   //Construct Meaning
		   _n_adm_obj[4] = 0;   //Evaluate/Extend Meaning

	   }
	   else if (testLevel == 'M') { // table 10 in test specification for TABE 11-13
           loss = 255;
		   hoss = 702;
	//	   _testLength = testLength;
		   std = 77.47;
		   mean = 504.01;
           // set number of items in each obj to be administrated from Tabel 5, TABE 10 
           _n_adm_obj[0] = 6;    //Words in Context
           _n_adm_obj[1] = 4;    //Interpret Graphic Information 
           _n_adm_obj[2] = 13;   //Recall Information
		   _n_adm_obj[3] = 17;   //Construct Meaning
		   _n_adm_obj[4] = 10;   //Evaluate/Extend Meaning
	   }
	   else{  // student do not give test level
	       loss = 175;
		   hoss = 812;
		//   _testLength = testLength;
		   std = 92.06;
		   mean = 511.40;
	   }
   }
   else{}

   _items = (struct item_info *) malloc(n_items * sizeof(struct item_info));
	
    if (_items == NULL) {
        return 1;
    }
    
	for ( i = 0; i < MAX_NUM_OBJ; i++) {
        _n_obj_items[i] = 0;    // initialize to 0
	}
	condition_code = getItems(par_filename, _items, n_items, &_n_obj, 
			  _objID, _n_obj_items);
 // QA passed
	/* print out once for info
	j = 0;
    for ( i = 0; i < _n_obj; i++) {
        printf(" %d %s %d \n", _objID[i], _items[j].obj_title, _n_obj_items[i]);
		j = j + _n_obj_items[i]; 
	}
    */
    if (condition_code != GETPAR_OK) {
        printf("getPar failed \n"); 
        return 1;
	}

	// set testLength
   _testLength = 0;
   for ( i = 0; i < _n_obj; i++) {
        _testLength = _testLength + _n_adm_obj[i];
   }
    
    _theta = (double*) malloc((_testLength+1) * sizeof(double));
	_theta[0] = 0.5*(loss+hoss);
    if (_theta == NULL) {
          return 1;
	}

    // set Q points
    _tq = (double*) malloc((Qs+1) * sizeof(double));
    _ft = (double*) malloc((Qs+1) * sizeof(double));

    for ( i = 0; i < Qs; i++) {
        _tq[i] = loss + (double)i * (hoss - loss)/(double)(Qs-1) ;    // initialize to 0
	    _ft[i] = getNormDisDen(_tq[i], mean, std);
	}
    _item_adm = (struct item_info *) malloc( _testLength * sizeof(struct item_info));

	// item pool partition i=0, n_obj - 1;j = 0, aStrata -1; k=0,1
    condition_code = new3DIntArray(_n_obj, _aStrata, 2, &_pItems);
    if (condition_code != ARRAY_MALLOC_OK) {
    printf(" malloc 3D array failed! \n");
	return ARRAY_MALLOC_FAILED;	
	} 

    condition_code = new2DIntArray(_n_obj, _aStrata, &_nItems_a_c);
    if (condition_code != ARRAY_MALLOC_OK) {
       printf(" malloc 2D array failed! \n");
	   return ARRAY_MALLOC_FAILED;	
	} 

	_n_a = (int *) malloc(_aStrata * sizeof(int));
    _max_nItem_obj = (int *) malloc(_aStrata * sizeof(int));
    set_pItems();   
   // print_pItems();
    _itemInfo = (double *) malloc(_max_pItems_diff * sizeof(double));
    _indx = (int *) malloc(_max_pItems_diff * sizeof(int));
     
	_seeds++;
    srand(((int)time(0)+_seeds)); // set seed at different value for different time.
    
	_o = (int *) malloc(_n_obj * sizeof(int));
	
	set_obj_order(); // set randomly obj adapt order

	ran0(-1);  // for simulation

	return condition_code;
}

JNIEXPORT jint JNICALL Java_com_ctb_tdc_web_utils_CATEngineProxy_getTestLength (JNIEnv * env, jclass theclass) {
	return getTestLength();
}

int DLL_EXP_IMP WINAPI getTestLength(){
	return _testLength;
}

void set_obj_order(){   // set rand obj order
//	 int i, j, k;
//	 int flag = 0;
     
	 // set Descend order, let the highest number of obj item to be tested first.
     indexIntDescend( _n_obj, _n_adm_obj, _o); 

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
      */ 
	 /*
     for( i = 0; i < _n_obj; i++) {
	     printf(" %d  %d \n", _o[i], _n_adm_obj[_o[i]]);
	 }	
	 */
}

JNIEXPORT jint JNICALL Java_com_ctb_tdc_web_utils_CATEngineProxy_adapt_1n_1item (JNIEnv * env, jclass theclass, jint itemnumber) {
	return adapt_n_item(itemnumber);
}

int DLL_EXP_IMP WINAPI adapt_n_item(int n) {
    int itemID;
    int obj, aSt;
	int condition_code;
	double theta;

    if (n == 0 ) _iadm = 0;

    theta = _theta[n];
	// determine n item at [obj, aSt]
	condition_code = get_o_a(&obj, &aSt, n);
//	printf("n= %d %d %d \n", n, aSt, obj); 

	itemID = adapt_aItem(obj, aSt, theta);
	return itemID;
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

//   printf("n= %d %d %d \n", n, &aSt, &obj); 
   return GET_O_A_FAILURE;
}

int adapt_aItem(int obj, int aSt, double theta) {
    // found best n items with highest item info at theta
	// randomly select one of above n items
	// return item ID and set it as adminitrated.
   int k, i=0;
   int n_cell_items = 0;
   
   int ns;  // from best 5 items
   int itemID;
   // theta = 478.5;
   
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
   
   indexDescend( k, _itemInfo, _indx); 

   // set best 30% in the pool
   if (n_cell_items < 1) printf("Error: run out of item in pool \n");
   else if (n_cell_items > 3 ) 
       ns = (int)( _n_rate * n_cell_items );
   else ns = n_cell_items;

   // rand select one of first n _indx, available items are n_cell_items
   if (n_cell_items == 1 )
	  i = 0;
   else
      i = randomNoBetween(0, (ns -1));  // rand select from best ns items
   _ik = _pItems[obj][aSt][0] + _indx[i];
   
   itemID = _items[_ik].item_id;
   _items[_ik].adm_flag = 0;   // set this items as administrated

   // copy items to item_adm with continue index
   _item_adm[_iadm].item_id = _items[_ik].item_id;
   _item_adm[_iadm].item_no = _items[_ik].item_no;

   _item_adm[_iadm].parameters = (double *) malloc( 3*sizeof(double) );

   _item_adm[_iadm].parameters[0] = _items[_ik].parameters[0];
   _item_adm[_iadm].parameters[1] = _items[_ik].parameters[1];
   _item_adm[_iadm].parameters[2] = _items[_ik].parameters[2];
   _iadm++;
   
   /*
   printf(" %d  %d  %d  %d  \n", obj, aSt, _pItems[obj][aSt][0], _pItems[obj][aSt][1]);    
   for (i = 0; i < k; i++) { 
       printf(" %10.8lf  %d \n", _itemInfo[_indx[i]], _indx[i]);
   }
   */
   return itemID;
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
		printf(" %6.2lf, %6.2lf \n", theta, final_theta);
		for ( j = 0; j < _iadm; j++) 
		   printf("%d ", _item_adm[j].item_no);
        printf("\n");

		for ( j = 0; j < _iadm; j++) 
		   printf("%d", _item_adm[j].rwo);
		printf("\n");
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
         n_a = _n_obj_items[i]/_aStrata;  // all items in an a strata and one obj 
         n_a_c = _n_adm_obj[i]/_aStrata;  //administraed items in an a strata and one obj 

		 for ( j = 0; j < _aStrata; j++) {
             _pItems[i][j][0] = j*n_a + n_ob;  
			 if (j == (_aStrata -1)) { 
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

     for ( j = 0; j < _aStrata; j++) {
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
		 for ( j = 0; j < _aStrata; j++) {
             printf("i= %d j= %d %d %d %d \n", i, j, _pItems[i][j][0], _pItems[i][j][1], 
				 _nItems_a_c[i][j]); 
		 }
	 }
      
	 printf(" _max_pItems_diff =  %d  \n", _max_pItems_diff);

     for ( j = 0; j < _aStrata; j++) {
             printf("%d  %d  %d \n", j, _n_a[j], _max_nItem_obj[j]); 
	 }
 
}

JNIEXPORT void JNICALL Java_com_ctb_tdc_web_utils_CATEngineProxy_setoff_1cat (JNIEnv * env, jclass theclass) {
	setoff_cat();
}

void DLL_EXP_IMP WINAPI setoff_cat(){
   free(_theta);
   free(_tq);
   free(_ft);
   free(_items);  //@todo
   free(_n_a);
   free(_max_nItem_obj);
   free(_itemInfo);
   free(_indx);
   free3DIntArray(_pItems);
   free2DIntArray(_nItems_a_c);
}

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
    for (i = 0; i < n_items; i++) {
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