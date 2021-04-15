package de.oaknetwork.oaknetlink.api.gui.components;

/**
 * This class is used to Color GuiComponents.
 * 
 * It also provides some default Colors.
 * 
 * @author Fabian Fila
 */
public class Color {

	public static Color RED = new Color(255, 0, 0, 255);
	public static Color DARKRED = new Color(128, 0, 0, 255);
	public static Color GREEN = new Color(0, 255, 0, 255);
	public static Color DARKGREEN = new Color(0, 128, 0, 255);
	public static Color AQUA = new Color(0, 200, 255, 255);
	public static Color BLUE = new Color(0, 0, 255, 255);
	public static Color DARKBLUE = new Color(0, 0, 128, 255);
	public static Color YELLOW = new Color(255, 255, 0, 255);
	public static Color PINK = new Color(255, 0, 255, 255);
	public static Color DARKGRAY = new Color(40, 40, 40, 255);
	public static Color GRAY = new Color(128, 128, 128, 255);
	public static Color LIGHTGRAY = new Color(200, 200, 200, 255);
	public static Color BLACK = new Color(0, 0, 0, 255);
	public static Color WHITE = new Color(255, 255, 255, 255);
	public static Color PURPLE = new Color(149, 0, 255, 255);
	public static Color BROWN = new Color(115, 54, 28, 255);

	public int R;
	public int G;
	public int B;
	public int Alpha;

	/**
	 * Creates a new Color Object with the given R, G, B and Alpha
	 * 
	 * 0-255 per channel
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @param alpha
	 */
	public Color(int r, int g, int b, int alpha) {
		R = r;
		G = g;
		B = b;
		Alpha = alpha;
	}

	/**
	 * Changes the Red channel of the Color
	 * 
	 * 0-255
	 * 
	 * @param value the red value which should be used
	 * @return the color which has been changed
	 */
	public Color setR(int value) {
		R = value;
		return this;
	}

	/**
	 * Changes the Green channel of the Color
	 * 
	 * 0-255
	 * 
	 * @param value the red value which should be used
	 * @return the color which has been changed
	 */
	public Color setG(int value) {
		G = value;
		return this;
	}

	/**
	 * Changes the Blue channel of the Color
	 * 
	 * 0-255
	 * 
	 * @param value the red value which should be used
	 * @return the color which has been changed
	 */
	public Color setB(int value) {
		B = value;
		return this;
	}

	/**
	 * Changes the Red channel of the Color
	 * 
	 * 0-255
	 * 
	 * @param value the red value which should be used
	 * @return the color which has been changed
	 */
	public Color setAlpha(int value) {
		Alpha = value;
		return this;
	}

	/**
	 * returns a copy of the given Color Object
	 * 
	 * @return copy of the Color
	 */
	public Color copy() {
		return new Color(R, G, B, Alpha);
	}
}
