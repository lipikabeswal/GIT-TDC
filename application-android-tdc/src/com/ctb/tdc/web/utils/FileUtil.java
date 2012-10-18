package com.ctb.tdc.web.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.jdom.JDOMException;

import android.os.Environment;

import com.ctb.tdc.web.dto.TrackerData;



public class FileUtil {
	static final byte[] buffer = new byte[8 * 1024];


	public static void mergeFile(String destFileName, String trckerXml,
			String sourcePath, String destinationPath) throws JDOMException,
			IOException {
		TreeMap<Long, TrackerData> map = XMLUtil.getContentList(trckerXml);
		
		FileOutputStream output = new FileOutputStream(sourcePath + destFileName);
		/*for (Map.Entry<Long, TrackerData> entry : map.entrySet()) {
			BufferedInputStream input = getFileInputStream(destinationPath
					+ entry.getValue().getValue());
			int bytesRead;
			while ((bytesRead = input.read(buffer)) != -1) {
				output.write(buffer, 0, bytesRead);
			}
			closeResource(input);
		}*/
		Long start = 1L;
		
		do {
			TrackerData data =  map.get(start);
			BufferedInputStream input = getFileInputStream(destinationPath+data.getValue());
			int bytesRead;
			while ((bytesRead = input.read(buffer)) != -1) {
				output.write(buffer, 0, bytesRead);
			}
			closeResource(input);
			
			start = data.getNext();
			deleteFile(destinationPath+data.getValue());
		} while (start != null );
		
		
		
		closeResource(output);

	}

	private static BufferedInputStream getFileInputStream(String filename)
			throws FileNotFoundException {
		return new BufferedInputStream(new FileInputStream(new File(filename)));

	}

	public static void unCompressFile(String sourcefile, String sourcePath,
			String destinationPath) throws IOException {
		ZipInputStream in = new ZipInputStream(new FileInputStream(sourcePath
				+ sourcefile));
		ZipFile zf = new ZipFile(sourcePath + sourcefile);
		OutputStream out = null;
		int a = 0;
		for (Enumeration<? extends ZipEntry> em = zf.entries(); em
				.hasMoreElements();) {
			String targetfile = destinationPath + em.nextElement().toString();
			in.getNextEntry();
			out = new FileOutputStream(targetfile);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			a = a + 1;
		}
		if (a > 0)
			System.out.println("Files unzipped.");
		closeResource(out);
		closeResource(in);
		deleteFile(sourcePath + sourcefile);

	}

	public static boolean isFileExists(String filename) {

		File f = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(),filename);
		return f.exists();

	}

	static void closeResource(Closeable out) {
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
			}
		}
	}

	public static String readFileIntoString(String filePath) throws IOException {
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory().getAbsoluteFile()+filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		try {
			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
				buf = new char[1024];
			}
		} finally {
			closeResource(reader);
		}

		return fileData.toString();

	}

	public static void saveContentIntoFile(String fileName, String trckerXml)
			throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
		try {
			out.write(trckerXml);
		} finally {
			closeResource(out);
		}

	}

	public static void saveHttpDownloadContentIntoFile(DataInputStream input,
			String filename) throws IOException {
		OutputStream output = null;
		boolean isSuccessfullyDownLoaded = false;
		byte[] buffer = new byte[8 * 1024];
		int bytesRead;
		output = new FileOutputStream(filename);

		try {
			while ((bytesRead = input.read(buffer)) != -1) {
				output.write(buffer, 0, bytesRead);
			}
			isSuccessfullyDownLoaded = true;
		} finally {
			closeResource(output);
			if (!isSuccessfullyDownLoaded){
				deleteFile(filename);
			}
		}

	}

	private static void deleteFile(String filename) {
		File f = new File(filename);
		if(f.exists()) {
			System.gc();
			Boolean status = f.delete();
			System.out.println("Deleting file" + filename +" :: " + status );
			
		}
		
	}
}
