 var editorContentJSON, accomPkg, isStandAlone = false,
    droppedSequence, isPageLoaded = true,
    accomPkgScoreOb = 0,
    accomPkgPossibleSc = 0;

$(document).ready(function () {
      /* load custom font file dynamically */
	  if ($.browser.msie) {
	    $("head").append("<link rel='stylesheet' href='../../TECommonPkg/styles/css/ie-font.css' type='text/css' />");
	  } else if ($.browser.mozilla) {
	    $("head").append("<link rel='stylesheet' href='../../TECommonPkg/styles/css/moz-font.css' type='text/css' />");
	  } else {
	    $("head").append("<link rel='stylesheet' href='../../TECommonPkg/styles/css/tdc-font.css' type='text/css' />");
	  }

   scaleDropArea();

    /* hides the customized dropdown in mcqs*/
    $(document).live("click", function (event) {
        $("div.select > ul > li").hide();
        $("div.select").css("z-index", "0");
    });

    /* hides the customized dropdown in mcqs if text div is clicked*/
    $(".text").live("click", function (event) {
        $("div.select > ul > li").hide();
        $("div.select").css("z-index", "0");
    });

    /* hides the customized dropdown after answer is chosen in mcqs*/
    $("div.select").live("click", function (event) {
        var $this = $(this);
        if ($this.css("opacity") == 1) {
            $("div.select > ul > li").hide();
            $this.find("li.option").toggle(0);
            $("div.select").css("z-index", "0");
            $this.css("z-index", "999");
            event.stopPropagation();
        }
    });


    /* displays the value of the chosen customized dropdown in mcqs*/
    $("li.option").live("click", function (event) {
        $("div.select").find("li.option").hide();
        $("div.select").css("z-index", "0");
        $(this).parents().siblings(".selected_text").html($(this).html());
        event.stopPropagation();
    });

    isStandAlone = (($("#htmlUrl").length > 0) ? true : false);

    /* an object of accommodationPKG is created*/
    if (isStandAlone) {
        accomPkg = new accommodationPKG();
    }

    if (!isStandAlone) {
        scaleDropArea();
    }

    /* draggable item is removed on clicking cross button */
    $(".item-remove").live("click tap", function (evt) {
        removeItem(this);
    })

    /* pagination is initiated */
    $(":button", "#pagination").live("click", paginateScreen);

    /* closes the preview of item if window cross button is clicked */
    $("#hiddenButton").click(function () {
        var html = parent.$("#editorArea").children().clone();
        var pos = parent.$("#editorArea").position();
        var selectedScreen = parent.$("#editorArea .selected")
            .attr("id");
        activateControls(html, pos, parent.editorContent,
            selectedScreen);
    });

    /* checks whether preview is done from authoring tool or outside*/
    if (isStandAlone) {
        loadPreviewMode();
    } else {
        if ($("#isPreviewMode").val() == "true") {
            loadPreviewMode();
        }
    }

    /* prevents toggling of checkbox selection on spacebar press*/
    $("input").on('keypress', function (event) {
        var $this = $(this);
        if (event.keyCode == 0) {
            if ($this.is(":checked")) {
                $this.attr('checked', false);
            } else {
                $this.attr('checked', true);
            }
        }
		sendNotification();
    });

    $("#previewArea :checkbox").die("change").live("change", function (evt) {
        sendNotification();
    });

    setZIndexValue(); // z-index for all the elements are set
    initLayout(); // preview layout is initialized
    initEvents(); // Events in the preview page are initialized
    initDialog(); // Dialogs in the Preview page are initialized

    //converts text to speech
    $(".element .text, .element[behavior='2'] .handle").die("dblclick").live("click", textToSpeechEvent);

    prepareAltText();

    /* sets a class for selected customized dropdown value */
    $('#previewArea div.select ul li.option').die("click").live("click", function () {
        $(this).siblings().removeClass('selected').addClass('selected');
    });

    removeTtsAttr();
    /*defect */
    $("body").unbind("mouseleave").mouseleave(function (evt) {
        $("body").trigger("mouseup");
    });
    var ele, textFreeFlowComponents = $("#previewArea div[interactiontype='7']");
    for (var index = 0; index < textFreeFlowComponents.length; index++) {
        ele = textFreeFlowComponents.eq(index).find(".text");
        makeFFDraggable(ele);
    }
    var mathBtn = $(".palette-button").css("background-image", "");
	
});

function makeFFDraggable(ele) {
    ele.on("dragstart", function (evt) {

        var sel = window.getSelection();
        var drag = $(sel.anchorNode.parentNode).parents(".divbox").attr("id");
        blokFreeFlowTextSelection();
        var text = getFreeFlowSelectionHtml();
        if (text != undefined) {
            $("body").append("<div id = '" + drag + "' class = 'ffText'>" + text + "</div>");
			//$("body").append("<div class = 'draggabletxt'>" + $('.ffText').text() + "</div>");
            var $moveable = $('.ffText');
			//var $moveable = $('.draggabletxt');
            $(document).mousemove(function (e) {
                var isOpera = !! window.opera || navigator.userAgent.indexOf(' OPR/') >= 0;
                var isChrome = !! window.chrome && !isOpera;
                var scaleX = Number($("#scaleX").val()),scaleY = Number($("#scaleY").val());
				scalingContent($moveable, scaleX, scaleY);
				$moveable.css({
                    'top': e.pageY,
                    'left': e.pageX,
                    "width": "400px",
                    "height":"250px",
                    "font-size":"12px"
                });
            });
            evt.preventDefault();
            return false;
        }

    });

}
//checks if the rendering environment is ipad
function isPad() {
    var isiPad = navigator.userAgent.match(/iPad/i) != null;
    var ua = navigator.userAgent;
    var isiPad = /iPad/i.test(ua) || /iPhone OS 3_1_2/i.test(ua) || /iPhone OS 3_2_2/i.test(ua);
    return isiPad;
}

// z-index for all the elements are set

function setZIndexValue() {
    var elements = $(".element:not([sharedstimulusdiv='true'],[sharedstimulus='true'])") /*$(".element")*/ ,
        id, ele;
    for (var index = 0; index < elements.length; index++) {
        ele = elements.eq(index);
        if (ele.parents("div.divbox[interactiontype=4]").length == 0) {
            id = parseInt(ele.attr("id").split("_")[1], 10);
            ele.css("z-index", id + 1);
        }
    }
}

// make custom radio and check-box buttons

function makeCustomButtons() {
    var radios = $(":radio"),
        $ele;
    for (var index = 0; index < radios.length; index++) {
        $ele = radios.eq(index);
        $ele.hide();
        $ele.after("<div class='radio-button' onclick='checkRadioBtn(this);'></div>");
    }
}

function checkRadioBtn(obj) {
    var $elem = $(obj).prev(":radio");
    $elem.trigger("mousedown");
    $elem.trigger("click");
}

function checkCheckboxBtn(obj) {
    var $elem = $(obj).prev(":checkbox");
    $elem.trigger("mousedown");
    $elem.trigger("click");
}
// 

function replaceImage() {
    var bkImg, attrs, img, imgList = $("img"),
        imgHeight, imgWidth;
    for (var index = 0; index < imgList.length; index++) {
        img = imgList.eq(index);
        attrs = getAttrMap(img.get(0).attributes);
        bkImg = $("<div></div>", attrs);
        imgHeight = img.height() + "px";
        imgWidth = img.width() + "px";
        bkImg.css({
            "height": imgHeight,
            "width": imgWidth,
            "background": "url(" + attrs["src"] + ") 100% 100%"
        });
        img.parent().append(bkImg);
        img.remove();
    }
}

//

function getAttrMap(attrMap) {
    var attrs = {};
    $.each(attrMap, function (i, e) {
        attrs[e.nodeName] = e.nodeValue;
    });
    return attrs;
}

//replacing the default radio button with the customized one

function replaceCustomRadioButton() {
    var radioChecked, allRadio = $(":radio"),
        input;
    var setCurrent = function (e) {
        var obj = e.target;
        radioChecked = $(obj).attr('checked');
    }
    var setCheck = function (e) {
        if (e.type == 'keypress' && e.charCode != 32) {
            return false;
        }
        setTimeout(function () {
            var obj = e.target;
            if (radioChecked) {
                $(obj).attr('checked', false);
                $(obj).next(".radio-button").removeClass("checkedRadio");
            } else {
                var inputs = $(obj).parent().parent().find(":radio");
                for (var i = 0; i < inputs.length; i++) {
                    input = inputs.eq(i);
                    input.attr('checked', false);
                    input.next(".radio-button").removeClass("checkedRadio");
                }
                $(obj).attr('checked', true);
                $(obj).next(".radio-button").addClass("checkedRadio");
            }
			 sendNotification();
        }, 1);
       
    }
    allRadio.die("mousedown keydown").live("mousedown keydown", setCurrent);
    allRadio.die("click").live("click", setCheck);
}

//converts text to audio

function textToSpeechEvent(evt) {
    var $this = $(this);
    evt.preventDefault();
    evt.stopImmediatePropagation();
    stopPlaying();
    var title = $this.parent().attr("title-data"),
        textContent = $this.parent().children(".text").text();
    title = (($.trim(title) == "") ? $.trim(textContent) : $.trim(title));
    var audioRequired = $(parent).find("body").eq(0).find(".sound").length;
    //var audioRequired = parent.$("body").find(".sound").length;
    var suppresstts = parseInt($this.parent().attr("suppresstts"), 10);

    if ((audioRequired == 1) && (suppresstts == 1)) {
        textTospeech(title, $this);
    }
}

//stops the audio button when multiple texts are played

function stopPlaying() {
    var audio = $(".audio");
    if (audio.length > 0) {
        audio.find("embed")[0].stop();
        audio.remove();
    }
}

// audio tag is created

function textTospeech(title, $this) {
    $.ajax({
        type: "GET",
        cache: false,
        dataType: "text",
        data: {
            "textContent": title
        },
        beforeSend: function () {
            blockUI();
        },
        url: "getAltTextAudioFile.do?timestamp=" + Number(new Date()),
        success: function (data) {
            if (data != undefined) {
                data = JSON.parse(data);
                var audiofilePath = data.audioFilePath;
                var audioPresent = $("#previewArea").find(".audio").length;
                if (audioPresent == 0) {
                    var html = "<div id='audio' class='audio' style='display:none'><embed height='100' width='100' src='" + audiofilePath + "'></div>";
                    $this.parent().append(html);
                } else {
                    $("#previewArea").find(".audio").remove();
                    var html = "<div id='audio' class='audio' style='display:none'><embed height='100' width='100' src='" + audiofilePath + "'></div>";
                    $this.parent().append(html);
                }
            }
        },
        error: function () {

        },
        complete: function () {
            unblockUI();
        }
    });
}

//loads preview page from outside tool

function loadPreviewMode() {
    var html, pos, selectedScreen, jsonUrl = "",
        htmlUrl = "";
    if (isStandAlone) {
        jsonUrl = $("#jsonUrl").val() + "?timestamp=" + Number(new Date());
        htmlUrl = $("#htmlUrl").val() + "?timestamp=" + Number(new Date());
    } else {
        jsonUrl = "getJsonContent.do?callingPage=" + $("#callingPage").val();
        htmlUrl = "getHtmlContent.do?callingPage=" + $("#callingPage").val();
    }
    // load json structure in edit mode
    $.ajax({
        type: "GET",
        async: false,
        cache: false,
        dataType: "json",
        beforeSend: function () {
            blockUI();
        },
        url: jsonUrl,
        success: function (data) {
            if (isStandAlone) {
                editorContent = data;
                accomPkg.onloadEditor(editorContent);
            } else {
                editorContent = data.json;
            }
        },
        error: function () {
            displayUserInfoDialog("error", userInfo.loadHTMLError, "Error");
        },
        complete: function () {
            unblockUI();
        }
    });

    // load html in edit mode
    $.ajax({
        type: "GET",
        async: false,
        cache: false,
        dataType: "html",
        beforeSend: function () {
            blockUI();
        },
        url: htmlUrl + "&timestamp=" + Number(new Date()),
        success: function (data) {
            html = data;
            selectedScreen = null;
            pos = {
                "top": 100,
                "left": 199
            };
            if (html != '') {
                activateControls(html, pos, editorContent, selectedScreen, true);
            }
        },
        error: function () {
            displayUserInfoDialog("error", userInfo.loadHTMLError, "Error");
        },
        complete: function () {
            unblockUI();
        }
    });

    var isTtsEnabled = $("#tts").val();
    if (accomPkg) {
        accomPkg.setCursor(isTtsEnabled);
    }
}

// function for displaying correct answers for both chioce and DND

function displayCorrectAnswer() {
    var screen, choice, scoreObj, ele, dnd, index = 0,
        choiceWithSeq, dndWithSeq, dndPassageRestricted, dndPassageFreeflow, dndMath, scrNames = new Array(),
        getMCQScoreStatus, getDNDScoreStatus, getDNDSScoreStatus, getMCQSScoreStatus, getDNDPRScoreStatus, getDNDPFScoreStatus, getInteractionStatus = false;
    resetPreview();
    for (var screenId in editorContentJSON) {
        screen = $("#" + screenId);
        choice = screen.find("[interactiontype='2']");
        dnd = screen.find("[interactiontype='3']");
        choiceWithSeq = screen.find("[interactiontype='4']");
        dndWithSeq = screen.find("[interactiontype='5']");
        dndPassageRestricted = screen.find("[interactiontype='6']");
        dndPassageFreeflow = screen.find("[interactiontype='7']");
        dndMath = screen.find("[interactiontype='8']");
        mcqDnd = screen.find("[interactiontype='9']");
        noScoringDefined = screen.find("[interactiontype='1']");

        var errMsg = "";
        var scoringMsg = isPreviewScoringDefined();
        if (scoringMsg != "") {
            errMsg += "<span>" + userInfo.scoringNotDefined + scoringMsg + "</span>";
            displayUserInfoDialog("error", errMsg, "Error");
            $("#userInfoDialog").height(60);
        }
        
        if (choice.length > 0) {
            for (index = 0; index < choice.length; index++) {
                ele = choice.eq(index);
                scoreObj = editorContentJSON[screenId][ele.attr("id")].property.scoring;
                showCorrectAnsChoice(ele, scoreObj.correctAns, screenId);
            }
        }
        if (dnd.length > 0) {
            for (index = 0; index < dnd.length; index++) {
                ele = dnd.eq(index);
                scoreObj = editorContentJSON[screenId][ele.attr("id")].property.scoring;
                showCorrectDnDAns(ele, scoreObj.correctAns);
            }
        }
        if (choiceWithSeq.length > 0) {
            for (index = 0; index < choiceWithSeq.length; index++) {
                ele = choiceWithSeq.eq(index);
                scoreObj = editorContentJSON[screenId][ele.attr("id")].property.scoring;
                showCorrectSeqAnsChoice(ele, scoreObj);
            }
        }

        if (dndWithSeq.length > 0) {
            for (index = 0; index < dndWithSeq.length; index++) {
                ele = dndWithSeq.eq(index);
                scoreObj = editorContentJSON[screenId][ele.attr("id")].property.scoring;
                showCorrectDnDSeqAns(ele, scoreObj);
            }
        }
        if (dndPassageRestricted.length > 0) {
            for (index = 0; index < dndPassageRestricted.length; index++) {
                ele = dndPassageRestricted.eq(index);
                scoreObj = editorContentJSON[screenId][ele.attr("id")].property.scoring;
                showCorrectDnDPassageRestrictedAns(ele, scoreObj.correctAns);
            }
        }
        if (dndPassageFreeflow.length > 0) {
            for (index = 0; index < dndPassageFreeflow.length; index++) {
                ele = dndPassageFreeflow.eq(index);
                scoreObj = editorContentJSON[screenId][ele.attr("id")].property.scoring;
                showCorrectDnDPassageFreeflowAns(ele, scoreObj.correctAns);
            }
        }
        if (dndMath.length > 0) {
            for (index = 0; index < dndMath.length; index++) {
                ele = dndMath.eq(index);
                scoreObj = editorContentJSON[screenId][ele.attr("id")].property.scoring;
                if (scoreObj.complete && dndMath.find(".droparea").attr("matharea") == "2" && dndMath.find(".droparea").length == "1") {
                    showCorrectDnDMathAns(ele, scoreObj, screenId);
                } else {
                    displayUserInfoDialog("error", "Interaction is not created properly", "Error");
                }
            }
        }
        if (mcqDnd.length > 0) {
            for (index = 0; index < mcqDnd.length; index++) {
                ele = mcqDnd.eq(index);
                scoreObj = editorContentJSON[screenId][ele.attr("id")].property.scoring;
                showCorrectDnDSeqAns(ele, scoreObj);
            }
        }

    }
}

function isPreviewScoringDefined() {
    var flag = false,
        screenArr = new Array(),
        elementGrpStr = "",
        elementGrpId, scrId, eleGrp, eleIndex = 0,
        elementGroups, scr, scrIndex = 0,
        screens = $("#previewArea > .editor");
    for (scrIndex = 0; scrIndex < screens.length; scrIndex++) {
        scr = screens.eq(scrIndex);
        scrId = scr.attr("id");
        elementGroups = scr.find(".divbox");
        for (eleIndex = 0; eleIndex < elementGroups.length; eleIndex++) {
            eleGrp = elementGroups.eq(eleIndex);
            elementGrpId = eleGrp.attr("id");
            if (!editorContentJSON[scrId][elementGrpId].property.scoring.complete) {
                if (eleGrp.find(".droparea").length > 0 || eleGrp.find(".radio").length > 0 || eleGrp.find(".checkbox").length > 0 || eleGrp.find(".element").length == 0) {
                    elementGrpStr += "," + elementGrpId;
                    flag = true;
                }
            }
        }
        if (elementGroups.length == 0) {
            flag = true;
        }
        if (flag) {
            screenArr.push({
                "screenId": scrId,
                "elements": elementGrpStr.substring(1, elementGrpStr.length)
            });
            elementGrpStr = "";
            flag = false;
        }

    }
    return getScoringErrMsg(screenArr, "P");

}
// showing correct answers for DND elements
function showCorrectDnDAns(control, correctAns) {
    var ele, qs, ans, len;
    for (var index = 0; index < correctAns.length; index++) {
        qs = control.find("#" + correctAns[index].correctQs);
        len = correctAns[index].correctAns.length;
        ans = correctAns[index].correctAns;
        for (var count = 0; count < len; count++) {
            ele = control.children("#" + ans[count]);
            prepareDNDElements(ele, qs);
        }
    }
}
// showing correct answers for DnDPassageRestricted elements

