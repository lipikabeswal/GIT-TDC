//============Login.html scripts===============

var gTransX;
var gTransY;
var TEAssetPath = "http:items/";
function disableSpellCheck() {
  var elm = document.getElementsByTagName( "input" );
  for ( var i = 0; i < elm.length; i++ ) {
    elm[i].setAttribute( "autocapitalize", "off" );
    elm[i].setAttribute( "autocorrect", "off" );
    elm[i].setAttribute( "autocomplete", "off" );
  }
  elm = document.getElementsByTagName( "textarea" );
  for ( var i = 0; i < elm.length; i++ ) {
    elm[i].setAttribute( "autocapitalize", "off" );
    elm[i].setAttribute( "autocorrect", "off" );
    elm[i].setAttribute( "autocomplete", "off" );
  }
}

var frameCalled = false;

function setFrameSource( filepath ) {

  var iframe = $( 'iframe[name=htmlwigetframe]' )

  if ( iframe ) {
    iframe.attr( "src", filepath );
    gController.afterTEItemLoad();
    return true;
  } else {
    return false;
  }

}
function getTEAssetPath(){
	lz.embed.setCanvasAttribute("TEAssetPath", TEAssetPath);
}
function isAppPreviewer(){
	lz.embed.setCanvasAttribute("isPreviewer", false);
}

function showFootnote(header)
{
	lz.embed.setCanvasAttribute("footnotedata", header);
}

function isMac(){
	return (window.navigator.platform.indexOf("Mac") != -1);
}

<!-- following functions are using for scratchpad scrollbar -->

function hideScrollbar(arg){
    var scrid = document.getElementById(arg);
    scrid.setAttribute('style',"font-family: CTB; overflow-x: hidden; white-space: pre-wrap; overflow-y: scroll;");
}

function setScrollEdittextWidth(elmId,comWidth,fontSize,textFontColor){
    var scrid = document.getElementById(elmId);
    scrid.setAttribute('style','font-family: CTB; overflow-x: hidden; white-space: pre-wrap; overflow-y: scroll; color: '+textFontColor+'; font-size: '+fontSize+'; width: '+comWidth+'');
}

function setScrollEdittextHeight(elmId,comHeight,fontSize,textFontColor){
    var scrid = document.getElementById(elmId);
    scrid.setAttribute('style','font-family: CTB; overflow-x: hidden; white-space: pre-wrap; overflow-y: scroll; color: '+textFontColor+'; font-size: '+fontSize+'; height: '+comHeight+'');
}
function setCurLasItemId(id) {
    currentLasAssetItemId = id;
    assetCount = 0;
}
function showMagnify() {
    isMagnifierVisible = "true";
  /*  if(jQuery("#magnifierWindow").length < 1){
        jQuery('body').addpowerzoom({
            defaultpower: 1.5,
            powerrange: [1.5,2],
            largeimage: null,
            magnifiersize: [300,100]
        });
    }
    jQuery.ajax({
        url: "servlet/PersistenceServlet.do?method=captureScreenshot",
        beforeSend: function() {
            jQuery(ddpowerzoomer.$magnifier.outer).hide();
        },
        dataType: "xml",
        success: function(data) {
            //xmlDoc = jQuery.parseXML( data ),
            xml = jQuery( data );
            ok = xml.find( "OK" );
            //alert(ok);
            var timeStamp = ok.text();//jQuery(jQuery.parseXML(data)).find("ok").text();
            //alert(timeStamp);
            var imageName = "cache/screenshot"+timeStamp+".png"

            jQuery(ddpowerzoomer.$magnifier.outer).css('left',(document.body.clientWidth/2 - 165)+ 'px');
            jQuery(ddpowerzoomer.$magnifier.outer).css('top',(document.body.clientHeight/2 - 75) + 'px');
            jQuery(ddpowerzoomer.$magnifier.outer).show();
            jQuery(ddpowerzoomer).initMagnify({largeimage:imageName});
        }
    });*/


}

