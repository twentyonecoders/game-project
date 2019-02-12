package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import fonts.GUIText;
import gameEngine.Main;
import toolBox.MousePicker;

public class Soldier extends Image{

	int ID;
	
	public Soldier(Vector3f position, float rotX, float rotY, float rotZ, float scale, int id) {
		super(("Soldat"), position, rotX, rotY, rotZ, scale);
		this.ID = id;
	}
	
	public void update(MousePicker picker) {
		super.update(picker);
		while(Keyboard.next()) {
			if(Keyboard.getEventKeyState()) {
				if(Keyboard.getEventKey() == Keyboard.KEY_M) { setLocationSet(false); }
			}
		}
	}
	
	protected void showGUI() {
		super.showGUI();
		GUIText Text = new GUIText("ID : " + ID, 1.5f, Main.font, new Vector2f(0.0f, 0.1f), 1f, true);
		texts.add(Text);
	}
}
