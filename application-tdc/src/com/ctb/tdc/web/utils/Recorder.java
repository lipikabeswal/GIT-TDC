package com.ctb.tdc.web.utils;

import javax.sound.sampled.*;
import java.io.*;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import com.ctb.tdc.web.servlet.PersistenceServlet;
import com.sun.media.sound.*;


import java.lang.reflect.*;
/**
 * 
 * 
 */
public class Recorder {
	static Logger logger = Logger.getLogger(PersistenceServlet.class);
	String osName = System.getProperty("os.name").toLowerCase();
    // record duration, in milliseconds
    //static final long RECORD_TIME = 500; // .5 sec 
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
    	//logger.info("MicDetection Start!!"+System.currentTimeMillis());
		boolean isMic = false;
    	AudioFormat format = getAudioFormat();
    	DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
    	/**
    	 * Always compile the below code with Java 1.7_u51 or below version
    	 * 
    	 */
        try {
        		try {
					Class jdkServicesClass = Class.forName("com.sun.media.sound.JDK13Services");
					 Method jdkServicesmethods[] = jdkServicesClass.getDeclaredMethods();
					 for (int i = 0; i < jdkServicesmethods.length; i++){
						     if(ServletUtils.SET_CACHING_PERIOD_METHOD.equals(jdkServicesmethods[i].toString())){
				            	logger.info("setCachingPeriod method present");
				            		com.sun.media.sound.JDK13Services.setCachingPeriod(1);
				            	break;
				            }
					 }
				} catch (ClassNotFoundException e) {
					logger.info("com.sun.media.sound.JDK13Services not found");
					e.printStackTrace();
				}catch (Exception e) {	
					e.printStackTrace();
				}
        	
            if (!AudioSystem.isLineSupported(info)) {
            //	logger.info("Line not supported");
                isMic=false;
            }
            else{
            	
            		line = (TargetDataLine) AudioSystem.getLine(info);
            	
            	if(line!=null){
            	//	logger.info("Line not null");
            		line.open(format);
            		line.flush();
            		line.start();   // start capturing
            		AudioInputStream ais = new AudioInputStream(line);
			        ais.read(data);
			        ArrayList frame = new ArrayList();
					      for (int i = 0; i < data.length; i++) {
					    	 if (data[i]>0){
					    		 frame.add(new Byte(data[i]));
						  	  }
					      }
					      if(frame.size()>0){
					    	  isMic=true;
					      }
				}
            }
          //  logger.info("Mic Detection complete!!"+System.currentTimeMillis());
	  } catch (LineUnavailableException ex) {
        	logger.info("Inside LineUnavailableException Exception");
        	if (osName.indexOf("win") >= 0) {
        		if(AudioSystem.isLineSupported(Port.Info.MICROPHONE)){
        			//logger.info("Inside Exception win if...");
            		isMic=true;
        		}else{
        			//logger.info("Inside Exception win else...");
            		isMic=false;
        		}
        	}else{
        		if(AudioSystem.isLineSupported(info)){
            		//logger.info("Inside if Mac/Linux");
            		isMic=true;
            	}else{
            	//	logger.info("Inside else Mac/Linux");
            		isMic=false;
            	}
        		
        	}
            //ex.printStackTrace();
        }catch (IllegalArgumentException iae) {
        	logger.info("Inside IllegalArgumentException");
        	if (osName.indexOf("win") >= 0) {
        		if(AudioSystem.isLineSupported(Port.Info.MICROPHONE)){
        			//logger.info("Inside win if");
            		isMic=true;
        		}else{
        			//logger.info("Inside win else");
            		isMic=false;
        		}
        	}else{
        		if(AudioSystem.isLineSupported(info)){
            		//logger.info("Inside if Mac/Linux");
            		isMic=true;
            	}else{
            		//logger.info("Inside else Mac/Linux");
            		isMic=false;
            	}
        		
        	}
            //iae.printStackTrace();
        }catch (IOException io) {
        	//logger.info("Exception*****"+io);
        	io.printStackTrace();
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
    	if(ServletUtils.recordingFailed){
    		ServletUtils.recordingFailed = false;
    		logger.info("************Recording has Failed*********");
    		return "<false />";
    	}
        final Recorder recorder = new Recorder();
        
 		 // creates a new thread that waits for a specified
        // of time before stopping
       /* Thread stopper = new Thread(new Runnable() {
            public void run() {
                try {
                	
                    Thread.sleep(RECORD_TIME);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                recorder.finish();
            }
        });
 
        stopper.start();*/
       // start recording
       // long startTime = System.currentTimeMillis();
        ServletUtils.micDetectionInProgress = true;
        	microphone=recorder.start(); 
       /*try {
        	System.out.println("Sleep started*****");
			Thread.sleep(2000);
        	System.out.println("Sleep stopped*****");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        //System.out.println("In main>>>>"+microphone);
        if(microphone){
        	logger.info("A microphone is attached to your system");
        	
        	recorder.finish();
        	 ServletUtils.micDetectionInProgress = false;
        	// long stopTime = System.currentTimeMillis();
        	// long totalTime = stopTime - startTime - 2000;
        	// System.out.println("totalTime******"+totalTime);
        	 return "<true />";
        	
        	}
        else{
        	logger.info("Microphone is not attached");
        	recorder.finish();
        	ServletUtils.micDetectionInProgress = false;
        	//long stopTime2 = System.currentTimeMillis();
        //	long totalTime = stopTime2 - startTime - 2000;
        //	System.out.println("totalTime******"+totalTime);
        	return "<false />";
       }
       
    }
}