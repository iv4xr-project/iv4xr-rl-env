package rl.environment.generic;

/**
 * Generic RL reward function interface.
 *
 * @param <RLObsType> type of the RL Observation (or state).
 * @param <RLActionType> type of the RL Action.
 */
@FunctionalInterface
public interface RLReward<RLObsType, RLActionType> {
	/**
	 * Generic RL reward function interface. Compute the reward from the
	 * (state, action, next_state) triplet of a RL environment transition.
	 *
	 * @param state start state of the transition.
	 * @param action action taken by the agent.
	 * @param nextState end state of the transition.
	 * @return reward value.
	 */
	double computeReward(RLObsType state, RLActionType action, RLObsType nextState);
}
