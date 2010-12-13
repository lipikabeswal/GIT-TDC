#!/bin/bash 
 
#ps -aef > processcheck 
check=0 
strVal="" 
 
#BROWSERS 
 
 if [ ! -z "$(ps -Ae | grep 'OmniWeb' | grep -v grep)" ] 
 	then check=1 
	strVal="$strVal|$check" 
 
 fi 
 
 if [ ! -z "$(ps -Ae | grep 'Shirra' | grep -v grep)" ] 
 	then check=2 
	strVal="$strVal|$check" 
fi 
 
if [ ! -z "$(ps -Ae | grep 'Camino' | grep -v grep)" ] 
 	then check=3 
	strVal="$strVal|$check" 
	 
fi 
 
if [ ! -z "$(ps -Ae | grep 'Stainless' | grep -v grep)" ] 
 	then check=4 
	strVal="$strVal|$check" 
fi 
 
if [ ! -z "$(ps -Ae | grep 'Safari.app' | grep -v grep)" ] 
 	then check=5 
	strVal="$strVal|$check" 
	 
fi 
 
if [ ! -z "$(ps -Ae | grep 'Firefox' | grep -v grep)" ] 
 	then check=36 
	strVal="$strVal|$check" 
fi 
 
#SCREENCAPTURE 
 
 if [ ! -z "$(ps -Ae | grep 'Capture Me' | grep -v grep)" ] 
 	then check=6 
	strVal="$strVal|$check" 
fi 
 
 if [ ! -z "$(ps -Ae | grep 'Constrictor' | grep -v grep)" ] 
 	then check=7 
	strVal="$strVal|$check" 
fi 
 
 if [ ! -z "$(ps -Ae | grep 'Copernius' | grep -v grep)" ]  
 	then check=8 
	strVal="$strVal|$check" 
fi 
 
 if [ ! -z "$(ps -Ae | grep 'Flash-It' | grep -v grep)" ] 
 	then check=9 
	strVal="$strVal|$check" 
fi 
 
if [ ! -z "$(ps -Ae | grep 'Fly Sketch' | grep -v grep)" ] 
 	then check=10 
	strVal="$strVal|$check" 
 
fi 
#IM 
 
 if [ ! -z "$(ps -Ae | grep 'Adium' | grep -v grep)" ] 
 	then check=11 
	strVal="$strVal|$check" 
fi 
  
 if [ ! -z "$(ps -Ae | grep 'Skype' | grep -v grep)" ] 
 	then check=12 
	strVal="$strVal|$check"  
fi 
 
 if [ ! -z "$(ps -Ae | grep 'Pidgin' | grep -v grep)" ] 
 	then check=13 
	strVal="$strVal|$check"  
fi 
 
if [ ! -z "$(ps -Ae| grep 'Google Talk' | grep -v grep)" ] 
 	then check=14 
	strVal="$strVal|$check" 
fi 
 
 
if [ ! -z "$(ps -Ae | grep 'Trillian' | grep -v grep)" ] 
 	then check=15 
	strVal="$strVal|$check" 
 
fi 
 
#EMAIL-CLIENTS 
 
 if [ ! -z "$(ps -Ae | grep 'Mozilla Thunderbird' | grep -v grep)" ] 
 	then check=16 
	strVal="$strVal|$check" 
fi 
 
 if [ ! -z "$(ps -Ae | grep 'Eudora' | grep -v grep)" ] 
 	then check=17 
 	strVal="$strVal|$check" 
fi 
 
if [ ! -z "$(ps -Ae | grep 'Mac OS X Mail' | grep -v grep)" ] 
 	then check=18 
	strVal="$strVal|$check" 
 
fi 
 
 if [ ! -z "$(ps -Ae | grep 'Opera' | grep -v grep)" ] 
 then check=19 
strVal="$strVal|$check" 
fi 
  
 if [ ! -z "$(ps -Ae | grep 'Mulbery' | grep -v grep)" ] 
 	then check=20 
	strVal="$strVal|$check" 
fi 
 
