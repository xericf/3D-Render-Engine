package engine.utils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryUtil;

public class BufferUtils {
	private BufferUtils() {
		
	}
	
	public static ByteBuffer storeByteBuffer(byte[] arr) {
		ByteBuffer buff = ByteBuffer.allocate(arr.length);
		buff.put(arr);
		buff.flip();
		return buff;
	}
	
	public static IntBuffer storeIntBuffer(int[] arr) {
		/**
		 * Store an integer buffer
		 * */
		
		IntBuffer buff = MemoryUtil.memAllocInt(arr.length);
		
		buff.put(arr);
		buff.flip();
		
		return buff;
	}
	
	public static FloatBuffer storeFloatBuffer(float[] arr) {
		/**
		 * This is different from the storeFloatBuffer method in the Loader class, it will accept float[] instead
		 * 
		 * */
		FloatBuffer buff = MemoryUtil.memAllocFloat(arr.length);
		
		buff.put(arr);
		buff.flip();
		
		return buff;
		
	}
	
}
