package com.ctb.tdc.web.utils;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.jdom.JDOMException;


import com.ctb.tdc.web.dto.TrackerData;
import com.ctb.tdc.web.utils.ServletUtils;



public class ContentRetriever  {
/*	public void run() {
		try {
			String trckerXml = getTrackerXML();
			TreeMap<Long, TrackerData> map = XMLUtil.getContentList(trckerXml);
			
			for(Map.Entry<Long, TrackerData> entry : map.entrySet()){
				getContent(entry.getValue().getValue());
			}
			mergeFile(trckerXml);
			unCompressFile();
			
			

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}*/

	public static void unCompressFile(String subtestId, String subtestHash) throws IOException {
		String trckerName = subtestId + "$" + subtestHash+ ".zip";
		String sourcePath = ServletUtils.tempPath;
		String destinationPath = ServletUtils.outputPath;
		FileUtil.unCompressFile(trckerName, sourcePath, destinationPath);
		
	}

	public static void mergeFile(String trckerXml,String subtestId, String subtestHash) throws JDOMException, IOException {
		String destFileName = subtestId + "$" + subtestHash+ ".zip";
		String sourcePath = ServletUtils.tempPath;
		String destinationPath = sourcePath;
		FileUtil.mergeFile(destFileName, trckerXml, sourcePath, destinationPath);
	}

	public static String getContent(String filename) throws IOException {
		String tempPath = ServletUtils.tempPath;
		System.out.println("Get Content File name" + filename);
		if (!FileUtil.isFileExists(tempPath + filename)) {
			HttpUtil.downloadContent(filename);
		} 
		return "OK";
	}

	public static String getTrackerXML(String subtestId, String subtestHash) throws Exception {
		String trckerXml;
		System.out.println("trackerXMl + " + subtestId + "::" +subtestHash);
		String trckerName = subtestId + "$" + subtestHash;
		String tempTrackerFilePath = ServletUtils.outputPath
				+ trckerName + ".xml";
		if (FileUtil.isFileExists(tempTrackerFilePath)) {
			trckerXml = FileUtil.readFileIntoString(tempTrackerFilePath);
		} else {
			trckerXml = HttpUtil.getTracker(trckerName,subtestId);
			//FileUtil.saveContentIntoFile(tempTrackerFilePath, trckerXml);
			// Subhendu Remember: create a thread that will clean all existing
			// data
		}
		return trckerXml;
	}
	
}
