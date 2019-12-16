/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.Color;
import javax.swing.JPanel;
import klijent.Klijent;
import listeners.HostPortListener;
import listeners.NotifListener;

/**
 *
 * @author Uros
 */
public class JJPanel extends JPanel {
    Klijent klijent;
    NotifListener notifListener;
    HostPortListener hostPortListener;
    public JJPanel () {
        setBackground(new Color(187,218,249));
    }
    public void setKlijent(Klijent klijent) {
        this.klijent = klijent;
    }
    public Klijent getKlijent() {
        return this.klijent;
    }
    public void setNotifListener (NotifListener listener) {
        notifListener = listener;
    }
    public void setHostPortListener(HostPortListener listener) {
        hostPortListener = listener;
    }
}
