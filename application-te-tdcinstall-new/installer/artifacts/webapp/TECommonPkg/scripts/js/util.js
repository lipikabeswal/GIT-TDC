function getMathPalette(type) {
	var index = 0, html = "", numberSymbol = ["basic_pt_0","basic_pt_1","basic_pt_2","basic_pt_3","basic_pt_4","basic_pt_5","basic_pt_6","basic_pt_7","basic_pt_8","basic_pt_9"], basicSymbol = ["basic_pt_10","basic_pt_11","basic_pt_12","basic_pt_13","basic_pt_14"], advancedSymbol = ["advanced_pt_15","advanced_pt_16","advanced_pt_17","advanced_pt_18","advanced_pt_19","advanced_pt_20","advanced_pt_21","advanced_pt_22","advanced_pt_23","advanced_pt_24","advanced_pt_25","advanced_pt_26","advanced_pt_27","advanced_pt_28","advanced_pt_29","advanced_pt_30","advanced_pt_31","advanced_pt_32","advanced_pt_33","advanced_pt_34"];
	for(index=0; index<numberSymbol.length; index++) {
		html += "<div id = '"+numberSymbol[index]+"' class='palette-button' behavior='2' style='background:url(../../TECommonPkg/styles/css/image/basicSymbol/"+numberSymbol[index]+".PNG)'></div>";
	}
	for(index=0; index<basicSymbol.length; index++) {
		html += "<div id = '"+basicSymbol[index]+"' class='palette-button' behavior='2' style='background:url(../../TECommonPkg/styles/css/image/basicSymbol/"+basicSymbol[index]+".PNG)'></div>";
	}
	if(type == "advanced") {
		for(index=0; index<advancedSymbol.length; index++) {
			html += "<div id = '"+advancedSymbol[index]+"' class='palette-button' behavior='2' style='background:url(../../TECommonPkg/styles/css/image/advancedSymbol/"+advancedSymbol[index]+".PNG)'></div>";
		}
	}
	return html;
}

function getControlIndex(id) {
	return parseInt(id.split("_")[1],10);
}

// getting control name
function getControlName(role) {
	var controlName = "";
	if(role == "divbox") {
		controlName = "Element Group"
	} else if(role == "radio") {
		controlName = "Single Selection";
	} else if(role == "checkbox") {
		controlName = "Multiple Selection";
	} else if(role == "textarea") {
		controlName = "Text Area";
	} else if(role == "droparea") {
		controlName = "Drop Area";
	} else if(role == "mathpalette") {
		controlName = "Math Palette";
	}
	return controlName;
}

// get the default color pallete
function getColorPallete() {
	return [
			['#FF0000', '#FFFFFF'],
			['#00FFFF', '#C0C0C0'],
			['#0000FF', '#808080'],
			['#0000A0', '#000000'],
			["#ADD8E6", "#FFA500"],
			["#800080", "#A52A2A"],
			["#FFFF00", "#800000"],
			["#00FF00", "#008000"],
			["#FF00FF", "#808000"],
			["#ffffb0"]
		];
}

// displying status bar msg
function showStatusMsg(msg) {
	$("#statusBar").stop().text(msg).fadeIn("fast").delay(2000).fadeOut("slow");
}

//display status message in the given content
function showDialogMsg(dialogId,msg) {
	$("#msgInfo").remove();
	var html = "<div id='msgInfo' class='ui-state-highlight user-info'><span class='ui-icon ui-icon-info'></span><span class='info'>"+msg+"</span></div>";
	$("#"+dialogId).stop().prepend(html);
	$("#msgInfo").fadeIn("fast").delay(4000).fadeOut("slow");
}

// this is default property of any element
function getDefaultProperty(controlType) {
	var data =  {
		id: "",
		height: "",
		width: "",
		text: "Enter text here",
		maxallowed: "",
		controltype: "",
		behavior: "", // possible values are 'none', 'draggable', 'droppable'
		type: "", // possible values are 'stem', 'stimulus' etc
		background: "#ffffff",
		name: "",
		border: "#BFBFBF",
		duplicacy: "", //possible values are 'no', 'yes'
		allowmultiple: "", // possible values are 'no', 'yes'
		x: 0,
		y: 0,
		children: [],
		borderrequired: 1,
		bgrequired: 1,// possible values are 'no', 'yes'
		alternatetext: "",
		alternatetextfor: 1,
		screenId: "",
		dropalignment:1,
		interactiontype: "",
		scoring: {},
		transparency: 100,
		matharea: 1,
		//zoom:1,
		mathpaletteType: 1,
		//enlargeable: 1,
		textAlignmentCenter: 1,
		ansArea: false
	};
	if(controlType == "radio") {
		data.height = 16;
		data.width = 300;
		data.suppresstts=1;
	} else if(controlType == "checkbox") {
		data.height = 16;
		data.width = 300;
		data.suppresstts=1;
	} else if(controlType == "textarea") {
		data.height = 150;
		data.width = 150;
		data.type = 1;
		data.behavior = 1;
		data.duplicacy = 1;
		data.suppresstts=1;
		//data.zoom = 1;
	} else if(controlType == "droparea") {
		data.height = 150;
		data.width = 150;
		data.allowmultiple = 1;
		data.maxallowed = 1;
		data.borderrequired = 2;
		data.text = " ";
		data.suppresstts=1;
	} else if(controlType == "divbox") {
		data.height = 400;
		data.width = 400;
		data.interactiontype = 1;
		data.transparency = 80;
		
	}else if(controlType == "mathpalette") {
		data.height = 92;
		data.width = 187;
		data.suppresstts=1;
    }
	
	return data;
}

