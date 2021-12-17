package rl.environment.intrusion;

import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.spatial.Vec3;
import intrusionSimulation.ISObservation;
import rl.environment.generic.RLObservation;

import java.util.HashMap;

/**
 * Observation for the Intruder RL Agent.
 */
public class RLIntrusionObservation extends RLObservation<HashMap<String, double[]>, ISObservation> {
	private class LocalObservationStack {
		// Objective:
		// Limit the capacity of the local observations
		// Preserve the order of an observed entity between 2 consecutive observations
		// TODO: implement this
	}

	private double computeOrientationFromVelocity(Vec3 velocity) {
		return Math.atan2(velocity.y, velocity.x);
	}

	public RLIntrusionObservation(ISObservation womState) {
		super(womState);
	}

	@Override
	public RLIntrusionObservation clone() {
		return new RLIntrusionObservation(this.getWomState());
	}

	@Override
	public HashMap<String, double[]> initRawObservation() {
		Vec3 agentPosition = getWomState().position;
		double orientation = computeOrientationFromVelocity(getWomState().velocity);
		double[] intruderObservation = { agentPosition.x, agentPosition.y, orientation };

		double[] guardObservation = new double[5 * RLIntrusionConfig.numGuards];
		double[] cameraObservation = new double[5 * RLIntrusionConfig.numCameras];
		int guardIndex = 0;
		int cameraIndex = 0;
		for (WorldEntity detectedEntity : getWomState().elements.values()) {
			if (detectedEntity.id.contains("FixedGuard")) {
				double[] raw = {
						detectedEntity.position.x,
						detectedEntity.position.y,
						0.0,
						0.0,
						RLIntrusionConfig.cameraVisionRange
				};
				System.arraycopy(raw, 0, cameraObservation, cameraIndex * 5, 5);
				cameraIndex++;
			} else if (detectedEntity.id.contains("Guard")) {
				orientation = computeOrientationFromVelocity(detectedEntity.velocity);
				double[] raw = {
						detectedEntity.position.x,
						detectedEntity.position.y,
						orientation, 0.0,
						RLIntrusionConfig.guardVisionRange
				};
				System.arraycopy(raw, 0, guardObservation, guardIndex * 5, 5);
				guardIndex++;
			} else {
				throw new IllegalArgumentException("Unexpected detected entity" + detectedEntity.id);
			}
		}

		var observation = new HashMap<String, double[]>();
		observation.put("intruder", intruderObservation);
		observation.put("goal", RLIntrusionConfig.goalPosition);
		observation.put("guard", guardObservation);
		observation.put("fixed_guard", cameraObservation);
		return observation;
	}

}
