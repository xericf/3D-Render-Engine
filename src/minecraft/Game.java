package minecraft;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import engine.graphics.Loader;
import engine.graphics.Mesh;
import engine.graphics.Renderer;
import engine.graphics.Shader;
import engine.graphics.Texture;
import engine.graphics.Vertex;
import engine.io.Input;
import engine.io.Window;
import engine.math.Vec2f;
import engine.math.Vec3f;
import engine.objects.Background;


public class Game implements Runnable { // Runnable for threading, windowlistener for disposing of window event listener
	
	Thread tMain; // main thread
	public Window window;

	final int width = 850;
	final int height = 850;
	
	boolean running;
	
	int frames = 0;
	long time = 0;
	float fps = 0;

	Vertex[] vertices = {
			// Front
			new Vertex(new Vec3f(-10f,  10f, 10.0f), new Vec3f(0.0f, 0.0f, 0.0f), new Vec2f(0.0f, 1.0f)), // position, color
			new Vertex(new Vec3f(-10f, -10f, 10.0f), new Vec3f(0.0f, 1.0f, 1.0f), new Vec2f(0.0f, 0.0f)),
			new Vertex(new Vec3f( 10f, -10f, 10.0f), new Vec3f(1.0f, 1.0f, 0.0f), new Vec2f(1.0f, 0.0f)),
			new Vertex(new Vec3f( 10f,  10f, 10.0f), new Vec3f(1.0f, 1.0f, 1.0f), new Vec2f(1.0f, 1.0f)),
			
			// Back
			new Vertex(new Vec3f(-10f,  10f, -10.0f), new Vec3f(0.0f, 0.0f, 0.0f), new Vec2f(0.0f, 1.0f)), // position, color
			new Vertex(new Vec3f(-10f, -10f, -10.0f), new Vec3f(0.0f, 1.0f, 1.0f), new Vec2f(0.0f, 0.0f)),
			new Vertex(new Vec3f( 10f, -10f, -10.0f), new Vec3f(1.0f, 1.0f, 0.0f), new Vec2f(1.0f, 0.0f)),
			new Vertex(new Vec3f( 10f,  10f, -10.0f), new Vec3f(1.0f, 1.0f, 1.0f), new Vec2f(1.0f, 1.0f)),
			
			
	};
	
	Texture wood_plank;
	
	Mesh rect;

	Shader shader;
	Renderer renderer;
	
	Background background;
	Game() {
		
		this.init();
		
		this.start();
		this.stop();
	}
	
	public void init() {
		this.tMain = new Thread(this); // Pass in the running class.
		
		this.window = new Window(this.width, this.height, "Eric's game");
		this.renderer = new Renderer(window);
		
		time = System.currentTimeMillis();
	}

	public void update() {
		// Calculations goes here
		this.window.update();
		
		this.handleKey();
		this.handleMouse();
		
		this.countFrames();
	}
	
	public void handleKey() {
		//Keyboard event handler
		if(Input.getKey(GLFW.GLFW_KEY_ESCAPE)) {
			GLFW.glfwSetWindowShouldClose(this.window.getWindow(), true);
		} 
		
	}
	
	public void handleMouse() {
		// Mouse button and coords handler
		double[] m = Input.getMousePos();
		if(Input.getMouseButton(GLFW.GLFW_MOUSE_BUTTON_1)) {
			System.out.println("X: " + m[0] + " Y: "+m[1]);
			
			background.setClicking((float) m[0], (float) m[1]);
		}
		
	}
	
	public void render() {
		// Rendering goes here
		
		Renderer.prepare(); // Prepare every frame
		renderer.render(background);
		
		
		this.window.swapBuffers();
	}

	@Override
	public void run() {
		// This is called automatically when tMain.start() is ran, this is because of the implemented Runnable interface.

		this.window.init();  // IMPORTANT: In this function, we made the current context, so put all of the rendering initialization after this.
		
		Texture.initTextures();
		
		
		wood_plank = Texture.get("background.png");
		rect = new Mesh(vertices, wood_plank, new int[] {
				// 0 - (-10, 10, -10) // front, left top
				// 1 - (-10, -10, -10) // front left bottom
				// 2 - (10, -10, -10) front, right bottom
				// 3 - (10, 10 , -10) - front, right top
			
				// 4 - (-10, 10, 10) // back, left top
				// 5 - (-10, -10, 10) // back left bottom
				// 6 - (10, -10, 10) back, right bottom
				// 7 - (10, 10 ,-10) - back, right top
			
				// Front face
				0, 1, 2,
				0, 3, 2,

				// backface
				4, 5, 6,
				4, 7, 6,
				
				// left face
				0, 1, 5,
				0, 4, 5,
				
				// right face
				3, 2, 7,
				2, 7, 6,
				
				// top face
				0, 7, 4,
				0, 7, 3,
				
				// bottom face
				1, 6, 2,
				1, 6, 5,
				
			});
		rect.create();
		
		shader = new Shader("src/resources/vertex.glsl", "src/resources/frag.glsl");
		
		background = new Background(
				new Vec3f(0,0,0),
				new Vec3f(0,0,0),
				new Vec3f(1.1f,1.1f,1.1f),
				rect,
				shader
				);
		

		
		// For some reason (Maybe an obvious one), this needs to be called AFTER this.start() starts its threading. 
		// It says The simplest rule is: only call GLFW functions from one thread, so probably because we called it before we gave tMain the job of this task, it
		// wasn't happy and decided to freeze BECAUSE IN UPDATE GLFW.glfwPollEvents(); wasn't called in the same thread, which is the reason why most of 
		// these errors happen because the threads couldn't communicate with each other for glfwPollEvents (Probably)
		
		
		while(!window.shouldClose()) { // The game loop that handles updates and calculations
			
			update();
			render();
			
		}
		
		this.window.cleanup();
		Loader.cleanup(); // Cleanup the VAOs, VBOs
		shader.cleanup();
	}
	
	private void countFrames() {
		long ct = System.currentTimeMillis();

		if(ct -time>= 1000) {
			this.time = ct;
			this.fps = frames; // Not 100% accurate (A more accurate method is to take  (ct - time)/frames *1000  to extrapolate exact miliseconds to frame rate, this right now would be off very slightly probably)
			this.frames = 0;
			GLFW.glfwSetWindowTitle(this.window.getWindow(), "Eric's game - FPS: "+ this.fps);
		}
		this.frames++;
	}
	
	private synchronized void start() { // Syncrhonized is used so that only one thread can use this method at once.
		this.running = true;
		this.tMain.start();
	}
	
	private synchronized void stop() {
		this.running = false;
		try {
			this.tMain.join(); //  put the current thread on wait until the thread on which it is called is dead
		} catch(Exception e) {
			System.out.println(e);
		}
				
	}


}

/**
 * Code that's not used anymore:
		
		
		long startTime = System.nanoTime();
		long interval = 1000000000l / this.fps; //1 second / 60 for interval for a single frame, 1 second has 9 zeroes if in nanosecond form
		long endTime = System.nanoTime();
			endTime = System.nanoTime();
			if(endTime - startTime > interval) { // If it has been atleast {interval} amount of nanoseconds since the last startTime call, then run again.
				startTime = System.nanoTime();
				
				update();
				render();
			}
			
 */