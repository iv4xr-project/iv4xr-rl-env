package rl.environment.generic;

public abstract class RLAction<ActionType, CommandType> {
	protected ActionType rawAction;
	
	public RLAction(ActionType rawAction) {
		super();
		this.rawAction = rawAction;
	}
	public abstract CommandType asCommand(int agentId);
	public ActionType getRawAction() {
		return rawAction;
	}
}