function hideMagnify() {
    isMagnifierVisible = "false";
  //  jQuery(ddpowerzoomer.$magnifier.outer).hide();
}

/* Method to reinstate the magnifier to previous state */
function displayMagnifier() {

    isMagnifierVisible = "true";
    //jQuery(ddpowerzoomer.$magnifier.outer).show();
}


function setMouseUpVal() {
    lz.embed.setCanvasAttribute('mouseUpFired','false');
}

<!-- following functions are using for CR item scrolledittext scrollbar -->

function hideScrollbarForCRitem(arg){
    var crItemid = document.getElementById(arg);
    var fontcolor = getCRcolor();
    crItemid.setAttribute('style','overflow-x: hidden; overflow-y: scroll; color: '+fontcolor+';');
}
function setCRscrollEdittextWidth(elmId,comWidth){
    var crItemid = document.getElementById(elmId);
    var fontcolor = getCRcolor();
    crItemid.setAttribute('style','font-family: CTB;overflow-x: hidden; overflow-y: scroll; color: '+fontcolor+'; width: '+comWidth+'');
}
function setCRscrollEdittextHeight(elmId,comHeight){
    var crItemid = document.getElementById(elmId);
    var fontcolor = getCRcolor();
    crItemid.setAttribute('style','font-family: CTB;overflow-x: hidden; overflow-y: scroll; color: '+fontcolor+'; height: '+comHeight+'');
}

function getCRcolor(){
    var fontcolor;
    var answerFontColor = gController.answerFontColor;
    if(Number(answerFontColor) == 0)
        answerFontColor = "0x000000";
    if(answerFontColor.indexOf('0x') != -1) {
        fontcolor = answerFontColor.replace('0x', '#');
    }
    else {
        fontcolor = answerFontColor;
    }
    return fontcolor;
}

function checkTextDragging(){
    jQuery("textarea").on("dragstart", function(e) {
        e.preventDefault();
    });
}
<!-- End CR item scrolledittext scrollbar functions -->

<!-- following functions are using for scratchpad scrollbar -->

function hideScrollbar(arg){
    var scrid = document.getElementById(arg);
    scrid.setAttribute('style',"font-family: CTB; overflow-x: hidden; white-space: pre-wrap; overflow-y: scroll;");
}

function setScrollEdittextWidth(elmId,comWidth,fontSize,textFontColor){
    var scrid = document.getElementById(elmId);
    scrid.setAttribute('style','font-family: CTB; overflow-x: hidden; white-space: pre-wrap; overflow-y: scroll; color: '+textFontColor+'; font-size: '+fontSize+'; width: '+comWidth+'');
}

function setScrollEdittextHeight(elmId,comHeight,fontSize,textFontColor){
    var scrid = document.getElementById(elmId);
    scrid.setAttribute('style','font-family: CTB; overflow-x: hidden; white-space: pre-wrap; overflow-y: scroll; color: '+textFontColor+'; font-size: '+fontSize+'; height: '+comHeight+'');
}

<!-- End scratchpad scrollbar functions-->

function addReadOnlyCR(){
    if(document.getElementsByTagName('textarea').length > 0){
        document.getElementsByTagName('textarea')[0].setAttribute('readonly',true);
    }

}
function removeReadOnlyCR(){
    if(document.getElementsByTagName('textarea').length > 0){
        document.getElementsByTagName('textarea')[0].removeAttribute('readonly');
    }
}

function checkEmptyString(str) {
    return (!str || /^\s*$/.test(str) || /^\n*$/.test(str) || /^\r*$/.test(str));
}
function setScaleFactor(xscalefactor,yscalefactor){
	if(!xscalefactor){
    	xscalefactorjs = 1.2;
    }else{
    	xscalefactorjs = xscalefactor;
    }
    if(!yscalefactor){
    	yscalefactorjs = 1.2;
    }else{
    	yscalefactorjs = yscalefactor;
    }
}


