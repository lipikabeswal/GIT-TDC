Chromium Embedded Framework (CEF) Standard Binary Distribution for Mac OS-X
-------------------------------------------------------------------------------

Date:             May 08, 2013

CEF Version:      3.1453.1255
CEF URL:          https://chromiumembedded.googlecode.com/svn/branches/1453/cef3@1255

Chromium Verison: 27.0.1453.73
Chromium URL:     http://src.chromium.org/svn/branches/1453/src@197225

This distribution contains all components necessary to build and distribute an
application using CEF on the Mac OS-X platform. Please see the LICENSING
section of this document for licensing terms and conditions.


CONTENTS
--------

cefclient   Contains the cefclient sample application configured to build
            using the files in this distribution.

Debug       Contains libcef.dylib and other components required to run the debug
            version of CEF-based applications.

include     Contains all required CEF header files.

libcef_dll  Contains the source code for the libcef_dll_wrapper static library
            that all applications using the CEF C++ API must link against.

Release     Contains libcef.dylib and other components required to run the
            release version of CEF-based applications.

Resources   Contains images and resources required by applications using CEF.
            The contents of this folder should be transferred to the
            Contents/Resources folder in the app bundle.

tools       Scripts that perform post-processing on Mac release targets.


USAGE
-----

Xcode 3 and 4: Open the cefclient.xcodeproj project and build.

When using Xcode 4.2 or newer you will need to change the "Compiler for
C/C++/Objective-C" setting to "LLVM GCC 4.2" under "Build Settings" for
each target.

Please visit the CEF Website for additional usage information.

http://code.google.com/p/chromiumembedded


REDISTRIBUTION
--------------

This binary distribution contains the below components. Components listed under
the "required" section must be redistributed with all applications using CEF.
Components listed under the "optional" section may be excluded if the related
features will not be used.

Required components:

* CEF core library
    libcef.dylib

* Cursor resources
    Resources/*.png
    Resources/*.tiff

Optional components:

* Localized resources
    Resources/*.lproj/
  Note: Contains localized strings for WebKit UI controls. A .pak file is loaded
  from this folder based on the CefSettings.locale value. Only configured
  locales need to be distributed. If no locale is configured the default locale
  of "en" will be used. Locale file loading can be disabled completely using
  CefSettings.pack_loading_disabled.

* Other resources
    Resources/cef.pak
    Resources/devtools_resources.pak
  Note: Contains WebKit image and inspector resources. Pack file loading can be
  disabled completely using CefSettings.pack_loading_disabled. The resources
  directory path can be customized using CefSettings.resources_dir_path.

* FFmpeg audio and video support
    ffmpegsumo.so
  Note: Without this component HTML5 audio and video will not function.


LICENSING
---------

The CEF project is BSD licensed. Please read the LICENSE.txt file included with
this binary distribution for licensing terms and conditions. Other software
included in this distribution is provided under other licenses. Please visit
"about:credits" in a CEF-based application for complete Chromium and third-party
licensing information.
