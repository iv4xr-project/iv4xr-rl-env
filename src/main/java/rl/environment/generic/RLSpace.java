package rl.environment.generic;

import java.io.Serializable;
import java.util.Random;

public abstract class RLSpace<ElementType extends Serializable> {
	/**
	 * Description name to help with OpenAI Gym interfaces
	 */
	public String name;

	public RLSpace(String name) {
		super();
		this.name = name;
	}
	
	public abstract ElementType sample(Random rnd);	
}
