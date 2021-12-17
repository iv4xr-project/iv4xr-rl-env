package rl.environment.generic;

/**
 * Specifies a RL environment Observation (output) and Action (input) format.
 * This way, a generic DRL algorithm can be implemented on this environment.
 *
 * @param <ActionSpaceType> type of the action space.
 * @param <ObservationSpaceType> type of the observation space.
 */
public class RLEnvSpec<ActionSpaceType, ObservationSpaceType> {
	String envName;
	ActionSpaceType actionSpace;
	ObservationSpaceType observationSpace;

	/**
	 * Constructor.
	 *
	 * @param envName name of this environment.
	 * @param actionSpace action space.
	 * @param observationSpace observation space.
	 */
	public RLEnvSpec(String envName, ActionSpaceType actionSpace, ObservationSpaceType observationSpace) {
		this.envName = envName;
		this.actionSpace = actionSpace;
		this.observationSpace = observationSpace;
	}
}
