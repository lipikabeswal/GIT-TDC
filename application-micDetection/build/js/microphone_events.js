function microphone_events() {

  console.log( "microphone_events: %s", arguments[0] );

  switch(arguments[0]) {
    case "ready":
      MicDetector.connect( "MicrophoneDetection", 0);
      break;

    case "no_microphone_found":
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
      MicDetector.defaultSize();
      break;

    case "microphone_activity":
      $('#activity_level').text(arguments[1]);
      break;

  } // end switch

}


