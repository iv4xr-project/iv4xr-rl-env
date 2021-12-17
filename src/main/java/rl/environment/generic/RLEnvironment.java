package rl.environment.generic;

/**
 * Base class for an RL Environment that implements the Gym Interface.
 * All its types and subcomponents must be implemented as sub-classes of the
 * relevant classes from this generic package.
 *
 * An example of specification is provided in the intrusion package, to
 * model the scenario of the Powerplant Intrusion simulation.
 *
 *
 * @param <ActionType> type of the RL Action.
 * @param <ObservationType> type of the RL Observation (or state).
 * @param <RewardType> type of the RL Reward.
 * @param <EndConditionType> type of the RL End Condition.
 * @param <ActionSpaceType> type of the Action Space.
 * @param <ObservationSpaceType> type of the Observation Space.
 */
public abstract class RLEnvironment<ActionType, ObservationType, RewardType, EndConditionType, ActionSpaceType, ObservationSpaceType> {
	
	protected RLEnvSpec<ActionSpaceType, ObservationSpaceType> envSpec;
	protected RewardType rewardFunction;
	protected EndConditionType endCondition;
	
	protected ObservationType initialObservation;
	protected ObservationType currentObservation;

	/**
	 * Reset this environment to its initial state.
	 *
	 * @return initial observation.
	 */
	public abstract ObservationType reset();

	/**
	 * Execute one decision step in this environment, and get its output as
	 * next state, reward, done, info.
	 *
	 * @param action action provided by the RL Agent.
	 * @return step output as next observation, reward, done and info.
	 */
	public abstract RLStepOutput<ObservationType> step(ActionType action);

	/**
	 * Construct an empty environment.
	 *
	 * @param envSpec specification of this environment, with its name, action and
	 *  			  observation spaces.
	 */
	public RLEnvironment(RLEnvSpec<ActionSpaceType, ObservationSpaceType> envSpec) {
		super();
		this.envSpec = envSpec;
	}

	/**
	 * Get the action space of this environment.
	 *
	 * @return action space.
	 */
	public ActionSpaceType getActionSpace() {
		return envSpec.actionSpace;
	}

	/**
	 * Get the observation space of this environment.
	 *
	 * @return observation space.
	 */
	public ObservationSpaceType getObservationSpace() {
		return envSpec.observationSpace;
	}
}
