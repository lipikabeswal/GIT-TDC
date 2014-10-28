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
     /*setTimeout(function(){
                $.setMagnifierHTML();
                }, 500);*/
	
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

// code placed here from login.html

function disableSpellCheck()
{
    var elm=document.getElementsByTagName("input");
    for (var i=0; i<elm.length; i++) {
        elm[i].setAttribute("autocapitalize","off");
        elm[i].setAttribute("autocorrect","off");
        elm[i].setAttribute("autocomplete","off");
    }
    elm=document.getElementsByTagName("textarea");
    for (var i=0; i<elm.length; i++) {
        elm[i].setAttribute("autocapitalize","off");
        elm[i].setAttribute("autocorrect","off");
        elm[i].setAttribute("autocomplete","off");
    }
}

function checkResponse(arg1,arg2,arg3){
}

var frameCalled = false;

function setFrameSource(filepath){
    console.log("inside setFrameSource");
    
    var iframe=$('iframe[name=htmlwigetframe]')
    
    console.log("inside setFrameSource iframe"+iframe);
    if(iframe){
        iframe.attr("src",filepath);
        gController.afterTEItemLoad();
        return true;
    }else{
        return false;
    }
    
}

function sendRequest(method,xml,action){
    console.log("method : "+method+", xml_string :"+xml+",class_name: "+action);
    var jsonObj = ['{"method" : "'+method+'", "xml_string" :"'+xml+'","class_name" : "'+action+'"}'];
    //DelegatePlugin.prototype.LOGIN (jsonObj);
}

function ttsRequest(text,speedValue){
    stopTTS();
    //DelegatePlugin.ttsRequest(text,speedValue);
}

function isTextPlaying()
{
    console.log("Is Playing ???");
    //DelegatePlugin.ttsIsPlaying();
}

function getItem(xmlString)
{
    var jsonObj = ['{"method" : "getItem", "xml_string" :"'+xmlString+'","class_name" : "ContentAction"}'];
    //DelegatePlugin.getItem(jsonObj);
    
}

function getImage(xmlString)
{
    var jsonObj = ['{"method" : "getImage", "xml_string" :"'+xmlString+'","class_name" : "ContentAction"}'];
    //DelegatePlugin.getImage(jsonObj);
    
}

function stopTTS()
{
    console.log("STOP TTS");
    sendRequest('','STOP_READ_TEXT','SpeechAction');
}

function saveRequest(xml_string){
    sendRequest('save',xml_string,'PersistenceAction');
}

function checkForScreenshotTaken()
{
    console.log("Checking for screenshot!!!!!!!!!!!!");
    //setTimeout(DelegatePlugin.checkForScreenshot(),1000);
}

function ping(){
    var method="login";
    var xml = "xml";
    // Android.doRequest(method,xml);
}

function vibrate() {
    navigator.notification.vibrate(2000);
}

function showFootnote(header)
{
    lz.embed.setCanvasAttribute("footnotedata", header);
}

function isMac(){
    return (window.navigator.platform.indexOf("Mac") != -1);
}

function closeBrowser()
{
    if(isMac()){
        window.close();
    }
    else{
        LDBJSBridgeCTL.closeLockdownBrowser();
    }
}


function swfCallback(isPlaying, id) {
    var attr = 'isMultiAudio=true,isPlaying='+isPlaying+',audioId='+id;
    lzSetCanvasAttribute('isMultiAudio',attr);
}

function updateLDB(){
    _LDB_BypassSetFocusOnPageLoad = 1;
}

function setResponse(result) {
    
    lz.embed.setCanvasAttribute("response",result);
}

function buttonClicked(magnifierPosition){
    if(magnifierPosition == "T") {
        $.setPreviousPosition();
    }
    document.getElementById('mybutton').click();
}

var _LDB_BypassSetFocusOnPageLoad = 0;
var spText;
var fontAccom;
var backColorString;
var itemSet = false;
var ansSet = false;
var readable = false;
var iid = null;


function iframeState1(){
    
    var iframe = $("iframe")[0];
    if(iframe){
        setAnswerNow('complete');
        var fontObj = getFontAccomodation();
        var bgColorObj = getBackColorAccomodation();
        
        if(iframe.contentWindow.accomPkg!= null && iframe.contentWindow.accomPkg!=undefined){
            /*if(fontObj.hasFontMag){
             iframe.contentWindow.accomPkg.setVisualAccessFeatures(fontObj.fgcolor, '18px',bgColorObj);
             }
             else {
             iframe.contentWindow.accomPkg.setVisualAccessFeatures(fontObj.fgcolor, '12px',bgColorObj);
             }*/
            
            iframe.contentWindow.accomPkg.setVisualAccessFeatures(fontObj.fgcolor, '12px',bgColorObj);
            //lz.embed.setCanvasAttribute('frameLoaded',true);
            gController.accomodationLoaded();
        }
        
	}
}


function iframeState(){
    setTimeout(function(){
               iframeState1();
               }, 300);
}

function destroyDiv(divElem) {
    var div = document.getElementById(divElem);
    if(div != null)
     	div.parentNode.removeChild(div);
}

function setAnswerNow(args){
	if(gController.htmlFields.length > 0){
		gController.htmlFields[0].ref.setAnswer();
	}
}
function freezeUI(){
    if(document.getElementById('__lz0')){
		document.getElementById('__lz0').contentWindow.freezeUI();
	}
}

