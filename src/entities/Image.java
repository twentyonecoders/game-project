package entities;

import org.lwjgl.util.vector.Vector3f;

import gameEngine.Main;
import models.TexturedModel;
import renderEngine.Loader;
import textures.ModelTexture;
import toolBox.MousePicker;

public class Image extends Entity{

	static Loader loader = new Loader();
	public boolean isClicked;
	public boolean locationSet = true;
	
	public Image (String fileName, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(new TexturedModel(loader.loadToVAO(vertices, textureCoords, indices), new ModelTexture(loader.loadTexture(fileName))), position, rotX, rotY, rotZ, scale);
		Main.images.add(this);
	}
	
	public static float[] vertices = {
		    -1f, 1f, 0f,   //v0
		    -1f, -1f, 0f,  //v1
		    1f, -1f, 0f,   //v2
		    1f, 1f, 0f     //v3
		  };
	
	public static int[] indices = {
			0, 1, 3,
			3, 1, 2
	};
	
	public static float[] textureCoords = {
			0, 0,
			0, 1,
			1, 1,
			1, 0
	};
	
	public void changeImage(String fileName) {
		this.setModel(new TexturedModel(loader.loadToVAO(vertices, textureCoords, indices), new ModelTexture(loader.loadTexture(fileName))));
	}
	
	public void update(MousePicker picker) {
		if(picker.isLeftButtonDown()) {
			if(hit(picker)) {
				setClicked(true);
			}
		}
	}
	
	public void run(MousePicker picker) {
		if(picker.isRightButtonDown()) {
			this.setClicked(false);
		}
		if(!this.isLocationSet()) {
			this.setPosition(new Vector3f(picker.getCurrentRay().x * 10f, picker.getCurrentRay().y * 10f, -10f));
			if(picker.isLeftButtonDown()) {
				this.setLocationSet(true);
			}
		}
	}
	
	public boolean hit(MousePicker picker) {
		if(picker.getCurrentRay().x * 10 <= this.getPosition().x + 0.5f &&
				picker.getCurrentRay().x * 10 >= this.getPosition().x - 0.5f &&
				picker.getCurrentRay().y * 10 <= this.getPosition().y + 0.5f &&
				picker.getCurrentRay().y * 10 >= this.getPosition().y - 0.5f) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isClicked() {
		return isClicked;
	}

	public void setClicked(boolean isClicked) {
		if(isClicked == false) {
			this.setScale(0.5f);
			this.setLocationSet(true);
			this.isClicked = isClicked;
		} else if(isClicked == true) {
			Main.disableImages();
			this.setScale(0.6f);
			this.isClicked = isClicked;
		}
	}
	
	public boolean isLocationSet() {
		return locationSet;
	}

	public void setLocationSet(boolean locationSet) {
		this.locationSet = locationSet;
	}

}
