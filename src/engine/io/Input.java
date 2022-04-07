package engine.io;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;


public class Input {
	private static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST]; // the index of the last readable key. It's value is 348. Arraymap of all keys if they're on or off
	private static double mx,my; // mouse coordinates relative to window 
	private static boolean[] buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST]; // Determine if mouse button is clicked
	
	private GLFWKeyCallback keyboard;
	private GLFWCursorPosCallback mouse;
	private GLFWMouseButtonCallback mbtn;
	
	public Input() {
		
		
		/**
		 * Explanation for these following functions
		 * For some reason I couldn't really find these on the documenations, however you could reference a new GLFWKeyCallback
		 * and initialize the invoke method as something, then set the glfw event listener to it at a different file like in Window.java
		 * This is just to keep the project organized as putting everything in Window.java may be a little bit too much.
		 * The code below are essentially event listeners for keyboard inputs, mouse button inputs, and mouse position listeners.
		 * 
		 * */
		keyboard = new GLFWKeyCallback() {
			
			public void invoke(long window, int key, int scancode, int action, int mods) {
				
				keys[key] = (action==GLFW.GLFW_REPEAT || action == GLFW.GLFW_PRESS); // Will set the specified key that's called to true if it's being held down.

			}
		};
		
		mouse = new GLFWCursorPosCallback() {
			public void invoke(long window, double xpos, double ypos) {
				mx = xpos;
				my = ypos;
			}
		};
		
		mbtn = new GLFWMouseButtonCallback() {
			public void invoke(long window, int btn, int action, int mods) {
				buttons[btn] = (action == GLFW.GLFW_PRESS ||action==GLFW.GLFW_REPEAT ); // If mouse button is pressed or held
			}
		};
		
	}
	
	public void destroy() {
		// Cleanup, maybe this isn't necessary due to the cleanup in window.java
		keyboard.free();
		mouse.free();
		mbtn.free();
	}
	
	
	public static boolean getKey(int key) {
		// Will return if a key is pressed or not
		return keys[key]; // if key pressed
	}
	
	public static double[] getMousePos() {
		// Will return mouse position
		return new double[] {mx, my};
	}
	
	public static boolean getMouseButton(int btn) {
		// will return if mouse button is pressed or not
		return buttons[btn];
	}
	
	public GLFWKeyCallback getKeyCallback() {
		return keyboard;
	}
	
	public GLFWMouseButtonCallback getButtonCallback() {
		return mbtn;
	}
	
	public GLFWCursorPosCallback getCursorPosCallback() {
		return mouse;
	}
	
	
	
	
}
