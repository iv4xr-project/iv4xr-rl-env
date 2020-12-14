package rl.environment.generic;

import java.util.Arrays;
import java.util.Random;

public class RLBoxSpace2D extends RLSpace<double[][]> {
	/**
	 * Description name to help with OpenAI Gym interfaces
	 */	
	static transient final String NAME = "Box"; 

	double[][] minBoundaries;
	double[][] maxBoundaries;
	
	public RLBoxSpace2D(double[][] minBoundaries, double[][] maxBoundaries) {
		super(NAME);
		this.minBoundaries = minBoundaries;
		this.maxBoundaries = maxBoundaries;
	}
	
	public RLBoxSpace2D(double low, double high, int N, int P) {
		super(NAME);
		this.minBoundaries = new double[N][P];
		this.maxBoundaries = new double[N][P];
		for (int i = 0; i < N; i++) {
			Arrays.fill(this.minBoundaries[i], low);
			Arrays.fill(this.maxBoundaries[i], high);
		}
	}

	@Override
	public double[][] sample(Random rnd) {	
		int N = minBoundaries.length;
		int P = minBoundaries[0].length;
		var randomActions = new double[N][P];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < P; j++) {
				double min = minBoundaries[i][j];
				double max = maxBoundaries[i][j];
				randomActions[i][j] = min + (max - min) * rnd.nextDouble();
			}
		}
		return randomActions;
	}

}
