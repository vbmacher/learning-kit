package cz.zoom.whiteboard;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import static cz.zoom.whiteboard.QRCode.prepareForDecoding;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.jcip.annotations.Immutable;

@Immutable
public class Boundary {
    private static final String BOUNDARY_BEGIN = "*B*";
    private final String yamlText;
    
    public Boundary(String yamlText) {
        this.yamlText = yamlText;
    }
    
    public BufferedImage render(String text) throws IOException, WriterException {
        BufferedImage image = QRCode.encode(BOUNDARY_BEGIN + yamlText);
        
        Graphics2D gr = image.createGraphics();
        
        gr.setColor(Color.BLACK);
        
        gr.drawString(text, 15, 15);
        gr.dispose();
        return image;
    }
    
    private Point getAveragePoint(ResultPoint[] points) {
        int x = 0;
        int y = 0;
        for (ResultPoint point : points) {
            x += point.getX();
            y += point.getY();
        }
        x /= points.length;
        y /= points.length;
        return new Point(x,y);
    }
    
    public static Boundary findByText(BufferedImage capturedImage, String text) throws NotFoundException {
        Result[] result = QRCode.decode(capturedImage);
        
        if (result == null) {
            return null;
        }
        for (Result res : result) {
            if (res.getText().equals(BOUNDARY_BEGIN + text)) {
                return new Boundary(text);
            }
        }
        return null;
    }

    
    public static List<Boundary> findAll(BufferedImage capturedImage) throws NotFoundException {
        Result[] result = QRCode.decode(capturedImage);
        
        if (result == null) {
            return null;
        }

        List<Boundary> boundary = new ArrayList<Boundary>();
        Set<String> texts = new HashSet<String>();

        for (Result res : result) {
            if (res.getText().startsWith(BOUNDARY_BEGIN)) {
                String yaml = res.getText().substring(BOUNDARY_BEGIN.length());
                if (!texts.contains(yaml)) {
                    boundary.add(new Boundary(yaml));
                } else {
                    texts.add(yaml);
                }
            }
        }
        return boundary;
    }
    
    public Rectangle locate(BufferedImage capturedImage) throws NotFoundException {
        Result[] result = QRCode.decode(capturedImage);
        
        if (result == null) {
            return null;
        }
        String boundaryString = BOUNDARY_BEGIN + yamlText;
        
        int minX=Integer.MAX_VALUE, minY=Integer.MAX_VALUE, maxX=0, maxY=0;
        
        for (Result res : result) {
            if (boundaryString.equals(res.getText())) {
                Point point = getAveragePoint(res.getResultPoints());
                
                minX = Math.min(minX, point.x);
                minY = Math.min(minY, point.y);
                maxX = Math.max(maxX, point.x);
                maxY = Math.max(maxY, point.y);
            }
        }
        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }
    
    private BufferedImage crop(BufferedImage capturedImage) throws NotFoundException {
        Rectangle boundary = locate(capturedImage);
        
        if (boundary == null) {
            return capturedImage;
        }
        
        BufferedImage croppedImage = new BufferedImage(
                boundary.width,
                boundary.height,
                capturedImage.getType());
        
        Graphics2D g = croppedImage.createGraphics();
        g.drawImage(capturedImage,
                boundary.x,
                boundary.y,
                boundary.width,
                boundary.height,
                null);
        g.dispose();
        return croppedImage;
    }
    
    public String[] decodeIssues(BufferedImage capturedImage) throws NotFoundException {
        BufferedImage boundary = crop(capturedImage);
        
        Result[] results = QRCode.decode(boundary);
        List<String> yamlResult = new ArrayList<String>();
        
        for (Result result : results) {
            String text = result.getText();

            if (!text.startsWith(BOUNDARY_BEGIN)) {
                yamlResult.add(result.getText());
            }
        }
        return yamlResult.toArray(new String[0]);
    }

}
