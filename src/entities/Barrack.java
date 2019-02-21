package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import fonts.GUIText;
import gameEngine.Main;
import toolBox.MousePicker;

public class Barrack extends Image{
	
	public int ID;
	public static int goldCost = 40;
	public static int woodCost = 30;
	
	public Barrack(Vector3f position, float rotX, float rotY, float rotZ, float scale, int id) {
		super(("Kaserne"), position, rotX, rotY, rotZ, scale, 1);
		this.ID = id;
		hp = 30;
		Main.gold -= goldCost;
		Main.wood -= woodCost;
	}
	
	public void update(MousePicker picker) {
		super.update(picker);
		while(Keyboard.next()) {
			if(Keyboard.getEventKeyState()) {
				if(Keyboard.getEventKey() == Keyboard.KEY_1 && Main.gold >= Soldier.goldCost && Main.food >= Soldier.foodCost) { buySoldier(); }
				else if(Keyboard.getEventKey() == Keyboard.KEY_M) { setLocationSet(false); }
			}
		}
		if(hp == 0) {
			dead = true;
			setClicked(false);
		}
	}
	
	protected void showGUI() {
		GUIText Text = new GUIText("ID : " + ID, 1.5f, Main.font, new Vector2f(0.0f, 0.1f), 1f, true);
		GUIText soldierText = new GUIText("Press '1' to buy soldier (" + Soldier.goldCost + " Gold, " + Soldier.foodCost + " Food)", 1.5f, Main.font, new Vector2f(0.0f, 0.3f), 1f, true);
		GUIText moveText = new GUIText("Press 'M' to move building", 1.5f, Main.font, new Vector2f(0.0f, 0.2f), 1f, true);
		soldierText.setColour(255, 255, 255);
		moveText.setColour(255, 255, 255);
		texts.add(Text);
		texts.add(soldierText);
		texts.add(moveText);
	}
		
	private void buySoldier() {
		Main.disableImages();
		Soldier soldier = new Soldier(new Vector3f(0, 0, 1), 0, 0, 0, 0.075f, Main.soldiers.size());
		Main.soldiers.add(soldier);
		Main.gold -= 10;
	}
	
}
