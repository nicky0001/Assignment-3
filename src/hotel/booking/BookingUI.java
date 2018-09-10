package hotel.booking;

import java.text.SimpleDateFormat;
import java.util.Date;

import hotel.credit.CreditCardType;
import hotel.entities.RoomType;
import hotel.exceptions.CancelException;
import hotel.exceptions.NullInputException;
import hotel.utils.IOUtils;

public class BookingUI {
	
	BookingCTL bookingCTL;
	
	public static enum State {PHONE, ROOM, REGISTER, TIMES, CREDIT, CANCELLED, COMPLETED};
	
	private State state;
	
	
	public BookingUI(BookingCTL bookingCTL) {
		this.bookingCTL = bookingCTL;
		this.state = State.PHONE;
	}

	
	public void run() {
		IOUtils.trace("BookingUI: run");
		int phoneNumber = 0;
		
		boolean completed = false;
		
		while (!completed) {
			try {
				switch (state) {
				
				case PHONE:
					phoneNumber = enterPhoneNumber();
					bookingCTL.phoneNumberEntered(phoneNumber);
					break;
					
				case REGISTER:
					GuestDetails guestDetails = registerGuest();
					bookingCTL.guestDetailsEntered(guestDetails.name, guestDetails.address);
					break;
					
				case ROOM:
					RoomTypeAndOccupants roomAndOccupants = enterRoomTypeAndOccupants();
					bookingCTL.roomTypeAndOccupantsEntered(
							roomAndOccupants.selectedRoomType, roomAndOccupants.occupantNumber);
					break;
					
				case TIMES:
					BookingTimes intendedBookingTimes = enterBookingTimes();
					bookingCTL.bookingTimesEntered(
							intendedBookingTimes.arrivalDate, intendedBookingTimes.stayLength);
					break;
				
				case CREDIT:
					CreditDetails creditDetails = enterCreditDetails();
					bookingCTL.creditDetailsEntered(creditDetails.type, creditDetails.number, creditDetails.ccv);
					break;
					
				case CANCELLED:
					completed = true;
					break;
					
				case COMPLETED:
					IOUtils.input("Hit <enter> to continue");
					bookingCTL.completed();
					completed = true;
					break;
					
				default:
					String mesg = String.format("BookingUI: run : unknown state : %s", state);
					IOUtils.outputln(mesg);
				}
			}
			catch (CancelException e) {
				bookingCTL.cancel();
				completed = true;
			}
		}
	}

	
	public int enterPhoneNumber() {
		IOUtils.trace("BookingUI: enterPhoneNumber");
		
		int number = 0;
		try {
			number = IOUtils.getValidPositiveInt("Enter phone number: ");
		}
		catch (NullInputException e) {
			IOUtils.outputln("BookingUI: User cancelled at input phone number");
			throw new CancelException();
		}
		return number;
	}

	
	public GuestDetails registerGuest() {
		IOUtils.trace("BookingUI: getGuestDetails");
		boolean invalid = true;
		String name = null;
		String address = null;
		while (invalid) {
			name = IOUtils.input("Enter guest name: ");
			if (name.isEmpty()) {
				IOUtils.outputln("BookingUI: User cancelled at input guest name");
				throw new CancelException();
			}
			
			address = IOUtils.input("Enter guest address: ");
			if (address.isEmpty()) {
				IOUtils.outputln("BookingUI: User cancelled at input guest address");
				throw new CancelException();
			}
			invalid = false;
		}
		return new GuestDetails(name, address);
	}
	
	
	private RoomTypeAndOccupants enterRoomTypeAndOccupants() {
		IOUtils.trace("BookingUI: enterRoomTypeAndOccupants");
		RoomType selectedRoomType = null;
		int numberOfOccupants = 0;
		boolean entered = false;
		while (!entered) {
			try {
				selectedRoomType = IOUtils.getValidRoomType("Enter room type");
			}
			catch (NullInputException e) {
				IOUtils.outputln("BookingUI: User cancelled at entering room type");
				throw new CancelException();
			}
			try {
				numberOfOccupants = IOUtils.getValidPositiveInt("Enter number of occupants: ");
				entered = true;
			}
			catch (NullInputException e) {
				IOUtils.outputln("Selecting another room type");
			}
		}
		return new RoomTypeAndOccupants(selectedRoomType, numberOfOccupants);
	}


