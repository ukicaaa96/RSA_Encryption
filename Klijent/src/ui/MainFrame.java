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
	FirstPanel firstPanel;
        SecondPanel secondPanel;
        ThirdPanel thirdPanel;
        Klijent klijent;
	public MainFrame() {
		super("Sifrovanje javnim kljucem");
		firstPanel = new FirstPanel();
                secondPanel = new SecondPanel();
                thirdPanel = new ThirdPanel();
                
                klijent = new Klijent();
                
                firstPanel.setKlijent(klijent);
                secondPanel.setKlijent(klijent);
                thirdPanel.setKlijent(klijent);

                firstPanel.setThirdPanel(thirdPanel);
		setLayout(new BorderLayout());
                
                this.add(thirdPanel,BorderLayout.NORTH);
                this.add(firstPanel,BorderLayout.WEST);
                this.add(secondPanel,BorderLayout.SOUTH);

		setSize(725, 150);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