function showCorrectDnDPassageRestrictedAns(control, correctAns) {
    var ele, qs, ans, len, escapedHtml, escapedString;
    for (var index = 0; index < correctAns.length; index++) {
        qs = control.find("#" + correctAns[index].correctQs);
        len = control.find(".element").find(".text").find(".drag").length;
        ans = correctAns[index].correctAns;
        for (var count = 0; count < len; count++) {
            text = control.find(".element").find(".text").find(".drag").eq(count).text();
            for (var count1 = 0; count1 < correctAns[index].correctAns.length; count1++) {
                /* convert html entities into string*/
                escapedHtml = $("<p></p>").html(ans[count1]);
                escapedString = $(escapedHtml).text();
                if (escapedString == text) {
                    ele = control.find(".element").find(".text").find(".drag").eq(count);
                    makePassageRes(ele, qs, "viewAns");
                }
            }
        }
    }
}

// showing correct answers for DnDPassageRestricted elements

function showCorrectDnDPassageFreeflowAns(control, correctAns) {
    var ele, qs, ans, len;
    for (var index = 0; index < correctAns.length; index++) {
        qs = control.find("#" + correctAns[index].correctQs);
        ans = correctAns[index].correctAns;
        for (var count1 = 0; count1 < correctAns[index].correctAns.length; count1++) {
            //qs.prepend("<div class='dropped'style='width:100%;z-index:100;background: none repeat scroll 0 0 white;'><span class='item-remove' title='Click to delete'></span><span class='answer' style='width:40px;'>"+ ans[count1] + "</span><div style='width:100%;height:100%;' class='handle'></div></div>");
            viewPassageFreeflowAns(qs, ans[count1]);
        }
    }
}

// showing correct answers for SR items

function showCorrectAnsChoice(control, correctAns, screenId) {
    var ele, chk, rad;
    for (var index = 0; index < correctAns.length; index++) {
        ele = $("#" + screenId + " #" + control.attr("id")).find("#" + correctAns[index].correctAns);
        chk = ele.find(":checkbox");
        rad = ele.find(":radio");
        if (chk.length > 0) {
            chk.attr("checked", true);
        }
        if (rad.length > 0) {
            resetAllRadioBtn(rad.parent().parent());
            rad.attr("checked", true);
            var $elem = rad.next(".radio-button");
            $elem.addClass("checkedRadio");

        }
    }
}

function resetAllRadioBtn(obj) {
    var input, inputs = $(obj).find(":radio");
    for (var i = 0; i < inputs.length; i++) {
        input = inputs.eq(i);
        input.attr('checked', false);
        input.next(".radio-button").removeClass("checkedRadio");
    }
}

// showing correct answers for SR with Mcq items

function showCorrectSeqAnsChoice(control, correctAns) {
    var ele, chk, rad, selectedElement;
    for (var index = 0; index < correctAns.correctAns.length; index++) {
        ele = control.find("#" + correctAns.correctAns[index].correctAns);
        chk = ele.find(":checkbox");
        if (chk.length > 0) {
            chk.attr("checked", true);
            selectedElement = chk;
            selectedElement.parent().find(".select").css("opacity", 1);
        }
    }
    selectedElement = control.find(":checkbox:checked");
    for (var index1 = 0; index1 < correctAns.mcqSeqCorrectAns.length; index1++) {
        selectedElement.eq(index1).parent().find(".select").find("span.selected_text").html(correctAns.mcqSeqCorrectAns[index1].sequence);
    }
}

// showing correct answers for SR with Mcq items

function showCorrectDnDSeqAns(control, correctAns) {
    var ele, qs, ans, dndSeqScoringMethod = correctAns.dndSeqScoringMethod,
        weight = new Array(),
        weightArr = new Array(),
        prWt, crWt, greatestWeight, posWt;
    if (dndSeqScoringMethod == 1) {
        for (var index = 0; index < correctAns.dndSeqCorrectAns.length; index++) {
            qs = control.find("[behavior = 3]");
            ans = correctAns.dndSeqCorrectAns[index].ctId;
            for (var count = 0; count < ans.length; count++) {
                ele = control.children("#" + ans[count]);
                prepareDNDElements(ele, qs);
            }
        }
    } else {
        for (var index = 0; index < correctAns.dndSeqCorrectAns.length; index++) {
            weight.push(parseInt(correctAns.dndSeqCorrectAns[index].weight, 10));
        }
        $.extend(true, weightArr, weight);
        weight = weight.sort(function (a, b) {
            return b - a
        });
        posWt = weightArr.indexOf(weight[0]);
        qs = control.find("[behavior = 3]");
        ans = correctAns.dndSeqCorrectAns[posWt].ctId;
        for (var count = 0; count < ans.length; count++) {
            ele = control.find("#" + ans[count]);
            prepareDNDElements(ele, qs);
        }
    }

}

//correct dnd math answers rae displayed

function showCorrectDnDMathAns(control, correctAns, screenId) {
    var ele, qs, ans, weight = new Array(),
        weightArr = new Array(),
        posWt;
    for (var index = 0; index < correctAns.correctAns.length; index++) {
        weight.push(parseInt(correctAns.correctAns[index].weight, 10));
    }
    $.extend(true, weightArr, weight);
    weight = weight.sort(function (a, b) {
        return b - a
    });
    posWt = weightArr.indexOf(weight[0]);
    qs = control.find("[behavior = 3]");
    ans = correctAns.correctAns[posWt].id;
    for (var count = 0; count < ans.length; count++) {
        ele = $("#" + screenId + " .mathpalette").find("#" + ans[count]);
        prepareDNDElements(ele, qs);
    }
}

// displaying total score dialog window

function displayScore() {
    $("#displayScoreDialog").dialog("open");
}

//attribute page is displayed from preview dialog

function displayAttribute() {
    var contentPk = $("#contentPk").val();
    $.ajax({
        data: {
            'contentPk': contentPk
        },
        dataType: "html",
        url: "getAttributePreview.do?timestamp=" + Number(new Date()),
        type: "POST",
        cache: false,
        error: function (e) {
            showAjaxError(e);
        },
        success: function (data) {
            $("#previewAttributeDialog").html(data);
            $("#previewAttributeDialog").dialog("open");
        }
    });
}

function displayStimulusAttribute() {
    var contentPk = $("#contentPk").val();
    $.ajax({
        data: {
            'contentPk': contentPk
        },
        dataType: "html",
        url: "getAttributePreview.do?timestamp=" + Number(new Date()),
        type: "POST",
        cache: false,
        error: function (e) {
            showAjaxError(e);
        },
        success: function (data) {
            $("#previewStimulusAttributeDialog").html(data);
            $("#previewStimulusAttributeDialog").dialog("open");
        }
    });
}

function initLayout() {
    $(":button").button();

    if (!isStandAlone) {
        var colorPallete = getColorPallete();
        $("#quesBackground").spectrum({
            color: "#ffffff",
            showInput: true,
            showPalette: true,
            palette: colorPallete
        });

        $("#ansBackground").spectrum({
            color: "#ffffff",
            showInput: true,
            showPalette: true,
            palette: colorPallete
        });
        $("#quesFontColor").spectrum({
            color: "#000000",
            showInput: true,
            showPalette: true,
            palette: colorPallete
        });

        $("#ansFontColor").spectrum({
            color: "#000000",
            showInput: true,
            showPalette: true,
            palette: colorPallete
        });
        $("#textBackground").spectrum({
            color: "#000000",
            showInput: true,
            showPalette: true,
            palette: colorPallete
        });
        $("#quesFontSize, #ansFontSize").val(1);
    }
}

function initEvents() {

    $("#changeQues").unbind("click").click(function () {
        changeQues();
    });
    $("#textToSpeech").unbind("click").click(function () {
        textToSpeechConvert();
    });
    $("#attributes").unbind("click").click(function () {
        displayAttribute();
    });
    $("#reset").unbind("click").click(function () {
        resetPreview();
    });
    $("#viewAnswer").unbind("click").click(function () {
        displayCorrectAnswer();
    });
    $("#getTotalScore").unbind("click").click(function () {
        displayScore();
    });
    $("#close").unbind("click").click(function () {
        window.close();
    });
    $("#stimulusAttributes").unbind("click").click(function () {
        displayStimulusAttribute();
    });

    $("body").unbind("mouseleave").mouseleave(function (evt) {

        $("body").trigger("mouseup");

    });
    $("body").unbind("mouseup").mouseup(function () {
        if ($("body").css("cursor").indexOf("url") < 0) {
            $("body").css("cursor", "auto");
        }
    });
    $(document).click(function (e) {
        if (e.ctrlKey) {
            e.preventDefault();
        }
    });
    $(document).bind("contextmenu", function (e) {
        e.preventDefault();
        return false;
    });
}

function releaseDrag() {
    $("body").trigger("mouseup");
}

function initDialog() {
    // initializing display score dialog
    $("#displayScoreDialog").dialog({
        modal: true,
        resizable: false,
        autoOpen: false,
        position: "top",
        width: 500,
        height: 400,
        title: "Estimated Score",
        open: function () {
            getScore();
        },
        buttons: [{
            text: "OK",
            click: function () {
                $(this).dialog("close");
            }
        }]
    });

    // initializing change background dialog
    $("#questionDialog").dialog({
        modal: true,
        resizable: false,
        autoOpen: false,
        position: "top",
        width: 250,
        height: 440,
        draggable: false,
        close: function (evt, ui) {
            $(".sp-container").hide();
        },
        title: "Font and Color Properties",
        buttons: [{
            text: "OK",
            click: function () {
                //changeBackgroundColor(this);
                changeQuesProp(this);
                $(this).dialog("close");
            }
        }]
    });

    // init user info dialog window
    $("#userInfoDialog").dialog({
        modal: true,
        resizable: false,
        autoOpen: false,
        position: "top",
        width: 410,
        minHeight: 25,
        buttons: [{
            text: "OK",
            click: function () {
                $(this).dialog("close");
            }
        }]
    });

    // init attribute dialog window
    $("#previewAttributeDialog").dialog({
        modal: true,
        resizable: false,
        autoOpen: false,
        position: "top",
        height: 550,
        width: 750,
        title: "Attributes",
        open: function () {
            var sIdentifier = parent.isStimulusPresent();
            if (sIdentifier == "S") {
                $("#previewAttributeDialog").dialog("option", "height", "350");
                $("#previewAttributeDialog").dialog("option", "weight", "750");
            }
        },
        buttons: [{
            text: "OK",
            click: function () {
                $(this).dialog("close");
            }
        }]
    });


    $("#previewStimulusAttributeDialog").dialog({
        modal: true,
        resizable: false,
        autoOpen: false,
        position: "top",
        height: 470,
        width: 750,
        title: "Attributes",
        buttons: [{
            text: "OK",
            click: function () {
                $(this).dialog("close");
            }
        }]
    });
}

// for removing dnd elements

function removeItem(obj) {
    // duplicate items can be removed
    var dropSeq, $this = $(obj).parent(),
        id = $this.attr("id"),
        $container = $this.parent(),
        screen = $container.parent(),
        top = 0,
        left = 0;
    var dropHeight = $container.height(),
        dropWidth = $container.width(),
        dropChildHeight = 0,
        dropChildWidth, isChildLarge = false;
    var ht = parseInt($this.height(), 10) + 6,
        wd = parseInt($this.width(), 10) + 6;
    var dropAlignment = $container.attr("dropalignment");
    var selector = $this.attr("class").split(" ")[0];
    if (droppedSequence) {
        for (var index = 0; index < droppedSequence.length; index++) {
            dropSeq = droppedSequence.eq(index);
            //if(dropAlignment == "1") {
            if (((dropAlignment == undefined || dropAlignment == "") && (selector != "palette-button")) || (dropAlignment == "1")) {
                if (selector == "palette-button") {
                    if ((parseInt(dropSeq.css("top").split("px")[0], 10) + 5) > (parseInt($this.css("top").split("px")[0], 10) + 5)) {
                        top = (parseInt(dropSeq.css("top").split("px")[0], 10) + 5) - ht;
                    } else {
                        top = (parseInt(dropSeq.css("top").split("px")[0], 10));
                    }
                } else {
                    if ((parseInt(dropSeq.css("top").split("px")[0], 10)) > (parseInt($this.css("top").split("px")[0], 10))) {
                        top = parseInt(dropSeq.css("top").split("px")[0], 10) - ht;
                    } else {
                        top = (parseInt(dropSeq.css("top").split("px")[0], 10));
                    }
                }

                left = 0;
            } //else if (dropAlignment == "2") {
            else if (dropAlignment == "2" || selector == "palette-button") {
                top = 0;
                if (selector != "palette-button") {
                    if (parseInt(dropSeq.css("left").split("px")[0], 10) > (parseInt($this.css("left").split("px")[0], 10) + 5)) {
                        left = parseInt(dropSeq.css("left").split("px")[0], 10) - wd;
                    } else {
                        left = parseInt(dropSeq.css("left").split("px")[0], 10);
                    }
                } else {
                    if (parseInt(dropSeq.css("left").split("px")[0], 10) > (parseInt($this.css("left").split("px")[0], 10) + 5)) {
                        left = parseInt(dropSeq.css("left").split("px")[0], 10) - wd - 5;
                    } else {
                        left = parseInt(dropSeq.css("left").split("px")[0], 10);
                    }
                }
            }
            if (!dropSeq.is(".text")) {
                dropSeq.css({
                    "top": top,
                    "left": left
                });
            }
        }
    }
    $this.remove();
    $container.droppable("enable").css("opacity", 1);
    screen.find("#" + id).show();
    setDropAreaScrollbar($container);
    makeDroppableEnable($container);
	//addCustomScrollbar($container);
    sendNotification();
}

// pagination implementation for multiple screens

function paginateScreen() {
    // creating pagination of individual screen display
    var index = String($(this).attr("id")).split("_")[1];
    $("#previewArea").children().hide();
    $("#previewArea > :eq(" + index + ")").show();
}

// activating previewing controls

function activateControls($previewHtml, pos, json, selectedScreen, isFromOutside) {
    var html = "",
        $scr, index = 0,
        $ele, top = 0,
        left = 0,
        screens;
    $("#previewArea").css("background-color", "rgb(255, 255, 255)");
    if (isStandAlone) {
        $previewHtml = resetFontName($previewHtml);
    }
    if (isFromOutside == undefined) {
        $previewHtml = $($previewHtml);
    } else {
        $previewHtml = $($previewHtml).find("#editorArea").children();
    }
    screens = $previewHtml.length;
    $("#displayScoreDialog").dialog("close");
    $("#propertyDialog").dialog("close");
    editorContentJSON = json;

    if (screens > 1) {
        // create pagination if multiple screen present
        for (index = 0; index < screens; index++) {
            html += "<button id='pgBtn_" + index + "'>" + (index + 1) + "</button>";
        }
        $("#pagination").html(html);
        $("#pagination :button").button();
        $("#paginationArea").show();
    } else {
        $("#paginationArea").hide();
    }

    // removing unneccessary css classess
    $previewHtml.find(".handle").remove();
    $previewHtml.find(".ui-resizable-handle").remove();

    for (count = 0; count < screens; count++) {
        $scr = $previewHtml.eq(count).children();
        // re-positioning divbox and sroparea elements only
        for (index = 0; index < $scr.length; index++) {
            $ele = $scr.eq(index);
            top = parseInt($ele.css("top").split("px")[0], 10) - pos.top;
            left = parseInt($ele.css("left").split("px")[0], 10) - pos.left;
            $ele.css({
                top: top + "px",
                left: left + "px"
            });
        }
    }
    previewerHtml = $previewHtml;

    // preparing previewer html	
    $("#previewArea").html($previewHtml);
    if (selectedScreen == null) {
        if (isFromOutside == true) {
            selectedScreen = $("#previewArea .editor").eq(0).attr("id");
        } else {
            selectedScreen = $("#previewArea .editor.selected").attr("id");
        }

    }
    // pagination displaying conditionally
    var selectedEditor = getControlIndex(selectedScreen);
    $("#previewArea > :first").show();
    if (screens > 1) {
        $("#previewArea .editor").hide();
        $("#previewArea #editor_" + (selectedEditor)).show();
    }
    var passageFreeflow = $("#previewArea .editor").find("[interactiontype = 7]");
    if (passageFreeflow.length > 0) {
        makePassageDroppable();
    }
    makeDrggable();
    makeDroppable();
    addSequencing();
    //makeMathPalette();
    setZIndexValue();
    replaceCustomRadioButton();
    makeCustomButtons();
    blockingUserSelection();

    $("[borderrequired='1']").addClass("no-border");

    var matharea = $("[behavior='3'][matharea='2']");
    var controls = $("#previewArea .element:not([data-role='mathpalette'])");
    for (var index = 0; index < controls.length; index++) {
        //setControlHeightWidth(controls.eq(index));
        adjustControlHeightWidth(controls.eq(index));
        setPreviewControlHeightWidth(controls.eq(index));

    }
    $(".ui-draggable-dragging").remove();
    var imageList = $("img");
    for (index = 0; index < imageList.length; index++) {
        imageList.eq(index).attr("onmousedown", " event.preventDefault();");
    }

    if (navigator.userAgent.indexOf("Linux") != -1) {
        setTimeout(removeSmallScrollbars, 1);
        setTimeout(removeZindexForMCQS, 1);
    } else {
        removeSmallScrollbars();
        removeZindexForMCQS();
    }
	checkMCQResp();
}

function checkMCQResp(){
	if(isPad()){
		console.log("inside IsPad");
		$(".radio .text").off("click").on("click", function() {
			checkRadioBtn($(this).prev(".radio-button"));
			sendNotification();
		}); 
		$(".checkbox .text").off("click").on("click", function() {
			checkCheckboxBtn($(this));
			sendNotification();
		});
	}
}
		
function removeZindexForMCQS() {
    var divbox = $("#previewArea").find("div.divbox[interactiontype = 4]");
    for (var index = 0; index < divbox.length; index++) {
        divbox.eq(index).find(".checkbox").end().css("z-index", 0);
    }

}
//remove unwanted scroll bars in ipad and lockdown browser

/* defect 75469,75497,75500 starts*/
function blockingUserSelection() {
    var txtVal, texts = $("#previewArea .text");
    $("#previewArea").addClass("no-user-select");
    for (var index = 0; index < texts.length; index++) {
        txtVal = texts.eq(index);
        if (txtVal.find(".correctAns").length == 0) {
            txtVal.addClass("no-user-select");
            txtVal.attr("onselectstart", "return false;");
        } else {
            txtVal.removeClass("no-user-select");
            txtVal.addClass("user-select");
            txtVal.removeAttr("onselectstart");
        }
    }
	$("body").addClass("no-user-select");
    if (isPad()) {
        $("body").addClass("no-user-select").attr({
            "onmousedown": "event.preventDefault();",
            "draggable": true
        });
    }

}


