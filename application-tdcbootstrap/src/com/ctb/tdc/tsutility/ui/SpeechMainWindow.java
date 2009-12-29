package com.ctb.tdc.tsutility.ui;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import com.ctb.tdc.tsutility.AppResourceBundleUtil;
import com.ctb.tdc.tsutility.analyzer.TextToSpeechLaunch;


/**
 * Simulate text to speech.
 */
public class SpeechMainWindow extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private static final Dimension WINDOW_DIMENSIONS = new Dimension(650,550);
	private static final Color WINDOW_BACKGROUND_COLOR = SystemColor.control;
	private static final Font WINDOW_FONT_PLAIN = new Font("Arial", Font.PLAIN, 12);  
	private static final Font WINDOW_FONT_BOLD = new Font("Arial", Font.BOLD, 12);  
	
	private static final String WINDOW_TITLE = AppResourceBundleUtil.getString("tsutility.mainWindow.title");
	private static final String INTRODUCTION = AppResourceBundleUtil.getString("tsutility.mainWindow.introductionTTS");
	private static final String BUTTON_EXIT  = AppResourceBundleUtil.getString("tsutility.mainWindow.buttons.exit");
	private static final String TASKS_GROUPING = AppResourceBundleUtil.getString("tsutility.mainWindow.question.groupingTTS");  
	private static final String DETAILS_GROUPING = AppResourceBundleUtil.getString("tsutility.mainWindow.details.grouping");  
	
	private static final String MODULE_NETWORK_TAB        = AppResourceBundleUtil.getString("tsutility.mainWindow.module.networkTab");
	private static final String MODULE_BANDWIDTH_TAB        = AppResourceBundleUtil.getString("tsutility.mainWindow.module.bandwidthTab");
	private static final String MODULE_SPEECH_TAB        = AppResourceBundleUtil.getString("tsutility.mainWindow.module.SpeechTab");
	
	private static final String ACTION_EXIT = "exit";
	
	private static final String ACTION_SWITCH_TO_NETWORK = "switchToNetwork";
	private static final String ACTION_SWITCH_TO_BANDWIDTH = "switchToBandwidth";
	private static final String ACTION_SWITCH_TO_SPEECH = "switchToSpeech";
	
	private JPanel scaffoldPanel = null;
	private JPanel headerPanel = null;
	private JPanel contentPanel = null;
	private JPanel introductionPanel = null;
	private JLabel introductionLabel = null;
	private JPanel taskListPanel = null;
	private JPanel taskDetailPanel = null;
	private JTextArea taskDetailTextArea = null;
	private JPanel buttonsPanel = null;
	private JButton speakButton = null;
	
	private JButton exitButton = null;
	private JButton switchNetworkButton = null;
	private JButton switchBandwidthButton = null;
	private JButton switchSpeechButton = null;

	private JTextField textField = null;
	private JLabel labelSpeak = null;
	
	private JFrame networkWindow = null;
	private JFrame bandwidthWindow = null;
	private JFrame speechWindow = null;
	
	//--------------------------------------------------------------------------

	
	  class FixedSizePlainDocument extends PlainDocument {
		private static final long serialVersionUID = 1L;
			int maxSize;
	    
	        public FixedSizePlainDocument(int limit) {
	            maxSize = limit;
	        }
	    
	        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
	            if ((getLength() + str.length()) <= maxSize) {
	                super.insertString(offs, str, a);
	            } else {
	                throw new BadLocationException("Insertion exceeds max size of document", offs);
	            }
	        }
	    }
	
	
	public SpeechMainWindow() throws HeadlessException {
		super();
		initialize();
	}

	public SpeechMainWindow(GraphicsConfiguration gc) {
		super(gc);
		initialize();
	}

	public SpeechMainWindow(String title) throws HeadlessException {
		super(title);
		initialize();
	}

	public SpeechMainWindow(String title, GraphicsConfiguration gc) {
		super(title, gc);
		initialize();
	}


	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		
		
		ImageIcon iconImage = new ImageIcon( MainWindow.class.getResource("images/icon.gif") );
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		
		this.setContentPane(getScaffoldPanel());
		this.setIconImage(iconImage.getImage());
		this.setResizable(false);
		this.setTitle( WINDOW_TITLE );
		this.setSize( WINDOW_DIMENSIONS );
		this.setLocation( (int)(screenSize.getWidth()/2 - this.getWidth()/2) ,
				  		  (int)(screenSize.getHeight()/2 - this.getHeight()/2) );
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

	public void setFrames(JFrame networkWindow, JFrame bandwidthWindow, JFrame speechWindow) {
		this.networkWindow = networkWindow;
		this.bandwidthWindow = bandwidthWindow;
		this.speechWindow = speechWindow;
	}
	

	private void switchFrame(JFrame activeWindow) {
		
		Point point = this.getLocation();
		
		this.networkWindow.setVisible(false);
		this.bandwidthWindow.setVisible(false);
		this.speechWindow.setVisible(false);

		if (activeWindow == this.networkWindow) {
			((MainWindow)this.networkWindow).activateWindow(point);			
		}
		
		if (activeWindow == this.bandwidthWindow) {
			((BandwidthMainWindow)this.bandwidthWindow).activateWindow(point);			
		}
		
		if (activeWindow == this.speechWindow) {		
			((SpeechMainWindow)this.speechWindow).activateWindow(point);			
		}
		
	}
	
	public void activateWindow(Point point) {
		
		this.setLocation(point);
		this.setVisible(true);
		getSwitchSpeechButton().setEnabled(false);
		getSwitchSpeechButton().setFont( WINDOW_FONT_BOLD );	
		//getSwitchSpeechButton().setBackground(new Color(255, 255, 153));	// RGB = FFFF99		
		
		getSwitchNetworkButton().setEnabled(true);
		getSwitchBandwidthButton().setEnabled(true);
	}
	
	/**
	 * This method initializes scaffoldPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getScaffoldPanel() {
		if (scaffoldPanel == null) {
			
			scaffoldPanel = new JPanel();
			scaffoldPanel.setLayout(new BorderLayout());
			scaffoldPanel.add(getHeaderPanel(), BorderLayout.NORTH);
			scaffoldPanel.add(getContentPanel(), BorderLayout.CENTER);
			scaffoldPanel.add(getButtonsPanel(), BorderLayout.SOUTH);
		}
		return scaffoldPanel;
	}

	/**
	 * This method initializes headerPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getHeaderPanel() {
		if (headerPanel == null) {
			ImageIcon headerIcon = new ImageIcon( MainWindow.class.getResource("images/header.gif") );
			
			JLabel headerImageLabel = new JLabel();
			headerImageLabel.setIcon(headerIcon);
			
			headerPanel = new JPanel();
			headerPanel.setLayout(new BorderLayout());
			headerPanel.setBackground(Color.white);
			headerPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			
			headerPanel.add(headerImageLabel, BorderLayout.NORTH);		
			
			headerPanel.add(getSwitchNetworkButton(), BorderLayout.WEST);		
			headerPanel.add(getSwitchBandwidthButton(), BorderLayout.CENTER);			
			headerPanel.add(getSwitchSpeechButton(), BorderLayout.EAST);			
			
		}
		return headerPanel;
	}

	/**
	 * This method initializes contentPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getContentPanel() {
		if (contentPanel == null) {
			
			BorderLayout borderLayout = new BorderLayout();
			borderLayout.setVgap(20);
			contentPanel = new JPanel();
			contentPanel.setLayout(borderLayout);
			contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			contentPanel.setBackground(WINDOW_BACKGROUND_COLOR);

			contentPanel.add(getIntroductionPanel(), BorderLayout.NORTH);
			contentPanel.add(getTaskListPanel(), BorderLayout.CENTER);
			contentPanel.add(getTaskDetailPanel(), BorderLayout.SOUTH);
			
		}
		return contentPanel;
	}

	/**
	 * This method initializes introductionPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getIntroductionPanel() {
		if (introductionPanel == null) {
			BorderLayout borderLayout2 = new BorderLayout();
			borderLayout2.setHgap(0);
			borderLayout2.setVgap(0);
			introductionLabel = new JLabel();
			introductionLabel.setVerticalAlignment(SwingConstants.TOP);
			introductionLabel.setFont( WINDOW_FONT_PLAIN );
			introductionLabel.setBackground( WINDOW_BACKGROUND_COLOR);
			introductionLabel.setText( INTRODUCTION );
			
			introductionPanel = new JPanel();
			introductionPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,10,0,10));
			introductionPanel.setLayout(borderLayout2);
			introductionPanel.setBackground( WINDOW_BACKGROUND_COLOR );
			introductionPanel.add(introductionLabel, BorderLayout.CENTER);
		}
		return introductionPanel;
	}	

	/**
	 * This method initializes taskDetailPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getTaskDetailPanel() {
		if (taskDetailPanel == null) {
			BorderLayout borderLayout1 = new BorderLayout();
			borderLayout1.setHgap(5);
			borderLayout1.setVgap(5);
			taskDetailPanel = new JPanel();
			taskDetailPanel.setLayout(borderLayout1);
			taskDetailPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(SystemColor.controlDkShadow, 1), DETAILS_GROUPING, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), SystemColor.controlDkShadow), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
			taskDetailPanel.setBackground( WINDOW_BACKGROUND_COLOR);

			taskDetailPanel.add(getTaskDetailTextArea(), BorderLayout.NORTH);
			

		}
		return taskDetailPanel;
	}
	
	/**
	 * This method initializes taskDetailTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getTaskDetailTextArea() {
		if (taskDetailTextArea == null) {
			taskDetailTextArea = new JTextArea();
			taskDetailTextArea.setRows(11);
			taskDetailTextArea.setLineWrap(true);
			taskDetailTextArea.setWrapStyleWord(true);
			taskDetailTextArea.setFont( WINDOW_FONT_PLAIN);
			taskDetailTextArea.setBackground( WINDOW_BACKGROUND_COLOR);
			taskDetailTextArea.setEditable(false);			
		}
		return taskDetailTextArea;
	}
	
	
	/**
	 * This method initializes buttonsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getButtonsPanel() {
		if (buttonsPanel == null) {
			buttonsPanel = new JPanel();
			buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			buttonsPanel.setBackground( WINDOW_BACKGROUND_COLOR );
			
			buttonsPanel.add(getSpeakButton(), null);
			buttonsPanel.add(getExitButton(), null);
		}
		return buttonsPanel;
	}

	/**
	 * This method initializes runButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSpeakButton() {
		if (speakButton == null) {
			speakButton = new JButton( "Speak" );
			speakButton.setFont( WINDOW_FONT_PLAIN );
			speakButton.setActionCommand( "speak" );
			speakButton.addActionListener(this);
		}
		return speakButton;
	}
	/**
	 * This method initializes exitButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getExitButton() {
		if (exitButton == null) {
			exitButton = new JButton( BUTTON_EXIT );
			exitButton.setFont(WINDOW_FONT_PLAIN);
			exitButton.setActionCommand( ACTION_EXIT );
			exitButton.addActionListener(this);
		}
		return exitButton;
	}
	
	private JButton getSwitchNetworkButton() {
		if (switchNetworkButton == null) {
			switchNetworkButton = new JButton( "     " + MODULE_NETWORK_TAB + "     " );
			switchNetworkButton.setFont( WINDOW_FONT_PLAIN );
			switchNetworkButton.setActionCommand( ACTION_SWITCH_TO_NETWORK );
			switchNetworkButton.addActionListener(this);
		}
		return switchNetworkButton;
	}

	private JButton getSwitchBandwidthButton() {
		if (switchBandwidthButton == null) {
			switchBandwidthButton = new JButton( MODULE_BANDWIDTH_TAB );
			switchBandwidthButton.setFont( WINDOW_FONT_PLAIN );
			switchBandwidthButton.setActionCommand( ACTION_SWITCH_TO_BANDWIDTH );
			switchBandwidthButton.addActionListener(this);
		}
		return switchBandwidthButton;
	}

	private JButton getSwitchSpeechButton() {
		if (switchSpeechButton == null) {
			switchSpeechButton = new JButton( "       " + MODULE_SPEECH_TAB + "       ");
			switchSpeechButton.setFont( WINDOW_FONT_PLAIN );
			switchSpeechButton.setActionCommand( ACTION_SWITCH_TO_SPEECH );
			switchSpeechButton.addActionListener(this);
		}
		return switchSpeechButton;
	}
	
	//--------------------------------------------------------------------------


	

	
	/**
	 * This method initializes taskListPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getTaskListPanel() {
		if (taskListPanel == null) {
			
			GridBagConstraints gbcInputtext = new GridBagConstraints();

			taskListPanel = new JPanel();
			taskListPanel.setLayout(new GridBagLayout());
			taskListPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(SystemColor.controlDkShadow, 1), TASKS_GROUPING, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), SystemColor.controlDkShadow));
			taskListPanel.setBackground(SystemColor.control);
			
			taskListPanel.add(getLabelSpeak(), gbcInputtext);
			
			taskListPanel.add(getTextField(), gbcInputtext);

		}
		return taskListPanel;
	}

	
	private JTextField getTextField() {
		if (textField == null) {
			textField = new JTextField(20);	
			
			FixedSizePlainDocument fspd = new FixedSizePlainDocument(30);
			try {
				fspd.insertString(0, "Welcome To OAS", null);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			textField.setDocument(fspd);
			
			
		}
		return textField; 
	}
	

	private JLabel getLabelSpeak() {
		if (labelSpeak == null) {
			labelSpeak = new JLabel();
			labelSpeak.setText( "Enter text to speak (max. 30 characters): " );
			labelSpeak.setFont( WINDOW_FONT_PLAIN );
		}
		return labelSpeak; 
	}

	
	
	
	//--------------------------------------------------------------------------
	//--------------------------------------------------------------------------
	//--------------------------------------------------------------------------

	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e) {

		if( e.getActionCommand().equalsIgnoreCase(ACTION_EXIT) ) {
			
			System.exit(0);
			
		} else if( e.getActionCommand().equalsIgnoreCase( "Speak" ) ) {

			String text = getTextField().getText();		
			if ((text == null) || (text.trim().length() == 0)) {
				JOptionPane.showMessageDialog(null, "Please enter a valid text to speak", "Network Utility", JOptionPane.ERROR_MESSAGE);				
			}
			else {
				String resultString = TextToSpeechLaunch.speak(text);			
				getTaskDetailTextArea().setText(resultString);
			}

		} else if( e.getActionCommand().equalsIgnoreCase(ACTION_SWITCH_TO_NETWORK) ) {
			
			switchFrame(this.networkWindow);

		} else if( e.getActionCommand().equalsIgnoreCase(ACTION_SWITCH_TO_BANDWIDTH) ) {
			
			switchFrame(this.bandwidthWindow);

		} else if( e.getActionCommand().equalsIgnoreCase(ACTION_SWITCH_TO_SPEECH) ) {
			
			switchFrame(this.speechWindow);
			
		} else {
			System.err.println(e.getActionCommand() + " " + e.getSource().toString());
		}
	}


}
