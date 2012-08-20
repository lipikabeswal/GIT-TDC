package com.ctb.tdc.web.servlet.fixed;


import java.io.UnsupportedEncodingException;


public class test{
	
	private static final String xml= "<v>%3Canswers%3E%3Canswer%20id%3D%22widget1001%22%3E%3C%21%5BCDATA%5BSano%C3%BC%5D%5D%3E%3C%2Fanswer%3E%3C%2Fanswers%3E</v>";
	 public static String decode(String s, String enc) 
		throws UnsupportedEncodingException{
		
		boolean needToChange = false;
		int numChars = s.length();
		StringBuffer sb = new StringBuffer(numChars > 500 ? numChars / 2 : numChars);
		int i = 0;

		if (enc.length() == 0) {
		    throw new UnsupportedEncodingException ("URLDecoder: empty string enc parameter");
		}

		char c;
		byte[] bytes = null;
		while (i < numChars) {
	            c = s.charAt(i);
	            System.out.println("Characrter C : " + c);
	            switch (c) {
		    case '+':
			sb.append(' ');
			i++;
			needToChange = true;
			break;
		    case '%':
			try {

			    if (bytes == null)
				bytes = new byte[(numChars-i)/3];
			    int pos = 0;
			    
			    while ( ((i+2) < numChars) && 
				    (c=='%')) {
				bytes[pos++] = 
				    (byte)Integer.parseInt(s.substring(i+1,i+3),16);
				i+= 3;
				if (i < numChars)
				    c = s.charAt(i);
			    }
			    if ((i < numChars) && (c=='%'))
				throw new IllegalArgumentException(
			         "URLDecoder: Incomplete trailing escape (%) pattern");
			    
			    sb.append(new String(bytes, 0, pos, enc));
			} catch (NumberFormatException e) {
			    throw new IllegalArgumentException(
	                    "URLDecoder: Illegal hex characters in escape (%) pattern - " 
			    + e.getMessage());
			}
			needToChange = true;
			break;
		    default: 
			sb.append(c); 
			i++;
			break; 
	            }
	        }

	        return (needToChange? sb.toString() : s);
	    }
	 
	 public static void main(String[] args) {
		 String decodedXml = null;
		try{
		  decodedXml = decode(xml,"UTF-8");
		  
		  System.out.println("radix test" + Integer.parseInt("11",16));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		 System.out.println("Decoded Xml: " + decodedXml);
		 
	}
}
 