/*
 * Created: Q3 2010
 * Author: Lianghua Shu
 */
/* Commented below lines while removing windows related dependency */
/* #include <windows.h>
 #include <tlhelp32.h>
 #include <tchar.h>
*/

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
#define MAX_NUM_LVL 6
#define Qs 101   /* q points */
#define UnReportableScore -1.0

//HINSTANCE	hInst;		    /* Instance handle */

char par_filename[MAX_FILENAME_LENGTH]= "";
char ssByLvl_filename[MAX_FILENAME_LENGTH]="";
char ssByCnt_filename[MAX_FILENAME_LENGTH]="";   /* NOT USE IT, IF USE 'M' level as default */
static char _NoItemsEachObjByLvl_filename[MAX_FILENAME_LENGTH]="";

/* char log_file[MAX_FILENAME_LENGTH]; */
static int _n_items; 
static double _loss; 
static double _hoss; 
static int _testLength = 25;
static int _1st_nItems = 5;
static int _2nd_nItems = 5;
static int _max_psg_size_1st_nItems = 3; /* 3 for reading & LA */

double std;
double mean;
static double *_theta = NULL;
static struct item_info *_items = NULL;
static int _objID[MAX_NUM_OBJ];       
static int _n_obj_items[MAX_NUM_OBJ]; /* total number of items in objID[j] in item bank */
static int _n_adm_obj[MAX_NUM_OBJ];   /* total number of items in objID[j] need to be adiministrated. */
static int _n_obj_adapted[MAX_NUM_OBJ]; /* total number of items in objID[j] has been adapted. */
static int _n_obj = 0;
static int*** _pItems = NULL ;   /* in a 3D array */
/* static int** _nItems_a_c; // number of items in a strata and content 2D array 
 static int* _n_a;
*/
static int _new_objID[MAX_NUM_OBJ]; /* total number of items in collapsed objID[j] in item bank */
static int _n_new_obj = 0;                /* totalnumber of collapsed objective IDs   */

static int _aStrata = 3;
static int _aStrata_o = 1;
static double _n_rate = 0.1;
/*
static int* _o; // obj order 
// static int* _max_nItem_obj;
// static int _max_pItems_diff;
*/
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
static double ssSts[5];
static char _subTest[3];
static int _is_psg = 0;
int NEXT_ITEM_FAILURE = -999;
int END_ITEM = -1; /* end of test when tester comlete the test: itemID = -1 */ 
int END_TEST = -9; /* end of test due to running out of time or termination by tester: rwo = -9 */ 

static struct item_info *_FTitems = NULL;   /* FT items */
char parFT_filename[MAX_FILENAME_LENGTH]= "";  /* FT item bank name  */
static int _n_FTitems;                  /* total number of FT items */
static int _n_FTitemsLvl[MAX_NUM_LVL];  /* number of FT items per level */
static int _FTtestLength = 4;        /* number of FT items per examinee */
static int _FTinLastNitems = 5;      /* FT item will appear in last 5 items. */
static int _iadmFT = 0;
static int _ik_FT = 0;   /* FT item adm index */
static int _is_FTpsg = 0;
static int _i_lvlFT = 0;     /* level index for FT items */
static char _lvlFT;    
static int _min_nItems4Obj_score = 4;  /* min number of items for objective scores. */
static int _isFTitem = 0;
static int _enemy_item = 0;
static int _psg_id = 0;
static int _enemy_id = 0;
static int _i_stop = -1;
static double _mean_b;
static double _min_a;
static double _mean_c = 0.0;
static int _report_ss = 0;    /* report scale score flag: 0 do not report, otherwise report */

static int _tot_obj_rs = 0;
static int _obj_rs = 0;
static double _sumCs = 0.0;
static double _lvl_loss[4];
static double _lvl_hoss[4];
static double _ss1 = 0.0; /* objective scale score at raw score = perfect RS - 1. */
static struct objSScut *_objSS_cut = NULL;
char objLvlCut_filename[MAX_FILENAME_LENGTH];
char lvlLH_filename[MAX_FILENAME_LENGTH];

int get_i_obj(char obj_lvl);  /* get objective indx i from objective level */

int get_FTpsg_size(int psg_id);
int next_FTpsg_item(int psg_id);
int get_1st_FTpsg_item(int psg_id);
void update_FTiBank();
int get_FTitemID_fromIdx(int idx);
int adapt_aFTitem();

FILE *log_file;
char log_filename[MAX_FILENAME_LENGTH];

void set_pItems();
int adapt_aItem(int obj, int aSt, double theta);
int adapt_aItemFromAll(double theta);  /* no obj and alpha stratificatoin constraint. */
int adapt_aItemFromIdx(double theta, int idx1, int idx2);
int adapt_aItemFromIdx2(double theta, int idx1, int idx2);
/* int get_o_a(int *obj, int *aSt, int n); */

int get_objID_idx();
/* void set_obj_order(char subTest[], char testLevel); */
/* void set_testLength(); */

void turnOff_enemy();
void update_iBank();
void update_iBank_stop();
int get_itemID_fromIdx(int idx);
int get_psg_size(int psg_id);
int next_psg_item(int psg_id);
int get_1st_psg_item(int psg_id);
double psg_info(int psg_id, double theta);
int get_loss(int obj_id);
int get_hoss(int obj_id);
double TCCinverse(double rs, int obj_id, int loss, int hoss);
int getMasterLvlFromSS(double ss, int obj_id);
void set_psgID(int psg_id);

JNIEXPORT jint JNICALL Java_com_ctb_tdc_web_utils_CATEngineProxy_setup_1cat (JNIEnv * env, jclass theclass, jstring subtest) {
	const jchar* subtestS = (*env)->GetStringUTFChars( env, subtest, 0 );
	char* subtestString = (char*) subtestS;
	return setup_cat(subtestString);
}

