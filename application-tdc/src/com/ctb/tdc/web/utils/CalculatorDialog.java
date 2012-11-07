package com.ctb.tdc.web.utils;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JMenu;


public class CalculatorDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private boolean calculatorRunning;
	
	static {
		  CalculatorDialog.setDefaultLookAndFeelDecorated(true);
	}
	
	public CalculatorDialog(Frame frame, String title){
		super(frame);
        this.setTitle(title);
        //in case the user closes the window
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
         //enables Window Events on this Component
        //this.addWindowListener(new CalculatorWindowLintener());
        this.addComponentListener(new ComponentAdapter() {
        	@Override
        	public void componentMoved(ComponentEvent e) {
        		CalculatorDialog dialog = (CalculatorDialog) e.getSource();
        		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        		int maximumWidth = Double.valueOf(screenSize.getWidth()).intValue() - 300;
        		int maximumHeight = Double.valueOf(screenSize.getHeight()).intValue() - 600;
        		if (dialog.getLocation().x < 0) {
                	dialog.setLocation(new Point(0, dialog.getLocation().y));
                } else if (dialog.getLocation().y < 0) {
                	dialog.setLocation(new Point(dialog.getLocation().x, 0));
                } else if (dialog.getLocation().x > maximumWidth) {
                	dialog.setLocation(new Point(maximumWidth, dialog.getLocation().y));
                } else if (dialog.getLocation().y > maximumHeight) {
                	dialog.setLocation(new Point(dialog.getLocation().x, maximumHeight));
                }
            }
        });
        this.calculatorRunning = true;
        this.setFocusable(false);
        this.setFocusableWindowState(false);
        removeCloseButton(this);
    }
	
	@Override
	public void dispose() {
		this.setCalculatorRunning(false);
		super.dispose();
	}
	
	public boolean isCalculatorRunning() {
		return calculatorRunning;
	}

	public void setCalculatorRunning(boolean calculatorRunning) {
		this.calculatorRunning = calculatorRunning;
	}
	
	public static void removeCloseButton(Component comp) {
        if (comp instanceof JMenu) {
          Component[] children = ((JMenu) comp).getMenuComponents();
          for (int i = 0; i < children.length; ++i)
            removeCloseButton(children[i]);
        }
        else if (comp instanceof AbstractButton) {
          Action action = ((AbstractButton) comp).getAction();
          String cmd = (action == null) ? "" : action.toString();
          if (cmd.contains("CloseAction")) {
            comp.getParent().remove(comp);
          }
        }
        else if (comp instanceof Container) {
          Component[] children = ((Container) comp).getComponents();
          for (int i = 0; i < children.length; ++i)
            removeCloseButton(children[i]);
        }
     }
}
