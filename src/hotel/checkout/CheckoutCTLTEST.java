package hotel.checkout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import hotel.credit.CreditCardType;
import hotel.entities.Booking;
import hotel.entities.Hotel;

class CheckoutCTLTest {

	@Test
	void testCreditDetailsEntered() {
		Hotel ho = new Hotel();
		CheckoutCTL ch = new CheckoutCTL(ho);
	
	 ch.state = ch.state.CREDIT;
	 Hotel hotel = new Hotel();
	 Booking book = new Booking();
//	 hotel.put.activeBookingsByRoomId(101);
		hotel.activeBookingsByRoomId.put(101, book);

	 ch.roomId = 101;
		CreditCardType ctp = CreditCardType.VISA;
		
		ch.creditDetailsEntered(ctp, 1, 12);
	}

}
