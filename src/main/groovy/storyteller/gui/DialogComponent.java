package storyteller.gui;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *
 * @author jakubco
 */
public class DialogComponent {
  public final static Color BACKGROUND_COLOR = new Color(0,0,0,0.5);
  public final static String FONT_FILE_NAME = "/YEWBN___.ttf";
  private static Font textFont;

  public final static Color UNSELECTED_FOREGROUND_COLOR = Color.BLACK;
  public final static Color SELECTED_FOREGROUND_COLOR = Color.WHITE;

  public static Font getDefaultFont() {
    if (textFont == null) {
      textFont = Font.loadFont(DialogComponent.class.getResourceAsStream(FONT_FILE_NAME), 32);
    }
    return textFont;
  }
}
