LzBrowser.loadURL = function( url, target ) {
    _root.Debug.write( 'loadURL ', url );
    _root.getURL( url, target );
}

LzBrowser.loadJS = function( js, target ) {
    _root.Debug.write( 'loadJS ', js );
    _root.getURL( 'javascript:' + js + ';void(0);', target );
}