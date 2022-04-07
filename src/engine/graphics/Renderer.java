package engine.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import engine.io.Window;
import engine.objects.GameObject;

public class Renderer {

	Window window;
	public Renderer(Window window) {
		// * Each game object or unique category of objects will contain a renderer function to make rendering easier.
		this.window = window;
	}
	
	public static void prepare() {
		/**
		 * This function will be called once every frame so that the color is cleared from the last frame
		 * 
		 * For some reason the glClear needs the result of the piping to work and render things. My guess is that GL11.GL_DEPTH_BUFFER_BIT was 
		 * Referenced in @Window.java.init when glEnable(GL11.GL_DEPTH_BUFFER_BIT) was called.
		 * */
		// Clear the color with an RGBA this is essentially the background
		GL11.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
		
		// glClear is used to clear buffers to preset values
		// GL_COLOR_BUFFER_BIT Indicates the buffers currently enabled for color writing.
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	

	
	public void render(GameObject object) {
		/**
		 * This is used to render Meshes. It uses the glDrawElements method as opposed to glDrawArrays so that it would reuse vertices
		 * @param model - The target mesh to render
		 * */
		
		/**
		 * Notice how object is actually of the type GameObject, which is really just an 
		 * interface. It will accept any type of object that implements the GameObject interface
		 * but that means that the object will only work with the methods in the GameObject
		 * interface.
		 * */
		Mesh model = object.getMesh();
		Shader shader = object.getShader();
		
		GL30.glBindVertexArray(model.getVAO()); // bind the VAO
		
		//Enable the attribute list with an id
		GL20.glEnableVertexAttribArray(0); // It's 0 because we previously stored our vertexes in the 0 indice attribute array
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, model.getIBO()); // Bind the indices buffer object
		
		Texture tex = model.getTextureObject();
		
		// glActiveTexture represents setting a particular texture unit that is being used. Think about it like it's choosing
		// Which picture it wants to use to render.
		GL13.glActiveTexture(GL13.GL_TEXTURE0); 
		
		GL13.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTexture()); // Bind the texture with the texture ID
		shader.bind();
		
		object.update();
		object.callUniforms(window.getPerspectiveMatrix());
		
		// glDrawElements draws the vertices using indices, meaning that it will not redraw the same vertex twice, it will instead reuse it which will save performance on high vertices projects
		// The indices used for this method from the buffer object that we binded before, this explains the model.getIndices().length so the program knows how long the object is
		GL30.glDrawElements(GL11.GL_TRIANGLES, model.getIndices().length, GL11.GL_UNSIGNED_INT,0);
		
		shader.unbind();
		GL13.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0); // unbind
		// Disable the same attribute list as we are done using it.
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		
		GL30.glBindVertexArray(0); // unbind it
		
		
	}
}