//calculating positions of dropped element
function getDroppedPosition($pos,height,width) {
	var area = $("#editorArea").offset(), maxHeight = $("body").height(), maxWidth = area.left+$("#editorArea").width(), minWidth = area.left, minHeight = area.top;
	var top = (($pos.top+height > maxHeight) ? maxHeight-height : $pos.top);
	var left = (($pos.left+width > maxWidth) ? maxWidth-width : $pos.left);
	return { top: parseInt(top,10), left: parseInt(left,10) };
}

// making editor as droppable
function makingDroppableArea($ele, acceptStr) {
	var $controlType;
	$ele.droppable({
		accept: acceptStr,
		greedy: true,
		tolerance: "fit",
		scope : "topmost",
		drop: function( event, ui ) {
			var assetId = ui.draggable.attr("assetid");
			var itemRObjectId = $("#itemRObjectId").val();
			var itemBusinessId = $("#itemBusinessId").val();
			var docbaseName = $("#docbaseName").val();
			if(assetId != undefined) {
				
				$.ajax({
					type:"POST",
					cache: false,
					url:'associateAssetWithItem.do?timestamp='+Number(new Date()),
					data: {
						"as_Id": assetId,
						"itemRObjectId" : itemRObjectId,
						"docbaseName" : docbaseName,
						"itemBusinessId" : itemBusinessId
					},
					dataType:"json",
					beforeSend: function() {						
						blockUI();	
					},
					success:function(result) {	
						var assets = result.assocAssetWithItemResult;
						for(var count=0; count<assets.length; count++) { 
							var assetCont = assets[count].assetCont;
							var screen = $("#editorArea .selected");
							copiedControl = JSON.parse(assetCont.jsonContent);
							assetCreation(copiedControl, screen);
						}
					},
					error: function(e) {
						showAjaxError(e);
						unblockUI();
					},
					complete: unblockUI
				});
											
			} else {
				$controlType = ui.draggable.attr("data-role");
				if($controlType == "mathpalette") {
					if($("#editorArea .selected").find(".mathpalette").length == 0) {
						//createControl($controlType, this, ui.position,true);
						createControl($controlType, this, ui.position,true);
					}
				} else {
					//createControl($controlType, this, ui.position,true);
					createControl($controlType, this, ui.position,true);
				}
			}
		}
	});
}

// making textbox as numeric stepper
function makeSpinner(id, min, max) {
	$("#"+id).spinner({
		min: min,
		numberformat: "n",
		max: max,
		step:1
	});
}

// making sure that inner elements inside divbox, can not be resized more than itself
function makingDrggableControl($control) {
	var $parent, $selectedEditor, screenId = "", role = "", maxWidth = 0, maxHeight = 0, controlId = "", parentPos;
	$control.draggable({
		containment: "parent",
		handle: "> .handle",
		//grid: [1, 1],
		drag: function( event, ui ) {
			parentPos = $(this).parent().offset();
			if(parentPos.top+$(this).parent().height() <= $(this).height()+ui.offset.top) {
				$control.css({'top' : $(this).parent().height()-$(this).height()-2});
				return false;
			} else if(parentPos.left+$(this).parent().width() <= $(this).width()+ui.offset.left) {
				$control.css({'left' : $(this).parent().width()-$(this).width()-2});
				return false;
			} else {
				return true;
			}
		},
		stop: function(event, ui) {
			$parent = ui.helper.parent();
			$selectedEditor = $("#editorArea .selected");
			screenId = $selectedEditor.attr("id");
			controlId = ui.helper.attr("id");
			role = ui.helper.attr("data-role");

			// calculating maximum height and width
			if(role == "divbox") {
				maxWidth = $selectedEditor.width()+$selectedEditor.offset().left-ui.position.left;
				maxHeight = $("body").height()-ui.position.top;
				$control.css({'top' : parseInt(ui.position.top),'left' : parseInt(ui.position.left)});
			} else {
				maxHeight = $parent.height()-ui.position.top;
				maxWidth = $parent.width()-ui.position.left;
			}
			// updating JSON
			editorContent[screenId][controlId].property.x = ui.position.left;
			editorContent[screenId][controlId].property.y = ui.position.top;

			// updating maxwidth and maxheight
			setPerimeterRange(controlId,screenId);
			ui.helper.resizable("option","maxWidth",maxWidth);
			ui.helper.resizable("option","maxHeight",maxHeight);
		}
	});
}

