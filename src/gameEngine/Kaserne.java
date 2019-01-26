package gameEngine;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import entities.Image;
import toolBox.MousePicker;

public class Kaserne extends Image{

	int ID;
	
	public Kaserne(Vector3f position, float rotX, float rotY, float rotZ, float scale, int id) {
		super(("Kaserne"), position, rotX, rotY, rotZ, scale);
		this.ID = id;
	}
	
	public void update(MousePicker picker) {
		super.update(picker);
		if(this.isClicked == true) {
			run(picker);
		}
	}
	
	public void run(MousePicker picker) {
		super.run(picker);
		while(Keyboard.next()) {
			if(Keyboard.getEventKeyState()) {
				if(Keyboard.getEventKey() == Keyboard.KEY_M) { this.setLocationSet(false); }
				else if(Keyboard.getEventKey() == Keyboard.KEY_U) { System.out.println("Kaserne upgraded"); }
				else if(Keyboard.getEventKey() == Keyboard.KEY_1) { buySoldier(); }
			}
		}
	}
		
	public void buySoldier() {
		Soldat soldat = new Soldat(new Vector3f(0, 0, -10), 0, 0, 0, 0.5f, 1);
		Main.entities.add(soldat);
		Main.gold -= 10;
	}

}
