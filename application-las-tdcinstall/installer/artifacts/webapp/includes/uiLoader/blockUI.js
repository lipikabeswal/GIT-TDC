function blockUI(ref) {
	gController.freezeUI();
    $("#appcontainer").append('<div id="blockDiv" class="loading-background"><img class="loading-img" src="includes/uiLoader/includes/busy.gif"/><p class="loading-text">Please Wait while the Sound Recorder is activated</p></div>');
    unblockUIDelayed(ref);
}

function unblockUI(ref) {
	if(gController.recorderState == 'RECORDING_START'){
    	$("#blockDiv").css("cursor", "normal").remove();
    	gSpeakNowPopup.show();
    	setTimeout(function(){
          hidePopup(ref);
          }, 1000);
    	gController.setAttribute('recorderState','');
    }else{
    	unblockUIDelayed(ref);
    }
}

function unblockUIDelayed(ref){
	setTimeout(function(){
          unblockUI(ref);
          }, 2000);
}

function hidePopup(ref){
	gSpeakNowPopup.hide();
	if(!ref.startRecord){
		ref.setStartRecord();
	}
	gController.unlockUI();
}