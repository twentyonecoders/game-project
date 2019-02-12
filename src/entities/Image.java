package entities;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import audio.AudioMaster;
import audio.Source;
import fonts.GUIText;
import fonts.TextMaster;
import gameEngine.Main;
import models.ModelTexture;
import models.TexturedModel;
import renderEngine.Loader;
import toolBox.MousePicker;

public class Image extends Entity{

	static Loader loader = new Loader();
	private Source source = new Source();
	private int buildBuffer = AudioMaster.loadSound("audio/build.wav");
	
	public List<GUIText> texts = new ArrayList<GUIText>();
	
	public boolean clicked = true;
	public boolean locationSet = false;
	
	public Image (String fileName, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(new TexturedModel(loader.loadToVAO(vertices, textureCoords, indices), new ModelTexture(loader.loadTexture(fileName))), position, rotX, rotY, rotZ, scale);
		Main.images.add(this);
		source.play(buildBuffer);
		AudioMaster.sources.add(source);
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
		if(!this.isLocationSet()) {
			int collisions = 0;
			setPosition(picker.getCurrentRay());
			if(picker.isLeftButtonDown()) {
				for(int i = 0; i < Main.images.size(); i++) {
					if(hit(Main.images.get(i).getPosition())) { collisions++; }
				}
				if(collisions < 2) { setLocationSet(true); }
			}
		}
		if(picker.isRightButtonDown()) {
			setClicked(false);
		}
	}
	
	public void setClicked(boolean clicked) {
		if(clicked) {
			showGUI();
			setScale(0.08f);
		} else { 
			hideGUI();
			setScale(0.075f); 
		}
		this.clicked = clicked;
	}
	
	public boolean isClicked() {
		return clicked;
	}
	
	public boolean hit(MousePicker picker) {
		if(picker.getCurrentRay().x <= getPosition().x + 0.075f &&
				picker.getCurrentRay().x >= getPosition().x - 0.075f &&
				picker.getCurrentRay().y <= getPosition().y + 0.12f &&
				picker.getCurrentRay().y >= getPosition().y - 0.12f) {
			return true;
		} else {
			return false;
		}
	}
	
	protected boolean hit(Vector3f pos) {
		if(pos.x <= getPosition().x + 0.15f &&
				pos.x >= getPosition().x - 0.15f &&
				pos.y <= getPosition().y + 0.24f &&
				pos.y >= getPosition().y - 0.24f) {
			return true;
		} else {
			return false;
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

	protected boolean isLocationSet() {
		return locationSet;
	}

	protected void setLocationSet(boolean locationSet) {
		this.locationSet = locationSet;
	}
}
