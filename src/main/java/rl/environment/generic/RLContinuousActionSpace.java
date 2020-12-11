package rl.environment.generic;

public class RLContinuousActionSpace extends RLActionSpace<double[]> {

	double[] minBoundaries;
	double[] maxBoundaries;
	
	public RLContinuousActionSpace(double[] minBoundaries, double[] maxBoundaries) {
		this.minBoundaries = minBoundaries;
		this.maxBoundaries = maxBoundaries;
	}

	@Override
	public double[] sample() {
		var randomActions = new double[minBoundaries.length];
		for (int i = 0; i < minBoundaries.length; i++) {
			double min = minBoundaries[i];
			double max = maxBoundaries[i];
			randomActions[i] = min + (max - min) * rnd.nextDouble();
		}
		return randomActions;
	}

}
