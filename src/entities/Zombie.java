package entities;

import org.lwjgl.util.vector.Vector3f;

import gameEngine.Main;

public class Zombie extends Image{

	public int ID;

	Vector3f target;
	
	public Zombie (Vector3f position, float rotX, float rotY, float rotZ, float scale, int id) {
		super(("Zombie"), position, rotX, rotY, rotZ, scale, 2);
		this.ID = id;
		hp = 10;
		setClicked(false);
	}
	
	public void die() {
		if(hitsGoldmine() || hitsSoldier());
		else {
			target = nearestObject().getPosition();
			increasePosition(calculateDirection(target).x, calculateDirection(target).y, 0);
		}
		if(hp == 0) {
			dead = true;
			setClicked(false);
		}
	}
	
	private boolean hitsGoldmine() {
		if(!Main.goldmines.isEmpty()) {
			for(Goldmine goldmine: Main.goldmines) {
				if(hit(goldmine.getPosition())) {
					goldmine.hp -= 5;
					return true;
				}
			}
		}
		return false;
	}

	private boolean hitsSoldier() {
		if(!Main.soldiers.isEmpty()) {
			for(Soldier soldier: Main.soldiers) {
				if(hit(soldier.getPosition())) {
					soldier.hp -= 5;
					hp -= 5;
					return true;
				}
			}
		}
		return false;
	}
	
	private Image nearestObject(){
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