/* For no obj and aStr case
// To switch from memo8 to memo9
// 1) _n_rate = 0.05; to 0.1
// 2) itemID = adapt_aItemFromAll(theta) to ...
*/  
int setup_cat(char subTest[]) {
   int condition_code = 0;  /* Returned condition code  */
   int i, j;
   double *a;
   char versionInfo[]="TABE CAT version 1.6 (Build 007), 5/25/2012.";
 
   strcpy(_subTest, subTest);   /* @todo set to up case */

   if (LOG_FILE_FLAG) {
       strcpy(log_filename, "");
       /* strcat(log_filename, ".\\Data\\"); */
       strcat(log_filename, "C:\\Program Files\\CTB\\Online Assessment\\Data\\");
       strcat(log_filename, subTest);
       strcat(log_filename, ".log");

       log_file = fopen(log_filename, "w");
  
       if (log_file == NULL) {
           printf ("Error: Couldn't open log file: %s \n", log_filename);
           return SETUP_CAT_FAILURE;
        }

        fprintf(log_file, " %s \n", versionInfo);
   }
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

  /* strcpy(_NoItemsEachObjByLvl_filename, ".\\Data\\NoItemsAnObjByLvl.csv"); */
   strcpy(_NoItemsEachObjByLvl_filename, "C:\\Program Files\\CTB\\Online Assessment\\Data\\NoItemsAnObjByLvl.csv");
   /*
   condition_code = getNadmObj(NoItemsEachObjByLvl_filename, subTest, testLevel, _n_adm_obj);
   if (condition_code != GETN_ADMOBJ_OK) {
	   fprintf(log_file, "Error: getNadmObj failed for subTest %s and Level %c \n", subTest, testLevel);
	   return SETUP_CAT_FAILURE; 
   }
   */
   _mean_c = 0.0;
   if ( !strcmp("MC", subTest)) {  
       _loss = 235;
       _hoss = 755;
       _testLength = 20;  
	//   _min_a =   0.00908;  // min a parameter in 2PPC metric
	//   _mean_b = 4.2713;
	
	   _min_a =   0.02884;   // geometric mean of a parameters in 2PPC metric
       _mean_b =  13.5675, 
	   _mean_c = 0.15624;
   }
   else if ( !strcmp("AM", subTest)) {
       _loss = 200;
       _hoss = 795;  
       _testLength = 25; 
	//   _min_a =  0.00612 ; 
	//   _mean_b = 3.2017; 

	   _min_a =   0.02636;  // geometric mean of a parameters in 2PPC metric
       _mean_b =  13.7912;
	   _mean_c = 0.20595;
   }
   else if ( !strcmp("RD", subTest)) {
       _loss = 175;
       _hoss = 812;  
       _testLength = 25; 
//	    _min_a =  0.00532;
//      _mean_b = 2.5974; 

	   _min_a =   0.02555;  // geometric mean of a parameters in 2PPC metric
       _mean_b =  12.4706;
	   _mean_c = 0.21325;
	  
   }
   else if ( !strcmp("VO", subTest)) {
       _loss = 175;
       _hoss = 812;  
       _testLength = 20;  
   }
   else if ( !strcmp("LA", subTest)) {
       _loss = 235;
       _hoss = 826;   
       _testLength = 25;  /* @todo should be max test length for all levels; then reset after 10 item test. */
//	   _min_a = 0.00869;
//	   _mean_b = 4.3393;

	   _min_a = 0.02631;   // geometric mean of a parameters in 2PPC metric
       _mean_b =  13.1265;
	   _mean_c = 0.21312;
 //      _testLength = 20;
   }
   else if ( !strcmp("LM", subTest)) {
       _loss = 235;
       _hoss = 826;   
       _testLength = 20;  
   }
   else if ( !strcmp("SP", subTest)) {
       _loss = 220;
       _hoss = 745;   
       _testLength = 20;  
   }
   else {} /* @todo */

   _aStrata = 3;    /* for first n items only */
   _n_rate = 0.1;   /* for first n items only */
   _aStrata_o = 1;  /* after 10 item test */
   _1st_nItems = 5;   /* from lowest 1/3 alpha strata */
   _2nd_nItems = 5;   /* from 2nd 1/3 alpha strata */
   mean = 0.5*(_loss + _hoss);
   std = 0.25*(_hoss - _loss);

   strcpy(par_filename, "");
 /*  strcat(par_filename, ".\\Data\\"); */
   strcat(par_filename, "C:\\Program Files\\CTB\\Online Assessment\\Data\\");
   strcat(par_filename, subTest);
   strcat(par_filename, ".csv");
   
   _n_items = get_n_items(par_filename);
   if (_n_items == GET_NITEM_FAILURE ) {
       if (LOG_FILE_FLAG) fprintf(log_file, "Error: get_n_items failed in %s \n", par_filename);
	   return SETUP_CAT_FAILURE; 
   }

   _items = (struct item_info *) malloc(_n_items * sizeof(struct item_info));
	
    if (_items == NULL) {
		if (LOG_FILE_FLAG) fprintf(log_file, "Error: malloc _items failed! \n");
        return  SETUP_CAT_FAILURE;
    }
    
	for ( i = 0; i < MAX_NUM_OBJ; i++) {
        _n_obj_items[i] = 0;    /* initialize to 0 */
		_n_obj_adapted[i] = 0;
	}
	condition_code = getItems(par_filename, _items, _n_items, &_n_obj, 
			  _objID, _n_obj_items, &_n_new_obj, _new_objID);
 /* QA passed */
	/* print out once for info
	j = 0;
    for ( i = 0; i < _n_obj; i++) {
        fprintf(log_file, " %d %s %d \n", _objID[i], _items[j].obj_title, _n_obj_items[i]);
		j = j + _n_obj_items[i]; 
	}
    */
    if (condition_code != GETPAR_OK) {
        if (LOG_FILE_FLAG) fprintf(log_file, "Error: getItems failed %s \n", par_filename); 
        return SETUP_CAT_FAILURE;
	}

   /*
   for ( i = 0; i < _n_obj; i++) {
        _testLength = _testLength + _n_adm_obj[i];
   }
   */ 

	/* set level specified loss and hoss */
	/*
	strcpy(lvlLH_filename,"C:\\Program Files\\CTB\\Online Assessment\\Data\\TabeScaleBounds.csv");
	condition_code = get_LH4lvl(lvlLH_filename, subTest, _lvl_loss, _lvl_hoss);
	if (condition_code != GETPAR_OK) {
        if (LOG_FILE_FLAG) fprintf(log_file, "Error: get_LH4lvl failed in Data\TabeScaleBounds.cvs. \n"); 
        return SETUP_CAT_FAILURE;
	}
	*/
	/* set obj level specified cut scores */
	
	strcpy(objLvlCut_filename, "");
    strcat(objLvlCut_filename, "C:\\Program Files\\CTB\\Online Assessment\\Data\\");
    strcat(objLvlCut_filename, subTest);
 //   strcat(objLvlCut_filename, "-Lvl-Obj-Cuts.csv");
    strcat(objLvlCut_filename, "-Obj-Cuts.csv");
	_objSS_cut = (struct objSScut *) malloc(_n_new_obj * sizeof(struct objSScut));
	
    if (_objSS_cut == NULL) {
		if (LOG_FILE_FLAG) fprintf(log_file, "Error: malloc _objSS_cut failed! \n");
        return  SETUP_CAT_FAILURE;
    }
	condition_code = get_objLvlCut(objLvlCut_filename, _new_objID, _objSS_cut);
	if (condition_code != GETPAR_OK) {
        if (LOG_FILE_FLAG) fprintf(log_file, "Error: get_objLvlCut failed in %s. \n", objLvlCut_filename); 
        return SETUP_CAT_FAILURE;
	}

	/* QA passed
	for ( i = 0; i < _n_new_obj; i++) {
        printf(" %d = %d \n", _new_objID[i], _objSS_cut[i].objID); 
		for (j= 0; j < 4; j++) {
			printf(" %5.0lf \n", _objSS_cut[i].s75[j]); 
		}
	}*/

/* get FT items */
   strcpy(parFT_filename, "");
 /*  strcat(parFT_filename, ".\\Data\\"); */
   strcat(parFT_filename, "C:\\Program Files\\CTB\\Online Assessment\\Data\\");
   strcat(parFT_filename, subTest);
   strcat(parFT_filename, "_FT.csv");
   
   _n_FTitems = get_n_items(parFT_filename);
   if (_n_FTitems == GET_NITEM_FAILURE ) {
       if (LOG_FILE_FLAG) fprintf(log_file, "Error: get_n_items failed in %s \n", parFT_filename);
	   return SETUP_CAT_FAILURE; 
   }

   _FTitems = (struct item_info *) malloc(_n_FTitems * sizeof(struct item_info));
	
    if (_FTitems == NULL) {
		if (LOG_FILE_FLAG) fprintf(log_file, "Error: malloc _FTitems failed! \n");
        return  SETUP_CAT_FAILURE;
    }
    
	/*
	for ( i = 0; i < MAX_NUM_LVL; i++) {
		_n_FTitemsLvl[i] = 0;   // initialize to 0
	}
	*/
	condition_code = getFT_Items(parFT_filename, _FTitems, _n_FTitems, _n_FTitemsLvl);
 
    if (condition_code != GETPAR_OK) {
        if (LOG_FILE_FLAG) fprintf(log_file, "Error: getFT_Items failed %s \n", parFT_filename); 
        return SETUP_CAT_FAILURE;
	}

	/*
	//print out once for info
  //  for ( i = 0; i < MAX_NUM_LVL; i++) {
  //      fprintf(log_file, "level %d = %d \n", i, _n_FTitemsLvl[i]);
//	}
*/
    
    _theta = (double*) malloc((_testLength+1) * sizeof(double));
	if (_theta == NULL) {
		if (LOG_FILE_FLAG) fprintf(log_file, "Error: malloc _theta failed! \n");
        return  SETUP_CAT_FAILURE;
    }
	_theta[0] = mean; // 0.5*(_loss+_hoss);
    
    /* set Q points */
    _tq = (double*) malloc((Qs+1) * sizeof(double));
    _ft = (double*) malloc((Qs+1) * sizeof(double));
    if ((_tq == NULL) || (_ft == NULL) ) {
		if (LOG_FILE_FLAG) fprintf(log_file, "Error: malloc _tq or _ft failed! \n");
        return  SETUP_CAT_FAILURE;
    }

    for ( i = 0; i < Qs; i++) {
        _tq[i] = _loss + (double)i * (_hoss - _loss)/(double)(Qs-1) ; 
	    _ft[i] = getNormDisDen(_tq[i], mean, std);
	}
    _item_adm = (struct item_info *) malloc( _testLength * sizeof(struct item_info));
    if (_item_adm == NULL) {
		if (LOG_FILE_FLAG) fprintf(log_file, "Error: malloc _item_adm failed! \n");
        return  SETUP_CAT_FAILURE;
    }

	
	/* item pool partition i=0, _n_obj - 1;j = 0, aStrata_o -1; k=0,1 */
    
    condition_code = new3DIntArray(_n_obj, _aStrata_o, 2, &_pItems);
    if (condition_code != ARRAY_MALLOC_OK) {
        if (LOG_FILE_FLAG) fprintf(log_file, "Error:  malloc 3D array failed! \n");
	    return SETUP_CAT_FAILURE;	
	} 
/*
    condition_code = new2DIntArray(_n_obj, _aStrata_o, &_nItems_a_c);
    if (condition_code != ARRAY_MALLOC_OK) {
       fprintf(log_file, "Error: malloc 2D array failed! \n");
	   return SETUP_CAT_FAILURE;	
	} 

	_n_a = (int *) malloc(_aStrata_o * sizeof(int));
    _max_nItem_obj = (int *) malloc(_aStrata_o * sizeof(int));
    if ( (_n_a == NULL) || (_max_nItem_obj == NULL) ) {
		fprintf(log_file, "Error: malloc _n_a or _max_nItem_obj failed! \n");
        return  SETUP_CAT_FAILURE;
    }

   // set_pItems();   
   // print_pItems();
  */

    _itemInfo = (double *) malloc(_n_items * sizeof(double));
    _indx = (int *) malloc(_n_items * sizeof(int));
    _indx_a = (int *) malloc(_n_items * sizeof(int));
	a = (double *) malloc(_n_items * sizeof(double));
    if ( (_itemInfo == NULL) || (_indx == NULL) || (_indx_a == NULL ) || (a == NULL )) {
		if (LOG_FILE_FLAG) fprintf(log_file, "Error: malloc _itemInfo or _indx failed! \n");
        return  SETUP_CAT_FAILURE;
    }
	
	_seeds++;
    srand(((int)time(0)+_seeds)); /* set seed at different value for different time. */
    
    /* sort item bank by a parameter value */
    for (i = 0; i < _n_items; i++)
		a[i] = _items[i].parameters[0];

    indexx(_n_items, a, _indx_a);
	_idx1 = _n_items/_aStrata;   
	_idx2 = _n_items*2/_aStrata;
	free(a);
/*	_o = (int *) malloc(_n_obj * sizeof(int));
	
//	set_obj_order(subTest, testLevel); // set randomly obj adapt order
*/
	ran0(-1); /*  for simulation */

	/* initilize variables */
	_is_psg = 0;
	_ik = 0;
    _iadm = 0;
	_iadmFT = 0;
	_is_FTpsg = 0;
	_ik_FT = 0;
	_isFTitem = 0;
	_enemy_item = 0;
	_psg_id = 0;
	_i_stop = -1;
	_report_ss = 0;
	return SETUP_CAT_OK;
}

void set_initial_theta (double theta0){
	_theta[0] = theta0;
}

JNIEXPORT jint JNICALL Java_com_ctb_tdc_web_utils_CATEngineProxy_getTestLength (JNIEnv * env, jclass theclass) {
	return getTestLength();
}

int getTestLength(){
	return (_testLength + _FTtestLength); // retrun total number of OP and FT items
}

int getNumItems(){
	return _n_items;
}

int getNumObj(){
	return _n_obj;
}

JNIEXPORT jint JNICALL Java_com_ctb_tdc_web_utils_CATEngineProxy_get_1nObj (JNIEnv * env, jclass theclass) {
	return get_nObj();
}

int get_nObj(){
	return _n_new_obj;
}

JNIEXPORT jint JNICALL Java_com_ctb_tdc_web_utils_CATEngineProxy_get_1objID (JNIEnv * env, jclass theclass, jint k) {
	return get_objID(k);
}

int get_objID(int k){
	return _new_objID[k];
}

double getLoss(){
	return _loss;
}

double getHoss(){
	return _hoss;
}

void set_psgID(int psg_id){
	_psg_id = psg_id;
}

void set_enemyID(int enemy_id) {
	_enemy_id = enemy_id;
}

int get_enemyID() {
	return _enemy_id;
}

JNIEXPORT jint JNICALL Java_com_ctb_tdc_web_utils_CATEngineProxy_get_1psgID (JNIEnv * env, jclass theclass) {
	return get_psgID();
}
int get_psgID() {
	/*
	int j;
	if ( _items[_ik].item_id == 304698 ) {
	    for (j = 0; j < _n_items; j++) {
		    if (_items[j].enemy == 17 ) {
	           if (LOG_FILE_FLAG) fprintf(log_file, "j = %d  itemID = %d  adm_flag = %d ", j, _items[j].item_id, _items[j].adm_flag);
			}
		}
	}
	*/
	return _psg_id;
}

