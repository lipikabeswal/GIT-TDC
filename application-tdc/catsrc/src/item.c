/*
 * Module of utility functions for IRT items.
 * Created: Q3 2010
 * Author: Lianghua Shu
 */
#include <stddef.h>
#include <stdio.h>
#include <stdlib.h>

#include "item.h"
#include "irt.h"

/*
 * Given n items in an array, check that each
 * item contains valid data.
 *
 * Arguments:
 *     n            The number of items in array the_items
 *     items        An array of struct item_info of length n
 *
 * Returns VALID_ITEMS if the items are valid,
 * INVALID_ITEMS otherwise.
 */
int validate_items(long n, struct item_info *items) {
    long i;
    
    if (items == NULL || n <= 0) {
        return INVALID_ITEMS;
    }
    
    for (i = 0; i < n; i++) {
        /* If the omit flag is true, this item does not have valid
         * item information, so skip. */
        if (items[i].omit_flag == ITEM_OMIT_TRUE) {
            continue;
        }
        
        /* Since the omit flag is not true, it better be false.
         * Check to make sure it has the correct value for false. */
        if (items[i].omit_flag != ITEM_OMIT_FALSE) {
            return INVALID_ITEMS;
        }
        
        
        /*
         * Check to make sure item_type is one of the appropriate values
         * and for each type, check parameter ranges.
         */
        
        if (items[i].item_type == ITEM_TYPE_3PL) {
			int return_code = validate3PL(items[i].parameters);
			if (return_code != IRT_VALID_ITEM) {
				return INVALID_ITEMS;
			}
        }
        
        else if (items[i].item_type == ITEM_TYPE_2PPC) {
			int return_code = validate2PPC(items[i].parameters, items[i].item_levels);
			if (return_code != IRT_VALID_ITEM) {
				return INVALID_ITEMS;
			}
        }
        
        /* otherwise item_type not one of the valid types */
        else {
            return INVALID_ITEMS;
        }
        
    } /* end for loop */
    
    /* Otherwise, we made it through all items without finding invalid data */
    return VALID_ITEMS;
}

/*
 * Given n items and n responses in parallel arrays, validate that each
 * response is appropriate for the corresponding item.
 *
 * Arguments:
 *     n            The number of items in array the_items
 *     items        An array of struct item_info of length n
 *     responses    An array of integers representing one examinee's
 *                  responses to the items.
 *                  Assumed to be of length n and to be parallel
 *                  to the items array - i.e., the_responses[i] contains
 *                  an examinees responses to items[i].
 *
 * Returns VALID_ITEM_RESPONSES if the repsonses are valid,
 * INVALID_ITEM_RESPONSES otherwise.
 */
int validate_item_responses(long n, struct item_info *items, int *responses) {

    long i;
    
    for (i = 0; i < n; i++) {
        /* if the omit flag is true, we assume we don't have valid
         * item information, so skip. */
        if (items[i].omit_flag == ITEM_OMIT_TRUE) {
            continue;
        }
        
        if (items[i].item_type == ITEM_TYPE_3PL) {
            /* response must be 0 or 1 or omit */
            if ( !(responses[i] == 0 || responses[i] == 1 ||
                   responses[i] == LIKELIHOOD_IGNORE_RESPONSE) ) {
                return INVALID_ITEM_RESPONSES;
            }
        }
        else if (items[i].item_type == ITEM_TYPE_2PPC) {
            /* response must be in range 0..item_levels-1 or omit */
            if (!((responses[i] >= 0 && responses[i] < items[i].item_levels) ||
                    responses[i] == LIKELIHOOD_IGNORE_RESPONSE)) {
                return INVALID_ITEM_RESPONSES;
            }
        }
        else {
            /* Invalid item type so invalidate also */
            return INVALID_ITEM_RESPONSES;
        }
    }
    
    /* Otherwise, all responses and items checked out okay. */
    return VALID_ITEM_RESPONSES;
}

/*
 * Utility function to create a copy of an item array. 
 * 
 * Arguments:
 *   n_items      The number of items in array items
 *   items        An array of struct item_info of length n_items.
 *                It is assumed that all items contain valid values.
 *                That is, validate_items(n_items, items) should return 
 *                VALID_ITEMS.
 *
 * Returns:
 *   A newly allocated copy of the array items.
 *   If any memory allocations fail, returns NULL.
 */
struct item_info *copy_items(long n_items, struct item_info *items) {
    long i;
    struct item_info *tmp_item_array;
    

    tmp_item_array =
        (struct item_info *) malloc(n_items * sizeof(struct item_info));
    if (tmp_item_array == NULL) {
        return NULL;
    }

