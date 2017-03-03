package cz.zoom.whiteboard;

import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.WriterException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.jcip.annotations.Immutable;

@Immutable
public class Group {
    private static final String GROUP_BEGIN = "*B*";
    private final String name;

    public Group(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public BufferedImage render(String text) throws IOException, WriterException {
        return render(text, Code.DEFAULT_QR_WIDTH, Code.DEFAULT_QR_HEIGHT);
    }

    public BufferedImage render(String text, int width, int height) throws IOException, WriterException {
        BufferedImage image = Code.encodeCode128(GROUP_BEGIN + name, width, height);

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

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Group) {
            Group other = (Group)o;
            return other.name.equals(name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    public static Group findByText(BufferedImage capturedImage, String text) throws NotFoundException {
        Result[] result = Code.decode(capturedImage);

        if (result == null) {
            return null;
        }
        for (Result res : result) {
            if (res.getText().equals(GROUP_BEGIN + text)) {
                return new Group(text);
            }
        }
        return null;
    }


    public static Set<Group> findAll(BufferedImage capturedImage) throws NotFoundException {
        Result[] result = Code.decode(capturedImage);

        if (result == null) {
            return null;
        }

        Set<Group> groups = new HashSet<Group>();
        Set<String> texts = new HashSet<String>();

        for (Result res : result) {
            if (res.getText().startsWith(GROUP_BEGIN)) {
                String yaml = res.getText().substring(GROUP_BEGIN.length());
                if (!texts.contains(yaml)) {
                    groups.add(new Group(yaml));
                } else {
                    texts.add(yaml);
                }
            }
        }
        return groups;
    }

    public Rectangle locate(BufferedImage capturedImage) throws NotFoundException {
        Result[] result = Code.decode(capturedImage);

        if (result == null) {
            return null;
        }
        String groupString = GROUP_BEGIN + name;

        int minX=Integer.MAX_VALUE, minY=Integer.MAX_VALUE, maxX=0, maxY=0;

        for (Result res : result) {
            if (groupString.equals(res.getText())) {
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

    public String[] decode(BufferedImage capturedImage) throws NotFoundException {
        BufferedImage boundary = crop(capturedImage);

        Result[] results = Code.decode(boundary);
        List<String> contentResult = new ArrayList<String>();

        for (Result result : results) {
            String text = result.getText();

            if (!text.startsWith(GROUP_BEGIN)) {
                contentResult.add(result.getText());
            }
        }
        return contentResult.toArray(new String[0]);
    }

    @Override
    public String toString() {
        return name;
    }
}
