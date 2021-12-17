package rl.environment.generic;

import java.util.Arrays;
import java.util.Random;

/**
 * Two-dimensional Box space. This box is the cartesian product of N*P closed intervals
 * in (R^N)^P. Each interval has the form of [a, b], ]-inf, b], [a, -inf[ or ]-inf, inf[
 *
 * One element of this space has the form of a N*P image.
 */
public class RLBoxSpace2D extends RLSpace<double[][]> {
	/**
	 * Description name to help with OpenAI Gym interfaces
	 */	
	static transient final String NAME = "Box"; 

	double[][] minBoundaries;
	double[][] maxBoundaries;

	/**
	 * Initialized with minimum and maximum boundaries given in N*P image format.
	 *
	 * @param minBoundaries minimum boundaries.
	 * @param maxBoundaries maximum boundaries.
	 */
	public RLBoxSpace2D(double[][] minBoundaries, double[][] maxBoundaries) {
		super(NAME);
		this.minBoundaries = minBoundaries;
		this.maxBoundaries = maxBoundaries;
	}

	/**
	 * Initialize with single low and high values, and (N, P) dimensions.
	 *
	 * @param low low boundary.
	 * @param high high boundary.
	 * @param N width dimension.
	 * @param P height dimension.
	 */
	public RLBoxSpace2D(double low, double high, int N, int P) {
		super(NAME);
		this.minBoundaries = new double[N][P];
		this.maxBoundaries = new double[N][P];
		for (int i = 0; i < N; i++) {
			Arrays.fill(this.minBoundaries[i], low);
			Arrays.fill(this.maxBoundaries[i], high);
		}
	}

	/**
	 * Get a random element from this space.
	 * Uniform sampling is performed.
	 *
	 * @param rnd random generator.
	 * @return one random (N, P) image.
	 */
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
