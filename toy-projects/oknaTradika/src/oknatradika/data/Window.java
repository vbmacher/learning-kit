/*
 * Window.java
 *
 * Created on 21.6.2008, 15:40:49
 * hold to: KISS, YAGNI
 *
 */

package oknatradika.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import oknatradika.Main;


/**
 *
 * @author vbmacher
 */
public class Window {
    private int id;
    private int subtype_id;
    private int gx_from;
    private int gx_to;
    private int gy_from;
    private int gy_to;
    private float prize;

    /**
     * Už konšruktor nájde okno podľa rozmerov (v metroch)
     */
    public Window(int subtype_id, int width, int height) {
        this.subtype_id = subtype_id;
        try {
            DerbyHandler db = DerbyHandler.getInstance();
            ResultSet rs = db.query("select * from Windows where subtype_id = ?"
                    + " and gx_from <= ? and gx_to >= ?"
                    + " and gy_from <= ? and gy_to >= ?",
                    subtype_id, width,width,height,height);
            if (rs.next()) {
                this.id = rs.getInt("id");
                this.gx_from = rs.getInt("gx_from");
                this.gx_to = rs.getInt("gx_to");
                this.gy_from = rs.getInt("gy_from");
                this.gy_to = rs.getInt("gy_to");
                this.prize = rs.getFloat("prize");
            } else {
                this.id = -1;
                Main.error("Ľutujem, okno sa nenašlo !");
            }
        } catch (SQLException ex) {
            Main.error("Ľutujem, okno sa nenašlo !");
            id = -1;
        }
    }

    public int getID() { return id; }
   // public int getSubtypeID() { return subtype_id; }
   // public int getGX_from() { return gx_from; }
  //  public int getGX_to() { return gx_to; }
   // public int getGY_from() { return gy_from; }
   // public int getGY_to() { return gy_to; }
    public float getPrize() { return prize; }

    
    public static boolean importWindows(int subtype, File fn)
            throws IOException {
        int count = 0,col,c,row;
        float val;
        FileInputStream ins = new FileInputStream(fn);
        DerbyHandler db = DerbyHandler.getInstance();
        
        int[][] hrx = new int[100][2];
        int[][] hry = new int[100][2];
        float[][] vals = new float[100][100];
        
        for (int i = 0; i < 100; i++)
            for (int j = 0; j < 100; j++)
                vals[i][j] = 0;
        
        
        String ood;
        
        c = ins.read();
        if (c != ';') {
            Main.error("Očakával sa znak ';'\n");
            ins.close();
            return false;
        }

        /* horne hranice - gx */
        col = 0; /* stlpec */
        c = ins.read();
        while ((ins.available() > 0) && (c != '\n')) {
            /* od */
            while (!Character.isDigit(c) && (c != ';') 
                    && (c != '\n')) c = ins.read();
            if (c == ';') { /* dalsi stlpec ? */
                col++;
                c = ins.read();
                continue;
            }
            if (c == '\n') break;
            ood = "";
            while (Character.isDigit(c)) {
                ood += (char)c;
                c = ins.read();
            }
            hrx[col][0] = Integer.parseInt(ood);
            /* do */
            while (!Character.isDigit(c) && (c != ';'))
                c = ins.read(); /* POZOR: ignorujem pomlcku 
                                   na oddelenie hranic !!*/
            if (c == ';') { /* dalsi stlpec ? - chyba do !! */
                Main.error("Ocakavala sa horna hranica !\n");
                ins.close();
                return false;
            }
            ood = "";
            while (Character.isDigit(c)) {
                ood += (char)c;
                c = ins.read();
            }
            hrx[col][1] = Integer.parseInt(ood);
        }
        /* hranica gy a potom hodnoty */
        col = 0; /* stlpec */
        row = 0;
        while (ins.available() > 0 && (c = ins.read()) != '\n') {
            /* od */
            while (!Character.isDigit(c) && (c != ';') && (c != '\n'))
                c = ins.read();
            if (c == ';') { /* dalsi stlpec ? */
                col++;
                continue;
            }
            if (c == '\n') break;
            ood = "";
            while (Character.isDigit(c)) {
                ood += (char)c;
                c = ins.read();
            }
            hry[row][0] = Integer.parseInt(ood);
            /* do */
            while (!Character.isDigit(c) && (c != ';')) 
                c = ins.read(); /* POZOR: ignorujem pomlcku
                                   na oddelenie hranic !!*/
            if (c == ';') { /* dalsi stlpec ? - chyba do !! */
                Main.error("Ocakavala sa horna hranica !\n");
                ins.close();
                return false;
            }
            ood = "";
            while (Character.isDigit(c)) {
                ood += (char)c;
                c = ins.read();
            }
            hry[row][1] = Integer.parseInt(ood);

            /* hodnoty */
            c = ins.read();
            while (ins.available() > 0 && (c != '\n')) {
                while (!Character.isDigit(c)) {
                    if (c == ';') /* dalsi stlpec ? */
                        col++;
                    else if (c == '\n') /* dalsi riadok ? */
                        break;
                    c = ins.read();
                }
                if (c == '\n') {
                    row++;
                    col = 0;
                    break;
                }
                ood = "";
                while (Character.isDigit(c) || (c == '.')) {
                    ood += (char)c;
                    c = ins.read();
                }
                val = Float.parseFloat(ood);
                count++;
                vals[col][row] = val;
            }
        }
        ins.close();
        int ccount = 0;
        for (int i = 0; i < 100; i++)
            for (int j = 0; j < 100; j++) {
                if (vals[i][j] != 0) {
                    try {
                        db.execute("insert into Windows(subtype_id,gx_from,"
                                + "gx_to,gy_from,gy_to,prize) values (?,?,?,?,?,?)",
                                subtype, hrx[i][0], hrx[i][1], hry[j][0],
                                hry[j][1], vals[i][j]);
                        ccount++;
                    } catch (SQLException e) {
                        Main.error(e.getMessage());
                    }
                }
            }
        Main.msg("Bolo importovaných " + ccount + "/" + count + " okien.");
        
        return true;
    }
  
    public static boolean deleteWindows(int subtype) {
        try {
            DerbyHandler db = DerbyHandler.getInstance();
            db.execute("delete from Windows where subtype_id=?",subtype);
            return true;
        } catch (SQLException e) { return false; }
    }

}
