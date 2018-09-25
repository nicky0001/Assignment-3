package hotel.checkout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


import hotel.credit.CreditCardType;
import hotel.entities.Booking;
import hotel.entities.Hotel;
import hotel.entities.ServiceType;

class CheckoutCTLTest {

	@Test
	void testCreditDetailsEntered() throws NullPointerException{
		
		Hotel  ht = new Hotel();
		CheckoutCTL ch = new CheckoutCTL(ht);
		CreditCardType ct = CreditCardType.MASTERCARD;
		ch.creditDetailsEntered(ct.MASTERCARD, 1, 1 );
		
		
	}

}
