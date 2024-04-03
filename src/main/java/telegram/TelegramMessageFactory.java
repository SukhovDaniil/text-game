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
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class TelegramMessageFactory {

    private final Map<Class<? extends Printable>, BiFunction<Collection<Printable>, Long, Set<SendMessage>>> messagesSuppliers =
        new HashMap<>() {{
            put(Replica.class, (printables, id) -> generateTextMessage(printables, id));
            put(Choice.class, (printables, id) -> generateKeyboardMessage(printables, id));
        }};

    public HashSet<SendMessage> getMessageFrom(Collection<? extends Printable> printables, long id) {
        Map<Class<? extends Printable>, List<Printable>> printablesByType = printables.stream()
            .collect(Collectors.toMap(
                Printable::getClass,
                printable -> new LinkedList<>(List.of(printable)),
                (exist, additional) -> {
                    exist.addAll(additional);
                    return exist;
                }));

        return printablesByType.entrySet().stream()
            .flatMap(e -> messagesSuppliers.get(e.getKey()).apply(e.getValue(), id).stream())
            .collect(Collectors.toCollection(HashSet::new));
    }

    private Set<SendMessage> generateKeyboardMessage(Collection<Printable> printables, long id) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        printables.forEach(printable -> {
            InlineKeyboardButton[] buttonRaw = new InlineKeyboardButton[1];
            buttonRaw[0] = new InlineKeyboardButton(printable.get()).callbackData(printable.get());
            keyboardMarkup.addRow(buttonRaw);
        });

        return Set.of(new SendMessage(id, "Доступен выбор")
            .replyMarkup(keyboardMarkup));
    }

    private Set<SendMessage> generateTextMessage(Collection<Printable> printables, long id) {
        return printables.stream()
            .map(printable -> new SendMessage(id, printable.get()))
            .collect(Collectors.toSet());
    }
}
