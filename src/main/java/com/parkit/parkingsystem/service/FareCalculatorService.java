package com.parkit.parkingsystem.service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public void calculateFare(Ticket ticket, int vehicleRegNumberReccurrence) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		Date inDate = ticket.getInTime();
		System.out.println("in date" + inDate);
		Date outDate = ticket.getOutTime();
		System.out.println("out date" + outDate);

		long difference_In_Time = outDate.getTime() - inDate.getTime();
		System.out.println("difference_In_Time : " + difference_In_Time);

		long vehicleInHourTime = TimeUnit.MILLISECONDS.toHours(difference_In_Time);
		System.out.println("in hour time" + vehicleInHourTime);
		long vehicleInMinutesTime = TimeUnit.MILLISECONDS.toMinutes(difference_In_Time);
		System.out.println("in minute time" + vehicleInMinutesTime);

		if (vehicleRegNumberReccurrence > 1) {
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
				System.out.println("dans autres if esle");
				if (vehicleInHourTime == 1) {
					System.out.println("dans if");
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
					System.out.println("dans esle");
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