function sendRequest( method, xml, action ) {
  console.log( "method : " + method + ", xml_string :" + xml + ",class_name: " + action );
  var jsonObj = ['{"method" : "' + method + '", "xml_string" :"' + xml + '","class_name" : "' + action + '"}'];
  //DelegatePlugin.prototype.LOGIN (jsonObj);
  executeRequest( method, xml, action );


}

function executeRequest( method, xml, action ) {
  if ( method == "isRestart" ) {
    gCommunicator.finishCall( "N" );
  }
  else if ( method == "updateRequired" ) {
    gCommunicator.finishCall( "N" );
  }
  else if ( method == "isGuidedAccess" ) {
    gCommunicator.finishCall( "Y" );
  }
  else if ( method == "isProductCheck" ) {
    gCommunicator.finishCall( "ISTEP" );
  }
  else if ( method == "login" ) {
    //login(xml);
    console.log( "inside login call---->>>>" )
    sendMsgToApp( method, xml );
  }
  else if ( method == "save" ) {
    console.log( "inside save call---->>>>" )
    sendMsgToApp( method, xml );
  } else if ( method == "getSubtest" ) {
      console.log( "inside getSubtest call---->>>>" )
      sendMsgToApp( method, xml );
  } else if ( method == "downloadFileParts" ) {
      gCommunicator.finishCall("<FILE_PART_OK />"); //static response
  } else if ( method == "downloadItem" ) {
      sendMsgToApp( method, xml );
  } else if ( method == "getItem" ) {
      sendMsgToApp( method, xml );
  } else if ( method == "getImage" ) {
      sendMsgToApp( method, xml );
  }

}


function ttsRequest( text, speedValue ) {
  //console.log("method : "+text+", xml_string :"+speedValue+",class_name: "+action);
  stopTTS();
  //text=escape(text);
  //sendRequest(text,speedValue,'SpeechAction');
  DelegatePlugin.ttsRequest( text, speedValue );
}

function isTextPlaying() {
  console.log( "Is Playing ???" );
  DelegatePlugin.ttsIsPlaying();
}

var responseQueue = [];
var eventCompleted = true;


function sendResponse(resp){
    console.log("sendResponse : "+resp.length);
    gCommunicator.finishCall(resp);
    eventCompleted = false;
    console.log("*******Sending Response Alternate***********: " + resp.length);
}
function setEventCompleted(){
    console.log("setEventCompleted Response queue length : "+responseQueue.length);
    //eventCompleted = true;
    if(responseQueue.length>0){
        var resp = responseQueue.shift();
        console.log("SET EVENT COMPLETED: " + resp.length);
        sendResponse(resp);
    }
}

function setContentResponse(result){
    console.log("Result : "+result);
    if(result.indexOf("||") >  -1){
        console.log("*******Queue Result***********");
        responseQueue.push(result);
    }else{
        console.log("*******Setting Result***********");
        gCommunicator.finishCall(result);
    }


    /*  gCommunicator.finishCall(result);
     //lz.embed.setCanvasAttribute("response", result);
     console.log("Response has been set");*/

};
function getItem( xmlString ) {
  sendRequest('getItem',xmlString,'ContentAction');
  //var jsonObj = ['{"method" : "getItem", "xml_string" :"' + xmlString + '","class_name" : "ContentAction"}'];
  //DelegatePlugin.getItem( jsonObj );

}

function getImage( xmlString ) {
  sendRequest('getImage',xmlString,'ContentAction');
//  var jsonObj = ['{"method" : "getImage", "xml_string" :"' + xmlString + '","class_name" : "ContentAction"}'];
//  DelegatePlugin.getImage( jsonObj );

}

function stopTTS() {
  console.log( "STOP TTS" );
  sendRequest( '', 'STOP_READ_TEXT', 'SpeechAction' );
}

function saveRequest( xml_string ) {
  sendRequest( 'save', xml_string, 'PersistenceAction' );
}

function checkForScreenshotTaken() {
  console.log( "Checking for screenshot!!!!!!!!!!!!" );
  //setTimeout( DelegatePlugin.checkForScreenshot(), 1000 );
}

