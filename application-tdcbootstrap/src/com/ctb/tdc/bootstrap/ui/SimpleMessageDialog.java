package com.ctb.tdc.bootstrap.ui;

import javax.swing.JOptionPane;

import com.ctb.tdc.bootstrap.util.ResourceBundleUtils;

/**
 * Simple class to provide simplier access to javax.swing.JOptionPane and
 * its ability to give simple dialogs.  See, simple.  
 * 
 * @author Giuseppe_Gennaro
 *
 */
public class SimpleMessageDialog {

	public static final String DEFAULT_TITLE = ResourceBundleUtils.getString("bootstrap.main.simpleMessageDialog.title");
	
	/**
	 * @param message
	 */
	public static void showErrorDialog(String message) {
		showDialog(message, DEFAULT_TITLE, JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * @param message
	 */
	public static void showInfoDialog(String message) {
		showDialog(message, DEFAULT_TITLE, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * @param message
	 */
	public static void showWarningDialog(String message) {
		showDialog(message, DEFAULT_TITLE, JOptionPane.WARNING_MESSAGE);
	}

	
	/**
	 * 
	 */
	private static void showDialog(String message, String title, int type) {
		JOptionPane.showMessageDialog(null, message, title, type);
	}
}
