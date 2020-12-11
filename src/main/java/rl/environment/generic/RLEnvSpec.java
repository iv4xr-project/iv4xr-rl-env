package rl.environment.generic;

public class RLEnvSpec<ActionSpaceType> {
	String envName;
	ActionSpaceType actionSpace;
	
	public RLEnvSpec(String envName, ActionSpaceType actionSpace) {
		this.envName = envName;
		this.actionSpace = actionSpace;
		
	}
}
