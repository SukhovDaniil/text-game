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
import integration.AbstractMessageDelivery;
import integration.Message;
import integration.printable.Replica;
import java.util.Arrays;

@Singleton
public class TelegramMessageDelivery extends AbstractMessageDelivery {

    private final TelegramBot telegramBot;
    private Long chatId;
    private TelegramMessageFactory messageFactory;

    @Inject
    public TelegramMessageDelivery(@Named("telegram.token") String token) {
        this.telegramBot = new TelegramBot(token);
        setMessageFormUserListener();
        setMessageToUserListener();

    }

    private void setMessageFormUserListener() {
        this.telegramBot.setUpdatesListener(updates -> {
            updates.forEach(update -> {
                if (update.message() != null) {
                    initChat(updates.get(0).message().from().id());
                } else if (update.myChatMember() != null) {
                    initChat(updates.get(0).myChatMember().chat().id());
                } else {
                    initChat(update.callbackQuery().from().id());
                }

                this.sendMessage(
                    Message.builder()
                        .from(USER)
                        .to(isBotCommand(update) ? GAME_CONTROLLER : PERSON)
                        .message(new Replica(getMessageString(update)))
                        .build()
                );
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void initChat(long chatId) {
        if (this.chatId == null) {
            this.chatId = chatId;
            this.messageFactory = new TelegramMessageFactory(this.chatId);
        }
    }

    private String getMessageString(Update update) {
        if (update.message() != null) {
            return update.message().text();
        }
        return update.callbackQuery().data();
    }

    private boolean isBotCommand(Update update) {
        return update.message() != null
            && update.message().entities() != null
            && Arrays.stream(update.message().entities())
            .anyMatch(entity -> entity.type().equals(bot_command));
    }

    private void setMessageToUserListener() {
        this.addListener(message -> {
            if (message.to(USER)) {
                messageFactory.getMessageFrom(message.getMessages()).forEach(telegramBot::execute);
            }
        });
    }
}
