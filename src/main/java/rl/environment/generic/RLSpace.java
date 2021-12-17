package rl.environment.generic;

import java.io.Serializable;
import java.util.Random;

/**
 * This class is the base for the generic representation of RL Action and
 * Observation spaces representation. It also helps with interoperability
 * with Python Gym spaces.
 *
 * @param <ElementType> element type in this space.
 */
public abstract class RLSpace<ElementType extends Serializable> {
	/**
	 * Description name to help with OpenAI Gym interfaces
	 */
	public String name;

	/**
	 * Constructor.
	 *
	 * @param name this space name.
	 */
	public RLSpace(String name) {
		super();
		this.name = name;
	}

	/**
	 * Get a random element from this space.
	 * Uniform sampling is privileged.
	 *
	 * @param rnd random generator.
	 * @return a random element from this space.
	 */
	public abstract ElementType sample(Random rnd);	
}
