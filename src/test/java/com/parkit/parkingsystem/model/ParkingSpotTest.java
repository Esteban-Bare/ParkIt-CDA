package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParkingSpotTest {

    @Test
    void setId() {
        ParkingSpot spot = new ParkingSpot(1,ParkingType.BIKE,false);
        spot.setId(5);
        assertEquals(5,spot.getId());
    }

    @Test
    void setParkingType() {
        ParkingSpot spot = new ParkingSpot(1,ParkingType.CAR,false);
        spot.setParkingType(ParkingType.BIKE);
        assertEquals(ParkingType.BIKE,spot.getParkingType());
    }

    @Test
    void testEquals() {
        ParkingSpot spot = new ParkingSpot(1, ParkingType.CAR,false);
        assertEquals(spot, spot);
    }

    @Test
    void testEqualsObjects() {
        ParkingSpot spot1 = new ParkingSpot(1,ParkingType.BIKE,false);
        ParkingSpot spot2 = new ParkingSpot(1,ParkingType.BIKE,false);
        assertEquals(spot1, spot2);
    }

    @Test
    void testNotEqualObjects() {
        ParkingSpot spot1 = new ParkingSpot(1,ParkingType.BIKE,false);
        ParkingSpot spot2 = new ParkingSpot(2,ParkingType.CAR,false);
        assertNotEquals(spot1, spot2);
    }

    @Test
    void testObjectNull() {
        ParkingSpot spot = new ParkingSpot(1,ParkingType.BIKE,false);
        assertNotEquals(null, spot);
    }

    @Test
    void testDifferentClass() {
        ParkingSpot spot = new ParkingSpot(1,ParkingType.BIKE,false);
        Object i = new Object();
        assertNotEquals(spot, i);
    }

    @Test
    void testHashCode() {
        ParkingSpot spot = new ParkingSpot(1,ParkingType.BIKE,false);
        assertEquals(1,spot.hashCode());
    }
}