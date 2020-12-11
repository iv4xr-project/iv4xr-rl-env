package rl.environment.generic;

public interface RLEndCondition<ObservationType> {
	public boolean isDone(ObservationType state);

}
