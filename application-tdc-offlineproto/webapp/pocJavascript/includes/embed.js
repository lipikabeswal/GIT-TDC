// A_LZ_COPYRIGHT_BEGIN

/*
 * JavaScript library for embedding Laszlo applications
 *
 * Usage:
 * In the <html><head> of an HTML document that embeds a Laszlo application,
 * add this line:
 *   <script src="{$lps}/embed.js" language="JavaScript" type="text/javascript"/>
 * At the location within the <html><body> where the application is to be
 * embeded, add this line:
 *   <script language="JavaScript" type="text/javascript">
 *     lzEmbed({url: 'myapp.lzx?lzt=swf', bgcolor: '#000000', width: '800', height: '600'});
 *   </script>
 * where the url matches the URI that the application is served from, and
 * the other properties match the attributes of the application's canvas.
 */

/* Write a tag start.  This code assumes that the attribute values don't
 * require inner quotes; for instance, {x: '100'} works, but
 * {url: 'a>b'} or {url: 'a"b'} won't. */
function lzWriteElement(name, attrs, closep, escapeme) {
    var lt = escapeme ? '&lt;' : '<';
    var o = lt + name;
    for (var p in attrs)
        o += ' ' + p + '="' + attrs[p] + '"';
    if (closep)
        o += '/';
    o += '>';
    return o;
}

function containskey (arr, key) {
    return (arr[key] != null);
}

/* Update each property of a with the value of the same-named property
 * on b. For example, lzUpdate({a:1, b:2}, {b:3, c:4}) mutates the
 * first argument into {a:1, b:3}. */
function lzUpdate(a, b) {
    for (var p in a)
        if (containskey(b,p)) {
            a[p] = b[p];
        }
}


/* Write an <object> and <embed> tag into the document at the location
 * where this function is called.  Properties is an Object whose properties
 * override the attributes and <param> children of the <object> tag, and
 * the attributes of the <embed> tag.
 */
function lzEmbed(properties, ieupgradeversion, escapeme) {
    // don't upgrade IE activex control unless asked
    if (ieupgradeversion == null) ieupgradeversion = 6;

    var url = properties.url;

    // strip query string and use FlashVars instead
    var sp = properties.url.split('?');
    url = sp[0];
    if (sp.length == 1) sp[1] = ''
    var flashvars = new Query(sp[1]);
    var query = '?'
    for (var i in flashvars.d) {
        // add lps vars to query string
        //
        // forcing addition to the query string fixes mac IE, but
        // it'll break the cache and the LFC loader will require
        // fixes.
        if (i == 'lzr' || i == 'lzt' || 
            i == 'krank' || i == 'debug' || 
            i == 'lzdebug' || i == 'lzkrank' || 
            i == 'fb' || i == 'sourcelocators' ) {
            query += i + '=' + flashvars.d[i] + '&';
        }
    }
    query = query.substr(0, query.length - 1);
    url += query;

    var width = properties.width;
    var height = properties.height;
    var id = properties.id;
    var o = '';
    var lt = escapeme ? '&lt;' : '<';
    
    var wmode = properties.wmode;
    objectAttributes = {
        type: 'application/x-shockwave-flash',
        data: url,
        width: 0, height: 0, name: 'lzapp', id: 'lzapp'
    };
    lzUpdate(objectAttributes, properties);
    if (wmode) objectAttributes['wmode'] = wmode;

    objectParams = {
        movie: url,
        scale: 'noscale',
        quality: 'high',
        menu: 'false',
        salign: 'lt',
        // The properties parameter should override these.
        width: 0, height: 0, bgcolor: 0};
    lzUpdate(objectParams, properties);
    // only add wmode if it's specified 
    if (wmode) objectParams['wmode'] = wmode;

    embedAttributes = {
        type: 'application/x-shockwave-flash',
        pluginspage: "http://www.macromedia.com/go/getflashplayer",
        scale: 'noscale',
        src: url,
        quality: 'high',
        salign: 'lt',
        menu: 'false',
        // The properties parameter should override these.
        width: 0, height: 0, bgcolor: 0, name: 'lzapp'};
    lzUpdate(embedAttributes, properties);
    // only add wmode if it's specified 
    if (wmode) embedAttributes['wmode'] = wmode;
    
    // Prehistoric netscape (not Mozilla)
    var ns = (document.layers)? true:false;
    // Some windows browser
    var win = navigator.appVersion.indexOf('Win') != -1;
    // !&@#(&!@# safari requires an embed tag to use flash vars - go figure...
    var safari = navigator.appVersion.indexOf('Safari') != -1;
    //alert('win: ' + win + ', ns ' + ns + ', safari ' + safari)

    // Invalid XHTML, used by windows to upgrade players
    if (win) {
        objectAttributes.classid = 'clsid:D27CDB6E-AE6D-11cf-96B8-444553540000';
        objectAttributes.codebase =  "http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=" + ieupgradeversion + ",0,0,0";
    }

    if (flashvars) {
        objectParams.FlashVars = flashvars.toString();
        embedAttributes.FlashVars = flashvars.toString();
    }

    
    if  (ns) {
        o = lzWriteElement('embed', embedAttributes, true, escapeme);
    } else {
        o = lzWriteElement('object', objectAttributes, false, escapeme);
        for (var p in objectParams)
        o += lt + 'param name="' +
                 p + '" value="' +
                 objectParams[p] + '" />\n';
        // More invalid XHTML, used only by windows
        // required by safari
        if (win || safari)  {
            o += lzWriteElement('embed', embedAttributes, true, escapeme);
        }
        o += lt + '/object>\n';
    } 
    //alert(o);
    document.write(o);
    return o;
}

