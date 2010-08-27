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
 * Computes the item response function for the two parameter partial credit
 * model:
 * 
 * P(obtaining category k | ability, a, b) = 
 *
 *               exp( (k-1)*a*theta - sum{v=2 to k}[b_v] )
 *     --------------------------------------------------------------
 *      1 + sum{c=2 to m}[exp( (c-1)*a*theta - sum{v=2 to c}[b_v] )]
 *
 * where m is the number of categories and k is in {1..m}.
 *
 * Arguments:
 *   int response            An integer representing category response of the
 *                           examinee
 *                           MUST be an integer in the set {0, 1, ..., m-1}
 *                           NOTE that this means 'response' is zero offset
 *                           while 'k' in the above formula and in the code
 *                           below is unit offset.
 *                           We assume response is encoded this way b/c it is
 *                           the traditional method of representing responses
 *                           in CTB's data.
 *
 *   double ability          The ability level of the examinee.  Can take on
 *                           any value.
 *
 *   double parameters[]     An array of parameter values for the 2PPC model.
 *                           MUST have exactly m elements:
 *                             parameters[0] -> a      discrimination
 *                             parameters[1] -> b_2    threshold between
 *                                                     categories 1 and 2
 *                             parameters[2] -> b_3    threshold between
 *                                                     categories 2 and 3
 *                             ...
 *                             parameters[m-1] -> b_m  threshold between
 *                             categories m-1 and m
 *   int numCategories       The total number of response categories, m
 *
 * Returns:
 *   double                  The probability of the examinee obtaining the
 *                           given response given their ability level and
 *                           the item parameters.
 *
 * Assumes the following about the input arguments:
 *   a >= 0.0
 *   b_v can be any value
 *   ability is on the same scale as the b_v parameters
 *   (not really checkable, but included here for completeness)
 *   the array parameters is of length numCategories
 *   response is an integer in the set {0, 1, ..., m-1}
 *
 * For performance reasons, this function does not include range checks on the
 * input arguments.
 */
double respProb2PPC(int response, double ability, double parameters[],
                    int numCategories) {

    /* Notice that the term in the numerator and each term in the sum in the 
     * denominator are of the same form.
     *
     * So the idea behind the code below is to calculate all of the terms in
     * the sum for the denominator, but then notice when we get to the term
     * that is the correct one for the numerator.  In the formula above this
     * translates to (c == k) and in the code below, to (k == response+1)
     */

    /* Initialize numerator term to 1.0
     * Note that if response is the firstResponse, then the value of numer
     * never changes from 1.0.  Otherwise, numer is set to the appropriate
     * value for the obtained category.
     */
    double numer = 1.0;

    /* Initialize accumulator for the denominator term to 1.0
     * Note that we initialize to 1.0 and loop from 2 instead of
     * initializing to 0.0 and looping from 1
     */
    double denom = 1.0;
    
    int k;
    for (k = 2; k <= numCategories; k++) {
        double ez;
        
        ez = expCalculation(k, ability, parameters);

        /* Check to see if k has reached the observed response category
         * If so, set the value of the numerator accordingly
         * NOTE: k is on the 1..numCategories scale,
         * response is on the 0..numCategories-1 scale
         */
        if (k == response + 1) {
            numer = ez;
        }

        /* Accumulate in denominator term */
        denom += ez;
    }

    return numer/denom;
}


