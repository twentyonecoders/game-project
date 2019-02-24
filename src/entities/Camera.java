package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import gameEngine.Main;
import toolBox.MousePicker;

public class Camera {

	private Vector3f position = new Vector3f(0,0,0);
	private float pitch;
	private float yaw;
	private float roll;
	
	public void move(MousePicker picker) {
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			position.x -= 0.02f;
			picker.dx -= 0.02f;
			for(Image image: Main.images) {	image.health.getPosition().translate(0.02f, 0); }
		}else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			position.x += 0.02f;
			picker.dx += 0.02f;
			for(Image image: Main.images) {	image.health.getPosition().translate(-0.02f, 0); }
		}else if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			position.y += 0.02f;
			picker.dy += 0.02f;
			for(Image image: Main.images) {	image.health.getPosition().translate(0, -0.02f); }
		}else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			position.y -= 0.02f;
			picker.dy -= 0.02f;
			for(Image image: Main.images) {	image.health.getPosition().translate(0, 0.02f); }
		}
	}
	
	public Vector3f getPosition() {
		return position;
	}
	public float getPitch() {
		return pitch;
	}
	public float getYaw() {
		return yaw;
	}
	public float getRoll() {
		return roll;
	}
	
}
