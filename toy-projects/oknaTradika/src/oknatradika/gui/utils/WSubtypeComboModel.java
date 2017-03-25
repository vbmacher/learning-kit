/*
 * WSubtypeComboModel.java
 *
 * Created on 20.6.2008, 12:50:58
 * hold to: KISS, YAGNI
 *
 */

package oknatradika.gui.utils;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import oknatradika.data.WindowSubtype;

/**
 *
 * @author vbmacher
 */
public class WSubtypeComboModel implements ComboBoxModel {
    private WindowSubtype[] wt;
    private Object selectedObject = null;
    
    public WSubtypeComboModel(WindowSubtype[] wt) { this.wt = wt; }
    public void setSelectedItem(Object anItem) { selectedObject = anItem; }
    public Object getSelectedItem() { return selectedObject; }
    public int getSize() { return (wt == null) ? 0 : wt.length; }
    public Object getElementAt(int index) { return wt[index].getName(); }
    public int getIdAt(int index) { 
        return ((wt == null) || (index == -1)) ? -1 : wt[index].getID(); 
    }
    public void selectAtId(int id) { 
        if (wt == null) return;
        for (int i = 0; i < wt.length; i++)
            if (wt[i].getID() == id) {
                selectedObject = wt[i].getName();
                break;
            }
    }
    public WindowSubtype getAt(int index) { return wt[index]; }
        
    public void addListDataListener(ListDataListener l) {}
    public void removeListDataListener(ListDataListener l) {}

}
