package cz.zoom.whiteboard.task;

import com.google.zxing.WriterException;
import cz.zoom.whiteboard.JiraAdapter;
import cz.zoom.whiteboard.QRCode;
import static cz.zoom.whiteboard.QRCode.DEFAULT_QR_HEIGHT;
import static cz.zoom.whiteboard.QRCode.DEFAULT_QR_WIDTH;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import net.jcip.annotations.Immutable;
import net.rcarz.jiraclient.Field;
import net.rcarz.jiraclient.Issue;
import org.yaml.snakeyaml.Yaml;

@Immutable
public class Task {
    private final static Font FONT_ID = new Font("Monospaced", Font.BOLD, 26);
    private final static Font FONT_SUMMARY = new Font("Tahoma", Font.PLAIN, 25);

    private final Map<String, String> fields;
    
    public Task(Map<String, String> task) throws TaskException {
        Map<String, String> tmpMap = new HashMap<String, String>();
        tmpMap.putAll(task);
        fields = Collections.unmodifiableMap(tmpMap);
    }
    
    public Task(Issue issue) throws TaskException {
        Map<String, String> issueData = new HashMap<String, String>();
        
        issueData.put(Field.SUMMARY, issue.getSummary());
        issueData.put(JiraAdapter.ISSUE_KEY, issue.getKey());
        
        fields = Collections.unmodifiableMap(issueData);
    }
    
    public String get(String key) {
        return fields.get(key);
    }
    
    public Set<Entry<String, String>> getFields() {
        return fields.entrySet();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        if (!(other instanceof Task)) {
            return false;
        }
        
        return fields.equals(((Task)other).fields);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + fields.hashCode();
        return hash;
    }
    
    private void drawWrappedString(Graphics g, String s, int x, int y, int width) {
        FontMetrics fm = g.getFontMetrics();

        int lineHeight = fm.getHeight();
        int curX = x;
        int curY = y;

        String[] words = s.split(" ");
        for (String word : words) {
            int wordWidth = fm.stringWidth(word + " ");

            if (curX + wordWidth >= x + width) {
                curY += lineHeight;
                curX = x;
            }
            g.drawString(word, curX, curY);
            curX += wordWidth;
        }
    }
    
    private BufferedImage appendWithText(BufferedImage qrCodeImage, int qrWidth, int qrHeight) {
        BufferedImage dimg = new BufferedImage(2 * qrWidth, qrHeight, qrCodeImage.getType());
        Graphics2D g = dimg.createGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 2 * qrWidth, qrHeight);
        g.drawImage(qrCodeImage, 0, 0, null);

        g.setColor(Color.BLACK);
        g.setFont(FONT_SUMMARY);
        
        int cursorHeight = 45;
        String issueKey = get(JiraAdapter.ISSUE_KEY);
        if (issueKey != null) {
            g.setFont(FONT_ID);
            g.drawString(issueKey, qrWidth + 5, cursorHeight);
            cursorHeight += FONT_ID.getSize() + 10;
        }
        
        String summary = get(Field.SUMMARY);
        if (summary != null) {
            g.setFont(FONT_SUMMARY);
            drawWrappedString(g, summary, qrWidth + 10, cursorHeight, qrWidth - 5);
        }
        g.drawRect(5, 5, 2 * qrWidth - 10, qrHeight - 10);

        g.dispose();
        return dimg;
    }
    
    public BufferedImage render() throws IOException, WriterException {
        BufferedImage qrCodeImage = QRCode.encode(dumpToYaml(fields));
        return appendWithText(qrCodeImage, DEFAULT_QR_WIDTH, DEFAULT_QR_HEIGHT);
    }
    
    private static String dumpToYaml(Object data) {
        Yaml yaml = new Yaml();
        return yaml.dump(data);
    }

}