function ping() {
  var method = "login";
  var xml = "xml";
  // Android.doRequest(method,xml);
}

function vibrate() {
  navigator.notification.vibrate( 2000 );
}


/*
 function showFootnote(header)
 {
 alert(header);
 lzSetCanvasAttribute("footnotedata", header);
 //lz.setCanvasAttribute("footnotedata", header);
 }
 */

function showFootnote( header ) {
  lz.embed.setCanvasAttribute( "footnotedata", header );
}

function isMac() {
  return (window.navigator.platform.indexOf( "Mac" ) != -1);
}

function closeBrowser() {
  if ( isMac() ) {
    window.close();
  }
  else {
    LDBJSBridgeCTL.closeLockdownBrowser();
  }
}


function swfCallback( isPlaying, id ) {
  var attr = 'isMultiAudio=true,isPlaying=' + isPlaying + ',audioId=' + id;
  lzSetCanvasAttribute( 'isMultiAudio', attr );
}

function updateLDB() {
  _LDB_BypassSetFocusOnPageLoad = 1;
}

//function sendRequest(method,xml,action){
//	alert("button clicked"+method+xml+action);
//}

function checkStop( arg1, arg2, arg3 ) {
  // alert("inside checkStop arg1="+arg1+" arg2="+arg2+" arg3="+arg3);
}

function setResponse( result ) {

  lz.embed.setCanvasAttribute( "response", result );
}

function buttonClicked( magnifierPosition ) {
  if ( magnifierPosition == "T" ) {
    $.setPreviousPosition();
  }
  document.getElementById( 'mybutton' ).click();
}


var _LDB_BypassSetFocusOnPageLoad = 0;
var spText;
var fontAccom;
var backColorString;
var itemSet = false;
var ansSet = false;
var readable = false;
var iid = null;

function checkVals( arg1, arg2, arg3 ) {
  // alert("inside checkVals "+arg1+arg2+arg3);
}

function checkQues( arg1, arg2, arg3 ) {
  // alert("inside checkQues "+arg1+arg2+arg3);
}

function iframeState( id ) {
  console.log( "iframeState1****" );

  var iframe = $( "iframe" )[0];
  if ( iframe ) {
    console.log( "if iframe****" );
    setAnswerNow( 'complete' );
    //check whether iFrame loaded successfully or not
    /* if(iframe != null
     && iframe.contentWindow != null
     && typeof iframe.contentWindow.accomPkg != 'undefined') {
     console.log("if");
     //iframeLoaded = true;
     }*/
    console.log( "after set answer now****" );
    var fontObj = getFontAccomodation();
    var bgColorObj = getBackColorAccomodation();

    if ( iframe.contentWindow.accomPkg != null && iframe.contentWindow.accomPkg != undefined ) {
      /*if(fontObj.hasFontMag){
       iframe.contentWindow.accomPkg.setVisualAccessFeatures(fontObj.fgcolor, '18px',bgColorObj);
       }
       else {
       iframe.contentWindow.accomPkg.setVisualAccessFeatures(fontObj.fgcolor, '12px',bgColorObj);
       }*/
      //sending the screen_reader accomodation value to TE item
      iframe.contentWindow.isReadable( canvas.readable );
      //sending the font color and font size accomodation details to TE item
      iframe.contentWindow.accomPkg.setVisualAccessFeatures( fontObj.fgcolor, '12px', bgColorObj );
      //scaling the TE item to fit the preview area and to allow all related (DND, higlight etc) functions to work properly
      var xscalefact = 780 / 800;
      var yscalefact = 450 / 462;
      iframe.contentWindow.translate( xscalefact, yscalefact );
      //added to set TE item id in scoring JSON.
      if ( iframe.contentWindow.getItemId ) {
        iframe.contentWindow.getItemId( id );
      }
      lz.embed.setCanvasAttribute( 'frameLoaded', true );
    }

  }
}


function iframeState1() {
  setTimeout( function() {
    iframeState();
  }, 300 );
}

