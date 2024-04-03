package integration;

import integration.printable.Printable;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

@Builder
public class Message {

    @Getter
    private final long chatId;

    private final Messagers from;
    private final Messagers to;
    @Getter
    @Singular
    private final List<? extends Printable> messages;

    public Message(long chatId, Messagers from, Messagers to, Printable message) {
        this(chatId, from, to, List.of(message));
    }

    public Message(long chatId, Messagers from, Messagers to, List<? extends Printable> messages) {
        this.chatId = chatId;
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

    @Override
    public String toString() {
        return "{\"chat_id\": %s, \"from\": \"%s\", \"to\": \"%s\", \"messages\": [%s]}"
            .formatted(chatId, from.name(), to.name(),
                String.join(", ", messages.stream().map(p -> "\"%s\"".formatted(p.get())).toList()));
    }
}
