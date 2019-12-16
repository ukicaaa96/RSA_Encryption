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
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Uros
 */
public class GenPanel extends JJPanel {
    private JLabel genLabel;
    private JButton genBtn,razmenaBtn;
    
    public GenPanel () {
        genLabel = new JLabel("Generisanje privatnog i javnog kljuca");
        genBtn = new JButton("Generisi");
        razmenaBtn = new JButton("Razmeni");
        
        
        genBtn.setBackground(Color.WHITE);
        razmenaBtn.setBackground(Color.WHITE);
        
        genBtn.addActionListener(new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    //"Zavrseno generisanje privatnog i javnog kljuca!"
                    klijent.generisanjePiJKljuca();
                    klijent.setGenerisanPPk(true);
                } catch (NoSuchAlgorithmException ex) {
//                    notifListener.proslediPoruku("Zapocinjem generisanje privatnog kljuca i javnog kljuca..\n");
                }
                notifListener.proslediPoruku("Zavrseno generisanje privatnog i javnog kljuca!");

            }
            
        });
        
        razmenaBtn.addActionListener(new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent ae) {
                
                try {
                    if(klijent.isGenerisanPPk() == true) {
                        hostPortListener.proslediHP();
                        klijent.saljiUTF("Primanje javnog kljuca");
                        Thread.sleep(100);
                        klijent.slanjeJKljuca();
                        klijent.zatvaranje();
                        Thread.sleep(100);
                        hostPortListener.proslediHP();
                        klijent.saljiUTF("Slanje javnog kljuca");
                        klijent.primanjeJKljuca();
                        klijent.zatvaranje();
                        notifListener.proslediPoruku("Klijent je poslao serveru svoj PublicKey.\nKlijent je primio PublicKey od servera.");
                        klijent.setPrimljenPk(true);
                    }
                    if(klijent.isGenerisanPPk() == false) {
                        notifListener.proslediPoruku("Ne moze se razmeniti Public Key ukoliko nije generisan");
                    }
                    
                    
                } 
                
                catch (IOException ex) {
                    notifListener.proslediPoruku("Greska kod slanja.");
                } catch (InterruptedException ex) {
                    Logger.getLogger(GenPanel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(GenPanel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvalidKeySpecException ex) {
                    Logger.getLogger(GenPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        });
        
        this.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        
        
        gc.gridy = 0;
        gc.gridx = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(15, 10, 0, 0);
        this.add(genLabel,gc);
        
        gc.gridy = 1;
        gc.gridx = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(15, 10, 0, 0);
        this.add(genBtn,gc);
        
        gc.gridy = 1;
        gc.gridx = 1;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(15, -100, 0, 0);
        this.add(razmenaBtn,gc);
        
        
    }
}
