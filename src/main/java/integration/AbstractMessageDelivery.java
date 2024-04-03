package integration;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractMessageDelivery implements MessageDelivery {

    private final List<Listener<Message>> listeners = new ArrayList<>();

    @Override
    public void sendMessage(Message message) {
        log.debug("New message in listener: %s".formatted(message.toString()));
        this.listeners.forEach(listener -> listener.onMessage(message));
    }

    @Override
    public void addListener(Listener<Message> listener) {
        this.listeners.add(listener);
    }
}
