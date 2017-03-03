package cz.zoom.whiteboard.decoder;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamMotionDetector;
import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;
import cz.zoom.whiteboard.Code;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import javax.swing.JPanel;

public class CameraCanvas extends JPanel implements WebcamMotionListener {
    private volatile BufferedImage snapshot;
    
    private WebcamMotionDetector detector;
    private WebcamMotionListener externalListener;

    public void motionDetected(WebcamMotionEvent wme) {
        Webcam webcam = ((WebcamMotionDetector) wme.getSource()).getWebcam();
        
        if (webcam.isOpen()) {
            setPreferredSize(webcam.getViewSize());
            setSize(webcam.getViewSize());
            BufferedImage tmpImage = Code.prepareForDecoding(webcam.getImage());
            snapshot = tmpImage;
            repaint();
        }
    }
    
    public void setWebcam(Webcam webcam, WebcamMotionListener listener) {
        if (detector != null) {
            detector.stop();
            detector.removeMotionListener(this);
            if (externalListener != null) {
                detector.removeMotionListener(externalListener);
            }
        }
        
        if (webcam == null) {
            return;
        }
        
        detector = new WebcamMotionDetector(webcam);
        detector.setInterval(100); // one check per 100 ms (10 FPS)
        detector.addMotionListener(this);
        if (listener != null) {
            detector.addMotionListener(listener);
            externalListener = listener;
        }
        detector.start();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        BufferedImage tmpImage = snapshot;

        if (tmpImage != null) {
            g.drawImage(tmpImage, 0, 0, this);
        }
    }
    
    public BufferedImage snapshot() {
        BufferedImage tmpImage = snapshot;

        if (tmpImage != null) {
            ColorModel cm = tmpImage.getColorModel();
            boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
            WritableRaster raster = tmpImage.copyData(null);
            return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
        }
        return null;
    }
}
