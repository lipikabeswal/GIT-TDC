/*Image Power Zoomer v1.1 (June 18th, 2010)
* This notice must stay intact for usage 
* Author: Dynamic Drive at http://www.dynamicdrive.com/
* Visit http://www.dynamicdrive.com/ for full source code
*/

//June 18th, 10: Adds ability to specify a different, higher resolution version of the original image as the image shown inside the magnifying glass.

jQuery.noConflict()
var isDragging = 0;
var ddpowerzoomer={
	dsetting: {defaultpower:2, powerrange:[2,7], magnifiersize:[75, 75]},
	//mousewheelevt: (/Firefox/i.test(navigator.userAgent))? "DOMMouseScroll" : "mousewheel", //FF doesn't recognize mousewheel as of FF3.x
	$magnifier: {outer:null, inner:null, image:null},
	activeimage: null,
	imageRef:null,
	zommersettings:null,

	movemagnifier:function(e, moveBol, zoomdir){
		var activeimage=ddpowerzoomer.activeimage //get image mouse is currently over
		var activeimginfo=activeimage.info
		var coords=activeimginfo.coords //get offset coordinates of image relative to upper left corner of page
		var $magnifier=ddpowerzoomer.$magnifier
		var magdimensions=activeimginfo.magdimensions //get dimensions of magnifier
		var power=activeimginfo.power.current
		var powerrange=activeimginfo.power.range
		/*
		if(e != null){
			var x=e.pageX-coords.left //get x coords of mouse within image (where top corner of image is 0)	
		}else {
			var x= (document.body.clientWidth/2) + 200 + - coords.left //get x coords of mouse within image (where top corner of image is 0)
		}
		
		if(e != null){
			var y=e.pageY-coords.top
		}else {
			var y=(document.body.clientHeight/2)-coords.top
		}*/
		
		var x = jQuery("#magnifierWindow").offset().left - coords.left;
		
		var y = jQuery("#magnifierWindow").offset().top - coords.top ;
		
		if(y > (document.body.clientHeight)/2 && x < (document.body.clientWidth)/2) {
			y = y + 48;
			x = x + 8;
		} else if(y > (document.body.clientHeight)/2 && x > (document.body.clientWidth)/2) {
			y = y + 48;
			x = x + 115;
		} else if(y < (document.body.clientHeight)/2 && y > (document.body.clientHeight * .05) && x < (document.body.clientWidth)/2) {
			y = y + 15;
			x = x + 8;
		} else if (y < (document.body.clientHeight)/2 && y > (document.body.clientHeight * .05) && x > (document.body.clientWidth)/2) {
			y = y + 15;
			x = x + 115;
		} else if(y < (document.body.clientHeight * .05) && x > (document.body.clientWidth * 0.25)) {
			y = y + 7;
			x = x + 115;
		} else if(y < (document.body.clientHeight * .05) && x < (document.body.clientWidth * 0.25)) {
			y = y + 7;
			x = x + 8;
		} 
		 /*else if (y < (document.body.clientHeight * .05)) {
			x = x + 50;
		}*/
		
		/*if(x < (document.body.clientWidth)/2) {
			x = x + 8;
		}else if(x > (document.body.clientWidth)/2) {
			x = x + 8;
		}*/
		
		
		
		/*if (moveBol==true){
			if (e.pageX>=coords.left && e.pageX<=coords.right && e.pageY>=coords.top && e.pageY<=coords.bottom)  //if mouse is within currently within boundaries of active base image
				$magnifier.outer.css({left:e.pageX-magdimensions[0]/2, top:e.pageY-magdimensions[1]/2})	//move magnifier so it follows the cursor
			else{ //if mouse is outside base image
				ddpowerzoomer.activeimage=null
				$magnifier.outer.hide() //hide magnifier
			}
		}*/
		if (zoomdir){ //if zoom in
			var od=activeimginfo.dimensions //get dimensions of image
			var newpower=(zoomdir=="in")? Math.min(power+1, powerrange[1]) : Math.max(power-1, powerrange[0]) //get new power from zooming in or out
			var nd=[od[0]*newpower, od[1]*newpower] //calculate dimensions of new enlarged image within magnifier
			$magnifier.image.css({width:nd[0], height:nd[1]})
			activeimginfo.power.current=newpower //set current power to new power after magnification
		}
		power=activeimginfo.power.current //get current power
		var newx=-x*power//+magdimensions[0]/2 //calculate x coord to move enlarged image
		var newy=-y*power//+magdimensions[1]/2
	
		$magnifier.inner.css({left:newx, top:newy}) //move image wrapper within magnifier so the correct image area is shown
	},

	setupimage:function($, imgref, options){
		var zommersettings=jQuery.extend({}, ddpowerzoomer.dsetting, options)
		var imgref=imgref
		imgref.info={ //create object to remember various info regarding image 
			power: {current:zommersettings.defaultpower, range:zommersettings.powerrange},
			magdimensions: zommersettings.magnifiersize,
			dimensions: [$(imgref).width(), $(imgref).height()],
			coords: null
		}
		ddpowerzoomer.$imageRef = imgref
		ddpowerzoomer.$zommersettings = zommersettings
		ddpowerzoomer.init($)
		
	},

	
	init:function($){
		
		var $magnifier=$('<div style="position:absolute;z-index:1000000;width:'+parseInt(ddpowerzoomer.$zommersettings.magnifiersize[0])+ 'px;height:'+parseInt(ddpowerzoomer.$zommersettings.magnifiersize[1])+'px;display:none;" />')
			.append('<div  id="magnifierWindow" style="width:'+ddpowerzoomer.$zommersettings.magnifiersize[0]+ 'px;height:'+ddpowerzoomer.$zommersettings.magnifiersize[1]+'px;overflow:hidden; border:8px solid #333333;"><div style="position:relative;left:0;top:0;" /></div>')
			.appendTo(document.body).draggable({
			containment: [0, 0,document.body.offsetWidth - 316,$("body").height()-116],	
			cursorAt: {
				top: parseInt(ddpowerzoomer.$zommersettings.magnifiersize[1])/2,
				left: parseInt(ddpowerzoomer.$zommersettings.magnifiersize[0])/2
			},
			start: function(event,ui) {
			    //alert(window.isDragging);
			    if(isDragging == 0){
			    isDragging = 1;				
			    jQuery("#magnifierWindow").css('visibility','hidden');
				setTimeout(function(){jQuery.ajax({
					url: "servlet/PersistenceServlet.do?method=captureScreenshot",					
					dataType: "xml",
					success: function(data) {
					//xmlDoc = jQuery.parseXML( data ),
				    xml = jQuery( data );
				    ok = xml.find( "OK" );
					//alert(ok);
					var timeStamp = ok.text();//jQuery(jQuery.parseXML(data)).find("ok").text();
					//alert(timeStamp);
					var imageName = "cache/screenshot"+timeStamp+".png";
					jQuery(ddpowerzoomer).initMagnify({largeimage:imageName});
					jQuery("#magnifierWindow").find("img").attr("onmousedown","event.preventDefault();");
					jQuery("#magnifierWindow").css('visibility','visible');
					isDragging = 0;
					}
				})},35);
				}
			},
			drag: function(event, ui) {
			    if($.browser.safari){
			    	$('#magnifierWindow').css("cursor", "url('./assets/dragger_press.png'), pointer");
			    }else{
			    	$('#magnifierWindow').css( 'cursor', 'move' );
			    }
			    if(isMagnifierVisible == false || isMagnifierVisible == 'false') {
					ddpowerzoomer.$magnifier.outer.hide()
				}
				else {
					var $magnifier=ddpowerzoomer.$magnifier
					var s = ddpowerzoomer.$zommersettings
					$magnifier.outer.css({width:parseInt(s.magnifiersize[0]), height:parseInt(s.magnifiersize[1])}) //set magnifier's size
					var imgref = ddpowerzoomer.$imageRef;
					var offset= $(imgref).offset() //get image offset from document
					var power=imgref.info.power.current
					//$magnifier.inner.html('<img src="'+s.largeimagesrc+'"/>') //get base image's src and create new image inside magnifier based on it
					/*$magnifier.image=$magnifier.outer.find('img:first')
						.css({width:imgref.info.dimensions[0]*power, height:imgref.info.dimensions[1]*power})*/ //set size of enlarged image
					var coords={left:offset.left, top:offset.top, right:offset.left+imgref.info.dimensions[0], bottom:offset.top+imgref.info.dimensions[1]}
					imgref.info.coords=coords //remember left, right, and bottom right coordinates of image relative to doc
					$magnifier.outer.show()
					ddpowerzoomer.activeimage=imgref
					if (ddpowerzoomer.activeimage){ //if mouse is currently over a magnifying image
						ddpowerzoomer.movemagnifier(event, true) //move magnifier
					}
				}
			},
			stop: function(event, ui) {
				if(isMagnifierVisible == false || isMagnifierVisible == 'false') {
					ddpowerzoomer.$magnifier.outer.hide()
				}
				$('#magnifierWindow').css( 'cursor', 'pointer' );
			}
			
		}) //create magnifier container and add to doc
		ddpowerzoomer.$magnifier={outer:$magnifier, inner:$($magnifier.find('div:eq(0)')).find('div:eq(0)'), image:null} //reference and remember various parts of magnifier
		$magnifier=ddpowerzoomer.$magnifier
		
		$magnifier.outer.bind(ddpowerzoomer.mousewheelevt, function(e){ //bind mousewheel event to magnifier
			if (ddpowerzoomer.activeimage){
				var delta=e.originalEvent.detail? e.originalEvent.detail*(-120) : e.originalEvent.wheelDelta //delta returns +120 when wheel is scrolled up, -120 when scrolled down
				if (delta<=-120){ //zoom out
					ddpowerzoomer.movemagnifier(e, false, "out")
				}
				else{ //zoom in
					ddpowerzoomer.movemagnifier(e, false, "in")
				}
				e.preventDefault()
			}
		})
			
		
	}
} //ddpowerzoomer


