package base;

import groovy.beans.PropertyReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public final class LoadProperties {

    private static final Properties prop = new Properties();
    private static final String FILE_NAME = "properties/app.properties";
    private static boolean isLoaded = false;

    private void PropertyReader() {
        // prevent instantiation
    }

    public static String getProperty(String key) {

        if (!isLoaded) {
            synchronized (PropertyReader.class) {
                if (!isLoaded) {
                    loadProperties();
                    isLoaded = true;
                }
            }
        }
        return prop.getProperty(key);
    }

    private static void loadProperties() {
        try (InputStream input = PropertyReader.class
                .getClassLoader()
                .getResourceAsStream(FILE_NAME)) {

            if (input == null) {
                throw new RuntimeException("app.properties not found in resources");
            }
            prop.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file", e);
        }
    }
}
