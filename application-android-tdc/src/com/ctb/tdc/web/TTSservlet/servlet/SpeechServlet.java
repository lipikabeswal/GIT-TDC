// Decompiled by DJ v3.6.6.79 Copyright 2004 Atanas Neshkov  Date: 7/31/2012 12:33:25 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 

package com.ctb.tdc.web.TTSservlet.servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import android.speech.tts.TextToSpeech;

import com.ctb.tdc.web.TTSservlet.utils.TTSUtil;
import com.ctb.tdc.web.utils.ServletUtils;

public class SpeechServlet {

	private static final String OK_RESPONSE = "<status>OK</status>";
	private static String text;
	private static String speedValue;
	private static int delayCounter;
	private static long lastDelay;
	public static final int MAX_DELAYS = 3;
	private TextToSpeech mTts = null;

	public String speechRead()

	{
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
		try {
			long l = System.currentTimeMillis();
			com.ctb.tdc.web.TTSservlet.utils.TTSUtil.MP3 mp3 = null;
			String s = TTSUtil.checkCache((new StringBuilder())
					.append(speedValue).append(text).toString());
			if (s != null) {
				System.out.println((new StringBuilder())
						.append("TTS: cache hit, MP3 URL: ").append(s)
						.toString());
				TTSUtil.decryptFile(s.replaceAll(".mp3", ".enc"), s);
			} else {
				HashMap<String, String> map = null;
				map = new HashMap<String, String>();
				map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,
						"callbackId");
				mp3 = TTSUtil.speak(text, speedValue);
				mTts.speak(text, TextToSpeech.QUEUE_ADD, map);
				TTSUtil.cacheFile((new StringBuilder()).append(speedValue)
						.append(text).toString(), mp3);
			}
			s = (String) TTSUtil.mp3CacheMap.get((new StringBuilder())
					.append(speedValue).append(text).toString());
			mp3 = new com.ctb.tdc.web.TTSservlet.utils.TTSUtil.MP3();
			File file = new File(s);
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

			(new File(s)).delete();
			System.out.println("TTS: finished stream, response flushed");
			return byteBuffer.toByteArray().toString();
		} catch (Exception exception) {
			exception.printStackTrace();			
		}
		return byteBuffer.toByteArray().toString();
	}

	public String readPostDataRequest(String xmlData)

	{
		
		
		text = ServletUtils.parseTagRequest("text", xmlData);
		if (text == null)
			TTSUtil.stop();

		speedValue = ServletUtils.parseTagRequest("speedValue", xmlData);
		String ByteStream=this.speechRead();
		return ByteStream;
	}

}