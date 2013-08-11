package storyteller.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *
 * @author jakubco
 */
public class DialogComponent {
  public final static Color BACKGROUND_COLOR = new Color(0,0,0,50);
  public final static String FONT_FILE_NAME = "/YEWBN___.ttf";
  private static Font textFont;

  public final static Color UNSELECTED_FOREGROUND_COLOR = Color.BLACK;
  public final static Color SELECTED_FOREGROUND_COLOR = Color.WHITE;

  public static Font getDefaultFont() throws FontFormatException, IOException {
    if (textFont == null) {
      textFont = Font.createFont(
              Font.TRUETYPE_FONT,
              DialogComponent.class.getResourceAsStream(FONT_FILE_NAME)
      ).deriveFont((float)32);
    }
    return textFont;
  }

    public static FontMetrics getDefaultFontMetrics() {
        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        return e.createGraphics(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)).getFontMetrics(textFont);
    }

}
