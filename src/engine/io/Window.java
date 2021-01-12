package engine.io;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import engine.math.Matrix4f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.Callbacks.*;

import org.lwjgl.glfw.GLFW;


public class Window {
	
	private int width;
	private int height;
	private String title;
	
	boolean isResized = false; // Checks if the window has been resized
	
	private long window; // window handle
	
	public Input input;
	
	private Matrix4f perspective;
	
	
	public Window(int width, int height, String title){
		this.width = width;
		this.height = height;
		this.title = title;
		
		this.perspective = Matrix4f.Orthographic(-10.0f, 10.0f, -10.0f, 10.0f, -15.0f, 15.0f);

	}
	
	public void init() {
		
		// This is to setup error callbacks where they will print to System.err
		GLFWErrorCallback.createPrint(System.err).set();
		
		// If GLFW has been initialized
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW"); // will print to System.err because of GLFWError callback
		
		window = GLFW.glfwCreateWindow(this.width, this.height, this.title,0,0); // Create a window using GLFW
		if(window == 0 ) {
			System.err.println("ERROR: GLFW WIndow has not been created");
			return;
		} 
		
		
		
		GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor()); // Get the primary monitor
		GLFW.glfwSetWindowPos(this.window, (videoMode.width()-this.width)/2, (videoMode.height()-this.height)/2); // set the position of the window
		
		GLFW.glfwMakeContextCurrent(this.window); // make this the current context
		
		this.initInput();

		GL.createCapabilities();
		
		GL11.glEnable(GL11.GL_DEPTH_TEST); // Enable server side GL capabilities
		GL11.glViewport(0, 0, this.width, this.height);
		
		GLFW.glfwSwapInterval(1); // Enable v-sync
		GLFW.glfwShowWindow(this.window);
		
	}
	
	private void initInput() {
		this.input = new Input();
		
		// instead of making the callbacks right here, we will store it in Input.java to save space and be organized
		GLFW.glfwSetKeyCallback(this.window, this.input.getKeyCallback()); 
		GLFW.glfwSetCursorPosCallback(this.window, this.input.getCursorPosCallback());
		GLFW.glfwSetMouseButtonCallback(window, this.input.getButtonCallback()); 
		
	}
	
	
	public void update() {
		
		/*
		 * @desc - Gets called in Game.java, updates the window and all events
		 * */
		if (isResized) {
			GL11.glViewport(0, 0, this.width, this.height);
			isResized = false;
		}
		
		
		GLFW.glfwPollEvents(); //processes only those events that have already been received and then returns immediately.
	}
	
	public void swapBuffers() {
		/*
		 * @desc - Sets all of the colors to the correct color.
		 * */
		
		GLFW.glfwSwapBuffers(this.window); //swaps the front and back buffers of the specified window
	}
	
	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(this.window);
	}
	
	public void cleanup() {
		// Free the window callbacks and destroy the window
		
		glfwFreeCallbacks(this.window);
		glfwDestroyWindow(this.window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		
		glfwSetErrorCallback(null).free();
	}
	public Matrix4f getPerspectiveMatrix() {
		return this.perspective;
	}
	
	public long getWindow() {
		return this.window;
	}
	
	
}
