package org.dosimonline.server;
import org.dosimonline.server.tiles.Ceiling;
import org.dosimonline.server.tiles.Ladder;
import org.dosimonline.server.tiles.Solid;

public class Structure {
	public static void addStructure(int x, int y, int numOfFloors) {
		for (int i = 0; i < numOfFloors; i++, y -= 448)
			addFloor(x, y);
	}

	static void addFloor(int x, int y) {
		addWall(x, y);
		addWall(x, y + 128);
		addWall(x + 384, y);
		addWall(x + 384, y + 128);
		addCeiling(x, y - 64);
		for (int i = 2; i < 4; i++)
			addCeiling(x + 128 * i, y - 64);
		addLadder(x + 128, y - 128);
		addLadder(x + 128, y);
		addLadder(x + 128, y + 128);
		addLadder(x + 128, y + 256);
	}

	static void addWall(int x, int y) {
		DosimOnlineServer.entities.add(new Solid(x, y));
	}

	static void addCeiling(int x, int y) {
		DosimOnlineServer.entities.add(new Ceiling(x, y));
	}

	static void addLadder(int x, int y) {
		DosimOnlineServer.entities.add(new Ladder(x, y));
	}
}
