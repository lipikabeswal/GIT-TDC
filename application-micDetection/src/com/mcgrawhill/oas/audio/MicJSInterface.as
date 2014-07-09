package com.mcgrawhill.oas.audio
{
	
	import flash.events.Event;
	import flash.events.IOErrorEvent;
	import flash.events.SecurityErrorEvent;
	import flash.events.StatusEvent;
	import flash.external.ExternalInterface;
	import flash.media.Microphone;
	
	public class MicJSInterface
	{
		
		public var eventHandler:String = "microphone_events";
		
		private var micTracker:MicrophoneTracker;
		
		public static var READY:String = "ready";
		
		public static var GET_MICROPHONE_RETURNS_NULL:String = "get_microphone_returns_null";
		public static var NO_MICROPHONE_FOUND:String = "no_microphone_found";
		public static var MICROPHONE_USER_REQUEST:String = "microphone_user_request";
		public static var MICROPHONE_CONNECTED:String = "microphone_connected";
		public static var MICROPHONE_NOT_CONNECTED:String = "microphone_not_connected";
		public static var MICROPHONE_ACTIVITY:String = "microphone_activity";
		public static var MICROPHONE_STATUS:String = "microphone_status";
		
		public static var IO_ERROR:String = "io_error";
		public static var SECURITY_ERROR:String = "security_error";
		
		/**
		 * Constructor
		 */
		public function MicJSInterface() {
			if(ExternalInterface.available && ExternalInterface.objectID) {
				ExternalInterface.call('console.log', "MicJSInterface Constructor");
				ExternalInterface.addCallback("init", init);				
				ExternalInterface.addCallback("permit", requestMicrophoneAccess);
				ExternalInterface.addCallback("configure", configureMicrophone);
				ExternalInterface.addCallback("setUseEchoSuppression", setUseEchoSuppression);
				ExternalInterface.addCallback("setLoopBack", setLoopBack);
				ExternalInterface.addCallback("getMicrophone", getMicrophone);
				ExternalInterface.addCallback("startTracking", startTracking);
				ExternalInterface.addCallback("stopTracking", stopTracking);
				ExternalInterface.addCallback("getMaxActivityLevel", getMaxActivityLevel);
			}
			this.micTracker = new MicrophoneTracker();
			this.micTracker.addEventListener(MicrophoneTracker.ACTIVITY, microphoneActivity);
			this.micTracker.addEventListener(MicrophoneTracker.STATUS, microphoneStatus);
			this.micTracker.addEventListener(MicrophoneTracker.NO_MICROPHONE, microphoneError);
		}
		
		
		public function init(url:String=null, fieldName:String=null, formData:Array=null):void {
			ExternalInterface.call('console.log', "MicJSInterface#init called");
		}
		
		public function ready():void {
			ExternalInterface.call('console.log', "MicJSInterface#ready");
			ExternalInterface.call('console.log', "Calling '" + this.eventHandler + "' now!");
			ExternalInterface.call(this.eventHandler, MicJSInterface.READY);
			var available:Boolean = isMicrophoneAvailable();
		}
		
		private function microphoneActivity(event:Event):void {
			ExternalInterface.call('console.log', "MicJSInterface.microphoneActivity");
			ExternalInterface.call(this.eventHandler, MicJSInterface.MICROPHONE_ACTIVITY, this.micTracker.mic.activityLevel);
		}
		
		private function microphoneStatus(event:Event):void {
			ExternalInterface.call(this.eventHandler, MicJSInterface.MICROPHONE_STATUS, 
								   this.micTracker.isTracking, this.micTracker.mic.activityLevel);
		}
		
		private function microphoneError(event:Event):void {
			ExternalInterface.call('console.warn', "MicJSInterface.microphoneError: reload the iFrame!");			
			ExternalInterface.call(this.eventHandler, MicJSInterface.GET_MICROPHONE_RETURNS_NULL);
		}
		
		public function isMicrophoneAvailable():Boolean {
			ExternalInterface.call("console.log", "isMicrophoneAvailable: this.micTracker.mic=" + this.micTracker.mic);
			if(this.micTracker.mic == null) { 
				return false;
			} else if (this.micTracker.mic.muted) {
				return false;
			} else if(Microphone.names.length == 0) {
				ExternalInterface.call(this.eventHandler, MicJSInterface.NO_MICROPHONE_FOUND);
				return false;
			} else {
				ExternalInterface.call(this.eventHandler, MicJSInterface.MICROPHONE_USER_REQUEST);
			}
			return true;
		}
		
		public function requestMicrophoneAccess():void {
			this.micTracker.mic.addEventListener(StatusEvent.STATUS, onMicrophoneStatus);
		}
		
		private function onMicrophoneStatus(event:StatusEvent):void {
			ExternalInterface.call("console.log", "onMicrophoneStatus");
			if(event.code == "Microphone.Unmuted") {
				this.configureMicrophone();
				ExternalInterface.call(this.eventHandler, MicJSInterface.MICROPHONE_CONNECTED, this.micTracker.mic);
			} else {
				ExternalInterface.call(this.eventHandler, MicJSInterface.MICROPHONE_NOT_CONNECTED);
			} 
		}
		
		public function configureMicrophone(rate:int=22, gain:int=60, silenceLevel:Number=5, silenceTimeout:int=3000):void {
			var log:String = "rate=" + rate + ", gain=" + gain + ", silenceLevel=" + silenceLevel;
			log += ", silenceTimeout=" + silenceTimeout;
			ExternalInterface.call("console.log", "configureMicrophone: " + log);
			this.micTracker.mic.rate = rate;
			this.micTracker.mic.gain = gain;
			this.micTracker.mic.setSilenceLevel(silenceLevel, silenceTimeout);
		}
		
		public function setUseEchoSuppression(useEchoSuppression:Boolean):void {
			this.micTracker.mic.setUseEchoSuppression(useEchoSuppression);
		}
		
		public function setLoopBack(state:Boolean):void {
			this.micTracker.mic.setLoopBack(state);
		}
		
		public function getMicrophone():Boolean {
			return this.micTracker.mic == null;
		}
		
		public function getMaxActivityLevel():Number {
			return this.micTracker.maxActivityLevel;
		}
		
		public function startTracking():void {
			this.micTracker.startTracking();
		}
		
		public function stopTracking():Number {
			this.micTracker.stopTracking();
			return this.micTracker.maxActivityLevel;
		}
		
		private function onIOError(event:Event):void {
			ExternalInterface.call(this.eventHandler, MicJSInterface.IO_ERROR, IOErrorEvent(event).text);
		}
		
		private function onSecurityError(event:Event):void {
			ExternalInterface.call(this.eventHandler, MicJSInterface.SECURITY_ERROR, SecurityErrorEvent(event).text);
		}		
	}
}