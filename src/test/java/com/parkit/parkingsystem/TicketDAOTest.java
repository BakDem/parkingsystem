package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
@ExtendWith(MockitoExtension.class)
public class TicketDAOTest {

	private static TicketDAO ticketDAO;
	private static DataBasePrepareService dataBasePrepService;
	
	@Mock
	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	@Mock
	private static Connection connection;
	@Mock
	private static PreparedStatement myPrepareStatement;
	
	@BeforeAll
	public static void setUp() throws ClassNotFoundException, SQLException {
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepService = new DataBasePrepareService();
	}
	
	@BeforeEach
	public void eachTestSetUp() throws ClassNotFoundException, SQLException  {
		dataBasePrepService.clearDataBaseEntries();
	
	}
	
	@Test
	public void saveTicketTest() {
		//GIVEN :
		//A Car ticket : spot = 1, registerNumber = ABCDEF, price = DBprise5(1.5), inTime = DBInTime, outTime = DBOutTime
		Ticket myTicketTest = new Ticket();
		myTicketTest.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
		myTicketTest.setVehicleRegNumber("ABCDEF");
		myTicketTest.setPrice(1.5);
		myTicketTest.setInTime(new Date());
		myTicketTest.setOutTime(new Date());
		
		//WHEN : save the ticket
		boolean myBoolean = ticketDAO.saveTicket(myTicketTest);
		
		//THEN : we can find the same ticket from the DataBase
		assertFalse(myBoolean);
		Ticket dataFromDataBase = ticketDAO.getTicket("ABCDEF");
		assertEquals(dataFromDataBase.getParkingSpot().getId(), 1);
		assertEquals(dataFromDataBase.getParkingSpot().getParkingType(), ParkingType.CAR);
		assertEquals(dataFromDataBase.getVehicleRegNumber(), "ABCDEF");
		assertEquals(dataFromDataBase.getPrice(), 1.5);
		assertNotNull(dataFromDataBase.getInTime());
		assertNotNull(dataFromDataBase.getOutTime());
	}
	
	@Test
	public void saveTicketTestNoOutTime() {
		//GIVEN :
		//A Car ticket : spot = 1, registerNumber = ABCDEF, price = DBprise5(1.5), inTime = DBInTime, outTime = ?
		Ticket myTicketTest = new Ticket();
		myTicketTest.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
		myTicketTest.setVehicleRegNumber("ABCDEF");
		myTicketTest.setPrice(1.5);
		myTicketTest.setInTime(new Date());
		myTicketTest.setOutTime(null);
		
		//WHEN : save the ticket
		boolean myBoolean = ticketDAO.saveTicket(myTicketTest);
		
		//THEN : we can find the same ticket from the DataBase
		assertFalse(myBoolean);
		Ticket dataFromDataBase = ticketDAO.getTicket("ABCDEF");
		assertEquals(dataFromDataBase.getParkingSpot().getId(), 1);
		assertEquals(dataFromDataBase.getParkingSpot().getParkingType(), ParkingType.CAR);
		assertEquals(dataFromDataBase.getVehicleRegNumber(), "ABCDEF");
		assertEquals(dataFromDataBase.getPrice(), 1.5);
		assertNotNull(dataFromDataBase.getInTime());
		assertNull(dataFromDataBase.getOutTime());
	}
	
	@Test
	public void getTicketTest() {
		//GIVEN
		//A Car ticket : spot = 1, registerNumber = ABCDEF, price = DBprise5(1.5), inTime = DBInTime, outTime = ?
		Ticket myTicketTest = new Ticket();
		myTicketTest.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
		myTicketTest.setVehicleRegNumber("ABCDEF");
		myTicketTest.setPrice(1.5);
		myTicketTest.setInTime(new Date());
		myTicketTest.setOutTime(new Date());
		
		//WHEN
		ticketDAO.saveTicket(myTicketTest);
		Ticket myTicket =  ticketDAO.getTicket(myTicketTest.getVehicleRegNumber());
		
		//THEN
		assertEquals(myTicket.getParkingSpot().getId(), myTicketTest.getParkingSpot().getId());
		assertEquals(myTicket.getParkingSpot().getParkingType(), myTicketTest.getParkingSpot().getParkingType());
		assertEquals(myTicket.getVehicleRegNumber(), myTicketTest.getVehicleRegNumber());
		assertEquals(myTicket.getPrice(), myTicketTest.getPrice());
		assertNotNull(myTicketTest.getInTime());
		assertNotNull(myTicketTest.getOutTime());
	}
	
	@Test
	public void updateTicketTest() throws SQLException {
		//GIVEN
		Ticket myTicketTest = new Ticket();
		myTicketTest.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
		myTicketTest.setVehicleRegNumber("ABCDEF");
		myTicketTest.setPrice(1.5);
		myTicketTest.setInTime(new Date());
		myTicketTest.setOutTime(new Date());
		
		//WHEN
		//when(connection.prepareStatement(DBConstants.UPDATE_TICKET)).thenReturn(myPrepareStatement);
		//when(myPrepareStatement.executeUpdate()).thenReturn(1);
			
		//THEN
		assertEquals(ticketDAO.updateTicket(myTicketTest), true);
		
	}
	
	@Test
	public void getReccurrenceTest() {
		//GIVEN
		String vehicleRegisterNumber = "ABCDEF";
		
		//WHEN
		int myReccurenceNumber = ticketDAO.getReccurence(vehicleRegisterNumber);
		
		//THEN
		assertEquals(myReccurenceNumber, 0);
	}
}
