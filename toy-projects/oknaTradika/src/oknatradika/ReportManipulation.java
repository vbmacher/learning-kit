/*
 * ReportManipulation.java
 *
 * Created on 21.6.2008, 8:32:11
 * hold to: KISS, YAGNI
 *
 */

package oknatradika;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.imageio.ImageIO;
import javax.swing.JEditorPane;
import javax.swing.text.html.HTMLEditorKit;

/**
 *
 * @author vbmacher
 */
public class ReportManipulation {
    public static boolean shouldStop = false;
    static final byte[] HEX_CHAR_TABLE = {
        (byte)'0', (byte)'1', (byte)'2', (byte)'3',
        (byte)'4', (byte)'5', (byte)'6', (byte)'7',
        (byte)'8', (byte)'9', (byte)'a', (byte)'b',
        (byte)'c', (byte)'d', (byte)'e', (byte)'f'
    };
    
    public static boolean saveFile(File f, Report[] res, JEditorPane txt) {
        try {
            String fn = f.getAbsolutePath();
            FileOutputStream vystup = new FileOutputStream(fn);
            shouldStop = false;
            if (fn.endsWith(".rtf")) {
                return saveRTF(fn, res, vystup);
            } else if (fn.endsWith(".html")) {
                HTMLEditorKit rkit = (HTMLEditorKit)txt
                        .getEditorKitForContentType("text/html");
                rkit.write(vystup, txt.getDocument(), 0, txt.getText().length());
            } else if (fn.endsWith(".txt")) {
                return saveText(fn, res, vystup);
            }
            vystup.close();
            return true;
        } catch (Exception e) {
            Main.error("Nepodarilo sa uložiť súbor: " + f.getPath()
                    + "\n\n" + e.getMessage());
        }
        return false;
    }

    public static String getHexString(byte[] raw, int len)
            throws UnsupportedEncodingException {
        int index = 0,i;
        byte[] hex = new byte[2*raw.length];
        for (i = 0; i < len; i++) {
            byte b = raw[i];
            int v = b & 0xFF;
            hex[index++] = HEX_CHAR_TABLE[v >>> 4];
            hex[index++] = HEX_CHAR_TABLE[v & 0xF];
        }
        return new String(hex, "ASCII");
    }

