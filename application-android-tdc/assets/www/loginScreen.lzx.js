LzResourceLibrary.lzfocusbracket_rsrc={ptype:"sr",frames:['lps/components/lz/resources/focus/focus_top_lft.png','lps/components/lz/resources/focus/focus_top_rt.png','lps/components/lz/resources/focus/focus_bot_lft.png','lps/components/lz/resources/focus/focus_bot_rt.png'],width:7,height:7,sprite:'lps/components/lz/resources/focus/focus_top_lft.sprite.png',spriteoffset:0};LzResourceLibrary.lzfocusbracket_shdw={ptype:"sr",frames:['lps/components/lz/resources/focus/focus_top_lft_shdw.png','lps/components/lz/resources/focus/focus_top_rt_shdw.png','lps/components/lz/resources/focus/focus_bot_lft_shdw.png','lps/components/lz/resources/focus/focus_bot_rt_shdw.png'],width:9,height:9,sprite:'lps/components/lz/resources/focus/focus_top_lft_shdw.sprite.png',spriteoffset:7};LzResourceLibrary.lzbutton_face_rsc={ptype:"sr",frames:['lps/components/lz/resources/button/simpleface_up.png','lps/components/lz/resources/button/simpleface_mo.png','lps/components/lz/resources/button/simpleface_dn.png','lps/components/lz/resources/button/autoPng/simpleface_dsbl.png'],width:2,height:18,sprite:'lps/components/lz/resources/button/simpleface_up.sprite.png',spriteoffset:16};LzResourceLibrary.lzbutton_bezel_inner_rsc={ptype:"sr",frames:['lps/components/lz/resources/autoPng/bezel_inner_up.png','lps/components/lz/resources/autoPng/bezel_inner_up.png','lps/components/lz/resources/autoPng/bezel_inner_dn.png','lps/components/lz/resources/autoPng/outline_dsbl.png'],width:500,height:500,sprite:'lps/components/lz/resources/autoPng/bezel_inner_up.sprite.png',spriteoffset:34};LzResourceLibrary.lzbutton_bezel_outer_rsc={ptype:"sr",frames:['lps/components/lz/resources/autoPng/bezel_outer_up.png','lps/components/lz/resources/autoPng/bezel_outer_up.png','lps/components/lz/resources/autoPng/bezel_outer_dn.png','lps/components/lz/resources/autoPng/transparent.png','lps/components/lz/resources/autoPng/default_outline.png'],width:500,height:500,sprite:'lps/components/lz/resources/autoPng/bezel_outer_up.sprite.png',spriteoffset:534};LzResourceLibrary.__allcss={path:'loginScreen.sprite.png'};var loginMain=null;var main=null;var loginDs=null;var login=null;var li=null;var loginId=null;var Main=null;Class.make("$lzc$class_m2",["response",void 0,"$m1",function($0){
with(this){
if($0){
loginMain.setAttribute("visible",false);main.setAttribute("visible",true);if($0.indexOf("200")){
main.show($0)
}else{
main.show($0)
}}}},"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzCanvas,["displayName","<anonymous extends='canvas'>","attributes",new LzInheritedHash(LzCanvas.attributes)]);canvas=new $lzc$class_m2(null,{$delegates:["onresponse","$m1",null],__LZproxied:"false",appbuilddate:"2012-08-22T13:42:02Z",bgcolor:3291989,embedfonts:true,font:"Verdana,Vera,sans-serif",fontsize:11,fontstyle:"plain",height:"100%",lpsbuild:"branches/4.9@17752 (17752)",lpsbuilddate:"2010-10-22T16:12:42Z",lpsrelease:"Production",lpsversion:"4.9.0",response:void 0,runtime:"dhtml",width:"100%"});lz.colors.offwhite=15921906;lz.colors.gray10=1710618;lz.colors.gray20=3355443;lz.colors.gray30=5066061;lz.colors.gray40=6710886;lz.colors.gray50=8355711;lz.colors.gray60=10066329;lz.colors.gray70=11776947;lz.colors.gray80=13421772;lz.colors.gray90=15066597;lz.colors.iceblue1=3298963;lz.colors.iceblue2=5472718;lz.colors.iceblue3=12240085;lz.colors.iceblue4=14017779;lz.colors.iceblue5=15659509;lz.colors.palegreen1=4290113;lz.colors.palegreen2=11785139;lz.colors.palegreen3=12637341;lz.colors.palegreen4=13888170;lz.colors.palegreen5=15725032;lz.colors.gold1=9331721;lz.colors.gold2=13349195;lz.colors.gold3=15126388;lz.colors.gold4=16311446;lz.colors.sand1=13944481;lz.colors.sand2=14276546;lz.colors.sand3=15920859;lz.colors.sand4=15986401;lz.colors.ltpurple1=6575768;lz.colors.ltpurple2=12038353;lz.colors.ltpurple3=13353453;lz.colors.ltpurple4=15329264;lz.colors.grayblue=12501704;lz.colors.graygreen=12635328;lz.colors.graypurple=10460593;lz.colors.ltblue=14540287;lz.colors.ltgreen=14548957;{
Class.make("$lzc$class_basefocusview",["active",void 0,"$lzc$set_active",function($0){
with(this){
setActive($0)
}},"target",void 0,"$lzc$set_target",function($0){
with(this){
setTarget($0)
}},"duration",void 0,"_animatorcounter",void 0,"ontarget",void 0,"_nexttarget",void 0,"onactive",void 0,"_xydelegate",void 0,"_widthdel",void 0,"_heightdel",void 0,"_delayfadeoutDL",void 0,"_dofadeout",void 0,"_onstopdel",void 0,"reset",function(){
with(this){
this.setAttribute("x",0);this.setAttribute("y",0);this.setAttribute("width",canvas.width);this.setAttribute("height",canvas.height);setTarget(null)
}},"setActive",function($0){
this.active=$0;if(this.onactive)this.onactive.sendEvent($0)
},"doFocus",function($0){
with(this){
this._dofadeout=false;this.bringToFront();if(this.target)this.setTarget(null);this.setAttribute("visibility",this.active?"visible":"hidden");this._nexttarget=$0;if(visible){
this._animatorcounter+=1;var $1=null;var $2;var $3;var $4;var $5;if($0["getFocusRect"])$1=$0.getFocusRect();if($1){
$2=$1[0];$3=$1[1];$4=$1[2];$5=$1[3]
}else{
$2=$0.getAttributeRelative("x",canvas);$3=$0.getAttributeRelative("y",canvas);$4=$0.getAttributeRelative("width",canvas);$5=$0.getAttributeRelative("height",canvas)
};var $6=this.animate("x",$2,duration);this.animate("y",$3,duration);this.animate("width",$4,duration);this.animate("height",$5,duration);if(this.capabilities["minimize_opacity_changes"]){
this.setAttribute("visibility","visible")
}else{
this.animate("opacity",1,500)
};if(!this._onstopdel)this._onstopdel=new LzDelegate(this,"stopanim");this._onstopdel.register($6,"onstop")
};if(this._animatorcounter<1){
this.setTarget(this._nexttarget);var $1=null;var $2;var $3;var $4;var $5;if($0["getFocusRect"])$1=$0.getFocusRect();if($1){
$2=$1[0];$3=$1[1];$4=$1[2];$5=$1[3]
}else{
$2=$0.getAttributeRelative("x",canvas);$3=$0.getAttributeRelative("y",canvas);$4=$0.getAttributeRelative("width",canvas);$5=$0.getAttributeRelative("height",canvas)
};this.setAttribute("x",$2);this.setAttribute("y",$3);this.setAttribute("width",$4);this.setAttribute("height",$5)
}}},"stopanim",function($0){
with(this){
this._animatorcounter-=1;if(this._animatorcounter<1){
this._dofadeout=true;if(!this._delayfadeoutDL)this._delayfadeoutDL=new LzDelegate(this,"fadeout");lz.Timer.addTimer(this._delayfadeoutDL,1000);this.setTarget(_nexttarget);this._onstopdel.unregisterAll()
}}},"fadeout",function($0){
with(this){
if(_dofadeout){
if(this.capabilities["minimize_opacity_changes"]){
this.setAttribute("visibility","hidden")
}else{
this.animate("opacity",0,500)
}};this._delayfadeoutDL.unregisterAll()
}},"setTarget",function($0){
with(this){
this.target=$0;if(!this._xydelegate){
this._xydelegate=new LzDelegate(this,"followXY")
}else{
this._xydelegate.unregisterAll()
};if(!this._widthdel){
this._widthdel=new LzDelegate(this,"followWidth")
}else{
this._widthdel.unregisterAll()
};if(!this._heightdel){
this._heightdel=new LzDelegate(this,"followHeight")
}else{
this._heightdel.unregisterAll()
};if(this.target==null)return;var $1=$0;var $2=0;while($1!=canvas){
this._xydelegate.register($1,"onx");this._xydelegate.register($1,"ony");$1=$1.immediateparent;$2++
};this._widthdel.register($0,"onwidth");this._heightdel.register($0,"onheight");followXY(null);followWidth(null);followHeight(null)
}},"followXY",function($0){
with(this){
var $1=null;if(target["getFocusRect"])$1=target.getFocusRect();if($1){
this.setAttribute("x",$1[0]);this.setAttribute("y",$1[1])
}else{
this.setAttribute("x",this.target.getAttributeRelative("x",canvas));this.setAttribute("y",this.target.getAttributeRelative("y",canvas))
}}},"followWidth",function($0){
with(this){
var $1=null;if(target["getFocusRect"])$1=target.getFocusRect();if($1){
this.setAttribute("width",$1[2])
}else{
this.setAttribute("width",this.target.width)
}}},"followHeight",function($0){
with(this){
var $1=null;if(target["getFocusRect"])$1=target.getFocusRect();if($1){
this.setAttribute("height",$1[3])
}else{
this.setAttribute("height",this.target.height)
}}},"$m3",function(){
with(this){
return lz.Focus
}},"$m4",function($0){
with(this){
this.setActive(lz.Focus.focuswithkey);if($0){
this.doFocus($0)
}else{
this.reset();if(this.active){
this.setActive(false)
}}}},"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzView,["tagname","basefocusview","attributes",new LzInheritedHash(LzView.attributes)]);(function($0){
with($0)with($0.prototype){
{
LzNode.mergeAttributes({$delegates:["onstop","stopanim",null,"onfocus","$m4","$m3"],_animatorcounter:0,_delayfadeoutDL:null,_dofadeout:false,_heightdel:null,_nexttarget:null,_onstopdel:null,_widthdel:null,_xydelegate:null,active:false,duration:400,initstage:"late",onactive:LzDeclaredEvent,ontarget:LzDeclaredEvent,options:{ignorelayout:true},target:null,visible:false},$lzc$class_basefocusview.attributes)
}}})($lzc$class_basefocusview)
};Class.make("$lzc$class_ml",["$m5",function($0){
with(this){
var $1=-classroot.offset;if($1!==this["x"]||!this.inited){
this.setAttribute("x",$1)
}}},"$m6",function(){
with(this){
try{
return [classroot,"offset"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$m7",function($0){
with(this){
var $1=-classroot.offset;if($1!==this["y"]||!this.inited){
this.setAttribute("y",$1)
}}},"$m8",function(){
with(this){
try{
return [classroot,"offset"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$classrootdepth",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzView,["displayName","<anonymous extends='view'>","children",[{attrs:{$classrootdepth:2,opacity:0.25,resource:"lzfocusbracket_shdw",x:1,y:1},"class":LzView},{attrs:{$classrootdepth:2,resource:"lzfocusbracket_rsrc"},"class":LzView}],"attributes",new LzInheritedHash(LzView.attributes)]);Class.make("$lzc$class_mm",["$m9",function($0){
with(this){
var $1=parent.width-width+classroot.offset;if($1!==this["x"]||!this.inited){
this.setAttribute("x",$1)
}}},"$ma",function(){
with(this){
try{
return [parent,"width",this,"width",classroot,"offset"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$mb",function($0){
with(this){
var $1=-classroot.offset;if($1!==this["y"]||!this.inited){
this.setAttribute("y",$1)
}}},"$mc",function(){
with(this){
try{
return [classroot,"offset"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$classrootdepth",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzView,["displayName","<anonymous extends='view'>","children",[{attrs:{$classrootdepth:2,frame:2,opacity:0.25,resource:"lzfocusbracket_shdw",x:1,y:1},"class":LzView},{attrs:{$classrootdepth:2,frame:2,resource:"lzfocusbracket_rsrc"},"class":LzView}],"attributes",new LzInheritedHash(LzView.attributes)]);Class.make("$lzc$class_mn",["$md",function($0){
with(this){
var $1=-classroot.offset;if($1!==this["x"]||!this.inited){
this.setAttribute("x",$1)
}}},"$me",function(){
with(this){
try{
return [classroot,"offset"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$mf",function($0){
with(this){
var $1=parent.height-height+classroot.offset;if($1!==this["y"]||!this.inited){
this.setAttribute("y",$1)
}}},"$mg",function(){
with(this){
try{
return [parent,"height",this,"height",classroot,"offset"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$classrootdepth",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzView,["displayName","<anonymous extends='view'>","children",[{attrs:{$classrootdepth:2,frame:3,opacity:0.25,resource:"lzfocusbracket_shdw",x:1,y:1},"class":LzView},{attrs:{$classrootdepth:2,frame:3,resource:"lzfocusbracket_rsrc"},"class":LzView}],"attributes",new LzInheritedHash(LzView.attributes)]);Class.make("$lzc$class_mo",["$mh",function($0){
with(this){
var $1=parent.width-width+classroot.offset;if($1!==this["x"]||!this.inited){
this.setAttribute("x",$1)
}}},"$mi",function(){
with(this){
try{
return [parent,"width",this,"width",classroot,"offset"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$mj",function($0){
with(this){
var $1=parent.height-height+classroot.offset;if($1!==this["y"]||!this.inited){
this.setAttribute("y",$1)
}}},"$mk",function(){
with(this){
try{
return [parent,"height",this,"height",classroot,"offset"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$classrootdepth",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzView,["displayName","<anonymous extends='view'>","children",[{attrs:{$classrootdepth:2,frame:4,opacity:0.25,resource:"lzfocusbracket_shdw",x:1,y:1},"class":LzView},{attrs:{$classrootdepth:2,frame:4,resource:"lzfocusbracket_rsrc"},"class":LzView}],"attributes",new LzInheritedHash(LzView.attributes)]);{
Class.make("$lzc$class_focusoverlay",["offset",void 0,"topleft",void 0,"topright",void 0,"bottomleft",void 0,"bottomright",void 0,"doFocus",function($0){
with(this){
(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["doFocus"]||this.nextMethod(arguments.callee,"doFocus")).call(this,$0);if(visible)this.bounce()
}},"bounce",function(){
with(this){
this.animate("offset",12,duration/2);this.animate("offset",5,duration)
}},"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],$lzc$class_basefocusview,["tagname","focusoverlay","children",[{attrs:{$classrootdepth:1,name:"topleft",x:new LzAlwaysExpr("x","numberExpression","$m5","$m6",null),y:new LzAlwaysExpr("y","numberExpression","$m7","$m8",null)},"class":$lzc$class_ml},{attrs:{$classrootdepth:1,name:"topright",x:new LzAlwaysExpr("x","numberExpression","$m9","$ma",null),y:new LzAlwaysExpr("y","numberExpression","$mb","$mc",null)},"class":$lzc$class_mm},{attrs:{$classrootdepth:1,name:"bottomleft",x:new LzAlwaysExpr("x","numberExpression","$md","$me",null),y:new LzAlwaysExpr("y","numberExpression","$mf","$mg",null)},"class":$lzc$class_mn},{attrs:{$classrootdepth:1,name:"bottomright",x:new LzAlwaysExpr("x","numberExpression","$mh","$mi",null),y:new LzAlwaysExpr("y","numberExpression","$mj","$mk",null)},"class":$lzc$class_mo}],"attributes",new LzInheritedHash($lzc$class_basefocusview.attributes)]);(function($0){
with($0)with($0.prototype){
{
LzNode.mergeAttributes({offset:5},$lzc$class_focusoverlay.attributes)
}}})($lzc$class_focusoverlay)
};{
Class.make("$lzc$class__componentmanager",["focusclass",void 0,"keyhandlers",void 0,"lastsdown",void 0,"lastedown",void 0,"defaults",void 0,"currentdefault",void 0,"defaultstyle",void 0,"ondefaultstyle",void 0,"init",function(){
with(this){
var $0=this.focusclass;if(typeof canvas.focusclass!="undefined"){
$0=canvas.focusclass
};if($0!=null){
canvas.__focus=new (lz[$0])(canvas);canvas.__focus.reset()
};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["init"]||this.nextMethod(arguments.callee,"init")).call(this)
}},"_lastkeydown",void 0,"upkeydel",void 0,"$mp",function(){
with(this){
return lz.Keys
}},"dispatchKeyDown",function($0){
with(this){
var $1=false;if($0==32){
this.lastsdown=null;var $2=lz.Focus.getFocus();if($2 instanceof lz.basecomponent){
$2.doSpaceDown();this.lastsdown=$2
};$1=true
}else if($0==13&&this.currentdefault){
this.lastedown=this.currentdefault;this.currentdefault.doEnterDown();$1=true
};if($1){
if(!this.upkeydel)this.upkeydel=new LzDelegate(this,"dispatchKeyTimer");this._lastkeydown=$0;lz.Timer.addTimer(this.upkeydel,50)
}}},"dispatchKeyTimer",function($0){
if(this._lastkeydown==32&&this.lastsdown!=null){
this.lastsdown.doSpaceUp();this.lastsdown=null
}else if(this._lastkeydown==13&&this.currentdefault&&this.currentdefault==this.lastedown){
this.currentdefault.doEnterUp()
}},"findClosestDefault",function($0){
with(this){
if(!this.defaults){
return null
};var $1=null;var $2=null;var $3=this.defaults;$0=$0||canvas;var $4=lz.ModeManager.getModalView();for(var $5=0;$5<$3.length;$5++){
var $6=$3[$5];if($4&&!$6.childOf($4)){
continue
};var $7=this.findCommonParent($6,$0);if($7&&(!$1||$7.nodeLevel>$1.nodeLevel)){
$1=$7;$2=$6
}};return $2
}},"findCommonParent",function($0,$1){
while($0.nodeLevel>$1.nodeLevel){
$0=$0.immediateparent;if(!$0.visible)return null
};while($1.nodeLevel>$0.nodeLevel){
$1=$1.immediateparent;if(!$1.visible)return null
};while($0!=$1){
$0=$0.immediateparent;$1=$1.immediateparent;if(!$0.visible||!$1.visible)return null
};return $0
},"makeDefault",function($0){
with(this){
if(!this.defaults)this.defaults=[];this.defaults.push($0);this.checkDefault(lz.Focus.getFocus())
}},"unmakeDefault",function($0){
with(this){
if(!this.defaults)return;for(var $1=0;$1<this.defaults.length;$1++){
if(this.defaults[$1]==$0){
this.defaults.splice($1,1);this.checkDefault(lz.Focus.getFocus());return
}}}},"$mq",function(){
with(this){
return lz.Focus
}},"checkDefault",function($0){
with(this){
if(!($0 instanceof lz.basecomponent)||!$0.doesenter){
if($0 instanceof lz.inputtext&&$0.multiline){
$0=null
}else{
$0=this.findClosestDefault($0)
}};if($0==this.currentdefault)return;if(this.currentdefault){
this.currentdefault.setAttribute("hasdefault",false)
};this.currentdefault=$0;if($0){
$0.setAttribute("hasdefault",true)
}}},"$mr",function(){
with(this){
return lz.ModeManager
}},"$ms",function($0){
with(this){
switch(arguments.length){
case 0:
$0=null;

};if(lz.Focus.getFocus()==null){
this.checkDefault(null)
}}},"setDefaultStyle",function($0){
this.defaultstyle=$0;if(this.ondefaultstyle)this.ondefaultstyle.sendEvent($0)
},"getDefaultStyle",function(){
with(this){
if(this.defaultstyle==null){
this.defaultstyle=new (lz.style)(canvas,{isdefault:true})
};return this.defaultstyle
}},"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzNode,["tagname","_componentmanager","attributes",new LzInheritedHash(LzNode.attributes)]);(function($0){
with($0)with($0.prototype){
{
LzNode.mergeAttributes({$delegates:["onkeydown","dispatchKeyDown","$mp","onfocus","checkDefault","$mq","onmode","$ms","$mr"],_lastkeydown:0,currentdefault:null,defaults:null,defaultstyle:null,focusclass:"focusoverlay",keyhandlers:null,lastedown:null,lastsdown:null,ondefaultstyle:LzDeclaredEvent,upkeydel:null},$lzc$class__componentmanager.attributes)
}}})($lzc$class__componentmanager)
};{
Class.make("$lzc$class_style",["isstyle",void 0,"$mt",function($0){
with(this){
this.setAttribute("canvascolor",LzColorUtils.convertColor("null"))
}},"canvascolor",void 0,"$lzc$set_canvascolor",function($0){
with(this){
setCanvasColor($0)
}},"$mu",function($0){
with(this){
this.setAttribute("textcolor",LzColorUtils.convertColor("gray10"))
}},"textcolor",void 0,"$lzc$set_textcolor",function($0){
with(this){
setStyleAttr($0,"textcolor")
}},"$mv",function($0){
with(this){
this.setAttribute("textfieldcolor",LzColorUtils.convertColor("white"))
}},"textfieldcolor",void 0,"$lzc$set_textfieldcolor",function($0){
with(this){
setStyleAttr($0,"textfieldcolor")
}},"$mw",function($0){
with(this){
this.setAttribute("texthilitecolor",LzColorUtils.convertColor("iceblue1"))
}},"texthilitecolor",void 0,"$lzc$set_texthilitecolor",function($0){
with(this){
setStyleAttr($0,"texthilitecolor")
}},"$mx",function($0){
with(this){
this.setAttribute("textselectedcolor",LzColorUtils.convertColor("black"))
}},"textselectedcolor",void 0,"$lzc$set_textselectedcolor",function($0){
with(this){
setStyleAttr($0,"textselectedcolor")
}},"$my",function($0){
with(this){
this.setAttribute("textdisabledcolor",LzColorUtils.convertColor("gray60"))
}},"textdisabledcolor",void 0,"$lzc$set_textdisabledcolor",function($0){
with(this){
setStyleAttr($0,"textdisabledcolor")
}},"$mz",function($0){
with(this){
this.setAttribute("basecolor",LzColorUtils.convertColor("offwhite"))
}},"basecolor",void 0,"$lzc$set_basecolor",function($0){
with(this){
setStyleAttr($0,"basecolor")
}},"$m10",function($0){
with(this){
this.setAttribute("bgcolor",LzColorUtils.convertColor("white"))
}},"bgcolor",void 0,"$lzc$set_bgcolor",function($0){
with(this){
setStyleAttr($0,"bgcolor")
}},"$m11",function($0){
with(this){
this.setAttribute("hilitecolor",LzColorUtils.convertColor("iceblue4"))
}},"hilitecolor",void 0,"$lzc$set_hilitecolor",function($0){
with(this){
setStyleAttr($0,"hilitecolor")
}},"$m12",function($0){
with(this){
this.setAttribute("selectedcolor",LzColorUtils.convertColor("iceblue3"))
}},"selectedcolor",void 0,"$lzc$set_selectedcolor",function($0){
with(this){
setStyleAttr($0,"selectedcolor")
}},"$m13",function($0){
with(this){
this.setAttribute("disabledcolor",LzColorUtils.convertColor("gray30"))
}},"disabledcolor",void 0,"$lzc$set_disabledcolor",function($0){
with(this){
setStyleAttr($0,"disabledcolor")
}},"$m14",function($0){
with(this){
this.setAttribute("bordercolor",LzColorUtils.convertColor("gray40"))
}},"bordercolor",void 0,"$lzc$set_bordercolor",function($0){
with(this){
setStyleAttr($0,"bordercolor")
}},"$m15",function($0){
this.setAttribute("bordersize",1)
},"bordersize",void 0,"$lzc$set_bordersize",function($0){
with(this){
setStyleAttr($0,"bordersize")
}},"$m16",function($0){
with(this){
this.setAttribute("menuitembgcolor",LzColorUtils.convertColor("textfieldcolor"))
}},"menuitembgcolor",void 0,"isdefault",void 0,"$lzc$set_isdefault",function($0){
with(this){
_setdefault($0)
}},"onisdefault",void 0,"_setdefault",function($0){
with(this){
this.isdefault=$0;if(isdefault){
lz._componentmanager.service.setDefaultStyle(this);if(this["canvascolor"]!=null){
canvas.setAttribute("bgcolor",this.canvascolor)
}};if(this.onisdefault)this.onisdefault.sendEvent(this)
}},"onstylechanged",void 0,"setStyleAttr",function($0,$1){
this[$1]=$0;if(this["on"+$1])this["on"+$1].sendEvent($1);if(this.onstylechanged)this.onstylechanged.sendEvent(this)
},"setCanvasColor",function($0){
with(this){
if(this.isdefault&&$0!=null){
canvas.setAttribute("bgcolor",$0)
};this.canvascolor=$0;if(this.onstylechanged)this.onstylechanged.sendEvent(this)
}},"extend",function($0){
with(this){
var $1=new (lz.style)();$1.canvascolor=this.canvascolor;$1.textcolor=this.textcolor;$1.textfieldcolor=this.textfieldcolor;$1.texthilitecolor=this.texthilitecolor;$1.textselectedcolor=this.textselectedcolor;$1.textdisabledcolor=this.textdisabledcolor;$1.basecolor=this.basecolor;$1.bgcolor=this.bgcolor;$1.hilitecolor=this.hilitecolor;$1.selectedcolor=this.selectedcolor;$1.disabledcolor=this.disabledcolor;$1.bordercolor=this.bordercolor;$1.bordersize=this.bordersize;$1.menuitembgcolor=this.menuitembgcolor;$1.isdefault=this.isdefault;for(var $2 in $0){
$1[$2]=$0[$2]
};new LzDelegate($1,"_forwardstylechanged",this,"onstylechanged");return $1
}},"_forwardstylechanged",function($0){
if(this.onstylechanged)this.onstylechanged.sendEvent(this)
},"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzNode,["tagname","style","attributes",new LzInheritedHash(LzNode.attributes)]);(function($0){
with($0)with($0.prototype){
{
LzNode.mergeAttributes({basecolor:new LzOnceExpr("basecolor","color","$mz",null),bgcolor:new LzOnceExpr("bgcolor","color","$m10",null),bordercolor:new LzOnceExpr("bordercolor","color","$m14",null),bordersize:new LzOnceExpr("bordersize","number","$m15",null),canvascolor:new LzOnceExpr("canvascolor","color","$mt",null),disabledcolor:new LzOnceExpr("disabledcolor","color","$m13",null),hilitecolor:new LzOnceExpr("hilitecolor","color","$m11",null),isdefault:false,isstyle:true,menuitembgcolor:new LzOnceExpr("menuitembgcolor","color","$m16",null),onisdefault:LzDeclaredEvent,onstylechanged:LzDeclaredEvent,selectedcolor:new LzOnceExpr("selectedcolor","color","$m12",null),textcolor:new LzOnceExpr("textcolor","color","$mu",null),textdisabledcolor:new LzOnceExpr("textdisabledcolor","color","$my",null),textfieldcolor:new LzOnceExpr("textfieldcolor","color","$mv",null),texthilitecolor:new LzOnceExpr("texthilitecolor","color","$mw",null),textselectedcolor:new LzOnceExpr("textselectedcolor","color","$mx",null)},$lzc$class_style.attributes)
}}})($lzc$class_style)
};canvas.LzInstantiateView({"class":lz.script,attrs:{script:function(){
lz._componentmanager.service=new (lz._componentmanager)(canvas,null,null,true)
}}},1);Class.make("$lzc$class_statictext",["$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzText,["tagname","statictext","attributes",new LzInheritedHash(LzText.attributes)]);{
Class.make("$lzc$class_basecomponent",["enabled",void 0,"$lzc$set_focusable",function($0){
with(this){
_setFocusable($0)
}},"_focusable",void 0,"onfocusable",void 0,"text",void 0,"doesenter",void 0,"$lzc$set_doesenter",function($0){
this._setDoesEnter($0)
},"$m17",function($0){
var $1=this.enabled&&(this._parentcomponent?this._parentcomponent._enabled:true);if($1!==this["_enabled"]||!this.inited){
this.setAttribute("_enabled",$1)
}},"$m18",function(){
try{
return [this,"enabled",this,"_parentcomponent",this._parentcomponent,"_enabled"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}},"_enabled",void 0,"$lzc$set__enabled",function($0){
this._setEnabled($0)
},"_parentcomponent",void 0,"_initcomplete",void 0,"isdefault",void 0,"$lzc$set_isdefault",function($0){
this._setIsDefault($0)
},"onisdefault",void 0,"hasdefault",void 0,"_setEnabled",function($0){
with(this){
this._enabled=$0;var $1=this._enabled&&this._focusable;if($1!=this.focusable){
this.focusable=$1;if(this.onfocusable.ready)this.onfocusable.sendEvent()
};if(_initcomplete)_showEnabled();if(this.on_enabled.ready)this.on_enabled.sendEvent()
}},"_setFocusable",function($0){
this._focusable=$0;if(this.enabled){
this.focusable=this._focusable;if(this.onfocusable.ready)this.onfocusable.sendEvent()
}else{
this.focusable=false
}},"construct",function($0,$1){
with(this){
(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["construct"]||this.nextMethod(arguments.callee,"construct")).call(this,$0,$1);var $2=this.immediateparent;while($2!=canvas){
if(lz.basecomponent["$lzsc$isa"]?lz.basecomponent.$lzsc$isa($2):$2 instanceof lz.basecomponent){
this._parentcomponent=$2;break
};$2=$2.immediateparent
}}},"init",function(){
with(this){
(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["init"]||this.nextMethod(arguments.callee,"init")).call(this);this._initcomplete=true;this._mousedownDel=new LzDelegate(this,"_doMousedown",this,"onmousedown");if(this.styleable){
_usestyle()
};if(!this["_enabled"])_showEnabled()
}},"_doMousedown",function($0){},"doSpaceDown",function(){
return false
},"doSpaceUp",function(){
return false
},"doEnterDown",function(){
return false
},"doEnterUp",function(){
return false
},"_setIsDefault",function($0){
with(this){
this.isdefault=this["isdefault"]==true;if(this.isdefault==$0)return;if($0){
lz._componentmanager.service.makeDefault(this)
}else{
lz._componentmanager.service.unmakeDefault(this)
};this.isdefault=$0;if(this.onisdefault.ready){
this.onisdefault.sendEvent($0)
}}},"_setDoesEnter",function($0){
with(this){
this.doesenter=$0;if(lz.Focus.getFocus()==this){
lz._componentmanager.service.checkDefault(this)
}}},"updateDefault",function(){
with(this){
lz._componentmanager.service.checkDefault(lz.Focus.getFocus())
}},"$m19",function($0){
this.setAttribute("style",null)
},"style",void 0,"$lzc$set_style",function($0){
with(this){
styleable?setStyle($0):(this.style=null)
}},"styleable",void 0,"_style",void 0,"onstyle",void 0,"_styledel",void 0,"_otherstyledel",void 0,"setStyle",function($0){
with(this){
if(!styleable)return;if($0!=null&&!$0["isstyle"]){
var $1=this._style;if(!$1){
if(this._parentcomponent){
$1=this._parentcomponent.style
}else $1=lz._componentmanager.service.getDefaultStyle()
};$0=$1.extend($0)
};this._style=$0;if($0==null){
if(!this._otherstyledel){
this._otherstyledel=new LzDelegate(this,"_setstyle")
}else{
this._otherstyledel.unregisterAll()
};if(this._parentcomponent&&this._parentcomponent.styleable){
this._otherstyledel.register(this._parentcomponent,"onstyle");$0=this._parentcomponent.style
}else{
this._otherstyledel.register(lz._componentmanager.service,"ondefaultstyle");$0=lz._componentmanager.service.getDefaultStyle()
}}else if(this._otherstyledel){
this._otherstyledel.unregisterAll();this._otherstyledel=null
};_setstyle($0)
}},"_usestyle",function($0){
switch(arguments.length){
case 0:
$0=null;

};if(this._initcomplete&&this["style"]&&this.style.isinited){
this._applystyle(this.style)
}},"_setstyle",function($0){
with(this){
if(!this._styledel){
this._styledel=new LzDelegate(this,"_usestyle")
}else{
_styledel.unregisterAll()
};if($0){
_styledel.register($0,"onstylechanged")
};this.style=$0;_usestyle();if(this.onstyle.ready)this.onstyle.sendEvent(this.style)
}},"_applystyle",function($0){},"setTint",function($0,$1,$2){
switch(arguments.length){
case 2:
$2=0;

};if($0.capabilities.colortransform){
if($1!=""&&$1!=null){
var $3=$1;var $4=$3>>16&255;var $5=$3>>8&255;var $6=$3&255;$4+=51;$5+=51;$6+=51;$4=$4/255;$5=$5/255;$6=$6/255;$0.setAttribute("colortransform",{redMultiplier:$4,greenMultiplier:$5,blueMultiplier:$6,redOffset:$2,greenOffset:$2,blueOffset:$2})
}}},"on_enabled",void 0,"_showEnabled",function(){},"acceptValue",function($0,$1){
switch(arguments.length){
case 1:
$1=null;

};this.setAttribute("text",$0)
},"presentValue",function($0){
switch(arguments.length){
case 0:
$0=null;

};return this.text
},"$lzc$presentValue_dependencies",function($0,$1,$2){
switch(arguments.length){
case 2:
$2=null;

};return [this,"text"]
},"applyData",function($0){
this.acceptValue($0)
},"updateData",function(){
return this.presentValue()
},"destroy",function(){
with(this){
this.styleable=false;this._initcomplete=false;if(this["isdefault"]&&this.isdefault){
lz._componentmanager.service.unmakeDefault(this)
};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["destroy"]||this.nextMethod(arguments.callee,"destroy")).call(this)
}},"toString",function(){
var $0="";var $1="";var $2="";if(this["id"]!=null)$0="  id="+this.id;if(this["name"]!=null)$1=' named "'+this.name+'"';if(this["text"]&&this.text!="")$2="  text="+this.text;return this.constructor.tagname+$1+$0+$2
},"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzView,["tagname","basecomponent","attributes",new LzInheritedHash(LzView.attributes)]);(function($0){
with($0)with($0.prototype){
{
LzNode.mergeAttributes({_enabled:new LzAlwaysExpr("_enabled","expression","$m17","$m18",null),_focusable:true,_initcomplete:false,_otherstyledel:null,_parentcomponent:null,_style:null,_styledel:null,doesenter:false,enabled:true,focusable:true,hasdefault:false,on_enabled:LzDeclaredEvent,onfocusable:LzDeclaredEvent,onisdefault:LzDeclaredEvent,onstyle:LzDeclaredEvent,style:new LzOnceExpr("style","expression","$m19",null),styleable:true,text:""},$lzc$class_basecomponent.attributes)
}}})($lzc$class_basecomponent)
};{
Class.make("$lzc$class_basebutton",["normalResourceNumber",void 0,"overResourceNumber",void 0,"downResourceNumber",void 0,"disabledResourceNumber",void 0,"$m1a",function($0){
this.setAttribute("maxframes",this.totalframes)
},"maxframes",void 0,"resourceviewcount",void 0,"$lzc$set_resourceviewcount",function($0){
this.setResourceViewCount($0)
},"respondtomouseout",void 0,"$m1b",function($0){
this.setAttribute("reference",this)
},"reference",void 0,"$lzc$set_reference",function($0){
with(this){
setreference($0)
}},"onresourceviewcount",void 0,"_msdown",void 0,"_msin",void 0,"setResourceViewCount",function($0){
this.resourceviewcount=$0;if(this._initcomplete){
if($0>0){
if(this.subviews){
this.maxframes=this.subviews[0].totalframes;if(this.onresourceviewcount){
this.onresourceviewcount.sendEvent()
}}}}},"_callShow",function(){
if(this._msdown&&this._msin&&this.maxframes>=this.downResourceNumber){
this.showDown()
}else if(this._msin&&this.maxframes>=this.overResourceNumber){
this.showOver()
}else this.showUp()
},"$m1c",function(){
with(this){
return lz.ModeManager
}},"$m1d",function($0){
if($0&&(this._msdown||this._msin)&&!this.childOf($0)){
this._msdown=false;this._msin=false;this._callShow()
}},"$lzc$set_frame",function($0){
with(this){
if(this.resourceviewcount>0){
for(var $1=0;$1<resourceviewcount;$1++){
this.subviews[$1].setAttribute("frame",$0)
}}else{
(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzc$set_frame"]||this.nextMethod(arguments.callee,"$lzc$set_frame")).call(this,$0)
}}},"doSpaceDown",function(){
if(this._enabled){
this.showDown()
}},"doSpaceUp",function(){
if(this._enabled){
this.onclick.sendEvent();this.showUp()
}},"doEnterDown",function(){
if(this._enabled){
this.showDown()
}},"doEnterUp",function(){
if(this._enabled){
if(this.onclick){
this.onclick.sendEvent()
};this.showUp()
}},"$m1e",function($0){
if(this.isinited){
this.maxframes=this.totalframes;this._callShow()
}},"init",function(){
(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["init"]||this.nextMethod(arguments.callee,"init")).call(this);this.setResourceViewCount(this.resourceviewcount);this._callShow()
},"$m1f",function($0){
this.setAttribute("_msin",true);this._callShow()
},"$m1g",function($0){
this.setAttribute("_msin",false);this._callShow()
},"$m1h",function($0){
this.setAttribute("_msdown",true);this._callShow()
},"$m1i",function($0){
this.setAttribute("_msdown",false);this._callShow()
},"_showEnabled",function(){
with(this){
reference.setAttribute("clickable",this._enabled);showUp()
}},"showDown",function($0){
switch(arguments.length){
case 0:
$0=null;

};this.setAttribute("frame",this.downResourceNumber)
},"showUp",function($0){
switch(arguments.length){
case 0:
$0=null;

};if(!this._enabled&&this.disabledResourceNumber){
this.setAttribute("frame",this.disabledResourceNumber)
}else{
this.setAttribute("frame",this.normalResourceNumber)
}},"showOver",function($0){
switch(arguments.length){
case 0:
$0=null;

};this.setAttribute("frame",this.overResourceNumber)
},"setreference",function($0){
this.reference=$0;if($0!=this)this.setAttribute("clickable",false)
},"_applystyle",function($0){
with(this){
setTint(this,$0.basecolor)
}},"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],$lzc$class_basecomponent,["tagname","basebutton","attributes",new LzInheritedHash($lzc$class_basecomponent.attributes)]);(function($0){
with($0)with($0.prototype){
{
LzNode.mergeAttributes({$delegates:["onmode","$m1d","$m1c","ontotalframes","$m1e",null,"onmouseover","$m1f",null,"onmouseout","$m1g",null,"onmousedown","$m1h",null,"onmouseup","$m1i",null],_msdown:false,_msin:false,clickable:true,disabledResourceNumber:4,downResourceNumber:3,focusable:false,maxframes:new LzOnceExpr("maxframes","number","$m1a",null),normalResourceNumber:1,onclick:LzDeclaredEvent,onresourceviewcount:LzDeclaredEvent,overResourceNumber:2,reference:new LzOnceExpr("reference","expression","$m1b",null),resourceviewcount:0,respondtomouseout:true,styleable:false},$lzc$class_basebutton.attributes)
}}})($lzc$class_basebutton)
};{
Class.make("$lzc$class_swatchview",["ctransform",void 0,"color",void 0,"construct",function($0,$1){
with(this){
(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["construct"]||this.nextMethod(arguments.callee,"construct")).call(this,$0,$1);this.capabilities=new LzInheritedHash(this.capabilities);this.capabilities.colortransform=true;if($1["width"]==null){
$1["width"]=this.immediateparent.width
};if($1["height"]==null){
$1["height"]=this.immediateparent.height
};if($1["fgcolor"]==null&&$1["bgcolor"]==null){
$1["fgcolor"]=16777215
}}},"$lzc$set_fgcolor",function($0){
this.setAttribute("bgcolor",$0)
},"$lzc$set_bgcolor",function($0){
with(this){
this.color=$0;if(this["ctransform"]!=null){
$0=LzColorUtils.applyTransform($0,ctransform)
};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzc$set_bgcolor"]||this.nextMethod(arguments.callee,"$lzc$set_bgcolor")).call(this,$0)
}},"$lzc$set_colortransform",function($0){
this.ctransform=$0;this.setAttribute("bgcolor",this.color)
},"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzView,["tagname","swatchview","attributes",new LzInheritedHash(LzView.attributes)]);(function($0){
with($0)with($0.prototype){
{
LzNode.mergeAttributes({color:16777215,ctransform:null},$lzc$class_swatchview.attributes)
}}})($lzc$class_swatchview)
};Class.make("$lzc$class_m2s",["$m1u",function($0){
with(this){
var $1=parent.width-1;if($1!==this["width"]||!this.inited){
this.setAttribute("width",$1)
}}},"$m1v",function(){
with(this){
try{
return [parent,"width"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$m1w",function($0){
with(this){
var $1=parent.height-1;if($1!==this["height"]||!this.inited){
this.setAttribute("height",$1)
}}},"$m1x",function(){
with(this){
try{
return [parent,"height"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$classrootdepth",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzView,["displayName","<anonymous extends='view'>","attributes",new LzInheritedHash(LzView.attributes)]);Class.make("$lzc$class_m2t",["$m1y",function($0){
with(this){
var $1=parent.width-3;if($1!==this["width"]||!this.inited){
this.setAttribute("width",$1)
}}},"$m1z",function(){
with(this){
try{
return [parent,"width"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$m20",function($0){
with(this){
var $1=parent.height-3;if($1!==this["height"]||!this.inited){
this.setAttribute("height",$1)
}}},"$m21",function(){
with(this){
try{
return [parent,"height"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$classrootdepth",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzView,["displayName","<anonymous extends='view'>","attributes",new LzInheritedHash(LzView.attributes)]);Class.make("$lzc$class_m2u",["$m22",function($0){
with(this){
var $1=parent.width-4;if($1!==this["width"]||!this.inited){
this.setAttribute("width",$1)
}}},"$m23",function(){
with(this){
try{
return [parent,"width"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$m24",function($0){
with(this){
var $1=parent.height-4;if($1!==this["height"]||!this.inited){
this.setAttribute("height",$1)
}}},"$m25",function(){
with(this){
try{
return [parent,"height"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$classrootdepth",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzView,["displayName","<anonymous extends='view'>","attributes",new LzInheritedHash(LzView.attributes)]);Class.make("$lzc$class_m2v",["$m26",function($0){
with(this){
var $1=parent.parent.width-2;if($1!==this["x"]||!this.inited){
this.setAttribute("x",$1)
}}},"$m27",function(){
with(this){
try{
return [parent.parent,"width"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$m28",function($0){
with(this){
var $1=parent.parent.height-2;if($1!==this["height"]||!this.inited){
this.setAttribute("height",$1)
}}},"$m29",function(){
with(this){
try{
return [parent.parent,"height"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$classrootdepth",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzView,["displayName","<anonymous extends='view'>","attributes",new LzInheritedHash(LzView.attributes)]);Class.make("$lzc$class_m2w",["$m2a",function($0){
with(this){
var $1=parent.parent.height-2;if($1!==this["y"]||!this.inited){
this.setAttribute("y",$1)
}}},"$m2b",function(){
with(this){
try{
return [parent.parent,"height"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$m2c",function($0){
with(this){
var $1=parent.parent.width-3;if($1!==this["width"]||!this.inited){
this.setAttribute("width",$1)
}}},"$m2d",function(){
with(this){
try{
return [parent.parent,"width"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$classrootdepth",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzView,["displayName","<anonymous extends='view'>","attributes",new LzInheritedHash(LzView.attributes)]);Class.make("$lzc$class_m2x",["$m2e",function($0){
with(this){
var $1=parent.parent.width-1;if($1!==this["x"]||!this.inited){
this.setAttribute("x",$1)
}}},"$m2f",function(){
with(this){
try{
return [parent.parent,"width"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$m2g",function($0){
with(this){
var $1=parent.parent.height;if($1!==this["height"]||!this.inited){
this.setAttribute("height",$1)
}}},"$m2h",function(){
with(this){
try{
return [parent.parent,"height"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$classrootdepth",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzView,["displayName","<anonymous extends='view'>","attributes",new LzInheritedHash(LzView.attributes)]);Class.make("$lzc$class_m2y",["$m2i",function($0){
with(this){
var $1=parent.parent.height-1;if($1!==this["y"]||!this.inited){
this.setAttribute("y",$1)
}}},"$m2j",function(){
with(this){
try{
return [parent.parent,"height"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$m2k",function($0){
with(this){
var $1=parent.parent.width-1;if($1!==this["width"]||!this.inited){
this.setAttribute("width",$1)
}}},"$m2l",function(){
with(this){
try{
return [parent.parent,"width"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$classrootdepth",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzView,["displayName","<anonymous extends='view'>","attributes",new LzInheritedHash(LzView.attributes)]);Class.make("$lzc$class_m2z",["$m2m",function($0){
with(this){
var $1=parent.text_x+parent.titleshift;if($1!==this["x"]||!this.inited){
this.setAttribute("x",$1)
}}},"$m2n",function(){
with(this){
try{
return [parent,"text_x",parent,"titleshift"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$m2o",function($0){
with(this){
var $1=parent.text_y+parent.titleshift;if($1!==this["y"]||!this.inited){
this.setAttribute("y",$1)
}}},"$m2p",function(){
with(this){
try{
return [parent,"text_y",parent,"titleshift"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$m2q",function($0){
with(this){
var $1=parent.text;if($1!==this["text"]||!this.inited){
this.setAttribute("text",$1)
}}},"$m2r",function(){
with(this){
try{
return [parent,"text"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$classrootdepth",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzText,["displayName","<anonymous extends='text'>","attributes",new LzInheritedHash(LzText.attributes)]);{
Class.make("$lzc$class_button",["text_padding_x",void 0,"text_padding_y",void 0,"$m1j",function($0){
var $1=this.width/2-this._title.width/2;if($1!==this["text_x"]||!this.inited){
this.setAttribute("text_x",$1)
}},"$m1k",function(){
try{
return [this,"width",this._title,"width"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}},"text_x",void 0,"$m1l",function($0){
var $1=this.height/2-this._title.height/2;if($1!==this["text_y"]||!this.inited){
this.setAttribute("text_y",$1)
}},"$m1m",function(){
try{
return [this,"height",this._title,"height"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}},"text_y",void 0,"$m1n",function($0){
var $1=this._title.width+2*this.text_padding_x;if($1!==this["width"]||!this.inited){
this.setAttribute("width",$1)
}},"$m1o",function(){
try{
return [this._title,"width",this,"text_padding_x"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}},"$m1p",function($0){
var $1=this._title.height+2*this.text_padding_y;if($1!==this["height"]||!this.inited){
this.setAttribute("height",$1)
}},"$m1q",function(){
try{
return [this._title,"height",this,"text_padding_y"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}},"buttonstate",void 0,"$m1r",function($0){
var $1=this.buttonstate==1?0:1;if($1!==this["titleshift"]||!this.inited){
this.setAttribute("titleshift",$1)
}},"$m1s",function(){
try{
return [this,"buttonstate"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}},"titleshift",void 0,"leftalign",void 0,"_showEnabled",function(){
with(this){
showUp();setAttribute("clickable",_enabled)
}},"showDown",function($0){
with(this){
switch(arguments.length){
case 0:
$0=null;

};if(this.hasdefault){
this._outerbezel.setAttribute("frame",5)
}else{
this._outerbezel.setAttribute("frame",this.downResourceNumber)
};this._face.setAttribute("frame",this.downResourceNumber);this._innerbezel.setAttribute("frame",this.downResourceNumber);setAttribute("buttonstate",2)
}},"showUp",function($0){
with(this){
switch(arguments.length){
case 0:
$0=null;

};if(_enabled){
if(this.hasdefault){
this._outerbezel.setAttribute("frame",5)
}else{
this._outerbezel.setAttribute("frame",this.normalResourceNumber)
};this._face.setAttribute("frame",this.normalResourceNumber);this._innerbezel.setAttribute("frame",this.normalResourceNumber);if(this.style)this._title.setAttribute("fgcolor",this.style.textcolor)
}else{
if(this.style)this._title.setAttribute("fgcolor",this.style.textdisabledcolor);this._face.setAttribute("frame",this.disabledResourceNumber);this._outerbezel.setAttribute("frame",this.disabledResourceNumber);this._innerbezel.setAttribute("frame",this.disabledResourceNumber)
};setAttribute("buttonstate",1)
}},"showOver",function($0){
with(this){
switch(arguments.length){
case 0:
$0=null;

};if(this.hasdefault){
this._outerbezel.setAttribute("frame",5)
}else{
this._outerbezel.setAttribute("frame",this.overResourceNumber)
};this._face.setAttribute("frame",this.overResourceNumber);this._innerbezel.setAttribute("frame",this.overResourceNumber);setAttribute("buttonstate",1)
}},"$m1t",function($0){
with(this){
if(this._initcomplete){
if(this.buttonstate==1)showUp()
}}},"_applystyle",function($0){
with(this){
if(this.style!=null){
this.textcolor=$0.textcolor;this.textdisabledcolor=$0.textdisabledcolor;if(enabled){
_title.setAttribute("fgcolor",$0.textcolor)
}else{
_title.setAttribute("fgcolor",$0.textdisabledcolor)
};setTint(_outerbezel,$0.basecolor);setTint(_innerbezel,$0.basecolor);setTint(_face,$0.basecolor)
}}},"_outerbezel",void 0,"_innerbezel",void 0,"_face",void 0,"_innerbezelbottom",void 0,"_outerbezelbottom",void 0,"_title",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],$lzc$class_basebutton,["tagname","button","children",[{attrs:{$classrootdepth:1,bgcolor:LzColorUtils.convertColor("0x919191"),height:new LzAlwaysExpr("height","size","$m1w","$m1x",null),name:"_outerbezel",width:new LzAlwaysExpr("width","size","$m1u","$m1v",null),x:0,y:0},"class":$lzc$class_m2s},{attrs:{$classrootdepth:1,bgcolor:LzColorUtils.convertColor("0xffffff"),height:new LzAlwaysExpr("height","size","$m20","$m21",null),name:"_innerbezel",width:new LzAlwaysExpr("width","size","$m1y","$m1z",null),x:1,y:1},"class":$lzc$class_m2t},{attrs:{$classrootdepth:1,height:new LzAlwaysExpr("height","size","$m24","$m25",null),name:"_face",resource:"lzbutton_face_rsc",stretches:"both",width:new LzAlwaysExpr("width","size","$m22","$m23",null),x:2,y:2},"class":$lzc$class_m2u},{attrs:{$classrootdepth:1,name:"_innerbezelbottom"},children:[{attrs:{$classrootdepth:2,bgcolor:LzColorUtils.convertColor("0x585858"),height:new LzAlwaysExpr("height","size","$m28","$m29",null),width:1,x:new LzAlwaysExpr("x","numberExpression","$m26","$m27",null),y:1},"class":$lzc$class_m2v},{attrs:{$classrootdepth:2,bgcolor:LzColorUtils.convertColor("0x585858"),height:1,width:new LzAlwaysExpr("width","size","$m2c","$m2d",null),x:1,y:new LzAlwaysExpr("y","numberExpression","$m2a","$m2b",null)},"class":$lzc$class_m2w}],"class":LzView},{attrs:{$classrootdepth:1,name:"_outerbezelbottom"},children:[{attrs:{$classrootdepth:2,bgcolor:LzColorUtils.convertColor("0xffffff"),height:new LzAlwaysExpr("height","size","$m2g","$m2h",null),opacity:0.7,width:1,x:new LzAlwaysExpr("x","numberExpression","$m2e","$m2f",null),y:0},"class":$lzc$class_m2x},{attrs:{$classrootdepth:2,bgcolor:LzColorUtils.convertColor("0xffffff"),height:1,opacity:0.7,width:new LzAlwaysExpr("width","size","$m2k","$m2l",null),x:0,y:new LzAlwaysExpr("y","numberExpression","$m2i","$m2j",null)},"class":$lzc$class_m2y}],"class":LzView},{attrs:{$classrootdepth:1,name:"_title",resize:true,text:new LzAlwaysExpr("text","text","$m2q","$m2r",null),x:new LzAlwaysExpr("x","numberExpression","$m2m","$m2n",null),y:new LzAlwaysExpr("y","numberExpression","$m2o","$m2p",null)},"class":$lzc$class_m2z}],"attributes",new LzInheritedHash($lzc$class_basebutton.attributes)]);(function($0){
with($0)with($0.prototype){
{
LzNode.mergeAttributes({$delegates:["onhasdefault","$m1t",null],buttonstate:1,clickable:true,doesenter:true,focusable:true,height:new LzAlwaysExpr("height","size","$m1p","$m1q",null),leftalign:false,maxframes:4,pixellock:true,styleable:true,text_padding_x:11,text_padding_y:4,text_x:new LzAlwaysExpr("text_x","number","$m1j","$m1k",null),text_y:new LzAlwaysExpr("text_y","number","$m1l","$m1m",null),titleshift:new LzAlwaysExpr("titleshift","number","$m1r","$m1s",null),width:new LzAlwaysExpr("width","size","$m1n","$m1o",null)},$lzc$class_button.attributes)
}}})($lzc$class_button)
};{
Class.make("$lzc$class_basevaluecomponent",["value",void 0,"type",void 0,"getValue",function(){
return this.value==null?this.text:this.value
},"$lzc$getValue_dependencies",function($0,$1){
return [this,"value",this,"text"]
},"acceptValue",function($0,$1){
switch(arguments.length){
case 1:
$1=null;

};if($1==null)$1=this.type;(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["acceptValue"]||this.nextMethod(arguments.callee,"acceptValue")).call(this,$0,$1);this.acceptAttribute("value",$1,$0)
},"presentValue",function($0){
with(this){
switch(arguments.length){
case 0:
$0=null;

};if($0==null)$0=this.type;return lz.Type.presentTypeValue($0,this.getValue(),this,"value")
}},"$lzc$presentValue_dependencies",function($0,$1,$2){
switch(arguments.length){
case 2:
$2=null;

};return [this,"type"].concat(this.$lzc$getValue_dependencies($0,$1))
},"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],$lzc$class_basecomponent,["tagname","basevaluecomponent","attributes",new LzInheritedHash($lzc$class_basecomponent.attributes)]);(function($0){
with($0)with($0.prototype){
{
LzNode.mergeAttributes({type:"expression",value:null},$lzc$class_basevaluecomponent.attributes)
}}})($lzc$class_basevaluecomponent)
};{
Class.make("$lzc$class_baseformitem",["_parentform",void 0,"submitname",void 0,"$m30",function($0){
with(this){
var $1=enabled;if($1!==this["submit"]||!this.inited){
this.setAttribute("submit",$1)
}}},"$m31",function(){
try{
return [this,"enabled"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}},"submit",void 0,"changed",void 0,"$lzc$set_changed",function($0){
this.setChanged($0)
},"$lzc$set_value",function($0){
this.setValue($0,false)
},"onchanged",void 0,"onvalue",void 0,"rollbackvalue",void 0,"ignoreform",void 0,"init",function(){
if(this.submitname=="")this.submitname=this.name;if(this.submitname==""){};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["init"]||this.nextMethod(arguments.callee,"init")).call(this);var $0=this.findForm();if($0!=null){
$0.addFormItem(this);this._parentform=$0
}},"destroy",function(){
if(this._parentform)this._parentform.removeFormItem(this);(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["destroy"]||this.nextMethod(arguments.callee,"destroy")).call(this)
},"setChanged",function($0,$1){
with(this){
switch(arguments.length){
case 1:
$1=null;

};if(!this._initcomplete){
this.changed=false;return
};var $2=this.changed;this.changed=$0;if(this.changed!=$2){
if(this.onchanged)this.onchanged.sendEvent(this.changed)
};if(!$1&&this.changed&&!ignoreform){
if(this["_parentform"]&&this._parentform["changed"]!=undefined&&!this._parentform.changed){
this._parentform.setChanged($0,false)
}};if(!$1&&!this.changed&&!ignoreform){
if(this["_parentform"]&&this._parentform["changed"]!=undefined&&this._parentform.changed){
this._parentform.setChanged($0,true)
}}}},"rollback",function(){
if(this.rollbackvalue!=this["value"]){
this.setAttribute("value",this.rollbackvalue)
};this.setAttribute("changed",false)
},"commit",function(){
this.rollbackvalue=this.value;this.setAttribute("changed",false)
},"setValue",function($0,$1){
switch(arguments.length){
case 1:
$1=null;

};var $2=this.value!=$0;this.value=$0;if($1||!this._initcomplete){
this.rollbackvalue=$0
};this.setChanged($2&&!$1&&this.rollbackvalue!=$0);if(this["onvalue"])this.onvalue.sendEvent($0)
},"acceptValue",function($0,$1){
with(this){
switch(arguments.length){
case 1:
$1=null;

};if($1==null)$1=this.type;this.setValue(lz.Type.acceptTypeValue($1,$0,this,"value"),true)
}},"findForm",function(){
with(this){
if(_parentform!=null){
return _parentform
}else{
var $0=this.immediateparent;var $1=null;while($0!=canvas){
if($0["formdata"]){
$1=$0;break
};$0=$0.immediateparent
};return $1
}}},"toXML",function($0){
with(this){
var $1=this.value;if($0){
if(typeof $1=="boolean")$1=$1-0
};return lz.Browser.xmlEscape(this.submitname)+'="'+lz.Browser.xmlEscape($1)+'"'
}},"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],$lzc$class_basevaluecomponent,["tagname","baseformitem","attributes",new LzInheritedHash($lzc$class_basevaluecomponent.attributes)]);(function($0){
with($0)with($0.prototype){
{
LzNode.mergeAttributes({_parentform:null,changed:false,ignoreform:false,onchanged:LzDeclaredEvent,onvalue:LzDeclaredEvent,rollbackvalue:null,submit:new LzAlwaysExpr("submit","boolean","$m30","$m31",null),submitname:"",value:null},$lzc$class_baseformitem.attributes)
}}})($lzc$class_baseformitem)
};Class.make("$lzc$class__internalinputtext",["construct",function($0,$1){
if($0["textwidth"]!=null)$1.textwidth=$0.textwidth;if($0["_initialtext"]!=null)$1.text=$0._initialtext;if($0["password"]!=null)$1.password=$0.password;if($0["multiline"]!=null)$1.multiline=$0.multiline;(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["construct"]||this.nextMethod(arguments.callee,"construct")).call(this,$0,$1)
},"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzInputText,["tagname","_internalinputtext","attributes",new LzInheritedHash(LzInputText.attributes)]);Class.make("$lzc$class_m4r",["$m3a",function($0){
with(this){
var $1=immediateparent.width;if($1!==this["width"]||!this.inited){
this.setAttribute("width",$1)
}}},"$m3b",function(){
with(this){
try{
return [immediateparent,"width"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$classrootdepth",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzView,["displayName","<anonymous extends='view'>","attributes",new LzInheritedHash(LzView.attributes)]);Class.make("$lzc$class_m4s",["$m3c",function($0){
with(this){
var $1=immediateparent.height;if($1!==this["height"]||!this.inited){
this.setAttribute("height",$1)
}}},"$m3d",function(){
with(this){
try{
return [immediateparent,"height"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$classrootdepth",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzView,["displayName","<anonymous extends='view'>","attributes",new LzInheritedHash(LzView.attributes)]);Class.make("$lzc$class_m4t",["$m3e",function($0){
with(this){
var $1=immediateparent.height;if($1!==this["height"]||!this.inited){
this.setAttribute("height",$1)
}}},"$m3f",function(){
with(this){
try{
return [immediateparent,"height"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$classrootdepth",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzView,["displayName","<anonymous extends='view'>","attributes",new LzInheritedHash(LzView.attributes)]);Class.make("$lzc$class_m4u",["$m3g",function($0){
with(this){
var $1=immediateparent.width;if($1!==this["width"]||!this.inited){
this.setAttribute("width",$1)
}}},"$m3h",function(){
with(this){
try{
return [immediateparent,"width"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$classrootdepth",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzView,["displayName","<anonymous extends='view'>","attributes",new LzInheritedHash(LzView.attributes)]);Class.make("$lzc$class_m4q",["$m34",function($0){
with(this){
var $1=parent.width;if($1!==this["width"]||!this.inited){
this.setAttribute("width",$1)
}}},"$m35",function(){
with(this){
try{
return [parent,"width"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$m36",function($0){
with(this){
var $1=parent.height;if($1!==this["height"]||!this.inited){
this.setAttribute("height",$1)
}}},"$m37",function(){
with(this){
try{
return [parent,"height"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$classrootdepth",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzView,["displayName","<anonymous extends='view'>","children",[{attrs:{$classrootdepth:2,$m38:function($0){
var $1=this.classroot.enabled;if($1!==this["applied"]||!this.inited){
this.setAttribute("applied",$1)
}},$m39:function(){
try{
return [this.classroot,"enabled"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}},applied:new LzAlwaysExpr("applied","boolean","$m38","$m39",null),pooling:true},children:[{attrs:{$classrootdepth:2,bgcolor:LzColorUtils.convertColor("0x989898"),height:1,width:new LzAlwaysExpr("width","size","$m3a","$m3b",null)},"class":$lzc$class_m4r},{attrs:{$classrootdepth:2,bgcolor:LzColorUtils.convertColor("0x989898"),height:new LzAlwaysExpr("height","size","$m3c","$m3d",null),width:1},"class":$lzc$class_m4s},{attrs:{$classrootdepth:2,align:"right",bgcolor:LzColorUtils.convertColor("0xe1e1e1"),height:new LzAlwaysExpr("height","size","$m3e","$m3f",null),width:1},"class":$lzc$class_m4t},{attrs:{$classrootdepth:2,bgcolor:LzColorUtils.convertColor("0xe1e1e1"),height:1,valign:"bottom",width:new LzAlwaysExpr("width","size","$m3g","$m3h",null)},"class":$lzc$class_m4u}],"class":LzState}],"attributes",new LzInheritedHash(LzView.attributes)]);Class.make("$lzc$class_m4w",["$m3o",function($0){
with(this){
var $1=parent.width-1;if($1!==this["width"]||!this.inited){
this.setAttribute("width",$1)
}}},"$m3p",function(){
with(this){
try{
return [parent,"width"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$classrootdepth",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzView,["displayName","<anonymous extends='view'>","attributes",new LzInheritedHash(LzView.attributes)]);Class.make("$lzc$class_m4x",["$m3q",function($0){
with(this){
var $1=immediateparent.height;if($1!==this["height"]||!this.inited){
this.setAttribute("height",$1)
}}},"$m3r",function(){
with(this){
try{
return [immediateparent,"height"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$classrootdepth",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzView,["displayName","<anonymous extends='view'>","attributes",new LzInheritedHash(LzView.attributes)]);Class.make("$lzc$class_m4y",["$m3s",function($0){
with(this){
var $1=immediateparent.width;if($1!==this["width"]||!this.inited){
this.setAttribute("width",$1)
}}},"$m3t",function(){
with(this){
try{
return [immediateparent,"width"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$classrootdepth",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzView,["displayName","<anonymous extends='view'>","attributes",new LzInheritedHash(LzView.attributes)]);Class.make("$lzc$class_m4z",["$m3w",function($0){
with(this){
var $1=immediateparent.width;if($1!==this["width"]||!this.inited){
this.setAttribute("width",$1)
}}},"$m3x",function(){
with(this){
try{
return [immediateparent,"width"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$m3y",function($0){
with(this){
var $1=immediateparent.height;if($1!==this["height"]||!this.inited){
this.setAttribute("height",$1)
}}},"$m3z",function(){
with(this){
try{
return [immediateparent,"height"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$classrootdepth",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzView,["displayName","<anonymous extends='view'>","attributes",new LzInheritedHash(LzView.attributes)]);Class.make("$lzc$class_m50",["$m40",function($0){
with(this){
var $1=parent.width-2;if($1!==this["width"]||!this.inited){
this.setAttribute("width",$1)
}}},"$m41",function(){
with(this){
try{
return [parent,"width"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$m42",function($0){
with(this){
var $1=parent.height-2;if($1!==this["height"]||!this.inited){
this.setAttribute("height",$1)
}}},"$m43",function(){
with(this){
try{
return [parent,"height"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$classrootdepth",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzView,["displayName","<anonymous extends='view'>","attributes",new LzInheritedHash(LzView.attributes)]);Class.make("$lzc$class_m4v",["$m3i",function($0){
with(this){
var $1=parent.width-2;if($1!==this["width"]||!this.inited){
this.setAttribute("width",$1)
}}},"$m3j",function(){
with(this){
try{
return [parent,"width"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$m3k",function($0){
with(this){
var $1=parent.height-2;if($1!==this["height"]||!this.inited){
this.setAttribute("height",$1)
}}},"$m3l",function(){
with(this){
try{
return [parent,"height"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$classrootdepth",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzView,["displayName","<anonymous extends='view'>","children",[{attrs:{$classrootdepth:2,$m3m:function($0){
var $1=this.classroot.enabled;if($1!==this["applied"]||!this.inited){
this.setAttribute("applied",$1)
}},$m3n:function(){
try{
return [this.classroot,"enabled"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}},applied:new LzAlwaysExpr("applied","boolean","$m3m","$m3n",null),pooling:true},children:[{attrs:{$classrootdepth:2,bgcolor:LzColorUtils.convertColor("0x262626"),height:1,width:new LzAlwaysExpr("width","size","$m3o","$m3p",null)},"class":$lzc$class_m4w},{attrs:{$classrootdepth:2,bgcolor:LzColorUtils.convertColor("0x262626"),height:new LzAlwaysExpr("height","size","$m3q","$m3r",null),width:1},"class":$lzc$class_m4x},{attrs:{$classrootdepth:2,bgcolor:LzColorUtils.convertColor("0xefefef"),height:1,valign:"bottom",width:new LzAlwaysExpr("width","size","$m3s","$m3t",null)},"class":$lzc$class_m4y}],"class":LzState},{attrs:{$classrootdepth:2,$m3u:function($0){
var $1=!this.classroot.enabled;if($1!==this["applied"]||!this.inited){
this.setAttribute("applied",$1)
}},$m3v:function(){
try{
return [this.classroot,"enabled"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}},applied:new LzAlwaysExpr("applied","boolean","$m3u","$m3v",null),pooling:true},children:[{attrs:{$classrootdepth:2,bgcolor:LzColorUtils.convertColor("0xa4a4a4"),height:new LzAlwaysExpr("height","size","$m3y","$m3z",null),width:new LzAlwaysExpr("width","size","$m3w","$m3x",null)},"class":$lzc$class_m4z},{attrs:{$classrootdepth:2,bgcolor:LzColorUtils.convertColor("0xffffff"),height:new LzAlwaysExpr("height","size","$m42","$m43",null),width:new LzAlwaysExpr("width","size","$m40","$m41",null),x:1,y:1},"class":$lzc$class_m50}],"class":LzState}],"attributes",new LzInheritedHash(LzView.attributes)]);Class.make("$lzc$class_m51",["$m44",function($0){
with(this){
var $1=parent.width-3;if($1!==this["width"]||!this.inited){
this.setAttribute("width",$1)
}}},"$m45",function(){
with(this){
try{
return [parent,"width"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$m46",function($0){
with(this){
var $1=parent.height-4;if($1!==this["height"]||!this.inited){
this.setAttribute("height",$1)
}}},"$m47",function(){
with(this){
try{
return [parent,"height"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$classrootdepth",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzView,["displayName","<anonymous extends='view'>","attributes",new LzInheritedHash(LzView.attributes)]);Class.make("$lzc$class_m52",["$m48",function($0){
var $1=this.classroot.text_y;if($1!==this["y"]||!this.inited){
this.setAttribute("y",$1)
}},"$m49",function(){
try{
return [this.classroot,"text_y"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}},"$m4a",function($0){
with(this){
var $1=parent.width-4;if($1!==this["width"]||!this.inited){
this.setAttribute("width",$1)
}}},"$m4b",function(){
with(this){
try{
return [parent,"width"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$m4c",function($0){
with(this){
var $1=parent.height-y-2;if($1!==this["height"]||!this.inited){
this.setAttribute("height",$1)
}}},"$m4d",function(){
with(this){
try{
return [parent,"height",this,"y"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$classrootdepth",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzView,["displayName","<anonymous extends='view'>","attributes",new LzInheritedHash(LzView.attributes)]);Class.make("$lzc$class_m53",["$m4e",function($0){
with(this){
this.setAttribute("password",parent.password)
}},"$m4f",function($0){
var $1=this.classroot.text_y;if($1!==this["y"]||!this.inited){
this.setAttribute("y",$1)
}},"$m4g",function(){
try{
return [this.classroot,"text_y"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}},"$m4h",function($0){
with(this){
var $1=parent.width-4;if($1!==this["width"]||!this.inited){
this.setAttribute("width",$1)
}}},"$m4i",function(){
with(this){
try{
return [parent,"width"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$m4j",function($0){
with(this){
var $1=parent.height-y-2;if($1!==this["height"]||!this.inited){
this.setAttribute("height",$1)
}}},"$m4k",function(){
with(this){
try{
return [parent,"height",this,"y"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$m4l",function($0){
with(this){
if(parent["onfocus"])parent.onfocus.sendEvent($0)
}},"$m4m",function($0){
with(this){
if(parent["onblur"])parent.onblur.sendEvent($0)
}},"$m4n",function($0){
with(this){
if(parent["onkeyup"])parent.onkeyup.sendEvent($0);if($0==13&&parent.doesenter)parent.doEnterUp()
}},"$m4o",function($0){
with(this){
if(parent["onkeydown"])parent.onkeydown.sendEvent($0);if($0==13&&parent.doesenter)parent.doEnterDown()
}},"getFocusRect",function(){
with(this){
return parent.getFocusRect()
}},"$m4p",function($0){
with(this){
parent.text=this.getText();if(parent["ontext"]&&parent.ontext.ready)parent.ontext.sendEvent(parent.text);parent.setValue(parent.text,false)
}},"$classrootdepth",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],$lzc$class__internalinputtext,["displayName","<anonymous extends='_internalinputtext'>","attributes",new LzInheritedHash($lzc$class__internalinputtext.attributes)]);{
Class.make("$lzc$class_edittext",["multiline",void 0,"password",void 0,"resizable",void 0,"$m32",function($0){
with(this){
var $1=multiline?2:Math.round((this.height-this.field.getTextHeight())/2);if($1!==this["text_y"]||!this.inited){
this.setAttribute("text_y",$1)
}}},"$m33",function(){
with(this){
try{
return [this,"multiline",this,"height"].concat($lzc$getFunctionDependencies("getTextHeight",this,this.field,[],null))
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"text_y",void 0,"maxlength",void 0,"pattern",void 0,"$lzc$set_fgcolor",function($0){},"_fgcolor",void 0,"_initialtext",void 0,"init",function(){
with(this){
if(!this.hassetwidth){
if(typeof this.textwidth=="undefined"){
this.textwidth=100
};setAttribute("width",this.textwidth+6)
};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["init"]||this.nextMethod(arguments.callee,"init")).call(this);if(this._initialtext!=null){
this.setAttribute("text",this._initialtext)
};field.setAttribute("maxlength",this.maxlength);field.setAttribute("pattern",this.pattern)
}},"setText",function($0){
this.setAttribute("text",$0)
},"$lzc$set_text",function($0){
if(this._initcomplete){
this.setValue($0,true);this.field.setAttribute("text",$0)
}else{
this.text=$0;this._initialtext=$0
}},"clearText",function(){
this.setAttribute("text","")
},"setMaxlength",function($0){
this.setAttribute("maxlength",$0)
},"setPattern",function($0){
this.setAttribute("pattern",$0)
},"$lzc$set_maxlength",function($0){
with(this){
if(this["maxlength"]===$0)return;this.maxlength=$0;if(this.isinited)field.setAttribute("maxlength",$0);if(this["onmaxlength"])this.onmaxlength.sendEvent($0)
}},"$lzc$set_pattern",function($0){
with(this){
if(this["pattern"]===$0)return;this.pattern=$0;if(this.isinited)field.setAttribute("pattern",$0);if(this["onpattern"])this.onpattern.sendEvent($0)
}},"setSelection",function($0,$1){
with(this){
switch(arguments.length){
case 1:
$1=null;

};field.setSelection($0,$1)
}},"getFocusRect",function(){
with(this){
var $0=this.getAttributeRelative("x",canvas);var $1=this.getAttributeRelative("y",canvas);var $2=this.getAttributeRelative("width",canvas);var $3=this.getAttributeRelative("height",canvas);return [$0,$1,$2,$3]
}},"_outerbezel",void 0,"_innerbezel",void 0,"_face",void 0,"$lzc$set_font",function($0){
with(this){
(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzc$set_font"]||this.nextMethod(arguments.callee,"$lzc$set_font")).call(this,$0);if(this["field"])field.setAttribute("font",$0)
}},"$lzc$set_fontsize",function($0){
with(this){
(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzc$set_fontsize"]||this.nextMethod(arguments.callee,"$lzc$set_fontsize")).call(this,$0);if(this["field"])field.setAttribute("fontsize",$0)
}},"$lzc$set_fontstyle",function($0){
with(this){
(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzc$set_fontstyle"]||this.nextMethod(arguments.callee,"$lzc$set_fontstyle")).call(this,$0);if(this["field"])field.setAttribute("fontstyle",$0)
}},"content",void 0,"field",void 0,"getText",function(){
if(this._initcomplete){
return this.field.getText()
}else{
return this._initialtext
}},"applyData",function($0){
this.field.applyData($0)
},"acceptValue",function($0,$1){
switch(arguments.length){
case 1:
$1=null;

};this.applyData($0)
},"updateData",function(){
this.updateText();return this.text
},"updateText",function(){
this.setAttribute("text",this.field.getText())
},"getValue",function(){
return this.field.getText()
},"$lzc$getValue_dependencies",function($0,$1){
$1=this.field;return $1.$lzc$getText_dependencies($0,$1)
},"_showEnabled",function(){
with(this){
if(_enabled){
this.field.setAttribute("enabled",true);this.field.setAttribute("fgcolor",this.style!=null?this.style.textcolor:null);this._face.setAttribute("opacity",1);this._outerbezel.setAttribute("frame",1);this._innerbezel.setAttribute("frame",1)
}else{
this.field.setAttribute("enabled",false);this.field.setAttribute("width",this.width-6);this.field.setAttribute("height",this.height-6);this.field.setAttribute("fgcolor",this.style!=null?this.style.textdisabledcolor:null);this._face.setAttribute("opacity",0.65);this._outerbezel.setAttribute("frame",2);this._innerbezel.setAttribute("frame",2)
}}},"_applystyle",function($0){
with(this){
if(this.style!=null){
_face.setAttribute("bgcolor",$0.textfieldcolor)
}}},"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],$lzc$class_baseformitem,["tagname","edittext","children",[{attrs:{$classrootdepth:1,height:new LzAlwaysExpr("height","size","$m36","$m37",null),name:"_outerbezel",width:new LzAlwaysExpr("width","size","$m34","$m35",null)},"class":$lzc$class_m4q},{attrs:{$classrootdepth:1,height:new LzAlwaysExpr("height","size","$m3k","$m3l",null),name:"_innerbezel",width:new LzAlwaysExpr("width","size","$m3i","$m3j",null),x:1,y:1},"class":$lzc$class_m4v},{attrs:{$classrootdepth:1,height:new LzAlwaysExpr("height","size","$m46","$m47",null),name:"_face",width:new LzAlwaysExpr("width","size","$m44","$m45",null),x:2,y:2},"class":$lzc$class_m51},{attrs:{$classrootdepth:1,height:new LzAlwaysExpr("height","size","$m4c","$m4d",null),name:"content",width:new LzAlwaysExpr("width","size","$m4a","$m4b",null),x:3,y:new LzAlwaysExpr("y","numberExpression","$m48","$m49",null)},"class":$lzc$class_m52},{attrs:{$classrootdepth:1,$delegates:["onfocus","$m4l",null,"onblur","$m4m",null,"onkeyup","$m4n",null,"onkeydown","$m4o",null,"ontext","$m4p",null],height:new LzAlwaysExpr("height","size","$m4j","$m4k",null),name:"field",password:new LzOnceExpr("password","boolean","$m4e",null),width:new LzAlwaysExpr("width","size","$m4h","$m4i",null),x:3,y:new LzAlwaysExpr("y","numberExpression","$m4f","$m4g",null)},"class":$lzc$class_m53},{attrs:"content","class":$lzc$class_userClassPlacement}],"attributes",new LzInheritedHash($lzc$class_baseformitem.attributes)]);(function($0){
with($0)with($0.prototype){
{
LzNode.mergeAttributes({_fgcolor:0,_initialtext:"",fgcolor:LzColorUtils.convertColor("0x0"),focusable:false,height:26,maxlength:null,multiline:false,password:false,pattern:"",resizable:false,text_y:new LzAlwaysExpr("text_y","number","$m32","$m33",null),width:106},$lzc$class_edittext.attributes)
}}})($lzc$class_edittext)
};{
Class.make("LzLayout",["vip",void 0,"locked",void 0,"$lzc$set_locked",function($0){
if(this.locked==$0)return;if($0){
this.lock()
}else{
this.unlock()
}},"subviews",void 0,"updateDelegate",void 0,"construct",function($0,$1){
with(this){
this.locked=2;(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["construct"]||this.nextMethod(arguments.callee,"construct")).call(this,$0,$1);this.subviews=[];this.vip=this.immediateparent;if(this.vip.layouts==null){
this.vip.layouts=[this]
}else{
this.vip.layouts.push(this)
};this.updateDelegate=new LzDelegate(this,"update");if(this.immediateparent.isinited){
this.__parentInit()
}else{
new LzDelegate(this,"__parentInit",this.immediateparent,"oninit")
}}},"$m54",function($0){
with(this){
new LzDelegate(this,"gotNewSubview",this.vip,"onaddsubview");new LzDelegate(this,"removeSubview",this.vip,"onremovesubview");var $1=this.vip.subviews.length;for(var $2=0;$2<$1;$2++){
this.gotNewSubview(this.vip.subviews[$2])
}}},"destroy",function(){
if(this.__LZdeleted)return;this.releaseLayout(true);(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["destroy"]||this.nextMethod(arguments.callee,"destroy")).call(this)
},"reset",function($0){
switch(arguments.length){
case 0:
$0=null;

};if(this.locked){
return
};this.update($0)
},"addSubview",function($0){
var $1=$0.options["layoutAfter"];if($1){
this.__LZinsertAfter($0,$1)
}else{
this.subviews.push($0)
}},"gotNewSubview",function($0){
if(!$0.options["ignorelayout"]){
this.addSubview($0)
}},"removeSubview",function($0){
var $1=this.subviews;for(var $2=$1.length-1;$2>=0;$2--){
if($1[$2]==$0){
$1.splice($2,1);break
}};this.reset()
},"ignore",function($0){
var $1=this.subviews;for(var $2=$1.length-1;$2>=0;$2--){
if($1[$2]==$0){
$1.splice($2,1);break
}};this.reset()
},"lock",function(){
this.locked=true
},"unlock",function($0){
switch(arguments.length){
case 0:
$0=null;

};this.locked=false;this.reset()
},"__parentInit",function($0){
with(this){
switch(arguments.length){
case 0:
$0=null;

};if(this.locked==2){
if(this.isinited){
this.unlock()
}else{
new LzDelegate(this,"unlock",this,"oninit")
}}}},"releaseLayout",function($0){
switch(arguments.length){
case 0:
$0=null;

};if($0==null&&this.__delegates!=null)this.removeDelegates();if(this.immediateparent&&this.vip.layouts){
for(var $1=this.vip.layouts.length-1;$1>=0;$1--){
if(this.vip.layouts[$1]==this){
this.vip.layouts.splice($1,1)
}}}},"setLayoutOrder",function($0,$1){
var $2=this.subviews;for(var $3=$2.length-1;$3>=0;$3--){
if($2[$3]===$1){
$2.splice($3,1);break
}};if($3==-1){
return
};if($0=="first"){
$2.unshift($1)
}else if($0=="last"){
$2.push($1)
}else{
for(var $4=$2.length-1;$4>=0;$4--){
if($2[$4]===$0){
$2.splice($4+1,0,$1);break
}};if($4==-1){
$2.splice($3,0,$1)
}};this.reset();return
},"swapSubviewOrder",function($0,$1){
var $2=-1;var $3=-1;var $4=this.subviews;for(var $5=$4.length-1;$5>=0&&($2<0||$3<0);$5--){
if($4[$5]===$0){
$2=$5
};if($4[$5]===$1){
$3=$5
}};if($2>=0&&$3>=0){
$4[$3]=$0;$4[$2]=$1
};this.reset();return
},"__LZinsertAfter",function($0,$1){
var $2=this.subviews;for(var $3=$2.length-1;$3>=0;$3--){
if($2[$3]==$1){
$2.splice($3,0,$0)
}}},"update",function($0){
switch(arguments.length){
case 0:
$0=null;

}},"toString",function(){
return "lz.layout for view "+this.immediateparent
},"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzNode,["tagname","layout","attributes",new LzInheritedHash(LzNode.attributes)]);(function($0){
with($0)with($0.prototype){
{
LzNode.mergeAttributes({$delegates:["onconstruct","$m54",null],locked:2},LzLayout.attributes)
}}})(LzLayout)
};{
Class.make("$lzc$class_simplelayout",["axis",void 0,"$lzc$set_axis",function($0){
this.setAxis($0)
},"inset",void 0,"$lzc$set_inset",function($0){
this.inset=$0;if(this.subviews&&this.subviews.length)this.update();if(this["oninset"])this.oninset.sendEvent(this.inset)
},"spacing",void 0,"$lzc$set_spacing",function($0){
this.spacing=$0;if(this.subviews&&this.subviews.length)this.update();if(this["onspacing"])this.onspacing.sendEvent(this.spacing)
},"setAxis",function($0){
if(this["axis"]==null||this.axis!=$0){
this.axis=$0;this.sizeAxis=$0=="x"?"width":"height";if(this.subviews.length)this.update();if(this["onaxis"])this.onaxis.sendEvent(this.axis)
}},"addSubview",function($0){
this.updateDelegate.register($0,"on"+this.sizeAxis);this.updateDelegate.register($0,"onvisible");if(!this.locked){
var $1=null;var $2=this.subviews;for(var $3=$2.length-1;$3>=0;--$3){
if($2[$3].visible){
$1=$2[$3];break
}};if($1){
var $4=$1[this.axis]+$1[this.sizeAxis]+this.spacing
}else{
var $4=this.inset
};$0.setAttribute(this.axis,$4)
};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["addSubview"]||this.nextMethod(arguments.callee,"addSubview")).call(this,$0)
},"update",function($0){
switch(arguments.length){
case 0:
$0=null;

};if(this.locked)return;var $1=this.subviews.length;var $2=this.inset;for(var $3=0;$3<$1;$3++){
var $4=this.subviews[$3];if(!$4.visible)continue;if($4[this.axis]!=$2){
$4.setAttribute(this.axis,$2)
};if($4.usegetbounds){
$4=$4.getBounds()
};$2+=this.spacing+$4[this.sizeAxis]
}},"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzLayout,["tagname","simplelayout","attributes",new LzInheritedHash(LzLayout.attributes)]);(function($0){
with($0)with($0.prototype){
{
LzNode.mergeAttributes({axis:"y",inset:0,spacing:0},$lzc$class_simplelayout.attributes)
}}})($lzc$class_simplelayout)
};loginDs=canvas.lzAddLocalData("loginDs",'<data><tmssvc_request>\n\t<login_request user_name="" password="" access_code="" sds_date_time="2007-01-29T10:44:07" sds_id="string" is_reopen="true" />\n</tmssvc_request></data>',false,false);loginDs==true;;(function(){
var $0=LzCSSStyle,$1=LzCSSStyleRule;$0._addRule(new $1({s:1,t:"text"},{font:new LzStyleIdent("sans-serif")}))
})();Class.make("$lzc$class_m5i",["$m58",function($0){
with(this){
var $1=282-width;if($1!==this["x"]||!this.inited){
this.setAttribute("x",$1)
}}},"$m59",function(){
try{
return [this,"width"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}},"$classrootdepth",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzText,["displayName","<anonymous extends='text'>","attributes",new LzInheritedHash(LzText.attributes)]);Class.make("$lzc$class_m5h",["$m56",function($0){
with(this){
var $1=immediateparent.width;if($1!==this["width"]||!this.inited){
this.setAttribute("width",$1)
}}},"$m57",function(){
with(this){
try{
return [immediateparent,"width"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"loginIdField",void 0,"$classrootdepth",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzView,["displayName","<anonymous extends='view'>","children",[{attrs:{$classrootdepth:2,fgcolor:LzColorUtils.convertColor("0xffffff"),fontsize:12,resize:true,text:"<b>Login ID:</b>",valign:"middle",x:new LzAlwaysExpr("x","numberExpression","$m58","$m59",null)},"class":$lzc$class_m5i},{attrs:{$classrootdepth:2,$lzc$bind_id:function($0,$1){
switch(arguments.length){
case 1:
$1=true;

};if($1){
$0.id="loginId";loginId=$0
}else if(loginId===$0){
loginId=null;$0.id=null
}},fontsize:12,id:"loginId",name:"loginIdField",pattern:"[a-zA-Z0-9_-]*",width:265,x:287},"class":$lzc$class_edittext}],"attributes",new LzInheritedHash(LzView.attributes)]);Class.make("$lzc$class_m5j",["$m5a",function($0){
with(this){
var $1=282-width;if($1!==this["x"]||!this.inited){
this.setAttribute("x",$1)
}}},"$m5b",function(){
try{
return [this,"width"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}},"$classrootdepth",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzText,["displayName","<anonymous extends='text'>","attributes",new LzInheritedHash(LzText.attributes)]);Class.make("$lzc$class_m5k",["$m5c",function($0){
with(this){
var $1=282-width;if($1!==this["x"]||!this.inited){
this.setAttribute("x",$1)
}}},"$m5d",function(){
try{
return [this,"width"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}},"$classrootdepth",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzText,["displayName","<anonymous extends='text'>","attributes",new LzInheritedHash(LzText.attributes)]);Class.make("$lzc$class_m5m",["$m5g",function($0){
with(this){
login()
}},"login",function(){
with(this){
classroot.loginAction()
}},"$classrootdepth",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],$lzc$class_button,["displayName","<anonymous extends='button'>","children",LzNode.mergeChildren([],$lzc$class_button["children"]),"attributes",new LzInheritedHash($lzc$class_button.attributes)]);Class.make("$lzc$class_m5l",["$m5e",function($0){
with(this){
var $1=parent.ac.accesscodeField.x;if($1!==this["x"]||!this.inited){
this.setAttribute("x",$1)
}}},"$m5f",function(){
with(this){
try{
return [parent.ac.accesscodeField,"x"]
}
catch($lzsc$e){
if(Error["$lzsc$isa"]?Error.$lzsc$isa($lzsc$e):$lzsc$e instanceof Error){
lz.$lzsc$thrownError=$lzsc$e
};throw $lzsc$e
}}},"$classrootdepth",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzView,["displayName","<anonymous extends='view'>","children",[{attrs:{$classrootdepth:2,axis:"x",spacing:10},"class":$lzc$class_simplelayout},{attrs:{$classrootdepth:2,$delegates:["onclick","$m5g",null],clickable:true,text:"Login",y:3},"class":$lzc$class_m5m},{attrs:{$classrootdepth:2,text:"Cancel",y:3},"class":$lzc$class_button}],"attributes",new LzInheritedHash(LzView.attributes)]);{
Class.make("$lzc$class_login",["loginAction",function(){
with(this){
Debug.write("loginAction request");var $0=getLoginXml(li.loginIdField.getText(),pw.passwordField.getText(),ac.accesscodeField.getText(),12345678);doScriptRequest("LOGIN",$0)
}},"getLoginXml",function($0,$1,$2,$3){
with(this){
dp.setXPath("loginDs:/tmssvc_request/login_request");dp.setNodeAttribute("user_name",$0);dp.setNodeAttribute("password",$1);dp.setNodeAttribute("access_code",$2);dp.setNodeAttribute("cid",$3);dp.setXPath("loginDs:/tmssvc_request");return dp.serialize()
}},"doScriptRequest",function($0,$1){
with(this){
var $2="javascript:sendRequest('"+$0+"','"+$1+"')";lz.Browser.loadJS($2)
}},"$m55",function($0){
with(this){
this.setAttribute("dp",new LzDatapointer())
}},"dp",void 0,"li",void 0,"pw",void 0,"ac",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzView,["tagname","login","children",[{attrs:{$classrootdepth:1,axis:"y",spacing:5},"class":$lzc$class_simplelayout},{attrs:{$classrootdepth:1,fgcolor:LzColorUtils.convertColor("0xffffff"),fontsize:24,resize:true,text:"Hello, please log in to begin"},"class":LzText},{attrs:{$classrootdepth:1,$lzc$bind_id:function($0,$1){
switch(arguments.length){
case 1:
$1=true;

};if($1){
$0.id="li";li=$0
}else if(li===$0){
li=null;$0.id=null
}},id:"li",loginIdField:void 0,name:"li",width:new LzAlwaysExpr("width","size","$m56","$m57",null)},"class":$lzc$class_m5h},{attrs:{$classrootdepth:1,name:"pw",passwordField:void 0},children:[{attrs:{$classrootdepth:2,fgcolor:LzColorUtils.convertColor("0xffffff"),fontsize:12,resize:true,text:"<b>Password:</b>",valign:"middle",x:new LzAlwaysExpr("x","numberExpression","$m5a","$m5b",null)},"class":$lzc$class_m5j},{attrs:{$classrootdepth:2,fontsize:12,name:"passwordField",password:true,pattern:"[a-zA-Z0-9_]*",width:265,x:287},"class":$lzc$class_edittext}],"class":LzView},{attrs:{$classrootdepth:1,accesscodeField:void 0,name:"ac"},children:[{attrs:{$classrootdepth:2,fgcolor:LzColorUtils.convertColor("0xffffff"),fontsize:12,resize:true,text:"<b>Access Code:</b>",valign:"middle",x:new LzAlwaysExpr("x","numberExpression","$m5c","$m5d",null)},"class":$lzc$class_m5k},{attrs:{$classrootdepth:2,fontsize:12,name:"accesscodeField",pattern:"[a-zA-Z0-9_]*",width:265,x:287},"class":$lzc$class_edittext}],"class":LzView},{attrs:{$classrootdepth:1,x:new LzAlwaysExpr("x","numberExpression","$m5e","$m5f",null)},"class":$lzc$class_m5l}],"attributes",new LzInheritedHash(LzView.attributes)]);(function($0){
with($0)with($0.prototype){
{
LzNode.mergeAttributes({$lzc$bind_id:function($0,$1){
switch(arguments.length){
case 1:
$1=true;

};if($1){
$0.id="login";login=$0
}else if(login===$0){
login=null;$0.id=null
}},dp:new LzOnceExpr("dp","expression","$m55",null),id:"login"},$lzc$class_login.attributes)
}}})($lzc$class_login)
};{
Class.make("$lzc$class_Main",["show",function($0){
with(this){
if($0){
screen.success.setAttribute("visible",true);screen.failure.setAttribute("visible",false);screen.success.setText($0)
}else{
screen.failure.setAttribute("visible",true);screen.success.setAttribute("visible",false);screen.success.setText($0)
}}},"screen",void 0,"$lzsc$initialize",function($0,$1,$2,$3){
switch(arguments.length){
case 0:
$0=null;
case 1:
$1=null;
case 2:
$2=null;
case 3:
$3=false;

};(arguments.callee["$superclass"]&&arguments.callee.$superclass.prototype["$lzsc$initialize"]||this.nextMethod(arguments.callee,"$lzsc$initialize")).call(this,$0,$1,$2,$3)
}],LzView,["tagname","Main","children",[{attrs:{$classrootdepth:1,failure:void 0,name:"screen",success:void 0,x:50,y:100},children:[{attrs:{$classrootdepth:2,align:"center",fgcolor:LzColorUtils.convertColor("0xffffff"),fontsize:25,name:"success",text:'<font size="16" color="white">Success!!!</font>',visible:false},"class":LzText},{attrs:{$classrootdepth:2,align:"center",fgcolor:LzColorUtils.convertColor("0xffffff"),fontsize:25,name:"failure",text:'<font size="16" color="white">Failure!!!</font>',visible:false},"class":LzText}],"class":LzView}],"attributes",new LzInheritedHash(LzView.attributes)]);(function($0){
with($0)with($0.prototype){
{
LzNode.mergeAttributes({$lzc$bind_id:function($0,$1){
switch(arguments.length){
case 1:
$1=true;

};if($1){
$0.id="Main";Main=$0
}else if(Main===$0){
Main=null;$0.id=null
}},id:"Main"},$lzc$class_Main.attributes)
}}})($lzc$class_Main)
};canvas.LzInstantiateView({attrs:{$lzc$bind_name:function($0,$1){
switch(arguments.length){
case 1:
$1=true;

};if($1){
loginMain=$0
}else if(loginMain===$0){
loginMain=null
}},align:"center",name:"loginMain",valign:"middle",visible:true},"class":$lzc$class_login},60);canvas.LzInstantiateView({attrs:{$lzc$bind_name:function($0,$1){
switch(arguments.length){
case 1:
$1=true;

};if($1){
main=$0
}else if(main===$0){
main=null
}},name:"main",visible:false},"class":$lzc$class_Main},4);lz["basefocusview"]=$lzc$class_basefocusview;lz["focusoverlay"]=$lzc$class_focusoverlay;lz["_componentmanager"]=$lzc$class__componentmanager;lz["style"]=$lzc$class_style;lz["statictext"]=$lzc$class_statictext;lz["basecomponent"]=$lzc$class_basecomponent;lz["basebutton"]=$lzc$class_basebutton;lz["swatchview"]=$lzc$class_swatchview;lz["button"]=$lzc$class_button;lz["basevaluecomponent"]=$lzc$class_basevaluecomponent;lz["baseformitem"]=$lzc$class_baseformitem;lz["_internalinputtext"]=$lzc$class__internalinputtext;lz["edittext"]=$lzc$class_edittext;lz["layout"]=LzLayout;lz["simplelayout"]=$lzc$class_simplelayout;lz["login"]=$lzc$class_login;lz["Main"]=$lzc$class_Main;canvas.initDone();