/*
 * Computes the item response function for the two parameter partial credit
 * model:
 * 
 * P(obtaining category k | ability, a, b)
 *
 * for ALL values of k from 1 to m, where m is the total number of response
 * categories.
 * (See also comments for respProb2PPC() above.)
 *
 * Arguments:
 *   double ability          The ability level of the examinee.  Can take on
 *                           any value.
 *
 *   double parameters[]     An array of parameter values for the 2PPC model.
 *                           MUST have exactly m elements:
 *                             parameters[0] -> a      discrimination
 *                             parameters[1] -> b_2    threshold between
 *                                                     categories 1 and 2
 *                             parameters[2] -> b_3    threshold between
 *                                                     categories 2 and 3
 *                             ...
 *                             parameters[m-1] -> b_m  threshold between
 *                                                     categories m-1 and m
 *   int numCategories       The total number of response categories, m
 * 
 *   double probabilities[]  Output:  On return, probabilities[i] contains the
 *                           probability of the examinee obtaining the category
 *                           i+1 (or equivalently "response" i - remember we
 *                           think of responses as zero offset.)
 *
 *
 * We write two different versions of allRespProb2PPC() and respProb2PPC()
 * instead of implementing one and calling the other (either direction would
 * work) for performance reasons.  In one direction, we would needlessly
 * recompute the denominator term multiple times.  In the other direction,
 * we would need to allocate an array for all probabilities, return one and
 * discard the rest.  Either would be inefficient.
 *
 * Assumes the following about the input arguments:
 *   a >= 0.0
 *   b_v can be any value
 *   ability is on the same scale as the b_v parameters
 *   (not really checkable, but included here for completeness)
 *   the array parameters is of length numCategories
 *   the output array probabilities has been allocated memory enough
 *     for numCategories elements
 *
 * For performance reasons, this function does not include range checks
 * on the input arguments.
 */
void allRespProb2PPC(double ability, double parameters[], int numCategories,
                     double *probabilities) {
    int k;  /* index variables */
    double denom;
    
    /* We treat the first category as a special case to save computation: */
    
    /* Initialize numerator term for first category which by definition
     * is 1.0 */
    probabilities[0] = 1.0;

    /* Initialize accumulator for the denominator term to 1.0
     * Note that we initialize to 1.0 and loop from 2 instead of
     * initializing to 0.0 and looping from 1
     */
    denom = 1.0;

    /* For the rest of the categories (2 and up) compute each numerator term
     * and also accumulate in the denominator term.
     */
    for (k = 2; k <= numCategories; k++) {
        double ez;
        
        ez = expCalculation(k, ability, parameters);

        /* The numerator term for category k */
        probabilities[k-1] = ez;
        
        /* Accumulate in denominator term */
        denom += ez;
    }
    
    /* compute the final probabilities by dividing by normalizing factor */
    for (k = 1; k <= numCategories; k++) {
        probabilities[k-1] /= denom;
    }
}



/*
 * Utility function for the respProb2PPC() and allRespProb2PPC() functions.
 * Note the use of static.  This function should not be called by any external
 * caller.
 * Computes the exponential term in the 2PPC model:
 *
 *   exp( (k-1)*a*theta - sum{v=2 to k}[b_v] )
 *
 * where k is the desired category response (in the range 1..m, where m is the
 * total number of response categories).
 *
 * Arguments:
 *   int category            An integer representing an examinee response
 *                           category
 *                           MUST be an integer in the set {1, 2, ..., m}
 *
 *   double ability          The ability level of the examinee.  Can take on
 *                           any value.
 *
 *   double parameters[]     An array of parameter values for the 2PPC model.
 *                           MUST have exactly m elements:
 *                             parameters[0] -> a      discrimination
 *                             parameters[1] -> b_2    threshold between
 *                                                     categories 1 and 2
 *                             parameters[2] -> b_3    threshold between
 *                                                     categories 2 and 3
 *                             ...
 *                             parameters[m-1] -> b_m  threshold between
 *                                                     categories m-1 and m
 * Returns:
 *   double                  The exponential term in the 2PPC model.
 *
 * For performance reasons, this function does NOT include range checks on the
 * input arguments.
 * !!!!! Note that we assume that the array 'parameters' has at least
 * 'category' number of elements !!!!!
 */
static double expCalculation(int category, double ability,
                             double parameters[]) {
    int j;
    double bSum, z, ez;
    
    bSum = 0.0;
    for (j = 2; j <= category; j++) {
        bSum += parameters[j - 1];
    }

    z = (double)(category-1) * (parameters[0] * ability) - bSum;

    ez = exp(z);
    
    return ez;
}


/*
 * Compute the "weight" for the 3PL model:
 *
 *   w = a / ( 1 + c * exp(-(a*ability - b)) )
 *
 * NOTE that this function uses the "triangular" form of the logit:
 * a*ability - b.
 * See also equation 1.6 in the high-level design spec for the Grid Search.
 * This quantity is used in the calculation of the derivatives of the log of
 * the probability.
 *
 * Arguments:
 *   double ability          The ability level of the examinee.  Can take on
 *                           any value.
 *
 *   double parameters[]     An array of parameter values for the 3PL model.
 *                           MUST have exactly 3 elements:
 *                             parameters[0] -> a           discrimination
 *                             parameters[1] -> b           threshold
 *                             parameters[2] -> c           psuedo-guessing
 */
