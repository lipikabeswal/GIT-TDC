var gTransX;
var gTransY;


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
  executeRequest(method,xml,action);


}

function executeRequest(method,xml,action)
{
  if(method == "isRestart")
  {
    gCommunicator.finishCall("N");
  }
  else if(method == "updateRequired")
  {
    gCommunicator.finishCall("N");
  }
  else if(method == "isGuidedAccess")
  {
    gCommunicator.finishCall("Y");
  }
  else if(method == "isProductCheck")
  {
    gCommunicator.finishCall("ISTEP");
  }
  else if(method == "login")
  {
    login(xml);
  }
}


function ttsRequest(text,speedValue){
  //console.log("method : "+text+", xml_string :"+speedValue+",class_name: "+action);
  stopTTS();
  //text=escape(text);
  //sendRequest(text,speedValue,'SpeechAction');
  DelegatePlugin.ttsRequest(text,speedValue);
}

function isTextPlaying()
{
  console.log("Is Playing ???");
  DelegatePlugin.ttsIsPlaying();
}

function getItem(xmlString)
{
  //sendRequest('getItem',xmlString,'ContentAction');
  var jsonObj = ['{"method" : "getItem", "xml_string" :"'+xmlString+'","class_name" : "ContentAction"}'];
  DelegatePlugin.getItem(jsonObj);

}

function getImage(xmlString)
{
  //sendRequest('getItem',xmlString,'ContentAction');
  var jsonObj = ['{"method" : "getImage", "xml_string" :"'+xmlString+'","class_name" : "ContentAction"}'];
  DelegatePlugin.getImage(jsonObj);

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
  setTimeout(DelegatePlugin.checkForScreenshot(),1000);
}

function ping(){
  var method="login";
  var xml = "xml";
  // Android.doRequest(method,xml);
}

function vibrate() {
  navigator.notification.vibrate(2000);
}

</script>


  <SCRIPT LANGUAGE="JavaScript">
  /*
  function showFootnote(header)
     {
       alert(header);
       lzSetCanvasAttribute("footnotedata", header);
       //lz.setCanvasAttribute("footnotedata", header);
       }
  */

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

  //function sendRequest(method,xml,action){
    //	alert("button clicked"+method+xml+action);
    //}

  function checkStop(arg1,arg2,arg3){
    // alert("inside checkStop arg1="+arg1+" arg2="+arg2+" arg3="+arg3);
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

