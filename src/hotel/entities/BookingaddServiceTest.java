package hotel.checkout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.sun.corba.se.spi.orbutil.fsm.State;

import hotel.credit.CreditCardType;
import hotel.entities.Booking;
import hotel.entities.Hotel;
import hotel.entities.ServiceType;

class CheckoutCTLTest {

	@Test
	void testCreditDetailsEntered() throws NullPointerException{
		
		
		Booking book = new Booking();
		book.state = book.state.CHECKED_IN;
		ServiceType ser = ServiceType.BAR_FRIDGE;
		book.addServiceCharge(ser.BAR_FRIDGE, 0);
		
		
	}

}
