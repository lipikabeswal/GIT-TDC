#!/bin/sh

# Requirements for building
# Download "Open Source Flex SDK" from http://www.adobe.com/devnet/flex/flex-sdk-download-all.html
#
# mkdir flex4.open.source.sdk
# cd flex4.open.source.sdk
# Copy flex_sdk_4.1.0.16076_mpl.zip to this folder - cp ~/Downloads/flex_sdk_4.1.0.16076_mpl.zip .
#
# unzip flex_sdk_4.1.0.16076_mpl.zip
# cd ../
# ln -s flex4.open.source.sdk flex
#
# Add flex/bin to your $PATH
# PATH="<home directory>/flex/bin:$PATH"
#

export PROJECT_HOME=`pwd`
export BUILD_FOLDER="$PROJECT_HOME/build"

if [ -d $BUILD_FOLDER ]; then
	cd $BUILD_FOLDER
    rm -rf $BUILD_FOLDER
fi

cd $PROJECT_HOME/src
mxmlc MicrophoneDetection.as -output $BUILD_FOLDER/MicrophoneDetection.swf

if [ -f $BUILD_FOLDER/MicrophoneDetection.swf ]; then
  echo "Copying HTML template to build folder."
  cd $PROJECT_HOME/html-template
  cp -r * $BUILD_FOLDER
  cd $BUILD_FOLDER
  rm $BUILD_FOLDER/index.template.html
fi
