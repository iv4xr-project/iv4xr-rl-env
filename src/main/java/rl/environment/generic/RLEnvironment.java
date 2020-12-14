package rl.environment.generic;

public abstract class RLEnvironment<ActionType, ObservationType, RewardType, EndConditionType, ActionSpaceType> {
	
	protected RLEnvSpec<ActionSpaceType> envSpec;
	protected RewardType rewardFunction;
	protected EndConditionType endCondition;
	
	protected ObservationType initialObservation;
	protected ObservationType currentObservation;
	
	public abstract ObservationType reset();
	public abstract RLStepOutput<ObservationType> step(ActionType action);
	
	public RLEnvironment(RLEnvSpec<ActionSpaceType> envSpec) {
		super();
		this.envSpec = envSpec;
	}
	
}
