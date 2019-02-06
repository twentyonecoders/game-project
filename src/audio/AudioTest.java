package audio;

import java.io.IOException;

import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

public class AudioTest {

	public static void main(String args[]) throws IOException, InterruptedException {
		AudioMaster.init();
		AudioMaster.setListenerData(0, 0, 0);
		
		int buffer = AudioMaster.loadSound("audio/bounce.wav");
		Source source = new Source();
		source.setLooping(true);
		source.setVolume(3);
		source.play(buffer);
		
		float xPos = 0;
		source.setPosition(0, 0, 0);
		
		char c = ' ';
		while(c != 'q') {
			
			xPos -= 0.03f;
			source.setPosition(xPos, 0, 0);
			System.out.println(xPos);
			Thread.sleep(10);
			
		}
		source.delete();
		AudioMaster.cleanUp();
	}

}
