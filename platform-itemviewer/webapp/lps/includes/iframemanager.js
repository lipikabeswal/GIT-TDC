/* X_LZ_COPYRIGHT_BEGIN ***************************************************
* Copyright 2001-2012 Laszlo Systems, Inc.  All Rights Reserved.          *
* Use is subject to license terms.                                        *
* X_LZ_COPYRIGHT_END ******************************************************/
lz.embed.iframemanager = {
    __counter: 0
    ,__frames: {}
    ,__ownerbyid: {}
    ,__loading: {}
    ,__callqueue: {}
    ,__calljsqueue: {}
    ,__sendmouseevents: {}
    ,__hidenativecontextmenu: {}
    ,__selectionbookmarks: {}

    // Browser information is sent with startfocus()
    ,__browser: ''
  
    // Constants to indicate how focus was obtained when startfocus() is called
    // These constants must match values in rtemanager.js and lz.Focus.
    ,FOCUS_KEYBOARD_PREV: -2
    ,FOCUS_KEYBOARD: -1
    ,FOCUS_MANUAL: 0
    ,FOCUS_MOUSE: 1

    // track urls set by setSrc()
    ,__srcbyid: {}
    ,create: function(owner, name, scrollbars, width_percent, height_percent, appendto, zoffset, canvasref) {
        //console.log('create: ' + owner + ', ' + name + ', ' + scrollbars + ', ' + appendto + ', ' + zoffset)
        var embed = lz.embed,
            id = '__lz' + embed.iframemanager.__counter++;
        if (typeof owner == 'string') {
            // swf
            // Add to table of owners so we can destroy the correct iframes in 
            // __reset();
            embed.iframemanager.__ownerbyid[id] = owner;
        }
        var src = 'javascript:""';
        var onload = 'lz.embed.iframemanager.__gotload("' + id + '")';

        if (name == null || name == 'null' || name == '') name = id;
        //console.log('using name', name);

        if (appendto == null || appendto == "undefined") {
            appendto = document.body;
        }

        // Create a top-level catch div to place after all iframes
        // In dhtml, this is not necessary
        var __post = null;
        if (typeof owner == 'string') {
            if (!document.getElementById('__post__Trap1')) {
                this.__createPostTrap ('__post__', appendto);
            }
            __post = document.getElementById('__post__Trap1');
        }



        //alert(owner + ', ' + name + ', ' + appendto)
        // Init IE9 like other browsers (LPP-10265)
        if (document.all && lz.embed.browser.version < 9) {
            // IE
            var html = "<iframe name='" + name + "' id='" + id + "' src='" + src + "' onload='" + onload + "' frameBorder='0' tabindex='0'";
            if (scrollbars != true) html += " scrolling='no'";
            html += "></iframe>";
            //alert(html);

            // A div surrounds the iframe and pre/post traps. Create it
            // in front of the trap (__post__Trap1) at the end
            var div = document.createElement('div');
            div.setAttribute('id', id + 'Container');
            //console.log("Create container");
            if (__post) {
                appendto.insertBefore(div, __post);  // swf
            }
            else {
                appendto.appendChild(div);  // dhtml
            }
                
            // Hide iframe container - see LPP-8753
            //             div.style.position = 'absolute';
            div.style.display = 'none';
            if (scrollbars != true) {
                // Prevent the iframe scrollbars from appearing
                div.style.overflow = 'hidden';
            }
            // Remove this setting: LPP-9994
            //            div.style.top = '0px';
            //            div.style.left = '0px';

            // Use percentage values to reduce scrollbar flicker
            if (width_percent) {
                div.style.width = width_percent;
            }
            if (height_percent) {
                div.style.height = height_percent;
            }

            div.innerHTML = html;

            var i = document.getElementById(id);

            // Create small <span>'s before and after the iframe to trap focus.
            // Originally, this was swf only, but is now used in dhtml
            this.__createPreTrap (id, div, i);
            this.__createPostTrap (id, div);
        } else {
            // For non-IE (or IE9+), the only use of the surrounding div is to hide
            // iframe scrollbars if scrollbars=false. (LPP-9973)
            var div = document.createElement('div');
            div.setAttribute('id', id + 'Container');

            // div.style.position = 'absolute';
            div.style.display = 'none';
            if (scrollbars != true) {
                // Prevent the iframe scrollbars from appearing
                div.style.overflow = 'hidden';
            }
            div.style.top = '0px';
            div.style.left = '0px';
            div.style.padding = '0px';
            div.style.margin = '0px';
            // div.setAttribute('tabindex', '0');

            // Use percentage values to reduce scrollbar flicker
            if (width_percent) {
                div.style.width = width_percent;
            }
            if (height_percent) {
                div.style.height = height_percent;
            }

            if (__post)
                appendto.insertBefore(div, __post);  // swf
            else
                appendto.appendChild(div);  //dhtml


            var i = document.createElement('iframe');
            i.setAttribute('name', name);
            i.setAttribute('src', src);
            i.setAttribute('id', id);
            i.setAttribute('onload', onload);
            // for Safari, see LPP-9098
            i.setAttribute('tabindex', '0');
            if (scrollbars != true) i.setAttribute('scrolling', 'no');

            this.appendTo(i, div);

            // Create small <span>'s before and after the iframe to trap focus.
            // Originally, this was swf only, but is now used in dhtml
            this.__createPreTrap (id, div, i);
            this.__createPostTrap (id, div);
        	//console.log("Create container");
        }

        if (i) {
            this.__finishCreate(id, owner, name, scrollbars, appendto, zoffset, canvasref);
        } else {
            // IE takes a while to create the iframe sometimes...
            // init call queue and set timeout to finish startup after the element can be found..
            this.__callqueue[id] = [ ['__finishCreate', id, owner, name, scrollbars, appendto, zoffset, canvasref] ];
            setTimeout('lz.embed.iframemanager.__checkiframe("' + id + '")', 10); 
        }

        return id + '';
    }

    // Create a focusable span that will catch tab focus.
    ,__createTrap: function(fullid) {
        // IE is picky about what tags you can use. You want something that takes up little space
        // and receives focus. In general, setting tabindex makes an element focusable.
        var catchall = document.createElement('span');
        catchall.setAttribute('id', fullid);
        catchall.setAttribute('tabindex', 0);
        catchall.style.width = '1px';
        catchall.style.height = '1px';
        if (document.all) {
            catchall.contentEditable = 'true'; // Without this, you won't tab into this element in IE
        }

        return catchall;
    }

    ,__createPostTrap: function(id, div) {
        // Add a small catch-all element to catch focus if it goes past the iframe
        //console.log('__createPostTrap ', id, ' ', div);
        var catchall = this.__createTrap (id+'Trap1');

        lz.embed.attachEventHandler(catchall, 'focus', lz.embed.iframemanager, '__iframetrap_post', id);
        this.appendTo(catchall, div);
    }

    // Create a focusable div before the iframe to catch focus
    ,__createPreTrap: function(id, div, iframe) {
        // Add a small catch-all element to catch focus if it goes before the iframe
        var catchall = this.__createTrap (id+'Trap0');

        lz.embed.attachEventHandler(catchall, 'focus', lz.embed.iframemanager, '__iframetrap_pre', id);
        div.insertBefore(catchall, iframe);
    }

    // check to see if the iframe is available yet...
    ,__checkiframe: function(id) {
        var iframe = document.getElementById(id),
            iframemanager = lz.embed.iframemanager;
        if (iframe) {
            var queue = iframemanager.__callqueue[id];
            delete iframemanager.__callqueue[id];
            iframemanager.__playQueue(queue);
        } else {
            // try again in a little while
            setTimeout('lz.embed.iframemanager.__checkiframe("' + id + '")', 10); 
        }
    }
    // generic function for playing back queues
    ,__playQueue: function(queue) {
        var scope = lz.embed.iframemanager;
        for (var i = 0; i < queue.length; i++) {
            var callback = queue[i];
            var methodName = callback.splice(0,1);
            scope[methodName].apply(scope, callback);
        }
    }
    // needed to break this out into a separate method to deal with IE
    ,__finishCreate: function(id, owner, name, scrollbars, appendto, zoffset, canvasref) {
        var i = document.getElementById(id),
            embed = lz.embed;
        // getElementById() may return `undefined` when iframe is already destroyed
        if (! i) return;
        // Find owner div
        if (typeof owner == 'string') {
            // only required for swf - dhtml iframes are positioned by being attached into the div heirarchy...
            i.appcontainer = (embed.applications[owner])._getSWFDiv();
        }

        var iframemanager = embed.iframemanager;

        i.__owner = owner;
        iframemanager.__frames[id] = i;

        // set style
        var iframe = iframemanager.getFrame(id);
        iframe.__gotload = iframemanager.__gotload;
        // Use defaultz passed in, otherwise assume Flash default
        iframe.__zoffset = zoffset != null ? zoffset : this.__bottomz;

        if (document.getElementById && !(document.all) ) {
            iframe.style.border = '0';
        } else if (document.all) {
            // IE
            // must be set before the iframe is appended to the document (LPP-7310)
            // iframe.setAttribute('frameBorder', '0');
            iframe.setAttribute('allowtransparency', 'true');

            iframe.frameBorder = 0;

            var metadata = embed[iframe.owner]
            if (metadata && metadata.runtime == 'swf') { 
                // register for onfocus event for swf movies - see LPP-5482 
                var div = metadata._getSWFDiv();
                div.onfocus = iframemanager.__refresh;
            }
        }
        iframe.style.position = 'absolute';

        // Call back to owner
        if (typeof owner == 'string') {
            // swf
            // Use timeout to ensure __setiframeid() is called after create() 
            // returns - see LPP-9272
            setTimeout("lz.embed.applications." +  owner + ".callMethod('lz.embed.iframemanager.__setiframeid(\"" + id + "\")')", 0);
        } else {
            owner.__setiframeid(id);
        }
    }
    ,appendTo: function(iframe, div) { 
        //console.log('appendTo', iframe, div, iframe.__appended);
        if (div.__appended == div) return;
        if (iframe.__appended) {
            // remove 
            //console.log('remove', iframe, iframe.__appended);
            old = iframe.__appended.removeChild(iframe);
            div.appendChild(old);
        } else {
            div.appendChild(iframe);
        }
        iframe.__appended = div;
    }
    ,getFrame: function(id) { 
        return lz.embed.iframemanager.__frames[id];
    }
    ,getFrameWindow: function(id) {
        if (!this['framesColl']) {
            if (document.frames) { //Opera, Internet Explorer
                this.framesColl = document.frames;
            }
            else {
                this.framesColl = window.frames; //Firefox, Safari, Netscape
            }
        }
        return this.framesColl[id];
    }
    ,setHTML: function(id, html) { 
        // must be called after the iframe loads, or it will be overwritten
        if (html) {
            var win = lz.embed.iframemanager.getFrameWindow(id);
            if (win) {
                win.document.body.innerHTML = html;
            }
        }
    }
    ,setSrc: function(id, s, history) { 
        if (this.__callqueue[id]) { 
            this.__callqueue[id].push(['setSrc', id, s, history]);
            return;
        }
        // clear out mouse listeners
        this.__setSendMouseEvents(id, false);
        //console.log('setSrc', id, s, history)
        if (history) {
            var embed = lz.embed,
                iframe = embed.iframemanager.getFrame(id);
            if (! iframe) return;
            iframe.setAttribute('src', s);
        } else {
            var iframe = this.getFrameWindow(id);
            if (! iframe) return;
            iframe.location.replace(s);
        }
        this.__srcbyid[id] = s;
        this.__loading[id] = true;
    }
    // last measured browser zoom (flash only)
    ,__lastzoom: -1
    ,__positionbyid: {} // Cache position info to speed up setPosition
    ,setPosition: function(id, x, y, width, height, visible, z, flash_width) { 
        if (this.__callqueue[id]) { 
            this.__callqueue[id].push(['setPosition', id, x, y, width, height, visible, z, flash_width]);
            return;
        }

  
        // If flash_width is non-zero, it means the width of the canvas is 
        // specified and the browser zoom should be measured. The passed values
        // of x, y, width, height will be scaled by the browser zoom. 
        // flash_width is only used for SWF.
        if (flash_width && flash_width > 0) {
            // Compute the width of the browser
            var browser_width = -1;
            try {
                browser_width = document.body.clientWidth;
                var zoom = browser_width / flash_width;
                zoom = Math.round(zoom*1000) / 1000; // Use 3 decimal places
                if (zoom > 0 && zoom != this.__lastzoom) {
                    this.__lastzoom = zoom;
                    //console.log("Zoom = ", zoom, browser_width, flash_width);
                }

                if (zoom != 1) {
                    if (x != null) x = Math.round(x * zoom);
                    if (y != null) y = Math.round(y * zoom);
                    if (width != null) width = Math.round(width * zoom);
                    if (height != null) height = Math.round(height * zoom);
                    //console.log('zoom',zoom,'x',x,'y',y,'width',width,'height',height);
                }
            }
            catch (e) {
            }
        }

        //Debug.write('setPosition', id);
        //console.log('setPosition', id, x, y, width, height, visible)
        var embed = lz.embed,
            iframe = embed.iframemanager.getFrame(id);
        if (! iframe) return;
        if (iframe.appcontainer) {
            // Flash needs the absolute position
            var pos = embed.getAbsolutePosition(iframe.appcontainer);
        } else {
            // default to the origin of the containing div for DHTML
            var pos = {x:0,y:0};
        }

        // See if the values have changed. If not, do nothing
        // Note: This is disabled until more testing can be done.
        //var cid = [x,y,width,height,visible,z,flash_width,pos.x,pos.y].join(':');
        //var lastcid = embed.iframemanager.__positionbyid[id];
        //if (lastcid && lastcid === cid){
        //    return;  // No change in position. return.
        //}

        //console.log('position change: ', id, cid);
        //embed.iframemanager.__positionbyid[id] = cid;


        var div = null;
        if (iframe.parentElement) // IE
            div = iframe.parentElement;
        if (iframe.parentNode)
            div = iframe.parentNode;

        // Set the width, height, x, y or the iframe parent.
        if (div) {
            if (div.style.position != 'absolute') div.style.position = 'absolute';

            if (x != null && ! isNaN(x)) div.style.left = (x + pos.x) + 'px';
            //console.log('div.style.left',div.style.left);
            if (y != null && ! isNaN(y) && (y + pos.y) > 20 ) div.style.top = (y + pos.y) + 'px';
            //console.log('div.style.top',div.style.top);
            if (div.style.width.indexOf('%') == -1) {
                if (width != null && ! isNaN(width)) div.style.width = width + 'px';
            }
            if (div.style.height.indexOf('%') == -1) {
                if (height != null && ! isNaN(height)) div.style.height = height + 'px';
            }

        }

        // Set the width, height of the iframe. top=left=0px. I found that you can't
        // set 100% because some browsers (like IE7) does not display properly.
       	 iframe.style.left = '0px';
        	iframe.style.top = '0px';
        //console.log("REVISITING");
        //if (width != null && ! isNaN(width)) iframe.style.width = width + 'px';
        //if (height != null && ! isNaN(height)) iframe.style.height = height + 'px';
        if (width != null && ! isNaN(width)) iframe.style.width = '100%';
        if (height != null && ! isNaN(height)) iframe.style.height = '100%';

        if (visible != null) {
            embed.iframemanager.setVisible(id, visible);
        }
        if (z != null) {
            embed.iframemanager.setZ(id, z);
        }
    }
    ,setVisible: function(id, v) { 
        if (this.__callqueue[id]) { 
            this.__callqueue[id].push(['setVisible', id, v]);
            return;
        }
        if (typeof v == 'string') {
            v = v == 'true';
        }
        //console.log('setVisible', id, v)
        //Debug.write('setVisible', id);
        lz.embed.iframemanager.setBothStyles(id, null, 'display', v ? 'block' : 'none');
    }
    // track highest z index 
    ,__topz: 100000
    // Force focus (originally was SWF-only, but now supports dhtml)
    ,forceFocus: function(id) {
        if (lz.embed.iframemanager.__ownerbyid[id]) {
            // Set focus to the swf object. This will blur any active html
            // components. __ownerbyid only exists for swf
            var iframe = lz.embed.iframemanager.getFrame(id);
            if (iframe && iframe.appcontainer) {
                setTimeout('lz.embed.iframemanager.getFrame("' + id + '").appcontainer.focus()', 1);
            }
        }
        else {
            // dhtml
            var div = LzSprite.__mouseActivationDiv;
            //console.log('Activating mouse div = ', div);
            div.onmouseover();
            return;
        }
    }
    // only used for Flash
    ,bringToFront: function(id, z) { 
        if (this.__callqueue[id]) { 
            this.__callqueue[id].push(['bringToFront', id, z]);
            return;
        }
        var embed = lz.embed,
            iframe = embed.iframemanager.getFrame(id);
        if (! iframe) return;
        // only update z offset if we're not already in back
        if (id !== embed.iframemanager.__front) {
            iframe.__zoffset = ++this.__topz;
            embed.iframemanager.__front = id;
            this.setZ(id, z);
        }
    }
    // track lowest z index 
    ,__bottomz: 99000
    // only called for Flash
    ,sendToBack: function(id, z) { 
        if (this.__callqueue[id]) { 
            this.__callqueue[id].push(['sendToBack', id, z]);
            return;
        }
        var embed = lz.embed,
            iframe = embed.iframemanager.getFrame(id);
        if (! iframe) return;
        // only update z offset if we're not already in back
        if (id !== embed.iframemanager.__back) {
            iframe.__zoffset = --this.__bottomz;
            embed.iframemanager.__back = id;
            this.setZ(id, z);
        }
    }
    ,setStyle: function (id, elementid, property, value) {
        var element;
        if (elementid == null) {
            // set the style of the iframe directly
            element = lz.embed.iframemanager.getFrame(id);
        } else {
            // look for an element by elementid
            var win = lz.embed.iframemanager.getFrameWindow(id);
            if (!win) return;
            element = win.document.getElementById(elementid);
        }
        try {
            element.style[property] = value;
        } catch (e) {
        }
    }
    // Like setStyle() but sets the parent div
    ,setDivStyle: function (id, elementid, property, value) {
        var element;
        if (elementid == null) {
            // set the style of the iframe directly
            element = lz.embed.iframemanager.getFrame(id);
        } else {
            // look for an element by elementid
            var win = lz.embed.iframemanager.getFrameWindow(id);
            if (!win) return;
            element = win.document.getElementById(elementid);
        }
        try {
            var div = null;
            if (element.parentElement) // IE
                div = element.parentElement;
            if (element.parentNode)
                div = element.parentNode;

            if (div)
                div.style[property] = value;
        } catch (e) {
        }
    }
    // Set both the iframe style and div style to the same value
    ,setBothStyles: function (id, elementid, property, value) {
        var element;
        if (elementid == null) {
            // set the style of the iframe directly
            element = lz.embed.iframemanager.getFrame(id);
        } else {
            // look for an element by elementid
            var win = lz.embed.iframemanager.getFrameWindow(id);
            if (!win) return;
            element = win.document.getElementById(elementid);
        }
        try {
            // Set the iframe style
            element.style[property] = value;

            var div = null;
            if (element.parentElement) // IE
                div = element.parentElement;
            if (element.parentNode)
                div = element.parentNode;

            // Set the div style
            if (div)
                div.style[property] = value;
        } catch (e) {
        }
    }
    // Sends a named event back to the component, called from an iframe.
    // arg should be a JSON encoded string
    ,asyncCallback: function(id, event, arg, callbackid) {
        var iframe = lz.embed.iframemanager.getFrame(id);
        if (! iframe || ! iframe.__owner) return;

        if (iframe.__owner.__iframecallback) {      
            // DHTML
            // try to parse JSON from lz.sendEvent()
            if (typeof arg === 'string'){
                try {
                    arg = JSON.parse(arg) || arg;
                } catch (e) {
                }
            }
            //console.log('asyncCallback', id, event, arg);
            iframe.__owner.__iframecallback(event, arg);
        } else {
            // Flash
            if (lz.embed[iframe.__owner]) {
                arg = (arg != null) ? "," + arg : '';
                arg += (callbackid != null) ? "," + callbackid + "" : '';
                //console.log("lz.embed.iframemanager.__iframecallback('" + id + "','" + event + "'" + arg + ")")
                // __iframecallback parses JSON
                lz.embed[iframe.__owner].callMethod("lz.embed.iframemanager.__iframecallback('" + id + "','" + event + "'" + arg + ")");
            } else {
                // installing a new player now...
                return;
            }
        }
    }
    ,__gotunload: function(e, id) {
        // Browser is moving to another page. Cleanup this object
        //console.log('__gotunload', id);
        lz.embed.iframemanager.__destroy(id);
    }
    ,__gotload: function(id) { 
        var iframe = lz.embed.iframemanager.getFrame(id);
        //console.log('__gotload IframeManager', id, iframe);
        //Alerting TDC for load complete
       iframeLoaded(id,iframe); //Custom code for event notification.
        if (! iframe || ! iframe.__owner) return;

        if (this.__loading[id] == true) {
            // finish loading
            this.__loading[id] = false;

            // Show iframe container - see LPP-8753
            var div = null;

            if (iframe.parentElement) // IE
                div = iframe.parentElement;
            if (iframe.parentNode)
                div = iframe.parentNode;
            if (div) {
                div.style.display = '';
            }

            // Enable mouse listeners if needed
            if (!this.__sendmouseevents[id]) {
                this.__setSendMouseEvents(id, true);
            }
            if (this.__calljsqueue[id]) {
                this.__playQueue(this.__calljsqueue[id]);
                delete this.__calljsqueue[id];
            }
        }

        var win = this.getFrameWindow(id);
        // Check window.location first, fall back to iframe.src.  See history 
        // flag in setSrc()

        var winlocation;
        try {
            // avoid permission error warning
            winlocation = win && win.location && win.location.href;
        } catch (e) {
        }

        var src = winlocation || iframe.src;

        if (src && this.__srcbyid[id] !== src && src === 'javascript:""') {
            // See LPP-9272.
            // javascript:"" is the default src set when the iframe is created.
            // Don't send onload for this value, unless it was explicitly set 
            // by setSrc().  
            
            //console.log('skipping', src);
            return
        }

        // Send callback to support setHTML() which must be called after the 
        // initial load
        // Use timeout to ensure frame is really loaded.
        setTimeout("lz.embed.iframemanager.asyncCallback('" + id + "', 'load')", 1);
        
       
        try {  
            iframe.contentWindow.document.__iframeid = id;                
            iframe.contentWindow.document.onclick =  function () {                    
               var ifmanager = window.lz.embed.iframemanager;                
               var iframe = ifmanager.getFrame(this.__iframeid);               
               var owner = iframe.__owner;
               if (owner) {
                    if (typeof owner == 'string') {
                      // swf TBD  
                    } else {
                      owner.onclick.sendEvent();   
                      LzMouseKernel.__scope[LzMouseKernel.__callback]("onclick", owner, null);   
                    } 
               }                
            } 
            
            iframe.contentWindow.document.onmousedown =  function () {                    
               var ifmanager = window.lz.embed.iframemanager;                
               var iframe = ifmanager.getFrame(this.__iframeid);               
               var owner = iframe.__owner;
               if (owner) {
                    if (typeof owner == 'string') {
                      // swf TBD  
                    } else {
                      owner.onmousedown.sendEvent();      
                      LzMouseKernel.__scope[LzMouseKernel.__callback]("onmousedown", owner, null);  
                    } 
               }                
            } 
            
            iframe.contentWindow.document.onmouseup =  function () {                    
               var ifmanager = window.lz.embed.iframemanager;                
               var iframe = ifmanager.getFrame(this.__iframeid);               
               var owner = iframe.__owner;
               if (owner) {
                    if (typeof owner == 'string') {
                      // swf TBD  
                    } else {
                      owner.onmouseup.sendEvent();     
                      LzMouseKernel.__scope[LzMouseKernel.__callback]("onmouseup", owner, null);   
                    } 
               }                
            } 
            
            
            
        } catch (e) {
            
        }
        
    }
    // called in IE for onfocus event in swf - see LPP-5482 
    ,__refresh: function() { 
        // refresh all iframes
        var frames = lz.embed.iframemanager.__frames;
        for (var id in frames) {
            var frame = frames[id];
            if (frame && frame.style.display=="block"){
                frame.style.display="none";
                frame.style.display="block"
            }
        } 
    }
    ,setZ: function(id, z) { 
        if (this.__callqueue[id]) { 
            this.__callqueue[id].push(['setZ', id, z]);
            return;
        }
        var iframe = lz.embed.iframemanager.getFrame(id);
        if (! iframe) return;
        z += iframe.__zoffset;
        // console.log('setZ ', z, ' ', iframe, ' ', iframe.__zoffset); 
        // Make sure the div and iframe have the same z-index
        lz.embed.iframemanager.setBothStyles(id, null, 'zIndex', z);
    }
    ,scrollBy: function(id, x, y) { 
        if (this.__callqueue[id]) { 
            this.__callqueue[id].push(['scrollBy', id, x, y]);
            return;
        }
        var iframe = this.getFrameWindow(id);
        if (! iframe) return;
        //console.log('scrollBy', x, y, iframe); 
        iframe.scrollBy(x, y);
    }
    ,setFocus: function(id, focusmethod) { 
        // setFocus() is called from lzx when the html component gets focus.
        // focusmethod indicates how focus was generated (see FOCUS_* constants)
        if (this.__callqueue[id]) { 
            this.__callqueue[id].push(['setFocus', id]);
            return;
        }

        // Initialize __browser with information from lz.embed.browser. This is an
        // object that is transmitted as a string.
        if (this.__browser.length == 0) {
            this.__browser = '{';
            this.__browser += "'browser':'" + lz.embed.browser.browser + "',";
            this.__browser += "'version':'" + lz.embed.browser.version + "',";
            this.__browser += "'subversion':'" + lz.embed.browser.subversion + "',";
            this.__browser += "'OS':'" + lz.embed.browser.OS + "'";
            this.__browser += '}';
        }

        // Make sure the swf gets blur on Chrome so focus can come back
        var iframe = lz.embed.iframemanager.getFrame(id);
        if (!iframe) return;
        if (iframe.appcontainer) {
            // Blur swf immediately for Chrome or you can't get focus back (LPP-10011)
            if (lz.embed.browser.isChrome)
                iframe.appcontainer.blur();
        }

        // Use a timer so the focus event does not happen within another event.
        // Construct the javascript that executes.
        var jsframe = 'var iframe = lz.embed.iframemanager.getFrameWindow(\''+id+'\');';

        var js = jsframe;

        js = js + 'if (iframe.startfocus) { setTimeout("' + jsframe + 'iframe.startfocus(' + focusmethod + ', ' + this.__browser + ');", 10);}';

        // Focusing the iframe is not needed on IE. If you include it on IE,
        // it will take 2 clicks to focus on an element within the iframe.
        if (!document.all)
            js = js + 'iframe.focus();';

        //console.log('js:', js);
        setTimeout(js, 2); // Make sure this is greater than the numer in setBlur()
    }
    ,setBlur: function(id) { 
        // It is important that you do not blur an iframe because in IE/FF3.6 the
        // browser window will move to the back.
        if (this.__callqueue[id]) { 
            this.__callqueue[id].push(['setBlur', id]);
            return;
        }

        //For IE7, move iframe focus to the body and then focus the swf
        if (lz.embed.browser.isIE && lz.embed.browser.version <= 7) {
            //console.log('IE7 blur');
            var iframe = lz.embed.iframemanager.getFrame(id);
            if (! iframe) return;
            if (iframe.appcontainer) {
                // Move the focus to the body
                setTimeout("lz.embed.iframemanager.getFrameWindow('" + id + "').document.body.focus()", 0);

                // Focus swf
                setTimeout("lz.embed.iframemanager.getFrame('" + id + "').appcontainer.focus()",1);
            }
        }
    }
    ,__destroy: function(id) { 
        if (this.__callqueue[id]) { 
            this.__callqueue[id].push(['__destroy', id]);
            return;
        }
        var iframemanager = lz.embed.iframemanager,
            iframe = iframemanager.__frames[id];
        if (iframe) {
            // clear out mouse listeners
            if (this.__sendmouseevents[id]) {
                this.__setSendMouseEvents(id, false);
            }
            iframe.__owner = null;
            iframe.appcontainer = null;

            // Destroy the iframe and its container
            var el = document.getElementById(id + 'Container');

            if (document.all) { // IE
                if (el.parentElement) {
                    el.parentElement.removeChild(el);
                }
            } else if (iframe.parentNode) {
                iframe.parentNode.removeChild(iframe);

                if (el.parentNode) {
                    el.parentNode.removeChild(el);
                }
            }
            delete iframemanager.__frames[id];
            delete iframemanager.__srcbyid[id];
            if (iframemanager.__ownerbyid[id])
                delete iframemanager.__ownerbyid[id];
        }
    }
    ,callJavascript: function(id, methodName, callbackDel, args) {
        if (this.__callqueue[id]) { 
            this.__callqueue[id].push(['callJavascript', id, methodName, callbackDel, args]);
            return;
        }
        if (this.__loading[id]) {
            // queue call for later
            if (! this.__calljsqueue[id]) {
                this.__calljsqueue[id] = [];
            }
            this.__calljsqueue[id].push(['callJavascript', id, methodName, callbackDel, args]);
            return;
        }
        var iframe = lz.embed.iframemanager.getFrameWindow(id);
        if (!args) args = [];
        try {
            // Must invoke eval in iframe context
            if(iframe){
            var method = iframe.eval(methodName);
            if (method) {
                // Ditto for method
                var retVal = method.apply(iframe, args);
                //console.log('callJavascript', methodName, args, 'in', iframe, 'result', retVal, callbackDel);
                if (callbackDel) callbackDel.execute(retVal);
                return retVal;
            }
            }
        } catch (e) {
            // dump error to console if available
            window.console && console.error && console.error('callJavascript() caught error:', e, ',',id, ',',methodName, ',',callbackDel, ',',args);
        }
    }
    ,callRPC: function(id, methodName, callback, args) {
        var iframemanager = lz.embed.iframemanager,
            iframe = iframemanager.getFrameWindow(id);
        var callobj =  {
            destination: iframe,
            publicProcedureName: methodName,
            params: args
        }
        if (callback != null) {
            if (typeof callback == 'number') {
                // Flash uses a callback ID
                //console.log('callRPC creating callback', callback);
                // store a copy to be closed over by onSuccess
                callobj.onSuccess = function(returnObj) {
                    //console.log('callRPC onSuccess', callback, returnObj.returnValue);
                    // Add the callbackID to the returnObj, so flash knows
                    // who to call
                    iframemanager.asyncCallback(id, '__lzcallback', JSON.stringify(returnObj.returnValue), callback);
                }
            } else {
                callobj.onSuccess = function(returnObj) {
                    //console.log('callRPC onSuccess', callback, returnObj.returnValue);
                    callback(returnObj.returnValue);
                }
            }
        }
        // IE 8 seems to get error callbacks even for valid calls...
//        if (window.console && console.error) {
//            callobj.onError = function(statusObj) {
//                console.error('lz.embed.iframemanager.callRPC error:', JSON.stringify(statusObj), 'with call', JSON.stringify(callobj));
//            }
//        }
        //console.log('pmrpc callRPC', callback, callobj);
        //pmrpc.call(callobj); 
    }
    ,__getRPCMethods: function(id) {
       
    }
    ,__mouseEvent: function(e, id) {
        //console.log("__mouseEvent", e, id);
        var embed = lz.embed;
        var iframe = embed.iframemanager.getFrame(id);
        if (! iframe) return;


        if (!e) {
            e = window.event;
        }

        var eventname = 'on' + e.type;
        if (iframe.__owner && iframe.__owner.sprite && iframe.__owner.sprite.__mouseEvent) {
            // dhtml
            if (eventname == 'oncontextmenu') {
                if (! embed.iframemanager.__hidenativecontextmenu[id]) {
                    return;
                } else {
                    var pos = embed.getAbsolutePosition(iframe); 
                    LzMouseKernel.__sendMouseMove(e, pos.x, pos.y)
                    return LzMouseKernel.__showContextMenu(e);
                }
            }
            iframe.__owner.sprite.__mouseEvent(eventname);

            // clear __lastMouseDown to prevent mouseover/out events being sent as dragin/out events - see LzSprite.js and LzMouseKernel.js - there will be no global mouseup sent from window.document to clear this...
            if (eventname == 'onmouseup') {
                if (LzMouseKernel.__lastMouseDown == iframe.__owner.sprite) {
                    LzMouseKernel.__lastMouseDown = null;
                }
            }
        } else {
            // Flash
            // deal with IE event names
            if (eventname == 'onmouseleave') {
                eventname = 'onmouseout';
            } else if (eventname == 'onmouseenter') {
                eventname = 'onmouseover';
            } else if (eventname == 'oncontextmenu') {
                return;
            }
            embed.iframemanager.asyncCallback(id,'__mouseevent',"\'" + eventname +"\'");
        }
    }
    ,__iframetrap_pre: function(e, id) {
        // Called when the <span> before the iframe gets focus. Move lzx focus backwards.
        //console.log('__iframetrap_pre', e, ' ', id);

        // Make sure lzx has the focus
        lz.embed.iframemanager.forceFocus(id);

        // Move focus to the swf and move to the previous element
        lz.embed.iframemanager.asyncCallback(id,'__focusevent',"'-move'");
    }
    ,__iframetrap_post: function(e, id) {
        // Called when the <span> after the iframe gets focus. Move lzx focus forward.
        //console.log('__iframetrap_post', e, ' ', id);

        if (id == '__post__') {
            // __post__ is a special case. It is after all lzx items so hitting
            // here means to go to the first focus item. (swf only)
            id = '__lz0'; // The first html component is always called __lz0.
        }

        lz.embed.iframemanager.forceFocus(id);

        // Move focus to the swf and move to the next element
        lz.embed.iframemanager.asyncCallback(id,'__focusevent',"'+move'");
    }
    ,__focusEvent: function(e, id) {
        // Called when focus or blur events are intercepted on the iframe.
        //console.log("__focusEvent ", e.type, ' ', id);
        var embed = lz.embed;
        var iframe = embed.iframemanager.getFrame(id);
        if (! iframe) return;

        if (!e) {
            e = window.event;
        }

        var eventname = 'on' + e.type;
        if (eventname == 'onfocus' || eventname == 'onfocusin') {
            /*
            if (document.all) {
                var i = document.getElementById(id);
                if (i) i.__activeelement = document.activeElement;
            }
            */
            // Add focus behavior here
            embed.iframemanager.asyncCallback(id,'__focusevent',"'focus'");
            //return false;
        }

        //TODO. Verify that no blur operations are needed and remove the following code.
        //DEBUG
        return;

        if (eventname == 'onblur' || eventname == 'onfocusout') {
            // Add blur behavior here

            // Move the focus
            embed.iframemanager.asyncCallback(id,'__focusevent',"'blur'");

            if (e.preventDefault) {
                e.preventDefault();
                e.stopPropagation();
                console.log('stopped event in Firefox and others');
                // Try to keep focus on the iframe
                var js1 = "document.body.focus();";
                var js2 = "lz.embed.iframemanager.getFrame('" + id + "').focus();";
                var js3 = "lz.embed.iframemanager.getFrameWindow('"+id+"').document.body.focus();";
                var js4 = "setTimeout('" + js2 + "', 10);";
                var js5 = "setTimeout('" + js3 + "', 20);";
                var js = js1 + js4 + js5;
                console.log("js:", js);
                //                setTimeout(js, 5);
            }
            return false;

            // Get the swf element to give it focus again
            //            var i = window.parent.document.getElementById(id),
            var appdiv = iframe.appcontainer.id;
            //console.log('going to blur. appcontainer=', iframe.appcontainer);
            //            i.appcontainer = embed.applications[owner]._getSWFDiv();

            // IE can generate unwanted blur events. Make sure we get the expected object
            if (document.all) {
                var i = document.getElementById(id);
                if (i && i.__activeelement) {
                    //console.log('lastactive: ', i.__activeelement, ' currentactive: ', document.activeElement);
                    if (i.__activeelement != document.activeElement) {
                        return;
                    }
                }
            }

            // Move the focus
            embed.iframemanager.asyncCallback(id,'__focusevent',"'blur'");

            //            js = js + "lz.embed.iframemanager.asyncCallback('" + id + "','__focusevent','blur');";
            //            setTimeout (js, 1);

            if (iframe.appcontainer){
                var div = null;
                if (iframe.parentElement) // IE
                    div = iframe.parentElement;
                if (iframe.parentNode)
                    div = iframe.parentNode;
                //if (div) div.focus(); // Move focus back to the main document.
                //                if (div) setTimeout (function() { div.focus();}, 0);

                //lz.embed.iframemanager.asyncCallback('" + id + "','__focusevent','\"blur\"');

                var js = "var i = lz.embed.iframemanager.getFrame('" + id + "'); i.appcontainer.focus();console.log('focus to appcontainer', i.appcontainer);";
                //    js = js + "lz.embed.iframemanager.asyncCallback('" + id + "','__focusevent','\"blur\"');";

                //    js = js + "lz.embed.iframemanager.asyncCallback('" + id + "','__focusevent','\"blur\"');";
                var i = document.getElementById(appdiv);
                console.log('js:', js);
                setTimeout (js, 100);
                //iframe.appcontainer.focus();
            }
            //            else
            //                iframe.blur();

            //alert('going to blur');
            if (e.preventDefault) {
                e.preventDefault();
                e.stopPropagation();
                console.log('stopped event in Firefox and others');
            }
            return false;
        }
    }
    ,setSendMouseEvents: function(id, send) {
        if (this.__callqueue[id]) { 
            this.__callqueue[id].push(['setSendMouseEvents', id, send]);
            return;
        }
        lz.embed.iframemanager.__setSendMouseEvents(id, send);
    }
    ,__setSendMouseEvents: function(id, send) {
        var iframemanager = lz.embed.iframemanager;
        var currval = iframemanager.__sendmouseevents[id] || false;
        if (send === currval) return;
        iframemanager.__sendmouseevents[id] = send;
        try {
            var doc = iframemanager.getFrameWindow(id).document;
            // Safari returns `undefined` for cross-domain
            // Opera throws when accessing valueOf() for cross-domain
            // doc.valueOf returns undefined in IE
            if (!doc) return;
            if (!doc.all && !doc.valueOf) return;
        } catch(e) {
            // console.log("cross domain issue", e);
            // this can fail due to cross-domain restrictions (IE, Firefox)
            return;
        }

        //console.log('sending', id, send);
        var method =  lz.embed[ send ? 'attachEventHandler' : 'removeEventHandler'];

        // bind/remove the unload event handler
        if (send) {
            var unload_event = (document.all) ? 'beforeunload' : 'unload';
            method(window, unload_event, iframemanager, '__gotunload', id);
        }

        // bind/remove into global events
        method(doc, 'mousedown', iframemanager, '__mouseEvent', id);
        method(doc, 'mouseup', iframemanager, '__mouseEvent', id);
        method(doc, 'click', iframemanager, '__mouseEvent', id);
        //method(doc, 'mousemove', iframemanager, '__mouseEvent', id);
        doc.oncontextmenu = function(e) {
            if (! e) e = window.event;
            return iframemanager.__mouseEvent(e, id);
        }

        if (lz.embed.browser.isIE) {
            method(doc, 'mouseenter', iframemanager, '__mouseEvent', id);
            method(doc, 'mouseleave', iframemanager, '__mouseEvent', id);

            method(doc, 'focus', iframemanager, '__focusEvent', id);
            method(doc, 'blur', iframemanager, '__focusEvent', id);

            // console.log('IE events setup ', doc.body, ' ', doc);
        } else {
            var obj = doc;
            if (lz.embed.browser.isSafari || lz.embed.browser.isChrome)
                obj = doc.defaultView;  // This is the window, not the document
            method(doc, 'mouseover', iframemanager, '__mouseEvent', id);
            method(doc, 'mouseout', iframemanager, '__mouseEvent', id);

            method(obj, 'focus', iframemanager, '__focusEvent', id);
            method(obj, 'blur', iframemanager, '__focusEvent', id);
            //console.log('other events setup ', obj);
        }
    }
    ,setShowNativeContextMenu: function(id, show) {
        this.__hidenativecontextmenu[id] = ! show;
    }
    ,storeSelection: function(id) {
        var ifm = lz.embed.iframemanager;
        var win = ifm.getFrameWindow(id);
        if (win && win.document && win.document.selection && win.document.selection.type=="Text"){
            ifm.__selectionbookmarks[id] = win.document.selection.createRange().getBookmark();
        }
    }
    ,restoreSelection: function(id) {
        var ifm = lz.embed.iframemanager;
        var win = ifm.getFrameWindow(id);
        if (ifm.__selectionbookmarks[id] && win) {
            var bookmark = ifm.__selectionbookmarks[id];
            var range = win.document.body.createTextRange();
            range.moveToBookmark(bookmark);
            range.select();
        }
    }
    /* Called when the flash movie reloads.  Destroy all iframes and allow them to be recreated */
    ,__reset: function(appid) {
        var iframemanager = lz.embed.iframemanager;
        //if (! (typeof __owner == 'string')) return;
        if (iframemanager.__counter) {
            var owners = iframemanager.__ownerbyid;
            // Find frames by app id
            for (var id in owners) {
                //alert('destroy: ' + owners[id] + appid)
                if (appid === owners[id]) {
                    iframemanager.__destroy(id);
                }
            }
        }
    }
}

