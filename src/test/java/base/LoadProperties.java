package base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoadProperties {
    private static FileInputStream input;
    private static Properties prop = null;
    private static final Logger LOG = LogManager.getLogger(LoadProperties.class);

    public static String getProperty(final String key) {

        try {
            input = new FileInputStream("src/test/resources/local.properties");
            prop = new Properties();
            prop.load(input);
        } catch (FileNotFoundException e) {
            LOG.error("Properties File Not Found", e);
        } catch (IOException e) {
            LOG.error("IO Exception when loading properties  ", e);
        } finally {
            try {
                input.close();
            } catch (IOException IOException) {
                LOG.error("IO Exception when closing properties", IOException);
            }
        }
        return prop.getProperty(key);
    }
}
