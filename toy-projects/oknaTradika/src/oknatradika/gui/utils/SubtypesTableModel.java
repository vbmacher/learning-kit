/*
 * SubtypesTableModel.java
 *
 * Created on 21.6.2008, 16:28:33
 * hold to: KISS, YAGNI
 *
 */

package oknatradika.gui.utils;

import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;
import oknatradika.data.WindowSubtype;

/**
 *
 * @author vbmacher
 */
public class SubtypesTableModel extends AbstractTableModel {
    private WindowSubtype[] subtypes;

    public SubtypesTableModel(WindowSubtype[] windows) {
        this.subtypes = windows;
    }
    
    public void updateModel(WindowSubtype[] windows) {
        this.subtypes = windows;
        this.fireTableDataChanged();
    }
    
    public int getRowCount() {
        return (subtypes == null) ? 0 : subtypes.length ;
    }

    // obrazok, typ okna, podtyp, sirka, zlava, vyska, zaluzie, montaz,
    // vyspravky, sietka
    public int getColumnCount() {
        return 3;
    }
    
    public String getColumnName(int i) {
        switch (i) {
            case 0: return "Obrázok";
            case 1: return "Názov";
            case 2: return "Počet okien";
            default: return "";
        }
    }    

    public Class getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: return ImageIcon.class;
            case 1: return String.class;
            case 2: return Integer.class;
        }
        return Object.class;
    }
    
    // zlava, zaluzie, montaz,
    // vyspravky, sietka

    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            switch (columnIndex) {
                case 0: return subtypes[rowIndex].getPicture();
                case 1: return subtypes[rowIndex].getName();
                case 2: return WindowSubtype.getWindowsCount(subtypes[rowIndex]
                        .getID());
            }
            return null;
        } catch (Exception e) { return null; }
    }
    
    public int getIdAt(int rowIndex) {
        return (subtypes == null) ? -1 : subtypes[rowIndex].getID();
    }

}
