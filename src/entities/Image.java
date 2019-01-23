package entities;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.Loader;
import textures.ModelTexture;

public class Image extends Entity{

	static Loader loader = new Loader();
	
	private static float[] vertices = {
		    -1f, 1f, 0f,   //v0
		    -1f, -1f, 0f,  //v1
		    1f, -1f, 0f,   //v2
		    1f, 1f, 0f     //v3
		  };
	
	private static int[] indices = {
			0, 1, 3,
			3, 1, 2
	};
	
	private static float[] textureCoords = {
			0, 0,
			0, 1,
			1, 1,
			1, 0
	};
	
	public Image (String fileName, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(new TexturedModel(loader.loadToVAO(vertices, textureCoords, indices), new ModelTexture(loader.loadTexture(fileName))), position, rotX, rotY, rotZ, scale);
	}

}
