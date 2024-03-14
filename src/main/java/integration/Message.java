package integration;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

@Builder
public class Message {

    private final Messagers from;
    private final Messagers to;
    @Getter
    @Singular
    private final List<? extends Printable> messages;

    public Message(Messagers from, Messagers to, Printable message) {
        this(from, to, List.of(message));
    }

    public Message(Messagers from, Messagers to, List<? extends Printable> messages) {
        this.from = from;
        this.to = to;
        this.messages = messages;
    }

    public boolean to(Messagers messager) {
        return this.to.equals(messager);
    }

    public boolean from(Messagers messager) {
        return this.from.equals(messager);
    }
}
