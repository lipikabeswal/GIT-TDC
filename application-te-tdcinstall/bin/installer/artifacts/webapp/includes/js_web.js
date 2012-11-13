/**
 * js_web.js
 */

function showNeedFlash7(){
alert("start showNeedFlash7");
	document.write('<div id="needFlash7" style="display:none"><table height="100%" width="100%"><tr><td height="30%" width="100%" colspan="3">&nbsp;</td></tr><tr><td height="40%" width="20%">&nbsp;</td><td height="40%" width="60%" bgcolor="#527DA4"><table><tr><td valign="top" style="padding:10px"><font color="white" size="6" face="Arial">The test cannot start because this computer is missing Flash 7.</font><br/></td></tr><tr><td valign="top" style="padding:20px"><font color="white" size="5" face="Arial">Please show this screen to the person in charge of testing and request a Flash 7 installation.</font></td></td></tr><tr><td valign="bottom" style="padding:20px" width="100%" align="right"><input type="image" src="images/Cancel.gif" onclick="loginCancel();"/></td></tr></table></td><td height="40%" width="20%"></td></tr><tr><td height="10%" width="20%">&nbsp;</td><td height="10%" width="60%" valign="top"><font color="#527DA4" size="2" face="Arial">CTB/McGraw-Hill</font></td><td height="10%" width="20%"></td></tr><td height="20%" width="100%" colspan="3">&nbsp;</td></tr></div>');
alert("end showNeedFlash7");
}

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