/*
void set_obj_order(char subTest[], char testLevel){   // set rand obj order
	 int i; //, j, k;
//	 int flag = 0;
     	 
	 if (!strcmp("MC", subTest))  {
		 if ((testLevel == 'L') || ( testLevel == 'E') |
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

	 // set randomly obj order
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
	 
}
*/
JNIEXPORT jint JNICALL Java_com_ctb_tdc_web_utils_CATEngineProxy_next_1item (JNIEnv * env, jclass theclass) {
	return next_item();
}

int next_item() {
	char lvl;
    int itemID;
	int objID_idx;
	int condition_code;
	double theta;
	int new_ik = 0;
	int i = 0;
	int j = 0;
	int k = 0, kj=0;
	int i_FTflag = 0;
	int psg_size = 0;

    if (( (_iadm == _testLength) && (_iadmFT == _FTtestLength )) || (_i_stop >= 0))  return END_ITEM;

    theta = _theta[_iadm];

	/* set target objectives for level */
	if (_iadm == (_1st_nItems + _2nd_nItems ) ) { 
			lvl = get_testLevel(theta, _subTest);
            condition_code = getNadmObj(_NoItemsEachObjByLvl_filename, _subTest, lvl, _n_adm_obj);
            if (condition_code != GETN_ADMOBJ_OK) {
	             if (LOG_FILE_FLAG) fprintf(log_file, "Error: getNadmObj failed for subTest %s and Level %c \n", _subTest, lvl);
	             return NEXT_ITEM_FAILURE; 
			}
			/* sort by obj_id and a parameter?? No! It already was sorted this way. */
			set_pItems();   
		/*	set_testLength(); */
	}

	/*
	// set field test item level
	if (_iadm == (_testLength - _FTinLastNitems )) {
			_lvlFT = get_testLevel(theta, _subTest);
		    if (_lvlFT == 'L') _lvlFT = 'E';  //@todo these can be inside get_testLevel()
	        if (_lvlFT == 'T') _lvlFT = 'D';

			switch(_lvlFT){
		   //    case 'L': ++n_FTitemsLvl[0]; break;  //@todo to match the real levels in FT item bank
		       case 'E': _i_lvlFT = 0; break;  // current set up for level, E, M, D, A only.
               case 'M': _i_lvlFT = 1; break;  //                           0, 1, 2, 3 
		       case 'D': _i_lvlFT = 2; break;
		       case 'A': _i_lvlFT = 3; break;
			   default : printf ("Error: invalid FT level in next_item() call \n"); break;   
		//       case 'T': ++n_FTitemsLvl[3]; break;
		     //  default : fprintf (log_file, "Error: invalid level in next_item() call \n"); break;    
		    }
	//		printf("i_lvlFT =  %d lvlFT = %c  theta = %f \n", _i_lvlFT, _lvlFT, theta);
	}
	*/
	
	if (_iadm < _testLength ) {
	    if ( _is_psg ) {  
		    itemID = next_psg_item(_items[_ik].psg_id);
		    if ( itemID != NEXT_ITEM_FAILURE ) {
			    _isFTitem = 0;   
			    update_iBank();
/*			printf("item_no = %d, itemID = %d, psg_id = %d, item_order = %d   \n", _items[_ik].item_no, itemID, _items[_ik].psg_id, _items[_ik].item_order);
*/
                return itemID;
		    }
		    else {  /* end of passage item */
				// turn off enemy passage item here.
			//	turnOff_enemy(); moved to update_ibank()
				_is_psg = 0;		// this may never be reached??
			}
	     }
	}

	if ((_isFTitem) && (_iadmFT < _FTtestLength) ) {
	     if ( _is_FTpsg ) {  /* assume no passage item in FT */
		    itemID = next_FTpsg_item(_FTitems[_ik_FT].psg_id);
		    if ( itemID != NEXT_ITEM_FAILURE ) {
			    update_FTiBank();
/*			printf("item_no = %d, itemID = %d, psg_id = %d, item_order = %d   \n", _items[_ik].item_no, itemID, _items[_ik].psg_id, _items[_ik].item_order);
*/
                return itemID;
		    }
		    else _is_FTpsg = 0;
	     }
	}
	/* deal with field test items */
	if ((_iadm >= (_testLength - _FTinLastNitems )) && (_iadmFT < _FTtestLength)) {
		i_FTflag = randomNoBetween(0, 1);
	/*	printf("random = %d \n", i_FTflag); */
        if ( i_FTflag || (_iadm >= _testLength) ) { /* gives FT items */
			itemID =  adapt_aFTitem();
			if ( itemID != NEXT_ITEM_FAILURE ) {
					update_FTiBank();
					return itemID;
			}
		}
	}

	/* memo 9 */
	if (_iadm < _testLength ) {
		_isFTitem = 0;
        if (_iadm < _1st_nItems )
            itemID = adapt_aItemFromIdx(theta, 0, _idx1);
	    else if ((_iadm >= _1st_nItems) && ( _iadm < (_1st_nItems + _2nd_nItems ) ) )
		    itemID = adapt_aItemFromIdx2(theta, _idx1, _idx2);
	    else {	
            objID_idx = get_objID_idx();
		    itemID = adapt_aItem(objID_idx, (_aStrata_o -1), theta); 
	    }  
	    if ( itemID != NEXT_ITEM_FAILURE ) 
            update_iBank();
	    else {
            if (LOG_FILE_FLAG) fprintf(log_file, "Error: NEXT_ITEM_FAILURE in next_item() \n");
		}
	}
/*	printf("item_no = %d, itemID = %d, psg_id = %d, item_order = %d   \n", _items[_ik].item_no, itemID, _items[_ik].psg_id, _items[_ik].item_order);
*/
	return itemID;
}



int adapt_aFTitem() {
	int i,j,kj,psg_size;
	int itemID;
	int findItemFlag = 0;
            _isFTitem = 1;
			i = randomNoBetween(0, (_n_FTitems-1));
			if (_FTitems[i].psg_id) 
                 psg_size = get_FTpsg_size(_FTitems[i].psg_id);
			else psg_size = 1;

			if ((_FTitems[i].adm_flag ) && (psg_size <= (_FTtestLength -_iadmFT)) ) {
				    findItemFlag = 1;
					_ik_FT = i;
			}
			else { /* looking for next items. */
				if (i < (int)_n_FTitems*0.5) {
					 for (kj=(i+1); kj < _n_FTitems; kj++) {
		                 if (_FTitems[kj].psg_id) 
                              psg_size = get_FTpsg_size(_FTitems[kj].psg_id);
					     else psg_size = 1;
				         if (_FTitems[kj].adm_flag && (psg_size <= (_FTtestLength -_iadmFT))) {
					          _ik_FT = kj;
							  findItemFlag = 1;
							  break;
						 }
					 }
				}
				else {
					for (kj=(i-1); kj >= 0; kj--) {
		                 if (_FTitems[kj].psg_id) 
                              psg_size = get_FTpsg_size(_FTitems[kj].psg_id);
					     else psg_size = 1;
				         if (_FTitems[kj].adm_flag && (psg_size <= (_FTtestLength -_iadmFT))) {
					          _ik_FT = kj;
							  findItemFlag = 1;
							  break;
						 }
					 }
				}
			} 

			if (! findItemFlag ) {  /* still can not find FT item, so search all FT items.
		//		printf(" *** findItemFlag in adapt_aFTitem() *** \n"); */

				for (j = 0; j < _n_FTitems; j++) {
					if (_FTitems[j].psg_id) 
                              psg_size = get_FTpsg_size(_FTitems[j].psg_id);
					else psg_size = 1;
					if (_FTitems[j].adm_flag && (psg_size <= (_FTtestLength -_iadmFT))) {
						   _ik_FT = j;
						   findItemFlag = 1;
						   break;
					}
				}
			}
		
			if (! findItemFlag  ) {
				    if (LOG_FILE_FLAG) fprintf(log_file, "Error: run out of FT_ITEMs in adapt_aFTitem() \n");
			        return NEXT_ITEM_FAILURE;
			}

			itemID = get_FTitemID_fromIdx(_ik_FT); 
			if ( itemID != NEXT_ITEM_FAILURE ) {
					return itemID;
			}
			else {
					if (LOG_FILE_FLAG) fprintf(log_file, "Error: return FT_ITEM_FAILURE in adapt_aFTitem() \n");
			        return NEXT_ITEM_FAILURE;
			}
}

int get_objID_idx(){
    double r; 
	double r_max = -1;
    int i;
	int i_max = 0;
	
	for ( i = 0; i < _n_obj; i++) {
	/*	printf("_n_obj_adapted, i= %d,  %d ",i, _n_obj_adapted[i]); */
		if (_n_adm_obj[i] > _n_obj_adapted[i]) {
            r = (double)(_n_adm_obj[i] - _n_obj_adapted[i])/_n_adm_obj[i];
   /*		printf(" i = %d, _n_obj_adapted = %d , r = %3.2lf ", i, _n_obj_adapted[i], r); */
			if (r > r_max ) {
				r_max = r;
				i_max = i;
			}
		}
    }
/*	printf(" imax = %d, rmax = %3.2lf  \n ", i_max, r_max); */
	return i_max;
}

/*  // To active this function, only if _testLength varies with test level.
void set_testLength() {
	int i;
	_testLength = 0;
	for ( i = 0; i < _n_obj; i++) {
        _testLength = _testLength + _n_adm_obj[i];
    }
}
*/

