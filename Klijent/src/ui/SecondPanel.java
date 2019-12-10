/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.FlowLayout;
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
import klijent.Klijent;

/**
 *
 * @author Uros
 */
public class SecondPanel extends JPanel{
    private JLabel genLbl;
    private JButton genBtn,sendBtn;
    private Klijent klijent;
    public SecondPanel ()   {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        this.genLbl = new JLabel("P i J kljuc:");
        this.genBtn = new JButton("GENERISI");
        this.sendBtn = new JButton("RAZMENI KLJUCEVE");
        this.klijent = new Klijent();
        
        //dugme za generisanje privatnog i javnog kljuca
        this.genBtn.addActionListener(new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    //generisanjePiJKljuca
                    klijent.generisanjePiJKljuca();
                } catch (NoSuchAlgorithmException ex) {
                    System.out.println("greska kod privatnog i javnog kljuca.");
                }
            }
        
        });
        this.sendBtn.addActionListener(new ActionListener () {
            @Override
            
            public void actionPerformed(ActionEvent ae) {
                try {
                    //razmena kljuceva sa serverom
                    klijent.saljiUTF("Razmena");
                    klijent.slanjeJKljuca();
                    klijent.primanjeJKljuca();
                } catch (IOException ex) {
                    Logger.getLogger(SecondPanel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(SecondPanel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvalidKeySpecException ex) {
                    Logger.getLogger(SecondPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        });
        
        this.add(sendBtn,FlowLayout.LEFT);
        this.add(genBtn,FlowLayout.LEFT);
        this.add(genLbl,FlowLayout.LEFT);
        
                
    }
    //svaki panel ima ovu klasu,znaci postoji samo jedan klijent za jedan gui
    void setKlijent(Klijent klijent) {
        this.klijent = klijent;
    }
}
