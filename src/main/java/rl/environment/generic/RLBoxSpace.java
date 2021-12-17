package rl.environment.generic;

import java.util.Arrays;
import java.util.Random;

/**
 * One-dimensional Box space. This box is the cartesian product of N closed intervals
 * in R^n. Each interval has the form of [a, b], ]-inf, b], [a, -inf[ or ]-inf, inf[
 *
 * One element of this space has the form of a [x_0, x_1, ..., x_N-1] vector.
 */
public class RLBoxSpace extends RLSpace<double[]> {
	/**
	 * Description name to help with OpenAI Gym interfaces
	 */	
	static transient final String NAME = "Box"; 

	double[] minBoundaries;
	double[] maxBoundaries;

	/**
	 * Initialize this space by giving its boundaries.
	 *
	 * @param minBoundaries minimum boundary values of the N intervals
	 * @param maxBoundaries maximum boundary values of the N intervals
	 */
	public RLBoxSpace(double[] minBoundaries, double[] maxBoundaries) {
		super(NAME);
		this.minBoundaries = minBoundaries;
		this.maxBoundaries = maxBoundaries;
	}

	/**
	 * Initialize this space with single boundary values and a vector size.
	 *
	 * @param low minimum boundary value (common for all dimensions)
	 * @param high maximum boundary value (common for all dimensions)
	 * @param N dimension of one element of this space
	 */
	public RLBoxSpace(double low, double high, int N) {
		super(NAME);
		this.minBoundaries = new double[N];
		this.maxBoundaries = new double[N];
		Arrays.fill(this.minBoundaries, low);
		Arrays.fill(this.maxBoundaries, high);
	}

	/**
	 * Get a random element from this space.
	 * Uniform sampling is performed.
	 *
	 * @param rnd random generator.
	 * @return one random [x_0, x_1, ..., x_N-1] vector.
	 */
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
