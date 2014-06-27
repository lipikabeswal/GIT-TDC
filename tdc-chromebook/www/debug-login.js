/*
Set the credentials here and run the following command from the console of the index.html
webviewMan.sendMsgToWebview({command:"debugLogin"})

*/
var tdcDebug = {
  credentials: {
    user: 'M-S-DHONI-0102',
    pass: 'SILO3',
    accessCode: 'ISOTOPIC'
  },
  login: function () {
    var ls = gScreens.contents.ls;
    ls.searchSubviews('name', 'loginIdField').setAttribute('text', this.credentials.user );
    ls.searchSubviews('name', 'istepPasswordField').setAttribute('text',  this.credentials.pass );
    ls.searchSubviews('name', 'accesscodeField').setAttribute('text',  this.credentials.accessCode );
    gScreens.contents.ls.login();
  }
}
