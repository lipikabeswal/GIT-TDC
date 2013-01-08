var curLasItemId;
var curLasItemHash;
var curLasItemKey;
var lasLinkState = {};
function sendItemDetLasRenderer(item) {
	item = item.split(",");
	curLasItemId = item[0];	
	curLasItemHash = item[1];
	curLasItemKey = item[2];
}

function getItemDetLasRenderer(){
	return curLasItemId +"||"+ curLasItemHash+"||"+curLasItemKey;
}

function toggleSelState(msg){
	var sel = msg.split("||");
	var elemId = sel[1];
	sel = sel[0];
	console.log("toggleSelState", sel);
	var elementFrm = document.getElementById(elemId);
	//alert(elementFrm.length);
	if(elementFrm != null ) {
		elementFrm.contentWindow.toggleSelStateFrame(sel);
	}

}

function getIsAnswered(id){
	return lasLinkState[id];
}

function saveState(id,resp,state){
 lasLinkState[id] = {resp: resp, state: state};
}

function checkItemPresence(id) {
	return lasLinkState[id];
}
