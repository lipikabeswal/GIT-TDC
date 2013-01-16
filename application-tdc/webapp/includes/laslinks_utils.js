var iframeObject = {}; // object containing the item id as key and object conatining folder name and iframe object as value.
var frameFolderObject = {};
function iframeLoaded(id, iframe){
 //console.log("UTILS -->",id);
 	var folderName = iframe.src.substring(iframe.src.indexOf('items')+6,iframe.src.indexOf('asset.html') -1);
	frameFolderObject[folderName] = iframe.id;
	//var currentItemId = window.parent.getItemDetLasRenderer().split('||')[0];
	if(iframe.src.indexOf('asset.html') > 0) {
		if(iframeObject[currentLasAssetItemId]) {
			iframeObject[currentLasAssetItemId][iframe.id] = {'folder' : iframe.src.substring(iframe.src.indexOf('items')+6,iframe.src.indexOf('asset.html') -1), 'iframeObj' : iframe, 'clickedOnce' : false, 'playedOnce' : false};
		} else {
			iframeObject[currentLasAssetItemId] = {};
			iframeObject[currentLasAssetItemId][iframe.id] = {'folder' : iframe.src.substring(iframe.src.indexOf('items')+6,iframe.src.indexOf('asset.html') -1), 'iframeObj' : iframe, 'clickedOnce' : false, 'playedOnce' : false};
		}
	}
// Currently not handling auto play

for(var i=0; i<gController.lasAssetArray.length;i++){
	if(gController.lasAssetArray[i].asset.aw.iframeid == iframe.id) {
		// Apart from first asset, rest should be disabled
		if(parseInt(gController.lasAssetArray[i].data.getAttr('playorder')) != 1) {
			iframe.contentWindow.disable();
		}
	}
}
gController.setAttribute('iframeLoaded',iframeObject);
gController.setAttribute('isLoaded',true);
}

/**
*First check whether all the assets are played once or not.
*Then disable rest of the buttons and enable the button next in play order sequence.
*If the last asset completes then enable all the assets.
*/
function enableNextButton(assetFolderId) {
	if(checkAllPlayedOnce()) {
	} else {
		getNextPlayOrder(assetFolderId);
	}
}

//Check whether all the assets are played once or not
function checkAllPlayedOnce() {
	for(var i=0; i<gController.lasAssetArray.length;i++){
		//console.log(iframeObject[currentLasAssetItemId][gController.lasAssetArray[i].asset.aw.iframeid].clickedOnce);
		//console.log(iframeObject[currentLasAssetItemId][gController.lasAssetArray[i].asset.aw.iframeid].playedOnce);
		if(!(iframeObject[currentLasAssetItemId][gController.lasAssetArray[i].asset.aw.iframeid].clickedOnce && iframeObject[currentLasAssetItemId][gController.lasAssetArray[i].asset.aw.iframeid].playedOnce)) {
			return false;
		}
	}
	return true;
}

function getNextPlayOrder(assetFolderId) {
	var currentPlayOrder = 0;
	var iframeIdVal;
	//Retrieve the current play order
	for(var i=0; i<gController.lasAssetArray.length;i++){
		if(gController.lasAssetArray[i].asset.aw.iframeid == iframeObject[currentLasAssetItemId][gController.lasAssetArray[i].asset.aw.iframeid].iframeObj.id) {
			currentPlayOrder = parseInt(gController.lasAssetArray[i].data.getAttr('playorder'));
			//console.log(" currentPlayOrder - ",currentPlayOrder);
			iframeIdVal = gController.lasAssetArray[i].asset.aw.iframeid;
			break;
		}
	}
	if(currentPlayOrder != gController.lasAssetArray.length) {//If it is not the last asset
		for(var j=0; j< gController.lasAssetArray.length; j++) {
			//console.log(parseInt(gController.lasAssetArray[j].data.getAttr('playorder')));
			if(parseInt(gController.lasAssetArray[j].data.getAttr('playorder')) == (currentPlayOrder + 1)) { // enable the next asset
				if(iframeObject[currentLasAssetItemId][gController.lasAssetArray[j].asset.aw.iframeid].iframeObj.id == gController.lasAssetArray[j].asset.aw.iframeid) {
					iframeObject[currentLasAssetItemId][gController.lasAssetArray[j].asset.aw.iframeid].iframeObj.contentWindow.resetAudio();
					//console.log(" RESET AUDIO ");
				}
			} else { // disable the rest of the assets
				if(iframeObject[currentLasAssetItemId][gController.lasAssetArray[j].asset.aw.iframeid].iframeObj.id == gController.lasAssetArray[j].asset.aw.iframeid) {
					iframeObject[currentLasAssetItemId][gController.lasAssetArray[j].asset.aw.iframeid].iframeObj.contentWindow.disable();
					//console.log(" DISABLE AUDIO ");
				}
			}
		}
	} else { // If the last one is finished playing once, then enable all the rest assets
		for(var j=0; j< gController.lasAssetArray.length; j++) {
			 // disable the rest of the assets except the last one
			if(iframeObject[currentLasAssetItemId][gController.lasAssetArray[j].asset.aw.iframeid].iframeObj.id == gController.lasAssetArray[j].asset.aw.iframeid) {
				iframeObject[currentLasAssetItemId][gController.lasAssetArray[j].asset.aw.iframeid].iframeObj.contentWindow.resetAudio();
			}
		}
	}
	
}
