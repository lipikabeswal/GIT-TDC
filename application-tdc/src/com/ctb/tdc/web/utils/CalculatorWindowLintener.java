package com.ctb.tdc.web.utils;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CalculatorWindowLintener extends WindowAdapter {
	
	public void windowClosing(WindowEvent e){
		CalculatorJFrame frame = (CalculatorJFrame) e.getSource();
		frame.dispose();
	}	
}