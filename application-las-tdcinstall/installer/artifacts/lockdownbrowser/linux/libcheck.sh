#!/bin/bash
#look for library libnss3.so.1d
lib1=$(find /usr/lib -name libnss3.so.1d)
#check if string returned is not empty
if [ -n "$lib1" ]; then
	echo $lib1 "found"
	#rm "$lib1"
else
#if string is empty, look for just the so file.
	lib1=$(find /usr/lib -name libnss3.so | grep -v 'firefox\|thunderbird\|chrome\|chromium'| head -n 1)
	if [ -n "$lib1" ]; then
		#link
		ln -s "$lib1" ChromiumLDB/libnss3.so.1d
		echo $lib1 "link created"
	else
		echo "libnss3.so.1d is not installed"
	fi
fi
#look for library libnssutil3.so.1d
lib1=$(find /usr/lib -name libnssutil3.so.1d)
#check if string returned is not empty
if [ -n "$lib1" ]; then
	echo $lib1 "found"
	#rm "$lib1"
else
#if string is empty, look for just the so file.
	lib1=$(find /usr/lib -name libnssutil3.so | grep -v 'firefox\|thunderbird\|chrome\|chromium'| head -n 1)
	if [ -n "$lib1" ]; then
		#link
		ln -s "$lib1" ChromiumLDB/libnssutil3.so.1d
		echo $lib1 "link created"
	else
		echo "libnssutil3.so.1d is not installed"
	fi
fi
lib1=$(find /usr/lib -name libsmime3.so.1d)
#check if string returned is not empty
if [ -n "$lib1" ]; then
	echo $lib1 "found"
	#rm "$lib1"
else
#if string is empty, look for just the so file.
	lib1=$(find /usr/lib -name libsmime3.so | grep -v 'firefox\|thunderbird\|chrome\|chromium'| head -n 1)
	if [ -n "$lib1" ]; then
		#link
		ln -s "$lib1" ChromiumLDB/libsmime3.so.1d
		echo $lib1 "link created"
	else
		echo "libsmime3.so.1d is not installed"
	fi
fi
lib1=$(find /usr/lib -name libplc4.so.0d)
#check if string returned is not empty
if [ -n "$lib1" ]; then
	echo $lib1 "found"
	#rm "$lib1"
else
#if string is empty, look for just the so file.
	lib1=$(find /usr/lib -name libplc4.so | grep -v 'firefox\|thunderbird\|chrome\|chromium'| head -n 1)
	if [ -n "$lib1" ]; then
		#link
		ln -s "$lib1" ChromiumLDB/libplc4.so.0d
		echo $lib1 "link created"
	else
		echo "libnss3.so.1d is not installed"
	fi
fi
lib1=$(find /usr/lib -name libnspr4.so.0d)
#check if string returned is not empty
if [ -n "$lib1" ]; then
	echo $lib1 "found"
	#rm "$lib1"
else
#if string is empty, look for just the so file.
	lib1=$(find /usr/lib -name libnspr4.so | grep -v 'firefox\|thunderbird\|chrome\|chromium'| head -n 1)
	if [ -n "$lib1" ]; then
		#link
		ln -s "$lib1" ChromiumLDB/libnspr4.so
		echo $lib1 "link created"
	else
		echo "libnss3.so.1d is not installed"
	fi
fi
