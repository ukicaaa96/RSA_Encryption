package klijent;

import java.io.*;
import java.net.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import ui.ThirdPanel;



public class Klijent {
    String PATH = "E:\\Program\\KlijentFolder\\";
    KeyPairGenerator keyGen;
    KeyPair key ;
    byte [] Mojkljuc ;
    Socket s;
    DataOutputStream dosKljuc ;
    byte [] KljucByte ;
    InputStream streamKljuc ;
    PublicKey publicKey ;
    FileOutputStream outKljuc ;
    File file ;
    byte[] fajlByte; 
    FileInputStream fis;
    Cipher cipher ;
    byte[] cipherText ;
    DataOutputStream dos ;
    byte data[] ;
    InputStream stream ;
    ByteArrayOutputStream baos ;
    byte[] cipherTextD;
    FileOutputStream out ;
    
    public Klijent () {}
    public void generisanjePiJKljuca () throws NoSuchAlgorithmException {
        System.out.println( "Zapocinjem generisanje privatnog kljuca i javnog kljuca" );
        keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        key = keyGen.generateKeyPair();
        System.out.println( "\nZavrseno generisanje Privatnog i javnog kljuca" );  
        Mojkljuc = key.getPublic().getEncoded();
        
    }
    public void povezivanjeNaServer (String host,int port) throws IOException {
        s = new Socket (host , port);
        System.out.println("Povezan na server.");
        
    }
    public void slanjeJKljuca () throws IOException {
        dosKljuc = new DataOutputStream(s.getOutputStream());
        dosKljuc.write(Mojkljuc);
        dosKljuc.flush();
        System.out.println("Klijent je poslao serveru PublicKey");
    }
    public void primanjeJKljuca () throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        KljucByte = new byte[2048];
        streamKljuc = s.getInputStream();
        streamKljuc.read(KljucByte);
        publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(KljucByte));
        
        outKljuc = new FileOutputStream(PATH +"\\PubK\\"+"KljucServer.key");
        outKljuc.write(KljucByte);
        System.out.println("Klijent je primio PublicKey od servera i sacuvao ga u fajl");
    
    }
    public void slanjeFajla (String putanja) throws FileNotFoundException, IOException, InvalidKeyException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException {
        file = new File(putanja);
        fajlByte = new byte[245]; 
        fis = new FileInputStream(file);
        fis.read(fajlByte); 
        fis.close();
        
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        cipherText = cipher.doFinal(fajlByte);
        
        dos = new DataOutputStream(s.getOutputStream());
        dos.write(cipherText);
        dos.flush();
        System.out.println("Klijent je poslao fajl serveru");
    }
    public void primanjeFajla (String imeFajla) throws IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        try {
            data = new byte[300];
            stream = s.getInputStream();
            baos = new ByteArrayOutputStream();
            baos.write(data, 0 , stream.read(data));
            byte result[] = baos.toByteArray();
            
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, key.getPrivate());
            cipherTextD = cipher.doFinal(result);
            
            out = new FileOutputStream(PATH+"PrimljeniFajlovi\\"+imeFajla);
            out.write(cipherTextD);
            System.out.println("Klijent je primio fajl od servera i sacuvao je taj fajl");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public InputStream getInS () throws IOException {
        return this.s.getInputStream();
    }
    public OutputStream getOuS () throws IOException {
        return this.s.getOutputStream();
    }
    public void saljiUTF(String poruka) {
        DataOutputStream out;
        try {
            out = new DataOutputStream(getOuS());
            out.writeUTF(poruka);
        } catch (IOException ex) {
            Logger.getLogger(ThirdPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String primiUTF() {
        String porukaOdServera=null;
        try {
            DataInputStream in = new DataInputStream(new BufferedInputStream(getInS()));
            porukaOdServera = in.readUTF();
//            if(porukaOdServera.startsWith("MENI:")) {}
//            else
//                System.out.println(porukaOdServera);
            }
            catch (IOException ex) {
                System.out.println("greska u soketu.");
            }
        return porukaOdServera;
    }
    public void discon () throws IOException {
        s.close();
    }
    public String getPath() {
        return this.PATH;
    }   
}


