package gameEngine;

import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import audio.AudioMaster;
import audio.Source;
import entities.Image;
import fonts.GUIText;
import toolBox.MousePicker;

public class Goldmine extends Image{
	
	public int ID;
	
	private int gold = 0;
	private int maxGold = 5;
	private int prodRate = 1;
	private int upgradeCost = 10;
	private int level = 1;
	
	Source source = new Source();
	private int buffer = AudioMaster.loadSound("audio/bounce.wav");
	
	public Goldmine(Vector3f position, float rotX, float rotY, float rotZ, float scale, int id) {
		super("Goldmine_1", position, rotX, rotY, rotZ, scale);
		ID = id;
		generateGold();
		source.play(buffer);
	}
	
	public void update(MousePicker picker) {
		super.update(picker);
		if(isClicked == true) {
			if(!source.isPlaying())
				source.play(buffer);
			run(picker);
			showGUI();
		} else {
			hideGUI();
		}
	}
	
	protected void run(MousePicker picker) {
		super.run(picker);
		if(picker.isLeftButtonDown()) {
			collect();
		}
		while(Keyboard.next()) {
			if(Keyboard.getEventKeyState()) {
				if(Keyboard.getEventKey() == Keyboard.KEY_U) { upgrade(); }
				else if(Keyboard.getEventKey() == Keyboard.KEY_M) { setLocationSet(false); }
			}
		}
	}
	
	protected void showGUI() {
		super.showGUI();
		GUIText upgradeText = new GUIText("Press 'U' to upgrade (" + upgradeCost + " Gold)", 1.5f, Main.font, new Vector2f(0.0f, 0.3f), 1f, true);
		upgradeText.setColour(255, 255, 255);
		texts.add(upgradeText);
	}
	
	private void generateGold(){
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				if (gold <= maxGold - 1) {
					gold += prodRate;
					System.out.println("Goldmine Nr " + ID + " has : " + gold + " Gold");
				} else {
					System.out.println("Goldmine Nr " + ID + " is full!");
				}
		    }
		}, 1*1000, 1*1000);
	}
	
	private void collect() {
		Main.gold += gold;
		gold = 0;
	}
	
	private void upgrade() {
		if(level < 5 && Main.gold >= upgradeCost) {
			level++;
			if(level == 2) { changeImage("Goldmine_2"); }
			else if(level == 3) { changeImage("Goldmine_3"); }
			else if(level == 4) { changeImage("Goldmine_4"); }
			else if(level == 5) { changeImage("Goldmine_5"); }
			maxGold += 10;
			prodRate++;
			Main.gold -= upgradeCost;
			upgradeCost *= 2;
			hideGUI();
		} else { System.out.println("Goldmine Nr " + ID + " has reached the maximum level!"); }
	}

}
