/*
 * WindowsTableModel.java
 *
 * Created on 21.6.2008, 16:28:33
 * hold to: KISS, YAGNI
 *
 */

package oknatradika.gui.utils;

import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;
import oknatradika.Report;

/**
 *
 * @author vbmacher
 */
public class WindowsTableModel extends AbstractTableModel {
    private Report[] windows;

    public WindowsTableModel(Report[] windows) {
        this.windows = windows;
    }
    
    public void updateModel(Report[] windows) {
        this.windows = windows;
        this.fireTableDataChanged();
    }
    
    public int getRowCount() {
        return (windows == null) ? 0 : windows.length ;
    }

    // obrazok, typ okna, podtyp, sirka, zlava, vyska, zaluzie, montaz,
    // vyspravky, sietka
    public int getColumnCount() {
        return 8;
    }
    
    public String getColumnName(int i) {
        switch (i) {
            case 0: return "Obrázok";
            case 1: return "Typ";
            case 2: return "Podtyp";
            case 3: return "Šírka [mm]";
            case 4: return "Výška [mm]";
            case 5: return "Počet kusov";
            case 6: return "Zľava [%]";
            case 7: return "Cena [Sk]";
            default: return "";
        }
    }    

    public Class getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: return ImageIcon.class;
            case 1: return String.class;
            case 2: return String.class;
            case 3: return Integer.class;
            case 4: return Integer.class;
            case 5: return Integer.class;
            case 6: return Integer.class;
            case 7: return String.class;
        }
        return Object.class;
    }
    
    // zlava, zaluzie, montaz,
    // vyspravky, sietka

    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            switch (columnIndex) {
                case 0: return windows[rowIndex].getPodtypOkna().getPicture();
                case 1: return windows[rowIndex].getTypOkna().getName();
                case 2: return windows[rowIndex].getPodtypOkna().getName();
                case 3: return windows[rowIndex].getInput().getSirka();
                case 4: return windows[rowIndex].getInput().getVyska();
                case 5: return windows[rowIndex].getInput().getKusov();
                case 6: return windows[rowIndex].getInput().getZlava();
                case 7: return String.format("%.2f",
                        windows[rowIndex].getCenaSpolu(true,false));
            }
            return null;
        } catch (Exception e) { return null; }
    }

}
