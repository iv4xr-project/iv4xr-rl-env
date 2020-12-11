package rl.environment.generic;

public abstract class RLObservation<RawObsType, WomType> {
	// The WOM state is kept to ease cloning of observations or to help compute rewards
	protected transient WomType womState;
	protected RawObsType rawObservation;
	
	public RLObservation(WomType womState) {
		this.womState = womState;
		this.rawObservation = initRawObservation();
	}
	
	public abstract RLObservation<RawObsType, WomType> clone();
	public abstract RawObsType initRawObservation();
	public RawObsType getRawObservation() {
		return rawObservation;
	}

	public WomType getWomState() {
		return womState;
	}
}
