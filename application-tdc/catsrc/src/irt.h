
#ifndef INCLUDE_IRT_H
#define INCLUDE_IRT_H

double respProb3PL(int response, double ability, double parameters[]);

double respProb2PPC(int response, double ability, double parameters[],
                    int numCategories);
void allRespProb2PPC(double ability, double parameters[], int numCategories,
                     double *probabilities);

double triangularLogit(double ability, double a, double b);
double derivWeight3PL(double ability, double parameters[]);

void logRespProb3PL_derivs(int response, double ability, double parameters[],
                           double *dl, double *ddl);

double derivWeight2PPC(double parameters[]);
void logRespProb2PPC_derivs(int response, double ability, double parameters[],
                            int numCategories, double *dl, double *ddl);
void kSum2PPC(double ability, double parameters[], int numCategories,
              double *ksum, double *k2sum);

double location3PL(double parameters[]);
double location2PPC(double parameters[], int numCategories);

/*
 * Return codes for valid or invalid items.
 */
#define IRT_INVALID_ITEM  0
#define IRT_VALID_ITEM    1

int validate3PL(double parameters[]);
int validate2PPC(double parameters[], int numCategories);

double fisherInfo(double theta, double parameters[]);

#endif
