package org.dosimonline.server.entities;
import org.dosimonline.server.DOServer;
import org.dosimonline.server.Entity;
import org.dosimonline.server.Vector2f;

public class Meatball extends Entity {
	float targetX;
	float targetY;
	Vector2f direction;
	float speed = 800f;
	int shouldDie = 6666;
	Container container;

	public Meatball(float x, float y, float targetX, float targetY) {
		super(x, y, 32, 32, "Meatball");
		this.targetX = targetX;
		this.targetY = targetY;

		direction = new Vector2f(targetX - x, targetY - y);
		direction.normalise();
	}

	@Override
	public void update(int delta) {
		super.update(delta);
		container = new Container(id, x, y);
		DOServer.server.sendToAllUDP(container);

		if (shouldDie > 0)
			shouldDie--;
		else
			destroy();
		x += direction.x * speed;
		y += direction.y * speed;

		Dos someDos = (Dos) collide(x, y, "Dos");
		if (someDos != null) {
			someDos.life--;
			destroy();
		}
	}

	public class Container {
		String type = "Meatball";
		int id;
		float x, y;

		public Container(int id, float x, float y) {
			this.x = x;
			this.y = y;
			this.id = id;
		}
	}
}