#!/bin/bash
ps -aef > processcheck
check=0
if [ -f plist ]; then
rm plist
fi

#SCREENCAPTURE
if [ ! -z "$(cat processcheck | awk '/gnome-panel-screenshot/')" ]
 then check=1
 echo "GNOME-PANEL-SCREENSHOT RUNNING !" >> plist
fi
  if [ ! -z "$(cat processcheck | awk '/ksnapshot/')" ]
   then check=1
   echo "KSANPSHOT RUNNING !" >> plist
fi	


    if [ ! -z "$(cat processcheck | awk '/klipper/')" ]
     then check=1
     echo "KLIPPER RUNNING !" >> plist
fi

      if [ ! -z "$(cat processcheck | awk '/shutter/')" ]
       then check=1
       echo "SHUTTER RUNNING !" >> plist
fi

        if [ ! -z "$(cat processcheck | awk '/istanbul/')" ]
         then check=1
	 echo "ISTANBUL RUNNING !" >> plist
fi
          if [ ! -z "$(cat processcheck | awk '/hypercam/')" ]
           then check=1
	   echo "HYPERCAM RUNNING !" >> plist
fi

            if [ ! -z "$(cat processcheck | awk '/xvidcap/')" ]
            then check=1
	    echo "XVIDCAP RUNNING !" >> plist
fi

             if [ ! -z "$(cat processcheck | awk '/recordmydesktop/')" ]
              then check=1
              echo "RECORD_MY_DESKTOP RUNNING !" >> plist

fi
                if [ ! -z "$(cat processcheck | awk '/cankiri/')" ]
                 then check=1
		 echo "CANKIRI RUNNING !" >> plist
fi

                  if [ ! -z "$(cat processcheck | awk '/imagemagik/')" ]
		   then check=1
	           echo "IMAGE_MAGIK RUNNING !" >> plist
fi
		    if [ ! -z "$(cat processcheck | awk '/wink/')" ]
		     then check=1
		     echo "WINK RUNNING !" >> plist
fi

	              if [ ! -z "$(cat processcheck | awk '/vnc2swf/')" ]
		       then check=1
		       echo "VNC2SWF RUNNING !" >> plist
fi

#BROWSERS

if [ ! -z "$(cat processcheck | awk '/amaya/')" ]
then check=1
echo "AMAYA RUNNING !" >> plist
fi

 if [ ! -z "$(cat processcheck | awk '/emacs/')" ]
  then check=1
  echo "EMACS RUNNING !" >> plist
fi
   if [ ! -z "$(cat processcheck | awk '/epiphany/')" ]
    then check=1
    echo "EPIPHANY RUNNING !" >> plist
fi

     if [ ! -z "$(cat processcheck | awk '/firefox/')" ]
      then check=1
      echo "FIREFOX RUNNING !" >> plist
fi

	if [ ! -z "$(cat processcheck | awk '/galeon/')" ]
	 then check=1
	 echo "GALEON RUNNING !" >> plist
fi

	  if [ ! -z "$(cat processcheck | awk '/hotjava/')" ]
	   then check=1
	   echo "HOT_JAVA RUNNING !" >> plist
fi

            if [ ! -z "$(cat processcheck | awk '/konqueror/')" ]
	     then check=1
	     echo "KONQUEROR RUNNING !" >> plist
fi

	      if [ ! -z "$(cat processcheck | awk '/lynx/')" ]
	       then check=1
	       echo "LYNX RUNNING !" >> plist
fi

		if [ ! -z "$(cat processcheck | awk '/mozilla-bin/')" ]
		 then check=1
		 echo "MOZILLA RUNNING !" >> plist
fi

		   if [ ! -z "$(cat processcheck | awk '/netscape/')" ]
		    then check=1
		    echo "NETSCAPE RUNNING !" >> plist

fi
		     if [ ! -z "$(cat processcheck | awk '/webcon/')" ]
		      then check=1
		      echo "WEBCON RUNNING !" >> plist
fi

			if [ ! -z "$(cat processcheck | awk '/opera/')" ]
			 then check=1
			 echo "OPERA RUNNING !" >> plist
fi

			   if [ ! -z "$(cat processcheck | awk '/flock/')" ]
			    then check=1
			    echo "FLOCK RUNNING !" >> plist
fi

			      if [ ! -z "$(cat processcheck | awk '/swiftfox/')" ]
			       then check=1
			       echo "SWIFT_FOX RUNNING !" >> plist

fi
				 if [ ! -z "$(cat processcheck | awk '/swiftweasel/')" ]
				  then check=1
				  echo "SWIFT_WEASEL RUNNING !" >> plist
fi

				    if [ ! -z "$(cat processcheck | awk '/seamonkey/')" ]
                                     then check=1
				     echo "SEAMONKEY RUNNING !" >> plist
fi

					if [ ! -z "$(cat processcheck | awk '/midori/')" ]
					 then check=1
					 echo "MODORI RUNNING !" >> plist
fi

					 if [ ! -z "$(cat processcheck | awk '/kazehakase/')" ]
					  then check=1
					  echo "KAZEHAKASE RUNNING !" >> plist
fi

					    if [ ! -z "$(cat processcheck | awk '/arora/')" ]
					     then check=1
					     echo "ARORA RUNNING !" >> plist

fi

#IM

if [ ! -z "$(cat processcheck | awk '/gajim/')" ]
then check=1
echo "GAJIM RUNNING !" >> plist
fi
   if [ ! -z "$(cat processcheck | awk '/pidgin/')" ]
    then check=1
    echo "PIDGIN RUNNING !" >> plist

fi
     if [ ! -z "$(cat processcheck | awk '/kopete/')" ]
      then check=1
      echo "KOPETE RUNNING !" >> plist
