package shaders;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import toolBox.Maths;

public class StaticShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";
	
	private int location_transformationMatrix;
	private int location_viewMatrix;
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}

	@Override
	protected void GetAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
	}

	public void loadTransformationMatrix(Matrix4f transformation) {
		super.loadMatrix(location_transformationMatrix, transformation);
	}
	
	public void loadViewMatrix(Camera camera) {
		Matrix4f view = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, view);
	}
}
