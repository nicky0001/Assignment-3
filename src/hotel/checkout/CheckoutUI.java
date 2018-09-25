package hotel.checkout;

import hotel.credit.CreditCardType;
import hotel.exceptions.CancelException;
import hotel.exceptions.NullInputException;
import hotel.utils.IOUtils;

public class CheckoutUI {

	public static enum State {ROOM, ACCEPT, CREDIT, CANCELLED, COMPLETED};
	
	private CheckoutCTL checkoutCTL;
	private State state;
	
	
	public CheckoutUI(CheckoutCTL checkoutCTL) {
		this.checkoutCTL = checkoutCTL;
		this.state = State.ROOM;
	}


	public void run() {
		IOUtils.trace("CheckoutUI: run");
		
		boolean completed = false;		
		while (!completed) {
			try {
				switch (state) {
				
				case ROOM:
					int roomId = enterRoomId();
					checkoutCTL.roomIdEntered(roomId);
					break;
					
				case ACCEPT:
					boolean accepted = acceptCharges();
					checkoutCTL.chargesAccepted(accepted);
					break;
					
				case CREDIT:
					CreditDetails details = enterCreditDetails();
					checkoutCTL.creditDetailsEntered(details.type, details.number, details.ccv);
					break;
										
				case CANCELLED:
					completed = true;
					break;
					
				case COMPLETED:
					IOUtils.input("Hit <enter> to continue");
					checkoutCTL.completed();
					completed = true;
					break;
					
				default:
					String mesg = String.format("CheckoutUI: run : unknown state : %s", state);
					IOUtils.outputln(mesg);
				}
			}
			catch (CancelException e) {
				checkoutCTL.cancel();
			}
		}
	}


	private boolean acceptCharges() {
		boolean accepted = IOUtils.getBooleanYesNoAnswer("Accept charges");
		return accepted;
	}


	public CreditDetails enterCreditDetails() {		
		IOUtils.trace("CheckoutUI: enterCreditDetails");		
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
				IOUtils.outputln("CheckoutUI: User cancelled at enter credit card type");
				throw new CancelException();
			}			
			//enter credit card number
			try {
				cardNumber = IOUtils.getValidPositiveInt("Enter credit card number: ");
			}
			catch (NullInputException e) {
				IOUtils.outputln("CheckoutUI: User reset at input credit card number");
				continue;
			}	
			//enter ccv
			try {
				ccv = IOUtils.getValidPositiveInt("Enter CCV: ");
			}
			catch (NullInputException e) {
				IOUtils.outputln("CheckoutUI: User reset at input CCV");
				continue;
			}	
			completed = true;
		}		
		CreditDetails creditDetails = new CreditDetails(creditCardType, cardNumber, ccv);
		return creditDetails;
	}


	private int enterRoomId() {
		IOUtils.trace("CheckoutUI: enterRoomId");
		
		int number = 0;
		try {
			number = IOUtils.getValidPositiveInt("Enter room number: ");
		}
		catch (NullInputException e) {
			IOUtils.outputln("CheckoutUI: User cancelled at input room number");
			throw new CancelException();
		}
		return number;
	}


	public void setState(State state) {
		this.state = state;
	}


	public void displayMessage(String message) {
		IOUtils.outputln(message);
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
