package gameEngine;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import fonts.GUIText;
import fonts.TextMaster;
import guis.GUITexture;
import renderEngine.Loader;

public class Menu {

	static GUIText playText;
	static GUITexture playButton;
	
	public static void showMenu(Loader loader) {
		playText = new GUIText("PLAY", 2f, Main.font, new Vector2f(0, 0.475f), 1, true);
		playText.setColour(255, 255, 0);
		playButton = new GUITexture(loader.loadTexture("Marmor"), new Vector2f(0, 0), new Vector2f(0.15f, 0.1f));
		Main.guiGraphics.add(playButton);
	}
	
	static void hideMenu(Loader loader) {
		if(!Game.initialized) {
			Game.initGame();
		}
		TextMaster.removeText(playText);
		Main.guiGraphics.remove(playButton);
		Game.startGame(loader);
	}
	
	public static void update(Loader loader) {
		while(Keyboard.next()) {
			if(Keyboard.getEventKeyState() && Keyboard.getEventKey() == Keyboard.KEY_P) {
				hideMenu(loader);
			}
		}
	}
}
