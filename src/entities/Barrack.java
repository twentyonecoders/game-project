package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import fonts.GUIText;
import gameEngine.Main;
import toolBox.MousePicker;

public class Barrack extends Image{

	int ID;
	
	public Barrack(Vector3f position, float rotX, float rotY, float rotZ, float scale, int id) {
		super(("Kaserne"), position, rotX, rotY, rotZ, scale);
		this.ID = id;
	}
	
	public void update(MousePicker picker) {
		super.update(picker);
		if(isClicked == true) {
			run(picker);
			showGUI();
		} else {
			hideGUI();
		}
	}
	
	public void run(MousePicker picker) {
		super.run(picker);
		while(Keyboard.next()) {
			if(Keyboard.getEventKeyState()) {
				if(Keyboard.getEventKey() == Keyboard.KEY_M) { setLocationSet(false); }
				else if(Keyboard.getEventKey() == Keyboard.KEY_U) { System.out.println("Barrack upgraded"); }
				else if(Keyboard.getEventKey() == Keyboard.KEY_1) { buySoldier(); }
			}
		}
	}
	
	protected void showGUI() {
		super.showGUI();
		GUIText buySoldierText = new GUIText("Press '1' to buy Soldier (10 Gold)", 1.5f, Main.font, new Vector2f(0.0f, 0.3f), 1f, true);
		buySoldierText.setColour(255, 255, 255);
		texts.add(buySoldierText);
	}
		
	private void buySoldier() {
		Soldier soldat = new Soldier(new Vector3f(0, 0, 1), 0, 0, 0, 0.125f, Main.soldiers.size() + 1);
		Main.soldiers.add(soldat);
		Main.gold -= 10;
	}

}
