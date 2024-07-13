import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

//Retrive weather data from API
//GUI shows latest weather to user
public class WeatherApp {
    public static JSONObject getWeather(String LocationName) {
        JSONArray locationData = getLocationData(LocationName);


        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("Latitude");
        double longitute = (double) location.get("Longitute");

        String urlString = "https://api.open-meteo.com/v1/forecast?latitude="+ latitude + "&longitude=" + longitute +"&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m";
        
        try {
            //Call weather API
            HttpURLConnection con = fetchApiResponse(urlString);

            if (con.getResponseCode() != 200) {
                System.out.println("Error: Could not connect to API");
            }

            //Scan and build the output JSON from API
            StringBuilder resultJSON = new StringBuilder();
            Scanner sc = new Scanner(con.getInputStream());
            while (sc.hasNext()) {
                resultJSON.append(sc.nextLine());
            }

            //close connection
            sc.close();
            con.disconnect();

            JSONParser p = new JSONParser();
            JSONObject resultJsonObject = (JSONObject) p.parse(String.valueOf(resultJSON));

            //Object holding timepoint data
            JSONObject hourly = (JSONObject) resultJsonObject.get("hourly");

            //Function called to find current hour index
            JSONArray time = (JSONArray) hourly.get("time");
            int index = getCurrentTimeIndex(time);

            //Get hour data based on time index
            JSONArray temperatureArray = (JSONArray) hourly.get("temperature_2m");
            double temperature = (double) temperatureArray.get(index);

            JSONArray humidityArray = (JSONArray) hourly.get("relativehumidity_2m");
            long humidity = (long) humidityArray.get(index);

            JSONArray windspeedArray = (JSONArray) hourly.get("windspeed_10m");
            double windspeed = (double) windspeedArray.get(index);

            JSONArray weatherCode = (JSONArray) hourly.get("weathercode");
            String weatherCondition = convertWeatherCode((int) weatherCode.get(index));


            //Create JSON object to interact with the frontend
            JSONObject weather = new JSONObject();
            weather.put("temperature", temperature);
            weather.put("weather condition", weatherCondition);
            weather.put("humidity", humidity);
            weather.put("windspeed", windspeed);

            return weather;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    //Function to grab the geolocation of the address entered.
    public static JSONArray getLocationData(String locationName) {
        locationName = locationName.replaceAll(" ", "+");

        String url = "https://geocoding-api.open-meteo.com/v1/search?name=" + locationName + "&count=10&language=en&format=json";

        try {
            //Geocoding API to translate location to LON / LAT data
            HttpURLConnection conn = fetchApiResponse(url);

            if (conn.getResponseCode() != 200) {
                System.out.println("Error: Could not connect to API");

            } else {

                //Scan and build the output JSON from API
                StringBuilder resultJSON = new StringBuilder();
                Scanner sc = new Scanner(conn.getInputStream());
                while (sc.hasNext()) {
                    resultJSON.append(sc.nextLine());
                }

                //close connection
                sc.close();
                conn.disconnect();
                
                //Parse the JSON string to JSON object, then grab the list of location data.
                JSONParser p = new JSONParser();
                JSONObject resultsJsonObs = (JSONObject) p.parse(String.valueOf(resultJSON));
                JSONArray locData = (JSONArray) resultsJsonObs.get("results");
                return locData;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Error path
        return null;
    }


    //Helper function for handling API call using GET
    private static HttpURLConnection fetchApiResponse(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            return conn;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private static int getCurrentTimeIndex(JSONArray timeList) {
        String currentTime = getCurrentTime();
        for (int i = 0; i < timeList.size(); i++ ) {
            String time = (String) timeList.get(i);
            if (time.equalsIgnoreCase(currentTime)) {
                return i;
            }
        }
        return 0;
    }


    //Grab and format DateTime to be YYYY:MM:DD + THH:MM 
    private static String getCurrentTime() {
        LocalDateTime dateTime = LocalDateTime.now();

        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");
        return dateTime.format(f);
    }


    private static String convertWeatherCode(int weatherCode) {
        String weatherCondition = "";

        if (weatherCode == 0) {
            weatherCondition = "Clear";
        } else if (weatherCode <= 3) {
            weatherCondition = "Cloudy";
        } else if (weatherCode >= 51 && weatherCode <= 67) {
            weatherCondition = "Rain";
        } else if (weatherCode >= 71 && weatherCode <= 77) {
            weatherCondition = "Snow";
        }

        return weatherCondition;
    }
}
