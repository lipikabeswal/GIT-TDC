#!/bin/bash


#look for library libnss3.so.1d
lib1=$(locate libnss3.so.1d |  grep /usr/lib)
#check if string returned is not empty
if [ -n "$lib1" ]; then
	echo $lib1 "found"
	#rm "$lib1"
else
#if string is empty, look for just the so file.
	lib1=$(locate libnss3.so | grep /usr/lib/ | grep -v 'firefox\|thunderbird\|chrome\|chromium'| head -n 1)
	if [ -n "$lib1" ]; then
		#link
		sudo ln -s "$lib1" "$lib1.1d"
		echo $lib1 "link created"
	else
		echo "libnss3.so.1d is not installed"
	fi
fi
#look for library libnssutil3.so.1d
lib1=$(locate libnssutil3.so.1d |  grep /usr/lib)
#check if string returned is not empty
if [ -n "$lib1" ]; then
	echo $lib1 "found"
	#rm "$lib1"
else
#if string is empty, look for just the so file.
	lib1=$(locate libnssutil3.so | grep /usr/lib/ | grep -v 'firefox\|thunderbird\|chrome\|chromium'| head -n 1)
	if [ -n "$lib1" ]; then
		#link
		sudo ln -s "$lib1" "$lib1.1d"
		echo $lib1 "link created"
	else
		echo "libnssutil3.so.1d is not installed"
	fi
fi
lib1=$(locate libsmime3.so.1d |  grep /usr/lib)
#check if string returned is not empty
if [ -n "$lib1" ]; then
	echo $lib1 "found"
	#rm "$lib1"
else
#if string is empty, look for just the so file.
	lib1=$(locate libsmime3.so |  grep /usr/lib | grep -v 'firefox\|thunderbird\|chrome\|chromium'| head -n 1)
	if [ -n "$lib1" ]; then
		#link
		sudo ln -s "$lib1" "$lib1.1d"
		echo $lib1 "link created"
	else
		echo "libsmime3.so.1d is not installed"
	fi
fi
lib1=$(locate libplc4.so.0d |  grep /usr/lib)
#check if string returned is not empty
if [ -n "$lib1" ]; then
	echo $lib1 "found"
	#rm "$lib1"
else
#if string is empty, look for just the so file.
	lib1=$(locate libplc4.so | grep /usr/lib/ | grep -v 'firefox\|thunderbird\|chrome\|chromium'| head -n 1)
	if [ -n "$lib1" ]; then
		#link
		sudo ln -s "$lib1" "$lib1.0d"
		echo $lib1 "link created"
	else
		echo "libnss3.so.1d is not installed"
	fi
fi
lib1=$(locate libnspr4.so.0d |  grep /usr/lib)
#check if string returned is not empty
if [ -n "$lib1" ]; then
	echo $lib1 "found"
	#rm "$lib1"
else
#if string is empty, look for just the so file.
	lib1=$(locate libnspr4.so | grep /usr/lib/ | grep -v 'firefox\|thunderbird\|chrome\|chromium'| head -n 1)
	if [ -n "$lib1" ]; then
		#link
		sudo ln -s "$lib1" "$lib1.0d"
		echo $lib1 "link created"
	else
		echo "libnss3.so.1d is not installed"
	fi
fi

