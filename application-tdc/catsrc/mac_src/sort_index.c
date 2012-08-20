#include <stdlib.h>
#define SWAP(a,b) ftemp=(a);(a)=(b);(b)=ftemp;
#define ISWAP(i,j) itemp=(i);(i)=(j);(j)=itemp;

void indexx(int n, double arr[], int indx[])
/* Indexes an arrayarr[0..n-1], i.e., outputs the array indx[0..n-1]
such that arr[indx[j]] is in ascending order for j = 0, 1,. .., N-1. 
The input quantities n and arr are not changed.
*/
{
	int i, j, itemp;
	double ftemp;
	double *temp;
    temp = (double *) malloc(n*sizeof(double));

	for (j=0;j<n;j++) {
		indx[j]=j;
		temp[j]=arr[j];
	}

	for (j=0;j<(n-1);j++) {
		for(i=(j+1); i < n; i++){
			if(temp[j] > temp[i] ) {
				SWAP(temp[j],temp[i]) 
				ISWAP(indx[j],indx[i])
			}
		}

	}
	free(temp);
}

void indInt(int n, int arr[], int indx[])
/* Indexes an arrayarr[0..n-1], i.e., outputs the array indx[0..n-1]
such that arr[indx[j]] is in ascending order for j = 0, 1,. .., N-1. 
The input quantities n and arr are not changed.
*/
{
	int i, j, itemp;
	int ftemp;
	int *temp;
    temp = (int *) malloc(n*sizeof(int));

	for (j=0;j<n;j++) {
		indx[j]=j;
		temp[j]=arr[j];
	}

	for (j=0;j<(n-1);j++) {
		for(i=(j+1); i < n; i++){
			if(temp[j] > temp[i] ) {
				SWAP(temp[j],temp[i]) 
				ISWAP(indx[j],indx[i])
			}
		}

	}
	free(temp);
}

void indexDescend(int n, double arr[], int indx[])
/* Indexes an arrayarr[0..n-1], i.e., outputs the array indx[0..n-1]
such that arr[indx[j]] is in descending order for j = 0, 1,. .., N-1. 
The input quantities n and arr are not changed.
*/
{
	int i, j, itemp;
	double ftemp;
	double *temp;
    temp = (double *) malloc(n*sizeof(double));

	for (j=0;j<n;j++) {
		indx[j]=j;
		temp[j]=arr[j];
	}

	for (j=0;j<(n-1);j++) {
		for(i=(j+1); i < n; i++){
			if(temp[j] < temp[i] ) {
				SWAP(temp[j],temp[i]) 
				ISWAP(indx[j],indx[i])
			}
		}

	}
	free(temp);
}

void indexIntDescend(int n, int arr[], int indx[])
/* Indexes an arrayarr[0..n-1], i.e., outputs the array indx[0..n-1]
such that arr[indx[j]] is in descending order for j = 0, 1,. .., N-1. 
The input quantities n and arr are not changed.
*/
{
	int i, j, itemp;
	int ftemp;
	int *temp;
    temp = (int *) malloc(n*sizeof(int));

	for (j=0;j<n;j++) {
		indx[j]=j;
		temp[j]=arr[j];
	}

	for (j=0;j<(n-1);j++) {
		for(i=(j+1); i < n; i++){
			if(temp[j] < temp[i] ) {
				SWAP(temp[j],temp[i]) 
				ISWAP(indx[j],indx[i])
			}
		}

	}
	free(temp);
}

void rmdup(int *array, int *length)
{
	int i,j;
	int current;
	int k;
	current = *length;

    for (i = 0; i < current; i++) {
		for (j = (i+1); j< current; j ++) {
		    if (array[i] == array[j]) {
				current--;
                for (k = j; k < current; k++) {
					array[k] = array[k+1];
				}
			}
		}
	}

	*length = current;
}