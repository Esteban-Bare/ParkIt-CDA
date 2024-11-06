package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {
    // Mock dependencies
    @Mock
    private InputReaderUtil inputReaderUtil;
    @Mock
    private ParkingSpotDAO parkingSpotDAO;
    @Mock
    private TicketDAO ticketDAO;

    private Ticket ticket;

    @InjectMocks
    private ParkingService parkingService;

    @BeforeEach
    public void setUpPerTest() {
        try {
            // Stubbing InputReaderUtil methods with lenient to avoid UnnecessaryStubbingException
            lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            lenient().when(inputReaderUtil.readSelection()).thenReturn(1); // Assuming 1 corresponds to CAR

            // Set up ParkingSpot and Ticket
            // Test objects
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000))); // 1 hour before now
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");

            // Stubbing DAOs with lenient where necessary
            lenient().when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            lenient().when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
            lenient().when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
            lenient().when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
    }

    @Test
    public void processIncomingVehicleTest() {
        // Call method under test
        parkingService.processIncomingVehicle();

        // Verify interactions with mocks
        verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, times(1)).saveTicket(any(Ticket.class));
    }

    @Test
    public void processExitingVehicleTest() {
        when(ticketDAO.getTicket("ABCDEF")).thenReturn(ticket);

        parkingService.processExitingVehicle();

        verify(ticketDAO, times(1)).getTicket("ABCDEF");
    }
}