function unlockUI(){
    if(document.getElementById('__lz0')){
		document.getElementById('__lz0').contentWindow.unlockUI();
	}
}
function getFontAccomodation(){
	var fontObj = new Object();
	var questionBgColor = gController.questionBgColor;
	var questionFontColor = gController.questionFontColor;
	
	if(typeof questionBgColor == "number"){
		questionBgColor = questionBgColor.toString(16);
		while(questionBgColor.length<6)
        {
            questionBgColor='0'+questionBgColor;
        }
		if(questionBgColor.indexOf("0x") < 0){
			questionBgColor = "0x"+ questionBgColor;
		}
	}
	
	if(typeof questionFontColor == "number"){
		questionFontColor = questionFontColor.toString(16);
		while(questionFontColor.length<6)
        {
            questionFontColor='0'+questionFontColor;
        }
		if(questionFontColor.indexOf("0x") < 0){
			questionFontColor = "0x"+ questionFontColor;
		}
	}
	fontObj.bgcolor = questionBgColor.replace('0x', '#');
	fontObj.fgcolor = questionFontColor.replace('0x', '#');
	fontObj.hasFontMag = gController.hasFontAccommodation;
	return fontObj;
}

function getBackColorAccomodation(){
	var bgColorObj = new Object();
	var questionBgColor = gController.questionBgColor;
	var answerBgColor = gController.answerBgColor;
	
	if(typeof questionBgColor == "number"){
		questionBgColor = questionBgColor.toString(16);
		while(questionBgColor.length<6)
        {
            questionBgColor='0'+questionBgColor;
        }
		if(questionBgColor.indexOf("0x") < 0){
			questionBgColor = "0x"+ questionBgColor;
		}
	}
	
	if(typeof answerBgColor == "number"){
		answerBgColor = answerBgColor.toString(16);
		while(answerBgColor.length<6)
        {
            answerBgColor='0'+answerBgColor;
        }
		if(answerBgColor.indexOf("0x") < 0){
			answerBgColor = "0x"+ answerBgColor;
		}
	}
	
	bgColorObj.stemArea = questionBgColor.replace('0x', '#');
	bgColorObj.responseArea = answerBgColor.replace('0x', '#');
	return bgColorObj;
}



function setFootnoteText(arg){
	lz.embed.setCanvasAttribute('footnotetext',arg);
}

function isAnswered(){
    var elem = $("iframe")[0];
	if(elem){
        if(elem.contentWindow.accomPkg)
            return elem.contentWindow.accomPkg.isItemAnswered();
    }
}

function getState(){
    var elem = $("iframe")[0];
    if(elem){
 		if(elem.contentWindow.accomPkg){
 			console.log("inside getState");
 			return elem.contentWindow.accomPkg.getState();
 		}
 	}
}

function setMagnifierIframeState()
{
    var widgetFrame=document.getElementsByName('htmlwigetframe')[0]; //get iframe
    var fontObj = getFontAccomodation();
    var bgColorObj = getBackColorAccomodation();
    if(widgetFrame)     //If iframe is present
    {
        
        var stateObj=widgetFrame.contentWindow.accomPkg.getState(); //get the state of the iframe
        var magnifiedIframe=$('.magnified_content').eq(0).find('iframe')[0]; //get iframe inside the magnifier
        if(magnifiedIframe) //check if iframe is present inside iframe
        {
            magnifiedIframe.contentWindow.accomPkg.setState(stateObj.htmlContent,stateObj.jsonContent,stateObj.checkedVals);
            /*if(fontObj.hasFontMag){
             magnifiedIframe.contentWindow.accomPkg.setVisualAccessFeatures(fontObj.fgcolor, '18px',bgColorObj);
             }
             else {
             magnifiedIframe.contentWindow.accomPkg.setVisualAccessFeatures(fontObj.fgcolor, '12px',bgColorObj);
             }*/
            magnifiedIframe.contentWindow.accomPkg.setVisualAccessFeatures(fontObj.fgcolor, '12px',bgColorObj);
            
        }
    }   
}


function setState(htmlContent,jsonContent,checkedVals){
 	var elem = $("iframe")[0];
 	if(elem){
 		if(elem.contentWindow){
            if(elem.contentWindow.accomPkg){
                elem.contentWindow.accomPkg.setState(htmlContent,jsonContent,checkedVals);
            }
        }
    }
}

function setItemId(itemId){
	iframeState();
   	if (itemSet == false){
	   	lz.embed.setCanvasAttribute('srcurl',itemId);
	   	iid = itemId;
	   	itemSet = true;
   	}
}

function setTTSText(text){
	console.log("setTTSText"+text);
	if(text && canvas.readable)
		gReadableText.read(text,false);
}

function enableHighlighter(isEnabled){
	var iframe = $("iframe")[0];
	if(iframe){
		if(iframe.contentWindow.accomPkg!= null && iframe.contentWindow.accomPkg!=undefined){
			iframe.contentWindow.accomPkg.enableHighlighter(isEnabled);
		}
	}
}

function enableEraser(isEnabled){
	var iframe = $("iframe")[0];
	if(iframe){
		if(iframe.contentWindow.accomPkg!= null && iframe.contentWindow.accomPkg!=undefined){
			iframe.contentWindow.accomPkg.enableEraser(isEnabled);
		}
	}
}

function replaceSpecialChar(text) {
	text = text.replace(/&quot;/g, '%22').replace(/&lt;/g, '%3C').replace(/&gt;/g, '%3E');
	return text;
}

function disableCopyPasteHeader(identity){
    $('#'+identity).css({'-webkit-user-select':'none','-webkit-touch-start':'none'});
    
}

function disableCopyPasteFooter(identity){
    $('#'+identity).css({'-webkit-user-select':'none','-webkit-touch-start':'none'});
}

function disableCopyPaste(identity){
    $('#'+identity).css({'-webkit-user-select':'none','-webkit-touch-start':'none'});
}