function unblockingUserSelection() {
    var txtVal, texts = $("#previewArea .text");
    $("#previewArea").removeClass("no-user-select");
    for (var index = 0; index < texts.length; index++) {
        txtVal = texts.eq(index);
        txtVal.removeClass("no-user-select");
        txtVal.removeAttr("onselectstart");
    }
    $("body").removeClass("no-user-select").removeAttr("onmousedown draggable");
}
/* defect 75469 ends*/

function blokFreeFlowTextSelection() {
    var ele, textFreeFlowComponents = $("#previewArea div[interactiontype='7']");
    for (var index = 0; index < textFreeFlowComponents.length; index++) {
        ele = textFreeFlowComponents.eq(index).find(".text");
        ele.removeClass("user-select");
        ele.addClass("no-user-select");
        ele.attr("onselectstart", "return false;");
    }
}

function unblokFreeFlowTextSelection() {
    var ele, textFreeFlowComponents = $("#previewArea div[interactiontype='7']");
    for (var index = 0; index < textFreeFlowComponents.length; index++) {
        ele = textFreeFlowComponents.eq(index).find(".text");
        for (var index1 = 0; index1 < ele.length; index1++) {
            txtVal = ele.eq(index1);
            if (txtVal.find(".correctAns").length == 0) {
                txtVal.removeClass("user-select");
                txtVal.addClass("no-user-select");
                txtVal.removeAttr("onselectstart");
            } else {
                txtVal.removeClass("no-user-select");
                txtVal.addClass("user-select");
                txtVal.removeAttr("onselectstart");
            }
        }
    }
}

// drop areas are scalled according to the scaling of the body

function scaleDropArea() {
    if (isPageLoaded) {
        var dropAreas = $(".droparea"),
            scaleX = Number($("#scaleX").val()),
            scaleY = Number($("#scaleY").val());
        dropAreas.droppable("option", "scaleX", scaleX);
        dropAreas.droppable("option", "scaleY", scaleY);
        isPageLoaded = false;
    }
}

// sorting numeric values in an array

function sortArray(val1, val2) {
    return (val1.sequence - val2.sequence);
}

//making dropdown for MCQ sequence items

function addSequencing() {
    var screens = $("#previewArea > .editor"),
        optionHtml, sequence, seqScoringObj;
    for (var index = 0; index < screens.length; index++) {
        scr = screens.eq(index);
        choiceWithSeq = scr.find("[interactiontype='4']");
        //dndWithSeq = scr.find("[interactiontype='5']");
        optionHtml = "";
        if (choiceWithSeq.length > 0) {
            for (var index1 = 0; index1 < choiceWithSeq.length; index1++) {
                seqScoringObj = editorContentJSON[scr.attr("id")][choiceWithSeq.eq(index1).attr("id")].property.scoring;
                if (seqScoringObj.complete) {
                    sequence = seqScoringObj.sequenceResponse.slice(0);
                    sequence.sort(sortArray);
                    for (var index2 = 0; index2 < sequence.length; index2++) {
                        optionHtml += "<li class='option" + (index2 == 0 ? ' selected' : '') + "'>" + (index2 + 1) + "</li>";
                    }

                    for (var index3 = 0; index3 < choiceWithSeq.eq(index1).find(".element").length; index3++) {
                        choiceWithSeq.eq(index1).find(".element").eq(index3).append(
                            "<div class='select'><span class='selected_text' id='selected_text_" + index3 + "'>1</span><ul id='element_" + index3 + "'>" + optionHtml + "</ul></div>");
                    }
                    $("#previewArea").find(".select").css("opacity", 0.7);
                }
            }
        }
    }
}

//highlighting action is stopped

function stopHighLighting(event) {
    if ($(document).data("active")) {
        $("#current").remove();
        $(document).data("highlighter", true);
        $(document).data("active", false);
        event.stopImmediatePropagation();
        event.stopPropagation();
    }
}

//highlighting action is resumed

function startHighLighting(event) {
    if ($(document).data("highlighter")) {
        $(document).data("active", true);
        $(document).data("highlighter", false);
        event.stopImmediatePropagation();
        event.stopPropagation();
    }
}

// making control as draggable controls

function makeDrggable() {
    var drg, w = 0,
        h = 0,
        $draggables = $("[behavior='2']:not([isdropped=true])"),
        index = 0,
        $ele, options = {}, elemnt, str = "",
        ele;
    addHandleDiv($draggables);
    if (isStandAlone) {
        options = {
            cursor: "move",
            //revert: "invalid",
            handle: ".handle",
            appendTo: "body",
            iframeFix: true,
            scope: "topmost",
            start: function (event, ui) {
                //window.parent.dManupulative = false; //OAS change
                localStorage.setItem("dManupulative", false);//OAS change
                scaleDropArea();
                if (isStandAlone) {
                    scalingContent($(ui.helper), $("#scaleX").val(), $("#scaleY").val());
                    stopHighLighting(event);
                }

            },
            stop: function (event, ui) {
                //window.parent.dManupulative = false; // OAS change
                localStorage.setItem("dManupulative", false);//OAS change
                $(document).trigger("mouseup");
                startHighLighting(event);

            },
			/*79456*/
			revert : function(event,ui){
				var checkdropped=$('body').find(".ui-draggable-dragging");
				var id = $(this).attr('id');
				if(event){
					var dropChildLen = event;
					var previousCount = parseInt($(dropChildLen).attr("currentChildCount"));
					var currentCount = $(dropChildLen).find(".element").length + $(dropChildLen).find(".palette-button").length+ $(dropChildLen).find(".dropped").length;
					if(previousCount == undefined){
						return true;
					}
					else if(currentCount > previousCount){
						//Something has been dropped. Do not return.
						return false;
					}
					else{
						return true;
					}
				}else{
					//means dropped outside dropbox
					return true;
				}				
			},
			/*79456*/
            helper: function () {
                var $this = $(this);
                $this.removeAttr("background");
                var isDragTxt = $this.is(".drag");
                var isOpera = !! window.opera || navigator.userAgent.indexOf(' OPR/') >= 0;
                var isChrome = !! window.chrome && !isOpera;
                return $this.clone().appendTo("body").css({
                    "zIndex": 9999,
                    "max-height": (isDragTxt) ? "250px" : "auto",
                    "max-width": (isDragTxt) ? "250px" : "auto",
                    "overflow": (isDragTxt) ? "auto" : "inherit",
                    "width": (isDragTxt) ? "250px" : "auto"
                    /* "width": ((isDragTxt) ? ((isChrome) ? "250px" : "") : "auto")*/
                }).show();
                $this.attr("background", "yellow");
            }
        };
    } else {
        options = {
            cursor: "move",
            revert: "invalid",
            handle: ".handle",
            appendTo: "body",
            scope: "topmost",
            iframeFix: true,
            start: function (event, ui) {
                scaleDropArea();
                if (isStandAlone) {
                    //scalingContent($(ui.helper), $("#scaleX").val(), $("#scaleY").val());
                    stopHighLighting(event);
                }
                scalingContent($(ui.helper), $("#scaleX").val(), $("#scaleY").val());
            },
            drag: function (event, ui) {
                scalingContent($(ui.helper), $("#scaleX").val(), $("#scaleY").val());
            },
            stop: function (event, ui) {
                startHighLighting(event);

            },
            helper: function () {
                var $this = $(this);
                $this.removeAttr("background");
                var isDragTxt = $this.is(".drag");
                $this.attr("background", "yellow");
                return $this.clone().appendTo("body").css({
                    "zIndex": 9999,
                    "max-height": (isDragTxt) ? "250px" : "auto",
                    "max-width": (isDragTxt) ? "250px" : "auto",
                    "overflow": (isDragTxt) ? "hidden" : "inherit"
                }).show();
            }
        };
    }
    // making drggable controls as originally drggable items
    for (index = 0; index < $draggables.length; index++) {
        drg = $draggables.eq(index);
        options["cursorAt"] = {
            "top": 10,
            "left": 10
        };
        drg.draggable(options);
    }
}

function addHandleDiv($draggables) {
    var index = 0,
        $ele, elemnt, str = "",
        ele;
    for (var index = 0; index < $draggables.length; index++) {
        ele = $draggables.eq(index);
        if (!ele.is(".drag") && !ele.is(".correctAns") && !ele.is(".palette-button")) {
            str = "";
            elemnt = ele.find(".text");
            if (elemnt.length > 0 && elemnt.get(0).scrollHeight > elemnt.innerHeight()) {
				if (isStandAlone) {
					str = "width:100%;";
				}else{
					str = "width:90%;";
				}
            } else {
                str = "width:100%;";
            }
            if (elemnt.length > 0 && elemnt.get(0).scrollWidth > elemnt.innerWidth()) {
                str += "height:90%;";
            } else {
                str += "height:100%;";
            }
        }
        if (ele.find(".handle").length == 0 && !ele.is(".drag")) {
            ele.append("<div class='handle' style='" + str + "'></div>");
        }
    }
}

// the whole items are scalled

function scalingContent(content, scaleX, scaleY) {
    var tr = "scale(" + scaleX + ", " + scaleY + ")";
    content.css({
        "-webkit-transform": tr,
        "transform": tr
    });
}

// get common restricted elements list

function getRestrictionHtml(allEle) {
    var html = "";
    for (var index = 0; index < allEle.length; index++) {
        html += "#" + allEle.eq(index).attr("id");
        if (index < allEle.length - 1) {
            html += ", ";
        }
    }
    return html;
}

// making droppable controls as originally droppable items

function makeDroppable() {
    var $droppables = $("[behavior='3']:not([interactiontype = 7])"),
        $this, clonedEle, passageRes, passageFreeFlow, mathareaDef, acceptVal = false,
        eleId;
    $droppables.droppable({
        greedy: true,
        accept: function (ui) {
            $this = $(this);
            acceptVal = ((ui.is(".palette-button") && $this.attr("matharea") == "2" && $this.parent().attr("interactiontype") == "8") ? true : false);
            if (!acceptVal) {
                acceptVal = (($this.find(ui).length > 0) ? false : true);
                if (acceptVal) {
                    acceptVal = ((ui.parents(".divbox").attr("id") == $this.parent().attr("id")) ? true : false);
                }
            }
            return acceptVal;
        },
        tolerance: 'pointer',
        scope: "topmost",
        drop: function (event, ui) {
            if (event.handleObj.namespace == "draggable" || event.handleObj.namespace == "") {
                passageRes = $("#previewArea .editor:visible").find("[interactiontype = 6]");
                passageFreeFlow = $("#previewArea .editor:visible").find("[interactiontype = 7]");
                $this = $(this);
				/*79456*/
				var dropChildLen = $this.find(".element").length + $this.find(".palette-button").length+ $this.find(".dropped").length;
				$this.attr("currentChildCount",dropChildLen);
				/*79456*/
                if (passageRes.length > 0 && ui.draggable.parents(".divbox").attr("interactiontype") == "6") {
                    makePassageRes(ui.draggable, $this, "dropAns");
                } else if (passageFreeFlow.length > 0 && ui.draggable.parents(".divbox").attr("interactiontype") == "7") {
                    makePassageDroppable("", $this);
                } else {
                   
                    var dropMax = parseInt($this.attr("maxallowed"), 10);
                    var dropAllowMul = $this.attr("allowmultiple");
                    if (dropAllowMul == 1) {
                        dropMax = 1;
                    } else {
                        dropMax = dropMax;
                    }
					 eleId = ui.draggable.attr("id");
					var isSameDNDElement = $this.find("#"+eleId).length > 0 && !$("#"+eleId).is(".palette-button")?true:false;//Already present and not palet button
					if (dropChildLen < dropMax) {
                       
						if($("#"+eleId).is(".palette-button")){
							prepareDNDElements(ui.draggable, $this);
						}
						else{
							if(!isSameDNDElement) { 
								prepareDNDElements(ui.draggable, $this);
							}
						}
                    }
                }
            }
			sendNotification();
        }
    });
}

// making active DND elements

function prepareDNDElements(drag, drop) {
    var $ele = drag.clone(),
        dropElePos, opacity, height = 0,
        index = 0,
        prevAll, width = 0;
    if (drag.attr("duplicacy") == "1") {
        drag.hide();
    }
    $ele.draggable("destroy");

    if (drag.hasClass("correctAns")) {
        var html = $ele.html();
        html = "<span class='text'>" + html + "</span>";
        $ele.html(html);
        $ele.prepend("<span class='item-remove' title='Click to delete'></span>");
    } else if (drag.hasClass("drag")) {
        $ele.prepend("<span class='item-remove' title='Click to delete'></span>");
    } else if (drag.hasClass("palette-button") && drag.hasClass("ui-draggable")) {
        $ele.prepend("<span class='item-remove' title='Click to delete'></span>");
    } else {
        $ele.prepend("<span class='item-remove' title='Click to delete'></span>");
    }

    // appending dragged element to the droppable
    drop.append($ele);
    if ($ele.is(":hidden")) {
        $ele.show();
    }

    $ele.draggable("destroy");
    $ele.attr("isdropped", true);
    opacity = parseInt(drop.attr("transparency"), 10) / 100;
    drop.children(".text").unbind("mousedown").bind("mousedown", function (evt) {
        if ($(evt.target).is(".text")) {
            evt.preventDefault();
            return false;
        }
    });

    var dragdropType;
    if (drop.children(".element").length > 0) {
        dragdropType = 1;
    } else if (drop.children(".correctAns").length > 0) {
        dragdropType = 3;
    } else if (drop.children(".palette-button").length > 0) {
        dragdropType = 4;
    }
    if (dragdropType == 1) {
        dropElePos = getDroppedPosition(drop, ".element", $ele);
    }
    if (dragdropType == 4) {
        dropElePos = getDroppedPosition(drop, ".palette-button", $ele);
    }
    if (dragdropType == 3) {
        dropElePos = getDroppedPosition(drop, ".correctAns", $ele);
    }

    $ele.css({
        "position": "absolute",
        "top": dropElePos.top,
        "left": dropElePos.left,
        "opacity": opacity
    });
    setDropAreaScrollbar(drop);
    makeDropAreaSortable(drop);

    // if droppable area accept single element, restricting that to drop many
    if (drop.attr("allowmultiple") == "1") {
        drop.droppable("disable").css("opacity", 1);
    } else {
        makeDroppableEnable(drop);
    }
}

//draggable items can be removed by dragging out of the droparea

function makeDropAreaSortable(drop) {
    drop.sortable({
        start: function (e, ui) {
            scaleDropArea();
            if (ui.item.is(".dropped")) {
                droppedSequence = ui.item.siblings(".dropped:not(.ui-sortable-placeholder)");
            } else {
                droppedSequence = ui.item.siblings(":not(.ui-sortable-placeholder)"); //.filter(".palette-button").filter(".element");
            }
            if (ui.item.is(".text")) {
                e.preventDefault();
                return false;
            }
            scalingContent($(ui.helper), $("#scaleX").val(), $("#scaleY").val());
            stopHighLighting(e);
        },
        containment: "document",
        cursorAt: {
            "top": 10,
            "left": 10
        },
        appendTo: "body",
        handle: ".handle",
        stop: function (e, ui) {
            startHighLighting(e);
            placeTextDivAtLast(this);
        },
        beforeStop: function (event, ui) {
            if (!ui.item.is(".text") && isDragOutside($(this), event.pageX, event.pageY)) {
                removeItem(ui.item.children());
                droppedSequence = "";
            }
        },
        helper: "clone",
        scroll: false
    });
}

/*function restrictDrag(ele) {
	
}*/

// checking whether no of dropped elements not exceeding max allowd limits

function makeDroppableEnable($ele) {
    if (parseInt($ele.attr("maxallowed"), 10) == $ele.find(".element:not(.ui-sortable-placeholder)").length) {
        $ele.droppable("disable").css("opacity", 1);
    } else {
        $ele.droppable("enable");
    }
}

// resetting chnages in the preview

function resetPreview() {
    $(".element :radio, .element :checkbox").attr("checked", false);
    resetAllRadioBtn($("#previewArea"));
    var drop = $("[behavior='3']"),
        index = 0,
        seq, isChecked;
    for (index = 0; index < drop.length; index++) {
        drop.eq(index).find(".item-remove").trigger("click");
    }
    var sequence = $("[interactiontype='4'] .select");
    for (index = 0; index < sequence.length; index++) {
        sequence.eq(index).css("opacity", 0.7);
        sequence.eq(index).parent().find(".select").find("span.selected_text").html(1);
    }
}

// evaluating total score

function getScore() {
    var getScore, scr, screens = $("#previewArea > .editor"),
        choice, dnd, index = 0,
        scoringMethod, choiceWithSeq, dndWithSeq, dndPassageRestricted, dndPassageFreeflow, dndMath;
    $("#displayScoreDialog").empty();
    for (index = 0; index < screens.length; index++) {
        accomPkgScoreOb = accomPkgPossibleSc = 0;
        scr = screens.eq(index);
        noItemType = scr.find("[interactiontype='1']");
        choice = scr.find("[interactiontype='2']");
        dnd = scr.find("[interactiontype='3']");
        choiceWithSeq = scr.find("[interactiontype='4']");
        dndWithSeq = scr.find("[interactiontype='5']");
        dndPassageRestricted = scr.find("[interactiontype='6']");
        dndPassageFreeflow = scr.find("[interactiontype='7']");
        dndMath = scr.find("[interactiontype='8']");
        mcqDnd = scr.find("[interactiontype='9']");

        var errMsg = "";
        var scoringMsg = isPreviewScoringTSDefined(scr);
        if (scoringMsg != "") {
            $("#displayScoreDialog").append("<fieldset><legend class='bold'>Screen " + (index + 1) + "</legend>" + "<div class='row'><span class='bold'>" + userInfo.scoringNotDefined + scoringMsg + "</span></div></fieldset>");
        } else if (scoringMsg == "" && scr.find(".divbox").length == 0) {
            $("#displayScoreDialog").append("<fieldset><legend class='bold'>Screen " + (index + 1) + "</legend>" + "<div class='row'><span class='bold'>" + userInfo.scoringNotDefined + " this screen.</span></div></fieldset>");
        }

        // choice scoring
        if (choice.length > 0) {
            getScore = mcqGetScore(choice, index, scr);
        }
        // dnd scoring
        if (dnd.length > 0) {
            getScore = dndGetScore(dnd, index, scr);
        }
        // choice with sequencing scoring
        if (choiceWithSeq.length > 0) {
            getScore = mcqSeqGetScore(choiceWithSeq, index, scr);
        }
        // dnd with sequence scoring
        if (dndWithSeq.length > 0) {
            getScore = dndSeqGetScore(dndWithSeq, index, scr);
        }
        // dndPassageRestricted scoring
        if (dndPassageRestricted.length > 0) {
            getScore = dndRestrictedGetScore(dndPassageRestricted, index, scr);
        }
        // dndPassageFreeflow scoring
        if (dndPassageFreeflow.length > 0) {
            getScore = dndFreeflowGetScore(dndPassageFreeflow, index, scr);
        }
        // dndMath scoring
        if (dndMath.length > 0) {
            if (dndMath.find(".droparea").attr("matharea") == "2") {
                if (dndMath.find(".droparea").length == "1") {
                    getScore = dndMathGetScore(dndMath, index, scr);
                } else {
                    $("#displayScoreDialog").append("<fieldset><legend class='bold'>Screen " + (index + 1) + "</legend>" + "<div class='row'><span class='bold'>" + userInfo.twoDroparea + "</span></div></fieldset>");
                }
            } else {
                $("#displayScoreDialog").append("<fieldset><legend class='bold'>Screen " + (index + 1) + "</legend>" + "<div class='row'><span class='bold'>" + userInfo.mathsymbol[3] + "</span></div></fieldset>");
            }
        }
        //mcq with dnd scoring
        if (mcqDnd.length > 0) {
            getScore = mcqDndGetScore(mcqDnd, index, scr);
        }
    }
}

