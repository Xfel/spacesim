/**
 * 
 */
package spacegame.ai.nn;

import java.util.AbstractMap.SimpleEntry;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import spacegame.ai.Autopilot;
import spacegame.util.Matrix;

/**
 * @author Joachim
 * 
 */
public abstract class NeuronalNetwork extends Autopilot {
	private Matrix modifier;
	private Matrix weight;
	private Matrix state;

	private int inputSize;
	private int outputSize;
	private LinkedList<Entry<Integer, Boolean>> layers;

	private boolean build;

	public NeuronalNetwork() {
		build = false;
		inputSize = 1;
		outputSize = 1;
	}

	public void build() {
		this.build = true;

		int total = 0;
		
		layers.addFirst(new SimpleEntry<Integer, Boolean>(inputSize, false));
		layers.addLast(new SimpleEntry<Integer, Boolean>(outputSize, false));
		
		for (Iterator<Entry<Integer, Boolean>> iterator = layers.iterator(); iterator.hasNext();) {
			Entry<Integer, Boolean> type = /*(Entry<Integer, Boolean>)*/ iterator.next();
			
			total += type.getKey();
			
			if(type.getValue()){
				total += type.getKey();
			}
		}
		
		this.modifier = new Matrix(total,total);
		this.weight = new Matrix(total, total);
		this.state = new Matrix(total, 1); // Vector
		
		generateModifierMatrix();
	}
	
	private void generateModifierMatrix(){
		
	}

	public void setInputSize(int inputSize) {
		if (build || inputSize <= 0) {
			throw new IllegalArgumentException(
					"Modification to net not allowed.");
		}

		this.inputSize = inputSize;
	}

	public void setOutputSize(int outputSize) {
		if (build || outputSize <= 0) {
			throw new IllegalArgumentException(
					"Modification to net not allowed.");
		}

		this.outputSize = outputSize;
	}

	public void addInputUnits(int number) {
		if (build || number < 0) {
			throw new IllegalArgumentException(
					"Cannot subtract units from net.");
		}

		this.inputSize += number;
	}

	public void addOutputUnits(int number) {
		if (build || number < 0) {
			throw new IllegalArgumentException(
					"Cannot subtract units from net.");
		}

		this.outputSize += number;
	}

	public void addHiddenLayer(int size, boolean context) {
		if (build || size <= 0) {
			throw new IllegalArgumentException("Modification to net not allowed.");
		}

		layers.add(new SimpleEntry<Integer, Boolean>(size, context));
	}
}