// making any control resizable
function makingResizableControl($control, maxWidth, maxHeight) {
	var child, childrens, childrenId = "", width = 0, height = 0, screenId = "", controlId = "", role, minWidth = 10, controlType = $control.attr("data-role");
	minWidth = ((controlType == "textarea" || controlType == "droparea") ? minWidth :  100);
	$control.resizable({
		helper: "ui-resizable-helper",
		autoHide: true,
		maxWidth:  maxWidth,
		maxHeight: maxHeight,
		minWidth: minWidth,
		minHeight: 16,
		start: function( event, ui ) {
			var controlRole = $control.attr("data-role"),minWidth1 = 10,mw=0,mw1,minHeight1 = 16,mh=0,mh1,element,maxWidth1,maxHeight1;
			var ele = $control.find(".element");
			if(controlRole == "divbox"){
				if(ele.length > 0){
					for(var index = 0; index<ele.length;index++){
						element = ele.eq(index);
						mw1 = parseInt(element.css("left").split("px")[0],10)+element.width()+5;
						mh1 = parseInt(element.css("top").split("px")[0],10)+element.height()+3;
						if(mw < mw1){
							mw = mw1;
						}
						if(mh < mh1){
							mh = mh1;
						}
					}
					minWidth1 = mw;
					minHeight1 = mh;
				}else{
					minWidth1 = 100;
				}
			}else {
				maxWidth1 = $control.parent().innerWidth() - $control.position().left;
				maxHeight1 = $control.parent().innerHeight() - $control.position().top;
				minWidth1= ((controlRole == "textarea" || controlRole == "droparea") ? 10 : 100);
				$(this).resizable("option","maxWidth",maxWidth1-1);
				$(this).resizable("option","maxHeight",maxHeight1-1); 
			}
			$(this).resizable( "option", "minWidth", minWidth1 );
			$(this).resizable( "option", "minHeight", minHeight1 );
		} ,
		stop: function(event, ui) {
			screenId = $("#editorArea .selected").attr("id");
			controlId = ui.element.attr("id");
            setCoordinateRange(controlId,screenId);
			role = $("#"+controlId).attr("data-role");
			if(role!="divbox"){
				setControlHeightWidth(ui.element);	
				adjustControlHeightWidth(ui.element);
			}
			/*if(role=="textarea"){
				var controlEle = $("#"+screenId+" #"+controlId);
				if(controlEle.find(".text").hasClass("centerAlignText") == true){
					if((controlEle.find(".text").outerHeight(true) >= controlEle.find(".text")[0].scrollHeight) && (parseInt($(".ui-resizable-helper").width(),10)+4 >= controlEle.find(".text")[0].scrollWidth)){
						//controlEle.find(".text").css("display","table-cell");	
					}else{
						//controlEle.find(".text").css("display","inline-block");	
					}	
				}
			}*/
			if(controlType=="textarea"){
				var contrl = $("#"+screenId+" #"+controlId);
				if(contrl.attr("textAlignmentCenter") == "2") {
					var diffHt = parseInt(contrl.find(".text").height(),10)- parseInt(contrl.find(".text").children().height(),10);
					if(diffHt < 0){
						contrl.find(".text").children().css("margin-top","0px");
					}else{
						contrl.find(".text").children().css("margin-top",(diffHt/2)+"px");
					}
				}else{
					contrl.find(".text").find(".centerAlignText").css("margin-top","0px");
				}
			}
			editorContent[screenId][controlId].property.height = ui.size.height-2;
			editorContent[screenId][controlId].property.width = ui.size.width-2;
			childrens = ui.element.find(".element");
			for(var index=0; index<childrens.length; index++) {
				child = childrens.eq(index);
				childrenId = child.attr("id");
				maxWidth = ui.element.innerWidth() - child.position().left;
				maxHeight = ui.element.innerHeight() - child.position().top;
				makingResizableControl(child, maxWidth, maxHeight);
				makeSpinner("width", 100, maxWidth-2);
				makeSpinner("height", 16, maxHeight-2);
			}
			ui.element.css({
				top: editorContent[screenId][controlId].property.y,
				left: editorContent[screenId][controlId].property.x
			});
		}
	});
}

function setControlHeightWidth($control) {
	var height = 0, width = 0 , role = $control.attr("data-role");
	if($control.find(".text").length > 0) {
		var parentHeight = $control.outerHeight(true), parentWidth = $control.outerWidth(true);
		if(role == "checkbox" || role == "radio") {
			width = (parentWidth-21);
			height = (parentHeight-2);
		} else {
			width = parentWidth-5;
			height = parentHeight-5;
		}
		$control.children(".text").css({
			"width":width+"px",
			"height":height+"px"
		});
		
	}
}

function setPreviewControlHeightWidth($control) {
	var height = 0, width = 0 , role = $control.attr("data-role");
	if($control.find(".text").length > 0) {
		var parentHeight = $control.outerHeight(true), parentWidth = $control.outerWidth(true);
		if(role == "checkbox" || role == "radio") {
			if($control.attr("borderrequired") == "1"){
				width = (parentWidth-18);
				height = (parentHeight);
			}else{
				width = Math.ceil(parentWidth-20);
				height = Math.ceil(parentHeight-2);
			}
		} else {
			if($control.attr("borderrequired") == "1"){
				width = parentWidth-6;
				height = parentHeight-6;
			}else{
				width = Math.ceil(parentWidth-8);
				height = Math.ceil(parentHeight-8);
			}
		}
		$control.children(".text").css({
			"width":width+"px",
			"height":height+"px"
		});
		
	}
}

function adjustControlHeightWidth($control,edit){
	var height = 0, width = 0 , role = $control.attr("data-role");
	if($control.find(".text").length > 0) {
		var parentHeight = $control.outerHeight(true), parentWidth = $control.outerWidth(true);
		/*if(edit == true){		
			if(role == "checkbox" || role == "radio") {
					width = Math.floor(parentWidth-20);
					height = Math.floor(parentHeight-2);
				} else {
					width = Math.floor(parentWidth-7);
					height = Math.floor(parentHeight-7);
				}
		}else{*/
			if(role == "checkbox" || role == "radio") {
				width = Math.floor(parentWidth-20);
				height = Math.floor(parentHeight-2);
			} else {
				width = Math.floor(parentWidth-8);
				height = Math.floor(parentHeight-8);
			}
		//}
		$control.children(".text").css({
			"width":width+"px",
			"height":height+"px"
		});
		
	}
}

// adding tree node in outline tree
function addTreeNode(appendTo, pos, name, id) {
	$("#outlineTree").jstree("create",appendTo,pos,name,function(a){$(a).attr("id",id);},true);
}

// removing tree node from outline tree
function removeTreeNode(nodeId) {
	$("#outlineTree").jstree("delete_node",nodeId);
}

// renaming outline tree node
function renameTreeNode(nodeId, name) {
	$("#outlineTree").jstree("rename_node",nodeId,name,false);
}

// selecting each element
function selectElement($this) {
	var controlName = getControlName($this.attr("data-role"));
	var screen = $("#editorArea .selected");
	screen.find(".element").removeClass("selected-control");
	$this.addClass("selected-control");
	showStatusMsg(controlName+userInfo.selectControl);
}

