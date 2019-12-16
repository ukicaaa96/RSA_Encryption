/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

/**
 *
 * @author Uros
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import klijent.Klijent;
import listeners.HostPortListener;
import listeners.MenuListener;
import listeners.NotifListener;


public class MainFrame extends JFrame {
//        svi paneli koji se koriste u gui-u
    ServerStatusPanel serverPanel;
    GenPanel genPanel;
    MenuPanel menuPanel;
    NotifPanel notifPanel;
    Klijent klijent;
	/*
    NotifPanel
    MenyPanel
    */
//        klijent koji je prisutan u svim klasama

	public MainFrame() {
		super("Sifrovanje javnim kljucem");
                setResizable(false);
                
                serverPanel = new ServerStatusPanel();
		genPanel = new GenPanel();
                menuPanel = new MenuPanel();
                notifPanel = new NotifPanel();
                klijent = new Klijent();
                
                serverPanel.setKlijent(klijent);
                genPanel.setKlijent(klijent);
                menuPanel.setKlijent(klijent);
                notifPanel.setKlijent(klijent);
                
                //Listeneri
                serverPanel.setNotifListener(new NotifListener () {
                    @Override
                    public void proslediPoruku(String poruka) {
                        notifPanel.dodajTextNaArea(poruka);
                    }
                });
                menuPanel.setNotifListener(new NotifListener () {
                    @Override
                    public void proslediPoruku(String poruka) {
                        notifPanel.dodajTextNaArea(poruka);
                    }
                });
                serverPanel.setMenuListener(new MenuListener () {
                    @Override
                    public void proslediMeni(String meni) {
                        menuPanel.addMenu(meni);
                    }
                
                });
                genPanel.setNotifListener(new NotifListener () {
                    @Override
                    public void proslediPoruku(String poruka) {
                        notifPanel.dodajTextNaArea(poruka);
                    }
                
                });
                genPanel.setHostPortListener(new HostPortListener () {
                    @Override
                    public void proslediHP() {
                        String host1 = serverPanel.getHost();
                        int port1 = serverPanel.getPort();
                        try {
                            klijent.povezivanjeNaServer(host1, port1);
                        } catch (IOException ex) {
                            
                        }
                    }
                    
                
                });
                menuPanel.setHostPortListener(new HostPortListener () {
                    @Override
                    public void proslediHP() {
                        String host1 = serverPanel.getHost();
                        int port1 = serverPanel.getPort();
                        try {
                            klijent.povezivanjeNaServer(host1, port1);
                        } catch (IOException ex) {
                            
                        }
                    }
                    
                
                });
                
                
                

		setLayout(new BorderLayout());
                //dodavanje na ekran
                notifPanel.setBackground(new Color(187,210,249));
                
                
                this.add(serverPanel,BorderLayout.NORTH);
                this.add(genPanel,BorderLayout.WEST);
                this.add(menuPanel,BorderLayout.CENTER);
                this.add(notifPanel,BorderLayout.SOUTH);
                
		setSize(725, 350);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                getContentPane().setBackground(new java.awt.Color(204, 166, 166));
                this.setVisible(true);
	}
}
