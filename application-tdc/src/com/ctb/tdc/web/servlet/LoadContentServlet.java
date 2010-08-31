package com.ctb.tdc.web.servlet;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.Base64;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.ctb.tdc.web.dto.SubtestKeyVO;
import com.ctb.tdc.web.utils.AssetInfo;
import com.ctb.tdc.web.utils.AuditFile;
import com.ctb.tdc.web.utils.ImageInfo;
import com.ctb.tdc.web.utils.MemoryCache;
import com.ctb.tdc.web.utils.ServletUtils;
import com.stgglobal.util.CryptoLE.Crypto;

/**
 * @author Tai_Truong
 */
public class LoadContentServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private static final int phaseOfOperation = 1;
    private static final String globalItemKey = "1u1piyyriN74U55CGnc4k1";
    static Logger logger = Logger.getLogger(LoadContentServlet.class);
	/**
	 * Constructor of the object.
	 */
	public LoadContentServlet() 
	{
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

    /**
     * The doPost method of the servlet. <br>
     *
     * This method is called when a form has its tag value method equals to post.
     * 
     * @param request the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws Exception 
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String method = request.getParameter("method");
        
        long startTime = System.currentTimeMillis();
        
        if ((method != null) && (! method.equals("none"))) {    
            if (method.equals(ServletUtils.LOAD_SUBTEST_METHOD)) {
                String itemSetId = ServletUtils.buildLoadContentParameters(request, method);
                handleEvent(request, response, method, itemSetId, null, null);
            }
            else
            if (method.equals(ServletUtils.LOAD_ITEM_METHOD)) {
                String itemId = ServletUtils.buildLoadContentParameters(request, method);
                handleEvent(request, response, method, null, itemId, null);
            }
            else
            if (method.equals(ServletUtils.LOAD_IMAGE_METHOD)) {
                String imageId = ServletUtils.buildLoadContentParameters(request, method);
                handleEvent(request, response, method, null, null, imageId);
            }
            else if ( method.equals( ServletUtils.LOAD_LOCAL_IMAGE_METHOD ) )
            {
                String fileName = request.getParameter( ServletUtils.LOAD_LOCAL_IMAGE_PARAM );
                getLocalImage( response, fileName );
            }
        }
        else {
            doGet(request, response);            
        }
        
        logger.info("LoadContentServlet: " + method + " took " + (System.currentTimeMillis() - startTime) + "\n");
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
        logger.info("LoadContentServlet: " + method + " . . . ");
        
        if ( method.equals( ServletUtils.LOAD_LOCAL_IMAGE_METHOD ) )
        {
            String fileName = request.getParameter( ServletUtils.LOAD_LOCAL_IMAGE_PARAM );
            getLocalImage( response, fileName );
            return;
        }
        String itemSetId = ServletUtils.getItemSetId(request);
        String itemId = ServletUtils.getItemId(request);
        String imageId = ServletUtils.getImageId(request);
        
        handleEvent(request, response, method, itemSetId, itemId, imageId);
        
        logger.info("took " + (System.currentTimeMillis() - startTime) + "\n");
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
    public void handleEvent(HttpServletRequest request, HttpServletResponse response, 
                            String method, String itemSetId, String itemId, String imageId)
            throws ServletException, IOException {
        
        boolean good = false;
        String filePath = System.getProperty(AuditFile.TDC_HOME) + "/data/objectbank";
                        
        SubtestKeyVO theSubtestKeyVO = null;
        if ( phaseOfOperation > 1 )
        {
            MemoryCache aMemoryCache = MemoryCache.getInstance();
            HashMap subtestInfoMap = aMemoryCache.getSubtestInfoMap();
            if ( subtestInfoMap.containsKey( itemSetId ))
            {
                theSubtestKeyVO = ( SubtestKeyVO )subtestInfoMap.get( itemSetId );
            }
        }
        if (method.equals(ServletUtils.LOAD_SUBTEST_METHOD))
        {
            if ( phaseOfOperation == 1 )
            {
                good = loadSubtest(response, "2203", "1757", "F16FF0BA9F3D0051F8D3630744BA0FCC"
                                        , globalItemKey, filePath);
            }
            else if ( theSubtestKeyVO != null )
            {
                good = loadSubtest( response, theSubtestKeyVO.getAdsItemSetId(),
                        theSubtestKeyVO.getAsmtEncryptionKey(), theSubtestKeyVO.getAsmtHash()
                                , theSubtestKeyVO.getItem_encryption_key(), filePath );
            }   
        }
        else if (method.equals(ServletUtils.LOAD_ITEM_METHOD))
            good = loadItem(response, itemId );        
        else if (method.equals(ServletUtils.LOAD_IMAGE_METHOD))
            good = loadImage(response, imageId );      
        
        if ( !good )
        {
            response.sendError( HttpServletResponse.SC_NO_CONTENT );
        }
    }
    
	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occure
	 */
	public void init() throws ServletException {
		// Put your code here
	}
	
	/**
	 * The updateItem method of the servlet. <br>
	 *
	 * This method removes assest element and add src image attribute.
	 * 
	 * @param item xml bytes
	 * @param image map
	 * @throws Exception if an error occurred
	 */
	private String updateItem( byte[] itemBytes, HashMap AssetTable ) throws Exception
    {
        MemoryCache aMemoryCache = MemoryCache.getInstance();
        org.jdom.Document itemDoc = aMemoryCache.saxBuilder.build( new ByteArrayInputStream( itemBytes ) );
        org.jdom.Element rootElement = (org.jdom.Element) itemDoc.getRootElement();
        rootElement.getChild( "assets" ).detach();
        List items = ServletUtils.extractAllElement( ".//image_widget", rootElement);
        for ( int i = 0; i < items.size(); i++ )
        {
            org.jdom.Element element = ( org.jdom.Element )items.get( i );
            String id = element.getAttributeValue( "image_ref" );
            if ( id != null && AssetTable.containsKey( id ))
                element.setAttribute( "src", id );
        }
        XMLOutputter aXMLOutputter = new XMLOutputter();
        StringWriter aStringWriter = new StringWriter();
        aXMLOutputter.output( rootElement, aStringWriter );
        return aStringWriter.getBuffer().toString();
    }
	
	/**
	 * The handleItem method of the servlet. <br>
	 *
	 * This method decrypts item
	 * 
	 * @param item bank directory
	 * @param item ID
	 * @param item hash value
	 * @param item decryption key
	 * @throws Exception if an error occurred
	 */
	private void handleItem( String bankDir, String itemID, 
	        				String ItemHashKey, String ItemKey ) throws Exception
    {
        String filePath = itemID + ".ecp";
        byte[] buffer = ServletUtils.readFromFile( new File( bankDir, filePath ) );
        MemoryCache aMemoryCache = MemoryCache.getInstance();
        HashMap itemMap = aMemoryCache.getItemMap();
        HashMap assetMap = aMemoryCache.getAssetMap();
        if ( Crypto.checkHash( ItemHashKey, buffer ))
        {
            Crypto aCrypto = new Crypto();
            byte[] result = aCrypto.checkHashAndDecrypt( ItemKey, ItemHashKey, buffer, true, false );           
            org.jdom.Document itemDoc = aMemoryCache.saxBuilder.build( new ByteArrayInputStream( result ) );
            org.jdom.Element element = (org.jdom.Element) itemDoc.getRootElement();
            element = element.getChild( "assets" );
            if ( element != null )
            {
	            List imageList = element.getChildren();
	            for ( int i = 0; i < imageList.size(); i++ )
	            {
	                element = ( org.jdom.Element )imageList.get( i );
	                String imageId = element.getAttributeValue( "id" );
	                if ( !assetMap.containsKey( imageId ))
	                {
	                    String mimeType = element.getAttributeValue( "type" );
	                    String ext = mimeType.substring( mimeType.lastIndexOf( "/" ) + 1 );
	                    String b64data = element.getText();
	                    byte[] imageData = Base64.decode( b64data );
	                    AssetInfo aAssetInfo = new AssetInfo();
	                    aAssetInfo.setData( imageData );
	                    aAssetInfo.setExt( ext );
	                    assetMap.put( imageId, aAssetInfo );
	                }
	            }
	            String itemxml = updateItem( result, assetMap );
	            itemxml = ServletUtils.doUTF8Chars( itemxml );
	            itemMap.put( itemID, itemxml.getBytes() );
            } 
        }
    }

    /**
     *  Load a subtest
     * @param HttpServletResponse
     * @param String obAssessmentId
     * @param String encryptionKey
     * @param String hashValue
     * @param String item decryption key
     * @param String bankDir
     * @throws IOException 
     * 
     *  retrieves encrypted subtest xml for this item set id from local directory
     *  decrypts subtest xml
     *  returns decrypted subtest xml
     *   
     */
    private boolean loadSubtest( HttpServletResponse response, String obAssessmentId
            					, String encryptionKey, String hashValue, String itemKey
            					, String bankDir ) throws IOException 
    {
        boolean result = true; 
        MemoryCache aMemoryCache = MemoryCache.getInstance();
        aMemoryCache.clearContent();
        try
        {
            String fileName = obAssessmentId + ".eam";
            byte[] buffer = ServletUtils.readFromFile( new File( bankDir, fileName ) );
            if ( Crypto.checkHash( hashValue, buffer ))
            {
                Crypto aCrypto = new Crypto();
                byte[] dataValue = aCrypto.checkHashAndDecrypt( encryptionKey, hashValue, buffer, true, false );
                String assxml = new String( dataValue );
                org.jdom.Document itemDoc = aMemoryCache.saxBuilder.build( new ByteArrayInputStream( dataValue ) );
                org.jdom.Element element = (org.jdom.Element) itemDoc.getRootElement();
                List items = element.getChild( "ob_element_list" ).getChildren();
                for ( int i = 0; i < items.size(); i++ )
                {
                    Element item = ( Element )items.get( i );
                    String obItemId = item.getAttributeValue( "id" );
                    String ItemHash = item.getAttributeValue( "h" );
                    String ItemKeyId = item.getAttributeValue( "k" );
                    handleItem( bankDir, obItemId, ItemHash, itemKey );
                }
                response.setContentType("text/xml");
                PrintWriter myOutput = response.getWriter();
                myOutput.print( assxml );
                myOutput.flush();
                myOutput.close();
            }
            else
                result = false;
        }
        catch( Exception e )
        {
            logger.error("Exception in loadSubtest() : " + ServletUtils.printStackTrace(e));
            result = false;
        }     
        return result;
    }

    /**
     *  Load an item
     * @param HttpServletResponse 
     * @param String item Id
     * @throws IOException 
     * 
     *  search item xml from memory cache and set value into response
     *  returns process result
     *   
     */
    private boolean loadItem(HttpServletResponse response, String itemId ) throws IOException {
        boolean result = true; 
        MemoryCache aMemoryCache = MemoryCache.getInstance();
        HashMap itemMap = aMemoryCache.getItemMap();
        if ( itemMap.containsKey( itemId ))
        {
            byte[] dataValue = ( byte[] )itemMap.get( itemId );
            response.setContentType( "text/xml" );
            PrintWriter myOutput = response.getWriter();
            myOutput.print( new String( dataValue, "UTF-8" ) );
            myOutput.flush();
            myOutput.close();
        }
        else
            result = false;
        return result;
    }

    /**
     *  Load an image
     * @param HttpServletResponse
     * @param String imageId
     * @throws IOException 
     * 
     *  search asset from memory cache and set value into response
     *  returns process result
     *   
     */
    private boolean loadImage(HttpServletResponse response, String imageId ) throws IOException 
    {   
        boolean result = true; 
        MemoryCache aMemoryCache = MemoryCache.getInstance();
        HashMap assetMap = aMemoryCache.getAssetMap();
        if ( assetMap.containsKey( imageId ))
        {
            AssetInfo aAssetInfo = ( AssetInfo )assetMap.get( imageId );
            String MIMEType = aAssetInfo.getMIMEType();
            response.setContentType( MIMEType );
            byte[] data = aAssetInfo.getData();
            int size = data.length;
            response.setContentLength( size );
            ServletOutputStream myOutput = response.getOutputStream();
            myOutput.write( data );
            myOutput.flush();
            myOutput.close();
            /*
            String filePath = System.getProperty(AuditFile.TDC_HOME) + "/webapp";
            File psFile = new File( filePath, imageId + ".swf" );
            FileOutputStream psfile = new FileOutputStream( psFile );
            psfile.write( data );
            psfile.flush();
            psfile.close();
            POC_Image( response, "http://localhost:12345/" + imageId + ".swf" );
            */
        }
        else
            result = false;
           
        return result;
    }
    
    private boolean imageReady( String fileName )
    {
        boolean ready = false;
        int tryCount = 0;
        int max_try = 5;
        try
        {
            while( !ready && tryCount < max_try )
            {
                File aFile = new File( fileName );
                if ( aFile.exists() && aFile.isFile() )
                {
                    if ( aFile.length() > 0 )
                        ready = true;
                }
                tryCount++;
                if ( !ready )
                {
                    //logger.info("file does not exist - " + fileName + "try count=" + tryCount);
                    Thread.sleep( 1000 );
                }
            }
        }
        catch( Exception e )
        {
            logger.error("exception thrown in imageReady()");
        }
        return ready;
    }
    
    public class ImageContentPiece
    {
        int size;
        byte[] buffer;
    }
    
    public ArrayList getFileContent( String fileName, HttpServletResponse response
                                        , String mimeType ) throws IOException
    {
        if (! isFileInTempDir( fileName ))
            return null;
        int EACH_READ_SIZE = 5000;
        ArrayList buffers = new ArrayList();
        File imageFile = new File( fileName );
        int imageSize = ( int )imageFile.length();
        response.setContentType( mimeType );
        response.setContentLength( imageSize );
        ServletOutputStream myOutput = response.getOutputStream();
        FileInputStream inputStream = new FileInputStream( imageFile );
        byte[] buffer = new byte[ EACH_READ_SIZE ];
        int readSize;
        boolean done = false;
        while( !done )
        {
            readSize = inputStream.read( buffer );
            if ( readSize <= 0 )
                done = true;
            else
            {
                ImageContentPiece aImageContentPiece = new ImageContentPiece();
                aImageContentPiece.size = readSize;
                aImageContentPiece.buffer = buffer;
                buffer = new byte[ EACH_READ_SIZE ];
                buffers.add( aImageContentPiece );
                myOutput.write( aImageContentPiece.buffer, 0, aImageContentPiece.size );
            }
        }
        inputStream.close();
        myOutput.flush();
        myOutput.close();
        return buffers;
    }
   
  /*  public ArrayList getFileContent( String fileName ) throws IOException
    {
        if (! isFileInTempDir( fileName ))
            return null;
        
        File imageFile = new File( fileName );
        int imageSize = ( int )imageFile.length();
        ArrayList buffers = new ArrayList();
        byte[] buffer = new byte[ imageSize ];
        BufferedInputStream inputStream = new BufferedInputStream( new FileInputStream( new File( fileName ) ) );
        int readSize;
        int totalRead = 0;
        boolean done = false;
        while( !done )
        {
            readSize = inputStream.read( buffer, totalRead, imageSize - totalRead );
            totalRead += readSize;
            if ( totalRead >= imageSize )
                done = true;
            else
                System.out.println( "Reading " + fileName );
        }
        ImageContentPiece aImageContentPiece = new ImageContentPiece();
        aImageContentPiece.size = imageSize;
        aImageContentPiece.buffer = buffer;
        buffers.add( aImageContentPiece );
        inputStream.close();
        return buffers;
    }
    */
    public boolean isFileInTempDir( String fileName ) throws IOException
    {
        File file1 = new File( fileName );
        String path1 = file1.getCanonicalPath();          
        File file2 = new File( System.getProperty( "java.io.tmpdir" ) );
        String path2 = file2.getCanonicalPath();  
        return (path1.toLowerCase().startsWith( path2.toLowerCase() ));
    }
      
    private void getLocalImage( HttpServletResponse response, String fileName ) throws IOException
    {   
        ImageInfo cachedImage = getCachedImage( fileName );         
        if ( cachedImage != null ) {
            ArrayList buffers = cachedImage.getData();
            String mimeType = cachedImage.getMimeType();
            writeToResponse( response, buffers, mimeType );
        }
        else
        if ( imageReady( fileName ) ) {
            String mimeType = getMimeType( fileName );
            if ( mimeType != null ) {
                ArrayList buffers = getFileContent( fileName, response, mimeType );
                if (buffers != null) {
                    setCachedImage( fileName, buffers, mimeType ); 
                    deleteImageFile( fileName );
                }
                else {
                    response.sendError( HttpServletResponse.SC_NO_CONTENT );                    
                }
            }
            else {
                response.sendError( HttpServletResponse.SC_NO_CONTENT );
            }            
        }
        else {
            response.sendError( HttpServletResponse.SC_NO_CONTENT );
        }
    }
    
    private ImageInfo getCachedImage( String fileName ) 
    {
        ImageInfo cachedImage = null;
        MemoryCache memoryCache = MemoryCache.getInstance();
        HashMap imageMap = memoryCache.getImageMap();
        if ( imageMap.containsKey( fileName ))
        {
            cachedImage = ( ImageInfo )imageMap.get( fileName );
        }
        return cachedImage;    
    }

    private void setCachedImage( String fileName, ArrayList buffers, String mimeType ) 
    {        
        ImageInfo cachedImage = new ImageInfo();
        cachedImage.setData( buffers );
        cachedImage.setMimeType( mimeType );
        MemoryCache memoryCache = MemoryCache.getInstance();
        HashMap imageMap = memoryCache.getImageMap();
        imageMap.put( fileName, cachedImage );
    }
    
    private void writeToResponse( HttpServletResponse response, ArrayList buffers, String mimeType ) throws IOException
    {
        int totalSize = 0;
        for ( int i = 0; i < buffers.size(); i++ )
        {
            ImageContentPiece aImageContentPiece = ( ImageContentPiece )buffers.get( i );
            totalSize += aImageContentPiece.size;
        }
        response.setContentType( mimeType );
        response.setContentLength( totalSize );
        ServletOutputStream myOutput = response.getOutputStream();
        for ( int i = 0; i < buffers.size(); i++ )
        {
            ImageContentPiece aImageContentPiece = ( ImageContentPiece )buffers.get( i );
            myOutput.write( aImageContentPiece.buffer, 0, aImageContentPiece.size );
        }
        myOutput.flush();
        myOutput.close();
    }
    
    private void deleteImageFile( String fileName )
    {
        File file = new File( fileName );
        file.delete();
    }

    private String getMimeType( String fileName )
    {
        String mimeType = null;
        String ext = fileName.substring( fileName.lastIndexOf( "." ) + 1 ).toLowerCase();
        if ( "swf".equals( ext ))
            mimeType = "application/x-shockwave-flash";
        else 
        if ( "jpg".equals( ext ))
            mimeType = "image/jpg";   
        else 
        if ( "gif".equals( ext ))
            mimeType = "image/gif";   
        
        return mimeType;
    }
    
    private void POC_Image( HttpServletResponse response, String imageURL )throws IOException
    {
        String html = "<html><body><table><tr><td><img src=\"" + imageURL 
                    + "\"/></td></tr></table></body></html>";
        response.setContentType( "text/html" );
        PrintWriter myOutput = response.getWriter();
        myOutput.print( html );
        myOutput.flush();
        myOutput.close();
    }
    
    private boolean isMacOS() {
        String os = System.getProperty("os.name");
        if (os == null) 
            os = "";
        return ( os.toLowerCase().indexOf("mac") != -1 );
    }
    
}
