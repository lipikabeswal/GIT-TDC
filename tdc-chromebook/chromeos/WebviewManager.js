/*
 * Code for integration between index.html and login.html running inside the webview.
 */

function WebviewManager() {
    console.log( "Constructor..." );
    this.loadFinished = false;
    this.isLoading = false;
    this.hasCrashed = false;
    this.view = null;
    this.logFromWebview = true;
    this.requestHandler = new RequestHandler();
    // Set to true to forward the TDC application console messages to the
    // top page (index.html) console. Useful for debugging.
}

/**
 * Registers events and prepares webview
 */
WebviewManager.prototype.initialize = function ( viewId ) {
    var that = this;
    console.log( "WebviewManager.initialize()" );
    this.view = document.getElementById( viewId );
    if ( !this.view ) {
        console.warn( "Could not initialize webview with id #" + viewId );
        return null;
    }
    // Handle crashs of the webview
    this.view.addEventListener( 'exit', function ( e ) {
        if ( e.reason === 'crash' ) {
            webview.src = 'data:text/plain, WEBVIEW CRASHED!!';
        }
    } );

    // Register event listeners and initialize communication once
    // the TDC client has been loaded into the webview.
    this.view.addEventListener( 'contentload', function(e) { that.contentLoaded.apply( that, [e] ); });

    // Forward console messages inside the webview to the top level console
    // Warning and erros will always be displayed.
    this.view.addEventListener( 'consolemessage',  function(e) { that.logger.apply( that, [e] ); });

    return this;
}

WebviewManager.prototype.logger = function(e) {
    if ( parseInt( e.level ) == 0 && this.logFromWebview == true ) {
        console.log( 'TDC log >>> ' + e.message );
    } else if ( e.level == 1 ) {
        // Warnings
        console.warn( "TDC log >>> " + e.message );
    } else if ( e.level == 2 ) {
        // Warnings
        console.error( "TDC log >>> " + e.message );
    }
}

WebviewManager.prototype.contentLoaded = function( e ) {
    var that = this; //that now refers to WebviewManager's this reference

    this.view.addEventListener( 'loadstop', function () {
        console.log("WebviewManager.contentLoaded: webview.loadstop event");
        // Send a post message to the webview.contentWindow to initialize
        // communication.

        window.addEventListener( "message", function ( event ) {
            console.log( 'WebviewManager.contentLoaded: index.html received message:', event.data )
            that.requestHandler.handleRequest(event.data.method,event.data.xml);

            /*if(event.data.method=='login'){
                PersistenceAction.prototype.login(event.data.xml);
            }
            if(event.data.method=='save'){
                PersistenceAction.prototype.save(event.data.xml);
            }
            if(event.data.method=='getSubtest'){
                getSubtest(event.data.xml);
            }*/
        } );
        that.view.contentWindow.postMessage( "handshake_started", '*' );
        // Now we can start the login request and access the server from out of the webview.
        console.log("WebviewManager.contentLoaded: Posted messaged 'handshake_started' to webview");
    } );

	 /*this.view.addEventListener( 'message', function () {
	    console.log("WebviewManager.contentLoaded: webview.message event");
	 } );
	  this.view.addEventListener( 'DOMContentLoaded', function () {
	    console.log("WebviewManager.contentLoaded: webview.DOMContentLoaded event");
	 } );

	  this.view.addEventListener( 'loadcommit', function () {
	    console.log("WebviewManager.contentLoaded: webview.loadcommit event");
	 } );
	 this.view.addEventListener( 'loadredirect', function () {
	    console.log("WebviewManager.contentLoaded: webview.loadredirect event");
	 } );
	 this.view.addEventListener( 'responsive', function () {
	    console.log("WebviewManager.contentLoaded: webview.responsive event");
	 } );
	 this.view.addEventListener( 'sizechanged', function () {
	    console.log("WebviewManager.contentLoaded: webview.sizechanged event");
	 } );
	 window.addEventListener( "message", function ( event ) {
            console.log( 'WebviewManager.contentLoaded: index.html received message:', event.data )
			if(event.data.method=='login'){
				login(event.data.xml);
			}
			if(event.data.method=='save'){
				save(event.data.xml);
			}
			if(event.data.method=='getSubtest'){
				getSubtest(event.data.xml);
			}
        } );*/

}

WebviewManager.prototype.loadPage = function ( url ) {
    this.view.setAttribute( 'src', url );
}

WebviewManager.prototype.sendMsgToWebview = function(data) {
    if (data) {
        this.view.contentWindow.postMessage( data, "*" );
    }
}