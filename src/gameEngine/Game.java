package gameEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import audio.AudioMaster;
import audio.Source;
import entities.Barrack;
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
import renderEngine.Loader;
import toolBox.MousePicker;

public class Game {

	public static int gold = 1500;
	public static int wood = 500;
	public static int food = 100;
	
	public static List<Goldmine> goldmines = new ArrayList<Goldmine>();
	public static List<Barrack> barracks = new ArrayList<Barrack>();
	public static List<Farm> farms = new ArrayList<Farm>();
	public static List<Lumberjack> lumberjacks = new ArrayList<Lumberjack>();
	public static List<Soldier> soldiers = new ArrayList<Soldier>();
	public static List<Zombie> zombies = new ArrayList<Zombie>();
	static List<GUIText> texts = new ArrayList<GUIText>();
	
	public static boolean moving = false;
	static boolean canSpawn = false;
	static boolean canMove = false;
	static boolean canBuySoldiers = false;
	static boolean initialized = false;
	
	static Timer timer =  new Timer();
	
	static Source source = new Source();
	static int backgroundBuffer = AudioMaster.loadSound("audio/RoyaleClash.wav");
	
	static TimerTask spawnTask, updateTask;
	
	//initialize game
	public static void initGame() {
		source.setLooping(true);
		AudioMaster.sources.add(source);
		source.play(backgroundBuffer);
		
		//start timer for zombie spawning, background music, GUI and game loop
		spawnTask = new TimerTask() {
			public void run() {
				canSpawn = true;
			}
		};
		updateTask = new TimerTask() {
			public void run() {
				canMove = true;
			}
		};
	}
	
	//start game
	public static void startGame(Loader loader) {
		for(Image image: Main.images) { Main.guiGraphics.add(image.health); }
		source.continuePlaying();
		timer.scheduleAtFixedRate(spawnTask, 0 * 1000, 10 * 1000);
		timer.scheduleAtFixedRate(updateTask, 0, 1 * 1000);
		setUpGUI(loader, Main.guiGraphics);
		Main.running = true;
	}
	
	//pause game
	public static void pauseGame(Loader loader) {
		for(GUIText text: texts) {
			TextMaster.removeText(text);
		}
		for(GUITexture guitexture: Main.guiGraphics) {
			guitexture.dead = true;
		}
		Main.guiGraphics.removeIf((GUITexture texture) -> texture.dead == true);
		source.pause();
		timer.purge();
		Main.running = false;
		Menu.showMenu(loader);
	}

