package rl.environment.generic;

/**
 * Generic RL Environment end condition, that shall trigger a reset of the
 * RL Environment.
 *
 * @param <ObservationType> RL Observation (or state) type.
 */
public interface RLEndCondition<ObservationType> {
	/**
	 * Check whether a RL Environment episode of train/test is over.
	 *
	 * @param state environment state evaluated
	 * @return whether the episode has ended.
	 */
	boolean isDone(ObservationType state);

}
