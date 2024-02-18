package configuration;

import com.google.inject.AbstractModule;

public class Configuration extends AbstractModule {

    @Override
    protected void configure() {
        install(new PropertiesModule());
        install(new MessageDeliveryModule());
    }
}
