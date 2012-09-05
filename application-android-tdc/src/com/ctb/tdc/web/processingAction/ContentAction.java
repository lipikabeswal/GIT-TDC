package com.ctb.tdc.web.processingAction;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import noNamespace.AdssvcRequestDocument;
import noNamespace.AdssvcResponseDocument;
import noNamespace.ErrorDocument;

import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.Base64;
import org.jdom.Attribute;
import org.jdom.Content;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import android.content.Context;
import android.os.Environment;

import com.bea.xml.XmlException;
import com.ctb.tdc.web.dto.SubtestKeyVO;
import com.ctb.tdc.web.exception.BlockedContentException;
import com.ctb.tdc.web.exception.DecryptionException;
import com.ctb.tdc.web.exception.HashMismatchException;
import com.ctb.tdc.web.exception.TMSException;
import com.ctb.tdc.web.utils.AssetInfo;
import com.ctb.tdc.web.utils.CATEngineProxy;
import com.ctb.tdc.web.utils.ContentFile;
import com.ctb.tdc.web.utils.ContentRetriever;
import com.ctb.tdc.web.utils.MemoryCache;
import com.ctb.tdc.web.utils.ServletUtils;

/**
 * @author John_Wang
 */
public class ContentAction {

	private static final Map<Object, Object> itemHashMap = new HashMap<Object, Object>();
	private static final Map<Object, Object> itemKeyMap = new HashMap<Object, Object>();
	public static final Map<Object, Object> itemCorrectMap = new HashMap<Object, Object>();

	public static final Map<Object, Object> itemSubstitutionMap = new HashMap<Object, Object>();
	private static Integer getSubtestCount = 0;
	private static String trackerXml = null;
	private static String currentSubtestId = null;
	private static String currentSubtestHash = null;
	private static Map<Object, Object> trackerStatus = new HashMap<Object, Object>();
	private Boolean ContentDownloaded = false;

	static Logger logger = Logger.getLogger(ContentAction.class);
	

