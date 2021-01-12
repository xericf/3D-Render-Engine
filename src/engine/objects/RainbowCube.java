package engine.objects;

import engine.graphics.Loader;
import engine.graphics.Mesh;
import engine.graphics.Shader;
import engine.graphics.Vertex;
import engine.math.Matrix4f;
import engine.math.Vec3f;

public class RainbowCube implements GameObject {
	
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
	
	public RainbowCube(Vec3f position, Vec3f rotation, Vec3f scale, Mesh mesh, Shader shader) {
		
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
			float[] r = rotation.get();
			
			for(int i = 0 ; i < s.length; i++) {
				if(s[i] < 0.25) {
					s[i] += 0.01f;
				}
			}
			
			/*
				This will return a float[3] by considering the current rotation xyz coordinates
				And then calculating the new vectors by adding the pixel distance 
				From your cursor to the cube center. Then it will add using a smaller 
				vector to make a smoothing effect.
			 */
			float[] rotAdd = easeIn(r, rotationCalculation(p, x, y));
			
			position.add((x-p[0])/20, (y-p[1])/20, 0); // Add the difference/20 of the cursor and x,y coords
			
			rotation.add(rotAdd[0], rotAdd[1], rotAdd[2]); 
			
			scale.set(s[0], s[1], s[2]);
			
			isClicking = false;
		} else {
			
			float x =  (float) pSin*radius;
			float y =  fac *(float) Math.sqrt(Math.pow(radius,2) - Math.pow((double) x, 2));
			
			float[] s = scale.get();
			float[] p = position.get();
			
			// This will make it so that it will always be a positive scale size
			// So that when you click on it, it sometimes doesn't get smaller then bigger
			float sSin = (float) Math.abs(Math.sin(positionSin)/4);
			
			for(int i = 0 ; i < s.length; i++) {
				s[i] += (sSin-s[i])/20;
			}
			
		
			p[0] += (x-p[0])/20;
			p[1] += (y-p[1])/20;
			
			float[] rot = easeIn(rotation.get(), 
					new float[] {pSin*360,pSin*360,pSin*360});
			
			position.set( p[0], p[1] , 0);
			//rotation.set(pSin*360,pSin*360,pSin*360);
			rotation.add(rot[0], rot[1], rot[2]);
			scale.set(s[0], s[1], s[2]);
			
		}
		
		forwardColor();

	}
	
	private float smoothCos(float n) {
		/**
		 * This will produce a smooth cosine function.
		 * Domain is real number
		 * Range is 0->1
		 * The pattern is for x->infinity, y will go from 0->1->0 and repeat.
		 * */
		float s = n%2;
		return Math.abs(-Math.abs(s)+1);
	}
	
	private void forwardColor() {
		Vertex[] vertices = this.mesh.getVertices();

		Vec3f[] v = new Vec3f[vertices.length];
		for(int i = 0; i < vertices.length; i++) {
			Vec3f col = vertices[i].getColor();
			float vx = col.getX() + 0.01f;
			float vy = col.getY() + 0.01f;
			float vz = col.getZ() + 0.01f;
			
			float x = (float) smoothCos(vx);
			float y = (float) smoothCos(vy);
			float z = (float) smoothCos(vz);
			
			col.set(vx, vy, vz);
			vertices[i].setColor(col);
			
			v[i] = new Vec3f(x,y,z);
		}
		
		this.mesh.setVertices(vertices);
		
		Loader.overwriteAttributeList(this.mesh.getCBO(), v);

	}
	

	private float[] rotationCalculation(float[] pos, float mouseX, float mouseY) {
		
		// since it's rotating ON the y axis, the rotation is determined by the mouseX pos
		float rotY = (mouseX - pos[0]); 
		
		float rotX = -(mouseY - pos[1]);
		float rotZ = rotX + rotY;
		float scale = 16.0f;
		
		return new float[] {rotX *scale, rotY*scale, 1};
	}
	
	public float[] easeIn(float[] src, float[] target) {
		/*
		 * Will simply return a small vector to add by so that the limit
		 * of using this function will equal the target vector
		 * */
		
		
		float x = (target[0] - src[0])/10;
		float y = (target[1] - src[1])/10;
		float z = (target[2] - src[2])/10;
		
		
		return new float[] {x,y,z};
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
