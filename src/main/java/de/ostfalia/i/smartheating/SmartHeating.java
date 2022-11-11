package de.ostfalia.i.smartheating;
import de.ostfalia.i.smartheating.graphs.GraphGenerator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.Year;
import java.util.Scanner;
import java.util.Vector;

import org.charts.dataviewer.utils.TraceColour;

import javax.swing.JFrame;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

class GraphConfig {
    public String x = "Hour";
    public String y = "Value";
    public String headline = "Smart Heating";
    public Object[] xArray = null;
}



public class SmartHeating extends JFrame {
    private String RoomName;
    private final Vector<Double> measurements = new Vector<>();
    private TraceColour traceColour = TraceColour.BLUE;
    public SmartHeating(){
        this.setSize(500, 500);
        this.setVisible(true);
    }
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

        GraphGenerator.showLinePlot( rooms, config.x, config.y, config.headline, config.xArray);

    }

    public static void drawLinePlotByArray(GraphConfig config,  SmartHeating[] rooms ) {

        GraphGenerator.showLinePlot( rooms, config.x, config.y, config.headline, config.xArray);

    }

    public static JSONObject jsonObject = null;
    public static void main(String[] args) throws FileNotFoundException{
        init();
        
        GraphConfig graphConfig = new GraphConfig();
        graphConfig.x = "Hour";
        graphConfig.y = "Value";
        graphConfig.headline = "Smart Heating";

        int year = 2018;
        int month = 1;
        int day = 1;

        SmartHeating[] response = drawDay(year, month,1, "Badezimmer", false, TraceColour.GREEN);
        drawLinePlot(graphConfig, response [0]);
        System.out.flush();
        // this makes the plot available on http://localhost:8090/view/heating
    }


    public static SmartHeating[] drawDay(int year, int month, int day, String room, boolean drawAbsolute, TraceColour color) throws FileNotFoundException{

        GraphConfig graphConfig = new GraphConfig();
        graphConfig.x = "Hour";
        graphConfig.y = "Value";
        graphConfig.headline = "Smart Heating";

        SmartHeating usage = new SmartHeating();
        usage.setName(room);
        usage.setTraceColour(color);
        SmartHeating absoHeating = new SmartHeating();
        absoHeating.setName(room);
        absoHeating.setTraceColour(color);
        int[] data = utils.getDayData(year, month, day, room);
        for (int i = 0; i < data.length; i++) {
            absoHeating.addMeasurement(data[i]);
        }

        
        int previoussum = data[0];
        for(int i = 0; i < data.length; i++){
            usage.addMeasurement(data[i] - previoussum);
            previoussum = data[i];
        }
        return new SmartHeating[] {usage, absoHeating};

        
    }

    public static void drawMonth(int year, int month, String room, boolean drawAbsolute, TraceColour color) throws FileNotFoundException{

        GraphConfig graphConfig = new GraphConfig();
        graphConfig.x = "Day";
        graphConfig.y = "Value";
        graphConfig.headline = "Smart Heating for " + month + "/" + year;

        SmartHeating usage = new SmartHeating();
        SmartHeating absoHeating = new SmartHeating();
        usage.setName(room);
        absoHeating.setName(room);
        usage.setTraceColour(color);
        absoHeating.setTraceColour(color);
        int[] data = utils.getAvailableDays(year, month, room);
        float previoussum = 0;
        int[] firstDay = utils.getDayData(year, month, data[0], room);
        //get average of first day
        int previousday = firstDay[0];
        float dayAvg = 0;
        for(int i = 0; i < firstDay.length; i++){
            dayAvg += firstDay[i] - previousday;
            previousday = firstDay[i];
        }
        dayAvg /= firstDay.length;
        previoussum = dayAvg;
        for (int day = 0; day < data.length; day++){
            int[] dayData = utils.getDayData(year, month, day + 1, room);


            previousday = dayData[0];
            dayAvg = 0;
            for(int i = 0; i < dayData.length; i++){
                dayAvg += dayData[i] - previousday;
                previousday = dayData[i];
            }

            absoHeating.addMeasurement(dayAvg);
            dayAvg /= dayData.length;


            usage.addMeasurement(previoussum - dayAvg);

            previoussum = dayAvg;
 
        }
        if (drawAbsolute)
            drawLinePlot(graphConfig, usage, absoHeating);
        else
            drawLinePlot(graphConfig, usage);


        
    }
// this makes the plot available on http://localhost:8090/view/heating


}
