/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.FileChooser;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JButton;
import javax.swing.JFileChooser;

/**
 *
 * @author Uros
 */
public class MenuPanel extends JJPanel{
   private JButton m1,m2,m3,m4,m5;
   private ArrayList <JButton> mBtns;
   private JFileChooser fc;
    public MenuPanel () {
        mBtns = new ArrayList <JButton>();
        fc = new JFileChooser();
//        m1 = new JButton("1.Zahtev za novim javnim kljucem");
//        m2 = new JButton("2.Slanje fajla");
//        m3 = new JButton("3.Primanje fajla");
//        
//        m1.setBackground(Color.WHITE);
//        m2.setBackground(Color.WHITE);
//        m3.setBackground(Color.WHITE);
//        m1.setT;
//        m4 = new JButton("4");
        
//        m4 = new JButton("D");
        
//        ;
//        this.add(m1);
//        this.add(m2);
//        this.add(m3);
//        this.add(m4);
        setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        
    }
    public void addMenu (String menu) {
        if(mBtns.size()>1) {
            for (int i=0;i<mBtns.size();i++) {
                this.remove(mBtns.get(i));
            }
            mBtns.clear();
        }
        
//        System.out.println(mBtns.size());
        this.revalidate();
        String [] menu2 = menu.split("MENI:")[1].split("\n");
        for (int i=0;i<menu2.length;i++) {
            JButton btn = new JButton(menu2[i]);
            btn.setBackground(Color.WHITE);
            mBtns.add(btn);
            this.add(mBtns.get(i));
//            this.add(btn);
        }
        this.revalidate();
        
        //podesavanjeBtnsa
        for (int i=0;i<mBtns.size();i++) {
              if(mBtns.get(i).getText().equals("1.Zahtev za novim javnim kljucem")) {
                  mBtns.get(i).addActionListener(new ActionListener () {
                      @Override
                      public void actionPerformed(ActionEvent ae) {
                          hostPortListener.proslediHP();
                          klijent.saljiUTF("1.Zahtev za novim javnim kljucem");
                          try {
                              klijent.primanjeJKljuca();
                              notifListener.proslediPoruku("Klijent je primio novi javni kljuc.");
                              klijent.setPrimljenPk(true);
                          } catch (IOException ex) {
                              notifListener.proslediPoruku("Pogresan PATH.");
                          } catch (NoSuchAlgorithmException ex) {
                              notifListener.proslediPoruku("Nepostojeci algoritam je primenjen.");
                          } catch (InvalidKeySpecException ex) {
                              notifListener.proslediPoruku("Pogresan key.");
                          }
                      }
                  
                  });
              }
              if(mBtns.get(i).getText().equals("2.Slanje fajla")) {
                  JButton btn = mBtns.get(i);
                  mBtns.get(i).addActionListener(new ActionListener () {
                      @Override
                      public void actionPerformed(ActionEvent ae) {
                        if(klijent.isPrimljenPk()==true) {
                            hostPortListener.proslediHP();
                            klijent.saljiUTF("2.Slanje fajla");
                            String pitanje = klijent.primiUTF();
                              try {
                                  klijent.zatvaranje();
                              } catch (IOException ex) {
                                  Logger.getLogger(MenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                              }
                            if(pitanje.equals("2.Ime fajla?")) {
                                fc.setCurrentDirectory(new File(klijent.getPath()));
                                fc.setDialogTitle("Odaberi fajl");
                                fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                                if(fc.showOpenDialog(btn) == JFileChooser.APPROVE_OPTION) {

                                }
                                hostPortListener.proslediHP();
                                String putanja = fc.getSelectedFile().getAbsolutePath();
                                klijent.saljiUTF("2.Slanje fajla_imeFajla:"+fc.getSelectedFile().getName());
                                try {
                                //klijent salje fajl serveru(server ceka prijem fajla)
                                Thread.sleep(500);
                                klijent.slanjeFajla(putanja);
                                notifListener.proslediPoruku("Klijent je poslao fajl serveru.");
                                klijent.zatvaranje();
                            }   catch (IOException ex) {
                                    Logger.getLogger(MenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (InvalidKeyException ex) {
                                    Logger.getLogger(MenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (IllegalBlockSizeException ex) {
                                    Logger.getLogger(MenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (NoSuchAlgorithmException ex) {
                                    Logger.getLogger(MenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (BadPaddingException ex) {
                                    Logger.getLogger(MenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (NoSuchPaddingException ex) {
                                    Logger.getLogger(MenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(MenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                                }
                        }
                            
                    }
                        if(klijent.isPrimljenPk()==false) {
                            notifListener.proslediPoruku("Klijent nema Public Key i zato ne moze da salje fajl.");
                        }
                        
                        
                        
                        
                }
                  
                  });
              }
              if(mBtns.get(i).getText().equals("3.Primanje fajla")) {
                  mBtns.get(i).addActionListener(new ActionListener () {
                      @Override
                      public void actionPerformed(ActionEvent ae) {
                          if(klijent.isPrimljenPk()==true) {
                            hostPortListener.proslediHP();
                            klijent.saljiUTF("3.Primanje fajla");
                            String imeFajla = klijent.primiUTF();
                            String imeFajla2=null;
                            if(imeFajla.startsWith("3.Primanje fajla_imeFajla:")) {
                                imeFajla2 = imeFajla.split(":")[1];
                                System.out.println(imeFajla2);
    //                            Thread.sleep(700);
    //                            klijent.primanjeFajla(imeFajla);
                            }
                            try {
                                klijent.zatvaranje();
                                hostPortListener.proslediHP();
                                klijent.saljiUTF("3.Primanje fajla_SALJI");
                              //klijent čeka fajl od servera
                              klijent.primanjeFajla(imeFajla2);
                              klijent.zatvaranje();
                              notifListener.proslediPoruku("Klijent je primio fajl od servera.");
                            } catch (IOException ex) {
                                notifListener.proslediPoruku("Taj fajl ne postoji..");

                            } catch (InvalidKeyException ex) {
                                notifListener.proslediPoruku("Pogresan key..");

                            } catch (IllegalBlockSizeException ex) {
                                notifListener.proslediPoruku("illegal block size");
                            } catch (BadPaddingException ex) {
                                notifListener.proslediPoruku("BadPaddingException");
                            }
                          }
                          if(klijent.isPrimljenPk()==false) {
                              notifListener.proslediPoruku("Klijent nema Public Key i zato ne moze da prima fajl.");
                          }
                        
                        
                      }
                  
                  });
              }
        }
//        System.out.println(mBtns.size()+"\n");
    }
}
