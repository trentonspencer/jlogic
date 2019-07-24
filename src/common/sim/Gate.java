package common.sim;

/**
 * A sim instance of a {@link GateComponent}
 */
public class Gate
{
	/**
	 * The GateComponent from which it will get its basic information
	 */
	private final GateComponent component_;
	
	/**
	 * The Pins that belong to this Gate
	 */
	private final Pin[] pins_;
	
	/**
	 * x position in the sim
	 */
	private int x_;
	
	/**
	 * y position in the sim
	 */
	private int y_;
	
	/**
	 * @param component {@link Gate#component_}
	 * @param x {@link Gate#x_}
	 * @param y {@link Gate#y_}
	 */
	public Gate(GateComponent component, int x, int y) {
		component_ = component;
		x_ = x;
		y_ = y;
		
		PinComponent[] pinComps = component.getPins();
		pins_ = new Pin[pinComps.length];
		
		for(int i = 0; i < pinComps.length; ++i) {
			pins_[i] = new Pin(pinComps[i], this);
		}
		
		update(); //force a logic update for inverted pins such as on the NOT
	}
	
	/**
	 * @return {@link Gate#component_}
	 */
	public GateComponent getGateComponent() {
		return component_;
	}

	/**
	 * @return {@link Gate#pins_}
	 */
	public Pin[] getPins() {
		return pins_;
	}
	
	/**
	 * @return {@link Gate#x_}
	 */
	public int getX() {
		return x_;
	}
	
	/**
	 * @return {@link Gate#y_}
	 */
	public int getY() {
		return y_;
	}
	
	/**
	 * Causes the Gate to queue itself in the sim
	 */
	public void update() {
		Simulation.queueGate(this);
	}
	
	/**
	 * @return The width of its GateComponent
	 */
	public int getWidth() {
		return component_.getWidth();
	}
	
	/**
	 * @return The height of its GateComponent
	 */
	public int getHeight() {
		return component_.getHeight();
	}
	
	/**
	 * Destroys all Wire connections and removes itself from the sim
	 */
	public void remove() {
		for(Pin pin : pins_) {
			if(pin.getWire() != null)
				pin.getWire().remove();
		}
		
		Simulation.removeGate(this);
	}
	
	/**
	 * Prints out current Pin states
	 */
	public void debug() {
		for(Pin pin : pins_)
			System.out.println(pin.getPinComponent().getLabel() + ": " + pin.getState());
	}
}
