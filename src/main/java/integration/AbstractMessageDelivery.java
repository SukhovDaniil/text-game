package integration;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMessageDelivery implements MessageDelivery {

    private final List<Listener<Message>> listeners = new ArrayList<>();

    @Override
    public void sendMessage(Message message) {
        this.listeners.forEach(listener -> listener.onMessage(message));
    }

    @Override
    public void addListener(Listener<Message> listener) {
        this.listeners.add(listener);
    }
}
