package entities;

import org.lwjgl.util.vector.Vector3f;

import toolBox.MousePicker;

public class Zombie extends Image{

	public int ID;
	
	public Zombie (Vector3f position, float rotX, float rotY, float rotZ, float scale, int id) {
		super(("Soldat"), position, rotX, rotY, rotZ, scale);
		this.ID = id;
	}
	
	public void update(MousePicker picker) {
		increasePosition(0.01f, 0, 0);
	}
}
