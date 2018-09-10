package hotel.checkout;

import java.text.SimpleDateFormat;
import java.util.List;

import hotel.credit.CreditAuthorizer;
import hotel.credit.CreditCard;
import hotel.credit.CreditCardType;
import hotel.entities.Booking;
import hotel.entities.Guest;
import hotel.entities.Hotel;
import hotel.entities.ServiceCharge;
import hotel.utils.IOUtils;

public class CheckoutCTL {

	private enum State {ROOM, ACCEPT, CREDIT, CANCELLED, COMPLETED };
	
	private Hotel hotel;
	private State state;
	private CheckoutUI checkoutUI;
	private double total;
	private int roomId;


	public CheckoutCTL(Hotel hotel) {
		this.hotel = hotel;
		this.checkoutUI = new CheckoutUI(this);
	}

	
	public void run() {
		IOUtils.trace("BookingCTL: run");
		state = State.ROOM;
		checkoutUI.run();
	}

	
	public void roomIdEntered(int roomId) {
		if (state != State.ROOM) {
			String mesg = String.format("CheckoutCTL: roomIdEntered : bad state : %s", state);
			throw new RuntimeException(mesg);
		}
		this.roomId = roomId;
		Booking booking = hotel.findActiveBookingByRoomId(roomId);
		if (booking == null) {
			String mesg = String.format("No active booking found for room id %d", roomId);			
			checkoutUI.displayMessage(mesg);
			//cancel();
		}	
		else {			
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("Charges for room: %d, booking: %d\n", 
					roomId, booking.getConfirmationNumber()));
			
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			String dateStr = format.format(booking.getArrivalDate());
			sb.append(String.format("Arrival date: %s, Staylength: %d\n", dateStr, booking.getStayLength()));
			
			Guest guest = booking.getGuest();
			sb.append(String.format("Guest: %s, Address: %s, Phone: %d\n", 
					guest.getName(), guest.getAddress(), guest.getPhoneNumber()));
			
			sb.append("Charges:\n");
			
			total = 0;
			List<ServiceCharge> charges = booking.getCharges();
			for (ServiceCharge sc : charges) {
				total += sc.getCost();
				String chargeStr = String.format("    %-12s:%10s", 
						sc.getDescription(), String.format("$%.2f", sc.getCost()));
				sb.append(chargeStr).append("\n");			
			}
			sb.append(String.format("Total: $%.2f\n", total));
			String mesg = sb.toString();
			checkoutUI.displayMessage(mesg);
			state = State.ACCEPT;
			checkoutUI.setState(CheckoutUI.State.ACCEPT);	
		}
	}


	public void chargesAccepted(boolean accepted) {
		if (state != State.ACCEPT) {
			String mesg = String.format("CheckoutCTL: roomIdEntered : bad state : %s", state);
			throw new RuntimeException(mesg);
		}
		if (!accepted) {
			checkoutUI.displayMessage("Charges not accepted");
			cancel();
		}
		else {
			checkoutUI.displayMessage("Charges accepted");
			state = State.CREDIT;
			checkoutUI.setState(CheckoutUI.State.CREDIT);
		}		
	}

	
	public void creditDetailsEntered(CreditCardType type, int number, int ccv) {
		// TODO Auto-generated method stub
	}


	public void cancel() {
		checkoutUI.displayMessage("Checking out cancelled");
		state = State.CANCELLED;
		checkoutUI.setState(CheckoutUI.State.CANCELLED);
	}
	
	
	public void completed() {
		checkoutUI.displayMessage("Checking out completed");
	}



}