// selecting each screen
function selectingScreen($this) {
	var screenIndex = getControlIndex($this.attr("id"));
	$("#editorTab li.ui-state-default").removeClass("ui-tabs-selected");
	$this.addClass("ui-tabs-selected");
	$("#editorArea .editor").hide();
	$("#editorArea .selected").removeClass("selected");
	$("#editor_"+screenIndex).addClass("selected").show();
	var screenName = $("#screenHeader_"+screenIndex).find(".editorTitle").text();
	showStatusMsg(screenName+" selected");
}

// create inner controls like checkbox, radio, textarea and droparea
function createInnerControl(controlData, pasteArea) {
	var $pos = { "top": controlData.y, "left": controlData.x }, controlName, pasteScreenId = pasteArea.attr("id");
	var screenId = $("#editorArea .selected").attr("id"), $childData = createControl(controlData.controltype, pasteArea, $pos,false);
	// positioning element at specified position
	if(controlData.controltype != "divbox") {
		$("#"+screenId+" #"+pasteScreenId+" #"+$childData.id).css($pos);
	}
	$childData = reArrangeProperties($childData, controlData);
	controlName = getControlName(controlData.controltype);
	// applying preselected properties to the newly created control
	applyPropertiesToControl($childData, $("#"+screenId+' #'+$childData.id));
	
	editorContent[screenId][$childData.id].property = $childData;
	$("#"+screenId+" #"+$childData.id).find(".text").html($childData.text);//Added for IQA defect fix by offshore
	var parentHeight=$("#"+screenId+" #"+$childData.id).outerHeight(),parentWidth=$("#"+screenId+" #"+$childData.id).outerWidth();
	if(controlData.controltype == "checkbox" || controlData.controltype == "radio") {
		$("#"+screenId+" #"+$childData.id).find(".text").css("width",(parentWidth-20)+"px");
		$("#"+screenId+" #"+$childData.id).find(".text").css("height",(parentHeight-2)+"px");
	}else{
		$("#"+screenId+" #"+$childData.id).find(".text").css({"width":parentWidth-8+"px","height":parentHeight-8+"px"});
	}
	/*if(controlData.controltype=="textarea"){
		var controlEle = $("#"+screenId+" #"+$childData.id);
		if(controlEle.find(".text").hasClass("centerAlignText") == true){
			if((controlEle.find(".text").outerHeight(true) >= controlEle.find(".text")[0].scrollHeight) || (parseInt(editorContent[screenId][$childData.id].property["width"],10)+4 < controlEle.find(".text")[0].scrollWidth)){
				//controlEle.find(".text").css("display","table-cell");	
			}else{
				//controlEle.find(".text").css("display","inline-block");	
			}	
		}else{
			//controlEle.find(".text").css("display","inline-block");	
		}
	}*/
	if(controlData.controltype=="textarea"){
		var contrl = $("#"+screenId+" #"+$childData.id);
		var textAlignCenter = editorContent[screenId][$childData.id].property.textAlignmentCenter;
		if(textAlignCenter == "2"){
			if (contrl.find(".text").find(".centerAlignText").length < 1){
				contrl.find(".text").wrapInner( "<div class='centerAlignText'></div>");
			}
		}else if(textAlignCenter == "1"){
			if (contrl.find(".text").find(".centerAlignText").length > 0){
				contrl.find(".text").find(".centerAlignText").unwrap();
			}
		}
		if(textAlignCenter == "2") {
			var diffHt = parseInt(contrl.find(".text").height(),10)- parseInt(contrl.find(".text").children().height(),10);
			if(diffHt <0){
				contrl.find(".text").children().css("margin-top","0px");
			}else{
				contrl.find(".text").children().css("margin-top",(diffHt/2)+"px");
			}
		}else{
			contrl.find(".text").find(".centerAlignText").css("margin-top","0px");
		}
	}
	showStatusMsg(controlName+userInfo.controlPasted);
	return $childData;
}

// create container controls like divbox and droparea
function createContainerControl(controlData, pasteArea) {
	var $pos = {}, controlName = getControlName(controlData[0].controltype), pasteScreenId = pasteArea.attr("id"),scoreMap={},$childData,childId, children = new Array();
		$pos.top = parseInt(controlData[0].y,10);
		$pos.left = parseInt(controlData[0].x,10);
	var scoreObj = copiedControl.scoring;
	// positioning element at fixed position
	var screenId = $("#"+copiedControl.screenId).attr("id");
	var $data = createControl(controlData[0].controltype, pasteArea, $pos,false);
	var dataId=$data.id; 
	var elementId=$("#" + copiedControl.id).attr("id");
	// applying preselected properties to the newly created control
	$data = reArrangeProperties($data, controlData[0]);
	applyPropertiesToControl($data, $("#"+pasteScreenId+" #"+$data.id));
	editorContent[pasteScreenId][$data.id].property = $data;
    scoreMap[controlData[0].id] = $data.id; 
	for(var count=1; count<controlData.length;count++){
		children.push(controlData[count].id);
	}
	for(var index=0; index<children.length; index++) {
		childId = String(children[index]);
		//controlData = editorContent[screenId][childId].property;
		childControlData = controlData[index+1];
		pasteArea = $("#"+pasteScreenId+" #"+$data.id);
		$childData= createInnerControl(childControlData, pasteArea);
		scoreMap[childId] = $childData.id;
	}
	if(scoreObj.complete){
		updateScoreContent(dataId,pasteArea,scoreMap);
	}
	showStatusMsg(controlName+userInfo.controlPasted);
}

