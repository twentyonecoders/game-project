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

public class Goldmine extends Image{
	
	public int ID;
	public static int woodCost = 20;
	public static int goldCost = 30;
	
	Source source = new Source();
	private int collectBuffer = AudioMaster.loadSound("audio/collect.wav");
	private int upgradeBuffer = AudioMaster.loadSound("audio/upgrade.wav");
	
	private int gold = 0;
	private int max = 20;
	private int prodRate = 2;
	private int upgradeCost = 10;
	private int level = 1;
	
	public Goldmine(Vector3f position, float rotX, float rotY, float rotZ, float scale, int id) {
		super("Goldmine_1", position, rotX, rotY, rotZ, scale, 1);
		ID = id;
		hp = 40;
		generate();
		AudioMaster.sources.add(source);
		Main.gold -= goldCost;
		Main.wood -= woodCost;
	}
	
	public void run(MousePicker picker) {
		super.run(picker);
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
	}
	
	public void update() {
		if(hp > 30) { health.setTexture(loader.loadTexture("/HPBar/100%"));
		} else if(hp > 20) { health.setTexture(loader.loadTexture("/HPBar/75%"));
		} else if(hp > 10) { health.setTexture(loader.loadTexture("/HPBar/50%"));
		} else if(hp > 0) { health.setTexture(loader.loadTexture("/HPBar/25%"));
		} else if(hp <= 0) {
			Main.guiGraphics.remove(health);
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
				if (gold <= max - 1) {
					gold += prodRate;
					//System.out.println("Goldmine Nr " + ID + " has : " + gold + " Gold");
				} else {
					//System.out.println("Goldmine Nr " + ID + " is full!");
				}
		    }
		}, 1*1000, 1*1000);
	}
	
	private void collect() {
		Main.gold += gold;
		gold = 0;
		source.play(collectBuffer);
	}
	
	private void upgrade() {
		if(level < 5) {
			source.play(upgradeBuffer);
			level++;
			if(level == 2) { changeImage("Goldmine_2"); }
			else if(level == 3) { changeImage("Goldmine_3"); }
			else if(level == 4) { changeImage("Goldmine_4"); }
			else if(level == 5) { changeImage("Goldmine_5"); }
			max += 10 * level;
			prodRate *= 2;
			Main.gold -= upgradeCost;
			upgradeCost *= 2;
		} else { System.out.println("Goldmine Nr " + ID + " has reached the maximum level!"); }
	}
}