	//process game logic
	public static void updateGame(MousePicker picker, Loader loader) {
		//when no object is moving, detect if object is clicked
		if(!moving) {
			for(Image image: Main.images) {
				if(picker.isLeftButtonDown() && image.hit(picker) && image.type != 3) {
					disableImages();
					image.setClicked(true);
				}
			}
		}
		
		//execute clicked object update function
		for(Image image: Main.images) { if(image.isClicked()) { image.run(picker); } }
		
		//update healthbars and soldier images
		for(Soldier soldier: soldiers) { soldier.update(); }
		for(Zombie zombie: zombies) { zombie.update(); }
		//for(Goldmine goldmine: goldmines) { goldmine.update(); }
		for(Image image: Main.images) { image.update(); }
		
		//update zombies
		if(canSpawn) { spawnEnemy(2); }
		if(canMove) { 
			for(Zombie zombie: zombies) { zombie.move(); }
			canMove = false;
		}
		
		//user input management
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
	            		soldierGUI(loader, Main.guiGraphics);
	            		canBuySoldiers = true;
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
	            } else if(Keyboard.getEventKey() == Keyboard.KEY_1) {
	            	if(canBuySoldiers && gold >= Soldier.goldCost && food >= Soldier.foodCost) {
	            		disableImages();
	            		Soldier soldier = new Soldier(new Vector3f(0, 0, 1), 0, 0, 0, 0.075f, Game.soldiers.size());
	            		soldiers.add(soldier);
	            	}
	            } else if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
	            	pauseGame(loader);
	            }
			}
		}
		
		//remove destroyed objects
		zombies.removeIf((Zombie zombie) -> zombie.dead == true);
		soldiers.removeIf((Soldier soldier) -> soldier.dead == true);
		goldmines.removeIf((Goldmine goldmine) -> goldmine.dead == true);
		Main.images.removeIf((Image image) -> image.dead == true);
	}
	
	//initialize Building GUI
	public static void setUpGUI(Loader loader, List<GUITexture> guiGraphics) {
		//Goldmine GUI
		GUITexture guiGoldMineBack = new GUITexture(loader.loadTexture("Marmor"), new Vector2f(-0.8f, -0.75f), new Vector2f(0.15f, 0.24f));
		GUITexture guiGoldMine = new GUITexture(loader.loadTexture("GoldMine"), new Vector2f(-0.8f, -0.75f), new Vector2f(0.07f, 0.112f));
		guiGraphics.add(guiGoldMineBack);
		guiGraphics.add(guiGoldMine);
		GUIText goldMineCostText = new GUIText("Cost: " + Goldmine.goldCost + " Gold, " + Goldmine.woodCost + " Wood", 1f, Main.font, new Vector2f(0.03f, 0.95f), 0.14f, true);
		goldMineCostText.setColour(255, 255, 0);
		GUIText goldMineText = new GUIText("Press 'G'", 1.5f, Main.font, new Vector2f(0.06f, 0.774f), 0.08f, true);
		goldMineText.setColour(255, 255, 0);
		
		//Barrack GUI;
		GUITexture guiBarrackBack = new GUITexture(loader.loadTexture("Marmor"), new Vector2f(-0.45f, -0.75f), new Vector2f(0.15f, 0.24f));
		GUITexture guiBarrack = new GUITexture(loader.loadTexture("Kaserne"), new Vector2f(-0.45f, -0.75f), new Vector2f(0.07f, 0.112f));
		guiGraphics.add(guiBarrackBack);
		guiGraphics.add(guiBarrack);
		GUIText barrackCostText = new GUIText("Cost : " + Barrack.goldCost + " Gold, " + Barrack.woodCost + " Wood", 1f, Main.font, new Vector2f(0.205f, 0.95f), 0.14f, true);
		barrackCostText.setColour(255, 255, 0);
		GUIText barrackText = new GUIText("Press 'K'", 1.5f, Main.font, new Vector2f(0.24f, 0.774f), 0.08f, true);
		barrackText.setColour(255, 255, 0);
		
		//Farm GUI
		GUITexture guiFarmBack = new GUITexture(loader.loadTexture("Marmor"), new Vector2f(-0.1f, -0.75f), new Vector2f(0.15f, 0.24f));
		GUITexture guiFarm = new GUITexture(loader.loadTexture("Farm"), new Vector2f(-0.1f, -0.75f), new Vector2f(0.07f, 0.112f));
		guiGraphics.add(guiFarmBack);
		guiGraphics.add(guiFarm);
		GUIText farmCostText = new GUIText("Cost : " + Farm.goldCost + " Gold, " + Farm.woodCost + " Wood", 1f, Main.font, new Vector2f(0.38f, 0.95f), 0.14f, true);
		farmCostText.setColour(255, 255, 0);
		GUIText farmText = new GUIText("Press 'F'", 1.5f, Main.font, new Vector2f(0.42f, 0.774f), 0.08f, true);
		farmText.setColour(255, 255, 0);
		
		//Lumerjack GUI
		GUITexture guiWoodBack = new GUITexture(loader.loadTexture("Marmor"), new Vector2f(0.25f, -0.75f), new Vector2f(0.15f, 0.24f));
		GUITexture guiWood = new GUITexture(loader.loadTexture("Holzfäller"), new Vector2f(0.25f, -0.75f), new Vector2f(0.07f, 0.112f));
		guiGraphics.add(guiWoodBack);
		guiGraphics.add(guiWood);
		GUIText woodCostText = new GUIText("Cost : " + Lumberjack.goldCost + " Gold, " + Lumberjack.woodCost + " Wood", 1f, Main.font, new Vector2f(0.555f, 0.95f), 0.14f, true);
		woodCostText.setColour(255, 255, 0);
		GUIText woodText = new GUIText("Press 'L'", 1.5f, Main.font, new Vector2f(0.6f, 0.774f), 0.08f, true);
		woodText.setColour(255, 255, 0);
		
		texts.add(goldMineCostText);
		texts.add(farmCostText);
		texts.add(barrackCostText);
		texts.add(woodCostText);
		texts.add(woodText);
		texts.add(barrackText);
		texts.add(goldMineText);
		texts.add(farmText);
	}
	
	//initialize Soldier GUI
	public static void soldierGUI(Loader loader, List<GUITexture> guiGrahpics) {
		GUITexture guiSoldierBack = new GUITexture(loader.loadTexture("Marmor"), new Vector2f(0.9f, 0.8f), new Vector2f(0.075f, 0.12f));
		GUITexture guiSoldier = new GUITexture(loader.loadTexture("Soldat"), new Vector2f(0.9f, 0.8f), new Vector2f(0.035f, 0.056f));
		guiGrahpics.add(guiSoldierBack);
		guiGrahpics.add(guiSoldier);
		GUIText soldierCostText = new GUIText(Soldier.goldCost + " Gold, " + Soldier.foodCost + " Food", 0.75f, Main.font, new Vector2f(0.88f, 0.13f), 0.14f, true);
		soldierCostText.setColour(255, 255, 0);
		GUIText soldierText = new GUIText("Press '1'", 1f, Main.font, new Vector2f(0.91f, 0.037f), 0.08f, true);
		soldierText.setColour(255, 255, 0);
		
		texts.add(soldierText);
		texts.add(soldierCostText);
	}
	
	//render GUI
	public static void renderGUI(FontType font, GUIRenderer guiRenderer, List<GUITexture> guiGraphics) {
		GUIText goldText = new GUIText("Gold : " + gold, 2, font, new Vector2f(0.01f, 0.01f), 1f, false);
		goldText.setColour(255, 255, 0);
		GUIText foodText = new GUIText("Food : " + food, 2, font, new Vector2f(0.01f, 0.06f), 1f, false);
		foodText.setColour(255, 255, 0);
		GUIText woodText = new GUIText("Wood : " + wood, 2, font, new Vector2f(0.01f, 0.11f), 1f, false);
		woodText.setColour(200, 200, 200);
		guiRenderer.render(guiGraphics);
		TextMaster.render();
		TextMaster.removeText(goldText);
		TextMaster.removeText(foodText);
		TextMaster.removeText(woodText);
	}

	//deavticate objects to avoid multiple objects from being active
	public static void disableImages() { 
		for(Image image: Main.images) { image.setClicked(false); } 
	}
	
	//spawn zombies
	private static void spawnEnemy(int amount) {
		for(int i = 0; i < amount; i++) {
			Random random = new Random();
			float x = -0.8f + random.nextFloat() * (-0.8f + 0.6f);
			float y = -0.5f + random.nextFloat() * (0.8f + 0.8f);
			Zombie zombie = new Zombie(new Vector3f(x, y, 1), 0, 0, 0, 0.075f, zombies.size());
			zombie.setClicked(false);
			zombies.add(zombie);
		}
		canSpawn = false;
	}
		
}
