package engine.objects;

import engine.graphics.Mesh;
import engine.graphics.Shader;
import engine.math.Matrix4f;
import engine.math.Vec3f;

public class Background implements GameObject {
	
	private Mesh mesh;
	
	// These will just hold the important properties of the object
	private Vec3f position;
	private Vec3f rotation;
	private Vec3f scale;
	
	private Shader shader;

	public float positionSin;
	
	private float radius = 8f;
	
	private boolean isClicking = false;;
	private float clickX = 0f;
	private float clickY = 0f;
	
	private float screenX = 850f;
	private float screenY = 850f;
	
	public Background(Vec3f position, Vec3f rotation, Vec3f scale, Mesh mesh, Shader shader) {
		
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.mesh = mesh;
		this.shader = shader;
		
		
	}
	
	@Override
	public void update() {
		positionSin +=0.01f;
		
		int fac = positionSin% Math.PI > (Math.PI/2) ? 1 : -1;
		
		float pSin =(float) Math.sin(positionSin);
		
		if(isClicking) {
			
			float x = (clickX/screenX) * 20 -10;
			float y = (clickY/screenY) * 20 -10;
			
			float[] s = scale.get();
			float[] p = position.get();
			
			for(int i = 0 ; i < s.length; i++) {
				if(s[i] < 0.25) {
					s[i] += 0.01f;
				}
			}
			
			if(p[0] > x + 0.02f || p[0] < x - 0.02f) {
				p[0] += (x-p[0])/20;
			}
			if(p[1] > y + 0.02f || p[1] < y - 0.02f) {
				p[1] += (y-p[1])/20;
			}
			
			position.set(p[0], p[1], 0);
			rotation.set((float) pSin*360 , (float) pSin*360 ,(float) pSin *360);
			
			scale.set(s[0], s[1], s[2]);
			
			isClicking = false;
		} else {
			
			float x = (float) pSin*radius;
			float y = fac * (float) Math.sqrt(Math.pow(radius,2) - Math.pow((double) x, 2));
			
			float[] s = scale.get();
			float[] p = position.get();
			
			float sSin = (float) Math.sin(positionSin)/4;
			for(int i = 0 ; i < s.length; i++) {
				s[i] += (sSin-s[i])/20;
			}
			
			if(x > p[0] + 0.02f || x < p[0] - 0.02f) {
				p[0] += (x-p[0])/20;
			}
			if(y > p[1] + 0.02f || y < p[1] - 0.02f) {
				p[1] += (y-p[1])/20;
			}
			
			position.set( p[0], p[1] , 0);
			rotation.set((float) pSin*360 , (float) pSin*360 ,(float) pSin *360);
			scale.set(s[0], s[1], s[2]);
			//scale.set((float) Math.sin(positionSin)/4, (float) Math.sin(positionSin)/4, (float) Math.sin(positionSin)/4);
			
		}


	}
	
	
	public void setClicking(float x, float y) {
		this.clickX = x;
		this.clickY = y;
		this.isClicking = true;
	}
	
	@Override
	public void callUniforms(Matrix4f perspective) {
		
		Matrix4f mat = Matrix4f.Transform(position, rotation, scale);

		shader.setUniform("model", mat);
		shader.setUniform("perspective", perspective);
		//System.out.println(mat.Multiply(perspective).get(0, 0));
	}

	@Override
	public Mesh getMesh() {
		// TODO Auto-generated method stub
		return mesh;
	}

	@Override
	public Vec3f getPosition() {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public Vec3f getRotation() {
		// TODO Auto-generated method stub
		return rotation;
	}

	@Override
	public Vec3f getScale() {
		// TODO Auto-generated method stub
		return scale;
	}

	@Override
	public Shader getShader() {
		// TODO Auto-generated method stub
		return shader;
	}
}
