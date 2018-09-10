package hotel.service;

import hotel.entities.ServiceType;
import hotel.exceptions.CancelException;
import hotel.exceptions.NullInputException;
import hotel.utils.IOUtils;

public class RecordServiceUI {
	
	public enum State {ROOM, SERVICE, CANCELLED, COMPLETED}
	
	private RecordServiceCTL recordServiceCTL;
	private State state;

	
	public RecordServiceUI(RecordServiceCTL payForServiceCTL) {
		this.recordServiceCTL = payForServiceCTL;
		this.state = State.ROOM;
	}

	
	public void run() {
		IOUtils.trace("RecordServiceUI: run");
		
		boolean completed = false;
		
		while (!completed) {
			try {
				switch (state) {
				
				case ROOM:
					int roomNumber = enterRoomDetails();
					recordServiceCTL.roomNumberEntered(roomNumber);
					break;
					
				case SERVICE:
					ServiceDetails details = enterServiceDetails();
					recordServiceCTL.serviceDetailsEntered(details.serviceType, details.cost);
					break;
					
				case CANCELLED:
					completed = true;
					break;
					
				case COMPLETED:
					IOUtils.input("Hit <enter> to continue");
					recordServiceCTL.completed();
					completed = true;
					break;
					
				default:
					String mesg = String.format("RecordServiceUI: run : unknown state : %s", state);
					IOUtils.outputln(mesg);
				}
			}
			catch (CancelException e) {
				recordServiceCTL.cancel();
			}
		}
	}


	
	private int enterRoomDetails() {
		IOUtils.trace("RecordServiceUI: enterRoomDetails");
		int roomId = 0;
		
		try {
			roomId = IOUtils.getValidPositiveInt("Enter Room Id: ");
			return roomId;
		}
		catch (NullInputException e) {
			IOUtils.outputln("RecordServiceUI: User cancelled at input room id");
			throw new CancelException();
		}	
	}


	private ServiceDetails enterServiceDetails() {
		IOUtils.trace("RecordServiceUI: enterServiceDetails");
		ServiceType serviceType = null;
		double cost = 0.0;
		
		boolean completed = false;
		while (!completed) {			
			//enter service type
			try {
				serviceType = IOUtils.getValidServiceType("Enter service type");
			}
			catch (NullInputException e) {
				IOUtils.outputln("RecordServiceUI: User cancelled at enter service type");
				throw new CancelException();
			}			
			//enter cost
			try {
				cost = IOUtils.getValidPositiveDouble("Enter cost: ");
			}
			catch (NullInputException e) {
				IOUtils.outputln("RecordServiceUI: User reset at input cost");
				continue;
			}	
			completed = true;
		}		
		ServiceDetails details = new ServiceDetails(serviceType, cost);
		return details;
	}


	public void displayServiceChargeMessage(int roomNumber, double cost, String serviceDescription) {
		String mesg = String.format("\nRoom %d charged $%.2f for %s\n",
				roomNumber, cost, serviceDescription);
		IOUtils.outputln(mesg);				
	}

	
	public void displayMessage(String mesg) {
		IOUtils.outputln(mesg);		
	}	
	

	public void setState(State state) {
		this.state = state;
	}


	
	class ServiceDetails{	
		ServiceType serviceType;
		double cost;
	
		public ServiceDetails(ServiceType serviceType, double cost) {
			this.serviceType = serviceType;
			this.cost = cost;
		}
	}



}
