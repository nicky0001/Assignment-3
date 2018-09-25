package hotel.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hotel.credit.CreditCard;
import hotel.utils.IOUtils;

public class Hotel {
	
	Map<Integer, Guest> guests;
	Map<RoomType, Map<Integer,Room>> roomsByType;
	Map<Long, Booking> bookingsByConfirmationNumber;
	Map<Integer, Booking> activeBookingsByRoomId;
	
	
	public Hotel() {
		guests = new HashMap<>();
		roomsByType = new HashMap<>();
		for (RoomType rt : RoomType.values()) {
			Map<Integer, Room> rooms = new HashMap<>();
			roomsByType.put(rt, rooms);
		}
		bookingsByConfirmationNumber = new HashMap<>();
		activeBookingsByRoomId = new HashMap<>();
	}

	
	public void addRoom(RoomType roomType, int id) {
		IOUtils.trace("Hotel: addRoom");
		for (Map<Integer, Room> rooms : roomsByType.values()) {
			if (rooms.containsKey(id)) {
				throw new RuntimeException("Hotel: addRoom : room number already exists");
			}
		}
		Map<Integer, Room> rooms = roomsByType.get(roomType);
		Room room = new Room(id, roomType);
		rooms.put(id, room);
	}

	
	public boolean isRegistered(int phoneNumber) {
		return guests.containsKey(phoneNumber);
	}

	
	public Guest registerGuest(String name, String address, int phoneNumber) {
		if (guests.containsKey(phoneNumber)) {
			throw new RuntimeException("Phone number already registered");
		}
		Guest guest = new Guest(name, address, phoneNumber);
		guests.put(phoneNumber, guest);		
		return guest;
	}

	
	public Guest findGuestByPhoneNumber(int phoneNumber) {
		Guest guest = guests.get(phoneNumber);
		return guest;
	}

	
	public Booking findActiveBookingByRoomId(int roomId) {
		Booking booking = activeBookingsByRoomId.get(roomId);;
		return booking;
	}

	
	public List<Room> findRoomsByType(RoomType type) {
		List<Room> rooms = new ArrayList<>(roomsByType.get(type).values());
		return Collections.unmodifiableList(rooms);
	}

	
	public Room findAvailableRoom(RoomType selectedRoomType, Date arrivalDate, int stayLength) {
		IOUtils.trace("Hotel: checkRoomAvailability");
		Map<Integer, Room> rooms = roomsByType.get(selectedRoomType);
		for (Room room : rooms.values()) {
			IOUtils.trace(String.format("Hotel: checking room: %d",room.getId()));
			if (room.isAvailable(arrivalDate, stayLength)) {
				return room;
			}			
		}
		return null;
	}

	
	public Booking findBookingByConfirmationNumber(long confirmationNumber) {
		return bookingsByConfirmationNumber.get(confirmationNumber);
	}

	
	public long book(Room room, Guest guest, 
			Date arrivalDate, int stayLength, int occupantNumber,
			CreditCard creditCard) {
		
		Booking booking = room.book(guest, arrivalDate, stayLength, occupantNumber, creditCard);
		long confirmationNumber = booking.getConfirmationNumber();
		bookingsByConfirmationNumber.put(confirmationNumber, booking);
		return confirmationNumber;		
	}

	
	public void checkin(long confirmationNumber) {
		Booking booking = bookingsByConfirmationNumber.get(confirmationNumber);
		if (booking == null) {
			String message = String.format("Hotel: checkin: No booking found for confirmation number %d", confirmationNumber);
			throw new RuntimeException(message);
		}
		int roomId = booking.getRoomId();
		
		booking.checkIn();
		activeBookingsByRoomId.put(roomId, booking);
	}


	public void addServiceCharge(int roomId, ServiceType serviceType, double cost) {
		Booking booking = activeBookingsByRoomId.get(roomId);
		if (booking == null) {
			String mesg = String.format("Hotel: addServiceCharge: no booking present for room id : %d", roomId);
			throw new RuntimeException(mesg);
		}
		booking.addServiceCharge(serviceType, cost);
	}

	
	public void checkout(int roomId) {
		Booking booking = activeBookingsByRoomId.get(roomId);
		if (booking == null) {
			String mesg = String.format("Hotel: checkout: no booking present for room id : %d", roomId);
			throw new RuntimeException(mesg);
		}
		booking.checkOut();
		activeBookingsByRoomId.remove(roomId);
	}
}
