@echo off


:: Requirements for building
:: Download "Open Source Flex SDK" from http://www.adobe.com/devnet/flex/flex-sdk-download-all.html
:: file similar to: flex_sdk_4.1.0.16076_mpl.zip
::
:: create directory on disc e.g "C:\Program Files\FlexSdk"
:: Copy and extract downloaded .zip to this location
::
:: Go to Start>My Computer (right click)>Properties>Advanced System Settings>Environment Variables
:: Edit System variable called "Path"
:: Add ";C:\Program Files\FlexSdk\bin" (without quotes) at the end of its value


SET PROJECT_HOME=%cd%
SET BUILD_FOLDER="%PROJECT_HOME%\build"

echo "Build target folder: %BUILD_FOLDER%"
rmdir /s /q build
mkdir build

cd %PROJECT_HOME%/src
mxmlc -static-link-runtime-shared-libraries MicrophoneDetection.as -output %BUILD_FOLDER%/MicrophoneDetection.swf 

xcopy /s %PROJECT_HOME%\html-template\*.* %BUILD_FOLDER%
del %BUILD_FOLDER%\index.template.html

cd %PROJECT_HOME%
