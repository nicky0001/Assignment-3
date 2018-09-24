package hotel.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hotel.credit.CreditCard;
import hotel.utils.IOUtils;

public class Room {
	
	private enum State {READY, OCCUPIED}
	
	int id;
	RoomType roomType;
	List<Booking> bookings;
	State state;
	BookingHelper bookingHelper;

	
	public Room(int id, RoomType roomType) {
		this.id = id;
		this.roomType = roomType;
		bookings = new ArrayList<>();
		bookingHelper = BookingHelper.getInstance();
		state = State.READY;
	}
	

	public String toString() {
		return String.format("Room : %d, %s", id, roomType);
	}


	public int getId() {
		return id;
	}
	
	public String getDescription() {
		return roomType.getDescription();
	}
	
	
	public RoomType getType() {
		return roomType;
	}
	
	public boolean isAvailable(Date arrivalDate, int stayLength) {
		IOUtils.trace("Room: isAvailable");
		for (Booking b : bookings) {
			if (b.doTimesConflict(arrivalDate, stayLength)) {
				return false;
			}
		}
		return true;
	}
	
	
	public boolean isReady() {
		return state == State.READY;
	}


	public boolean isOccupied() {
		return state == State.OCCUPIED;
	}


	public Booking book(Guest guest, Date arrivalDate, int stayLength, int numberOfOccupants, CreditCard creditCard) {
		if (!isAvailable(arrivalDate, stayLength)) {
			throw new RuntimeException("Cannot create an overlapping booking");
		}
		Booking booking = bookingHelper.makeBooking(guest, this, arrivalDate, stayLength, numberOfOccupants, creditCard);
		bookings.add(booking);
		return booking;		
	}


	public void checkin() {
		if (state != State.READY) {
			String mesg = String.format("Room: checkin : bad state : %s", state);
			throw new RuntimeException(mesg);
		}
		state = State.OCCUPIED;
	}


	public void checkout(Booking booking) {
		if (state != State.OCCUPIED) {
			String mesg = String.format("Room: checkout : bad state : %s", state);
			throw new RuntimeException(mesg);
		}
		bookings.remove(booking);
		state = State.READY;
	}


}
