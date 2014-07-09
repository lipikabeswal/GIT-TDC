(function( global ) {
  var MicDetector;

  MicDetector = {

    // Microphone configuration params
    // See the default values used in the function definition in MicJSInterface.configureMicrophone
    // http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/media/Microphone.html

    // Valid rates are: 5,8,11,22,44
    rate: 22,

    // The amount by which the microphone boosts the signal. Valid values are 0 to 100. The default value is 50.
    gain: 60,

    // The amount of sound the microphone is detecting.
    // Values range from 0 (no sound is detected) to 100 (very loud sound is detected).
    silenceLevel: 5,

    silenceTimeout: 3000,    // Default value is 2000

    useEchoSuppresion: true,


    mic: null,

    micOriginalWidth: 0,
    micOriginalHeight: 0,
    permissionWindowWidth: 240,
    permissionWindowHeight: 120,
    uploadFormId: null,
    uploadFieldName: null,
    isReady: false,

    connect: function( name, attempts ) {
      console.log("MicDetector.connect: name=%s, attempts=%s", name, attempts);
      if ( navigator.appName.indexOf( "Microsoft" ) != -1 ) {
        MicDetector.mic = window[name];
      } else {
        MicDetector.mic = document[name];
      }

      if ( attempts >= 40 ) {
        return;
      }

      // flash app needs time to load and initialize
      if ( MicDetector.mic && MicDetector.mic.init ) {
        return;
      }

      setTimeout( function() {MicDetector.connect( name, attempts + 1 );}, 100 );
    },

    showPermissionWindow: function() {
      MicDetector.mic.permit();
    },

    configureMicrophone: function() {
      _configure(this.rate, this.gain, this.silenceLevel, this.silenceTimeout);
    },

    _configure: function( rate, gain, silenceLevel, silenceTimeout ) {
      rate = parseInt( rate || 22 );
      gain = parseInt( gain || 100 );
      silenceLevel = parseInt( silenceLevel || 0 );
      silenceTimeout = parseInt( silenceTimeout || 4000 );
      switch ( rate ) {
        case 44:
        case 22:
        case 11:
        case 8:
        case 5:
          break;
        default:
          throw("invalid rate " + rate);
      }

      if ( gain < 0 || gain > 100 ) {
        throw("invalid gain " + gain);
      }

      if ( silenceLevel < 0 || silenceLevel > 100 ) {
        throw("invalid silenceLevel " + silenceLevel);
      }

      if ( silenceTimeout < -1 ) {
        throw("invalid silenceTimeout " + silenceTimeout);
      }

      MicDetector.mic.configure( rate, gain, silenceLevel, silenceTimeout );
    },

    setUseEchoSuppression: function( val ) {
      if ( typeof(val) != 'boolean' ) {
        throw("invalid value for setting echo suppression, val: " + val);
      }

      MicDetector.mic.setUseEchoSuppression( val );
    },

    setLoopBack: function( val ) {
      if ( typeof(val) != 'boolean' ) {
        throw("invalid value for setting loop back, val: " + val);
      }

      MicDetector.mic.setLoopBack( val );
    },

    startTracking: function() {
      MicDetector.mic.startTracking();
    },

    stopTracking: function() {
      return MicDetector.mic.stopTracking();
    },

    getMaxActivityLevel: function() {
      MicDetector.mic.getMaxActivityLevel();
    }
  };

  global.MicDetector = MicDetector;

})( this );

