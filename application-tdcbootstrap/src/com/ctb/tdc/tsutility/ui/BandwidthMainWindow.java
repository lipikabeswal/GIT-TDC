/*synchronized*/
		
package com.ctb.tdc.tsutility.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import com.ctb.tdc.tsutility.AppResourceBundleUtil;
import com.ctb.tdc.tsutility.analyzer.SimulatorList;
import com.ctb.tdc.tsutility.io.SimpleTextFileWriter;


/**
 * Simulate network bandwidth.
 */
public class BandwidthMainWindow extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private static final Dimension WINDOW_DIMENSIONS = new Dimension(650,550);
	private static final Color WINDOW_BACKGROUND_COLOR = SystemColor.control;
	private static final Font WINDOW_FONT_PLAIN = new Font("Arial", Font.PLAIN, 12);  
	private static final Font WINDOW_FONT_BOLD = new Font("Arial", Font.BOLD, 12);  
	private static final String WINDOW_TITLE = AppResourceBundleUtil.getString("tsutility.mainWindow.title");
	private static final String INTRODUCTION = AppResourceBundleUtil.getString("tsutility.mainWindow.introductionBW");
	private static final String BUTTON_START = AppResourceBundleUtil.getString("tsutility.mainWindow.buttons.start");
	private static final String BUTTON_EXIT  = AppResourceBundleUtil.getString("tsutility.mainWindow.buttons.exit");
	private static final String BUTTON_SAVE  = AppResourceBundleUtil.getString("tsutility.mainWindow.buttons.save");  
	private static final String BUTTON_STOP  = AppResourceBundleUtil.getString("tsutility.mainWindow.buttons.stop"); 
	private static final String DETAILS_GROUPING = AppResourceBundleUtil.getString("tsutility.mainWindow.details.grouping");  
	
	private static final String TASKS_GROUPING = AppResourceBundleUtil.getString("tsutility.mainWindow.question.groupingBW");  
	private static final String TASK_RESULT_UNATTEMPTED   = AppResourceBundleUtil.getString("tsutility.mainWindow.result.unattempted");
	private static final String TASK_RESULT_FAIL_DOWNLOAD = AppResourceBundleUtil.getString("tsutility.mainWindow.result.download.failed");
	
	private static final String MODULE_NETWORK_TAB        = AppResourceBundleUtil.getString("tsutility.mainWindow.module.networkTab");
	private static final String MODULE_BANDWIDTH_TAB        = AppResourceBundleUtil.getString("tsutility.mainWindow.module.bandwidthTab");
	private static final String MODULE_SPEECH_TAB        = AppResourceBundleUtil.getString("tsutility.mainWindow.module.SpeechTab");
	
	private static final String ACTION_EXIT = "exit";

	private static final String ACTION_SWITCH_TO_NETWORK = "switchToNetwork";
	private static final String ACTION_SWITCH_TO_BANDWIDTH = "switchToBandwidth";
	private static final String ACTION_SWITCH_TO_SPEECH = "switchToSpeech";
	
	private static final String ACTION_START = "start";	
	private static final String ACTION_SAVE = "save";
	private static final String ACTION_STOP = "stop";  
	private static final String ACTION_END_PROCESS = "endProcess";  
		
	private JPanel scaffoldPanel = null;
	private JPanel headerPanel = null;
	private JPanel contentPanel = null;
	private JPanel introductionPanel = null;
	private JLabel introductionLabel = null;
	private JPanel taskListPanel = null;
	
	private JScrollPane pScroll = null;
	
	private JTextPane textDetailPanel = null;
	
	private JPanel buttonsPanel = null;
	private JButton runButton = null;
	private JButton saveButton = null;
	
	private JButton exitButton = null;
	private JButton switchNetworkButton = null;
	private JButton switchBandwidthButton = null;
	private JButton switchSpeechButton = null;

	private JPanel resultPanel4 = null;
	private JPanel resultInitialPanel4 = null;
	private JPanel resultPassPanel4 = null;
	private JPanel resultFailPanel4 = null;
	private JPanel resultUnattemptedPanel4 = null;
	private JLabel resultPassLabel4 = null;
	private JLabel resultUnattemptedLabel4 = null;
	private JLabel resultFailIconLabel4 = null;
	private JPanel resultProgressPanel4 = null;
	
	private JProgressBar resultProgressBar = null;
	
	private JLabel labelSimulation = null;
	private JTextField textField = null;
	private SimulatorList simulatorList = null;
	public ArrayList simulationArray = null;
	
	private static final String URL_DOWNLOAD  = AppResourceBundleUtil.getString("tsutility.simulator.contentDownload");
			
	private static final int SECOND_TO_RUN = 10;						
	
	private int MIN_NUMBER_MACHINES = 12;
	private int DEFAULT_NUMBER_MACHINES = 100;
	private int NUMBER_MACHINES_LIMIT = 200;
	private double GOOD_BANDWIDTH = 12;
	private double VIGILANT_BANDWIDTH = 8;
	private double currentBandwidthStatus = 12;
		
	
	private String url = null;
	
	private JFrame networkWindow = null;
	private JFrame bandwidthWindow = null;
	private JFrame speechWindow = null;
	
	private FileDialog fileSaveDialog = null;
	private String outputText = null;
	public boolean continueToRun = true;
	
	//--------------------------------------------------------------------------

	
	public BandwidthMainWindow() throws HeadlessException {
		super();
		initialize();
	}

	public BandwidthMainWindow(GraphicsConfiguration gc) {
		super(gc);
		initialize();
	}

	public BandwidthMainWindow(String title) throws HeadlessException {
		super(title);
		initialize();
	}

	public BandwidthMainWindow(String title, GraphicsConfiguration gc) {
		super(title, gc);
		initialize();
	}

	  class FixedSizePlainDocument extends PlainDocument {
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
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {

		readConfigurationInfo();				
		
		ImageIcon iconImage = new ImageIcon( MainWindow.class.getResource("images/icon.gif") );
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		this.fileSaveDialog = new FileDialog(this, "Save", FileDialog.SAVE);
		this.fileSaveDialog.setFilenameFilter( new TextFilenameFilter() );
		this.fileSaveDialog.setFile("results.txt");
		
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
		getSwitchBandwidthButton().setEnabled(false);
		getSwitchBandwidthButton().setFont( WINDOW_FONT_BOLD );		
		//getSwitchBandwidthButton().setBackground(new Color(255, 255, 153));	// RGB = FFFF99
		
		getSwitchNetworkButton().setEnabled(true);
		getSwitchSpeechButton().setEnabled(true);
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
			borderLayout.setVgap(5);
			contentPanel = new JPanel();
			contentPanel.setLayout(borderLayout);
			contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			contentPanel.setBackground(WINDOW_BACKGROUND_COLOR);

			contentPanel.add(getIntroductionPanel(), BorderLayout.NORTH);
			contentPanel.add(getTaskListPanel(), BorderLayout.CENTER);
			
	        JTextPane detailTextPane = getTaskDetailPanel();
			
			pScroll = new JScrollPane(detailTextPane);
			pScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			pScroll.setPreferredSize(new Dimension(250, 230));
			pScroll.setMinimumSize(new Dimension(10, 10));
			pScroll.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(SystemColor.controlDkShadow, 1), DETAILS_GROUPING, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), SystemColor.controlDkShadow), BorderFactory.createEmptyBorder(5, 5, 5, 5)));			
			pScroll.setBackground( WINDOW_BACKGROUND_COLOR);
			
			contentPanel.add(pScroll, BorderLayout.SOUTH);
			
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
	private JTextPane getTaskDetailPanel() {
		if (textDetailPanel == null) {

			textDetailPanel = new JTextPane();			
			//textDetailPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(SystemColor.controlDkShadow, 1), DETAILS_GROUPING, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), SystemColor.controlDkShadow), BorderFactory.createEmptyBorder(5, 5, 5, 5)));			
			textDetailPanel.setBackground( WINDOW_BACKGROUND_COLOR);

			StyledDocument doc = textDetailPanel.getStyledDocument();
	        addStylesToDocument(doc);
			
		}
		textDetailPanel.setEditable(false);
		return textDetailPanel;
	}
	
    protected void addStylesToDocument(StyledDocument doc) {
        //Initialize some styles.
        Style def = StyleContext.getDefaultStyleContext().
                        getStyle(StyleContext.DEFAULT_STYLE);

        Style regular = doc.addStyle("regular", def);
        StyleConstants.setFontFamily(def, "SansSerif");

        Style s = doc.addStyle("italic", regular);
        StyleConstants.setItalic(s, true);

        s = doc.addStyle("bold", regular);
        StyleConstants.setBold(s, true);

        s = doc.addStyle("small", regular);
        StyleConstants.setFontSize(s, 10);

        s = doc.addStyle("large", regular);
        StyleConstants.setFontSize(s, 16);

        s = doc.addStyle("iconPass", regular);
        StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);        
        ImageIcon iconPass = new ImageIcon(getClass().getResource("images/pass.gif"));        
        if (iconPass != null) {
            StyleConstants.setIcon(s, iconPass);
        }

        s = doc.addStyle("iconFail", regular);
        StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);        
        ImageIcon iconfail = new ImageIcon(getClass().getResource("images/fail.gif"));        
        if (iconfail != null) {
            StyleConstants.setIcon(s, iconfail);
        }

        s = doc.addStyle("iconWarning", regular);
        StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);        
        ImageIcon iconWarning = new ImageIcon(getClass().getResource("images/warning.gif"));        
        if (iconWarning != null) {
            StyleConstants.setIcon(s, iconWarning);
        }
        
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path,
                                               String description) {
        java.net.URL imgURL = BandwidthMainWindow.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
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
			
			buttonsPanel.add(getRunButton(), null);
			buttonsPanel.add(getSaveButton(), null);
			buttonsPanel.add(getExitButton(), null);
		}
		return buttonsPanel;
	}

	/**
	 * This method initializes runButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getRunButton() {
		if (runButton == null) {
			runButton = new JButton( BUTTON_START );
			runButton.setFont( WINDOW_FONT_PLAIN );
			runButton.setActionCommand( ACTION_START );
			runButton.addActionListener(this);
		}
		return runButton;
	}
	private JButton getSaveButton() {
		if (saveButton == null) {
			saveButton = new JButton( BUTTON_SAVE );
			saveButton.setFont(WINDOW_FONT_PLAIN);
			saveButton.setActionCommand( ACTION_SAVE );
			saveButton.setEnabled(false);			
			saveButton.addActionListener(this);
		}
		return saveButton;
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

			taskListPanel = new JPanel();
			taskListPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			taskListPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(SystemColor.controlDkShadow, 1), TASKS_GROUPING, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), SystemColor.controlDkShadow));
			taskListPanel.setBackground( SystemColor.control );
			
			JLabel label = new JLabel("Enter number of workstations to simulate (max " + NUMBER_MACHINES_LIMIT + "): ");
			label.setFont(new Font("Dialog", Font.PLAIN, 12));
			taskListPanel.add(label, null);
			
			taskListPanel.add(getTextField(), null);
			
        	taskListPanel.add(new JLabel("  "), null);
			
			taskListPanel.add(getResultPanel4(), null);
			
			setDownloadProgress(0);
			
		}
		
		return taskListPanel;
	}

	
	
	/**
	 * This method initializes resultPanel4	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getResultPanel4() {
		if (resultPanel4 == null) {
			resultPanel4 = new JPanel();
			resultPanel4.setLayout(new CardLayout());
			resultPanel4.add(getResultInitialPanel4(), getResultInitialPanel4().getName());
			resultPanel4.add(getResultProgressPanel4(), getResultProgressPanel4().getName());
			resultPanel4.add(getResultPassPanel4(), getResultPassPanel4().getName());
			resultPanel4.add(getResultFailPanel4(), getResultFailPanel4().getName());
			resultPanel4.add(getResultUnattemptedPanel4(), getResultUnattemptedPanel4().getName());
		}
		return resultPanel4;
	}

	/**
	 * This method initializes resultInitialPanel4	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getResultInitialPanel4() {
		if (resultInitialPanel4 == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			
			resultInitialPanel4 = new JPanel();
			resultInitialPanel4.setLayout(flowLayout);
			resultInitialPanel4.setName("resultInitialPanel4");
			resultInitialPanel4.setBackground( WINDOW_BACKGROUND_COLOR );
		}
		return resultInitialPanel4;
	}

	/**
	 * This method initializes resultPassPanel4	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getResultPassPanel4() {
		if (resultPassPanel4 == null) {
			resultPassLabel4 = new JLabel();
			resultPassLabel4.setFont( WINDOW_FONT_BOLD );
			resultPassLabel4.setText( "x of y" );
			
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			
			resultPassPanel4 = new JPanel();
			resultPassPanel4.setLayout(flowLayout);
			resultPassPanel4.setName("resultPassPanel4");
			resultPassPanel4.setBackground( WINDOW_BACKGROUND_COLOR );
			resultPassPanel4.add(resultPassLabel4, null);
		}
		return resultPassPanel4;
	}

	/**
	 * This method initializes resultFailPanel4	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getResultFailPanel4() {
		if (resultFailPanel4 == null) {
			resultFailIconLabel4 = new JLabel();
			resultFailIconLabel4.setIcon(new ImageIcon(getClass().getResource("images/fail.gif")));
			resultFailIconLabel4.setFont( WINDOW_FONT_BOLD );
			resultFailIconLabel4.setText( TASK_RESULT_FAIL_DOWNLOAD );
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			
			resultFailPanel4 = new JPanel();
			resultFailPanel4.setLayout(flowLayout);
			resultFailPanel4.setName("resultFailPanel4");
			resultFailPanel4.setBackground( WINDOW_BACKGROUND_COLOR );
			resultFailPanel4.add(resultFailIconLabel4, null);
		}
		return resultFailPanel4;
	}
 
	/**
	 * This method initializes resultUnattemptedPanel4	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getResultUnattemptedPanel4() {
		if (resultUnattemptedPanel4 == null) {
			resultUnattemptedLabel4 = new JLabel();
			resultUnattemptedLabel4.setFont( WINDOW_FONT_BOLD );
			resultUnattemptedLabel4.setText( TASK_RESULT_UNATTEMPTED );

			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			
			resultUnattemptedPanel4 = new JPanel();
			resultUnattemptedPanel4.setLayout(flowLayout);
			resultUnattemptedPanel4.setName("resultUnattemptedPanel4");
			resultUnattemptedPanel4.setBackground( WINDOW_BACKGROUND_COLOR );
			resultUnattemptedPanel4.add(resultUnattemptedLabel4, null);
		}
		return resultUnattemptedPanel4;
	}

	/**
	 * This method initializes resultProgressPanel4	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getResultProgressPanel4() {
		if (resultProgressPanel4 == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			flowLayout.setHgap(0);
			resultProgressPanel4 = new JPanel();
			resultProgressPanel4.setLayout(flowLayout);
			resultProgressPanel4.setName("resultInProgressPanel4");
			resultProgressPanel4.setBackground( WINDOW_BACKGROUND_COLOR );
			resultProgressPanel4.add(getResultProgressBar(), null);
		}
		return resultProgressPanel4;
	}

	/**
	 * This method initializes resultProgressBar4	
	 * 	
	 * @return javax.swing.JProgressBar	
	 */
	public JProgressBar getResultProgressBar() {
		if (resultProgressBar == null) {
			resultProgressBar = new JProgressBar();
		}
		return resultProgressBar; 
	}

	private JLabel getLabelSimulation() {
		if (labelSimulation == null) {
			labelSimulation = new JLabel();
			labelSimulation.setText( "Simulation" );
			labelSimulation.setFont( WINDOW_FONT_PLAIN );
		}
		return labelSimulation; 
	}
	
	private JTextField getTextField() {
		if (textField == null) {
			textField = new JTextField(2);	// allow 3 characters
			
			FixedSizePlainDocument fspd = new FixedSizePlainDocument(3);
			textField.setDocument(fspd);
			textField.setText(String.valueOf(DEFAULT_NUMBER_MACHINES));
		}
		return textField; 
	}
	


	
	
	
	//--------------------------------------------------------------------------
	//--------------------------------------------------------------------------
	//--------------------------------------------------------------------------
	/**
	 */
	private void performSimulation(String url) {
		
		System.out.println("Start Simulation");	

		this.url = url;
				
		int maxMachines = getMaxNumberMachines();	
		
		if (maxMachines > 0) {
			
			this.simulationArray = populateMachineList(maxMachines);
		
			this.outputText = "";		
			String text = "  Workstations" + "\t" + "Evaluation" + "\t" + "     Evaluation Description" + "\n\n";
			removeReportContent();
			insertReportString(text, "bold");
			
			enableUI(false);
			resetProgressBar();
	
			startSimulation();
		}		
	}

	
	/**
	 * 
	 */
	public void startSimulation() {

		getResultProgressBar().setMaximum(SECOND_TO_RUN);
		
		setDownloadProgress(0);
		
		this.simulatorList = new SimulatorList(this, ((Integer) this.simulationArray.get(0)).intValue(), this.url, SECOND_TO_RUN);
		
		this.simulatorList.start();
	}

	/**
	 * 
	 */
	public void endProcess(boolean userStop) {

	
		if (userStop) {
			
			this.simulatorList.stopSimulators();
			this.simulatorList.stop();		
						
			System.out.println("Stopped by user.");		
			insertReportString("\n" + "Stopped by user", "bold");			
		}
		else {
			System.out.println("Process terminated normally.");		
			insertReportString("\n" + "Done", "bold");
		}

		this.simulationArray = null;

		
		enableUI(true);
		
	}
	
	public boolean showSimulationDetails(SimulatorList sl) {

		long totalBytesRead = sl.getTotalBytesRead();

		int numberMachines = sl.numberMachines;
		
		long numberSeconds = sl.duration / 1000;
		
		double bandwidth = ((totalBytesRead * 8) / numberMachines) / numberSeconds;	// number of bits per second
		
		bandwidth = bandwidth / 1024;										// number Kb per second
		
		
		String byteReadStr = String.format("%8d", totalBytesRead);		
		String bandwidthStr = String.format("%4.0f", bandwidth);		

		System.out.println("endSimulation ( " + numberMachines + " )   ---   time spent = " + numberSeconds + " seconds   ---   totalBytesRead = " + byteReadStr + "    ---   bandwidth = " + bandwidthStr + " Kbps");
		
    	String text = " " + numberMachines + "\t\t";		
    	insertReportString(text, "regular");
    	String description = "";
    	
		if (bandwidth >= GOOD_BANDWIDTH) {
			this.continueToRun = true;
			this.currentBandwidthStatus = GOOD_BANDWIDTH;
	    	insertReportString(" ", "iconPass");
	    	description = "Students are unlikely to experience any interruptions";			
		}
		else 
		if (bandwidth >= VIGILANT_BANDWIDTH) {
			this.continueToRun = true;
			this.currentBandwidthStatus = VIGILANT_BANDWIDTH;			
	    	insertReportString(" ", "iconWarning");
	    	description = "Some students may experience interruptions";
		}
		else { 
			this.continueToRun = false;
			this.currentBandwidthStatus = 0;			
	    	insertReportString(" ", "iconFail");
	    	description = "Students are likely to experience interruptions";
		}

    	insertReportString("\t", "regular");
    	insertReportString("     ", "regular");
    	insertReportString(description, "regular");
		
    	insertReportString("\n", "regular");
    		
    	this.outputText += numberMachines  + "\t\t" + byteReadStr + "\t" + bandwidthStr + "\t\t" + description;	
    	this.outputText += System.getProperty("line.separator");
    	
    	this.repaint();
    	
		return this.continueToRun;
	}
	
	/**
	 * 
	 */
	private void insertReportString(String src, String style) {
    	JTextPane tdPane = getTaskDetailPanel();
    	StyledDocument doc = tdPane.getStyledDocument();
        try {
        	doc.insertString(doc.getLength(), src, doc.getStyle(style));
        } 
        catch (BadLocationException ble) {
        	ble.printStackTrace();
        }		
	}

	/**
	 * 
	 */
	private void removeReportContent() {
    	JTextPane tdPane = getTaskDetailPanel();
    	StyledDocument doc = tdPane.getStyledDocument();
        try {
        	doc.remove(0, doc.getLength());
        } 
        catch (BadLocationException ble) {
        	ble.printStackTrace();
        }		
	}
	
	/**
	 * 
	 */
	private void enableUI(boolean enabled) {

		if (enabled) {
			getRunButton().setText(BUTTON_START);
			getRunButton().setActionCommand(ACTION_START);
		}
		else {
			getRunButton().setText(BUTTON_STOP);
			getRunButton().setActionCommand(ACTION_STOP);			
		}
		getSaveButton().setEnabled(enabled);
		getTextField().setEnabled(enabled);		
	}

	/**
	 * 
	 */
	private void resetProgressBar() {

		getResultProgressBar().setMinimum(0);
		getResultProgressBar().setMaximum(10);			
	}
	

	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e) {

		if( e.getActionCommand().equalsIgnoreCase(ACTION_EXIT) ) {
			
			System.exit(0);
			
		} else if( e.getActionCommand().equalsIgnoreCase(ACTION_SWITCH_TO_NETWORK) ) {
			
			switchFrame(this.networkWindow);

		} else if( e.getActionCommand().equalsIgnoreCase(ACTION_SWITCH_TO_BANDWIDTH) ) {

			switchFrame(this.bandwidthWindow);

		} else if( e.getActionCommand().equalsIgnoreCase(ACTION_SWITCH_TO_SPEECH) ) {

			switchFrame(this.speechWindow);
			
		} else if( e.getActionCommand().equalsIgnoreCase(ACTION_START) ) {
			
			performSimulation(URL_DOWNLOAD);

		} else if( e.getActionCommand().equalsIgnoreCase(ACTION_END_PROCESS) ) {
			
			endProcess( false );
			
		} else if( e.getActionCommand().equalsIgnoreCase(ACTION_STOP) ) {
			
			endProcess( true );
			
		} else if( e.getActionCommand().equalsIgnoreCase(ACTION_SAVE) ) {
			this.fileSaveDialog.setVisible(true);
			String filename  = this.fileSaveDialog.getFile();
			String directory = this.fileSaveDialog.getDirectory(); 
			if( filename != null ) {
				saveResultsToFile(directory, filename);
			}
			
		} else {
			System.err.println(e.getActionCommand() + " " + e.getSource().toString());
		}
	}

	private void saveResultsToFile(String directory, String filename) {
		
		try {
			SimpleTextFileWriter resultsFile = new SimpleTextFileWriter(directory, filename);
			
			resultsFile.writeDecorativeLine();
			resultsFile.writeln( this.getTitle() );
			resultsFile.writeDateTimestamp();
			resultsFile.writeDecorativeLine();
			resultsFile.writeln();
			
	    	String headerText = "Workstation"  + "\t" + "Bytes Read" + "\t" + "Bandwidth" + "\t" + "Evaluation";		
			resultsFile.writeln();
			
			resultsFile.writeln(headerText);
			resultsFile.writeln(this.outputText);
			resultsFile.writeln();

			resultsFile.writeln("Note: Bandwidth = Kbps");
			resultsFile.writeln();
			
			resultsFile.close();

		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readConfigurationInfo() {
		
		String value;

		value = AppResourceBundleUtil.getString("tsutility.simulator.bandwidth.Good");				
		GOOD_BANDWIDTH = Double.valueOf(value).doubleValue();	
		
		value = AppResourceBundleUtil.getString("tsutility.simulator.bandwidth.Vigilant");				
		VIGILANT_BANDWIDTH = Double.valueOf(value).doubleValue();

	}
	
	/**
	 * 
	 */
	public void setDownloadProgress(int percentComplete) {
		CardLayout cl = (CardLayout) getResultPanel4().getLayout();
		cl.show( getResultPanel4(), getResultProgressPanel4().getName() );
		getResultProgressBar().setValue(percentComplete);
		this.repaint();
	}

	/**
	 */
	private ArrayList populateSimulationArray(ArrayList machineList) {

		ArrayList list = new ArrayList();

		for (int i=0 ; i<machineList.size() ; i++) {
			
			Integer numMachines = (Integer)machineList.get(i);
			
			list.add(new Integer(numMachines));
			
		}
		return list;
	}
	
	/**
	 */
	private ArrayList populateMachineList(int maxMachines) {
		
		ArrayList list = new ArrayList();

		int numInterval = maxMachines / 4;
			
		int num = numInterval;
		while (num <= maxMachines) {
			list.add(new Integer(num));		
			num += numInterval;
		}			
		
		Integer value = (Integer)list.get(list.size()-1);		
		if (value.intValue() != maxMachines) {
			list.add(new Integer(maxMachines));		
		}

		return list;
	}

	/**
	 */
	private int getMaxNumberMachines() {
		int machines = -1;
		String value = getTextField().getText();
		
		if ( (value != null) && (value.length() > 0)) {
			try {
				machines = Integer.valueOf(value);
			} catch (Exception e) {		
				machines = -1;
			}
		}

		if (machines <= 0) {
			JOptionPane.showMessageDialog(null, "Please enter a valid number.", "Network Utility", JOptionPane.ERROR_MESSAGE);
			machines = -1;
		}
		
		if (machines < MIN_NUMBER_MACHINES) {
			JOptionPane.showMessageDialog(null, "Value must be equal to or greater than " + MIN_NUMBER_MACHINES, "Network Utility", JOptionPane.ERROR_MESSAGE);
			machines = -1;
		}
		
		if (machines > NUMBER_MACHINES_LIMIT) {
			JOptionPane.showMessageDialog(null, "Value must be equal to or less than " + NUMBER_MACHINES_LIMIT, "Network Utility", JOptionPane.ERROR_MESSAGE);
			machines = -1;
		}
		
		return machines; 
	}
	

}
