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
import java.util.List;
import net.jcip.annotations.Immutable;

@Immutable
public class Boundary {
    public static final String BOUNDARY_BEGIN = "*BOUNDARY*";
    private final String jiraStatus;
    
    public Boundary(String jiraStatus) {
        this.jiraStatus = jiraStatus;
    }
    
    public BufferedImage[] render() throws IOException, WriterException {
        List<BufferedImage> boundImages = new ArrayList<BufferedImage>();
        
        for (int i = 0; i < 4; i++) {
            boundImages.add(QRCode.encode(BOUNDARY_BEGIN + jiraStatus + "*" + i));
        }
        return boundImages.toArray(new BufferedImage[0]);
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
    
    public Point[] locate(BufferedImage capturedImage) throws NotFoundException {
        Result[] result = QRCode.decode(capturedImage);
        
        if (result == null) {
            return null;
        }
        Point[] boundary = new Point[4];
        for (int i = 0; i < 4; i++) {
            String boundaryString = BOUNDARY_BEGIN + jiraStatus + "*" + i;
            for (Result res : result) {
                if (boundaryString.equals(res.getText())) {
                    boundary[i] = getAveragePoint(res.getResultPoints());
                    break;
                }
            }
            if (boundary[i] == null) {
                return null;
            }
        }
        return boundary;
    }
    
    public BufferedImage crop(BufferedImage capturedImage) throws NotFoundException {
        Point[] boundary = locate(capturedImage);
        
        if (boundary == null) {
            return null;
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
    
}
