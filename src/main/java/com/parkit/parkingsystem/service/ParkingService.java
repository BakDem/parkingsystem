package com.parkit.parkingsystem.service;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;

/**
 * @author cisse
 *
 *         ParkingService is the class where the basics parking services are
 *         implemented.
 */
public class ParkingService {

	private static final Logger logger = LogManager.getLogger("ParkingService");

	private static FareCalculatorService fareCalculatorService = new FareCalculatorService();

	private InputReaderUtil inputReaderUtil;
	private ParkingSpotDAO parkingSpotDAO;
	private TicketDAO ticketDAO;

	private int vehicleRegNumberReccurence;

	public ParkingService(InputReaderUtil inputReaderUtil, ParkingSpotDAO parkingSpotDAO, TicketDAO ticketDAO) {
		this.inputReaderUtil = inputReaderUtil;
		this.parkingSpotDAO = parkingSpotDAO;
		this.ticketDAO = ticketDAO;
	}

	/**
	 * This method is how to save a vehicle when it in the parking.
	 */
	public void processIncomingVehicle() {
		try {
			ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();
			if (parkingSpot != null && parkingSpot.getId() > 0) {
				String vehicleRegNumber = getVehichleRegNumber();
				vehicleRegNumberReccurence = ticketDAO.getReccurence(vehicleRegNumber);
				// System.out.println(vehicleRegNumber+" existe : "+
				// vehicleRegNumberReccurence+" times.");
				if (vehicleRegNumberReccurence > 0) {
					System.out.println(
							"\"Welcome back! As a recurring user of our parking lot, you'll benefit from a 5% discount.\"");
				}
				parkingSpot.setAvailable(false);
				parkingSpotDAO.updateParking(parkingSpot);// allot this parking space and mark it's availability as
															// false

				Date inTime = new Date();
				Ticket ticket = new Ticket();
				// ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
				// ticket.setId(ticketID);
				ticket.setParkingSpot(parkingSpot);
				ticket.setVehicleRegNumber(vehicleRegNumber);
				ticket.setPrice(0);
				ticket.setInTime(inTime);
				ticket.setOutTime(null);
				ticketDAO.saveTicket(ticket);
				System.out.println("Generated Ticket and saved in DB");
				System.out.println("Please park your vehicle in spot number:" + parkingSpot.getId());
				System.out.println("Recorded in-time for vehicle number:" + vehicleRegNumber + " is:" + inTime);
			}
		} catch (Exception e) {
			logger.error("Unable to process incoming vehicle", e);
		}
	}

	/**
	 * @return
	 * @throws Exception
	 * 
	 *                   this method allow for getting a vehicle register number
	 *                   from the user keyboard.
	 */
	public String getVehichleRegNumber() throws Exception {
		System.out.println("Please type the vehicle registration number and press enter key");
		return inputReaderUtil.readVehicleRegistrationNumber();
	}

	/**
	 * @return
	 * 
	 *         this method allow for getting the next spot available in the parking.
	 */
	public ParkingSpot getNextParkingNumberIfAvailable() {
		int parkingNumber = 0;
		ParkingSpot parkingSpot = null;
		try {
			ParkingType parkingType = getVehichleType();
			parkingNumber = parkingSpotDAO.getNextAvailableSpot(parkingType);
			if (parkingNumber > 0) {
				parkingSpot = new ParkingSpot(parkingNumber, parkingType, true);
			} else {
				throw new Exception("Error fetching parking number from DB. Parking spots might be full");
			}
		} catch (IllegalArgumentException ie) {
			logger.error("Error parsing user input for type of vehicle", ie);
		} catch (Exception e) {
			logger.error("Error fetching next available parking spot", e);
		}
		return parkingSpot;
	}

	/**
	 * @return This method permits to get the type of the vehicle.
	 */
	private ParkingType getVehichleType() {
		System.out.println("Please select vehicle type from menu");
		System.out.println("1 CAR");
		System.out.println("2 BIKE");
		int input = inputReaderUtil.readSelection();
		switch (input) {
		case 1: {
			return ParkingType.CAR;
		}
		case 2: {
			return ParkingType.BIKE;
		}
		default: {
			System.out.println("Incorrect input provided");
			throw new IllegalArgumentException("Entered input is invalid");
		}
		}
	}

	/**
	 * This method is how to process an exiting of a vehicle in the parking.
	 */
	public void processExitingVehicle() {
		try {
			String vehicleRegNumber = getVehichleRegNumber();
			vehicleRegNumberReccurence = ticketDAO.getReccurence(vehicleRegNumber);

			Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);
			Date outTime = new Date();
			ticket.setOutTime(outTime);
			fareCalculatorService.calculateFare(ticket, vehicleRegNumberReccurence);
			if (ticketDAO.updateTicket(ticket)) {
				ParkingSpot parkingSpot = ticket.getParkingSpot();
				parkingSpot.setAvailable(true);
				parkingSpotDAO.updateParking(parkingSpot);
				System.out.println("Please pay the parking fare:" + ticket.getPrice());
				System.out.println(
						"Recorded out-time for vehicle number:" + ticket.getVehicleRegNumber() + " is:" + outTime);
			} else {
				System.out.println("Unable to update ticket information. Error occurred");
			}
		} catch (Exception e) {
			logger.error("Unable to process exiting vehicle", e);
		}
	}
}
