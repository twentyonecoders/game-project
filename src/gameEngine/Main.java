package gameEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Image;
import fonts.FontType;
import fonts.GUIText;
import fonts.TextMaster;
import guis.GUIRenderer;
import guis.GUITexture;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;
import toolBox.MousePicker;

public class Main {

	public static FontType font;
	
	public static int gold = 100;
	static int gmCost = 50;
	static int baCost = 80;
	
	public static List<Image> images = new ArrayList<Image>();
	static List<Goldmine> goldmines = new ArrayList<Goldmine>();
	static List<Barrack> kasernen = new ArrayList<Barrack>();
	static List<Soldier> soldiers = new ArrayList<Soldier>();
	static List<GUITexture> guiGraphics = new ArrayList<GUITexture>();
	
	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer();
		Camera camera = new Camera();
		MousePicker picker = new MousePicker(camera);
		GUIRenderer guiRenderer = new GUIRenderer(loader);
		TextMaster.init(loader);
		font = new FontType(loader.loadTexture("comicsans"), new File("res/comicsans.fnt"));
		
		images.clear();
		guiGraphics.clear();
		setUpGUI(loader, guiGraphics, font);
		Entity background = new Entity(new TexturedModel(loader.loadToVAO(Image.vertices, Image.textureCoords, Image.indices), 
				new ModelTexture(loader.loadTexture("grass"))), new Vector3f(0, 0, 1), 0, 0, 0, 1);
		
		while(!Display.isCloseRequested()) {
			camera.move(picker);
			picker.update();
			renderer.prepare();
			shader.start();
			shader.loadViewMatrix(camera);
			
			renderer.render(background, shader);
			for(Image image: images) { renderer.render(image, shader); }
			
			renderGUI(font, guiRenderer, guiGraphics);
			updateGame(picker);
			
			shader.stop();
			DisplayManager.updateDisplay();
		}
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
	
	//process game logic
	public static void updateGame(MousePicker picker) {
		
		for(Goldmine goldmine: goldmines) { goldmine.update(picker); }
		for(Barrack barrack: kasernen) { barrack.update(picker); }
		for(Soldier soldier: soldiers) { soldier.update(picker); }
		
		while (Keyboard.next()) {
			if(Keyboard.getEventKeyState()) {
	            if(Keyboard.getEventKey() == Keyboard.KEY_G) {
					if(gold >= gmCost) {
	            		Goldmine goldmine = new Goldmine(new Vector3f(0, 0, 1), 0, 0, 0, 0.125f, goldmines.size() + 1);
	            		gold -= gmCost;
	            		goldmines.add(goldmine);
	            	}
	            } else if(Keyboard.getEventKey() == Keyboard.KEY_K) {
	            	if(gold >= baCost) {
	            		Barrack barrack = new Barrack(new Vector3f(0, 0, 1), 0, 0, 0, 0.125f, kasernen.size() + 1);
	            		gold -= baCost;
	            		kasernen.add(barrack);
	            	}
	            }
	        } else {
	        
	        }
		}
	}
	
	//deavticate objects to avoid multiple objects from being active
	public static void disableImages() {
		for (Image image: images) { image.setClicked(false); }
	}
	
}
