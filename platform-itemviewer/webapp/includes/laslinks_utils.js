var iframeObject = {}; // object containing the item id as key and object conatining folder name and iframe object as value.
var frameFolderObject = {};
var currentPlayOrder = 0;
var playOrderArray = {};
var enabledArray = [];
var assetCount = 0;
var checkAnswered = false;
var answerClicked = null;
var pausedAssetID = null;
var pausedDisabledAssetID = null;
var iframeFolderId = null;
var canNotAnswerFlag = false;
var autoplayStopped = false;

function iframeLoaded(id, iframe){
	if(iframe){
		if(currentLasAssetItemId) {
			var folderName;
			var iframeSource;
			var imageSource;
			var iframeCount = [];
			var imageCount = [];
			iframeCount  = jQuery('iframe[src ^= "http:items/"]');
			for(var i=0; i<iframeCount.length; i++){
				iframeSource = iframeCount[i];
				imageCount = jQuery(iframeSource).contents().find('#wrapper');
				/*for(var j=0; j<imageCount.length; j++){
					imageSource = imageCount[j];*/
					jQuery(imageCount).on("dragstart" , function(e){
						e.preventDefault();
					});
				//}
			}
			if(iframe.src.indexOf('asset.html')!=-1){
				folderName = iframe.src.substring(iframe.src.indexOf('items')+6,iframe.src.indexOf('asset.html') -1);
			}else{
				folderName = iframe.src.substring(iframe.src.indexOf('items')+6,iframe.src.indexOf('video.html') -1);
			}
			frameFolderObject[folderName] = iframe.id;
			if(iframe.src.indexOf('asset.html') > 0) {
				if(iframeObject[currentLasAssetItemId]) {
					iframeObject[currentLasAssetItemId][iframe.id] = {'folder' : iframe.src.substring(iframe.src.indexOf('items')+6,iframe.src.indexOf('asset.html') -1), 'iframeObj' : iframe, 'clickedOnce' : false, 'playedOnce' : false, 'playEvent' : false};
					////console.log("*******  ",iframeObject[currentLasAssetItemId][iframe.id]);
				} else {
					iframeObject[currentLasAssetItemId] = {};
					iframeObject[currentLasAssetItemId][iframe.id] = {'folder' : iframe.src.substring(iframe.src.indexOf('items')+6,iframe.src.indexOf('asset.html') -1), 'iframeObj' : iframe, 'clickedOnce' : false, 'playedOnce' : false, 'playEvent' : false};
				}
			}else if(iframe.src.indexOf('video.html') > 0){
				if(iframeObject[currentLasAssetItemId]) {
					iframeObject[currentLasAssetItemId][iframe.id] = {'folder' : iframe.src.substring(iframe.src.indexOf('items')+6,iframe.src.indexOf('video.html') -1), 'iframeObj' : iframe, 'clickedOnce' : false, 'playedOnce' : false, 'playEvent' : false};
				} else {
					iframeObject[currentLasAssetItemId] = {};
					iframeObject[currentLasAssetItemId][iframe.id] = {'folder' : iframe.src.substring(iframe.src.indexOf('items')+6,iframe.src.indexOf('video.html') -1), 'iframeObj' : iframe, 'clickedOnce' : false, 'playedOnce' : false, 'playEvent' : false};
				}
			}
			getPlayOrder();  // To get the playOrder of all assets in array.
			////console.log("gController.lasAssetArray ->",gController.lasAssetArray);
			for(var i=0; i<gController.lasAssetArray.length;i++){
				if(gController.lasAssetArray[i].asset){
					var frameid = getFrameId(gController.lasAssetArray[i]);
					if(frameid == iframe.id) {
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
						    if(gMagnifyingGlass.magnifierOpen == false || gMagnifyingGlass.magnifierOpen == 'false'){
						    	//do nothing
						    	// this condition is for the defect 73859
						    }else{
						        gController.setAttribute('canNotAnswer',true);
						        canNotAnswerFlag = true;
							}	
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
					if(canNotAnswerFlag == false){
					 	gController.setAttribute('canNotAnswer',false);
					}else{
						//gController.setAttribute('canNotAnswer',false);
						gController.setAttribute('canNotAnswer',true);
					}
				}
			//restrictNavigation('lock');
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
			if(gController.lasAssetArray[i].data.getAttr('isMP4') == "true" || gController.lasAssetArray[i].data.getAttr('isMP4') == true) {
				var folderId = iframeObject[currentLasAssetItemId][gController.lasAssetArray[i].asset.video.iframeid].folder;
			} else {
				var folderId = iframeObject[currentLasAssetItemId][gController.lasAssetArray[i].asset.aw.iframeid].folder;
			}
			if(folderId != iframeFolderId) {
				iframeFolderId = folderId ; 			
				var frameid = getFrameId(gController.lasAssetArray[i]);					
				if(!(iframeObject[currentLasAssetItemId][frameid].clickedOnce && iframeObject[currentLasAssetItemId][frameid].playedOnce)) {
					return false;
				}
			}
		}
	}
	return true;
}
function getCurrentPlayOrder(currIframeId){
	for(var j=0; j< gController.lasAssetArray.length; j++) {
		if(gController.lasAssetArray[j].asset) {
			var frameid = getFrameId(gController.lasAssetArray[j]);
			if(iframeObject[currentLasAssetItemId][currIframeId].iframeObj.id == frameid) {
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
					var frameid = getFrameId(gController.lasAssetArray[j]);
					playOrderArray[currentLasAssetItemId][parseInt(gController.lasAssetArray[j].data.getAttr('playorder'))] = {'currentAsset' : frameid, 'playOnce' : gController.lasAssetArray[j].data.getAttr('playonce'), 'playIfAnswered' : gController.lasAssetArray[j].data.getAttr('playIfAnswered')};
				}
			}
	}else{
		playOrderArray[currentLasAssetItemId] = {};
		for(var j=0; j< gController.lasAssetArray.length; j++){
			if(gController.lasAssetArray[j].asset){
				////console.log("Inside else of getCurrentPlayOrder");
				var frameid = getFrameId(gController.lasAssetArray[j]);
				playOrderArray[currentLasAssetItemId][parseInt(gController.lasAssetArray[j].data.getAttr('playorder'))] = {'currentAsset' : frameid, 'playOnce' : gController.lasAssetArray[j].data.getAttr('playonce'), 'playIfAnswered' : gController.lasAssetArray[j].data.getAttr('playIfAnswered')};
				
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
				   if(playOrderArray[currentLasAssetItemId][k]['playOnce'] == "true") {
                    iframeObject[currentLasAssetItemId][playOrderArray[currentLasAssetItemId][k]['currentAsset']].iframeObj.contentWindow.disable();
				}
			}
			if(playOrderArray[currentLasAssetItemId][k] != undefined && playOrderArray[currentLasAssetItemId][k]['playIfAnswered'] == "false") {
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
				var frameid = getFrameId(gController.lasAssetArray[i]);
				if(frameid == frameId && gController.lasAssetArray[i].data.getAttr('responseAreaLocker') == "true") {
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
   if(currentPlayOrder == 0){
   answerClicked = frameId;
   }
   else if(!iframeObject[currentLasAssetItemId][gController.lasAssetArray[currentPlayOrder-1].asset.aw.iframeid].playedOnce) {
   		answerClicked = frameId;
   		
  	 }
   else if(gController.playIfAnswered) {
		if(iframeObject[currentLasAssetItemId] && iframeObject[currentLasAssetItemId][frameId]) {
		 if (checkAnswered == false){
			iframeObject[currentLasAssetItemId][frameId].iframeObj.contentWindow.enable();
			checkAnswered = true;
			}
		}
	}  
}

function playSingleAsset(currIframeId){

		var id = iframeObject[currentLasAssetItemId][currIframeId].iframeObj.id;
		for(var i=0; i< gController.lasAssetArray.length; i++) {
				if(gController.lasAssetArray[i].asset){
					var frameid = getFrameId(gController.lasAssetArray[i]);
					if(frameid != id){
						if(iframeObject[currentLasAssetItemId]) {
							if(iframeObject[currentLasAssetItemId][frameid].playEvent){		
								iframeObject[currentLasAssetItemId][frameid].iframeObj.contentWindow.resetAudio();
							}
						}
				}	
		}
	}
		
}

function setPlayingAttr(event,value){	
	if(value == 'true'){
		gController.setAttribute('isplaying',true);
		
	} 
	else if(event == 'pause'){
	
		var	k=0;
		for(var i=0; i<gController.lasAssetArray.length;i++){
					if(gController.lasAssetArray[i].asset){
						var frameid = getFrameId(gController.lasAssetArray[i]);
						if(iframeObject[currentLasAssetItemId][frameid].iframeObj.contentWindow.isPlaying == "true"){
							k++;
							
						}	
				}
 		}
		
		if(k>0){
			gController.setAttribute('isplaying',true);
	    } else{
	    	gController.setAttribute('isplaying',false);
	  	  }
	} else{
	gController.setAttribute('isplaying',false);
	}
}

function disableAssets(){
	var	k=0;
	for(var i=0; i<gController.lasAssetArray.length;i++){
				if(gController.lasAssetArray[i].asset){
					var frameid = getFrameId(gController.lasAssetArray[i]);
					if(iframeObject[currentLasAssetItemId][frameid].iframeObj.contentWindow.isPlaying == "false"){
						iframeObject[currentLasAssetItemId][frameid].iframeObj.contentWindow.disable();
						enabledArray[k]=frameid;
						k++;
					}
					
			}
	}
}


function enableAssets(){
		for(var i=0; i<gController.lasAssetArray.length;i++){
			for(var j=0; j<enabledArray.length;j++){
				if(gController.lasAssetArray[i].asset){
					var frameid = getFrameId(gController.lasAssetArray[i]);
					if(frameid == enabledArray[j]){
					iframeObject[currentLasAssetItemId][frameid].iframeObj.contentWindow.enable();
					
					}
				}
			}
		}
	enabledArray.length=0;

}

function startAutoplay(){
	if(gController.isStopScreen || gController.isPauseScreen){
		autoplayStopped = 'true';
	}else{
			for(var i=0; i<gController.lasAssetArray.length;i++){
				if(gController.lasAssetArray[i].data.getAttr('autoplay') == "true"){
					var frameid = getFrameId(gController.lasAssetArray[i]);
					autoPlayEvent = "true";
					iframeObject[currentLasAssetItemId][frameid].iframeObj.contentWindow.autoPlay();
					break;
				}
			}
		}
  }
  
  function pauseScreenShown(){
	currentPlayOrder = 0;
	pausedAssetID = null;
	pausedDisabledAssetID = null;
	  
  }
 
function autoplayIfStopped(){
 	if(autoplayStopped == 'true'){
 		startAutoplay();
 		autoplayStopped = 'false';
 	}
 }
  
   /*function restrictNavigation(arg){
   if(arg  == 'unlock'){
	 	if(checkAllPlayedOnce()) {
			gController.setAttribute('unlockNavigation',true);
		} else {
				gController.setAttribute('unlockNavigation',false);
	 		}
 	}else {
 	gController.setAttribute('unlockNavigation',false);
 	}
  }*/
  
    function pauseAudio() {
     if(currentPlayOrder == 0){
    	//Do nothing 
   	}else{
		for(var i=0; i<gController.lasAssetArray.length;i++){
		if(gController.lasAssetArray[i].asset){
			var frameid;
			var frameid = getFrameId(gController.lasAssetArray[i]);
			if(iframeObject[currentLasAssetItemId][frameid].iframeObj.contentWindow.isPlaying == "true"){
				iframeObject[currentLasAssetItemId][frameid].iframeObj.contentWindow.pause();
				pausedAssetID=frameid;
			}
			else if(iframeObject[currentLasAssetItemId][frameid].iframeObj.contentWindow.isPlaying == "disabled"){
				if((iframeObject[currentLasAssetItemId][frameid].clickedOnce) && !(iframeObject[currentLasAssetItemId][frameid].playedOnce)) {
					iframeObject[currentLasAssetItemId][frameid].iframeObj.contentWindow.pause();
						pausedDisabledAssetID=frameid;
					}
					
				}
				
			}
  		}
  	 }
  }
  
  function playAudio(){
  	if(pausedAssetID != null){
		for(var i=0; i<gController.lasAssetArray.length;i++){
				if(gController.lasAssetArray[i].asset){
					var frameid = getFrameId(gController.lasAssetArray[i]);
					if(frameid == pausedAssetID){
						iframeObject[currentLasAssetItemId][frameid].iframeObj.contentWindow.play();
						}
				}
			}
		}
	if(pausedDisabledAssetID != null){
		for(var i=0; i<gController.lasAssetArray.length;i++){
				if(gController.lasAssetArray[i].asset){
				  var frameid = getFrameId(gController.lasAssetArray[i]);
					if(frameid == pausedDisabledAssetID){
						iframeObject[currentLasAssetItemId][frameid].iframeObj.contentWindow.playAfterStop();
						}
				}
			}
		}
        
	pausedAssetID = null;
    pausedDisabledAssetID = null;
} 

function addReadOnlyCR(){
console.log("Inside addReadOnlyCR");
	if(document.getElementsByTagName('textarea').length > 0){
		document.getElementsByTagName('textarea')[0].setAttribute('readonly',true);
		console.log("addReadOnlyCR");
	}
	
}
function removeReadOnlyCR(){
console.log("Inside removeReadOnlyCR");
	if(document.getElementsByTagName('textarea').length > 0){
		document.getElementsByTagName('textarea')[0].removeAttribute('readonly');
		console.log("removeReadOnlyCR");
	}
}

function resetAllAssets(){
	if(gController.isThemePage() == 'true'){
		if(checkAllPlayedOnce()) {
			for(var i=0; i<gController.lasAssetArray.length;i++){
					if(gController.lasAssetArray[i].asset){
						if(gController.lasAssetArray[i].data.getAttr('isMP4') == "true" || gController.lasAssetArray[i].data.getAttr('isMP4') == true) {
							iframeObject[currentLasAssetItemId][gController.lasAssetArray[i].asset.video.iframeid].iframeObj.contentWindow.enable();
						} else {
							iframeObject[currentLasAssetItemId][gController.lasAssetArray[i].asset.aw.iframeid].iframeObj.contentWindow.enable();
						}
					}
			}
		}
	}
}

  function getFrameId(arg){
  	if(arg.data.getAttr('isMP4') == "true" || arg.data.getAttr('isMP4') == true){
		return arg.asset.video.iframeid;
	}else{
		return frameid = arg.asset.aw.iframeid;
	}
  }
  
  function checkEmptyString(str) {
  	return (!str || /^\s*$/.test(str) || /^\n*$/.test(str) || /^\r*$/.test(str));
  }