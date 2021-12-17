package rl.environment.generic;

/**
 * Generic observation (or state) class of a RL Agent on the iv4xr framework.
 *
 * @author Alexandre Kazmierowski
 *
 * @param <RawObsType> representation type of the RL observation.
 * @param <WomType> World Object Model type of the targeted SUT.
 */
public abstract class RLObservation<RawObsType, WomType> {
	// The WOM state is kept to ease cloning of observations or to help compute rewards
	protected transient WomType womState;
	protected RawObsType rawObservation;

	/**
	 * Construct a RL observation from a World Object Model state.
	 *
	 * @param womState current state of the World Object Model.
	 */
	public RLObservation(WomType womState) {
		this.womState = womState;
		this.rawObservation = initRawObservation();
	}

	/**
	 * Clone this observation.
	 * To override in sub-classes.
	 *
	 * @return a new, identical RL Observation.
	 */
	public abstract RLObservation<RawObsType, WomType> clone();

	/**
	 * Initialize the raw RL observation from the World Object Model state.
	 * To override in sub-classes.
	 */
	public abstract RawObsType initRawObservation();

	/**
	 * Accessor to the raw observation
	 *
	 * @return raw RL observation.
	 */
	public RawObsType getRawObservation() {
		return rawObservation;
	}

	/**
	 * Accessor to the World Object Model state.
	 *
	 * @return World Object Model state.
	 */
	public WomType getWomState() {
		return womState;
	}
}
