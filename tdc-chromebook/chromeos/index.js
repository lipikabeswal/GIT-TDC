// Global namespace for TDC
var tdc = {};

function $(id) {
    return document.getElementById(id);
}

function log(text) {
    $('log').value += text + '\n';
}

var port = 9999;
var isServer = false;
var webviewMan = null;
var webviewHTMLFile = 'login.html';

if (http.Server) {
    // Listen for HTTP connections.
    var server = new http.Server();
    server.listen(port);
    isServer = true;

    server.addEventListener('request', function (req) {
        var url = req.headers.url;
        if (url == '/')
            url = '/www/' + webviewHTMLFile;
        // Serve the pages of this chrome application.
        req.serveUrl(url);
        return true;
    });

}

document.addEventListener('DOMContentLoaded', function () {
    console.log("DOMContentLoaded event");
    /*webviewMan = new (WebviewManager);
    webviewMan.initialize('tdcWebview');
    webviewMan.loadPage('http://localhost:' + port + '/www/' + webviewHTMLFile);*/
    //webviewMan.loadPage('https://oastest.ctb.com/SessionWeb/login.jsp');	//just to experiment
});

window.addEventListener("load", function (e) {

    webviewMan = new (WebviewManager);
    webviewMan.initialize('tdcWebview');
    webviewMan.loadPage('http://localhost:' + port + '/www/' + webviewHTMLFile);
    zip.workerScriptsPath = "/chromeos/util/";
    zip.useWebWorkers = false;
    console.log("window.load event");
    tdc.contentAction = new ContentAction();
});
