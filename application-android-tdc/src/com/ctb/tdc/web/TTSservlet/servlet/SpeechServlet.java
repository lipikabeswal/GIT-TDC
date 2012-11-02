// Decompiled by DJ v3.6.6.79 Copyright 2004 Atanas Neshkov  Date: 7/31/2012 12:33:25 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 

package com.ctb.tdc.web.TTSservlet.servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.TargetApi;
import android.media.MediaPlayer;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.ctb.tdc.web.TTSservlet.utils.TTSUtil;
import com.ctb.tdc.web.TTSservlet.utils.TTSUtil.MP3;

@TargetApi(4)
public class SpeechServlet {

	private static final String OK_RESPONSE = "<status>OK</status>";
	private static String text;
	private static String speedValue;
	private static int delayCounter;
	private static long lastDelay;
	public static final int MAX_DELAYS = 3;
	private TextToSpeech mTts = null;
	MediaPlayer mediaPlayer = new MediaPlayer();
	public String speechRead()

	{
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
		try {
			long l = System.currentTimeMillis();
		
			MP3 mp3 = null;
			String s = TTSUtil.checkCache((new StringBuilder())
					.append(speedValue).append(text).toString());
			if (s != null) {
				System.out.println((new StringBuilder())
						.append("TTS: cache hit, MP3 URL: ").append(s)
						.toString());
				//TTSUtil.decryptFile(s.replaceAll(".mp3", ".enc"), s);
				TTSUtil.decryptWithKye(s.replaceAll(".mp3", ".enc"), s);
			} else {
				mp3 = TTSUtil.speak(text, speedValue);
				TTSUtil.cacheFile((new StringBuilder()).append(speedValue)
						.append(text).toString(), mp3);
			}
			s = (String) TTSUtil.mp3CacheMap.get((new StringBuilder())
					.append(speedValue).append(text).toString());
			mp3 = new MP3();
			this.MP3Play(s);
			Environment.getExternalStorageState();
			//File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator + "Android" + File.separator + s);
			File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator + s);
			//File file = new File("/mnt/sdcard"+ File.separator + "Android" + File.separator + s);
			mp3.setLength(file.length());
			mp3.setStream(new FileInputStream(file));
			long l1 = System.currentTimeMillis();
			if (l1 - l > 10000L && l1 - lastDelay < 60000L) {
				delayCounter++;
				lastDelay = l1;
			}
			if (delayCounter >= 3)
				throw new Exception("Repeated TTS delays ");

			int i = Integer.parseInt(Long.toString(mp3.getLength()));

			InputStream inputstream = mp3.getStream();
			byteBuffer = new ByteArrayOutputStream();
			int len = 0;
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];

			while ((len = inputstream.read(buffer)) != -1) {
				byteBuffer.write(buffer, 0, len);
			}
			Environment.getExternalStorageState();
			//File file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/"+ s);
			File file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ s);
			///File file1 = new File("/mnt/sdcard"+ File.separator + "Android" + File.separator + s);
			
			file1.delete();
		
			
			return byteBuffer.toByteArray().toString();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return byteBuffer.toByteArray().toString();
	}

	public void MP3Play(String mp3File) {
		try {
			FileDescriptor fd = null;
			Environment.getExternalStorageState();
			File baseDir = Environment.getExternalStorageDirectory();
			//File baseDir=new File("/mnt/sdcard");
		//	String audioPath = baseDir.getAbsolutePath() +"/Android/" + mp3File;
			String audioPath = baseDir.getAbsolutePath() +"/" + mp3File;
			FileInputStream fis = new FileInputStream(audioPath);
			fd = fis.getFD();
			if (fd != null) {
				//MediaPlayer mediaPlayer = new MediaPlayer();
				mediaPlayer.setDataSource(fd);
				mediaPlayer.prepare();
				mediaPlayer.start();
				
			}
			fis.close();
		} catch (FileNotFoundException ef) {
			Log.e("file not found ", ef.getMessage());
		} catch (IOException ei) {
			Log.e("IOException ", ei.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			Log.e("Error", ex.getMessage());
		}

	}
public void stopSpeech(){
	mediaPlayer.stop();
}
	public String readPostDataRequest(String reqData,String speedVal)

	{
		String ByteStream=null;
		//xmlData=TestXmlSample.getTTSData();

		//text = TTSUtil.parseTagRequest("text", xmlData);
		//speedValue = TTSUtil.parseTagRequest("speedValue", xmlData);
		text = reqData;
		speedValue = speedVal;
		if (text.equals("stopReading"))
			stopSpeech();
		else
		 ByteStream = this.speechRead();
		return ByteStream;
	}

}