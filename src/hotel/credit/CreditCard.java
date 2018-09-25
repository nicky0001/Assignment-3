package hotel.credit;

public class CreditCard implements ICreditCard {
	
	private CreditCardType type;
	private int number;
	private int ccv;
	
	
	public CreditCard(CreditCardType type, int number, int ccv) {
		this.type = type;
		this.number = number;
		this.ccv = ccv;
	}

	@Override
	public CreditCardType getType() {
		return type;
	}

	@Override
	public int getNumber() {
		return number;
	}

	@Override
	public int getCcv() {
		return ccv;
	}

	public String getVendor() {
		return type.getVendor();
	}

}
