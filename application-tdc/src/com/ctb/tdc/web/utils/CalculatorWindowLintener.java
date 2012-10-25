package com.ctb.tdc.web.utils;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CalculatorWindowLintener extends WindowAdapter {
	
	@Override
	public void windowClosing(WindowEvent e){
		CalculatorDialog dialog = (CalculatorDialog) e.getSource();
		dialog.dispose();
	}
}