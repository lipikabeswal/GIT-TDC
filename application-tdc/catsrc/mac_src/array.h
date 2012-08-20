/*
 * Utility functions to allocate dynamic Three-dimensional arrays.
 * Created: Q1 2003
 * Author: Lianghua Shu
 */

#ifndef INCLUDE_ARRAY_H
#define INCLUDE_ARRAY_H

#define ARRAY_MALLOC_OK 0
#define ARRAY_MALLOC_FAILED 1

int new2DDoubleArray(int nrows, int ncols, double*** array);
void free2DDoubleArray(double** array);

int new3DDoubleArray(int nrows, int ncols, int ngrid, double**** array);
void free3DDoubleArray(double*** array);

int new3DIntArray(int nrows, int ncols, int ngrid, int**** array);
void free3DIntArray(int*** array);

int new2DIntArray(int nrows, int ncols, int*** array);
void free2DIntArray(int** array);

#endif
