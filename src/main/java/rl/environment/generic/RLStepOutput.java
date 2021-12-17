package rl.environment.generic;

import java.util.Map;

/**
 * Generic model of a Gym interface's step method output.
 *
 * @param <ObservationType> type of the RL Observation (or state).
 */
public class RLStepOutput<ObservationType> {
	
	private final ObservationType nextObservation;
	private final double reward;
	private final boolean done;
	private final Map<String, String> info;

	/**
	 * Constructor.
	 *
	 * @param nextObservation environment observation at the end of the step.
	 * @param reward reward received.
	 * @param done whether the environment end condition is fulfilled
	 * @param info additional information on the environment, as a key-value map.
	 */
	public RLStepOutput(ObservationType nextObservation, double reward, boolean done, Map<String, String> info) {
		super();
		this.nextObservation = nextObservation;
		this.reward = reward;
		this.done = done;
		this.info = info;
	}

	/**
	 * Get this step's observation.
	 *
	 * @return RL Observation.
	 */
	public ObservationType getNextObservation() {
		return nextObservation;
	}

	/**
	 * Get this step's reward.
	 *
	 * @return reward.
	 */
	public double getReward() {
		return reward;
	}

	/**
	 * Get whether this episode is over.
	 *
	 * @return done.
	 */
	public boolean isDone() {
		return done;
	}

	/**
	 * Get the current additional information on the RL Environment.
	 *
	 * @return info.
	 */
	public Map<String, String> getInfo() {
		return info;
	}
	
	
}