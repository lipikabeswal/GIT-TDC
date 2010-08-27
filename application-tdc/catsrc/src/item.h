/*
 * Header file defining data structures for IRT items.
 * Created: Q3 2010
 * Author: Lianghua Shu
 */

#ifndef INCLUDE_ITEM_H
#define INCLUDE_ITEM_H

/*
 * Item types
 */
#define	ITEM_TYPE_3PL   1       /* Dichotomous item, multiple choice */
#define ITEM_TYPE_2PPC  2       /* Constructed response item, no guessing */

/*
 * Omit flag values
 */
#define ITEM_OMIT_FALSE 0       /* Include item in likelihood calculations */
#define ITEM_OMIT_TRUE  1       /* Do not include item in calculations */

/*
 * Item information structure
 *
 * item_no           Identification number for item
 *
 * omit_flag         Flag indicating whether item is to be included
 *                   in any computations such as calculating the
 *                   value of the likelihood.
 *                   *** Note that if this is
 *                   set to ITEM_OMIT_TRUE, none of the other components
 *                   of this structure are assumed to have valid values.
 *
 * item_type         One of the item types defined above
 * item_levels       Number level/categories for 2PPC items
 *
 * parameters        The IRT parameters for this item.  Encoding and length
 *                   depends on item_type and item_levels.  Currently,
 *                   both the lklihood.c and irt.c modules assume the
 *                   the following:
 *
 *                   For 3PL items, the array should hold 3 parameters
 *                   using the triangular metric:
 *                             parameters[0] -> a           discrimination
 *                             parameters[1] -> b           threshold
 *                             parameters[2] -> c           psuedo-guessing
 *
 *                   For 2PPC items, if items_levels == m, then the
 *                   array should hold m parameters:
 *                             parameters[0] -> alpha    discrimination
 *                             parameters[1] -> gamma_1  threshold between
 *                                                       categories 1 and 2
 *                             parameters[2] -> gamma_2  threshold between
 *                                                       categories 2 and 3
 *                             ...
 *                             parameters[m-1] -> gamma_(m-1)  threshold between
 *                             categories m-2 and m-1
 */
struct item_info {
	int item_no;
	int omit_flag;
	int item_type;
	int	item_levels;
	double *parameters;
	int item_id;
	int answer_key;
	int obj_id;
	char obj_title[50];
//	int obj_n;  /* total number of objectives */
	char level_id;
//	int a_level;  /* a parameter stratified levels */
	int adm_flag; /* initialized as 1, set to 0 after item is administrated */
	int rwo;
};

#define VALID_ITEMS   0
#define INVALID_ITEMS 1
int validate_items(long n, struct item_info *items);

#define VALID_ITEM_RESPONSES   0
#define INVALID_ITEM_RESPONSES 1
int validate_item_responses(long n, struct item_info *items, int *responses);

/*
 * Integer value for a response when a response should be
 * ignored. All likelihood calculations will simply ignore the response
 * (and corresponding item) when encountering this response value.
 * Note that this is not the same as coding a response as "wrong."
 *
 * Also note that this should be coded to be a negative value to
 * be safe since zero and all positive integers are potentially 
 * legitimate response values.
 */
#define LIKELIHOOD_IGNORE_RESPONSE -1

/*
 * Utility functions for operating on arrays of items
 */
struct item_info *copy_items(long n_items, struct item_info *items);
void free_items(long n_items, struct item_info *items);
void print_items(long n_items, struct item_info items[]);

/*
 * Other utility functions
 */
long check_parameter_range(long n_items, struct item_info items[],
                           double loss, double hoss);


#endif