function checkHtml( arg1, arg2 ) {
  // alert(arg1+arg2);
}

function destroyDiv( divElem ) {
  var div = document.getElementById( divElem );
  if ( div != null )
    div.parentNode.removeChild( div );
}

function setAnswerNow( args ) {
  //lz.embed.setCanvasAttribute('setanswer',args);
  if ( gController.htmlFields.length > 0 ) {
    gController.htmlFields[0].ref.setAnswer();
  }
}
function freezeUI() {
  if ( document.getElementById( '__lz0' ) ) {
    document.getElementById( '__lz0' ).contentWindow.freezeUI();
  }
}

function unlockUI() {
  if ( document.getElementById( '__lz0' ) ) {
    document.getElementById( '__lz0' ).contentWindow.unlockUI();
  }
}
function getFontAccomodation() {
  var fontObj = new Object();
  var questionBgColor = gController.questionBgColor;
  var questionFontColor = gController.questionFontColor;

  if ( typeof questionBgColor == "number" ) {
    questionBgColor = questionBgColor.toString( 16 );
    while ( questionBgColor.length < 6 ) {
      questionBgColor = '0' + questionBgColor;
    }
    if ( questionBgColor.indexOf( "0x" ) < 0 ) {
      questionBgColor = "0x" + questionBgColor;
    }
  }

  if ( typeof questionFontColor == "number" ) {
    questionFontColor = questionFontColor.toString( 16 );
    while ( questionFontColor.length < 6 ) {
      questionFontColor = '0' + questionFontColor;
    }
    if ( questionFontColor.indexOf( "0x" ) < 0 ) {
      questionFontColor = "0x" + questionFontColor;
    }
  }
  fontObj.bgcolor = questionBgColor.replace( '0x', '#' );
  fontObj.fgcolor = questionFontColor.replace( '0x', '#' );
  fontObj.hasFontMag = gController.hasFontAccommodation;
  return fontObj;
}

function getBackColorAccomodation() {
  var bgColorObj = new Object();
  var questionBgColor = gController.questionBgColor;
  var answerBgColor = gController.answerBgColor;

  if ( typeof questionBgColor == "number" ) {
    questionBgColor = questionBgColor.toString( 16 );
    while ( questionBgColor.length < 6 ) {
      questionBgColor = '0' + questionBgColor;
    }
    if ( questionBgColor.indexOf( "0x" ) < 0 ) {
      questionBgColor = "0x" + questionBgColor;
    }
  }

  if ( typeof answerBgColor == "number" ) {
    answerBgColor = answerBgColor.toString( 16 );
    while ( answerBgColor.length < 6 ) {
      answerBgColor = '0' + answerBgColor;
    }
    if ( answerBgColor.indexOf( "0x" ) < 0 ) {
      answerBgColor = "0x" + answerBgColor;
    }
  }

  bgColorObj.stemArea = questionBgColor.replace( '0x', '#' );
  bgColorObj.responseArea = answerBgColor.replace( '0x', '#' );
  return bgColorObj;
}


function setFootnoteText( arg ) {
  lz.embed.setCanvasAttribute( 'footnotetext', arg );
}

function isAnswered() {
  var elem = $( "iframe" )[0];
  if ( elem ) {
    if ( elem.contentWindow.accomPkg )
      return elem.contentWindow.accomPkg.isItemAnswered();
  }
}

function getState() {
  var elem = $( "iframe" )[0];
  if ( elem ) {
    if ( elem.contentWindow.accomPkg ) {
      console.log( "inside getState" );
      return elem.contentWindow.accomPkg.getState();
    }
  }
}

