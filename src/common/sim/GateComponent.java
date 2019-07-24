package common.sim;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Holds static information about a Gate
 */
public abstract class GateComponent implements Comparable<GateComponent>, GateInterface {
	/**
	 * The name of the GateComponent shown in the UI
	 */
	private final String name_;
	
	/**
	 * The description of how the GateComponent works
	 */
	private final String description_;
	
	/**
	 * The image the GateComponent refers to for being drawn
	 */
	private BufferedImage image_;
	
	/**
	 * The size of the GateComponent (size of {@link GateComponent#image_} if given)
	 */
	private final Dimension bounds_;
	
	/**
	 * The PinComponents from which a Gate's Pin's will reference information from
	 */
	private final PinComponent[] pins_;
	
	/**
	 * The constructor called if the Gate uses an image file
	 * @param name {@link GateComponent#name_}
	 * @param description {@link GateComponent#description_}
	 * @param image {@link GateComponent#image_}
	 * @param pins {@link GateComponent#pins_}
	 */
	public GateComponent(String name, String description, String image, PinComponent pins[])
	{
		name_ = name;
		description_ = description;
		
		try {
			image_ = ImageIO.read(GateComponent.class.getResourceAsStream(image));
		} catch (IOException e) {
			image_ = null;
			e.printStackTrace();
		}
		
		bounds_ = new Dimension(image_.getWidth(), image_.getHeight());
		pins_ = pins;
	}
	
	/**
	 * The constructor called if the Gate draws itself
	 * @param name {@link GateComponent#name_}
	 * @param description {@link GateComponent#description_}
	 * @param bounds {@link GateComponent#bounds_}
	 * @param pins {@link GateComponent#pins_}
	 */
	public GateComponent(String name, String description, Dimension bounds, PinComponent pins[])
	{
		name_ = name;
		description_ = description;
		image_ = null;
		bounds_ = bounds;
		pins_ = pins;
	}
	
	/**
	 * @return {@link GateComponent#name_}
	 */
	public String getName() {
		return name_;
	}
	
	/**
	 * @return {@link GateComponent#description_}
	 */
	public String getDescription() {
		return description_;
	}
	
	/**
	 * @return {@link GateComponent#image_}
	 */
	public BufferedImage getImage() {
		return image_;
	}
	
	/**
	 * @return {@link GateComponent#bounds_}
	 */
	public Dimension getBounds() {
		return bounds_;
	}
	
	/**
	 * @return The width of the GateComponent
	 */
	public int getWidth() {
		return bounds_.width;
	}
	
	/**
	 * @return The height of the GateComponent
	 */
	public int getHeight() {
		return bounds_.height;
	}
	
	/**
	 * @return {@link GateComponent#pins_}
	 */
	public PinComponent[] getPins() {
		return pins_;
	}
	
	/**
	 * Compares this GateComponent to another one alphabetically
	 * @return 1 if greater, 0 if equal, -1 is lesser
	 */
	public int compareTo(GateComponent other) {
		return name_.compareTo(other.name_);
	}
	
	/**
	 * @return {@link GateComponent#name_}
	 */
	public String toString() {
		return name_;
	}
	
	//Defines empty functionality for the GateInterface functions
	public void performLogic(Pin[] pins) {}
	public void draw(Graphics g, Pin[] pins) {}
	public void onMouseClicked(MouseEvent e, Pin[] pins) {}
	public void onMousePressed(MouseEvent e, Pin[] pins) {}
	public void onMouseReleased(MouseEvent e, Pin[] pins) {}
}
