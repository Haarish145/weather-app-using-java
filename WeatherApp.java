import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherApp {

    private static JTextField cityField;
    private static JLabel tempLabel, cityLabel, humidityLabel, windLabel, iconLabel;
    private static JPanel resultPanel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Weather App");
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel(new FlowLayout());
        cityField = new JTextField(15);
        JButton searchBtn = new JButton("Search");

        searchPanel.add(cityField);
        searchPanel.add(searchBtn);

        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setVisible(false);

        iconLabel = new JLabel();
        tempLabel = new JLabel();
        cityLabel = new JLabel();
        humidityLabel = new JLabel();
        windLabel = new JLabel();

        tempLabel.setFont(new Font("Arial", Font.BOLD, 28));
        cityLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        humidityLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        windLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        resultPanel.add(iconLabel);
        resultPanel.add(tempLabel);
        resultPanel.add(cityLabel);
        resultPanel.add(humidityLabel);
        resultPanel.add(windLabel);

        searchBtn.addActionListener(e -> {
            String city = cityField.getText();
            if (!city.isEmpty()) {
                fetchWeather(city);
            }
        });

        frame.add(searchPanel, BorderLayout.NORTH);
        frame.add(resultPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static void fetchWeather(String city) {
        try {
            String apiKey = "f27b269d54e4fa1e72993364a80fa8bd";
            String urlStr = "https://api.openweathermap.org/data/2.5/weather?q=" + city +
                    "&appid=" + apiKey + "&units=metric";

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject obj = new JSONObject(response.toString());
            String weather = obj.getJSONArray("weather").getJSONObject(0).getString("main");
            String iconPath = getIconPath(weather);

            ImageIcon icon = new ImageIcon(iconPath);
            iconLabel.setIcon(icon);
            tempLabel.setText((int) obj.getJSONObject("main").getDouble("temp") + "°C");
            cityLabel.setText(obj.getString("name"));
            humidityLabel.setText("Humidity: " + obj.getJSONObject("main").getInt("humidity") + "%");
            windLabel.setText("Wind Speed: " + obj.getJSONObject("wind").getDouble("speed") + " km/h");

            resultPanel.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "City not found or API error.");
            resultPanel.setVisible(false);
        }
    }

    private static String getIconPath(String weather) {
        switch (weather) {
            case "Clouds":
                return "IMAGE/cloud.png";
            case "Clear":
                return "IMAGE/clear.png";
            case "Rain":
                return "IMAGE/rain.png";
            case "Drizzle":
                return "IMAGE/drizzle.png";
            case "Mist":
                return "IMAGE/mist.png";
            case "Haze":
                return "IMAGE/haze.png";
            default:
                return "IMAGE/default.png";
        }
    }
}
