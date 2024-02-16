package configuration;

import com.google.inject.AbstractModule;
import integration.MessageDelivery;
import telegram.TelegramMessageDelivery;

public class MessageDeliveryModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MessageDelivery.class).to(TelegramMessageDelivery.class);
    }
}