char get_testLevel(double theta, char subTest[]){
	char lvl = 'M';
	
/*	if ( !strcmp("TM", subTest)) {  // total maths
        if (theta <= 313) lvl = 'L';
        else if ((theta > 313) && (theta <= 441) ) lvl = 'E';
        else if ((theta > 441) && (theta <= 505) ) lvl = 'M';
        else if ((theta > 505) && (theta <= 565) ) lvl = 'D';
        else if ((theta > 565) && (theta <= 594) ) lvl = 'A';
        else lvl = 'T';
	} */

	if ( !strcmp("MC", subTest)) {
        if (theta <= 304) lvl = 'L';
        else if ((theta > 304) && (theta <= 435) ) lvl = 'E';
        else if ((theta > 435) && (theta <= 506) ) lvl = 'M';
        else if ((theta > 506) && (theta <= 575) ) lvl = 'D';
        else if ((theta > 575) && (theta <= 602) ) lvl = 'A';
        else lvl = 'T';
	}
    else if ( !strcmp("AM", subTest)) {
        if (theta <= 349) lvl = 'L';
        else if ((theta > 349) && (theta <= 444) ) lvl = 'E';
        else if ((theta > 444) && (theta <= 503) ) lvl = 'M';
        else if ((theta > 503) && (theta <= 558) ) lvl = 'D';
        else if ((theta > 558) && (theta <= 589) ) lvl = 'A';
        else lvl = 'T';
	}
	else if ( !strcmp("RD", subTest) || !strcmp("VO", subTest)) {
		if (theta <= 367) lvl = 'L';
        else if ((theta > 367) && (theta <= 460) ) lvl = 'E';
        else if ((theta > 460) && (theta <= 517) ) lvl = 'M';
        else if ((theta > 517) && (theta <= 566) ) lvl = 'D';
        else if ((theta > 566) && (theta <= 595) ) lvl = 'A';
        else lvl = 'T';
	} 
	else if ( !strcmp("LA", subTest) || !strcmp("LM", subTest)) {
		if (theta <= 389) lvl = 'L';
        else if ((theta > 389) && (theta <= 490) ) lvl = 'E';
        else if ((theta > 490) && (theta <= 523) ) lvl = 'M';
        else if ((theta > 523) && (theta <= 559) ) lvl = 'D';
        else if ((theta > 559) && (theta <= 585) ) lvl = 'A';
        else lvl = 'T';
	} 
	else if ( !strcmp("SP", subTest)) {
        if (theta <= 338) lvl = 'L';
        else if ((theta > 338) && (theta <= 394) ) lvl = 'E';
        else if ((theta > 394) && (theta <= 467) ) lvl = 'M';
        else if ((theta > 467) && (theta <= 539) ) lvl = 'D';
        else if ((theta > 539) && (theta <= 577) ) lvl = 'A';
        else lvl = 'T';
	}
	else {} /* @todo */

	return lvl;
}

double get_theta0(double theta, char subTest[], int level){
	double theta0;

	if ( !strcmp("MC", subTest)) { /* simulation for MC only */
	/*	loss = 235;   hoss = 755; */
		switch(level){
		   case -1:  /* guess one level lower */
               if (theta <= 304) theta0 = 0.5*(_loss + 304);
               else if ((theta > 304) && (theta <= 435) ) theta0 = 0.5*(_loss + 304);
               else if ((theta > 435) && (theta <= 506) ) theta0 = 0.5*(435 + 305);
               else if ((theta > 506) && (theta <= 575) ) theta0 = 0.5*(436 + 506);
               else if ((theta > 575) && (theta <= 602) ) theta0 = 0.5*(575 + 507);
               else theta0 = 0.5*(576 + 602);
		       break;
		   case 0:  /* guess on level */
               if (theta <= 304) theta0 = 0.5*(_loss + 304);
               else if ((theta > 304) && (theta <= 435) ) theta0 = 0.5*(435 + 305);
               else if ((theta > 435) && (theta <= 506) ) theta0 = 0.5*(436 + 506);
               else if ((theta > 506) && (theta <= 575) ) theta0 = 0.5*(575 + 507);
               else if ((theta > 575) && (theta <= 602) ) theta0 = 0.5*(576 + 602);
               else theta0 = 0.5*(_hoss + 603);
		       break;
           case 1:  /* guess one level higher*/
               if (theta <= 304) theta0 = 0.5*(435 + 305);
               else if ((theta > 304) && (theta <= 435) ) theta0 = 0.5*(436 + 506);
               else if ((theta > 435) && (theta <= 506) ) theta0 = 0.5*(575 + 507);
               else if ((theta > 506) && (theta <= 575) ) theta0 = 0.5*(576 + 602);
               else if ((theta > 575) && (theta <= 602) ) theta0 = 0.5*(_hoss + 603);
               else theta0 = 0.5*(_hoss + 603);
		       break;

		   default : theta0 = 0.5*(_hoss + _loss); break; /* start from the middle of scale in the pool */
		}
	}
	else
		theta0 = 0.5*(_hoss + _loss); // other contents than MC

	return theta0;
}

/*
JNIEXPORT jchar JNICALL Java_com_ctb_tdc_web_utils_CATEngineProxy_get_1objLvl (JNIEnv * env, jclass theclass, jdouble theta) {
	return get_objLvl(theta);
}

char get_objLvl(double theta){
	char lvl = 'M';

	if ( !strcmp("MC", _subTest)) {
        if ( theta <= 435 ) lvl = 'E';
        else if ((theta > 435) && (theta <= 506) ) lvl = 'M';
        else if ((theta > 506) && (theta <= 575) ) lvl = 'D';
        else lvl = 'A';
	}
    else if ( !strcmp("AM", _subTest)) {
        if (theta <= 444) lvl = 'E';
        else if ((theta > 444) && (theta <= 503) ) lvl = 'M';
        else if ((theta > 503) && (theta <= 558) ) lvl = 'D';
        else lvl = 'A';  
	}
	else if ( !strcmp("RD", _subTest) || !strcmp("VO", _subTest)) {
		if (theta <= 460) lvl = 'E';
        else if ((theta > 460) && (theta <= 517) ) lvl = 'M';
        else if ((theta > 517) && (theta <= 566) ) lvl = 'D';
        else lvl = 'A';
	} 
	else if ( !strcmp("LA", _subTest) || !strcmp("LM", _subTest)) {
		if (theta <= 490) lvl = 'E';
        else if ((theta > 490) && (theta <= 523) ) lvl = 'M';
        else if ((theta > 523) && (theta <= 559) ) lvl = 'D';
        else lvl = 'A';
	} 
	else {} 

	return lvl;
}
*/
/*
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
*/
int adapt_aItemFromIdx(double theta, int idx1, int idx2) {  /* no obj and alpha stratificatoin constraint.
    // found best n items with highest item info at theta
	// randomly select one of above n items
	// return item ID and set it as adminitrated. */
   int k, i=0;
   int n_cell_items = 0; 
   int ns;  /* from best 5 items */
   int itemID;
   int psg_size = 1;
   int j;
   int findItemFlag = 0;
   int min_psg_size = 10;
   int save_ik = -1;
   int findItemFlag2 = 0;
   
   for (k = idx1; k < idx2; k++) { 
	   if ( _items[_indx_a[k]].adm_flag ) {
		   n_cell_items ++;
		   if (!_items[_indx_a[k]].psg_id) 
	           _itemInfo[i] = fisherInfo(theta, _items[_indx_a[k]].parameters);  
		   else /* average info over the same psg_id */
			    _itemInfo[i] = psg_info(_items[_indx_a[k]].psg_id, theta);
	   }
	   else
		   _itemInfo[i] = -9.0;
	   i++;
   }

   k = idx2 - idx1;
   
   if (n_cell_items < 1) {
	   if (LOG_FILE_FLAG) fprintf(log_file, "Error: run out of item in pool \n");
	   return NEXT_ITEM_FAILURE;
   }
   indexDescend( k, _itemInfo, _indx);  
   /*
   printf("theta = %lf \n", theta);
   for (i =0; i < 11; i++) {
	   printf("i = %d, ID = %d, info = %lf \n", i, _items[_indx_a[_indx[i] + idx1]].item_id, _itemInfo[_indx[i]]);
   }
   */

   /* set best 10% in the pool */
   ns = (int)( _n_rate * n_cell_items );
   if (ns < 1 ) 
       ns = n_cell_items;

   /* rand select one of first n _indx, available items are n_cell_items */
   if (ns == 1 )
	  i = 0;
   else
      i = randomNoBetween(0, (ns -1));  /* rand select from best ns items */

//   printf(" random i = %d, ns = %d \n", i, ns);

/* check passage size in the starting item groups.
   for (j = 0; j < ns; j++) {		    
			        _ik = _indx_a[_indx[j] + idx1];
					if ( _items[_ik].adm_flag ) {
						      psg_size = 1;
			             if (_items[_ik].psg_id) {
			                  psg_size = get_psg_size(_items[_ik].psg_id);	
						 }
                    printf(" j = %d, ItemID = %d, psg_id = %d, psg_size= %d \n", j,  _items[_ik].item_id, _items[_ik].psg_id, psg_size);
					}
   }
*/
   _ik = _indx_a[_indx[i] + idx1];
 //  save_ik = _ik;
   if (_items[_ik].psg_id) {
	   psg_size = get_psg_size(_items[_ik].psg_id);
/*	   printf("psg_size = %d \n", psg_size); */
 // first 5 items to avoid psg_size > 2, _max_psg_size_1st_nItems
	   if (psg_size > _max_psg_size_1st_nItems) {
			   if (i > (int) ns*0.5)  {
		         for (j = (i-1); j >= 0; j--) {
			        _ik = _indx_a[_indx[j] + idx1];
					if ( _items[_ik].adm_flag ) {
			             if (_items[_ik].psg_id) {
			                  psg_size = get_psg_size(_items[_ik].psg_id);
						      if (_max_psg_size_1st_nItems >= psg_size ) {findItemFlag = 1; break;}
			              }
			              else {findItemFlag = 1; break;}
					}
			     }
		       }
	           else {
			      for (j = (i+1); j < ns; j++) {
			        _ik = _indx_a[_indx[j] + idx1];
					if ( _items[_ik].adm_flag ) {
			            if (_items[_ik].psg_id) {
			                psg_size = get_psg_size(_items[_ik].psg_id);
			                if (_max_psg_size_1st_nItems >= psg_size ) {findItemFlag = 1; break;}
			            }
			            else {findItemFlag = 1; break;}
					}
			      }
		      }
	      	   
		   /* if still do not find, we need search other way around. */
	     if (!findItemFlag) {
	/*		   printf(" *** findItemFlag in adapt_aItemFromIdx() *** \n"); */
			   for (j = 0; j < ns; j++) {		    
			        _ik = _indx_a[_indx[j] + idx1];
					if ( _items[_ik].adm_flag ) {
			             if (_items[_ik].psg_id) {
			                  psg_size = get_psg_size(_items[_ik].psg_id);	
							  /* This results in the same starting item if psg_size always > max_psg_size_1st_nItems ?*/
                              if (psg_size < min_psg_size) {
								  min_psg_size = psg_size;
								  save_ik = _ik;
							  }
							  if (_max_psg_size_1st_nItems >= psg_size ) {findItemFlag2 =1; break;}
			              }
			              else { findItemFlag2 =1; break;}
					}
			   }
		}	
	    if ((!findItemFlag2) && (!findItemFlag) && (save_ik != -1)) { // still do not find the item with psg_size < min_psg_size.
			_ik = save_ik;
		}
/*	   printf("psg_size2 = %d \n", psg_size); */
      }
   }

   itemID = get_itemID_fromIdx(_ik);
/*   printf("itemID = %d \n", itemID); */

   return itemID;
}

