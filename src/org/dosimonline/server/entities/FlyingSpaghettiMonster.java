package org.dosimonline.server.entities;
import java.util.Random;
import org.dosimonline.server.DosimOnlineServer;
import org.dosimonline.server.Entity;
import org.dosimonline.server.Vector2f;

public class FlyingSpaghettiMonster extends Entity {
	final int MAX_SPEED = 50;
	final int CHANGE_DIRECTION_TIME = 7000; // 7sec
	final int ATTACK_DELAY = 7000;
	int speed = 50;
	int changeDirectionTimeout = 0;
	int attackTimeout = 2000;
	int life = 3;
	Vector2f direction = new Vector2f();
	Random random = new Random();

	public FlyingSpaghettiMonster(float x, float y) {
		super(x, y, 112, 97, "Pasta");
	}

	void shootOnDos() {
		for (Entity e : DosimOnlineServer.entities) {
			if (e instanceof Dos) {
				if (attackTimeout <= 0 && e.x > x - 500
					  && e.x < x + 500 && e.y > y - 500
					  && e.y < y + 500) {

					DosimOnlineServer.entities.add(new Meatball(x, y, e.x, e.y));
					attackTimeout = ATTACK_DELAY;
					return;
				}
			}
		}
	}
	
	boolean areThereAnyDoses() {
		for (Entity e : DosimOnlineServer.entities)
			if (e instanceof Dos)
				return true;
		return false;
	}
	
	void newVelocity() {
		speed = randomBetween(random, MAX_SPEED / 2, MAX_SPEED);

		// randomize one direction out of eight
		do {
			direction.x = randomBetween(random, -1, 1);
			direction.y = randomBetween(random, -1, 1);
		} while (direction.x == 0 && direction.y == 0); // We want to move!

		direction.normalise();
	}
	
	int randomBetween(Random random, int min, int max) {
		if (min <= max)
			return random.nextInt(max + 1 - min) + min;
		return random.nextInt(min - max + 1) + max;
	}

	@Override
	public void update() {
		super.update();
		if (!areThereAnyDoses())
			destroy();
		
		changeDirectionTimeout--;
		attackTimeout--;

		shootOnDos();

		if (changeDirectionTimeout <= 0) {
			newVelocity();
			changeDirectionTimeout = CHANGE_DIRECTION_TIME;
		}

		x += direction.x * speed;
		y += direction.y * speed;

		Dos someDos = (Dos) collide(x, y, "Dos");
		if (someDos != null)
			someDos.life = 0; // No one can touch the FSM and stay alive!

		Entity starOfDavidColl = collide(x, y, "Semitic Attack");
		if (starOfDavidColl != null) // Star of David hit the FSM
		{
			starOfDavidColl.destroy();
			life--;

			newVelocity(); // change direction and speed

			if (life == 0) {
				((StarOfDavid) starOfDavidColl).getShootingDos().score += 10;
				this.destroy();
			}
		}

		if (x < DosimOnlineServer.LEFT_BORDER
			  || x + width > DosimOnlineServer.RIGHT_BORDER)
			direction.x *= -1;

		if (y < DosimOnlineServer.TOP_BORDER
			  || y + height > DosimOnlineServer.BOTTOM_BORDER)
			direction.y *= -1;
	}
}