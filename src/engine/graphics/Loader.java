package engine.graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import engine.math.Vec2f;
import engine.math.Vec3f;

public class Loader {
	/**
	 * This is used to load 3d objects into memory by storing VAO objects about the models
	 * */
	
	private static List<Integer> vaos = new ArrayList<Integer>(); // Store the VAO ids so that we could clean it up when we're done
	private static List<Integer> vbos = new ArrayList<Integer>(); // Store the VBO ids so that we could clean it up when we're done
	
	
	static int createVAO() {
		// Creates a VAO and returns the id.
		int vaoID = GL30.glGenVertexArrays(); // create empty VAO and return the id
		
		GL30.glBindVertexArray(vaoID); // bind the VAO, because it needs to be bind before you could modify any states stored in it.
		
		vaos.add(vaoID); // add for memory cleaning
		
		return vaoID;
	}
	
	static int storeIndices(int[] indices) {
		/**
		 * This is used to store the indices that are required for glDrawElements that will be called in @engine.graphics.renderer
		 * Indices are essentially numbering for each vertex so they could be easily reused.
		 * */
		
		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length *3); ;
		indicesBuffer.put(indices).flip();
		
		int ibo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		return ibo;
	}
	
	public static int storeAttributeList(int attributeNumber, Vec3f[] data) {
		/**
		 * @desc - This will create a VBO then store it into the attributeNumber in the VAO.
		 * */
		
		int vbo = GL15.glGenBuffers(); // This will return the ID of a vbo that's been generated
		
		// Must bind the buffer to be able to work with the state of it always.
		// GL15.GL_ARRAY_BUFFER is the type of vbo it is
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		
		// When storing data into the VBO, we must store BUFFERS into it, hence the name Vertex Buffer Object
		FloatBuffer fb = storeFloatBuffer(data);
		
		//GL_STATIC_DRAW means that the buffer will not be edited after.
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, fb, GL15.GL_STATIC_DRAW);
		
		
		// This stores the buffer into one of the attribute list given by attributeNumber, the 3 is the length of each vertex (x,y,z)
		// GL11.GL_FLOAT is just the type of data, false represents if it had been normalized, 0 is if there is a distance between each vertex in
		// the float buffer, and the next 0 is the offset for reading it.
		GL20.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT, false, 0, 0);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); // unbind the vbo
		
		vbos.add(vbo);
		
		return vbo;
		
	}
	
	public static int storeAttributeList(int attributeNumber, Vec2f[] data) {
		/**
		 * @desc - This will create a VBO then store it into the attributeNumber in the VAO.
		 * This will store a vec2f instead of vec3f
		 * */
		
		int vbo = GL15.glGenBuffers(); // This will return the ID of a vbo that's been generated
		
		// Must bind the buffer to be able to work with the state of it always.
		// GL15.GL_ARRAY_BUFFER is the type of vbo it is
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		
		// When storing data into the VBO, we must store BUFFERS into it, hence the name Vertex Buffer Object
		FloatBuffer fb = storeFloatBuffer(data);
		
		//GL_STATIC_DRAW means that the buffer will not be edited after.
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, fb, GL15.GL_STATIC_DRAW);
		
		
		// This stores the buffer into one of the attribute list given by attributeNumber, the 2 is the length of each vertex (x,y)
		// GL11.GL_FLOAT is just the type of data, false represents if it had been normalized, 0 is if there is a distance between each vertex in
		// the float buffer, and the next 0 is the offset for reading it.
		GL20.glVertexAttribPointer(attributeNumber, 2, GL11.GL_FLOAT, false, 0, 0);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); // unbind the vbo
		
		vbos.add(vbo);
		
		return vbo;
	}
	
	public static void overwriteAttributeList(int buffer, Vec3f[] data) {
		/*
		 * This function is used to overwrite the attribute list because if you made a new
		 * buffer every single time with GL15.glGenBuffers, it will gradually increase the
		 * memory usage until there's no memory left.
		 * */
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
		FloatBuffer fb = storeFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, fb, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
		
		
	}
	
	static FloatBuffer storeFloatBuffer(Vec2f[] data) {
		/**
		 * This will store a vec2f float into the memory (Thank goodness methods
		 * with different data types)
		 * */
		FloatBuffer fb = MemoryUtil.memAllocFloat(data.length *2);
		
		float[] coords = Vec2f.getFloats(data);
		
		fb.put(coords); // Store the float array into the buffer
		fb.flip(); // This tells the program we have finished writing to the VBO
		
		return fb;
	}
	

	
	static FloatBuffer storeFloatBuffer(Vec3f[] data) {
		
		// The argument is the size of the float buffer being created, we multiply by 3 because in the Vec3f object we only store 3 floats.
		FloatBuffer fb = MemoryUtil.memAllocFloat(data.length *3); ;
		
		float[] coords = Vec3f.getFloats(data);
		
		fb.put(coords); // Store the float array into the buffer
		fb.flip(); // This tells the program we have finished writing to the the floatbuffer
		
		return fb;
		
	}
	
	
	static void unbindVAO() {
		GL30.glBindVertexArray(0); // this will unbind the VAO by setting it to 0, because no vao has an id of 0
	}
	
	public static void cleanup() {
		/**
		 * This is used to clean up the VAO and VBOs from memory after we're done using it. 
		 * */
		
		for(int i = 0 ; i < vaos.size(); i++) {
			GL30.glDeleteVertexArrays(vaos.get(i));
		}
		for(int i = 0 ; i < vbos.size(); i++) {
			GL15.glDeleteBuffers(vbos.get(i));
		}
	}
}
