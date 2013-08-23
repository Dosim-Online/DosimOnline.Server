package org.dosimonline.server.entities;
import java.util.Random;
import org.dosimonline.server.DOServer;
import org.dosimonline.server.Entity;
import org.dosimonline.server.Vector2f;

public class Nazi extends Entity {
	static final float GRAVITY = 1000;
	static final float INITIAL_SPEED = 66.66f;
	static final float CLIMB_SPEED = -500;
	static final int MAX_VERTICAL_SPEED = 1000;
	float velocityY;
	Random random = new Random();
	Dos victim = new Dos(x, y);
	int lifeAddChance = random.nextInt(30);
	Container container;

	public Nazi(int x, int y) {
		super(x, y, 20, 35, "Nazi");
	}

	@Override
	public void update(int delta) {
		super.update(delta);
		container = new Container(id, x, y);
		
		if (victim == null && areThereAnyDoses())
			findDosToChase();

		if (!areThereAnyDoses())
			this.destroy();

		float nextX = x;
		if (victim.x < x)
			nextX -= INITIAL_SPEED;
		else if (victim.x > x)
			nextX += INITIAL_SPEED;
		if (collide(nextX, y, "Solid") == null)
			x = nextX;

		if (collide(x, y, "Ladder") == null) {
			// Going up
			if (velocityY < 0)
				if (collide(x, y - velocityY, "Solid") != null)
					velocityY = 0;
		} else if (victim.y > y)
			velocityY = -CLIMB_SPEED;
		else if (victim.y < y)
			velocityY = +CLIMB_SPEED;
		y += velocityY;

		if (isAirborne()) {
			if (velocityY < MAX_VERTICAL_SPEED)
				velocityY += 10;
		} else {
			if (velocityY > 0) {
				y = (int) y;
				while (collide(x, y, "Solid") != null)
					y -= 1;
			}
			velocityY = 0;
		}

		// Scoring.
		Dos someDos = (Dos) collide(x, y, "Dos");
		if (someDos != null) {
			someDos.life--;
			destroy();
		}

		StarOfDavid someStartOfDavid = (StarOfDavid) collide(x, y, "Star of David");
		if (someStartOfDavid != null) {
			someStartOfDavid.getShootingDos().score += 1;
			if (lifeAddChance == 0)
				someStartOfDavid.getShootingDos().life++;
			destroy();
		}
	}

	void findDosToChase() {
		Dos closestDos = null;
		float closestDosSquaredDis = 0;

		for (Entity entity : DOServer.entities)
			if (entity instanceof Dos) {
				float distanceSquared = new Vector2f(x, y)
					.distanceSquared(new Vector2f(entity.x, entity.y));
				if (distanceSquared > closestDosSquaredDis) {
					closestDosSquaredDis = distanceSquared;
					closestDos = (Dos) entity;
				}
			}

		this.victim = closestDos;
	}

	boolean isAirborne() {
		return collide(x, y, "Solid") == null
			&& collide(x, y, "Solid") == null;
	}

	boolean areThereAnyDoses() {
		for (Entity e : DOServer.entities)
			if (e instanceof Dos)
				return true;
		return false;
	}

	public class Container {
		String type = "Nazi";
		int id;
		float x, y;

		public Container(int id, float x, float y) {
			this.x = x;
			this.y = y;
			this.id = id;
		}
	}
}