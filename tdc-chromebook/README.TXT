This README describes the modification done to the TDC iOS application to run
the application as a Chrome Packaged App inside ChromeOS

App root folder
├── chromeos
│   ├── ContentAction.js        Downloading and extracting content items.
│   ├── Persistence.js          Access to file system, storing content.
│   ├── RequestHandler.js       Helper class for HTTP requests.
│   ├── WebviewManager.js       Helper class for managing webview (event tracking, message sending/receiving).
    ├── background.js           Initial code executed by a Chrome Packaged App (CPA)
│   ├── cryptojs                CryptoJS for MD5 and RC4 support, see https://code.google.com/p/crypto-js/
│   │   ├── core.js
│   │   ├── md5.js
│   │   └── rc4.js
│   ├── http.js                 Embedded web server
│   ├── index.js                CPA main JS file for index.html
│   └── util                    Utility for unzipping content, see http://gildas-lormeau.github.io/zip.js/
│       ├── inflate.js
│       ├── sha1.js
│       └── zip.js
│
├── README.TXT              This document.
├── index.html              Top HTML page for CPA; contains the webview tag and load the login.html
├── manifest.json           CPA manifest file
└── www
    ├── TestClient.lzx.js
    ├── chromeos
    │   └── webview.js      Communicates with WebviewManager
    └── login.html


Folder overview
/chromeos
This folder contains all the JavaScript files which are related to the Chrome Packaged App
and Chrome OS integration of the TDC application.

/www
All the TDC application files are hosted inside the www folder, from where they are served
using an embedded web server. The local web server had to be used since the <webview> tag
in Chrome only allows loading content using a http:// or https:// URL.

/www/chromeos
Files related to communication between webview and top level HTML page as well as ChromeOS
specific features.


The following section describes how the communication between the top level HTML page and
the HTML page loaded into the webview works:
Top HTML document <-> Webview Communication Example

Files:

index.html                      Top level HTML page
chromeos/index.js               JavaScript code executed at startup.
chromeos/WebviewManager.js      Helper class for managing the webview and handling events.
chromeos/http.js                Webserver implementation.
www/chromeos/webview.js         Code included by login.html for post message communication.
www/login.html                  TDC login.html

Please read the Webview usage documentation section for a better understanding:
https://developer.chrome.com/apps/tags/webview#usage


Application initialization up to the point where the application is ready for
communication between the index.html and the webview content:

1) index.html is loaded
2) Webserver is started automatically (index.js)
3) Top document.DOMContentLoaded event received (index.js)
4) WebviewManager initializes, registers webview.contentload event (WebviewManager.js)
5) WebviewManager receives webview.contentload event; registers webview.loadstop event (WebviewManager.js)
   See https://developer.chrome.com/apps/tags/webview#event-loadstop
6) WebviewManager receives webview.loadstop event; now register window.message event (will be sent when
   message is received from webview).  (WebviewManager.js)
7) WebviewManager sends handshake method to webview to enable the webview to store
   the app reference and origin.  (WebviewManager.js)
   See
8) Webview responds by sending handshake response. www/lps/chromeos/webview.js
9) Application is now ready to start communication with server.


JavaScript functions for sending message:
1) Sending from index.html to webview:
   webviewMan.sendMsgToWebview( "message to webview " );
2) Sending from inside webview to index.html
   sendMsgToApp( "message to to level page of Chrome app" );


Configuration options:
File                Line no.    Property            Effect
WebviewManager.js   11          logFromWebview      If true, all console messages from webview will be
                                                    displayed in index.html console with prefix 'TDC log >>> '

index.js            22          basePort            Initial HTTP server port, currently 10000. Will be increased
                                                    by 1 for each window opened.

index.js            24          webviewHTMLFile     HTML file to be loaded into webview: 'login.html'