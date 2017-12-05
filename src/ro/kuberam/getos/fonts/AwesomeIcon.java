package ro.kuberam.getos.fonts;

import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * A few icons.
 * 
 * <p>
 * Uses Font Awesome by Dave Gandy - http://fontawesome.io.
 * </p>
 */
public enum AwesomeIcon {

    /**
     * FILE_ALT icon.
     */
	FILE_ALT(0xf016),

    /**
     * SAVE icon.
     */
	SAVE(0xf0c7),

    /**
     * POWER_OFF icon.
     */
	POWER_OFF(0xf011);

    private static final String STYLE_CLASS = "icon";
    private static final String FONT_AWESOME = "FontAwesome";
    private int unicode;

    /**
     * Creates a new awesome icon for the given unicode value.
     * 
     * @param unicode the unicode value as an integer
     */
    private AwesomeIcon(final int unicode) {
        this.unicode = unicode;
    }

    /**
     * Returns a new {@link Node} containing the icon.
     * 
     * @return a new node containing the icon
     */
    public Node node() {

        final Text text = new Text(String.valueOf((char) unicode));
        text.getStyleClass().setAll(STYLE_CLASS);
        text.setFont(Font.font(FONT_AWESOME, 22));

        return text;
    }
}