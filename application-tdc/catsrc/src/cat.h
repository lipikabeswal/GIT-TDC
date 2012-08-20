/*
 * Created: Q3 2010
 * Author: Lianghua Shu
 */

#include "item.h"

#ifndef INCLUDE_CAT_H
#define INCLUDE_CAT_H

#define DLL_EXP_IMP __declspec(dllexport)

/* DLL_EXP_IMP int WINAPI setup_cat(char subTest[]); */
int setup_cat(char subTest[]);
/* long n_items, double loss, double hoss, long testLength); */
int getTestLength();
int getNumItems();
int getNumObj();  /* original number of objectives */
/* DLL_EXP_IMP int WINAPI  get_nObj();   // number of collapsed objectives
//DLL_EXP_IMP int WINAPI  get_objID(int k);  // return collapsed objective ID, given objective number
*/
int get_nObj();   /* number of collapsed objectives */
int get_objID(int k);
double getLoss();
double getHoss();
char get_testLevel(double theta, char subTest[]);
// char get_objLvl(double theta);
/* void set_pItems();
// void print_pItems();
// int adapt_aItem(int obj, int aSt, double theta);
// int adapt_aItemFromAll(double theta);  // no obj and alpha stratificatoin constraint.
// int adapt_aItemFromIdx(double theta, int idx1, int idx2);
*/
int next_item();
/* int get_o_a(int *obj, int *aSt, int n); */
double score();
double getSEM(double theta);
double get_objScore(double theta, int obj_id);
double get_objScaleScore(int obj_id);
double get_objSSsem(double theta, int obj_id);

int get_rs(int obj_id);
int get_totObjRS();
int get_objRS();
int get_sumCs();
int get_psgID();
int get_enemyID();

// int get_objMasteryLvl(double obj_score, int obj_id, char obj_lvl);
int get_objMasteryLvl(double obj_score, int obj_id);

void set_simuRWO(int rwo, double theta0); /* simu */
void set_rwo(int rwo);
void resumeCAT(int nItems, int itemIDs[], int rwo[]);
void setoff_cat();
int simSS(int nStudents, double ss[], double mean, double std, double loss, double hoss);
void getAdmItems(struct item_info *items, int *n_obj, int objID[]);
void getAdmItemPars(struct item_info *items);
int checkPsgID();
void set_initial_theta (double theta0);
double get_theta0(double theta, char subTest[], int level);
/* Normal return, by convention defined to be zero */
#define SETUP_CAT_OK                 0
#define GET_O_A_OK                 0
#define PSGID_OK     0

/* Fatal errors: 1-1000 */
#define SETUP_CAT_FAILURE           1
#define GET_O_A_FAILURE           1
#define PSGID_DUP     1

#endif