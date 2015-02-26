var checkHighlighter = 0,
    subquestionIndex = 0;
(function () {
    var modifiers = ['ctrl', 'alt', 'shift'],
        KEY_MAP = {},
        shifted_symbols = {
            58: 59, // : -> ;
            43: 61, // = -> +
            60: 44, // < -> ,
            95: 45, // _ -> -
            62: 46, // > -> .
            63: 47, // ? -> /
            96: 192, // ` -> ~
            124: 92, // | -> \
            39: 222, // ' -> 222
            34: 222, // " -> 222
            33: 49, // ! -> 1
            64: 50, // @ -> 2
            35: 51, // # -> 3
            36: 52, // $ -> 4
            37: 53, // % -> 5
            94: 54, // ^ -> 6
            38: 55, // & -> 7
            42: 56, // * -> 8
            40: 57, // ( -> 9
            41: 58, // ) -> 0
            123: 91, // { -> [
            125: 93 // } -> ]
        };

    function isLower(ascii) {
        return ascii >= 97 && ascii <= 122;
    };

    function capitalize(str) {
        return str.substr(0, 1).toUpperCase() + str.substr(1).toLowerCase();
    };

    var is_gecko = navigator.userAgent.indexOf('Gecko') != -1,
        is_ie = navigator.userAgent.indexOf('MSIE') != -1,
        is_windows = navigator.platform.indexOf('Win') != -1,
        is_opera = window.opera && window.opera.version() < '9.5',
        is_konqueror = navigator.vendor && navigator.vendor.indexOf('KDE') != -1,
        is_icab = navigator.vendor && navigator.vendor.indexOf('iCab') != -1;

    var GECKO_IE_KEYMAP = {
        186: 59, // ;: in IE
        187: 61, // =+ in IE
        188: 44, // ,<
        109: 95, // -_ in Mozilla
        107: 61, // =+ in Mozilla
        189: 95, // -_ in IE
        190: 62, // .>
        191: 47, // /?
        192: 126, // `~
        219: 91, // {[
        220: 92, // \|
        221: 93 // }]
    };

    var OPERA_KEYMAP = {};

    if (is_opera && is_windows) {
        KEY_MAP = OPERA_KEYMAP;
    } else if (is_opera || is_konqueror || is_icab) {
        var unshift = [33, 64, 35, 36, 37, 94, 38, 42, 40, 41,
            58, 43, 60, 95, 62, 63, 124, 34
        ];
        KEY_MAP = OPERA_KEYMAP;
        for (var i = 0; i < unshift.length; ++i) {
            KEY_MAP[unshift[i]] = shifted_symbols[unshift[i]];
        }
    } else {
        KEY_MAP = GECKO_IE_KEYMAP;
    }

    if (is_konqueror) {
        KEY_MAP[0] = 45;
        KEY_MAP[127] = 46;
        KEY_MAP[45] = 95;
    }

    var key_names = {
        32: 'SPACE',
        13: 'ENTER',
        9: 'TAB',
        8: 'BACKSPACE',
        16: 'SHIFT',
        17: 'CTRL',
        18: 'ALT',
        20: 'CAPS_LOCK',
        144: 'NUM_LOCK',
        145: 'SCROLL_LOCK',
        37: 'LEFT',
        38: 'UP',
        39: 'RIGHT',
        40: 'DOWN',
        33: 'PAGE_UP',
        34: 'PAGE_DOWN',
        36: 'HOME',
        35: 'END',
        45: 'INSERT',
        46: 'DELETE',
        27: 'ESCAPE',
        19: 'PAUSE',
        222: "'"
    };

    function fn_name(code) {
        if (code >= 112 && code <= 123) return 'F' + (code - 111);
        return false;
    };

    function num_name(code) {
        if (code >= 96 && code < 106) return 'Num' + (code - 96);
        switch (code) {
        case 106:
            return 'Num*';
        case 111:
            return 'Num/';
        case 110:
            return 'Num.';
        default:
            return false;
        }
    };

    var current_keys = {
        codes: {},
        ctrl: false,
        alt: false,
        shift: false
    };

    function update_current_modifiers(key) {
        current_keys.ctrl = key.ctrl;
        current_keys.alt = key.alt;
        current_keys.shift = key.shift;
    };

    function same_modifiers(key1, key2) {
        return key1.ctrl === key2.ctrl && key1.alt === key2.alt && key1.shift === key2.shift;
    };

    if (typeof window.KeyCode != "undefined") {
        var _KeyCode = window.KeyCode;
    }

    var KeyCode = window.KeyCode = {
        no_conflict: function () {
            window.KeyCode = _KeyCode;
            return KeyCode;
        },
        fkey: function (num) {
            return 111 + num;
        },
        numkey: function (num) {
            switch (num) {
            case '*':
                return 106;
            case '/':
                return 111;
            case '.':
                return 110;
            default:
                return 96 + num;
            }
        },
        key: function (str) {
            var c = str.charCodeAt(0);
            if (isLower(c)) return c - 32;
            return shifted_symbols[c] || c;
        },
        key_equals: function (key1, key2) {
            return key1.code == key2.code && same_modifiers(key1, key2);
        },
        translate_key_code: function (code) {
            return KEY_MAP[code] || code;
        },
        translate_event: function (e) {
            e = e || window.event;
            var code = e.which || e.keyCode;
            return {
                code: KeyCode.translate_key_code(code),
                shift: e.shiftKey,
                alt: e.altKey,
                ctrl: e.ctrlKey
            };
        },
        key_down: function (e) {
            var key = KeyCode.translate_event(e);
            current_keys.codes[key.code] = key.code;
            update_current_modifiers(key);
        },
        key_up: function (e) {
            var key = KeyCode.translate_event(e);
            delete current_keys.codes[key.code];
            update_current_modifiers(key);
        },
        is_down: function (key) {
            var code = key.code;
            if (code == KeyCode.CTRL) return current_keys.ctrl;
            if (code == KeyCode.ALT) return current_keys.alt;
            if (code == KeyCode.SHIFT) return current_keys.shift;

            return current_keys.codes[code] !== undefined && same_modifiers(key, current_keys);
        },
        hot_key: function (key) {
            var pieces = [];
            for (var i = 0; i < modifiers.length; ++i) {
                var modifier = modifiers[i];
                if (key[modifier] && modifier.toUpperCase() != key_names[key.code]) {
                    pieces.push(capitalize(modifier));
                }
            }
            var c = key.code;
            var key_name = key_names[c] || fn_name(c) || num_name(c) || String.fromCharCode(c);
            pieces.push(capitalize(key_name))
            return pieces.join('+');
        }
    };

    // Add key constants 
    for (var code in key_names) {
        KeyCode[key_names[code]] = code;
    }
})();

