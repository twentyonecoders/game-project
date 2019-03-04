package gameEngine;

import org.lwjgl.util.vector.Vector2f;

import fonts.GUIText;
import fonts.TextMaster;
import guis.GUITexture;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import toolBox.MousePicker;

public class Menu {

	static GUIText playText, exitText;
	static GUITexture playButton, exitButton;
	
	public static void showMenu(Loader loader) {
		playText = new GUIText("PLAY", 2f, Main.font, new Vector2f(0f, 0.37f), 1, true);
		playText.setColour(255, 255, 0);
		playButton = new GUITexture(loader.loadTexture("Marmor"), new Vector2f(0f, 0.2f), new Vector2f(0.15f, 0.1f));
		Main.guiGraphics.add(playButton);

		exitText = new GUIText("EXIT", 2f, Main.font, new Vector2f(0f, 0.57f), 1, true);
		exitText.setColour(255, 255, 0);
		exitButton = new GUITexture(loader.loadTexture("Marmor"), new Vector2f(0f, -0.2f), new Vector2f(0.15f, 0.1f));
		Main.guiGraphics.add(exitButton);
	}
	
	static void hideMenu(Loader loader) {
		if(!Game.initialized) {
			Game.initGame();
		}
		TextMaster.removeText(playText);
		TextMaster.removeText(exitText);
		Main.guiGraphics.remove(playButton);
		Main.guiGraphics.remove(exitButton);
		Game.startGame(loader);
	}
	
	public static void update(Loader loader, MousePicker picker) {
		if(picker.isLeftButtonDown()) {
			if(playButton.hit(picker)) { hideMenu(loader); }
			else if(exitButton.hit(picker)) { DisplayManager.open = false; }
		}
	}
}