// this function copy properties into new one
function reArrangeProperties(newData, oldData) {
	for(var prop in oldData) {
		if(prop == "id" || /*prop == "name" ||*/ prop=="screenId" || (newData[prop] === "" && prop != "alternatetext")) { continue;}	
		else if(prop == "scoring") {
			$.extend(true, newData[prop], oldData[prop]);
		} else { 
			newData[prop] = oldData[prop]; 
		}
	}
	return newData;
}

// checking whether different controls are mixed up or not
function isMixedControls(controlData, pasteArea) {
	var flag = false, $pasteArea = $(pasteArea);
	if($pasteArea.children(".element").length > 0) {
		if(controlData.controltype == "radio" && $pasteArea.find(".radio").length == 0) { flag = true; }
		else if(controlData.controltype == "checkbox" && $pasteArea.find(".checkbox").length == 0) { flag = true; }
		else if((controlData.controltype == "textarea" || controlData.controltype == "droparea") && ($pasteArea.find(".textarea").length == 0 && $pasteArea.find(".droparea").length == 0)) { flag = true; }
	}
	return flag;
}

// applying altered property values to the control
function applyPropertiesToControl(property, $control) {
 	var val, prop, screenId = $("#editorArea .selected").attr("id"), controlId = $control.attr("id"),prop_height=property.height,prop_width=property.width;	
	for(prop in property) {
		val = property[prop];
		if(prop != "id" && /*prop != "name" &&*/ prop != "children" && prop != "scoring" && val != "") {
			if(prop == "background") {
				if(val.indexOf("rgb")>=0){
					$control.css("background-color",val);
				}else{
				$control.css("background-color","#"+val);
				}
			} else if(prop == "border") {
				if(val.indexOf("rgb")>=0){
					$control.css("border",val);
				}else{
					$control.css("border","1px solid #"+val);
				}
			}  else if(prop == "text") {
				$control.find(".text").html(val);
				$control.find(".text").css({
					"height":prop_height+"px",
					"width":prop_width+"px"
				});
			} else if(prop == "width" || prop == "height") {
				$control.css(prop,val+"px");
			} else if(prop == "x") {
				$("#"+screenId+" #"+controlId).css("left",val+"px");
			} else if(prop == "y") {
				$("#"+screenId+" #"+controlId).css("top",val+"px");
			} else if(prop == "ansArea") {
				if(val == true){
					$control.attr("ansarea",true);
				}else{
					$control.attr("ansarea",false);
				}
			} else if(prop == "textAlignmentCenter") {
				$control.find(".text").removeClass("centerAlignText");
				$control.attr(prop,val);
			}else {
				$control.attr(prop,val);
			}
		}
		if(val == "" && prop == "name"){
			$control.attr(prop,val);
		}
	}
	renameTreeNode("#tr-"+screenId+"-"+controlId, $control.attr("name"));
}

// updating children array on sorting elements
function swapChildrenPosition(element) {
	var $ele = $(element), $children = $ele.children(".element"), screenId = $("#editorArea .selected").attr("id");
	var controlId = $ele.attr("id"), children = new Array();
	for(var index=0; index<$children.length; index++) {
		children.push($children.eq(index).attr("id"));
	}
	editorContent[screenId][controlId].property.children = children;
}

function updateOperationQueue(eventType, oldValue, newValue) {
	// eventType will be "resizing", "sorting", "creating", "deleting", "dragging"
}

//restricting the max value of coordinate spinner
function setCoordinateRange(controlId,screenId)
{
	var control = $("#"+screenId+" #"+controlId), role = control.attr("data-role"), screen = $("#"+screenId), widthDiff, heightDiff;
	if(role== "divbox") {
		var editorHeight = screen.height(), editorWidth = screen.width(), elementHeight = control.height()+1, elementWidth = control.width()+1;
		widthDiff = parseInt((editorWidth-elementWidth),10);
		heightDiff = parseInt((editorHeight-elementHeight),10);
	} else {
		var parent = control.parent(), parentHeight = parent.height(), parentWidth = parent.width(), childHeight = control.height()+3, childWidth = control.width()+3;
		widthDiff = parseInt((parentWidth-childWidth),10);
		heightDiff = parseInt((parentHeight-childHeight),10);
	}
	makeSpinner("x", 0, widthDiff);
	makeSpinner("y", 0, heightDiff);
}

//restricting the max value of height and width spinner
function setPerimeterRange(controlId,screenId) {
	var control = $("#"+screenId+" #"+controlId), role = control.attr("data-role"), screen = $("#"+screenId), controlPos = control.offset(), widthDiff, heightDiff,minWidth = 10,mw=0,mw1,minHeight = 16,mh=0,mh1;
	if(role== "divbox") {
		var editorHeight = screen.height(), editorWidth = screen.width(), screenPos = screen.offset();
		widthDiff = parseInt((editorWidth-(controlPos.left-screenPos.left)-1),10);
		heightDiff = parseInt((editorHeight-(controlPos.top-screenPos.top)-1),10);
		var ele = control.find(".element");
				if(ele.length > 0){
					for(var index = 0; index<ele.length;index++){
						element = ele.eq(index);
						mw1 = parseInt(element.css("left").split("px")[0],10)+element.width()+5;
						mh1 = parseInt(element.css("top").split("px")[0],10)+element.height()+3;
						if(mw < mw1){
							mw = mw1;
						}
						if(mh < mh1){
							mh = mh1;
						}
					}
					minWidth = mw;
					minHeight = mh;
				}else{
					minWidth = 100;
				}
			

	} else {
		var parent = control.parent(), parentHeight = parent.height(), parentWidth = parent.width(), parentPos = parent.offset();
		widthDiff = parseInt((parentWidth-(controlPos.left-parentPos.left)-1),10);
		heightDiff = parseInt((parentHeight-(controlPos.top-parentPos.top)-1),10);
		minWidth= ((role == "textarea" || role == "droparea") ? 10 : 100);
	}
	
	makeSpinner("width", minWidth, widthDiff);
	makeSpinner("height", minHeight, heightDiff);
}

