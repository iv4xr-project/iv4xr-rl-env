package rl.environment.intrusion;

import rl.environment.generic.RLEndCondition;

public class RLDummyEndCondition implements RLEndCondition<RLIntrusionObservation> {

	@Override
	public boolean isDone(RLIntrusionObservation state) {
		return false;
	}

}
