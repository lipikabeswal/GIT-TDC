package com.ctb.tdc.bootstrap.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.border.EtchedBorder;

import com.ctb.tdc.bootstrap.util.ResourceBundleUtils;

/**
 * 
 * Typical splash window to provide the user some sense of status
 * of the bootstrap process.  Most methods were autogenerated by the
 * IDE with {@link #setStatus} as the exception.
 * 
 * @author Giuseppe_Gennaro
 *
 */
public class SplashWindow extends JWindow {

	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 300;
	private static final int HEIGHT = 200;
	
	private JPanel jContentPane = null;
	private JLabel jLabel = null;
	private JPanel jStatusPanel = null;
	private JLabel jStatusLabel = null;
	private JProgressBar jProgressBar = null;

	public SplashWindow() {
		super();
		// TODO Auto-generated constructor stub
		initialize();
	}

	public SplashWindow(Frame arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
		initialize();
	}

	public SplashWindow(GraphicsConfiguration arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
		initialize();
	}

	public SplashWindow(Window arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
		initialize();
	}

	public SplashWindow(Window arg0, GraphicsConfiguration arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		this.setSize(SplashWindow.WIDTH, SplashWindow.HEIGHT);
		this.setLocation( (int)(screenSize.getWidth()/2 - this.getWidth()/2) ,
						  (int)(screenSize.getHeight()/2 - this.getHeight()/2) );
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabel = new JLabel(new ImageIcon( SplashWindow.class.getResource("splash.gif") ));
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.setBorder(javax.swing.BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			jContentPane.setBackground(java.awt.Color.white);
			jContentPane.add(jLabel, java.awt.BorderLayout.CENTER);
			jContentPane.add(getJStatusPanel(), java.awt.BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jStatusPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJStatusPanel() {
		if (jStatusPanel == null) {

			jStatusLabel = new JLabel();
			jStatusLabel.setText( ResourceBundleUtils.getString("bootstrap.main.splashWindow.status.default") );
			jStatusLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
			jStatusLabel.setAlignmentX(0);

			jStatusPanel = new JPanel();
			jStatusPanel.setLayout(new BoxLayout(getJStatusPanel(), BoxLayout.Y_AXIS));
			jStatusPanel.setBackground(java.awt.Color.white);
			jStatusPanel.add(jStatusLabel, null);
			jStatusPanel.add(getJProgressBar(), null);
		}
		return jStatusPanel;
	}

	/**
	 * This method initializes jProgressBar	
	 * 	
	 * @return javax.swing.JProgressBar	
	 */
	private JProgressBar getJProgressBar() {
		if (jProgressBar == null) {
			jProgressBar = new JProgressBar();
			jProgressBar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
			jProgressBar.setBackground(java.awt.Color.white);
			jProgressBar.setForeground(new java.awt.Color(102,145,180));
			jProgressBar.setIndeterminate(true);
			jProgressBar.setAlignmentX(0);
		}
		return jProgressBar;
	}

	/**
	 * Sets the status of the splash window based on the given arguments.
	 * @param string  The message to be displayed above the progress bar.
	 * @param i  The percent completion to be displayed within the progress bar.  If i > maximum or i < minimum, then the indeterminate state will be displayed (cylon status).
	 */
	public void setStatus(String string, int i) {
		this.jStatusLabel.setText(string);
		if( i < this.jProgressBar.getMinimum() || i > this.jProgressBar.getMaximum() ) {
			this.jProgressBar.setIndeterminate(true);
		} else {
			this.jProgressBar.setIndeterminate(false);
			this.jProgressBar.setValue(i);
		}
			
	}

}
