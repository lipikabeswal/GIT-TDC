
#ifndef INCLUDE_IRT_H
#define INCLUDE_IRT_H

double respProb3PL(int response, double ability, double parameters[]);
double triangularLogit(double ability, double a, double b);
double fisherInfo(double theta, double parameters[]);
double respProb3PL_derivs(double ability, double parameters[]);
double respProb3PL_variance(double ability, double parameters[]);

#endif
