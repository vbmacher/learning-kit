package cz.zoom.whiteboard;

import com.google.zxing.WriterException;
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
import net.jcip.annotations.Immutable;
import net.rcarz.jiraclient.Issue;
import org.yaml.snakeyaml.Yaml;

/**
 * A task can contain:
 *  - Parent issue ID (optional)
 *  - Summary (mandatory)
 *  - ID (optional)
 *  - project (mandatory)
 *  - type (optional)
 * 
 * @author jakubco
 */
@Immutable
public class Task {
    private final static Font FONT_ID = new Font("Monospaced", Font.BOLD, 26);
    private final static Font FONT_SUMMARY = new Font("Tahoma", Font.PLAIN, 25);

    private final static String DEFAULT_TASK_TYPE = JiraAdapter.TYPE_TECHNICAL_TASK;    
    private final Map<String, String> taskData;
    
    public Task(Map<String, String> task) throws TaskException {
        Map<String, String> tmpMap = new HashMap<String, String>();
        tmpMap.putAll(task);
        validate(tmpMap);
        taskData = Collections.unmodifiableMap(tmpMap);
    }
    
    public Task(Issue issue) throws TaskException {
        Map<String, String> issueData = new HashMap<String, String>();
        
        issueData.put("description", issue.getDescription());
        issueData.put("summary", issue.getSummary());
        issueData.put("key", issue.getKey());
        issueData.put("type", issue.getIssueType().getName());
        issueData.put("project", issue.getProject().getKey());
        
        validate(issueData);
        taskData = Collections.unmodifiableMap(issueData);
    }
    
    private void validate(Map<String, String> tmpMap) throws TaskException {
        if (!tmpMap.containsKey("type")) {
            tmpMap.put("type", DEFAULT_TASK_TYPE);
        }
        if (!tmpMap.containsKey("project")) {
            throw new TaskException("Task must contain 'project' value!");
        }
    }    
    
    public String getSummary() {
        return taskData.get("summary");
    }
    
    public String getDescription() {
        return taskData.get("description");
    }
    
    public String getProject() {
        return taskData.get("project");
    }
    
    public String getParent() {
        return taskData.get("parent");
    }
    
    public String getType() {
        return taskData.get("type");
    }
        
    public String getKey() {
        return taskData.get("key");
    }
    
    public Map<String, String> getData() {
        return taskData;
    }
    
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        if (!(other instanceof Task)) {
            return false;
        }
        
        return taskData.equals(((Task)other).taskData);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + taskData.hashCode();
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
        if (getKey() != null) {
            g.setFont(FONT_ID);
            g.drawString(getKey(), qrWidth + 5, cursorHeight);
            cursorHeight += FONT_ID.getSize() + 10;
        }
        
        if (getSummary() != null) {
            g.setFont(FONT_SUMMARY);
            drawWrappedString(g, getSummary(), qrWidth + 10, cursorHeight, qrWidth - 5);
        }
        g.drawRect(5, 5, 2 * qrWidth - 10, qrHeight - 10);

        g.dispose();
        return dimg;
    }
    
    public BufferedImage render() throws IOException, WriterException {
        BufferedImage qrCodeImage = QRCode.encode(dumpToYaml(taskData));
        return appendWithText(qrCodeImage, DEFAULT_QR_WIDTH, DEFAULT_QR_HEIGHT);
    }
    
    private static String dumpToYaml(Object data) {
        Yaml yaml = new Yaml();
        return yaml.dump(data);
    }

}
