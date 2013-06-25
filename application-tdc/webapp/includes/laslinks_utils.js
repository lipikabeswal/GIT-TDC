var iframeObject = {}; // object containing the item id as key and object conatining folder name and iframe object as value.
var frameFolderObject = {};
var currentPlayOrder = 0;
var playOrderArray = {};
var enabledArray = [];
var assetCount = 0;

function iframeLoaded(id, iframe){
	if(currentLasAssetItemId) {
		
		////console.log("UTILS -->",id,"  ",iframe);
		var folderName = iframe.src.substring(iframe.src.indexOf('items')+6,iframe.src.indexOf('asset.html') -1);
		frameFolderObject[folderName] = iframe.id;
		if(iframe.src.indexOf('asset.html') > 0) {
			if(iframeObject[currentLasAssetItemId]) {
				iframeObject[currentLasAssetItemId][iframe.id] = {'folder' : iframe.src.substring(iframe.src.indexOf('items')+6,iframe.src.indexOf('asset.html') -1), 'iframeObj' : iframe, 'clickedOnce' : false, 'playedOnce' : false, 'playEvent' : false};
				////console.log("*******  ",iframeObject[currentLasAssetItemId][iframe.id]);
			} else {
				iframeObject[currentLasAssetItemId] = {};
				iframeObject[currentLasAssetItemId][iframe.id] = {'folder' : iframe.src.substring(iframe.src.indexOf('items')+6,iframe.src.indexOf('asset.html') -1), 'iframeObj' : iframe, 'clickedOnce' : false, 'playedOnce' : false, 'playEvent' : false};
			}
		}
		getPlayOrder();  // To get the playOrder of all assets in array.
		////console.log("gController.lasAssetArray ->",gController.lasAssetArray);
		for(var i=0; i<gController.lasAssetArray.length;i++){
			if(gController.lasAssetArray[i].asset){
				if(gController.lasAssetArray[i].asset.aw.iframeid == iframe.id) {
					//Not doing anything for auto play now.
					if(parseInt(gController.lasAssetArray[i].data.getAttr('playorder')) == 1){
						//if(gController.lasAssetArray[i].data.getAttr('autoplay') == "true")
			 			//iframe.contentWindow.autoPlay();
			 			//iframe.contentWindow.play();
					}
					// Apart from first asset, rest should be disabled
					if(parseInt(gController.lasAssetArray[i].data.getAttr('playorder')) != 1) {
						iframe.contentWindow.disable();
					}
					//Disable the response content, if the attribute responseAreaLocker is present
					if(gController.lasAssetArray[i].data.getAttr('responseAreaLocker') == "true"){
						gController.setAttribute('canNotAnswer',true);
						////console.log("responseAreaLocker ----");
						////console.log(iframe.id);
					}
					if(gController.lasAssetArray[i].data.getAttr('playIfAnswered') == "true"){
						////console.log("true playIfAnswered ----");
						gController.setAttribute('iframeid',iframe.id);
					}
				}
			}
		}
			assetCount++;
			if(gController.lasAssetArray.length == assetCount){
				setTimeout("startAutoplay()",500);
			}

		}
		
	}
	


/**
*First check whether all the assets are played once or not.
*Then disable rest of the buttons and enable the button next in play order sequence.
*If the last asset completes then enable all the assets checking for play once ofcourse.
*/
function enableNextButton(assetFolderId) {
////console.log("Inside enableNextButton  -->",assetFolderId);
	if(checkAllPlayedOnce()) {
		////console.log("Not printed");
	} else {
	////console.log("Inside else of enableNextButton");
		getNextPlayOrder(assetFolderId);
	}
}

//Check whether all the assets are played once or not
function checkAllPlayedOnce() {
	for(var i=0; i<gController.lasAssetArray.length;i++){
		if(gController.lasAssetArray[i].asset) {
			////console.log(iframeObject[currentLasAssetItemId][gController.lasAssetArray[i].asset.aw.iframeid].clickedOnce);
			////console.log(iframeObject[currentLasAssetItemId][gController.lasAssetArray[i].asset.aw.iframeid].playedOnce);
			////console.log("Inside iframeObject :",iframeObject[currentLasAssetItemId][gController.lasAssetArray[i].asset.aw.iframeid]);
			if(!(iframeObject[currentLasAssetItemId][gController.lasAssetArray[i].asset.aw.iframeid].clickedOnce && iframeObject[currentLasAssetItemId][gController.lasAssetArray[i].asset.aw.iframeid].playedOnce)) {
				return false;
			}
		}
	}
	return true;
}
function getCurrentPlayOrder(currIframeId){
	for(var j=0; j< gController.lasAssetArray.length; j++) {
		if(gController.lasAssetArray[j].asset) {
			if(iframeObject[currentLasAssetItemId][currIframeId].iframeObj.id == gController.lasAssetArray[j].asset.aw.iframeid) {
				currentPlayOrder = parseInt(gController.lasAssetArray[j].data.getAttr('playorder'));
				break;
			}
		}
	}
}
// To store playorder of all assets in object. 
function getPlayOrder(){
////console.log("Inside getCurrentPlayOrder");
	if(playOrderArray[currentLasAssetItemId]){
		for(var j=0; j< gController.lasAssetArray.length; j++){
			if(gController.lasAssetArray[j].asset){
				////console.log("Inside if of getCurrentPlayOrder");
					playOrderArray[currentLasAssetItemId][parseInt(gController.lasAssetArray[j].data.getAttr('playorder'))] = {'currentAsset' : gController.lasAssetArray[j].asset.aw.iframeid, 'playOnce' : gController.lasAssetArray[j].data.getAttr('playonce'), 'playIfAnswered' : gController.lasAssetArray[j].data.getAttr('playIfAnswered')};
				}
			}
	}else{
		playOrderArray[currentLasAssetItemId] = {};
		for(var j=0; j< gController.lasAssetArray.length; j++){
			if(gController.lasAssetArray[j].asset){
				////console.log("Inside else of getCurrentPlayOrder");
				playOrderArray[currentLasAssetItemId][parseInt(gController.lasAssetArray[j].data.getAttr('playorder'))] = {'currentAsset' : gController.lasAssetArray[j].asset.aw.iframeid, 'playOnce' : gController.lasAssetArray[j].data.getAttr('playonce'), 'playIfAnswered' : gController.lasAssetArray[j].data.getAttr('playIfAnswered')};
				
			}
		}
	}
////console.log("Final Array :",playOrderArray[currentLasAssetItemId]);
}