	private BookingTimes enterBookingTimes() {
		IOUtils.trace("BookingUI: getBookingTimes");		
		int stay = 0;
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		
		Date arrivalDate = null;
		boolean timesEntered = false;
		
		while (!timesEntered) {
			try {
				arrivalDate = IOUtils.getValidDate("Enter arrival date");
			}
			catch (NullInputException e) {
				IOUtils.outputln("BookingUI: User cancelled at entering arrival date");
				throw new CancelException();
			}
			IOUtils.outputln("Arrival Date: " + format.format(arrivalDate));
			//get stay length
			try {
				stay = IOUtils.getValidPositiveInt("Enter length of stay: ");
				timesEntered = true;
			}
			catch (NullInputException e) {
				IOUtils.outputln("BookingUI: User reset at enter length of stay");
				continue;
			}
		}
		return new BookingTimes(arrivalDate, stay);
	}


	public CreditDetails enterCreditDetails() {		
		IOUtils.trace("BookingUI: enterCreditDetails");		
		CreditCardType creditCardType = null;
		int cardNumber = 0;
		int ccv = 0;
		IOUtils.outputln("\nEnter credit card details");
		
		boolean completed = false;
		while (!completed) {			
			//enter credit card type
			try {
				creditCardType = IOUtils.getValidCreditType("Enter credit card type");
			}
			catch (NullInputException e) {
				IOUtils.outputln("BookingUI: User cancelled at enter credit card type");
				throw new CancelException();
			}			
			//enter credit card number
			try {
				cardNumber = IOUtils.getValidPositiveInt("Enter credit card number: ");
			}
			catch (NullInputException e) {
				IOUtils.outputln("BookingUI: User reset at input credit card number");
				continue;
			}	
			//enter ccv
			try {
				ccv = IOUtils.getValidPositiveInt("Enter CCV: ");
			}
			catch (NullInputException e) {
				IOUtils.outputln("BookingUI: User reset at input CCV");
				continue;
			}	
			completed = true;
		}		
		CreditDetails creditDetails = new CreditDetails(creditCardType, cardNumber, ccv);
		return creditDetails;
	}


	public void displayGuestDetails(String name, String address, int phoneNumber) {
		IOUtils.outputln(String.format("\nName: %s, Address: %s, Phone: %d\n", name, address, phoneNumber));
	}


	public void displayBookingDetails(String roomDescription, Date arrivalDate, int stayLength, double cost) {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		
		String outputStr = String.format(
				"\nThe cost of booking a %s from %s for %d nights is $%.2f",
				roomDescription, format.format(arrivalDate), stayLength, cost);
				
		IOUtils.outputln(outputStr);
	}


	public void displayConfirmedBooking(String roomDecription, int roomNumber,
			Date arrivalDate, int stayLength, 
			String guestName, String creditCardVendor, int cardNumber, 
			double cost, long confirmationNumber) {
		
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		String confirmationMessage = String.format(
				"\n%s %d is booked from %s for %d nights by %s.\n" +
		        "%s Credit card number %d has been debited $%.2f\n" +
		        "Confirmation Number : %d",
		        roomDecription, roomNumber,
				format.format(arrivalDate), stayLength, 
				guestName,
				creditCardVendor, cardNumber, cost,
				confirmationNumber);
				
		IOUtils.outputln(confirmationMessage);
	}


	public void displayMessage(String message) {
		IOUtils.outputln(message);
	}
	
	
	public void setState(State state) {
		this.state = state;
		
	}


	public void cancel() {
		bookingCTL.cancel();
	}


	
	class GuestDetails {
		String name;
		String address;
	
		public GuestDetails(String name, String address) {
			this.name = name;
			this.address = address;
		}
	}
	
	
	
	class RoomTypeAndOccupants {
	
		public RoomType selectedRoomType;
		public int occupantNumber;
		
		public RoomTypeAndOccupants(RoomType selectedRoomType, int occupantNumber) {
			this.selectedRoomType = selectedRoomType;
			this.occupantNumber = occupantNumber;
		}
	}

	
	
	class BookingTimes {	
		Date arrivalDate;
		int stayLength;
	
		public BookingTimes(Date arrivalDate, int stayLength) {
			this.arrivalDate = arrivalDate;
			this.stayLength = stayLength;
		}
	}	
	

	
	class CreditDetails {	
		CreditCardType type;
		int number;
		int ccv;
	
		public CreditDetails(CreditCardType creditCardType, int cardNumber, int ccv) {
			this.type = creditCardType;
			this.number = cardNumber;
			this.ccv = ccv;
		}
	}	
	
	

}	

