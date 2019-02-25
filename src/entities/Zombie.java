package entities;

import org.lwjgl.util.vector.Vector2f;
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
		health.setPosition(new Vector2f(position.x, position.y - 0.15f));
	}
	
	public void update() {
		if(checkSoldier()) {}
		else if(!Main.goldmines.isEmpty() && hitsGoldmine());
		else if(!Main.goldmines.isEmpty()){
			target = nearestObject().getPosition();
			increasePosition(calculateDirection(target).x, calculateDirection(target).y, 0);
			health.setPosition(new Vector2f(position.x, position.y - 0.15f));
		}
		if(hp == 5) { health.setTexture(loader.loadTexture("/HPBar/50%"));
		} else if(hp == 0) {
			Main.guiGraphics.remove(health);
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

	private boolean checkSoldier() {
		if(!Main.soldiers.isEmpty()) {
			for(Soldier soldier: Main.soldiers) {
				if(hit(soldier.getPosition()) && soldier.isActive()) {
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
