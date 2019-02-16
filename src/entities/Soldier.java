package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import fonts.GUIText;
import gameEngine.Main;
import toolBox.MousePicker;

public class Soldier extends Image{

	public int ID;
	
	public Soldier(Vector3f position, float rotX, float rotY, float rotZ, float scale, int id) {
		super(("Soldat"), position, rotX, rotY, rotZ, scale);
		this.ID = id;
		hp = 30;
	}
	
	public void update(MousePicker picker) {
		super.update(picker);
		while(Keyboard.next()) {
			if(Keyboard.getEventKeyState()) {
				if(Keyboard.getEventKey() == Keyboard.KEY_M) { setLocationSet(false); }
			}
		}
		if(hp == 0) {
			dead = true;
			setClicked(false);
		}
	}
	
	protected void showGUI() {
		GUIText Text = new GUIText("ID : " + ID + " | HP : " + hp, 1.5f, Main.font, new Vector2f(0.0f, 0.1f), 1f, true);
		GUIText moveText = new GUIText("Press 'M' to move soldier", 1.5f, Main.font, new Vector2f(0.0f, 0.2f), 1f, true);
		moveText.setColour(255, 255, 255);
		texts.add(Text);
		texts.add(moveText);
	}
}
