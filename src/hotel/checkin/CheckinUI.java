package hotel.checkin;

import java.text.SimpleDateFormat;
import java.util.Date;

import hotel.exceptions.CancelException;
import hotel.exceptions.NullInputException;
import hotel.utils.IOUtils;

public class CheckinUI {

	public static enum State {CHECKING, CONFIRMING, CANCELLED, COMPLETED};
	
	private CheckinCTL checkInCTL;
	private State state;
	

	public CheckinUI(CheckinCTL checkInCTL) {
		this.checkInCTL = checkInCTL;
		this.state = State.CHECKING;
	}

	
	public void run() {
		IOUtils.trace("CheckinUI: run");
		long confirmationNumber = 0;
		
		boolean completed = false;		
		while (!completed) {
			try {
				switch (state) {
				
				case CHECKING:
					confirmationNumber = enterConfirmationNumber();
					checkInCTL.confirmationNumberEntered(confirmationNumber);
					break;
					
				case CONFIRMING:
					boolean confirmed = enterCheckinConfirmation();
					checkInCTL.checkInConfirmed(confirmed);
					break;
					
				case CANCELLED:
					completed = true;
					break;
					
				case COMPLETED:
					IOUtils.input("Hit <enter> to continue");
					checkInCTL.completed();
					completed = true;
					break;
					
				default:
					String mesg = String.format("CheckInUI: run : unknown state : %s", state);
					IOUtils.outputln(mesg);
				}
			}
			catch (CancelException e) {
				checkInCTL.cancel();
			}
		}
	}

	
	private boolean enterCheckinConfirmation() {
		IOUtils.trace("checkInUI: enterCheckinConfirmation");
		
		boolean confirmed = false;
		try {
			confirmed = IOUtils.getBooleanYesNoAnswer("Confirm checkin? ");
		}
		catch (NullInputException e) {
			IOUtils.outputln("BookingUI: User cancelled at confirming checkin");
			throw new CancelException();
		}
		return confirmed;
	}


	private long enterConfirmationNumber() {
		IOUtils.trace("checkInUI: enterConfirmationNumber");
		
		long number = 0;
		try {
			number = IOUtils.getValidPositiveLong("Enter confirmation number: ");
		}
		catch (NullInputException e) {
			IOUtils.outputln("BookingUI: User cancelled at input confirmation number");
			throw new CancelException();
		}
		return number;
	}


	public void displayCheckinMessage(String roomDescription, int roomId, 
			Date arrivalDate, int stayLength, String guestName,
			String cardVendor, int cardNumber, 
			long confirmationNumber) {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		
		String message = String.format(
				"\n%s %d is booked from %s for %d nights by %s.\n" +
		        "%s Credit card number: %d\n" +
		        "Confirmation Number : %d",
		        roomDescription, roomId, 
		        format.format(arrivalDate), stayLength, 
		        guestName,
		        cardVendor, cardNumber, 
		        confirmationNumber);
		
		IOUtils.outputln(message);
	}	
	
	
	public void displayMessage(String message) {
		IOUtils.outputln(message);
	}

	
	protected void setState(State state) {
		this.state = state;
	}



}
