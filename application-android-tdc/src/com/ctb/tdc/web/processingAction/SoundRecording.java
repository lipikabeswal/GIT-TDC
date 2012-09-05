package com.ctb.tdc.web.processingAction;


	

	import java.io.File;
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
	    private static String mFileName = null;


	    private MediaRecorder mRecorder = null;


	    private MediaPlayer   mPlayer = null;

	    private void onRecord(boolean start) {
	        if (start) {
	            startRecording();
	        } else {
	            stopRecording();
	        }
	    }

	    private void onPlay(boolean start) {
	        if (start) {
	            startPlaying();
	        } else {
	            stopPlaying();
	        }
	    }

	    private void startPlaying() {
	        mPlayer = new MediaPlayer();
	        try {
	            mPlayer.setDataSource(mFileName);
	            mPlayer.prepare();
	            mPlayer.start();
	        } catch (IOException e) {
	            Log.e(LOG_TAG, "prepare() failed");
	        }
	    }

	    private void stopPlaying() {
	        mPlayer.release();
	        mPlayer = null;
	    }

	    private void startRecording() {
	        mRecorder = new MediaRecorder();
	        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
	        mRecorder.setOutputFile(mFileName);
	        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

	        try {
	            mRecorder.prepare();
	        } catch (IOException e) {
	            Log.e(LOG_TAG, "prepare() failed");
	        }

	        mRecorder.start();
	    }

	    private void stopRecording() {
	        mRecorder.stop();
	        mRecorder.release();
	        mRecorder = null;
	    }

	    private void captureAudio() {
	        boolean mStartRecording = true;

	                onRecord(mStartRecording);
	                
	                mStartRecording = !mStartRecording;
	           
	    }

	   

	    public void AudioRecordTest() {
	    	Context con=null;
	        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
	        mFileName += "/audiorecordtest.3gp";
	        ContextWrapper cw = new ContextWrapper(con);
	        File directory = cw.getDir("media", Context.MODE_PRIVATE); 
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

