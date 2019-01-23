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
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;
import toolBox.MousePicker;

public class Main {

	public static int gold = 100;
	
	static List<Entity> entities = new ArrayList<Entity>();
	static List<GUITexture> guiGraphics = new ArrayList<GUITexture>();
	
	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);
		Camera camera = new Camera();
		MousePicker picker = new MousePicker(renderer.getProjectionMatrix(), camera);
		GUIRenderer guiRenderer = new GUIRenderer(loader);
		TextMaster.init(loader);
		FontType font = new FontType(loader.loadTexture("comicsans"), new File("res/comicsans.fnt"));
		
		entities.clear();
		guiGraphics.clear();
		setUpGraphics(shader, renderer, entities);
		setUpGUI(loader, guiGraphics, font);
		
		while(!Display.isCloseRequested()) {
			camera.move();
			picker.update();
			renderer.prepare();
			shader.start();
			shader.loadViewMatrix(camera);
			
			for(Entity entity: entities) {
				renderer.render(entity, shader);
			}
			
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
	
	//initialize all used objects
	public static void setUpGraphics(StaticShader shader, Renderer renderer, List<Entity> entities) {
		Entity background = new Image("grass2", new Vector3f(0, 0, -11), 0, 0, 0, 15);
		entities.add(background);
	}
	
	//initialize GUI
	public static void setUpGUI(Loader loader, List<GUITexture> guis, FontType font) {
		GUITexture guiGoldMine = new GUITexture(loader.loadTexture("GoldMine"), new Vector2f(-0.8f, -0.7f), new Vector2f(0.07f, 0.14f));
		GUITexture guiKaserne = new GUITexture(loader.loadTexture("Kaserne"), new Vector2f(-0.5f, -0.7f), new Vector2f(0.07f, 0.14f));
		guis.add(guiGoldMine);
		guis.add(guiKaserne);
		GUIText goldMineText = new GUIText("Press 'G'", 2.5f, font, new Vector2f(0.03f, 0.9f), 1f, false);
		GUIText kaserneText = new GUIText("Press 'K'", 2.5f, font, new Vector2f(0.2f, 0.9f), 1f, false);
	}
	
	//render GUI
	public static void renderGUI(FontType font, GUIRenderer guiRenderer, List<GUITexture> guiGraphics) {
		GUIText text = new GUIText("Gold : " + gold, 3, font, new Vector2f(0.01f, 0.01f), 1f, false);
		guiRenderer.render(guiGraphics);
		TextMaster.render();
		TextMaster.removeText(text);
	}
	
	//process game logic
	public static void updateGame(MousePicker mousePicker) {
		if(mousePicker.isLeftButtonDown() == false) {
			for (Entity entity: entities){
				if(entity instanceof GoldMine) {
					//entities.get(1).setPosition(new Vector3f(mousePicker.getCurrentRay().x * 11.5f, mousePicker.getCurrentRay().y * 11.5f, -10f));
				}
			}
		}
		if(mousePicker.isLeftButtonDown() == true) {
			System.out.println(mousePicker.getCurrentRay());
			for (Entity entity: entities){
				if(entity instanceof GoldMine) {
					entities.get(1).setPosition(new Vector3f(mousePicker.getCurrentRay().x * 11.5f, mousePicker.getCurrentRay().y * 11.5f, -10f));
					
				}
			}
		}
		
		while (Keyboard.next()) {
	        if (Keyboard.getEventKeyState()) {
	            if(Keyboard.getEventKey() == Keyboard.KEY_1) {
	            	for (Entity entity: entities){
	    				if(entity instanceof GoldMine) {
	    					GoldMine goldmine = (GoldMine) entities.get(1);
	    					goldmine.takeGold();
	    				}
	    			}
	            }
	            if(Keyboard.getEventKey() == Keyboard.KEY_U) {
	            	for (Entity entity: entities){
	    				if(entity instanceof GoldMine) {
	    					GoldMine goldmine = (GoldMine) entities.get(1);
	    					goldmine.upgrade();
	    				}
	    			}
	            }
	            if(Keyboard.getEventKey() == Keyboard.KEY_G) {
	            	GoldMine goldMine1 = new GoldMine(new Vector3f(0, 0, -10), 0, 0, 0, 0.5f, 1);
	            	goldMine1.generateGold();
	            	entities.add(goldMine1);
	            	goldMine1.setLocationSet(false);
	            }
	        } else {
	        
	        }
		}
	}
	
}