    /*
     * Loop over items and copy each item into the newly allocated array
     *
     * Note we still need to allocate memory for item parameters arrays
     * for each item as appropriate.  If any malloc fails when attempting
     * to allocate for the item parameters, we free the entire item array
     * (including any item parameter arrays already allocated)
     * and return NULL.
     */
    for (i = 0; i < n_items; i++) {
        /* Copy item number */
        tmp_item_array[i].item_no = items[i].item_no;
        
        /* Copy omit flag value */
        tmp_item_array[i].omit_flag = items[i].omit_flag;
        /* If flag is true, then no valid item information is assumed.
         * Ensure parameters is set to NULL (to be safe) and skip rest
         * of copy operation. */
        if (items[i].omit_flag == ITEM_OMIT_TRUE) {
            tmp_item_array[i].parameters = NULL;
            continue;
        }

        /* Copy item type */
        tmp_item_array[i].item_type = items[i].item_type;
        
        /* Copy item_levels */
        tmp_item_array[i].item_levels = items[i].item_levels;
        
        /*
         * Copy item parameters
         * Allocate space for parameters as appropriate and copy values
         * Note that we assume source array parameters is not NULL
         * since validate_items() is true.
         */
        if (items[i].item_type == ITEM_TYPE_3PL) {
            
            tmp_item_array[i].parameters =
                (double *) malloc( 3*sizeof(double) );
            if (tmp_item_array[i].parameters == NULL) {
                free_items(n_items, tmp_item_array);
                return NULL;
            }
            
            tmp_item_array[i].parameters[0] = items[i].parameters[0];
            tmp_item_array[i].parameters[1] = items[i].parameters[1];
            tmp_item_array[i].parameters[2] = items[i].parameters[2];
        }
        
        else if (items[i].item_type == ITEM_TYPE_2PPC) {
            int j;
            
            tmp_item_array[i].parameters =
                (double *) malloc( items[i].item_levels*sizeof(double) );
            if (tmp_item_array[i].parameters == NULL) {
                free_items(n_items, tmp_item_array);
                return NULL;
            }
            
            for (j = 0; j < items[i].item_levels; j++) {
                tmp_item_array[i].parameters[j] = items[i].parameters[j];
            }
        
        } else {
            /* Should never get here, but just in case... */
            tmp_item_array[i].parameters = NULL;
        }
    
    }

    return tmp_item_array;
}

/*
 * Free all allocated memory for array items.
 * This includes checking each item to see if its parameters
 * array is not NULL and freeing it.
 */
void free_items(long n_items, struct item_info *items) {
    long i;
    
    if (items == NULL) {
        return;
    }

    /* First, loop through and free parameter arrays. */
    for (i = 0; i < n_items; i++) {
        if (items[i].parameters != NULL) {
            free(items[i].parameters);
        }
    }

    /* Now, free the actual array of item_info structs. */
    free(items);
}


/*
 * Utility function to print an item array to stdout.
 * 
 * Arguments:
 *   n_items      The number of items in array items
 *   items        An array of struct item_info of length n_items.
 */
void print_items(long n_items, struct item_info items[]) {
    
    long j;
    int num_parameters, k;
    
    for (j=0; j<n_items; j++) {
        
        printf("item_no = %d\n", items[j].item_no);
        printf("  omit_flag = %d\n", items[j].omit_flag);
        printf("  item_type = %d\n", items[j].item_type);
        printf("  item_levels = %d\n", items[j].item_levels);
        
        /* Print item parameters appropriately for each item type. */
        printf("  parameters = ");
        if (items[j].item_type == ITEM_TYPE_3PL) {
            num_parameters = 3;        
        } else if (items[j].item_type == ITEM_TYPE_2PPC) {
            num_parameters = items[j].item_levels;
        }
        for (k=0; k<num_parameters; k++) {
            printf("%lf ", items[j].parameters[k]);
        }
        printf("\n");
    }
}


/*
 * This routine can be used as a heuristic check to catch
 * instances where a set of item parameters may be
 * on a different scale from the loss and hoss values given.
 *
 * For the given set of items, we check the location of each item to
 * see if it is within the range defined by loss and hoss.
 *
 * Arguments:
 *     n_items          The number of items.
 *     items            An array of items of length n_items.
 *                      Note that item parameter values must be given
 *                      in the "triangular metric".
 *
 *     loss             Lowest scale score to consider
 *     hoss             Highest scale score to consider
 *
 * Returns:
 *   The number of items whose locations are out of range.
 */
long check_parameter_range(long n_items, struct item_info items[],
                           double loss, double hoss) {
    int i;
    long n_out_of_range = 0;
    
    for (i = 0; i < n_items; i++) {
        double location;
        
        /* If the omit flag is true, this item does not have valid
         * item information, so skip. */
        if (items[i].omit_flag == ITEM_OMIT_TRUE) {
            continue;
        }

        if (items[i].item_type == ITEM_TYPE_3PL) {
            location = location3PL(items[i].parameters);
        }
        
        else if (items[i].item_type == ITEM_TYPE_2PPC) {
            location = location2PPC(items[i].parameters, items[i].item_levels);
        }
        
        else {
            /* do nothing, we assume all items have had their type
             * validated already */
            continue;
        }
        
        if (location < loss || location > hoss) {
            n_out_of_range++;
        }
    }
    
    return n_out_of_range;
}



