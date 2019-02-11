package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import audio.AudioMaster;
import audio.Source;
import fonts.GUIText;
import gameEngine.Main;
import toolBox.MousePicker;

public class Barrack extends Image{
	
	public int ID;
	
	public boolean clicked = false;
	public boolean locationSet = false;
	
	public Barrack(Vector3f position, float rotX, float rotY, float rotZ, float scale, int id) {
		super(("Kaserne"), position, rotX, rotY, rotZ, scale);
		this.ID = id;
	}
	
	public void update(MousePicker picker) {
		if(!this.isLocationSet()) {
			int collisions = 0;
			setPosition(picker.getCurrentRay());
			if(picker.isLeftButtonDown()) {
				for(int i = 0; i < Main.images.size(); i++) {
					if(hit(Main.images.get(i).getPosition())) {
						collisions++;
					}
				}
				if(collisions < 2) {
					setLocationSet(true);
				}
			}
		}
		if(isClicked()) {
			if(picker.isRightButtonDown()) {
				setClicked(false);
			}
			while(Keyboard.next()) {
				if(Keyboard.getEventKeyState()) {
					if(Keyboard.getEventKey() == Keyboard.KEY_1) { System.out.println("bought soldier"); }
					else if(Keyboard.getEventKey() == Keyboard.KEY_M) { setLocationSet(false); }
				}
			}
		}
	}
	
	public void setClicked(boolean clicked) {
		if(clicked) {
			showGUI();
			setScale(0.15f);
		} else { 
			hideGUI();
			setScale(0.125f); 
		}
		this.clicked = clicked;
	}
	
	public boolean isClicked() {
		return clicked;
	}
	
	protected void showGUI() {
		super.showGUI();
		GUIText upgradeText = new GUIText("Press '1' to buy soldier (10 Gold)", 1.5f, Main.font, new Vector2f(0.0f, 0.3f), 1f, true);
		GUIText Text = new GUIText("ID : " + ID, 1.5f, Main.font, new Vector2f(0.0f, 0.1f), 1f, true);
		upgradeText.setColour(255, 255, 255);
		texts.add(upgradeText);
		texts.add(Text);
	}
		
	private void buySoldier() {
		Soldier soldat = new Soldier(new Vector3f(0, 0, 1), 0, 0, 0, 0.125f, Main.soldiers.size() + 1);
		Main.soldiers.add(soldat);
		Main.gold -= 10;
	}
	
	protected boolean isLocationSet() {
		return locationSet;
	}

	protected void setLocationSet(boolean locationSet) {
		this.locationSet = locationSet;
	}
}
