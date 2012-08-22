rm -rf *.o
rm -rf *.so
gcc -o libCATABE.so -shared -fPIC -I /usr/java/jdk1.6.0_21/include -I /usr/java/jdk1.6.0_21/include/linux array.c item.c irt.c sort_index.c utilityFun.c genRandomDis.c getPar.c cat.c -I.