	/**
	 * Get subtest from TMS
	 * 
	 * 
	 * @param XMLData
	 *            response - checks if subtest is currently on system and is up
	 *            to date using key - if content is not current, request content
	 *            for this subtest id from TMS - saves encrypted content to
	 *            local directory - return decrypted content to client
	 * 
	 */
	
	

	
	public String getSubtest(String xmlData) throws IOException {
		

		String xml = ServletUtils.getXml(xmlData);
		String subtestId = null;
		String hash;
		String key;
		org.jdom.Document subtestDoc = null;
		org.jdom.Document trackerDoc = null;
		SAXBuilder saxBuilder = new SAXBuilder();

		String xmlSubtestDoc = null;
		try {
			if (xml == null) {
				System.out.println("If XML IS NULL");
				subtestId = ServletUtils.getSubtestId(xmlData);
				hash = ServletUtils.getHash(xmlData);
				key = ServletUtils.getKey(xmlData);
				xml = ServletUtils.buildContentRequest(xmlData,
						ServletUtils.GET_SUBTEST_METHOD);
			} else {
				System.out.println("else XML IS NULL");
				AdssvcRequestDocument document = AdssvcRequestDocument.Factory
						.parse(xml);
				subtestId = document.getAdssvcRequest().getGetSubtest()
						.getSubtestid();
				hash = document.getAdssvcRequest().getGetSubtest().getHash();
				key = document.getAdssvcRequest().getGetSubtest().getKey();
			}

			if (subtestId != null && !"".equals(subtestId.trim())
					&& !"undefined".equals(subtestId.trim())) {
				System.out.println("If subtestId IS not NULL");
				currentSubtestId = subtestId;
				currentSubtestHash = hash;
				// String filePath = ContentFile.getContentFolderPath() +
				// subtestId+ ContentFile.SUBTEST_FILE_EXTENSION;
				// String filePath = ContentFile.getContentFolderPath() +
				// subtestId+ ContentFile.SUBTEST_FILE_EXTENSION;

				String filePath = "ctb/data/objectbank/";

				URL u = new URL(filePath);
				HttpURLConnection con = (HttpURLConnection) u.openConnection();
				con.setRequestMethod("GET");
				con.setDoOutput(true);
				con.connect();
				// FileOutputStream fileOutputStream = new FileOutputStream(new
				// File(Context.MODE_PRIVATE+filePath));

				boolean validHash = false;
				getSubtestCount++;

				MemoryCache aMemoryCache = MemoryCache.getInstance();
				Map subtestInfoMap = (HashMap) aMemoryCache.getSubtestInfoMap();
				String itemSetId = (String) ServletUtils.itemSetMap
						.get(subtestId);
				SubtestKeyVO subtestDetails = (SubtestKeyVO) subtestInfoMap
						.get(itemSetId);

				String isAdaptive = subtestDetails.getAdaptive();
				String cArea = subtestDetails.getContentArea();
				if ("True".equalsIgnoreCase(isAdaptive)) {
					ServletUtils.isCurSubtestAdaptive = true;
					if (getSubtestCount > ServletUtils.itemSetMap.size()) {
						// if(!ServletUtils.isDataDecrypted){
						// ContentFile.deleteDataFiles();
						ContentFile.decryptDataFiles();
						// ServletUtils.isDataDecrypted = true;
						// }
						CATEngineProxy.initCAT(cArea);

					}
				} else {
					ServletUtils.isCurSubtestAdaptive = false;
				}

				try {
					validHash = ContentFile.validateHash(filePath, hash);
					System.out.println("Valid Hash :blockContentDownload "
							+ validHash + "::"
							+ ServletUtils.blockContentDownload);
				} catch (Exception e) {
					validHash = false;
				}

				if (!validHash && !ServletUtils.blockContentDownload) {
					System.out
							.println("not a valid hash and block content download false");
					String result = "";
					AdssvcResponseDocument document = null;
					ErrorDocument.Error error = null;

					logger.info("***** downloadSubtest******* ");
					logger.info("subtestID: XML " + subtestId + "::" + xml);
					result = ServletUtils.httpClientSendRequest(
							ServletUtils.GET_SUBTEST_METHOD, xml);
					document = AdssvcResponseDocument.Factory.parse(result);
					error = document.getAdssvcResponse().getGetSubtest()
							.getError();

					if (error != null) {
						throw new TMSException(error.getErrorDetail());
					}

					byte[] content = document.getAdssvcResponse()
							.getGetSubtest().getContent();
					ContentFile.writeToFile(content, filePath);

					this.ContentDownloaded = false;
				} else {

					this.ContentDownloaded = true;
				}

				if (!validHash && ServletUtils.blockContentDownload) {
					throw new BlockedContentException();
				}

				byte[] decryptedContent = ContentFile.decryptFile(filePath,
						hash, key);
				/*
				 * String subtestXML = new String(decryptedContent);
				 * System.out.println(subtestXML);
				 */
				subtestDoc = saxBuilder.build(new ByteArrayInputStream(
						decryptedContent));
				if (ServletUtils.isCurSubtestAdaptive
						&& (getSubtestCount > ServletUtils.itemSetMap.size())) {
					org.jdom.Element element = (org.jdom.Element) subtestDoc
							.getRootElement();
					org.jdom.Attribute attribute = new Attribute("itemCount",
							"0");
					org.jdom.Element objectElement = element
							.getChild("ob_element_list");
					objectElement.setAttribute("itemCount", new Integer(
							CATEngineProxy.getTestLength()).toString());
					if (ServletUtils.isRestart) {
						org.jdom.Attribute restartAttr = new Attribute(
								"restart_ast", "0");
						List fNodes = objectElement.getChildren("f");
						Element fItem = (Element) fNodes
								.get(ServletUtils.restartItemCount);
						ServletUtils.landingItem = fItem
								.getAttributeValue("id");

						for (int i = 0; i < (ServletUtils.restartItemCount + 1); i++) {
							if (i == ServletUtils.restartItemCount) {
								Element item = (Element) fNodes.get(i);
								String itemEid = item.getAttributeValue("id");
								ServletUtils.landingFnode = itemEid;
								objectElement.setAttribute("restart_ast",
										itemEid);
								break;
							}
						}
					}

				}

				if (!trackerStatus.containsKey(currentSubtestId)) {
					trackerXml = ContentRetriever.getTrackerXML(
							currentSubtestId, currentSubtestHash);
					trackerDoc = saxBuilder.build(new ByteArrayInputStream(
							trackerXml.getBytes()));
					int numberOfFileParts = trackerDoc.getRootElement()
							.getChildren("tracker").size();

					List children = subtestDoc.getRootElement().getChildren();
					Content trackerFiles = null;

					for (int i = 0; i < numberOfFileParts; i++) {

						trackerFiles = trackerDoc.getRootElement()
								.getChild("tracker").detach();
						children.add(trackerFiles);

					}
					trackerStatus.put(currentSubtestId, currentSubtestHash);
				}
				Element root = subtestDoc.getRootElement();
				xmlSubtestDoc = root.toString();

				return xmlSubtestDoc;
			}
		} catch (HashMismatchException e) {
			logger.error("Exception occured in getSubtest(" + subtestId
					+ ") : " + ServletUtils.printStackTrace(e));
			String errorMessage = ServletUtils
					.getErrorMessage("tdc.servlet.error.hashMismatch");
			return xmlSubtestDoc = ServletUtils.writeResponse(ServletUtils
					.buildXmlErrorMessage("", errorMessage, ""));
		} catch (DecryptionException e) {
			logger.error("Exception occured in getSubtest(" + subtestId
					+ ") : " + ServletUtils.printStackTrace(e));
			String errorMessage = ServletUtils
					.getErrorMessage("tdc.servlet.error.decryptionFailed");
			return xmlSubtestDoc = ServletUtils.writeResponse(ServletUtils
					.buildXmlErrorMessage("", errorMessage, ""));
		} catch (TMSException e) {
			logger.error("TMS Exception occured in getSubtest(" + subtestId
					+ ") : " + ServletUtils.printStackTrace(e));
			String errorMessage = ServletUtils
					.getErrorMessage("tdc.servlet.error.getContentFailed");
			return xmlSubtestDoc = ServletUtils.writeResponse(ServletUtils
					.buildXmlErrorMessage("", errorMessage, ""));
		}
		// added for displaying separate error screen for blocked content
		catch (BlockedContentException e) {
			logger.error("Exception occured in getSubtest(" + subtestId
					+ ") : " + ServletUtils.printStackTrace(e));
			String errorMessage = ServletUtils
					.getErrorMessage("tdc.servlet.error.contentBlocked");
			return xmlSubtestDoc = ServletUtils.writeResponse(ServletUtils
					.buildXmlErrorMessage("", errorMessage, "209"));
		} catch (Exception e) {
			logger.error("Exception occured in getSubtest(" + subtestId
					+ ") : " + ServletUtils.printStackTrace(e));
			String errorMessage = ServletUtils
					.getErrorMessage("tdc.servlet.error.getContentFailed");

		}
		return xmlSubtestDoc;
	}

