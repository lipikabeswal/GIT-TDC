function microphone_events() {

  console.log( "microphone_events: %s", arguments[0] );

  switch(arguments[0]) {
    case "ready":
      MicDetector.connect( "MicrophoneDetection", 0);
      break;

    case "microphone_status":
    	setTimeout(function() { sendFrameToBack(); }, 200);
      //updateStatus(arguments[1], arguments[2]);
      break;

    case "get_microphone_returns_null":
    	console.log("get_microphone_returns_null***********");
      top.window.reloadIFrame();
      break;

    case "microphone_user_request":
      MicDetector.showPermissionWindow();
      break;

    case "microphone_connected":
      var mic = arguments[1];
      MicDetector.isReady = true;
      MicDetector.configureMicrophone();
      break;

    case "microphone_not_connected":
      break;

    case "microphone_activity":
      break;

  } // end switch

}


