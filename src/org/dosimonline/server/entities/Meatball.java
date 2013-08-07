package org.dosimonline.server.entities;
import org.dosimonline.server.Entity;
import org.dosimonline.server.Vector2f;

public class Meatball extends Entity {
	float targetX;
	float targetY;
	Vector2f direction;
	float speed = 800f;
	int shouldDie = 6666;
	
	public Meatball(float x, float y, float targetX, float targetY) {
		super(x, y, 32, 32, "Pasta Attack");
		this.targetX = targetX;
		this.targetY = targetY;
		
		direction = new Vector2f(targetX - x, targetY - y);
		direction.normalise();
	}

	@Override
	public void update() {
		super.update();
		x += direction.x * speed;
		y += direction.y * speed;

		Dos someDos = (Dos) collide(x, y, "Dos");
		if (someDos != null) {
			someDos.life--;
			destroy();
		}
	}
}