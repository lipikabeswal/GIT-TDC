package com.ctb.tdc.web.servlet;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Element;
import org.xml.sax.InputSource;
import org.jdom.output.XMLOutputter;

import com.ctb.tdc.web.utils.AssetInfo;
import com.ctb.tdc.web.utils.Base64;
import com.ctb.tdc.web.utils.FileUtils;
import com.ctb.tdc.web.utils.MemoryCache;
import com.ctb.tdc.web.utils.ServletUtils;
import com.stgglobal.util.CryptoLE.Crypto;

/**
 * @author Tai_Truong
 */
public class LoadContentServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    public String globalItemKey = "1u1piyyriN74U55CGnc4k1";
	/**
	 * Constructor of the object.
	 */
	public LoadContentServlet() {
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
        String itemSetId = ServletUtils.getItemSetId(request);
        String itemId = ServletUtils.getItemId(request);
        String imageId = ServletUtils.getImageId(request);
        String encryptionKey = ServletUtils.getEncryptionKey(request);
        
        if (method.equals(ServletUtils.LOAD_SUBTEST_METHOD))
            loadSubtest(response, "2203", "1757", "F16FF0BA9F3D0051F8D3630744BA0FCC", "./data/xmls");
        if (method.equals(ServletUtils.LOAD_ITEM_METHOD))
            loadItem(response, itemId, encryptionKey);        
        if (method.equals(ServletUtils.LOAD_IMAGE_METHOD))
            loadImage(response, imageId, encryptionKey);                
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
		// Put your code here
	}
	
	public byte[] readFromFile( File filePath ) throws Exception
    {
        FileInputStream aFileInputStream = new FileInputStream( filePath );
        int size = aFileInputStream.available();
        byte[] buffer = new byte[ size ];
        aFileInputStream.read( buffer );
        aFileInputStream.close();
        return buffer;
    }
	
	public static String replaceAll( String src, String toBeReplace, String replaceWith )
    {
    	String result = src;
    	int index = 0;
    	int difference = replaceWith.length();
    	while ( ( index = result.indexOf( toBeReplace, index )) >= 0 )
    	{
    		result = result.substring( 0, index ) + replaceWith + result.substring( index + toBeReplace.length() );
    		index += difference;
    	}
    	return result;
    }
	
	public static String JavasriptConvert( String input )
    {
        StringBuffer outputStr = new StringBuffer();
     	for ( int i = 0; i < input.length(); i++)
    	{
    		if (( input.charAt( i ) == 13) && ( input.charAt( i + 1 ) == 10))
    			i++;
    		else
    		    outputStr.append( input.charAt( i ) );	
    	}
    	String output = outputStr.toString();
 //   	output = replaceAll( output, "\t", " " );
 //   	output = replaceAll( output, "  ", " " );
    	output = replaceAll( output, "&#+;", "&#x002B;" );
    	output = replaceAll( output, "+", "&#x002B;" );
        return output;
    }

    public static String convertUTF8CharsToNumericEntityReferences( String input )
    {
        StringBuffer retVal = new StringBuffer( input.length() * 2 );
        boolean isMS = false;
        String s;
        for(int i = 0; i < input.length(); i++)
        {
            char c = input.charAt( i );
            int intc = c;
            if( intc != 9 && intc != 10 && intc != 13 )
            {
                if( intc <= 127 && intc != 43 )
                {
                    if( intc == 32 )
                    {
                        if( !isMS )
                        {
                            retVal.append( c );
                            isMS = true;
                        }
                    } 
                    else
                    {
                        isMS = false;
                        retVal.append( c );
                    }
                } 
                else
                {
                    isMS = false;
                    retVal.append( "&#" ).append( intc ).append( ';' );
                }
            }
        }
        s = retVal.toString();
        return s; 
    }
    
    public static List extractAllElement(String pattern, Element element ) throws Exception
    {
        // TO-DO: this will only work with simple './/name' queries as is . . .
        ArrayList results = new ArrayList();
        pattern = pattern.substring(pattern.indexOf(".//") + 3);
        List children = element.getChildren();
        Iterator iterator = children.iterator();
        while(iterator.hasNext()) {
            Element elem = (Element) iterator.next();
            if(pattern.equals(elem.getName())) {
                results.add(elem);
            }
            results.addAll(extractAllElement(".//" + pattern, elem));
        }
        return results;
        
        // This doesn't work with current JDOM
        /** XPath assetXPath = XPath.newInstance( pattern );
        List elementList = assetXPath.selectNodes( element );
        return elementList;*/
    }
	
	public String updateItem( String itemxml, HashMap AssetTable ) throws Exception
    {
        itemxml = itemxml.substring(0, itemxml.lastIndexOf("<assets>")) + itemxml.substring(itemxml.lastIndexOf("</assets>") + 9);
        MemoryCache aMemoryCache = MemoryCache.getInstance();
        org.jdom.Document itemDoc = aMemoryCache.saxBuilder.build( new InputSource( new StringReader(itemxml)) );
        org.jdom.Element rootElement = (org.jdom.Element) itemDoc.getRootElement();
        List items = extractAllElement( ".//image_widget", rootElement);
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
	
	public String getKeyByKeyId( String ItemKeyId )
    {
        return globalItemKey;
    }
	
	public void handleItem( String bankDir, String itemID, 
	        				String ItemHashKey, String ItemKeyId ) throws Exception
    {
        String filePath = itemID + ".ecp";
        byte[] buffer = readFromFile( new File( bankDir, filePath ) );
        MemoryCache aMemoryCache = MemoryCache.getInstance();
        HashMap itemMap = aMemoryCache.getItemMap();
        HashMap assetMap = aMemoryCache.getAssetMap();
        if ( Crypto.checkHash( ItemHashKey, buffer ))
        {
            byte[] result = aMemoryCache.aCrypto.checkHashAndDecrypt( getKeyByKeyId( ItemKeyId ), ItemHashKey, buffer, true, false );
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
	                    byte imageData[] = Base64.decodeToByteArray( b64data );
	                    AssetInfo aAssetInfo = new AssetInfo();
	                    aAssetInfo.setData( imageData );
	                    aAssetInfo.setExt( ext );
	                    assetMap.put( imageId, aAssetInfo );
	                }
	            }
	            String itemxml = new String( result );
	            itemxml = updateItem( itemxml, assetMap );
	            itemxml = convertUTF8CharsToNumericEntityReferences( itemxml );
	            itemxml = JavasriptConvert( itemxml );
	            itemMap.put( itemID, itemxml.getBytes() );
            }
        }
    }

    /**
     *  Load a subtest
     * @param String obAssessmentId
     * @param String encryptionKey
     * @param String hashValue
     * @param String bankDir
     * @throws IOException 
     * 
     *  retrieves encrypted subtest xml for this item set id from local directory
     *  decrypts subtest xml
     *  returns decrypted subtest xml
     *   
     */
    private boolean loadSubtest( HttpServletResponse response, String obAssessmentId
            					, String encryptionKey, String hashValue
            					, String bankDir ) throws IOException 
    {
        boolean result = true; 
        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        
        MemoryCache aMemoryCache = MemoryCache.getInstance();
        aMemoryCache.clearContent();
        try
        {
            String fileName = obAssessmentId + ".eam";
            byte[] buffer = readFromFile( new File( bankDir, fileName ) );
            if ( Crypto.checkHash( hashValue, buffer ))
            {
                byte[] dataValue = aMemoryCache.aCrypto.checkHashAndDecrypt( encryptionKey, hashValue, buffer, true, false );
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
                    handleItem( bankDir, obItemId, ItemHash, ItemKeyId );
                }
                response.setContentType( "text/xml" );
                ServletOutputStream myOutput = response.getOutputStream();
                myOutput.write( dataValue ); 
                myOutput.flush();
                myOutput.close();
            }
            else
                result = false;
        }
        catch( Exception e )
        {
            result = false;
        }     
        return result;
    }

    /**
     *  Load an item
     * @param String itemSetId
     * @param String encryptionKey
     * @throws IOException 
     * 
     *  retrieves encrypted item xml for this item id from local directory
     *  decrypts item xml
     *  saves assets to local directory
     *  returns decrypted item xml
     *   
     */
    private boolean loadItem(HttpServletResponse response, String itemId, String encryptionKey) throws IOException {
        boolean result = true; 
        MemoryCache aMemoryCache = MemoryCache.getInstance();
        HashMap itemMap = aMemoryCache.getItemMap();
        if ( itemMap.containsKey( itemId ))
        {
            byte[] dataValue = ( byte[] )itemMap.get( itemId );
            response.setContentType( "text/xml" );
            ServletOutputStream myOutput = response.getOutputStream();
            myOutput.write( dataValue ); 
            myOutput.flush();
            myOutput.close();
        }
        else
            result = false;
        return result;
    }

    /**
     *  Load an image
     * @param String imageId
     * @param String encryptionKey
     * @throws IOException 
     * 
     *  retrieves image from local directory
     *  decrypts image xml
     *  returns decrypted image xml
     *   
     */
    private boolean loadImage(HttpServletResponse response, String imageId, String encryptionKey) throws IOException 
    {
        boolean result = true; 
        MemoryCache aMemoryCache = MemoryCache.getInstance();
        HashMap assetMap = aMemoryCache.getAssetMap();
        if ( assetMap.containsKey( imageId ))
        {
            AssetInfo aAssetInfo = ( AssetInfo )assetMap.get( imageId );
            response.setContentType( aAssetInfo.getMIMEType() );
            response.setContentLength( aAssetInfo.getData().length );
            ServletOutputStream myOutput = response.getOutputStream();
            myOutput.write( aAssetInfo.getData() );
            myOutput.flush();
            myOutput.close();
        }
        else
            result = false;
           
        return result;
    }
    

    
}