int adapt_aItemFromIdx2(double theta, int idx1, int idx2) {  /* no obj and alpha stratificatoin constraint.
    // found best n items with highest item info at theta
	// randomly select one of above n items
	// return item ID and set it as adminitrated. */
   int k, i=0;
   int n_cell_items = 0; 
   int ns;  /* from best 5 items */
   int itemID;
   int psg_size = 1;
   int j;
   int findItemFlag = 0;
   int min_psg_size = 10;
   int save_ik = -1;
   int findItemFlag2 = 0;

   for (k = idx1; k < idx2; k++) { 
	   if ( _items[_indx_a[k]].adm_flag ) {
		   n_cell_items ++;
		   if (!_items[_indx_a[k]].psg_id) 
	           _itemInfo[i] = fisherInfo(theta, _items[_indx_a[k]].parameters);  
		   else /* average info over the same psg_id */
			    _itemInfo[i] = psg_info(_items[_indx_a[k]].psg_id, theta);
	   }
	   else
		   _itemInfo[i] = -9.0;
	   i++;
   }

   k = idx2 - idx1;
   
   if (n_cell_items < 1) {
	   if (LOG_FILE_FLAG) fprintf(log_file, "Error: run out of item in pool \n");
	   return NEXT_ITEM_FAILURE;
   }
   indexDescend( k, _itemInfo, _indx);  
   /*
   printf("theta = %lf \n", theta);
   for (i =0; i < 11; i++) {
	   printf("i = %d, ID = %d, info = %lf \n", i, _items[_indx_a[_indx[i] + idx1]].item_id, _itemInfo[_indx[i]]);
   }
   */

   /* set best 10% in the pool */
   ns = (int)( _n_rate * n_cell_items );
   if (ns < 1 ) 
       ns = n_cell_items;

   /* rand select one of first n _indx, available items are n_cell_items */
   if (ns == 1 )
	  i = 0;
   else
      i = randomNoBetween(0, (ns -1));  /* rand select from best ns items */

   _ik = _indx_a[_indx[i] + idx1];

   /* check k = 10 and size of psg to make sure
   // _1st_nItems + _2nd_nItems - (_iadm + 1) >= size of psg */
   if (_items[_ik].psg_id) {
	   psg_size = get_psg_size(_items[_ik].psg_id);
/*	   printf("psg_size = %d \n", psg_size); */

	   if ((_1st_nItems + _2nd_nItems - (_iadm + 1)) < psg_size ) {
		   if (i > (int) ns*0.5) {
		       for (j = (i-1); j >= 0; j--) {
			        _ik = _indx_a[_indx[j] + idx1];
					if ( _items[_ik].adm_flag ) {
			             if (_items[_ik].psg_id) {
			                  psg_size = get_psg_size(_items[_ik].psg_id);
						      if ((_1st_nItems + _2nd_nItems - (_iadm + 1)) >= psg_size ) {findItemFlag = 1; break;}
			                  }
			              else {findItemFlag = 1; break;}
					}
			   }
		   }
	       else {
			   for (j = (i+1); j < ns; j++) {
			        _ik = _indx_a[_indx[j] + idx1];
					if ( _items[_ik].adm_flag ) {
			            if (_items[_ik].psg_id) {
			                psg_size = get_psg_size(_items[_ik].psg_id);
			                if ((_1st_nItems + _2nd_nItems - (_iadm + 1)) >= psg_size ) {findItemFlag = 1; break;}
			            }
			            else {findItemFlag = 1; break;}
					}
			   }
		   }
	   }
	   		   /* if still do not find, we need search other way around. */
	     if (!findItemFlag) {
	/*		   printf(" *** findItemFlag in adapt_aItemFromIdx() *** \n"); */
			   for (j = 0; j < ns; j++) {		    
			        _ik = _indx_a[_indx[j] + idx1];
					if ( _items[_ik].adm_flag ) {
			             if (_items[_ik].psg_id) {
			                  psg_size = get_psg_size(_items[_ik].psg_id);
                              if (psg_size < min_psg_size) {
								  min_psg_size = psg_size;
								  save_ik = _ik;
							  }
							  if ((_1st_nItems + _2nd_nItems - (_iadm + 1)) >= psg_size ) {findItemFlag2 =1; break;}
			              }
			              else { findItemFlag2 =1; break;}
					}
			   }
		}	
	    if ((!findItemFlag2) && (!findItemFlag) && (save_ik != -1)) { // still do not find the item, then give the items with min psg size.
			_ik = save_ik;
		}
/*	   printf("psg_size2 = %d \n", psg_size); */
   }

   itemID = get_itemID_fromIdx(_ik);
/*   printf("itemID = %d \n", itemID); */

   return itemID;
}
int get_psg_size(int psg_id){
	int psg_size = 0;
	int j;
	for (j = 0; j < _n_items; j++) {
		if ( psg_id == _items[j].psg_id )
		    psg_size ++;
	}
	return psg_size;
}

int get_FTpsg_size(int psg_id){
	int psg_size = 0;
	int j;
	for (j = 0; j < _n_FTitems; j++) {
		if ( psg_id == _FTitems[j].psg_id )
		    psg_size ++;
	}
	return psg_size;
}

int get_FTitemID_fromIdx(int idx) {
	int itemID = NEXT_ITEM_FAILURE;
	if (!_FTitems[idx].psg_id) {
       itemID = _FTitems[idx].item_id;
	   _is_FTpsg = 0;
    }
    else {
	   _is_FTpsg = 1;
	   if (_FTitems[idx].item_order == 1) itemID = _FTitems[idx].item_id;
	   else itemID = get_1st_FTpsg_item(_FTitems[idx].psg_id);
    }
    return itemID;
}

int get_itemID_fromIdx(int idx) {
	int itemID = NEXT_ITEM_FAILURE;
	if (!_items[idx].psg_id) {
		if (_items[idx].adm_flag) {
              itemID = _items[idx].item_id;
	          _is_psg = 0;
		}
		else {
			 if (LOG_FILE_FLAG) fprintf(log_file, "Error: get_itemID_fromIdx failed for item idx = %d , this item ID = %d was already taken \n", idx, _items[idx].item_id);
             return NEXT_ITEM_FAILURE;
		}
    }
    else {
	   _is_psg = 1;
	   if (_items[idx].item_order == 1) itemID = _items[idx].item_id;
	   else itemID = get_1st_psg_item(_items[idx].psg_id);
    }
    return itemID;
}

int get_1st_psg_item(int psg_id) {
	int itemID = NEXT_ITEM_FAILURE;
	int j;
    for (j = 0; j < _n_items; j++) {
		if ((psg_id == _items[j].psg_id ) && (_items[j].item_order == 1)){
		     itemID = _items[j].item_id;
		     _ik = j;  /* update _ik */
		     return itemID;
		}
	}
	return itemID;
}

int get_1st_FTpsg_item(int psg_id) {
	int itemID = NEXT_ITEM_FAILURE;
	int j;
    for (j = 0; j < _n_FTitems; j++) {
		if ((psg_id == _FTitems[j].psg_id ) && (_FTitems[j].item_order == 1)){
		     itemID = _FTitems[j].item_id;
		     _ik_FT = j;  /* update _ik_FT */
		     return itemID;
		}
	}
	return itemID;
}

int next_FTpsg_item(int psg_id){
	int itemID = NEXT_ITEM_FAILURE;
	int j;
    for (j = 0; j < _n_FTitems; j++) {
		if ((psg_id == _FTitems[j].psg_id ) && ((_FTitems[j].item_order == (_FTitems[_ik_FT].item_order + 1)))){
		    itemID = _FTitems[j].item_id;
		    _ik_FT = j;
			return itemID;
	    }
	}
	return itemID;
}

int next_psg_item(int psg_id){
	int itemID = NEXT_ITEM_FAILURE;
	int j;
    for (j = 0; j < _n_items; j++) {
		if ((psg_id == _items[j].psg_id ) && ((_items[j].item_order == (_items[_ik].item_order + 1)))){
		    itemID = _items[j].item_id;
		    _ik = j;
			return itemID;
	    }
	}
	return itemID; /* NEXT_ITEM_FAILURE for the end of passage item */
}

double psg_info(int psg_id, double theta) {
/* average info over the same psg_id */
	double itemInfo = 0.0; 
	int n_psg = 0;
	int j;
	for (j = 0; j < _n_items; j++) {
		if (psg_id == _items[j].psg_id ) {
			 itemInfo = itemInfo + fisherInfo(theta, _items[j].parameters); 
			 n_psg++;
		}
	}
	if (n_psg > 0)
	    itemInfo = itemInfo / (double)n_psg;
	else {
		if (LOG_FILE_FLAG) fprintf(log_file,"Error: no psg item for psg_id = %d in psg_info \n", psg_id);
	}

	return itemInfo;
}


int adapt_aItemFromAll(double theta) {  /* no obj and alpha stratificatoin constraint, from not psg items only first!
										if none-psg item runs out, then use psg items; if psg item also run out, return error.
    // found best n items with highest item info at theta
	// randomly select one of above n items
	// return item ID and set it as adminitrated.*/
   int k, i=0;
   int n_cell_items = 0;
   
   int ns;  /* from best 10 items */
   int itemID;
   
   for (k = 0; k < _n_items; k++) { 
	   if ((_items[k].adm_flag ) && (!_items[k].psg_id)) {  /* from none-psg-items only */
	       _itemInfo[i] = fisherInfo(theta, _items[k].parameters);  
	       n_cell_items ++;
	   }
	   else
		   _itemInfo[i] = -9.0;
	   i++;
   }

   if (n_cell_items < 1) {
	   if (LOG_FILE_FLAG) fprintf(log_file, "Warning: run out of not-psg-item in pool \n");
	   /* In LA, we run out of none-psg item in the pool, so we will use psg-items */
	   i = 0;
	   n_cell_items = 0;
       for (k = 0; k < _n_items; k++) { 
	     if (_items[k].adm_flag ) {
			 n_cell_items ++;
		     if (!_items[k].psg_id)  /* from none-psg-items only */
	           _itemInfo[i] = fisherInfo(theta, _items[k].parameters);  
		     else   /* for the psg items */
               _itemInfo[i] = psg_info(_items[k].psg_id, theta);
	     }
	     else
		   _itemInfo[i] = -9.0;
	     i++;
       }   
   }
   if (n_cell_items < 1) {
	      if (LOG_FILE_FLAG) fprintf(log_file, "Error: run out of item in pool \n");
	      return NEXT_ITEM_FAILURE;
   }
   indexDescend( _n_items, _itemInfo, _indx);  

      /* set best 10% in the pool */
   ns = (int)( _n_rate * n_cell_items );
   if (ns < 1 ) 
       ns = n_cell_items;
   if (ns > 5) ns = 5; /* one of best 5 items */

   /* rand select one of first n _indx, available items are n_cell_items */
   if (ns == 1 )
	  i = 0;
   else
      i = randomNoBetween(0, (ns -1));  /* rand select from best ns items */

   _ik = _indx[i];
   /*
   if ( (_itemInfo[_ik] < 0 ) || (!_items[_ik].adm_flag ))
	   itemID = 0;
	*/

   itemID = get_itemID_fromIdx(_ik);
 //  itemID = _items[_ik].item_id;

   return itemID;
}

