package org.dosimonline.server;
import org.dosimonline.server.entities.Nazi;
import java.util.ArrayList;
import java.util.Random;

public class DosimOnlineServer {
	public static ArrayList<Entity> entities = new ArrayList<>();
	public static Nazi nazi = new Nazi(0, 0);
	
	public static final int TOP_BORDER = 0;
	public static final int LEFT_BORDER = 0;
	public static final int BOTTOM_BORDER = 5000;
	public static final int RIGHT_BORDER = 15000;
	
	static Random random = new Random();
	static long lastUpdate = getTime();
	static long delta;

	public static void main(String[] args) {
		System.out.println("You're running an offline Dosim Online server :D");
		entities.add(nazi);
		for (Entity e : entities)
			System.out.println("Entity " + e.id + ": " + e.type
				  + ", x = " + (int) e.x + ", y = " + (int) e.y);

		while (true) {
			delta = getTime() - lastUpdate;
			for (Entity e : entities)
				e.update((int) delta);

			lastUpdate = getTime();
		}
	}

	public static void remove(Entity toRemove) {
		for (Entity e : entities)
			if (e.equals(toRemove))
				entities.remove(e);
		System.out.println(entities.toString());
	}

	static long getTime() {
		return System.nanoTime() / 1000000;
	}
}
