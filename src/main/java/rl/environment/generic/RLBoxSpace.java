package rl.environment.generic;

import java.util.Arrays;
import java.util.Random;

public class RLBoxSpace extends RLSpace<double[]> {
	/**
	 * Description name to help with OpenAI Gym interfaces
	 */	
	static transient final String NAME = "Box"; 

	double[] minBoundaries;
	double[] maxBoundaries;
	
	public RLBoxSpace(double[] minBoundaries, double[] maxBoundaries) {
		super(NAME);
		this.minBoundaries = minBoundaries;
		this.maxBoundaries = maxBoundaries;
	}
	
	public RLBoxSpace(double low, double high, int N) {
		super(NAME);
		this.minBoundaries = new double[N];
		this.maxBoundaries = new double[N];
		Arrays.fill(this.minBoundaries, low);
		Arrays.fill(this.maxBoundaries, high);
	}

	@Override
	public double[] sample(Random rnd) {
		var randomActions = new double[minBoundaries.length];
		for (int i = 0; i < minBoundaries.length; i++) {
			double min = minBoundaries[i];
			double max = maxBoundaries[i];
			randomActions[i] = min + (max - min) * rnd.nextDouble();
		}
		return randomActions;
	}

}