int adapt_aItem(int obj, int aSt, double theta) {
    /* found best n items with highest item info at theta
	// randomly select one of above n items
	// return item ID and set it as adminitrated. */
   int k, i=0;
   int n_cell_items = 0;
   
 /*  int ns;  // from best 5 items */
   int itemID;
   int psg_size = 1;
   int psg_size_flag = 1;
  
   for (k = _pItems[obj][aSt][0]; k <= _pItems[obj][aSt][1]; k++) { 
	   if ( _items[k].adm_flag ) {
		   n_cell_items ++;
		   if (!_items[k].psg_id) 
	           _itemInfo[i] = fisherInfo(theta, _items[k].parameters);     
		   else /* average info over the same psg_id */
			   _itemInfo[i] = psg_info(_items[k].psg_id, theta);
	   }
	   else
		   _itemInfo[i] = -9.0;
	   i++;
   }
   k = _pItems[obj][aSt][1] - _pItems[obj][aSt][0] + 1;
   
   if (n_cell_items < 1) {
	   if (LOG_FILE_FLAG) fprintf(log_file, "Error: run out of item in pool \n");
	   return NEXT_ITEM_FAILURE;
   }
   
   indexDescend( k, _itemInfo, _indx);  /* @todo */
  /*
   // i = 0;   // pick up the best one	
   // _ik = _pItems[obj][aSt][0] + _indx[i];

   // make sure _items[ik].psg_id size <= _testLength - (_iadm + 1)
   */
   for (i = 0; i < n_cell_items; i++) {
	    _ik = _pItems[obj][aSt][0] + _indx[i];
	    if (_items[_ik].psg_id) {   /* psg_id = 0 for non-passage item. */
			 psg_size = get_psg_size(_items[_ik].psg_id);
			 if ((_testLength - _iadm ) >= psg_size ) {
				 psg_size_flag = 0;
				 break;
			 }
	    }
	    else {
			psg_size_flag = 0;
			break;
		}
   }

   if (psg_size_flag) {
	   if (LOG_FILE_FLAG) fprintf(log_file, "Warning: items in obj = %d failed to meet [psg_size <= _testLength - (iadm+1)] condition. \n", obj);
       _ik = _pItems[obj][aSt][0] + _indx[0]; /* to adapted the item with the highest info, regardless its psg_size. */
   }

   itemID = get_itemID_fromIdx(_ik);
   if ( itemID == NEXT_ITEM_FAILURE ) {
	   if (LOG_FILE_FLAG) fprintf(log_file, "Warning: run out of items in obj = %d. \n", obj);
	   /* to adapt a none psg item from the whole item bank first; if failed, then use psg items */
	   itemID = adapt_aItemFromAll(theta); /* randomly pick up one of top 10% items in the pool */
	   if ( itemID == NEXT_ITEM_FAILURE ) {
	        if (LOG_FILE_FLAG) fprintf(log_file, "Error: run out of items in the whole item bank  \n");
	   }
   }
   return itemID;
}

void update_iBank(){
	int i;
	int itemID =0;
	int save_ik;
   _items[_ik].adm_flag = 0;   /* set this items as administrated */

   /* copy items to item_adm with continue index */
   _item_adm[_iadm].item_id = _items[_ik].item_id;
   _item_adm[_iadm].item_no = _items[_ik].item_no;
   _item_adm[_iadm].obj_id = _items[_ik].obj_id;
   _enemy_item = _items[_ik].enemy;

   /*
   printf("_iadm = %d , itemID = %d, psgID = %d, itemOrder = %d, obj_id = %d ", _iadm, _item_adm[_iadm].item_id, _items[_ik].psg_id, _items[_ik].item_order, _item_adm[_iadm].obj_id); 
   printf(" _is_psg = %d \n", _is_psg);
   */
   _item_adm[_iadm].new_obj_id = _items[_ik].new_obj_id;  /* collapsed obj */

   /* trace number of items in obj[j] have been adapted */
   for (i = 0; i < _n_obj; i++) {
	   if (_objID[i] == _item_adm[_iadm].obj_id) {
		   _n_obj_adapted[i] ++;
		   break;
	   }
   }

   _item_adm[_iadm].parameters = (double *) malloc( 3*sizeof(double) );
    if (_item_adm[_iadm].parameters == NULL)  {
		if (LOG_FILE_FLAG) fprintf(log_file, "Error: malloc _item_adm[%d].parameters failed! \n", _iadm );
		exit(EXIT_FAILURE);
/*		return ADAPT_N_ITEM_FAILURE; */
    }
   _item_adm[_iadm].parameters[0] = _items[_ik].parameters[0];
   _item_adm[_iadm].parameters[1] = _items[_ik].parameters[1];
   _item_adm[_iadm].parameters[2] = _items[_ik].parameters[2];

   if (_items[_ik].psg_id) 
	   set_psgID(_items[_ik].psg_id);
   else
       set_psgID(0);

   if (_enemy_item ) {
	   set_enemyID(_enemy_item);
	   if (!_is_psg) 
	       turnOff_enemy();  /* turned off enemy items if adapted item is not psg item. */
	   else {  // if it is a passage item
		   save_ik = _ik;
	       itemID = next_psg_item(_items[_ik].psg_id);  // do not update _ik in this call!!
		   _ik = save_ik;
	       if ( itemID == NEXT_ITEM_FAILURE ) {  // end of passage item
		        turnOff_enemy(); 
		        _is_psg =0;
	       }
	   }
   }
   else set_enemyID(0);

   _iadm++;
}

void turnOff_enemy(){
	int j;
	if (_enemy_item) {
	    for (j = 0; j < _n_items; j++) {
		    if (_enemy_item == _items[j].enemy ) {
			   _items[j].adm_flag = 0; 
			}
	    }
	}
	_enemy_item = 0;
}

void update_FTiBank(){
   _FTitems[_ik_FT].adm_flag = 0;   /* set this items as administrated */
   set_psgID(0); // @todo: we assume there is no psg items in FT items and initialize id for OP items.
   _enemy_item = 0;
   set_enemyID(0); // @todo: we assume there is no enemy items in FT items and initialize id for OP items.
   
   _iadmFT++;
}

void update_iBank_stop(){

   /* copy items to item_adm with continue index */
   _item_adm[_iadm].obj_id = 0;
   _item_adm[_iadm].new_obj_id = 0;  /* collapsed obj */

   _item_adm[_iadm].parameters = (double *) malloc( 3*sizeof(double) );
    if (_item_adm[_iadm].parameters == NULL)  {
		if (LOG_FILE_FLAG) fprintf(log_file, "Error: malloc _item_adm[%d].parameters failed! \n", _iadm );
		exit(EXIT_FAILURE);
/*		return ADAPT_N_ITEM_FAILURE; */
    }

   _item_adm[_iadm].parameters[0] = _min_a;
   _item_adm[_iadm].parameters[1] = _mean_b;
   _item_adm[_iadm].parameters[2] = _mean_c;

   _iadm++;
}

JNIEXPORT void JNICALL Java_com_ctb_tdc_web_utils_CATEngineProxy_resumeCAT (JNIEnv * env, jclass theclass, jint nItems, jintArray itemIDs, jintArray rwos) {
	jint* itemID = (*env)->GetIntArrayElements(env, itemIDs, 0);
	jint* rwo = (*env)->GetIntArrayElements(env, rwos, 0);
	int * itemID0 = (int*) itemID;
	int * rwo0 = (int*) rwo;
	resumeCAT(nItems, itemID0, rwo0);
}

void resumeCAT(int nItems, int itemIDs[], int rwo[]){
	int i, j, k;
	int flag = 1;
	double theta;
	char lvl;
	int condition_code;

	for (i = 0; i < nItems; i++) {
		for (j = 0; j < _n_items; j++) {
			if (itemIDs[i] == _items[j].item_id){
				_ik = j;
				_isFTitem = 0;
				if (i == (nItems -1)) {
					if (_items[j].psg_id)
						_is_psg = 1;
					else
                        _is_psg = 0;
				}
				/* call set_pItems() after item 10. */
				if (_iadm == (_1st_nItems + _2nd_nItems ) ) { 
					theta = score();
			        lvl = get_testLevel(theta, _subTest);
                    condition_code = getNadmObj(_NoItemsEachObjByLvl_filename, _subTest, lvl, _n_adm_obj);
                    if (condition_code != GETN_ADMOBJ_OK) {
	                      if (LOG_FILE_FLAG) fprintf(log_file, "Error: getNadmObj failed for subTest %s and Level %c in resumeCAT() \n", _subTest, lvl);
	                      exit(EXIT_FAILURE); 
			        }
			        /* sort by obj_id and a parameter?? No! It already was sorted this way. */
			        set_pItems();   
			       /* set_testLength(); */
	            }
				update_iBank();
				set_rwo(rwo[i]);

				if (i != (nItems -1)) 
				    theta = score();
/*				printf("%d,   %d,   %d \n", _items[j].item_id, _items[j].new_obj_id, rwo[i]); */
				flag = 0;
				break;
			}
		}

		if (flag) {
		    for (k = 0; k < _n_FTitems; k++) {
			    if (itemIDs[i] == _FTitems[k].item_id){
				    _ik_FT = k;
					_isFTitem = 1;
					if (i == (nItems -1)) {
					    if (_FTitems[k].psg_id) 
						    _is_FTpsg = 1;
					    else
                            _is_FTpsg = 0;
				    }
				    update_FTiBank();
				    break;
			    }
		    }
		}
		flag = 1;
	}
}

