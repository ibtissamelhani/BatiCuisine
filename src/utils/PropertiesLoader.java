package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

    public static Properties readProperties() throws IOException {
        Properties properties = new Properties();
        try (InputStream input = PropertiesLoader.class.getClassLoader().getResourceAsStream("resources/db.properties")) {
            if (input == null) {
                throw new IOException("Sorry, unable to find db.properties");
            }
            properties.load(input);
        }
        return properties;
    }
}
