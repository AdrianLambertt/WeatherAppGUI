//Made by Adrian Lambert 15/07/2024
import javax.swing.*;

public class AppLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                new WeatherAppGui().setVisible(true);
            }
        });
    }
}
