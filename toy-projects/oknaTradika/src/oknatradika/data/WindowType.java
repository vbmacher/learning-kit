/*
 * WindowType.java
 *
 * Created on 20.6.2008, 12:29:00
 * hold to: KISS, YAGNI
 *
 */

package oknatradika.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 *
 * @author vbmacher
 */
public class WindowType {
    private int id;
    private String name;

    public WindowType(int id) {
        this.id = id;
        try {
            DerbyHandler db = DerbyHandler.getInstance();
            ResultSet rs = db.query("select * from Types where id = ?", id);
            if (rs.next()) {
                this.name = rs.getString("name");
            }
        } catch (SQLException ex) {}
    }
    
    public static WindowType[] getAllTypes() {
        try {
            DerbyHandler db = DerbyHandler.getInstance();
            ResultSet rs = db.query("select * from Types");
            ArrayList<WindowType> wt = new ArrayList<WindowType>();
            while (rs.next()) {
                int iid = rs.getInt("id");
                wt.add(new WindowType(iid));
            }
            return wt.toArray(new WindowType[0]);
        } catch (SQLException ex) {
            return null;
        }
    }

    public int getID() { return id; }
    public String getName() { return name; }
    
    public static boolean addType(String name) {
        try {
            DerbyHandler db = DerbyHandler.getInstance();
            db.execute("insert into Types(name) values ?",name);
            return true;
        } catch (SQLException e) { 
            return false;
        }
    }

    public static int editType(int id, String name) {
        try {
            DerbyHandler db = DerbyHandler.getInstance();
            db.update("update Types set name = ? where id=?",name,id);
            return 1;
        } catch (SQLException e) { return 0; }
    }
    
    public static boolean deleteType(int id) {
        try {
            DerbyHandler db = DerbyHandler.getInstance();
            db.execute("delete from Types where id=?",id);
            return true;
        } catch (SQLException e) { return false; }
    }
    

}
