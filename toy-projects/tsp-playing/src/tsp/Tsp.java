package tsp;

import javax.swing.SwingUtilities;

public class Tsp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Draw().setVisible(true);
            }
        });
    }

}
