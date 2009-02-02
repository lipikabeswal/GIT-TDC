var $dhtml=true;var $as3=false;var $js1=true;var $swf9=false;var $swf7=false;var $profile=false;var $swf8=false;var $runtime="dhtml";var $svg=false;var $as2=false;var $debug=false;var $j2me=false;try{
if(lz){

}}
catch(e){
lz={}};lz.embed={options:{},swf:function($1,$2){
if($2==null){
$2=8
};var $3=$1.url;var $4=$3.indexOf("lzr=swf");if($4==-1){
$3+="&lzr=swf8";$4=$3.indexOf("lzr=swf")
};$4+=7;var $5=$3.substring($4,$4+1)*1;if(lz.embed.dojo.info.commVersion>$5){
$3=$3.substring(0,$4)+lz.embed.dojo.info.commVersion+$3.substring($4+1,$3.length);$2=lz.embed.dojo.info.commVersion
}else{
if(lz.embed.dojo.info.commVersion<=7&&$5>7){
lz.embed.dojo.info.commVersion=8
}};if($5>$2){
$2=$5
};var $6=this.__getqueryurl($3);if($1.accessible=="true"){
$6.flashvars+="&accessible=true"
};if($1.bgcolor!=null){
$6.flashvars+="&bgcolor="+escape($1.bgcolor)
};$6.flashvars+="&width="+escape($1.width);$6.flashvars+="&height="+escape($1.height);$6.flashvars+="&__lzurl="+escape($3);$6.flashvars+="&__lzminimumversion="+escape($2);$6.flashvars+="&id="+escape($1.id);var $3=$6.url+"?"+$6.query;var $7={width:$1.width+"",height:$1.height+"",id:$1.id,bgcolor:$1.bgcolor,wmode:$1.wmode,flashvars:$6.flashvars,flash6:$3,flash8:$3,appenddiv:lz.embed._getAppendDiv($1.id,$1.appenddivid)};if(lz.embed[$1.id]){
alert("Warning: an app with the id: "+$1.id+" already exists.")
};var $8=lz.embed[$1.id]=lz.embed.applications[$1.id]={runtime:"swf",_id:$1.id,setCanvasAttribute:lz.embed._setCanvasAttributeSWF,getCanvasAttribute:lz.embed._getCanvasAttributeSWF,callMethod:lz.embed._callMethodSWF,_ready:lz.embed._ready,_onload:[],_getSWFDiv:lz.embed._getSWFDiv,loaded:false,_sendMouseWheel:lz.embed._sendMouseWheel,_setCanvasAttributeDequeue:lz.embed._setCanvasAttributeDequeue};if($1.history!=false){
$8._onload.push(lz.embed.history.init)
};lz.embed.dojo.addLoadedListener(lz.embed._loaded,$8);lz.embed.dojo.setSwf($7,$2);if($1.cancelmousewheel!=true&&(lz.embed.browser.OS=="Mac"||($7.wmode=="transparent"||$7.wmode=="opaque")&&lz.embed.browser.OS=="Windows"&&(lz.embed.browser.isOpera||lz.embed.browser.isFirefox))){
if(lz.embed["mousewheel"]){
lz.embed.mousewheel.setCallback($8,"_sendMouseWheel")
}}},lfc:function($1,$2){
if(!$2||typeof $2!="string"){
alert("WARNING: lfc requires resourceroot to be specified.");return
};lz.embed.options.resourceroot=$2;if(lz.embed.browser.isIE){
var $3=$2+"/lps/includes/excanvas.js";this.__dhtmlLoadScript($3)
};if(lz.embed.browser.isIE&&lz.embed.browser.version<7||lz.embed.browser.isSafari&&lz.embed.browser.version<=419.3){
var $4=$1.indexOf("debug.js")||$1.indexOf("backtrace.js");if($4!=-1){
var $5=$1.substring($4,$1.length-3);$1=$1.substring(0,$4)+$5+"-simple.js"
}};this.__dhtmlLoadScript($1)
},dhtml:function($1){
var $2=this.__getqueryurl($1.url,true);var $3=$2.url+"?lzt=object&"+$2.query;lz.embed.__propcache={bgcolor:$1.bgcolor,width:$1.width.indexOf("%")==-1?$1.width+"px":$1.width,height:$1.height.indexOf("%")==-1?$1.height+"px":$1.height,id:$1.id,appenddiv:lz.embed._getAppendDiv($1.id,$1.appenddivid),url:$3,cancelkeyboardcontrol:$1.cancelkeyboardcontrol,resourceroot:$1.resourceroot};if(lz.embed[$1.id]){
alert("Warning: an app with the id: "+$1.id+" already exists.")
};var $4=lz.embed[$1.id]=lz.embed.applications[$1.id]={runtime:"dhtml",_id:$1.id,_ready:lz.embed._ready,_onload:[],loaded:false,setCanvasAttribute:lz.embed._setCanvasAttributeDHTML,getCanvasAttribute:lz.embed._getCanvasAttributeDHTML};if($1.history!=false){
$4._onload.push(lz.embed.history.init)
};this.__dhtmlLoadScript($3)
},applications:{},__dhtmlLoadScript:function($1){
var $2='<script type="text/javascript" language="JavaScript1.5" src="'+$1+'"></script>';document.writeln($2);return $2
},__dhtmlLoadLibrary:function($1){
var $2=document.createElement("script");this.__setAttr($2,"type","text/javascript");this.__setAttr($2,"src",$1);document.getElementsByTagName("head")[0].appendChild($2);return $2
},__getqueryurl:function($1,$2){
var $3=$1.split("?");$1=$3[0];if($3.length==1){
return {url:$1,flashvars:"",query:""}};var $4=this.__parseQuery($3[1]);var $5="";var $6="";var $7=new RegExp("\\+","g");for(var $8 in $4){
if($8==""||$8==null){
continue
};var $9=$4[$8];if($8=="lzr"||$8=="lzt"||$8=="krank"||$8=="debug"||$8=="profile"||$8=="lzbacktrace"||$8=="lzdebug"||$8=="lzkrank"||$8=="lzprofile"||$8=="fb"||$8=="sourcelocators"||$8=="_canvas_debug"){
$5+=$8+"="+$9+"&"
};if($2){
if(window[$8]==null){
window[$8]=unescape($9.replace($7," "))
}};$6+=$8+"="+$9+"&"
};$5=$5.substr(0,$5.length-1);$6=$6.substr(0,$6.length-1);return {url:$1,flashvars:$6,query:$5}},__parseQuery:function($1){
if($1.indexOf("=")==-1){
return
};var $2=$1.split("&");var $3={};for(var $4=0;$4<$2.length;$4++){
var $5=$2[$4].split("=");if($5.length==1){
continue
};var $6=$5[0];var $7=$5[1];$3[$6]=$7
};return $3
},__setAttr:function($1,$2,$3){
$1.setAttribute($2,$3)
},_setCanvasAttributeSWF:function($1,$2,$3){
if(this.loaded&&lz.embed.dojo.comm[this._id]&&lz.embed.dojo.comm[this._id]["callMethod"]){
if($3){
lz.embed.history._store($1,$2)
}else{
lz.embed.dojo.comm[this._id].setCanvasAttribute($1,$2+"")
}}else{
if(this._setCanvasAttributeQ==null){
this._setCanvasAttributeQ=[[$1,$2,$3]]
}else{
this._setCanvasAttributeQ.push([$1,$2,$3])
}}},_setCanvasAttributeDHTML:function($1,$2,$3){
if($3){
lz.embed.history._store($1,$2)
}else{
if(canvas){
canvas.setAttribute($1,$2)
}}},_loaded:function($1){
if(lz.embed[$1].loaded){
return
};if(lz.embed.dojo.info.commVersion==8){
setTimeout('lz.embed["'+$1+'"]._ready.call(lz.embed["'+$1+'"])',100)
}else{
lz.embed[$1]._ready.call(lz.embed[$1])
}},_setCanvasAttributeDequeue:function(){
while(this._setCanvasAttributeQ.length>0){
var $1=this._setCanvasAttributeQ.pop();this.setCanvasAttribute($1[0],$1[1],$1[2])
}},_ready:function($1){
this.loaded=true;if(this._setCanvasAttributeQ){
this._setCanvasAttributeDequeue()
};if($1){
this.canvas=$1
};for(var $2=0;$2<this._onload.length;$2++){
var $3=this._onload[$2];if(typeof $3=="function"){
$3(this)
}};if(this.onload&&typeof this.onload=="function"){
this.onload(this)
}},_getCanvasAttributeSWF:function($1){
if(this.loaded){
return lz.embed.dojo.comm[this._id].getCanvasAttribute($1)
}else{
alert("Flash is not ready: getCanvasAttribute"+$1)
}},_getCanvasAttributeDHTML:function($1){
return canvas[$1]
},browser:{init:function(){
if(this.initted){
return
};this.browser=this.searchString(this.dataBrowser)||"An unknown browser";this.version=this.searchVersion(navigator.userAgent)||this.searchVersion(navigator.appVersion)||"an unknown version";this.OS=this.searchString(this.dataOS)||"an unknown OS";this.initted=true;if(this.browser=="Netscape"){
this.isNetscape=true
}else{
if(this.browser=="Safari"){
this.isSafari=true
}else{
if(this.browser=="Opera"){
this.isOpera=true
}else{
if(this.browser=="Firefox"){
this.isFirefox=true
}else{
if(this.browser=="Explorer"){
this.isIE=true
}}}}}},searchString:function($1){
for(var $2=0;$2<$1.length;$2++){
var $3=$1[$2].string;var $4=$1[$2].prop;this.versionSearchString=$1[$2].versionSearch||$1[$2].identity;if($3){
if($3.indexOf($1[$2].subString)!=-1){
return $1[$2].identity
}}else{
if($4){
return $1[$2].identity
}}}},searchVersion:function($1){
var $2=$1.indexOf(this.versionSearchString);if($2==-1){
return
};return parseFloat($1.substring($2+this.versionSearchString.length+1))
},dataBrowser:[{string:navigator.userAgent,subString:"Apple",identity:"Safari",versionSearch:"WebKit"},{prop:window.opera,identity:"Opera"},{string:navigator.vendor,subString:"iCab",identity:"iCab"},{string:navigator.vendor,subString:"KDE",identity:"Konqueror"},{string:navigator.userAgent,subString:"Firefox",identity:"Firefox"},{string:navigator.userAgent,subString:"Iceweasel",versionSearch:"Iceweasel",identity:"Firefox"},{string:navigator.userAgent,subString:"Netscape",identity:"Netscape"},{string:navigator.userAgent,subString:"MSIE",identity:"Explorer",versionSearch:"MSIE"},{string:navigator.userAgent,subString:"Gecko",identity:"Mozilla",versionSearch:"rv"},{string:navigator.userAgent,subString:"Mozilla",identity:"Netscape",versionSearch:"Mozilla"}],dataOS:[{string:navigator.platform,subString:"Win",identity:"Windows"},{string:navigator.platform,subString:"Mac",identity:"Mac"},{string:navigator.platform,subString:"Linux",identity:"Linux"}]},_callMethodSWF:function(js){
if(this.loaded){
return lz.embed.dojo.comm[this._id].callMethod(js)
}else{
var $1=function(){
lz.embed.dojo.comm[this._id].callMethod(js)
};lz.embed.dojo.addLoadedListener($1,this)
}},_broadcastMethod:function($1){
var $2=[].slice.call(arguments,1);for(var $3 in lz.embed.applications){
var $4=lz.embed.applications[$3];if(!$4.loaded){
continue
};if($4[$1]){
$4[$1].apply($4,$2)
}}},setCanvasAttribute:function($1,$2,$3){
lz.embed._broadcastMethod("setCanvasAttribute",$1,$2,$3)
},callMethod:function($1){
lz.embed._broadcastMethod("callMethod",$1)
},_getAppendDiv:function($1,$2){
var $3=$2?$2:$1+"Container";var $4=document.getElementById($3);if(!$4){
document.writeln('<div id="'+$3+'"></div>');$4=document.getElementById($3)
};return $4
},_getSWFDiv:function(){
return lz.embed.dojo.obj[this._id].get()
},_sendMouseWheel:function($1){
if($1!=null){
this.callMethod("LzKeys.__mousewheelEvent("+$1+")")
}},attachEventHandler:function($1,$2,callbackscope,callbackname){
if(!callbackscope||!callbackname||!callbackscope[callbackname]){
return
};var $3=$1+$2+callbackscope+callbackname;var $4=function(){
var $1=window.event?[window.event]:arguments;callbackscope[callbackname].apply(callbackscope,$1)
};this._handlers[$3]=$4;if($1["addEventListener"]){
$1.addEventListener($2,$4,false);return true
}else{
if($1["attachEvent"]){
return $1.attachEvent("on"+$2,$4)
}}},removeEventHandler:function($1,$2,$3,$4){
var $5=$1+$2+$3+$4;var $6=this._handlers[$5];this._handlers[$5]=null;if(!$6){
return
};if($1["removeEventListener"]){
$1.removeEventListener($2,$6,false);return true
}else{
if($1["detachEvent"]){
return $1.detachEvent("on"+$2,$6)
}}},_handlers:{},_cleanupHandlers:function(){
lz.embed._handlers={}}};lz.embed.browser.init();lz.embed.attachEventHandler(window,"beforeunload",lz.embed,"_cleanupHandlers");try{
if(lzOptions){
if(lzOptions.dhtmlKeyboardControl){
alert("WARNING: this page uses lzOptions.dhtmlKeyboardControl.  Please use the cancelkeyboardcontrol embed argument for lz.embed.dhtml() instead.")
};if(lzOptions.ServerRoot){
alert("WARNING: this page uses lzOptions.ServerRoot.  Please use the second argument of lz.embed.lfc() instead.")
}}}
catch(e){

};lz.embed.dojo=function(){

};lz.embed.dojo={defaults:{flash6:null,flash8:null,ready:false,visible:true,width:500,height:400,bgcolor:"#ffffff",wmode:"window",flashvars:"",minimumVersion:7,id:"flashObject",appenddiv:null},obj:{},comm:{},_loadedListeners:[],_loadedListenerScopes:[],_installingListeners:[],_installingListenerScopes:[],setSwf:function($1,$2){
if($1==null){
return
};var $3={};for(var $4 in this.defaults){
var $5=$1[$4];if($5!=null){
$3[$4]=$5
}else{
$3[$4]=this.defaults[$4]
}};if($2!=null){
this.minimumVersion=$2
};this._initialize($3)
},useFlash6:function($1){
var $2=lz.embed.dojo.obj[$1].properties;if($2.flash6==null){
return false
}else{
if($2.flash6!=null&&lz.embed.dojo.info.commVersion==6){
return true
}else{
return false
}}},useFlash8:function($1){
var $2=lz.embed.dojo.obj[$1].properties;if($2.flash8==null){
return false
}else{
if($2.flash8!=null&&lz.embed.dojo.info.commVersion==8){
return true
}else{
return false
}}},addLoadedListener:function($1,$2){
this._loadedListeners.push($1);this._loadedListenerScopes.push($2)
},addInstallingListener:function($1,$2){
this._installingListeners.push($1);this._installingListenerScopes.push($2)
},loaded:function($1){
if(lz.embed.dojo._isinstaller){
top.location=top.location+""
};lz.embed.dojo.info.installing=false;lz.embed.dojo.ready=true;if(lz.embed.dojo._loadedListeners.length>0){
for(var $2=0;$2<lz.embed.dojo._loadedListeners.length;$2++){
var $3=lz.embed.dojo._loadedListenerScopes[$2];if($1!=$3._id){
continue
};lz.embed.dojo._loadedListeners[$2].apply($3,[$3._id])
}}},installing:function(){
if(lz.embed.dojo._installingListeners.length>0){
for(var $1=0;$1<lz.embed.dojo._installingListeners.length;$1++){
var $2=lz.embed.dojo._installingListenerScopes[$1];lz.embed.dojo._installingListeners[$1].apply($2,[$2._id])
}}},_initialize:function($1){
var $2=new (lz.embed.dojo.Install)($1.id);lz.embed.dojo.installer=$2;var $3=new (lz.embed.dojo.Embed)($1);lz.embed.dojo.obj[$1.id]=$3;if($2.needed()==true){
$2.install()
}else{
$3.write(lz.embed.dojo.info.commVersion);lz.embed.dojo.comm[$1.id]=new (lz.embed.dojo.Communicator)($1.id)
}}};lz.embed.dojo.Info=function(){
if(lz.embed.browser.isIE){
document.writeln('<script language="VBScript" type="text/vbscript">');document.writeln("Function VBGetSwfVer(i)");document.writeln("  on error resume next");document.writeln("  Dim swControl, swVersion");document.writeln("  swVersion = 0");document.writeln('  set swControl = CreateObject("ShockwaveFlash.ShockwaveFlash." + CStr(i))');document.writeln("  if (IsObject(swControl)) then");document.writeln('    swVersion = swControl.GetVariable("$version")');document.writeln("  end if");document.writeln("  VBGetSwfVer = swVersion");document.writeln("End Function");document.writeln("</script>")
};this._detectVersion();this._detectCommunicationVersion()
};lz.embed.dojo.Info.prototype={version:-1,versionMajor:-1,versionMinor:-1,versionRevision:-1,capable:false,commVersion:6,installing:false,isVersionOrAbove:function($1,$2,$3){
$3=parseFloat("."+$3);if(this.versionMajor>=$1&&this.versionMinor>=$2&&this.versionRevision>=$3){
return true
}else{
return false
}},_detectVersion:function(){
var $1;for(var $2=25;$2>0;$2--){
if(lz.embed.browser.isIE){
$1=VBGetSwfVer($2)
}else{
$1=this._JSFlashInfo($2)
};if($1==-1){
this.capable=false;return
}else{
if($1!=0){
var $3;if(lz.embed.browser.isIE){
var $4=$1.split(" ");var $5=$4[1];$3=$5.split(",")
}else{
$3=$1.split(".")
};this.versionMajor=$3[0];this.versionMinor=$3[1];this.versionRevision=$3[2];var $6=this.versionMajor+"."+this.versionRevision;this.version=parseFloat($6);this.capable=true;break
}}}},_JSFlashInfo:function($1){
if(navigator.plugins!=null&&navigator.plugins.length>0){
if(navigator.plugins["Shockwave Flash 2.0"]||navigator.plugins["Shockwave Flash"]){
var $2=navigator.plugins["Shockwave Flash 2.0"]?" 2.0":"";var $3=navigator.plugins["Shockwave Flash"+$2].description;var $4=$3.split(" ");var $5=$4[2].split(".");var $6=$5[0];var $7=$5[1];if($4[3]!=""){
var $8=$4[3].split("r")
}else{
var $8=$4[4].split("r")
};var $9=$8[1]>0?$8[1]:0;var $10=$6+"."+$7+"."+$9;return $10
}};return -1
},_detectCommunicationVersion:function(){
if(this.capable==false){
this.commVersion=null;return
};if(typeof lz.embed.options["forceFlashComm"]!="undefined"&&typeof lz.embed.options["forceFlashComm"]!=null){
this.commVersion=lz.embed.options["forceFlashComm"];return
};if(lz.embed.browser.isSafari==true||lz.embed.browser.isOpera==true){
this.commVersion=8
}else{
this.commVersion=6
}}};lz.embed.dojo.Embed=function($1){
this.properties=$1;if(!this.properties.width){
this.properties.width="100%"
};if(!this.properties.height){
this.properties.height="100%"
};if(!this.properties.bgcolor){
this.properties.bgcolor="#ffffff"
};if(!this.properties.visible){
this.properties.visible=true
}};lz.embed.dojo.Embed.prototype={protocol:function(){
switch(window.location.protocol){
case "https:":
return "https";break;
default:
return "http";break;

}},__getCSSValue:function($1){
if($1&&$1.length&&$1.indexOf("%")!=-1){
return "100%"
}else{
return $1+"px"
}},write:function($1,$2){
var $3="";$3+="width: "+this.__getCSSValue(this.properties.width)+";";$3+="height: "+this.__getCSSValue(this.properties.height)+";";if(this.properties.visible==false){
$3+="position: absolute; ";$3+="z-index: 10000; ";$3+="top: -1000px; ";$3+="left: -1000px; "
};var $4;var $5;if($1==6){
$5=this.properties.flash6;$4='<embed id="'+this.properties.id+'" src="'+$5+'" '+'    type="application/x-shockwave-flash" '+'    quality="high" bgcolor="'+this.properties.bgcolor+'"'+'    width="'+this.properties.width+'" height="'+this.properties.height+'" '+'    name="'+this.properties.id+'" '+'    wmode="'+this.properties.wmode+'" '+'    FlashVars="'+this.properties.flashvars+'"'+'    align="middle" '+'    allowScriptAccess="sameDomain" '+'    swLiveConnect="true" '+'    pluginspage="'+this.protocol()+'://www.macromedia.com/go/getflashplayer">'
}else{
if($1>lz.embed.dojo.version){
$2=true
};$5=this.properties.flash8;var $6=this.properties.flashvars;var $7=this.properties.flashvars;if($2){
var $8=escape(window.location);document.title=document.title.slice(0,47)+" - Flash Player Installation";var $9=escape(document.title);$6+="&MMredirectURL="+$8+"&MMplayerType=ActiveX"+"&MMdoctitle="+$9;$7+="&MMredirectURL="+$8+"&MMplayerType=PlugIn"
};if(lz.embed.browser.isIE){
$4='<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" '+'codebase="'+this.protocol()+"://fpdownload.macromedia.com/pub/shockwave/cabs/flash/"+'swflash.cab#version=8,0,0,0" '+'width="'+this.properties.width+'" '+'height="'+this.properties.height+'" '+'id="'+this.properties.id+'" '+'align="middle"> '+'<param name="allowScriptAccess" value="sameDomain" /> '+'<param name="movie" value="'+$5+'" /> '+'<param name="quality" value="high" /> '+'<param name="FlashVars" value="'+$6+'" /> '+'<param name="bgcolor" value="'+this.properties.bgcolor+'" /> '+'<param name="wmode" value="'+this.properties.wmode+'" /> '+"</object>"
}else{
$4='<embed src="'+$5+'" '+'quality="high" '+'bgcolor="'+this.properties.bgcolor+'" '+'wmode="'+this.properties.wmode+'" '+'width="'+this.properties.width+'" '+'height="'+this.properties.height+'" '+'id="'+this.properties.id+'" '+'name="'+this.properties.id+'" '+'FlashVars="'+$7+'" '+'swLiveConnect="true" '+'align="middle" '+'allowScriptAccess="sameDomain" '+'type="application/x-shockwave-flash" '+'pluginspage="'+this.protocol()+'://www.macromedia.com/go/getflashplayer" />'
}};var $10=this.properties.id+"Container";var $11=this.properties.appenddiv;if($11){
$11.innerHTML=$4;$11.setAttribute("style",$3)
}else{
$4='<div id="'+$10+'" style="'+$3+'"> '+$4+"</div>";document.writeln($4)
}},get:function(){
try{
var $1=document.getElementById(this.properties.id+"")
}
catch(e){

};return $1
},setVisible:function($1){
var $2=document.getElementById(this.properties.id+"Container");if($1==true){
$2.style.visibility="visible"
}else{
$2.style.position="absolute";$2.style.x="-1000px";$2.style.y="-1000px";$2.style.visibility="hidden"
}},center:function(){
var $1=this.properties.width;var $2=this.properties.height;var $3=0;var $4=0;var $5=document.getElementById(this.properties.id+"Container");$5.style.top=$4+"px";$5.style.left=$3+"px"
}};lz.embed.dojo.Communicator=function($1){
this._id=$1;if(lz.embed.dojo.useFlash6($1)){
this._writeFlash6()
}else{
if(lz.embed.dojo.useFlash8($1)){
this._writeFlash8()
}}};lz.embed.dojo.Communicator.prototype={_writeFlash6:function(){
var $1=lz.embed.dojo.obj[this._id].properties.id;document.writeln('<script language="JavaScript">');document.writeln("  function "+$1+"_DoFSCommand(command, args){ ");document.writeln("    lz.embed.dojo.comm."+$1+'._handleFSCommand(command, args, "'+$1+'"); ');document.writeln("}");document.writeln("</script>");if(lz.embed.browser.isIE){
document.writeln("<SCRIPT LANGUAGE=VBScript> ");document.writeln("on error resume next ");document.writeln("Sub "+$1+"_FSCommand(ByVal command, ByVal args)");document.writeln(" call "+$1+"_DoFSCommand(command, args, "+$1+")");document.writeln("end sub");document.writeln("</SCRIPT> ")
}},_writeFlash8:function(){

},_handleFSCommand:function($1,$2,$3){
if($1!=null&&RegExp("^FSCommand:(.*)").test($1)==true){
$1=$1.match(RegExp("^FSCommand:(.*)"))[1]
};if($1=="addCallback"){
this._fscommandAddCallback($1,$2,$3)
}else{
if($1=="call"){
this._fscommandCall($1,$2,$3)
}else{
if($1=="fscommandReady"){
this._fscommandReady($3)
}}}},_fscommandAddCallback:function($1,$2,id){
var functionName=$2;var $3=function(){
return lz.embed.dojo.comm[id]._call(functionName,arguments,id)
};lz.embed.dojo.comm[id][functionName]=$3;var $4=lz.embed.dojo.obj[id].get();if($4){
$4.SetVariable("_succeeded",true)
}},_fscommandCall:function($1,$2,$3){
var $4=lz.embed.dojo.obj[$3].get();var $5=$2;if(!$4){
return
};var $6=parseInt($4.GetVariable("_numArgs"));var $7=[];for(var $8=0;$8<$6;$8++){
var $9=$4.GetVariable("_"+$8);$7.push($9)
};var $10;var $11=window;if($5.indexOf(".")==-1){
$10=window[$5]
}else{
$10=eval($5);$11=eval($5.substring(0,$5.lastIndexOf(".")))
};var $12=null;if($10!=null){
$12=$10.apply($11,$7)
};$12+="";$4.SetVariable("_fsreturnResult",$12)
},_fscommandReady:function($1){
var $2=lz.embed.dojo.obj[$1].get();if($2){
$2.SetVariable("fscommandReady","true")
}},_call:function($1,$2,$3){
var $4=lz.embed.dojo.obj[$3].get();$4.SetVariable("_functionName",$1);$4.SetVariable("_numArgs",$2.length);for(var $5=0;$5<$2.length;$5++){
var $6=$2[$5];$6=$6.replace(RegExp("\\0","g"),"\\0");$4.SetVariable("_"+$5,$6)
};$4.TCallFrame("/flashRunner",1);var $7=$4.GetVariable("_returnResult");$7=$7.replace(RegExp("\\\\0","g"),"\0");return $7
},_addExternalInterfaceCallback:function(methodName,id){
var $1=function(){
var $1=[];for(var $2=0;$2<arguments.length;$2++){
$1[$2]=arguments[$2]
};$1.length=arguments.length;return lz.embed.dojo.comm[id]._execFlash(methodName,$1,id)
};lz.embed.dojo.comm[id][methodName]=$1
},_encodeData:function($1){
var $2=RegExp("\\&([^;]*)\\;","g");$1=$1.replace($2,"&amp;$1;");$1=$1.replace(RegExp("<","g"),"&lt;");$1=$1.replace(RegExp(">","g"),"&gt;");$1=$1.replace("\\","&custom_backslash;&custom_backslash;");$1=$1.replace(RegExp("\\n","g"),"\\n");$1=$1.replace(RegExp("\\r","g"),"\\r");$1=$1.replace(RegExp("\\f","g"),"\\f");$1=$1.replace(RegExp("\\0","g"),"\\0");$1=$1.replace(RegExp("\\'","g"),"\\'");$1=$1.replace(RegExp('\\"',"g"),'\\"');return $1
},_decodeData:function($1){
if($1==null||typeof $1=="undefined"){
return $1
};$1=$1.replace(RegExp("\\&custom_lt\\;","g"),"<");$1=$1.replace(RegExp("\\&custom_gt\\;","g"),">");$1=eval('"'+$1+'"');return $1
},_chunkArgumentData:function($1,$2,$3){
var $4=lz.embed.dojo.obj[$3].get();var $5=Math.ceil($1.length/1024);for(var $6=0;$6<$5;$6++){
var $7=$6*1024;var $8=$6*1024+1024;if($6==$5-1){
$8=$6*1024+$1.length
};var $9=$1.substring($7,$8);$9=this._encodeData($9);$4.CallFunction('<invoke name="chunkArgumentData" '+'returntype="javascript">'+"<arguments>"+"<string>"+$9+"</string>"+"<number>"+$2+"</number>"+"</arguments>"+"</invoke>")
}},_chunkReturnData:function($1){
var $2=lz.embed.dojo.obj[$1].get();var $3=$2.getReturnLength();var $4=[];for(var $5=0;$5<$3;$5++){
var $6=$2.CallFunction('<invoke name="chunkReturnData" '+'returntype="javascript">'+"<arguments>"+"<number>"+$5+"</number>"+"</arguments>"+"</invoke>");if($6=='""'||$6=="''"){
$6=""
}else{
$6=$6.substring(1,$6.length-1)
};$4.push($6)
};var $7=$4.join("");return $7
},_execFlash:function($1,$2,$3){
var $4=lz.embed.dojo.obj[$3].get();$4.startExec();$4.setNumberArguments($2.length);for(var $5=0;$5<$2.length;$5++){
this._chunkArgumentData($2[$5],$5,$3)
};$4.exec($1);var $6=this._chunkReturnData($3);$6=this._decodeData($6);$4.endExec();return $6
}};lz.embed.dojo.Install=function($1){
this._id=$1
};lz.embed.dojo.Install.prototype={needed:function(){
if(lz.embed.dojo.info.capable==false){
return true
};if(lz.embed.browser.isSafari==true&&!lz.embed.dojo.info.isVersionOrAbove(8,0,0)){
return true
};if(lz.embed.dojo.minimumVersion>lz.embed.dojo.info.versionMajor){
return true
};if(!lz.embed.dojo.info.isVersionOrAbove(6,0,0)){
return true
};return false
},install:function(){
lz.embed.dojo.info.installing=true;lz.embed.dojo.installing();var $1=lz.embed.dojo.obj[this._id].properties;var $2=$1.flash8;var $3=$2.indexOf("swf7");if($3!=-1){
lz.embed.dojo._tempurl=$2;$2=$2.substring(0,$3+3)+"8"+$2.substring($3+4,$2.length);$1.flash8=$2
};lz.embed.dojo.ready=false;if(lz.embed.dojo.info.capable==false){
lz.embed.dojo._isinstaller=true;var $4=new (lz.embed.dojo.Embed)($1);$4.write(8)
}else{
if(lz.embed.dojo.info.isVersionOrAbove(6,0,65)){
var $4=new (lz.embed.dojo.Embed)($1);$4.write(8,true);$4.setVisible(true);$4.center()
}else{
alert("This content requires a more recent version of the Macromedia "+" Flash Player.");window.location="http://www.macromedia.com/go/getflashplayer"
}}},_onInstallStatus:function($1){
if($1=="Download.Complete"){
lz.embed.dojo._initialize()
}else{
if($1=="Download.Cancelled"){
alert("This content requires a more recent version of the Macromedia "+" Flash Player.");window.location="http://www.macromedia.com/go/getflashplayer"
}else{
if($1=="Download.Failed"){
alert("There was an error downloading the Flash Player update. "+"Please try again later, or visit macromedia.com to download "+"the latest version of the Flash plugin.")
}}}}};lz.embed.dojo.info=new (lz.embed.dojo.Info)();lz.embed.iframemanager={__counter:0,__frames:{},__namebyid:{},create:function($1,$2,$3,$4,$5){
var $6=document.createElement("iframe");$6.owner=$1;$6.skiponload=true;var $7="__lz"+lz.embed.iframemanager.__counter++;lz.embed.iframemanager.__frames[$7]=$6;if($2==null||$2=="null"||$2==""){
$2=$7
};if($2!=""){
lz.embed.__setAttr($6,"name",$2)
};lz.embed.iframemanager.__namebyid[$7]=$2;lz.embed.__setAttr($6,"src",'javascript:""');if($4==null||$4=="undefined"){
$4=document.body
};lz.embed.__setAttr($6,"id",$7);if($3!=true){
lz.embed.__setAttr($6,"scrolling","no")
};this.appendTo($7,$4);var $8=lz.embed.iframemanager.getFrame($7);lz.embed.__setAttr($8,"onload",'lz.embed.iframemanager.__gotload("'+$7+'")');$8.__gotload=lz.embed.iframemanager.__gotload;$8._defaultz=$5?$5:99900;this.setZ($7,$8._defaultz);lz.embed.iframemanager.__topiframe=$7;if(document.getElementById&&!document.all){
$8.style.border="0"
}else{
if(document.all){
lz.embed.__setAttr($8,"border","0");lz.embed.__setAttr($8,"allowtransparency","true");var $9=lz.embed[$8.owner];if($9&&$9.runtime=="swf"){
var $10=$9._getSWFDiv();$10.onfocus=lz.embed.iframemanager.__refresh
}}};$8.style.position="absolute";return $7+""
},appendTo:function($1,$2){
var $3=lz.embed.iframemanager.getFrame($1);if($2.__appended==$2){
return
};if($3.__appended){
old=$3.__appended.removeChild($3);$2.appendChild(old)
}else{
$2.appendChild($3)
};$3.__appended=$2
},getFrame:function($1){
return lz.embed.iframemanager.__frames[$1]
},setSrc:function($1,$2,$3){
if($3){
var $4=lz.embed.iframemanager.getFrame($1);if(!$4){
return
};lz.embed.__setAttr($4,"src",$2)
}else{
var $1=lz.embed.iframemanager.__namebyid[$1];var $4=window[$1];if(!$4){
return
};$4.location.replace($2)
}},setPosition:function($1,$2,$3,$4,$5,$6,$7){
var $8=lz.embed.iframemanager.getFrame($1);if(!$8){
return
};if($2!=null){
$8.style.left=$2+"px"
};if($3!=null){
$8.style.top=$3+"px"
};if($4!=null){
$8.style.width=$4+"px"
};if($5!=null){
$8.style.height=$5+"px"
};if($6!=null){
if(typeof $6=="string"){
$6=$6=="true"
};$8.style.display=$6?"block":"none"
};if($7!=null){
this.setZ($1,$7+$8._defaultz)
}},setVisible:function($1,$2){
if(typeof $2=="string"){
$2=$2=="true"
};var $3=lz.embed.iframemanager.getFrame($1);if(!$3){
return
};$3.style.display=$2?"block":"none"
},bringToFront:function($1){
var $2=lz.embed.iframemanager.getFrame($1);if(!$2){
return
};$2._defaultz=100000;this.setZ($1,$2._defaultz);lz.embed.iframemanager.__topiframe=$1
},sendToBack:function($1){
var $2=lz.embed.iframemanager.getFrame($1);if(!$2){
return
};$2._defaultz=99900;this.setZ($1,$2._defaultz)
},__gotload:function($1){
var $2=lz.embed.iframemanager.getFrame($1);if(!$2){
return
};if($2.skiponload){
$2.skiponload=false;return
};if($2.owner&&$2.owner.__gotload){
$2.owner.__gotload()
}else{
lz.embed[$2.owner].callMethod("lz.embed.iframemanager.__gotload('"+$1+"')")
}},__refresh:function(){
if(lz.embed.iframemanager.__topiframe){
var $1=lz.embed.iframemanager.getFrame(lz.embed.iframemanager.__topiframe);if($1.style.display=="block"){
$1.style.display="none";$1.style.display="block"
}}},setZ:function($1,$2){
var $3=lz.embed.iframemanager.getFrame($1);if(!$3){
return
};$3.style.zIndex=$2
},scrollBy:function($1,$2,$3){
var $1=lz.embed.iframemanager.__namebyid[$1];var $4=window.frames[$1];if(!$4){
return
};$4.scrollBy($2,$3)
}};lz.embed.mousewheel={__mousewheelEvent:function($1){
if(!$1){
$1=window.event
};var $2=0;if($1.wheelDelta){
$2=$1.wheelDelta/120;if(lz.embed.browser.isOpera){
$2=-$2
}}else{
if($1.detail){
$2=-$1.detail/3
}};if($1.preventDefault){
$1.preventDefault()
};$1.returnValue=false;var $3=lz.embed.mousewheel.__callbacks.length;if($2!=null&&$3>0){
for(var $4=0;$4<$3;$4+=2){
var $5=lz.embed.mousewheel.__callbacks[$4];var $6=lz.embed.mousewheel.__callbacks[$4+1];if($5&&$5[$6]){
$5[$6]($2)
}}}},__callbacks:[],setCallback:function($1,$2){
var $3=lz&&lz.embed&&lz.embed.options&&lz.embed.options.cancelkeyboardcontrol!=true||true;if(lz.embed.mousewheel.__callbacks.length==0&&$3){
if(window.addEventListener){
lz.embed.attachEventHandler(window,"DOMMouseScroll",lz.embed.mousewheel,"__mousewheelEvent")
};lz.embed.attachEventHandler(document,"mousewheel",lz.embed.mousewheel,"__mousewheelEvent")
};lz.embed.mousewheel.__callbacks.push($1,$2)
}};lz.embed.history={_currentstate:null,_apps:[],_intervalID:null,init:function($1){
var $2=lz.embed.history;$2._apps.push($1);$2._title=top.document.title;var $3=$2.get();if(lz.embed.browser.isSafari){
$2._historylength=history.length;$2._history=[];for(var $4=1;$4<$2._historylength;$4++){
$2._history.push("")
};$2._history.push($3);var $5=document.createElement("form");$5.method="get";document.body.appendChild($5);$2._form=$5;if(!top.document.location.lzaddr){
top.document.location.lzaddr={}};if(top.document.location.lzaddr.history){
$2._history=top.document.location.lzaddr.history.split(",")
};if($3!=""){
$2.set($3)
}}else{
if(lz.embed.browser.isIE){
var $3=top.location.hash;if($3){
$3=$3.substring(1)
};var $4=document.createElement("iframe");lz.embed.__setAttr($4,"id","lzHistory");lz.embed.__setAttr($4,"frameborder","no");lz.embed.__setAttr($4,"scrolling","no");lz.embed.__setAttr($4,"width","0");lz.embed.__setAttr($4,"height","0");lz.embed.__setAttr($4,"src",'javascript:""');document.body.appendChild($4);$4=document.getElementById("lzHistory");$2._iframe=$4;$4.style.display="none";$4.style.position="absolute";$4.style.left="-999px";var $6=$4.contentDocument||$4.contentWindow.document;$6.open();$6.close();if($3!=""){
$6.location.hash="#"+$3;$2._parse($3)
}}else{
if($3!=""){
$2._parse($3);$2._currentstate=$3
}}};if(this._intervalID==null){
this._intervalID=setInterval("lz.embed.history._checklocationhash()",100)
}},_checklocationhash:function(){
if(lz.embed.dojo&&lz.embed.dojo.info&&lz.embed.dojo.info.installing){
return
};if(lz.embed.browser.isSafari){
var $1=this._history[this._historylength-1];if($1==""||$1=="#"){
$1="#0"
};if(!this._skip&&this._historylength!=history.length){
this._historylength=history.length;if(typeof $1!="undefined"){
$1=$1.substring(1);this._currentstate=$1;this._parse($1)
}}else{
this._parse($1.substring(1))
}}else{
var $1=lz.embed.history.get();if($1==""){
$1="0"
};if(lz.embed.browser.isIE){
if($1!=this._currentstate){
top.location.hash=$1=="0"?"":"#"+$1;this._currentstate=$1;this._parse($1)
};if(top.document.title!=this._title){
top.document.title=this._title
}}else{
this._currentstate=$1;this._parse($1)
}}},set:function($1){
if($1==null){
$1=""
};if(lz.embed.history._currentstate==$1){
return
};lz.embed.history._currentstate=$1;var $2="#"+$1;if(lz.embed.browser.isIE){
top.location.hash=$2=="#0"?"":$2;var $3=lz.embed.history._iframe.contentDocument||lz.embed.history._iframe.contentWindow.document;$3.open();$3.close();$3.location.hash=$2;lz.embed.history._parse($1+"")
}else{
if(lz.embed.browser.isSafari){
lz.embed.history._history[history.length]=$2;lz.embed.history._historylength=history.length+1;if(lz.embed.browser.version<412){
if(top.location.search==""){
lz.embed.history._form.action=$2;top.document.location.lzaddr.history=lz.embed.history._history.toString();lz.embed.history._skip=true;lz.embed.history._form.submit();lz.embed.history._skip=false
}}else{
var $4=document.createEvent("MouseEvents");$4.initEvent("click",true,true);var $5=document.createElement("a");$5.href=$2;$5.dispatchEvent($4)
}}else{
top.location.hash=$2;lz.embed.history._parse($1+"")
}};return true
},get:function(){
var $1="";if(lz.embed.browser.isIE){
if(lz.embed.history._iframe){
var $2=lz.embed.history._iframe.contentDocument||lz.embed.history._iframe.contentWindow.document;$1=$2.location.hash
}}else{
$1=top.location.href
};var $3=$1.indexOf("#");if($3!=-1){
return $1.substring($3+1)
};return ""
},_parse:function($1){
var $2=lz.embed.history;if($1.length==0){
return
};for(var $3 in lz.embed.history._apps){
var $4=lz.embed.history._apps[$3];if(!$4.loaded||$4._lasthash==$1){
continue
};$4._lasthash=$1;if($1.indexOf("_lz")!=-1){
$1=$1.substring(3);var $5=$1.split(",");for(var $6=0;$6<$5.length;$6++){
var $7=$5[$6];var $8=$7.indexOf("=");var $9=unescape($7.substring(0,$8));var $10=unescape($7.substring($8+1));lz.embed.setCanvasAttribute($9,$10);if(window["canvas"]){
canvas.setAttribute($9,$10)
}}}else{
if($4.runtime=="swf"){
$2.__setFlash($1,$4._id)
}else{
if(window["LzHistory"]&&LzHistory["isReady"]&&LzHistory["receiveHistory"]){
LzHistory.receiveHistory($1)
}}}}},_store:function($1,$2){
if($1 instanceof Object){
var $3="";for(var $4 in $1){
if($3!=""){
$3+=","
};$3+=escape($4)+"="+escape($1[$4])
}}else{
var $3=escape($1)+"="+escape($2)
};this.set("_lz"+$3)
},__setFlash:function($1,$2){
var $3=lz.embed[$2];if($3&&$3.loaded&&$3.runtime=="swf"){
var $4=$3._getSWFDiv();if($4){
var $5=$4.GetVariable("_callbackID")+"";if($5=="null"){
lz.embed[$2]._lasthash=$3.callMethod("LzHistory.receiveHistory("+$1+")")
}else{
setTimeout("lz.embed.history.__setFlash("+$1+',"'+$2+'")',10)
}}}}};if(lz.embed.browser.isFirefox){
window.onunload=function(){

}}