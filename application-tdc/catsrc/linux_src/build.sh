rm -rf *.o
rm -rf *.so
gcc -o libCATABE.so -shared -fPIC -I /home/ayanb/Desktop/jdk1.6.0_18/include -I /home/ayanb/Desktop/jdk1.6.0_18/include/linux array.c item.c irt.c sort_index.c utilityFun.c genRandomDis.c getPar.c cat.c -I.