function getNextPlayOrder(assetFolderId) {
	////console.log("Inside getNextPlayOrder");
	if(playOrderArray[currentLasAssetItemId]) {
		////console.log("Inside playOrderArray :",playOrderArray[currentLasAssetItemId][currentPlayOrder + 1]);
		if(currentPlayOrder == iframeObject[currentLasAssetItemId].length) {
			// Do nothing currently as this is the last asset in the item
		} else {
			for(var k = 1; k < currentPlayOrder + 1; k++) {
				if(playOrderArray[currentLasAssetItemId][k]['playOnce'] == "false") {
					iframeObject[currentLasAssetItemId][playOrderArray[currentLasAssetItemId][k]['currentAsset']].iframeObj.contentWindow.enable();
				}
			}
			if(playOrderArray[currentLasAssetItemId][k]['playIfAnswered'] == "false") {
				iframeObject[currentLasAssetItemId][playOrderArray[currentLasAssetItemId][currentPlayOrder + 1]['currentAsset']].iframeObj.contentWindow.enable();
			}
		}
	}
}

function unlockResponseArea(frameId) { // Assuming there will be only one such asset per item
	////console.log("SCRIPT gController.canNotAnswer - ",gController.canNotAnswer);
	if(gController.canNotAnswer) {
		for(var i=0; i< gController.lasAssetArray.length; i++) {
			if(gController.lasAssetArray[i].asset) {
				if(gController.lasAssetArray[i].asset.aw.iframeid == frameId && gController.lasAssetArray[i].data.getAttr('responseAreaLocker') == "true") {
					gController.setAttribute('canNotAnswer',false);
					////console.log("canNotAnswer set to false");
				}
			}
		}
	}
}

function checkValIfAnswered(frameId) {
   ////console.log("playIfAnswered----->",gController.playIfAnswered);
   ////console.log("frameId-->",frameId);
   if(gController.playIfAnswered) {
		if(iframeObject[currentLasAssetItemId] && iframeObject[currentLasAssetItemId][frameId]) {
			iframeObject[currentLasAssetItemId][frameId].iframeObj.contentWindow.enable();
		}
	}  
}

function playSingleAsset(currIframeId){

		var id = iframeObject[currentLasAssetItemId][currIframeId].iframeObj.id;
		//console.log("ID Playing currently :", id);
		for(var i=0; i< gController.lasAssetArray.length; i++) {
				if(gController.lasAssetArray[i].asset){
			//	console.log("ID Lasasset : ",gController.lasAssetArray[i].asset.aw.iframeid);
					if(gController.lasAssetArray[i].asset.aw.iframeid != id){
				//		console.log("play Event : ",iframeObject[currentLasAssetItemId][gController.lasAssetArray[i].asset.aw.iframeid].playEvent);
						if(iframeObject[currentLasAssetItemId]) {
							if(iframeObject[currentLasAssetItemId][gController.lasAssetArray[i].asset.aw.iframeid].playEvent){		
								iframeObject[currentLasAssetItemId][gController.lasAssetArray[i].asset.aw.iframeid].iframeObj.contentWindow.resetAudio();
							}
						}
				}	
		}
	}
		
}

function setPlayingAttr(arg){	
	if(arg == 'true'){
		gController.setAttribute('isplaying',true);
	} else{
		gController.setAttribute('isplaying',false);
	}
}

function disableAssets(){
	var	k=0;
	for(var i=0; i<gController.lasAssetArray.length;i++){
				if(gController.lasAssetArray[i].asset){
				if(iframeObject[currentLasAssetItemId][gController.lasAssetArray[i].asset.aw.iframeid].iframeObj.contentWindow.isPlaying == "false"){
				iframeObject[currentLasAssetItemId][gController.lasAssetArray[i].asset.aw.iframeid].iframeObj.contentWindow.disable();
						enabledArray[k]=gController.lasAssetArray[i].asset.aw.iframeid;
						k++;
			//console.log("inside if ");
				}
					
			}
	}
}


function enableAssets(){
		for(var i=0; i<gController.lasAssetArray.length;i++){
			for(var j=0; j<enabledArray.length;j++){
				if(gController.lasAssetArray[i].asset){
					if(gController.lasAssetArray[i].asset.aw.iframeid == enabledArray[j]){
					iframeObject[currentLasAssetItemId][gController.lasAssetArray[i].asset.aw.iframeid].iframeObj.contentWindow.enable();
					
					}
				}
			}
		}
	enabledArray.length=0;

}

function startAutoplay(){
		for(var i=0; i<gController.lasAssetArray.length;i++){
			if(gController.lasAssetArray[i].data.getAttr('autoplay') == "true"){
				iframeObject[currentLasAssetItemId][gController.lasAssetArray[i].asset.aw.iframeid].iframeObj.contentWindow.autoPlay();
				autoPlayEvent = "true";
				break;
			}
		}
  }