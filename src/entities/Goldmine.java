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
	
	Source source = new Source();
	private int collectBuffer = AudioMaster.loadSound("audio/collect.wav");
	private int upgradeBuffer = AudioMaster.loadSound("audio/upgrade.wav");
	
	public int ID;
	
	private int gold = 0;
	private int maxGold = 5;
	private int prodRate = 1;
	private int upgradeCost = 10;
	private int level = 1;
	
	public boolean clicked = false;
	public boolean locationSet = false;
	
	public Goldmine(Vector3f position, float rotX, float rotY, float rotZ, float scale, int id) {
		super("Goldmine_1", position, rotX, rotY, rotZ, scale);
		ID = id;
		generateGold();
		AudioMaster.sources.add(source);
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
			
			if(picker.isLeftButtonDown() && hit(picker)) {
				collect();
			}
			while(Keyboard.next()) {
				if(Keyboard.getEventKeyState()) {
					if(Keyboard.getEventKey() == Keyboard.KEY_U) { upgrade(); }
					else if(Keyboard.getEventKey() == Keyboard.KEY_M) { setLocationSet(false); }
				}
			}
		}
	}
	
	protected void run(MousePicker picker) {
		//super.run(picker);
		if(picker.isRightButtonDown()) {
			setClicked(false);
		}
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
		if(picker.isLeftButtonDown()) {
			if(clicked) {
			collect();
			}
		}
		while(Keyboard.next()) {
			if(Keyboard.getEventKeyState()) {
				if(Keyboard.getEventKey() == Keyboard.KEY_U) { upgrade(); }
				else if(Keyboard.getEventKey() == Keyboard.KEY_M) { setLocationSet(false); }
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
		GUIText upgradeText = new GUIText("Press 'U' to upgrade (" + upgradeCost + " Gold)", 1.5f, Main.font, new Vector2f(0.0f, 0.3f), 1f, true);
		GUIText Text = new GUIText("ID : " + ID, 1.5f, Main.font, new Vector2f(0.0f, 0.1f), 1f, true);
		upgradeText.setColour(255, 255, 255);
		texts.add(upgradeText);
		texts.add(Text);
	}
	
	private void generateGold(){
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				if (gold <= maxGold - 1) {
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
		source.play(upgradeBuffer);
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

	protected boolean isLocationSet() {
		return locationSet;
	}

	protected void setLocationSet(boolean locationSet) {
		this.locationSet = locationSet;
	}
}
