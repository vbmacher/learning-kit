/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oknatradika;

import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import oknatradika.data.DerbyHandler;
import oknatradika.gui.MainWindow;

/**
 *
 * @author vbmacher
 */
public class Main {

    public static void error(String mes) {
        JOptionPane.showMessageDialog(null,"Chyba: " + mes, "Okná Tradika",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void msg(String mes) {
        JOptionPane.showMessageDialog(null, mes, "Okná Tradika",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {}

        DerbyHandler.getInstance();
        new MainWindow().setVisible(true);
    }

}
