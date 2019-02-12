package entities;

import org.lwjgl.util.vector.Vector3f;

public class Zombie extends Image{

	public int ID;
	
	public Zombie (Vector3f position, float rotX, float rotY, float rotZ, float scale, int id) {
		super(("Soldat"), position, rotX, rotY, rotZ, scale);
		this.ID = id;
	}
	
	public void update(Vector3f target) {
		increasePosition(calculateDirection(target).x / 10, calculateDirection(target).y / 10, 0);
	}
	
	public Vector3f calculateDirection(Vector3f target) {
		Vector3f direction = new Vector3f();
		Vector3f.sub(target, position, direction);
		//direction.set(direction.x / 10);
		//float distance = direction.length();
		return direction;
	}
}
