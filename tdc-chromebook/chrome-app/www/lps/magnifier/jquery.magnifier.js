$(document).ready(function() {
				var scale = 2;
				var mag = null;
                var $magnifyingBorder = $('<div id="mborder" class="magnifying_border"></div>');
				var $magnifyingGlass = $('<div id="mglass" class="magnifying_glass"></div>');
				var $magnifiedContent = $('<div class="magnified_content"></div>');
				var $magnifyingLens = $('<div class="magnifying_lens"></div>');
				var prev_x = null;
				var prev_y = null;
				var isPreviousPos = "F";
				var refreshing=false;
				var x=document.getElementById('mybutton2');
				
				//setup
				
				$magnifiedContent.css({
					backgroundColor: $("html").css("background-color") || $("body").css("background-color"),
					backgroundImage: $("html").css("background-image") || $("body").css("background-image"),
					backgroundAttachment: $("html").css("background-attachment")  || $("body").css("background-attachment"),
					backgroundPosition: $("html").css("background-position") || $("body").css("background-position")
					
				});
				
				/*x.onclick=function(){
					takeSS();
				}*/
				
				function updateViewSize() {
					$magnifiedContent.css({width: $(document).width(), height: $(document).height()});
				}
				
				updateViewSize();
				
				$(window).resize(updateViewSize);
				
				$magnifyingBorder.mousedown(function(e) {
					
					e.preventDefault();
					$(this).data("drag", {mouse: {top: e.pageY, left: e.pageX}, offset: {top: $(this).offset().top, left: $(this).offset().left}});
					$.setMagnifierHTML();
					mag=true;
				});
				
				
				$.closeMagnifier = function() { 
                      isPreviousPos = "F";
			    }; 
			    
			    $.setMagnifierHTML = function() { 
        		 var appDiv = document.getElementById("appcontainer");
                   $magnifiedContent.html(appDiv.innerHTML);
				   setMagnifierIframeStateDel();
                };
			    
			     $.setPreviousPosition = function() { 
                     isPreviousPos = "T";
		   		};
				
				$(document.body).mousemove(function(e) {
					if ($magnifyingBorder.data("drag")) {
						if(mag==true){
							setMagnifierIframeState();
						}
						var drag =$magnifyingBorder.data("drag");
						
						var left = drag.offset.left + (e.pageX-drag.mouse.left);
						var top = drag.offset.top + (e.pageY-drag.mouse.top);
						
						var maxLeft=(document.body.clientWidth)/2 + 438;
                 		var maxTop=document.body.clientHeight/2 + 410;
		                  var minLeft=-9;
		                  var minTop=-25;
		                  // retaining last position
		                  prev_x = left;
		                  prev_y = top;
		                  //To Prevent highlighter from going out of view
		                  if(left<=minLeft)
		                  {
		                  left=minLeft;
		                  }
		                  if(top<=minTop)
		                  {
		                  top=minTop;
		                  }
		                  if(left>=maxLeft)
		                  {
		                  left=maxLeft;
		                  }
		                  if(top>=maxTop)
		                  {
		                  top=maxTop;
		                  }
						
						$magnifyingBorder.css({left: left, top: top});
						$magnifiedContent.css({left: -left*scale, top: -top*scale});
						mag=false;
					}
				}).mouseup(function() {
					$magnifyingBorder.removeData("drag");
				});
			});