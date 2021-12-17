package rl.environment.intrusion;

import rl.environment.generic.RLReward;

public class RLDummyReward implements RLReward<RLIntrusionObservation, RLMoveAction> {

	@Override
	public double computeReward(RLIntrusionObservation state, RLMoveAction action, RLIntrusionObservation nextState) {
		// Dumb placeholder reward: we check that the destination suggested by the action is reached
		double[] rawAction = action.getRawAction();
		double[] rawNextObs = nextState.getRawObservation().get("intruder");
		boolean destinationReached = true;
		for (int i = 0; i < 2; ++i) {
			destinationReached &= Math.abs(rawAction[i] - rawNextObs[i]) < 1e-3;
		}
		return destinationReached ? 1.0 : 0.0;
	}

	
	
}
