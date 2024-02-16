package configuration;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import java.util.Properties;

public class PropertiesModule extends AbstractModule {

    @Override
    protected void configure() {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load properties file", e);
        }
        Names.bindProperties(binder(), properties);
    }
}
