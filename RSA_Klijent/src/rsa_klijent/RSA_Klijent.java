package rsa_klijent;

import java.io.*;
import java.net.*;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;



public class RSA_Klijent {
    
    public static void main(String[] args) throws Exception{
        
        
         String PATH = "C:/Users/meksa/Desktop/RazmenaFajlovaRSA/";
         
         
        System.out.println( "Zapocinjem generisanje privatnog kljuca i javnog kljuca" );
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair key = keyGen.generateKeyPair();
        System.out.println( "\nZavrseno generisanje Privatnog i javnog kljuca" );  
        byte [] Mojkljuc = key.getPublic().getEncoded();
    
        Socket s = new Socket ("192.168.1.5" , 1612);
    
        ///////////////////////Salje kljuc///////////////////////////////////////////
        
        DataOutputStream dosKljuc = new DataOutputStream(s.getOutputStream());
        dosKljuc.write(Mojkljuc);
        dosKljuc.flush();
        System.out.println("Klijent je poslao serveru Public Key");
        
        //////////////////////Prima kljuc///////////////////////////////////////////
        
        byte [] KljucByte = new byte[2048];
        InputStream streamKljuc = s.getInputStream();
        streamKljuc.read(KljucByte);
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(KljucByte));
        
        FileOutputStream outKljuc = new FileOutputStream(PATH+"KljucServer.key");
        outKljuc.write(KljucByte);
        System.out.println("Klijent je primio PublicKey od servera i sacuvao ga u fajl");
        
        ////////////////////////////Salje Fajl///////////////////////////////////////
        
        File file = new File(PATH+"Klijent.txt");
        byte[] fajlByte = new byte[245]; 
        FileInputStream fis = new FileInputStream(file);
        fis.read(fajlByte); 
        fis.close();
        
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cipherText = cipher.doFinal(fajlByte);
        
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        dos.write(cipherText);
        dos.flush();
        System.out.println("Klijent je poslao fajl serveru");
        
        /////////////////////////////Prima fajl//////////////////////////////////////
        byte data[] = new byte[300];
        InputStream stream = s.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	baos.write(data, 0 , stream.read(data));		
	byte result[] = baos.toByteArray();
        
        
        cipher.init(Cipher.DECRYPT_MODE, key.getPrivate());
        byte[] cipherTextD = cipher.doFinal(result);
        
        FileOutputStream out = new FileOutputStream(PATH+"PrimljenoOdServera.txt");
        out.write(cipherTextD);
        System.out.println("Server je primio fajl od klijenta i sacuvao je taj fajl");
        
        }
        

         

    
}


