package gameEngine;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import entities.Image;
import toolBox.MousePicker;

public class Soldat extends Image{

	int ID;
	
	public Soldat(Vector3f position, float rotX, float rotY, float rotZ, float scale, int id) {
		super(("Soldat"), position, rotX, rotY, rotZ, scale);
		this.ID = id;
	}
	
	public void update(MousePicker picker) {
		super.update(picker);
		if(this.isClicked == true) {
			run(picker);
		}
	}
	
	protected void run(MousePicker picker) {
		super.run(picker);
		while(Keyboard.next()) {
			if(Keyboard.getEventKeyState()) {
				if(Keyboard.getEventKey() == Keyboard.KEY_M) { this.setLocationSet(false); }
				else if(Keyboard.getEventKey() == Keyboard.KEY_U) { System.out.println("Soldat upgraded"); }
			}
		}
	}
}