function isPreviewScoringTSDefined(scr) {
    var flag = false,
        screenArr = new Array(),
        elementGrpStr = "",
        elementGrpId, scrId, eleGrp, eleIndex = 0,
        elementGroups;
    scrId = scr.attr("id");
    elementGroups = scr.find(".divbox");
    for (eleIndex = 0; eleIndex < elementGroups.length; eleIndex++) {
        eleGrp = elementGroups.eq(eleIndex);
        elementGrpId = eleGrp.attr("id");
        if (!editorContentJSON[scrId][elementGrpId].property.scoring.complete) {
            if (eleGrp.find(".droparea").length > 0 || eleGrp.find(".radio").length > 0 || eleGrp.find(".checkbox").length > 0 || eleGrp.find(".element").length == 0) {
                elementGrpStr += "," + elementGrpId;
                flag = true;
            }
        }
    }
    if (elementGroups.length == 0) {
        flag = true;
    }
    if (flag) {
        screenArr.push({
            "elements": elementGrpStr.substring(1, elementGrpStr.length)
        });
        elementGrpStr = "";
        flag = false;
    }
    return getScoringTSErrMsg(screenArr, scrId);
}


function getScoringTSErrMsg(screenArr, scrId) {
    var i = 0,
        j = 0,
        elementNames = "",
        name = "",
        scrId, eleId, eleArr;
    var msg = "";
    for (i = 0; i < screenArr.length; i++) {
        eleArr = screenArr[i].elements.split(",");
        elementNames = "";
        if (screenArr[i].elements != "") {
            for (j = 0; j < eleArr.length; j++) {
                eleId = eleArr[j];
                name = editorContentJSON[scrId][eleId].property.name;
                elementNames += ", " + ((name == "") ? userInfo.noName : name);
            }
        } else {
            elementNames = "";
        }
        if (elementNames != "") {
            msg += elementNames.substring(1, elementNames.length);
        }
    }
    return msg;

}

//calculates the score obtained after answering MCQ secuence item
function getMcqSeqScoreObtained(scoreObj, method, correctAns, ansCount, correctAnswers, incorrectAnswers, sequenceArr, seqWeigthedScoreType, inseqCount) {
    var totalScore = parseInt(scoreObj.mcqSeqTotalScore, 10),
        scoreObtained = 0,
        index = 0,
        givenSeqResp, submittedSeqResp, seqCount = 0,
        incorrectSeqCount;
    for (var index = 0; index < correctAnswers.length; index++) {
        submittedSeqResp = correctAnswers[index].crAns;
        for (var index1 = 0; index1 < scoreObj.mcqSeqCorrectAns.length; index1++) {
            givenSeqResp = scoreObj.mcqSeqCorrectAns[index1].correctAns;
            if (givenSeqResp == submittedSeqResp) {
                if (correctAnswers[index].seq == scoreObj.mcqSeqCorrectAns[index1].sequence) {
                    seqCount++;
                    break;
                }
            }
        }
    }

    incorrectSeqCount = String(inseqCount + ((correctAnswers.length) - seqCount));
    correctseqCount = String(seqCount);
    if (method == 0) {
        if (seqCount > 0 && seqCount == correctAns && seqCount == scoreObj.mcqSeqCorrectAns.length && incorrectSeqCount == 0) {
            scoreObtained = totalScore;
        } else {
            scoreObtained = 0;
        }
    } else if (method == 1) {
        if (seqCount > 0 && seqCount == correctAns && seqCount == scoreObj.mcqSeqCorrectAns.length && incorrectSeqCount == 0) {
            scoreObtained = totalScore;
        } else if (seqCount > 0 && seqCount >= (correctAns / 2) && seqCount <= scoreObj.mcqSeqCorrectAns.length) {
            scoreObtained = Math.floor(totalScore / 2);
        } else {
            scoreObtained = 0;
        }
    } else if (method == 2) {
        var correctWeight, incorrectWeight, correctVal, incorrectVal;
        if (seqWeigthedScoreType == "pts") {
            correctVal = (scoreObj.seqCombination[0].seqAnsCount).indexOf(correctseqCount);
            incorrectVal = (scoreObj.seqCombination[0].seqInansCount).indexOf(incorrectSeqCount);
            if (correctVal >= 0) {
                correctWeight = scoreObj.seqCombination[0].seqWeight[correctVal];
                scoreObtained += (parseInt(correctWeight, 10));

            }
            if (incorrectVal >= 0) {
                incorrectWeight = scoreObj.seqCombination[0].seqIncorrectWeight[incorrectVal];
                scoreObtained -= (parseInt(incorrectWeight, 10));
            }
        } else {
            correctVal = (scoreObj.seqCombination[0].seqAnsCount).indexOf(correctseqCount);
            incorrectVal = (scoreObj.seqCombination[0].seqInansCount).indexOf(incorrectSeqCount);
            if (correctVal >= 0) {
                correctWeight = scoreObj.seqCombination[0].seqWeight[correctVal];
                scoreObtained += ((parseInt(correctWeight, 10) * 100) / totalScore);
            }
            if (incorrectVal >= 0) {
                incorrectWeight = scoreObj.seqCombination[0].seqIncorrectWeight[incorrectVal];
                scoreObtained -= ((parseInt(incorrectWeight, 10) * 100) / totalScore);
            }

        }
    }

    return scoreObtained;
}

// calculates score for sequencing of dnd

function getDNDSeqScoreObtained(scoreObj, seqMethod, seqAnsCount, submittedAns, pos, weigthedScoreType, correctAnsLen) {
    if (pos != -1) {
        var totalScore = parseInt(scoreObj.dndSeqCorrectAns[pos].weight, 10),
            sequenceLength = scoreObj.dndSeqTotalCorrectAns,
            scoreObtained = 0;
        if (seqMethod == 0) {
            if (seqAnsCount == 0) {
                scoreObtained = 0;
            } else {
                scoreObtained = ((correctAnsLen - (seqAnsCount - 1)) * totalScore) / sequenceLength;
            }
        } else if (seqMethod == 1) {
            if (weigthedScoreType == "pts") {
                if (seqAnsCount == 1) {
                    scoreObtained = totalScore;
                } else {
                    scoreObtained = 0;
                }
            } else {
                if (seqAnsCount == 1) {
                    scoreObtained = 100;
                } else {
                    scoreObtained = 0;
                }
            }
        }
    } else {
        scoreObtained = 0;
    }
    return parseInt(scoreObtained, 10);
}

//calculates the score obtained after answering mcq item

function getScoreObtained(scoreObj, method, correctAns, ansCount,
    incorrAnsCount, correctAnswers, incorrectAnswers, weigthedScoreType,
    submittedAnsCount, totalOptions) {
    var totalScore = parseInt(scoreObj.totalScore, 10),
        scoreObtained = 0,
        totalAnsCount = (ansCount + incorrAnsCount);
    if (method == 0) {
        if (ansCount > 0 && scoreObj.correctAns.length == ansCount && submittedAnsCount == ansCount) {
            scoreObtained = totalScore;
        } else {
            scoreObtained = 0;
        }
    } else if (method == 1) {
        if (ansCount > 0 && scoreObj.correctAns.length == ansCount && submittedAnsCount == ansCount) {
            scoreObtained = totalScore;
        } else if (submittedAnsCount == totalOptions.length) {
            scoreObtained = 0;
        } else if (ansCount > 0 && ansCount >= (scoreObj.correctAns.length / 2) && ansCount == totalAnsCount) {
            scoreObtained = Math.floor(totalScore / 2);
        } else {
            scoreObtained = 0;
        }
    } else if (method == 2) {
        var correctWeight, incorrectWeight, correctVal, incorrectVal;
        var correctAnsLength = String(ansCount);
        var incorrectAnsLength = String(incorrAnsCount);
        if (weigthedScoreType == "pts") {
            correctVal = (scoreObj.combination[0].ansCount).indexOf(correctAnsLength);
            incorrectVal = (scoreObj.combination[0].inansCount).indexOf(incorrectAnsLength);
            if (correctVal >= 0) {
                correctWeight = scoreObj.combination[0].weight[correctVal];
                scoreObtained += parseInt(correctWeight, 10);
            }
            if (incorrectVal >= 0) {
                incorrectWeight = scoreObj.combination[0].incorrectWeight[incorrectVal];
                scoreObtained -= parseInt(incorrectWeight, 10);
            }
        } else {
            correctVal = (scoreObj.combination[0].ansCount).indexOf(correctAnsLength);
            incorrectVal = (scoreObj.combination[0].inansCount).indexOf(incorrectAnsLength);
            if (correctVal >= 0) {
                correctWeight = scoreObj.combination[0].weight[correctVal];
                scoreObtained += ((parseInt(correctWeight, 10) * 100) / totalScore);
            }
            if (incorrectVal >= 0) {
                incorrectWeight = scoreObj.combination[0].incorrectWeight[incorrectVal];
                scoreObtained -= ((parseInt(incorrectWeight, 10) * 100) / totalScore);
            }
        }
    }

    return scoreObtained;
}

//calculates the score obtained after answering dnd and freeflow item

function getDndScoreObtained(scoreObj, method, correctAns, dndAnsCountArr, correctAnswers, incorrectAnswers, drop, weigthedScoreType) {
    var totalScore = parseInt(scoreObj.totalScore, 10),
        scoreObtained = 0,
        isSomeWrong = false,
        submittedCorrectAnswerCount = 0,
        declaredCorrectAnswerCount = 0;
    var correctAnsLen, incorrectAnsLen, totalSubmittedAnswer = 0,
        correctAnsLength = 0,
        incorrectAnsLength = 0,
        submittedIncortrectAnswerCount = 0;
    for (var i = 0; i < dndAnsCountArr.length; i++) {
        correctAnsLength += dndAnsCountArr[i].correct;
    }
    for (var i = 0; i < dndAnsCountArr.length; i++) {
        incorrectAnsLength += dndAnsCountArr[i].incorrect;
    }
    correctAnsLen = String(correctAnsLength);
    incorrectAnsLen = String(incorrectAnsLength);
    if (method == 0) {
        for (var count = 0; count < drop.length; count++) {
            totalSubmittedAnswer = dndAnsCountArr[count].correct + dndAnsCountArr[count].incorrect;
            if (scoreObj.correctAns[count].correctAns.length == dndAnsCountArr[count].correct && totalSubmittedAnswer == scoreObj.correctAns[count].correctAns.length && scoreObj.correctAns[count].correctAns.length > 0) {
                isSomeWrong = true;
            } else {
                isSomeWrong = false;
                break;
            }
        }
        if (!isSomeWrong) {
            scoreObtained = 0;
        } else {
            scoreObtained = totalScore;
        }
    } else if (method == 1) {
        for (var count = 0; count < dndAnsCountArr.length; count++) {
            submittedCorrectAnswerCount += parseInt((dndAnsCountArr[count].correct), 10);
            declaredCorrectAnswerCount += parseInt((scoreObj.correctAns[count].correctAns.length), 10);
            submittedIncortrectAnswerCount += parseInt((dndAnsCountArr[count].incorrect), 10);
        }
        if (submittedCorrectAnswerCount > 0 && (declaredCorrectAnswerCount == submittedCorrectAnswerCount) && submittedIncortrectAnswerCount == 0) {
            scoreObtained = totalScore;
        } else if (submittedCorrectAnswerCount > 0 && (submittedCorrectAnswerCount >= (declaredCorrectAnswerCount / 2)) && (submittedIncortrectAnswerCount + submittedCorrectAnswerCount) <= declaredCorrectAnswerCount) {
            scoreObtained = Math.floor(totalScore / 2);
        } else {
            scoreObtained = 0;
        }
    } else if (method == 2) {
        var correctWeight, incorrectWeight, correctVal, incorrectVal;
        if (weigthedScoreType == "pts") {
            correctVal = (scoreObj.dndAnsCombination[0].ansCount).indexOf(correctAnsLen);
            incorrectVal = (scoreObj.dndAnsCombination[0].inansCount).indexOf(incorrectAnsLen);
            if (correctVal >= 0) {
                correctWeight = scoreObj.dndAnsCombination[0].weight[correctVal];
                scoreObtained += parseInt(correctWeight, 10);
            }
            if (incorrectVal >= 0) {
                incorrectWeight = scoreObj.dndAnsCombination[0].incorrectWeight[incorrectVal];
                scoreObtained -= parseInt(incorrectWeight, 10);
            }

        } else {
            correctVal = (scoreObj.dndAnsCombination[0].ansCount).indexOf(correctAnsLen);
            incorrectVal = (scoreObj.dndAnsCombination[0].inansCount).indexOf(incorrectAnsLen);
            if (correctVal >= 0) {
                correctWeight = scoreObj.dndAnsCombination[0].weight[correctVal];
                scoreObtained += ((parseInt(correctWeight, 10) * 100) / totalScore);
            }
            if (incorrectVal >= 0) {
                incorrectWeight = scoreObj.dndAnsCombination[0].incorrectWeight[incorrectVal];
                scoreObtained -= ((parseInt(incorrectWeight, 10) * 100) / totalScore);
            }
        }
    }
    return scoreObtained;
}

//calculates the score obtained after answering passage restricted item

function getDndPassageScoreObtained(scoreObj, method, correctAns, dndAnsCountArr, drop, weigthedScoreType) {
    var totalScore = parseInt(scoreObj.totalScore, 10),
        scoreObtained = 0,
        isSomeWrong = false,
        submittedCorrectAnswerCount = 0,
        declaredCorrectAnswerCount = 0,
        submittedIncortrectAnswerCount = 0;
    var correctAnsLen = 0,
        incorrectAnsLen = 0; // correctTableLength = $("#correctScoring tbody tr"), incorrectTableLength = $("#incorrectScoring tbody tr");
    if (method == 0) {
        for (var count = 0; count < drop.length; count++) {
            if (dndAnsCountArr[count].incorrect == 0 && scoreObj.correctAns[count].correctAns.length == dndAnsCountArr[count].correct && scoreObj.correctAns[count].correctAns.length > 0) {
                isSomeWrong = true;
            } else {
                isSomeWrong = false;
                break;
            }
        }
        if (!isSomeWrong) {
            scoreObtained = 0;
        } else {
            scoreObtained = totalScore;
        }
    } else if (method == 1) {
        for (var count = 0; count < dndAnsCountArr.length; count++) {
            submittedCorrectAnswerCount += parseInt((dndAnsCountArr[count].correct), 10);
            declaredCorrectAnswerCount += parseInt((scoreObj.correctAns[count].correctAns.length), 10);
            submittedIncortrectAnswerCount += parseInt((dndAnsCountArr[count].incorrect), 10);

        }
        if (submittedCorrectAnswerCount > 0 && (declaredCorrectAnswerCount == submittedCorrectAnswerCount) && submittedIncortrectAnswerCount == 0) {
            scoreObtained = totalScore;
        } else if (submittedCorrectAnswerCount > 0 && (submittedCorrectAnswerCount >= (declaredCorrectAnswerCount / 2)) && (submittedIncortrectAnswerCount + submittedCorrectAnswerCount) <= declaredCorrectAnswerCount) {
            scoreObtained = Math.floor(totalScore / 2);
        } else {
            scoreObtained = 0;
        }
    } else if (method == 2) {
        var correctWeight, incorrectWeight, correctVal, incorrectVal;
        for (var count = 0; count < dndAnsCountArr.length; count++) {
            correctAnsLen += parseInt((dndAnsCountArr[count].correct), 10);
            incorrectAnsLen += parseInt((dndAnsCountArr[count].incorrect), 10);
        }
        if (weigthedScoreType == "pts") {
            correctVal = (scoreObj.dndAnsCombination[0].ansCount).indexOf(String(correctAnsLen));
            incorrectVal = (scoreObj.dndAnsCombination[0].inansCount).indexOf(String(incorrectAnsLen));
            if (correctVal >= 0) {
                correctWeight = scoreObj.dndAnsCombination[0].weight[correctVal];
                scoreObtained += parseInt(correctWeight, 10);
            }
            if (incorrectVal >= 0) {
                incorrectWeight = scoreObj.dndAnsCombination[0].incorrectWeight[incorrectVal];
                scoreObtained -= parseInt(incorrectWeight, 10);
            }

        } else {
            correctVal = (scoreObj.dndAnsCombination[0].ansCount).indexOf(String(correctAnsLen));
            incorrectVal = (scoreObj.dndAnsCombination[0].inansCount).indexOf(String(incorrectAnsLen));
            if (correctVal >= 0) {
                correctWeight = scoreObj.dndAnsCombination[0].weight[correctVal];
                scoreObtained += ((parseInt(correctWeight, 10) * 100) / totalScore);
            }
            if (incorrectVal >= 0) {
                incorrectWeight = scoreObj.dndAnsCombination[0].incorrectWeight[incorrectVal];
                scoreObtained -= ((parseInt(incorrectWeight, 10) * 100) / totalScore);
            }
        }
    }
    return scoreObtained;
}

function enableSequencing() {
    var element = $(this).parent();
    element.find("select").attr("disabled", false);
}

