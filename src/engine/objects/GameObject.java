package engine.objects;

import engine.graphics.Mesh;
import engine.graphics.Shader;
import engine.math.Matrix4f;
import engine.math.Vec3f;

public interface GameObject {

	public void update();
	
	public Mesh getMesh();
	
	public Shader getShader();
	
	public Vec3f getPosition();
	
	public Vec3f getRotation();
	
	public Vec3f getScale();

	public void callUniforms(Matrix4f perspective);

}
