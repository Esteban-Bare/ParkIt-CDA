package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TicketDAOTest {

    @InjectMocks
    private TicketDAO ticketDAO;

    @Mock
    private DataBaseConfig dataBaseConfig;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private PreparedStatement preparedStatementGet;

    @Mock
    private PreparedStatement preparedStatementUpdate;

    @Mock
    private ResultSet resultSet;

    private Ticket testTicket;
    private final String vehicleRegNumber = "ABC123";

    @BeforeEach
    public void setUp() throws SQLException, ClassNotFoundException {
        MockitoAnnotations.initMocks(this);

        testTicket = new Ticket();
        testTicket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR,false));
        testTicket.setId(1);
        testTicket.setVehicleRegNumber(vehicleRegNumber);
        testTicket.setPrice(10.0);
        testTicket.setInTime(new java.util.Date());
        testTicket.setOutTime(new java.util.Date());

        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(DBConstants.SAVE_TICKET)).thenReturn(preparedStatement);
        when(preparedStatement.execute()).thenReturn(true);

        when(connection.prepareStatement(DBConstants.GET_TICKET)).thenReturn(preparedStatementGet);
        when(preparedStatementGet.executeQuery()).thenReturn(resultSet);

        when(connection.prepareStatement(DBConstants.UPDATE_TICKET)).thenReturn(preparedStatementUpdate);
        when(preparedStatementUpdate.execute()).thenReturn(true);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(testTicket.getParkingSpot().getId());
        when(resultSet.getInt(2)).thenReturn(testTicket.getId());
        when(resultSet.getString(6)).thenReturn(testTicket.getParkingSpot().getParkingType().toString());
        when(resultSet.getDouble(3)).thenReturn(testTicket.getPrice());
        when(resultSet.getTimestamp(4)).thenReturn(new Timestamp(testTicket.getInTime().getTime()));
        when(resultSet.getTimestamp(5)).thenReturn(new Timestamp(testTicket.getOutTime().getTime()));
    }

    @Test
    void saveTicket() throws SQLException {
        boolean result = ticketDAO.saveTicket(testTicket);

        assertTrue(result, "Should be true");

        verify(preparedStatement).setInt(1,testTicket.getParkingSpot().getId());
        verify(preparedStatement).setString(2,testTicket.getVehicleRegNumber());
        verify(preparedStatement).setDouble(3,testTicket.getPrice());
        verify(preparedStatement).setTimestamp(4,new Timestamp(testTicket.getInTime().getTime()));
        verify(preparedStatement).setTimestamp(5, new Timestamp(testTicket.getOutTime().getTime()));
        verify(preparedStatement).execute();
        verify(dataBaseConfig).closeConnection(connection);
    }

    @Test
    void getTicket() throws SQLException {
        Ticket realTicket = ticketDAO.getTicket(vehicleRegNumber);

        assertNotNull(realTicket);
        assertEquals(testTicket.getId(),realTicket.getId());
        assertEquals(testTicket.getParkingSpot().getId(),realTicket.getParkingSpot().getId());
        assertEquals(testTicket.getParkingSpot().getParkingType(),realTicket.getParkingSpot().getParkingType());
        assertEquals(testTicket.getPrice(),realTicket.getPrice());
        assertEquals(testTicket.getOutTime(),realTicket.getOutTime());
        assertEquals(testTicket.getInTime(),realTicket.getInTime());

        verify(preparedStatementGet).setString(1,vehicleRegNumber);
        verify(preparedStatementGet).executeQuery();
        verify(dataBaseConfig).closeResultSet(resultSet);
        verify(dataBaseConfig).closePreparedStatement(preparedStatementGet);
        verify(dataBaseConfig).closeConnection(connection);
    }

    @Test
    void updateTicket() throws SQLException {
        boolean resultUpdate = ticketDAO.updateTicket(testTicket);

        assertTrue(resultUpdate, "Update done c: !");

        verify(preparedStatementUpdate).setDouble(1,testTicket.getPrice());
        verify(preparedStatementUpdate).setTimestamp(2, new Timestamp(testTicket.getOutTime().getTime()));
        verify(preparedStatementUpdate).setInt(3,testTicket.getId());
        verify(preparedStatementUpdate).execute();
        verify(dataBaseConfig).closeConnection(connection);
    }
}