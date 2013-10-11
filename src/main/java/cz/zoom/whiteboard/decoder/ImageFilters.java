package cz.zoom.whiteboard.decoder;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.RescaleOp;

public class ImageFilters {

    public static BufferedImage setBrightness(BufferedImage source, float brightness) {
        RescaleOp op = new RescaleOp(brightness, 0, null);
        return op.filter(source, null);
    }

    public static BufferedImage setContrast(BufferedImage source, float contrast) {
        RescaleOp op = new RescaleOp(1.0f, contrast, null);
        return op.filter(source, null);
    }
    
    public static BufferedImage setBrightnessAndContrast(BufferedImage source, float brightness, float contrast) {
        RescaleOp op = new RescaleOp(brightness, contrast, null);
        return op.filter(source, null);
    }
    
    public static BufferedImage sharpen(BufferedImage source) {
//        if (source == null) {
//            return null;
//        }
//        Kernel kernel = new Kernel(3, 3, new float[]{
//             0.0f, -1.0f,  0.0f,
//            -1.0f,  5.0f, -1.0f,
//             0.0f, -1.0f,  0.0f});
//
//        ConvolveOp op = new ConvolveOp(kernel);
//        return op.filter(source, null);
        return source;
    }
    
    
}
