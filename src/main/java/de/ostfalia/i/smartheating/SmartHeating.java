package de.ostfalia.i.smartheating;
import de.ostfalia.i.smartheating.graphs.GraphGenerator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Vector;

import org.charts.dataviewer.utils.TraceColour;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

class GraphConfig {
    public String x = "Hour";
    public String y = "Value";
    public String headline = "Smart Heating";
    public Object[] xArray = null;
}

public class SmartHeating {
    private String RoomName;
    private final Vector<Double> measurements = new Vector<>();
    private TraceColour traceColour = TraceColour.BLUE;

    private static void init(){
        Object obj = null;
        try (FileReader fileReader = new FileReader("src/main/data.json")){
            obj = new JSONParser().parse(fileReader);
            jsonObject= (JSONObject) obj;

        } catch (Exception e) {
            System.out.println(e);
            System.err.println("Error while reading file");
        }
    }

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

    /**
     * @return The measurements as a double array
     */

    public double getAverage() {
        double measurementsSum = 0;
        for (double m : measurements) {
            measurementsSum += m;
        }
        return measurementsSum / measurements.size();
    }

    /**
     * @param config The configuration for the graph to be generated based on the config object
     * @param rooms An array of rooms to be displayed in the graph based on the SmartHeating class
     * @return The path to the generated graph
     */

    public static void drawLinePlot(GraphConfig config,  SmartHeating... rooms ) {
        Object[] roomsArray = new Object[rooms.length];

        GraphGenerator.showLinePlot( rooms, config.x, config.y, config.headline, config.xArray);

    }

    public static JSONObject jsonObject = null;
    public static void main(String[] args) throws FileNotFoundException{
        init();
        
        GraphConfig graphConfig = new GraphConfig();
        graphConfig.x = "Hour";
        graphConfig.y = "Value";
        graphConfig.headline = "Smart Heating";

        SmartHeating s = new SmartHeating();
        SmartHeating s2 = new SmartHeating();

        s.setTraceColour(TraceColour.RED);
        s.setRoomName("Badezimmer");
        
        s2.setTraceColour(TraceColour.GREEN);
        for(int i: utils.getDayData(2018, 1, 1, "Badezimmer")){
           s.addMeasurement(i);
        }
        utils.addDay(2018, 1, 2, "Badezimmer");
        drawLinePlot( graphConfig, s2, s);
        Scanner keyboard = new Scanner(System.in);
        keyboard.nextLine();
        drawLinePlot(graphConfig, s);
        keyboard.close();
        
        // this makes the plot available on http://localhost:8090/view/heating
    }

}
