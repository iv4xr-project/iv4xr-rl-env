package rl.environment.generic;

public class RLEnvSpec<ActionSpaceType, ObservationSpaceType> {
	String envName;
	ActionSpaceType actionSpace;
	ObservationSpaceType observationSpace;
	
	public RLEnvSpec(String envName, ActionSpaceType actionSpace, ObservationSpaceType observationSpace) {
		this.envName = envName;
		this.actionSpace = actionSpace;
		this.observationSpace = observationSpace;
	}
}
