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
	
		double duration = ((double) (outHour - inHour)) / (1000 * 60 * 60);

		if (duration > 0.5) {
			switch (ticket.getParkingSpot().getParkingType()) {
				case CAR: {
					ticket.setPrice(Math.round(duration * Fare.CAR_RATE_PER_HOUR *100.00)/100.00);
					break;
				}
				case BIKE: {
					ticket.setPrice(Math.round(duration * Fare.BIKE_RATE_PER_HOUR *100.00)/100.00);
					break;
				}
				default:
					throw new IllegalArgumentException("Unkown Parking Type");
				}
		} else {
			ticket.setPrice(0.00);
		}
		if (discount) {
			ticket.setPrice(Math.round((0.95*ticket.getPrice())*100.00)/100.00);
		}
		
	}
}