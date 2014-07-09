
File structure of the Flash Microphone POC

1. HTML page
The main HTML page is the file html-template/index.template.html. During the build
process in Flash Builder the file is copied over into the bin-debug folder and is
renamed to MicrophoneDetection.html. The HTML page embeds the Flash movie using the
ID "MicrophoneDetection". Communication with the ActionScript classes is done
by exposing ActionScript methods through the ExternalInterface API inside the
MicJSInterface.as class.

2. JavaScript files: Folder html-template/js
The JavaScript files are inside the  folder. MicDetector.js mirrors
the APIs on the MicJSInterface.as class and exposes the methods  to the JavaSCript
environment. The microphone_event.js is called from the MicJSInterface.as class.

3. ActionScript files: Folder src
There are three ActionScript files. MicrophoneDetection.as, the main sprite class.
The other files are MicJSInterface.as for the JavaScript-Flash communication, and
the MicrophoneTracker.as file for the Microphone functionality.


Communication flow within application
Example: Call into Flash to start the tracking of the microphone

HTML            MicDetector.js, microphone_events.js, SWF embed code

JavaScript      a) Send commands to Flash using MicDetector, e.g. MicDetector.startTracking()
                b) MicDetector then calls ActionScript methods exposed through the
                   ExternalInterface API in MicJSInterface.as class.

FLash           c) MicJSInterface.startTracking() is called. Event handlers are added
                   for microphone activity in MicrophoneTracker.as class; a timer is used
                   to track the maximum activity level of the microphone.
                d) Callbacks into JavaScript are made using the ExternalInterface.call() method.

JavaScript      e) The microphone_events() function microphone_event.js processes the calls
                   coming from Flash.
                   
                   
Building the project:
The build can be executed through FlashBuilder, or through the shell/batch script in the project
top level folder.

MAC/Linux:
Make the build.sh file executable and run it. The application with all necessary files will be
in folder "build".