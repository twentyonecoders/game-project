package gameEngine;

import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import entities.Image;
import toolBox.MousePicker;

public class GoldMine extends Image{
	
	private int gold = 0;
	private int maxGold = 5;
	private int prodRate = 1;
	private int upgradeCost = 10;

	public int ID;
	
	public GoldMine(Vector3f position, float rotX, float rotY, float rotZ, float scale, int id) {
		super("Goldmine", position, rotX, rotY, rotZ, scale);
		this.ID = id;
		generateGold();
	}
	
	public void update(MousePicker picker) {
		super.update(picker);
		if(this.isClicked == true) {
			run(picker);
		}
	}
	
	public void run(MousePicker picker) {
		super.run(picker);
		if(picker.isLeftButtonDown()) {
			takeGold();
		}
		while(Keyboard.next()) {
			if(Keyboard.getEventKeyState()) {
				if(Keyboard.getEventKey() == Keyboard.KEY_U) { this.upgrade(); }
				else if(Keyboard.getEventKey() == Keyboard.KEY_M) { this.setLocationSet(false); }
			}
		}
	}
	
	public void generateGold(){
		Timer timer = new Timer();
		    
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				if (gold <= maxGold - 1) {
					gold += prodRate;
					System.out.println("Goldmine Nr " + ID + " hat : " + gold + " Gold");
				} else {
					System.out.println("Goldmine Nr " + ID + " voll!");
				}
		    }
		}, 1*1000, 1*1000);
	}
	
	public void takeGold() {
		Main.gold += gold;
		gold = 0;
	}
	
	public void upgrade() {
		maxGold += 10;
		prodRate++;
		Main.gold -= upgradeCost;
		upgradeCost *= 2;
	}

}
