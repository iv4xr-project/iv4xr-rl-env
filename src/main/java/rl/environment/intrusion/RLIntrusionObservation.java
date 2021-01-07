package rl.environment.intrusion;

import eu.iv4xr.framework.spatial.Vec3;
import rl.environment.generic.RLObservation;
import world.LegacyObservation;

public class RLIntrusionObservation extends RLObservation<double[], LegacyObservation> {

	public RLIntrusionObservation(LegacyObservation womState) {
		super(womState);
	}

	@Override
	public RLIntrusionObservation clone() {
		return new RLIntrusionObservation(this.getWomState());
	}

	@Override
	public double[] initRawObservation() {
		Vec3 agentPosition = getWomState().agentPosition;
		double[] raw = { agentPosition.x, agentPosition.y };
		return raw;	
	}

}
