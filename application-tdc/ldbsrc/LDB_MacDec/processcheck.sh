#!/bin/bash

ps -aef > processcheck
check=0

#MAC
#BROWSERS

 if [ ! -z "$(cat processcheck | grep 'OmniWeb' | grep -v grep)" ]
 then check=1
 
 #elif [ ! -z "$(cat processcheck | grep 'Shirra' | grep -v grep)" ]
 #then check=2

 elif [ ! -z "$(cat processcheck | grep 'Camino' | grep -v grep)" ]
 then check=3

 elif [ ! -z "$(cat processcheck | grep 'Stainless' | grep -v grep)" ]
 then check=4

 #elif [ ! -z "$(cat processcheck | grep 'Safari' | grep -v grep)" ]
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

 #LINUX
 #SCREENCAPTURE
 elif [ ! -z "$(cat processcheck | awk '/gnome-panel-screenshot/')" ]
   then check=1
   zenity --error --text "GNOME-PANEL-SCREENSHOT RUNNING !"


  elif [ ! -z "$(cat processcheck | awk '/ksnapshot/')" ]
   then check=1
   zenity --error --text "KSANPSHOT RUNNING !"


    elif [ ! -z "$(cat processcheck | awk '/klipper/')" ]
     then check=1
     zenity --error --text "KLIPPER RUNNING !"


      elif [ ! -z "$(cat processcheck | awk '/shutter/')" ]
       then check=1
       zenity --error --text "SHUTTER RUNNING !"


        elif [ ! -z "$(cat processcheck | awk '/istanbul/')" ]
         then check=1
	 zenity --error --text "ISTANBUL RUNNING !"

          elif [ ! -z "$(cat processcheck | awk '/hypercam/')" ]
           then check=1
	   zenity --error --text "HYPERCAM RUNNING !"


            elif [ ! -z "$(cat processcheck | awk '/xvidcap/')" ]
            then check=1
	    zenity --error --text "XVIDCAP RUNNING !"


             elif [ ! -z "$(cat processcheck | awk '/recordmydesktop/')" ]
              then check=1
              zenity --error --text "RECORD_MY_DESKTOP RUNNING !"


                elif [ ! -z "$(cat processcheck | awk '/cankiri/')" ]
                 then check=1
		 zenity --error --text "CANKIRI RUNNING !"


                  elif [ ! -z "$(cat processcheck | awk '/imagemagik/')" ]
		   then check=1
	           zenity --error --text "IMAGE_MAGIK RUNNING !"

		    elif [ ! -z "$(cat processcheck | awk '/wink/')" ]
		     then check=1
		     zenity --error --text "WINK RUNNING !"


	              elif [ ! -z "$(cat processcheck | awk '/vnc2swf/')" ]
		       then check=1
		       zenity --error --text "VNC2SWF RUNNING !"


#BROWSERS

