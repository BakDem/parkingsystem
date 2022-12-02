package com.parkit.parkingsystem.service;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }
        
        Date inDate = ticket.getInTime();
        Date outDate = ticket.getOutTime();
        
       long difference_In_Time = outDate.getTime() - inDate.getTime();System.out.println("difference in time : "+difference_In_Time);	
       long vehicleInHourTime = TimeUnit.MILLISECONDS.toHours(difference_In_Time);System.out.println("in hour : "+vehicleInHourTime);
       long vehicleInMinutesTime = TimeUnit.MILLISECONDS.toMinutes(difference_In_Time);System.out.println("in minute : "+vehicleInMinutesTime);
      
        //TODO: Some tests are failing here. Need to check if this logic is correct
     //   int duration = outHour - inHour;
        
      //  System.out.println("la durée"+duration);
       if (vehicleInHourTime == 0 && vehicleInMinutesTime == 45)
       {
       	switch (ticket.getParkingSpot().getParkingType()){
           case CAR: {
               ticket.setPrice(0.75 * Fare.CAR_RATE_PER_HOUR);
               break;
           }
           case BIKE: {
               ticket.setPrice(0.75 * Fare.BIKE_RATE_PER_HOUR);
               break;
           }
           default: throw new IllegalArgumentException("Unkown Parking Type");
       }
       }
       else if (vehicleInHourTime == 0 && vehicleInMinutesTime <= 30)
        {
        	switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(0 * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(0 * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
        }else {
        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(vehicleInHourTime * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(vehicleInHourTime * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }}
    }
}