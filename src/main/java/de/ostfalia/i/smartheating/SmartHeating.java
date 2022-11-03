package de.ostfalia.i.smartheating;
import de.ostfalia.i.smartheating.graphs.GraphGenerator;

import java.util.Vector;

public class SmartHeating {
    private final Vector<Double> measurements = new Vector<>();

    public void addMeasurement(double measurement) {
        measurements.add(measurement);
    }

    public Vector<Double> getMeasurements(){
        return measurements;
    }

    public double getAverage() {
        double measurementsSum = 0;
        for (double m : measurements) {
            measurementsSum += m;
        }
        return measurementsSum / measurements.size();
    }

    public static void main(String[] args){
        SmartHeating s = new SmartHeating();
        s.addMeasurement(1234);
        s.addMeasurement(1235);
        s.addMeasurement(1237);
        s.addMeasurement(1240);
        s.addMeasurement(1241);

        // this makes the plot available on http://localhost:8090/view/heating
        GraphGenerator.showLinePlot(s.getMeasurements());
    }

}