function blockUI() {
	$("body").append("<div id='blockPage' class='block-page'><div class='block-page-loading'></div></div>");
}

function unblockUI() {
	$("#blockPage").remove();
}

// this is for displaying information to the user
function displayUserInfoDialog(type, msg, title) {
	var $userInfo = $("#userInfo");
	if(type == "success") {
		$userInfo.addClass("ui-state-highlight");
		$userInfo.removeClass("ui-state-error");
		$userInfo.find(".ui-icon").addClass("ui-icon-info");
		$userInfo.find(".ui-icon").removeClass("ui-icon-alert");
	} else if(type == "error") {
		$userInfo.removeClass("ui-state-highlight");
		$userInfo.addClass("ui-state-error");
		$userInfo.find(".ui-icon").removeClass("ui-icon-info");
		$userInfo.find(".ui-icon").addClass("ui-icon-alert");
	}
	$userInfo.find(".info").html(msg);
	title = (title) ? title : "Item Saving";
	$("#userInfoDialog").dialog("option","title",title);
	$("#userInfoDialog").dialog("open");
}

function showAjaxError (response) {
	var displayMsg = 'Error: ';
	if (response != undefined) { 
		var myJSONObject = $.parseJSON(response.responseText);
		displayMsg += myJSONObject.exceptionMessage;
	}	
	displayUserInfoDialog("error",displayMsg,"Error");	
}

var newObj = '';
function correctImageTag(htmlData){
	var str = htmlData;
	newObj = '';
	var objarr = str.split('<img');
	for(var i=0; i<objarr.length; i++) {
		if(i == 0) 
			modifyImageTag( objarr[i]  );
		else if(i > 0) 
			modifyImageTag( '<img '+objarr[i]  );
	}
	htmlData = newObj;	
	return htmlData;
}

function modifyImageTag(obj) {
	var loc =  obj.indexOf('<img');	
	if( loc != -1 ) { 
		newObj = newObj + obj.substring(0, loc);
		var subs = obj.substring(loc, obj.length);
		var newLoc = subs.indexOf('>');
		var endTagLoc = subs.indexOf('/>');		
		if(endTagLoc == -1 || (newLoc < endTagLoc)) { // no end tag of image present
			newObj = newObj + subs.substring(0, newLoc);
			newObj = newObj + '/>' + subs.substring(newLoc+1, obj.length);
		} else {
			newObj = newObj + subs.substring(0, newLoc);
			newObj = newObj + '>' + subs.substring(newLoc+1, obj.length);
		}		
	} else {
		newObj = newObj + obj;
	}
}

