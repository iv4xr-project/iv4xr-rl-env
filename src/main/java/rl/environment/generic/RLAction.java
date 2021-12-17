package rl.environment.generic;

/**
 * Generic action class of a RL Agent on the iv4xr framework.
 * This needs to be subclassed when handling a SUT-specific problem.
 *
 * @author Alexandre Kazmierowski
 *
 * @param <ActionType> representation type of the RL action.
 * @param <CommandType> type of the command sent to the iv4xr SUT.
 */
public abstract class RLAction<ActionType, CommandType> {
	protected ActionType rawAction;

	/**
	 * Constructor.
	 *
	 * @param rawAction action, with the raw RL representation.
	 */
	public RLAction(ActionType rawAction) {
		super();
		this.rawAction = rawAction;
	}

	/**
	 * Translate the action as a command for the iv4xr SUT.
	 * To override in sub-classes.
	 *
	 * @param agentId Identifier of the controlled agent.
	 * @return rawAction translated as a SUT command.
	 */
	public abstract CommandType asCommand(int agentId);

	/**
	 * Accessor to the raw action.
	 *
	 * @return raw RL action.
	 */
	public ActionType getRawAction() {
		return rawAction;
	}
}
