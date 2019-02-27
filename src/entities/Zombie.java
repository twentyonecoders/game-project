package entities;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import gameEngine.Main;

public class Zombie extends Image{

	public int ID;

	Vector3f target;
	
	public Zombie (Vector3f position, float rotX, float rotY, float rotZ, float scale, int id) {
		super(("Zombie"), position, rotX, rotY, rotZ, scale, 3);
		this.ID = id;
		hp = 10;
		setClicked(false);
		health.setPosition(new Vector2f(position.x, position.y - 0.15f));
	}
	
	public void move() {
		if(!hitObject()) {
			if(!Main.goldmines.isEmpty()){
				target = nearestObject().getPosition();
				Vector3f step = (calcStep(calcDirection(target)));
				increasePosition(step.x, step.y, 0);
				health.setPosition(new Vector2f(position.x, position.y - 0.15f));
			}
		}
	}
	
	private boolean hitObject() {
		for(Soldier soldier: Main.soldiers) {
			if(soldier.hit(position) && soldier.isActive()) {
				soldier.hp -= 5;
				hp -= 5;
				return true;
			}
		}
		for(Image image: Main.images) {
			if(image.hit(position) && image.type == 1) {
				image.hp -= 5;
				return true;
			}
		}
		return false;
	}

	private Image nearestObject(){
		Image nearest = null;
		nearest = Main.goldmines.get(0);
		for(Image image: Main.images) {
			if(image.type != 3 && calcDirection(image.getPosition()).length() < calcDirection(nearest.getPosition()).length()) {
				nearest = image;
			}
		}
		
		for(Goldmine goldmine: Main.goldmines) {
			if(calcDirection(goldmine.getPosition()).length() < calcDirection(nearest.getPosition()).length()) {
				nearest = goldmine;
			}
		}
		return nearest;
	}
	
	private Vector3f calcDirection(Vector3f target) {
		Vector3f direction = new Vector3f();
		Vector3f.sub(target, position, direction);
		direction.set(direction.x, direction.y, 0);
		return direction;
	}
	
	private Vector3f calcStep(Vector3f direction) {
		Vector3f step = new Vector3f();
		float distance = direction.length() * 20;
		step.set(direction.x / distance, direction.y / distance, 0);
		return step;
	}
}
