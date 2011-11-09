/* Author: Lianghua Shu
 
 */

#ifndef GENRANDOM_H
#define GENRANDOM_H

/* #define RAND_SEED 1 */

double ran0(long idum);   /* better use this !!! */
double ran1(long *idum);  /* seems having problem on this ??? */
double genNormDis(double mean, double std, double low, double high);
double getNormDisDen(double theta, double mean, double std);
int randomNoBetween(int x, int y);
double corr(int n, double *x, double *y, double *xMean, double *yMean,
			double *xStd, double *yStd, double *rmsd); 
#endif