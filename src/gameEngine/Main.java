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
import entities.Farm;
import entities.Goldmine;
import entities.Image;
import entities.Lumberjack;
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
	
	public static int gold = 150;
	public static int wood = 50;
	public static int food = 0;

	static int updateRate = 0;
	
	public static boolean moving = false;
	static boolean canSpawn = false;
	
	public static List<Image> images = new ArrayList<Image>();
	public static List<Goldmine> goldmines = new ArrayList<Goldmine>();
	public static List<Barrack> barracks = new ArrayList<Barrack>();
	public static List<Farm> farms = new ArrayList<Farm>();
	public static List<Lumberjack> lumberjacks = new ArrayList<Lumberjack>();
	public static List<Soldier> soldiers = new ArrayList<Soldier>();
	public static List<Zombie> zombies = new ArrayList<Zombie>();
	
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
		Source source = new Source();
		
		//initialize sound
		int backgroundBuffer = AudioMaster.loadSound("audio/RoyaleClash.wav");
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
		TimerTask spawnTask = new TimerTask() {
			public void run() {
				canSpawn = true;
			}
		};
		timer.scheduleAtFixedRate(spawnTask, 5000, 10 * 1000);
		
		//source.play(backgroundBuffer);
		
		//display update loop
		while(!Display.isCloseRequested()) {
			camera.move(picker);
			picker.update();
			updateGame(picker, source);
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
		//Goldmine GUI
		GUITexture guiGoldMineBack = new GUITexture(loader.loadTexture("Marmor"), new Vector2f(-0.8f, -0.75f), new Vector2f(0.15f, 0.24f));
		GUITexture guiGoldMine = new GUITexture(loader.loadTexture("GoldMine"), new Vector2f(-0.8f, -0.75f), new Vector2f(0.07f, 0.112f));
		guis.add(guiGoldMineBack);
		guis.add(guiGoldMine);
		GUIText goldMineCostText = new GUIText("Cost: " + Goldmine.goldCost + " Gold, " + Goldmine.woodCost + " Wood", 1f, font, new Vector2f(0.03f, 0.95f), 0.14f, true);
		goldMineCostText.setColour(255, 255, 0);
		GUIText goldMineText = new GUIText("Press 'G'", 1.5f, font, new Vector2f(0.06f, 0.774f), 0.08f, true);
		goldMineText.setColour(255, 255, 0);
		
		//Barrack GUI;
		GUITexture guiBarrackBack = new GUITexture(loader.loadTexture("Marmor"), new Vector2f(-0.45f, -0.75f), new Vector2f(0.15f, 0.24f));
		GUITexture guiBarrack = new GUITexture(loader.loadTexture("Kaserne"), new Vector2f(-0.45f, -0.75f), new Vector2f(0.07f, 0.112f));
		guis.add(guiBarrackBack);
		guis.add(guiBarrack);
		GUIText barrackCostText = new GUIText("Cost : " + Barrack.goldCost + " Gold, " + Barrack.woodCost + " Wood", 1f, font, new Vector2f(0.2f, 0.95f), 0.14f, true);
		barrackCostText.setColour(255, 255, 0);
		GUIText barrackText = new GUIText("Press 'K'", 1.5f, font, new Vector2f(0.24f, 0.774f), 0.08f, true);
		barrackText.setColour(255, 255, 0);
		
		//Farm GUI
		GUITexture guiFarmBack = new GUITexture(loader.loadTexture("Marmor"), new Vector2f(-0.1f, -0.75f), new Vector2f(0.15f, 0.24f));
		GUITexture guiFarm = new GUITexture(loader.loadTexture("Farm"), new Vector2f(-0.1f, -0.75f), new Vector2f(0.07f, 0.112f));
		guis.add(guiFarmBack);
		guis.add(guiFarm);
		GUIText farmCostText = new GUIText("Cost : " + Farm.goldCost + " Gold, " + Farm.woodCost + " Wood", 1f, font, new Vector2f(0.37f, 0.95f), 0.14f, true);
		farmCostText.setColour(255, 255, 0);
		GUIText farmText = new GUIText("Press 'F'", 1.5f, font, new Vector2f(0.42f, 0.774f), 0.08f, true);
		farmText.setColour(255, 255, 0);
		
		//Lumerjack GUI
		GUITexture guiWoodBack = new GUITexture(loader.loadTexture("Marmor"), new Vector2f(0.25f, -0.75f), new Vector2f(0.15f, 0.24f));
		GUITexture guiWood = new GUITexture(loader.loadTexture("Holzfäller"), new Vector2f(0.25f, -0.75f), new Vector2f(0.07f, 0.112f));
		guis.add(guiWoodBack);
		guis.add(guiWood);
		GUIText woodCostText = new GUIText("Cost : " + Lumberjack.goldCost + " Gold, " + Lumberjack.woodCost + " Wood", 1f, font, new Vector2f(0.54f, 0.95f), 0.14f, true);
		woodCostText.setColour(255, 255, 0);
		GUIText woodText = new GUIText("Press 'L'", 1.5f, font, new Vector2f(0.6f, 0.774f), 0.08f, true);
		woodText.setColour(255, 255, 0);
	}
	
	//render GUI
	public static void renderGUI(FontType font, GUIRenderer guiRenderer, List<GUITexture> guiGraphics) {
		GUIText goldText = new GUIText("Gold : " + gold, 2, font, new Vector2f(0.01f, 0.01f), 1f, false);
		goldText.setColour(255, 255, 0);
		GUIText foodText = new GUIText("Bread : " + food, 2, font, new Vector2f(0.01f, 0.06f), 1f, false);
		foodText.setColour(255, 255, 0);
		GUIText woodText = new GUIText("Wood : " + wood, 2, font, new Vector2f(0.01f, 0.11f), 1f, false);
		woodText.setColour(200, 200, 200);
		guiRenderer.render(guiGraphics);
		TextMaster.render();
		TextMaster.removeText(goldText);
		TextMaster.removeText(foodText);
		TextMaster.removeText(woodText);
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
	public static void updateGame(MousePicker picker, Source source) {
		
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
		for(Soldier soldier: soldiers) { soldier.updateImage(); }
		
		if(updateRate == 60) {
			if(!goldmines.isEmpty()) {
				for(Zombie zombie: zombies) {
					zombie.update();
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
					if(gold >= Goldmine.goldCost && wood >= Goldmine.woodCost) {
						disableImages();
	            		Goldmine goldmine = new Goldmine(new Vector3f(0, 0, 1), 0, 0, 0, 0.075f, goldmines.size());
	            		goldmines.add(goldmine);
	            	}
	            } else if(Keyboard.getEventKey() == Keyboard.KEY_K) {
	            	if(gold >= Barrack.goldCost && wood >= Barrack.woodCost) {
	            		disableImages();
	            		Barrack barrack = new Barrack(new Vector3f(0, 0, 1), 0, 0, 0, 0.075f, barracks.size());
	            		barracks.add(barrack);
	            	}
	            } else if(Keyboard.getEventKey() == Keyboard.KEY_F) {
	            	if(gold >= Farm.goldCost && wood >= Farm.woodCost) {
	            		disableImages();
	            		Farm farm = new Farm(new Vector3f(0, 0, 1), 0, 0, 0, 0.075f, farms.size());
	            		farms.add(farm);
	            	}
	            } else if(Keyboard.getEventKey() == Keyboard.KEY_L) {
	            	if(gold >= Lumberjack.goldCost && wood >= Lumberjack.woodCost) {
	            		disableImages();
	            		Lumberjack lumberjack = new Lumberjack(new Vector3f(0, 0, 1), 0, 0, 0, 0.075f, lumberjacks.size());
	            		lumberjacks.add(lumberjack);
	            	}
	            } else if(Keyboard.getEventKey() == Keyboard.KEY_P) {
	            	if(source.isPlaying()) { source.pause();
	            	} else { source.continuePlaying(); }
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
