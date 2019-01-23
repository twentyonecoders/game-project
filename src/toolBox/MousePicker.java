package toolBox;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;

public class MousePicker {

	Vector3f currentRay;
	
	Matrix4f projectionMatrix;
	Matrix4f viewMatrix;
	Camera camera;
	
	boolean leftButtonDown;
	boolean rightButtonDown;
	
	public MousePicker(Matrix4f projectionMatrix, Camera camera) {
		this.projectionMatrix = projectionMatrix;
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
	}
	
	private Vector3f calculateMouseRay() {
		float mouseX = Mouse.getX();
		float mouseY = Mouse.getY();
		Vector2f normalizedCoords = getNormalizedDeviceCoords(mouseX, mouseY);
		Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1f, 1f);
		Vector4f eyeCoords = getEyeCoords(clipCoords);
		Vector3f worldCoords = getWorldCoords(eyeCoords);
		return worldCoords;
	}
	
	private Vector2f getNormalizedDeviceCoords(float mouseX, float mouseY) {
		float x = (2f*mouseX) / Display.getWidth() - 1;
		float y = (2f*mouseY) / Display.getHeight() - 1;
		return new Vector2f(x, y);
	}
	
	private Vector4f getEyeCoords(Vector4f clipCoords) {
		Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
		Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 1f);
	}
	
	private Vector3f getWorldCoords(Vector4f eyeCoords) {
		Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
		Vector4f worldCoords = Matrix4f.transform(invertedView, eyeCoords, null);
		Vector3f mouseRay = new Vector3f(worldCoords.x, worldCoords.y, worldCoords.z);
		mouseRay.normalise();
		return mouseRay;
	}
}
