package de.ostfalia.i.smartheating.test;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import de.ostfalia.i.smartheating.SmartHeating;

public class TestSmartHeating {

    @Test
    public void testMean() {
        SmartHeating s = new SmartHeating();
        s.addMeasurement(19);
        s.addMeasurement(20);
        s.addMeasurement(21);
        assertEquals(20, s.getAverage(), 0.01);
    }

}