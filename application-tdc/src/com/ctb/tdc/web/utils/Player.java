package com.ctb.tdc.web.utils;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import com.ctb.tdc.web.servlet.TuningUtilitySoundRecorder;


public class Player extends Thread{
	protected AudioInputStream audioInputStream;
	protected AudioFormat audioFormat;
	protected DataLine.Info info;
	public SourceDataLine line;
	protected byte[] buffer;
	protected int written;
	protected int read;
    protected String state;
    public boolean playDisableFlag = false;
    private boolean playComplete = false;
	protected Thread thread;
	protected TuningUtilitySoundRecorder parentObj;
	
    public Player(){}

    public void streamPass(AudioInputStream ais){
            audioInputStream = ais;
	}

	public void start(String play) {

            this.state = play;
            //if((playDisableFlag)) {
                thread = new Thread(this);
                thread.setName("Playback");
                thread.start();
            //}
        }

	public void stopPlaying(String state) {

            //playDisableFlag = true;
            this.state = state;
            thread = null;
            if (line != null) {
                    line.stop();
                    line.close();
                    line = null;
                   // playDisableFlag=true;
            }
            try {
                    audioInputStream.close();
            }
            catch (IOException e) {
                    e.printStackTrace();
            }
            audioInputStream = null;
	}

       public void pauseIt(String state) {
            //playDisableFlag = true;
            System.out.println("within pause method");
            this.state = state;
            if(line != null) {
                System.out.println("within if method");
                line.stop();
            }
       }

       public void setupSound(){

		audioFormat = audioInputStream.getFormat();
		System.out.println("audio Format :  "  + audioFormat);
		info = new DataLine.Info(SourceDataLine.class, audioFormat);

                // If the audioFormat is not directly supported
		if (!AudioSystem.isLineSupported(info)) {
			AudioFormat sourceFormat = audioFormat;
			AudioFormat targetFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					sourceFormat.getSampleRate(),
					16,
					sourceFormat.getChannels(),
					sourceFormat.getChannels() * 2,
					sourceFormat.getSampleRate(),
					false);
			audioInputStream = AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
			audioFormat = audioInputStream.getFormat();
			info = new DataLine.Info(SourceDataLine.class, audioFormat);
		}
		// We have to open the line for it to be ready to receive audio data.
		try {
			line = (SourceDataLine) AudioSystem.getLine(info);
			line.open(audioFormat);
		}
		catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		buffer = new byte[128000];
		written = 0;
		read = 0;
	}

	synchronized public void run()
	{
            System.out.println("Thread Run : "+state);
            //playDisableFlag = false;

            if("PLAY".equalsIgnoreCase(state)) {
                while (thread != null && read != -1) {
                    if (written >= read) {
                        try {
                            read = audioInputStream.read(buffer, 0, buffer.length);
                            written = 0;
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (read > written) {
                        if("PAUSED".equalsIgnoreCase(state)) {
                            break;
                        }
                        int temp = line.write(buffer, written, read-written);
                        written += temp;
                     }
                }

                if("PLAY".equalsIgnoreCase(state)) {
                    try {
                        System.out.println("byteClone");
                        line.drain();
                        //After playing all the data.
                        line.close();
                        playComplete = true;
                        //playDisableFlag = true;
                        //jsObj.eval("getPlayCompleted(\"true\")");
                        parentObj.stopAudio();
                    }
                    catch (Exception ex) {
                        //Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
	}

	public boolean statusPlayDisableFlag()
	{
            return playDisableFlag;
	}

        public boolean statusPlayCompleteFlag()
	{
            return playComplete;
	}

	public boolean isPlayDisableFlag() {
		return playDisableFlag;
	}

	public void setPlayDisableFlag(boolean playDisableFlag) {
		this.playDisableFlag = playDisableFlag;
	}

	public boolean isPlayComplete() {
		return playComplete;
	}

	public void setPlayComplete(boolean playComplete) {
		this.playComplete = playComplete;
	}

	public TuningUtilitySoundRecorder getParentObj() {
		return parentObj;
	}

	public void setParentObj(TuningUtilitySoundRecorder parentObj) {
		this.parentObj = parentObj;
	}

   
}