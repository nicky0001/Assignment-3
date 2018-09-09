package hotel.credit;

import java.util.HashMap;
import java.util.Map;

import hotel.utils.IOUtils;

public enum CreditCardType { 
	VISA("V", "Visa"), 
	MASTERCARD("M", "MasterCard");
	
	private static final Map<String, CreditCardType> map = new HashMap<>();
	static {
		for (CreditCardType ct : CreditCardType.values()) {
			map.put(ct.identifier, ct);
		}
	}
	
	
	public static CreditCardType getCardTypeByIdentifier(String identifier) {
		IOUtils.trace("CreditCardType: getCardTypeByIdentifier");
		return map.get(identifier);
	}

	public static boolean isValidIdentifier(String identifier) {
		IOUtils.trace("CreditCardType: isValidIdentifier");
		return map.containsKey(identifier);
	}
	
	private String identifier;
	private String vendor;
	
	private CreditCardType(String identifier, String vendor) {
		this.identifier = identifier;
		this.vendor = vendor;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public String getVendor() {
		return vendor;
	}
}

