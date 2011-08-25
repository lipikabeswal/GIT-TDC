package com.ctb.tdc.web.servlet;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.log4j.Logger;

import com.ctb.tdc.web.dto.ServletSettings;
import com.ctb.tdc.web.utils.AssetInfo;
import com.ctb.tdc.web.utils.AuditFile;
import com.ctb.tdc.web.utils.MemoryCache;
import com.ctb.tdc.web.utils.ServletUtils;
 
/**
 * @author Tai_Truong
 */
public class UtilityServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    static Logger logger = Logger.getLogger(PersistenceServlet.class);
    private static boolean LDBShutdown = false;
    private static boolean HTTPClientShutdown = false;
    
	/**
	 * Constructor of the object.
	 */
	public UtilityServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

        String method = ServletUtils.getMethod(request);
        
        long startTime = System.currentTimeMillis();
        
        if (method.equals("deleteAuditFile")) {
            deleteAuditFile(request);
            ServletUtils.writeResponse(response, ServletUtils.OK);
        }
        else
        if (method.equals("auditFileGetLineCount")) {
            int lineCount = auditFileGetLineCount(request);
            String str = String.valueOf(lineCount);
            ServletUtils.writeResponse(response, str);
        }
        else
        if (method.equals("auditFileExists")) {
            boolean exists = auditFileExists(request);
            String str = String.valueOf(exists);
            ServletUtils.writeResponse(response, str);
        }
        else
        if (method.equals("servletSetting")) {
            servletSetting(request);
            ServletUtils.writeResponse(response, ServletUtils.OK);
        }
        else
        if (method.equals("getLocalResource")) {
                getLocalResource(request,response);
        }
        else
        if (method.equals("exit")) {
        	logger.info("Exit called");
        	
        	exit();
        }    
        
        logger.info("UtilityServlet: " + method + " took " + (System.currentTimeMillis() - startTime) + "\n");
    }
	
	public static synchronized void exit() {
		try {
        	new KillThem().start();
        	new KillMe().start();
        	
        	int i = 0;
        	while (i < 8 && (!HTTPClientShutdown || !LDBShutdown)) {
        		Thread.sleep(250);
        		i++;
        	}
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
    		System.exit(0);
    	}
	}
	
	private static class KillThem extends Thread {
		public void run() {
			try {
				if(isLinux()) {
	        		Runtime.getRuntime().exec("killall OASTDC");
	        		Thread.sleep(250);
	        		Runtime.getRuntime().exec("killall OASTDC");
	        	} else if(isMacOS()) {
	        		Runtime.getRuntime().exec("killall -KILL LockDownBrowser");
	        		Thread.sleep(250);
	        		Runtime.getRuntime().exec("killall -KILL LockDownBrowser");
	        	} else {
	        		try {
		        		Runtime.getRuntime().exec("taskkill /IM \"LockdownBrowser.exe\"");
		        		Thread.sleep(250);
		        		Runtime.getRuntime().exec("taskkill /IM \"LockdownBrowser.exe\"");
	        		} catch (Exception e) {
	        			e.printStackTrace();
	        		}
	        		try {
	        			Runtime.getRuntime().exec("tskill \"LockdownBrowser\"");
		        		Thread.sleep(250);
		        		Runtime.getRuntime().exec("tskill \"LockdownBrowser\"");
	        		} catch (Exception e) {
	        			e.printStackTrace();
	        		}	
	        	}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				LDBShutdown = true;
			}
		}
	}
	
	private static class KillMe extends Thread {
		public void run() {
			try {
				ServletUtils.shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				HTTPClientShutdown = true;
			}
		}
	}
	
    private static boolean isMacOS() {
        String os = System.getProperty("os.name");
        if (os == null) 
            os = "";
        return ( os.toLowerCase().indexOf("mac") != -1 );
    }
    
    private static boolean isLinux() {
        String os = System.getProperty("os.name");
        if (os == null) 
            os = "";
        return ( os.toLowerCase().indexOf("linux") != -1 );
    }

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	    doGet(request, response);
	}
 
	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occure
	 */
	public void init() throws ServletException {
		// do nothing
	}
  
    private void servletSetting(HttpServletRequest request) {

        MemoryCache memoryCache = MemoryCache.getInstance();
        ResourceBundle rbTdc = ResourceBundle.getBundle(ServletUtils.SERVLET_NAME);
        ResourceBundle rbProxy = ResourceBundle.getBundle(ServletUtils.PROXY_NAME);
        ServletSettings srvSettings = new ServletSettings(rbTdc, rbProxy);

        String tmsPersist = request.getParameter("tmsPersist");
        if (tmsPersist != null) {
            srvSettings.setTmsPersist(tmsPersist.equals("true"));
        }
        String tmsAckRequired = request.getParameter("tmsAckRequired");
        if (tmsAckRequired != null) {
            srvSettings.setTmsAckRequired(tmsAckRequired.equals("true"));
        }
        String proxyHost = request.getParameter("proxyHost");
        if (proxyHost != null) {
            srvSettings.setProxyHost(proxyHost);
        }
        String proxyPort = request.getParameter("proxyPort");
        if (proxyPort != null) {
            srvSettings.setProxyPort(Integer.parseInt(proxyPort));
        }
        String proxyUserName = request.getParameter("proxyUserName");
        if (proxyUserName != null) {
            srvSettings.setProxyUserName(proxyUserName);
        }
        String proxyPassword = request.getParameter("proxyPassword");
        if (proxyPassword != null) {
            srvSettings.setProxyPassword(proxyPassword);
        }
        
        memoryCache.setSrvSettings(srvSettings);       
        memoryCache.setLoaded(true);
    }
    
    private void deleteAuditFile(HttpServletRequest request) {
        String fileName = request.getParameter("fileName");
        String tdcHome = System.getProperty(AuditFile.TDC_HOME);
        fileName = tdcHome + AuditFile.AUDIT_FOLDER + fileName;

        try {
            AuditFile.deleteLogger(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }                      
    }
    
    private boolean auditFileExists(HttpServletRequest request) {
        boolean exists = false;
        String fileName = request.getParameter("fileName");
        String tdcHome = System.getProperty(AuditFile.TDC_HOME);
        fileName = tdcHome + AuditFile.AUDIT_FOLDER + fileName;

        try {
            exists = AuditFile.exists(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }      
        return exists;
    }
    
    private int auditFileGetLineCount(HttpServletRequest request) throws IOException {
        String fileName = request.getParameter("fileName");
        String tdcHome = System.getProperty(AuditFile.TDC_HOME);
        fileName = tdcHome + AuditFile.AUDIT_FOLDER + fileName;
        
        String buff = null;
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        int lineCount = 0;
        while((buff = reader.readLine()) != null) {
            lineCount++;
        }
        reader.close();
        return lineCount;
    }
    
    
    private void getLocalResource(HttpServletRequest request,HttpServletResponse response) throws IOException {
    	String filename = request.getParameter("resourcePath");
    	
    	
    	try {
    		
		    	if (filename == null || "".equals(filename.trim())) 
					throw new Exception("No  in request.");
				
				String filePath = this.RESOURCE_FOLDER_PATH + File.separator  + filename;
				          
				//System.out.println("Image filepath : " + filePath);
                int index= filename.lastIndexOf(".");
                //System.out.println(index);
				String ext = filename.substring(filename.lastIndexOf(".")+1);
				//String ext = filename.substring(filename.lastIndexOf("."),3);
				//System.out.println("ext" + ext);
				AssetInfo assetInfo = new AssetInfo();
				assetInfo.setExt(ext);
				String mimeType = assetInfo.getMIMEType();
				
				
				FileInputStream fstream = new FileInputStream(filePath);
				DataInputStream in = new DataInputStream(fstream);
				
				ServletOutputStream myOutput = response.getOutputStream();
				byte[] data = new byte[4096];
				int cnt = 0;
				int size = 0;
				response.setContentType(mimeType);
				while ((cnt = in.read(data, 0, 4096)) == 4096) {
					size += cnt;
					myOutput.write( data );
				}
				size += cnt;
				size = ((size / 4096) + 1) * 4096;
				myOutput.write( data );
				in.close();
		       
		        response.setContentLength( size ); 
		        myOutput.flush();
		        myOutput.close();	
	        
	 } catch (Exception e) {
		logger.error("Exception occured in getImage() : "
				+ ServletUtils.printStackTrace(e));
		ServletUtils.writeResponse(response, ServletUtils.ERROR);
	 }
   }

public static final String TDC_HOME = "tdc.home";
public static final String RESOURCE_FOLDER_PATH = System.getProperty(TDC_HOME) + File.separator + 
	                             "webapp" + File.separator + "resources";

   

    
}
