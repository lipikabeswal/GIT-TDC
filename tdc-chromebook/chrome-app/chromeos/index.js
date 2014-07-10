/*
 * Please refer to the README.TXT file in the parent folder for an overview
 * of the application's files.
 */

// Global namespace for TDC
var tdc = {};

function $( id ) {
  return document.getElementById( id );
}

function log( text ) {
  $( 'log' ).value += text + '\n';
}

// The basePort is the port used when the first application window is
// launched. On subsequent launches the port number will be increased by
// 1, therefore the 2nd window will use port basePort + 1 for the HTTP
// server. The values are stored inside
tdc.http = {
  basePort: 10000,  // The value of the getTimesLoaded() function call
                    // in background.js will be added to the basePort value;
  webviewHTMLFile: 'login.html'
}

var webviewMan = null;


// Code which needs to be executed for the DOMContentLoaded event of the
// index.html goes here.
document.addEventListener( 'DOMContentLoaded', function() {
  console.log( "DOMContentLoaded event" );
} );

/**
 * Lauches HTTP server with given port.
 * @param port
 */
function launchWebServer( port ) {
  if ( http.Server ) {
    // Listen for HTTP connections.
    var server = tdc.server = new http.Server();
    server.listen( port );
    server.addEventListener( 'request', function( req ) {
      var url = req.headers.url;
      if ( url == '/' )
        url = '/www/' + tdc.http.webviewHTMLFile;
      // Serve the pages of this chrome application.
      req.serveUrl( url );
      return true;
    } );

  }

}

/**
 * When the window.load event is fired, the following steps
 * have to executed:
 *   a) Using the background.js getTimesLoaded() function
 *      we know how many windows have been launched, and
 *      which port has to be used when launching the HTTP
 *      server.
 *   b) The login.html has to be loaded into the webview tag.
 *   c) ContentAction class is used start the login and content
 *      loading.
 */
window.addEventListener( "load", function( e ) {
  console.log( "window.load event" );
  chrome.runtime.getBackgroundPage( function( w ) {
    var timesLoaded = w.getTimesLoaded();
    var port = tdc.http.basePort + timesLoaded;
    console.log( "Application has been loaded %s times.", timesLoaded );
    launchWebServer( port );
    webviewMan = new (WebviewManager);
    webviewMan.initialize( 'tdcWebview' );
    var url = 'http://localhost:' + port + "/www/" + tdc.http.webviewHTMLFile;
    webviewMan.loadPage( url );
    zip.workerScriptsPath = "/chromeos/util/";
    zip.useWebWorkers = false;
    console.log("window.load event");
    tdc.contentAction = new ContentAction();    
  } );
} );