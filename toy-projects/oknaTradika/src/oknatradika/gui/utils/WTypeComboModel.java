package oknatradika.gui.utils;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import oknatradika.data.WindowType;

public class WTypeComboModel implements ComboBoxModel {
    private WindowType[] wt;
    private Object selectedObject = null;
    
    public WTypeComboModel(WindowType[] wt) { this.wt = wt; }
        
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
    public void addListDataListener(ListDataListener l) {}
    public void removeListDataListener(ListDataListener l) {}
}
