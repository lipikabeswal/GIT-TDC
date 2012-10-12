package com.ctb.demo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ti.eps.emu84.testAgency.EmulatorComponent;

public class CalcDemo {
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Graphing Calculator");
        
        frame.setAlwaysOnTop(true);
        frame.setResizable(false);
        frame.setIconImage(new ImageIcon("calc.png").getImage());
        
        EmulatorComponent emu = new EmulatorComponent(frame);
        
        emu.setFaceSize(EmulatorComponent.MEDIUM);
        emu.ResetEmulator();
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel emptyLabel = new JLabel("");
        //emptyLabel.setPreferredSize(new Dimension(175, 100));
        frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);

        //Display the window.
        frame.getContentPane().add(emu);
        frame.pack();
        
        frame.setVisible(true);
        
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}

