/*
 * WindowSubtype.java
 *
 * Created on 20.6.2008, 12:46:46
 * hold to: KISS, YAGNI
 *
 */

package oknatradika.data;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author vbmacher
 */
public class WindowSubtype {
    private int id;
    private int type_id;
    private String name;
    private ImageIcon picture;

    public WindowSubtype(int id) {
        this.id = id;
        try {
            DerbyHandler db = DerbyHandler.getInstance();
            ResultSet rs = db.query("select * from Subtypes where id = ?", id);
            if (rs.next()) {
                this.type_id = rs.getInt("type_id");
                this.name = rs.getString("name");
                Blob blob = rs.getBlob("picture");
                this.picture =  new ImageIcon(blob.getBytes(1, 
                        (int)blob.length()));
            }
        } catch (Exception ex) {}
    }
    
    public static WindowSubtype[] getAllSubtypes(int type_id) {
        try {
            DerbyHandler db = DerbyHandler.getInstance();
            ResultSet rs = db.query("select * from Subtypes where type_id = ?",
                    type_id);
            ArrayList<WindowSubtype> wt = new ArrayList<WindowSubtype>();
            while (rs.next()) {
                int iid = rs.getInt("id");
                wt.add(new WindowSubtype(iid));
            }
            return wt.toArray(new WindowSubtype[0]);
        } catch (SQLException ex) {
            return null;
        }
    }

    public static int getWindowsCount(int subtype_id) {
        int i = 0;
        try {
            DerbyHandler db = DerbyHandler.getInstance();
            ResultSet rs = db.query("select id from Windows where subtype_id = ?",
                    subtype_id);
            while (rs.next()) i++;
        } catch (SQLException ex) {}
        return i;       
    }
    
    public int getID() { return id; }
    public int getTypeID() { return type_id; }
    public String getName() { return name; }
    public ImageIcon getPicture() { return picture; }    

    
    public static boolean addSubtype(int type_id, String name, ImageIcon picture) {
        try {
            DerbyHandler db = DerbyHandler.getInstance();
            if (picture != null) {
                File tmp = new File("tmp.picture");
                Image img = picture.getImage();
                RenderedImage ren = null;
                if (img instanceof RenderedImage)
                    ren = (RenderedImage)img;
                else {
                    BufferedImage buf = new BufferedImage(picture.getIconWidth(),
                            picture.getIconHeight(), BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = buf.createGraphics();
                    g.drawImage(img, 0, 0, null);
                    g.dispose();
                    ren = buf;
                }
                ImageIO.write(ren, "JPEG", tmp);
                FileInputStream i = new FileInputStream(tmp);
                db.execute("insert into Subtypes(type_id,name,picture) "
                    + "values (?,?,?)",type_id, name, i);
                i.close();
                tmp.delete();
            } else {
                db.execute("insert into Subtypes(type_id,name) "
                    + "values (?,?)",type_id, name);
            }
            return true;
        } catch (Exception e) { 
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static int editSubtype(int id, String name, ImageIcon picture) {
        try {
            DerbyHandler db = DerbyHandler.getInstance();
            String s = "update Subtypes set name = ? ";
            if (picture != null) {
                File tmp = new File("tmp.picture");
                Image img = picture.getImage();
                RenderedImage ren = null;
                if (img instanceof RenderedImage)
                    ren = (RenderedImage)img;
                else {
                    BufferedImage buf = new BufferedImage(picture.getIconWidth(),
                            picture.getIconHeight(), BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = buf.createGraphics();
                    g.drawImage(img, 0, 0, null);
                    g.dispose();
                    ren = buf;
                }
                ImageIO.write(ren, "JPEG", tmp);
                FileInputStream i = new FileInputStream(tmp);
                db.update(s + ", picture = ? where id=?",name,i,id);
                i.close();
                tmp.delete();
            } else
                db.update(s + " where id=?",name,id);
            return 1;
        } catch (Exception e) { return 0; }
    }
    
    public static boolean deleteSubtype(int id) {
        try {
            DerbyHandler db = DerbyHandler.getInstance();
            db.execute("delete from Subtypes where id=?",id);
            return true;
        } catch (SQLException e) { return false; }
    }
    
}
