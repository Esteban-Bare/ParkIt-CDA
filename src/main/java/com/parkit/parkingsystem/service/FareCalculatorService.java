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
    public DataBaseConfig dataBaseConfig = new DataBaseConfig();
    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        int inHour = (int) ticket.getInTime().getTime();
        int outHour = (int) ticket.getOutTime().getTime();

        //TODO: Some tests are failing here. Need to check if this logic is correct
        double duration = ((outHour - inHour) / 3600000.0);

        if (duration < 0.30) {
            ticket.setPrice(0);
        } else {
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    if (recurrenVisitor(ticket)) {
                        ticket.setPrice((duration * Fare.CAR_RATE_PER_HOUR) * Fare.REDUCTION_RECURRENT_VISITOR);
                    } else {
                        ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                    }
                    break;
                }
                case BIKE: {
                    if (recurrenVisitor(ticket)) {
                        ticket.setPrice((duration * Fare.BIKE_RATE_PER_HOUR) * Fare.REDUCTION_RECURRENT_VISITOR);
                    } else {
                        ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                    }
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unkown Parking Type");
            }
        }
    }

    public boolean recurrenVisitor(Ticket ticket) {
        Connection con = null;
        int vcount = 0;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.RECURRENT_VISITOR);
            ps.setString(1,ticket.getVehicleRegNumber());

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                vcount++;
                if (vcount > 5) {
                    return true;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            dataBaseConfig.closeConnection(con);
        }

        return false;
    }
}