/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author Uros
 */
public class TestServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        
        Server server = new Server();
        server.povezivanje();
        server.generisanjePiJKljuca();
        String line = "";
        String imeFajla2 = null;
        String putanja = null;
        DataInputStream in = new DataInputStream(new BufferedInputStream(server.getInputStream()));
        try {
            while(true) {
                line = in.readUTF();
                if(line.equals("Zahtev za menijem")) {
                    server.saljiUTF("MENI:1.Zahtev za novim javnim kljucem\n2.Slanje fajla\n3.Primanje fajla\n4.Gasenje konekcije.");
                }
                if (line.equals("Zahtev za close") | line.equals("4.Gasenje konekcije.")) {
                    server.saljiUTF("Exit");
                    break;
                }
                if (line.equals("1.Zahtev za novim javnim kljucem"))  {
                    System.out.println("NOVI JAVNI KLJUC");
                    server.generisanjePiJKljuca();
                    server.slanjeJKljuca();
                }
                if (line.startsWith("2.Slanje fajla")) {
                    try {
                        if(line.equals("2.Slanje fajla")) {
                            server.saljiUTF("2.Ime fajla?");
//                            server.primanjeFajla();
                        }
                        if(line.startsWith("2.Slanje fajla_imeFajla:")) {
                            String imeFajla = line.split(":")[1];
                            server.primanjeFajla(imeFajla);
//                            server.primanjeFajla();
                        }
                        
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(TestServer.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NoSuchPaddingException ex) {
                        Logger.getLogger(TestServer.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvalidKeyException ex) {
                        Logger.getLogger(TestServer.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalBlockSizeException ex) {
                        Logger.getLogger(TestServer.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (BadPaddingException ex) {
                        Logger.getLogger(TestServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (line.startsWith("3.Primanje fajla")) {
                    if(line.equals("3.Primanje fajla")) {
                        putanja = server.biranjeSlucajnogFajla();
                        System.out.println(putanja);
                        String[] imeFajla = putanja.split("\\\\");
                        server.saljiUTF("3.Primanje fajla_imeFajla:"+imeFajla[imeFajla.length-1]);
                        System.out.println(imeFajla[imeFajla.length-1]);
                    }
                    
                    if(line.equals("3.Primanje fajla_SALJI")) {
                        server.slanjeFajla(putanja);
                    }
                }
                if (line.equals("Razmena")) {
                    try {
                        server.primanjeJKljuca();
                        Thread.sleep(2000);
                        server.slanjeJKljuca();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(TestServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            
            }
            System.out.println("Connection is closed.");
            
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        
    }
    
}
