/*
 * Created: Q3 2010
 * Author: Lianghua Shu
 */

#include "item.h"

#ifndef INCLUDE_CAT_H
#define INCLUDE_CAT_H

#define DLL_EXP_IMP __declspec(dllexport)

DLL_EXP_IMP int WINAPI setup_cat(char subTest[]);
// long n_items, double loss, double hoss, long testLength);
int getTestLength();
int getNumItems();
int getNumObj();
double getLoss();
double getHoss();
char get_testLevel(double theta, char subTest[]);
// void set_pItems();
void print_pItems();
// int adapt_aItem(int obj, int aSt, double theta);
// int adapt_aItemFromAll(double theta);  // no obj and alpha stratificatoin constraint.
// int adapt_aItemFromIdx(double theta, int idx1, int idx2);
DLL_EXP_IMP int WINAPI next_item();
// int get_o_a(int *obj, int *aSt, int n);
DLL_EXP_IMP double WINAPI score();
DLL_EXP_IMP double WINAPI getSEM(double theta);
void set_simuRWO(int rwo, double theta0); //simu
DLL_EXP_IMP void WINAPI set_rwo(int rwo);
DLL_EXP_IMP void WINAPI setoff_cat();
int simSS(int nStudents, double ss[], double mean, double std, double loss, double hoss);
void getAdmItems(struct item_info *items, int *n_obj, int objID[]);
/* Normal return, by convention defined to be zero */
#define SETUP_CAT_OK                 0
#define GET_O_A_OK                 0
#define GETN_ADMOBJ_OK						0

/* Fatal errors: 1-1000 */
#define SETUP_CAT_FAILURE           1
#define GET_O_A_FAILURE           1
#define GET_NITEM_FAILURE         1

#endif