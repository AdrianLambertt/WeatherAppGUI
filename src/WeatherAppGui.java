//Made by Adrian Lambert 15/07/2024


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.json.simple.JSONObject;


public class WeatherAppGui extends JFrame {
    private JSONObject weatherData;

    JLabel weatherConditionImg = new JLabel(loadImage("img/cloudy.png"));
    JLabel weatherConditionDesc = new JLabel("");
    JLabel TemperatureText = new JLabel("");
    JLabel humidityText = new JLabel("");
    JLabel windSpeedText = new JLabel("");


    public WeatherAppGui() {
        super("Weather App");

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //px size
        setSize(450, 700);

        //setup for GUI elements
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        addGuiElements();
    }

    private void addGuiElements(){
        //Create and modify the search bar
        JTextField searchField = new JTextField();
        searchField.setBounds(20, 15, 400, 50);
        searchField.setFont(new Font("Serif", Font.PLAIN, 20));
        add(searchField);

        //Add condition img
        weatherConditionImg.setBounds(100, 150, 250, 200);
        add(weatherConditionImg);

        weatherConditionDesc.setBounds(0, 455, 450, 36);
        weatherConditionDesc.setFont(new Font("Dialog", Font.PLAIN, 32));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherConditionDesc);

        //Add temperature and center
        TemperatureText.setBounds(0, 375, 450, 54);
        TemperatureText.setFont(new Font("Dialog", Font.BOLD, 48));
        TemperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(TemperatureText);

        JLabel humidityImg = new JLabel(loadImage("img/humidity.png"));
        humidityImg.setBounds(15, 550, 74, 66);
        add(humidityImg);

        humidityText.setBounds(90, 550, 85, 55);
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(humidityText);

        JLabel windSpeedImg = new JLabel(loadImage("img/windspeed.png"));
        windSpeedImg.setBounds(220, 550, 74, 66);
        add(windSpeedImg);


        windSpeedText.setBounds(310, 550, 85, 55);
        windSpeedText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(windSpeedText);

        //create search btn
        JButton searchButton = new JButton(loadImage("img/search.png"));
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(20, 75, 150, 50);

        //Retrive weather data via ActionListener
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = searchField.getText();
                if (input.replaceAll("\\s", "").length() <=0) {
                    return;
                }
                setNewLocation(input);
            }
        });

        add(searchButton);

        //Home is used to store the locationt that is pulled when the program starts.
        JButton setHomeButton = new JButton("Set home");
        setHomeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setHomeButton.setBounds(320, 75, 100, 50);
        setHomeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = searchField.getText();
                if (input.replaceAll("\\s", "").length() <=0) {
                    return;
                }

                //Create and write to file "homeLocation for location storing."
                try {
                    FileWriter home = new FileWriter("homeLocation.txt");
                    home.write(input);
                    home.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                

            }
        });

        add(setHomeButton);

        //Read and set new location as soon as program starts
        File f = new File("homeLocation.txt");
        if (f.exists()) {
            try {
                Scanner s = new Scanner(f);
                String location = s.nextLine();
                s.close();
                setNewLocation(location);
            } catch (Exception e) {
                e.printStackTrace();
            }  
        }
    }

    private void setNewLocation(String input) {
        //Set new data on GUI
        weatherData = WeatherApp.getWeather(input);
        String weatherCondition = (String) weatherData.get("weather_condition");
        weatherConditionDesc.setText(weatherCondition);
        
        switch (weatherCondition) {
            case "Clear":
                weatherConditionImg.setIcon(loadImage("img/clear.png"));
                break;
            case "Cloudy":
                weatherConditionImg.setIcon(loadImage("img/cloudy.png"));
                break;
            case "Rain":
                weatherConditionImg.setIcon(loadImage("img/rain.png"));
                break;
            case "Snow":
                weatherConditionImg.setIcon(loadImage("img/snow.png"));
                break;
        }

        double temperature = (double) weatherData.get("temperature");
        TemperatureText.setText(temperature + " C");

        long humidity = (long) weatherData.get("humidity");
        humidityText.setText("<html><b>Humidity</b> "+ humidity +"%</html>");

        double windspeed = (double) weatherData.get("windspeed");
        windSpeedText.setText("<html><b>Windspeed</b> "+ windspeed +"mph</html>");
    }



    //Function for loading images for GUI
    private ImageIcon loadImage(String path){
        try{
            BufferedImage img = ImageIO.read(new File(path));
            return new ImageIcon(img);
        } catch(IOException e){
            e.printStackTrace();
            System.out.println("Image could not be loaded");
            return null;
        }


    }
}