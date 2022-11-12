package de.ostfalia.i.smartheating;
import de.ostfalia.i.smartheating.graphs.GraphGenerator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.Year;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;
import java.util.Vector;

import org.charts.dataviewer.utils.TraceColour;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
    private JComboBox comboBox;
    private JFrame frame;
    

    public SmartHeating() {
        initialize();
    }

    /**
     * initialize the app by reading the data from the file
     */
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


    /**
     * set the color of a specific measurement to a specific color
     * @param traceColour the color to set
     */
    public void setTraceColour(TraceColour traceColour) {
        this.traceColour = traceColour;
    }


    /**
     * @return the color of the measurement
     */
    public TraceColour getTraceColour() {
        return traceColour;
    }

    /**
     * @return the name of the room
     */
    public String getName() {
        return RoomName;
    }

    /**
     * @param name the name of the room
     */
    public void setName(String name) {
        RoomName = name;
    }

    /**
     * @param measurement the measurement to add
     */
    public void addMeasurement(double measurement) {
        measurements.add(measurement);
    }


    /**
     * @return set the name of the room
     */
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





    public static SmartHeating[] getYearData(int year, String room){
        SmartHeating response = new SmartHeating();
        
        int[] availableMonths = utils.getAvailableMonths(year, room);
        Arrays.sort(availableMonths);
        int count = 1;
        for (int i = 1; i < 13; i++) {
            try {
                SmartHeating month = getMonthMeasurement(year, i, room, false, TraceColour.BLACK)[0];
                response.addMeasurement(month.getAverage());
                count++;
            } catch (Exception e) {
                response.addMeasurement(0);
                count++;
            }
        }


        return new SmartHeating[]{response};
    }









    public static SmartHeating[] getDayMeasurememt(int year, int month, int day, String room, boolean drawAbsolute, TraceColour color) throws FileNotFoundException{

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

    public static SmartHeating[] getMonthMeasurement(int year, int month, String room, boolean drawAbsolute, TraceColour color) throws FileNotFoundException{

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


        int[] data = utils.getAvailableDays(year, (long)month, room);
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


            usage.addMeasurement(dayAvg);

            previoussum = dayAvg;
 
        }

        return new SmartHeating[] {usage, absoHeating};
        
    }

    /**
     * initialize the window
     */
    private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JToolBar toolBar = new JToolBar();
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		comboBox = new JComboBox();
		comboBox.setSelectedIndex(-1);
		frame.getContentPane().add(comboBox, BorderLayout.WEST);
        comboBox.addItem("Room 1");
        comboBox.addItem("Room 2");
	}

    public static SmartHeating[] getWeekData(int year, int month, int day, String room) {
        float[] data = new float[7];
        if(day > 7){
            for(int i = 0; i < 7; i++){
                float[] dayData = getDailyUsage(year, month, day - 7 + i, room);
                Float counter = 0.0f;
                for(Float j: dayData){
                    counter += j;
                }

                data[i] = counter/dayData.length;
            }
        }

        if(day <= 7){
            int c = 0;
            try {
                int[] availableDaysPrevoiusMonth = null;
                try {
                    
                    availableDaysPrevoiusMonth = utils.getAvailableDays(year, month - 1, room);
                } catch (Exception e) {
                    availableDaysPrevoiusMonth = utils.getAvailableDays(year - 1, 12, room);
                }
                
                for(int i =  availableDaysPrevoiusMonth.length - 7 + day; i < availableDaysPrevoiusMonth.length; i++){
                    float[] dayData = getDailyUsage(year, month-1, availableDaysPrevoiusMonth[availableDaysPrevoiusMonth.length - (i-day)], room);
    
                    Float counter = 0.0f;
                    for(Float j: dayData){
                        counter += j;
                    }
                    data[c] = counter/dayData.length;
                    c++;
    
                }
            } catch (Exception e) {
                c = 7 - day;
            }

            for(int i = 0; i < day; i++){
                float[] dayData = getDailyUsage(year, month, day-i, room);
                Float counter = 0.0f;
                for(Float j: dayData){
                    counter += j;
                }

                data[c] = counter/dayData.length;
                c++;
            }
        }

        SmartHeating usage = new SmartHeating();
        usage.setName(room);
        for (float f : data) {
            usage.addMeasurement(f);
        }


        return new SmartHeating[] {usage};

    }

    public static float[] getDailyUsage(int year, int month, int day, String room) {
        int[] data = utils.getDayData(year, month, day, room);
        float[] usage = new float[data.length];
        float previoussum = data[0];
        for(int i = 0; i < data.length; i++){
            usage[i] = data[i] - previoussum;
            previoussum = data[i];
        }
        return usage;
    }

    public static void main(String[] args) throws FileNotFoundException{
        init();

    
        GraphConfig graphConfig = new GraphConfig();
        graphConfig.x = "Hour";
        graphConfig.y = "Value";
        graphConfig.headline = "Smart Heating";

        int year = 2022;
        int month = 1;
        
        SmartHeating[] response = getDayMeasurememt(year, month,1, "Badezimmer", false, TraceColour.GREEN);
        
        SmartHeating test = new SmartHeating();

        SmartHeating yearData =  getYearData(2022, "Badezimmer")[0];
        SmartHeating monthData = getMonthMeasurement(2022, 7, "Badezimmer", false, TraceColour.GREEN)[0];
        //SmartHeating weekData = getWeekData(2022, 7, 8, "Badezimmer")[0];

        
        //drawLinePlot(graphConfig, yearData, monthData);
        
        
        //utils.push();
        //utils.pull();
        
        // this makes the plot available on http://localhost:8090/view/heating
    }


}
