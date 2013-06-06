package com.ctb.tdc.web.utils;

import javax.sound.sampled.*;
import java.io.*;
import java.util.ArrayList;
 
/**
 * 
 * 
 */
public class Recorder {
    // record duration, in milliseconds
    static final long RECORD_TIME = 500; // .5 sec 
	//static final long RECORD_TIME = 90000; 
    private static boolean microphone = false;
    // path of the wav file

 
    // format of audio file
    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
 
    // the line from which audio data is captured
    TargetDataLine line;
    byte[] data = new byte[16000/5];
    
 
    /**
     * Defines an audio format
     */
    AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                                             channels, signed, bigEndian);
        return format;
    }
 
    /**
     * Captures the sound and record into a WAV file
     */
    public boolean start() {
    	boolean isMic = false;
        try {
            AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            
            // checks if system supports the data line
            if (!AudioSystem.isLineSupported(info)) {
               // System.out.println("Line not supported");
                isMic=false;
            }
            else{
            line = (TargetDataLine) AudioSystem.getLine(info);
           //System.out.println("buffersize"+line.getBufferSize());
            line.open(format);
            line.flush();
            line.start();   // start capturing
          //  System.out.println("Start capturing...");
            AudioInputStream ais = new AudioInputStream(line);
            ais.read(data);
            //System.out.println("Start recording...")
            
            ArrayList<Byte> frame = new ArrayList<Byte>();
		      for (int i = 0; i < data.length; i++) {
		    	 if (data[i]>0){
			  		 frame.add(data[i]);
			  		// System.out.println("Frame Value:::"+data[i]);
			  	  }
		    	 
		  	  
		      }
		      if(frame.size()>0){
		    	  isMic=true;
		      }
		      
		      //System.out.println("in start "+isMic);
            }
 
        } catch (LineUnavailableException ex) {
        	isMic=true;
        	return isMic;
            //ex.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
		return isMic;
    }
 
    /**
     * Closes the target data line to finish capturing and recording
     */
    public void finish() {
    	try{
    		if(line!=null){
	        line.stop();
	        line.flush();
	        line.close();
    		}
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	
    }
 
    /**
     * Entry to run the program
     */
    /**
     * @param args
     */
   public static String micDetection() {
    	System.out.println("Inside micDetection");
        final Recorder recorder = new Recorder();
 
        // creates a new thread that waits for a specified
        // of time before stopping
        Thread stopper = new Thread(new Runnable() {
            public void run() {
                try {
                	
                    Thread.sleep(RECORD_TIME);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                recorder.finish();
            }
        });
 
        stopper.start();
        
 
        // start recording
        microphone=recorder.start(); 
        //System.out.println("In main>>>>"+microphone);
        if(microphone){
        	//System.out.println("A microphone is attached to your system");
        	return "<true />";
        	}
        else{
        	//System.out.println("Microphone is not attached");
        	return "<false />";
        }
       
    }
}