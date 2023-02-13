package com.parkit.parkingsystem.service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

/**
 * @author cisse
 *
 *         FareCalculatorService is the class where the different fare are
 *         calculate
 */
public class FareCalculatorService {

	/**
	 * @param ticket
	 * @param vehicleRegNumberReccurrence
	 * 
	 *                                    the method calculateFare permits to
	 *                                    calculate different fare.
	 */
	public void calculateFare(Ticket ticket, int vehicleRegNumberReccurrence) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		Date inDate = ticket.getInTime(); // get in time
		Date outDate = ticket.getOutTime(); // get out time

		long difference_In_Time = outDate.getTime() - inDate.getTime(); // get parking duration in millisecond

		long vehicleInHourTime = TimeUnit.MILLISECONDS.toHours(difference_In_Time); // parking duration in hours
		long vehicleInMinutesTime = TimeUnit.MILLISECONDS.toMinutes(difference_In_Time); // parking duration in minutes

		if (vehicleRegNumberReccurrence > 1) {
			// the following instructions will be executed only for recurrence user.
			// A 5% discount is granted for the recurrence user.
			if (vehicleInHourTime == 0 && vehicleInMinutesTime > 30) {
				switch (ticket.getParkingSpot().getParkingType()) {
				case CAR: {
					ticket.setPrice(((vehicleInMinutesTime / 60) * Fare.CAR_RATE_PER_HOUR)
							- (0.05 * ((vehicleInMinutesTime / 60) * Fare.CAR_RATE_PER_HOUR)));
					break;
				}
				case BIKE: {
					ticket.setPrice(((vehicleInMinutesTime / 60) * Fare.BIKE_RATE_PER_HOUR)
							- (0.05 * ((vehicleInMinutesTime / 60) * Fare.BIKE_RATE_PER_HOUR)));
					break;
				}
				default:
					throw new IllegalArgumentException("Unkown Parking Type");
				}
			} else if (vehicleInHourTime == 0 && vehicleInMinutesTime <= 30) {
				switch (ticket.getParkingSpot().getParkingType()) {
				case CAR: {
					ticket.setPrice(0);
					break;
				}
				case BIKE: {
					ticket.setPrice(0);
					break;
				}
				default:
					throw new IllegalArgumentException("Unkown Parking Type");
				}
			}

			else {
				if (vehicleInHourTime == 1) {
					switch (ticket.getParkingSpot().getParkingType()) {
					case CAR: {
						ticket.setPrice(Fare.CAR_RATE_PER_HOUR - (0.05 * Fare.CAR_RATE_PER_HOUR));
						break;
					}
					case BIKE: {
						ticket.setPrice(Fare.BIKE_RATE_PER_HOUR - (0.05 * Fare.BIKE_RATE_PER_HOUR));
						break;
					}
					default:
						throw new IllegalArgumentException("Unkown Parking Type");
					}
				} else {
					switch (ticket.getParkingSpot().getParkingType()) {
					case CAR: {
						ticket.setPrice((vehicleInHourTime * Fare.CAR_RATE_PER_HOUR)
								- (0.05 * (vehicleInHourTime * Fare.CAR_RATE_PER_HOUR)));
						break;
					}
					case BIKE: {
						ticket.setPrice((vehicleInHourTime * Fare.BIKE_RATE_PER_HOUR)
								- (0.05 * (vehicleInHourTime * Fare.BIKE_RATE_PER_HOUR)));
						break;
					}
					default:
						throw new IllegalArgumentException("Unkown Parking Type");
					}
				}

			}
		} else {
			// the following instructions will be executed only for user first time.
			if (vehicleInHourTime == 0 && vehicleInMinutesTime > 30) {
				switch (ticket.getParkingSpot().getParkingType()) {
				case CAR: {
					ticket.setPrice((vehicleInMinutesTime / 60) * Fare.CAR_RATE_PER_HOUR);
					break;
				}
				case BIKE: {
					ticket.setPrice((vehicleInMinutesTime / 60) * Fare.BIKE_RATE_PER_HOUR);
					break;
				}
				default:
					throw new IllegalArgumentException("Unkown Parking Type");
				}
			} else if (vehicleInHourTime == 0 && vehicleInMinutesTime <= 30) {
				switch (ticket.getParkingSpot().getParkingType()) {
				case CAR: {
					ticket.setPrice(0);
					break;
				}
				case BIKE: {
					ticket.setPrice(0);
					break;
				}
				default:
					throw new IllegalArgumentException("Unkown Parking Type");
				}
			}

			else {
				if (vehicleInHourTime == 1) {
					switch (ticket.getParkingSpot().getParkingType()) {
					case CAR: {
						ticket.setPrice(Fare.CAR_RATE_PER_HOUR);
						break;
					}
					case BIKE: {
						ticket.setPrice(Fare.BIKE_RATE_PER_HOUR);
						break;
					}
					default:
						throw new IllegalArgumentException("Unkown Parking Type");
					}
				} else {
					switch (ticket.getParkingSpot().getParkingType()) {
					case CAR: {
						ticket.setPrice(vehicleInHourTime * Fare.CAR_RATE_PER_HOUR);
						break;
					}
					case BIKE: {
						ticket.setPrice(vehicleInHourTime * Fare.BIKE_RATE_PER_HOUR);
						break;
					}
					default:
						throw new IllegalArgumentException("Unkown Parking Type");
					}
				}
			}
		}

	}
}