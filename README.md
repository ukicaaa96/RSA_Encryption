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
klijent.povezivanjeNaServer(host, port);
klijent.saljiUTF("Provera");
klijent.zatvaranje();
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
            //"Zavrseno generisanje privatnog i javnog kljuca!"
            klijent.generisanjePiJKljuca();
            klijent.setGenerisanPPk(true);
        } catch (NoSuchAlgorithmException ex) {
        }
        notifListener.proslediPoruku("Zavrseno generisanje privatnog i javnog kljuca!");

    }
            
});
```

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
            if(klijent.isGenerisanPPk() == true) {
                hostPortListener.proslediHP();
                klijent.saljiUTF("Primanje javnog kljuca");
                Thread.sleep(100);
                klijent.slanjeJKljuca();
                klijent.zatvaranje();
                Thread.sleep(100);
                hostPortListener.proslediHP();
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
Ukoliko korisnik želi da pošalje fajl:
![](https://i.postimg.cc/KcNSjm1P/slanjefajla.png)  
```java
if(mBtns.get(i).getText().equals("2.Slanje fajla")) {
      JButton btn = mBtns.get(i);
      mBtns.get(i).addActionListener(new ActionListener () {
          @Override
          public void actionPerformed(ActionEvent ae) {
            if(klijent.isPrimljenPk()==true) {
                hostPortListener.proslediHP();
                klijent.saljiUTF("2.Slanje fajla");
                String pitanje = klijent.primiUTF();
                  try {
                      klijent.zatvaranje();
                  } catch (IOException ex) {
                      Logger.getLogger(MenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                  }
                if(pitanje.equals("2.Ime fajla?")) {
                    fc.setCurrentDirectory(new File(klijent.getPath()));
                    fc.setDialogTitle("Odaberi fajl");
                    fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                    if(fc.showOpenDialog(btn) == JFileChooser.APPROVE_OPTION) {

                    }
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
Ukoliko želi da primi fajl:
![](https://i.postimg.cc/fyYGfhCN/prim3.png)  
```java
if(mBtns.get(i).getText().equals("3.Primanje fajla")) {
  mBtns.get(i).addActionListener(new ActionListener () {
      @Override
      public void actionPerformed(ActionEvent ae) {
          if(klijent.isPrimljenPk()==true) {
                hostPortListener.proslediHP();
                klijent.saljiUTF("3.Primanje fajla");
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
[Home](https://bitbucket.org/mruros1/sifrovanje-javnim-kljucem-sk/src/master/#markdown-header-aplikacija-za-razmenu-fajlova)
