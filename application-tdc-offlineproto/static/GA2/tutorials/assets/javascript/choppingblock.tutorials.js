
var choppingblock = {}

choppingblock.Tutorial = function(data){
	
	this.data = data;
	
	/*
	*	Build HTML for Lightbox
	*/
	
	this.$lightboxHTML = $(document.createElement("div"));
	
	$(document.createElement("h2")).addClass("tutorial-heading").text("Testing Tool-Tips").appendTo(this.$lightboxHTML);
	$(document.createElement("h1")).addClass("tutorial-title").text(this.data.toolName).appendTo(this.$lightboxHTML);
	
	var stepsDiv = $(document.createElement("div")).addClass("swiper-container");
	
	
	var leftArrow = $(document.createElement("a")).addClass("left-arrow").appendTo(this.$lightboxHTML);
	var rightArrow = $(document.createElement("a")).addClass("right-arrow").appendTo(this.$lightboxHTML);	
	
	var stepsWrapper = $(document.createElement("div")).addClass("swiper-wrapper")
	$.each(this.data.tutorialSteps, function(index,value){
		
		var step = $(document.createElement("div"));
		step.addClass("tutorial-step swiper-slide");
				
		$(document.createElement("img")).attr("src", value.imageUrl).attr("alt", value.content).appendTo(step);
		$(document.createElement("h3")).addClass("tutorial-step-title").text(value.title).appendTo(step);
		$(document.createElement("p")).addClass("tutorial-step-description").text(value.content).appendTo(step);

		step.appendTo(stepsWrapper);

	});


	stepsWrapper.appendTo(stepsDiv);

	this.$lightboxHTML.append(stepsDiv);



	var paginationContainer = $(document.createElement("div")).addClass("tutorial-pagination").appendTo(this.$lightboxHTML);

	for (var i = 0; i < this.data.tutorialSteps.length; i++) {
		$(document.createElement("span")).addClass("tutorial-pagination-mark").appendTo(paginationContainer);
	}



	this.buildButton = function(container){


		var content = this.data.toolName;
		this.$buttonEl = $(document.createElement("a")).addClass("tool-tip-button").attr("title", content).css("background-image", "url("+ this.data.toolLinkImage +")");


		var lightboxHTML = this.$lightboxHTML;
		var that = this;
		this.$buttonEl.appendTo(container);
		this.$buttonEl.on("click", function(){

			//Open Lightbox
			new choppingblock.Lightbox(lightboxHTML, "tutorials", function(){


			    that.swiper = new Swiper('.swiper-container',{
			      mode:'horizontal',
				  calculateHeight: true,
				  initialSlide: 0,
				  pagination: ".tutorial-pagination",
				  paginationClickable: true,
			      loop: false,
				  onInit: function(swiper){
				  	swiper.swipeTo(0, 0);
				  }

			    });


				$(".left-arrow").on("click", function(){

					that.swiper.swipePrev();
				})

				$(".right-arrow").on("click", function(){

					that.swiper.swipeNext();
				})


			}, function(){
				that.swiper.destroy(true);
			});
		})
	}



}


/*
* Build a modal lightbox.
*
*/
choppingblock.Lightbox = function(content, cssPrefix, openCallback, closeCallback){


	// Build modal background div
	var modalBackground = $(document.createElement("div")).addClass(cssPrefix + "-background").appendTo("body");

	$("html").addClass("stop-scroll");

	// Build dialog
	var dialog = $(document.createElement("div")).addClass(cssPrefix + "-dialog");

	// Center on screen based on dialog content
	var setDialogDimensions = function(){

		//Calculate Left
		var windowWidth = $(window).width();
		var dialogWidth = dialog.outerWidth();
		var dialogLeft = windowWidth/2-dialogWidth/2;


		//Calculate Top
		var dialogTop;
		var windowHeight = $(window).height();
		var dialogHeight = dialog.outerHeight();

		if( dialogHeight < windowHeight){

			dialogTop = $(window).height()/2-dialogHeight/2;

		}else{

			dialogTop = 0;

		}


		dialog.css({
			top: dialogTop,
			left: dialogLeft

		});


	}


	$(document.createElement("div")).addClass(cssPrefix + "-close-button").appendTo(dialog).text("close").click(function(){
		$("html").removeClass("stop-scroll");
		$(this).parents("." + cssPrefix + "-dialog").remove();
		closeCallback();
		modalBackground.remove();
	});

	dialog.append(content);


	$("body").append(dialog);


	// Run callback for any setup inside lightbox
	openCallback();

	setDialogDimensions();


	$(window).on("resize", function(){
		setDialogDimensions();
	});

}

