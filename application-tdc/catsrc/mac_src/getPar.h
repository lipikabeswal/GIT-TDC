/* Author: Lianghua Shu
 * read par file.
 *  
 * Exhaustive Search for QA4irt version 1.0. 
 * created: 2-19-03
 *  
 * Modified: 2-21-03 to test Grid Search
 * Updated : April-May, 2003 for ESF 1.0
 *  
 */
#include "item.h"
#include "utilityFun.h"

#ifndef GETPAR_H
#define GETPAR_H

#define GETPAR_OK 0                    /* Normal return */
#define GETPAR_FAILURE 1               /* Fatal errors  */

#define GETSS_STATS_OK 0                    /* Normal return */
#define GETSS_STATS_FAILURE 1               /* Fatal errors  */

#define GETN_ADMOBJ_OK 0                    /* Normal return */
#define GETN_ADMOBJ_FAILURE 1               /* Fatal errors  */

#define GET_NITEM_OK 0         /* Normal return */
#define GET_NITEM_FAILURE 1    /* Fatal errors  */

#define lineLen_max 300
#define LOG_FILE_FLAG 0   /* 0/1 turned off/on log_file output */

struct objSScut {
//	double s50[4];
	double s75[4];
	int objID;
};

int getCurrentline (char *line, int max, FILE *fp);
int getItems (char inPar[], struct item_info items[], int n_items, int *n_obj, 
			  int objID[], int n_obj_items[], int *n_new_obj, int new_objID[]);
int getSSstats (char inFileByLvl[], char inFileByCnt[], char subTest[], char testLevel, double ssSts[]);
int getNadmObj(char inFileNoItemObj[], char subTest[], char testLevel, int n_adm_obj[]);
int get_n_items(char inPar[]);

int getFT_Items(char inPar[], struct item_info items[], int n_items, int *n_FTitemsLvl);

int get_LH4lvl(char inFile[], char subTest[], double loss[], double hoss[]);
int get_objLvlCut(char inFile[], int objID[], struct objSScut objSS_cut[]);
 
#endif

