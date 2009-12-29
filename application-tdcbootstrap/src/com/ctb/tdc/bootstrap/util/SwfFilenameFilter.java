package com.ctb.tdc.bootstrap.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SwfFilenameFilter implements FilenameFilter {

	private Pattern p = Pattern.compile("LoadContentServlet.*\\.swf");

	public boolean accept(File dir, String name) {

		Matcher m = p.matcher(name);
		if( m.matches() ) {
			return true;
		}
		
		return false;
	}

}
