package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

import junit.framework.Assert;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;
	private static TicketDAO ticketDAO;
	private static DataBasePrepareService dataBasePrepareService;

	@Mock
	private static InputReaderUtil inputReaderUtil;

	@BeforeAll
	private static void setUp() throws Exception {
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
	}

	@BeforeEach
	private void setUpPerTest() throws Exception {
		//when(inputReaderUtil.readSelection()).thenReturn(1);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		dataBasePrepareService.clearDataBaseEntries();
	}

	@AfterAll
	private static void tearDown() {
		dataBasePrepareService.clearDataBaseEntries();
	}

	@Test
	public void testParkingACar() {
		 //GIVEN
        when(inputReaderUtil.readSelection()).thenReturn(1);
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        
        //WHEN
        parkingService.processIncomingVehicle();
        
        //THEN
        //check that a ticket is actualy saved in DB and Parking table is updated with availability
        Ticket ticketUnderTest = ticketDAO.getTicket("ABCDEF");
        assertNotNull(ticketUnderTest);
        int nextAvailableSpot = parkingSpotDAO.getNextAvailableSpot(ParkingType.CAR);
        assertNotEquals(1, nextAvailableSpot);
	}

	@Test
	public void testParkingABike() {
		 //GIVEN
        when(inputReaderUtil.readSelection()).thenReturn(2);
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        
        //WHEN
        parkingService.processIncomingVehicle();
        
        //THEN
        //check that a ticket is actualy saved in DB and Parking table is updated with availability
        Ticket ticketUnderTest = ticketDAO.getTicket("ABCDEF");
        assertNotNull(ticketUnderTest);
        int nextAvailableSpot = parkingSpotDAO.getNextAvailableSpot(ParkingType.BIKE);
        assertNotEquals(1, nextAvailableSpot);
	}

	@Test	
	public void testParkingPotExit() {
		//GIVEN
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        Ticket t = new Ticket();
        
        t.setParkingSpot(new ParkingSpot(2, ParkingType.CAR, false));
        t.setId(1);
        t.setVehicleRegNumber("ABCDEF");
        t.setPrice(0);
        t.setInTime(new Date(System.currentTimeMillis() - 60 * 60 * 1000));
        t.setOutTime(null);
        
         ticketDAO.saveTicket(t);
        parkingSpotDAO.updateParking(t.getParkingSpot());

        //WHEN
        parkingService.processExitingVehicle();

        //THEN
        //check that the fare generated and out time are populated correctly in the database
        Ticket ticketUnderTest = ticketDAO.getTicket("ABCDEF");
        assertNotEquals(null, ticketUnderTest.getOutTime());
        assertEquals(1.5,ticketUnderTest.getPrice());
	}
	
	@Test
	public void testParkingPotExitfor24Hours() {
		//GIVEN
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        Ticket t = new Ticket();
        
        t.setParkingSpot(new ParkingSpot(2, ParkingType.CAR, false));
        t.setId(1);
        t.setVehicleRegNumber("ABCDEF");
        t.setPrice(0);
        t.setInTime(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000));
        t.setOutTime(null);
        
         ticketDAO.saveTicket(t);
        parkingSpotDAO.updateParking(t.getParkingSpot());

        //WHEN
        parkingService.processExitingVehicle();

        //THEN
        //check that the fare generated and out time are populated correctly in the database
        Ticket ticketUnderTest = ticketDAO.getTicket("ABCDEF");
        assertNotEquals(null, ticketUnderTest.getOutTime());
        assertEquals((1.5*24),ticketUnderTest.getPrice());
	}
	
	@Test
	public void testParkingPotExitLessThan3OMinute() {
		//GIVEN
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        Ticket t = new Ticket();
        
        t.setParkingSpot(new ParkingSpot(2, ParkingType.CAR, false));
        t.setId(1);
        t.setVehicleRegNumber("ABCDEF");
        t.setPrice(0);
        t.setInTime(new Date(System.currentTimeMillis() - 30 * 60 * 1000));
        t.setOutTime(null);
        
         ticketDAO.saveTicket(t);
        parkingSpotDAO.updateParking(t.getParkingSpot());

        //WHEN
        parkingService.processExitingVehicle();

        //THEN
        //check that the fare generated and out time are populated correctly in the database
        Ticket ticketUnderTest = ticketDAO.getTicket("ABCDEF");
        assertNotEquals(null, ticketUnderTest.getOutTime());
        assertNotEquals(0,ticketUnderTest.getPrice());
	}

}
