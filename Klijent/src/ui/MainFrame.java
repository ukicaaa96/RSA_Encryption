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
import javax.swing.JFrame;
import klijent.Klijent;


public class MainFrame extends JFrame {
//        svi paneli koji se koriste u gui-u
	FirstPanel firstPanel;
        SecondPanel secondPanel;
        ThirdPanel thirdPanel;
//        klijent koji je prisutan u svim klasama
        Klijent klijent;

	public MainFrame() {
		super("Sifrovanje javnim kljucem");
                setResizable(false);

		firstPanel = new FirstPanel();
                secondPanel = new SecondPanel();
                thirdPanel = new ThirdPanel();
                klijent = new Klijent();

                //svaki panel ce imati isti klijentski pokazivac
                firstPanel.setKlijent(klijent);
                secondPanel.setKlijent(klijent);
                thirdPanel.setKlijent(klijent);
                //prvi panel je u vezi sa trecim(jer su drugacije opcije)
                firstPanel.setThirdPanel(thirdPanel);
                

		setLayout(new BorderLayout());
                //dodavanje na ekran
                this.add(thirdPanel,BorderLayout.NORTH);
                this.add(firstPanel,BorderLayout.WEST);
                this.add(secondPanel,BorderLayout.SOUTH);

                
		setSize(725, 150);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                this.setVisible(true);
	}
}
