 function loginBlock(ref){
  		             setTimeout(function(){
                    	ref.login();
                    }, 200);  		
  }
  function stepNavigateBackBlock(ref){
  		             setTimeout(function(){
                    	ref.stepNavigateBack();
                    }, 200);  		
  }
  
  function stepNavigateForwardBlock(ref){
  		             setTimeout(function(){
                    	ref.stepNavigateForward();
                    }, 200);  		
  }
  
  function navigateFromItemBlock(ref){
  		             setTimeout(function(){
                    	ref.navigateFromItem();
                    }, 200);  		
  }
  
  function checkTTSResponse(ref){
		            setTimeout(function(){
                   	ref.checkTextPlayed();
                   }, 500);  		
  }
  function stopTextReading(ref){
		            setTimeout(function(){
                   	ref.stopReading(false);
                   }, 200);  		
  }
 function setImageSize(divid, imgHeight, imgWidth) {
	//console.log("setImageSize************",divid,imgWidth,imgHeight)
	var elm = document.getElementById(divid);
	if(elm != null) {
		var imgElm = elm.getElementsByTagName("img");
		if(imgElm != null) {
			imgElm[0].setAttribute("width", imgWidth);
			imgElm[0].setAttribute("height", imgHeight);	
		}
	}					
 }

 
 var gDivid;
 var gImgWidth;
 var gImgHeight;
 
 function onImgZoomIn(divid, imgWidth, imgHeight) {
	gDivid = divid;
	gImgWidth = imgWidth;	
	gImgHeight = imgHeight;
 }
 
 function onImgZoomOut() {
	setImageSize(gDivid, gImgHeight, gImgWidth);
	gDivid = '';
	gImgWidth = 0;	
	gImgHeight = 0;
 }


function blockiPADUI(){
	$.blockUI({ message :'<img src="lps/resources/lps/includes/busy.gif" />', timeout :10000, fadeIn: 0, fadeOut: 0});
}

function blockUIforVersionCheck(){
	$.blockUI({ message :'<img src="lps/resources/lps/includes/busy_small.gif" />', css: {left:'395px', top:'290px'}});
}

function unblockiPADUI () {
	$.unblockUI();
}


function blockGuidedAccess(){
	$.blockGuidedUI();
}

function unblockGuidedAccess () {
	$.unblockGuidedUI();
}

 function closeMagnifier(){
    var olddiv = document.getElementById('mborder');
    if (olddiv && olddiv.parentNode && olddiv.parentNode.removeChild)
		olddiv.parentNode.removeChild(olddiv);
	$.closeMagnifier();
 }
 
 function setMagnifierContent () {
	console.log("setMagnifierHTML!!!!!");
     setTimeout(function(){
                $.setMagnifierHTML();
                }, 500);
	
 }
 
 function zoomEnableElement(id) {
                console.log("zoom button enabled");
                if ( document.getElementById(id) ) {
                    
                    document.getElementById(id).removeAttribute("disabled");
                    document.getElementById(id).setAttribute("enabled",'true');
                    
                }
            }

 function zoomDisableElement(id) {
     console.log("zoom button disabled");
                if ( document.getElementById(id) ) {
                    
                    document.getElementById(id).removeAttribute("enabled");
                    document.getElementById(id).setAttribute("disabled",'true');
                    
                    
                }
 }

 function trackCutOperation(ref){
 	$("textarea").bind('cut paste', function(e) { 
        setTimeout(function(){
                   textLength(ref);
                   }, 100);
        
 	});
 
 }

function textLength(ref){
    ref.outer.rich.answer.inp.getDisplayObject().setAttribute('name','multiText');
    ref.outer.rich.answer.inp.setAttribute('text',$('[name="multiText"]')[0].value);
    ref.outer.rich.answer.inp.getDisplayObject().setAttribute('name',null);
}

function disableAc(){
	jQuery('input[disabled]').css("color","#000");
}
