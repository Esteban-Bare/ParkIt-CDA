package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.model.Ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VisitorService {
    private final DataBaseConfig dataBaseConfig = new DataBaseConfig();

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
