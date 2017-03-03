/*
 * Calculation.java
 *
 * Created on 20.6.2008, 9:39:50
 * hold to: KISS, YAGNI
 *
 */

package oknatradika;

import oknatradika.data.Window;

/**
 *
 * @author vbmacher
 */
public class Calculation {
    
    public static Report calculate(Input vstup) {
        float w = (float)vstup.getSirka() / (float)1000.0;
        float h = (float)vstup.getVyska() / (float)1000.0;
        float sl = (float)vstup.getSpojLength() / (float)1000.0;
        
        float vonp = 0;
        float vnup = 0;
        float zal = 0;
        float mon = 0;
        float vys = 0;
        float siet = 0;
        float spoj = 0;

        if (vstup.getVON()) vonp = (float) ((w + (float)0.1) * (float)290.0);
        if (vstup.getVNU()) vnup = (float) ((w + (float)0.1) * (float)390.0);
        if (vstup.getZAL()) zal = (float)((w - (float)0.1) * (h - (float)0.1) * (float)800.0);
        if (vstup.getMON()) mon = (float) ((float)2 * (w + h) * (float)150);
        if (vstup.getVYS()) vys = (float) ((float)2 * (w + h) * (float)150);
        if (vstup.getSIE()) siet = (float) ((w  - (float)0.1) * (h - 0.1) * (float)600.0);
        if (vstup.getSPOJ()) spoj = (float)(sl * (float)700);
        
        Window win = new Window(vstup.getPodtyp().getID(), vstup.getSirka(),
                vstup.getVyska());
        if (win.getID() == -1)
            return new Report(vnup, vonp, zal, mon, vys, siet, 0, spoj, vstup);
        else {
            return new Report(vnup, vonp, zal, mon, vys, siet, 
                    win.getPrize() * vstup.getKoeficient(), spoj, vstup);
        }
    }
    
    public static float getCenaCelkom(Report[] r) {
        float suma = 0;
        for (int i = 0; i < r.length; i++)
            suma += r[i].getCenaSpolu(true,true);
        return suma;
    }
    
    public static int getPocetKusov(Report[] r) {
        if (r == null) return 0;
        int k = 0;
        for (int i = 0; i < r.length; i++)
            k += r[i].getInput().getKusov();
        return k;
    }
    
}
