/**
 * DelegatePlugin.js
 *
 * Phonegap MyPlugin Instance plugin
 * Copyright (c) Sabyasachi shadangi 2012
 *
 */

var DelegatePlugin = function()
{
};

var responseQueue = [];
var eventCompleted = true;

/*var responseQueueWorker = setInterval(function (){
		console.log("Response queue trigger responseQueue.length:"+responseQueue.length);
		if(responseQueue.length > 0){
			if(eventCompleted == true){
				console.log("Response queue send response");
				sendResponse(responseQueue.shift());
			}
		}
	}, 2000);
*/




DelegatePlugin.prototype.LOGIN = function(jsonInput){
    // alert("Module 1 Alert:coming");
    //console.log("In delegate plug in :"+jsonInput);
    return Cordova.exec(
                        function(result)
                        {
                        setMyResponse(result);
                        },               //Function called upon success
                        function(error)
                        {
                        alert(error);
                        //console.log(error);
                        }
                        ,               //Function called upon error
                        "DelegatePlugin",      //Tell PhoneGap to run "DelegatePlugin" Plugin
                        "execute",             //Tell the which action we want to perform, matches an "action" string
                        jsonInput);            //A list of args passed to the plugin
};


function setMyResponse(result){
    console.log("Result : "+result);
    /*if(result.indexOf("||") >  -1){
        console.log("*******Queue Result***********");
        gCommunicator.finishCall(result);
        //responseQueue.push(result);
        //getImageInterval();
    }else{*/
        console.log("*******Setting Result***********");
        gCommunicator.finishCall(result);
    //}
    
	
  /*  gCommunicator.finishCall(result);
    //lz.embed.setCanvasAttribute("response", result);
    console.log("Response has been set");*/
 
};

DelegatePlugin.contentAction = function(jsonInput){
    // alert("Module 1 Alert:coming");
    //console.log("In delegate plug in :"+jsonInput);
    return Cordova.exec(
                        function(result)
                        {
                        setContentResponse(result);
                        },               //Function called upon success
                        function(error)
                        {
                        alert(error);
                        //console.log(error);
                        }
                        ,               //Function called upon error
                        "DelegatePlugin",      //Tell PhoneGap to run "DelegatePlugin" Plugin
                        "execute",             //Tell the which action we want to perform, matches an "action" string
                        jsonInput);            //A list of args passed to the plugin
};


function setContentResponse(result){
    console.log("Result : "+result);
    if(result.indexOf("||") >  -1){
        console.log("*******Queue Result***********");
        responseQueue.push(result);
    }else{
        console.log("*******Setting Result***********");
        gCommunicator.finishCall(result);
    }
    
	
    /*  gCommunicator.finishCall(result);
     //lz.embed.setCanvasAttribute("response", result);
     console.log("Response has been set");*/
    
};

DelegatePlugin.persistenceAction = function(jsonInput){
    // alert("Module 1 Alert:coming");
    //console.log("In delegate plug in :"+jsonInput);
    return Cordova.exec(
                        function(result)
                        {
                        setpersistenceResponse(result);
                        },               //Function called upon success
                        function(error)
                        {
                        alert(error);
                        //console.log(error);
                        }
                        ,               //Function called upon error
                        "DelegatePlugin",      //Tell PhoneGap to run "DelegatePlugin" Plugin
                        "execute",             //Tell the which action we want to perform, matches an "action" string
                        jsonInput);            //A list of args passed to the plugin
};


function setpersistenceResponse(result){
   
	
     gCommunicator.finishCall(result);
     //lz.embed.setCanvasAttribute("response", result);
     console.log("Persistence Response has been set");
    
};

DelegatePlugin.getItem = function(jsonInput){
    return Cordova.exec(
                        function(result)
                        {
                        gCommunicator.itemFinishCall(result);
                        },               //Function called upon success
                        function(error)
                        {
                        alert(error);
                        //console.log(error);
                        }
                        ,               //Function called upon error
                        "DelegatePlugin",      //Tell PhoneGap to run "DelegatePlugin" Plugin
                        "execute",             //Tell the which action we want to perform, matches an "action" string
                        jsonInput);            //A list of args passed to the plugin
};

