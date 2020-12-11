package rl.environment.generic;

import java.util.Map;

public class RLStepOutput<ObservationType> {
	
	private ObservationType nextObservation;
	private double reward;
	private boolean done;
	private Map<String, String> info;
	
	public RLStepOutput(ObservationType nextObservation, double reward, boolean done, Map<String, String> info) {
		super();
		this.nextObservation = nextObservation;
		this.reward = reward;
		this.done = done;
		this.info = info;
	}
	
	public ObservationType getNextObservation() {
		return nextObservation;
	}
	public double getReward() {
		return reward;
	}
	public boolean isDone() {
		return done;
	}
	public Map<String, String> getInfo() {
		return info;
	}
	
	
}