function accommodationPKG() {

    // private attribute
    var editorContent = {};
    var loaded = false;
    var enableHighlighterArea = false;
    var enableEraserArea = false;
    var checkedVals = new Array();
    var selectedVals = new Array();
    var ttsEnabled = true;

    // public method
    this.onloadEditor = function (json) {
        this.loaded = true;
        this.editorContent = json;
        this.enableHighlighterArea = false;
        this.enableEraserArea = false;
        //this.editorContent = getItemJson();
        var accomPkgScoreOb = accomPkgPossibleSc = 0;
        document.onkeydown = function (e) {
            /*defect fix 75505 starts*/
            //var dropAreas = $(".droparea"); 
            var removableDiv = $("body").children("[isDropped = true]");
            var removablePas = $("body").children(".dropped");
            var draggingDiv = $(".ui-draggable-dragging");
            var moveable = $('body').find(".ffText");
            /*defect fix 75505 ends*/

            e = e || window.event
            var k = KeyCode,
                arg = "",
                isShift = false,
                isCapslock = false,
                isCtrl = false;
            var key = k.hot_key(k.translate_event(e));
            arg = key;
            KeyCode.key_down(e);
            if (e.preventDefault) {
                e.preventDefault();
            }
            if (removableDiv.length == 0 && removablePas.length == 0 && draggingDiv.length == 0 && moveable.length == 0) {
                window.parmm = arg;
                enableHotKeys();
                return false;
            }

        };
        document.onmousemove = function (e) {
            hideTooltip();
            return true;
        };
        this.ttsEnabled = (($("#tts").val() == "true") ? true : false);

        this.enableTTS(this.ttsEnabled);
    }

    // private method
    var enableHotKeys = function () {
        var arg = window.parmm;
		if(arg == "Ctrl+S"){
			$(".text").each(function () {
				var topPos = "";
				if (this.scrollHeight > $(this).outerHeight(true)) {
					topPos = $(this).scrollTop();
					var parent = $(this).parent();
					var dom = $(this).clone();
					$(this).remove();
					$(parent).append(dom);
					$(dom).scrollTop(topPos);
					var ele, textFreeFlowComponents = $("#previewArea div[interactiontype='7']"),
						textRestrictedComponents = $("#previewArea div[interactiontype='6']");
					if (textRestrictedComponents.length > 0) {
						makeDrggable();
					}
					for (var index = 0; index < textFreeFlowComponents.length; index++) {
						ele = textFreeFlowComponents.eq(index).find(".text");
						makeFFDraggable(ele);
					}
				}
			});
		}
        window.parent.enableHotKeys(arg);
    }

    // private method
    var hideTooltip = function () {
        window.parent.hideTooltip();
    }

    this.releaseItem = function () {
        $("body").trigger("mouseup");
    }

    this.notifyChange = function () {
        // OAS method
        
		accomPkg.getState();
    }

    // public method
    this.isItemAnswered = function () {
        var screens = $("#previewArea > .editor"),
            screen, elements, isAnswered = false,
            screenId, elementId, scoring;
        for (var index = 0; index < screens.length; index++) {
            screen = screens.eq(index);
            elements = screen.find(".divbox");
            screenId = screen.attr("id");
            for (var index1 = 0; index1 < elements.length; index1++) {
                elementId = elements.eq(index1).attr("id");
                scoring = this.editorContent[screenId][elementId].property.scoring;

                if (scoring.complete) {
                    var radio = elements.eq(index1).find(":radio");
                    var checkbox = elements.eq(index1).find(":checkbox");
                    var dnd = elements.eq(index1).find("[behavior='3']");
                    if (radio.length > 0 || checkbox.length > 0 || dnd.length > 0) {
                        if (radio.length > 0 || checkbox.length > 0) {
                            isAnswered = isAnswered ||((radio.filter(":checked").length > 0) || (checkbox.filter(":checked").length > 0)) ? true : false;
                        }
                        if (dnd.length > 0) {
							isAnswered = isAnswered ||
								((dnd.find("[behavior='2']").length > 0) ||
								((dnd.find(".dropped").length > 0) &&
								((dnd.find(".answer")).length > 0))) ? true : false;
							
                        }
                    }
                }
            }
        }
        return isAnswered;
    }

    // public method
    this.isContentLoaded = function () {
        return this.loaded;
    }

    // public method
    this.getScore = function () {

        var scoring = [],
            scr, screens = $("#previewArea > .editor"),
            choice, dnd, index = 0,
            scoringMethod, getScore, scoreObtained = 0,
            possibleScore = 0;
        for (index = 0; index < screens.length; index++) {
            scr = screens.eq(index);
            var accomPkgScoreOb = accomPkgPossibleSc = 0;
            choice = scr.find("[interactiontype='2']");
            dnd = scr.find("[interactiontype='3']");
            choiceWithSeq = scr.find("[interactiontype='9']");
            dndWithSeq = scr.find("[interactiontype='5']");
            dndPassageRestricted = scr.find("[interactiontype='6']");
            dndPassageFreeflow = scr.find("[interactiontype='7']");
            dndMath = scr.find("[interactiontype='8']");
            subquestionIndex = 0;
            // choice scoring
            if (choice.length > 0) {
                getScore = mcqGetScore(choice, index, scr);
                scoring.push(getScore.scoring);
                itemId = getScore.itemID;
                scoreObtained += parseInt(getScore.ScoreObtained, 10);
                possibleScore += parseInt(getScore.PossibleScore, 10);
            }
            // dnd scoring
            if (dnd.length > 0) {
                getScore = dndGetScore(dnd, index, scr);
                scoring.push(getScore.scoring);
                itemId = getScore.itemID;
                scoreObtained += parseInt(getScore.ScoreObtained, 10);
                possibleScore += parseInt(getScore.PossibleScore, 10);
            }
            if (choiceWithSeq.length > 0) {
                getScore = mcqDndGetScore(choiceWithSeq, index, scr);
                scoring.push(getScore.scoring);
                itemId = getScore.itemID;
                scoreObtained += parseInt(getScore.ScoreObtained, 10);
                possibleScore += parseInt(getScore.PossibleScore, 10);
            }
            // dnd with sequence scoring
            if (dndWithSeq.length > 0) {
                getScore = dndSeqGetScore(dndWithSeq, index, scr);
                scoring.push(getScore.scoring);
                itemId = getScore.itemID;
                scoreObtained += parseInt(getScore.ScoreObtained, 10);
                possibleScore += parseInt(getScore.PossibleScore, 10);
            }
            // dndPassageRestricted scoring
            if (dndPassageRestricted.length > 0) {
                getScore = dndRestrictedGetScore(dndPassageRestricted, index, scr);
                scoring.push(getScore.scoring);
                itemId = getScore.itemID;
                scoreObtained += parseInt(getScore.ScoreObtained, 10);
                possibleScore += parseInt(getScore.PossibleScore, 10);
            }
            // dndPassageFreeflow scoring
            if (dndPassageFreeflow.length > 0) {
                getScore = dndFreeflowGetScore(dndPassageFreeflow, index, scr);
                scoring.push(getScore.scoring);
                itemId = getScore.itemID;
                scoreObtained += parseInt(getScore.ScoreObtained, 10);
                possibleScore += parseInt(getScore.PossibleScore, 10);
            }
            // dndMath scoring
            if (dndMath.length > 0) {
                getScore = dndMathGetScore(dndMath, index, scr);
                scoring.push(getScore.scoring);
                itemId = getScore.itemID;
                scoreObtained += parseInt(getScore.ScoreObtained, 10);
                possibleScore += parseInt(getScore.PossibleScore, 10);
            }
        }
		
		var highlighterStyles = gethighlightedRegion();
        return {
            "scoring": scoring,
            "itemId": itemId,
            "totalScoreObtained": String(Math.floor(scoreObtained)),
            "totalMaxScore": String(possibleScore),
            "highlights": highlighterStyles
        };
    }

    /*OAS-1647 - Validate if student has large font accomodation*/
    this.setVisualAccessFeatures = function (fontColor) {
        setColorFontAccomm(fontColor.hasFontMag);
    }



    // public method
    this.getState = function () {
        //return {
		var response = {
            jsonContent: this.getScore(),
            htmlContent: "",
            checkedVals: [], 
            selectedVals: [] 
        };
		parent.getResponse(response);
    }

    // public method
    this.setState = function (html, scoreJson, checkedVals, selectedVals, directionVal) {
        //html=getEncodedString(html);
        
        if (html != undefined) {
            json = this.editorContent;
            if (html == "") {
                selectedScreen = null;
                pos = {
                    "top": 100,
                    "left": 199
                };
				console.log("JSON start=" + Number(new Date()));
				activateControlsByJson(html, pos, json, selectedScreen, undefined, scoreJson);
			    console.log("JSON end=" + Number(new Date()));
            }
        }
       
    }

    this.getEncodedString = function (htmlData) {
        var returnData = "",
            charCode = "";
        for (var counter = 0; counter < htmlData.length; counter++) {
            if ((charCode = htmlData[counter].charCodeAt()) > 127) {
                returnData += "&#x" + charCode.toString(16) + ";";
            } else {
                returnData += htmlData[counter];
            }
        }
        return returnData;
    }

    // private method
    var activateStandAloneControls = function (prevHtml, json, checkedVals, selectedVals, accomPkg, directionVal) {
        var html = "",
            $scr, index = 0,
            $ele, top = 0,
            left = 0,
            pos = {
                "top": 111,
                "left": 199
            }, $previewHtml, selectVal;
        if (prevHtml != "") {
            if (directionVal != undefined) {
                prevHtml = prevHtml.replace(/{OASFrom}/gi, "");
                prevHtml = prevHtml.replace(/{OASTo}/gi, "");
                prevHtml = prevHtml.replace(/{OASCurrent}/gi, "");
            }
            $previewHtml = $(prevHtml).find("#editorArea").children();
            var screens = $previewHtml.length;

            if ($previewHtml.length == 0) {
                $previewHtml = $(prevHtml);
            } else {
                for (count = 0; count < screens; count++) {
                    $scr = $previewHtml.eq(count).children();
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
            }
        }
        this.editorContent = json;

        if (screens > 1) {
            for (index = 0; index < screens; index++) {
                html += "<button id='pgBtn_" + index + "'>" + (index + 1) + "</button>";
            }
            $("#pagination").html(html);
            $("#pagination :button").button();
            $("#paginationArea").show();
        } else {
            $("#paginationArea").hide();
        }

        //$previewHtml.find(".handle").remove();
        $previewHtml.find(".ui-resizable-handle").remove();

        $("body").html($previewHtml);
        //this.enableTTS(true);

        var passageFreeflow = $("#previewArea .editor").find("[interactiontype = 7]");
        if (passageFreeflow.length > 0) {
            var ele;
            if (accomPkg.enableHighlighterArea == false) {
                for (var index = 0; index < passageFreeflow.length; index++) {
                    ele = passageFreeflow.eq(index).find(".text");
                    makeFFDraggable(ele);
                }
            }
            makePassageDroppable();
        }

        makeDrggable();
        makeDroppable();
        addSequencing();
        //makeMathPalette();

        var txtVal, texts = $("#previewArea .text");
        for (var index = 0; index < texts.length; index++) {
            txtVal = texts.eq(index);
            if (txtVal.find(".correctAns").length == 0) {
                txtVal.removeClass("user-select");
                txtVal.addClass("no-user-select");
                txtVal.attr("onselectstart", "return false;");
            } else {
                txtVal.removeClass("no-user-select");
                txtVal.addClass("user-select");
                txtVal.removeAttr("onselectstart");
            }
        }
        $("[borderrequired='1']").addClass("no-border");
        //var matharea = $("[behavior='3'][matharea='2']");
        //matharea.css("overflow", "auto");

        $(":checkbox, :radio").attr("checked", false);
        if (checkedVals && checkedVals.length > 0) {
            for (index = 0; index < checkedVals.length; index++) {
                selectVal = checkedVals[index];
                $("#" + selectVal.screenId + " #" + selectVal.id + " :checkbox, #" + selectVal.screenId + " #" + selectVal.id + " :radio").attr("checked", true);
            }
        }
        if (selectedVals && selectedVals.length > 0) {
            for (index = 0; index < selectedVals.length; index++) {
                selectVal = selectedVals[index];
                $("#" + selectVal.screenId + " #" + selectVal.id + " select").val(selectVal.val);
            }
        }
        var dropAreas = $(".droparea"); //, scaleX = Number($("#scaleX").val()), scaleY = Number($("#scaleY").val());
        //dropAreas.droppable("option","scaleX",scaleX);
        //dropAreas.droppable("option","scaleY",scaleY);
        for (index = 0; index < dropAreas.length; index++) {
            makeDropAreaSortable(dropAreas.eq(index));
        }
        $(".ui-draggable-dragging").remove();
        var imageList = $("img");
        for (index = 0; index < imageList.length; index++) {
            imageList.eq(index).attr("onmousedown", " event.preventDefault();");
        }
        $(".palette-button[isdropped]").draggable("destroy");
        //accomPkg.enableTTS(accomPkg.ttsEnabled);
        accomPkg.enableEraser(false); //defect 
		accomPkg.enableHighlighter(false);
    }

    // private method

    var activateControlsByJson = function ($previewHtml, pos, editorContent, selectedScreen, isFromOutside, scoreJson) {
        var html = "",
            $scr, index = 0,
            $ele, top = 0,
            left = 0,
            screens;
        $("#previewArea").css("background-color", "rgb(255, 255, 255)");
        $("#displayScoreDialog").dialog("close");
        $("#propertyDialog").dialog("close");
        editorContentJSON = editorContent;
        makeCustomButtons();
        replaceCustomRadioButton();

        /* re-create the html response through JSON*/
        recreatePreviewarea(scoreJson);

        /* adding handle div so that the dropped elements can be dragged out*/
        var $draggables = $("[isdropped=true]");
        addHandleDiv($draggables);

        var passageFreeflow = $("#previewArea .editor").find("[interactiontype = 7]");
        if (passageFreeflow.length > 0) {
            makePassageDroppable();
        }
        makeDrggable();
        makeDroppable();
        addSequencing();
        setZIndexValue();
        blockingUserSelection();

        $("[borderrequired='1']").addClass("no-border");

        var matharea = $("[behavior='3'][matharea='2']");
        var controls = $("#previewArea .element:not([data-role='mathpalette'])");
        for (var index = 0; index < controls.length; index++) {
            adjustControlHeightWidth(controls.eq(index));
            setPreviewControlHeightWidth(controls.eq(index));

        }
        var dropAreas = $(".droparea");
        for (index = 0; index < dropAreas.length; index++) {
            makeDropAreaSortable(dropAreas.eq(index));
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
		accomPkg.enableHighlighter(false); //defect 
		accomPkg.enableEraser(false); //defect 
		checkMCQResp();
    }

    // public method
    this.enableHighlighter = function (isEnabled) {
        this.enableHighlighterArea = isEnabled;
        if (isEnabled) {
            $(document).data("active", true);
            blokFreeFlowTextSelection();
            var passageFreeflow = $("#previewArea .editor").find("[interactiontype = 7]");
            if (passageFreeflow.length > 0) {
                clearSelection();
            }
			 /*makes the html draggable if the rendering environment is ipad*/
				if (isPad()) {
					$("body").draggable();
					}
            enabledHighlighterBox();
        } else {
			if (isPad()) {
				$("body").draggable("destroy");			      
			}
            $(document).data("active", false);
            unblokFreeFlowTextSelection();
			
        }
		
    }

    this.setFocusItem = function () {
        $("body").trigger("click");
    }

    var enabledHighlighterBox = function () {
        var x1, y1;
        var dirUp = "";
        var prevX = "";
        var prevY = "";
        var selectStart = false;
        var dirDown = "";
        var isScrollableDivY = false;
        var initialScrollHeight = 0;
        var initialScrollWidth = 0;
        var textEle = "";
        var mouseOnUnwantedEle = false,
            dataRole;
        $(document).on("mousedown", function (e) {
            dataRole = $(e.target.parentNode).attr("data-role");
            if (dataRole == "droparea" || dataRole == "checkbox" || dataRole == "radio") {
                mouseOnUnwantedEle = true;
            } else {
                mouseOnUnwantedEle = false;
                if ($(document).data("active")) {
                    $("#current").attr({
                        id: ''
                    });
                    var box = $('<div style="pointer-events:none;background:yellow;position:absolute;opacity:0.3;z-index:99999;" class="highlighter"></div>').hide(); //defect #75456
                    x1 = e.pageX;
                    y1 = e.pageY;

                    var text = $(document.elementFromPoint(x1, y1));
                    if ($(text).is("div.text")) {
                        textEle = text;
						
                    } else if ($(text).is("div.textarea")) {
                        textEle = text.find("div.text").eq(0);
						
                    } else {
                        textEle = text.parents(".text").eq(0);
						
                    }
                    selectStart = true;
					if (textEle && textEle.length > 0 && textEle[0].scrollHeight > textEle.outerHeight(true) + 5) {
						isScrollableDivY = true;
                        prevX = "NA";
                        prevY = "NA";
                        initialScrollHeight = 25;//padding adjustments
                        initialScrollWidth = 0;
						
						//Defect 80253 fix
                        $(textEle).children().each(function () {
							if(!$(this).hasClass("highlighter"))
							{
							initialScrollHeight += $(this).outerHeight();
							
							
							
							
                            initialScrollWidth = $(this).outerWidth() > initialScrollWidth ? $(this).outerWidth() : initialScrollWidth;
							}
							
                        });
						initialScrollWidth += 2;//padding adjustments
						var retObj = scrollableDivMouseDown(e, x1, y1, textEle);
						x1 = retObj.x1;
                        y1 = retObj.y1;
                    } else {
                        $(document.body).append(box);
                        box.attr({
                            id: 'current'
                        }).css({
                            top: e.pageY,
                            left: e.pageX
                        }).fadeIn();
                    }
                }
            }
			/*var radio = $(".radio-button").within(e.pageX, e.pageY); 
			if(radio.length < 0){
				e.originalEvent.preventDefault();
			}*/
        });
        if (!mouseOnUnwantedEle) {
            $(document).on("mousemove", function (e) {
                if ($(document).data("active")) {
					
                    $("#current").css("border", "2px solid");
					if (isScrollableDivY) {
						var retObj = scrollableDivMouseMove(e, x1, y1, dirUp, prevX, prevY, selectStart, dirDown, initialScrollHeight, initialScrollWidth);
						dirUp = retObj.dirUp;
                        prevX = retObj.prevX;
                        prevY = retObj.prevY;
                        selectStart = retObj.selectStart;
                        isScrollableDivY = true;
                        dirDown = retObj.dirDown;
						
					} else {

                        if (selectStart && !(isInsideScrollableDiv(x1, y1, e.pageX, e.pageY))) {
						  if (x1 > e.pageX && y1 > e.pageY) {
							$("#current").css({
                                    width: Math.abs(e.pageX - x1),
                                    height: Math.abs(e.pageY - y1),
                                    top: e.pageY,
                                    left: e.pageX
                                }).fadeIn();
							} else if (x1 > e.pageX && y1 < e.pageY) {
							$("#current").css({
                                    width: Math.abs(e.pageX - x1),
                                    height: Math.abs(e.pageY - y1),
                                    top: y1,
                                    left: e.pageX
                                }).fadeIn();
							} else if (x1 < e.pageX && y1 > e.pageY) {
							$("#current").css({
                                    width: Math.abs(e.pageX - x1),
                                    height: Math.abs(e.pageY - y1),
                                    top: e.pageY,
                                    left: x1
                                }).fadeIn();
							} else {
							$("#current").css({
                                    width: Math.abs(e.pageX - x1),
                                    height: Math.abs(e.pageY - y1)
                                }).fadeIn();
							}

                        } else {
						//indicating highlighter has entered in the scrollable div atleast once
                            selectStart = false;
                        }

                    }
                }
            });

            $(document).on("mouseup", function (e) {
                if ($(document).data("active")) {
                    //sendNotification();
                    selectStart = false;
					if (isScrollableDivY) {
					removeOrReduceHighlighterBox(initialScrollHeight-6, initialScrollWidth-3);
					scrollableDivMouseUp(e);
						isScrollableDivY = false;
                    } else {
					var pos = $("#current").position();
                        $("#current").attr({
                            id: ''
                        });
                        $(".highlighter").css("border", "0px");
                        $(".highlighter").css("background", "yellow");
                    }
					sendNotification();
                }
            });
        }
    }

        function setPosition(newParent, event, newBox, scalingFactorY, scalingFactorX) {
            var newHeight = newBox.height() * scalingFactorY;
            var newWidth = newBox.width() * scalingFactorX;
            var top = (event.clientY - newParent.offset().top) * scalingFactorY + newParent.scrollTop() - newHeight;
            var left = (event.clientX - newParent.offset().left) * scalingFactorX + newParent.scrollLeft() - newWidth;

            $(newBox).css({
                top: top,
                left: left,
                width: newWidth,
                height: newHeight
            });

        }

        function areaScrollWithHighlighter(obj) {
            var ele, divs = $(".text"),
                highlighterBox, high, $this, boxPos, highPos, scrollPos = 0,
                highTop = 0;
            var boxX1 = 0,
                boxY1 = 0,
                boxX2 = 0,
                boxY2 = 0,
                hX1 = 0,
                hX2 = 0,
                hY1 = 0,
                hY2 = 0,
                text;
            for (var index = 0; index < divs.length; index++) {
                ele = divs.eq(index);
                if (ele[0].scrollHeight > ele.outerHeight(true) + 5) {

                    $this = ele.parent();
                    boxPos = $this.position();
                    boxX1 = boxPos.left;
                    boxX2 = boxX1 + $this.width();
                    boxY1 = boxPos.top;
                    boxY2 = boxY1 + $this.height();

                    highlighterBox = $(".highlighter");
                    for (var i = 0; i < highlighterBox.length; i++) {
                        high = highlighterBox.eq(i);
                        highPos = high.position();
                        hX1 = highPos.left;
                        hX2 = hX1 + high.width();
                        hY1 = highPos.top;
                        hY2 = hY1 + high.height();

                        if (hX1 > boxX1 && hX2 < boxX2 && hY1 > boxY1 && hY2 < boxY2) {
                            text = $this.find(".text");
                            scrollPos = text.scrollTop();
                            highTop = parseInt(high.css("top").split("px")[0], 10);
                            var scrollHeight = text[0].scrollHeight;
                            var clone = high.clone(true);
                            clone.css({
                                "position": "relative",
                                "top": (highTop - scrollHeight * 0.5) + "px"
                            });
                            text.append(clone);
                            high.remove();
                        }
                    }
                }
            }
        }

        // public method
    this.enableEraser = function (isEnabled) {
        this.enableEraserArea = isEnabled;
        $(document).data("active", false);
        if (isEnabled) {
            //sendNotification();
            $(".highlighter").css("pointer-events", "auto");
            $(".highlighter").on("mousedown", function (e) { //defect #73261
                $(this).remove();
                e.preventDefault();
				sendNotification();
            });
            var passageFreeflow = $("#previewArea .editor").find("[interactiontype = 7]");
            if (passageFreeflow.length > 0) {
                clearSelection();
            }
            this.removeHighlighterCursor();
            blokFreeFlowTextSelection();
			//sendNotification();
        } else {
            $(".highlighter").off("click");
            $(".highlighter").css("pointer-events", "none");
            unblokFreeFlowTextSelection();
			//sendNotification();
        }
    }

    // public method
    this.enableTTS = function (ttsEnabled) {
        this.enableTTSSpeech = ttsEnabled;
        $(".handle, .text").die("click tap").live("click tap", function (evt) {
			getReadAloudText($(this));
        });
        if (ttsEnabled) {
            $("[suppresstts='1']").css("cursor", "pointer");
        } else {
            $("[suppresstts='1']").css("cursor", "default");
            this.enableTTSSpeech = true;
        }
    }

    // private method
    var getReadAloudText = function (obj) {
		if ($("#tts").val() == "true" && $(obj).parent().attr("suppresstts") == "1") {
			var title = $(obj).parent().attr("title-data"),
                textContent = $(obj).parent().children(".text").text();
			var readAloudText = (($.trim(title) == "") ? $.trim(textContent) : $.trim(title));
			//var readAloudText =$.trim($(obj).parent().attr("title-data"));
            if (readAloudText && readAloudText != "") {
				window.parent.setTTSText(readAloudText);
				// call OAS exposed function
            }
        }
    }

    // public method
    this.setHighlighterCursor = function (cursorType) {
        var cursor = "";
        if (cursorType.search(".") == -1) {
            cursor = cursorType;
        } else {
            cursor = "url('" + cursorType + "'), auto";
        }
        $("body").css("cursor", cursor);
        $("body").draggable("option", "cursor", cursor);
		//$("[suppresstts='1']").css("cursor",cursor);
        $(".element").css("cursor", cursor);
        $("[behavior='2'].textarea").css("cursor", "move");
        $("[behavior='3'].droparea").css("cursor", "default");
    }

    // public method
    this.removeHighlighterCursor = function () {
        $("body").css("cursor", "");
        var getCursor = $("#tts").val();
        accomPkg.setCursor(getCursor);
        $("[behavior='2'].textarea").css("cursor", "move");
    }

    // public method
    this.setEraserCursor = function (cursorType) {
        var cursor = "";
        if (cursorType.search(".") == -1) {
            cursor = cursorType;
        } else {
            cursor = "url('" + cursorType + "'), auto";
        }
        $(".highlighter").css("cursor", cursor);
    }

    // public method
    this.setCursor = function (cursorType) {
        var cursor = "";
        if (cursorType == "true" || cursorType == true) {
            $("[suppresstts='1']").css("cursor", "pointer");
            $("[suppresstts='2']").css("cursor", "default");
            $(".divbox").css("cursor", "default");
        } else {
            $("[suppresstts='1']").css("cursor", "default");
            $("[suppresstts='2']").css("cursor", "default");
            $(".divbox").css("cursor", "default");
        }
    }
}

function scrollableDivMouseDown(e, x1, y1, targetParentObject) {
    var x1, y1;
    var isScrollableDivY = "";
    /*OAS-1647 - Apply scaling while highlighting*/
    var transX, transY;
    if(localStorage.getItem("hasFontMag") != undefined){
        if(localStorage.getItem("hasFontMag") == 'true' || localStorage.getItem("hasFontMag") == true){
    		transX = 1 / 1.4;
        	transY = 1 / 1.4;
    	}else{
    		transX = 1 / $("#scaleX").val();
        	transY = 1 / $("#scaleY").val();
    	}
    }else{
    	// do nothing
    }	    
    $("#current").attr({
        id: ''
    })
    var box = $('<div id=\'current\' style="background:yellow;position:absolute;opacity:0.3;z-index:99999;" class="highlighter"></div>').hide();
    $(targetParentObject).append(box);
    $(targetParentObject).css("position", "absolute");
    $(targetParentObject).attr('mainParent', 'true');
    $(box).css("pointer-events", "none");
    $(box).css("pointer-events", "none");

    prevX = "NA";
    prevY = "NA";
    if ($(targetParentObject).offset() != null) {
	    var actualParentOffsetTop = $(targetParentObject).offset().top * transY;
        var actualParentOffsetLeft = $(targetParentObject).offset().left * transX;
        x1 = (e.clientX * transX) - actualParentOffsetLeft + $(targetParentObject).scrollLeft();
        y1 = (e.clientY * transY) - actualParentOffsetTop + $(targetParentObject).scrollTop();
        box.css({
            top: y1,
            left: x1
        });
        return {
            "x1": x1,
            "y1": y1
        };
	}
}

function scrollableDivMouseMove(event, x1, y1, dirUp, prevX, prevY, selectStart, dirDown, initialScrollHeight, initialScrollWidth) {
    var transX, transY;
    var targetObj = "";
    /*OAS-1647 - Apply scaling while highlighting*/
    if(localStorage.getItem("hasFontMag") != undefined){
        if(localStorage.getItem("hasFontMag") == 'true' || localStorage.getItem("hasFontMag") == true){
    		transX = 1 / 1.4;
        	transY = 1 / 1.4;
    	}else{
    		transX = 1 / $("#scaleX").val();
        	transY = 1 / $("#scaleY").val();
    	}
    }else{
    	// do nothing
    }
	if ($(event.target).is("div.text") || $(event.target).is("div.textarea")) {
        targetObj = $(event.target);
    } else if ($(event.target).parents("div.text").length > 0) {
        targetObj = $(event.target).parents("div.text")[0];
    } else if ($(event.target).parents("div.divbox").length > 0) {
        targetObj = $(event.target).parents("div.divbox")[0];
    }
    $("#current").css("border", "2px solid");

    if ($(targetObj).offset() != null) {
		var scrollTop = $(targetObj).is(".text")? $(targetObj).scrollTop():$(targetObj).find("div.text").eq(0).scrollTop();
        var actualParentOffsetTop = $(targetObj).offset().top * transY;
        var actualParentOffsetLeft = $(targetObj).offset().left * transX;
        var currentTop = (event.clientY * transY) - actualParentOffsetTop + scrollTop;
        var currentLeft = (event.clientX * transX) - actualParentOffsetLeft + $(targetObj).scrollLeft();
        var targetObjHeight = $(targetObj).height();
        var targetObjWidth = $(targetObj).width();
		
		
        var width = 0;
        var height = 0;
        if (prevY == "NA") {
            dirUp = y1 > currentTop;
            dirDown = y1 < currentTop;
        } else {
            dirUp = currentTop < prevY;
            dirDown = currentTop > prevY;
        }
		var isInsideMainparent = false;
		if($(targetObj).find(":first-child").attr('mainParent') || $(targetObj).attr('mainParent')){
			isInsideMainparent = true;
		}
		
		//Checking if the cursor is in same container or not.					
        if (!isInsideMainparent || (initialScrollHeight  < currentTop) || (initialScrollWidth  < currentLeft)) {
            selectStart = false;
			removeOrReduceHighlighterBox(initialScrollHeight-6, initialScrollWidth-6);
        }
        if (selectStart) {
            var scrollHeightAdjust = 0;
            if ((initialScrollHeight > targetObjHeight +scrollTop) && currentTop > ( targetObjHeight +scrollTop) - 10 && dirDown) {
                var scrollableHeight = initialScrollHeight -scrollTop-targetObjHeight;
                if (scrollableHeight > 10) {
                    $(targetObj).scrollTop(scrollTop + 5);
                    scrollHeightAdjust = 5;
                   
                    
                } else {
                    $(targetObj).scrollTop(scrollTop + scrollableHeight);
                    scrollHeightAdjust = scrollableHeight;
                }
            } else if (currentTop -scrollTop - 10 < 0 && dirUp && y1> currentTop) {
                var scrollableHeight =scrollTop;
                if (scrollableHeight > 10) {
                    $(targetObj).scrollTop(scrollTop - 5);
                    scrollHeightAdjust = -5;
                } else {
                    $(targetObj).scrollTop(scrollTop - scrollableHeight);
                    scrollHeightAdjust = -scrollableHeight;
                }
            }
            if (x1 > currentLeft && y1 > currentTop) {
                width = Math.abs(currentLeft - x1);
                height = Math.abs(currentTop - y1);
				if($("#current").css("display")=="none" && width > 2 && height>2){
					$("#current").remove();
					selectStart = false;
				}else{
					$("#current").css({
                    width: width,
                    height: height,
                    top: currentTop + scrollHeightAdjust,
                    left: currentLeft
					}).fadeIn();
				}
                
            } else if (x1 > currentLeft && y1 < currentTop) {
				width = Math.abs(currentLeft - x1);
                height = Math.abs(currentTop - y1)+scrollHeightAdjust;
				if($("#current").css("display")=="none" && width > 2 && height>2){
					$("#current").remove();
					selectStart = false;
				}else{
					$("#current").css({
                    width: width,
                    height: height,
                    top: y1 ,
                    left: currentLeft
					}).fadeIn();
				}
                
            } else if (x1 < currentLeft && y1 > currentTop) {
                width = Math.abs(currentLeft - x1);
                height = Math.abs(currentTop - y1);
                $("#current").css({
                    width: width,
                    height: height,
                    top: currentTop + scrollHeightAdjust,
                    left: x1
                }).fadeIn();
            } else {
                width = Math.abs(currentLeft - x1);
                height = Math.abs(currentTop - y1)+scrollHeightAdjust ;
                $("#current").css({
                    width: width,
                    height: height
                }).fadeIn();
            }
        }
        prevY = currentTop;
        prevX = currentLeft;
    }
    return {
        "dirUp": dirUp,
        "dirDown": dirDown,
        "prevX": prevX,
        "prevY": prevY,
        "selectStart": selectStart
    };
}

function scrollableDivMouseUp(e) {
    var targetParentObject = '';
    if ($(e.target).is("div.text") || $(e.target).is("div.divbox")) {
        targetParentObject = $(e.target);
    } else if ($(e.target).parents("div.text").length > 0) {
        targetParentObject = $(e.target).parents("div.text")[0];
    } else if ($(e.target).parents("div.divbox").length > 0) {
        targetParentObject = $(e.target).parents("div.divbox")[0];
    }
    if ($(document).data("active")) {
        $(targetParentObject).removeAttr('mainParent');
        $("#current").attr({
            id: ''
        });
        $(".highlighter").css("border", "0px");
        $(".highlighter").css("background", "yellow");
    }
}

function recreatePreviewarea(scoreJson) {
    var interactionType, scoringJ = scoreJson.scoring,
        scoringChild, answered, ele, dragElement, dragEleId, dragEle;
    for (scoringJCount = 0; scoringJCount < scoringJ.length; scoringJCount++) {
        scoringChild = scoringJ[scoringJCount];
        if (scoringChild.length > 0) {
            for (scoringChildCount = 0; scoringChildCount < scoringChild.length; scoringChildCount++) {
                answered = scoringChild[scoringChildCount].answered;
                screenId = parseInt(scoringChild[scoringChildCount].screenName.split(" ")[1]) - 1;
                interactionType = scoringChild[scoringChildCount].interactionType;
                if (answered != undefined && answered.length > 0) {
                    if (interactionType == "MCQ") {
                        for (answeredCount = 0; answeredCount < answered.length; answeredCount++) {
                            eleId = answered[answeredCount].id;
                            ele = $("#previewArea #editor_" + screenId + " #" + eleId).find(":input");
                            if (ele.attr("type") == "radio") {
                                ele.attr("checked", true);
                                ele.next(".radio-button").addClass("checkedRadio");

                            } else if (ele.attr("type") == "checkbox") {
                                ele.attr("checked", true);
                            }
                        }
                    } else {
                        for (answeredCount = 0; answeredCount < answered.length; answeredCount++) {
                            dropEleId = answered[answeredCount].droparea.id;
                            dragEle = answered[answeredCount].dragarea;
                            for (dragEleCount = 0; dragEleCount < dragEle.length; dragEleCount++) {
                                if (interactionType == "Text Restricted" || interactionType == "Text Freeflow") {
                                    dragEleId = dragEle[dragEleCount].htmlresponse;
                                    $("#previewArea #" + dropEleId).find(".text").before($(dragEleId));
                                    setDropAreaScrollbar($("#previewArea #" + dropEleId));
                                } else if (interactionType == "Drag drop" || interactionType == "MCQ with sequencing" || interactionType == "Drag-drop with sequencing") {
                                    dragEleId = dragEle[dragEleCount].id;
                                    prepareDNDElements($("#" + dragEleId), $("#" + dropEleId));
                                } else if (interactionType == "DND with Math Palette") {
                                    if (dragEle[dragEleCount].id != "") {
                                        dragEleId = dragEle[dragEleCount].id.split(",");
                                        for (dragEleIDCount = 0; dragEleIDCount < dragEleId.length; dragEleIDCount++) {
                                            dragElement = dragEleId[dragEleIDCount];
											/*var attempt = 1;
											if($("#" + dropEleId).attr("droptired")){
												attempt = parseInt($("#" + dropEleId).attr("droptired")) + 1;
											}
											$("#" + dropEleId).attr("droptired",attempt);*/
                                            prepareDNDElements($("#" + dragElement), $("#" + dropEleId));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    highlightItems(scoreJson);
}

function isInsideScrollableDiv(startPosX, startPosY, currentPosX, currentPosY) {
    var testResult = false;
    var tempX = tempY = "";
    var text = "";
    for (counter = 0; counter < 3; counter++) {
        switch (counter) {
        case 0:
            tempX = currentPosX;
            tempY = currentPosY;
            break;
        case 1:
            tempX = currentPosX;
            tempY = startPosY;
            break;
        case 2:
            tempX = startPosX;
            tempY = currentPosY;
            break;
        default:

        }
        text = $(document.elementFromPoint(tempX, tempY));
        var curEle = "";
        if ($(text).is("div.text")) {
            curEle = text;
        } else if ($(text).is("div.textarea")) {
            curEle = text.find("div.text").eq(0);
        } else {
            curEle = text.parents(".text").eq(0);
        }
        if (curEle && curEle.length > 0 && curEle[0].scrollHeight > curEle.outerHeight(true) + 5) {
            testResult = true;
            break;
        }



    }
    return testResult;
}

function clearSelection() {
    if (window.getSelection) {
        window.getSelection().removeAllRanges();
    } else if (document.selection) {
        document.selection.empty();
    }
}

function gethighlightedRegion() {
    var highlighter = $("body").find(".highlighter");
    var styleArr = new Array(),
        styleObj = {}, ht = wd = lt = tp = 0;
    for (var hCount = 0; hCount < highlighter.length; hCount++) {
        highlighterObj = $(highlighter[hCount]);
        ht = highlighterObj.css("height");
        wd = highlighterObj.css("width");
        lt = highlighterObj.css("left");
        tp = highlighterObj.css("top");
        highlighterObj.parent().is("body") ? ct = "body" : ct = highlighterObj.parents(".element").attr("id");
        styleObj = {
            "height": ht,
            "width": wd,
            "left": lt,
            "top": tp,
            "container": ct
        }
        styleArr.push(styleObj);
    }
    return styleArr;
}

function highlightItems(json) {
    var highlighterDiv;
    for (var hDivCount = 0; hDivCount < json.highlights.length; hDivCount++) {
        highlighterObj = json.highlights[hDivCount];
        highlighterDiv = "<div class='highlighter' style='pointer-events: none; background: none repeat scroll 0% 0% yellow; position: absolute; opacity: 0.3; z-index: 99999; display: block; top: " + highlighterObj.top + "; left: " + highlighterObj.left + "; border: 0px none; width: " + highlighterObj.width + "; height: " + highlighterObj.height + ";' id=''></div>";
        if (highlighterObj.container == "body") {
            $("body").append(highlighterDiv);
        } else {
            var targetParentObject = $("#" + highlighterObj.container).find(".text");
            $(targetParentObject).append(highlighterDiv);
            $(targetParentObject).css("position", "absolute");
            $(targetParentObject).attr('mainParent', 'true');
        }
    }
}
function removeOrReduceHighlighterBox(initialScrollHeight, initialScrollWidth){
	var highlighterDiv = $("#current");
	if(highlighterDiv.length > 0) {
	//fix for 79597
		var allHighlighters = highlighterDiv.parent(".text").find("div.highlighter").each(function(){
		var width = $(this).width();
		var height = $(this).height();
		var strLeft = $(this).css("left");
		var strTop = $(this).css("top");
		var left = "";
		var top = "";
		if(strLeft && strTop){
			left = parseInt(strLeft.substring(0,strLeft.indexOf("px")));
			top = parseInt(strTop.substring(0,strTop.indexOf("px")));

		}
		if(initialScrollWidth<=left){
			$(this).remove();
		}else if(initialScrollHeight <=top){
			$(this).remove();
		}else if(left+width>initialScrollWidth){
			var derivedWidth = initialScrollWidth -left;
			$(this).css("width",derivedWidth+"px");
			
		}else if(top+height>initialScrollHeight){
			var derivedHeight = initialScrollHeight -top;
			$(this).css("height",derivedHeight+"px");
			
		}
		});
	}
	
}