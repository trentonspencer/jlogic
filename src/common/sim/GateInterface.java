package common.sim;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

/**
 * Events that a Gate will have occur
 */
public interface GateInterface {
	/**
	 * Performs the logic of the GateComponent
	 * @param pins The pins of a Gate
	 */
	public void performLogic(Pin[] pins);
	
	/**
	 * Allows a GateComponent to render custom graphics if needed
	 * @param g The Graphics context to draw on
	 * @param pins The pins of a Gate (null when the user is placing a new Gate)
	 */
	public void draw(Graphics g, Pin[] pins);
	
	/**
	 * Callback for when a gate is clicked by the user
	 * @param e The MouseEvent that occurred
	 * @param pins The Pins of the Gate affected
	 */
	public void onMouseClicked(MouseEvent e, Pin[] pins);
	
	/**
	 * Callback for when a gate is pressed on by the user
	 * @param e The MouseEvent that occurred
	 * @param pins The Pins of the Gate affected
	 */
	public void onMousePressed(MouseEvent e, Pin[] pins);
	
	/**
	 * Callback for when the mouse is released after having been pressed on a Gate
	 * @param e The MouseEvent that occurred
	 * @param pins The Pins of the Gate affected
	 */
	public void onMouseReleased(MouseEvent e, Pin[] pins);
}
