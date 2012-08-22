#include<math.h>
#include <stdlib.h>
#include<time.h>
#include <stdio.h>

#define IA 16807 
#define IM 2147483647 
#define AM (1.0/IM) 
#define IQ 127773 
#define IR 2836 
#define NTAB 32 
#define NDIV (1+(IM-1)/NTAB) 
#define EPS 1.2e-7 
#define RNMX (1.0-EPS)

static double ran0v[98];
static double ran0y;
double ran01();

double gasdev(long idum);
/* double gasdev(long *idum); */
int random_num();

/* Return random number from x to y inclusive */
int randomNoBetween(int x, int y) {
    return (random_num() %(y-x+1))+x;
}

int random_num() {
  return (rand()<<15) | rand();
}



double getNormDisDen(double theta, double mean, double std){
	double x;
	double pi = 3.14159265;
    x = exp(-0.5*(theta-mean)*(theta-mean)/(std*std))/(std*sqrt(2.0*pi));	
    return x;
}
  
double genNormDis(double mean, double std, double low, double high){
	double x;
	long i = 1;
/*	srand(RAND_SEED);
//	x = mean + gasdev(&i) * std;
*/
    x = mean + gasdev(i) * std;
	if ( x < low ) x = low;
	if (x > high ) x = high;

    return x;
}

double ran01(){
	/* We assume rand() returns an integer in the range [0, RAND_MAX]
     * (inclusive).
     * So frac is a double in the range [0, RAND_MAX/(RAND_MAX+1)] or
     * approximately [0, 1) (not inclusive of one). */
   return (double)rand()/(double)(RAND_MAX+1);
}

  /* generate a uniform random value between 0-1
  // call ran0(-1) to initialize once first,
  // then use ran0(1) to generate random value
  */
double ran0(long idum){ /* Adapted from Numerical Recipes in Pascal */

  double dum; /* ran0y; */
  int j; 
/*  double ran0v[98]; */
  if (idum < 0 ){ 
  
 /*   srand(RAND_SEED);
 //	  Randomize();
 //	  srand( (unsigned)time( NULL ) );
 */

    idum = 1;
    for (j = 1; j <= 97; j++) 
      dum = ran01();
    for (j = 1; j <= 97; j++) 
      ran0v[j] = ran01();
    ran0y = ran01();

  }
  j = 1 + (int)(97.0 * ran0y);
  if ((j > 97) || (j < 1)) //
    printf("Error in function ran0 j= %d", j);
  ran0y = ran0v[j];
  ran0v[j] = ran01();
  return ran0y;
}

double ran1(long *idum) /* from Numverical recipes
// "Minimal" random number generator of Park and Miller with Bays-Durham shu?e and 
// added safeguards. Returns a uniform random deviate between 0.0 and 1.0 
// (exclusive of the endpoint values). Call with idum a negative integer to initialize; 
// thereafter, do not alter idum between successive deviates in a sequence. 
// RNMX should approximate the largest ?oating value that is less than 1. 
*/
{ 
	int j; 
	long k; 
	static long iy=0; 
	static long iv[NTAB]; 
	double temp; 
    if (*idum <= 0 || !iy) { /* Initialize.  */
		if (-(*idum) < 1) *idum=1; /* Be sure to prevent idum =0. */
		else *idum = -(*idum); 
		for (j=NTAB+7;j>=0;j--) { /*Load the shu?e table (after 8 warm-ups). */
            k=(*idum)/IQ;
            *idum=IA*(*idum-k*IQ)-IR*k;
            if (*idum < 0) *idum += IM;
            if (j < NTAB) iv[j] = *idum;
		} 
        iy=iv[0]; 
	} 
	k=(*idum)/IQ; /* Start here when not initializing.  */
	*idum=IA*(*idum-k*IQ)-IR*k; /* Compute idum=(IA*idum) % IM without over- */
	if (*idum < 0) *idum += IM; /* ?ows by Schrage's method.  */
	j=iy/NDIV;  /* Will be in the range 0..NTAB-1. */
	iy=iv[j];   /* Output previously stored value and re?ll the */
	iv[j] = *idum; /* shu?e table.  */
	if ((temp=AM*iy) > RNMX) return RNMX; /* Because users don't expect endpoint values. */
	else return temp; 
} 

double gasdev(long idum) /* from Numverical recipes
// double gasdev(long *idum)
// Returns a normally distributed deviate with zero mean and unit variance, 
// using ran1(idum) as the source of uniform deviates. 
*/
{ 
	double ran0(long idum); 
/*    double ran1(long *idum);  */

	static int iset=0; 
	static double gset; 
	double fac,rsq,v1,v2; 
    if (iset == 0) { /* We don't have an extra deviate handy, so  */
        do { 
	/*		v1=2.0*ran1(idum)-1.0; // pick two uniform numbers in the square ex-
	//		v2=2.0*ran1(idum)-1.0; // tending from -1 to +1 in each direction,
	*/		
			v1=2.0*ran0(idum)-1.0; /* pick two uniform numbers in the square ex- */
			v2=2.0*ran0(idum)-1.0;

			rsq=v1*v1+v2*v2; /* see if they are in the unit circle,  */
		} while (rsq >= 1.0 || rsq == 0.0); /* and if they are not, try again. */

        fac=sqrt(-2.0*log(rsq)/rsq); 
        /* Now make the Box-Muller transformation to get two normal deviates. Return one and 
	    // save the other for next time. 
		*/
	    gset=v1*fac; 
    	iset=1; 	/* Set ?ag. */
    	return v2*fac; 	
	} 	
	else { 	/* We have an extra deviate handy, */
		iset=0;  /* 	so unset the ?ag, */
		return gset; /*	andreturnit. */
	} 		
} 	

double corr(int n, double *x, double *y, double *xMean, double *yMean,
			double *xStd, double *yStd, double *rmsd) {
	double xm = 0.0;
	double ym = 0.0;
	double rms = 0.0;
	double sx = 0.0;
	double sy = 0.0;
	double xy = 0.0;
	double cor;
	int i;
    for ( i = 0; i < n; i++) {
        xm = xm + x[i];
	    ym = ym + y[i];	
	}
	xm = xm/(double)n;
	ym = ym/(double)n;
	
	
    for ( i = 0; i < n; i++) {
        sx = sx + (x[i]-xm)*(x[i]-xm);
	    sy = sy + (y[i]-ym)*(y[i]-ym);
		xy = xy + (x[i]-xm)*(y[i]-ym);
		rms = rms + (x[i] - y[i])*(x[i] - y[i]);
	}

	cor = xy/sqrt(sx*sy);

    sx = sqrt(sx/(double)n);
	sy = sqrt(sy/(double)n);
	rms = sqrt(rms/(double)n);

    *xMean = xm;
    *yMean = ym;
    *xStd = sx;
	*yStd = sy;
    *rmsd = rms;
  
    return cor;
}		