function updateScoreContent(dataId, pasteArea, scoreMap) {
	var pasteScreenId, screenId, elementId, scoring, correctAns, incorrectAns, crAns, crWeight, incrAns, incrWeight, interactionType,dndCorrectAns,dndIncorrectAns,correctQs,mcqSeqCorrectAns,mcqSeqIncorrectAns,dndSequence,mcqSequence,mcqSeqcrAns,mcqSeqincrAns,mcqSequenceId,dndSequences;
	pasteScreenId = $("#editorArea .selected").attr("id");
	if(copiedControl.scoring==undefined){
		screenId = $("#" + copiedControl.elementGroup.screenId).attr("id");
		elementId = $("#" + copiedControl.elementGroup.id).attr("id");
		scoring = copiedControl.elementGroup.scoring;
		correctAns = scoring.correctAns;
		incorrectAns = scoring.incorrectAns;
		interactionType = scoring.interactionType;
		dndCorrectAns = copiedControl.elementGroup.scoring.correctAns;
		dndIncorrectAns = copiedControl.elementGroup.scoring.incorrectAns;
		mcqSeqCorrectAns = copiedControl.elementGroup.scoring.mcqSeqCorrectAns;
		mcqSeqIncorrectAns = copiedControl.elementGroup.scoring.mcqSeqIncorrectAns;
		mcqSequence=copiedControl.elementGroup.scoring.sequence;
		dndSeqCorrectAns = copiedControl.elementGroup.scoring.dndSeqCorrectAns;
		dndSeqIncorrectAns = copiedControl.elementGroup.scoring.dndSeqIncorrectAns;
		dndSequence=copiedControl.elementGroup.scoring.sequence;	
	}
	else{
		screenId = $("#" + copiedControl.screenId).attr("id");
		elementId = $("#" + copiedControl.id).attr("id");
		scoring = copiedControl.scoring;
		correctAns = scoring.correctAns;
		incorrectAns = scoring.incorrectAns;
		interactionType = scoring.interactionType;
		dndCorrectAns = copiedControl.scoring.correctAns;
		dndIncorrectAns = copiedControl.scoring.incorrectAns;
		mcqSeqCorrectAns = copiedControl.scoring.mcqSeqCorrectAns;
		mcqSeqIncorrectAns = copiedControl.scoring.mcqSeqIncorrectAns;
		mcqSequence=copiedControl.scoring.sequence;
		dndSeqCorrectAns = copiedControl.scoring.dndSeqCorrectAns;
		dndSeqIncorrectAns = copiedControl.scoring.dndSeqIncorrectAns;
		dndSequence=copiedControl.scoring.sequence;
	}
	if (interactionType == "2") {
		for (index = 0; index < correctAns.length; index++) {
			crAns = correctAns[index].correctAns;
			editorContent[pasteScreenId][dataId].property.scoring.correctAns[index].correctAns = scoreMap[crAns];
		}
		for (index = 0; index < incorrectAns.length; index++) {
			incrAns = incorrectAns[index].incorrectAns;
			editorContent[pasteScreenId][dataId].property.scoring.incorrectAns[index].incorrectAns = scoreMap[incrAns];
		}
	} else if (interactionType == "3") {		 
		for ( var index = 0; index < dndCorrectAns.length; index++) {
			correctAns = dndCorrectAns[index].correctAns;
			correctQs= dndCorrectAns[index].correctQs;
			editorContent[pasteScreenId][dataId].property.scoring.correctAns[index].correctQs = scoreMap[correctQs];
			for ( var index1 = 0; index1 < correctAns.length; index1++) {
				crAns = correctAns[index1];
				editorContent[pasteScreenId][dataId].property.scoring.correctAns[index].correctAns[index1] = scoreMap[crAns];
			}
		}
		for ( var index = 0; index < dndIncorrectAns.length; index++) {
			incorrectAns = dndIncorrectAns[index].ans;
			editorContent[pasteScreenId][dataId].property.scoring.incorrectAns[index].ans = scoreMap[incorrectAns];
		}
	} else if (interactionType == "4") {
		for (var index = 0; index < mcqSeqCorrectAns.length; index++) {
			mcqSeqcrAns = mcqSeqCorrectAns[index].correctAns;
			editorContent[pasteScreenId][dataId].property.scoring.mcqSeqCorrectAns[index].correctAns = scoreMap[mcqSeqcrAns];
		}
		for (var index = 0; index < mcqSeqIncorrectAns.length; index++) {
			mcqSeqincrAns = mcqSeqIncorrectAns[index].ans;
			editorContent[pasteScreenId][dataId].property.scoring.mcqSeqIncorrectAns[index].ans = scoreMap[mcqSeqincrAns];
		}
		for (var index = 0; index < correctAns.length; index++) {
			crAns = correctAns[index].correctAns;
			editorContent[pasteScreenId][dataId].property.scoring.correctAns[index].correctAns = scoreMap[crAns];
		}
		for (var index = 0; index < incorrectAns.length; index++) {
			incrAns = incorrectAns[index].incorrectAns;
			editorContent[pasteScreenId][dataId].property.scoring.incorrectAns[index].incorrectAns = scoreMap[incrAns];
		}
		for (var index = 0; index < mcqSequence.length; index++) {
			mcqSequenceId = mcqSequence[index].id;
			editorContent[pasteScreenId][dataId].property.scoring.sequence[index].id = scoreMap[mcqSequenceId];
		}

	}else if (interactionType == "5" || interactionType == "9") {
		for (var index = 0; index < dndSeqCorrectAns.length; index++) {

			dndSeqcrAns = dndSeqCorrectAns[index].ctAns;
			dndSeqcrId = dndSeqCorrectAns[index].ctId;
			for(var index1=0;index1<dndSeqcrAns.length;index1++){
				dndcrAns=dndSeqcrAns[index1];
				dndcrId=dndSeqcrId[index1];
				editorContent[pasteScreenId][dataId].property.scoring.dndSeqCorrectAns[index].ctAns[index1] = dndcrAns;
				editorContent[pasteScreenId][dataId].property.scoring.dndSeqCorrectAns[index].ctId[index1] = scoreMap[dndcrId];
			}
		}
		for (var index = 0; index < dndSequence.length; index++) {
			dndSequences = dndSequence[index];
			editorContent[pasteScreenId][dataId].property.scoring.sequence[index] = dndSequences;
		}
	}
}

function assetCreation(copiedControl, screen){
	var index=0,eleGrpId,$pos={},scoreMap={};
	var pasteScreenId = screen.attr("id");
	var controlType=copiedControl.elementGroup.controltype;
	$pos.top = parseInt(copiedControl.elementGroup.y,10);
	$pos.left = parseInt(copiedControl.elementGroup.x,10);
	var $elementdata = createControl(controlType, screen, $pos,false);
	// applying preselected properties to the newly created control
	$elementdata = reArrangeProperties($elementdata, copiedControl.elementGroup);
	applyPropertiesToControl($elementdata, $("#"+pasteScreenId+" #"+$elementdata.id));
	editorContent[pasteScreenId][$elementdata.id].property = $elementdata;
	scoreMap[copiedControl.elementGroup.id] = $elementdata.id;
	for(var index=0;index<copiedControl.children.length;index++){
		
		var childItem=copiedControl.children[index];
		var controlType=childItem.controltype;
		$pos.top = parseInt(childItem.y,10);
		$pos.left = parseInt(childItem.x,10);
		var $data = createControl(controlType, $("#"+pasteScreenId+" #"+$elementdata.id),$pos,false);
		// applying preselected properties to the newly created control
		$data = reArrangeProperties($data, childItem);
		applyPropertiesToControl($data, $("#"+pasteScreenId+" #"+$data.id));
		var parentHeight=$("#"+pasteScreenId+" #"+$data.id).outerHeight(),parentWidth=$("#"+pasteScreenId+" #"+$data.id).outerWidth();
		if(controlType == "checkbox" || controlType == "radio") {
			$("#"+pasteScreenId+" #"+$data.id).find(".text").css("width",(parentWidth-20)+"px");
			$("#"+pasteScreenId+" #"+$data.id).find(".text").css("height",(parentHeight-2)+"px");
		}else{
			$("#"+pasteScreenId+" #"+$data.id).find(".text").css({"width":parentWidth-8+"px","height":parentHeight-8+"px"});
		}
		editorContent[pasteScreenId][$data.id].property = $data;
		if(controlType=="textarea"){
			var contrl = $("#"+pasteScreenId+" #"+$data.id);
			var textAlignCenter = editorContent[pasteScreenId][$data.id].property["textAlignmentCenter"];
			if(textAlignCenter == "2"){
				if (contrl.find(".text").find(".centerAlignText").length < 1){
					contrl.find(".text").wrapInner( "<div class='centerAlignText'></div>");
				}
			}else if(textAlignCenter == "1"){
				if (contrl.find(".text").find(".centerAlignText").length > 0){
					contrl.find(".text").find(".centerAlignText").unwrap();
				}
			}
			if(textAlignCenter == "2") {
				var diffHt = parseInt(contrl.find(".text").height(),10)- parseInt(contrl.find(".text").children().height(),10);
				if(diffHt < 0){
					contrl.find(".text").find(".centerAlignText").css("margin-top","0px");
				}else{
					contrl.find(".text").children().css("margin-top",(diffHt/2)+"px");
				}
			}else{
				contrl.find(".text").find(".centerAlignText").css("margin-top","0px");
			}
		}
		scoreMap[childItem.id] = $data.id;
	}
	if(copiedControl.elementGroup.scoring.complete==true){
		updateScoreContent($elementdata.id, screen, scoreMap);
	}
}

