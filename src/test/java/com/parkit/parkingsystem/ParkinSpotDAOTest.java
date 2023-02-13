package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;

/**
 * @author cisse this class ParkinSpotDAOTest are the basic class where the test
 *         of parking spot data access object are calculate
 */
@ExtendWith(MockitoExtension.class)
public class ParkinSpotDAOTest {

	private static ParkingSpotDAO parkingSpotDAO;

	@Mock
	private static DataBaseTestConfig dataBaseTestConfig;
	@Mock
	private static PreparedStatement myPrepareStatement;
	@Mock
	private static Connection connection;
	@Mock
	private static ResultSet resultSet;

	@BeforeEach
	public void setUp() throws ClassNotFoundException, SQLException {
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;

		when(dataBaseTestConfig.getConnection()).thenReturn(connection);
	}

	@Test
	public void getNextAvailableSpotTest() throws SQLException {
		//GIVEN
		
		//WHEN
		when(connection.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT)).thenReturn(myPrepareStatement);
		when(myPrepareStatement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true);
		when(resultSet.getInt(1)).thenReturn(3);
		
		//THEN
		assertEquals(parkingSpotDAO.getNextAvailableSpot(ParkingType.CAR), 3);
		
	}

	@Test
	public void updateParkingTest() throws SQLException {
		// GIVEN
		ParkingSpot parkingSpot = new ParkingSpot(2, ParkingType.CAR, true);

		// WHEN
		when(connection.prepareStatement(DBConstants.UPDATE_PARKING_SPOT)).thenReturn(myPrepareStatement);
		when(myPrepareStatement.executeUpdate()).thenReturn(1);

		// THEN
		assertEquals(parkingSpotDAO.updateParking(parkingSpot), true);

	}
}
