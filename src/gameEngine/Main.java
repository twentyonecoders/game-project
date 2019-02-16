package gameEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import audio.AudioMaster;
import audio.Source;
import entities.Barrack;
import entities.Camera;
import entities.Entity;
import entities.Goldmine;
import entities.Image;
import entities.Soldier;
import entities.Zombie;
import fonts.FontType;
import fonts.GUIText;
import fonts.TextMaster;
import guis.GUIRenderer;
import guis.GUITexture;
import models.ModelTexture;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;
import toolBox.MousePicker;

public class Main {

	public static FontType font;
	
	public static int gold = 1000;
	static int gmCost = 50;
	static int baCost = 80;
	static int updateRate = 0;
	
	public static boolean moving = false;
	static boolean canSpawn = false;
	
	public static List<Image> images = new ArrayList<Image>();
	public static List<Goldmine> goldmines = new ArrayList<Goldmine>();
	public static List<Barrack> barracks = new ArrayList<Barrack>();
	public static List<Soldier> soldiers = new ArrayList<Soldier>();
	public static List<Zombie> zombies = new ArrayList<Zombie>();
	
	public static List<Image> toRemove =  new ArrayList<Image>();
	static List<GUITexture> guiGraphics = new ArrayList<GUITexture>();
	
	public static void main(String[] args) {
		//initialize display & tools
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer();
		Camera camera = new Camera();
		MousePicker picker = new MousePicker(camera);
		GUIRenderer guiRenderer = new GUIRenderer(loader);
		TextMaster.init(loader);
		AudioMaster.init();
		AudioMaster.setListenerData(0, 0, 0);
		
		//initialize sound
		int backgroundBuffer = AudioMaster.loadSound("audio/RoyaleClash.wav");
		Source source = new Source();
		source.setLooping(true);
		AudioMaster.sources.add(source);
		
		font = new FontType(loader.loadTexture("comicsans"), new File("res/comicsans.fnt"));
		
		images.clear();
		guiGraphics.clear();
		setUpGUI(loader, guiGraphics, font);
		Entity background = new Entity(new TexturedModel(loader.loadToVAO(Image.vertices, Image.textureCoords, Image.indices), 
				new ModelTexture(loader.loadTexture("Background"))), new Vector3f(0, 0, 1), 0, 0, 0, 1);

		//start timer for zombie spawning
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				canSpawn = true;
			}
		}, 5000, 10 * 1000);
		
		//source.play(backgroundBuffer);
		
		//display update loop
		while(!Display.isCloseRequested()) {
			camera.move(picker);
			picker.update();
			updateGame(picker);
			if(canSpawn) { spawnEnemy(); }
			
			renderer.prepare();
			shader.start();
			shader.loadViewMatrix(camera);
			
			renderer.render(background, shader);
			for(Image image: images) { renderer.render(image, shader); }
			renderGUI(font, guiRenderer, guiGraphics);
			
			shader.stop();
			DisplayManager.updateDisplay();
		}
		AudioMaster.cleanUp();
		TextMaster.cleanUp();
		guiRenderer.cleanUp();
		shader.cleanUp();
		loader.cleanUp();
		
		DisplayManager.exitDisplay();
		System.exit(0);
	}
	
	//initialize GUI
	public static void setUpGUI(Loader loader, List<GUITexture> guis, FontType font) {
		GUITexture guiGoldMine = new GUITexture(loader.loadTexture("GoldMine"), new Vector2f(-0.8f, -0.65f), new Vector2f(0.07f, 0.14f));
		GUITexture guiGoldMineBack = new GUITexture(loader.loadTexture("Marmor"), new Vector2f(-0.8f, -0.65f), new Vector2f(0.08f, 0.15f));
		GUITexture guiKaserne = new GUITexture(loader.loadTexture("Kaserne"), new Vector2f(-0.45f, -0.65f), new Vector2f(0.07f, 0.14f));
		GUITexture guiKaserneBack = new GUITexture(loader.loadTexture("Marmor"), new Vector2f(-0.45f, -0.65f), new Vector2f(0.08f, 0.15f));
		guis.add(guiGoldMineBack);
		guis.add(guiKaserneBack);
		guis.add(guiGoldMine);
		guis.add(guiKaserne);
		GUIText goldMineCostText = new GUIText("Cost : " + gmCost + " Gold", 1.5f, font, new Vector2f(0.03f, 0.95f), 0.15f, true);
		GUIText kaserneCostText = new GUIText("Cost : " + baCost + " Gold", 1.5f, font, new Vector2f(0.2f, 0.95f), 0.15f, true);
		goldMineCostText.setColour(255, 255, 0);
		kaserneCostText.setColour(255, 255, 0);
		GUIText goldMineText = new GUIText("Press 'G'", 1.5f, font, new Vector2f(0.03f, 0.9f), 0.15f, true);
		GUIText kaserneText = new GUIText("Press 'K'", 1.5f, font, new Vector2f(0.2f, 0.9f), 0.15f, true);
		goldMineText.setColour(0, 0, 0);
		kaserneText.setColour(0, 0, 0);
	}
	
	//render GUI
	public static void renderGUI(FontType font, GUIRenderer guiRenderer, List<GUITexture> guiGraphics) {
		GUIText text = new GUIText("Gold : " + gold, 3, font, new Vector2f(0.01f, 0.01f), 1f, false);
		text.setColour(255, 255, 0);
		guiRenderer.render(guiGraphics);
		TextMaster.render();
		TextMaster.removeText(text);
	}

	//spawn zombies
	public static void spawnEnemy() {
		Random random = new Random();
		float x = -0.8f + random.nextFloat() * (-0.8f + 0.6f);
		float y = -0.5f + random.nextFloat() * (0.8f + 0.8f);
		Zombie zombie = new Zombie(new Vector3f(x, y, 1), 0, 0, 0, 0.075f, zombies.size());
		zombie.setClicked(false);
		zombies.add(zombie);
		canSpawn = false;
	}
	
	//process game logic
	public static void updateGame(MousePicker picker) {
		
		if(!moving) {
			for(Image image: images) { 
				if(picker.isLeftButtonDown() && image.hit(picker)) {
					disableImages();
					image.setClicked(true);
				}
			}
		}
		
		for(int i = 0; i < images.size(); i++) {
			if(images.get(i).isClicked()) {
				images.get(i).update(picker);
			}
		}
		
		if(updateRate == 60) {
			if(!goldmines.isEmpty()) {
				for(Zombie zombie: zombies) {
					zombie.die();
				}
			}
			updateRate = 0;
		}
		
		if(picker.isRightButtonDown()) {
			disableImages();
			moving = false;
		}
		
		while (Keyboard.next()) {
			if(Keyboard.getEventKeyState()) {
	            if(Keyboard.getEventKey() == Keyboard.KEY_G) {
					if(gold >= gmCost) {
						disableImages();
	            		Goldmine goldmine = new Goldmine(new Vector3f(0, 0, 1), 0, 0, 0, 0.075f, goldmines.size());
	            		gold -= gmCost;
	            		goldmines.add(goldmine);
	            	}
	            } else if(Keyboard.getEventKey() == Keyboard.KEY_K) {
	            	if(gold >= baCost) {
	            		disableImages();
	            		Barrack barrack = new Barrack(new Vector3f(0, 0, 1), 0, 0, 0, 0.075f, barracks.size());
	            		gold -= baCost;
	            		barracks.add(barrack);
	            	}
	            }
			}
		}
		
		zombies.removeIf((Zombie zombie) -> zombie.dead == true);
		soldiers.removeIf((Soldier soldier) -> soldier.dead == true);
		goldmines.removeIf((Goldmine goldmine) -> goldmine.dead == true);
		images.removeIf((Image image) -> image.dead == true);
		
		updateRate++;
	}
	
	//deavticate objects to avoid multiple objects from being active
	public static void disableImages() {
		for(Image image: images) { image.setClicked(false); }
	}
	
}
