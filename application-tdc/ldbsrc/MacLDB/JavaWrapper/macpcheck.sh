#!/bin/bash

#ps -aef > processcheck
check=0
strVal=""

#BROWSERS

 if [ ! -z "$(ps -ef | grep 'OmniWeb' | grep -v grep)" ]
 	then check=1
	strVal="$strVal|$check"

 fi

 if [ ! -z "$(ps -ef | grep 'Shirra' | grep -v grep)" ]
 	then check=2
	strVal="$strVal|$check"
fi

if [ ! -z "$(ps -ef | grep 'Camino' | grep -v grep)" ]
 	then check=3
	strVal="$strVal|$check"
	
fi

if [ ! -z "$(ps -ef | grep 'Stainless' | grep -v grep)" ]
 	then check=4
	strVal="$strVal|$check"
fi

if [ ! -z "$(ps -ef | grep 'Safari' | grep -v grep)" ]
 	then check=5
	strVal="$strVal|$check"
	
fi

if [ ! -z "$(ps -ef | grep 'Firefox' | grep -v grep)" ]
 	then check=36
	strVal="$strVal|$check"
fi

#SCREENCAPTURE

 if [ ! -z "$(ps -ef | grep 'Capture Me' | grep -v grep)" ]
 	then check=6
	strVal="$strVal|$check"
fi

 if [ ! -z "$(ps -ef | grep 'Constrictor' | grep -v grep)" ]
 	then check=7
	strVal="$strVal|$check"
fi

 if [ ! -z "$(ps -ef | grep 'Copernius' | grep -v grep)" ] 
 	then check=8
	strVal="$strVal|$check"
fi

 if [ ! -z "$(ps -ef | grep 'Flash-It' | grep -v grep)" ]
 	then check=9
	strVal="$strVal|$check"
fi

if [ ! -z "$(ps -ef | grep 'Fly Sketch' | grep -v grep)" ]
 	then check=10
	strVal="$strVal|$check"

fi
#IM

 if [ ! -z "$(ps -ef | grep 'Adium' | grep -v grep)" ]
 	then check=11
	strVal="$strVal|$check"
fi
 
 if [ ! -z "$(ps -ef | grep 'Skype' | grep -v grep)" ]
 	then check=12
	strVal="$strVal|$check" 
fi

 if [ ! -z "$(ps -ef | grep 'Pidgin' | grep -v grep)" ]
 	then check=13
	strVal="$strVal|$check" 
fi

if [ ! -z "$(ps -ef| grep 'Google Talk' | grep -v grep)" ]
 	then check=14
	strVal="$strVal|$check"
fi


if [ ! -z "$(ps -ef | grep 'Trillian' | grep -v grep)" ]
 	then check=15
	strVal="$strVal|$check"

fi

#EMAIL-CLIENTS

 if [ ! -z "$(ps -ef | grep 'Mozilla Thunderbird' | grep -v grep)" ]
 	then check=16
	strVal="$strVal|$check"
fi

 if [ ! -z "$(ps -ef | grep 'Eudora' | grep -v grep)" ]
 	then check=17
 	strVal="$strVal|$check"
fi

if [ ! -z "$(ps -ef | grep 'Mac OS X Mail' | grep -v grep)" ]
 	then check=18
	strVal="$strVal|$check"

fi

 if [ ! -z "$(ps -ef | grep 'Opera' | grep -v grep)" ]
 then check=19
strVal="$strVal|$check"
fi
 
 if [ ! -z "$(ps -ef | grep 'Mulbery' | grep -v grep)" ]
 	then check=20
	strVal="$strVal|$check"
fi

#P2P
 
 if [ ! -z "$(ps -ef | grep 'iGotcha' | grep -v grep)" ]
 	then check=21
	strVal="$strVal|$check"
fi
 
 if [ ! -z "$(ps -ef | grep 'MLDonkey' | grep -v grep)" ]
 	then check=22
	strVal="$strVal|$check"
fi
 
 if [ ! -z "$(ps -ef | grep 'aMule' | grep -v grep)" ]
 	then check=23
	strVal="$strVal|$check"
fi

 if [ ! -z "$(ps -ef | grep 'Phex' | grep -v grep)" ]
 	then check=24
	strVal="$strVal|$check"
fi

 if [ ! -z "$(ps -ef | grep 'Torrentfileshar' | grep -v grep)" ]
 	then check=25
	strVal="$strVal|$check"
fi
#MULTIMEDIA

 if [ ! -z "$(ps -ef | grep 'Apple iLife' | grep -v grep )" ]
 	then check=26
	strVal="$strVal|$check"
fi

 if [ ! -z "$(ps -ef | grep 'Anmie Sudio' | grep -v grep)" ]
 	then check=27
	strVal="$strVal|$check"
fi

 if [ ! -z "$(ps -ef | grep 'Div Xpro' | grep -v grep)" ]
 	then check=28
	strVal="$strVal|$check"
fi

 if [ ! -z "$(ps -ef | grep 'Quick time 7 pro' | grep -v grep)" ]
 	then check=29
	strVal="$strVal|$check"
fi

 if [ ! -z "$(ps -ef | grep 'Roxio Toast 10 Titanium' | grep -v grep)" ]
 	then check=30
	strVal="$strVal|$check"
fi

#BLOG/TWITTER TOOLS

 if [ ! -z "$(ps -ef | grep 'Flock' | grep -v grep)" ]
 	then check=31
	strVal="$strVal|$check"
fi


 if [ ! -z "$(ps -ef | grep 'MarsEdit' | grep -v grep)" ]
 	then check=32
	strVal="$strVal|$check"

fi

 if [ ! -z "$(ps -ef | grep 'Ecto' | grep -v grep)" ]
 	then check=33
	strVal="$strVal|$check"
fi

 if [ ! -z "$(ps -ef | grep 'Blogo' | grep -v grep)" ]
 	then check=34
	strVal="$strVal|$check"
fi

 if [ ! -z "$(ps -ef | grep 'Tumblr Dashboard Widget' | grep -v grep)" ]
 	then check=35
	strVal="$strVal|$check"
 fi

echo $strVal > temp_forbidden
exit $check
