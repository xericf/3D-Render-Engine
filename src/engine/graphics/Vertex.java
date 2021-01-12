package engine.graphics;

import engine.math.*;

public class Vertex {
	private Vec3f position;
	private Vec3f color;
	private Vec2f textureCoord;
	
	
	public Vertex(Vec3f pos) {
		/**
		 * This vertex class only supports points
		 */
		 
		this.position = pos;
		this.color = null;
		this.textureCoord = null;
	}
	
	public Vertex(Vec3f pos, Vec3f col) {
		/**
		 * This vertex class supports colors that correspond with the points
		 */
		this.position = pos;
		this.color=col;
		this.textureCoord = null;
	}
	
	public Vertex(Vec3f pos, Vec3f col, Vec2f textureCoord) {
		/**
		 * This vertex class supports colors that correspond with the points
		 */
		this.position = pos;
		this.color=col;
		this.textureCoord = textureCoord;
		
	}
	
	public Vec3f getColor() {
		return this.color;
	}
	
	public Vec3f getPosition() {
		return this.position;
	}
	
	public Vec2f getTextureCoord() {
		return this.textureCoord;
	}
	
	public void setPosition(Vec3f pos) {
		this.position = pos;
	}
	
	public void setColor(Vec3f col) {
		this.color = col;
	}
	public void setTextureCoord(Vec2f tex) {
		this.textureCoord = tex;
	}
	
	public static float[] getFloats(Vertex[] verts) {
		float[] floats = new float[verts.length*3];
		
		for(int i = 0; i < verts.length; i++) {
			float[] n = verts[i].getPosition().get();
			floats[0 + i*3] = n[0];
			floats[1 + i*3] = n[1];
			floats[2 + i*3] = n[2];
		}
		
		return floats;
		
	}
	
	public static Vec3f[] getColors(Vertex[] vertices) {
		Vec3f vecs[] = new Vec3f[vertices.length];
		for(int i = 0; i < vertices.length; i++) {
			vecs[i] = vertices[i].getColor();
		}
		
		return vecs;
	}
	
	public static Vec3f[] getPositions(Vertex[] vertices) {
		Vec3f vecs[] = new Vec3f[vertices.length];
		for(int i = 0; i < vertices.length; i++) {
			vecs[i] = vertices[i].getPosition();
		}
		
		return vecs;
	}

	public static Vec2f[] getTextureCoords(Vertex[] vertices) {
		Vec2f vecs[] = new Vec2f[vertices.length];
		for(int i = 0; i < vertices.length; i++) {
			vecs[i] = vertices[i].getTextureCoord();
		}
		
		return vecs;
	}


}
