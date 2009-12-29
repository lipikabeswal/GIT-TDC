package com.ctb.tdc.tsutility.ui;

import java.io.File;
import java.io.FilenameFilter;

public class TextFilenameFilter implements FilenameFilter {

	public boolean accept(File dir, String name) {
		
		String lowercaseName = name.toLowerCase();
		if( lowercaseName.endsWith(".txt") ) 
			return true;
		
		return false;
	}

}
