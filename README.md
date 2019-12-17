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

u notifPanelu korisnik saznaje tu informaciju:
```java
notifListener.proslediPoruku("Server funkcioniše.");
```
![](https://i.postimg.cc/52VG1G3H/ob.png)
[Home](https://bitbucket.org/mruros1/sifrovanje-javnim-kljucem-sk/src/master/#markdown-header-aplikacija-za-razmenu-fajlova)
