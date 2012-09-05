package com.ctb.tdc.web.processingAction;

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
public class processTest {
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

	 Logger logger = Logger.getLogger("log");
	public String getValue(){
		return "Hi...";
	}

}
