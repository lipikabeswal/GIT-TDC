var SamplePlugin = function()
{
};
 
SamplePlugin.prototype.LOGIN = function(successCallback, failureCallback, jsonInput)
{

	return PhoneGap.exec(
            successCallback,        //Function called upon success
            failureCallback,        //Function called upon error
            'DelegatePlugin',        //Tell PhoneGap to run "DelegatePlugin" Plugin
            'LOGIN',                //Tell the which action we want to perform, matches an "action" string
            [jsonInput]);           //A list of args passed to the plugin, in this case empty
};
 
PhoneGap.addConstructor(function()
{
    PhoneGap.addPlugin("SamplePlugin", new SamplePlugin());
});