package rl.environment.generic;

public class RLDiscreteActionSpace extends RLActionSpace<Integer> {

	int numberOfActions;
	
	public RLDiscreteActionSpace(int numberOfActions) {
		this.numberOfActions = numberOfActions;
	}

	@Override
	public Integer sample() {
		return rnd.nextInt(this.numberOfActions);
	}

}
