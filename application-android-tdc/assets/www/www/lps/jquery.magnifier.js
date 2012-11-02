$(document).ready(function() {
				var scale = 2;
				
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
					// $magnifyingGlass.append($magnifyingLens); //comment this line to allow interaction
					$(document.body).append($magnifyingGlass);
					$magnifyingGlass.css({left: 575, top: 250});
					$magnifiedContent.css({left: -575*scale, top: -250*scale});
			    	/*var ids = [];
				    $("#appcontainer").find("div").each(function(){
				    	if(this.id == "loginScreenId"){
				        	ids.push(this);
				        }
				    });
				    alert("before "+ids.length);
				    var x = ids[ids.length-1];
					x.parentNode.removeChild(x);
					
				    alert(ids.length);*/
				}
				
				function updateViewSize() {
					$magnifiedContent.css({width: $(document).width(), height: $(document).height()});
				}
				
				updateViewSize();
				
				$(window).resize(updateViewSize);
				
				$magnifyingGlass.bind("mousedown",function(e) {
					e.preventDefault();
					$(this).data("drag", {mouse: {top: e.pageY, left: e.pageX}, offset: {top: $(this).offset().top, left: $(this).offset().left}});
				});
				
				$magnifyingGlass.bind("mousemove",function(e) {
				e.preventDefault();
					if ($magnifyingGlass.data("drag")) {
						var drag =$magnifyingGlass.data("drag");
						
						var left = drag.offset.left + (e.pageX-drag.mouse.left);
						var top = drag.offset.top + (e.pageY-drag.mouse.top);
						$magnifyingGlass.css({left: left, top: top});
						$magnifiedContent.css({left: -left*scale, top: -top*scale});
					}
				}).bind("mouseup",function() {
					$magnifyingGlass.removeData("drag");
				});
				
			});