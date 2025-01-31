package com.parkit.parkingsystem.integration.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.config.DataBaseConfig;

/**
 * @author cisse
 *
 *         This class allow connection with the Data Base
 */
public class DataBaseTestConfig extends DataBaseConfig {

	private static final Logger logger = LogManager.getLogger("DataBaseTestConfig");
	InputStream inputStream;

	/**
	 * this method request connection to the DBMS by providing information.
	 * 
	 * @throws IOException
	 */
	public Connection getConnection() throws ClassNotFoundException, SQLException, IOException {
		Properties properties = new Properties();
		String propFileName = "system.properties";
		inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		properties.load(inputStream);

		String url = properties.getProperty("url");
		String user = properties.getProperty("userName");
		String pass = properties.getProperty("password");

		return DriverManager.getConnection(url, user, pass);
	}

	public void closeConnection(Connection con) {
		if (con != null) {
			try {
				con.close();
				logger.info("Closing DB connection");
			} catch (SQLException e) {
				logger.error("Error while closing connection", e);
			}
		}
	}

	public void closePreparedStatement(PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
				logger.info("Closing Prepared Statement");
			} catch (SQLException e) {
				logger.error("Error while closing prepared statement", e);
			}
		}
	}

	public void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
				logger.info("Closing Result Set");
			} catch (SQLException e) {
				logger.error("Error while closing result set", e);
			}
		}
	}
}
