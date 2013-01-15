var curLasItemId;
var curLasItemHash;
var curLasItemKey;
var lasLinkState = {};
var iframeid;
var itemMarkedState = {};
var manipState = {};
var lsid;
function sendItemDetLasRenderer(item) {
	item = item.split(",");
	curLasItemId = item[0];	
	curLasItemHash = item[1];
	curLasItemKey = item[2];
	lsid  = item[3];
}

function getItemDetLasRenderer(){
	return curLasItemId +"||"+ curLasItemHash+"||"+curLasItemKey;
}

function triggerIframeIdSave(){
lz.embed.setCanvasAttribute('triggeridsave',"id");
}

//Saving iframeID
function saveIframeId(arg){
//console.log("saveIframeId",arg);
	iframeid = arg;
}

function toggleAndSave(msg){
	var sel = msg.split("||");
	var elemId = sel[1];
	selVal = sel[0];
	var userAction = sel[2];
	//console.log("toggleSelState", sel);
	var elementFrm = document.getElementById(elemId);
	if(userAction != 'e') {
		saveResponse(selVal,userAction);
	}
	//alert(elementFrm.length);
	if(elementFrm != null && userAction != 'e') {
		elementFrm.contentWindow.toggleSelStateFrame(selVal);
	}

}

function getIsAnswered(id){
	return lasLinkState[id];
}

function checkItemPresence(id) {
	return lasLinkState[id];
}


function toggleSelectionMain(sel) {
	lz.embed.setCanvasAttribute('selectionFromLasAsset',sel);
}
//This is to prepare the restart data and update the laslinks save state
function saveLoginResponse(arg){
	//console.log(arg);
	var xmlDoc;
	var parser = new DOMParser();
	xmlDoc=parser.parseFromString(arg,"text/xml");
	//console.log("XML DOC:" + xmlDoc);
	var responseIterator = xmlDoc.evaluate("//ist", xmlDoc,
	                 null, XPathResult.ANY_TYPE, null);
	var result=responseIterator.iterateNext();              
	while (result){
	   // alert(result.singleNodeValue.firstChild.nodeValue);
	   //console.log(result.getAttribute('eid'));
	   
	   if(result.getElementsByTagName('answers').length > 0){
	   		if(result.getElementsByTagName('answer').length > 0){
			 	for(var i=0; i < result.getElementsByTagName('answer').length; i++){
				   	var pattern = result.getAttribute('eid') +"_"+ result.getElementsByTagName('answer')[i].getAttribute('id');
				   lasLinkState[pattern] = {widget_id: result.getElementsByTagName('answer')[i].getAttribute('id'),answer:result.getElementsByTagName('answer')[i].textContent,answered: true,crType: 'MCR'};
				   //console.log("CR Response set");
			   }
		   }
	   }else{
	   		if(result.firstChild.firstChild.textContent.length > 0){
	  		 	lasLinkState[result.getAttribute('eid')] = {resp: result.firstChild.firstChild.textContent, state: 's'} ;
	  		 	//console.log("SR Response set");
	  		 }
	   }
	   result=responseIterator.iterateNext();
	}

}

function saveResponse(arg, userAction){
//console.log("saveResponse main",arg,userAction)
lasLinkState[curLasItemId] = {resp: arg, state: userAction};
}
function saveResponseFromWrapper(id,resp,state){
//console.log("save Response wrapper",id,resp,state);
lasLinkState[id] = {resp: resp, state: state};
}

function applySRResponse(){
//console.log("applySRREsponse", iframeid,curLasItemId,lasLinkState[curLasItemId]);
applyManipDetails();
var elementFrm = document.getElementsByTagName('iframe')[0];
	if(lasLinkState[curLasItemId]){
		if(elementFrm != null ) {
			elementFrm.contentWindow.gController.setAttribute('calledFromMainApp',true);
			elementFrm.contentWindow.gController.setItemSelState(lasLinkState[curLasItemId].resp,lasLinkState[curLasItemId].state);	
		}
	}

}

function getCRResponse(widgetId){
var elementFrm = document.getElementsByTagName('iframe')[0];
var itemIdPatternCR = curLasItemId +"_" +widgetId; 
	if(lasLinkState[itemIdPatternCR]){
		if(elementFrm != null ) {					
				return lasLinkState[itemIdPatternCR]['answer'];
			}
	}
}

