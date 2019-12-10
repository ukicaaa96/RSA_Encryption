package server;

import java.io.*;
import java.net.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;



public class Server {
    String PATH = "E:\\Program\\ServerFolder\\";
    KeyPairGenerator keyGen ; 
    KeyPair key; 
    byte [] MojKljuc;
    ServerSocket ss;
    Socket s;
    byte [] KljucByte;
    InputStream streamKljuc;
    int PrimljenKljuc;
    PublicKey publicKey;

    FileOutputStream outKljuc ;
    DataOutputStream dosKljuc;
    byte data[] ;
    InputStream stream;
    ByteArrayOutputStream baos ;		
    byte result[] ;

    Cipher cipher ;
    byte[] cipherText ;

    FileOutputStream out ;

    File file ; 
    byte[] fajlByte ; 
    FileInputStream fis ;
    byte[] cipherTextE ;

    DataOutputStream dos ;
    
    public Server () {}
    //metoda za generisanje privatnog i javnog kljuca
    public void generisanjePiJKljuca () throws NoSuchAlgorithmException {
        System.out.println( "Zapocinjem generisanje privatnog kljuca i javnog kljuca.." );
        keyGen = KeyPairGenerator.getInstance("RSA"); 
        keyGen.initialize(2048);
        key = keyGen.generateKeyPair();
        System.out.println( "Zavrseno generisanje privatnog i javnog kljuca!" );  
        MojKljuc = key.getPublic().getEncoded();
    }
    //metoda za prihvatanje konekcija
    public void povezivanje () throws IOException {
        System.out.println("Server je pokrenut.");
        ss = new ServerSocket(1612);
        s = ss.accept();
        System.out.println("Klijent je povezan!");
        DataOutputStream out  = new DataOutputStream(s.getOutputStream()); 
//        out.writeUTF("MENI:1.Zahtev za novim javnim ključem\n2.Slanje fajla\n3.Primanje fajla\n4.Gasenje konekcije.");
    }
    //metoda za slanje javnog kljuca
    public void slanjeJKljuca () throws IOException, IOException {
        dosKljuc = new DataOutputStream(s.getOutputStream());
        dosKljuc.write(MojKljuc);
        dosKljuc.flush();
        System.out.println("Server je poslao klijentu svoj PublicKey.");
    }
    //metoda koju server koristi za primanje javnog kljuca
    public void primanjeJKljuca () throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        KljucByte = new byte[2048];
        streamKljuc = s.getInputStream();
        PrimljenKljuc = streamKljuc.read(KljucByte);
        publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(KljucByte));
        
        outKljuc = new FileOutputStream(PATH +"\\PubK\\"+"KljucKlijent.key");
        outKljuc.write(KljucByte);
        System.out.println("Server je primio PublicKey od klijenta i sacuvao ga u fajl.");
    }
    //metoda koja se koristi kada server salje fajl
    public void slanjeFajla (String putanja) throws FileNotFoundException, IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
//        String odabranFajl = this.biranjeSlucajnogFajla();
        file = new File(putanja); 
        fajlByte = new byte[245]; 
        fis = new FileInputStream(file);
        fis.read(fajlByte); 
        fis.close();
        
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            cipherTextE = cipher.doFinal(fajlByte);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());

        }
        
        dos = new DataOutputStream(s.getOutputStream());
        dos.write(cipherTextE);
        dos.flush();
        System.out.println("Server je poslao fajl klijentu.");
    }
    //metoda koja se koristi kada server ocekuje fajl od klijenta
    public void primanjeFajla (String imeFajla) throws FileNotFoundException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        data = new byte[300];
        stream = s.getInputStream();
        baos = new ByteArrayOutputStream();
        baos.write(data, 0 , stream.read(data));		
        result = baos.toByteArray();
        
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key.getPrivate());
        cipherText = cipher.doFinal(result);
        
        FileOutputStream out = new FileOutputStream(PATH+"PrimljeniFajlovi\\"+imeFajla);
        out.write(cipherText);
        System.out.println("Server je primio fajl od klijenta i sacuvao je taj fajl.");
        out.close();
    }
    

    public InputStream getInputStream() throws IOException {
        return s.getInputStream();
    }
    public OutputStream getOuS () throws IOException {
        return this.s.getOutputStream();
    }
    //laksa komunikacija sa klijentom
    public void saljiUTF(String poruka) {
        DataOutputStream out;
        try {
            out = new DataOutputStream(getOuS());
            out.writeUTF(poruka);
        } catch (IOException ex) {
            System.out.println("greska pri unosu");
        }
    }
    public String getPath() {
        return this.PATH;
    }
    //biranje slucajnog fajla
    public String biranjeSlucajnogFajla () {
        File f = new File(PATH); // current directory
            ArrayList <String> fajlovi = new ArrayList <String>();
            File[] files = f.listFiles();
            for (File file : files) {
            try {
                if (file.isDirectory()) {

                } else {
//                        System.out.print("     file:");
                    fajlovi.add(file.getCanonicalPath());
                }

            }
            catch (IOException ex) {
                    System.out.println("fajl ne postoji.");
                }
		}
                Random rand = new Random(); 
                String nesto =  fajlovi.get(rand.nextInt(fajlovi.size())); 
                
                return nesto;
    }
        
        
}
        
       
        
    

