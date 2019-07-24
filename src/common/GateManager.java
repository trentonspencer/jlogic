package common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import common.sim.GateComponent;

/**
 * Organizes GateComponents into containers based on a key
 */
public class GateManager {
	/**
	 * The container for the GateComponents
	 */
	private Map<String, ArrayList<GateComponent>> map_;
	
	public GateManager() {
		map_ = new HashMap<>();
	}
	
	/**
	 * Adds a new container to put GateComponents into
	 * @param key The unique key for the container
	 */
	public void addContainer(String key) {
		map_.put(key, new ArrayList<GateComponent>());
	}
	
	/**
	 * Adds a {@link GateComponent} to a container added via {@link common.GateManager#addContainer(String)}
	 * @param key The key of the container
	 * @param component The component to add
	 */
	public void add(String key, GateComponent component) {
		ArrayList<GateComponent> components = map_.get(key);
		if(components != null)
			components.add(component);
	}
	
	/**
	 * @return A Set of all container keys
	 */
	public Set<String> getContainers() {
		return map_.keySet();
	}
	
	/**
	 * Returns a specific container
	 * @param key The container to return
	 * @return The container corresponding to key, or null
	 */
	public ArrayList<GateComponent> getContainer(String key) {
		return map_.get(key);
	}
}
