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
struct item_info *copy_items(int n_items, struct item_info *items) {
    int i;
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
void free_items(int n_items, struct item_info *items) {
    int i;
    
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
void print_items(int n_items, struct item_info items[]) {
    
    int j;
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


