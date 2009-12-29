#include <stdio.h>

#include <stdlib.h>

#include <string.h>

#include <ctype.h>


int main()

{

        static const char filename1[] = "TempProcess";

	int value;

        FILE *file1 = fopen(filename1,"r");
	
	if( file1 != NULL )
	{	
		value=fgetc(file1);
	}
	
	return value;
	
}