function viewPassageFreeflowAns(dropElement, text, event) {
    var $this = $(dropElement);
    var arr = new Array(),
        dropElePos, allowMultiple = $this.attr("allowmultiple"),
        maxAllowed = 0,
        $droppedEle, dropAreaWidth;
    var noOfChildren = $this.find(".dropped");
    if (allowMultiple != "1") {
        if (noOfChildren.length == 0) {
            $droppedEle = $("<div class='dropped' style='display:block;z-index:100;background: none repeat scroll 0 0 white;'><span class='item-remove' title='Click to delete'></span><span class='answer'>" + text + "</span><div style='width:100%;height:100%;' class='handle'></div></div>");
        } else {
            var childtext;
            for (index = 0; index < noOfChildren.length; index++) {
                childtext = noOfChildren[index].textContent;
                arr.push($.trim(childtext));
            }
            var value = arr.indexOf($.trim($(text).text()));
            maxAllowed = parseInt($this.attr("maxallowed"), 10)
            if (value < 0 && noOfChildren.length < maxAllowed) {
                $droppedEle = $("<div class='dropped' style='display:block;z-index:100;background: none repeat scroll 0 0 white;'><span class='item-remove' title='Click to delete'></span><span class='answer'>" + text + "</span><div style='width:100%;height:100%;' class='handle'></div></div>");
            }
        }
        arr.length = 0;
    } else {
        if (noOfChildren.length == 0) {
            $droppedEle = $("<div class='dropped' style='display:block;z-index:100;background: none repeat scroll 0 0 white;'><span class='item-remove' title='Click to delete'></span><span class='answer'>" + text + "</span><div style='width:100%;height:100%;' class='handle'></div></div>");
        } else {
            var childtext = noOfChildren[0].textContent;
            if ((childtext != text) && (noOfChildren.length != 1)) {
                $droppedEle = $("<div class='dropped' style='display:block;z-index:100;background: none repeat scroll 0 0 white;'><span class='item-remove' title='Click to delete'></span><span class='answer'>" + text + "</span><div style='width:100%;height:100%;' class='handle'></div></div>");
            }
        }
    }
    if (event) {
        event.originalEvent.stopPropagation();
        event.originalEvent.preventDefault();
    }

    $this.find(".text").before($droppedEle);
    dropAreaWidth = $this.width() - 25;
    if ($droppedEle != undefined) {
	    var txtWidth = $droppedEle.width();
	    if (txtWidth == 0) txtWidth = getActualWidth($droppedEle);
	    if (txtWidth < dropAreaWidth - 25) dropAreaWidth = txtWidth + 25;
	    $droppedEle.css("width", dropAreaWidth);
	    dropElePos = getDroppedPosition($this, ".dropped", $droppedEle);
	    $droppedEle.css({
	        "top": dropElePos.top,
	        "left": dropElePos.left
	    });
	    $droppedEle.find(".answer").css("color", $this.find(".text").css("color"));
	}
    setDropAreaScrollbar($this);
}

function getActualWidth(obj) {
    var clone = obj.clone();
    clone.css("visibility", "hidden");
    $('body').append(clone);
    var width = clone.width();
    clone.remove();
    return width;
}
// dropping the selected text of passage freeflow inside droparea

function makePassageDroppable(drag, drop) {
    var target = false;
    $(window).mouseup(function (e) {
        if (accomPkg.enableHighlighterArea == false && accomPkg.enableEraserArea == false) {
            var ids = $(".droparea").within(e.pageX, e.pageY);
            if (e.pageX === undefined) return;
            if (ids.length > 0) {
                unblokFreeFlowTextSelection();
                e.preventDefault();
                target = true;
                var text = $(".ffText").html(); //getFreeFlowSelectionHtml();
                /*amit: 6425*/
                var textStr = text.replace(/(\n|\t|<br>|&nbsp;)/gm, "");
                var isEmptyString = ($(textStr).text() == "" ? true : false);
                //var sel = window.getSelection();
                var drag = $(".ffText"); //$(sel.anchorNode.parentNode).parents(".divbox"),
                drop = $(ids).parents(".divbox");
                if (text && (drag.attr("id") == drop.attr("id") && isEmptyString == false)) {
                    viewPassageFreeflowAns(ids, text, e);
                } else {
                    e.stopPropagation();
                    e.stopImmediatePropagation();
                    e.preventDefault();
                }
				//sendNotification();
            } else {
                target = false;
                unblokFreeFlowTextSelection();
                var dropareas = $("#previewArea .droparea");
                makeDropAreaSortable(dropareas);
            }
            $(".ffText").remove();
			//$(".draggabletxt").remove();
            $(document).unbind("mousemove");
			sendNotification();
        }
		
        
    });

}

function getFreeFlowSelectionHtml() {
    var html = "";
    if (typeof window.getSelection != "undefined") {
        var sel = window.getSelection();
        if (sel.rangeCount) {
            var commAnsStyle, ele, con, container = document.createElement("div");
            for (var i = 0, len = sel.rangeCount; i < len; ++i) {
                con = document.createElement("span");
                ele = sel.getRangeAt(i);
                commAnsStyle = ele.startContainer.parentNode.getAttribute("style");
                con.setAttribute("style", commAnsStyle);
                con.appendChild(ele.cloneContents());
                container.appendChild(con);
            }
            html = container.innerHTML;
        }
    } else if (typeof document.selection != "undefined") {
        if (document.selection.type == "Text") {
            html = document.selection.createRange().htmlText;
        }
    }
    /*amit: 6425*/
    if (html.indexOf("div") >= 0 || html.indexOf("button") >= 0 || html.indexOf("input") >= 0) {
        html = undefined;
    }
    /* end amit: 6425*/
    return html;
}

// dropping the selected draggable text of passage restricted inside droparea

function makePassageRes(drag, drop, accessType) {
    var dropElePos, arr = new Array(),
        allowMultiple = $(drop).attr("allowmultiple"),
        maxAllowed = 0;
    var noOfChildren = $(drop).find(".dropped"),
        text = drag.html(),
        textContent = drag.text(),
        dropWidth = $(drop).width() - 25,
        $droppedElement = "";
    var colorToSet = drag.parents('div:eq(0)').css("color");
    if (allowMultiple != "1") {
        if (noOfChildren.length == 0) {
            $droppedElement = $("<div class='dropped' style='display:block;z-index:100;background: none repeat scroll 0 0 white;'><span class='item-remove' title='Click to delete'></span><span class='answer' style='color: " + colorToSet + "'>" + text + "</span><div style='width:100%;height:100%;' class='handle'></div></div>");
        } else {
            var childtext;
            for (index = 0; index < noOfChildren.length; index++) {
                childtext = noOfChildren[index].textContent;
                arr.push(childtext);
            }
            var value = arr.indexOf(textContent);
            if (accessType == "dropAns") {
                maxAllowed = parseInt(($(drop).attr("maxallowed")), 10);
                if (value < 0 && noOfChildren.length < maxAllowed) {
                    $droppedElement = $("<div class='dropped'style='display:inline-block;z-index:100;background: none repeat scroll 0 0 white;'><span class='item-remove' title='Click to delete'></span><span class='answer' style='color: " + colorToSet + "'>" + text + "</span><div style='width:100%;height:100%;' class='handle'></div></div>");
                }
            } else if (accessType == "viewAns") {
                $droppedElement = $("<div class='dropped'style='display:inline-block;z-index:100;background: none repeat scroll 0 0 white;'><span class='item-remove' title='Click to delete'></span><span class='answer' style='color: " + colorToSet + "'>" + text + "</span><div style='width:100%;height:100%;' class='handle'></div></div>");
            }
        }
        arr.length = 0;
    } else {
        if (noOfChildren.length == 0) {
            $droppedElement = $("<div class='dropped'style='display:inline-block;z-index:100;background: none repeat scroll 0 0 white;'><span class='item-remove' title='Click to delete'></span><span class='answer' style='color: " + colorToSet + "'>" + text + "</span><div style='width:100%;height:100%;' class='handle'></div></div>");
        } else {
            var childtext = noOfChildren[0].textContent;
            if (accessType == "dropAns") {
                maxAllowed = parseInt(($(drop).attr("maxallowed")), 10);
                if ((childtext != this.value) && (noOfChildren.length < (maxAllowed - 1))) {
                    $droppedElement = $("<div class='dropped'style='display:inline-block;z-index:100;background: none repeat scroll 0 0 white;'><span class='item-remove' title='Click to delete'></span><span class='answer' style='color: " + colorToSet + "'>" + text + "</span><div style='width:100%;height:100%;' class='handle'></div></div>");

                }
            } else if (accessType == "viewAns") {
                $droppedElement = $("<div class='dropped'style='display:inline-block;z-index:100;background: none repeat scroll 0 0 white;'><span class='item-remove' title='Click to delete'></span><span class='answer' style='color: " + colorToSet + "'>" + text + "</span><div style='width:100%;height:100%;' class='handle'></div></div>");
            }
        }
    }
    drop.find(".text").before($droppedElement);
    if ($droppedElement != "") {
        var txtWidth = $droppedElement.width();
        if (txtWidth == 0) txtWidth = getActualWidth($droppedElement);
        if (txtWidth < dropWidth - 25) dropWidth = txtWidth + 25;
        $droppedElement.css("width", dropWidth);
        dropElePos = getDroppedPosition(drop, ".dropped", $droppedElement);
        $droppedElement.css({
            "top": dropElePos.top,
            "left": dropElePos.left
        });
        $droppedElement.find(".answer").css("color", drop.find(".text").css("color"));
    }

    setDropAreaScrollbar(drop);
    makeDropAreaSortable(drop);
}

//Checking and unchecking of text to speech button from inside tool preview mode

function ttsConvert() {

    var getSound = parent.$(".ui-dialog-buttonset").find(".sound");
    var soundLen = parent.$(".ui-dialog-buttonset").find(".sound").length;
    var getMute = parent.$(".ui-dialog-buttonset").find(".mute");
    var muteLen = parent.$(".ui-dialog-buttonset").find(".mute").length;
    if (soundLen > 0) {
        getSound.removeClass("sound");
        getSound.addClass("mute");
    }
    if (muteLen > 0) {
        getMute.removeClass("mute");
        getMute.addClass("sound");
    }
    stopPlaying();
}

function changeQues() {
    $("#questionDialog").dialog("open");
}

//sets background to question and answers in preview dialog

function changeQuesProp(dialog) {
    var quesBackgroundColor = $("#questionDialog").find("input").eq(0).spectrum("get").toHex();
    var ansBackgroundColor = $("#questionDialog").find("input").eq(1).spectrum("get").toHex();
    var quesFontColor = $("#questionDialog").find("input").eq(2).spectrum("get").toHex();
    var ansFontColor = $("#questionDialog").find("input").eq(3).spectrum("get").toHex();
    var fontSize = $("#questionDialog").find("#quesFontSize").val();

    setColorFontAccomm(quesBackgroundColor, ansBackgroundColor, quesFontColor, ansFontColor);
    if (fontSize == "2") {
        setLargeFont(1.5, 1.5);
        $("#previewArea").css("overflow", "auto");
        $("#scaleX").val(1.5);
        $("#scaleY").val(1.5);
    } else {
        setLargeFont(1, 1);
        $("#previewArea").css("overflow", "hidden");
        $("#scaleX").val(1);
        $("#scaleY").val(1);
    }
}

function setColorFontAccomm(quesBackgroundColor, ansBackgroundColor, quesFontColor, ansFontColor) {
    var qsArea = $("div[ansarea='false']"),
        quesArea = $("div.element:not(.mathpalette,[ansarea])");
    for (var index = 0; index < qsArea.length; index++) {
        if (qsArea.eq(index).attr("bgrequired") == "1") {
            qsArea.eq(index).css("background-color", "#" + quesBackgroundColor);
        }
        //if(!qsArea.eq(index).is("div.divbox")){
        qsArea.eq(index).children(".text").css("color", "#" + quesFontColor);
        //qsArea.eq(index).find(".correctAns").css("color", "#" + quesFontColor);
        qsArea.eq(index).children(".text table, td").css("border-color", "#000000");
        // }
        if (qsArea.eq(index).find(".answer").length > 0) {
            qsArea.eq(index).find(".answer").css("color", "#" + quesFontColor);
        }
    }
    for (var index1 = 0; index1 < quesArea.length; index1++) {
        if (quesArea.eq(index1).attr("bgrequired") == "1" || quesArea.eq(index1).attr("bgrequired") == undefined) {
            quesArea.eq(index1).css("background-color", "#" + quesBackgroundColor);
        }
        // if(!quesArea.eq(index1).is("div.divbox")){
        quesArea.eq(index1).children(".text").css("color", "#" + quesFontColor);
        //quesArea.eq(index1).find(".correctAns").css("color", "#" + quesFontColor);
        quesArea.eq(index1).children(".text table, td").css("border-color", "#000000");
        // }
        if (quesArea.eq(index1).find(".answer").length > 0) {
            quesArea.eq(index1).find(".answer").css("color", "#" + quesFontColor);
        }
    }
    $("#previewArea").css("background-color", "#" + quesBackgroundColor);
    var ansArea = $("div[ansarea='true']");
    for (var index2 = 0; index2 < ansArea.length; index2++) {
        if (ansArea.eq(index2).attr("bgrequired") == "1") {
            ansArea.eq(index2).css("background-color", "#" + ansBackgroundColor);
        }
        // if(!ansArea.eq(index2).is("div.divbox")){
        ansArea.eq(index2).children(".text").css("color", "#" + ansFontColor);
        if (ansArea.eq(index2).find(".answer").length > 0) {
            ansArea.eq(index2).find(".answer").css("color", "#" + ansFontColor);
        }

        //ansArea.eq(index2).find(".correctAns").css("color", "#" + ansFontColor);
        ansArea.eq(index2).children(".text table, td").css("border-color", "#000000");
        //	  }
    }
}

//Checking and unchecking of text to speech button from outside tool preview mode

function textToSpeechConvert() {
    var getSound = $("body").find(".sound");
    var soundLen = $("body").find(".sound").length;
    var getMute = $("body").find(".mute");
    var muteLen = $("body").find(".mute").length;
    if (soundLen > 0) {
        getSound.removeClass("sound");
        getSound.addClass("mute");
    }
    if (muteLen > 0) {
        getMute.removeClass("mute");
        getMute.addClass("sound");
    }
    stopPlaying();
}

//gets the score of MCQ

function mcqGetScore(choice, index, scr) {
    var chk, rad, html = "",
        choice, count = 0,
        ele, scoreObj, method = 0,
        correctAns = 0,
        scoreObtained, ansCount, ans, index1 = 0,
        index2 = 0,
        scoringMethod, accomPkgScoreOb = 0,
        accomPkgPossibleSc = 0;
    var correctAnswers, crAns, incorrectAnswers,
        submittedAns, incorrAnsCount = 0,
        weigthedScoreType, submittedAnsCount, totalOptions;
    var scoring = new Array();
    for (count = 0; count < choice.length; count++) {
        subquestionIndex++;
        correctAnswers = new Array();
        incorrectAnswers = new Array();
        html = "<fieldset><legend class='bold'>Screen " + (index + 1) + "</legend>";
        scoreObtained = ansCount = incorrAnsCount = possibleScore = 0/*, crAnsArr = new Array()*/;
        var items;
        ele = choice.eq(count);
        scoreObj = editorContentJSON[scr.attr("id")][ele.attr("id")].property.scoring;
        if (scoreObj.complete) {
            method = parseInt(scoreObj.scoringMethod, 10) - 1;
            if (method == 2) {
                weigthedScoreType = editorContentJSON[scr.attr("id")][ele.attr("id")].property.scoring.scoreIn;
            } else {
                weigthedScoreType = "pts";
            }
            chk = ele.find(":checkbox:checked");
            rad = ele.find(":radio:checked");
            items = $(ele).children();
            var subAnsArr = new Array();
            var isImg = checkImage(items);
            
            if (chk.length > 0) {
                ans = chk;
                submittedAns = chk.length;
                for (var a = 0; a < submittedAns; a++) {
                    if (isImg == false) {
                        subAnsArr.push({
                            "format": "Text",
                            "id": chk.eq(a).parent().attr("id"),
                            "response": identifySpecialChar(chk.eq(a).parent().find(".text").html())
                        });
                    } else {
                        subAnsArr.push({
                            "format": "Name",
                            "id": chk.eq(a).parent().attr("id"),
                            "response": chk.eq(a).parent().attr("name")
                        });
                    }
                }
            } else if (rad.length > 0) {
                ans = rad;
                submittedAns = rad.length;
                for (var a = 0; a < submittedAns; a++) {
                    if (isImg == false) {
                        subAnsArr.push({
                            "format": "Text",
                            "id": rad.eq(a).parent().attr("id"),
                            "response": identifySpecialChar(rad.eq(a).parent().find(".text").html())
                        });
                    } else {
                        subAnsArr.push({
                            "format": "Name",
                            "id": rad.eq(a).parent().attr("id"),
                            "response": rad.eq(a).parent().attr("name")
                        });
                    }
                }
            } else {
                submittedAns = 0;
            }
            submittedAnsCount = submittedAns;
            totalOptions = ele.find(":checkbox");
            for (index1 = 0; index1 < submittedAns; index1++) {
                for (index2 = 0; index2 < scoreObj.correctAns.length; index2++) {
                    crAns = ans.eq(index1).parent().attr("id");
                    if (scoreObj.correctAns[index2].correctAns == crAns) {
                        ansCount++;
                        correctAnswers.push(crAns);
                    }
                }
            }
            if (ans) {
                for (index1 = 0; index1 < ans.length; index1++) {
                    crAns = ans.eq(index1).parent().attr("id");
                    for (index2 = 0; index2 < scoreObj.incorrectAns.length; index2++) {
                        if (crAns == scoreObj.incorrectAns[index2].incorrectAns) {
                            incorrAnsCount++;
                            incorrectAnswers.push(crAns);
                        }
                    }
                }
            }
        
            scoreObtained = getScoreObtained(scoreObj, method, submittedAns,
                ansCount, incorrAnsCount, correctAnswers, incorrectAnswers,
                weigthedScoreType, submittedAnsCount, totalOptions);
            scoringMethod = userInfo.scoringMethodology[method];
            if (scoreObtained < 0) {
                scoreObtained = 0;
                accomPkgScoreOb += 0;
            } else {
                scoreObtained = scoreObtained;
                accomPkgScoreOb += scoreObtained;
            }
            possibleScore = scoreObj.totalScore;
            accomPkgPossibleSc += parseInt(scoreObj.totalScore, 10);

            html += "<div class='row'><span class='bold'>Interaction:</span><span>" + userInfo.itemType[0] + "</span></div>";
            html += "<div class='row'><span class='bold'>Methodology:</span><span>" + scoringMethod + "</span></div>";
            html += "<div class='row'><span class='bold'>Correct answer(s):</span><span>" + ansCount + "</span></div>";
            html += "<div class='row'><span class='bold'>Total correct answer(s):</span><span>" + scoreObj.totalCorrectAns + "</span></div>";
            if (weigthedScoreType == "pts") {
                html += "<div class='row'><span class='bold'>Score obtained:</span><span>" + scoreObtained + "</span></div>";
            } else {
                if (scoreObtained != 0) {
                    html += "<div class='row'><span class='bold'>Score obtained:</span><span>" + scoreObtained + "%" + "</span></div>";
                } else {
                    html += "<div class='row'><span class='bold'>Score obtained:</span><span>" + scoreObtained + "</span></div>";
                }
            }

            html += "<div class='row'><span class='bold'>Possible Score:</span><span>" + possibleScore + "</span></div>";
            html += "</fieldset>";
        } else {
            //html += "<div>" + userInfo.wrongScoringDefined + "</div>";
            html = "";
        }
        $("#displayScoreDialog").append(html);

        var editorId = ele.parent().attr("id"),
            interactionType = userInfo.itemType[0],
            part = parseInt(ele.parent().attr("id").split("_")[1]) + 1;
        //part = $("#screenId").val();
        scoring.push(getScoreJson(interactionType/*, scoringMethod*/, scoreObtained, subAnsArr, possibleScore, part, /*crAnsArr, scoreObj, possibleAnsArr,*/ subquestionIndex));
    }
    return {
        "scoring": scoring,
        "itemID": $("#itemId").val(),
        "ScoreObtained": accomPkgScoreOb,
        "PossibleScore": accomPkgPossibleSc
    };
}

