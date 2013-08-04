package org.dosimonline.server;

public class Vector2f {
	public float x;
	public float y;

	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2f() {
	} // We don't need no parameters!

	public float distanceSquared(Vector2f vector) {
		float dx = vector.x - x;
		float dy = vector.y - y;

		return (float) (dx * dx) + (dy * dy);
	}

	public Vector2f normalise() {
		x /= length();
		y /= length();
		return this;
	}

	float length() {
		double lengthSquared = Math.pow(x, 2) + Math.pow(y, 2);
		return (float) Math.sqrt(lengthSquared);
	}
}
