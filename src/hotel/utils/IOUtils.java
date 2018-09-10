package hotel.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import hotel.credit.CreditCardType;
import hotel.entities.RoomType;
import hotel.entities.ServiceType;
import hotel.exceptions.NullInputException;

public class IOUtils {
	
	private static Scanner stdin;
	private static boolean traceSet = false;
	
	
	public static void setTrace(boolean trace) {
		traceSet = trace;
	}

	
	public static void output(Object o) {
		System.out.print(o);
	}

	
	public static void outputln(Object o) {
		System.out.println(o);
	}

	
	public static void trace(Object o) {
		if (traceSet) {
			System.err.println(o);			
		}
	}

	
	public static String input(String prompt) {
		output(prompt);
		return nextln();
		
	}


	public static String nextln() {
		if (stdin == null) {
		    stdin = new Scanner(System.in);
		}
		String ans = stdin.nextLine();
		return ans.trim();
	}
	
	
	public static int getValidPositiveInt(String prompt) {
		int number = 0;
		while (number <= 0) {
			try {
				String in = IOUtils.input(prompt);
				if (in.isEmpty()) {
					throw new NullInputException();
				}
				number = Integer.parseInt(in);
				if (number <= 0) {
					throw new NumberFormatException();
				}
			}
			catch (NumberFormatException e) {
				IOUtils.outputln("Invalid number, please try again");
				continue;
			}
		}
		return number;		
	}
	
	
	public static Date getValidDate(String prompt) {		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;

		IOUtils.outputln(prompt);		
		boolean invalid = true;		
		while (invalid) {
			try {
				String dayStr = IOUtils.input("Enter day (dd): ");
				if (dayStr.isEmpty()) {
					IOUtils.outputln("User reset at enter arrival day");
					throw new NullInputException();
				}
				String monthStr = IOUtils.input("Enter month (mm): ");
				if (monthStr.isEmpty()) {
					IOUtils.outputln("User reset at enter arrival month");
					continue;
				}
				String yearStr = IOUtils.input("Enter year (yyyy): ");
				if (yearStr.isEmpty()) {
					continue;
				}
				String dateStr = String.format("%s-%s-%s", dayStr, monthStr, yearStr);
				date = format.parse(dateStr);
				invalid = false;
			}
			catch (ParseException e) {
				IOUtils.outputln("Invalid date, please try again");
				continue;
			}
		}
		return date;
	}
	
	
	public static RoomType getValidRoomType(String prompt) {
		RoomType roomTypeSelection = null;
		
		boolean valid = false;
		while (!valid) {
			for (RoomType r : RoomType.values()) {
				String line = String.format("%s:\t%s, %d", 
						r.getIdentifier(), r.getDescription(), r.getMaxOccupancy());
				IOUtils.outputln(line);
			}
			String roomStr = IOUtils.input("Enter room type selection: ").toUpperCase();
			if (roomStr.isEmpty()) {
				throw new NullInputException();
			}
			roomTypeSelection = RoomType.getRoomTypeByIdentifier(roomStr);
			if (roomTypeSelection != null) {
				valid = true;
			}
			else {
				IOUtils.outputln("Invalid selection, please try again");
			}
		}
		return roomTypeSelection;		
	}


	public static CreditCardType getValidCreditType(String prompt) {
		String cardSelector = null;
		
		boolean validType = false;
		while (!validType) {
			for (CreditCardType ct : CreditCardType.values()) {
				String line = String.format("%s:\t%s", 
						ct.getIdentifier(), ct.getVendor());
				IOUtils.outputln(line);
			}
			cardSelector = IOUtils.input("Enter credit card type selection: ").toUpperCase();
			if (cardSelector.isEmpty()) {
				throw new NullInputException();
			}
			validType = CreditCardType.isValidIdentifier(cardSelector);
			if (!validType) {
				IOUtils.outputln("Invalid selection, please try again");
			}
		}
		CreditCardType creditCardType = CreditCardType.getCardTypeByIdentifier(cardSelector);
		return creditCardType;		
	}


	public static ServiceType getValidServiceType(String prompt) {
		ServiceType serviceType = null;
		
		boolean valid = false;
		while (!valid) {
			for (ServiceType r : ServiceType.values()) {
				String line = String.format("%s:\t%s", 
						r.getIdentifier(), r.getDescription());
				IOUtils.outputln(line);
			}
			String roomStr = IOUtils.input(prompt).toUpperCase();
			if (roomStr.isEmpty()) {
				throw new NullInputException();
			}
			serviceType = ServiceType.getServiceTypeByIdentifier(roomStr);
			if (serviceType != null) {
				valid = true;
			}
			else {
				IOUtils.outputln("Invalid selection, please try again");
			}
		}
		return serviceType;		
	}


	public static boolean getBooleanYesNoAnswer(String prompt) {
		String answer = null;
		
		boolean validAnswer = false;
		while (!validAnswer) {
			answer = IOUtils.input(prompt + "(Y/N) : ").toUpperCase().trim();
			if (answer.isEmpty()) {
				throw new NullInputException();
			}
			validAnswer = answer.equals("N") || answer.equals("Y");
			if (!validAnswer) {
				IOUtils.outputln("Invalid answer, please try again");
			}
		}
		return answer.equals("Y");
	}


	public static long getValidPositiveLong(String prompt) {
		long number = 0;
		while (number <= 0) {
			try {
				String in = IOUtils.input(prompt);
				if (in.isEmpty()) {
					throw new NullInputException();
				}
				number = Long.parseLong(in);
				if (number <= 0) {
					throw new NumberFormatException();
				}
			}
			catch (NumberFormatException e) {
				IOUtils.outputln("Invalid number, please try again");
				continue;
			}
		}
		return number;		
	}


	public static double getValidPositiveDouble(String prompt) {
		double number = 0;
		while (number <= 0) {
			try {
				String in = IOUtils.input(prompt);
				if (in.isEmpty()) {
					throw new NullInputException();
				}
				number = Double.parseDouble(in);
				if (number <= 0) {
					throw new NumberFormatException();
				}
			}
			catch (NumberFormatException e) {
				IOUtils.outputln("Invalid number, please try again");
				continue;
			}
		}
		return number;		
	}
	
}
