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
import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

public class App {

	public static void main(String[] args) throws IOException {
            
                //efikasnije pokretanje GUI-a
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new MainFrame();

			}
		});	
//                
//                String nesto = "E:\\Program\\ServerFolder\\prvi.txt";
//                String [] lista = nesto.split("\\\\");
//                System.out.println(lista[lista.length-1]);
		}
        
		
	}

//}
