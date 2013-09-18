function blockUI() {
    $("#appcontainer").append('<div id="blockDiv" class="loading-background"><img src="lps/resources/lps/includes/busy.gif" style="left:50%;top:40%;position:absolute;z-index:9999"/></div>');
    /*if (isLoading == true) {
        $("#blockDiv").css("cursor", "wait");
    }*/
}

function unblockUI() {
    $("#blockDiv").css("cursor", "normal").remove();
}
