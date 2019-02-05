package entities;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import fonts.GUIText;
import fonts.TextMaster;
import gameEngine.Main;
import models.TexturedModel;
import renderEngine.Loader;
import textures.ModelTexture;
import toolBox.MousePicker;

public class Image extends Entity{

	static Loader loader = new Loader();
	public boolean isClicked;
	public boolean locationSet = true;
	
	public List<GUIText> texts = new ArrayList<GUIText>();
	
	public Image (String fileName, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(new TexturedModel(loader.loadToVAO(vertices, textureCoords, indices), new ModelTexture(loader.loadTexture(fileName))), position, rotX, rotY, rotZ, scale);
		Main.images.add(this);
	}
	
	public static float[] vertices = {
			-1, 1.6f, 0,
			-1, -1.6f, 0,
			1, -1.6f, 0,
			1, 1.6f, 0
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
		setModel(new TexturedModel(loader.loadToVAO(vertices, textureCoords, indices), new ModelTexture(loader.loadTexture(fileName))));
	}
	
	public void update(MousePicker picker) {
		if(picker.isLeftButtonDown()) {
			if(hit(picker)) {
				setClicked(true);
			}
		}
	}
	
	protected void run(MousePicker picker) {
		if(picker.isRightButtonDown()) {
			setClicked(false);
		}
		if(!this.isLocationSet()) {
			setPosition(picker.getCurrentRay());
			if(picker.isLeftButtonDown()) {
				setLocationSet(true);
			}
		}
	}
	
	protected void showGUI() {
		GUIText moveText = new GUIText("Press 'M' to move building", 1.5f, Main.font, new Vector2f(0.0f, 0.2f), 1f, true);
		moveText.setColour(255, 255, 255);
		texts.add(moveText);
	}
	
	protected void hideGUI() {
		for(GUIText text: texts) {
			TextMaster.removeText(text);
		}
	}
	
	private boolean hit(MousePicker picker) {
		if(picker.getCurrentRay().x <= getPosition().x + 0.125f &&
				picker.getCurrentRay().x >= getPosition().x - 0.125f &&
				picker.getCurrentRay().y <= getPosition().y + 0.2f &&
				picker.getCurrentRay().y >= getPosition().y - 0.2f) {
			return true;
		} else {
			return false;
		}
	}

	public void setClicked(boolean isClicked) {
		if(isClicked == false) {
			setScale(0.125f);
			setLocationSet(true);
			this.isClicked = isClicked;
		} else if(isClicked == true) {
			Main.disableImages();
			setScale(0.15f);
			this.isClicked = isClicked;
		}
	}
	
	protected boolean isLocationSet() {
		return locationSet;
	}

	protected void setLocationSet(boolean locationSet) {
		this.locationSet = locationSet;
	}

}
