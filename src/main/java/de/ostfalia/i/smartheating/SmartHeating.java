package de.ostfalia.i.smartheating;
import de.ostfalia.i.smartheating.graphs.GraphGenerator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Vector;

import org.charts.dataviewer.utils.TraceColour;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class SmartHeating {
    private String RoomName;
    private final Vector<Double> measurements = new Vector<>();
    private TraceColour traceColour = TraceColour.BLUE;

    public void setTraceColour(TraceColour traceColour) {
        this.traceColour = traceColour;
    }

    public TraceColour getTraceColour() {
        return traceColour;
    }


    public String getName() {
        return RoomName;
    }

    public void setName(String name) {
        RoomName = name;
    }

    public void addMeasurement(double measurement) {
        measurements.add(measurement);
    }

    public void setRoomName(String roomName) {
        RoomName = roomName;
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


    public static void drawLinePlot( TraceColour color,  SmartHeating... rooms ) {
 
        GraphGenerator.showLinePlot( rooms, x, y, headline);
    }

    public static JSONObject jsonObject = null;
    public static String x = "Hour";
    public static String y = "Value";
    public static String headline = "Smart Heating";
    public static void main(String[] args) throws FileNotFoundException{

        Object obj = null;

        try (FileReader fileReader = new FileReader("src/main/data.json")){
            obj = new JSONParser().parse(fileReader);
            jsonObject= (JSONObject) obj;

        } catch (Exception e) {
            System.out.println(e);
            System.err.println("Error while reading file");
        }



        SmartHeating s = new SmartHeating();
        SmartHeating s2 = new SmartHeating();
        s.setTraceColour(TraceColour.RED);
        s.setRoomName("Badezimmer");
        
        
        s.addMeasurement(1234);
        s.addMeasurement(1235);
        s.addMeasurement(1237);
        s.addMeasurement(1240); //hi 
        s.addMeasurement(1241);
        //hello
        
        s2.addMeasurement(1232);
        s2.addMeasurement(1237);

    
        drawLinePlot( TraceColour.BLUE, s2, s);

        Scanner keyboard = new Scanner(System.in);
        keyboard.nextLine();
        drawLinePlot(TraceColour.BLACK, s);
        
        // this makes the plot available on http://localhost:8090/view/heating
    }

}
