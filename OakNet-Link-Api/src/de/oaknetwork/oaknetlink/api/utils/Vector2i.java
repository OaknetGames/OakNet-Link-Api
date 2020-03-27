package de.oaknetwork.oaknetlink.api.utils;

/**
 * This represents a two dimensional Vector and provides some basic calculations
 * It's mainly used in the GUI
 * 
 * @author Fabian Fila
 */
public class Vector2i {

	public int X;
	public int Y;

	public Vector2i(int x, int y) {
		this.X = x;
		this.Y = y;
	}

	public Vector2i add(Vector2i vector) {
		X = X + vector.X;
		Y = Y + vector.Y;
		return this;
	}

	public Vector2i negate() {
		X = -X;
		Y = -Y;
		return this;
	}

	public Vector2i copy() {
		return new Vector2i(X, Y);
	}

	public Vector2i multiply(float multiplicator) {
		X = (int) (X * multiplicator);
		Y = (int) (Y * multiplicator);
		return this;
	}

	public Vector2i divide(int divisor) {
		X = X / divisor;
		Y = Y / divisor;
		return this;
	}
}
