package cz.zoom.whiteboard;

import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.WriterException;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
    
    public BufferedImage render() throws IOException, WriterException {
        return QRCode.encode(BOUNDARY_BEGIN + yamlText);
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
    
    private Rectangle rectangleFromPoints(Point[] points) {
        int x = points[0].x;
        int y = points[0].y;
        
        int width = points[1].x - points[0].x;
        int height = points[2].y - points[0].y;
        
        return new Rectangle(x, y, width, height);
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
    
    public Point[] locate(BufferedImage capturedImage) throws NotFoundException {
        Result[] result = QRCode.decode(capturedImage);
        
        if (result == null) {
            return null;
        }
        List<Point> boundary = new ArrayList<Point>();
        String boundaryString = BOUNDARY_BEGIN + yamlText;
        for (Result res : result) {
            if (boundaryString.equals(res.getText())) {
                boundary.add(getAveragePoint(res.getResultPoints()));
            }
        }
        return boundary.toArray(new Point[0]);
    }
    
    private BufferedImage crop(BufferedImage capturedImage) throws NotFoundException {
        Point[] boundary = locate(capturedImage);
        
        if (boundary == null) {
            return capturedImage;
        }
        Rectangle rect = rectangleFromPoints(boundary);
        BufferedImage croppedImage = new BufferedImage(
                rect.width,
                rect.height,
                capturedImage.getType());
        
        Graphics2D g = croppedImage.createGraphics();
        g.drawImage(capturedImage, rect.x, rect.y, rect.width, rect.height, null);
        g.dispose();
        return croppedImage;
    }
    
    public String[] decodeIssues(BufferedImage capturedImage) throws NotFoundException {
        BufferedImage boundary = crop(capturedImage);
        
        Result[] results = QRCode.decode(boundary);
        List<String> yamlResult = new ArrayList<String>();
        
        for (Result result : results) {
            yamlResult.add(result.getText());
        }
        return yamlResult.toArray(new String[0]);
    }
    
}