//gets the score of DND

function dndGetScore(dnd, index, scr) {
    var html = "",
        isCorrectAns, count = 0,
        ele, scoreObj, method = 0,
        correctAns = 0,
        scoreObtained, ansCount, ans, drop, index1 = 0,
        index2 = 0,
        accomPkgScoreOb = 0,
        accomPkgPossibleSc = 0;
    var cns, eachAns, scoringMethod, correctAnswers, incorrectAnswers, submittedAns, dndAnsCountArr, weigthedScoreType, isAnswerWrong;
    var scoring = new Array(),
        subAnsArr, possibleAnsArr, crAnsArr, submittedOptions, correctOptions, possibleOptions;
    for (count = 0; count < dnd.length; count++) {
        subquestionIndex++;
        html = "<fieldset><legend class='bold'>Screen " + (index + 1) + "</legend>";
        scoreObtained = ansCount = possibleScore = 0;
        correctAnswers = new Array();
        incorrectAnswers = new Array();
        dndAnsCountArr = new Array();
        ele = dnd.eq(count);
        scoreObj = editorContentJSON[scr.attr("id")][ele.attr("id")].property.scoring;
        if (scoreObj.complete) {
            method = parseInt(scoreObj.scoringMethod, 10) - 1;
            if (method == 2) {
                weigthedScoreType = editorContentJSON[scr.attr("id")][ele.attr("id")].property.scoring.scoreIn;
            } else {
                weigthedScoreType = "pts";
            }
            drop = ele.find("[behavior='3']");
            drag = ele.children("[behavior='2']");
            ans = drop.children("[behavior='2']");
            subAnsArr = new Array()/*, possibleAnsArr = new Array(), crAnsArr = new Array()*/;
            var isImg = checkImage(drag);
            for (index1 = 0; index1 < drop.length; index1++) {
                submittedOptions = new Array(), correctOptions = new Array(), possibleOptions = new Array();
                dndCorrectAnsCount = dndIncorrectAnsCount = 0;
                submittedAns = drop.eq(index1).children("[behavior='2']");
                for (index2 = 0; index2 < submittedAns.length; index2++) {
                    eachAns = submittedAns.eq(index2);
                    cns = scoreObj.correctAns[index1];
                    if (cns.correctQs == eachAns.parent().attr("id")) {
                        for (var innerCount = 0; innerCount < cns.correctAns.length; innerCount++) {
                            pos = cns.correctAns[innerCount].indexOf(eachAns.attr("id"));
                            if (pos > -1) {
                                break;
                            }
                        }
                        if (pos > -1) {
                            dndCorrectAnsCount++;
                        } else {
                            dndIncorrectAnsCount++;
                        }
                    }
                    if (isImg == false) {
                        submittedOptions.push({
                            "format": "Text",
                            "id": eachAns /*.eq(index2)*/ .attr("id"),
                            "response": identifySpecialChar(eachAns /*.eq(index2)*/ .find(".text").html())
                        });
                    } else {
                        submittedOptions.push({
                            "format": "Name",
                            "id": eachAns /*.eq(index2)*/ .attr("id"),
                            "response": eachAns /*.eq(index2)*/ .attr("name")
                        });
                    }
                }

                dndAnsCountArr.push({
                    "correct": dndCorrectAnsCount,
                    "incorrect": dndIncorrectAnsCount
                });

              
                subAnsArr.push({
                    "droparea": {
                        "name": drop.eq(index1).attr("name"),
                        "id": drop.eq(index1).attr("id")
                    },
                    "dragarea": submittedOptions
                });
            }
            scoreObtained += getDndScoreObtained(scoreObj, method, correctAns,
                dndAnsCountArr, correctAnswers, incorrectAnswers, drop,
                weigthedScoreType);
            scoringMethod = userInfo.scoringMethodology[method];
            if (scoreObtained < 0) {
                scoreObtained = 0;
                accomPkgScoreOb += 0;
            } else {
                scoreObtained = scoreObtained;
                accomPkgScoreOb += scoreObtained;
            }
            possibleScore = scoreObj.totalScore;
            accomPkgPossibleSc += parseInt(scoreObj.totalScore, 10);

            html += "<div class='row'><span class='bold'>Interaction:</span><span>" + userInfo.itemType[1] + "</span></div>";
            html += "<div class='row'><span class='bold'>Methodology:</span><span>" + scoringMethod + "</span></div>";

            if (weigthedScoreType == "pts") {
                html += "<div class='row'><span class='bold'>Score obtained:</span><span>" + scoreObtained + "</span></div>";
            } else {
                if (scoreObtained != 0) {
                    html += "<div class='row'><span class='bold'>Score obtained:</span><span>" + scoreObtained + "%" + "</span></div>";
                } else {
                    html += "<div class='row'><span class='bold'>Score obtained:</span><span>" + scoreObtained + "</span></div>";
                }
            }
            html += "<div class='row'><span class='bold'>Possible Score:</span><span>" + possibleScore + "</span></div>";
            html += "</fieldset>";
        } else {
            //html += "<div>" + userInfo.wrongScoringDefined + "</div>";
            html = "";
        }
        $("#displayScoreDialog").append(html);

        var editorId = ele.parent().attr("id"),
            interactionType = userInfo.itemType[1],
            part = parseInt(ele.parent().attr("id").split("_")[1]) + 1;
        //part = $("#screenId").val();
        scoring.push(getScoreJson(interactionType/*, scoringMethod*/, scoreObtained, subAnsArr, possibleScore, part, /*crAnsArr, scoreObj, possibleAnsArr,*/ subquestionIndex));
    }
    return {
        "scoring": scoring,
        "itemID": $("#itemId").val(),
        "ScoreObtained": accomPkgScoreOb,
        "PossibleScore": accomPkgPossibleSc
    };
}

//gets the score of MCQ sequence

function mcqSeqGetScore(choiceWithSeq, index, scr) {
    var seqArr = new Array(),
        seq, seqScoreObtained = 0,
        chk, rad, html = "",
        count = 0,
        ele, scoreObj, method = 0,
        correctAns = 0,
        scoreObtained, ansCount;
    var ans, index1 = 0,
        index2 = 0,
        scoringMethod, correctAnswers = new Array(),
        crAns, incorrectAnswers = new Array(),
        incorrAnsCount = 0;
    var weigthedScoreType, possibleResponseScore, possibleSequenceScore, submittedAnsCount, totalOptions, blockLen, blockId, blockOpacity, inseqCount = 0;
    for (count = 0; count < choiceWithSeq.length; count++) {
        html = "<fieldset><legend class='bold'>Screen " + (index + 1) + "</legend>";
        scoreObtained = ansCount = possibleScore = 0;
        ele = choiceWithSeq.eq(count);
        scoreObj = editorContentJSON[scr.attr("id")][ele.attr("id")].property.scoring;
        if (scoreObj.complete) {
            method = parseInt(scoreObj.scoringMethod, 10) - 1;
            if (method == 2) {
                weigthedScoreType = editorContentJSON[scr.attr("id")][ele.attr("id")].property.scoring.scoreIn;
            } else {
                weigthedScoreType = "pts";
            }
            sequencingMethod = parseInt(scoreObj.mcqSeqScoringMethod, 10) - 1;
            if (sequencingMethod == 2) {
                seqWeigthedScoreType = editorContentJSON[scr.attr("id")][ele.attr("id")].property.scoring.mcqSeqScoreIn;
            } else {
                seqWeigthedScoreType = "pts";
            }

            chk = ele.find(":checkbox:checked");
            rad = ele.find(":radio:checked");
            blockLen = ele.find(".select");
            for (var count = 0; count < blockLen.length; count++) {
                blockOpacity = $(blockLen[count]).css("opacity");
                if (blockOpacity == 1) {
                    blockId = $(blockLen[count]).find("ul").attr("id");
                    crAns = $(blockLen[count]).find("span.selected_text").html();
                    seqArr.push({
                        "sequence": crAns,
                        "id": blockId
                    });
                }
            }

            correctAns = scoreObj.mcqSeqCorrectAns.length;
            if (chk.length > 0) {
                ans = chk;
            } else if (rad.length > 0) {
                ans = rad;
            } else {
                correctAns = 0;
            }
            submittedAnsCount = chk.length;
            totalOptions = ele.find(":checkbox");

            if (submittedAnsCount > 0) {
                for (var index1 = 0; index1 < ans.length; index1++) {
                    for (var index2 = 0; index2 < scoreObj.mcqSeqCorrectAns.length; index2++) {
                        crAns = ans.eq(index1).parent().attr("id");
                        if (scoreObj.mcqSeqCorrectAns[index2].correctAns == crAns) {
                            ansCount++;
                            correctAnswers.push({
                                "crAns": crAns,
                                "seq": $(".divbox #" + crAns).find(".select .selected_text").html()
                            });
                        }
                    }
                }
                if (ans) {
                    for (var index3 = 0; index3 < ans.length; index3++) {
                        crAns = ans.eq(index3).parent().attr("id");
                        for (var index4 = 0; index4 < scoreObj.incorrectAns.length; index4++) {
                            if (crAns == scoreObj.incorrectAns[index4].incorrectAns) {
                                incorrAnsCount++;
                                incorrectAnswers.push({
                                    "incrAns": crAns
                                });
                            }
                        }
                    }
                }
                inseqCount = (chk.length) - (correctAnswers.length);
                scoreObtained = getScoreObtained(scoreObj, method, correctAns,
                    ansCount, incorrAnsCount, correctAnswers, incorrectAnswers,
                    weigthedScoreType, submittedAnsCount, totalOptions);
                seqScoreObtained = getMcqSeqScoreObtained(scoreObj, sequencingMethod,
                    correctAns, ansCount, correctAnswers, incorrectAnswers, seqArr,
                    seqWeigthedScoreType, inseqCount);
                if (scoreObtained < 0) {
                    scoreObtained = 0;
                } else {
                    scoreObtained = scoreObtained;
                }
                if (seqScoreObtained < 0) {
                    seqScoreObtained = 0;
                } else {
                    seqScoreObtained = seqScoreObtained;
                }
            } else {
                scoreObtained = 0;
                seqScoreObtained = 0;
            }
            scoringMethod = userInfo.scoringMethodology[method];
            sequencingMethod = userInfo.scoringMethodology[sequencingMethod];
            possibleResponseScore = parseInt(scoreObj.totalScore, 10);
            possibleSequenceScore = parseInt(scoreObj.mcqSeqTotalScore, 10);
            possibleScore = possibleResponseScore + possibleSequenceScore;

            html += "<div class='row'><span class='bold'>Interaction:</span><span>" + userInfo.itemType[2] + "</span></div>";
            html += "<div class='row'><span class='bold'>Response scoring Methodology:</span><span>" + scoringMethod + "</span></div>";
            html += "<div class='row'><span class='bold'>Correct answer(s):</span><span>" + ansCount + "</span></div>";
            html += "<div class='row'><span class='bold'>Total correct answer(s):</span><span>" + scoreObj.totalCorrectAns + "</span></div>";
            if (weigthedScoreType == "pts") {
                html += "<div class='row'><span class='bold'>Score obtained:</span><span>" + scoreObtained + "</span></div>";
            } else {
                if (scoreObtained != 0) {
                    html += "<div class='row'><span class='bold'>Score obtained:</span><span>" + scoreObtained + "%" + "</span></div>";
                } else {
                    html += "<div class='row'><span class='bold'>Score obtained:</span><span>" + scoreObtained + "</span></div>";
                }
            }
            html += "<div class='row'><span class='bold'>Possible Score:</span><span>" + scoreObj.totalScore + "</span></div>";

            html += "<div class='row'><span class='bold'>Sequencing Methodology:</span><span>" + sequencingMethod + "</span></div>";
            if (seqWeigthedScoreType == "pts") {
                html += "<div class='row'><span class='bold'>Score obtained in sequence:</span><span>" + seqScoreObtained + "</span></div>";
            } else {
                if (scoreObtained != 0) {
                    html += "<div class='row'><span class='bold'>Score obtained in sequence:</span><span>" + seqScoreObtained + "%" + "</span></div>";
                } else {
                    html += "<div class='row'><span class='bold'>Score obtained in sequence:</span><span>" + seqScoreObtained + "</span></div>";
                }
            }
            html += "<div class='row'><span class='bold'>Possible Score in sequence:</span><span>" + scoreObj.mcqSeqTotalScore + "</span></div>";
            html += "</fieldset>";
        } else {
            //html += "<div>" + userInfo.wrongScoringDefined + "</div>";
            html = "";
        }
    }
    $("#displayScoreDialog").append(html);
    return {
        "ScoreObtained": scoreObtained,
        "PossibleScore": possibleScore
    };
}

//gets the score of DND sequence

function dndSeqGetScore(dndWithSeq, index, scr) {
    var seqMethod, html = "",
        count = 0,
        ele, scoreObj, method = 0,
        scoreObtained, ansCount, ans, drop, index1 = 0,
        index2 = 0,
        allCorrect = false,
        accomPkgScoreOb = 0,
        accomPkgPossibleSc = 0;
    var scoringMethod, pos, submittedAns, seqAnsCount = 0,
        dndSeqCorrectAns, weigthedScoreType, corAns = new Array(),
        correctAnsLen;
    var scoring = new Array(),
        subAnsArr, possibleAnsArr, crAnsArr, submittedOptions, correctOptions, possibleOptions;
    for (count = 0; count < dndWithSeq.length; count++) {
        subquestionIndex++;
        html = "<fieldset><legend class='bold'>Screen " + (index + 1) + "</legend>";
        scoreObtained = seqAnsCount = possibleScore = 0;
        ele = dndWithSeq.eq(count);
        scoreObj = editorContentJSON[scr.attr("id")][ele.attr("id")].property.scoring;
        if (scoreObj.complete) {
            subAnsArr = new Array(), possibleAnsArr = new Array(), crAnsArr = new Array();
            submittedOptions = new Array()/*, correctOptions = new Array(), possibleOptions = new Array()*/;
            seqMethod = parseInt(scoreObj.dndSeqScoringMethod, 10) - 1;
            if (seqMethod == 1) {
                weigthedScoreType = editorContentJSON[scr.attr("id")][ele.attr("id")].property.scoring.dndSeqScoreIn;
            } else {
                weigthedScoreType = "pts";
            }
            drop = ele.find("[behavior='3']");
            ans = drop.children("[behavior='2']");
            drag = ele.children("[behavior='2']");
            dndSeqCorrectAns = scoreObj.dndSeqCorrectAns;
            var submittedAns = new Array();
            var isImg = checkImage(drag);
            for (var sindex1 = 0; sindex1 < ans.length; sindex1++) {
                submittedAns.push(ans.eq(sindex1).attr("id"));
                if (isImg == false) {
                    submittedOptions.push({
                        "format": "Text",
                        "id": ans.eq(sindex1).attr("id"),
                        "response": identifySpecialChar(ans.eq(sindex1).find(".text").html())
                    });
                } else {
                    submittedOptions.push({
                        "format": "Name",
                        "id": ans.eq(sindex1).attr("id"),
                        "response": ans.eq(sindex1).attr("name")
                    });
                }
            }
            subAnsArr.push({
                "droparea": {
                    "name": drop.eq(index1).attr("name"),
                    "id": drop.eq(index1).attr("id")
                },
                "dragarea": submittedOptions
            });
            if ((seqMethod) == 0) {
                for (var index3 = 0; index3 < dndSeqCorrectAns.length; index3++) {
                    if (String(submittedAns) == String(dndSeqCorrectAns[index3].ctId)) {
                        seqAnsCount++;
                        pos = index3;
                        allCorrect = true;
                    } else {
                        var correctSeq = dndSeqCorrectAns[index3].ctId,
                            prevSeq = false;
                        for (var index5 = 0; index5 < submittedAns.length; index5++) {
                            if (index5 != submittedAns.length - 1) {
                                posAtCorrAns = correctSeq.indexOf(submittedAns[index5]);
                                if (submittedAns[index5 + 1] == correctSeq[posAtCorrAns + 1]) {
                                    corAns.push(submittedAns[index5]);
                                    corAns.push(submittedAns[index5 + 1]);
                                    currSeq = true;
                                } else {
                                    if (prevSeq == true) {
                                        seqAnsCount++;
                                    }
                                    currSeq = false;
                                }
                                prevSeq = currSeq;
                            }
                        }
                        if (prevSeq == true) {
                            seqAnsCount++;
                        }

                        pos = 0;
                    }
                }
            } else {
                for (index2 = 0; index2 < dndSeqCorrectAns.length; index2++) {
                    var correctSeq = dndSeqCorrectAns[index2].ctId;
                    if (String(submittedAns) == String(correctSeq)) {
                        seqAnsCount++;
                        pos = index2;
                        break;
                    } else {
                        pos = -1;
                    }
                }
            }
            if (allCorrect == true) {
                scoreObtained = parseInt(scoreObj.dndSeqCorrectAns[pos].weight, 10);
                accomPkgScoreOb += parseInt(scoreObj.dndSeqCorrectAns[pos].weight, 10);
                allCorrect = false;
            } else {
                correctAnsLen = removeDups(corAns);
                scoreObtained = getDNDSeqScoreObtained(scoreObj, seqMethod,
                    seqAnsCount, submittedAns, pos, weigthedScoreType, correctAnsLen);
                accomPkgScoreOb += getDNDSeqScoreObtained(scoreObj, seqMethod,
                    seqAnsCount, submittedAns, pos, weigthedScoreType, correctAnsLen);
            }

            scoringMethod = userInfo.dndScoringMethodology[seqMethod];
            possibleScore = parseInt(scoreObj.dndSeqTotalScore, 10);
            accomPkgPossibleSc += parseInt(scoreObj.dndSeqTotalScore, 10);

            html += "<div class='row'><span class='bold'>Interaction:</span><span>" + userInfo.itemType[3] + "</span></div>";
            html += "<div class='row'><span class='bold'>Methodology:</span><span>" + scoringMethod + "</span></div>";
            html += "<div class='row'><span class='bold'>Total correct answer(s):</span><span>" + scoreObj.dndSeqCorrectAns.length + "</span></div>";

            if (weigthedScoreType == "pts") {
                html += "<div class='row'><span class='bold'>Score obtained:</span><span>" + scoreObtained + "</span></div>";
            } else {
                if (scoreObtained != 0) {
                    html += "<div class='row'><span class='bold'>Score obtained:</span><span>" + scoreObtained + "%" + "</span></div>";
                } else {
                    html += "<div class='row'><span class='bold'>Score obtained:</span><span>" + scoreObtained + "</span></div>";
                }
            }

            html += "<div class='row'><span class='bold'>Possible Score in sequence:</span><span>" + possibleScore + "</span></div>";
            html += "</fieldset>";
        } else {
            //html += "<div>" + userInfo.wrongScoringDefined + "</div>";
            html = "";
        }
        $("#displayScoreDialog").append(html);
        var editorId = ele.parent().attr("id"),
            interactionType = userInfo.itemType[3],
            part = parseInt(ele.parent().attr("id").split("_")[1]) + 1;
        //part = $("#screenId").val();
        scoring.push(getScoreJson(interactionType, /*scoringMethod,*/ scoreObtained, subAnsArr, possibleScore, part, /*crAnsArr, scoreObj, possibleAnsArr,*/ subquestionIndex));
    }
    return {
        "scoring": scoring,
        "itemID": $("#itemId").val(),
        "ScoreObtained": accomPkgScoreOb,
        "PossibleScore": accomPkgPossibleSc
    };
}


