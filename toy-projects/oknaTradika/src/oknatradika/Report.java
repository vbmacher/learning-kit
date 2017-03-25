/*
 * Report.java
 *
 * Created on 20.6.2008, 9:59:18
 * hold to: KISS, YAGNI
 *
 */

package oknatradika;

import oknatradika.data.WindowSubtype;
import oknatradika.data.WindowType;

/**
 *
 * @author vbmacher
 */
public class Report {
    public final float DPH = (float) 1.19;
    private float vonkajsie_parapety;
    private float vnutorne_parapety;
    private float zaluzie;
    private float montaz;
    private float vyspravky;
    private float sietky;
    private float okno;
    private float spojovaci_profil;
    private Input vstup;
    
    public Report(float vnutornep, float vonkajsiep, float zaluzie, float montaz,
            float vyspravky, float sietky, float okno, float spojovaci_profil,
            Input vstup) {
        this.vonkajsie_parapety = vonkajsiep;
        this.vnutorne_parapety = vnutornep;
        this.zaluzie = zaluzie;
        this.montaz = montaz;
        this.vyspravky = vyspravky;
        this.okno = okno;
        this.sietky = sietky;
        this.spojovaci_profil = spojovaci_profil;
        this.vstup = vstup;
    }
    
    public Input getInput() { return vstup; }
    
    public float getVnutorneParapety() {
        return vstup.getDphVNU() ? (vnutorne_parapety * DPH) : vnutorne_parapety;
    }
    
    public float getVonkajsieParapety() { 
        return vstup.getDphVON() ? (vonkajsie_parapety * DPH) : vonkajsie_parapety;
    }
    
    public float getZaluzie() {
        return vstup.getDphZAL() ? (zaluzie * DPH) : zaluzie;
    }
    public float getMontaz() {
        return vstup.getDphMON() ? (montaz * DPH) : montaz;
    }
    public float getVyspravky() { 
        return vstup.getDphVYS() ? (vyspravky * DPH) : vyspravky;
    }
    public float getSietky() { 
        return vstup.getDphSIE() ? (sietky * DPH) : sietky;
    }
    public float getOkno() {
        float zl = (float)1.0 - (float)vstup.getZlava()/100;
        return vstup.getDphOKN() ? (okno * zl * DPH) : (okno * zl);
    }
    public float getSpojovaciProfil() {
        return vstup.getDphSPOJ() ? (spojovaci_profil * DPH) :
            spojovaci_profil;
    }
    
    public float getCenaSpolu(boolean dph, boolean pocitat_kusy) { 
        float zl = (float)1.0 - (float)vstup.getZlava()/100;
        float cena = (float) ((!dph) ? 
                    (vnutorne_parapety + vonkajsie_parapety + zaluzie + montaz 
                    + vyspravky + sietky + okno * zl + spojovaci_profil) 
                : (getVnutorneParapety() + getVonkajsieParapety() 
                    + getZaluzie() + getSietky() + getOkno() + getMontaz() 
                    + getVyspravky()) + getSpojovaciProfil());
        return (pocitat_kusy) ? vstup.getKusov() * cena : cena;
    }

    public WindowType getTypOkna() { return vstup.getTyp(); }
    public WindowSubtype getPodtypOkna() { return vstup.getPodtyp(); }
}
