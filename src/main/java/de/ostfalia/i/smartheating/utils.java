package de.ostfalia.i.smartheating;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.*;


public class utils {
    /* Here is the explanation for the code above:
    1. The method getDayData takes 4 arguments: year, month, day and room name.
    2. After that it creates a new object named response.
    3. It creates a new array named data.
    4. It gets the "data" object from the json file.
    5. It iterates through the "data" array.
    6. It gets the "name" object from the "data" array and checks if it's equal to the room name.
    7. If it's true it gets the "measurements" object from the "data" array.
    8. It gets the "years" array from the "measurements" object.
    9. It iterates through the "years" array.
    10. It gets the "year" object from the "years" array and checks if it's equal to the year.
    11. If it's true it gets the "month" array from the "years" object.
    12. It iterates through the "months" array.
    13. It gets the "month" object from the "months" array and checks if it's equal to the month.
    14. If it's true it gets the "days" array from the "months" object.
    15. It iterates through the "days" array.
    16. It gets the "day" object from the "days" array and checks if it's equal to the day.
    17. If it's true it gets the "hourlyUsage" object from the "days" array.
    18. It gets the "hourlyUsage" array from the "hourlyUsage" object.
    19. It creates a new array named data.
    20. It iterates through the "hourlyUsage" array.
    21. It gets the "hourlyUsage" object from the "hourlyUsage" array and casts it to int.
    22. It returns the data array. 
    */ 
    /**
     * 
     * @param yearValue the year to find the data for
     * @param monthValue the month to find the data for
     * @param dayValue the day to find the data for
     * @param roomName the room to find the data for
     * @return the hourly measurement data for the given parameters
     */
    public static int[] getDayData(long yearValue, long monthValue, long dayValue,  String roomName){
        Object response = null;
        int[] data = null;
        JSONArray rooms = (JSONArray) SmartHeating.jsonObject.get("data");
        for (Object room : rooms) {
            JSONObject roomObject = (JSONObject) room;
            if (roomObject.get("name").equals(roomName)){
                JSONObject measurements = (JSONObject) roomObject.get("measurements");
                JSONArray years = (JSONArray) measurements.get("years");
                for (Object year : years) {
                    JSONObject yearObject = (JSONObject) year;
                    if(yearObject.get("year").equals( (Object)yearValue)){
                        JSONArray months = (JSONArray) yearObject.get("months");
                        for (Object month : months) {
                            JSONObject monthObject = (JSONObject) month;
                            if (monthObject.get("month").equals( (Object)monthValue)) {
                                JSONArray days = (JSONArray) monthObject.get("days");
                                for (Object day : days) {
                                    JSONObject dayObject = (JSONObject) day;
                                    if (dayObject.get("day").equals( (Object)dayValue)) {
                                        response = dayObject.get("hourlyUsage");
                                        JSONArray hourly = (JSONArray) response;
                                        data = new int[hourly.size()];
                                        for (int i = 0; i < hourly.size(); i++) {
                                            data[i] = (int) (long) hourly.get(i);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return data;
    }

    /**
     * 
     * @param yearValue the year to find the data for
     * @param monthValue the month to find the data for
     * @param roomName the room to find the data for
     * @return the available days for the given parameters
     */

    public static int[] getAvailableDays(long yearValue, long monthValue, String roomName){
        int[] data = null;
        JSONArray rooms = (JSONArray) SmartHeating.jsonObject.get("data");
        for (Object room : rooms) {
            JSONObject roomObject = (JSONObject) room;
            if (roomObject.get("name").equals(roomName)){
                JSONObject measurements = (JSONObject) roomObject.get("measurements");
                JSONArray years = (JSONArray) measurements.get("years");
                for (Object year : years) {
                    JSONObject yearObject = (JSONObject) year;
                    if(yearObject.get("year").equals( (Object)yearValue)){
                        JSONArray months = (JSONArray) yearObject.get("months");

                        for (Object month : months) {
                            JSONObject monthObject = (JSONObject) month;
                            if ((long)monthObject.get("month") == monthValue) {
                               
                                JSONArray days = (JSONArray) monthObject.get("days");
                                data = new int[days.size()];
                                int i = 0;
                                for (Object day : days) {
                                    JSONObject dayObject = (JSONObject) day;
                                    data[i] = (int) (long) dayObject.get("day");
                                    i++;
                                }
                            return data;
                            }
                            
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 
     * @param yearValue the year to find the data for
     * @param roomName the room to find the data for
     * @return the available months for the given parameters
     */

    public static int[] getAvailableMonths(long yearValue, String roomName){
        int[] data = null;
        JSONArray rooms = (JSONArray) SmartHeating.jsonObject.get("data");
        for (Object room : rooms) {
            JSONObject roomObject = (JSONObject) room;
            if (roomObject.get("name").equals(roomName)){
                JSONObject measurements = (JSONObject) roomObject.get("measurements");
                JSONArray years = (JSONArray) measurements.get("years");
                for (Object year : years) {
                    JSONObject yearObject = (JSONObject) year;
                    if(yearObject.get("year").equals( (Object)yearValue)){
                        JSONArray months = (JSONArray) yearObject.get("months");
                        data = new int[months.size()];
                        int i = 0;
                        for (Object month : months) {
                            JSONObject monthObject = (JSONObject) month;
                            data[i] = (int) (long) monthObject.get("month");
                            i++;
                        }
                        return data;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 
     * @param roomName the room to find the data for
     * @return the available years for the given parameters
     */	

    public static int[] getAvialableYears(String roomName){
        int[] data = null;
        JSONArray rooms = (JSONArray) SmartHeating.jsonObject.get("data");
        for (Object room : rooms) {
            JSONObject roomObject = (JSONObject) room;
            if (roomObject.get("name").equals(roomName)){
                JSONObject measurements = (JSONObject) roomObject.get("measurements");
                JSONArray years = (JSONArray) measurements.get("years");
                data = new int[years.size()];
                int i = 0;
                for (Object year : years) {
                    JSONObject yearObject = (JSONObject) year;
                    data[i] = (int) (long) yearObject.get("year");
                    i++;
                }
                return data;
            }
        }
        return null;
    }

    /**
     * 
     * @return the available rooms
     */


    public static String[] getAvailableRooms(){
        String[] data = null;
        JSONArray rooms = (JSONArray) SmartHeating.jsonObject.get("data");
        data = new String[rooms.size()];
        int i = 0;
        for (Object room : rooms) {
            JSONObject roomObject = (JSONObject) room;
            data[i] = (String) roomObject.get("name");
            i++;
        }
        return data;
    }

    
    /* The code above does the following:
    1. Creates a new gson object
    2. Adds a json array to the json object
    3. Adds a json object to the json array
    4. Adds a json array to the json object
    5. Adds a json object to the json array
    6. Writes the json object to a file 
    */

    private static void writeToFile(JSONObject jsonObject){

        try (FileWriter file = new FileWriter("src/main/data.json")) {
            Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
                file.write(gson.toJson(jsonObject));
 
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 
     * @param yearValue the year to add the data to
     * @param monthValue the month to add the data to
     * @param dayValue the day to add the data to
     * @param value the hourly usage to set
     * @param hour the hour to set the value to. If the hour is already set, the value will be overwritten. If the hour is -1, the value will be added to the end of the array
     * @param roomName the room to find the data to
     * @return weather the operation was successful and the data was added, fails if the day cannot be found or there are 24 hours of data already
     */
    public static boolean addMeasurementToDay(long yearValue, long monthValue, long dayValue, double value, long hour, String roomName){
        Object response = null;

        JSONObject jsonObject = SmartHeating.jsonObject;
        JSONArray rooms = (JSONArray) jsonObject.get("data");
        for (Object room : rooms) {
            JSONObject roomObject = (JSONObject) room;
            if (roomObject.get("name").equals(roomName)){
                JSONObject measurements = (JSONObject) roomObject.get("measurements");
                JSONArray years = (JSONArray) measurements.get("years");
                for (Object year : years) {
                    JSONObject yearObject = (JSONObject) year;
                    if(yearObject.get("year").equals( (Object)yearValue)){
                        JSONArray months = (JSONArray) yearObject.get("months");
                        for (Object month : months) {
                            JSONObject monthObject = (JSONObject) month;
                            if (monthObject.get("month").equals( (Object)monthValue)) {
                                JSONArray days = (JSONArray) monthObject.get("days");
                                for (Object day : days) {
                                    JSONObject dayObject = (JSONObject) day;
                                    if (dayObject.get("day").equals( (Object)dayValue)) {
                                        response = dayObject.get("hourlyUsage");
                                        JSONArray hourly = (JSONArray) response;
                                        if (hour >= 0){
                                            hourly.set((int)hour -1, (long)value);
                                            SmartHeating.jsonObject = jsonObject;
                                            writeToFile(jsonObject);
                                            return true;
                                        } else{
                                            for (int i = hourly.size()-1; i >= 0; i--) {
                                                if ((long)hourly.get(i) != 0){
                                                    try {
                                                        hourly.set(i , (long)value);
                                                        SmartHeating.jsonObject = jsonObject;
                                                        writeToFile(jsonObject);
                                                        return true;
                                                    } catch (Exception e) {
                                                        return false;
                                                    }
                           
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

     /**
     * 
     * @param yearValue the year to add the data to
     * @param monthValue the month to add the data to
     * @param dayValue the day to add the data to
     * @param roomName the room to find the data to
     * @param value the hourly usage to set
     * @return weather the operation was successful and the data was added
     */
    public static boolean addDay(long yearValue, long monthValue, long dayValue, String roomName){
        JSONObject jsonObject = SmartHeating.jsonObject;
        JSONArray rooms = (JSONArray) jsonObject.get("data");
        for (Object room : rooms) {
            JSONObject roomObject = (JSONObject) room;
            if (roomObject.get("name").equals(roomName)){
                JSONObject measurements = (JSONObject) roomObject.get("measurements");
                JSONArray years = (JSONArray) measurements.get("years");
                for (Object year : years) {
                    JSONObject yearObject = (JSONObject) year;
                    if(yearObject.get("year").equals( (Object)yearValue)){
                        JSONArray months = (JSONArray) yearObject.get("months");
                        for (Object month : months) {
                            JSONObject monthObject = (JSONObject) month;
                            if (monthObject.get("month").equals( (Object)monthValue)) {
                                JSONArray days = (JSONArray) monthObject.get("days");
                                for (Object day : days) {
                                    JSONObject dayObject = (JSONObject) day;
                                    if (dayObject.get("day").equals( (Object)dayValue)) {
                                        return false;
                                    }
                                }
                                JSONObject newDay = new JSONObject();
                                newDay.put("day", dayValue);
                                JSONArray hourlyUsage = new JSONArray();
                                for (int i = 0; i < 24; i++) {
                                    hourlyUsage.add(0);
                                }
                                JSONArray hourlyTemperature = new JSONArray();
                                for (int i = 0; i < 24; i++) {
                                    hourlyTemperature.add(0);
                                }
                                newDay.put("hourlyUsage", hourlyUsage);
                                newDay.put("hourlyTemperature", hourlyTemperature);
                                days.add(newDay);
                                SmartHeating.jsonObject = jsonObject;
                                writeToFile(jsonObject);
                                return true;                                
                                
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 
     * @param yearValue the year to add the data to
     * @param monthValue the month to add the data to
     * @param roomName the room to find the data to
     * @return weather the operation was successful and the data was added
     */
    public static boolean AddMonth(long yearValue, long monthValue, String roomName){
        JSONObject jsonObject = SmartHeating.jsonObject;
        JSONArray rooms = (JSONArray) jsonObject.get("data");
        for (Object room : rooms) {
            JSONObject roomObject = (JSONObject) room;
            if (roomObject.get("name").equals(roomName)){
                JSONObject measurements = (JSONObject) roomObject.get("measurements");
                JSONArray years = (JSONArray) measurements.get("years");
                for (Object year : years) {
                    JSONObject yearObject = (JSONObject) year;
                    if(yearObject.get("year").equals( (Object)yearValue)){
                        JSONArray months = (JSONArray) yearObject.get("months");
                        for (Object month : months) {
                            JSONObject monthObject = (JSONObject) month;
                            if (monthObject.get("month").equals( (Object)monthValue)) {
                                return false;
                            }
                        }
                        JSONObject newMonth = new JSONObject();
                        newMonth.put("month", monthValue);
                        JSONArray days = new JSONArray();
                        newMonth.put("days", days);
                        months.add(newMonth);
                        SmartHeating.jsonObject = jsonObject;
                        writeToFile(jsonObject);
                        return true;                                
                        
                    }
                }
            }
        }
        return false;
    }

    /**
     * 
     * @param yearValue the year to add the data to
     * @param roomName the room to find the data to
     * @return weather the operation was successful and the data was added
     */

    public static boolean addYear(long yearValue, String roomName){
        JSONObject jsonObject = SmartHeating.jsonObject;
        JSONArray rooms = (JSONArray) jsonObject.get("data");
        for (Object room : rooms) {
            JSONObject roomObject = (JSONObject) room;
            if (roomObject.get("name").equals(roomName)){
                JSONObject measurements = (JSONObject) roomObject.get("measurements");
                JSONArray years = (JSONArray) measurements.get("years");
                for (Object year : years) {
                    JSONObject yearObject = (JSONObject) year;
                    if(yearObject.get("year").equals( (Object)yearValue)){
                        return false;
                    }
                }
                JSONObject newYear = new JSONObject();
                newYear.put("year", yearValue);
                JSONArray months = new JSONArray();
                newYear.put((String)"months", (Object) months);
                years.add(newYear);
                SmartHeating.jsonObject = jsonObject;
                writeToFile(jsonObject);
                return true;                                           
            }
        }
        return false;
    }

    /**
     * 
     * @param roomName the room to find the data to
     * @return weather the operation was successful and the data was added
     */

    public static boolean addRoom(String roomName){
        JSONObject jsonObject = SmartHeating.jsonObject;
        JSONArray rooms = (JSONArray) jsonObject.get("data");
        for (Object room : rooms) {
            JSONObject roomObject = (JSONObject) room;
            if (roomObject.get("name").equals(roomName)){
                return false;
            }
        }
        JSONObject newRoom = new JSONObject();
        newRoom.put("name", roomName);
        JSONObject measurements = new JSONObject();
        JSONArray years = new JSONArray();
        measurements.put("years", years);
        newRoom.put("measurements", measurements);
        rooms.add(newRoom);
        SmartHeating.jsonObject = jsonObject;
        writeToFile(jsonObject);
        return true;                                           
    }


    public static void createDataset(){ 
        String room = "Schlafzimmer";
        addRoom(room);
        int[] months = {31,28,31,30,31,30,31,31,30,31,30,31};
        int[] years = {2022};
        float value = 50.0f;
        System.out.println("Creating dataset");
        for(int year: years){
            utils.addYear(year, room);
            for(int month = 1; month <= 12; month ++){
                utils.AddMonth(year, month, room);
                for(int day = 1; day <= months[month-1]; day++){
                    utils.addDay(year, month, day, room);
                    for(int hour = 0; hour < 24; hour++){
                        utils.addMeasurementToDay(year, month, day, (long) value, hour, room);
                        // increase value by a random number between 0 and 5
                        value += 3.0f + (Math.random() * 20);
                        System.out.println("Added value: " + value);
                    }
                }
            }
        };
    }


    /* The code above does the following:
    1. Connects to the database
    2. Selects the column "file" from the table "ekdi"
    3. Gets the content of the column
    4. Converts the content to a JSON object and stores it in the "jsonObject" variable
    5. Calls the "writeToFile" method 
    */
    public static void pull(){
        String connectionUrl ="jdbc:mysql://db4free.net:3306/school";
        String statement = "SELECT file FROM ekdi";
        try {
            String content = "";
            Connection conn = DriverManager.getConnection(connectionUrl, "ekdiadmin", "12345678");
            PreparedStatement ps = conn.prepareStatement(statement);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                content = rs.getString("file");
            }
            Object obj = null;
            try (FileReader fileReader = new FileReader("src/main/data.json")){
                obj = new JSONParser().parse(content);
                SmartHeating.jsonObject= (JSONObject) obj;
                writeToFile(SmartHeating.jsonObject);
            } catch (Exception e) {
                System.out.println(e);
                System.err.println("Error while reading file");
            }
            

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    /* The code above does the following:
    1. Establishes a connection to the database. 
    2. Creates a prepared statement (statement) with the query "UPDATE 
    */

    public static void push(){
        String connectionUrl ="jdbc:mysql://db4free.net:3306/school";
        String statement = "UPDATE ekdi SET file = ?";
        try {
            Connection conn = DriverManager.getConnection(connectionUrl, "ekdiadmin", "12345678");
            PreparedStatement ps = conn.prepareStatement(statement);
            ps.setString(1, SmartHeating.jsonObject.toJSONString());
            ps.executeUpdate();
        } catch (Exception e) {

        }
    }
    /* The code above does the following:
    1. It creates an HttpClient object.
    2. It creates a HttpRequest object.
    3. It sends the request and gets a response.
    4. It prints the response.
    5. It parses the JSON response and gets the temperature value.
    6. It assigns the temperature value to the temperature field of the SmartHeating class. 
    */
    public static void getTemperature(){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://thermostat1/status"))
            .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        {
            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());
                Object r = new JSONParser().parse(response.body());
                JSONObject jsonObject = (JSONObject) r;
                JSONArray thermostats = (JSONArray) jsonObject.get("thermostats");
                JSONObject thermostat = (JSONObject) thermostats.get(0);
                JSONObject tmp = (JSONObject) thermostat.get("tmp");
                SmartHeating.temperature = (double) tmp.get("value");
            } catch (Exception e) {
                System.out.println(e);
            }
        }

    }

}
