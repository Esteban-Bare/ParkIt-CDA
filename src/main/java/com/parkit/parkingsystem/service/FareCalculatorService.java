package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FareCalculatorService {
    private final VisitorService visitorService;
    private final DiscountService discountService = new DiscountService();

    public FareCalculatorService(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        int inHour = (int) ticket.getInTime().getTime();
        int outHour = (int) ticket.getOutTime().getTime();

        //TODO: Some tests are failing here. Need to check if this logic is correct
        double duration = ((outHour - inHour) / 3600000.0);

        System.out.println(duration);
        if (duration <= 0.50) { //0.50 represents half an hour, verifying if the duration of the ticket is less than an hour
            ticket.setPrice(0);
        } else {
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    double price = (duration * Fare.CAR_RATE_PER_HOUR);
                    System.out.println(price);
                    double priceDiscount = discountService.applyDiscount(price, this.visitorService.recurrenVisitor(ticket));
                    System.out.println(priceDiscount);
                    ticket.setPrice(priceDiscount);
                    break;
                }
                case BIKE: {
                    double price = (duration * Fare.BIKE_RATE_PER_HOUR);
                    System.out.println(price);
                    double priceDiscount = discountService.applyDiscount(price, this.visitorService.recurrenVisitor(ticket));
                    System.out.println(priceDiscount);
                    ticket.setPrice(priceDiscount);
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unkown Parking Type");
            }
        }
    }


}