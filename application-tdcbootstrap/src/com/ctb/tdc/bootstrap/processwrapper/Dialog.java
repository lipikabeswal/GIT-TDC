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
	String[] forms = {"Form A / Form B / Español A","Form C / Form D / Español B"}; 
	String[] formsMac = {"Form C / Form D / Español B","Form A / Form B / Español A"}; 
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
	String[] forms = {"TABE Online","TABE Testlets"}; 
	String[] formsMac = {"TABE Testlets","TABE Online"}; 
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
	
