package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown(){

    }

    
    @Test
    public void testParkingACar(){
        //Arrange
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Ticket testTicket = new Ticket();
        testTicket.setVehicleRegNumber("ABCDEF");
        int nbTicketBeforeIncoming = ticketDAO.getNbTicket(testTicket);
        int nbTicketAfterIncoming;
        Ticket testAvailabilityTicket = new Ticket();

        //Act
        parkingService.processIncomingVehicle();
        nbTicketAfterIncoming = ticketDAO.getNbTicket(testTicket);
        testAvailabilityTicket = ticketDAO.getTicket("ABCDEF");

        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability       
        //Assert
                
        assertTrue(outContent.toString().trim().contains("Generated Ticket and saved in DB"));
        assertEquals(nbTicketBeforeIncoming + 1, nbTicketAfterIncoming);
        assertFalse(testAvailabilityTicket.getParkingSpot().isAvailable());
   
    }
    

    
    @Test
    public void testParkingLotExit(){
        //Arrange
        testParkingACar();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        //Act
        parkingService.processExitingVehicle();

        //Assert
        //TODO: check that the fare generated and out time are populated correctly in the database


    }
    
    

    /*
    @Test
    public void testParkingLotExitRecurringUser() {
        //Arrange
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        double priceFirstPassage;
        double priceSecondPassage;

        //Act
        parkingService.processIncomingVehicle();
        priceFirstPassage = parkingService.processExitingVehicle();
        parkingService.processIncomingVehicle();
        priceSecondPassage = parkingService.processExitingVehicle();

        //Assert
        assertTrue(priceSecondPassage == (0.95 * priceFirstPassage));
	
    }
    */
    

}
