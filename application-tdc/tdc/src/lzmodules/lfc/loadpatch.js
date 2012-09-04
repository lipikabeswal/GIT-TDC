LzLoadQueue.loadMovieProxiedOrDirect = function (loadobj) {
    var reqstr;
    if ( !loadobj.proxied ) {
        reqstr = loadobj.reqobj.url;
    } else {

        delete loadobj.proxied;
        reqstr = _root.LzBrowser.getBaseURL( loadobj.secure,
                                                 loadobj.secureport ).toString();
        //fix up URL
        var url = loadobj.reqobj.url;
        if ( url != null) {
            // [2005 03 10] we don't try to munge URLs to HTTPS anymore,
            // the user specifies the URL protocol explicitly if they want https.
            loadobj.reqobj.url = _root.LzBrowser.toAbsoluteURL(url, false);
        }
        //set cache parameters
        if ( loadobj.reqobj.cache ){
            //if caching is on, client caching is on too unless you say
            //otherwise
            if ( loadobj.reqobj.ccache == null ) loadobj.reqobj.ccache = true;
        } else {
            //if caching is off or unmentioned, client caching is off unless 
            //you say otherwise
            loadobj.reqobj.cache = false;
            if ( loadobj.reqobj.ccache == null ) loadobj.reqobj.ccache = false;
        }

        var sep = "?";

        var dopost = (loadobj.reqobj.reqtype.toUpperCase() == 'POST');
        if (!dopost) {
            for ( var keys in loadobj.reqobj ){
                reqstr += sep + keys + "=" + escape( loadobj.reqobj[ keys ] );
                if ( sep == "?" ){
                    sep = "&";
                }
            }
        }

        // Defeat browser request cache
        if ( loadobj.reqobj.cache != true ) {
            var d = new Date();
            reqstr += sep + "__lzbc__=" + d.getTime();
        }

        if (dopost) {
            loadobj.reqobj.ccache = loadobj.reqobj.cache = false;
            loadobj.reqobj.fpv = _root.LzBrowser.getVersion();
        }
    }
    // [2005-08-19 ptw] the movie that will be loaded is attached to
    // the loadobj as both `lmc` and `lmc + loadobj.lmcnum++`.  What
    // is the purpose of this?  Since the `lmc + loadobj.lmcnum`s are
    // never cleaned up, this would seem to be a leak.  I don't see
    // how there could ever be more than one movie being loaded into a
    // loadobj.
    // [2005-08-22 adam] There is a bug in the flash player (at least
    // in older ones) where if you attach a movie that has the same
    // name as a movie that was previously attached (in the same
    // actionscript block) the attach doesn't work right and you end
    // up with the old movie.
    if (loadobj.isaudio == true) {
        loadobj.lmc = loadobj;
        //_root.Debug.write('LzLoadQueue new lmc for loadobj', loadobj, loadobj.lmc);
    } else {
    loadobj.lmcnum++;
    //loadobj.attachMovie( "empty" , "lmc" + loadobj.lmcnum , 9 );
    loadobj.createEmptyMovieClip( "lmc" + loadobj.lmcnum , 9 );
    loadobj.lmc = loadobj[ 'lmc' + loadobj.lmcnum ];
    }

    _root.Debug.write( 'IMG LOAD:', reqstr );
    if ( dopost ){
        for ( var k in loadobj.reqobj ){
            loadobj.lmc[ k ] = loadobj.reqobj[ k ];
        }
        loadobj.lmc.loadMovie( reqstr , "POST" );
    } else {
        loadobj.lmc.loadMovie( reqstr );
    }
}