DelegatePlugin.getImage = function(jsonInput){
    return Cordova.exec(
                        function(result)
                        {
                         gCommunicator.imageResponse(result);
                        },               //Function called upon success
                        function(error)
                        {
                        alert(error);
                        //console.log(error);
                        }
                        ,               //Function called upon error
                        "DelegatePlugin",      //Tell PhoneGap to run "DelegatePlugin" Plugin
                        "execute",             //Tell the which action we want to perform, matches an "action" string
                        jsonInput);            //A list of args passed to the plugin
};


DelegatePlugin.checkForScreenshot= function(){
    var jsonObj = ['{"method" : "checkForScreenshot", "xml_string" :"","class_name" : "TestSecurityAction"}'];
    console.log("DelegatePlugin.checkForScreenshot :"+jsonObj);
    return Cordova.exec(
                        function(result)
                        {
                        console.log("Checking for Screenshot");
                        },               //Function called upon success
                        function(error)
                        {
                        alert(error);
                        //console.log(error);
                        }
                        ,               //Function called upon error
                        "DelegatePlugin",      //Tell PhoneGap to run "DelegatePlugin" Plugin
                        "execute",             //Tell the which action we want to perform, matches an "action" string
                        jsonObj);            //A list of args passed to the plugin
};

DelegatePlugin.ttsRequest= function(text,speed){
    var jsonObj = ['{"method" : "'+text+'", "xml_string" :"'+speed+'","class_name" : "SpeechAction"}'];
    console.log("DelegatePlugin.ttsRequest :"+jsonObj);
    return Cordova.exec(
                        function(result)
                        {
                        gCommunicator.ttsResponse(result);
                        },               //Function called upon success
                        function(error)
                        {
                        alert(error);
                        //console.log(error);
                        }
                        ,               //Function called upon error
                        "DelegatePlugin",      //Tell PhoneGap to run "DelegatePlugin" Plugin
                        "execute",             //Tell the which action we want to perform, matches an "action" string
                        jsonObj);            //A list of args passed to the plugin
};


DelegatePlugin.ttsIsPlaying= function(){
    var jsonObj = ['{"method" : "isPlaying", "xml_string" :"","class_name" : "SpeechAction"}'];
    console.log("DelegatePlugin.ttsRequest :"+jsonObj);
    return Cordova.exec(
                        function(result)
                        {
                        gController._handleIsPlayingResponse(result);
                        },               //Function called upon success
                        function(error)
                        {
                        alert(error);
                        //console.log(error);
                        }
                        ,               //Function called upon error
                        "DelegatePlugin",      //Tell PhoneGap to run "DelegatePlugin" Plugin
                        "execute",             //Tell the which action we want to perform, matches an "action" string
                        jsonObj);            //A list of args passed to the plugin
};




	function sendResponse(resp){
		console.log("sendResponse : "+resp.length);
        gCommunicator.finishCall(resp);
		eventCompleted = false;
		console.log("*******Sending Response Alternate***********: " + resp.length);
	}
	function setEventCompleted(){
        console.log("setEventCompleted Response queue length : "+responseQueue.length);
		//eventCompleted = true;
        if(responseQueue.length>0){
            var resp = responseQueue.shift();
            console.log("SET EVENT COMPLETED: " + resp.length);
            sendResponse(resp);
        }
	}
/*

DelegatePlugin.addConstructor(function()
{
    DelegatePlugin.addPlugin("DelegatePlugin", new DelegatePlugin());
}); */

cordova.addConstructor(function() {
                       if (!window.Cordova) {
                       window.Cordova = cordova;
                       };
                       
                       if(!window.plugins) window.plugins = {};
                       
                       window.plugins.DelegatePlugin = new DelegatePlugin();
                       });
