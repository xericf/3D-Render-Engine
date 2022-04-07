package engine.math;


public class Matrix4f {
	public final int SIZE = 4;
	private float[] matrix = new float[4*4]; // create a 4x4 matrix
	
	/**
	 * A matrix is used to store a systems of equations that looked like this
	 * 
	 * x	y	z	w
	 * [
	 * 1	0	0	0
	 * 0	1	0	0
	 * 0	0	1	0
	 * 0	0	0	1
	 * ]
	 * w represents the constant, however when w is used in a projection matrix
	 * it allows us to have two parrelel lines somehow intersect when w=0, this means
	 * that we are now allowed to create realistic depth.
	 * 
	 * The above matrix represents a default identity matrix, the columns
	 * represent a variable for all of the elements below it.
	 * 
	 * */
	
	public static Matrix4f identity() {
		/**
		 * An Identity matrix, is essentially just the number 1 because each matrix multipying this will equal the original matrix
		 * 
		 * [
		 * 1 0 0 0
		 * 0 1 0 0
		 * 0 0 1 0
		 * 0 0 0 1
		 * ]
		 * */
		
		// new matrix
		Matrix4f nMatrix = new Matrix4f(); 
		
		nMatrix.matrix[0 + 0*4] = 1.0f; // The first number represents the row, the second represents the column
		nMatrix.matrix[1 + 1*4] = 1.0f;
		nMatrix.matrix[2 + 2*4] = 1.0f;
		nMatrix.matrix[3 + 3*4] = 1.0f;
		
		
		return nMatrix;
		
		 
	}
	
	
	
	public Matrix4f Multiply(Matrix4f targetMatrix) {
		/**
		 * Matrix multiplication function
		 * @param targetMatrix - the matrix you want to multiply by
		 * */
		
		
		// No need to error check for matrix of different size as it's Matrix4f required, so it will always be mulitplaible
		Matrix4f result = new Matrix4f();
		for(int i = 0; i < 4; i++) {
			// column		
			for(int j = 0; j < 4; j++) {
				// row
				float number = 0.0f;
				// Add column of first matrix by row of second matrix, do it 4 times for each element in the source matrix
				
				for(int e = 0; e < 4; e++) {
					number += this.matrix[e+i*4] * targetMatrix.matrix[j+e*4]; // The source keeps the same row, the target keeps the same column
				}
				result.matrix[j + i*4] = number;			
			}
		}
		
		return result;
	}
	
	public static Matrix4f Rotate(float angle, Vec3f vector) {
		/**
		 * This will return a Matrix4f with coordinates that is the result of rotating a point by an angle rotated in 3 dimensions
		 * @param angle - The angle 0.0f - 360.0f
		 * @param vector - Vec3f of the direction the point is in relation to you.
		 * 
		 
		 * */
		
		return new Matrix4f();
	}
	
	public static Matrix4f RotateZ(float angle) {
		/**
		 * This will return a Matrix4f with a rotation matrix that you could multiply with another matrix to get rotated coordinates
		 * This will rotate matrix on a 2 dimensional plane.
		 * @param angle - The angle 0.0f - 360.0f
		 * 
		 * Proof:
		 * let t = original angle, r be length from origin to point, (x0,y0) = original point, a = rotation angle -> all of these are known
		 * let (x1,y1) be new point: We will find (x1, y1) which is not known, 
		 * x1 =rcos(t+a)
		 * x1 = r((x0/r) * cos(a) - (y0/r) * sin(a))
		 * x1 = x0 * cos(a) - y0 * sin(a);
		 * 
		 * y1 = rsin(t+a)
		 * .. just do the same thing but with summation for sin formula
		 * 
		 * Converted to matrix it's
		 * [	  	[       					[
		 * x0	* 	cos(a)	-sin(a)			= 	x1
		 * y0		sin(a)	cos(a)				y1
		 * ]									]
		 * */
		
		Matrix4f nMatrix = identity(); // So that when multipied, the matrix won't return 0 for all of the other elements of the matrix that are not edited
		
		float rad = (float) Math.toRadians(angle);
		// Remember the arg should be in radians for trigonometry functions
		nMatrix.matrix[0 + 0*4] = (float) Math.cos(rad);
		nMatrix.matrix[0 + 1*4] = (float) -Math.sin(rad);
		nMatrix.matrix[1 + 0*4] = (float) Math.sin(rad);
		nMatrix.matrix[1 + 1*4] = (float) Math.cos(rad);
		
		return nMatrix;
	}
	
