package engine.math;

public class Vec3f {
	private float x,y,z;
	
	/*
	 * Vector with 3 floats used for storing x,y,z coordinates
	 * */
	public Vec3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public float[] get() {
		return new float[] {x,y,z};
	}
	
	public float getX() {
		return this.x;
	}
	public float getY() {
		return this.y;
	}
	public float getZ() {
		return this.z;
	}
	
	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}
	public void setZ(float z) {
		this.z = z;
	}
	
	public void set(float x, float y, float z) {
		this.x = x; 
		this.y = y;
		this.z = z;
	}
	
	public void add(float x, float y, float z) {
		this.x +=x;
		this.y +=y;
		this.z +=z;
	}
	
	public void add(Vec3f a) {
		float[] vec = a.get();
		this.x += vec[0];
		this.y += vec[1];
		this.z += vec[2];
		
	}
	
	public static float[] getFloats(Vec3f[] vectors) {
		
		/**
		 * @desc - Gets the float array from an array of vectors.
		 * */
		
		float[] floats = new float[vectors.length*3]; // as each vector stores 3 floats, make the length that number
		
		for(int i = 0; i < vectors.length; i++) {
			floats[i*3 + 0] = vectors[i].x;
			floats[i*3 + 1] = vectors[i].y;
			floats[i*3 + 2] = vectors[i].z;
		}
		
		return floats;
	}

}
