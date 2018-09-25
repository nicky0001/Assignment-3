package hotel.booking;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import hotel.credit.CreditAuthorizer;
import hotel.credit.CreditCard;
import hotel.credit.CreditCardType;
import hotel.entities.Guest;
import hotel.entities.Hotel;
import hotel.entities.Room;
import hotel.entities.RoomType;
import hotel.utils.IOUtils;

public class BookingCTL {
	
	
	private static enum State {PHONE, ROOM, REGISTER, TIMES, CREDIT, APPROVED, CANCELLED, COMPLETED}	
	
	private BookingUI bookingUI;
	private Hotel hotel;

	private Guest guest;
	private Room room;
	private double cost;
	
	private State state;
	private int phoneNumber;
	private RoomType selectedRoomType;
	private int occupantNumber;
	private Date arrivalDate;
	private int stayLength;

	
	public BookingCTL(Hotel hotel) {
		this.bookingUI = new BookingUI(this);
		this.hotel = hotel;
		state = State.PHONE;
	}

	
	public void run() {		
		IOUtils.trace("BookingCTL: run");
		bookingUI.run();
	}
	
	
	public void phoneNumberEntered(int phoneNumber) {
		if (state != State.PHONE) {
			String mesg = String.format("BookingCTL: phoneNumberEntered : bad state : %s", state);
			throw new RuntimeException(mesg);
		}
		this.phoneNumber = phoneNumber;
		
		boolean isRegistered = hotel.isRegistered(phoneNumber);
		
		if (isRegistered) {
			guest = hotel.findGuestByPhoneNumber(phoneNumber);
			bookingUI.displayGuestDetails(guest.getName(), guest.getAddress(), guest.getPhoneNumber());
			this.state = State.ROOM;
			bookingUI.setState(BookingUI.State.ROOM);
		}
		else {
			this.state = State.REGISTER;
			bookingUI.setState(BookingUI.State.REGISTER);
		}
	}


	public void guestDetailsEntered(String name, String address) {
		if (state != State.REGISTER) {
			String mesg = String.format("BookingCTL: guestDetailsEntered : bad state : %s", state);
			throw new RuntimeException(mesg);
		}
		guest = hotel.registerGuest(name, address, phoneNumber);
		
		bookingUI.displayGuestDetails(guest.getName(), guest.getAddress(), guest.getPhoneNumber());
		state = State.ROOM;
		bookingUI.setState(BookingUI.State.ROOM);
	}


	public void roomTypeAndOccupantsEntered(RoomType selectedRoomType, int occupantNumber) {
		if (state != State.ROOM) {
			String mesg = String.format("BookingCTL: roomTypeAndOccupantsEntered : bad state : %s", state);
			throw new RuntimeException(mesg);
		}
		this.selectedRoomType = selectedRoomType;
		this.occupantNumber = occupantNumber;
		
		boolean suitable = selectedRoomType.isSuitable(occupantNumber);
		
		if (!suitable) {			
			String notSuitableMessage = "\nRoom type unsuitable, please select another room type\n";
			bookingUI.displayMessage(notSuitableMessage);
		}
		else {
			state = State.TIMES;
			bookingUI.setState(BookingUI.State.TIMES);
		}
	}


	public void bookingTimesEntered(Date arrivalDate, int stayLength) {
		if (state != State.TIMES) {
			String mesg = String.format("BookingCTL: bookingTimesEntered : bad state : %s", state);
			throw new RuntimeException(mesg);
		}
		this.arrivalDate = arrivalDate;
		this.stayLength = stayLength;
		
		room = hotel.findAvailableRoom(selectedRoomType, arrivalDate, stayLength);
		
		if (room == null) {				
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(arrivalDate);
			calendar.add(Calendar.DATE, stayLength);
			Date departureDate = calendar.getTime();
			
			String notAvailableStr = String.format("\n%s is not available between %s and %s\n",
					selectedRoomType.getDescription(),
					format.format(arrivalDate),
					format.format(departureDate));
			
			bookingUI.displayMessage(notAvailableStr);
		}
		else {
			cost = selectedRoomType.calculateCost(arrivalDate, stayLength);
			String description = selectedRoomType.getDescription();
			bookingUI.displayBookingDetails(description, arrivalDate, stayLength, cost);
			state = State.CREDIT;
			bookingUI.setState(BookingUI.State.CREDIT);
		}
	}


	public void creditDetailsEntered(CreditCardType type, int number, int ccv) {
		// TODO Auto-generated method stub
	}


	public void cancel() {
		IOUtils.trace("BookingCTL: cancel");
		bookingUI.displayMessage("Booking cancelled");
		state = State.CANCELLED;
		bookingUI.setState(BookingUI.State.CANCELLED);
	}
	
	
	public void completed() {
		IOUtils.trace("BookingCTL: completed");
		bookingUI.displayMessage("Booking completed");
	}

	

}