	public static Matrix4f RotateY(float angle) {
		/**
		 * Will rotate around the Y-Axis
		 * */
		
		Matrix4f nMatrix = identity(); // So that when multipied, the matrix won't return 0 for all of the other elements of the matrix that are not edited
		
		float rad = (float) Math.toRadians(angle);
		// Remember the arg should be in radians for trigonometry functions
		nMatrix.matrix[0 + 0*4] = (float) Math.cos(rad);
		nMatrix.matrix[2 + 0*4] = (float) -Math.sin(rad);
		nMatrix.matrix[0 + 2*4] = (float) Math.sin(rad);
		nMatrix.matrix[2 + 2*4] = (float) Math.cos(rad);
		
		return nMatrix;
	}
	public static Matrix4f RotateX(float angle) {
		/**
		* Will rotate around the X-Axis
		 * */
		
		Matrix4f nMatrix = identity(); // So that when multipied, the matrix won't return 0 for all of the other elements of the matrix that are not edited
		
		float rad = (float) Math.toRadians(angle);
		// Remember the arg should be in radians for trigonometry functions
		nMatrix.matrix[1 + 1*4] = (float) Math.cos(rad);
		nMatrix.matrix[1 + 2*4] = (float) -Math.sin(rad);
		nMatrix.matrix[2 + 1*4] = (float) Math.sin(rad);
		nMatrix.matrix[2 + 2*4] = (float) Math.cos(rad);
		
		return nMatrix;
	}
	
	
	public float get(int x, int y) {
		/**
		 * This function will get a float using row major order.
		 * */
		return this.matrix[y + x*4];
	}
	
	public void setIndex(int x, int y, float data) {
		/**
		 * This function will set the matrix using row major order.
		 */
		
		this.matrix[y + x*4] = data;
	}
	

	
	public float[] getAll() {
		return this.matrix;
	}
	
	public static Matrix4f Translate(Vec3f vec) {
		/**
		 * Will create a translation matrix based on a vector, this just translates
		 * A matrix by the translation matrix
		 * 
		 * x y z w
		 * [
		 * 1 0 0 x
		 * 0 1 0 y
		 * 0 0 1 z
		 * 0 0 0 1
		 * ]
		 * 
		 * Notice how it's below the constant w, which means that it's translating
		 * by a constant. It's affecting the column with a 1 in it.
		 * */ 
		
		Matrix4f result = identity();
		
		
		result.matrix[0 + 3*4] = vec.getX();
		result.matrix[1 + 3*4] = vec.getY();
		result.matrix[2 + 3*4] = vec.getZ();
		// w will always remain 1.0f with the identity setting it beforehand.
		
		return result;
	}
	
	public static Matrix4f Scale(Vec3f vec) {
		/**
		 * Will create a scale matrix based on a vector, this just translates
		 * A matrix by the translation matrix
		 * 
		 * x y z w
		 * [
		 * x 0 0 0
		 * 0 y 0 0
		 * 0 0 z 0
		 * 0 0 0 1
		 * ]
		 * 
		 * The scaler will manipulate the x,y,z parts of the equation instead of the constant
		 * for each equation. This means when it multiplies it will actually multiply by x,y,z
		 * parts of the matrix.
		 * */ 
		Matrix4f result= identity();
		
		result.matrix[0 + 0*4] = vec.getX();
		result.matrix[1 + 1*4] = vec.getY();
		result.matrix[2 + 2*4] = vec.getZ();
		
		return result;
		
	}
	
	public static Matrix4f Transform(Vec3f translation, Vec3f rotation, Vec3f scale) {
		
		Matrix4f rotX = RotateX(rotation.getX());
		Matrix4f rotY = RotateY(rotation.getY());
		Matrix4f rotZ = RotateZ(rotation.getZ());
		
		Matrix4f overallRot = rotX.Multiply(rotY).Multiply(rotZ);
		
		Matrix4f translate = Translate(translation);
		Matrix4f scaler = Scale(scale);
		
		// You have to multiply the overallRot with the scaler THEN multiply it by the translation
		// Matrix Order matters to a huge extent apparently. 
		//https://gamedev.stackexchange.com/questions/16719/what-is-the-correct-order-to-multiply-scale-rotation-and-translation-matrices-f
		return overallRot.Multiply(scaler).Multiply(translate); 
	}
	
	public static Matrix4f Orthographic(float left, float right, float top, float bottom, float near, float far) {
		/**
		 * Imagine this matrix 4f producing a box using the positions of left, right, top, bottom, near, and far. The rectangular prism will
		 * determine what we are able to see. Everything inside that box will be rendered.
		 * 
		 * This method will essentially take the difference of each dimension's points
		 * and then when multiplied with another matrix will scale their points to the difference.
		 * 
		 * */
		
		
		
		
		Matrix4f result = identity();
		
		result.matrix[0 + 0*4] = 2.0f / (right-left); // 1/(average of difference) for each xyz value
		result.matrix[1 + 1*4] = 2.0f/ (top-bottom); 
		result.matrix[2 + 2*4] = 2.0f / (near - far);
		result.matrix[0 + 3*4] = (left+right) / (left-right);
		result.matrix[1 + 3*4] = (bottom+top) / (bottom-top);
		result.matrix[2 + 3*4] = (near + far) / (far - near);
		
		return result;
	}
}
