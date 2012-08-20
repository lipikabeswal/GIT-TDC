/*
 * Utility functions to allocate dynamic Three-dimensional arrays.
 * Created: Q1 2003
 * Author: Lianghua Shu
 */

#include <stdlib.h>
#include <stddef.h> 
#include "array.h"

/*
 * Allocate a dynamic, three-dimensional array.
 *
 */
int new3DIntArray(int nrows, int ncols, int ngrid, int**** array) {

  int*** tmp_array = NULL; 
  int* tmp_array3 = NULL;
  int** tmp_array2 = NULL;

  int ndx;
  
  /* allocate 3D array[nrows][ncols][ngrid] */
  
  tmp_array3 = (int *) malloc(nrows*ncols*ngrid*sizeof(int));
  if (tmp_array3 == NULL) {
    return ARRAY_MALLOC_FAILED;        
  }
  
  tmp_array2 = (int **) malloc(nrows*ncols*sizeof(int *));
  if (tmp_array2 == NULL) {
    free(tmp_array3);
    return ARRAY_MALLOC_FAILED;
  }
  
  tmp_array = (int ***) malloc(nrows*sizeof(int **));
  if (tmp_array == NULL) {
    free(tmp_array3);
    free(tmp_array2);
    return ARRAY_MALLOC_FAILED;        
  }

  for (ndx=0; ndx < nrows*ncols; ndx++) {
    tmp_array2[ndx] = tmp_array3;
    tmp_array3 += ngrid;
  }
  /* if free(tmp_array3) here,
     you will get error: "Segmentation fault (core dumped)" */
  
  for (ndx=0; ndx < nrows; ndx++) {
    tmp_array[ndx] = tmp_array2;
    tmp_array2 += ncols;
  }
  /* can not free(tmp_array2) here */
  
  /* Set output argument to point to the newly created array */
  *array = tmp_array;

  /* can not free(tmp_array), here */
  
  return ARRAY_MALLOC_OK;

}

/*
 * Free the memory for a Three-dimensional array allocated by
 * new3DIntArray().
 *
 */
void free3DIntArray(int*** array) {
  /* free array of  pointers */
  free(array[0][0]);  /* free tmp_array3 */
  free(array[0]);     /* free tem_array2 */
  free(array);        /* free tem_array  */
}

int new3DDoubleArray(int nrows, int ncols, int ngrid, double**** array) {

  double*** tmp_array = NULL; 
  double* tmp_array3 = NULL;
  double** tmp_array2 = NULL;

  int ndx;
  
  /* allocate 3D array[nrows][ncols][ngrid] */
  
  tmp_array3 = (double *) malloc(nrows*ncols*ngrid*sizeof(double));
  if (tmp_array3 == NULL) {
    return ARRAY_MALLOC_FAILED;        
  }
  
  tmp_array2 = (double **) malloc(nrows*ncols*sizeof(double *));
  if (tmp_array2 == NULL) {
    free(tmp_array3);
    return ARRAY_MALLOC_FAILED;
  }
  
  tmp_array = (double ***) malloc(nrows*sizeof(double **));
  if (tmp_array == NULL) {
    free(tmp_array3);
    free(tmp_array2);
    return ARRAY_MALLOC_FAILED;        
  }

  for (ndx=0; ndx < nrows*ncols; ndx++) {
    tmp_array2[ndx] = tmp_array3;
    tmp_array3 += ngrid;
  }
  /* if free(tmp_array3) here,
     you will get error: "Segmentation fault (core dumped)" */
  
  for (ndx=0; ndx < nrows; ndx++) {
    tmp_array[ndx] = tmp_array2;
    tmp_array2 += ncols;
  }
  /* can not free(tmp_array2) here */
  
  /* Set output argument to point to the newly created array */
  *array = tmp_array;

  /* can not free(tmp_array), here */
  
  return ARRAY_MALLOC_OK;

}

/*
 * Free the memory for a Three-dimensional array allocated by
 * new3DDoubleArray().
 *
 */
void free3DDoubleArray(double*** array) {
  /* free array of  pointers */
  free(array[0][0]);  /* free tmp_array3 */
  free(array[0]);     /* free tem_array2 */
  free(array);        /* free tem_array  */
}

/*
 * Allocate a dynamic, two-dimensional array.
 * Uses the contiguous block of memory approach.
 *
 */
int new2DDoubleArray(int nrows, int ncols, double*** array) {
    int i;
	
	double** tmp_array;
	
	/* Allocate pointers for each "row" of the 2D array */
	tmp_array = (double**)malloc((size_t)(nrows * sizeof(double*)));
	if (tmp_array == NULL) {
	    return ARRAY_MALLOC_FAILED;
	}
	
	/* Allocate contiguous block of memory for the array */
	tmp_array[0] = (double*)malloc((size_t)(nrows * ncols * sizeof(double)));
	if (tmp_array[0] == NULL) {
	    free(tmp_array);
	    return ARRAY_MALLOC_FAILED;
	}
	
	/* Use pointer arithmetic to set indices correctly for each "row".
	 * Note that the first pointer, tmp_array[0], is already set correctly.
	 * Each subsequent element of tmp_array[] is just the previous element
	 * plus the number of columns.
	 */
	for(i = 1; i < nrows; i++) {
		tmp_array[i] = tmp_array[i-1] + ncols;
    }
    
    /* Set output argument to point to the newly created array */
    *array = tmp_array;
    return ARRAY_MALLOC_OK;

}

/*
 * Free the memory for a two-dimensional array allocated by
 * new2DDoubleArray().
 *
 */
void free2DDoubleArray(double** array) {
    if (array == NULL) {
        return;
    }
    /* free memory for the contiguous block */
    if (array[0] != NULL) {
        free(array[0]);
    }
    /* free array of row pointers */
    free(array);
}

int new2DIntArray(int nrows, int ncols, int*** array) {
    int i;
	
	int** tmp_array;
	
	/* Allocate pointers for each "row" of the 2D array */
	tmp_array = (int**)malloc((size_t)(nrows * sizeof(int*)));
	if (tmp_array == NULL) {
	    return ARRAY_MALLOC_FAILED;
	}
	
	/* Allocate contiguous block of memory for the array */
	tmp_array[0] = (int*)malloc((size_t)(nrows * ncols * sizeof(int)));
	if (tmp_array[0] == NULL) {
	    free(tmp_array);
	    return ARRAY_MALLOC_FAILED;
	}
	
	/* Use pointer arithmetic to set indices correctly for each "row".
	 * Note that the first pointer, tmp_array[0], is already set correctly.
	 * Each subsequent element of tmp_array[] is just the previous element
	 * plus the number of columns.
	 */
	for(i = 1; i < nrows; i++) {
		tmp_array[i] = tmp_array[i-1] + ncols;
    }
    
    /* Set output argument to point to the newly created array */
    *array = tmp_array;
    return ARRAY_MALLOC_OK;

}

/*
 * Free the memory for a two-dimensional array allocated by
 * new2DIntArray().
 *
 */
void free2DIntArray(int** array) {
    if (array == NULL) {
        return;
    }
    /* free memory for the contiguous block */
    if (array[0] != NULL) {
        free(array[0]);
    }
    /* free array of row pointers */
    free(array);
}