package rl.environment.intrusion;

import eu.iv4xr.framework.spatial.Vec3;
import rl.environment.generic.RLObservation;
import world.LegacyEntity;
import world.LegacyObservation;

public class RLIntrusionObservation extends RLObservation<double[], LegacyObservation> {
	//public static int numDetections = 5;
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
		double[] raw = RLIntrusionConfig.computeObservationMin();
		raw[0] = agentPosition.x;
		raw[1] = agentPosition.y;

		int i = 0;
		for (LegacyEntity detectedEntity : getWomState().entities) {
			raw[2 + 2*i] = detectedEntity.position.x;
			raw[2 + 2*i+1] = detectedEntity.position.y;
			i += 1;
			if (i == RLIntrusionConfig.numDetections)
				break;
		}

		return raw;
	}

/*	@Override
	public HashMap<String, double[]> initRawObservation() {
		Vec3 agentPosition = getWomState().agentPosition;
		double[] raw = { agentPosition.x, agentPosition.y };

		double[] detections = new double[2*numDetections];
		int i = 0;
		for (LegacyEntity detectedEntity : getWomState().entities) {
			detections[2*i] = detectedEntity.position.x;
			detections[2*i+1] = detectedEntity.position.y;
			i += 1;
			if (i == numDetections)
				break;
		}

		var observation = new HashMap<String, double[]>();
		observation.put("internalState", raw);
		observation.put("detections", detections);
		return observation;
	}*/

}