elif [ ! -z "$(cat processcheck | awk '/amaya/')" ]
then check=1
zenity --error --text "AMAYA RUNNING !"


 elif [ ! -z "$(cat processcheck | awk '/emacs/')" ]
  then check=1
  zenity --error --text "EMACS RUNNING !"

   elif [ ! -z "$(cat processcheck | awk '/epiphany/')" ]
    then check=1
    zenity --error --text "EPIPHANY RUNNING !"


     elif [ ! -z "$(cat processcheck | awk '/firefox/')" ]
      then check=1
      zenity --error --text "FIREFOX RUNNING !"


	elif [ ! -z "$(cat processcheck | awk '/galeon/')" ]
	 then check=1
	 zenity --error --text "GALEON RUNNING !"


	  elif [ ! -z "$(cat processcheck | awk '/hotjava/')" ]
	   then check=1
	   zenity --error --text "HOT_JAVA RUNNING !"


            elif [ ! -z "$(cat processcheck | awk '/konqueror/')" ]
	     then check=1
	     zenity --error --text "KONQUEROR RUNNING !"


	      elif [ ! -z "$(cat processcheck | awk '/lynx/')" ]
	       then check=1
	       zenity --error --text "LYNX RUNNING !"


		elif [ ! -z "$(cat processcheck | awk '/mozilla-bin/')" ]
		 then check=1
		 zenity --error --text "MOZILLA RUNNING !"


		   elif [ ! -z "$(cat processcheck | awk '/netscape/')" ]
		    then check=1
		    zenity --error --text "NETSCAPE RUNNING !"


		     elif [ ! -z "$(cat processcheck | awk '/webcon/')" ]
		      then check=1
		      zenity --error --text "WEBCON RUNNING !"


			elif [ ! -z "$(cat processcheck | awk '/opera/')" ]
			 then check=1
			 zenity --error --text "OPERA RUNNING !"


			   elif [ ! -z "$(cat processcheck | awk '/flock/')" ]
			    then check=1
			    zenity --error --text "FLOCK RUNNING !"


			      elif [ ! -z "$(cat processcheck | awk '/swiftfox/')" ]
			       then check=1
			       zenity --error --text "SWIFT_FOX RUNNING !"


				 elif [ ! -z "$(cat processcheck | awk '/swiftweasel/')" ]
				  then check=1
				  zenity --error --text "SWIFT_WEASEL RUNNING !"


				    elif [ ! -z "$(cat processcheck | awk '/seamonkey/')" ]
                                     then check=1
				     zenity --error --text "SEAMONKEY RUNNING !"


					elif [ ! -z "$(cat processcheck | awk '/midori/')" ]
					 then check=1
					 zenity --error --text "MODORI RUNNING !"


					 elif [ ! -z "$(cat processcheck | awk '/kazehakase/')" ]
					  then check=1
					  zenity --error --text "KAZEHAKASE RUNNING !"


					    elif [ ! -z "$(cat processcheck | awk '/arora/')" ]
					     then check=1
					     zenity --error --text "ARORA RUNNING !"



#IM

elif [ ! -z "$(cat processcheck | awk '/gajim/')" ]
then check=1
zenity --error --text "GAJIM RUNNING !"

   elif [ ! -z "$(cat processcheck | awk '/pidgin/')" ]
    then check=1
    zenity --error --text "PIDGIN RUNNING !"


     elif [ ! -z "$(cat processcheck | awk '/kopete/')" ]
      then check=1
      zenity --error --text "KOPETE RUNNING !"


	elif [ ! -z "$(cat processcheck | awk '/gabber/')" ]
	 then check=1
         zenity --error --text "GABBER RUNNING !"


	  elif [ ! -z "$(cat processcheck | awk '/psi/')" ]
	   then check=1
           zenity --error --text "PSI RUNNING !"


	     elif [ ! -z "$(cat processcheck | awk '/jabbim/')" ]
	     then check=1
	     zenity --error --text "JABBIM RUNNING !"


	       elif [ ! -z "$(cat processcheck | awk '/kmess/')" ]
		then check=1
		zenity --error --text "KMESS RUNNING !"


		 elif [ ! -z "$(cat processcheck | awk '/mercurymessenger/')" ]
		  then check=1
		  zenity --error --text "MERCURY_MESSENGER RUNNING !"


		   elif [ ! -z "$(cat processcheck | awk '/amsn/')" ]
		     then check=1
		     zenity --error --text "aMSN RUNNING !"


			elif [ ! -z "$(cat processcheck | awk '/bitlbee/')" ]
			 then check=1
			 zenity --error --text "BitLBee RUNNING !"


			  elif [ ! -z "$(cat processcheck | awk '/emesene/')" ]
			   then check=1
			   zenity --error --text "EMESENE RUNNING !"


			     elif [ ! -z "$(cat processcheck | awk '/gyacheimproved/')" ]
			      then check=1
			      zenity --error --text "GYACHE_IMPROVED RUNNING !"



#EMAIL-CLIENTS

