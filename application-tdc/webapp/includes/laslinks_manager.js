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