function prepareAltText()  {
	 var titleList = $("#editorArea").find("[title]"), titleDataList = $("#toolbar").find("[title-data]"), ttsList = $("#editorArea").find(".element:not([suppresstts]):not(.divbox)");
	 replaceAttribute(titleList, "title", "title-data");
	 replaceAttribute(titleDataList, "title-data", "title");
	 for(var index=0; index<ttsList.length; index++) {
		ttsList.eq(index).attr("suppresstts","1");
	 }
}

function replaceAttribute(list, oldAttr, newAttr) {
	var index=0, ele, text = "";
	for(index=0; index<list.length; index++) {
		ele = list.eq(index);
		text = ele.attr(oldAttr);
		if(text) {
			ele.attr(newAttr,text);
			ele.removeAttr(oldAttr);
		}
	 }
}
function replaceSpecialChar(val) {
	val = val.replace("&rdquo;","\u201D");
	val = val.replace("&ldquo;","\u201C");
	val = val.replace("&rsquo;","\u2019");
	val = val.replace("&lsquo;","\u2018");
	return val;
}

function resetFontName(html) {
	var itemHtml = String(html);
	itemHtml = itemHtml.replace(/\b(oasmathv3)\b/gi,"oasmathv3regular");
	itemHtml = itemHtml.replace(/\b(CTB)\b/gi,"oasmathv3regular");
	return itemHtml;
}
/*
function removeSmallScrollbars() {
	console.log("without latest changes starts:  "+new Date().getTime());
	var texts = $(".text"), scrollHeight = 0, height = 0, txt;	
	for(var index=0; index<texts.length; index++) {
		txt = texts.eq(index);
		scrollHeight = txt[0].scrollHeight;
		height = txt.outerHeight(true);			
		if(scrollHeight >= height && Math.abs(scrollHeight-height) <= 5) {
			txt.css("overflow","hidden");
		}
	}
	console.log("without latest changes ends:  "+new Date().getTime());
}*/
/*79461 starts*/

function removeSmallScrollbars() {
console.log("latest changes starts:  "+new Date().getTime());
	var texts = $(".text"), scrollHeight = 0, height = 0, txt,enlargedTxt = new Array();	
	for(var index=0; index<texts.length; index++) {
		txt = texts.eq(index);
		scrollHeight = txt[0].scrollHeight;
		height = txt.outerHeight(true);
		scrollWidth = txt[0].scrollWidth;
		width = txt.outerWidth(true);			
		if(scrollHeight >= height) {
			if(Math.abs(scrollHeight-height) <= 5){
				txt.css("overflow","hidden");
			}else{
				var enlarged=shortened=0;
				var actualWdt = txt.width();
				txt.css("width",actualWdt+20+"px");
				enlargedTxt.push({
					"text": txt,
					"width": actualWdt
				});
			}
		}else if(scrollWidth >= width && Math.abs(scrollWidth-width) <= 5) {
			txt.css("overflow","hidden");
		}
	}
		for(var i=0;i<enlargedTxt.length;i++){
			var textCont = enlargedTxt[i].text;
			var textContWd = enlargedTxt[i].width;
			$(textCont).css("width",textContWd+"px");
		}
	console.log("latest changes ends:  "+new Date().getTime());
} 
/*79461 ends*/

function getScoringErrMsg(screenArr,callPage) {
	var i = 0, j = 0, elementNames = "", name = "", scrId, eleId, eleArr;
	var msg = ((screenArr.length == 0)? "":"<ul>");
	for(i=0; i<screenArr.length; i++) {
		scrId = screenArr[i].screenId;		
		eleArr = screenArr[i].elements.split(",");
		elementNames = "";
		if(screenArr[i].elements != "") {
			for(j=0; j<eleArr.length; j++) {
				eleId = eleArr[j];
				name = ((callPage == "P") ? editorContentJSON[scrId][eleId].property.name : editorContent[scrId][eleId].property.name);
				elementNames += ", "+((name == "") ? userInfo.noName : name);
			}
		} else {
			elementNames = "";
		}
		msg += "<li><span class='bold'></span><span>"+(callPage == "E" ? getScreenId(scrId) : parent.getScreenId(scrId) )+"</span>";
		if(elementNames != "") {
			msg += ":"+elementNames.substring(1,elementNames.length);
		} 
		msg += "</li>";		
	}
	if(msg != "") {
		msg += "</ul>";
	} else {
		msg = "";
	}
	return msg;
}

function getScreenId(screenId) {
  var screenIndex = screenId.split("_")[1];
  return $("#screenHeader_" + screenIndex).text();
}

function removeTtsAttr(){
	var noOfDiv=$("#previewArea").find(".divbox");
	for(var i=0;i<noOfDiv.length;i++){
	  noOfDiv.eq(i).removeAttr("suppresstts");
	}
}