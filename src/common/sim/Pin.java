package common.sim;

import java.awt.Point;

/**
 * A sim instance of a PinComponent
 */
public class Pin {
	/**
	 * The PinComponent from which it will get its basic information
	 */
	private PinComponent component_;
	
	/**
	 * The Gate this Pin belongs to
	 */
	private Gate gate_;
	
	/**
	 * The Wire this pin is attached to
	 */
	private Wire wire_;
	
	/**
	 * The current state of this Pin
	 */
	private boolean state_;
	
	/**
	 * The Pin's position in the sim
	 */
	private Point point_;
	
	/**
	 * @param component {@link Pin#component_}
	 * @param gate {@link Pin#gate_}
	 */
	public Pin(PinComponent component, Gate gate) {
		component_ = component;
		gate_ = gate;
		wire_ = null;
		state_ = false;
		
		Point offset = component.getOffset();
		point_ = new Point(gate.getX()+offset.x, gate.getY()+offset.y);
	}
	
	/**
	 * @return The PinType of this Pin's {@link Pin#component_}
	 */
	public PinType getType() {
		return component_.getType();
	}
	
	/**
	 * @return {@link Pin#state_}
	 */
	public boolean getState() {
		return state_;
	}
	
	/**
	 * Sets the state of this Pin if it differs and updates any attached Wire
	 * @param state The state to change the Pin's {@link Pin#state_} to
	 */
	public void setState(boolean state) {
		if(state_ != state) {
			state_ = state;
			if(wire_ != null)
				wire_.update();
		}
	}
	
	/**
	 * Sets the state of a Pin of type {@link PinType#INPUT}
	 * @param state The state to change the Pin's {@link Pin#state_} to
	 */
	public void setInputState(boolean state) {
		if(state_ != state) {
			state_ = state;
			
			if(component_.doesCauseUpdate())
				gate_.update();
		}
	}
	
	/**
	 * @return {@link Pin#gate_}
	 */
	public Gate getGate() {
		return gate_;
	}

	/**
	 * @return {@link Pin#wire_}
	 */
	public Wire getWire() {
		return wire_;
	}
	
	/**
	 * Sets the Pin's {@link Pin#wire_}
	 * @param wire
	 */
	public void setWire(Wire wire) {
		wire_ = wire;
	}
	
	/**
	 * @return {@link Pin#point_}
	 */
	public Point getPoint() {
		return point_;
	}
	
	/**
	 * @return {@link Pin#component_}
	 */
	public PinComponent getPinComponent() {
		return component_;
	}
}
