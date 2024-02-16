package telegram;

import static com.pengrad.telegrambot.model.MessageEntity.Type.bot_command;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;
import integration.AbstractMessageDelivery;
import integration.Message;
import integration.Messagers;
import java.util.Arrays;

@Singleton
public class TelegramMessageDelivery extends AbstractMessageDelivery {

    private final TelegramBot telegramBot;
    private long chatId;

    @Inject
    public TelegramMessageDelivery(@Named("telegram.token") String token) {
        this.telegramBot = new TelegramBot(token);
        this.telegramBot.setUpdatesListener(updates -> {
            updates.forEach(update -> {
                if (updates.get(0).message() == null) {
                    this.chatId = updates.get(0).myChatMember().chat().id();
                } else {
                    this.chatId = updates.get(0).message().from().id();
                }
                Messagers from = Messagers.USER;
                Messagers to = Messagers.PERSON;
                if (Arrays.stream(update.message().entities()).anyMatch(entity -> entity.type().equals(bot_command))) {
                    to = Messagers.GAME_CONTROLLER;
                }
                this.sendMessage(new Message(from, to, update.message().text()));
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

        this.addListener(message -> {
            if (message.to(Messagers.USER)) {
                telegramBot.execute(new SendMessage(chatId, message.getMessage()));
            }
        });
    }
}