//gets the score of DND sequence

function mcqDndGetScore(mcqDnd, index, scr) {
    var seqMethod, html = "",
        count = 0,
        ele, scoreObj, method = 0,
        scoreObtained, correctOptions, ansCount, ans, drop, index1 = 0,
        index2 = 0,
        allCorrect = false,
        accomPkgScoreOb = 0,
        accomPkgPossibleSc = 0;
    var scoringMethod, subAnsArr, pos, submittedAns, seqAnsCount = 0,
        dndSeqCorrectAns, weigthedScoreType, submittedOptions, corAns = new Array(),
        correctAnsLen;
    var scoring = new Array();
    for (count = 0; count < mcqDnd.length; count++) {
        html = "<fieldset><legend class='bold'>Screen " + (index + 1) + "</legend>";
        scoreObtained = seqAnsCount = 0;
        ele = mcqDnd.eq(count);
        scoreObj = editorContentJSON[scr.attr("id")][ele.attr("id")].property.scoring;
        if (scoreObj.complete) {
            drop = ele.find("[behavior='3']");
            ans = drop.children("[behavior='2']");
            drag = ele.children("[behavior='2']");
            dndSeqCorrectAns = scoreObj.dndSeqCorrectAns;
            correctOptions = new Array();
            submittedOptions = new Array();
            possibleOptions = new Array();
            var submittedAns = new Array();
            var subAnsArr = new Array();
           // var crAnsArr = new Array();
            //var possibleAnsArr = new Array();
            var isImg = checkImage(drag);
            for (var index1 = 0; index1 < ans.length; index1++) {
                submittedAns.push(ans.eq(index1).attr("id"));
                if (isImg == false) {
                    submittedOptions.push({
                        "format": "Text",
                        "id": ans.eq(index1).attr("id"),
                        "response": identifySpecialChar(ans.eq(index1).find(".text").html())
                    });
                } else {
                    submittedOptions.push({
                        "format": "Name",
                        "id": ans.eq(index1).attr("id"),
                        "response": ans.eq(index1).attr("name")
                    });
                }
            }
            subAnsArr.push({
                "droparea": {
                    "name": drop.attr("name"),
                    "id": drop.attr("id")
                },
                "dragarea": submittedOptions
            });
            for (index2 = 0; index2 < dndSeqCorrectAns.length; index2++) {
                var correctSeq = dndSeqCorrectAns[index2].ctId;
               
                if (String(submittedAns) == String(correctSeq)) {
                    scoreObtained = parseInt(scoreObj.dndSeqCorrectAns[index2].weight, 10);
                    accomPkgScoreOb += parseInt(scoreObj.dndSeqCorrectAns[index2].weight, 10);
                } else {
                    scoreObtained = 0;
                    accomPkgScoreOb += 0;
                }
            }
           
            scoringMethod = userInfo.scoringMethodology[0];
            possibleScore = parseInt(scoreObj.mcqdndSeqTotalScore, 10);
            accomPkgPossibleSc += parseInt(scoreObj.mcqdndSeqTotalScore, 10);

            html += "<div class='row'><span class='bold'>Interaction:</span><span>" + userInfo.itemType[7] + "</span></div>";
            html += "<div class='row'><span class='bold'>Methodology:</span><span>" + scoringMethod + "</span></div>";
            html += "<div class='row'><span class='bold'>Total correct answer(s):</span><span>" + scoreObj.dndSeqCorrectAns.length + "</span></div>";
            html += "<div class='row'><span class='bold'>Score obtained:</span><span>" + scoreObtained + "</span></div>";
            html += "<div class='row'><span class='bold'>Possible Score in sequence:</span><span>" + possibleScore + "</span></div>";
            html += "</fieldset>";
        } else {
            //html += "<div>" + userInfo.wrongScoringDefined + "</div>";
            html = "";
        }
        $("#displayScoreDialog").append(html);

        var editorId = ele.parent().attr("id"),
            interactionType = userInfo.itemType[7],
            part = parseInt(ele.parent().attr("id").split("_")[1]) + 1;
        //part = $("#screenId").val();
        scoring.push(getScoreJson(interactionType, /*scoringMethod,*/ scoreObtained, subAnsArr, possibleScore, part/*, crAnsArr, scoreObj, ""*/));
    }
    return {
        "scoring": scoring,
        "itemID": $("#itemId").val(),
        "ScoreObtained": accomPkgScoreOb,
        "PossibleScore": accomPkgPossibleSc
    };
}

//calculates the sequence in DND sequence

function removeDups(array) {
    var index = {};
    // traverse array from end to start so removing the current item from the array
    // doesn't mess up the traversal
    for (var i = array.length - 1; i >= 0; i--) {
        if (array[i] in index) {
            // remove this item
            array.splice(i, 1);
        } else {
            // add this value index
            index[array[i]] = true;
        }
    }
    return (array.length);
}

//gets the score of DND restricted

function dndRestrictedGetScore(dndPassageRestricted, index, scr) {
    var html = "",
        isCorrectAns, count = 0,
        ele, scoreObj, method = 0,
        correctAns = 0,
        scoreObtained, ansCount, ans, drop, index1 = 0,
        index2 = 0,
        accomPkgScoreOb = 0,
        accomPkgPossibleSc = 0;
    var cns, eachAns, scoringMethod, correctAnswers, incorrectAnswers, submittedOptions, possibleOptions, correctOptions, submittedAns, dndAnsCountArr = new Array(),
        possibleAnsArr, crAnsArr, subAnsArr, weigthedScoreType, isAnswerWrong, possible, pos, escapedHtml, escapedString;
    var scoring = new Array();
    for (count = 0; count < dndPassageRestricted.length; count++) {
        subquestionIndex++;
        html = "<fieldset><legend class='bold'>Screen " + (index + 1) + "</legend>";
        scoreObtained = ansCount = possibleScore = 0;
        correctAnswers = new Array();
        incorrectAnswers = new Array();
        ele = dndPassageRestricted.eq(count);
        possibleOptions = new Array();
        scoreObj = editorContentJSON[scr.attr("id")][ele.attr("id")].property.scoring;
        if (scoreObj.complete) {
            method = parseInt(scoreObj.scoringMethod, 10) - 1;
            if (method == 2) {
                weigthedScoreType = editorContentJSON[scr.attr("id")][ele.attr("id")].property.scoring.scoreIn;
            } else {
                weigthedScoreType = "pts";
            }
            drop = ele.find("[behavior='3']");
            ans = drop.find("span.answer");
            
            var subAnsArr = new Array()//,
                /*crAnsArr = new Array()*/;
            for (index1 = 0; index1 < drop.length; index1++) {
                var submittedOptions = new Array(),
                    correctOptions = new Array();
                dndCorrectAnsCount = dndIncorrectAnsCount = 0;
                submittedQuestion = drop[index1].id;
                submittedAns = drop.eq(index1).find("span.answer");
                for (index2 = 0; index2 < submittedAns.length; index2++) {
                    eachAns = submittedAns.eq(index2).text();
                    cns = scoreObj.correctAns[index1];
                    if (cns.correctQs == submittedQuestion) {
                        for (var innerCount = 0; innerCount < cns.correctAns.length; innerCount++) {
                            escapedHtml = $("<p></p>").html(cns.correctAns[innerCount]);
                            escapedString = $(escapedHtml).text();
                            cns.correctAns[innerCount] = escapedString;
                            pos = cns.correctAns[innerCount].indexOf(eachAns);
                            if (pos > -1) {
                                break;
                            }
                        }
                        if (pos > -1) {
                            dndCorrectAnsCount++;
                        } else {
                            dndIncorrectAnsCount++;
                        }
                    }
                    var demo = $("<div />").append(drop.eq(index1).find("div.dropped").eq(index2).clone()).html();
                    submittedOptions.push({
                        "format": "Text",
                        "id": "",
                        "response": identifySpecialChar(submittedAns.eq(index2).html()),
                        "htmlresponse": identifySpecialCharPassage($.trim(demo))
                    })

                }

                dndAnsCountArr.push({
                    "correct": dndCorrectAnsCount,
                    "incorrect": dndIncorrectAnsCount
                });
                
                subAnsArr.push({
                    "droparea": {
                        "name": drop.eq(index1).attr("name"),
                        "id": drop.eq(index1).attr("id")
                    },
                    "dragarea": submittedOptions
                });

            }

            scoreObtained += getDndPassageScoreObtained(scoreObj, method,
                correctAns, dndAnsCountArr, drop, weigthedScoreType);
            if (scoreObtained < 0) {
                scoreObtained = 0;
                accomPkgScoreOb += 0;
            } else {
                scoreObtained = scoreObtained;
                accomPkgScoreOb += scoreObtained;
            }
            scoringMethod = userInfo.scoringMethodology[method];
            possibleScore = scoreObj.totalScore;
            accomPkgPossibleSc += parseInt(scoreObj.totalScore, 10);

            html += "<div class='row'><span class='bold'>Interaction:</span><span>" + userInfo.itemType[4] + "</span></div>";
            html += "<div class='row'><span class='bold'>Methodology:</span><span>" + scoringMethod + "</span></div>";

            if (weigthedScoreType == "pts") {
                html += "<div class='row'><span class='bold'>Score obtained:</span><span>" + scoreObtained + "</span></div>";
            } else {
                if (scoreObtained != 0) {
                    html += "<div class='row'><span class='bold'>Score obtained:</span><span>" + scoreObtained + "%" + "</span></div>";
                } else {
                    html += "<div class='row'><span class='bold'>Score obtained:</span><span>" + scoreObtained + "</span></div>";
                }
            }

            html += "<div class='row'><span class='bold'>Possible Score:</span><span>" + possibleScore + "</span></div>";
            html += "</fieldset>";
        } else {
            //html += "<div>" + userInfo.wrongScoringDefined + "</div>";
            html = "";
        }
        $("#displayScoreDialog").append(html);
        //Making the array empty
        dndAnsCountArr.length = 0;
        var editorId = ele.parent().attr("id"),
            interactionType = userInfo.itemType[4],
            part = parseInt(ele.parent().attr("id").split("_")[1]) + 1;
        //part = $("#screenId").val();
        scoring.push(getScoreJson(interactionType, /*scoringMethod,*/ scoreObtained, subAnsArr, possibleScore, part, /*crAnsArr, scoreObj, possibleAnsArr,*/ subquestionIndex));
    }
    return {
        "scoring": scoring,
        "itemID": $("#itemId").val(),
        "ScoreObtained": accomPkgScoreOb,
        "PossibleScore": accomPkgPossibleSc
    };
}

//gets the score of DND freeflow

function dndFreeflowGetScore(dndPassageFreeflow, index, scr) {
    var html = "",
        isCorrectAns, count = 0,
        count1 = 0,
        ele, scoreObj, method = 0,
        correctAns = 0,
        scoreObtained, ansCount, ans, drop, index1 = 0,
        index2 = 0,
        cns, eachAns, scoringMethod, accomPkgScoreOb = 0,
        accomPkgPossibleSc = 0;
    var correctAnswers, incorrectAnswers, submittedAns, dndAnsCountArr = new Array(),
        correctOptions, weigthedScoreType, isAnswerWrong, submittedCrAns = new Array();
    var scoring = new Array();
    for (count = 0; count < dndPassageFreeflow.length; count++) {
        subquestionIndex++;
        html = "<fieldset><legend class='bold'>Screen " + (index + 1) + "</legend>";
        scoreObtained = ansCount = possibleScore = 0;
        correctAnswers = new Array();
        incorrectAnswers = new Array();
        ele = dndPassageFreeflow.eq(count);
        correctOptions = new Array(),
        scoreObj = editorContentJSON[scr.attr("id")][ele.attr("id")].property.scoring;
        for (var k = 0; k < scoreObj.correctAns.length; k++) {
            for (var j = 0; j < scoreObj.correctAns[k].correctAns.length; j++) {
                var correctOp = scoreObj.correctAns[k].correctAns[j];
                correctOptions.push({
                    "format": "Text",
                    "response": identifySpecialChar(correctOp)
                });
            }
        }
        if (scoreObj.complete) {
            method = parseInt(scoreObj.scoringMethod, 10) - 1;
            if (method == 2) {
                weigthedScoreType = editorContentJSON[scr.attr("id")][ele.attr("id")].property.scoring.scoreIn;
            } else {
                weigthedScoreType = "pts";
            }
            drop = ele.find("[behavior='3']");
            ans = drop.find(".dropped");
            dndAnsCountArr = new Array();
            var subAnsArr = new Array()//,
                /*possibleAnsArr = new Array(),
                crAnsArr = new Array()*/;
            for (index1 = 0; index1 < drop.length; index1++) {
                var submittedOptions = new Array(),
                    possibleOptions = new Array();
                dndCorrectAnsCount = dndIncorrectAnsCount = 0;
                isCorrectAns = false;
                submittedQuestion = drop[index1].id;
                submittedAns = drop.eq(index1).find(".dropped");
                for (index2 = 0; index2 < submittedAns.length; index2++) {
                    cns = scoreObj.correctAns[index1];
                    eachAns = $.trim((submittedAns.eq(index2).find("span.answer").text()));
                    for (var count1 = 0; count1 < cns.correctAns.length; count1++) {
                        escapedHtml = $("<p></p>").html(cns.correctAns[count1]);
                        escapedString = $(escapedHtml).text();
                        cns.correctAns[count1] = escapedString;
                        submittedCrAns = eachAns.split(cns.correctAns[count1]);
                        if (submittedCrAns != eachAns) {
                            var range = scoreObj.range;
                            if (submittedCrAns[0] == "" && submittedCrAns[1] == "") {
                                isCorrectAns = true;
                                dndCorrectAnsCount++;
                                correctAnswers.push({
                                    "qs": submittedQuestion,
                                    "corrAns": eachAns
                                });
                                break;
                            } else {
                                if (submittedCrAns[0] == "" && submittedCrAns[1] != "") {
                                    var nextAns = submittedCrAns[1];
                                    var sizeOfNextAns = (nextAns.split(" ")).length - 1;
                                    if (sizeOfNextAns <= range) {
                                        isCorrectAns = true;
                                        dndCorrectAnsCount++;
                                        correctAnswers.push({
                                            "qs": submittedQuestion,
                                            "corrAns": eachAns
                                        });
                                        break;
                                    } else {
                                        isCorrectAns = false;
                                        dndIncorrectAnsCount++;
                                        incorrectAnswers.push({
                                            "qs": $(drop[index1]).attr("id"),
                                            "incorrAns": eachAns
                                        });
                                    }
                                } else if (submittedCrAns[0] != "" && submittedCrAns[1] != "") {
                                    var nextAns = submittedCrAns[1];
                                    var sizeOfNextAns = (nextAns.split(" ")).length - 1;
                                    var prevAns = submittedCrAns[0];
                                    var sizeOfPrevAns = (prevAns.split(" ")).length - 1;
                                    if (sizeOfNextAns <= range && sizeOfPrevAns <= range) {
                                        isCorrectAns = true;
                                        dndCorrectAnsCount++;
                                        correctAnswers.push({
                                            "qs": submittedQuestion,
                                            "corrAns": eachAns
                                        });
                                        break;
                                    } else {
                                        isCorrectAns = false;
                                        dndIncorrectAnsCount++;
                                        incorrectAnswers.push({
                                            "qs": $(drop[index1]).attr("id"),
                                            "incorrAns": eachAns
                                        });
                                    }
                                }
                                if (submittedCrAns[0] != "" && submittedCrAns[1] == "") {
                                    var prevAns = submittedCrAns[0];
                                    var sizeOfPrevAns = (prevAns.split(" ")).length - 1;
                                    if (sizeOfPrevAns <= range) {
                                        isCorrectAns = true;
                                        dndCorrectAnsCount++;
                                        correctAnswers.push({
                                            "qs": submittedQuestion,
                                            "corrAns": eachAns
                                        });
                                        break;
                                    } else {
                                        isCorrectAns = false;
                                        dndIncorrectAnsCount++;
                                        incorrectAnswers.push({
                                            "qs": $(drop[index1]).attr("id"),
                                            "incorrAns": eachAns
                                        });
                                    }
                                }
                            }
                        } else {
                            isCorrectAns = false;
                        }
                    }
                    if (isCorrectAns == false) {
                        dndIncorrectAnsCount++;
                        incorrectAnswers.push({
                            "qs": $(drop[index1]).attr("id"),
                            "incorrAns": eachAns
                        });
                    }
                    var demo = $("<div />").append(submittedAns.eq(index2).clone()).html();
                    submittedOptions.push({
                        "format": "Text",
                        "id": "",
                        "response": identifySpecialChar($.trim((submittedAns.eq(index2).find("span.answer").html()))),
                        "htmlresponse": identifySpecialCharPassage($.trim(demo))
                    });
                }

                dndAnsCountArr.push({
                    "correct": dndCorrectAnsCount,
                    "incorrect": dndIncorrectAnsCount
                });
               
                subAnsArr.push({
                    "droparea": {
                        "name": drop.eq(index1).attr("name"),
                        "id": drop.eq(index1).attr("id")
                    },
                    "dragarea": submittedOptions
                });
            }

            scoreObtained += getDndScoreObtained(scoreObj, method, correctAns,
                dndAnsCountArr, correctAnswers, incorrectAnswers, drop,
                weigthedScoreType);
            scoringMethod = userInfo.scoringMethodology[method];
            if (scoreObtained < 0) {
                scoreObtained = 0;
                accomPkgScoreOb += 0;
            } else {
                scoreObtained = scoreObtained;
                accomPkgScoreOb += scoreObtained;
            }
            possibleScore = scoreObj.totalScore;
            accomPkgPossibleSc += parseInt(scoreObj.totalScore, 10);

            html += "<div class='row'><span class='bold'>Interaction:</span><span>" + userInfo.itemType[5] + "</span></div>";
            html += "<div class='row'><span class='bold'>Methodology:</span><span>" + scoringMethod + "</span></div>";

            if (weigthedScoreType == "pts") {
                html += "<div class='row'><span class='bold'>Score obtained:</span><span>" + scoreObtained + "</span></div>";
            } else {
                if (scoreObtained != 0) {
                    html += "<div class='row'><span class='bold'>Score obtained:</span><span>" + scoreObtained + "%" + "</span></div>";
                } else {
                    html += "<div class='row'><span class='bold'>Score obtained:</span><span>" + scoreObtained + "</span></div>";
                }
            }
            html += "<div class='row'><span class='bold'>Possible Score:</span><span>" + possibleScore + "</span></div>";
            html += "</fieldset>";
        } else {
            //html += "<div>" + userInfo.wrongScoringDefined + "</div>";
            html = "";
        }
        $("#displayScoreDialog").append(html);
        var editorId = ele.parent().attr("id"),
            interactionType = userInfo.itemType[5],
            part = parseInt(ele.parent().attr("id").split("_")[1]) + 1;
        //part = $("#screenId").val();
        scoring.push(getScoreJson(interactionType, /*scoringMethod,*/ scoreObtained, subAnsArr, possibleScore, part, /*crAnsArr, scoreObj, "",*/ subquestionIndex));
    }
    return {
        "scoring": scoring,
        "itemID": $("#itemId").val(),
        "ScoreObtained": accomPkgScoreOb,
        "PossibleScore": accomPkgPossibleSc
    };
}