function setMagnifierIframeState() {
  var widgetFrame = document.getElementsByName( 'htmlwigetframe' )[0]; //get iframe
  var fontObj = getFontAccomodation();
  var bgColorObj = getBackColorAccomodation();
  if ( widgetFrame )     //If iframe is present
  {

    var stateObj = widgetFrame.contentWindow.accomPkg.getState(); //get the state of the iframe
    var magnifiedIframe = $( '.magnified_content' ).eq( 0 ).find( 'iframe' )[0]; //get iframe inside the magnifier
    if ( magnifiedIframe ) //check if iframe is present inside iframe
    {
      magnifiedIframe.contentWindow.accomPkg.setState( stateObj.htmlContent, stateObj.jsonContent, stateObj.checkedVals );
      /*if(fontObj.hasFontMag){
       magnifiedIframe.contentWindow.accomPkg.setVisualAccessFeatures(fontObj.fgcolor, '18px',bgColorObj);
       }
       else {
       magnifiedIframe.contentWindow.accomPkg.setVisualAccessFeatures(fontObj.fgcolor, '12px',bgColorObj);
       }*/
      magnifiedIframe.contentWindow.accomPkg.setVisualAccessFeatures( fontObj.fgcolor, '12px', bgColorObj );

    }

  }
  //$('.magnified_content').eq(0).find('iframe')[0].contentWindow.accomPkg.setState(stateObj.htmlContent,stateObj.jsonContent,stateObj.checkedVals);


}


function setState( htmlContent, jsonContent, checkedVals ) {
  var elem = $( "iframe" )[0];
  if ( elem ) {
    if ( elem.contentWindow ) {
      if ( elem.contentWindow.accomPkg ) {
        elem.contentWindow.accomPkg.setState( htmlContent, jsonContent, checkedVals );
        //ansSet = true;
      }
    }
  }
}

function setItemId( itemId ) {
  //console.log("itemSet", itemSet);
  iframeState();
  if ( itemSet == false ) {
    lz.embed.setCanvasAttribute( 'srcurl', itemId );
    iid = itemId;
    itemSet = true;
  }
}

function setTTSText( text ) {
  console.log( "setTTSText" + text );
  if ( text && canvas.readable )
    gReadableText.read( text, false );
}

function enableHighlighter( isEnabled ) {
  var iframe = $( "iframe" )[0];
  if ( iframe ) {
    if ( iframe.contentWindow.accomPkg != null && iframe.contentWindow.accomPkg != undefined ) {
      iframe.contentWindow.accomPkg.enableHighlighter( isEnabled );
    }
  }
}

function enableEraser( isEnabled ) {
  var iframe = $( "iframe" )[0];
  if ( iframe ) {
    if ( iframe.contentWindow.accomPkg != null && iframe.contentWindow.accomPkg != undefined ) {
      iframe.contentWindow.accomPkg.enableEraser( isEnabled );
    }
  }
}

function replaceSpecialChar( text ) {
  text = text.replace( /&quot;/g, '%22' ).replace( /&lt;/g, '%3C' ).replace( /&gt;/g, '%3E' );
  return text;
}

function disableCopyPasteHeader( identity ) {
  $( '#' + identity ).css( {'-webkit-user-select': 'none', '-webkit-touch-start': 'none'} );

}

function disableCopyPasteFooter( identity ) {
  $( '#' + identity ).css( {'-webkit-user-select': 'none', '-webkit-touch-start': 'none'} );
}

function disableCopyPaste( identity ) {
  $( '#' + identity ).css( {'-webkit-user-select': 'none', '-webkit-touch-start': 'none'} );
}
function embedLaszlo() {

  lz.embed.dhtml( {url: 'TestClient.lzx.js?servletUrl=/servlet', lfcurl: 'lps/includes/lfc/LFCdhtml.js', serverroot: 'lps/resources/', bgcolor: '#6691b4', width: '100%', height: '600', id: 'lzapp', accessible: 'true', cancelmousewheel: false, cancelkeyboardcontrol: false, skipchromeinstall: false, usemastersprite: false, approot: '', appenddivid: 'appcontainer'} );
  lz.embed.applications.lzapp.onload = function loaded() {
    // called when this application is done loading
    var el = document.getElementById( 'lzsplash' );
    if ( el.parentNode ) {
      el.parentNode.removeChild( el );
    }
  }

}

