package rsa_server;

import java.io.*;
import java.net.*;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;



public class RSA_Server {

    public static void main(String[] args) throws Exception{
        
        
        String PATH = "C:/Users/meksa/Desktop/RazmenaFajlovaRSA/";
        
        
        System.out.println( "Zapocinjem generisanje privatnog kljuca i javnog kljuca" );
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA"); 
        keyGen.initialize(2048);
        KeyPair key = keyGen.generateKeyPair();
        System.out.println( "\nZavrseno generisanje privatnog i javnog kljuca" );  
        byte [] MojKljuc = key.getPublic().getEncoded();
        
        /////////////////////////////////////////////////////////////////////////////////////////////
        ServerSocket ss = new ServerSocket(1612);
        Socket s = ss.accept();
        //////////////////////////////////Prima Kljuc/////////////////////////////////////////////////
        byte [] KljucByte = new byte[2048];
        InputStream streamKljuc = s.getInputStream();
        int PrimljenKljuc = streamKljuc.read(KljucByte);
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(KljucByte));
        
        FileOutputStream outKljuc = new FileOutputStream(PATH +"KljucKlijent.key");
        outKljuc.write(KljucByte);
        System.out.println("Server je primio Public Key od klijenta i sacuvao ga u fajl");
        
        
        ////////////////////////////////Salje kljuc///////////////////////////////////////
        DataOutputStream dosKljuc = new DataOutputStream(s.getOutputStream());
        dosKljuc.write(MojKljuc);
        dosKljuc.flush();
        System.out.println("Server je poslao klijentu Public Key");
        ////////////////////////////Prima Fajl///////////////////////////////////////////
        byte data[] = new byte[300];
        InputStream stream = s.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(data, 0 , stream.read(data));		
	byte result[] = baos.toByteArray();
        
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key.getPrivate());
        byte[] cipherText = cipher.doFinal(result);
        
        FileOutputStream out = new FileOutputStream(PATH+"PrimljenoOdKlijenta.txt");
        out.write(cipherText);
        System.out.println("Server je primio fajl od klijenta i sacuvao je taj fajl");
        
        ////////////////////Salje Fajl////////////////////////////////////////////
     
        File file = new File(PATH+"Server.txt"); 
        byte[] fajlByte = new byte[245]; 
        FileInputStream fis = new FileInputStream(file);
        fis.read(fajlByte); 
        fis.close();
        
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cipherTextE = cipher.doFinal(fajlByte);
        
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        dos.write(cipherTextE);
        dos.flush();
        System.out.println("Klijent je poslao fajl serveru");
      
        //////////////////////////////////////////////////////////////////////////
        
        
        }
    
            
        
  
        
       
        
    }
       
        
       
        
    

