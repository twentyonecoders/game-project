package entities;

import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import audio.AudioMaster;
import audio.Source;
import fonts.GUIText;
import gameEngine.Main;
import toolBox.MousePicker;

public class Farm extends Image {

	public int ID;
	public static int woodCost = 30;
	public static int goldCost = 50;
	
	boolean run = true;
	Source source = new Source();
	private int upgradeBuffer = AudioMaster.loadSound("audio/upgrade.wav");
	
	private int prodRate = 1;
	private int upgradeCost = 10;
	private int level = 1;
	
	public Farm(Vector3f position, float rotX, float rotY, float rotZ, float scale, int id) {
		super("Farm", position, rotX, rotY, rotZ, scale, 1);
		ID = id;
		hp = 50;
		generateFood();
		AudioMaster.sources.add(source);
		Main.gold -= goldCost;
		Main.wood -= woodCost;
	}
	
	public void update(MousePicker picker) {
		super.update(picker);
		while(Keyboard.next()) {
			if(Keyboard.getEventKeyState()) {
				if(Keyboard.getEventKey() == Keyboard.KEY_U && Main.gold >= upgradeCost) { upgrade(); }
				else if(Keyboard.getEventKey() == Keyboard.KEY_M) { 
					setLocationSet(false);
				}
			}
		}
	}
	
	protected void showGUI() {
		GUIText Text = new GUIText("ID : " + ID + " | HP : " + hp, 1.5f, Main.font, new Vector2f(0.0f, 0.1f), 1f, true);
		GUIText upgradeText = new GUIText("Press 'U' to upgrade (" + upgradeCost + " Gold)", 1.5f, Main.font, new Vector2f(0.0f, 0.3f), 1f, true);
		GUIText moveText = new GUIText("Press 'M' to move building", 1.5f, Main.font, new Vector2f(0.0f, 0.2f), 1f, true);
		upgradeText.setColour(255, 255, 255);
		moveText.setColour(255, 255, 255);
		texts.add(Text);
		texts.add(upgradeText);
		texts.add(moveText);
	}
	
	private void generateFood(){
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
				if(run)
					Main.food += prodRate;
			}
		};
		timer.schedule(task, 1000, 1000);
	}
	
	private void upgrade() {
		if(level < 5) {
			source.play(upgradeBuffer);
			level++;
			prodRate++;
			Main.gold -= upgradeCost;
			upgradeCost *= 2;
		} else { System.out.println("Farm Nr " + ID + " has reached the maximum level!"); }
	}
}
