package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    /*
    @BeforeEach
    private void setUpPerTest() {
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
        }
    */

    @Test
    public void processExitingVehicleTest(){

        //Arrange
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
        when(ticketDAO.getNbTicket(any(Ticket.class))).thenReturn(4);

        //Act
        parkingService.processExitingVehicle();

        //Assert
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    public void processExitingVehicleTestUnableUpdate(){

        //Arrange
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);

            //when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
        when(ticketDAO.getNbTicket(any(Ticket.class))).thenReturn(1);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        //Act
        parkingService.processExitingVehicle();

        //Assert
        verify(parkingSpotDAO, Mockito.times(0)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, Mockito.times(1)).updateTicket(any(Ticket.class));
        assertTrue(outContent.toString().trim().contains("Unable to update ticket information. Error occurred"));
    }

    
    @Test
    public void testProcessIncomingVehicle() {
        //Arrange
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            //ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
            //Ticket ticket = new Ticket();
            //ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
            //ticket.setParkingSpot(parkingSpot);
            //ticket.setVehicleRegNumber("ABCDEF");
            //when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            //when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
        when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(2);
        when(inputReaderUtil.readSelection()).thenReturn(1);
  
        //Act
        parkingService.processIncomingVehicle();

        //Assert
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));

    }

    @Test
    public void testGetNextParkingNumberIfAvailable() {
        //Arrange
        ParkingSpot parkingSpot;
        try {
            //when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            parkingSpot = new ParkingSpot(4, ParkingType.CAR,false);
            //Ticket ticket = new Ticket();
            //ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
            //ticket.setParkingSpot(parkingSpot);
            //ticket.setVehicleRegNumber("ABCDEF");
            //when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            //when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            //when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        
  
        //Act
        parkingSpot = parkingService.getNextParkingNumberIfAvailable();

        //Assert
        assertEquals(1, parkingSpot.getId());
	
    }

    @Test
    public void testGetNextParkingNumberIfAvailableParkingNumberNotFound() {
        //Arrange
        ParkingSpot parkingSpot;
        try {
            //when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            parkingSpot = new ParkingSpot(4, ParkingType.CAR,false);
            //Ticket ticket = new Ticket();
            //ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
            //ticket.setParkingSpot(parkingSpot);
            //ticket.setVehicleRegNumber("ABCDEF");
            //when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            //when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            //when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(0);
  
        //Act
        parkingSpot = parkingService.getNextParkingNumberIfAvailable();

        //Assert
        verify(parkingSpotDAO, Mockito.times(1)).getNextAvailableSlot(any(ParkingType.class));
        assertEquals(null, parkingSpot);        
	
    }

    
    @Test
    public void testGetNextParkingNumberIfAvailableParkingNumberWrongArgument() {
        //Arrange
        ParkingSpot parkingSpot;
        try {
            //when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            parkingSpot = new ParkingSpot(4, ParkingType.CAR,false);
            //Ticket ticket = new Ticket();
            //ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
            //ticket.setParkingSpot(parkingSpot);
            //ticket.setVehicleRegNumber("ABCDEF");
            //when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            //when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            //when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
        when(inputReaderUtil.readSelection()).thenReturn(3);
  
        //Act
        parkingSpot = parkingService.getNextParkingNumberIfAvailable();

        //Assert
        verify(inputReaderUtil, Mockito.times(1)).readSelection();
        verify(parkingSpotDAO, Mockito.times(0)).getNextAvailableSlot(any(ParkingType.class));

	
    }
    
    

}