package integration;

public interface MessageDelivery {

    void sendMessage(Message message);

    void addListener(Listener<Message> listener);

}
