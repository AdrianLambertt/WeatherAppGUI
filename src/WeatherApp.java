import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

//Retrive weather data from API
//GUI shows latest weather to user
public class WeatherApp {
    public static JSONObject getWeather(String LocationName) {
        JSONArray locationData = getLocationData(LocationName);

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
}
