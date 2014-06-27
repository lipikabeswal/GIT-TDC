function PersistenceAction()
{
    //this.loginResponse=null;

}

PersistenceAction.loginResponse=null;
PersistenceAction.prototype.login = function(reqXml)
{
    console.log("Inside Login method");
    var url = "http://tms-oas-qa.ctb.com/TestDeliveryWeb/CTB/login.do";
    var posting = $.post(url, { method: "login", requestXML: reqXml});

    posting.done(function (data) {
        console.log("Success : " + data);
        var xmlDoc = $.parseXML(data);
        PersistenceAction.loginResponse = $(xmlDoc);
        webviewMan.sendMsgToWebview(data);
    });
    posting.fail(function (data) {
        console.log("Fail : " + data);
        webviewMan.sendMsgToWebview("<ERROR />");
    });

}

PersistenceAction.prototype.save = function(reqXml)
{
    console.log("Inside save method");
    var url = "http://tms-oas-qa.ctb.com/TestDeliveryWeb/CTB/save.do";
    var posting = $.post(url, { method: "save", requestXML: reqXml});

    posting.done(function (data) {
        console.log("Success : " + data);
        webviewMan.sendMsgToWebview(data);
    });

    posting.fail(function (data) {
        console.log("Fail : " + data);
        webviewMan.sendMsgToWebview("<ERROR />");
    });

}
