var curLasItemId;
var curLasItemHash;
var curLasItemKey;
var lasLinkState = {};
var iframeid;
function sendItemDetLasRenderer(item) {
	item = item.split(",");
	curLasItemId = item[0];	
	curLasItemHash = item[1];
	curLasItemKey = item[2];
}

function getItemDetLasRenderer(){
	return curLasItemId +"||"+ curLasItemHash+"||"+curLasItemKey;
}

function triggerIframeIdSave(){
lz.embed.setCanvasAttribute('triggeridsave',"id");
}

//Saving iframeID
function saveIframeId(arg){
console.log("saveIframeId",arg);
	iframeid = arg;
}

function toggleAndSave(msg){
	var sel = msg.split("||");
	var elemId = sel[1];
	selVal = sel[0];
	var userAction = sel[2];
	console.log("toggleSelState", sel);
	var elementFrm = document.getElementById(elemId);
	saveResponse(selVal,userAction);
	//alert(elementFrm.length);
	if(elementFrm != null ) {
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
	console.log(arg);
	var xmlDoc;
	var parser = new DOMParser();
	xmlDoc=parser.parseFromString(arg,"text/xml");
	console.log("XML DOC:" + xmlDoc);
	var responseIterator = xmlDoc.evaluate("//ist", xmlDoc,
	                 null, XPathResult.ANY_TYPE, null);
	var result=responseIterator.iterateNext();              
	while (result){
	   // alert(result.singleNodeValue.firstChild.nodeValue);
	   console.log(result.getAttribute('eid'));
	   result=responseIterator.iterateNext();
	}

}

function saveResponse(arg, userAction){
console.log("saveResponse main",arg,userAction)
lasLinkState[curLasItemId] = {resp: arg, state: userAction};
}
function saveResponseFromWrapper(id,resp,state){
console.log("save Response wrapper",id,resp,state);
lasLinkState[id] = {resp: resp, state: state};
}

function applySRResponse(){
console.log("applySRREsponse", iframeid,curLasItemId,lasLinkState[curLasItemId]);
var elementFrm = document.getElementsByTagName('iframe')[0];
	if(lasLinkState[curLasItemId]){
		if(elementFrm != null ) {
					elementFrm.contentWindow.gController.setAttribute('calledFromMainApp',true);
				    elementFrm.contentWindow.gController.setItemSelState(lasLinkState[curLasItemId].resp,lasLinkState[curLasItemId].state);	
			
			}
	}

}

function applyCRResponse(){

}