double derivWeight3PL(double ability, double parameters[]) {
    double logit = triangularLogit(ability, parameters[0], parameters[1]);
    double w = parameters[0]/(1.0 + parameters[2] * exp(-logit));
    return w;
}


/*
 * Compute the "weight" for the 2PPC model which is just the a parameter
 *
 * Arguments:
 *   double parameters[]     An array of parameter values for the 2PPC model.
 *                           MUST have exactly m elements:
 *                             parameters[0] -> a      discrimination
 *                             parameters[1] -> b_2    threshold between
 *                                                     categories 1 and 2
 *                             parameters[2] -> b_3    threshold between
 *                                                     categories 2 and 3
 *                             ...
 *                             parameters[m-1] -> b_m  threshold between
 *                                                     categories m-1 and m
 */
double derivWeight2PPC(double parameters[]) {
    return parameters[0];
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


/*
 * Computes the first and second derivatives of the log of the
 * item response function for the three parameter logistic model:
 * 
 * See equations 1.1 and 1.2 of the high-level design spec for grid search.
 * 
 * NOTE that this function uses the "triangular" form of the logit:
 * a*ability - b.
 *
 * Note that we opted to create one function to calculate both the first and
 * second deriviatives.  It so happens that both the first and second
 * derivatives can be computed using many of the same sub-expressions and
 * because the Newton method needs both anyway, we calculate both at the same
 * time to avoid some (but NOT ALL) of the unnecessary duplicate calculations
 * (especially to the expensive exp() call).
 *
 * The code below was written to balance the competing
 * needs of readability/modularity and performance.  However, the current bias
 * is toward readability/modularity.  Would could rewrite this function to
 * eliminate a few more redundant sub-expression evaluations, but will defer
 * that until peformance benchmarks are performed.
 *
 * Arguments:
 *   int response            An integer representing whether the examinee 
 *                           responded correctly or incorrectly.
 *                           MUST be a zero for incorrect or one for correct.
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
 *   double *dl              Output: The first derivative of the log of the
 *                           3PL response function.
 *   double *ddl             Output: The second derivative of the log of the
 *                           3PL response function.
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
void logRespProb3PL_derivs(int response, double ability, double parameters[],
                           double *dl, double *ddl) {
    double p = respProb3PL(1, ability, parameters);
    double w = derivWeight3PL(ability, parameters);
    double logit = triangularLogit(ability, parameters[0], parameters[1]);
    
    *dl = w * ((double)response - p);
    
    *ddl = w*w * exp(-logit) * (parameters[2] * (double)response - p*p);
}


/*
 * Computes the first and second derivatives of the log of the
 * item response function for the two parameter partial credit model:
 * 
 * See equations 1.1 and 1.2 of the high-level design spec for grid search.
 * 
 * Arguments:
 *   int response            An integer representing the category response of
 *                           the examinee
 *                           MUST be an integer in the set {0, 1, ..., m-1}
 *                           NOTE that this means 'response' is zero offset
 *                           while 'k' in the above formula and in the code
 *                           below is unit offset.
 *                           We assume response is encoded this way b/c it is
 *                           the traditional method of representing responses
 *                           in CTB's data.
 *
 *   double ability          The ability level of the examinee.
 *                           Can take on any value.
 *
 *   double parameters[]     An array of parameter values for the 2PPC model.
 *                           MUST have exactly m elements:
 *                             parameters[0] -> a      discrimination
 *                             parameters[1] -> b_2    threshold between
 *                                                     categories 1 and 2
 *                             parameters[2] -> b_3    threshold between
 *                                                     categories 2 and 3
 *                             ...
 *                             parameters[m-1] -> b_m  threshold between
 *                                                     categories m-1 and m
 *   int numCategories       The total number of response categories, m
 *
 *   double *dl              Output: The first derivative of the log of the
 *                           2PPC response function.
 *   double *ddl             Output: The second derivative of the log of the
 *                           2PPC response function.
 */
void logRespProb2PPC_derivs(int response, double ability, double parameters[],
                            int numCategories,
                            double *dl, double *ddl) {
    double w;
    double ksum, k2sum;
    
    w = derivWeight2PPC(parameters);
    
    kSum2PPC(ability, parameters, numCategories, &ksum, &k2sum);
    
    /* note that we add one to the response since we assume response is
     * encoded zero-offset on input, but the formula in equation 1.1 of
     * the high-level design spec calls for unit-offset response (u_i
     * in equation 1.1). */
    *dl = w * ((double)(response + 1) - ksum);
    
    *ddl = - ( w*w * (k2sum - ksum*ksum) );
}


/*
 * Utility function for the logRespProb2PPC_derivs() function.  Also used by
 * likelihood_compute_weights() in the likelihood module.
 *
 *   sum[k=1 to m]( k * p_k )    and     sum[k=1 to m]( k^2 * p_k )
 *
 * where m is the number of response categories and p_k is the probability of
 * an examinee of the given ability obtaining response category k.  These sums
 * are used in the computation of the derivatives of the log of the response
 * probabilities.
 *
 * Arguments:
 *   double ability          The ability level of the examinee.
 *                           Can take on any value.
 *
 *   double parameters[]     An array of parameter values for the 2PPC model.
 *                           MUST have exactly m elements:
 *                             parameters[0] -> a      discrimination
 *                             parameters[1] -> b_2    threshold between
 *                                                     categories 1 and 2
 *                             parameters[2] -> b_3    threshold between
 *                                                     categories 2 and 3
 *                             ...
 *                             parameters[m-1] -> b_m  threshold between
 *                             categories m-1 and m
 *   int numCategories       The total number of response categories, m
 * 
 *   double *ksum            Output:  On return, contains the value of
 *                           sum[k=1 to m]( k * p_k )
 *   double *k2sum           Output:  On return, contains the value of
 *                           sum[k=1 to m]( k^2 * p_k )
 */
void kSum2PPC(double ability, double parameters[], int numCategories,
              double *ksum, double *k2sum) {
    double *prob = NULL;
    int k;
    
    /* allocate temporary work array */
    prob = (double *) malloc((size_t)(numCategories*sizeof(double)));
    if (prob == NULL) {
        fprintf(stderr, "Malloc failure in kSum2PPC().\n");
        exit(EXIT_FAILURE);
    }
    
    /* compute the response probabilities for all possible responses */
    /* note that this is the expensive part of the calculation */
    allRespProb2PPC(ability, parameters, numCategories, prob);
    
    /* now calculate the sum of the probabilities, weighted by k and k^2 */
    *ksum = 0.0;
    *k2sum = 0.0;
    for (k = 1; k <= numCategories; k++) {
        *ksum  += ((double)k) * prob[k-1];
        *k2sum += ((double)(k*k)) * prob[k-1];
    }
    
    /* free temporary work array */
    free(prob);
    
    return;
}


/*
 * Calculate the item location for the 3PL model given item parameters in
 * the triangular metric.  The item location the examinee ability that
 * maximizes the item information function and is given by the following
 * formula (for the triangular metric):
 *
 *   b + ln[ 0.5 + 0.5(1 + 8c)^0.5 ]
 *   -------------------------------
 *                 a
 *
 * Arguments:
 *   double parameters[]     An array of parameter values for the 3PL model.
 *                           MUST have exactly 3 elements:
 *                             parameters[0] -> a           discrimination
 *                             parameters[1] -> b           threshold
 *                             parameters[2] -> c           psuedo-guessing
 *
 * Returns:
 *   double   The item location
 *
 * For performance reasons, this function does not include range checks on
 * the input arguments and assumes the parameter values all make sense.
 * However, it does check for a == 0 since we have to divide by a.
 */
double location3PL(double parameters[]) {
    double location;
    double a = parameters[0];
    double b = parameters[1];
    double c = parameters[2];
    
    if (a == 0.0) {
        fprintf(stderr, "Discrimination equal to zero in location3PL()\n");
        fprintf(stderr, "resulting in divide by zero.\n");
        exit(EXIT_FAILURE);
    }
    
    location = b + log( 0.5 + 0.5*sqrt(1.0 + 8.0*c) );
    location /= a;
    
    return location;
}


/*
 * Calculate the "item location" for the 2PPC model.  As there is no 
 * closed form for the 2PPC item location we use the following
 * approximation:
 *
 *   sum[k=2 to m](b_k)
 *   ---------------------
 *       a(m - 1)
 *
 * where m is the number of categories for the item.
 * See also the Hoss-Loss Check design document.
 *
 * Arguments:
 *   double parameters[]     An array of parameter values for the 2PPC model.
 *                           MUST have exactly m elements:
 *                             parameters[0] -> a      discrimination
 *                             parameters[1] -> b_2    threshold between
 *                                                     categories 1 and 2
 *                             parameters[2] -> b_3    threshold between
 *                                                     categories 2 and 3
 *                             ...
 *                             parameters[m-1] -> b_m  threshold between
 *                             categories m-1 and m
 *   int numCategories       The total number of response categories, m
 *
 * Returns:
 *   double   The item location
 *
 * For performance reasons, this function does not include range checks on
 * the input arguments and assumes the parameter values all make sense.
 */
double location2PPC(double parameters[], int numCategories) {
    int i;
    double location;
    double bsum;
    double denom;

    /* Compute the denominator a(m - 1) */
    denom = parameters[0] * (double)(numCategories - 1);
    if (denom == 0.0) {
        fprintf(stderr, "Denominator is zero in location2PPC()\n");
        exit(EXIT_FAILURE);
    }
    
    /* Compute sum of the b parameters for the numerator */
    bsum = 0.0;
    for (i = 1; i < numCategories; i++) {
        bsum += parameters[i];
    }
    
    location = bsum / denom;
    
    return location;
}

/*
 * Validate parameters for the 3PL model.
 * Valid parameter ranges are:
 *
 * Discrimination must be greater than zero.
 * Threshold can take any value.
 * Pseudo-guessing parameter must be between zero and one.
 *
 * Arguments:
 *   double parameters[]     An array of parameter values for the 3PL model.
 *                           MUST have exactly 3 elements:
 *                             parameters[0] -> a           discrimination
 *                             parameters[1] -> b           threshold
 *                             parameters[2] -> c           pseudo-guessing
 *
 * Returns:
 *   int     Return code indicating valid or invalid parameters
 */
int validate3PL(double parameters[]) {

    if (parameters == NULL) {
        return IRT_INVALID_ITEM;
    }
    
    /* Discrimination must be greater than zero.
     * Pseudo-guessing parameter must be in [0, 1]. */
    if (parameters[0] <= 0.0 ||
        parameters[2] < 0.0 ||
        parameters[2] > 1.0) {
        return IRT_INVALID_ITEM;
    }
    
    return IRT_VALID_ITEM;
}


/*
 * Validate parameters for the 2PPC model.
 * Valid parameter ranges are:
 *
 * Discrimination must be greater than zero.
 * Thresholds can take any value.
 * The number of categories must be greater than one.
 *
 * Arguments:
 *   double parameters[]     An array of parameter values for the 2PPC model.
 *                           MUST have exactly m elements:
 *                             parameters[0] -> a      discrimination
 *                             parameters[1] -> b_2    threshold between
 *                                                     categories 1 and 2
 *                             parameters[2] -> b_3    threshold between
 *                                                     categories 2 and 3
 *                             ...
 *                             parameters[m-1] -> b_m  threshold between
 *                             categories m-1 and m
 *   int numCategories       The total number of response categories, m
 *
 * Returns:
 *   int     Return code indicating valid or invalid parameters
 */
int validate2PPC(double parameters[], int numCategories) {
    
    if (parameters == NULL) {
        return IRT_INVALID_ITEM;
    }
    
    /* Number of levels must be greater than 1.
     * Discrimination must be greater than zero. */
    if (numCategories < 2 ||
        parameters[0] <= 0.0) {
        return IRT_INVALID_ITEM;
    }
    
    return IRT_VALID_ITEM;
}

double fisherInfo(double theta, double parameters[]) {
    double fisherInfo, p;
    double a = parameters[0];
    double c = parameters[2];
     
    p = respProb3PL(1, theta, parameters);
    fisherInfo = a*a*(1.0 - p)*(p-c)*(p-c)/(p*(1.0-c)*(1.0-c));
    
    return fisherInfo;
}