/*
*	This is the information for populating the tutorials
*   The links on the page are automatically built with this information
*   To add another, just add another object to the array with the correct information
*/
var tutorialInformation = [
		{
			toolName: "Highlighter Tool",
			toolLinkImage: "./tutorials/content/highlight-btn-sprite.png",
			tutorialSteps: [
				{
					title: "The Highlighter Tool",
					imageUrl: "./tutorials/content/highlight-step01.png",
					content: "Use the highlighter tool to highlight some text."
				},
				{
					title: "Step 1: Turn on the Highlighter",
					imageUrl: "./tutorials/content/highlight-step02.png",
					content: "Click the highlighter tool in the tool bar."
				},
				{
					title: "Step 2: Using the Highlighter",
					imageUrl: "./tutorials/content/highlight-step03.png",
					content: "Click and drag the highlighter tool over the text that you would like to highlight."
				},
				{
					title: "Step 3: Turn on the Eraser",
					imageUrl: "./tutorials/content/highlight-step04.png",
					content: "To remove the highlight from the text, first click the eraser tool in the tool bar."
				},
				{
					title: "Step 4: Removing Text Highlights",
					imageUrl: "./tutorials/content/highlight-step05.png",
					content: "Click the highlighted area with the eraser tool to remove the highlight."
				},
				{
					title: "Step 5: Turn Off Eraser Tool",
					imageUrl: "./tutorials/content/highlight-step06.png",
					content: "When finished removing highlights, click the eraser tool again to turn off the eraser and continue the current task."
				}

			]
		},
    /*
		{
			toolName: "Magnifier Tool",
			toolLinkImage: "./tutorials/content/magnify-btn-sprite.png",
			tutorialSteps: [
				{
					title: "The Magnifier Tool",
					imageUrl: "./tutorials/content/magnify-step01.png",
					content: "The magnifier tool is used to enlarge portions of a text passage."
				},
				{
					title: "Step 1: Activate the Magnifier",
					imageUrl: "./tutorials/content/magnify-step02.png",
					content: "Click the magnifier tool in the tool bar."
				},
				{
					title: "Step 2: Position the Magnifier Frame",
					imageUrl: "./tutorials/content/magnify-step03.png",
					content: "Use the move cursor to click and drag the magnifier frame over the passage of text that you would like to read."
				},
				{
					title: "Step 3: Using the Magnifier Frame",
					imageUrl: "./tutorials/content/magnify-step04.png",
					content: "Slowly move the magnifier frame along the passage of text that you would like to read."
				},
				{
					title: "Step 4: Removing the Magnifier Frame",
					imageUrl: "./tutorials/content/magnify-step05.png",
					content: "To disable the magnifier frame, click the magnifier tool in the tool bar."
				}

			]
		},
		{
			toolName: "Calculator Tool",
			toolLinkImage: "./tutorials/content/calculator-btn-sprite.png",
			tutorialSteps: [
				{
					title: "The Calculator Tool",
					imageUrl: "./tutorials/content/calc-step01.png",
					content: "The calculator tools help you perform mathematical operations."
				},
				{
					title: "Step 1: Activate the Calculator",
					imageUrl: "./tutorials/content/calc-step02.png",
					content: "Click the calculator tool in the tool bar."
				},
				{
					title: "Step 2: Position the Calculator",
					imageUrl: "./tutorials/content/calc-step03.png",
					content: "Use the move cursor to position your calculator by clicking and dragging the calculator header."
				},
				{
					title: "Step 3: Using the Calculator",
					imageUrl: "./tutorials/content/calc-step04.png",
					content: "Use your cursor to interact directly with the calculator buttons."
				},
				{
					title: "Step 4: Removing the Calculator",
					imageUrl: "./tutorials/content/calc-step05.png",
					content: "To close the calculator, click the close button in the calculator header."
				}

			]
		},
		*/
		{
			toolName: "Blocking Ruler",
			toolLinkImage: "./tutorials/content/blockruler-btn-sprite.png",
			tutorialSteps: [
				{
					title: "The Blocking Ruler",
					imageUrl: "./tutorials/content/block-step01.png",
					content: "The blocking ruler can help you to read one line of text at a time by hiding what comes next."
				},
				{
					title: "Step 1: Turn on the Blocking Ruler",
					imageUrl: "./tutorials/content/block-step02.png",
					content: "Click the blocking ruler tool in the tool bar."
				},
				{
					title: "Step 2: Move the Blocking Ruler",
					imageUrl: "./tutorials/content/block-step03.png",
					content: "Use the hand cursor to position the blocking ruler by clicking and dragging anywhere on the ruler."
				},
				{
					title: "Step 3: Use the Blocking Ruler",
					imageUrl: "./tutorials/content/block-step04.png",
					content: "Use the hand cursor to move the blocking ruler as you read."
				},
				{
					title: "Step 4: Close the Blocking Ruler",
					imageUrl: "./tutorials/content/block-step05.png",
					content: "To close the blocking ruler, click the close button in the upper right corner of the ruler."
				}				

			]
		}
	]



var container = $(".tool-tips");

// Loop through and build tutorials
for (var i = 0; i < tutorialInformation.length; i++) {
	var tutorialData = tutorialInformation[i];
	
	var tutorial = new choppingblock.Tutorial(tutorialData);
	tutorial.buildButton(container);
	
}
