package gameEngine;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import fonts.GUIText;
import fonts.TextMaster;
import guis.GUITexture;
import renderEngine.Loader;

public class Menu {

	GUIText playText;
	GUITexture playButton;
	
	public void init(Loader loader) {
		playText = new GUIText("PLAY", 2f, Main.font, new Vector2f(0, 0.475f), 1, true);
		playText.setColour(255, 255, 0);
		playButton = new GUITexture(loader.loadTexture("Marmor"), new Vector2f(0, 0), new Vector2f(0.15f, 0.1f));
		Main.guiGraphics.add(playButton);
	}
	
	public void update(Loader loader) {
		while(Keyboard.next()) {
			if(Keyboard.getEventKeyState() && Keyboard.getEventKey() == Keyboard.KEY_P) {
				TextMaster.removeText(playText);
				Main.guiGraphics.remove(playButton);
				Main.startGame(loader);
			}
		}
	}
}
