rm -f getPar.o
rm -f array.o
rm -f genRandomDis.o
rm -f getPar.o
rm -f irt.o
rm -f item.o
rm -f sort_index.o
rm -f utilityFun.o
rm -f cat.o
rm -f libCATABE.jnilib
cc -c -I/System/Library/Frameworks/JavaVM.framework/Headers getPar.c
cc -c -I/System/Library/Frameworks/JavaVM.framework/Headers array.c
cc -c -I/System/Library/Frameworks/JavaVM.framework/Headers genRandomDis.c
cc -c -I/System/Library/Frameworks/JavaVM.framework/Headers getPar.c
cc -c -I/System/Library/Frameworks/JavaVM.framework/Headers irt.c
cc -c -I/System/Library/Frameworks/JavaVM.framework/Headers item.c
cc -c -I/System/Library/Frameworks/JavaVM.framework/Headers sort_index.c
cc -c -I/System/Library/Frameworks/JavaVM.framework/Headers utilityFun.c
cc -c -I/System/Library/Frameworks/JavaVM.framework/Headers cat.c
cc -dynamiclib -o libCATABE.jnilib array.o item.o irt.o sort_index.o utilityFun.o getPar.o genRandomDis.o cat.o -framework JavaVM
cp ./libCATABE.jnilib /Applications/Online\ Assessment/.
