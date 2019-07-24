package common.sim;

import java.awt.Point;

/**
 * Allows connecting Pins together
 */
public class Wire {
	/**
	 * The start Point of a Wire
	 */
	private Point start_;
	
	/**
	 * The end Point of a Wire
	 */
	private Point end_;
	
	/**
	 * The input Pin connected to the Wire
	 */
	private Pin input_;
	
	/**
	 * The output Pin connected to the WIre
	 */
	private Pin output_;
	
	/**
	 * @param start {@link Wire#start_}
	 * @param end {@link Wire#end_}
	 */
	public Wire(Point start, Point end) {
		start_ = start;
		end_ = end;
		input_ = null;
		output_ = null;
	}
	
	/**
	 * Sets both {@link Wire#start_} and {@link Wire#end_} to p
	 * @param p
	 */
	public Wire(Point p) {
		this(p, p);
	}
	
	/**
	 * @return {@link Wire#end_}
	 */
	public Point getEndPoint() {
		return end_;
	}
	
	/**
	 * Sets the {@link Wire#end_} Point
	 * @param p
	 */
	public void setEndPoint(Point p) {
		end_ = p;
	}
	
	/**
	 * @return {@link Wire#start_}
	 */
	public Point getStartPoint() {
		return start_;
	}
	
	/**
	 * @return {@link Wire#input_}
	 */
	public Pin getInputPin() {
		return input_;
	}

	/**
	 * @return {@link Wire#output_}
	 */
	public Pin getOutputPin() {
		return output_;
	}
	
	/**
	 * Causes the Wire to queue itself in the sim
	 */
	public void update() {
		Simulation.queueWire(this);
	}
	
	/**
	 * Destroys all Pin connections and removes itself from the sim
	 */
	public void remove() {
		if(input_ != null) {
			input_.setWire(null);
			input_.setInputState(false);
		}
		if(output_ != null) {
			output_.setWire(null);
			output_.setState(false);
		}
		
		input_ = null;
		output_ = null;
		Simulation.removeWire(this);
	}
	
	/**
	 * Tries to connect two Pins of differing types to itself
	 * @return True if successful, false otherwise
	 */
	public boolean tryConnection() {
		Pin spin = Simulation.findPinAt(getStartPoint());
		Pin epin = Simulation.findPinAt(getEndPoint());
		
		if(spin != null && epin != null && spin.getType() != epin.getType()) {
			if(epin.getWire() != null)
				epin.getWire().remove();
			if(spin.getWire() != null)
				spin.getWire().remove();
			
			spin.setWire(this);
			epin.setWire(this);
			if(spin.getType() == PinType.INPUT) {
				input_ = spin;
				output_ = epin;
			}
			else {
				input_ = epin;
				output_ = spin;
			}
			
			update();
			return true;
		}
		
		return false;
	}
}
