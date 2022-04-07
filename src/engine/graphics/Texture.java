package engine.graphics;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import engine.utils.*;
import org.lwjgl.opengl.GL30;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Texture {
	
	public static File textureFolder;

	public static File[] textureFiles;
	public static List<Texture> textures;
	
	private int pixels[];
	
	private int width;
	private int height;
	
	private String loc;
	private String name;
	
	private int texture; // this is the id for the shader texture
	
	
	// TODO: add implementations for 6 sides of a cube.
	
	/*
	 * @Desc - Will hold the texture for an object.
	 * @param loc - will hold the location of the texture that is needed
	 * @param size - will be the size of the texture (size*size) in pixels. Square form.
	 * */
	public Texture(String loc, String name){
		
		this.loc = loc;

		this.name = name;
		
		this.load();
		
	}
	
	private void load() {
		// Read the RGB value of each pixel in textures.
		try {
			File imgFile = new File(this.loc);
			
			BufferedImage img = ImageIO.read(imgFile); // REMEMBER: ImageIO is a STATIC METHOD! Pay attention to static
			this.height = img.getHeight();
			this.width = img.getWidth();
			this.pixels = new int[height*width];
			this.pixels = img.getRGB(0, 0, width, height, this.pixels , 0, width);
			// The last argument is apparently to tell the reader when to stop and start each row, so it will read this.size amount of pixels then move y coord down.
			
		} catch (IOException e) {
			
			System.out.println(e);
		}
		System.out.println("Loaded Image: " + this.name);
		for(int i = 0; i < this.pixels.length; i++) {
			this.pixels[i] = getARGB(this.pixels[i]);
		}
		
		this.texture = GL30.glGenTextures();
		
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, this.texture); // Bind the texture with GL_TEXTURE_2D
		
		// Minifying parameter, it will allow us to map the texture onto a larger area than the source.
		GL30.glTexParameterIi(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_NEAREST);
		
		// Magnifying parameter, it will allow us to map the texture onto a smaller area than the source.
		GL30.glTexParameterIi(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_NEAREST);
		
		/*
		 * The last argument is essentially feeding the generated opengl texture with an array of RGB values which correspond
		 * to an X and Y coordinate.
		 * */
		GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA, this.width, this.height, 0, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, BufferUtils.storeIntBuffer(this.pixels));
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, 0); //unbind trhe texture
	}
	
	public static int getARGB(int col) {
		/**
		 * Essentially this method just reformats the bits in int col. It will take every 8 bits and isolate it so that it is only 8 bits long.
		 * Then it will reformat using bitwise operaters and make each necessary bit fit into the correct slot. Then it will concatenate them together to get one 
		 * 32 bit number.
		 * */
		
		
		int a = (col & 0xFF);		// Target 0x000000FF, the ff numbers matter for this property, this will just essentially turn the bits that don't matter into 0s
		int b = (col & 0xFF00) >> 8; 	// Target 0x0000FF00
		int c = (col & 0xFF0000) >> 16; 	// Target 0x00FF0000
		int d = (col & 0xFF000000) >> 24; // Target 0xFF000000, alpha
		
		

	
		return (d << 24) | (a << 16) | (c) | (b << 8); // Concatenate them together to form one big bit
	}
	
	public static void initTextures() {
		/*
		 * This is to make an array containing all the textures found in the textures folder
		 * */
		

		textures = new ArrayList<Texture>();
		try {
			textureFolder = new File("src/minecraft/textures"); // for somereason java doesn't use ./ to reference local paths, and if you put / before the name then it will reference an absolute path
			textureFiles = textureFolder.listFiles();
			for(int i = 0; i < textureFiles.length; i++) {
				
				File t = textureFiles[i]; // implicit reference to specified file to reduce typing
				String name = t.getName();
				
				
				textures.add(new Texture(t.getAbsolutePath(), name));
			}
		} catch(Exception e) {
			System.out.println(e);
		}
		System.out.println("Initialized Textures in /textures");
	}
	
	// Getters,
	
	public static Texture get(String name) {
		for(int i =0 ; i < textures.size(); i++) {
			Texture t =textures.get(i);
			
			
			if(t.getName().equals(name)) {
			
				return t;
			}
		}
		System.err.println("Couldn't find: " + name);
		return null;
	}
	
	public int getTexture() {
		return texture;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int[] getPixels() { 
		return this.pixels;
	}
	
	public String getLocation() {

		return this.loc;
	}

	public String getName() {
		return this.name;
	}
	
	// the purpose of setters is to always know if a property is changed, so use it like an event listener
	
	
}