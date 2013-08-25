package org.dosimonline.server;
import com.esotericsoftware.kryonet.Server;
import java.io.IOException;
import org.dosimonline.server.entities.Nazi;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import org.dosimonline.server.entities.Dos;
import org.dosimonline.server.entities.FlyingSpaghettiMonster;
import org.dosimonline.server.tiles.Solid;

public class DOServer {
	//World stuff
	static final int SOLID_HEIGHT = 128, SOLID_WIDTH = 128;
	static final int NUM_OF_BUILDINGS = 20;
	static final int BUILDING_WIDTH = SOLID_WIDTH * 6;
	static final int BUILDING_HEIGHT = SOLID_HEIGHT * 3 + 64; // Because we have a ceiling, too.
	static Random random = new Random();
	static long spawnFSM, spawnNazi;
	static ArrayList<Entity> tiles = new ArrayList<>();
	static Date date = new Date();
	public static ArrayList<Entity> entities = new ArrayList<>();
	public static Nazi nazi = new Nazi(0, 0);
	public static final int TOP_BORDER = 0;
	public static final int LEFT_BORDER = 0;
	public static final int BOTTOM_BORDER = 5000;
	public static final int RIGHT_BORDER = NUM_OF_BUILDINGS * BUILDING_WIDTH;
	static final int SPAWN_NAZI_DELAY = 10000; // 10 seconds
	static final int SPAWN_FSM_DELAY = 30000; // 30 seconds
	//Server stuff
	public static Server server = new Server();
	static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) throws IOException {
		print("You're running a Dosim Online server :D");
		server.start();

		print("Enter your port, so I could set up a server: ");
		int port = scanner.nextInt();
		server.bind(port);

		// Initialize
		int[] numsOfFloors = new int[NUM_OF_BUILDINGS];
		for (int i = 0; i < numsOfFloors.length; i++)
			numsOfFloors[i] = random.nextInt(4);

		for (int i = 0; i <= NUM_OF_BUILDINGS; i++) {
			for (int x = i; x < i + BUILDING_WIDTH / SOLID_WIDTH; x++)
				tiles.add(new Solid(x, BOTTOM_BORDER - SOLID_HEIGHT));

			Structure.addStructure(i * BUILDING_WIDTH,
				BOTTOM_BORDER - BUILDING_HEIGHT, numsOfFloors[i / BUILDING_WIDTH]);
		}

		spawnFSM = 0;
		spawnNazi = 0;

		long delta = 0;
		// Update
		while (true) {
			long startTime = System.currentTimeMillis();
			update((int) delta);
			delta = System.currentTimeMillis() - startTime;
		}
	}

	static void update(int delta) {
		for (int i = 0; i < entities.size(); i++)
			entities.get(i).update(delta);

		if (spawnNazi > 0)
			spawnNazi -= delta;
		else if (areThereAnyDoses()) {
			int naziX = random.nextInt(4100) + 600;
			entities.add(new Nazi(naziX, 0));
			print("Spawned a Nazi at " + naziX + ", 0");
			spawnNazi = SPAWN_NAZI_DELAY;
		}

		if (spawnFSM > 0)
			spawnFSM -= delta;
		else if (areThereAnyDoses()) {
			int fsmX = random.nextInt(RIGHT_BORDER);
			entities.add(new FlyingSpaghettiMonster(fsmX, BOTTOM_BORDER / 2));
			print("Spawned an instance of the Flying Spaghetti Monster at " + fsmX + ", -500");
			spawnFSM = SPAWN_FSM_DELAY;
		}
	}

	public static void print(Object o) {
		Date date = new Date();
		String hours = date.getHours() > 10 ? "" + date.getHours() : "0" + date.getHours();
		String minutes = date.getMinutes() > 10 ? "" + date.getMinutes() : "0" + date.getMinutes();
		String seconds = date.getSeconds() > 10 ? "" + date.getSeconds() : "0" + date.getSeconds();
		System.out.println("[" + hours + ":" + minutes + ":" + seconds + "] " + o);
	}
	
	static boolean areThereAnyDoses() {
		for (Entity e : DOServer.entities)
			if (e instanceof Dos)
				return true;
		return false;
	}
}