/* simulation */
void set_simuRWO(int rwo, double theta0) {
// simulated studies
   double p0;
  // double theta0 = 400.0;
  if (!_isFTitem) {
      p0 = 1.0- respProb3PL(1, theta0, _item_adm[_iadm-1].parameters);
      if (p0 >= ran0(1)) rwo = 0;
      else rwo = 1;
       // end of simulation

      _item_adm[_iadm -1].rwo = rwo;
      _items[_ik].rwo = rwo;   // simulation
	  if (!_report_ss) 
		 _report_ss = 1;   // always report ss
  }
}

JNIEXPORT void JNICALL Java_com_ctb_tdc_web_utils_CATEngineProxy_set_1rwo (JNIEnv * env, jclass theclass, jint raw) {
	set_rwo(raw);
}

void set_rwo(int rwo) {
	if (rwo == END_TEST) {
		if (!_isFTitem)
		    _i_stop = _iadm -1;
		else 
            _i_stop = _iadm;
		
		rwo = 0;
	}
	if (!_isFTitem)
       _item_adm[_iadm -1].rwo = rwo;

	if (!_report_ss) {
		if ((rwo == 1) || (_iadm >= 6 )) _report_ss = 1;      /* report scale score, if answered any one item right or answered >= 5 items */
	}

}

JNIEXPORT jdouble JNICALL Java_com_ctb_tdc_web_utils_CATEngineProxy_score (JNIEnv * env, jclass theclass) {
	return score();
}

