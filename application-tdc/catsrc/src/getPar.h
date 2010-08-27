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

#define GET_NITEM_LINELEN_OK 0         /* Normal return */
#define GET_NITEM_LINELEN_FAILURE 1    /* Fatal errors  */

int getline (char *line, int max, FILE *fp);
int getItems (char inPar[], struct item_info items[], int n_items, int *n_obj, 
			  int objID[], int n_obj_items[]);

#endif

