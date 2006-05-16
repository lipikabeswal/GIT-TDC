/**
 * js_web.js
 */

function checkForPCNotIE(){
    var result = false;
    
    var agt = navigator.userAgent.toLowerCase();
    var ie  = (agt.indexOf("msie") != -1);
    var win = ((agt.indexOf("win")!=-1) || (agt.indexOf("32bit")!=-1));
    
    if (win && !ie)
        result = true;
        
    return result;
}

function getElement(name, type){
    var result;
    var elements=document.getElementsByTagName(type);
    for(var i=0;i<elements.length;i++) { 
        var element = elements[i];
        if(element.id.indexOf(name) != -1){
            result = element;
            break;
        }
    }
    return result;
}

function mydetectIE(ClassID) { 
    document.write('<SCRIPT LANGUAGE=VBScript>\non error resume next\ndmInstalled = IsObject(CreateObject("' + ClassID + '"))\n</SCRIPT>\n'); 
}

function mydetectNS(ClassID) { 
    if (nse.indexOf(ClassID) != -1) 
        if (navigator.mimeTypes[ClassID].enabledPlugin != null) 
            dmInstalled = true;
}

function showElement(element){
    element.style.display="block";
}

function hideElement(element){
    element.style.display="none";
}

var myflashinstalled = 0;
var myflashversion = 0;
var myMSDetect = "false";

function mydetectFlash(){ 
    mydetectFlashUsingJavascript();
    if(myMSDetect == "true"){
        mydetectFlashUsingVBScript();
    }
    myflashversion = new Number(myflashversion);
}

function mydetectFlashUsingJavascript(){
    if (navigator.plugins && navigator.plugins.length)
    {
        navigator.plugins.refresh(true);
        x = navigator.plugins["Shockwave Flash"];
        if (x)
        {
            myflashinstalled = 2;
            if (x.description)
            {
                y = x.description;
                myflashversion = y.charAt(y.indexOf('.')-1);
            }
        }
        else
            myflashinstalled = 1;
        if (navigator.plugins["Shockwave Flash 2.0"])
        {
            myflashinstalled = 2;
            myflashversion = 2;
        }
    }
    else if (navigator.mimeTypes && navigator.mimeTypes.length)
    {
        x = navigator.mimeTypes['application/x-shockwave-flash'];
        if (x && x.enabledPlugin)
            myflashinstalled = 2;
        else
            myflashinstalled = 1;
    }
    else
        myMSDetect = "true";
}

function mydetectFlashUsingVBScript() { 
    document.write('<SCRIPT LANGUAGE="VBScript">on error resume next\nIf myMSDetect = "true" Then\nFor i = 2 to 8\nIf Not(IsObject(CreateObject("ShockwaveFlash.ShockwaveFlash." & i))) Then\nElse\nmyflashinstalled = 2\nmyflashversion = i\nEnd If\nNext\nEnd If\nIf myflashinstalled = 0 Then\nmyflashinstalled = 1\nEnd If</SCRIPT>'); 
}