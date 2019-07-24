package window;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import common.sim.Constants;
import common.sim.Gate;
import common.sim.GateComponent;
import common.sim.Pin;
import common.sim.PinComponent;
import common.sim.PinType;
import common.sim.Simulation;
import common.sim.Wire;
import common.sim.events.SimulationListener;

/**
 * Handles user interactivity and rendering
 */
@SuppressWarnings("serial")
public class JLogicPanel extends JPanel {
	/**
	 * The current GateComponent being added by the user
	 */
	private GateComponent userGateComponent_;
	
	/**
	 * The grid-locked mouse position of the user
	 */
	private Point userPoint_;
	
	/**
	 * The currently selected Gate
	 */
	private Gate selectedGate_;
	
	/**
	 * The Pin currently being hovered over by the mouse
	 */
	private Pin highlightedPin_;
	
	/**
	 * The Wire currently being added by the user
	 */
	private Wire userWire_;
	
	public JLogicPanel() {
		super();
		
		userPoint_ = new Point(0, 0);
		
		MouseAdapter adapter = new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				userPoint_.setLocation(roundToGrid(e.getPoint()));
				
				if(userGateComponent_ == null)
					highlightedPin_ = Simulation.findPinAt(userPoint_);
				
				repaint();
			}
			
			public void mousePressed(MouseEvent e) {
				if(userGateComponent_ == null) {
					if(highlightedPin_ != null)
						userWire_ = new Wire(highlightedPin_.getPoint());
					else {
						selectedGate_ = Simulation.findGateAt(userPoint_);
						if(selectedGate_ != null) {
							selectedGate_.getGateComponent().onMousePressed(e, selectedGate_.getPins());
							selectedGate_.getGateComponent().onMouseClicked(e, selectedGate_.getPins());
						}
					}
					
					repaint();
				}
			}
			
			public void mouseReleased(MouseEvent e) {
				if(userGateComponent_ != null) {
					int x = userPoint_.x;
					int y = userPoint_.y;
					int xx = x+userGateComponent_.getWidth();
					int yy = y+userGateComponent_.getHeight();
					
					GateComponent comp;
					int gx;
					int gy;
					int gxx;
					int gyy;
					
					for(Gate gate : Simulation.getGates()) {
						comp = gate.getGateComponent();
						gx = gate.getX();
						gy = gate.getY();
						gxx = gx+comp.getWidth();
						gyy = gy+comp.getHeight();
						
						if(y < gyy && gy < yy && x < gxx && gx < xx)
							return;
					}
					
					Simulation.addGate(new Gate(userGateComponent_, userPoint_.x, userPoint_.y));
					//userGateComponent_ = null;
				}
				else {
					if(userWire_ != null && userWire_.tryConnection())
						Simulation.addWire(userWire_);
					
					userWire_ = null;
					
					if(selectedGate_ != null)
						selectedGate_.getGateComponent().onMouseReleased(e, selectedGate_.getPins());
					
					repaint();
				}
			}
			
			public void mouseDragged(MouseEvent e) {
				userPoint_.setLocation(roundToGrid(e.getPoint()));
				
				if(userWire_ != null) {
					highlightedPin_ = Simulation.findPinAt(userPoint_);
					if(highlightedPin_ != null)
						userWire_.setEndPoint(highlightedPin_.getPoint());
					else
						userWire_.setEndPoint(e.getPoint());
					
					repaint();
				}
			}
		};
		
		addMouseListener(adapter);
		addMouseMotionListener(adapter);
		
		Simulation.setSimulationListener(new SimulationListener() {
			@Override
			public void onRequestRepaint() {
				repaint();
			}
		});
	}
	
	/**
	 * @param component {@link GateComponent}
	 */
	public void setUserGateComponent(GateComponent component) {
		userGateComponent_ = component;
		selectedGate_ = null;
		highlightedPin_ = null;
	}
	
	/**
	 * Rounds p to the nearest grid position
	 * @param p The Point to be rounded
	 * @return a new Point that has been rounded to the grid
	 */
	private Point roundToGrid(Point p) {
		final int size = Constants.GRIDSIZE;
		int x = Math.round((float)p.x/(float)size) * size;
		int y = Math.round((float)p.y/(float)size) * size;
		return new Point(x, y);
	}
	
	/**
	 * Draws a GateComponent
	 * @param g The Graphics context
	 * @param component The GateComponent to be drawn
	 * @param x The x-position to draw it at
	 * @param y The y-position to draw it at
	 * @param gate A Gate that references component
	 */
	private void drawGateComponent(Graphics g, GateComponent component, int x, int y, Gate gate) {
		g.translate(x, y);
		
		BufferedImage img = component.getImage();
		if(img != null)
			g.drawImage(img, 0, 0, null);
			
		PinComponent[] pins = component.getPins();
		Point p;
		for(PinComponent pin : pins) {
			p = pin.getOffset();
			
			g.setColor(pin.getType() == PinType.INPUT ? Color.BLUE : Color.RED);
			g.fillRect(p.x-2, p.y-2, 4, 4);
		}
		
		g.setColor(Color.BLACK);
		if(gate == null)
			component.draw(g, null);
		else
			component.draw(g, gate.getPins());
		
		g.translate(-x, -y);
	}
	
	/**
	 * Draws a Wire
	 * @param g The Graphics context
	 * @param wire The Wire to be drawn
	 */
	private void drawWire(Graphics g, Wire wire) {
		Point start = wire.getStartPoint();
		Point end = wire.getEndPoint();
		g.setColor(Color.GREEN);
		g.drawLine(start.x, start.y, end.x, end.y);
		g.setColor(Color.BLACK);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int size = Constants.GRIDSIZE;
		for(int x = 0; x <= getWidth(); x += size) {
			for(int y = 0; y <= getHeight(); y += size)
				g.drawLine(x, y, x, y);
		}
		
		GateComponent gateComponent;
		
		for(Gate gate : Simulation.getGates()) {
			gateComponent = gate.getGateComponent();
			drawGateComponent(g, gateComponent, gate.getX(), gate.getY(), gate);
		}
		
		for(Wire wire : Simulation.getWires())
			drawWire(g, wire);
		
		if(userGateComponent_ != null)
			drawGateComponent(g, userGateComponent_, userPoint_.x, userPoint_.y, null);
		
		if(selectedGate_ != null)
			g.drawRect(selectedGate_.getX()-2, selectedGate_.getY()-2, selectedGate_.getWidth()+4, selectedGate_.getHeight()+4);
		
		if(userWire_ != null)
			drawWire(g, userWire_);
		
		if(highlightedPin_ != null)
			g.drawOval(userPoint_.x-5, userPoint_.y-5, 10, 10);
	}
}
