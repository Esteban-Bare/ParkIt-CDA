package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ParkingSpotDAOTest {

    @InjectMocks
    private ParkingSpotDAO parkingSpotDAO;

    @Mock
    private DataBaseConfig dataBaseConfig;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatementCar;

    @Mock
    private PreparedStatement preparedStatementUpdate;

    @Mock
    private ResultSet resultSet;

    private ParkingSpot testParkingSpot;
    private final String  bikeType = "BIKE";
    private final int carParking = 1;

    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {
        MockitoAnnotations.initMocks(this);

        testParkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        when(dataBaseConfig.getConnection()).thenReturn(connection);

        when(connection.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT)).thenReturn(preparedStatementCar);
        when(preparedStatementCar.executeQuery()).thenReturn(resultSet);

        when(connection.prepareStatement(DBConstants.UPDATE_PARKING_SPOT)).thenReturn(preparedStatementUpdate);
        when(preparedStatementUpdate.executeUpdate()).thenReturn(1);



        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(testParkingSpot.getId());
    }

    @Test
    void getNextAvailableSlotCar() throws SQLException {
        int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

        assertEquals(result,testParkingSpot.getId());

        verify(preparedStatementCar).setString(1, String.valueOf(ParkingType.CAR));
        verify(preparedStatementCar).executeQuery();
        verify(dataBaseConfig).closeResultSet(resultSet);
        verify(dataBaseConfig).closePreparedStatement(preparedStatementCar);
        verify(dataBaseConfig).closeConnection(connection);
    }

    @Test
    void updateParking() throws SQLException {
        boolean resultUpdate = parkingSpotDAO.updateParking(testParkingSpot);

        assertTrue("update done", resultUpdate);

        verify(preparedStatementUpdate).setBoolean(1,testParkingSpot.isAvailable());
        verify(preparedStatementUpdate).setInt(2,testParkingSpot.getId());
        verify(preparedStatementUpdate).executeUpdate();
        verify(dataBaseConfig).closeConnection(connection);
    }
}