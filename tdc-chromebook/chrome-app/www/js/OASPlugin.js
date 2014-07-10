function echo(jsonInput)
{
	alert("inside Echo!")
	return Cordova.exec(
			function(result)
            {
                alert(result);
            },        //Function called upon success
            function(error)
            {
            	
                alert(error);
            },        //Function called upon error
            'DelegatePlugin',        //Tell PhoneGap to run "DelegatePlugin" Plugin
            'echo',                //Tell the which action we want to perform, matches an "action" string
            [jsonInput]);           //A list of args passed to the plugin, in this case empty
};

function executeRequest(method,xml,action)
{
	return Cordova.exec(
			function(result)
            {
            	console.log("SUCCESS :"+result+" !!!!!!!!!");
				gCommunicator.finishCall(result);
            },        //Function called upon success
            function(error)
            {
            	
                alert(error);
            },        //Function called upon error
            'DelegatePlugin',        //Tell PhoneGap to run "DelegatePlugin" Plugin
            action,                //Tell the which action we want to perform, matches an "action" string
            [method, xml]);           //A list of args passed to the plugin, in this case empty
};
