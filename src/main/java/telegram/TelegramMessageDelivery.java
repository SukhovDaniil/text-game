package telegram;

import static com.pengrad.telegrambot.model.MessageEntity.Type.bot_command;
import static integration.Messagers.GAME_CONTROLLER;
import static integration.Messagers.PERSON;
import static integration.Messagers.USER;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import integration.AbstractMessageDelivery;
import integration.Message;
import java.util.Arrays;

@Singleton
public class TelegramMessageDelivery extends AbstractMessageDelivery {

    private final TelegramBot telegramBot;
    private long chatId;

    @Inject
    public TelegramMessageDelivery(@Named("telegram.token") String token) {
        this.telegramBot = new TelegramBot(token);
        setMessageFormUserListener();
        setMessageToUserListener();

    }

    private void setMessageFormUserListener() {
        this.telegramBot.setUpdatesListener(updates -> {
            updates.forEach(update -> {
                if (updates.get(0).message() == null) {
                    this.chatId = updates.get(0).myChatMember().chat().id();
                } else {
                    this.chatId = updates.get(0).message().from().id();
                }

                this.sendMessage(
                    Message.builder()
                        .from(USER)
                        .to(isBotCommand(update) ? GAME_CONTROLLER : PERSON)
                        .message(update.message().text())
                        .build()
                );
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private boolean isBotCommand(Update update) {
        return Arrays.stream(update.message().entities())
            .anyMatch(entity -> entity.type().equals(bot_command));
    }

    private void setMessageToUserListener() {
        this.addListener(message -> {
            if (message.to(USER)) {
                telegramBot.execute(new SendMessage(chatId, message.getMessage()));
            }
        });
    }
}
