package gameEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import audio.AudioMaster;
import entities.Camera;
import entities.Entity;
import entities.Image;
import fonts.FontType;
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
	public static List<GUITexture> guiGraphics = new ArrayList<GUITexture>();
	
	public static List<Image> images = new ArrayList<Image>();
	
	static boolean running = false;
	
	public static void main(String[] args) {
		
		/*-----------------------------------------------------------------
								initialization
		-----------------------------------------------------------------*/
		
		//initialize components
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer();
		Camera camera = new Camera();
		MousePicker picker = new MousePicker(camera);
		GUIRenderer guiRenderer = new GUIRenderer(loader);
		
		//initialize text
		TextMaster.init(loader);
		font = new FontType(loader.loadTexture("comicsans"), new File("res/comicsans.fnt"));
		
		//initialize sound
		AudioMaster.init();
		AudioMaster.setListenerData(0, 0, 0);

		//preparation
		images.clear();
		guiGraphics.clear();
		Entity background = new Entity(new TexturedModel(loader.loadToVAO(Image.vertices, Image.textureCoords, Image.indices), 
				new ModelTexture(loader.loadTexture("Background"))), new Vector3f(0, 0, 1), 0, 0, 0, 1);
		
		//initialize menu
		Menu.showMenu(loader);
		
		/*-----------------------------------------------------------------
							display update loop
		-----------------------------------------------------------------*/
		
		while(DisplayManager.open || !Display.isCloseRequested()) {
			picker.update();
			renderer.prepare();
			shader.start();
			shader.loadViewMatrix(camera);
			renderer.render(background, shader);
			
			
			if(!running) {
				Menu.update(loader, picker);
				guiRenderer.render(guiGraphics);
				TextMaster.render();
			} else if(running) {
				camera.move(picker);
				Game.updateGame(picker, loader);
				
				for(Image image: images) { renderer.render(image, shader); }
				Game.renderGUI(font, guiRenderer, guiGraphics);
			}
			
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

}
