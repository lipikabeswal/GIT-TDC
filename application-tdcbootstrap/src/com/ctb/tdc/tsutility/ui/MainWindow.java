package com.ctb.tdc.tsutility.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.ctb.tdc.tsutility.AppResourceBundleUtil;
import com.ctb.tdc.tsutility.AppConstants.AnalysisState;
import com.ctb.tdc.tsutility.analyzer.NetworkAnalyzer;
import com.ctb.tdc.tsutility.io.SimpleTextFileWriter;

/**
 * Test network connectivity.
 */
public class MainWindow extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private static final Dimension WINDOW_DIMENSIONS = new Dimension(650,550);
	private static final Color WINDOW_BACKGROUND_COLOR = SystemColor.control;
	private static final Font WINDOW_FONT_PLAIN = new Font("Arial", Font.PLAIN, 12);  //  @jve:decl-index=0:
	private static final Font WINDOW_FONT_BOLD = new Font("Arial", Font.BOLD, 12);  
	private static final String WINDOW_TITLE = AppResourceBundleUtil.getString("tsutility.mainWindow.title");
	private static final String INTRODUCTION = AppResourceBundleUtil.getString("tsutility.mainWindow.introduction");
	private static final String BUTTON_START = AppResourceBundleUtil.getString("tsutility.mainWindow.buttons.start");
	private static final String BUTTON_STOP  = AppResourceBundleUtil.getString("tsutility.mainWindow.buttons.stop"); 
	private static final String BUTTON_SAVE  = AppResourceBundleUtil.getString("tsutility.mainWindow.buttons.save");  //  @jve:decl-index=0:
	private static final String BUTTON_EXIT  = AppResourceBundleUtil.getString("tsutility.mainWindow.buttons.exit");
	private static final String TASKS_GROUPING = AppResourceBundleUtil.getString("tsutility.mainWindow.question.grouping");  //  @jve:decl-index=0:
	private static final String TASK_QUESTION1 = AppResourceBundleUtil.getString("tsutility.mainWindow.question.oasAvailable");  //  @jve:decl-index=0:
	private static final String TASK_QUESTION2 = AppResourceBundleUtil.getString("tsutility.mainWindow.question.oasdeliveryAvailable");  //  @jve:decl-index=0:
	private static final String TASK_QUESTION3 = AppResourceBundleUtil.getString("tsutility.mainWindow.question.dynamicPagesNotCached");  //  @jve:decl-index=0:
	private static final String TASK_QUESTION4 = AppResourceBundleUtil.getString("tsutility.mainWindow.question.downloadContent");  //  @jve:decl-index=0:
	private static final String TASK_RESULT_PASS          = AppResourceBundleUtil.getString("tsutility.mainWindow.result.pass");  //  @jve:decl-index=0:
	private static final String TASK_RESULT_FAIL          = AppResourceBundleUtil.getString("tsutility.mainWindow.result.fail");
	private static final String TASK_RESULT_UNATTEMPTED   = AppResourceBundleUtil.getString("tsutility.mainWindow.result.unattempted");
	private static final String TASK_RESULT_FAIL_DOWNLOAD = AppResourceBundleUtil.getString("tsutility.mainWindow.result.download.failed");
	private static final String DOWNLOAD_MINUTES = AppResourceBundleUtil.getString("tsutility.mainWindow.result.download.minutes");  //  @jve:decl-index=0:
	private static final String DOWNLOAD_MINUTE = AppResourceBundleUtil.getString("tsutility.mainWindow.result.download.minute");
	private static final String DOWNLOAD_SECONDS = AppResourceBundleUtil.getString("tsutility.mainWindow.result.download.seconds");  //  @jve:decl-index=0:
	private static final String DOWNLOAD_SECOND = AppResourceBundleUtil.getString("tsutility.mainWindow.result.download.second");
	private static final String DETAILS_GROUPING = AppResourceBundleUtil.getString("tsutility.mainWindow.details.grouping");  //  @jve:decl-index=0:
	private static final String DETAILS_DONE        = AppResourceBundleUtil.getString("tsutility.mainWindow.details.done");  //  @jve:decl-index=0:
	
	private static final String MODULE_NETWORK_TAB        = AppResourceBundleUtil.getString("tsutility.mainWindow.module.networkTab");
	private static final String MODULE_BANDWIDTH_TAB        = AppResourceBundleUtil.getString("tsutility.mainWindow.module.bandwidthTab");
	private static final String MODULE_SPEECH_TAB        = AppResourceBundleUtil.getString("tsutility.mainWindow.module.SpeechTab");
	
	private static final String ACTION_EXIT = "exit";
	
	private static final String ACTION_SWITCH_TO_NETWORK = "switchToNetwork";
	private static final String ACTION_SWITCH_TO_BANDWIDTH = "switchToBandwidth";
	private static final String ACTION_SWITCH_TO_SPEECH = "switchToSpeech";
	
	private static final String ACTION_START = "start";
	private static final String ACTION_STOP = "stop";  
	private static final String ACTION_SAVE = "save";
	private static final String ACTION_INTERRUPTED = "interrupted";  //  @jve:decl-index=0:
	private static final String ACTION_COMPLETE = "completed";
	
	
	private NetworkAnalyzer networkTroubleshooter = null;  //  @jve:decl-index=0:
	private FileDialog fileSaveDialog = null;
	private JPanel scaffoldPanel = null;
	private JPanel headerPanel = null;
	private JPanel contentPanel = null;
	private JPanel introductionPanel = null;
	private JLabel introductionLabel = null;
	private JPanel taskListPanel = null;
	private JPanel taskDetailPanel = null;
	private JTextArea taskDetailTextArea = null;
	private JPanel buttonsPanel = null;
	private JButton runButton = null;
	private JButton saveButton = null;
	private JButton exitButton = null;
	private JButton switchNetworkButton = null;
	private JButton switchBandwidthButton = null;
	private JButton switchSpeechButton = null;
	
	private JLabel questionLabel1 = null;
	private JLabel questionLabel2 = null;
	private JLabel questionLabel3 = null;
	private JLabel questionLabel4 = null;
	
	private JPanel resultPanel1 = null;
	private JPanel resultInitialPanel1 = null;
	private JPanel resultPassPanel1 = null;
	private JLabel resultPassIconLabel1 = null;
	private JPanel resultFailPanel1 = null;
	private JLabel resultFailIconLabel1 = null;
	private JPanel resultUnattemptedPanel1 = null;
	private JLabel resultUnattemptedLabel1 = null;
	private JPanel resultPanel2 = null;
	private JPanel resultInitialPanel2 = null;
	private JPanel resultPassPanel2 = null;
	private JPanel resultFailPanel2 = null;
	private JPanel resultUnattemptedPanel2 = null;
	private JLabel resultPassIconLabel2 = null;
	private JLabel resultFailIconLabel2 = null;
	private JLabel resultUnattemptedLabel2 = null;
	private JPanel resultPanel3 = null;
	private JPanel resultInitialPanel3 = null;
	private JPanel resultPassPanel3 = null;
	private JPanel resultFailPanel3 = null;
	private JPanel resultUnattemptedPanel3 = null;
	private JLabel resultPassIconLabel3 = null;
	private JLabel resultFailIconLabel3 = null;
	private JLabel resultUnattemptedLabel3 = null;
	private JPanel resultPanel4 = null;
	private JPanel resultInitialPanel4 = null;
	private JPanel resultPassPanel4 = null;
	private JPanel resultFailPanel4 = null;
	private JPanel resultUnattemptedPanel4 = null;
	private JLabel resultPassLabel4 = null;
	private JLabel resultUnattemptedLabel4 = null;
	private JLabel resultFailIconLabel4 = null;
	private JPanel resultProgressPanel4 = null;
	private JProgressBar resultProgressBar4 = null;
	
	private JFrame networkWindow = null;
	private JFrame bandwidthWindow = null;
	private JFrame speechWindow = null;
	
	//--------------------------------------------------------------------------

	
	public MainWindow() throws HeadlessException {
		super();
		
		initialize();
	}

	public MainWindow(GraphicsConfiguration gc) {
		super(gc);
		initialize();
	}

	public MainWindow(String title) throws HeadlessException {
		super(title);
		initialize();
	}

	public MainWindow(String title, GraphicsConfiguration gc) {
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
		
		this.fileSaveDialog = new FileDialog(this, "Save", FileDialog.SAVE);
		this.fileSaveDialog.setFilenameFilter( new TextFilenameFilter() );
		
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
	

	public void switchFrame(JFrame activeWindow) {
		
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
		getSwitchNetworkButton().setEnabled(false);
		getSwitchNetworkButton().setFont( WINDOW_FONT_BOLD );
		//getSwitchNetworkButton().setBackground(new Color(255, 255, 153));	// RGB = FFFF99
		
		getSwitchBandwidthButton().setEnabled(true);
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
			borderLayout.setVgap(10);
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
			taskDetailTextArea.setRows(3);
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

	/**
	 * This method initializes saveButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
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

			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.gridy = 3;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints2.gridy = 2;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints1.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints.gridy = 0;
			GridBagConstraints gbc4 = new GridBagConstraints();
			gbc4.gridx = 0;
			gbc4.anchor = GridBagConstraints.WEST;
			gbc4.insets = new Insets(5, 5, 5, 5);
			gbc4.fill = GridBagConstraints.HORIZONTAL;
			gbc4.weightx = 1.0D;
			gbc4.gridy = 3;
			questionLabel4 = new JLabel();
			questionLabel4.setText( TASK_QUESTION4 );
			questionLabel4.setFont( WINDOW_FONT_PLAIN );
			GridBagConstraints gbc3 = new GridBagConstraints();
			gbc3.gridx = 0;
			gbc3.insets = new Insets(5, 5, 5, 5);
			gbc3.anchor = GridBagConstraints.WEST;
			gbc3.fill = GridBagConstraints.HORIZONTAL;
			gbc3.weightx = 1.0D;
			gbc3.gridy = 2;
			questionLabel3 = new JLabel();
			questionLabel3.setText( TASK_QUESTION3 );
			questionLabel3.setFont( WINDOW_FONT_PLAIN );
			GridBagConstraints gbc2 = new GridBagConstraints();
			gbc2.gridx = 0;
			gbc2.anchor = GridBagConstraints.WEST;
			gbc2.insets = new Insets(5, 5, 5, 5);
			gbc2.fill = GridBagConstraints.HORIZONTAL;
			gbc2.weightx = 1.0D;
			gbc2.gridy = 1;
			questionLabel2 = new JLabel();
			questionLabel2.setText( TASK_QUESTION2 );
			questionLabel2.setFont( WINDOW_FONT_PLAIN );
			GridBagConstraints gbc1 = new GridBagConstraints();
			gbc1.gridx = 0;
			gbc1.anchor = GridBagConstraints.WEST;
			gbc1.insets = new Insets(5, 5, 5, 5);
			gbc1.fill = GridBagConstraints.HORIZONTAL;
			gbc1.gridwidth = 1;
			gbc1.weightx = 1.0D;
			gbc1.gridy = 0;
			questionLabel1 = new JLabel();
			questionLabel1.setText( TASK_QUESTION1 );
			questionLabel1.setFont( WINDOW_FONT_PLAIN );
			
			taskListPanel = new JPanel();
			taskListPanel.setLayout(new GridBagLayout());
			taskListPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(SystemColor.controlDkShadow, 1), TASKS_GROUPING, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), SystemColor.controlDkShadow));
			taskListPanel.setBackground(SystemColor.control);
			taskListPanel.add(questionLabel1, gbc1);
			taskListPanel.add(questionLabel2, gbc2);
			taskListPanel.add(questionLabel3, gbc3);
			taskListPanel.add(questionLabel4, gbc4);
			taskListPanel.add(getResultPanel1(), gridBagConstraints);
			taskListPanel.add(getResultPanel2(), gridBagConstraints1);
			taskListPanel.add(getResultPanel3(), gridBagConstraints2);
			taskListPanel.add(getResultPanel4(), gridBagConstraints3);
		}
		return taskListPanel;
	}

	
	
	
	//--------------------------------------------------------------------------
	
	
	
	
	
	
	
	/**
	 * This method initializes resultPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getResultPanel1() {
		if (resultPanel1 == null) {
			resultPanel1 = new JPanel();
			resultPanel1.setLayout(new CardLayout());
			resultPanel1.add(getResultInitialPanel1(), getResultInitialPanel1().getName());
			resultPanel1.add(getResultPassPanel1(), getResultPassPanel1().getName());
			resultPanel1.add(getResultFailPanel1(), getResultFailPanel1().getName());
			resultPanel1.add(getResultUnattemptedPanel1(), getResultUnattemptedPanel1().getName());
		}
		return resultPanel1;
	}

	/**
	 * This method initializes resultInitialPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getResultInitialPanel1() {
		if (resultInitialPanel1 == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			resultInitialPanel1 = new JPanel();
			resultInitialPanel1.setLayout(flowLayout);
			resultInitialPanel1.setName("resultInitialPanel1");
			resultInitialPanel1.setBackground( WINDOW_BACKGROUND_COLOR );
		}
		return resultInitialPanel1;
	}

	/**
	 * This method initializes resultPassPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getResultPassPanel1() {
		if (resultPassPanel1 == null) {
			resultPassIconLabel1 = new JLabel();
			resultPassIconLabel1.setFont( WINDOW_FONT_BOLD );
			resultPassIconLabel1.setText( TASK_RESULT_PASS );
			resultPassIconLabel1.setIcon(new ImageIcon(getClass().getResource("images/pass.gif")));

			
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			
			resultPassPanel1 = new JPanel();
			resultPassPanel1.setLayout(flowLayout);
			resultPassPanel1.setName("resultPassPanel1");
			resultPassPanel1.setBackground( WINDOW_BACKGROUND_COLOR );
			resultPassPanel1.add(resultPassIconLabel1, null);
		}
		return resultPassPanel1;
	}

	/**
	 * This method initializes resultFailPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getResultFailPanel1() {
		if (resultFailPanel1 == null) {
			resultFailIconLabel1 = new JLabel();
			resultFailIconLabel1.setFont( WINDOW_FONT_BOLD );
			resultFailIconLabel1.setText( TASK_RESULT_FAIL);
			resultFailIconLabel1.setIcon(new ImageIcon(getClass().getResource("images/fail.gif")));

			
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			
			resultFailPanel1 = new JPanel();
			resultFailPanel1.setLayout(flowLayout);
			resultFailPanel1.setName("resultFailPanel1");
			resultFailPanel1.setBackground( WINDOW_BACKGROUND_COLOR );
			resultFailPanel1.add(resultFailIconLabel1, null);
		}
		return resultFailPanel1;
	}

	/**
	 * This method initializes resultUnattemptedPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getResultUnattemptedPanel1() {
		if (resultUnattemptedPanel1 == null) {
			resultUnattemptedLabel1 = new JLabel();
			resultUnattemptedLabel1.setFont( WINDOW_FONT_BOLD );
			resultUnattemptedLabel1.setText( TASK_RESULT_UNATTEMPTED );
			
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			
			resultUnattemptedPanel1 = new JPanel();
			resultUnattemptedPanel1.setLayout(flowLayout);
			resultUnattemptedPanel1.setName("resultUnattemptedPanel1");
			resultUnattemptedPanel1.setBackground( WINDOW_BACKGROUND_COLOR );
			resultUnattemptedPanel1.add(resultUnattemptedLabel1, null);
		}
		return resultUnattemptedPanel1;
	}

	/**
	 * This method initializes resultPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getResultPanel2() {
		if (resultPanel2 == null) {
			resultPanel2 = new JPanel();
			resultPanel2.setLayout(new CardLayout());
			resultPanel2.add(getResultInitialPanel2(), getResultInitialPanel2().getName());
			resultPanel2.add(getResultPassPanel2(), getResultPassPanel2().getName());
			resultPanel2.add(getResultFailPanel2(), getResultFailPanel2().getName());
			resultPanel2.add(getResultUnattemptedPanel2(), getResultUnattemptedPanel2().getName());
		}
		return resultPanel2;
	}

	/**
	 * This method initializes resultInitialPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getResultInitialPanel2() {
		if (resultInitialPanel2 == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			
			resultInitialPanel2 = new JPanel();
			resultInitialPanel2.setLayout(flowLayout);
			resultInitialPanel2.setName("resultInitialPanel2");
			resultInitialPanel2.setBackground( WINDOW_BACKGROUND_COLOR );
		}
		return resultInitialPanel2;
	}

	/**
	 * This method initializes resultPassPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getResultPassPanel2() {
		if (resultPassPanel2 == null) {
			resultPassIconLabel2 = new JLabel();
			resultPassIconLabel2.setIcon(new ImageIcon(getClass().getResource("images/pass.gif")));
			resultPassIconLabel2.setFont( WINDOW_FONT_BOLD );
			resultPassIconLabel2.setText( TASK_RESULT_PASS );
			
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			
			resultPassPanel2 = new JPanel();
			resultPassPanel2.setLayout(flowLayout);
			resultPassPanel2.setName("resultPassPanel2");
			resultPassPanel2.setBackground( WINDOW_BACKGROUND_COLOR );
			resultPassPanel2.add(resultPassIconLabel2, null);
		}
		return resultPassPanel2;
	}

	/**
	 * This method initializes resultFailPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getResultFailPanel2() {
		if (resultFailPanel2 == null) {
			resultFailIconLabel2 = new JLabel();
			resultFailIconLabel2.setIcon(new ImageIcon(getClass().getResource("images/fail.gif")));
			resultFailIconLabel2.setFont( WINDOW_FONT_BOLD );
			resultFailIconLabel2.setText( TASK_RESULT_FAIL );
			
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			
			resultFailPanel2 = new JPanel();
			resultFailPanel2.setLayout(flowLayout);
			resultFailPanel2.setName("resultFailPanel2");
			resultFailPanel2.setBackground( WINDOW_BACKGROUND_COLOR );
			resultFailPanel2.add(resultFailIconLabel2, null);
		}
		return resultFailPanel2;
	}

	/**
	 * This method initializes resultUnattemptedPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getResultUnattemptedPanel2() {
		if (resultUnattemptedPanel2 == null) {
			resultUnattemptedLabel2 = new JLabel();
			resultUnattemptedLabel2.setFont( WINDOW_FONT_BOLD );
			resultUnattemptedLabel2.setText( TASK_RESULT_UNATTEMPTED );

			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			
			resultUnattemptedPanel2 = new JPanel();
			resultUnattemptedPanel2.setLayout(flowLayout);
			resultUnattemptedPanel2.setName("resultUnattemptedPanel2");
			resultUnattemptedPanel2.setBackground( WINDOW_BACKGROUND_COLOR );
			resultUnattemptedPanel2.add(resultUnattemptedLabel2, null);
		}
		return resultUnattemptedPanel2;
	}

	/**
	 * This method initializes resultPanel3	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getResultPanel3() {
		if (resultPanel3 == null) {
			resultPanel3 = new JPanel();
			resultPanel3.setLayout(new CardLayout());
			resultPanel3.add(getResultInitialPanel3(), getResultInitialPanel3().getName());
			resultPanel3.add(getResultPassPanel3(), getResultPassPanel3().getName());
			resultPanel3.add(getResultFailPanel3(), getResultFailPanel3().getName());
			resultPanel3.add(getResultUnattemptedPanel3(), getResultUnattemptedPanel3().getName());
		}
		return resultPanel3;
	}

	/**
	 * This method initializes resultInitialPanel3	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getResultInitialPanel3() {
		if (resultInitialPanel3 == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			
			resultInitialPanel3 = new JPanel();
			resultInitialPanel3.setLayout(flowLayout);
			resultInitialPanel3.setName("resultInitialPanel3");
			resultInitialPanel3.setBackground( WINDOW_BACKGROUND_COLOR );
		}
		return resultInitialPanel3;
	}

	/**
	 * This method initializes resultPassPanel3	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getResultPassPanel3() {
		if (resultPassPanel3 == null) {
			resultPassIconLabel3 = new JLabel();
			resultPassIconLabel3.setIcon(new ImageIcon(getClass().getResource("images/pass.gif")));
			resultPassIconLabel3.setFont( WINDOW_FONT_BOLD );
			resultPassIconLabel3.setText( TASK_RESULT_PASS );
			
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			
			resultPassPanel3 = new JPanel();
			resultPassPanel3.setLayout(flowLayout);
			resultPassPanel3.setName("resultPassPanel3");
			resultPassPanel3.setBackground( WINDOW_BACKGROUND_COLOR );
			resultPassPanel3.add(resultPassIconLabel3, null);
		}
		return resultPassPanel3;
	}

	/**
	 * This method initializes resultFailPanel3	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getResultFailPanel3() {
		if (resultFailPanel3 == null) {
			resultFailIconLabel3 = new JLabel();
			resultFailIconLabel3.setIcon(new ImageIcon(getClass().getResource("images/fail.gif")));
			resultFailIconLabel3.setFont( WINDOW_FONT_BOLD );
			resultFailIconLabel3.setText( TASK_RESULT_FAIL );
			
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			
			resultFailPanel3 = new JPanel();
			resultFailPanel3.setLayout(flowLayout);
			resultFailPanel3.setName("resultFailPanel3");
			resultFailPanel3.setBackground( WINDOW_BACKGROUND_COLOR );
			resultFailPanel3.add(resultFailIconLabel3, null);
		}
		return resultFailPanel3;
	}

	/**
	 * This method initializes resultUnattemptedPanel3	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getResultUnattemptedPanel3() {
		if (resultUnattemptedPanel3 == null) {
			resultUnattemptedLabel3 = new JLabel();
			resultUnattemptedLabel3.setFont( WINDOW_FONT_BOLD );
			resultUnattemptedLabel3.setText( TASK_RESULT_UNATTEMPTED );

			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			
			resultUnattemptedPanel3 = new JPanel();
			resultUnattemptedPanel3.setLayout(flowLayout);
			resultUnattemptedPanel3.setName("resultUnattemptedPanel3");
			resultUnattemptedPanel3.setBackground( WINDOW_BACKGROUND_COLOR );
			resultUnattemptedPanel3.add(resultUnattemptedLabel3, null);
		}
		return resultUnattemptedPanel3;
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
			resultProgressPanel4.add(getResultProgressBar4(), null);
		}
		return resultProgressPanel4;
	}

	/**
	 * This method initializes resultProgressBar4	
	 * 	
	 * @return javax.swing.JProgressBar	
	 */
	private JProgressBar getResultProgressBar4() {
		if (resultProgressBar4 == null) {
			resultProgressBar4 = new JProgressBar();
			resultProgressBar4.setValue(50);
		}
		return resultProgressBar4;
	}




	
	
	
	//--------------------------------------------------------------------------
	//--------------------------------------------------------------------------
	//--------------------------------------------------------------------------
	
	/**
	 * 
	 */
	private void startNetworkTroubleshooter() {
		
		if( this.networkTroubleshooter != null ) {
			stopNetworkTroubleshooter();
		}
		this.networkTroubleshooter = new NetworkAnalyzer(this);
		this.networkTroubleshooter.start();
		
	}

	
	/**
	 * 
	 */
	private void stopNetworkTroubleshooter() {
		
		if( this.networkTroubleshooter != null && this.networkTroubleshooter.isAlive() ) {
			this.networkTroubleshooter.interruptByUser();
		}
		this.networkTroubleshooter = null;
	}
	
	
	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e) {

		if( e.getActionCommand().equalsIgnoreCase(ACTION_EXIT) ) {
			stopNetworkTroubleshooter();
			System.exit(0);

		} else if( e.getActionCommand().equalsIgnoreCase(ACTION_SWITCH_TO_NETWORK) ) {
			switchFrame(this.networkWindow);

		} else if( e.getActionCommand().equalsIgnoreCase(ACTION_SWITCH_TO_BANDWIDTH) ) {
			switchFrame(this.bandwidthWindow);

		} else if( e.getActionCommand().equalsIgnoreCase(ACTION_SWITCH_TO_SPEECH) ) {
			switchFrame(this.speechWindow);
			
		} else if( e.getActionCommand().equalsIgnoreCase(ACTION_START) ) {
			setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) );
			resetTasks();
			getSaveButton().setEnabled(false);
			getRunButton().setText(BUTTON_STOP);
			getRunButton().setActionCommand(ACTION_STOP);
			startNetworkTroubleshooter();
			
		} else if( e.getActionCommand().equalsIgnoreCase(ACTION_STOP) ) {
			getRunButton().setEnabled(false);
			stopNetworkTroubleshooter();
			
		} else if( e.getActionCommand().equalsIgnoreCase(ACTION_INTERRUPTED) ) {
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR) );
			getRunButton().setText(BUTTON_START);
			getRunButton().setActionCommand(ACTION_START);
			getRunButton().setEnabled(true);
			getSaveButton().setEnabled(true);
			
		} else if( e.getActionCommand().equalsIgnoreCase(ACTION_COMPLETE) ) {
			setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR) );
			getRunButton().setText(BUTTON_START);
			getRunButton().setActionCommand(ACTION_START);
			getRunButton().setEnabled(true);
			getSaveButton().setEnabled(true);
			getTaskDetailTextArea().setText( DETAILS_DONE );
			
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


	/**
	 * 
	 */	
	public void setAnalysisInterrupted() {
		ActionEvent ae = new ActionEvent(this, 0, ACTION_INTERRUPTED);
		this.actionPerformed(ae);
	}

	/**
	 * 
	 */
	public void setAnalysisComplete() {
		ActionEvent ae = new ActionEvent(this, 0, ACTION_COMPLETE);
		this.actionPerformed(ae);
	}
	
	
	/**
	 * 
	 */
	public void setResultForCheckingOas(AnalysisState result, String details) {
		CardLayout cl = (CardLayout) getResultPanel1().getLayout();
		
		if( result == AnalysisState.PASS ) {
			cl.show( getResultPanel1(), getResultPassPanel1().getName() );
		} else if( result == AnalysisState.FAIL ) {
			cl.show( getResultPanel1(), getResultFailPanel1().getName() );
			getTaskDetailTextArea().setText(details);
		} else if( result == AnalysisState.UNATTEMPTED ) {
			cl.show( getResultPanel1(), getResultUnattemptedPanel1().getName() );
		} else {
			cl.show( getResultPanel1(), getResultInitialPanel1().getName() );
		}
	}

	/**
	 * 
	 */
	public void setResultForCheckingOasDelivery(AnalysisState result, String details) {
		CardLayout cl = (CardLayout) getResultPanel2().getLayout();

		if( result == AnalysisState.PASS ) {
			cl.show( getResultPanel2(), getResultPassPanel2().getName() );
		} else if( result == AnalysisState.FAIL ) {
			cl.show( getResultPanel2(), getResultFailPanel2().getName() );
			getTaskDetailTextArea().setText(details);
		} else if( result == AnalysisState.UNATTEMPTED ) {
			cl.show( getResultPanel2(), getResultUnattemptedPanel2().getName() );
		} else {
			cl.show( getResultPanel2(), getResultInitialPanel2().getName() );
		}
	}
	
	/**
	 * 
	 */
	public void setResultForCheckingDynamicPages(AnalysisState result, String details) {
		CardLayout cl = (CardLayout) getResultPanel3().getLayout();

		if( result == AnalysisState.PASS ) {
			cl.show( getResultPanel3(), getResultPassPanel3().getName() );
		} else if( result == AnalysisState.FAIL ) {
			cl.show( getResultPanel3(), getResultFailPanel3().getName() );
			getTaskDetailTextArea().setText(details);
		} else if( result == AnalysisState.UNATTEMPTED ) {
			cl.show( getResultPanel3(), getResultUnattemptedPanel3().getName() );
		} else {
			cl.show( getResultPanel3(), getResultInitialPanel3().getName() );
		}
	}

	/**
	 * 
	 */
	public void setResultForDownloadContent(AnalysisState result, String details, int percentComplete, long millis) {
		
		if( percentComplete < 0 ) 
			percentComplete = 0;
		else if( percentComplete > 100 ) 
			percentComplete = 100;
		
		CardLayout cl = (CardLayout) getResultPanel4().getLayout();
		
		if( result == AnalysisState.PASS ) {
			if( percentComplete == 100 ) {
				this.resultPassLabel4.setText( formatDuration(millis) );
				cl.show( getResultPanel4(), getResultPassPanel4().getName() );
			} else {
				cl.show( getResultPanel4(), getResultProgressPanel4().getName() );
				getResultProgressBar4().setValue(percentComplete);
			}
		} else if( result == AnalysisState.FAIL ) {
			cl.show( getResultPanel4(), getResultFailPanel4().getName() );
			getTaskDetailTextArea().setText(details);
		} else if( result == AnalysisState.UNATTEMPTED ) {
			cl.show( getResultPanel4(), getResultUnattemptedPanel4().getName() );
		} else {
			cl.show( getResultPanel4(), getResultInitialPanel4().getName() );
		}
	}
	

	
	/**
	 * 
	 */
	private void resetTasks() {
		CardLayout cl;
		
		cl = (CardLayout) getResultPanel1().getLayout();
		cl.show( getResultPanel1(), getResultInitialPanel1().getName() );
		
		cl = (CardLayout) getResultPanel2().getLayout();
		cl.show( getResultPanel2(), getResultInitialPanel2().getName() );

		cl = (CardLayout) getResultPanel3().getLayout();
		cl.show( getResultPanel3(), getResultInitialPanel3().getName() );

		cl = (CardLayout) getResultPanel4().getLayout();
		cl.show( getResultPanel4(), getResultInitialPanel4().getName() );
		
		getTaskDetailTextArea().setText("");
	}


	
	
	/**
	 * 
	 */
	private String formatDuration(long millis) {
		String output = "";

		long minutes = (millis/1000) / 60;
		long seconds = (millis/1000) % 60;
		
		if( minutes > 1 ) {
			output = String.valueOf(minutes) + " " + DOWNLOAD_MINUTES;
		} else if( minutes > 0 ) {
			output = String.valueOf(minutes) + " " + DOWNLOAD_MINUTE;
		}
		
		if( seconds > 1 ) {
			output += " " + String.valueOf(seconds) + " " + DOWNLOAD_SECONDS;
		} else if( seconds > 0 ) {
			output += " " + String.valueOf(seconds) + " " + DOWNLOAD_SECOND;
		} else {
			output = " < 1 " + DOWNLOAD_SECOND;
		}
		
		return output;
	}


	/**
	 * @param filename2 
	 * 
	 */
	private void saveResultsToFile(String directory, String filename) {
		
		try {
			SimpleTextFileWriter resultsFile = new SimpleTextFileWriter(directory, filename);
			
			resultsFile.writeDecorativeLine();
			resultsFile.writeln( this.getTitle() );
			resultsFile.writeDateTimestamp();
			resultsFile.writeDecorativeLine();
			resultsFile.writeln();
			
			resultsFile.writeln( this.questionLabel1.getText() );
			if( getResultPassPanel1().isVisible() ) 
				resultsFile.writeln( this.resultPassIconLabel1.getText() );
			else if( getResultFailPanel1().isVisible() )
				resultsFile.writeln( this.resultFailIconLabel1.getText() );
			else if( getResultUnattemptedPanel1().isVisible() ) 
				resultsFile.writeln( this.resultUnattemptedLabel1.getText() );
			resultsFile.writeln();
			
			resultsFile.writeln( this.questionLabel2.getText() );
			if( getResultPassPanel2().isVisible() ) 
				resultsFile.writeln( this.resultPassIconLabel2.getText() );
			else if( getResultFailPanel2().isVisible() )
				resultsFile.writeln( this.resultFailIconLabel2.getText() );
			else if( getResultUnattemptedPanel2().isVisible() ) 
				resultsFile.writeln( this.resultUnattemptedLabel2.getText() );
			resultsFile.writeln();

			resultsFile.writeln( this.questionLabel3.getText() );
			if( getResultPassPanel3().isVisible() ) 
				resultsFile.writeln( this.resultPassIconLabel3.getText() );
			else if( getResultFailPanel3().isVisible() )
				resultsFile.writeln( this.resultFailIconLabel3.getText() );
			else if( getResultUnattemptedPanel3().isVisible() ) 
				resultsFile.writeln( this.resultUnattemptedLabel3.getText() );
			resultsFile.writeln();

			resultsFile.writeln( this.questionLabel4.getText() );
			if( getResultPassPanel4().isVisible() ) 
				resultsFile.writeln( this.resultPassLabel4.getText() );
			else if( getResultFailPanel4().isVisible() )
				resultsFile.writeln( this.resultFailIconLabel4.getText() );
			else if( getResultUnattemptedPanel4().isVisible() ) 
				resultsFile.writeln( this.resultUnattemptedLabel1.getText() );
			resultsFile.writeln();

			resultsFile.writeln();
			resultsFile.writeln( DETAILS_GROUPING );
			resultsFile.writeDecorativeLine();
			resultsFile.writeln( getTaskDetailTextArea().getText() );
			
			resultsFile.close();
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, 
							AppResourceBundleUtil.getString("tsutility.mainWindow.saveDialog.error.message.ioException"), 
							AppResourceBundleUtil.getString("tsutility.mainWindow.saveDialog.error.title"), 
							JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
}  //  @jve:decl-index=0:visual-constraint="7,7"