fi

	if [ ! -z "$(cat processcheck | awk '/gabber/')" ]
	 then check=1
         echo "GABBER RUNNING !" >> plist
fi

	  if [ ! -z "$(cat processcheck | awk '/psi/')" ]
	   then check=1
           echo "PSI RUNNING !" >> plist

fi
	     if [ ! -z "$(cat processcheck | awk '/jabbim/')" ]
	     then check=1
	     echo "JABBIM RUNNING !" >> plist

fi
	       if [ ! -z "$(cat processcheck | awk '/kmess/')" ]
		then check=1
		echo "KMESS RUNNING !" >> plist
fi

		 if [ ! -z "$(cat processcheck | awk '/mercurymessenger/')" ]
		  then check=1
		  echo "MERCURY_MESSENGER RUNNING !" >> plist
fi

		   if [ ! -z "$(cat processcheck | awk '/amsn/')" ]
		     then check=1
		     echo "aMSN RUNNING !" >> plist
fi

			if [ ! -z "$(cat processcheck | awk '/bitlbee/')" ]
			 then check=1
			 echo "BitLBee RUNNING !" >> plist

fi
			  if [ ! -z "$(cat processcheck | awk '/emesene/')" ]
			   then check=1
			   echo "EMESENE RUNNING !" >> plist

fi
			     if [ ! -z "$(cat processcheck | awk '/gyacheimproved/')" ]
			      then check=1
			      echo "GYACHE_IMPROVED RUNNING !" >> plist
fi


#EMAIL-CLIENTS

if [ ! -z "$(cat processcheck | awk '/thunderbird/')" ]
then check=1
echo "MOZILLA_THUNDERBIRD RUNNING !" >> plist
fi   

   if [ ! -z "$(cat processcheck | awk '/ evolution/')" ]
   then check=1
   echo "EVOLUTION RUNNING !" >> plist
fi

     if [ ! -z "$(cat processcheck | awk '/kmail/')" ]
     then check=1
     echo "KMAIL RUNNING !" >> plist
fi

       if [ ! -z "$(cat processcheck | awk '/alpine/')" ]
        then check=1
        echo "ALPINE RUNNING !" >> plist

fi
	  if [ ! -z "$(cat processcheck | awk '/mutt/')" ]
	   then check=1
	   echo "MUTT RUNNING !" >> plist
fi

	     if [ ! -z "$(cat processcheck | awk '/balsa/')" ]
	      then check=1
              echo "BALSA RUNNING !" >> plist
fi

		if [ ! -z "$(cat processcheck | awk '/clawsmail/')" ]
		 then check=1
		 echo "CLAWS_MAIL RUNNING !" >> plist

fi
		   if [ ! -z "$(cat processcheck | awk '/gnus/')" ]
		    then check=1
		    echo "GNUS RUNNING !" >> plist
fi

		      if [ ! -z "$(cat processcheck | awk '/sylpheed/')" ]
		       then check=1
 		       echo "SYLPHEED RUNNING !" >> plist
fi


#P2P

if [ ! -z "$(cat processcheck | awk '/edonkey2000/')" ]
then check=1
echo "eDONKEY2000 RUNNING !" >> plist
fi

   if [ ! -z "$(cat processcheck | awk '/kazaa/')" ]
    then check=1
    echo "KAZAA RUNNING !" >> plist

fi
      if [ ! -z "$(cat processcheck | awk '/gnutella/')" ]
       then check=1
       echo "GNUTELLA RUNNING !" >> plist
fi       

	  if [ ! -z "$(cat processcheck | awk '/qtella/')" ]
	   then check=1
	   echo "QTELLA RUNNING !" >> plist
fi


#MULTIMEDIA

if [ ! -z "$(cat processcheck | awk '/helix/')" ]
then check=1
echo "HELIX RUNNING !" >> plist

fi
   if [ ! -z "$(cat processcheck | awk '/kdemediaplayer/')" ]
    then check=1
    echo "KDE MEDIA PLAYER RUNNING !" >> plist
fi

      if [ ! -z "$(cat processcheck | awk '/pytube/')" ]
       then check=1
       echo "PYTUBE RUNNING !" >> plist
fi

	 if [ ! -z "$(cat processcheck | awk '/utuberipper/')" ]
	  then check=1
	  echo "UTUBE RIPPER RUNNING !" >> plist
fi

	    if [ ! -z "$(cat processcheck | awk '/gnetvideoplayer/')" ]
	     then check=1
             echo "GNET VIDEO PLAYER RUNNING !" >> plist

fi

#BLOG/TWITTER TOOLS

if [ ! -z "$(cat processcheck | awk '/gnomeblogentryposter/')" ]
then check=1
echo "GNOME BLOG ENTRT POSTER RUNNING !" >> plist
fi

  if [ ! -z "$(cat processcheck | awk '/driveljournaleditor/')" ]
   then check=1
   echo "DRIVEL JOURNAL EDITOR RUNNING !" >> plist
fi

     if [ ! -z "$(cat processcheck | awk '/blogtkblogeditor/')" ]
      then check=1
      echo "BLOGTK BLOG EDITOR RUNNING !" >> plist
fi

	if [ ! -z "$(cat processcheck | awk '/scribefirefirefoxextension/')" ]
	 then check=1
	 echo "SCRIBEFIRE FIREFOX RUNNING !" >> plist
fi

	  if [ ! -z "$(cat processcheck | awk '/googledocs/')" ]
	  then check=1
           echo "GOOGLE_DOCS RUNNING !" >> plist

fi

if [ $check -eq 1 ]; then 
cat plist|zenity --text-info --title="ERROR"
fi

exit $check