function saveCRState(mesg){
	var msg = mesg.split("||");
	var itemIdPatternCR = curLasItemId +"_" +msg[0];
	lasLinkState[itemIdPatternCR] = {widget_id: msg[0],answer:msg[1],answered: msg[2],crType: msg[3]};
	lz.embed.setCanvasAttribute('triggercrsave',mesg);
}


function toggleLasManip(arg){
//console.log("mainapplication toggleLas", arg);
	var msg = arg.split("||");
	var elementFrm = document.getElementsByTagName('iframe')[0];
	elementFrm.contentWindow.toggleManip(msg[0],msg[1]);
}

function markAndUnmark(arg){
	var msg = arg.split("||");
	msg[1] = (msg[1] == 'true') ? true : false;
	itemMarkedState[msg[0]] = msg[1];
	if(manipState[msg[0]]) {
		manipState[msg[0]]['marked'] = msg[1];
	} else {
		manipState[msg[0]] = {};
		manipState[msg[0]]['marked'] = msg[1];
	}
}

/*function designSaveManip(){
	if(manipState[curLasItemId]) {
		if(manipState[curLasItemId].marked) {
			manipState[curLasItemId] = {marked : manipState[curLasItemId].marked,protractor: '',mm_ruler: ''};
		} else {
			manipState[curLasItemId] = {marked : '',protractor: '',mm_ruler: ''};
		}
	} else {
		manipState[curLasItemId] = {marked : '',protractor: '',mm_ruler: ''};
	}
}*/

function applyManipDetails() {
	var elementFrm = document.getElementsByTagName('iframe')[0];
	if(elementFrm) {
		if(manipState[curLasItemId]) {
			if(manipState[curLasItemId].marked) {
				if(manipState[curLasItemId]['mm_ruler']) {
					if(manipState[curLasItemId]['mm_ruler'].state) {
						elementFrm.contentWindow.toggleManip('mm_ruler',manipState[curLasItemId]['mm_ruler'].state);
					}
				} else if(manipState[curLasItemId]['protractor']) {
					if(manipState[curLasItemId]['protractor'].state) {
						elementFrm.contentWindow.toggleManip('protractor',manipState[curLasItemId]['protractor'].state);
					}
				} else if(manipState[curLasItemId]['oneeighth_inch_ruler']) {
					if(manipState[curLasItemId]['oneeighth_inch_ruler'].state) {
						elementFrm.contentWindow.toggleManip('oneeighth_inch_ruler',manipState[curLasItemId]['oneeighth_inch_ruler'].state);
					}
				} else if(manipState[curLasItemId]['half_inch_ruler']) {
					if(manipState[curLasItemId]['half_inch_ruler'].state) {
						elementFrm.contentWindow.toggleManip('half_inch_ruler',manipState[curLasItemId]['half_inch_ruler'].state);
					}
				} else if(manipState[curLasItemId]['cm_ruler']) {
					if(manipState[curLasItemId]['cm_ruler'].state) {
						elementFrm.contentWindow.toggleManip('cm_ruler',manipState[curLasItemId]['cm_ruler'].state);
					}
				} else if(manipState[curLasItemId]['straight_edge']) {
					if(manipState[curLasItemId]['straight_edge'].state) {
						elementFrm.contentWindow.toggleManip('straight_edge',manipState[curLasItemId]['straight_edge'].state);
					}
				} else if(manipState[curLasItemId]['standard_calculator']) {
					if(manipState[curLasItemId]['standard_calculator'].state) {
						elementFrm.contentWindow.toggleManip('standard_calculator',manipState[curLasItemId]['standard_calculator'].state);
					}
				}
			}
		}
	}
}

function saveDetailsManip(toolType,detailType,value) {
	if(manipState[curLasItemId]) {
		if(manipState[curLasItemId][toolType]) {
			manipState[curLasItemId][toolType][detailType] = value;
		} else {
			manipState[curLasItemId][toolType] = {};
			manipState[curLasItemId][toolType][detailType] = value;
		}
	} else {
		manipState[curLasItemId] = {};
		manipState[curLasItemId][toolType] = {};
		manipState[curLasItemId][toolType][detailType] = value;
	}
}

function iframeLoaded(id, iframe){

}