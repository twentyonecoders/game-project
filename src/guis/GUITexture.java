package guis;

import org.lwjgl.util.vector.Vector2f;

import toolBox.MousePicker;

public class GUITexture {

	public boolean dead;
	
	private int texture;
	private Vector2f position;
	private Vector2f scale;
	
	float width, height;
	
	public GUITexture(int texture, Vector2f position, Vector2f scale) {
		this.texture = texture;
		this.position = position;
		this.scale = scale;
		width = 0.3f;
		height = 0.175f;
	}
	
	public boolean hit(MousePicker picker) {
		if(picker.getCurrentRay().x <= getPosition().x + width / 2 &&
				picker.getCurrentRay().x >= getPosition().x - width / 2 &&
				picker.getCurrentRay().y <= getPosition().y + height / 2 &&
				picker.getCurrentRay().y >= getPosition().y - height / 2) {
			return true;
		} else {
			return false;
		}
	}

	public int getTexture() {
		return texture;
	}

	public void setTexture(int texture) {
		this.texture = texture;
	}

	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public Vector2f getScale() {
		return scale;
	}
	
}
