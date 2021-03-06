package entities;

import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import fonts.GUIText;
import gameEngine.Game;
import gameEngine.Main;
import toolBox.MousePicker;

public class Soldier extends Image{

	public int ID;
	public static int goldCost = 10;
	public static int foodCost = 10;
	
	public boolean active;
	
	public Soldier(Vector3f position, float rotX, float rotY, float rotZ, float scale, int id) {
		super(("Soldat"), position, rotX, rotY, rotZ, scale, 2);
		this.ID = id;
		hp = 20;
		active = true;
		useFood();
		Game.gold -= goldCost;
		Game.food -= foodCost;
	}
	
	public void run(MousePicker picker) {
		super.run(picker);
		while(Keyboard.next()) {
			if(Keyboard.getEventKeyState()) {
				if(Keyboard.getEventKey() == Keyboard.KEY_M) { setLocationSet(false); }
			}
		}
	}
	
	public void update() {
		super.update();
		if(!active) {
			changeImage("Soldat_inactive");
		} else if (active) {
			changeImage("Soldat");
		}
	}
	
	protected void showGUI() {
		GUIText Text = new GUIText("ID : " + ID + " | HP : " + hp, 1.5f, Main.font, new Vector2f(0.0f, 0.1f), 1f, true);
		GUIText moveText = new GUIText("Press 'M' to move soldier", 1.5f, Main.font, new Vector2f(0.0f, 0.2f), 1f, true);
		moveText.setColour(255, 255, 255);
		texts.add(Text);
		texts.add(moveText);
	}
	
	private void useFood() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				if(Game.food > 0) {
					Game.food--;
					setActive(true);
				} else {
					setActive(false);
				}
			}
		}, 1000, 1000);
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean isActive() {
		return this.active;
	}
}
