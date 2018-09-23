package hotel.entities;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import hotel.utils.IOUtils;

public enum RoomType {
	
	SINGLE    (1, "S", "Single room", 100.0), 
	DOUBLE    (2, "D", "Double room", 200.0),
	TWIN_SHARE(3, "T", "Twin share",  300.0);
	

	private static final Map<String,RoomType> map = new HashMap<>();
	static {
		for (RoomType r : RoomType.values()) {
			map.put(r.identifier, r);
		}
	}

	
	public static RoomType getRoomTypeByIdentifier(String identifier) {
		IOUtils.trace("RoomType: getRoomTypeByIdentifier");
		return map.get(identifier);
	}
	

	public static boolean isValidIdentifier(String identifier) {
		IOUtils.trace("RoomType: isValidIdentifier");
		return map.containsKey(identifier);
	}
	

	private int maxOccupancy;
	private String identifier;
	private String description;
	private double costPerDay;
	

	private RoomType(int maxOccupancy, String identifier, String description, double costPerDay) {
		this.maxOccupancy = maxOccupancy;
		this.identifier = identifier;
		this.description = description;
		this.costPerDay = costPerDay;
	}
	

	public boolean isSuitable(int occupants) {
		IOUtils.trace("RoomType: isSuitable");
		return 0 < occupants && occupants <= maxOccupancy;
	}
	

	public String getIdentifier() {
		return identifier;
	}
	
	
	public String getDescription() {
		return description;
	}

	
	public int getMaxOccupancy() {
		return maxOccupancy;
	}

	
	public double calculateCost(Date arrivalDate, int stayLength) {		
		double cost = stayLength * costPerDay;
		return cost;
	}
	
}
