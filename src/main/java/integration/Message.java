package integration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Message {

    private final Messagers from;
    private final Messagers to;
    @Getter
    private final String message;

    public boolean to(Messagers messager) {
        return this.to.equals(messager);
    }

    public boolean from(Messagers messager) {
        return this.from.equals(messager);
    }
}