package hotel.entities;

import java.util.HashMap;
import java.util.Map;

import hotel.utils.IOUtils;

public enum ServiceType {
	
	BAR_FRIDGE("B", "Bar Fridge"),
	RESTAURANT("R","Restaurant"),
	ROOM_SERVICE("S","Room Service");

	
	private static final Map<String, ServiceType> map = new HashMap<>();
	static {
		for (ServiceType r : ServiceType.values()) {
			map.put(r.identifier, r);
		}
	}

	
	public static ServiceType getServiceTypeByIdentifier(String identifier) {
		IOUtils.trace("RoomType: getRoomTypeByIdentifier");
		return map.get(identifier);
	}
	

	public static boolean isValidIdentifier(String identifier) {
		IOUtils.trace("RoomType: isValidIdentifier");
		return map.containsKey(identifier);
	}
	

	private String identifier;
	private String description;
	

	private ServiceType(String identifier, String description) {
		this.identifier = identifier;
		this.description = description;	
	}

	
	public String getIdentifier() {
		return identifier;
	}
	
	
	public String getDescription() {
		return description;
	}

	

}
