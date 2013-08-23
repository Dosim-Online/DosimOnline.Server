package org.dosimonline.server;

public class Entity {
	public float x;
	public float y;
	public int width;
	public int height;
	public String type;
	public int id;

	public Entity(float x, float y, int width, int height, String type) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.type = type;
		id = type.equals("Solid") ? DOServer.tiles.size() : DOServer.entities.size();
	}

	public Entity collide(float x, float y, String type) {
		for (Entity e : DOServer.entities)
			if (e.type.equals(type)
				&& x < e.x + e.width
				&& y < e.y + e.height
				&& x + width > e.x
				&& y + height > e.y)
				return e;
		return null;
	}

	public void update(int delta) {
	}

	public void destroy() {
		DOServer.entities.remove(this);
	}
}
