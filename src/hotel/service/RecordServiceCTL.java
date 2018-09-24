package hotel.service;

import hotel.entities.Booking;
import hotel.entities.Hotel;
import hotel.entities.ServiceType;
import hotel.utils.IOUtils;

public class RecordServiceCTL {
	
	static enum State {ROOM, SERVICE, CHARGE, CANCELLED, COMPLETED};
	
	Hotel hotel;
	RecordServiceUI recordServiceUI;
	State state;
	
	Booking booking;
	int roomNumber;


	public RecordServiceCTL(Hotel hotel) {
		this.recordServiceUI = new RecordServiceUI(this);
		state = State.ROOM;
		this.hotel = hotel;
	}

	
	public void run() {		
		IOUtils.trace("PayForServiceCTL: run");
		recordServiceUI.run();
	}


	public void roomNumberEntered(int roomNumber) {
		if (state != State.ROOM) {
			String mesg = String.format("PayForServiceCTL: roomNumberEntered : bad state : %s", state);
			throw new RuntimeException(mesg);
		}
		booking = hotel.findActiveBookingByRoomId(roomNumber);
		if (booking == null) {
			String mesg = String.format("No active booking for room id: %d", roomNumber);
			recordServiceUI.displayMessage(mesg);
		}
		else {
			this.roomNumber = roomNumber;
			state = State.SERVICE;
			recordServiceUI.setState(RecordServiceUI.State.SERVICE);
		}
	}
	
	
	public void serviceDetailsEntered(ServiceType serviceType, double cost) {
		if (state != State.SERVICE) {
			String mesg = String.format("PayForServiceCTL: serviceDetailsEntered : bad state : %s", state);
			throw new RuntimeException(mesg);
		}
		hotel.addServiceCharge(roomNumber, serviceType, cost);
		
		recordServiceUI.displayServiceChargeMessage(roomNumber, cost, serviceType.getDescription());
		state = State.COMPLETED;
		recordServiceUI.setState(RecordServiceUI.State.COMPLETED);
	}


	public void cancel() {
		recordServiceUI.displayMessage("Pay for service cancelled");
		state = State.CANCELLED;
		recordServiceUI.setState(RecordServiceUI.State.CANCELLED);
	}


	public void completed() {
		recordServiceUI.displayMessage("Pay for service completed");
	}

	

}