double score(){
    double theta;
	double fpq, p, pq;
	double sfpq, tfpq;
	double max_pq = -1.0;
	int max_i, loss_i, hoss_i;
    int i, j;
	double final_theta, old_max_pq;
	
    if (_i_stop >= 0) { // test termination in the middle of test 	
		if (!_report_ss) return UnReportableScore;   // 0 score means NOT reporting scale score 

		if (!_isFTitem)
		    _iadm--; // reset item parameters and response for the current item that tester runs out of time. 

		_isFTitem = 0;
		for (j = _i_stop; j < _testLength; j++) {
			update_iBank_stop();
			set_rwo(0);
		} 
	}
    
	if (_isFTitem) return _theta[_iadm];

	sfpq = 0.0;
	tfpq = 0.0;

    for ( i = 0; i < Qs; i++) {
		pq = 1.0;
        for ( j = 0; j < _iadm; j++) {
            p = respProb3PL(1, _tq[i], _item_adm[j].parameters);
			if (_item_adm[j].rwo == 1)
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
    
	if (_iadm == _testLength) { /* final theta */
		
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
	
	//	loss_i = (int)_loss;
	//	hoss_i = (int)_hoss;
		
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
		*/
		/*
		for ( j = 0; j < _iadm; j++) {
		   fprintf(log_file, "%d", _item_adm[j].rwo);
           if ((_item_adm[j].rwo != 0 ) && (_item_adm[j].rwo != 1) )
		       ;
		}
		fprintf(log_file, "\n");
	    */
		theta = final_theta;  /* final theta from ES. */
        _theta[_iadm] = theta; 
	}

	return theta;
}
JNIEXPORT jdouble JNICALL Java_com_ctb_tdc_web_utils_CATEngineProxy_getSEM (JNIEnv * env, jclass theclass, jdouble theta) {
	return getSEM(theta);
}

double getSEM(double theta) {
	int j;
	double info = 0.0;
	double sem = 0.0;
    for ( j = 0; j < _iadm; j++) {  /* to do need to be changed to final_theta */
         info = info + fisherInfo(theta, _item_adm[j].parameters);		
	} 

	if (info > 0.000001)
        sem = (double)1.0/ sqrt(info) ;
	else 
		sem = 999.0;
    
	return sem;
}

JNIEXPORT jdouble JNICALL Java_com_ctb_tdc_web_utils_CATEngineProxy_get_1objScore (JNIEnv * env, jclass theclass, jdouble theta, jint obj_id) {
	return get_objScore(theta, obj_id);
}

double get_objScore(double theta, int obj_id) {
	int j;
	double p = 0.0;
	int num_obj = 0;
    for ( j = 0; j < _iadm; j++) {  /* to do need to be changed to final_theta */
		if (_item_adm[j].new_obj_id == obj_id ) {
           p = p + respProb3PL(1, theta, _item_adm[j].parameters);	
		   num_obj++;
		}
	} 
    
	if (num_obj >= _min_nItems4Obj_score)
	    return p/(double)num_obj;
	else
		return -1.0;
}

JNIEXPORT jdouble JNICALL Java_com_ctb_tdc_web_utils_CATEngineProxy_get_1objScaleScore (JNIEnv * env, jclass theclass, jint obj_id) {
	return get_objScaleScore(obj_id);
}

double get_objScaleScore(int obj_id) {
	int j, k;
	double TCC0 = 0.0;
    double TCC = 0.0;
	int num_obj = 0;
	double theta = -1.0;
	int rs = 0;
	int loss = (int)_loss;
	int hoss = (int)_hoss;

	double sumCs = 0.0;


	rs = get_rs(obj_id);
	num_obj = get_totObjRS();  /* max_rs */
	sumCs = get_sumCs();

	/* set level loss and hoss */
	/*
	k = get_i_obj(obj_lvl);
	if (k < 0) {
		printf ("Error: invalid objective level in get_objScaleScore calls get_i_obj() \n"); 
		return -1.0;
	}

	loss = _lvl_loss[k]; 
	hoss = _lvl_hoss[k];
	*/
	if (num_obj >= _min_nItems4Obj_score) {
        /* set loss hoss based on level @todo? */
		loss = get_loss(obj_id);
		hoss = get_hoss(obj_id);
		if (rs <= sumCs) theta = (double)loss;
		else if (rs == num_obj) theta = (double)hoss;
		else theta = TCCinverse((double)rs, obj_id, loss, hoss);  
		return theta;
	}
	else
		return UnReportableScore;	
}

double TCCinverse(double rs, int obj_id, int loss, int hoss) {
	double theta = -1.0;
	double TCC0 = 0.0;
	double TCC = 0.0;
	int k, j;

	    for (k = loss; k <= hoss; k++) {
			 TCC0 = TCC; // for TCC at k-1
		     TCC = 0.0;
             for ( j = 0; j < _iadm; j++) {  
		         if (_item_adm[j].new_obj_id == obj_id ) {
                     TCC = TCC + respProb3PL(1, (double)k, _item_adm[j].parameters);	
			     }
		     }
			 if (TCC >= rs ) {
				 if (k == loss) 
				     theta = (double)k;
				 else {
					 if ((TCC + TCC0) <= 2.0*rs) 
					      theta = (double)k;
					 else
						  theta = (double)(k-1);
				 }
				 break;
			 } 
			 else {
				 if (k == hoss) {
					 theta = (double)k;
					 break;
				 }
			 }
	    } 	
		
		if (theta < 0) {
			if (LOG_FILE_FLAG) fprintf(log_file, "Warning: TCC inverse failed for objective ID %d \n", obj_id);
		}
	    return theta;
}

int get_loss(int obj_id){
	int loss = (int)_loss;
	double rs1 = _sumCs + 1.0;
	double rs2 = _sumCs + 2.0;
	double ss1 = TCCinverse(rs1, obj_id, loss, (int)_hoss);
	double ss2 = TCCinverse(rs2, obj_id, loss, (int)_hoss);
//	printf(" %6.2lf ", (2.0*ss1 - ss2));
	loss = (int)(2.0*ss1 - ss2);
//	printf(" %d  %d \n", loss, (int)_loss);
	if (loss < (int)_loss) loss = (int)_loss;
	return loss;
}

int get_hoss(int obj_id){
	int hoss = (int)_hoss;
	double rs1 = _tot_obj_rs - 1.0;
	double rs2 = _tot_obj_rs - 2.0;	
	double ss2 = TCCinverse(rs2, obj_id, (int)_loss, hoss);

	_ss1 = TCCinverse(rs1, obj_id, (int)_loss, hoss);
	hoss = (int)(2.0*_ss1 - ss2);
//	printf(" %d  %d \n", hoss, (int)_hoss);
	if (hoss > (int)_hoss) hoss = (int)_hoss;
	return hoss;
}

int get_i_obj(char obj_lvl){
	int i = -1;
	switch(obj_lvl) {
	    case 'E': i = 0; break;  /* current set up for level, E, M, D, A only. */
        case 'M': i = 1; break;                         
		case 'D': i = 2; break;
		case 'A': i = 3; break;
	    default : printf ("Error: invalid objective level in get_i_obj() call \n"); break;    
    }
	return i;
}

JNIEXPORT jdouble JNICALL Java_com_ctb_tdc_web_utils_CATEngineProxy_get_1objSSsem (JNIEnv * env, jclass theclass, jdouble theta, jint obj_id) {
	return get_objSSsem(theta, obj_id);
}

double get_objSSsem(double theta, int obj_id) {  /* NC SEM */
    double info, sem; 
    double sumOFdEdtheta = 0.0;
    double sumOFvar = 0.0;
    int j;

    for ( j = 0; j < _testLength; j++) {  
	    if (_item_adm[j].new_obj_id == obj_id ) {
		    sumOFdEdtheta += respProb3PL_derivs(theta, _item_adm[j].parameters);
		    sumOFvar += respProb3PL_variance(theta, _item_adm[j].parameters);	     
	    }
    }
    info = sumOFdEdtheta * sumOFdEdtheta / sumOFvar ;
	if (info > 0.000001)
        sem = (double)1.0/ sqrt(info) ;
	else 
		sem = 999.0;
    return sem;			
}

int get_rs(int obj_id) {
	int j;
	int rs = 0;
	int num_obj = 0;
	double sumCs = 0.0;
	for ( j = 0; j < _testLength; j++) {  
		if (_item_adm[j].new_obj_id == obj_id ) {
           rs = rs + _item_adm[j].rwo;	
		   num_obj++;
		   sumCs = sumCs + _item_adm[j].parameters[2];
/*		   printf("%d,   %d,   %d  %lf \n", _item_adm[j].item_id, _item_adm[j].new_obj_id, _item_adm[j].rwo, _item_adm[j].parameters[2]); */
		}
	} 
	_obj_rs = rs;
	_tot_obj_rs = num_obj;
	_sumCs = sumCs;

/*	printf("sumCs =   %lf \n", _sumCs); */
	return rs;
}

JNIEXPORT jint JNICALL Java_com_ctb_tdc_web_utils_CATEngineProxy_get_1totObjRS (JNIEnv * env, jclass theclass) {
	return get_totObjRS();
}

int get_totObjRS() {
	return _tot_obj_rs;  /* must be called after get_rs() or get_objScaleScore() */
}

int get_sumCs() {
	return _sumCs;  /* must be called after get_rs() or get_objScaleScore() */
}

JNIEXPORT jint JNICALL Java_com_ctb_tdc_web_utils_CATEngineProxy_get_1objRS (JNIEnv * env, jclass theclass) {
	return get_objRS();
}

int get_objRS() {
	return _obj_rs;  /* must be called after get_rs() or get_objScaleScore() */
}

JNIEXPORT jint JNICALL Java_com_ctb_tdc_web_utils_CATEngineProxy_get_1objMasteryLvl (JNIEnv * env, jclass theclass, jdouble obj_score, jint obj_id) {
	return get_objMasteryLvl(obj_score, obj_id);
}

// int get_objMasteryLvl(double obj_score, int obj_id, char obj_lvl){ /* todo */
int get_objMasteryLvl(double obj_score, int obj_id){
	int MasteryLvl = -1;
	int k;
	int j;
	int masteryLvl1, masteryLvl2;

	/*
	k = get_i_obj(obj_lvl);
	if (k < 0) {
		printf ("Error: invalid objective level in get_objMasteryLvl calls get_i_obj() \n"); 
		return -1.0;
	}
	*/

	if (_obj_rs <= _sumCs) return 0;
	if (_obj_rs == _tot_obj_rs) {
		// use _ss1 & obj_score to determine level
		masteryLvl1 = getMasterLvlFromSS(_ss1, obj_id) + 1;
		masteryLvl2 = getMasterLvlFromSS(obj_score, obj_id);
		if (masteryLvl2 > masteryLvl1) return masteryLvl1;
		else return masteryLvl2;
	}
	return getMasterLvlFromSS(obj_score, obj_id);
}

int getMasterLvlFromSS(double ss, int obj_id) {
	int j;
	int MasteryLvl = -1;
	for (j = 0; j < _n_new_obj; j++) {
		if (obj_id == _objSS_cut[j].objID) {
			if (ss < _objSS_cut[j].s75[0]) MasteryLvl = 0;   /* non-mastery */
			else if ((ss >= _objSS_cut[j].s75[0]) && (ss < _objSS_cut[j].s75[1])) MasteryLvl = 1;  /* begining mastery */
			else if ((ss >= _objSS_cut[j].s75[1]) && (ss < _objSS_cut[j].s75[2])) MasteryLvl = 2;  /* partical mastery */
			else if ((ss >= _objSS_cut[j].s75[2]) && (ss < _objSS_cut[j].s75[3])) MasteryLvl = 3;  /* mastery */
			else MasteryLvl = 4;  /* advanced mastery */
			break;
		}
	}
    return MasteryLvl;
}

void set_pItems(){
     int i, j, n_a;
     int n_ob = 0;

	 for ( i = 0; i < _n_obj; i++) {
         n_a = _n_obj_items[i]/_aStrata_o;  /* all items in an a strata and one obj  */
		 for ( j = 0; j < _aStrata_o; j++) {
             _pItems[i][j][0] = j*n_a + n_ob;  
			 if (j == (_aStrata_o -1)) { 
		         _pItems[i][j][1] = _n_obj_items[i] -1 + n_ob;
			 }
			 else {
				 _pItems[i][j][1] = (j+1)*n_a -1 + n_ob;
             }
		 }
         n_ob = n_ob + _n_obj_items[i]; 
	 }
}
/*  old code for alpha stra and obj rotation
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
*/
JNIEXPORT void JNICALL Java_com_ctb_tdc_web_utils_CATEngineProxy_setoff_1cat (JNIEnv * env, jclass theclass) {
	setoff_cat();
}

void setoff_cat(){  /* For no obj and aStr case */

   if (LOG_FILE_FLAG) fclose(log_file);
   free(_theta);
   free(_tq);
   free(_ft);
   free(_objSS_cut);
   free(_FTitems); /* note there is no parameters in FT. */
   free_items(_n_items, _items); 

   if (_iadm == _testLength)
      free_items(_iadm, _item_adm);
   else
	  free(_item_adm);
/*   free(_n_a);
//   free(_max_nItem_obj);
*/
   free(_itemInfo);
   free(_indx);
   free(_indx_a); 
   free3DIntArray(_pItems);
/*   free2DIntArray(_nItems_a_c); */
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
/* simulate scale score in normal distribution */
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

void getAdmItemPars(struct item_info *items){
    int i;
    for (i = 0; i < _testLength; i++) {
		 items[i].parameters = (double *) malloc(3*sizeof(double));
        if (items[i].parameters == NULL) {
		    if (LOG_FILE_FLAG) fprintf (log_file, "Error: malloc item[%d].parameters failed! \n", i);
            exit;
        }	  
		items[i].rwo = _item_adm[i].rwo;
		items[i].parameters[0] = _item_adm[i].parameters[0];
		items[i].parameters[1] = _item_adm[i].parameters[1];
		items[i].parameters[2] = _item_adm[i].parameters[2];
	}
}

void getAdmItems(struct item_info *items, int *n_obj, int objID[]){
    int i, j;
    for (i = 0; i < _n_items; i++) {
	   items[i].item_no = _items[i].item_no;
       items[i].adm_flag = _items[i].adm_flag; 
       items[i].obj_id = _items[i].obj_id; 
   /*    items[i].obj_n = _items[i].obj_n;  */
	   items[i].rwo = _items[i].rwo; 
       items[i].org_psg_id = _items[i].org_psg_id;
	   items[i].item_id = _items[i].item_id;
	   items[i].enemy = _items[i].enemy;

	}
    for (j = 0; j < _n_obj; j++) {
       objID[j] = _objID[j];    
	}
    *n_obj = _n_obj;
}

int checkPsgID() {
	int i,j, k;
	int itemOrder[6];
	int indx[6];
	int kk;
	double min_a = 99.0; /* 3PL metric */
	double mean_b = 0.0;
	double mean_c = 0.0;
	double geo_mean_a = 0.0;

	for (i = 0; i < _n_items; i++) {

		/* find lowest a and compute average b */
        mean_b = mean_b + _items[i].parameters[1]/_items[i].parameters[0];  /* b in 3pl metric */
		if (min_a > _items[i].parameters[0])
			min_a = _items[i].parameters[0];

       geo_mean_a = geo_mean_a + log(_items[i].parameters[0]);
	   mean_c = mean_c + _items[i].parameters[2];
		/* check duplicated item IDs */
		for (j = 0; j < _n_items; j++) {           
				if  ((_items[i].item_id == _items[j].item_id) && (i != j)){
					    printf("Error: duplicated Item ID %d at i = %d, j = % d, \n", _items[i].item_id, i, j);
					    return PSGID_DUP;
			    }
		}

		/* add check original passage id and enemy item */
		if (_items[i].org_psg_id != 0) {
		    for (j = 0; j < _n_items; j++) {           
				if  ((_items[i].org_psg_id == _items[j].org_psg_id) && (i != j)){
				    if (_items[i].enemy != _items[j].enemy) {
					    printf("Error: same org_psg_id has different enemy ID %d at i = %d, j = % d, \n", _items[i].org_psg_id, i, j);
					    return PSGID_DUP;
				    } 
			    }
			}
		}

		/* check new passage id and item order */
		itemOrder[0] = _items[i].item_order;
		k = 1;
		if (_items[i].psg_id != 0) {
		    for (j = 0; j < _n_items; j++) {
	            if  ((_items[i].psg_id == _items[j].psg_id) && (i != j)){
				    if (_items[i].item_order == _items[j].item_order) {
					    printf("Error: duplicated psg_id and item_order %d at i = %d, j = % d, \n", _items[i].psg_id, i, j);
					    return PSGID_DUP;
				    }
				    itemOrder[k] = _items[j].item_order;
				    k++;

					/* check new psg_id is a part of org_psg_id */
					 if (_items[i].org_psg_id != _items[j].org_psg_id) {
					    printf("Error: org_psg_id = %d at i = %d, NOT= it at j = % d, \n", _items[i].org_psg_id, i, j);
					    return PSGID_DUP;
				    }
			    }
				
		    }
		    /* check max item size within the passage */
			/* old max size = 3; new max size = 5? */
		    if (k > 5) {
			    printf("Error: psg size is over 3 for psg_id = %d \n", _items[i].psg_id );
			    return PSGID_DUP;
		    }
            
			if (k > 1) {
			    indInt(k, itemOrder, indx);
		        if (itemOrder[indx[0]] != 1) {
                    printf("Error: the starting item_order is not 1 for psg_id = %d \n", _items[i].psg_id );
			        return PSGID_DUP;
		        }

			     for (kk = 0; kk < (k -1); kk++) {
		             if ( itemOrder[indx[kk+1]] != (itemOrder[indx[kk]] + 1)) {
					      printf("Error: item_order is not a seq. number for psg_id = %d \n", _items[i].psg_id );
			              return PSGID_DUP;
				     }
			     }
			}
	   }
	}
	min_a = min_a/1.7;  /* from 2PPC to 3PL */
	
	mean_b = mean_b/(double)_n_items;
	mean_c = mean_c/(double)_n_items;
    geo_mean_a = exp(geo_mean_a/(double)_n_items);
	geo_mean_a = geo_mean_a/1.7;

	printf("Content = %s, min_a =  %8.5lf, geo_mean_a = %8.5lf,  mean_b = %8.4lf, mean_c = %8.5lf in 3PL metric \n", _subTest,min_a, geo_mean_a, mean_b, mean_c );
    printf("Content = %s, min_a =  %8.5lf, geo_mean_a = %8.5lf,  mean_b = %8.4lf, mean_c = %8.5lf in 2PPC metric \n", _subTest, min_a*1.7, geo_mean_a*1.7, mean_b*geo_mean_a*1.7, mean_c);

	return PSGID_OK ;
}

/***********************
 * LIBRARY ENTRY POINT.*
 ***********************/
/*Removed  WINAPI from the signature while removing windows related dependency*/
/* BOOL WINAPI DllMain(HINSTANCE hInstDll, DWORD fdwReason, LPVOID lpReserved) */
/*BOOL DllMain(HINSTANCE hInstDll, DWORD fdwReason, LPVOID lpReserved)
{
    hInst = hInstDll;

	return TRUE;
}*/