/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import klijent.Klijent;

/**
 *
 * @author Uros
 */
public class FirstPanel extends JPanel {
    private JLabel hostLabel,portLabel;
    private JTextField hostField,portField;
    private JButton sendBtn;
    private Klijent klijent;
    private ThirdPanel thirdPanel;
    public FirstPanel () {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        hostLabel = new JLabel("HOST");
        portLabel = new JLabel("PORT");
        hostField = new JTextField("192.168.1.6");
        portField = new JTextField("1612");
        sendBtn = new JButton("POVEZI SE");
        
        sendBtn.addActionListener(new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent ae){
                String host = hostField.getText();
                int port = Integer.parseInt(portField.getText());
                try {
                    klijent.povezivanjeNaServer(host, port);
                    setVisible(false);
                    thirdPanel.setVi(true);
                    
//                    DataInputStream in = new DataInputStream(new BufferedInputStream(klijent.getSocket()));
//                    String porukaOdServera = in.readUTF();
//                    System.out.println(porukaOdServera);
                    
                } catch (IOException ex) {
                    Logger.getLogger(FirstPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        });
        
        this.add(sendBtn,FlowLayout.LEFT);
        this.add(portField,FlowLayout.LEFT);
        this.add(portLabel,FlowLayout.LEFT);
        this.add(hostField,FlowLayout.LEFT);
        this.add(hostLabel,FlowLayout.LEFT);
        
    }

    public void setKlijent(Klijent klijent) {
        this.klijent = klijent;
    }
    public void setThirdPanel(ThirdPanel tp) {
        this.thirdPanel = tp;
    }
}
