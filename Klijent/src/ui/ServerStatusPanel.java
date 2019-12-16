/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import listeners.MenuListener;
import listeners.NotifListener;

/**
 *
 * @author Uros
 */
public class ServerStatusPanel extends JJPanel {
    private JLabel hostLabel,portLabel,statusLabel,statusLabel2;
    private JTextField hostField,portField;
    private JButton proveraButton;
    private JButton zahtevButton;
    private JButton promeniButton;
//    private NotifListener notifListener;
    private MenuListener menuListener;
    public ServerStatusPanel () {
        
        hostLabel = new JLabel("HOST:");  
        portLabel = new JLabel("PORT:");  
        statusLabel = new JLabel("Status servera:");
        statusLabel2 = new JLabel(" ");

//        hostField = new JTextField("",13);
        hostField = new JTextField("192.168.1.6",13);
        portField = new JTextField("1612",7);

        proveraButton = new JButton("Proveri");
        zahtevButton = new JButton("Zahtevaj meni");

        proveraButton.setBackground(Color.WHITE);
        zahtevButton.setBackground(Color.WHITE);
        
        //////////LISTENERS/////////////////////////
        proveraButton.addActionListener(new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String host = hostField.getText();
                int port = Integer.parseInt(portField.getText());
                String poruka = "";
                try {
                    if(proveraButton.getText().equals("Novi HiP")) {
                        hostField.setEditable(true);
                        portField.setEditable(true);
                        proveraButton.setText("Proveri");
                        klijent.setPrimljenPk(false);
                        klijent.setGenerisanPPk(false);
                    }
                    else {
                        klijent.povezivanjeNaServer(host, port);
                        notifListener.proslediPoruku("Server funkcioniše.");
                        klijent.saljiUTF("Provera");
                        klijent.zatvaranje();
                        proveraButton.setText("Novi HiP");
                        hostField.setEditable(false);
                        portField.setEditable(false);
                    }
                } catch (IOException ex) {
                     notifListener.proslediPoruku("Server ne funkcionise.");
                }
                
                
            }
        
        
        });
        zahtevButton.addActionListener(new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String poruka = "";
                String host = getHost();
                int port = getPort();
                try {
                    klijent.povezivanjeNaServer(host, port);
                    klijent.saljiUTF("Zahtev za meni");
                    poruka = klijent.primiUTF();
                    notifListener.proslediPoruku("Klijent je primio meni.");
                    menuListener.proslediMeni(poruka);
                    klijent.zatvaranje();
                } catch (IOException ex) {
                    poruka = "Meni nije dostupan.";
                    notifListener.proslediPoruku(poruka);
                }
                
            }
        
        });
        
        
        ////////////////////////////////////////////
        
        this.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
      
        gc.gridy = 0;
        gc.gridx = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(15, 0, 0, 0);
        this.add(hostLabel,gc);

        gc.gridy = 0;
        gc.gridx = 1;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.fill = GridBagConstraints.NONE;
        gc.insets = new Insets(15, 10, 0, 0);
        this.add(hostField,gc);

        gc.gridy = 0;
        gc.gridx = 2;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.fill = GridBagConstraints.NONE;
        gc.insets = new Insets(15, 5, 0, 0);
        this.add(portLabel,gc);

        gc.gridy = 0;
        gc.gridx = 3;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.fill = GridBagConstraints.NONE;
        gc.insets = new Insets(15, 10, 0, 380);
        this.add(portField,gc);

        gc.gridy = 1;
        gc.gridx = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.fill = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets(10, 0, 0,-380);
        this.add(statusLabel,gc);

        gc.gridy = 1;
        gc.gridx = 1;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(10, 55, 0,-350);
        this.add(statusLabel2,gc);
  //      
        gc.gridy = 2;
        gc.gridx = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(10, 00, 0,-380);
        this.add(proveraButton,gc);

        gc.gridy = 2;
        gc.gridx = 1;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(10, 60, 0,-380);
        this.add(zahtevButton,gc);
      
    }
    
    public String getHost () {
        return this.hostField.getText();
    }
    public int getPort () {
        return Integer.parseInt(portField.getText());
    }
    public void setMenuListener (MenuListener listener) {
        this.menuListener = listener;
    }
    
    
}
