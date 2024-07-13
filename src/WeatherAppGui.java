import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;


public class WeatherAppGui extends JFrame {
    public WeatherAppGui() {
        super("Weather App");

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //px size
        setSize(450, 650);

        //setup for GUI elements
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        addGuiElements();
    }

    private void addGuiElements(){
        JTextField searchField = new JTextField();

        //Create and modify the search bar
        searchField.setBounds(15, 15, 350, 50);
        searchField.setFont(new Font("Serif", Font.PLAIN, 20));
        add(searchField);

        //create search btn
        JButton searchButton = new JButton(loadImage("img/search.png"));
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375, 15, 50, 50);
        add(searchButton);

        //Add condition img
        JLabel weatherConditionImg = new JLabel(loadImage("img/cloudy.png"));
        weatherConditionImg.setBounds(0, 100, 450, 220);
        add(weatherConditionImg);

        JLabel weatherConditionDesc = new JLabel("Cloudy");
        weatherConditionDesc.setBounds(0, 405, 450, 36);
        weatherConditionDesc.setFont(new Font("Dialog", Font.PLAIN, 32));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherConditionDesc);

        //Add temperature and center
        JLabel TemperatureText = new JLabel("XX C");
        TemperatureText.setBounds(0, 325, 450, 54);
        TemperatureText.setFont(new Font("Dialog", Font.BOLD, 48));
        TemperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(TemperatureText);

        JLabel humidityImg = new JLabel(loadImage("img/humidity.png"));
        humidityImg.setBounds(15, 500, 74, 66);
        add(humidityImg);

        JLabel humidityText = new JLabel("<html><b>Humidity</b> 10%</html>");
        humidityText.setBounds(90, 500, 85, 55);
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(humidityText);

        JLabel windSpeedImg = new JLabel(loadImage("img/windspeed.png"));
        windSpeedImg.setBounds(220, 500, 74, 66);
        add(windSpeedImg);

        JLabel windSpeedText = new JLabel("<html><b>Windspeed</b> 5mph</html>");
        windSpeedText.setBounds(310, 500, 85, 55);
        windSpeedText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(windSpeedText);

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