elif [ ! -z "$(cat processcheck | awk '/thunderbird/')" ]
then check=1
zenity --error --text "MOZILLA_THUNDERBIRD RUNNING !"


  elif [ ! -z "$(cat processcheck | awk '/evolution-exchange-storage/')" ]
   then check=1
   zenity --error --text "EVOLUTION RUNNING !"


     elif [ ! -z "$(cat processcheck | awk '/kmail/')" ]
     then check=1
     zenity --error --text "KMAIL RUNNING !"


       elif [ ! -z "$(cat processcheck | awk '/alpine/')" ]
        then check=1
        zenity --error --text "ALPINE RUNNING !"


	  elif [ ! -z "$(cat processcheck | awk '/mutt/')" ]
	   then check=1
	   zenity --error --text "MUTT RUNNING !"


	     elif [ ! -z "$(cat processcheck | awk '/balsa/')" ]
	      then check=1
              zenity --error --text "BALSA RUNNING !"


		elif [ ! -z "$(cat processcheck | awk '/clawsmail/')" ]
		 then check=1
		 zenity --error --text "CLAWS_MAIL RUNNING !"


		   elif [ ! -z "$(cat processcheck | awk '/gnus/')" ]
		    then check=1
		    zenity --error --text "GNUS RUNNING !"


		      elif [ ! -z "$(cat processcheck | awk '/sylpheed/')" ]
		       then check=1
 		       zenity --error --text "SYLPHEED RUNNING !"



#P2P

elif [ ! -z "$(cat processcheck | awk '/edonkey2000/')" ]
then check=1
zenity --error --text "eDONKEY2000 RUNNING !"


   elif [ ! -z "$(cat processcheck | awk '/kazaa/')" ]
    then check=1
    zenity --error --text "KAZAA RUNNING !"


      elif [ ! -z "$(cat processcheck | awk '/gnutella/')" ]
       then check=1
       zenity --error --text "GNUTELLA RUNNING !"
       

	  elif [ ! -z "$(cat processcheck | awk '/qtella/')" ]
	   then check=1
	   zenity --error --text "QTELLA RUNNING !"



#MULTIMEDIA

elif [ ! -z "$(cat processcheck | awk '/helix/')" ]
then check=1
zenity --error --text "HELIX RUNNING !"


   elif [ ! -z "$(cat processcheck | awk '/kdemediaplayer/')" ]
    then check=1
    zenity --error --text "KDE MEDIA PLAYER RUNNING !"


      elif [ ! -z "$(cat processcheck | awk '/pytube/')" ]
       then check=1
       zenity --error --text "PYTUBE RUNNING !"


	 elif [ ! -z "$(cat processcheck | awk '/utuberipper/')" ]
	  then check=1
	  zenity --error --text "UTUBE RIPPER RUNNING !"


	    elif [ ! -z "$(cat processcheck | awk '/gnetvideoplayer/')" ]
	     then check=1
             zenity --error --text "GNET VIDEO PLAYER RUNNING !"



#BLOG/TWITTER TOOLS

elif [ ! -z "$(cat processcheck | awk '/gnomeblogentryposter/')" ]
then check=1
zenity --error --text "GNOME BLOG ENTRT POSTER RUNNING !"


  elif [ ! -z "$(cat processcheck | awk '/driveljournaleditor/')" ]
   then check=1
   zenity --error --text "DRIVEL JOURNAL EDITOR RUNNING !"


     elif [ ! -z "$(cat processcheck | awk '/blogtkblogeditor/')" ]
      then check=1
      zenity --error --text "BLOGTK BLOG EDITOR RUNNING !"


	elif [ ! -z "$(cat processcheck | awk '/scribefirefirefoxextension/')" ]
	 then check=1
	 zenity --error --text "SCRIBEFIRE FIREFOX RUNNING !"


	  elif [ ! -z "$(cat processcheck | awk '/googledocs/')" ]
	  then check=1
           zenity --error --text "GOOGLE_DOCS RUNNING !"

 fi

exit $check

