package com.parkit.parkingsystem.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InputReaderUtilTest {

    @InjectMocks
    private InputReaderUtil inputReaderUtil;


    @Test
    void readSelectionValid() {
        String input = "1\n";

        InputStream inputStream = new ByteArrayInputStream(input.getBytes());

        System.setIn(inputStream);
        Scanner scanner = new Scanner(System.in);
        inputReaderUtil = new InputReaderUtil(scanner);

        int select = inputReaderUtil.readSelection();

        assertEquals(1,select);
    }

    @Test
    void readSelectionInvalid(){
        String input = "invalid\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        Scanner scanner = new Scanner(System.in);
        inputReaderUtil = new InputReaderUtil(scanner);

        int selection = inputReaderUtil.readSelection();

        assertEquals(-1,selection);
    }

    @Test
    void readVehicleRegistrationNumber() throws Exception {
        String regInput = "ABC123";
        InputStream in = new ByteArrayInputStream(regInput.getBytes());

        System.setIn(in);

        Scanner scanner = new Scanner(System.in);
        inputReaderUtil = new InputReaderUtil(scanner);

        String regNumber = inputReaderUtil.readVehicleRegistrationNumber();

        assertEquals("ABC123",regNumber);
    }

    @Test
    void readVehicleRegistrationNumber_emptyInput() {
        String fakeInput = "\n";
        InputStream in = new ByteArrayInputStream(fakeInput.getBytes());
        System.setIn(in);

        Scanner scanner = new Scanner(System.in);
        inputReaderUtil = new InputReaderUtil(scanner);

        assertThrows(IllegalArgumentException.class, () -> inputReaderUtil.readVehicleRegistrationNumber());
    }
}