/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Uros
 */
public class NotifPanel extends JJPanel {
    private JTextArea textArea;
    private JLabel obavLabel;
    
    public NotifPanel () {
        String empty = "";
        for (int i=0;i<60;i++) {
            empty+=" ";
        }
        textArea = new JTextArea("",4,30);
//        textArea
        obavLabel = new JLabel("Obavestenja:");
        textArea.setEditable(false);
        
        this.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        
        gc.gridy = 0;
        gc.gridx = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(0, -340, 0, 0);
        this.add(obavLabel,gc);
        
        gc.gridy = 1;
        gc.gridx = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(10, -340, 20, 0);
        this.add(new JScrollPane(textArea),gc);
    }
    public void dodajTextNaArea (String poruka) {
        textArea.setText("");
        textArea.append(poruka);
    }
}
