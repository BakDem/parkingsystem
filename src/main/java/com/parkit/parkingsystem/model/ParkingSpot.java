package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

/**
 * @author cisse
 *
 *         ParkingSpot is the parking spot DAO model
 */
public class ParkingSpot {
	private int number;
	private ParkingType parkingType;
	private boolean isAvailable;

	/**
	 * @param number
	 * @param parkingType
	 * @param isAvailable
	 */
	public ParkingSpot(int number, ParkingType parkingType, boolean isAvailable) {
		this.number = number;
		this.parkingType = parkingType;
		this.isAvailable = isAvailable;
	}

	/**
	 * @return the number
	 */
	public int getId() {
		return number;
	}

	/**
	 * @param number : the number to set
	 */
	public void setId(int number) {
		this.number = number;
	}

	/**
	 * @return the parkingType
	 */
	public ParkingType getParkingType() {
		return parkingType;
	}

	/**
	 * @param parkingType : the parkingType to set
	 */
	public void setParkingType(ParkingType parkingType) {
		this.parkingType = parkingType;
	}

	/**
	 * @return à boolean : true or false
	 */
	public boolean isAvailable() {
		return isAvailable;
	}

	/**
	 * @param available : set the boolean value
	 */
	public void setAvailable(boolean available) {
		isAvailable = available;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ParkingSpot that = (ParkingSpot) o;
		return number == that.number;
	}

	@Override
	public int hashCode() {
		return number;
	}
}
