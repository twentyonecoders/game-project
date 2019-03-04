package entities;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import audio.AudioMaster;
import audio.Source;
import fonts.GUIText;
import fonts.TextMaster;
import gameEngine.Game;
import gameEngine.Main;
import guis.GUITexture;
import models.ModelTexture;
import models.TexturedModel;
import renderEngine.Loader;
import toolBox.MousePicker;

public class Image extends Entity{

	static Loader loader = new Loader();
	private Source source = new Source();
	private int buildBuffer = AudioMaster.loadSound("audio/build.wav");
	
	public List<GUIText> texts = new ArrayList<GUIText>();
	
	public int type;
	public boolean clicked = true;
	public boolean locationSet = false;
	public boolean dead = false;
	
	protected GUITexture health;
	
	protected int hp;
	private float width_right, width_left, height;
	
	public Image (String fileName, Vector3f position, float rotX, float rotY, float rotZ, float scale, int type) {
		super(new TexturedModel(loader.loadToVAO(vertices, textureCoords, indices), new ModelTexture(loader.loadTexture(fileName))), position, rotX, rotY, rotZ, scale);
		this.type = type;
		Main.images.add(this);
		Game.moving = true;
		if(type == 1) {
			AudioMaster.sources.add(source);
			source.setVolume(0.1f);
			source.play(buildBuffer);
		}
		//buildings
		if(type == 1) {
			width_right = 0.15f;
			width_left = 0.15f;
			height = 0.24f;
			hp = 40;
		}
		//soldiers
		if(type == 2) { hp = 20; }
		//zombies
		if(type == 3) { hp = 10; }
		//soldiers & zombies hitbox
		if(type == 2 || type == 3) {
			width_right = 0.06f;
			width_left = 0.08f;
			height = 0.2f;
		}
		health = new GUITexture(loader.loadTexture("/HPBar/100%"), new Vector2f(0, 0), new Vector2f(0.07f, 0.02f));
		Main.guiGraphics.add(health);
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
	
	public void run(MousePicker picker) {
		if(!this.isLocationSet()) {
			int collisions = 0;
			setPosition(picker.getCurrentRay());
			health.setPosition(new Vector2f(picker.getCurrentRay().x, picker.getCurrentRay().y - 0.15f));
			if(picker.isLeftButtonDown()) {
				for(int i = 0; i < Main.images.size(); i++) {
					if(hit(Main.images.get(i).getPosition())) { collisions++; }
				}
				if(collisions < 2) { setLocationSet(true); }
			}
		}
	}
	
	public void update() {
		if(type == 1) {
			if(hp > 30) { health.setTexture(loader.loadTexture("/HPBar/100%"));
			} else if(hp > 20) { health.setTexture(loader.loadTexture("/HPBar/75%"));
			} else if(hp > 10) { health.setTexture(loader.loadTexture("/HPBar/50%"));
			} else if(hp > 0) { health.setTexture(loader.loadTexture("/HPBar/25%"));
			}
		} else if(type == 2) {
			if(hp > 15) { health.setTexture(loader.loadTexture("/HPBar/100%"));
			} else if(hp > 10) { health.setTexture(loader.loadTexture("/HPBar/75%"));
			} else if(hp > 5) { health.setTexture(loader.loadTexture("/HPBar/50%"));
			} else if(hp > 0) { health.setTexture(loader.loadTexture("/HPBar/25%"));
			}
		} else if(type == 3) {
			if(hp > 5) { health.setTexture(loader.loadTexture("/HPBar/100%"));
			} else if(hp > 0) { health.setTexture(loader.loadTexture("/HPBar/50%"));
			}
		}
		if(hp <= 0) {
			Main.guiGraphics.remove(health);
			dead = true;
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
			Game.moving = false;
		}
		this.clicked = clicked;
	}
	
	public boolean isClicked() {
		return clicked;
	}
	
	public boolean hit(MousePicker picker) {
		if(picker.getCurrentRay().x <= getPosition().x + width_right / 2 &&
				picker.getCurrentRay().x >= getPosition().x - width_left / 2 &&
				picker.getCurrentRay().y <= getPosition().y + height / 2 &&
				picker.getCurrentRay().y >= getPosition().y - height / 2) {
			return true;
		} else {
			return false;
		}
	}
	
	protected boolean hit(Vector3f pos) {
		if(pos.x <= getPosition().x + width_right &&
				pos.x >= getPosition().x - width_left &&
				pos.y <= getPosition().y + height &&
				pos.y >= getPosition().y - height) {
			return true;
		} else {
			return false;
		}
	}
	
	protected void showGUI() {
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
		if(locationSet) { Game.moving = false; }
		else { Game.moving = true; }
		this.locationSet = locationSet;
	}
}
