package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;
import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

/**
 * @author cisse
 *
 *         this class FareCalculatorServiceTest are the basic class where the
 *         test of far calculating
 */
public class FareCalculatorServiceTest {

	private static FareCalculatorService fareCalculatorService;
	private Ticket ticket;

	@BeforeAll
	public static void setUp() {
		fareCalculatorService = new FareCalculatorService();
	}

	@BeforeEach
	public void setUpPerTest() {
		ticket = new Ticket();
	}

	@Test
	public void calculateFareCar() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, 1); // First time in the parking
		assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
	}

	@Test
	public void calculateFareBike() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, 1); // First time in the parking
		assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
	}

	@Test
	public void calculateFareUnkownType() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket, 1));
	}

	@Test
	public void calculateFareBikeWithFutureInTime() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket, 1));
	}

	@Test
	public void calculateFareBikeWithLessThanOneHourParkingTime() {
		Random rand = new Random();
		int minutes = rand.nextInt(59 - 31 + 1) + 31;
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (minutes * 60 * 1000));

		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, 1);// First time in the parking
		assertEquals(((minutes / 60) * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareCarWithLessThanOneHourParkingTime() {
		Random rand = new Random();
		int minutes = rand.nextInt(59 - 31 + 1) + 31;
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (minutes * 60 * 1000));// 45 minutes parking time should give 3/4th
																			// parking fare
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, 1);// First time in the parking
		assertEquals(((minutes / 60) * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareCarWithMoreThanADayParkingTime() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));// 24 hours parking time should give 24 *
																			// parking fare per hour
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, 1);// First time in the parking
		assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareBikeWithMoreThanADayParkingTime() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));// 24 hours parking time should give 24 *
																			// parking fare per hour
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, 1);// First time in the parking
		assertEquals((24 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareBikeWithLessThanThirtyMinParkingTime() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));// 30 minutes parking time should give zero
																		// parking fare
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, 1);// First time in the parking
		assertEquals((0 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareCarWithLessThanThirtyMinParkingTime() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));// 30 minutes parking time should give zero
																		// parking fare
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, 1);// First time in the parking
		assertEquals((0 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	// these tests for five percent discount
	@Test
	public void calculateFareCarWithFivePercentDiscount() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, 2);// Current user of parking, more than 1 time
		assertEquals((Fare.CAR_RATE_PER_HOUR - (Fare.CAR_RATE_PER_HOUR * 0.05)), ticket.getPrice());
	}

	@Test
	public void calculateFareBikeWithFivePercentDiscount() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, 2);// Current user of parking, more than 1 time
		assertEquals(Fare.BIKE_RATE_PER_HOUR - (Fare.BIKE_RATE_PER_HOUR * 0.05), ticket.getPrice());
	}

	@Test
	public void calculateFareBikeWithLessThanOneHourParkingTimeWithFivePercentDiscount() {
		Random rand = new Random();
		int minutes = rand.nextInt(59 - 31 + 1) + 31;
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (minutes * 60 * 1000));// 45 minutes parking time should give 3/4th
																			// parking fare
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, 2);// Current user of parking, more than 1 time
		assertEquals((((minutes / 60) * Fare.BIKE_RATE_PER_HOUR) - (((minutes / 60) * Fare.BIKE_RATE_PER_HOUR) * 0.05)),
				ticket.getPrice());
	}

	@Test
	public void calculateFareCarWithLessThanOneHourParkingTimeWithFivePercentDiscount() {
		Random rand = new Random();
		int minutes = rand.nextInt(59 - 31 + 1) + 31;
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (minutes * 60 * 1000));// 45 minutes parking time should give 3/4th
																			// parking fare
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, 2);// Current user of parking, more than 1 time
		assertEquals((((minutes / 60) * Fare.CAR_RATE_PER_HOUR) - (((minutes / 60) * Fare.CAR_RATE_PER_HOUR) * 0.05)),
				ticket.getPrice());
	}

	@Test
	public void calculateFareCarWithMoreThanADayParkingTimeWithFivePercentDiscount() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));// 24 hours parking time should give 24 *
																			// parking fare per hour
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, 2);// Current user of parking, more than 1 time
		assertEquals(((24 * Fare.CAR_RATE_PER_HOUR) - ((24 * Fare.CAR_RATE_PER_HOUR) * 0.05)), ticket.getPrice());
	}

	@Test
	public void calculateFareBikeWithMoreThanADayParkingTimeWithFivePercentDiscount() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));// 24 hours parking time should give 24 *
																			// parking fare per hour
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, 2);// Current user of parking, more than 1 time
		assertEquals(((24 * Fare.BIKE_RATE_PER_HOUR) - ((24 * Fare.BIKE_RATE_PER_HOUR * 0.05))), ticket.getPrice());
	}

	@Test
	public void calculateFareBikeWithLessThanThirtyMinParkingTimeWithFivePercentDiscount() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));// 30 minutes parking time should give zero
																		// parking fare
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, 2);// Current user of parking, more than 1 time
		assertEquals((0 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareCarWithLessThanThirtyMinParkingTimeWithFivePercentDiscount() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));// 30 minutes parking time should give zero
																		// parking fare
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, 2);// Current user of parking, more than 1 time
		assertEquals((0 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}
}
