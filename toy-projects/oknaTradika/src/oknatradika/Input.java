/*
 * Input.java
 *
 * Created on 22.6.2008, 9:30:08
 * hold to: KISS, YAGNI
 *
 */

package oknatradika;

import oknatradika.data.WindowType;
import oknatradika.data.WindowSubtype;

/**
 *
 * @author vbmacher
 */
public class Input {
    private WindowType typ;
    private WindowSubtype podtyp;
    private int sirka;
    private int vyska;
    private int zlava; // zlava na okno
    private float koef; // koeficient ceny okna
    private int kusov;
    private boolean SPOJ;  // spojovaci profil
    private int spojLength; // spojovaci profil dlzka
    private boolean ownLength; // vlastna dlzka profilu?

    private boolean ZAL;
    private boolean MON;
    private boolean VYS;
    private boolean SIE;
    private boolean VNUP;
    private boolean VONP;

    private boolean dphSPOJ;
    private boolean dphVON;
    private boolean dphVNU;
    private boolean dphZAL;
    private boolean dphMON;
    private boolean dphVYS;
    private boolean dphSIE;
    private boolean dphOKN;
    
    public Input(int sirka, int vyska, int zlava, float koef, boolean VON,
            boolean VNU, boolean ZAL, boolean MON, boolean VYS, boolean SIE,
            boolean dphVON, boolean dphVNU, boolean dphZAL, boolean dphMON,
            boolean dphVYS, boolean dphSIE, boolean dphOKN, WindowType typ,
            WindowSubtype podtyp, int kusov, boolean SPOJ, int spojLength,
            boolean dphSPOJ, boolean ownLength) {
        this.typ = typ;
        this.podtyp = podtyp;
        this.koef = koef;
        this.VONP = VON;
        this.VNUP = VNU;
        this.ZAL = ZAL;
        this.MON = MON;
        this.VYS = VYS;
        this.SIE = SIE;
        this.dphVON = dphVON;
        this.dphVNU = dphVNU;
        this.dphMON = dphMON;
        this.dphOKN = dphOKN;
        this.dphSIE = dphSIE;
        this.dphVYS = dphVYS;
        this.dphZAL = dphZAL;
        this.sirka = sirka;
        this.vyska = vyska;
        this.zlava = zlava;
        this.kusov = kusov;
        this.SPOJ = SPOJ;
        this.spojLength = spojLength;
        this.dphSPOJ = dphSPOJ;
        this.ownLength = ownLength;
    }
    
    public int getSirka() { return sirka; }
    public int getVyska() { return vyska; }
    public int getZlava() { return zlava; }
    public int getKusov() { return kusov; }
    public int getSpojLength() { return spojLength; }
    public boolean getOwnLength() { return ownLength; }

    public boolean getSPOJ() { return SPOJ; }
    public boolean getVON() { return VONP; }
    public boolean getVNU() { return VNUP; }
    public boolean getZAL() { return ZAL; }
    public boolean getMON() { return MON; }
    public boolean getVYS() { return VYS; }
    public boolean getSIE() { return SIE; }
    
    public boolean getDphVON() { return dphVON; }
    public boolean getDphVNU() { return dphVNU; }
    public boolean getDphZAL() { return dphZAL; }
    public boolean getDphMON() { return dphMON; }
    public boolean getDphVYS() { return dphVYS; }
    public boolean getDphSIE() { return dphSIE; }
    public boolean getDphOKN() { return dphOKN; }
    public boolean getDphSPOJ() { return dphSPOJ; }
    
    public WindowType getTyp() { return typ; }
    public WindowSubtype getPodtyp() { return podtyp; }
    
    public float getKoeficient() { return koef; }

}
