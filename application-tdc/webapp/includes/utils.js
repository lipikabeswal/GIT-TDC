var textAreaElement = null;
var scratchpadText = null;
function scratchpadmousehandler(){


}
function resizeSP(){
var scratchpadFrame = document.getElementById('__lz0');
scratchpadFrame.style.height = gScratchpad.height;
scratchpadFrame.style.width = scratchpadFrame.parentElement.parentElement.style.width;
}


function initScratchpad(arg){
		var scratchpadFrame = document.getElementById('__lz0');
		 var innerHTML = '<style type="text/css">html,body {background-color: #FFFFB0; margin: 0px; height: 100%; width: 100%;} @font-face{font-family: OASmathv3;src: url("./../manipulatives/resource/fonts/OASmathv3.ttf");}</style>'
			                   + '<textarea id="myarea" style="height: 100%; width: 100%;"></textarea>">';
		gScratchpad.setHtml(innerHTML);
		
		var elem  = scratchpadFrame.contentWindow.document.getElementById('myarea');
		elem.style.width = "100%";
		//Height 100% doesnt work in AIR browser we have to keep the html,body to 100% inorder for this to work
		elem.style.height= "100%";  
		elem.style.border = "none";
		elem.style.resize = "none";
		elem.style.backgroundColor = 'transparent';
		elem.style.clip = "rect(0px 400px 227px 0px)";
		elem.style.overflowY = "scroll";
		elem.style.overflowX = "hidden";
		elem.style.fontFamily = 'OASmathv3';
		elem.maxlength = 10000;
		textAreaElement = elem;
		if(scratchpadText){
			applyTextToArea();
		}
		if(arg == 'true'){
			elem.style.fontSize = "18px";
		}else{
			elem.style.fontSize = "12px";
		}
		textAreaElement.onfocus = function() {
    	moveCaretToEnd(textAreaElement);

    // Work around Chrome's little problem
		    window.setTimeout(function() {
		        moveCaretToEnd(textAreaElement);
		    }, 1);
		};
		textAreaElement.onkeydown = function(e) {
    		switch (e.keyCode) {			
				case 8: // backspace			
				case 46: // delete			
				case 35: // end			
				case 36: // home			
				case 37: // left			
				case 38: // up			
				case 39: // right			
				case 40: // down			
				case 45: // insert			
				case 116: // F5				
				return true;		
			}	
				
			if(textAreaElement.value.length > 10000) {
				textAreaElement.value = textAreaElement.value.substring(0, 10001);
				//textAreaElement.scrollTop = 1;
				return false;
			} else {
				return true;
			}
		};
}

function getSelectedText(){
if(textAreaElement.selectionStart == textAreaElement.selectionEnd ){
	return textAreaElement.value;
}else{
	return textAreaElement.value.substring(textAreaElement.selectionStart,textAreaElement.selectionEnd);
}
}


function applyTextToArea(){
	if(textAreaElement){
		textAreaElement.value = scratchpadText;
		//textAreaElement.scrollTop=textAreaElement.scrollHeight; 
		//textAreaElement.focus();
	}	
}
function setScratchpadText(arg){
		scratchpadText = arg;	
}

function getScratchpadText(){
return textAreaElement.value;
}


function setFocus(){
	if(textAreaElement){
		//textAreaElement.scrollTop=textAreaElement.scrollHeight; 
		textAreaElement.focus();
		window.setTimeout(function() {
		        setFocusAir();
		}, 100);
	}
}

function setFocusAir(){
	if(textAreaElement){

		//textAreaElement.scrollTop=textAreaElement.scrollHeight; 
		textAreaElement.focus();
	}

}
function moveCaretToEnd(el) {
    if (typeof el.selectionStart == "number") {
        el.selectionStart = el.selectionEnd = el.value.length;
    } else if (typeof el.createTextRange != "undefined") {
        el.focus();
        var range = el.createTextRange();
        range.collapse(false);
        range.select();
    }
}