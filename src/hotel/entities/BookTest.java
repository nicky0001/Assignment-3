package hotel.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;

import hotel.credit.CreditCard;
import hotel.credit.CreditCardType;

class HotelTest {

	//private ServiceType ServiceType ;

	@Test
	void test() {
		Room r1= new Room(10, RoomType.SINGLE);
		Guest g1 = new Guest("abc","abc",789302);
		Date d1 = new Date();
		CreditCard c = new CreditCard(CreditCardType.MASTERCARD, 0, 0);
		r1.book(g1, d1, 10, 1,c);
		
		
		assertEquals(True,r1.isAvailable(d1,10));	
		}
}
