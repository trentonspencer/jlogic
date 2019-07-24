package common.sim;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;

import common.sim.events.SimulationListener;

/**
 * Main container for Gates and Wires and handles the sim logic
 */
public class Simulation {
	/**
	 * The current tick of the sim
	 */
	private static int currentTick_ = 0;
	
	/**
	 * The Wires/Gates added by the user that can be undone
	 */
	private static Stack<Object> undoStack_ = new Stack<>();
	
	/**
	 * A Vector that holds all Gates
	 */
	private static Vector<Gate> gates_ = new Vector<>();
	
	/**
	 * A Vector that holds all Wires
	 */
	private static Vector<Wire> wires_ = new Vector<>();
	
	/**
	 * A Queue that stores all Gates needed to be checked for the next tick
	 */
	private static Queue<Gate> gateQueue_ = new LinkedList<>();
	
	/**
	 * A Queue that stores all Wires needed to be checked for the next tick
	 */
	private static Queue<Wire> wireQueue_ = new LinkedList<>();
	
	/**
	 * Provides event-based callbacks when certain things occur
	 */
	private static SimulationListener listener_;
	
	/**
	 * Removes all Gates and Wires from the sim and queues
	 */
	public static void clear() {
		gates_.clear();
		wires_.clear();
		gateQueue_.clear();
		wireQueue_.clear();
	}
	
	/**
	 * @return {@link Simulation#currentTick_}
	 */
	public static int getCurrentTick() {
		return currentTick_;
	}
	
	/**
	 * Adds a Gate to the sim and the undo stack
	 * @param gate The Gate to be added
	 */
	public static void addGate(Gate gate) {
		gates_.add(gate);
		undoStack_.push(gate);
	}
	
	/**
	 * Adds a Wire to the sim and the undo stack
	 * @param wire The Wire to be added
	 */
	public static void addWire(Wire wire) {
		wires_.add(wire);
		undoStack_.push(wire);
	}
	
	/**
	 * Undoes the most recently added Wire/Gate
	 */
	public static void undo() {
		if(!undoStack_.isEmpty()) {
			Object obj = undoStack_.pop();
			if(obj instanceof Gate)
				((Gate)obj).remove();
			else
				((Wire)obj).remove();
		}
	}
	
	/**
	 * Removes a Gate from the sim
	 * @param gate The Gate to be removed
	 */
	public static void removeGate(Gate gate) {
		gates_.remove(gate);
	}
	
	/**
	 * Removes a Wire from the sim
	 * @param wire The Wire to be removed
	 */
	public static void removeWire(Wire wire) {
		wires_.remove(wire);
	}
	
	/**
	 * @return {@link Simulation#gates_}
	 */
	public static Vector<Gate> getGates() {
		return gates_;
	}
	
	/**
	 * @return {@link Simulation#wires_}
	 */
	public static Vector<Wire> getWires() {
		return wires_;
	}
	
	/**
	 * Queues a Wire for the next tick
	 * @param wire The Wire to queue
	 */
	public static void queueWire(Wire wire) {
		wireQueue_.offer(wire);
	}
	
	/**
	 * Queues a Gate for the next tick
	 * @param gate The Gate to queue
	 */
	public static void queueGate(Gate gate) {
		gateQueue_.offer(gate);
	}
	
	/**
	 * Attempts to locate a Gate at Point p
	 * @param p The Point to search for a Gate at
	 * @return A Gate that exists at Point p, null otherwise
	 */
	public static Gate findGateAt(Point p) {
		int x = p.x;
		int y = p.y;
		
		int gx;
		int gy;
		int gxx;
		int gyy;
		for(Gate gate : gates_) {
			gx = gate.getX();
			gy = gate.getY();
			gxx = gx+gate.getGateComponent().getWidth();
			gyy = gy+gate.getGateComponent().getHeight();
			
			if(x >= gx && x <= gxx && y >= gy && y <= gyy)
				return gate;
		}
		
		return null;
	}
	
	/**
	 * Tries to find a Pin at Point p
	 * @param p The Point to search for a Pin at
	 * @return A Pin that exists at Point p, null otherwise
	 */
	public static Pin findPinAt(Point p) {
		Pin fpin = null;
		Gate gate = Simulation.findGateAt(p);
		if(gate != null) {
			for(Pin pin : gate.getPins()) {
				if(pin.getPoint().equals(p)) {
					fpin = pin;
					break;
				}
			}
		}
		
		return fpin;
	}
	
	/**
	 * Tries to find a Wire at Point p
	 * @param p The Point to search for a Wire at
	 * @return A Wire that exists at Point p, null otherwise
	 */
	public static Wire findWireAt(Point p) {
		for(Wire wire : wires_) {
			if(wire.getStartPoint().equals(p) || wire.getEndPoint().equals(p))
				return wire;
		}
		return null;
	}
	
	/**
	 * Sets the SimulationListener
	 * @param listener The listener to set as the active listener
	 */
	public static void setSimulationListener(SimulationListener listener) {
		listener_ = listener;
	}
	
	/**
	 * Ticks the sim, causing Gates and Wires to update and render the canvas
	 */
	public static void tick() {
		for(Wire wire : wireQueue_)
			wire.getInputPin().setInputState(wire.getOutputPin().getState());
		wireQueue_.clear();
		
		for(Gate gate : gateQueue_)
			gate.getGateComponent().performLogic(gate.getPins());
		gateQueue_.clear();
		
		if(listener_ != null)
			listener_.onRequestRepaint();
		
		++currentTick_;
	}
}
