package telegram;

import static com.pengrad.telegrambot.model.MessageEntity.Type.bot_command;
import static integration.GameCommand.DELETE_USER;
import static integration.GameCommand.NEW_USER;
import static integration.GameCommand.isGameCommand;
import static integration.Messagers.GAME_CONTROLLER;
import static integration.Messagers.PERSON;
import static integration.Messagers.USER;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.ChatMember.Status;
import com.pengrad.telegrambot.model.Update;
import integration.AbstractMessageDelivery;
import integration.Message;
import integration.printable.Replica;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public class TelegramMessageDelivery extends AbstractMessageDelivery {

    private final TelegramBot telegramBot;

    @Inject
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
                log.debug("Message from tg: " + update);
                this.sendMessage(fromUpdate(update));
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private Message fromUpdate(Update update) {
        Message.MessageBuilder messageBuilder = Message.builder()
            .from(USER);
        if (update.myChatMember() != null) {
            return messageBuilder
                .chatId(update.myChatMember().chat().id())
                .to(GAME_CONTROLLER)
                .message(new Replica(update.myChatMember().newChatMember().status().equals(Status.member)
                    ? NEW_USER.getValue()
                    : DELETE_USER.getValue()))
                .build();
        }
        if (update.message() != null) {
            return messageBuilder
                .chatId(update.message().chat().id())
                .message(new Replica(update.message().text()))
                .to(isBotCommand(update) && isGameCommand(update.message().text()) ? GAME_CONTROLLER : PERSON)
                .build();
        }
        if (update.callbackQuery() != null) {
            return messageBuilder
                .chatId(update.callbackQuery().from().id())
                .message(new Replica(update.callbackQuery().data()))
                .to(PERSON)
                .build();
        }
        throw new UnsupportedOperationException("Unknown message type");
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
                messageFactory.getMessageFrom(message.getMessages(), message.getChatId()).forEach(s -> {
                    log.debug("Message to tg: " + s.toString());
                    telegramBot.execute(s);
                });
            }
        });
    }
}