	public String downloadItem(String xmlData) throws IOException {
	xmlData="<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
	               +"<adssvc_request method=\"downloadItem\" sdsid=\"string\" token=\"string\" xmlns=\"\">"
			       +"<download_item itemid=\"\" hash=\"\" key=\"\" />"
	               +"</adssvc_request>";
		String xml = ServletUtils.getXml(xmlData);
		String itemId = null;
		String hash;
		String key;
		String xmlDataReturn = null;
		try {
			if (xml == null) {
				itemId = ServletUtils.getItemId(xmlData);
				hash = ServletUtils.getHash(xmlData);
				key = ServletUtils.getKey(xmlData);
				xml = ServletUtils.buildContentRequest(xmlData,
						ServletUtils.DOWNLOAD_ITEM_METHOD);
			} else {
				AdssvcRequestDocument document = AdssvcRequestDocument.Factory
						.parse(xml);
				itemId = document.getAdssvcRequest().getDownloadItem()
						.getItemid();
				hash = document.getAdssvcRequest().getDownloadItem().getHash();
				key = document.getAdssvcRequest().getDownloadItem().getKey();
			}

			if (itemId != null && !"".equals(itemId.trim())) {
				String filePath = ContentFile.getContentFolderPath() + itemId
						+ ContentFile.ITEM_FILE_EXTENSION;

				boolean hashValid = false;
				try {
					hashValid = ContentFile.validateHash(filePath, hash);
				} catch (Exception e) {
					hashValid = false;
				}

				if (!hashValid && !ServletUtils.blockContentDownload) {
					int errorIndex = 0;
					String result = "";
					int i = 1;
					// logger.info("***** downloadItem " + itemId);
					result = ServletUtils.httpClientSendRequest(
							ServletUtils.DOWNLOAD_ITEM_METHOD, xml);
					errorIndex = result.indexOf("<ERROR>");

					if (errorIndex >= 0) {
						throw new TMSException(result);
					}

					AdssvcResponseDocument document = AdssvcResponseDocument.Factory
							.parse(result);
					ErrorDocument.Error error = document.getAdssvcResponse()
							.getDownloadItem().getError();
					if (error != null)
						throw new TMSException(error.getErrorDetail());

					byte[] content = document.getAdssvcResponse()
							.getDownloadItem().getContent();
					ContentFile.writeToFile(content, filePath);
				} else if (!hashValid && ServletUtils.blockContentDownload) {
					throw new BlockedContentException();
				}

				if (ServletUtils.isCurSubtestAdaptive) {
					String catItemIdPattern = ".TABECAT";
					try {
						byte[] decryptedContent = ContentFile.decryptFile(
								filePath, hash, key);
						String itemXML = new String(decryptedContent);
						// System.out.println(itemXML);
						itemCorrectMap.put(itemId,
						ServletUtils.parseCorrectAnswer(itemXML));
						itemHashMap.put(itemId, hash);
						itemKeyMap.put(itemId, key);
						String iid = ServletUtils.parseItemId(itemXML);
						iid = iid.substring(0,
								iid.length() - catItemIdPattern.length());
						Integer peId = Integer.parseInt(iid);
						Integer adsItemId = Integer.parseInt(itemId);
						// System.out.println("Populating map: " + peId + " :: "
						// +adsItemId);
						CATEngineProxy.itemIdMap.put(String.valueOf(peId),
								Integer.valueOf(adsItemId));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				xmlDataReturn = ServletUtils.writeResponse(ServletUtils.OK);
				return xmlDataReturn;
			}

			return xmlDataReturn;
		} catch (TMSException e) {
			logger.error("TMS Exception occured in downloadItem(" + itemId
					+ ") : " + ServletUtils.printStackTrace(e));
			String errorMessage = ServletUtils
					.getErrorMessage("tdc.servlet.error.getContentFailed");
			ServletUtils.writeResponse(ServletUtils.buildXmlErrorMessage("",
					errorMessage, ""));
		} catch (XmlException e) {
			logger.error("XML Exception occured in downloadItem(" + itemId
					+ ") : " + ServletUtils.printStackTrace(e));
			String errorMessage = ServletUtils
					.getErrorMessage("tdc.servlet.error.getContentFailed");
			ServletUtils.writeResponse(ServletUtils.buildXmlErrorMessage("",
					errorMessage, ""));
		}
		// added for displaying separate error screen for blocked content
		catch (BlockedContentException e) {
			logger.error("Exception occured in downloadItem(" + itemId + ") : "
					+ ServletUtils.printStackTrace(e));
			String errorMessage = ServletUtils
					.getErrorMessage("tdc.servlet.error.contentBlocked");
			ServletUtils.writeResponse(ServletUtils.buildXmlErrorMessage("",
					errorMessage, "209"));
		}
		return xmlDataReturn;
	}

	public String getItem(String xmlData) throws IOException {
		MemoryCache aMemoryCache = MemoryCache.getInstance();
		HashMap assetMap = aMemoryCache.getAssetMap();
		String itemxml = null;
		// clear up image cache for each getItem call
		// assetMap.clear();

		String xml = ServletUtils.getXml(xmlData);
		String itemId = null;
		String hash;
		String key;

		try {
			if (xml == null) {
				itemId = ServletUtils.getItemId(xmlData);
				hash = ServletUtils.getHash(xmlData);
				key = ServletUtils.getKey(xmlData);
			} else {

				itemId = getAttributeValue("itemid", xml);
				hash = getAttributeValue("hash", xml);
				key = getAttributeValue("key", xml);

			}

			String originalItemId = null;
			boolean isRestart = false;
			if (ServletUtils.isCurSubtestAdaptive) {
				if (ServletUtils.isRestart && ServletUtils.landingItem != null) {

					originalItemId = ServletUtils.landingItem;
					itemId = CATEngineProxy.getNextItem();
					itemSubstitutionMap.put(originalItemId, itemId);
					ServletUtils.isRestart = false;
					isRestart = true;
				} else {
					originalItemId = itemId;
					itemId = CATEngineProxy.getNextItem();
					// System.out.println("get item itemId:"+itemId);
					itemSubstitutionMap.put(originalItemId, itemId);
				}
				ServletUtils.currentItem = itemId;

			}
			if (itemId != null) {
				if (ServletUtils.isCurSubtestAdaptive) {
					hash = (String) itemHashMap.get(itemId);
					key = (String) itemKeyMap.get(itemId);
				}

				if (itemId == null || "".equals(itemId.trim())) // invalid item
																// id
					throw new Exception("No item id in request.");

				String filePath = Environment
						.getExternalStorageDirectory()
						.getAbsolutePath()
						.concat("ctb/data/objectbank/" + itemId
								+ ContentFile.ITEM_FILE_EXTENSION);
				byte[] decryptedContent = ContentFile.decryptFile(filePath,
						hash, key);
				if (decryptedContent == null)
					throw new DecryptionException("Cannot decrypt '" + filePath
							+ "'");
				org.jdom.Document itemDoc = null;
				synchronized (aMemoryCache.saxBuilder) {
					itemDoc = aMemoryCache.saxBuilder
							.build(new ByteArrayInputStream(decryptedContent));
				}
				org.jdom.Element element = (org.jdom.Element) itemDoc
						.getRootElement();
				element = element.getChild("assets");
				if (element != null) {
					List imageList = element.getChildren();
					for (int i = 0; i < imageList.size(); i++) {
						element = (org.jdom.Element) imageList.get(i);
						String imageId = element.getAttributeValue("id");
						if (!assetMap.containsKey(imageId)) {
							String mimeType = element.getAttributeValue("type");
							String ext = mimeType.substring(mimeType
									.lastIndexOf("/") + 1);
							String b64data = element.getText();
							byte[] imageData = Base64.decode(b64data);
							AssetInfo aAssetInfo = new AssetInfo();
							aAssetInfo.setData(imageData);
							aAssetInfo.setExt(ext);
							assetMap.put(imageId, aAssetInfo);
						}
					}
				}
				itemxml = updateItem(decryptedContent, assetMap);
				itemxml = ServletUtils.doUTF8Chars(itemxml);
				// System.out.println(" original get Item xml:"+itemxml);
				if (ServletUtils.isCurSubtestAdaptive) {
					itemxml = itemxml.replaceAll(itemId, originalItemId);
				}

			} else {
				if (ServletUtils.isCurSubtestAdaptive) {
					System.out.println("CAT Over!");
					logger.info("CAT Over!");
					ServletUtils.writeResponse(ServletUtils
							.buildXmlErrorMessage("CAT OVER", "Ability: "
									+ CATEngineProxy.getAbilityScore()
									+ ", SEM: " + CATEngineProxy.getSEM(),
									"000"));
					CATEngineProxy.deInitCAT();
				}
			}
			return itemxml;
		} catch (HashMismatchException e) {
			logger.error("Exception occured in getItem(" + itemId + ") : "
					+ ServletUtils.printStackTrace(e));
			String errorMessage = ServletUtils
					.getErrorMessage("tdc.servlet.error.hashMismatch");
			ServletUtils.writeResponse(ServletUtils.buildXmlErrorMessage("",
					errorMessage, ""));
		} catch (DecryptionException e) {
			logger.error("Exception occured in getItem(" + itemId + ") : "
					+ ServletUtils.printStackTrace(e));
			String errorMessage = ServletUtils
					.getErrorMessage("tdc.servlet.error.decryptionFailed");
			ServletUtils.writeResponse(ServletUtils.buildXmlErrorMessage("",
					errorMessage, ""));
		} catch (Exception e) {
			logger.error("Exception occured in getItem(" + itemId + ") : "
					+ ServletUtils.printStackTrace(e));
			String errorMessage = ServletUtils
					.getErrorMessage("tdc.servlet.error.getContentFailed");
			ServletUtils.writeResponse(ServletUtils.buildXmlErrorMessage("",
					errorMessage, ""));
		}
		return itemxml;

	}

	public void downloadFileParts(String xmlData) throws IOException {

		try {
			if (!this.ContentDownloaded) {
				String xml = ServletUtils.getXml(xmlData);
				String downloadFilePart = getAttributeValue("name", xml);
				String sequence_number = getAttributeValue("sequence_number",
						xml);
				String next = getAttributeValue("next", xml);
				String status = ContentRetriever.getContent(downloadFilePart);
				System.out.println("Download File Parts: " + downloadFilePart
						+ " :: " + sequence_number + " :: " + next);
				if (next.equalsIgnoreCase("NULL")) {
					ContentRetriever.mergeFile(trackerXml, currentSubtestId,
							currentSubtestHash);
					ContentRetriever.unCompressFile(currentSubtestId,
							currentSubtestHash);
					deleteFile(ServletUtils.tempPath + currentSubtestId + "$"
							+ currentSubtestHash + ".zip");

				}
			}
			ServletUtils.writeResponse(ServletUtils.FILE_PART_OK);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = ServletUtils
					.getErrorMessage("tdc.servlet.error.getContentFailed");
			ServletUtils.writeResponse(ServletUtils.buildXmlErrorMessage("",
					errorMessage, ""));
		}

	}

	public String updateItem(byte[] itemBytes, HashMap assetMap)
			throws Exception {
		MemoryCache aMemoryCache = MemoryCache.getInstance();
		org.jdom.Document itemDoc = null;
		synchronized (aMemoryCache.saxBuilder) {
			itemDoc = aMemoryCache.saxBuilder.build(new ByteArrayInputStream(
					itemBytes));
		}
		org.jdom.Element rootElement = (org.jdom.Element) itemDoc
				.getRootElement();
		if (rootElement.getChild("assets") != null)
			rootElement.getChild("assets").detach();
		List items = ServletUtils.extractAllElement(".//image_widget",
				rootElement);
		for (int i = 0; i < items.size(); i++) {
			org.jdom.Element element = (org.jdom.Element) items.get(i);
			String id = element.getAttributeValue("image_ref");
			if (id != null && assetMap.containsKey(id))
				element.setAttribute("src", id);
		}
		XMLOutputter aXMLOutputter = new XMLOutputter();
		StringWriter aStringWriter = new StringWriter();
		aXMLOutputter.output(rootElement, aStringWriter);
		return aStringWriter.getBuffer().toString();
	}

	/**
	 * Get iamge from memory cache
	 * 
	 * @param HttpServletRequest
	 *            request
	 * @param HttpServletResponse
	 *            response - get image from memory cache
	 * 
	 */
	public String getImage(String xmlData) throws IOException {
		MemoryCache aMemoryCache = MemoryCache.getInstance();
		HashMap assetMap = aMemoryCache.getAssetMap();

		String xml = ServletUtils.getXml(xmlData);
		String imageId;

		try {
			if (xml == null) {
				imageId = ServletUtils.getImageId(xmlData);
			} else {
				AdssvcRequestDocument document = AdssvcRequestDocument.Factory
						.parse(xml);
				imageId = document.getAdssvcRequest().getGetImage()
						.getImageid();
			}

			if (imageId == null || "".equals(imageId.trim())) // invalid image
																// id
				throw new Exception("No image id in request.");

			if (!assetMap.containsKey(imageId))
				throw new Exception(
						"Image with id '"
								+ imageId
								+ "' not found in memory cache. Please call getItem before getImage.");

			AssetInfo assetInfo = (AssetInfo) assetMap.get(imageId);
			if (assetInfo == null)
				throw new Exception(
						"Image with id '"
								+ imageId
								+ "' not found in memory cache. Please call getItem before getImage.");
			String MIMEType = assetInfo.getMIMEType();
			// response.setContentType( MIMEType );
			byte[] data = assetInfo.getData();
			int size = data.length;
			return data.toString();

		} catch (Exception e) {
			logger.error("Exception occured in getImage() : "
					+ ServletUtils.printStackTrace(e));
			String errorMessage = ServletUtils
					.getErrorMessage("tdc.servlet.error.getContentFailed");
			return ServletUtils.buildXmlErrorMessage("", errorMessage, "");
		}
	}

	public String getLocalResource(String requestXml) throws IOException {
		// String filename = request.getParameter("resourcePath");
		String filename = ServletUtils.parseTagRequest("resourcePath",
				requestXml);

		try {

			if (filename == null || "".equals(filename.trim()))
				throw new Exception("No  in request.");

			String filePath = this.RESOURCE_FOLDER_PATH + File.separator
					+ filename;

			FileInputStream input = new FileInputStream(filePath.replaceAll(
					".swf", ".enc"));
			int size = input.available();
			byte[] src = new byte[size];
			input.read(src);
			input.close();

			byte[] decrypted = ContentFile.decrypt(src);

			int index = filename.lastIndexOf(".");
			String ext = filename.substring(index + 1);
			AssetInfo assetInfo = new AssetInfo();
			assetInfo.setExt(ext);
			String mimeType = assetInfo.getMIMEType();

			return decrypted.toString();

		} catch (Exception e) {
			logger.error("Exception occured in getLocalResource() : "
					+ ServletUtils.printStackTrace(e));
			return ServletUtils.ERROR;
		}
	}

	private String getAttributeValue(String attributeName, String xml) {
		String result = null;
		int startIndex = xml.indexOf(attributeName);
		if (startIndex >= 0) {
			startIndex += attributeName.length();
			startIndex = xml.indexOf("\"", startIndex);
			startIndex += 1;
			int endIndex = xml.indexOf("\"", startIndex);
			if (endIndex > startIndex)
				result = xml.substring(startIndex, endIndex);
		}
		return result;

	}

	public static final String TDC_HOME = "tdc.home";
	public static final String RESOURCE_FOLDER_PATH = System
			.getProperty(TDC_HOME)
			+ File.separator
			+ "webapp"
			+ File.separator
			+ "resources";

	public String getMusicData(String xmlData) throws IOException {

		String musicId = ServletUtils.parseTagRequest("musicId", xmlData);
		String filePath = RESOURCE_FOLDER_PATH + File.separator + "music_"
				+ musicId + ".mp3";
		// PrintWriter out = null;
		String result = null;
		File f1 = new File(filePath);

		if (!f1.exists()) {

			try {
				InputStream input = ServletUtils.httpClientSendRequestBlob(
						ServletUtils.LOAD_MUSIC_DATA_METHOD, musicId);

				FileOutputStream os = new FileOutputStream(filePath);
				byte[] buffer = new byte[1024];
				for (int length = 0; (length = input.read(buffer)) != -1;) {
					os.write(buffer, 0, length);
				}
				os.close();
			} catch (Exception e) {
				result = ServletUtils.buildXmlErrorMessage("", e.getMessage(),
						"");
			}
		}

		result = "<result>File_Downloaded</result>";

		return result;

	}

	private static void deleteFile(String filename) {
		// ContextWrapper cw = new ContextWrapper(Context. );
		File f = new File(Context.MODE_PRIVATE + ("/") + filename);

		if (f.exists()) {
			System.gc();
			Boolean status = f.delete();
			System.out.println("Deleting Content servlet file" + filename
					+ " :: " + status);

		}

	}
}
