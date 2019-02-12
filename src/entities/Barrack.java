package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import fonts.GUIText;
import gameEngine.Main;
import toolBox.MousePicker;

public class Barrack extends Image{
	
	public int ID;
	
	public Barrack(Vector3f position, float rotX, float rotY, float rotZ, float scale, int id) {
		super(("Kaserne"), position, rotX, rotY, rotZ, scale);
		this.ID = id;
	}
	
	public void update(MousePicker picker) {
		super.update(picker);
		while(Keyboard.next()) {
			if(Keyboard.getEventKeyState()) {
				if(Keyboard.getEventKey() == Keyboard.KEY_1) { buySoldier(); }
				else if(Keyboard.getEventKey() == Keyboard.KEY_M) { setLocationSet(false); }
			}
		}
	}
	
	protected void showGUI() {
		super.showGUI();
		GUIText soldierText = new GUIText("Press '1' to buy soldier (10 Gold)", 1.5f, Main.font, new Vector2f(0.0f, 0.3f), 1f, true);
		GUIText Text = new GUIText("ID : " + ID, 1.5f, Main.font, new Vector2f(0.0f, 0.1f), 1f, true);
		soldierText.setColour(255, 255, 255);
		texts.add(soldierText);
		texts.add(Text);
	}
		
	private void buySoldier() {
		Main.disableImages();
		Soldier soldat = new Soldier(new Vector3f(0, 0, 1), 0, 0, 0, 0.075f, Main.soldiers.size());
		Main.soldiers.add(soldat);
		Main.gold -= 10;
	}
	
}
