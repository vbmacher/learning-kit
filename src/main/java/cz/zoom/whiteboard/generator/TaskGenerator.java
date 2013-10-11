package cz.zoom.whiteboard.generator;

import cz.zoom.whiteboard.QRCode;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import javax.imageio.ImageIO;

public class TaskGenerator {

    private final static int QR_WIDTH = 250;
    private final static int QR_HEIGHT = 250;
    
    private final static Font idFont = new Font("Monospaced", Font.BOLD, 26);
    private final static Font summaryFont = new Font("Tahoma", Font.PLAIN, 25);

    public void drawWrappedString(Graphics g, String s, int x, int y, int width) {
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

    public BufferedImage appendWithText(Task task, BufferedImage qrCodeImage) {
        BufferedImage dimg = new BufferedImage(2 * QR_WIDTH, QR_HEIGHT, qrCodeImage.getType());
        Graphics2D g = dimg.createGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 2 * QR_WIDTH, QR_HEIGHT);
        g.drawImage(qrCodeImage, 0, 0, null);

        g.setColor(Color.BLACK);
        g.setFont(summaryFont);
        
        int cursorHeight = 45;
        if (task.getId() != null) {
            g.setFont(idFont);
            g.drawString(task.getId(), QR_WIDTH + 5, cursorHeight);
            cursorHeight += idFont.getSize() + 10;
        }
        
        if (task.getSummary() != null) {
            g.setFont(summaryFont);
            drawWrappedString(g, task.getSummary(), QR_WIDTH + 10, cursorHeight, QR_WIDTH - 5);
        }
        g.drawRect(5, 5, 2 * QR_WIDTH - 10, QR_HEIGHT - 10);

        g.dispose();
        return dimg;
    }

    public void generateTasks(String yamlFile) throws FileNotFoundException {
        TaskParser parser = new TaskParser();

        String fileNameBase = "task";
        int i = 0;
        for (Task task : parser.parseYaml(yamlFile)) {
            try {
                QRCode qrCode = new QRCode();
                BufferedImage qrCodeImage = qrCode.encode(parser.dump(task.getData()), QR_WIDTH, QR_HEIGHT);

                qrCodeImage = appendWithText(task, qrCodeImage);
                File taskFile = new File(String.format("%s-%03d.png", fileNameBase, i++));

                ImageIO.write(qrCodeImage, "PNG", taskFile);
            } catch (Exception e) {
                System.err.println("Could not encode QR Code: " + e.getMessage());
            }

        }
    }

}
