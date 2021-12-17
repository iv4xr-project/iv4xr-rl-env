package rl.environment.generic;

import java.util.Random;

/**
 * The discrete space represents the {0, 1, ..., N-1} ensemble.
 */
public class RLDiscreteSpace extends RLSpace<Integer> {
	/**
	 * Description name to help with OpenAI Gym interfaces
	 */
	static transient final String NAME = "Discrete";

	/**
	 * This space's cardinal.
	 */
	int numberOfElements;

	/**
	 * Builds a discrete space.
	 *
	 * @param numberOfElements number of elements: strictly positive integer.
	 */
	public RLDiscreteSpace(int numberOfElements) {
		super(NAME);
		this.numberOfElements = numberOfElements;
	}

	/**
	 * Get a random element from this space.
	 * Uniform sampling is privileged.
	 *
	 * @param rnd random generator.
	 * @return a random element from this space.
	 */
	@Override
	public Integer sample(Random rnd) {
		return rnd.nextInt(this.numberOfElements);
	}

}
