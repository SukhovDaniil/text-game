package telegram;

import com.google.inject.Singleton;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import integration.printable.Choice;
import integration.printable.Printable;
import integration.printable.Replica;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class TelegramMessageFactory {

    private final long chatId;
    private final Map<Class<? extends Printable>, Function<Collection<Printable>, Set<SendMessage>>> messagesSuppliers =
        new HashMap<>() {{
            put(Replica.class, printables -> generateTextMessage(printables));
            put(Choice.class, printables -> generateKeyboardMessage(printables));
        }};

    public HashSet<SendMessage> getMessageFrom(Collection<? extends Printable> printables) {
        Map<Class<? extends Printable>, List<Printable>> printablesByType = printables.stream()
            .collect(Collectors.toMap(
                Printable::getClass,
                printable -> new LinkedList<>(List.of(printable)),
                (exist, additional) -> {
                    exist.addAll(additional);
                    return exist;
                }));

        return printablesByType.entrySet().stream()
            .flatMap(e -> messagesSuppliers.get(e.getKey()).apply(e.getValue()).stream())
            .collect(Collectors.toCollection(HashSet::new));
    }

    private Set<SendMessage> generateKeyboardMessage(Collection<Printable> printables) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        printables.forEach(printable -> {
            InlineKeyboardButton[] buttonRaw = new InlineKeyboardButton[1];
            buttonRaw[0] = new InlineKeyboardButton(printable.get()).callbackData(printable.get());
            keyboardMarkup.addRow(buttonRaw);
        });

        return Set.of(new SendMessage(this.chatId, "Доступен выбор")
            .replyMarkup(keyboardMarkup));
    }

    private Set<SendMessage> generateTextMessage(Collection<Printable> printables) {
        return printables.stream()
            .map(printable -> new SendMessage(this.chatId, printable.get()))
            .collect(Collectors.toSet());
    }
}
