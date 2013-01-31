package spacegame.util;

/**
 * A generic matrix.
 * 
 * @author Felix Treede
 * 
 */
public class Matrix {

	private float[] data;

	private int width, height;

	public Matrix(int width, int height) {
		this.width = width;
		this.height = height;

		this.data = new float[width * height];
	}

	// checks the coords and returns the index in the data array
	private int getDataIndex(int x, int y) {
		if (x < 0 || x >= width) {
			throw new IndexOutOfBoundsException("x coord out of bounds: " + x);
		}
		if (y < 0 || y >= height) {
			throw new IndexOutOfBoundsException("y coord out of bounds: " + y);
		}

		return x + y * width;
	}

	/**
	 * Retrieves an element.
	 * 
	 * @param x
	 *            the column index
	 * @param y
	 *            the row index
	 * @return the value at the given position
	 */
	public float getElement(int x, int y) {
		return data[getDataIndex(x, y)];
	}

	/**
	 * Modifies an element.
	 * 
	 * @param x
	 *            the column index
	 * @param y
	 *            the row index
	 * @param newValue
	 *            the new value
	 */
	public void setElement(int x, int y, float newValue) {
		data[getDataIndex(x, y)] = newValue;
	}

	/**
	 * Multiplies an element with the given factor.
	 * 
	 * @param x
	 *            the column index
	 * @param y
	 *            the row index
	 * @param factor
	 *            the factor
	 * @return the new value
	 */
	public float multElement(int x, int y, float factor) {
		return data[getDataIndex(x, y)] *= factor;
	}

	/**
	 * Adds the given number to an element.
	 * 
	 * @param x
	 *            the column index
	 * @param y
	 *            the row index
	 * @param val
	 *            the value to add
	 * @return the new value
	 */
	public float addElement(int x, int y, float val) {
		return data[getDataIndex(x, y)] += val;
	}

	public Matrix mult(float factor, Matrix dst) {
		if (dst == null) {
			dst = new Matrix(width, height);
		} else if (this.width != dst.width || this.height != dst.height) {
			throw new IllegalArgumentException("Mismatching destination size");
		}
		for (int i = 0; i < data.length; i++) {
			dst.data[i] = data[i] * factor;
		}

		return dst;
	}

	public Matrix mult(Matrix other, Matrix dst) {
		if (this.width != other.height) {
			throw new IllegalArgumentException("Mismatching operand size");
		}

		if (dst == null) {
			dst = new Matrix(other.width, height);
		} else if (this.height != dst.height || other.width != dst.width) {
			throw new IllegalArgumentException("Mismatching destination size");
		}

		float[] dstArray;
		boolean dstIsCopy;
		if (dst == this || dst == other) {
			dstIsCopy = true;
			dstArray = new float[dst.data.length];
		} else {
			dstIsCopy = false;
			dstArray = dst.data;
		}

		for (int dx = 0; dx < other.width; dx++) {
			for (int dy = 0; dy < height; dy++) {
				float sum = 0;

				for (int i = 0; i < width; i++) {
					sum += data[dy * width + i] * other.data[i * other.width + dy];
				}

				dstArray[dy * other.width + dx] = sum;
			}
		}

		if (dstIsCopy) {
			System.arraycopy(dstArray, 0, dst.data, 0, dstArray.length);
		}

		return dst;
	}

	public Matrix add(Matrix other, Matrix dst) {
		if (this.width != other.width || this.height != other.height) {
			throw new IllegalArgumentException("Mismatching operand size");
		}
		if (dst == null) {
			dst = new Matrix(width, height);
		} else if (this.width != dst.width || this.height != dst.height) {
			throw new IllegalArgumentException("Mismatching destination size");
		}

		for (int i = 0; i < data.length; i++) {
			dst.data[i] = data[i] + other.data[i];
		}

		return dst;
	}

	public Matrix addScaled(Matrix other, float factor, Matrix dst) {
		if (this.width != other.width || this.height != other.height) {
			throw new IllegalArgumentException("Mismatching operand size");
		}
		if (dst == null) {
			dst = new Matrix(width, height);
		} else if (this.width != dst.width || this.height != dst.height) {
			throw new IllegalArgumentException("Mismatching destination size");
		}

		for (int i = 0; i < data.length; i++) {
			dst.data[i] = data[i] + other.data[i] * factor;
		}

		return dst;
	}

}
