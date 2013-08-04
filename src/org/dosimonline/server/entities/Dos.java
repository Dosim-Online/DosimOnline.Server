package org.dosimonline.server.entities;
import org.dosimonline.server.Entity;

public class Dos extends Entity {
	public int life = 5;
	public int score = 0;
	
	public Dos(float x, float y) {
		super(x, y, 20, 36, "Dos");
	}

	@Override
	public void update(int delta) {
		super.update(delta);
		// Get details from the main entity array.
	}
}
