package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.model.Ticket;

import java.sql.*;


public class VisitorService {
    public DataBaseConfig dataBaseConfig;

    public VisitorService(DataBaseConfig dataBaseConfig) {
        this.dataBaseConfig = dataBaseConfig;
    }
    public boolean recurrenVisitor(Ticket ticket) {
        Connection con = null;
        int vcount = 0;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.RECURRENT_VISITOR);
            ps.setString(1,ticket.getVehicleRegNumber());

            ResultSet resultSet = ps.executeQuery();
            dataBaseConfig.closeConnection(con);
            while (resultSet.next()) {
                vcount++;
                if (vcount > 5) {
                    return true;
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return false;
    }
}
