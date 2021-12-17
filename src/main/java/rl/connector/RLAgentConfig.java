package rl.connector;

/**
 * Configuration of the connection to the Python RL Agent.
 */
public class RLAgentConfig {
	public transient String host = "localhost";
	public transient int port = 5555;

	public RLAgentConfig(){}
}
