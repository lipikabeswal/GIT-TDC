package com.ctb.tdc.web.servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import org.apache.log4j.Logger;
import org.xiph.speex.spi.SpeexEncoding;
import org.xiph.speex.spi.SpeexFileFormatType;

import com.ctb.tdc.web.utils.Base64;
import com.ctb.tdc.web.utils.ServletUtils;

/**
 * @author Santhana R S
 *
 */

public class SoundRecorder extends HttpServlet {

	private static final long serialVersionUID = 1L;

	static protected boolean running;
	static ByteArrayOutputStream out;
	static String fileName;
	AudioFormat audioFormat;
	TargetDataLine targetDataLine;
	static Thread myThread = null;
	String osName = System.getProperty("os.name").toLowerCase();
	static Logger logger = Logger.getLogger(SoundRecorder.class);
	String result = ServletUtils.ERROR;

	public SoundRecorder() {

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String method = request.getParameter("method");
		fileName = request.getParameter("filename");

		//System.out.println("Filename will be..." + fileName + ".spx");

		if ("record".equalsIgnoreCase(method)) {
			//System.out.println("record start");
			captureAudio(response);
		} else if ("stop".equalsIgnoreCase(method)) {
			stopCapture(request, response);
		} else if ("reset".equalsIgnoreCase(method)) {
			resetAudio(response);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getParameter("method");
		//System.out.println(method);
		doGet(request, response);
	}

	/**
	 * 
	 * captureAudio() Defines the Audio Format of the TargetDataLine and calls the CaptureThread class
	 * 
	 * 
	 * @param response
	 */
	private String captureAudio(HttpServletResponse response) {

		try {
			//Get things set up for capture
			if (osName.indexOf("win") >= 0) {
				this.audioFormat = getWindowsAudioFormat();
			} else {
				this.audioFormat = getMacAudioFormat();
			}
			DataLine.Info dataLineInfo = new DataLine.Info(
					TargetDataLine.class, audioFormat);
			targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);

			PrintWriter out = response.getWriter();
			out.write("<result>RECORDING_START</result>");
			out.flush();
			myThread = new CaptureThread();
			myThread.start();
			//System.out.println("*********Capture Start**");

		} catch (Exception e) {
			//e.printStackTrace();
			result = ServletUtils.buildXmlErrorMessage("", e.getMessage(), "");
		}
		return result;
	}

	/**
	 * 
	 * stopCapture() stops the recording. It closes the TargetDataLine and
	 * Calls the generateBase64String() to generate the encode the byteArray
	 * to Base64String
	 *  
	 * @param request
	 * @param response
	 */
	private String stopCapture(HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("stopCapture called....");
		try {

			targetDataLine.stop();
			targetDataLine.drain();
			targetDataLine.close();
			
			//this part has been moved to persistence servlet, session is not required		
			//base64String = generateBase64String();
			//request.getSession().setAttribute("encodedString", base64String);
		} catch (Exception e) {
			//	e.printStackTrace();
			result = ServletUtils.buildXmlErrorMessage("", e.getMessage(), "");
		} finally {
			PrintWriter out = null;

			try {
				System.out.println("RECORDING_STOP......");
				out = response.getWriter();
				out.write("<result>RECORDING_STOP</result>");
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				result = ServletUtils.buildXmlErrorMessage("", e.getMessage(),
						"");
			}
		}
		return result;
	}

	/***
	 * generateBase64String() helps in generating the base64 encoded String from 
	 * the Audio File
	 * 
	 * 
	 * @return
	 */
/*	private String generateBase64String() {
		String base64EncodedString = "";
		try {
			File file = new File(getServletContext().getRealPath("/")
					+ "//streams//" + fileName + ".spx");

			FileInputStream fis = new FileInputStream(file);

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			try {
				for (int readNum; (readNum = fis.read(buf)) != -1;) {
					bos.write(buf, 0, readNum); //Initial run so no doubt here it's 0
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			bos.close();
			fis.close();

			byte[] bytes = bos.toByteArray();
			base64EncodedString = Base64.encode(bytes);

		} catch (Exception e) {
			logger.error("Base64String Not Generated");
		}
		return base64EncodedString;

	}
*/
	private String resetAudio(HttpServletResponse response) {
		try {
			System.out.println("resetAudio....");
			boolean fileDeleted = true;
			targetDataLine.stop();
			targetDataLine.drain();
			targetDataLine.close();

			File SpeexFile = new File(getServletContext().getRealPath("/")
					+ "//streams//" + fileName + ".spx");
			if(SpeexFile.exists()){
				fileDeleted = SpeexFile.delete();
			}
			if (!fileDeleted) {
				logger.warn( "Audio" + fileName+".spx is not Deleted");
			}
			PrintWriter out = response.getWriter();
			out.write("<result>AUDIO_DELETED</result>");
			out.flush();

		} catch (Exception e) {
			result = ServletUtils.buildXmlErrorMessage("", e.getMessage(), "");
		}
		return result;
	}

	/**
	 * 
	 * getWindowsAudioFormat()- Audio Format to be set on target data line for Windows
	 * 
	 * @return
	 */
	private AudioFormat getWindowsAudioFormat() {
		float sampleRate = 44100.0F;
		int sampleSizeInBits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,
				bigEndian);
	}

	/***
	 * getMacAudioFormat()- Audio Format to be set on target data line for Mac
	 * 
	 * @return
	 */
	private AudioFormat getMacAudioFormat() {
		float sampleRate = 44100.0F;
		int sampleSizeInBits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,
				bigEndian);
	}

	/***
	 * Thread that is responsible for getting the data from the Target Data Line in Speex Format
	 * 
	 * 
	 * @author Santhana R S
	 *
	 */
	class CaptureThread extends Thread {
		public void run() {
			AudioFileFormat.Type fileType = null;
			File audioFile = null;
			float sampleRate;
			AudioInputStream ais = null;

			if (osName.indexOf("win") >= 0) {
				sampleRate = 44100.0F;

			} else {
				sampleRate = 44100.0F;
			}
			fileType = SpeexFileFormatType.SPEEX;
			audioFile = new File(getServletContext().getRealPath("/")
					+ "//streams//" + fileName + ".spx");
			AudioFormat speexFormat = new AudioFormat(SpeexEncoding.SPEEX_Q5,
					sampleRate, -1, // sample size in bits
					1, -1, // frame size
					-1, // frame rate
					false);
			try {
				ais = new AudioInputStream(targetDataLine);
				ais = AudioSystem.getAudioInputStream(speexFormat, ais);
				targetDataLine.open(audioFormat);
				targetDataLine.start();

				AudioSystem.write(ais, fileType, audioFile);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (ais != null) {
					try {
						ais.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Testing Purpose
	 * 
	 * 
	 */
	/*	private void playAudio() {
	 try {
	 File file = new File("sano1.flv");
	 AudioInputStream stream  = AudioSystem.getAudioInputStream(file);
	 AudioFormat format = stream.getFormat();
	 System.out.println("format :" + format);
	 DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat());
	 Clip clip = (Clip) AudioSystem.getLine(info);
	 clip.open(stream);      		
	 clip.start();
	 } catch (Exception e) {
	 System.err.println("Line unavailable: " + e);
	 System.exit(-4);
	 } 
	 }
	 */

}
