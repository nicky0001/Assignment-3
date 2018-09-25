package hotel.credit;

public interface ICreditAuthorizer {
	public boolean authorize(ICreditCard card, double amount);
		

}
