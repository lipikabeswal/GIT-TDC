package com.ctb.tdc.web.processingAction;


	

	import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

import android.content.Context;
import android.content.ContextWrapper;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;


	public class SoundRecording 
	{
	    private static final String LOG_TAG = "AudioRecordTest";
	   
	   // File mFileFolderName = new File( Environment.getExternalStorageDirectory()+
			//	 "/streams/" );
        String mFileFolderName= Environment.getExternalStorageDirectory()+"/streams/" ;
	    private MediaRecorder mRecorder = null;


	    private MediaPlayer   mPlayer = null;

	  
	   

	    private void startPlaying(String fileName) {
	        mPlayer = new MediaPlayer();
	        try {
	            mPlayer.setDataSource(mFileFolderName+fileName+".aac");
	            mPlayer.prepare();
	            mPlayer.start();
	        } catch (IOException e) {
	            Log.e(LOG_TAG, "prepare() failed");
	        }
	    }

	    public void stopPlaying() {
	        mPlayer.release();
	        mPlayer = null;
	    }

	    public void startRecording(String fileName) {
	    	 File psFile = new File(mFileFolderName);
	         if(!psFile.exists()) { 
	         	System.err.println("It seems like parent directory does not exist...");  
	         	if(!psFile.mkdirs())     {         
	         		System.err.println("And we cannot create it..."); 
	         		// we have to return, throw or something else  
	         		} 
	         	} 
	      
	       
	        mRecorder = new MediaRecorder();
	        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
	        mRecorder.setOutputFile(mFileFolderName+fileName+".aac");
	        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

	        try {
	            mRecorder.prepare();
	        } catch (IOException e) {
	            Log.e(LOG_TAG, "prepare() failed");
	        }

	        mRecorder.start();
	    }

	    public void stopRecording() {
	        mRecorder.stop();
	        mRecorder.release();
	        mRecorder = null;
	    }

	  
	  public void reset(){
		  mRecorder.reset();
	  }
	
	    public void onPause() {
	       
	        if (mRecorder != null) {
	            mRecorder.release();
	            mRecorder = null;
	        }

	        if (mPlayer != null) {
	            mPlayer.release();
	            mPlayer = null;
	        }
	    }
	}

