package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public void calculateFare(Ticket ticket) {
		calculateFare(ticket, false);

	}
	
	public void calculateFare(Ticket ticket, boolean discount) {
		if ((ticket.getOutTime() == null) || ticket.getOutTime().before(ticket.getInTime())) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		} 

		long inHour = ticket.getInTime().getTime();
		long outHour = ticket.getOutTime().getTime();

		// TODO: Some tests are failing here. Need to check if this logic is correct
		double duration = ((double) (outHour - inHour)) / (1000 * 60 * 60);

		if (duration > 0.5) {
			switch (ticket.getParkingSpot().getParkingType()) {
				case CAR: {
					ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
					break;
				}
				case BIKE: {
					ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
					break;
				}
				default:
					throw new IllegalArgumentException("Unkown Parking Type");
				}

		} else {
			ticket.setPrice(0);

		}
		if (discount) {
			ticket.setPrice(0.95*ticket.getPrice());
		}
		
	
	}
}