jQuery.fn.addpowerzoom=function(options){
	var $=jQuery
	return this.each(function(){ //return jQuery obj
		 //skip to next matched element
		if (typeof options=="undefined")
			options={}
		if (options.largeimage && options.largeimage.length>0){ //preload large image?
			options.preloadimg=new Image()
			options.preloadimg.src=options.largeimage
		}
		var $imgref=$(this)
		options.largeimagesrc=(options.preloadimg)? options.preloadimg.src : $imgref.attr('src')
		ddpowerzoomer.setupimage($, this, options);
		
	})
}


jQuery.fn.initMagnify=function(options){
	var $=jQuery
		var $magnifier=ddpowerzoomer.$magnifier
		var s = ddpowerzoomer.$zommersettings
		ddpowerzoomer.$zommersettings.largeimagesrc = options.largeimage ;
		$magnifier.outer.css({width:parseInt(s.magnifiersize[0]), height:parseInt(s.magnifiersize[1])}) //set magnifier's size
		var imgref = ddpowerzoomer.$imageRef
		var offset= $(imgref).offset() //get image offset from document
		var power=imgref.info.power.current
		$magnifier.inner.html('<img src="'+options.largeimage+'"/>') //get base image's src and create new image inside magnifier based on it
		$magnifier.image=$magnifier.outer.find('img:first')
			.css({width:imgref.info.dimensions[0]*power, height:imgref.info.dimensions[1]*power}) //set size of enlarged image
		var coords={left:offset.left, top:offset.top, right:offset.left+imgref.info.dimensions[0], bottom:offset.top+imgref.info.dimensions[1]}
		imgref.info.coords=coords //remember left, right, and bottom right coordinates of image relative to doc
		$magnifier.outer.show()
		
		ddpowerzoomer.activeimage=imgref
		if (ddpowerzoomer.activeimage){ //if mouse is currently over a magnifying image
			ddpowerzoomer.movemagnifier(null, true) //move magnifier
		}

}