    /**
     * Super-implementation of fast RTF output with tables and pictures support
     */
    public static boolean saveRTF(String fn, Report[] res, FileOutputStream vystup)
            throws IOException {
        int j = 0;
        float cenaCelkom = Calculation.getCenaCelkom(res);
        String s = "{\\rtf1\\ansi{\\fonttbl\\f0\\froman\\fprq2\\fcharset0\n"
                + " DejaVu Sans;}\\fs28\\b Plastov\\'e9 okn\\'e1\\par\\par\n"

                + "\\plain\\trowd\\irow0\\irowband0\\trgaph108\\cellx1900\\cellx9000\n"
                + "\\intbl {Po\\u269\\'3fet okien:}\\cell\n"
                + "\\pard {" + Calculation.getPocetKusov(res) + "}\\cell\\row\n"
                + "\\trowd\\irow1\\irowband1\\trgaph108\\cellx1900\\cellx9000\n"
                + "\\intbl {Cena celkom:}\\cell\n"
                + "\\pard {\\b " + String.format("%.2f EUR (%.2f Sk)",cenaCelkom/30.126,
                    cenaCelkom) + "}\\cell\\row\\pard\\par\n";

        for (int i = 0; i < res.length; i++) {
            if (shouldStop) return false;
            s += "\\b Okno \\u269\\'3f." + (i+1) + "\\plain\\par"
                    + "\\trowd\\irow" + i +"\\irowband" + i;
            if (i == (res.length - 1))
                s += "\\lastrow";
                    
            s += "\\ts11\\trgaph108\\cellx4000\\cellx9637\\pard\\plain"
                    + "\\intbl {";
                    
            if (res[i].getPodtypOkna().getPicture() != null) {
                
                s += "{\\pict\\jpegblip";
                
                File tmp = new File("tmpp.picture");
                Image img = res[i].getPodtypOkna().getPicture().getImage();
                int w = res[i].getPodtypOkna().getPicture().getIconWidth();
                int h = res[i].getPodtypOkna().getPicture().getIconHeight();
                double div = (double)((double)w / 3500.0);
                
                s += "\\picw" + w + "\\pich" + h + "\\picwgoal3500"
                        +"\\pichgoal" + (int)((double)h / div) + "\n";
                
                RenderedImage ren = null;
                if (img instanceof RenderedImage)
                    ren = (RenderedImage)img;
                else {
                    BufferedImage buf = new BufferedImage(w,h,
                            BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = buf.createGraphics();
                    g.drawImage(img, 0, 0, null);
                    g.dispose();
                    ren = buf;
                }
                ImageIO.write(ren, "JPEG", tmp);
                FileInputStream ins = new FileInputStream(tmp);
      
                // strasne dlho to trva
                int len;//,l,k;
                while (ins.available() > 0) {
                    if (shouldStop) {
                        ins.close();
                        return false;
                    }
                    byte[] bytes = new byte[ins.available()];
                    len = ins.read(bytes);
                    s += getHexString(bytes,len);
                }
                ins.close();
                tmp.delete();
                s += "}";
            }        
            s += "}\\cell\\pard\\intbl {"
                    + "\\intbl {\\plain \\'8a\\'edrka okna:}"
                    + String.format("%d", 
                    res[i].getInput().getSirka()) + " mm; "
                    + "v\\'fd\\'9aka okna:"
                    + String.format("%d", res[i].getInput()
                    .getVyska()) + " mm\\par "
                    + "Po\\u269\\'3fet kusov: "
                    + res[i].getInput().getKusov() + " \\plain\\par "
            
                    + "\\intbl\\itap2 {{\\b\\fs24 Polo\\'9eka}\\nestcell"
                    + "{\\nonesttables\\par}"
                    + "{\\b\\fs24 Cena}\\nestcell"
                    + "{\\nonesttables\\par}}\\pard"

                    // zaciatok vnorenej tabulky
                    + "\\intbl\\itap2 {\\*\\nesttableprops\\trowd\\irow0\\irowband0"
                    + "\\ts11\\trgaph70\\cellx2500\\cellx6000\\nestrow}"
                    + "\\pard\\intbl\\itap2 {\\plain Cena okna";
            
            j = 1; // cislo riadka vnutornej tabulky
            if (res[i].getInput().getZlava() != 0)
                s += " (vr\\'e1tane z\\u318\\'3favy)";
            s += ":\\nestcell{\\nonesttables\\par}"
                    + String.format("%.2f EUR (%.2f Sk)", res[i].getOkno()/30.126,
                    res[i].getOkno())
                    + "\\nestcell{\\nonesttables\\par}}"
                    + "\\pard\\intbl\\itap2{\\*\\nesttableprops\\trowd\\irow" + j 
                    + "\\irowband" + (j++) + "\\trgaph0\\cellx2500\\cellx6000"
                    +"\\nestrow}";
                    
            if (res[i].getInput().getVON())
                s += "\\pard\\intbl\\itap2 {\\plain Vonkaj\\'9aia parapeta:"
                        + "\\nestcell{\\nonesttables\\par}"
                        + String.format("%.2f EUR (%.2f Sk)", res[i].getVonkajsieParapety()/30.126, res[i].getVonkajsieParapety())
                        + "\\nestcell{\\nonesttables\\par}}"
                        + "\\pard\\intbl\\itap2{\\*\\nesttableprops\\trowd\\irow" + j
                        + "\\irowband" + (j++) + "\\ts11\\trgaph0\\cellx2500"
                        + "\\cellx6000\\nestrow}";
            
            if (res[i].getInput().getVNU())
                s += "\\pard\\intbl\\itap2 {Vn\\'fatorn\\'e1 parapeta:"
                        + "\\nestcell{\\nonesttables\\par}"
                        + String.format("%.2f EUR (%.2f Sk)", res[i].getVnutorneParapety()/30.126,res[i].getVnutorneParapety())
                        + "\\nestcell{\\nonesttables\\par}}"
                        + "\\pard\\intbl\\itap2{\\*\\nesttableprops\\trowd\\irow" + j
                        + "\\irowband" + (j++) + "\\ts11\\trgaph0\\cellx2500"
                        + "\\cellx6000\\nestrow}";
            
            if (res[i].getInput().getZAL())
                s += "\\pard\\intbl\\itap2 {\\'8eal\\'fazie:\\nestcell"
                        + "{\\nonesttables\\par}"
                        + String.format("%.2f EUR (%.2f Sk)", res[i].getZaluzie()/30.126, res[i].getZaluzie())
                        + "\\nestcell{\\nonesttables\\par}}\\pard\\intbl"
                        + "\\itap2{\\*\\nesttableprops\\trowd\\irow" + j
                        + "\\irowband" + (j++) + "\\ts11\\trgaph0\\cellx2500"
                        + "\\cellx6000\\nestrow}";

            if (res[i].getInput().getMON())
                s += "\\pard\\intbl\\itap2 {Mont\\'e1\\'9e:\\nestcell"
                        + "{\\nonesttables\\par}"
                        + String.format("%.2f EUR (%.2f Sk)", res[i].getMontaz()/30.126,res[i].getMontaz())
                        + "\\nestcell{\\nonesttables\\par}}\\pard\\intbl"
                        + "\\itap2{\\*\\nesttableprops\\trowd\\irow" + j
                        + "\\irowband" + (j++) + "\\ts11\\trgaph0\\cellx2500"
                        + "\\cellx6000\\nestrow}";

            if (res[i].getInput().getVYS())
                s += "\\pard\\intbl\\itap2 {Vyspr\\'e1vky:\\nestcell"
                        + "{\\nonesttables\\par}"
                        + String.format("%.2f EUR (%.2f Sk)", res[i].getVyspravky()/30.126,res[i].getVyspravky())
                        + "\\nestcell{\\nonesttables\\par}}\\pard\\intbl"
                        + "\\itap2{\\*\\nesttableprops\\trowd\\irow" + j
                        + "\\irowband" + (j++) + "\\ts11\\trgaph0\\cellx2500"
                        + "\\cellx6000\\nestrow}";
            
            if (res[i].getInput().getSIE()) 
                s += "\\pard\\intbl\\itap2 {Sie\\u357\\'3fky proti hmyzu:\\nestcell"
                        + "{\\nonesttables\\par}"
                        + String.format("%.2f EUR (%.2f Sk)", res[i].getSietky()/30.126,res[i].getSietky())
                        + "\\nestcell{\\nonesttables\\par}}\\pard\\intbl"
                        + "\\itap2{\\*\\nesttableprops\\trowd\\irow" + j
                        + "\\irowband" + (j++) + "\\ts11\\trgaph0\\cellx2500"
                        + "\\cellx6000\\nestrow}";

            if (res[i].getInput().getSPOJ()) 
                s += "\\pard\\intbl\\itap2 {Spojovac\\'ed profil:\\nestcell"
                        + "{\\nonesttables\\par}"
                        + String.format("%.2f EUR (%.2f Sk)", res[i].getSpojovaciProfil()/30.126,res[i].getSpojovaciProfil())
                        + "\\nestcell{\\nonesttables\\par}}\\pard\\intbl"
                        + "\\itap2{\\*\\nesttableprops\\trowd\\irow" + j
                        + "\\irowband" + (j++) + "\\ts11\\trgaph0\\cellx2500"
                        + "\\cellx6000\\nestrow}";

            j = 0;
            s += "\\par\\pard\\intbl\\itap2 {Cena spolu (bez DPH):\\nestcell"
                    + "{\\nonesttables\\par}\\par "
                    + String.format("%.2f EUR (%.2f Sk)",res[i].getCenaSpolu(false,false)/30.126,res[i].getCenaSpolu(false,false))
                    + "\\nestcell{\\nonesttables\\par}}\\pard\\intbl"
                    + "\\itap2{\\*\\nesttableprops\\trowd\\irow" + j
                    + "\\irowband" + (j++) + "\\ts11\\trgaph0\\cellx2500"
                    + "\\cellx6000\\nestrow}\\pard\\intbl\\itap2 {"
                    + "Cena spolu (s DPH):\\nestcell{\\nonesttables\\par}}"
                    + "{\\b " + String.format("%.2f EUR (%.2f Sk)",res[i].getCenaSpolu(true,false)/30.126,res[i].getCenaSpolu(true,false))
                    + "}\\plain\\nestcell{\\nonesttables\\par}\\pard\\intbl"
                    + "\\itap2{\\*\\nesttableprops\\trowd\\irow" + j
                    + "\\irowband" + (j++) + "\\lastrow\\ts11\\trgaph0"
                    + "\\cellx2500\\cellx6000\\nestrow}";
            s += "}\\cell\\row\\pard\\par";
        }
        s += "}";
        vystup.write(s.getBytes());
        return true;
    }

    public static boolean saveText(String fn, Report[] res, FileOutputStream vystup) 
            throws IOException {
        String s = "Plastové okná:\n"
                + "  Počet okien: " + Calculation.getPocetKusov(res) + "\n"
                + "  Cena celkom: " + Calculation.getCenaCelkom(res)/30.126 + " EUR"
                + ", " + Calculation.getCenaCelkom(res) + " Sk";
        
        for (int i = 0; i < res.length; i++) {
            if (shouldStop) return false;
            s += "\n  Okno č." + (i+1)
                    + "\n    Šírka okna:" + String.format("%d", res[i].getInput()
                    .getSirka()) + " mm"
                    + "\n    Výška okna:" + String.format("%d", res[i].getInput()
                    .getVyska()) + " mm"
                    + "\n    Počet kusov:" + res[i].getInput().getKusov()
                    + "\n    Cena okna";
            if (res[i].getInput().getZlava() != 0)
                s += " (vrátane zľavy)";
            s += ": " + String.format("%.2f EUR, %.2f Sk", res[i].getOkno()/30.126,res[i].getOkno());
            if (res[i].getInput().getVON())
                s += "\n    Vonkajšia parapeta: " + String.format("%.2f EUR, %.2f Sk\n",
                        res[i].getVonkajsieParapety()/30.126,res[i].getVonkajsieParapety());
            if (res[i].getInput().getVNU())
                s += "    Vnútorná parapeta: "
                        + String.format("%.2f EUR, %.2f Sk\n",
                        res[i].getVnutorneParapety()/30.126,res[i].getVnutorneParapety());
            if (res[i].getInput().getZAL())
                s += "    Žalúzie: "
                        + String.format("%.2f EUR, %.2f Sk\n", res[i].getZaluzie()/30.126,
                        res[i].getZaluzie());
            if (res[i].getInput().getZAL())
                s += "    Montáž: "
                        + String.format("%.2f EUR, %.2f Sk\n", res[i].getMontaz()/30.126,
                        res[i].getMontaz());
            if (res[i].getInput().getVYS())
                s += "    Vysprávky: "
                        + String.format("%.2f EUR, %.2f Sk\n", res[i].getVyspravky()/30.126,
                        res[i].getVyspravky());
            if (res[i].getInput().getSIE())
                s += "    Sieťky proti hmyzu: "
                        + String.format("%.2f EUR, %.2f Sk\n", res[i].getSietky()/30.126,
                        res[i].getSietky());
            if (res[i].getInput().getSPOJ())
                s += "    Sieťky proti hmyzu: "
                        + String.format("%.2f EUR, %.2f Sk\n", res[i].getSietky()/30.126,
                        res[i].getSietky());
             s += "\n    Cena spolu (bez DPH): "
                     + String.format("%.2f EUR, %.2f Sk\n",res[i].getCenaSpolu(false,false)/30.126,
                     res[i].getCenaSpolu(false,false))
                     + "    Cena spolu (s DPH): "
                     + String.format("%.2f EUR, %.2f Sk\n",res[i].getCenaSpolu(true,false)/30.126,
                     res[i].getCenaSpolu(true,false));
        }
        vystup.write(s.getBytes());
        return true;
    }

    public static String generateHtml(Report[] r) {
        String s = "<h2>Plastové okná</h2><br/>";
        
        s += "<table><tr><td>Počet okien:</td><td>" 
                + Calculation.getPocetKusov(r) + "</td></tr>"
                + "<tr><td>Cena celkom:</td><td><strong>" + 
                String.format("%.2f EUR, %.2f Sk", Calculation.getCenaCelkom(r)/30.126,
                Calculation.getCenaCelkom(r)) + "</strong></td></tr></table>";
        
        s += "<table>";
        for (int i = 0; i < r.length; i++) {
            if (shouldStop) return s;
            s += "<tr><th style=\"textalign:center;\">Okno č." + (i+1) + "</th></tr>"
                + "<tr><td></td><td>Šírka okna: "+ String.format("%d",
                r[i].getInput().getSirka()) + " mm; Výška okna: " 
                + String.format("%d", r[i].getInput().getVyska()) + " mm<br/>"
                + "Počet kusov: " + r[i].getInput().getKusov() + "<br/>"
                + "<table><tr><th>Položka</th><th>Cena (EUR)</th><th>Cena (Sk)</th>"
                
                + "<tr><td>Cena okna";
            if (r[i].getInput().getZlava() != 0)
                s += " (vrátane zľavy)";
            s += ": </td><td>" + String.format("%.2f", r[i].getOkno()/30.126)
                + "</td><td>" + String.format("%.2f", r[i].getOkno()) + "</td></tr>";
            
            if (r[i].getInput().getVON())
                s += "<tr><td>Vonkajšia parapeta:</td><td>"
                        + String.format("%.2f", r[i].getVonkajsieParapety()/30.126)
                        + "</td><td>" + String.format("%.2f", r[i].getVonkajsieParapety())
                        + "</td></tr>";
            if (r[i].getInput().getVNU())
                s += "<tr><td>Vnútorná parapeta:</td><td>"
                        + String.format("%.2f", r[i].getVnutorneParapety()/30.126)
                        + "</td><td>"+ String.format("%.2f", r[i].getVnutorneParapety())
                        +"</td></tr>";
            if (r[i].getInput().getZAL())
                    s += "<tr><td>Žalúzie:</td><td>" 
                            + String.format("%.2f", r[i].getZaluzie()/30.126)
                            + "</td><td>" +String.format("%.2f", r[i].getZaluzie())
                            + "</td></tr>";
            if (r[i].getInput().getMON())
                s += "<tr><td>Montáž:</td><td>" 
                        + String.format("%.2f", r[i].getMontaz()/30.126)
                        + "</td><td>"+String.format("%.2f", r[i].getMontaz())
                        +"</td></tr>";
            if (r[i].getInput().getVYS())
                s += "<tr><td>Vysprávky:</td><td>"
                        + String.format("%.2f", r[i].getVyspravky()/30.126)
                        + "</td><td>"+String.format("%.2f", r[i].getVyspravky())
                        +"</td></tr>";
            if (r[i].getInput().getSIE()) 
                s += "<tr><td>Sieťky proti hmyzu:</td>" 
                        + String.format("%.2f", r[i].getSietky()/30.126)
                        + "</td><td>"+String.format("%.2f", r[i].getSietky())
                        +"</td></tr>";
            if (r[i].getInput().getSPOJ()) 
                s += "<tr><td>Spojovací profil:</td>" 
                        + String.format("%.2f", r[i].getSpojovaciProfil()/30.126)
                        + "</td><td>"+String.format("%.2f", r[i].getSpojovaciProfil())
                        +"</td></tr>";

            s += "</table><br/><table><tr><td>Cena okna spolu (bez DPH):</td><td>"
                    + String.format("%.2f EUR</td><td>%.2f Sk</td></tr>",
                        r[i].getCenaSpolu(false,false)/30.126,r[i].getCenaSpolu(false,false))
                    + "<tr><td>Cena okna spolu (s DPH):</td><td>"
                    + String.format("%.2f EUR</td><td>%.2f Sk</td></tr>",
                    r[i].getCenaSpolu(true,false)/30.126,r[i].getCenaSpolu(true,false))
                    + "</table></td></tr>";
        }
        s += "</table>";
        return s;
    }
}
