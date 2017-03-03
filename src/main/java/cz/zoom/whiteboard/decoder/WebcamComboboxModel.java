package cz.zoom.whiteboard.decoder;

import com.github.sarxos.webcam.Webcam;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

public class WebcamComboboxModel implements ComboBoxModel<String> {
    
    private final List<Webcam> devices;
    private int selectedIndex = -1;
    
    public WebcamComboboxModel() {
        devices = Webcam.getWebcams();
    }

    private int findWebcam(String name) {
        for (Webcam camera : devices) {
            if (camera.getName().equals(name)) {
                return devices.indexOf(camera);
            }
        }
        return -1;
    }
    
    public void setSelectedItem(Object o) {
        selectedIndex = findWebcam((String)o);
    }

    public Object getSelectedItem() {
        if (selectedIndex != -1) {
            return devices.get(selectedIndex);
        }
        return null;
    }

    public int getSize() {
        return devices.size();
    }

    public String getElementAt(int i) {
        return devices.get(i).getName();
    }

    public void addListDataListener(ListDataListener ll) {
    }

    public void removeListDataListener(ListDataListener ll) {
    }
    
}
