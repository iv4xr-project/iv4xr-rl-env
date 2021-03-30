package rl.environment.generic;

public abstract class RLEnvironment<ActionType, ObservationType, RewardType, EndConditionType, ActionSpaceType, ObservationSpaceType> {
	
	protected RLEnvSpec<ActionSpaceType, ObservationSpaceType> envSpec;
	protected RewardType rewardFunction;
	protected EndConditionType endCondition;
	
	protected ObservationType initialObservation;
	protected ObservationType currentObservation;
	
	public abstract ObservationType reset();
	public abstract RLStepOutput<ObservationType> step(ActionType action);
	
	public RLEnvironment(RLEnvSpec<ActionSpaceType, ObservationSpaceType> envSpec) {
		super();
		this.envSpec = envSpec;
	}

	public ActionSpaceType getActionSpace() {
		return envSpec.actionSpace;
	}

	public ObservationSpaceType getObservationSpace() {
		return envSpec.observationSpace;
	}
}
