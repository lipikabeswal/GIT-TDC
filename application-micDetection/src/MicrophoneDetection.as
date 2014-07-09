package 
{
	import com.mcgrawhill.oas.audio.MicJSInterface;
	
	import flash.display.Sprite;
	import flash.display.StageAlign;
	import flash.display.StageScaleMode;
	import flash.media.Microphone;
	
	
	public class MicrophoneDetection extends Sprite
	{
		
		private var mic:Microphone = Microphone.getMicrophone();
		public var micInterface:MicJSInterface;

		private var micLevel:int = 0;
		
		public function MicrophoneDetection()
		{
			this.stage.align = StageAlign.TOP_LEFT;
			this.stage.scaleMode = StageScaleMode.NO_SCALE;
			micInterface = new MicJSInterface();
			
			if(this.root.loaderInfo.parameters["event_handler"]) {
				micInterface.eventHandler = this.root.loaderInfo.parameters["event_handler"];
			}
			micInterface.ready();
		}
		
	}
}