#P2P 
  
 if [ ! -z "$(ps -Ae | grep 'iGotcha' | grep -v grep)" ] 
 	then check=21 
	strVal="$strVal|$check" 
fi 
  
 if [ ! -z "$(ps -Ae | grep 'MLDonkey' | grep -v grep)" ] 
 	then check=22 
	strVal="$strVal|$check" 
fi 
  
 if [ ! -z "$(ps -Ae | grep 'aMule' | grep -v grep)" ] 
 	then check=23 
	strVal="$strVal|$check" 
fi 
 
 if [ ! -z "$(ps -Ae | grep 'Phex' | grep -v grep)" ] 
 	then check=24 
	strVal="$strVal|$check" 
fi 
 
 if [ ! -z "$(ps -Ae | grep 'Torrentfileshar' | grep -v grep)" ] 
 	then check=25 
	strVal="$strVal|$check" 
fi 
#MULTIMEDIA 
 
 if [ ! -z "$(ps -Ae | grep 'Apple iLife' | grep -v grep )" ] 
 	then check=26 
	strVal="$strVal|$check" 
fi 
 
 if [ ! -z "$(ps -Ae | grep 'Anmie Sudio' | grep -v grep)" ] 
 	then check=27 
	strVal="$strVal|$check" 
fi 
 
 if [ ! -z "$(ps -Ae | grep 'Div Xpro' | grep -v grep)" ] 
 	then check=28 
	strVal="$strVal|$check" 
fi 
 
 if [ ! -z "$(ps -Ae | grep 'Quick time 7 pro' | grep -v grep)" ] 
 	then check=29 
	strVal="$strVal|$check" 
fi 
 
 if [ ! -z "$(ps -Ae | grep 'Roxio Toast 10 Titanium' | grep -v grep)" ] 
 	then check=30 
	strVal="$strVal|$check" 
fi 
 
#BLOG/TWITTER TOOLS 
 
 if [ ! -z "$(ps -Ae | grep 'Flock' | grep -v grep)" ] 
 	then check=31 
	strVal="$strVal|$check" 
fi 
 
 
 if [ ! -z "$(ps -Ae | grep 'MarsEdit' | grep -v grep)" ] 
 	then check=32 
	strVal="$strVal|$check" 
 
fi 
 
 if [ ! -z "$(ps -Ae | grep 'Ecto' | grep -v grep)" ] 
 	then check=33 
	strVal="$strVal|$check" 
fi 
 
 if [ ! -z "$(ps -Ae | grep 'Blogo' | grep -v grep)" ] 
 	then check=34 
	strVal="$strVal|$check" 
fi 
 
 if [ ! -z "$(ps -Ae | grep 'Tumblr Dashboard Widget' | grep -v grep)" ] 
 	then check=35 
	strVal="$strVal|$check" 
 fi 
 
#OTHERS 
 
 #if [ ! -z "$(ps -Ae | grep 'bash' | grep -v grep)" ] 
 #	then check=37 
 #	strVal="$strVal|$check" 
 #fi 
 
 if [ ! -z "$(ps -Ae | grep 'TextEdit' | grep -v grep)" ] 
 	then check=38 
	strVal="$strVal|$check" 
 fi 
 
 if [ ! -z "$(ps -Ae | grep 'Yahoo' | grep -v grep)" ] 
 	then check=39 
	strVal="$strVal|$check" 
 fi 
 
 if [ ! -z "$(ps -Ae | grep 'Microsoft Messenger' | grep -v grep)" ] 
 	then check=40 
	strVal="$strVal|$check" 
 fi 
 
 if [ ! -z "$(ps -Ae | grep 'Console' | grep -v grep)" ] 
 	then check=41 
	strVal="$strVal|$check" 
 fi 
 
 if [ ! -z "$(ps -Ae | grep 'Microsoft Word' | grep -v grep)" ]
 	then check=42
	strVal="$strVal|$check"
 fi

 if [ ! -z "$(ps -Ae | grep 'Microsoft Excel' | grep -v grep)" ]
 	then check=43
	strVal="$strVal|$check"
 fi
 
echo $strVal > temp_forbidden 
exit $check
