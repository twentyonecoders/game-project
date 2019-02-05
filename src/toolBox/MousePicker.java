package toolBox;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;

public class MousePicker {

	Vector3f currentRay;
	
	Matrix4f projectionMatrix;
	Matrix4f viewMatrix;
	Camera camera;
	
	public float dx = 0f;
	public float dy = 0f;
	
	boolean leftButtonDown;
	boolean rightButtonDown;
	
	public MousePicker(Camera camera) {
		this.viewMatrix = Maths.createViewMatrix(camera);
		this.camera = camera;
	}
	
	public Vector3f getCurrentRay() {
		return currentRay;
	}
	
	public boolean isLeftButtonDown() {
		leftButtonDown = Mouse.isButtonDown(0);
		return leftButtonDown;
	}

	public boolean isRightButtonDown() {
		rightButtonDown = Mouse.isButtonDown(1);
		return rightButtonDown;
	}

	public void update() {
		viewMatrix = Maths.createViewMatrix(camera);
		currentRay = calculateMouseRay();
		move(dx, dy);
	}
	
	public void move(float dx, float dy) {
		currentRay = new Vector3f(currentRay.x + dx, currentRay.y + dy, currentRay.z);
	}
	
	public Vector3f calculateMouseRay() {
		float mouseX = Mouse.getX();
		float mouseY = Mouse.getY();
		Vector2f normalizedCoords = getNormalizedDeviceCoords(mouseX, mouseY);
		return new Vector3f(normalizedCoords.x, normalizedCoords.y, 1f);
	}
	
	public Vector2f getNormalizedDeviceCoords(float mouseX, float mouseY) {
		float x = (2f*mouseX) / Display.getWidth() - 1;
		float y = (2f*mouseY) / Display.getHeight() - 1;
		return new Vector2f(x, y);
	}
	
}
