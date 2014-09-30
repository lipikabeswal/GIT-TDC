// **********************************
// ** BSC V2 CORE
// ** browser_validation.js
// ** Framework to validate browsers
// **********************************
/// <reference path="../vsdoc.html" />

$(document).ready(function()
{
	// Add hidden browser warning to the top of the body.
	var header_html = "<div class='header_div_browser_warning' ";
	header_html += "style='width:99%; position:relative; top:0px; right:0px; font-family:Verdana; font-weight:bold; font-size:11pt; padding:5px; min-height:20px; background-color:#FFFDF6; border:1px solid #FFEAB2; color:Black; display:none;' "
	header_html += " ></div>";
	
	window.setTimeout(function()
	{
		ValidateBrowsers();
	},100);

	$(window).resize(ValidateBrowsers);

	if( navigator.userAgent.indexOf("iPad") > -1 )
		document.body.addEventListener('gesturestart', ValidateBrowsers);

	function ValidateBrowsers()
	{
		if( $(".header_div_browser_warning").length==0 )
			$("body").prepend(header_html);

		var browser_msg = "";

		// ** Append any browser warning messages.
		browser_msg += ValidateBrowser();
		browser_msg += ValidateScreenRes();
		browser_msg += ValidateiPadOrientation();

		// ** If the message is not empty, display it.
		if( browser_msg != "" )
		{
			var browser_warning_icon = "<img style='vertical-align:middle; padding-left:10px; padding-right:10px; display:inline;' src='images/browser_warning_icon.png'></img>";
			$(".header_div_browser_warning").html(browser_warning_icon + browser_msg);
			$(".header_div_browser_warning").slideDown(500);
		}
		else
		{
			$(".header_div_browser_warning").slideUp(500);
		}
	}

	// ** Validate min screen resolution.
	function ValidateScreenRes()
	{
		var browser_msg = "";
		
		if( new String(window.location).search("localhosta") > -1 ||
			navigator.userAgent.search("Android") > -1 ||
			navigator.userAgent.search("Mobile") > -1 ||
			navigator.userAgent.search("iPhone") > -1 ||
			navigator.userAgent.search("iPad") > -1 )
		{
			return( browser_msg );
		}

		var width = $(window).width();
		var height = $(window).height();

		if( width<1000 || height<720 )
		{
			//browser_msg = "You are using a screen resolution, "+width+" by "+height+" that is not sufficient to display this site. Please increase you screen resolution to at least 1024 by 768 to continue."
			//browser_msg = "You are using a screen resolution, "+width+" by "+height+", that is not sufficient to display this site.  Please adjust your screen resolution to 1024 x 768 to continue.";
			browser_msg = "You are using a browser window resolution that is not sufficient to display this site. Please adjust your browser window resolution to at least 1024 x 768 to continue.";
		}
		return( browser_msg );
	}

	// ** Validate lanscape iPad orientation.
	function ValidateiPadOrientation()
	{
		var browser_msg = "";

		if( navigator.userAgent.indexOf("iPad") > -1 && ( new String(window.location).search("login") > -1 || new String(window.location).search("index") > -1 ) )
		{
			// ** Landscape
			if( $(window).width() > 1000 )
			{
				//document.title="Landscape"
				$("#meta_viewport").attr("content","minimum-scale=.875, maximum-scale=.875, initial-scale=.875, user-scalable=no");
			}
			// ** Portrait
			else
			{
				//document.title="Portrait"
				//$("#meta_viewport").attr("content","minimum-scale=.77, maximum-scale=.77, user-scalable=no");
				browser_msg = "Please rotate your device for the best testing experience.";
			}
			$(document).scrollTop(0).scrollLeft(0);
		}

		return( browser_msg );
	}

	// ** Validate min browser version.
	function ValidateBrowser()
	{
		var browser_msg = "";
		try
		{
			var safari_version = "";
			try
			{
				safari_version = navigator.userAgent.split("Version/")[1].split(" Safari")[0];
			} catch(e) {}
			if( ($.browser.msie || navigator.userAgent.indexOf("Trident") > -1 ) && parseFloat($.browser.version) < 9 )
				browser_msg = "Internet Explorer "+$.browser.version;
			else if( navigator.userAgent.indexOf("iPad") > -1 )
			{
				if( $.browser.safari && parseFloat($.browser.version) < 534.46 )
					browser_msg = "iPad Safari "+$.browser.version
				else if( !$.browser.safari || navigator.userAgent.indexOf("CriOS") > -1 )
					browser_msg = "iPad Other";
			}
			else if( navigator.userAgent.indexOf("iPhone") > -1 )
			{
				if( $.browser.safari )
					browser_msg = "iPhone Safari "+$.browser.version
				else if( !$.browser.safari || navigator.userAgent.indexOf("CriOS") > -1 )
					browser_msg = "iPhone Other";
			}
			else if( $.browser.safari && parseFloat(safari_version) < 5.1 && navigator.userAgent.indexOf("Chrome") == -1 )
				browser_msg = "Safari "+safari_version;
			else if( navigator.userAgent.indexOf("Chrome")>-1 )
			{
				var chrome_version = parseFloat(navigator.userAgent.split("Chrome/")[1].split(" ")[0]); 
				if( chrome_version < 33 )
					browser_msg = "Chrome "+chrome_version;
			}
			else if( navigator.userAgent.indexOf("Firefox")>-1 && parseFloat($.browser.version) < 27 )
				browser_msg = "Firefox "+$.browser.version;
			else if( !$.browser.msie && !$.browser.safari && navigator.userAgent.indexOf("Trident") == -1 && navigator.userAgent.indexOf("Chrome") == -1 && navigator.userAgent.indexOf("Firefox") == -1 )
				browser_msg = "Not Supported";

			if( navigator.userAgent.indexOf("OPR/")>-1 || 
					 navigator.userAgent.indexOf("Dolfin")>-1 ||
					 navigator.userAgent.indexOf("Maxthon")>-1 ||
					 navigator.userAgent.indexOf("Mercury")>-1 )
				browser_msg = "Not Supported";
			if( browser_msg == "Not Supported" && navigator.userAgent.indexOf("iPad") > -1 )
				browser_msg = "iPad Other";
		}
		catch(e) {}

		// ** Warn of below min browser version.
		if( browser_msg != "" )
		{
			if( browser_msg.search( "iPad Safari" ) > -1 )
				browser_msg = "You are using a version of Safari which is not supported for this site, "+browser_msg+". <br />Some features may not function properly. Please update your device to continue.";
			else if( browser_msg.search( "iPhone Safari" ) > -1 )
				browser_msg = "You are using a version of Safari which is not supported for this site, "+browser_msg+". <br />Some features may not function properly.";
			else if( browser_msg == "iPad Other" || browser_msg == "iPhone Other" )
				browser_msg = "You are using a web browser which is not supported for this site. Please load this site using Safari. <br />Some features may not function properly.";
			else
			{
				if( browser_msg == "Not Supported" )
					browser_msg = "You are using a web browser which is not supported for this site. Some features may not function properly.";
				else
					browser_msg = "The browser version you are using, "+browser_msg+", is not supported for this site. Some features may not function properly.";

				browser_msg += "<br />Browser versions currently supported are: Chrome (33+), Firefox (27+), IE (9+), Safari (5.1+). <a href='https://www.google.com/intl/en-US/chrome/browser/' target='_blank' style='text-decoration:none; border-bottom:1px solid black;' >Click here to download the latest version of Chrome.</a>";
			}
		}
		return( browser_msg );
	}
});
