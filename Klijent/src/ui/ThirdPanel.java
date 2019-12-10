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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import klijent.Klijent;
import java.io.File;
import java.security.InvalidKeyException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author Uros
 */
public class ThirdPanel  extends JPanel {
    private JButton meniBtn;
    private JButton closeBtn;
    private Klijent klijent;
    private ArrayList <JRadioButton> radioBtns;
    private ButtonGroup radioGroup;
    private JButton meniSendBtn;
    private JFileChooser fc;
    
    //najbitniji panel za RSA
    public ThirdPanel () {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        //ovaj panel nije vidljiv jer je aktivan nakon ponasanja prvog
        this.setVisible(false);
        meniBtn = new JButton("ZAHTEVAJ MENI");
        closeBtn = new JButton("ZATVORI KONEKCIJU");
        meniSendBtn = new JButton("POSALJI");
        radioBtns = new ArrayList <JRadioButton>();
        radioGroup = new ButtonGroup();
        fc = new JFileChooser();
        meniBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                //kada klijent salje String server ceka isti tip podataka i salje odgovor
                klijent.saljiUTF("Zahtev za menijem");
                String meni [] = klijent.primiUTF().split("MENI:")[1].split("\n");
                //za dobijeni meni se kreiraju radio buttoni
                for (int i=0;i<meni.length;i++) {
                    JRadioButton rb = new JRadioButton(meni[i]);
                    radioBtns.add(rb);
                }
                //meni i close buttoni nisu vidljivi zbog mesta za meni
                meniBtn.setVisible(false);
                closeBtn.setVisible(false);
                //dodavanje opcije za slanje odabrane usluge iz menija
                add(meniSendBtn,FlowLayout.RIGHT);
                addRadioButtons();

            }
            
        });
        closeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                //gasenje konekcije
                klijent.saljiUTF("Zahtev za close");
                String porukaOdServera = klijent.primiUTF();
                if(porukaOdServera.equals("Exit")) {
                    try {
                        klijent.discon();
                    } catch (IOException ex) {
                        Logger.getLogger(ThirdPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.exit(0);
                }
            }
            
        });
        //najbitnije dugme koje pokazuje funkcije za sifrovanu komunikaciju
        meniSendBtn.addActionListener(new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent ae) {
                //ovo je odabrana opcija iz menija
                String komanda = radioGroup.getSelection().getActionCommand();
                if(komanda.endsWith("javnim kljucem")) {
                    klijent.saljiUTF("1.Zahtev za novim javnim kljucem");
//                    System.out.println(komanda);
                    try {
                        //klijent ceka od servera javni kljuc
                        klijent.primanjeJKljuca();
                    } catch (IOException ex) {
                        Logger.getLogger(ThirdPanel.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NoSuchAlgorithmException ex) {
                        Logger.getLogger(ThirdPanel.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvalidKeySpecException ex) {
                        Logger.getLogger(ThirdPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if(komanda.equals("2.Slanje fajla")) {
                    klijent.saljiUTF("2.Slanje fajla");
                    String pitanje = klijent.primiUTF();
                    if(pitanje.equals("2.Ime fajla?")) {
                        fc.setCurrentDirectory(new File(klijent.getPath()));
                        fc.setDialogTitle("Odaberi fajl");
                        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                        if(fc.showOpenDialog(meniSendBtn) == JFileChooser.APPROVE_OPTION) {

                        }
                        String putanja = fc.getSelectedFile().getAbsolutePath();
                        klijent.saljiUTF("2.Slanje fajla_imeFajla:"+fc.getSelectedFile().getName());
                    try {
                        //klijent salje fajl serveru(server ceka prijem fajla)
                        klijent.slanjeFajla(putanja);
                    } catch (IOException ex) {
                        Logger.getLogger(ThirdPanel.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvalidKeyException ex) {
                        Logger.getLogger(ThirdPanel.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalBlockSizeException ex) {
                        Logger.getLogger(ThirdPanel.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NoSuchAlgorithmException ex) {
                        Logger.getLogger(ThirdPanel.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (BadPaddingException ex) {
                        Logger.getLogger(ThirdPanel.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NoSuchPaddingException ex) {
                        Logger.getLogger(ThirdPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                    
//                    System.out.println();
                    
                }
                if(komanda.equals("3.Primanje fajla")) {
                    try {
                        klijent.saljiUTF("3.Primanje fajla");
                        String imeFajla = klijent.primiUTF();
                        String imeFajla2=null;
                        if(imeFajla.startsWith("3.Primanje fajla_imeFajla:")) {
                            imeFajla2 = imeFajla.split(":")[1];
                            System.out.println(imeFajla2);
//                            Thread.sleep(700);
//                            klijent.primanjeFajla(imeFajla);
                        }
                        klijent.saljiUTF("3.Primanje fajla_SALJI");
                        //klijent čeka fajl od servera
                        klijent.primanjeFajla(imeFajla2);
                        
                        
                    } catch (IOException ex) {
                        Logger.getLogger(ThirdPanel.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvalidKeyException ex) {
                        Logger.getLogger(ThirdPanel.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalBlockSizeException ex) {
                        Logger.getLogger(ThirdPanel.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (BadPaddingException ex) {
                        Logger.getLogger(ThirdPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if(komanda.equals("4.Gasenje konekcije.")) {
                    klijent.saljiUTF("Zahtev za close");
                    String porukaOdServera = klijent.primiUTF();
                    if(porukaOdServera.equals("Exit")) {
                    try {
                        //gasenje konekcije sa serverom i exit
                        klijent.discon();
                    } catch (IOException ex) {
                    }
                    System.exit(0);
                    }
                }
                
                
            }
        
        });
        
        
        
        this.add(meniBtn,FlowLayout.LEFT);
        this.add(closeBtn,FlowLayout.LEFT);
        
        
    }
    public void setVi(boolean b) {
        if(b==true) {
            this.setVisible(b);
        }
        else
            this.setVisible(false);
    }
    public void setKlijent(Klijent klijent) {
        this.klijent = klijent;
    }
    public void addRadioButtons() {
       for (int i=radioBtns.size()-1;i>-1;i--) {
//            System.out.println(btnLista.get(i).getText());
           radioBtns.get(i).setActionCommand(radioBtns.get(i).getText());
           this.add(radioBtns.get(i),FlowLayout.LEFT);
           this.radioGroup.add(radioBtns.get(i));
       }
   }
}
