package cz.zoom.whiteboard.decoder;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamMotionDetector;
import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import javax.swing.JPanel;

public class CameraCanvas extends JPanel implements WebcamMotionListener {
    private volatile BufferedImage snapshot;
    
    private volatile float brightness = 1.0f;
    private volatile float contrast = 1.0f;
    
    private WebcamMotionDetector detector;
    private WebcamMotionListener externalListener;

    public void motionDetected(WebcamMotionEvent wme) {
        Webcam webcam = ((WebcamMotionDetector) wme.getSource()).getWebcam();
        
        if (webcam.isOpen()) {
            setPreferredSize(webcam.getViewSize());
            setSize(webcam.getViewSize());
            BufferedImage tmpImage = applyFilters(webcam.getImage());
            snapshot = tmpImage;
            repaint();
        }
    }
    
    private BufferedImage applyFilters(BufferedImage image) {
        return ImageFilters.sharpen(ImageFilters.setBrightnessAndContrast(image, brightness, contrast));
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
    
    public void setBrightness(float brightness) {
        this.brightness = brightness; 
    }
    
    public void setContrast(float contrast) {
        this.contrast = contrast; 
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
