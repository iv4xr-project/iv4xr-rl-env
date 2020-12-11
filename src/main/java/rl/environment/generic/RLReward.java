package rl.environment.generic;

@FunctionalInterface
public interface RLReward<RLObsType, RLActionType> {
	public double computeReward(RLObsType state, RLActionType action, RLObsType nextState);
}