//gets the score of DND math

function dndMathGetScore(dndMath, index, scr) {
    var seqMethod, html = "",
        isCorrectAns, count = 0,
        ele, scoreObj, correctAns = 0,
        scoreObtained, ansCount, drop, index1 = 0,
        index2 = 0,
        eachAns, scoringMethod;
    var submittedAns, weigthedScoreType, accomPkgScoreOb = 0,
        accomPkgPossibleSc = 0,
        subAnsArr, possibleAnsArr, crAnsArr, submittedOptions, correctOptions, possibleOptions;
    var scoring = new Array();
    for (count = 0; count < dndMath.length; count++) {
        subquestionIndex++;
        html = "<fieldset><legend class='bold'>Screen " + (index + 1) + "</legend>";
        scoreObtained = score = possibleScore = 0, eachAns = symbol = "";
        ele = dndMath.eq(count);
        scoreObj = editorContentJSON[scr.attr("id")][ele.attr("id")].property.scoring;
        if (scoreObj.complete) {
            seqMethod = parseInt(scoreObj.dndMathScoringMethod, 10) - 1;
            weigthedScoreType = editorContentJSON[scr.attr("id")][ele.attr("id")].property.scoring.dndMathScoreIn;
            drop = ele.find("[behavior='3']");
            drag = ele.children("[behavior='2']");
            dndMathCorrectAns = scoreObj.correctAns;
            subAnsArr = new Array(), possibleAnsArr = new Array(), crAnsArr = new Array();
            for (index1 = 0; index1 < drop.length; index1++) {
                submittedOptions = new Array(), /* correctOptions = new Array(),*/ possibleOptions = new Array();
                isCorrectAns = false;
                submittedAns = drop.eq(index1).children("[behavior='2']");
                for (index2 = 0; index2 < submittedAns.length; index2++) {
                    if (eachAns != "") {
                        eachAns += ",";
                        eachAns += $(submittedAns[index2]).attr("id");
                        symbol += ",";
                        symbol += getMathpaletteSymbols($(submittedAns[index2]).attr("id").split("_")[2]);
                    } else {
                        eachAns += $(submittedAns[index2]).attr("id");
                        symbol += getMathpaletteSymbols($(submittedAns[index2]).attr("id").split("_")[2]);
                    }
                }
                submittedOptions.push({
                    "format": "Name",
                    "id": eachAns,
                    "response": symbol
                });
                for (var index3 = 0; index3 < dndMathCorrectAns.length; index3++) {
                    
                    if (eachAns == dndMathCorrectAns[index3].id) {
                        isCorrectAns = true;
                        score = dndMathCorrectAns[index3].weight;
                        break;
                    }
                }
                
                if (isCorrectAns == true) {
                    if (weigthedScoreType == "pts") {
                        scoreObtained = score;
                        accomPkgScoreOb += score;
                    } else {
                        scoreObtained = (score / scoreObj.dndMathTotalScore) * 100;
                        accomPkgScoreOb += (score / scoreObj.dndMathTotalScore) * 100;
                    }
                } else {
                    scoreObtained = 0;
                    accomPkgScoreOb += 0;
                }

                
                subAnsArr.push({
                    "droparea": {
                        "name": drop.eq(index1).attr("name"),
                        "id": drop.eq(index1).attr("id")
                    },
                    "dragarea": submittedOptions
                });
            }

           
            scoringMethod = "Weighted";

            possibleScore = scoreObj.dndMathTotalScore;
            accomPkgPossibleSc += parseInt(scoreObj.dndMathTotalScore, 10);

            html += "<div class='row'><span class='bold'>Interaction:</span><span>" + userInfo.itemType[6] + "</span></div>";
            html += "<div class='row'><span class='bold'>Methodology:</span><span>" + scoringMethod + "</span></div>";
            html += "<div class='row'><span class='bold'>Total correct answer(s):</span><span>" + dndMathCorrectAns.length + "</span></div>";

            if (weigthedScoreType == "pts") {
                html += "<div class='row'><span class='bold'>Score obtained:</span><span>" + scoreObtained + "</span></div>";
            } else {
                if (scoreObtained != 0) {
                    html += "<div class='row'><span class='bold'>Score obtained:</span><span>" + scoreObtained + "%" + "</span></div>";
                } else {
                    html += "<div class='row'><span class='bold'>Score obtained:</span><span>" + scoreObtained + "</span></div>";
                }
            }

            html += "<div class='row'><span class='bold'>Possible Score:</span><span>" + possibleScore + "</span></div>";
            html += "</fieldset>";
        } else {
            //html += "<div>" + userInfo.wrongScoringDefined + "</div>";
            html = "";
        }
        $("#displayScoreDialog").append(html);
        var editorId = ele.parent().attr("id"),
            interactionType = userInfo.itemType[6],
            part = parseInt(ele.parent().attr("id").split("_")[1]) + 1;
        //part = $("#screenId").val();
        scoring.push(getScoreJson(interactionType, /*scoringMethod,*/ scoreObtained, subAnsArr, possibleScore, part, /*crAnsArr, scoreObj, possibleAnsArr,*/ subquestionIndex));
    }

    return {
        "scoring": scoring,
        "itemID": $("#itemId").val(),
        "ScoreObtained": accomPkgScoreOb,
        "PossibleScore": accomPkgPossibleSc
    };
}

/* closes all teh dialog boxes in preview dialog when closed*/

function closeAllDialog() {
    setTimeout(function () {
        $("#questionDialog, #displayScoreDialog, #previewAttributeDialog, #userInfoDialog").dialog("close");
    }, 1);
}

function closeStimulusDialogs() {
    setTimeout(function () {
        $("#questionDialog,#previewStimulusAttributeDialog, #userInfoDialog").dialog("close");
    }, 1);
}

function releaseDrag() {
    $("body").trigger("mouseup");
}

function sendNotification() {
    if (isStandAlone) {
        accomPkg.notifyChange();
    }
}


function getDroppedPosition(drop, selector, $ele) {
    var dropElePos = {
        top: 0,
        left: 0
    }, height = 0,
        width = 0,
        index = 0,
        prevAll, dropAlignment, clone, ele;
    dropAlignment = drop.attr("dropalignment");
    if (drop.children(selector).length > 1) {
        prevAll = $ele.prevAll(selector + ":not(.ui-sortable-placeholder,:empty)");

        if (((dropAlignment == undefined || dropAlignment == "") && (selector != ".palette-button")) || (dropAlignment == "1")) {
            for (index = 0; index < prevAll.length; index++) {
                ele = prevAll.eq(index);
                clone = ele.clone();
                clone.css("top", "-1000px");
                $("body").append(clone);
                if (selector == ".palette-button") {
                    height += clone.height() + 1;
                } else {
                    height += clone.height() + 6;
                }
                clone.remove();
            }
            dropElePos.top = height + "px";
        } else if (dropAlignment == "2" || selector == ".palette-button") {
            for (index = 0; index < prevAll.length; index++) {
                ele = prevAll.eq(index)
                if (selector == ".palette-button") {
                    width += ele.width() + 11;
                } else {
                    width += ele.width() + 6;
                }
            }
            dropElePos.left = width + "px";
        }
    }
    return dropElePos;
}


function setDropAreaScrollbar(drop) {
    var ele, index = 0,
        elements = drop.children(":not(.text,.handle)") /*.find("> .dropped")*/ ,
        dropH = drop.height(),
        dropW = drop.width(),
        h = 0,
        w = 0,
        h1 = 0,
        w1 = 0,
        dropAlignment = drop.attr("dropalignment");
    for (index = 0; index < elements.length; index++) {
        ele = elements.eq(index);
        if (ele.children().length > 0) {
            if (dropAlignment == "1" || dropAlignment == undefined) {
                if (ele.height() == 0) {
                    h += getActualHeight(ele) + 6;
                } else {
                    h += ele.height() + 6;
                }
                h1 = h;
            } else {
                if (ele.height() == 0) {
                    h = getActualHeight(ele) + 6;
                } else {
                    h = ele.height() + 6;
                    if (h1 <= h) {
                        h1 = h;
                    }
                }
            }
            if (dropAlignment == "1" || dropAlignment == undefined) {
                w = ele.width() + 6;
                if (w1 <= w) {
                    w1 = w;
                }
            } else {
                w += ele.width() + 6;
                w1 = w;
            }
        }
    }
    if (h1 >= dropH && w1 >= dropW) {
        drop.css("overflow-x", "auto");
        drop.css("overflow-y", "auto");
    } else if (h1 >= dropH && w1 <= dropW) {
        drop.css("overflow", "auto");
        drop.css("overflow-x", "hidden");
    } else if (h1 <= dropH && w1 >= dropW) {
        drop.css("overflow", "auto");
        drop.css("overflow-y", "hidden");
    } else {
        drop.css("overflow", "hidden");
    }
}

function getActualHeight(obj) {
    var clone = obj.clone();
    clone.css("visibility", "hidden");
    $('body').append(clone);
    var height = clone.height();
    clone.remove();
    return height;
}

function setLargeFont(scaleX, scaleY) {
    var tr = "scale(" + scaleX + ", " + scaleY + ")";
    $(".editor").css({
        "-webkit-transform": tr,
        "-ms-transform": tr,
        "transform": tr,
        "transform-origin": "0px 0px",
        "-webkit-transform-origin": "0px 0px",
        "-ms-transform-origin": "0px 0px"
    });
}

function getScoreJson(interactionType,/* scoringMethod,*/ scoreObtained, answeredText, maxScore, part, /*correctAnswers, scoreObj, possibleAnswers,*/ subquestionIndex) {
     scoreJson = {
        "interactionType": interactionType,
        "subquestionIndex": subquestionIndex,
        "scoreObtained": String(scoreObtained),
        "answered": answeredText,
        "maxScore": maxScore,
        "screenName": "Screen " + part
    }
    return scoreJson;
}

function getMathpaletteSymbols(index) {

    var symbolName = '';
    switch (index) {
    case '0':
        {
            symbolName = '0';
            break;
        }
    case '1':
        {
            symbolName = '1';
            break;
        }
    case '2':
        {
            symbolName = '2';
            break;
        }
    case '3':
        {
            symbolName = '3';
            break;
        }
    case '4':
        {
            symbolName = '4';
            break;
        }
    case '5':
        {
            symbolName = '5';
            break;
        }
    case '6':
        {
            symbolName = '6';
            break;
        }
    case '7':
        {
            symbolName = '7';
            break;
        }
    case '8':
        {
            symbolName = '8';
            break;
        }
    case '9':
        {
            symbolName = '9';
            break;
        }
    case '10':
        {
            symbolName = 'plus';
            break;
        }
    case '11':
        {
            symbolName = 'minus';
            break;
        }
    case '12':
        {
            symbolName = 'multiplication';
            break;
        }
    case '13':
        {
            symbolName = 'division';
            break;
        }
    case '14':
        {
            symbolName = 'point';
            break;
        }
    case '15':
        {
            symbolName = 'a';
            break;
        }
    case '16':
        {
            symbolName = 'b';
            break;
        }
    case '17':
        {
            symbolName = 'c';
            break;
        }
    case '18':
        {
            symbolName = 'd';
            break;
        }
    case '19':
        {
            symbolName = 'h';
            break;
        }
    case '20':
        {
            symbolName = 'l';
            break;
        }
    case '21':
        {
            symbolName = 'p';
            break;
        }
    case '22':
        {
            symbolName = 'r';
            break;
        }
    case '23':
        {
            symbolName = 's';
            break;
        }
    case '24':
        {
            symbolName = 'w';
            break;
        }
    case '25':
        {
            symbolName = 'x';
            break;
        }
    case '26':
        {
            symbolName = 'y';
            break;
        }
    case '27':
        {
            symbolName = 'z';
            break;
        }
    case '28':
        {
            symbolName = 'dollar';
            break;
        }
    case '29':
        {
            symbolName = 'cent';
            break;
        }
    case '30':
        {
            symbolName = 'open-brace';
            break;
        }
    case '31':
        {
            symbolName = 'closing-brace';
            break;
        }
    case '32':
        {
            symbolName = 'colon';
            break;
        }
    case '33':
        {
            symbolName = 'degree';
            break;
        }
    case '34':
        {
            symbolName = 'percent';
            break;
        }
    }
    return symbolName;
}

function identifySpecialChar(htmlData) {
    var returnData = "",
        charCode = "";
    for (var counter = 0; counter < htmlData.length; counter++) {
        if ((charCode = htmlData[counter].charCodeAt()) > 127) {
            //returnData += "&#x" + charCode.toString(16) + ";";
            returnData += " /u" + charCode.toString(16) + " ";
        } else {
            returnData += htmlData[counter];
        }
    }
    returnData = returnData.replace(/(\t\n|\n|\t|<br>|&nbsp;)/gm, "");
    returnData = returnData.replace(/<sup>/g, " superscript ");
    returnData = returnData.replace(/<sub>/g, " subscript ");
    returnData = returnData.replace(/<span>/g, "");
    //returnData = returnData.replace(/<\/?[^(>|\\)]+(>|$)/g, "");
    returnData = returnData.replace(/<\/?[^>|\\]+(>|$)/g, "");
    return returnData;
}

function identifySpecialCharPassage(htmlData) {
    var returnData = "",
        charCode = "";
    for (var counter = 0; counter < htmlData.length; counter++) {
        if ((charCode = htmlData[counter].charCodeAt()) > 127) {
            returnData += "&#x" + charCode.toString(16) + ";";
            //returnData += " /u" + charCode.toString(16)+" ";
        } else {
            returnData += htmlData[counter];
        }
    }
    return returnData;
}

function checkImage(items) {
    var isImgPresent = false,
        itemText = "";
    for (var itemCount = 0; itemCount < items.length; itemCount++) {
        itemText = items.eq(itemCount).find(".text").html();
        if (itemText.indexOf("<img") > 0) {
            isImgPresent = true;
            break;
        } else {
            isImgPresent = false;
        }
    }
    return isImgPresent;
}

function isDragOutside(dp, pageX, pageY) {
    var flag = true,
        dp, pos, dpTop = 0,
        dpLeft = 0,
        dpBottom = 0,
        dpRight = 0,
        parentPos;
    parentPos = dp.parent().position();
    pos = dp.position();
    var scalex = $("#scaleX").val(),
        scaley = $("#scaleY").val();
    dpTop = parseInt(pos.top, 10) + parseInt(parentPos.top, 10) * scalex;
    dpLeft = parseInt(pos.left, 10) + parseInt(parentPos.left, 10) * scaley;
    dpBottom = dpTop + dp.outerHeight(true) * scalex;
    dpRight = dpLeft + dp.outerWidth(true) * scaley;

    if (pageX > dpLeft && pageX < dpRight && pageY > dpTop && pageY < dpBottom) {
        flag = false;
    }
    return flag;
}
function placeTextDivAtLast(obj){
	allObjs = $(obj).children();
	var counter = 0;
	var eachObj = "";
	var textObj = [];
	for(;counter < allObjs.length;counter++){
		eachObj = allObjs.eq(counter);
		if(eachObj.is('.text')){
			textObj.push(eachObj.clone());
			eachObj.remove();
		}
	}
	for(counter = 0;counter < textObj.length;counter ++){
		$(obj).append(textObj[counter]);
	}

}