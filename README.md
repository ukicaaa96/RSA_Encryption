# Aplikacija za razmenu fajlova
Primenom asimetričnog šifrovanja će se fajlovi slati od klijenta ka serveru i obrnuto.
## Folderi klijenta/servera
* PrimljeniFajlovi - fajlovi koje je poslao klijent/server
* PubK - .key fajl klijenta/servera
* .txt i .png fajlovi su fajlovi za slanje(korisnik može da izabere bilo koji fajl sa svog računara)

## Funkcionisanje programa
**Klijentska strana**

Klijent pokreće Klijent/src/ui/App.java .
Za efikasniji rad programa koristimo *javax.swing.SwingUtilities*,pošto koristimo te komponente
```java
SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new MainFrame();
			}
		});	
```
Nakon pokretanja se dobija ovakav ekran:  
![](https://i.postimg.cc/QxMWFxd1/mainframe.png)  
Tu je prikazan kontroler (*Klijent/src/ui/MainFrame.java*) svih panela:
```java
public class MainFrame extends JFrame {
//        svi paneli koji se koriste u gui-u
    ServerStatusPanel serverPanel;
    GenPanel genPanel;
    MenuPanel menuPanel;
    NotifPanel notifPanel;
```
i najbitniji panel:
```java

    Klijent klijent;
    }
```
Da bi komponente komunicirale međusobno neophodno je da svi imaju isti pokazivač na Klijenta:
```java
//klijent obj je prisutan u svakom panelu
serverPanel.setKlijent(klijent);
genPanel.setKlijent(klijent);
menuPanel.setKlijent(klijent);
notifPanel.setKlijent(klijent);
```
Zaobićićemo deo sa listenerima i ostalim delom(dodavanje na ekran,veličina ekrana..) koda jer nije toliko bitno za sada.

Nakon unosa:
![](https://i.postimg.cc/fTFFFy3v/hp.png)  

poželjno je da se proveri da li je server aktivan(*Klijent/src/ui/ServerStatusPanel.java*):
```java
proveraButton.addActionListener(new ActionListener () {
    @Override
    public void actionPerformed(ActionEvent ae) {
        //dobijanje informacija o hostu i portu(ova linija će se više puta vidjati)
        String host = hostField.getText();
        int port = Integer.parseInt(portField.getText());
        klijent.povezivanjeNaServer(host, port);
        klijent.saljiUTF("Provera");
        klijent.zatvaranje();
    }
}
```
tj.(*Klijent/src/klijent/Klijent.java*)
```java
public void povezivanjeNaServer (String host,int port) throws IOException {
        s = new Socket (host , port);
        
    }
``` 
``` java
public void saljiUTF(String poruka) {
        
        try {
            //getOutS() je s.getOutputStream()
            out2 = new DataOutputStream(getOuS());
            out2.writeUTF(poruka);
        } catch (IOException ex) {
//            Logger.getLogger(ServerStatusPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
```
u notifPanelu korisnik saznaje tu informaciju:  

```java
notifListener.proslediPoruku("Server funkcioniše.");
```
![](https://i.postimg.cc/52VG1G3H/ob.png)  
  
Klikom na:  

![](https://i.postimg.cc/y6rs1VgD/meni.png)  

```java
zahtevButton.addActionListener(new ActionListener () {
    @Override
    public void actionPerformed(ActionEvent ae) {
        String poruka = "";
        String host = getHost();
        int port = getPort();
        try {
            klijent.povezivanjeNaServer(host, port);
            klijent.saljiUTF("Zahtev za meni");
            poruka = klijent.primiUTF();
```

klijent dobija(ukoliko je Server aktivan) meni sa opcijama:  

![](https://i.postimg.cc/fLLpp9vB/meni2.png)  

```java
menuListener.proslediMeni(poruka);
klijent.zatvaranje();
```
Na početku smo rekli da sve komponente imaju istu instancu Klijenta,na sličan način su povezane jedna sa drugom preko listener interface. U ovom slučaju(*Klijent/src/ui/MainFrame.java*):
```java
serverPanel.setMenuListener(new MenuListener () {
    @Override
    public void proslediMeni(String meni) {
        menuPanel.addMenu(meni);
    }

});
```
Zatim se u klasi MenuPanel briše prethodni meni
```java
public void addMenu (String menu) {
    if(mBtns.size()>1) {
        for (int i=0;i<mBtns.size();i++) {
            this.remove(mBtns.get(i));
        }
        mBtns.clear();
    }
```
i dodaje nov.
```java
this.revalidate();
    String [] menu2 = menu.split("MENI:")[1].split("\n");
    for (int i=0;i<menu2.length;i++) {
        JButton btn = new JButton(menu2[i]);
        btn.setBackground(Color.WHITE);
        mBtns.add(btn);
        this.add(mBtns.get(i));
//            this.add(btn);
    }
    this.revalidate();...
```
Ostatak ove metode objašnjavamo posle.

Klijent bira jednu od opcija:  

![](https://i.postimg.cc/fyYGfhCN/prim3.png)  
  
i pojavljuje se obaveštenje:  

![](https://i.postimg.cc/HsgDsVWs/errorpk.png)  
  
Zbog toga postoji promenljiva (*Klijent/src/klijent/Klijent.java*) kojom saznajemo da li je klijent primio javni ključ od servera.
```java
boolean primljenPk = false;
```
```java
public boolean isPrimljenPk() {
        return this.primljenPk;
    }

    public void setPrimljenPk(boolean primljenPk) {
        this.primljenPk = primljenPk;
    }
```
Zbog toga je neophodno da se najpre generiše Public i Private key:
![](https://i.postimg.cc/L6NCKXnW/gen.png)  

```java
genBtn.addActionListener(new ActionListener () {
    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            klijent.generisanjePiJKljuca();
            klijent.setGenerisanPPk(true);
        } catch (NoSuchAlgorithmException ex) {
        }
        notifListener.proslediPoruku("Zavrseno generisanje privatnog i javnog kljuca!");

    }
            
});
```
Metoda generisanjePiJKljuca()
```java
public void generisanjePiJKljuca () throws NoSuchAlgorithmException {
//        System.out.println( "Zapocinjem generisanje privatnog kljuca i javnog kljuca.." );
    keyGen = KeyPairGenerator.getInstance("RSA");
    keyGen.initialize(2048);
    key = keyGen.generateKeyPair();
//        System.out.println( "Zavrseno generisanje privatnog i javnog kljuca!" );  
    Mojkljuc = key.getPublic().getEncoded();
    byte [] Privantikljuc = key.getPrivate().getEncoded();
    
}
```
Zatim sledi razmena:
![](https://i.postimg.cc/hj8F9Bpq/razmena2.png)  

```java
razmenaBtn.addActionListener(new ActionListener () {
    @Override
    public void actionPerformed(ActionEvent ae) {
        
        try {
        //ako je generisan privatan i javni kljuc onda može i da ga razmeni
            if(klijent.isGenerisanPPk() == true) {
                hostPortListener.proslediHP();
                klijent.saljiUTF("Primanje javnog kljuca");
                //Thread je ovde jer server mora imati otvoren getOut()
                Thread.sleep(100);
                klijent.slanjeJKljuca();
                //svaki put se zatvara konekcija
                klijent.zatvaranje();
                Thread.sleep(100);
                //povezivanje
                hostPortListener.proslediHP();
                //klijent prima javni kljuc
                klijent.saljiUTF("Slanje javnog kljuca");
                klijent.primanjeJKljuca();
                klijent.zatvaranje();
                notifListener.proslediPoruku("Klijent je poslao serveru svoj PublicKey.\nKlijent je primio PublicKey od servera.");
                klijent.setPrimljenPk(true);
            }
            if(klijent.isGenerisanPPk() == false) {
                notifListener.proslediPoruku("Ne moze se razmeniti Public Key ukoliko nije generisan");
            }
                    
                    
        }
        catch (...
```
Metoda klijent.slanjeJKljuca()
```java
//metoda za slanje javnog kljuca
    public void slanjeJKljuca () throws IOException {
        dosKljuc = new DataOutputStream(s.getOutputStream());
        dosKljuc.write(Mojkljuc);
        dosKljuc.flush();
//        System.out.println("Klijent je poslao serveru svoj PublicKey.");
    }
```
Metoda klijent.primanjeJKljuca()
```java
//metoda koju korisnik koristi za primanje javnog kljuca
    public void primanjeJKljuca () throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        KljucByte = new byte[2048];
        streamKljuc = s.getInputStream();
        streamKljuc.read(KljucByte);
        //publicKey koji se koristi za enkripciju
        publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(KljucByte));
        
        outKljuc = new FileOutputStream(PATH +"\\PubK\\"+"KljucServer.key");
        outKljuc.write(KljucByte);
//        System.out.println("Klijent je primio PublicKey od servera i sacuvao ga u fajl.");
    
    }
```



Ukoliko korisnik želi da pošalje fajl:
![](https://i.postimg.cc/KcNSjm1P/slanjefajla.png)  

```java
//ako je odabrana opcija iz menija slanje fajla
if(mBtns.get(i).getText().equals("2.Slanje fajla")) {
      JButton btn = mBtns.get(i);
      mBtns.get(i).addActionListener(new ActionListener () {
          @Override
          public void actionPerformed(ActionEvent ae) {
            if(klijent.isPrimljenPk()==true) {
                hostPortListener.proslediHP();
                klijent.saljiUTF("2.Slanje fajla");
                //pitanje postavlja server
                String pitanje = klijent.primiUTF();
                  try {
                      klijent.zatvaranje();
                  } catch (IOException ex) {
                      Logger.getLogger(MenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                  }
                //ako server pita za ime fajla
                if(pitanje.equals("2.Ime fajla?")) {
                    fc.setCurrentDirectory(new File(klijent.getPath()));
                    fc.setDialogTitle("Odaberi fajl");
                    fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                    if(fc.showOpenDialog(btn) == JFileChooser.APPROVE_OPTION) {

                    }
                    //nakon odabranog fajla se klijent opet povezuje i salje imefajla
                    hostPortListener.proslediHP();
                    String putanja = fc.getSelectedFile().getAbsolutePath();
                    klijent.saljiUTF("2.Slanje fajla_imeFajla:"+fc.getSelectedFile().getName());
                    try {
                    //klijent salje fajl serveru(server ceka prijem fajla)
                    Thread.sleep(500);
                    klijent.slanjeFajla(putanja);
                    notifListener.proslediPoruku("Klijent je poslao fajl serveru.");
                    klijent.zatvaranje();
                }   catch (IOExceptio..
```
Metoda klijent.slanjeFajla(putanja)
```java
//metoda koja se koristi kada klijent salje fajl
    public void slanjeFajla (String putanja) throws FileNotFoundException, IOException, InvalidKeyException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException {
        file = new File(putanja);
        //jako mala velicina fajla zbog PK
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
//        System.out.println("Klijent je poslao fajl serveru.");
    }
```
Ukoliko želi da primi fajl:
![](https://i.postimg.cc/fyYGfhCN/prim3.png)  

```java
//ako je odabrana opcija iz menija primanje fajla
if(mBtns.get(i).getText().equals("3.Primanje fajla")) {
  mBtns.get(i).addActionListener(new ActionListener () {
      @Override
      public void actionPerformed(ActionEvent ae) {
          if(klijent.isPrimljenPk()==true) {
                hostPortListener.proslediHP();
                klijent.saljiUTF("3.Primanje fajla");
                //imeFajla (server salje)
                String imeFajla = klijent.primiUTF();
                String imeFajla2=null;
                if(imeFajla.startsWith("3.Primanje fajla_imeFajla:")) {
                    imeFajla2 = imeFajla.split(":")[1];
                    System.out.println(imeFajla2);
//                            Thread.sleep(700);
//                            klijent.primanjeFajla(imeFajla);
                }
                try {
                    klijent.zatvaranje();
                    hostPortListener.proslediHP();
                    klijent.saljiUTF("3.Primanje fajla_SALJI");
                  //klijent čeka fajl od servera
                  klijent.primanjeFajla(imeFajla2);
                  klijent.zatvaranje();
                  notifListener.proslediPoruku("Klijent je primio fajl od servera.");
```
Ukoliko želi novi javni ključ:
![](https://i.postimg.cc/ZnprYJPX/zahtevnovi.png)  

```java
if(mBtns.get(i).getText().equals("1.Zahtev za novim javnim kljucem")) {
  mBtns.get(i).addActionListener(new ActionListener () {
      @Override
      public void actionPerformed(ActionEvent ae) {
          hostPortListener.proslediHP();
          klijent.saljiUTF("1.Zahtev za novim javnim kljucem");
          try {
              klijent.primanjeJKljuca();
              notifListener.proslediPoruku("Klijent je primio novi javni kljuc.");
              klijent.setPrimljenPk(true);
                          } catch (IOException e...
                  });
              }
```
Primanje javnog ključa klijent.primanjeJKljuca() je opisano ranije.  

**Serverska strana**
Server pokreće Server/src/server/TestServer.java .
```java
public static void main(String[] args) {
    //putanja do fajla koji server šalje
    String putanja = null;
    //najbitnija promenljiva u kojoj se nalaze sve metode za komunikaciju sa klijentom
    Server server = new Server();
    //brojac za proveru(generisanje javnog i privatnog ključa servera)
    int brojacZaKljuc = 0;
}
```
U while petlji server se za svaki zahtev povezuje i zatvara
```java
while(true) {
    server.povezivanje();
    System.out.println("--------------------------------------\n");
    if(brojacZaKljuc == 0) {
        brojacZaKljuc++;
        //server najpre mora generisati svoje ključeve pre nego što krene da radi
        server.generisanjePiJKljuca();
    }
    //poruke zahteva
    ...
    //zatvaranje konekcije
     server.zatvaranje();
    System.out.println("server je zatvoren.");
    System.out.println("--------------------------------------\n");
```
metoda server.povezivanje();
```java
public void povezivanje () throws IOException {
        //System.out.println("Server je pokrenut.");
        ss = new ServerSocket(1612);
        s = ss.accept();
        //System.out.println("Klijent je povezan!");
        DataOutputStream out  = new DataOutputStream(s.getOutputStream()); 
        //out se koristi za slanje stringova i ostalih tipova podataka
    }
```
metoda server.generisanjePiJKljuca();
```java
//metoda za generisanje privatnog i javnog kljuca
    public void generisanjePiJKljuca () throws NoSuchAlgorithmException {
        System.out.println( "Zapocinjem generisanje privatnog kljuca i javnog kljuca.." );
        keyGen = KeyPairGenerator.getInstance("RSA"); 
        //veličina ključa
        keyGen.initialize(2048);
        key = keyGen.generateKeyPair();
        System.out.println( "Zavrseno generisanje privatnog i javnog kljuca!" );  
        //public key koji će posle biti poslat klijentu
        MojKljuc = key.getPublic().getEncoded();
    }
```
Kada se klijent poveže ispisuje se poruka i postavlja se in promenljiva koja se koristi za primanje poruka
```java
System.out.println("Klijent je povezan.");
DataInputStream in = new DataInputStream(new BufferedInputStream(server.getInputStream()));
//bilo koji zahtev klijenta je string
String porukaOdKlijenta = in.readUTF();
```
Klijent neće dobiti nikakvu povratnu poruku tokom provere jer zna da je povezan(*java.net.Socket* bi ga obavestio da server ne radi)
```java
if(porukaOdKlijenta.equals("Provera")) {
    System.out.println("Klijent je prisutan.");
}
```
Server odgovara na zahtev za meni
```java
if(porukaOdKlijenta.equals("Zahtev za meni")) {
    server.saljiUTF("MENI:1.Zahtev za novim javnim kljucem\n2.Slanje fajla\n3.Primanje fajla");
    System.out.println("Server je poslao meni klijentu.");
}
```
metoda server.saljiUTF();
```java
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
```
Server prima javni ključ od klijenta
```java
if(porukaOdKlijenta.equals("Primanje javnog kljuca")) {
    server.primanjeJKljuca();
}
```
metoda server.primanjeJKljuca();
```java
//metoda koju server koristi za primanje javnog kljuca
public void primanjeJKljuca () throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
    KljucByte = new byte[2048];
    //promenljiva koja se koristi za primanje svih poruka
    streamKljuc = s.getInputStream();
    PrimljenKljuc = streamKljuc.read(KljucByte);
    publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(KljucByte));
    
    outKljuc = new FileOutputStream(PATH +"PubK\\"+"KljucKlijent.key");
    outKljuc.write(KljucByte);
    System.out.println("Server je primio PublicKey od klijenta i sacuvao ga u fajl.");
}
```
Server šalje javni ključ
```java
if(porukaOdKlijenta.equals("Slanje javnog kljuca")) {
    Thread.sleep(200);
    server.slanjeJKljuca();
}
```
metoda server.slanjeJKljuca();
```java
public void slanjeJKljuca () throws IOException, IOException {
    dosKljuc = new DataOutputStream(s.getOutputStream());
    //pre toga je javni ključ generisan
    dosKljuc.write(MojKljuc);
    dosKljuc.flush();
    System.out.println("Server je poslao klijentu svoj PublicKey.");
}
```
Prva usluga iz menija
```java
if(porukaOdKlijenta.equals("1.Zahtev za novim javnim kljucem")) {
    server.generisanjePiJKljuca();
    //neophodan je Thread jer klijent mora imati postavljen getIn()
    Thread.sleep(150);
    server.slanjeJKljuca();
}
```
Ove metode su već opisane.
Odgovor na zahtev za slanjem fajla od Klijenta
```java
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
```
metoda server.primanjeFajla(imeFajla);
```java
//metoda koja se koristi kada server ocekuje fajl od klijenta
    public void primanjeFajla (String imeFajla) throws FileNotF.. {
        data = new byte[300];
        stream = s.getInputStream();
        baos = new ByteArrayOutputStream();
        //u nizu su pročitani bajtovi sa inputStream()
        baos.write(data, 0 , stream.read(data));		
        result = baos.toByteArray();
        
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        //dekriptovanje
        cipher.init(Cipher.DECRYPT_MODE, key.getPrivate());
        cipherText = cipher.doFinal(result);
        
        FileOutputStream out = new FileOutputStream(PATH+"PrimljeniFajlovi\\"+imeFajla);
        //originalan fajl od klijenta
        out.write(cipherText);
        System.out.println("Server je primio fajl od klijenta i sacuvao je taj fajl.");
        out.close();
    }
```
Odgovor na zahtev za slanje fajla ka Klijentu:
```java
if(porukaOdKlijenta.startsWith("3.Primanje fajla")) {
    if(porukaOdKlijenta.equals("3.Primanje fajla")) {
            //server bira fajl
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
```
Metoda server.slanjeFajla(putanja);
```java
//metoda koja se koristi kada server salje fajl
public void slanjeFajla (String putanja) throws FileNotFoundEx.. {
//        String odabranFajl = this.biranjeSlucajnogFajla();
    //stavljanje u fajlByte 
    file = new File(putanja); 
    fajlByte = new byte[245]; 
    fis = new FileInputStream(file);
    fis.read(fajlByte); 
    fis.close();
        
        try {
            //enkripcija
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
```

[Home](https://bitbucket.org/mruros1/sifrovanje-javnim-kljucem-sk/src/master/#markdown-header-aplikacija-za-razmenu-fajlova)
