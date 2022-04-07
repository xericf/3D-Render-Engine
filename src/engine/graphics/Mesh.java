package engine.graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import engine.math.Vec2f;
import engine.math.Vec3f;
import static engine.graphics.Loader.*;

public class Mesh {
	/**
	 * This is the representation of a 3d object.
	 * */
	
	private int vaoID; // Must know the VAO ID so that it could be referenced in memory
	private int iboID;
	private int cboID;
	private int pboID;
	private int tboID;
	
	private int[] indices;
	
	private Vertex[] vertices;
	
	private Texture texture;
	
	public Mesh(Vertex[] vertices, Texture texture, int[] indices) {
		
		
		this.vertices = vertices;
		this.indices = indices;
		this.texture = texture;
		
	}
	
	
	
	
	public void create() {
		vaoID = createVAO();
		
		pboID = storeAttributeList(0, Vertex.getPositions(this.vertices)); // will store the positions in the first index
		
		cboID = storeAttributeList(1, Vertex.getColors(this.vertices)); // will store the colors in the vao as the 2nd index
		
		tboID = storeAttributeList(2, Vertex.getTextureCoords(this.vertices));
		
		iboID = storeIndices(this.indices);
		
		
		unbindVAO();
		
	}




	public Texture getTextureObject() {
		/**
		 * @desc- This method will get the Texture object
		 * */
		
		return this.texture;
	}
	
	public int getTexture() {
		/**
		 * @desc - This method will get the opengl texture address as opposed to the texture object
		 * */
		return this.texture.getTexture();
	}
	
	public int getVAO() {
		return vaoID;
	}
	
	public int getPBO() {
		return this.pboID;
	}

	public int getIBO() {
		return this.iboID;
	}
	
	public int getCBO() {
		return this.cboID;
	}
	
	public int getTBO() {
		return this.tboID;
	}
	
	public int[] getIndices() {
		return this.indices;
	}
}
