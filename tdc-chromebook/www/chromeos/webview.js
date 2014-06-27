/*
 * Code for webview and embedder integration. Once the webview
 * sends the contentload event, the event receiver in webview-embedder.js
 * for the embedding page will trigger a call into the webview using
 *
 */

var appWindow = null;
var appOrigin = null;
var sendData = {};

function messageFromEmbedder( event ) {
  console.log( "webview received message from embedder: " + event.data );
  // First message: store appWindow and appOrigin
  if ( !appWindow || !appOrigin ) {
    appWindow = event.source;
    appOrigin = event.origin;
    console.log( 'Opened communication with the Chrome wrapper app. appOrigin=' + appOrigin );
    sendMsgToApp( 'handshakereply' );
  }
  // Debug login; saves time for login during debugging
  var data = event.data;
  if (data.command && data.command == "debugLogin" ) {
    tdcDebug.login();
  } else if ( data.type)
  {
      if(data.type=="item")
      {
          console.log("Item Response!!!",data.xml);
          gCommunicator.itemFinishCall(data.xml);
      }
      if(data.type=="image")
      {
          console.log("get Image Response!!!");
          gCommunicator.imageResponse(data.base64string);
          setContentResponse(data.base64string);
      }

  }
  else if ( data.indexOf( '<' ) != -1 ) {
    gCommunicator.finishCall( event.data );
  }

}

function sendMsgToApp( data, xml ) {
  console.log( "inside sendMsgToApp method=------" + data + xml );
  if ( !appWindow || !appOrigin ) {
    return console.error( 'Cannot send message to Chrome wrapper app - communication channel has not yet been opened' );
  }
  sendData.method = data;
  sendData.xml = xml;
  appWindow.postMessage( sendData, appOrigin );
}

window.addEventListener( 'load', function( e ) {
  console.log( "webview.window.load event received, registering message event handler" );
  window.addEventListener( "message", messageFromEmbedder, false );
} );