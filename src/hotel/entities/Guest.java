package hotel.entities;

public class Guest {
	
	private String name;
	private String address;
	private int phoneNumber;


	public Guest(String name, String address, int phoneNumber) {
		this.name = name;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}

	
	public String getName() {
		return name;
	}

	
	public String getAddress() {
		return address;
	}

	
	public int getPhoneNumber() {
		return phoneNumber;
	}

	
	public String toString() {
		return String.format("Name: %s, Address: %s, Phone: %d", name, address, phoneNumber);
	}

}
