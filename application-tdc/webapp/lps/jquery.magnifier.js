$(document).ready(function() {
				var scale = 2;
				var elm=null;	
				var $magnifyingGlass = $('<div id="mglass" class="magnifying_glass"></div>');
				var $magnifiedContent = $('<div class="magnified_content"></div>');
				var $magnifyingLens = $('<div class="magnifying_lens"></div>');
				
				var x=document.getElementById('mybutton');
				
				//setup
				$magnifiedContent.css({
					backgroundColor: $("html").css("background-color") || $("body").css("background-color"),
					backgroundImage: $("html").css("background-image") || $("body").css("background-image"),
					backgroundAttachment: $("html").css("background-attachment")  || $("body").css("background-attachment"),
					backgroundPosition: $("html").css("background-position") || $("body").css("background-position")
				});
				
				
				x.onclick=function(){
					var appDiv = document.getElementById("appcontainer");
					$magnifiedContent.html(appDiv.innerHTML);
					$magnifyingGlass.append($magnifiedContent);
					$magnifyingGlass.append($magnifyingLens); //comment this line to allow interaction
					$(document.body).append($magnifyingGlass);
					$magnifyingGlass.css({left: 10, top: 250});
					$magnifiedContent.css({left: -10*scale, top: -250*scale});
                  $('.magnifying_glass').touch({
                                               animate: true,
                                               sticky: false,
                                               dragx: false,
                                               dragy: true,
                                               rotate: false,
                                               resort: false,
                                               scale: false
                                               });
				}
                  
                  
				
				function updateViewSize() {
					$magnifiedContent.css({width: $(document).width(), height: $(document).height()});
				}
				
				updateViewSize();
				
				$(window).resize(updateViewSize);
        
				$magnifyingGlass.bind("touchstart " , function(e) {
					e.preventDefault();
					
                        var touch = e.originalEvent.touches[0] || e.originalEvent.changedTouches[0];
                                      
                        elm = $(this).offset();
                        var x = touch.pageX - elm.left;
                        var y = touch.pageY - elm.top;
                        $magnifyingGlass.data("drag", {mouse: {top: y, left: x} });
                                      
                        
                                      
				});
				
				$(document.body).bind("touchmove "  ,function(e) {
				e.preventDefault(); 	
					if ($magnifyingGlass.data("drag")) {
						
                                      var touch = e.originalEvent.touches[0] || e.originalEvent.changedTouches[0];
                                      elm = $(this).offset();
                                      var x = touch.pageX - elm.left;
                                      var y = touch.pageY - elm.top;
                                      var drag =$magnifyingGlass.data("drag");
                                      var left = (x-drag.mouse.left);
                                      
                                      var top =  (y-drag.mouse.top);
                                      
                                      $magnifyingGlass.css({left: left, top: top});
                                      //$magnifiedContent.css({left: -left*scale, top: -top*scale});
                                      
                                     // console.log(x);
                                     // console.log(y);
					}
                                      });
                  $(document.body).bind(" touchend " , function() {
					$magnifyingGlass.removeData("drag");
				});
				
			});

