package engine.math;

public class Vec2f {
	
	private float x,y;
	
	public Vec2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}

	public float[] getXY() {
		return new float[] {x,y};
	}

	public static float[] getFloats(Vec2f[] vectors) {
		/**
		 * @desc - Gets the float array from an array of vec2f's.
		 * */
		
		float[] floats = new float[vectors.length*2]; // as each vector stores 3 floats, make the length that number
		
		for(int i = 0; i < vectors.length; i++) {
			floats[i*2 + 0] = vectors[i].getX();
			floats[i*2 + 1] = vectors[i].getY();

		}
		
		return floats;
	}
}
