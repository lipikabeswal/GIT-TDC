#!/bin/bash

ps -aef > processcheck
check=0

#BROWSERS

 if [ ! -z "$(cat processcheck | grep 'OmniWeb' | grep -v grep)" ]
 then check=1
 
 elif [ ! -z "$(cat processcheck | grep 'Shirra' | grep -v grep)" ]
 then check=2

 elif [ ! -z "$(cat processcheck | grep 'Camino' | grep -v grep)" ]
 then check=3

 elif [ ! -z "$(cat processcheck | grep 'Stainless' | grep -v grep)" ]
 then check=4

# elif [ ! -z "$(cat processcheck | grep 'Safari' | grep -v grep)" ]
 #then check=5

#SCREENCAPTURE

 elif [ ! -z "$(cat processcheck | grep 'Capture Me' | grep -v grep)" ]
 then check=6

 elif [ ! -z "$(cat processcheck | grep 'Constrictor' | grep -v grep)" ]
 then check=7

 elif [ ! -z "$(cat processcheck | grep 'Copernius' | grep -v grep)" ] 
 then check=8

 elif [ ! -z "$(cat processcheck | grep 'Flash-It' | grep -v grep)" ]
 then check=9

 elif [ ! -z "$(cat processcheck | grep 'Fly Sketch' | grep -v grep)" ]
 then check=10

#IM

 elif [ ! -z "$(cat processcheck | grep 'Adium' | grep -v grep)" ]
 then check=11
 
 elif [ ! -z "$(cat processcheck | grep 'Skype' | grep -v grep)" ]
 then check=12
 
 elif [ ! -z "$(cat processcheck | grep 'Pidgin' | grep -v grep)" ]
 then check=13
 
 elif [ ! -z "$(cat processcheck | grep 'Google Talk' | grep -v grep)" ]
 then check=14

 elif [ ! -z "$(cat processcheck | grep 'Trillian' | grep -v grep)" ]
 then check=15

#EMAIL-CLIENTS

 elif [ ! -z "$(cat processcheck | grep 'Mozilla Thunderbird' | grep -v grep)" ]
 then check=16

 elif [ ! -z "$(cat processcheck | grep 'Eudora' | grep -v grep)" ]
 then check=17
 
elif [ ! -z "$(cat processcheck | grep 'Mac OS X Mail' | grep -v grep)" ]
 then check=18

 elif [ ! -z "$(cat processcheck | grep 'Opera' | grep -v grep)" ]
 then check=19
 
 elif [ ! -z "$(cat processcheck | grep 'Mulbery' | grep -v grep)" ]
 then check=20

#P2P
 
 elif [ ! -z "$(cat processcheck | grep 'iGotcha' | grep -v grep)" ]
 then check=21
 
 elif [ ! -z "$(cat processcheck | grep 'MLDonkey' | grep -v grep)" ]
 then check=22
 
 elif [ ! -z "$(cat processcheck | grep 'aMule' | grep -v grep)" ]
 then check=23

 elif [ ! -z "$(cat processcheck | grep 'Phex' | grep -v grep)" ]
 then check=24

 elif [ ! -z "$(cat processcheck | grep 'Torrentfileshar' | grep -v grep)" ]
 then check=25

#MULTIMEDIA

 elif [ ! -z "$(cat processcheck | grep 'Apple iLife' | grep -v grep )" ]
 then check=26

 elif [ ! -z "$(cat processcheck | grep 'Anmie Sudio' | grep -v grep)" ]
 then check=27

 elif [ ! -z "$(cat processcheck | grep 'Div Xpro' | grep -v grep)" ]
 then check=28

 elif [ ! -z "$(cat processcheck | grep 'Quick time 7 pro' | grep -v grep)" ]
 then check=29

 elif [ ! -z "$(cat processcheck | grep 'Roxio Toast 10 Titanium' | grep -v grep)" ]
 then check=30

#BLOG/TWITTER TOOLS

 elif [ ! -z "$(cat processcheck | grep 'Flock' | grep -v grep)" ]
 then check=31

 elif [ ! -z "$(cat processcheck | grep 'MarsEdit' | grep -v grep)" ]
 then check=32

 elif [ ! -z "$(cat processcheck | grep 'Ecto' | grep -v grep)" ]
 then check=33

 elif [ ! -z "$(cat processcheck | grep 'Blogo' | grep -v grep)" ]
 then check=34

 elif [ ! -z "$(cat processcheck | grep 'Tumblr Dashboard Widget' | grep -v grep)" ]
 then check=35

 fi

exit $check
