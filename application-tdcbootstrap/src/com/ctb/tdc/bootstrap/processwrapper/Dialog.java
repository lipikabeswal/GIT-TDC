package com.ctb.tdc.bootstrap.processwrapper;


import javax.swing.JOptionPane;
public class Dialog extends JOptionPane {
	
private boolean isMac() {
	        String os = System.getProperty("os.name");
	        if (os == null) 
	            os = "";
	        return ( os.toLowerCase().indexOf("mac") != -1 );
	    }


public String showOptionDialogLASLINKS(){
	int rc;
	String[] forms = {"Form A/Form B/Espanol","Form C/Form D/Espanol2"}; 
	String[] formsMac = {"Form C/Form D/Espanol2","Form A/Form B/Espanol"}; 
	if(isMac()){
		rc = showOptionDialog(null, "Please Select Form :","Form Selection", 0, JOptionPane.PLAIN_MESSAGE, null, formsMac, formsMac[1]);
	}else{
		rc = showOptionDialog(null, "Please Select Form :","Form Selection", 0, JOptionPane.PLAIN_MESSAGE, null, forms, forms[0]);
	}
	
	if (rc==-1) { 
		return null;
	    
	   } 
	else {  
		 if(isMac()){
			 return formsMac[rc];
		 }else{
			 return forms[rc];
		 }
		
	  
	   }
}
public String showOptionDialogTABE(){
	int rc;
	String[] forms = {"TABE 9/10","TABE Adaptive"}; 
	String[] formsMac = {"TABE Adaptive","TABE 9/10"}; 
	if(isMac()){
		rc = showOptionDialog(null, "Please Select :","Test Selection", 0, JOptionPane.PLAIN_MESSAGE, null, formsMac, formsMac[1]);
	}else{
		rc = showOptionDialog(null, "Please Select :","Test Selection", 0, JOptionPane.PLAIN_MESSAGE, null, forms, forms[0]);
	}
	
	if (rc==-1) { 
		System.out.println("No Form was selected");
		return null;
	    
	   } 
	else {  
		 if(isMac()){
			 return formsMac[rc];
		 }else{
			 return forms[rc];
		 }
		
	  
	   }
}

}	
	
