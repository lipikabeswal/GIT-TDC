package com.ctb.tdc.web.utils;

import java.awt.Frame;

import javax.swing.JDialog;

public class CalculatorDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private boolean calculatorRunning;
	private boolean calculatorHidden;
	
	public CalculatorDialog(Frame frame, String title){
		super(frame);
        this.setTitle(title);
        //in case the user closes the window
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
         //enables Window Events on this Component
        this.addWindowListener(new CalculatorWindowLintener());
        this.calculatorRunning = true;
        this.calculatorHidden = false;
    }
	
	@Override
	public void dispose() {
		this.setCalculatorHidden(true);
		this.setCalculatorRunning(false);
		super.dispose();
	}
	
	public boolean isCalculatorRunning() {
		return calculatorRunning;
	}

	public void setCalculatorRunning(boolean calculatorRunning) {
		this.calculatorRunning = calculatorRunning;
	}
	
	public boolean isCalculatorHidden() {
		return calculatorHidden;
	}
	
	public void setCalculatorHidden(boolean calculatorHidden) {
		if(calculatorHidden) {
			this.setVisible(false);
		} else {
			this.setVisible(true);
		}
		this.calculatorHidden = calculatorHidden;
	}
}
