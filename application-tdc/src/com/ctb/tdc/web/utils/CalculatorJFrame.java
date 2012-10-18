package com.ctb.tdc.web.utils;

import javax.swing.JFrame;

public class CalculatorJFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private boolean calculatorRunning;
	
	public CalculatorJFrame(){
        super("Graphing Calculator");
        //in case the user closes the window
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         //enables Window Events on this Component
        this.addWindowListener(new CalculatorWindowLintener());
        this.setCalculatorRunning(true);
    }
	
	@Override
	public void dispose() {
		super.dispose();
		this.setCalculatorRunning(false);
	}
	
	public boolean isCalculatorRunning() {
		return calculatorRunning;
	}

	public void setCalculatorRunning(boolean calculatorRunning) {
		this.calculatorRunning = calculatorRunning;
	}
}
