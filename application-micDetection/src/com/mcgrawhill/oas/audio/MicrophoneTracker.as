package com.mcgrawhill.oas.audio
{
	import flash.events.ActivityEvent;
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;
	import flash.events.StatusEvent;
	import flash.events.TimerEvent;
	import flash.external.ExternalInterface;
	import flash.media.Microphone;
	import flash.system.Security;
	import flash.system.SecurityPanel;
	import flash.utils.Timer;
	import flash.media.SoundTransform;
	
	public class MicrophoneTracker extends EventDispatcher
	{
		
		public var mic:Microphone;
		public static var NO_MICROPHONE:String = "no_microphone";
		public static var ACTIVITY:String = "activity";
		public static var STATUS:String = "status";
		
		public var isTracking:Boolean = false;
		public var shouldStartTracking:Boolean = false;
		public var isMuted:Boolean = true;
		public var maxActivityLevel:Number = 0;
		private var timer:Timer = new Timer(250);	
		
		public function MicrophoneTracker(target:IEventDispatcher=null)
		{
			super(target);
		}
		
		private function initializeMicrophone():Boolean {
			this.mic = Microphone.getMicrophone();
			ExternalInterface.call('console.log', "Microphone.getMicrophone() returned " + this.mic);
			if (this.mic != null) {
				this.mic.setLoopBack(true);
				this.mic.setUseEchoSuppression(true);
				var transformMic:SoundTransform = new SoundTransform(0,0);
				transformMic.volume = 0;
				this.mic.soundTransform = transformMic;
				mic.addEventListener(StatusEvent.STATUS, this.onMicStatus);
				this.listMicrophones();
			} else {
				ExternalInterface.call('console.error', "MicrophoneTracker.initializeMicrophone: No microphone found!!");
				dispatchEvent(new Event(MicrophoneTracker.NO_MICROPHONE));								
			}
			return mic;
		}
		
		/**
		 * For debugging purposes: Lists the names of all microphones.
		 */
		private function listMicrophones():void {
			var deviceArray:Array = Microphone.names; 
			ExternalInterface.call('console.log', "MicrophoneTracker.listMicrophones: Listing all microphones found");
			for (var i:int = 0; i < deviceArray.length; i++) 
			{ 
				ExternalInterface.call('console.log', "Mic device #" + i + ": " + deviceArray[i]);
			}			
		}
		
		/**
		 * Event handler for microphone activity events.
		 * @param event: The event object.
		 * 
		 */
		private function onMicActivity(event:Event):void {
			ExternalInterface.call('console.log', "MicrophoneTracker.onMicActivity");
			dispatchEvent(new Event(MicrophoneTracker.ACTIVITY));
		}
		/* Event handler for microphone status events.
			* @param event: The event object.
				* 
				*/
					private function onMicStatus(event:StatusEvent):void {
						ExternalInterface.call('console.log', "MicrophoneTracker.onMicStatus: " + event.toString());
						ExternalInterface.call('console.log', "MicrophoneTracker.onMicStatus: muted=" + this.mic.muted + ", shouldStartTracking=" + shouldStartTracking);
						if (mic) 
							this.isMuted = this.mic.muted;
						if (! this.mic.muted && this.shouldStartTracking) {
							ExternalInterface.call("showRecordWaitPopup");
							this.startTrackingTimer();
						} else if (this.isTracking && this.mic.muted) {
							ExternalInterface.call('console.log', "MicrophoneTracker.onMicStatus: muted during recording, should stop tracking now!");
							this.stopTracking();
						} else if (! this.isTracking && ! this.mic.muted) {
							ExternalInterface.call('console.log', "MicrophoneTracker.onMicStatus: >> should start tracking now");
							ExternalInterface.call("showRecordWaitPopup");
							this.startTrackingTimer();
						}
						dispatchEvent(new Event(MicrophoneTracker.STATUS));
					}
		
		/**
		 * Starts tracking the microphone input level.
		 */
		public function startTracking():void {
			ExternalInterface.call('console.log', "MicrophoneTracker.startTracking");
			if (isTracking) {
				ExternalInterface.call('console.warn', "MicrophoneTracker.startTracking: already tracking mic, returning from function!");
				return;
			}
			initializeMicrophone();
			
			if (!mic) {
				ExternalInterface.call('console.log', "MicrophoneTracker.startTracking: No microphone available for tracking, returning from function!");
				return;
			}

			ExternalInterface.call('console.log', "MicrophoneTracker.startTracking: mic.muted=" + this.mic.muted);
			if(this.mic.muted) {
				if (! this.isTracking) {
					Security.showSettings(SecurityPanel.PRIVACY);
					ExternalInterface.call('bringFrameToFront');
					ExternalInterface.call('console.warn', "MicrophoneTracker.startTracking: mic is muted, showing SecurityPanel");
					this.shouldStartTracking = true;					
				} else {
					this.stopTracking();
				}
			} else {
				ExternalInterface.call('console.warn', "MicrophoneTracker.startTracking: Microphone not muted, no need to show SecurityPanel");
				this.startTrackingTimer();
			}
		}
		
		private function startTrackingTimer():void {
			ExternalInterface.call('console.log', "MicrophoneTracker.startTrackingTimer");
			this.shouldStartTracking = false;			
			this.maxActivityLevel = 0;
			this.isTracking = true;
			this.mic.addEventListener(ActivityEvent.ACTIVITY, onMicActivity);
			timer.start();
			ExternalInterface.call('console.warn', "Timer started....");
			timer.addEventListener(TimerEvent.TIMER, trackLevel);			
		}
		
		public function stopTracking():void {
			ExternalInterface.call('console.log', "MicrophoneTracker.stopTracking");
			timer.stop();
			this.mic.removeEventListener(ActivityEvent.ACTIVITY, onMicActivity);
			this.isTracking = false;
		}
		
		private function trackLevel(e:TimerEvent):void {
			var level:Number = this.mic.activityLevel;
			
			// Workaround to be able to detect the level if
			// the microphone is unplugged and then plugged
			// in again during tracking.
			if (level <= 0) {
				this.mic.removeEventListener(StatusEvent.STATUS, this.onMicStatus);						
				this.mic = Microphone.getMicrophone();
				if (this.mic == null) {
					
				} else {					
					this.mic.addEventListener(StatusEvent.STATUS, this.onMicStatus);
					this.mic.setLoopBack(true);
					this.mic.setUseEchoSuppression(true);
					var transformMic:SoundTransform = new SoundTransform(0,0);
					transformMic.volume = 0;
					this.mic.soundTransform = transformMic;
				}
			}
			
			ExternalInterface.call('updateCurrentLevel', level);
			if (level > this.maxActivityLevel) {
				this.maxActivityLevel = level;
			}
			ExternalInterface.call('updateActivityLevel', this.maxActivityLevel, level);
		}
	}
}