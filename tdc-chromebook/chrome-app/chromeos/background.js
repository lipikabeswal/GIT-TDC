var appInfo = {
  launches: 0
}

chrome.app.runtime.onLaunched.addListener( function() {
  appInfo.launches++;
  console.log("chrome.app.runtime.onLaunched: appInfo.launches=" + appInfo.launches);
  chrome.app.window.create( 'index.html', {
    width: 800,
    height: 650,
    minWidth: 800,
    minHeight: 650
  } );
} );

chrome.app.runtime.onRestarted.addListener( function() {
  console.log( "onRestarted" );
} );

chrome.runtime.onInstalled.addListener( function() {
  console.log( "chrome.runtime.onInstalled" );
  var storedInfo = chrome.storage.local.get( "appInfo", function( items ) {
    if ( items.appInfo === undefined ) {
      chrome.storage.local.set( {"appInfo": JSON.stringify( appInfo )} );
    }
    console.log( "storage get callback: items.appInfo=" + items.appInfo );

  } );
  console.log( "storedInfo=" + storedInfo );
} );

chrome.runtime.onSuspend.addListener( function() {
  console.warn("onSuspend");
})

function getTimesLoaded() {
  return appInfo.launches;
}