package com.ctb.tdc.bootstrap.processwrapper;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextPane;

public class CustomDialog extends JDialog {

	public CustomDialog(String processnames) {

	       
        JPanel topSpacer = new JPanel();
        topSpacer.setBounds(0,0,450,20);
		topSpacer.setBackground(Color.gray);
        this.add(topSpacer);

        JPanel basic = new JPanel();
        basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
        this.add(basic);

        JPanel topPanel = new JPanel(new GridLayout(3, 1));
        topPanel.setMaximumSize(new Dimension(450, 0));

        JLabel spacer = new JLabel(" ");
        topPanel.add(spacer);
        
        JLabel header = new JLabel("No other applications can be running during test administration.    ");
        header.setFont(new java.awt.Font("Tahoma", 1, 11));
        topPanel.add(header);

        JLabel direction = new JLabel("Please quit the following applications and start the test again.   ");
        direction.setFont(new java.awt.Font("Tahoma", 0, 11));
        direction.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        topPanel.add(direction, BorderLayout.SOUTH);

        basic.add(topPanel);
        
        JPanel leftspacer = new JPanel(new GridLayout(1, 1));
        leftspacer.add(new JLabel("            "));
		this.add(leftspacer,BorderLayout.WEST);
		
        JPanel textPanel = new JPanel(new BorderLayout());
      
        textPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        JTextPane pane = new JTextPane();
        pane.setEditable(false);
        pane.setBackground(null);

		String processname[] = processnames.split(",");
        String rProcesses = null;

        for (int i=0; i < processname.length; i++ ) {

        	if(i == 0) {
        		rProcesses = processname[i];
        	}
        	else {
        		rProcesses += "\n" +processname[i];
        	}
        	 
		}
       
        pane.setText(rProcesses);
        pane.setFont(new java.awt.Font("Tahoma", 0, 11));
        textPanel.add(pane);
       
		basic.add(textPanel);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
     
        JButton  close = new JButton("Quit");
        close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				dispose();
			}
		});
        close.setMnemonic(KeyEvent.VK_C);

   
        bottom.add(close);
        basic.add(bottom);

        bottom.setMaximumSize(new Dimension(450, 0));

        setSize(new Dimension(500, 300));
        System.out.println("text Panel "+pane.getSize().width +":"+ textPanel.getY());
        setUndecorated(true);
        
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        pack();
    }


   /* public static void main(String[] args) {
        new CustomDialog();
    }*/
}
