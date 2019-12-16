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
import java.net.ServerSocket;
import java.net.Socket;
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
    
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InterruptedException, FileNotFoundException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException {
        String putanja = null;
        Server server = new Server();
        int brojacZaKljuc = 0;
        int provera = 0;
        while(true) {
            server.povezivanje();
            System.out.println("--------------------------------------\n");
            if(brojacZaKljuc == 0) {
                brojacZaKljuc++;
                server.generisanjePiJKljuca();
            }
            
            System.out.println("Klijent je povezan.");
            DataInputStream in = new DataInputStream(new BufferedInputStream(server.getInputStream()));
            String porukaOdKlijenta = in.readUTF();
            if(porukaOdKlijenta.equals("Provera")) {
                System.out.println("Klijent je prisutan.");
            }
            if(porukaOdKlijenta.equals("Zahtev za meni")) {
                server.saljiUTF("MENI:1.Zahtev za novim javnim kljucem\n2.Slanje fajla\n3.Primanje fajla");
                System.out.println("Server je poslao meni klijentu.");
            }
            if(porukaOdKlijenta.equals("Primanje javnog kljuca")) {
                server.primanjeJKljuca();
            }
            if(porukaOdKlijenta.equals("Slanje javnog kljuca")) {
                Thread.sleep(200);
                server.slanjeJKljuca();
            }
            if(porukaOdKlijenta.equals("1.Zahtev za novim javnim kljucem")) {
                server.generisanjePiJKljuca();
                Thread.sleep(150);
                server.slanjeJKljuca();
            }
            if (porukaOdKlijenta.startsWith("2.Slanje fajla")) {
                    try {
                        if(porukaOdKlijenta.equals("2.Slanje fajla")) {
                            //server pita za ime fajla,da bi znao da u svom folderu pravilno imenuje
                            server.saljiUTF("2.Ime fajla?");
//                            server.primanjeFajla();
                        }
                        if(porukaOdKlijenta.startsWith("2.Slanje fajla_imeFajla:")) {
                            String imeFajla = porukaOdKlijenta.split(":")[1];
                            //server ceka na fajl
                            server.primanjeFajla(imeFajla);
//                            server.primanjeFajla();
                        }
                        
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            if(porukaOdKlijenta.startsWith("3.Primanje fajla")) {
                if(porukaOdKlijenta.equals("3.Primanje fajla")) {
                        //klijent prima fajl
                        putanja = server.biranjeSlucajnogFajla();
                        System.out.println(putanja);
                        String[] imeFajla = putanja.split("\\\\");
                        //server salje ime fajla klijentu radi bolje organizacije na njegovom folderu
                        server.saljiUTF("3.Primanje fajla_imeFajla:"+imeFajla[imeFajla.length-1]);
                        System.out.println(imeFajla[imeFajla.length-1]);
                    }
                if(porukaOdKlijenta.equals("3.Primanje fajla_SALJI")) {
                        Thread.sleep(1500);
                        //metoda za slanje fajla klijentu
                        server.slanjeFajla(putanja);
                        
//                        server.slanjeFajla(putanja);
//                        provera = 0;
                    }
            }
            
            server.zatvaranje();
            System.out.println("server je zatvoren.");
            System.out.println("--------------------------------------\n");
        }
        
                
    
    }
    
}
