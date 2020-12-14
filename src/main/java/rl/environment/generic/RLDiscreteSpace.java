package rl.environment.generic;

import java.util.Random;

public class RLDiscreteSpace extends RLSpace<Integer> {
	/**
	 * Description name to help with OpenAI Gym interfaces
	 */
	static transient final String NAME = "Discrete"; 
	
	int numberOfActions;
	
	public RLDiscreteSpace(int numberOfActions) {
		super(NAME);
		this.numberOfActions = numberOfActions;
	}	
	
	@Override
	public Integer sample(Random rnd) {
		return rnd.nextInt(this.numberOfActions);
	}

}
