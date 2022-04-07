package engine.graphics;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;

import engine.math.Matrix4f;
import engine.math.Vec2f;
import engine.math.Vec3f;
import engine.utils.FileUtils;

public class Shader {

	private int program;
	private int vertID, fragID;
	private String vertPath, fragPath;
	
	public Shader(String vertPath, String fragPath) {
		this.vertPath = vertPath;
		this.fragPath = fragPath;
		load(vertPath, fragPath);
	}
	
	public void load(String vertPath, String fragPath) {
		/**
		 * This will load both the vert and frag files as a string in memory,
		 * then it will call the create method to compile the shaders
		 * */
		
		String vf = FileUtils.loadAsString(vertPath); // vertfile
		String ff = FileUtils.loadAsString(fragPath); // fragfile
		
		create(vf, ff);
	}
	
	public void create(String vert, String frag) {
		/**
		 * This method will create a program and create vertex and fragment shaders
		 * Then it will compile the shaders and attach them to the program.
		 * @param vert - vertex file in string format
		 * @param frag - fragment file in string format
		 * */
		
		// Program which we will call with (GL20.glUseProgram(program)) when we want to use the shaders
		this.program = GL20.glCreateProgram(); 
		
		this.vertID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		this.fragID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		
		GL20.glShaderSource(this.vertID, vert); // Create vertex shader
		GL20.glShaderSource(this.fragID, frag); // create fragment shader

		GL20.glCompileShader(vertID); // Compile vertex shader
		
		if(GL20.glGetShaderi(this.vertID, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE) {
			// This is the error checking for shaders, it will check if it compiled correctly
			System.err.println("FAILED TO COMPILE VERTEX SHADER");
			System.err.println(GL20.glGetShaderInfoLog(this.vertID, 2048));
		}
		
		GL20.glCompileShader(fragID);
		
		if(GL20.glGetShaderi(this.fragID, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE) {
			System.err.println("FAILED TO COMPILE FRAGMENT SHADER");
			System.err.println(GL20.glGetShaderInfoLog(this.fragID, 2048));
		}
		
		 // Attach the shaders to the program
		GL20.glAttachShader(this.program, this.vertID);
		System.out.println("Attached shader - "+ this.vertPath);
		
		GL20.glAttachShader(this.program, this.fragID);
		System.out.println("Attached shader - "+ this.fragPath);
		
		GL20.glLinkProgram(this.program); // Permit the code from the shaders to be able to be executed
		
		if(GL20.glGetProgrami(this.program, GL20.GL_LINK_STATUS) == GL20.GL_FALSE) {
			System.err.println("Unsuccessful linking: "+GL20.glGetProgramInfoLog(this.program));
		}
		
		GL20.glValidateProgram(this.program); // checks to see if the executables in program can execute
		if(GL20.glGetProgrami(this.program, GL20.GL_VALIDATE_STATUS) == GL20.GL_FALSE) {
			System.err.println("Unsuccessful validation: "+GL20.glGetProgramInfoLog(this.program));
		}

	}
	
	public int getUniformLocation(String name) {
		// This will get the index of the uniform with a target name.
		return GL20.glGetUniformLocation(this.program, name); // Search in this.program for a uniform with (name)
	}
	
	/*
	 * Uniform setters
	 * 
	 * Basic format:
	 * glUniform1f(name, value);
	 * the function will set a uniform to (name) with a value of (value)
	 * */
	
	public void setUniform(String name, float value) {
		GL20.glUniform1f(getUniformLocation(name), value);
	}
	
	public void setUniform(String name, int value) {
		GL20.glUniform1i(getUniformLocation(name), value);
	}
	
	public void setUniform(String name, boolean value) {
		GL20.glUniform1i(getUniformLocation(name), value ? 1 : 0); // value is true then 1, else 0
	}
	
	public void setUniform(String name, Vec2f value) {
		GL20.glUniform2f(getUniformLocation(name), value.getX(), value.getY());
	}
	
	public void setUniform(String name, Vec3f value) {
		GL20.glUniform3f(getUniformLocation(name), value.getX(), value.getY(), value.getZ());
	}
	
	public void setUniform(String name, Matrix4f value) {
		FloatBuffer fb = MemoryUtil.memAllocFloat(value.SIZE*value.SIZE); // Will allocate 16 float worth of data 
		fb.put(value.getAll()).flip();
		
		GL20.glUniformMatrix4fv(getUniformLocation(name),true, fb); // The true determines if it's in row major order, or col major order. It's true because it's in row major order.
	}
	
	public void bind() {

		GL20.glUseProgram(this.program);
	}
	
	public void unbind() {
		GL20.glUseProgram(0);
	}
	
	public void cleanup() {
		/**
		 * Simple cleanup routine for shaders
		 * */
		
	     GL20.glDetachShader(this.program, this.vertID); // Detach the shaders from the program
         GL20.glDetachShader(this.program,this.fragID);

         GL20.glDeleteShader(this.vertID); // Delete the shaders
         GL20.glDeleteShader(this.fragID);

         GL20.glDeleteProgram(this.program); // Delete the program
	}

	public int getProgram() {
	
		return this.program;
	}
	
	
}
