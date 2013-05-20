package com.ctb.tdc.bootstrap.processwrapper;


import javax.swing.JOptionPane;
public class Dialog extends JOptionPane {
	


public String showOptionDialog(){

	String[] forms = {"Form A/Form B/Espanol", "Form C/Form D/Espanol2"}; 
	
	int rc = showOptionDialog(null, "Please Select Form :","Form Selection", 0, JOptionPane.PLAIN_MESSAGE, null, forms, forms[0]);
	if (rc==-1) { 
		System.out.println("No Form was selected");
		return "No Form was selected";
	    
	   } 
	else {  
		 System.out.println(forms[rc] + " was clicked"); 
		return forms[rc];
	  
	   }
	  }
	
}	
	
	
	/*public class Dialog implements ActionListener {
		
		
        
        
	   JFrame myFrame = null;
	   int optionType = JOptionPane.YES_NO_OPTION;
	   int messageType = JOptionPane.INFORMATION_MESSAGE;
	   public static void main(String[] a) {
	      (new Dialog()).test();
	   }
	   private void test() {
	      myFrame = new JFrame("Select Form....");
	      myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      myFrame.setBounds(500,300,500,500);
	      myFrame.setForeground(Color.GREEN);
	      myFrame.setBackground(Color.GREEN);
	      Container myPane = myFrame.getContentPane();
	     
	      myPane.setLayout(new GridBagLayout());
	      GridBagConstraints c = new GridBagConstraints();
	      setMyConstraints(c,200,200,GridBagConstraints.CENTER);
	      myPane.add(getFieldPanel(),c);
	      setMyConstraints(c,200,201,GridBagConstraints.CENTER);
	      //myPane.add(getButtonPanel(),c);
	      myFrame.pack();
	      myFrame.setVisible(true);
	   }
	   private JPanel getFieldPanel() {
		 
	      JPanel p = new JPanel(new GridBagLayout());
	     
	    //  p.setBorder(BorderFactory.createTitledBorder("Settings"));
	      GridBagConstraints c = new GridBagConstraints();
	     // setMyConstraints(c,0,0,GridBagConstraints.EAST);
	     // p.add(new JLabel("Option Type:"),c);
	     // setMyConstraints(c,1,0,GridBagConstraints.WEST);
	      //p.add(getOptionPanel(),c);
	      setMyConstraints(c,0,1,GridBagConstraints.CENTER);
	      p.add(new JLabel("Message type:"),c);
	      setMyConstraints(c,1,1,GridBagConstraints.CENTER);
	      p.add(getMessagePanel(),c);
	      return p;
	   }
	   private JPanel getOptionPanel() {
	      JPanel myPanel = new JPanel(new GridBagLayout());
	      ButtonGroup myGroup = new ButtonGroup();
	      JRadioButton myButton = new JRadioButton("Yes-No",true);
	      myButton.addActionListener(this);
	      myGroup.add(myButton);
	      myPanel.add(myButton);

	      myButton = new JRadioButton("Yes-No-Cancel",false);
	      myButton.addActionListener(this);
	      myGroup.add(myButton);
	      myPanel.add(myButton);

	      myButton = new JRadioButton("Ok-Cancel",false);
	      myButton.addActionListener(this);
	      myGroup.add(myButton);
	      myPanel.add(myButton);
	      return myPanel;
	   }
	   private JPanel getMessagePanel() {
	      JPanel myPanel = new JPanel(new GridBagLayout());
	      ButtonGroup myGroup = new ButtonGroup();
	      JRadioButton myButton = new JRadioButton("Form A/Form B/ Espanol",true);
	      myButton.addActionListener(this);
	      myGroup.add(myButton);
	      myPanel.add(myButton);
	      
	      myButton = new JRadioButton("Form C/Form D/ Espanol2",false);
	      myButton.addActionListener(this);
	      myGroup.add(myButton);
	      myPanel.add(myButton);

	      myButton = new JRadioButton("Error",false);
	      myButton.addActionListener(this);
	      myGroup.add(myButton);
	      myPanel.add(myButton);

	      myButton = new JRadioButton("Plain",false);
	      myButton.addActionListener(this);
	      myGroup.add(myButton);
	      myPanel.add(myButton);
	      return myPanel;
	   }
	   private JPanel getButtonPanel() {
	      JPanel p = new JPanel(new GridBagLayout());
	      JButton myButton = new JButton("Show");
	      myButton.addActionListener(this);
	      p.add(myButton);
	      return p;
	   }
	   public void actionPerformed(ActionEvent e) {
	      String cmd = ((AbstractButton) e.getSource()).getText();
	      System.out.println("Button clicked: "+cmd);
	      if (cmd.equals("Information")) {
	      	 messageType = JOptionPane.INFORMATION_MESSAGE;
	      } else if (cmd.equals("Warning")) {
	      	 messageType = JOptionPane.WARNING_MESSAGE;
	      } else if (cmd.equals("Error")) {
	      	 messageType = JOptionPane.ERROR_MESSAGE;
	      } else if (cmd.equals("Plain")) {
	      	 messageType = JOptionPane.PLAIN_MESSAGE;
	      } else if (cmd.equals("Yes-No")) {
	      	 optionType = JOptionPane.YES_NO_OPTION;
	      } else if (cmd.equals("Yes-No-Cancel")) {
	      	 optionType = JOptionPane.YES_NO_CANCEL_OPTION;
	      } else if (cmd.equals("Ok-Cancel")) {
	      	 optionType = JOptionPane.OK_CANCEL_OPTION;
	      } else if (cmd.equals("Show")) {
	         JOptionPane.showConfirmDialog(myFrame, 
	         "Confirmation dialog box text message.", 
	         "Confirmation Dialog Box", optionType, messageType);
	      }
	   }
	   private void setMyConstraints(GridBagConstraints c, 
	      int gridx, int gridy, int anchor) {
	      c.gridx = gridx;
	      c.gridy = gridy;
	      c.anchor = anchor;
	   }
	}	
	
*/
	
	

   

