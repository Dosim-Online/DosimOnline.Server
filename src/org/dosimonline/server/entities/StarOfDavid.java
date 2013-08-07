package org.dosimonline.server.entities;
import org.dosimonline.server.Entity;
import org.dosimonline.server.Vector2f;

public class StarOfDavid extends Entity {
	Dos shootingDos;
	Vector2f direction;
	int shouldDie = 3500; // 3.5 seconds
	int speed = 1200; // px/s
	
	public StarOfDavid(int x, int y, float targetX, float targetY, Dos shootingDos) {
		super(x, y, 32, 32, "Semitic Attack");
		this.shootingDos = shootingDos;
		
		if (x == targetX && y == targetY) // Mine (doesn't move)
			direction = new Vector2f();
		else
			direction = new Vector2f(targetX - x, targetY - y);
		direction.normalise();
	}
	
	public Dos getShootingDos() {
		return shootingDos;
	}

	@Override
	public void update() {
		super.update();
		x += direction.x * speed;
		y += direction.x * speed;

		if (collide(x, y, "Solid") != null) {
			this.destroy();
		}

		if (shouldDie > 0)
			shouldDie--;
		else
			destroy();
	}
}
