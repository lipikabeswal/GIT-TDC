/*
 * Module to implement calculation of IRT quantities for 3PL and 2PPC items.
 * Created: Q3 2010
 * Author: Lianghua Shu
 */

#include <stdlib.h>
#include <stddef.h>
#include <stdio.h>
#include <math.h>
#include "irt.h"

static double expCalculation(int category, double ability,
                             double parameters[]);

/*
 * Computes the item response function for the three parameter logistic model:
 * 
 * P(correct response | ability, a, b, c) =
 *
 *                 c + (1 - c) / (1 + exp(-(a*ability - b)))
 * 
 * NOTE that this function uses the "triangular" form of the logit:
 * a*ability - b.
 *
 * Arguments:
 *   int response            An integer representing whether the examinee 
 *                           responded correctly or incorrectly.
 *                           MUST be a zero for incorrect or one for correct.
 *
 *   double ability          The ability level of the examinee.  Can take on
 *                           any value.
 *
 *   double parameters[]     An array of parameter values for the 3PL model.
 *                           MUST have exactly 3 elements:
 *                             parameters[0] -> a           discrimination
 *                             parameters[1] -> b           threshold
 *                             parameters[2] -> c           psuedo-guessing
 *
 * Returns:
 *   double                  The probability of the examinee obtaining the
 *                           given response given their ability level and
 *                           the item parameters.
 *
 * Assumes the following about the input arguments:
 *   a >= 0.0
 *   c >= 0.0 and c <= 1.0
 *   ability is on the same scale as the b parameter
 *   (not really checkable, but included here for completeness)
 *   response = 0 or 1 only
 *
 * For performance reasons, this function does not include range checks on the
 * input arguments.
 */
double respProb3PL(int response, double ability, double parameters[]) {

    /* compute the logit: a*ability - b */
    double logit = triangularLogit(ability, parameters[0], parameters[1]);

    double probCorrect =
               parameters[2] + (1.0 - parameters[2]) / (1.0 + exp(-logit));

    /* assert: response is 0 or 1 */
    if (response == 0) {
        return 1.0 - probCorrect;
    } else { /* response == 1 */
        return probCorrect;
    }
}


/*
 * Computes the "triangular" form of the logit for the three parameter
 * logistic model:
 * 
 *   a*ability - b
 * 
 * Arguments:
 *   double ability          The ability level of the examinee.
 *                           Can take on any value.
 *   double a                The discrimination parameter
 *   double b                The threshold parameter
 *
 * Returns:
 *   double                  Value of the triangular logit.
 *
 * Assumes the following about the input arguments:
 *   a >= 0.0
 *   ability is on the same scale as the b parameter
 *   (not really checkable, but included here for completeness)
 *
 * For performance reasons, this function does not include range checks on
 * the input arguments.
 */
double triangularLogit(double ability, double a, double b) {
    double logit = (a * ability) - b;
    return logit;
}

double fisherInfo(double theta, double parameters[]) {
    double fisherInfo, p;
    double a = parameters[0];
    double c = parameters[2];
     
    p = respProb3PL(1, theta, parameters);
    fisherInfo = a*a*(1.0 - p)*(p-c)*(p-c)/(p*(1.0-c)*(1.0-c));
    
    return fisherInfo;
}

/*
 * Computes the first derivatives of the correct
 * item response function for the three parameter logistic model:
 *
 * NOTE that this function uses the "triangular" form of the logit:
 * a*ability - b.
 *
 *
 * Arguments:
 *
 *   double ability          The ability level of the examinee.
 *                           Can take on any value.
 *
 *   double parameters[]     An array of parameter values for the 3PL model.
 *                           MUST have exactly 3 elements:
 *                             parameters[0] -> a           discrimination
 *                             parameters[1] -> b           threshold
 *                             parameters[2] -> c           psuedo-guessing
 *
 *   double *d1              Output: The first derivative of the correct
 *                           3PL response function.
 */
/* Wendy's spec pp 93 */
double respProb3PL_derivs(double ability, double parameters[]) {
    double p = respProb3PL(1, ability, parameters);
    
    double d1 = parameters[0] * (p - parameters[2]) * ((double)1.0 - p)
		        /((double)1.0 - parameters[2]);
	return d1;
}

/* Return var: variance of the correct 3PL response item */
double respProb3PL_variance(double ability, double parameters[]) {
	double p = respProb3PL(1, ability, parameters);
    double var = p*((double)1.0 - p);;	
	return var;
}