// Based on moock fpi, cleaned up and simplified by Max Carlson
// Javascript 1.1 / VBScript block must run/be included before this is called
//
// moock fpi [f.lash p.layer i.nspector]
// version: 1.3.6
// written by colin moock
// code maintained at: http://www.moock.org/webdesign/flash/detection/moockfpi/
// terms of use posted at: http://www.moock.org/terms/

function detectFlash() {  
    var actualVersion = 0;
    var isIE  = navigator.appVersion.indexOf("MSIE") != -1;    // true if we're on ie
    if (navigator.plugins && 
        (navigator.plugins["Shockwave Flash 2.0"] || navigator.plugins["Shockwave Flash"]) ) {

        // Some version of Flash was found. Time to figure out which.
        // Set convenient references to flash 2 and the plugin description.
        var isVersion2 = navigator.plugins["Shockwave Flash 2.0"] ? " 2.0" : "";
        var flashDescription = navigator.plugins["Shockwave Flash" + isVersion2].description;

        var flashVersion = parseInt(flashDescription.substring(16));
        var minorVersion = flashDescription.substring(flashDescription.indexOf('r') + 1);
    } else if (! isIE) {
        var flashVersion = 0;
        var minorVersion = 0;
    } else {
        var vbver =  eval('VBFlashVer');
        if (vbver) {
            vbver = vbver.substring(vbver.indexOf(' ') + 1).split(',');
            var flashVersion = vbver[0];
            var minorVersion = vbver[2];
        } 
    }
  
    actualVersion = parseFloat(flashVersion + '.' + minorVersion)

    // If we're on msntv (formerly webtv), the version supported is 4 (as of
    // January 1, 2004). Note that we don't bother sniffing varieties
    // of msntv. You could if you were sadistic...
    if (navigator.userAgent.indexOf("WebTV") != -1) actualVersion = 4;  

    return actualVersion;
}

Query = function(s) {
    this.parse(s);
}

Query.prototype.parse = function(s) {
    if (s.indexOf('=') == -1) return;
    var p = s.split('&');
    this.d = {};
    for (i in p) {
        var nv = p[i].split('=');
        var n = nv[0];
        var v = nv[1];
        this.d[n] = v;
    }
}

Query.prototype.toString = function(del) {
    var o = '';
    if (!del) del = '';
    for (i in this.d) {
        o += del + i + '=' + this.d[i] + '&';
    }
    return o.substr(0, o.length - 1);
}

function getQuery(win) {
    if (win == null) win = top;
    var s = win.location.search;
    if (s.indexOf('=') > -1) {
        s = s.substr(1, s.length);
    }
    return s;
}

if (this != top) {
    top.Query = Query;
    top.getQuery = getQuery;
}

function lzHistEmbed(wr) {
	 //GS100305:  To set the URl for the swf in the local includes folder
    top.webapproot = wr;
	//alert(top.webapproot + ', ' + window.webapproot);
    document.write("<div id='lzhist' style='position:absolute;left:0px;top:0px;'><iframe src='"+top.webapproot+"/h.html' name='_lzhist' frameborder='0' scrolling='no' width='22' height='0'></iframe></div>");
    document.write('<div id="lzevent" style="position:absolute;top:0px;left:0px;"></div>');
}

function lzSetCanvasAttribute(n, v, h) {
    var i = 'lzevent';
    var o = '<object id="utility" name="h.swf" classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,14,0" width="1" height="1">' + 
'<param name="movie" value="'+top.webapproot+'/h.swf" />' +	 //GS100305:  To set the URl for the swf in the local includes folder
'<param name="FlashVars" value="n=' + escape(n) + '&v=' + escape(v) + '&__lzevent=1&__lzhistconn='+top.connuid+'"/>' +
'<param name="quality" value="high" />' +
'<param name="bgcolor" value="#FFFFFF" />' +
'<param name="profile" value="false" />' +
 //GS100305:  To set the URl for the swf in the local includes folder
'<embed id="utilityEmbed" name="h.swf" src="'+top.webapproot+'/h.swf" type="application/x-shockwave-flash" flashvars="n='+ escape(n) + '&v=' + escape(v) +'&__lzevent=1&__lzhistconn='+top.connuid+'" profile="false" quality="high" bgcolor="#FFFFFF" width="0" height="1" align="" pluginspage="http://www.macromedia.com/go/getflashplayer"></embed>' +
'</object>';
    //alert(o);
    if (h) {
		 //GS100305:  To set the URl for the swf in the local includes folder
        top.frames['_lzhist'].location = top.webapproot+'/h.html?n='+ escape(n) + '&v=' + escape(v) +'&__lzevent=1';
    } else {
        if (document.layers) {
            var oLayer = document.layers[i].document;
            oLayer.open();
            oLayer.write(o);
            oLayer.close();
        } else if (document.all) {
            document.all[i].innerHTML = o;
        } else if (parseInt(navigator.appVersion) >=5 && navigator.appName=="Netscape") {
            document.getElementById(i).innerHTML = o;
        }
    }
}

    top.connuid = Math.floor(Math.random() * 10000);
    //alert(top.connuid)
