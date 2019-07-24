package common.sim;

import java.awt.Point;

/**
 * Holds constant information from which a Pin will reference
 */
public class PinComponent
{
	/**
	 * A label that is displayed next to the pin
	 */
	private final String label_;
	
	/**
	 * The type of pin it is
	 */
	private final PinType type_;
	
	
	/**
	 * Causes the Pin's Gate to update when the Pin's {@link Pin#state} changes
	 */
	private final boolean causeUpdate_;
	
	/**
	 * The offset from a derived Pin's Gate
	 */
	private final Point offset_;
	
	/**
	 * @param label {@link PinComponent#label_} 
	 * @param type {@link PinComponent#type_}
	 * @param causeUpdate {@link PinComponent#causeUpdate_}
	 * @param offset {@link PinComponent#offset_}
	 */
	public PinComponent(String label, PinType type, boolean causeUpdate, Point offset)
	{
		label_ = label;
		type_ = type;
		causeUpdate_ = causeUpdate;
		offset_ = offset;
	}
	
	/**
	 * @return {@link PinComponent#label_}
	 */
	public String getLabel() {
		return label_;
	}

	/**
	 * @return {@link PinComponent#type_}
	 */
	public PinType getType() {
		return type_;
	}
	
	/**
	 * @return {@link PinComponent#causeUpdate_}
	 */
	public boolean doesCauseUpdate() {
		return causeUpdate_;
	}
	
	/**
	 * @return {@link PinComponent#offset_}
	 */
	public Point getOffset() {
		return offset_;
	}
}
