package hotel;

import hotel.booking.BookingCTL;
import hotel.checkin.CheckinCTL;
import hotel.checkout.CheckoutCTL;
import hotel.entities.Hotel;
import hotel.service.RecordServiceCTL;
import hotel.utils.IOUtils;

public class Main {
	
	private static Hotel hotel;

	public static void main(String[] args) throws Exception {
		
		IOUtils.setTrace(false);
				
		hotel = HotelHelper.loadHotel();
		
		boolean active = true;
		try {
			while (active) {
				String selection = IOUtils.input(menu);
				
				switch (selection.toUpperCase()) {
				
				case "B" : 
					bookRoom();
					break;
					
				case "C" : 
					checkin();
					break;
					
				case "R" : 
					recordService();
					break;
					
				case "D" : 
					checkout();
					break;
					
				case "Q" : 
					IOUtils.outputln("\nQuitting\n");
					active = false;
					break;
					
				default:
					IOUtils.outputln("\nInvalid selection\n");
					
				}
			}
		}
		catch (RuntimeException e) {
			IOUtils.outputln(e);
			e.printStackTrace(System.err);
		}
		HotelHelper.saveHotel();
		IOUtils.outputln("Exiting");

	}


	private static void recordService() {
		IOUtils.outputln("\nRecording service\n");
		new RecordServiceCTL(hotel).run();
	}
	
	
	private static void checkin() {
		IOUtils.outputln("\nChecking in\n");
		new CheckinCTL(hotel).run();
	}

	
	private static void checkout() {
		IOUtils.outputln("\nChecking out\n");
		new CheckoutCTL(hotel).run();
	}


	private static void bookRoom() {
		IOUtils.outputln("\nBooking Room\n");
		new BookingCTL(hotel).run();
	}
	
	
	private static String menu = 
		"\nHotel Management System \n\n" +
	    "Please select:\n\n" +
		"    B:    Book a Room\n" +
		"    C:    Check In\n" +
		"    R:    Record Service\n" +
		"    D:    Check Out\n" +
	    " \n" +
		"    Q:   Quit\n" +
	    " \n\n" +
		"Selection : ";

}
