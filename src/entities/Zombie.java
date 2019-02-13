package entities;

import org.lwjgl.util.vector.Vector3f;

import gameEngine.Main;

public class Zombie extends Image{

	public int ID;
	
	Vector3f target;
	
	public Zombie (Vector3f position, float rotX, float rotY, float rotZ, float scale, int id) {
		super(("Soldat"), position, rotX, rotY, rotZ, scale);
		this.ID = id;
	}
	
	public void update() {
		if(hitsGoldmine()) {
			System.out.println("hit goldmine");
		} else if(hitsSoldier()){
			System.out.println("hit soldier");
		} else {
			target = nearestObject().getPosition();
			increasePosition(calculateDirection(target).x, calculateDirection(target).y, 0);
		}
	}
	
	public boolean hitsGoldmine() {
		if(!Main.goldmines.isEmpty()) {
			for(Goldmine goldmine: Main.goldmines) {
				if(hit(goldmine.getPosition())) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean hitsSoldier() {
		if(!Main.soldiers.isEmpty()) {
			for(Soldier soldier: Main.soldiers) {
				if(hit(soldier.getPosition())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public Image nearestObject(){
		Image nearest = null;
		if(!Main.goldmines.isEmpty()) {
			nearest = Main.goldmines.get(0);
			for(Goldmine goldmine: Main.goldmines) {
				if(calculateDirection(goldmine.getPosition()).length() < calculateDirection(nearest.getPosition()).length()) {
					nearest = goldmine;
				}
			}
		}
		return nearest;
	}
	
	private Vector3f calculateDirection(Vector3f target) {
		Vector3f direction = new Vector3f();
		Vector3f.sub(target, position, direction);
		float distance = direction.length() * 20;
		direction.set(direction.x / distance, direction.y / distance, 0);
		direction.set(direction.x, direction.y, 0);
		return direction;
	}
}
