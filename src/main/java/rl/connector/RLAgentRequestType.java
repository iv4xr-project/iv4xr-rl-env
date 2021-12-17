package rl.connector;

/**
 * Enumeration of the requests/responses when connecting to a Python RL Agent.
 */
public enum RLAgentRequestType {
	ENV_SPEC,
	GET_ACTION,
	LOG_RETURNS,
	DISCONNECT,
	RESET,
	STEP,
	GET_SPEC,
}
