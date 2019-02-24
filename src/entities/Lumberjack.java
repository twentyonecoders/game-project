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

public class Lumberjack extends Image{
	
	public int ID;
	public static int woodCost = 10;
	public static int goldCost = 30;
	
	Source source = new Source();
	private int collectBuffer = AudioMaster.loadSound("audio/collect.wav");
	private int upgradeBuffer = AudioMaster.loadSound("audio/upgrade.wav");
	
	private int wood = 0;
	private int max = 10;
	private int prodRate = 1;
	private int upgradeCost = 10;
	private int level = 1;
	
	public Lumberjack(Vector3f position, float rotX, float rotY, float rotZ, float scale, int id) {
		super("Holzfäller", position, rotX, rotY, rotZ, scale, 1);
		ID = id;
		hp = 50;
		generate();
		AudioMaster.sources.add(source);
		Main.gold -= goldCost;
		Main.wood -= woodCost;
	}
	
	public void update(MousePicker picker) {
		super.update(picker);
		if(picker.isLeftButtonDown()) {
			collect();
		}
		while(Keyboard.next()) {
			if(Keyboard.getEventKeyState()) {
				if(Keyboard.getEventKey() == Keyboard.KEY_U && Main.gold >= upgradeCost) { upgrade(); }
				else if(Keyboard.getEventKey() == Keyboard.KEY_M) { 
					setLocationSet(false);
				}
			}
		}
		if(hp == 0) {
			dead = true;
			setClicked(false);
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
	
	private void generate(){
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				if (wood <= max - 1) {
					wood += prodRate;
					//System.out.println("Lumberjack Nr " + ID + " has : " + wood + " Wood");
				} else {
					//System.out.println("Lumberjack Nr " + ID + " is full!");
				}
		    }
		}, 1*1000, 1*1000);
	}
	
	private void collect() {
		Main.wood += wood;
		wood = 0;
		source.play(collectBuffer);
	}
	
	private void upgrade() {
		if(level < 5) {
			source.play(upgradeBuffer);
			level++;
			max += 10 * level;
			prodRate++;
			Main.gold -= upgradeCost;
			upgradeCost *= 2;
		} else { System.out.println("Goldmine Nr " + ID + " has reached the maximum level!"); }
	}
}
