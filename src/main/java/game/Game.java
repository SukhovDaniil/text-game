package game;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import data.GameController;
import integration.Message;
import integration.MessageDelivery;
import integration.Messagers;
import integration.printable.Choice;
import integration.printable.Replica;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class Game {

    private final MessageDelivery messageDelivery;

    private final Map<Long, Actor> actors = new HashMap<>();
    @Inject
    GameController gameController;

    @Inject
    public Game(MessageDelivery messageDelivery) {
        this.messageDelivery = messageDelivery;

        this.messageDelivery.addListener(message -> {
            if (message.getMessages().isEmpty()) {
                return;
            }
            String executionString = message.getMessages().get(0).get();
            if (message.to(Messagers.GAME_CONTROLLER)) {
                switch (executionString) {
                    case "/create", "/start" -> initGame(message.getChatId());
                    case "/delete" -> gameController.deleteUser(message.getChatId());
                    case "/exit" -> stopGame(message.getChatId());
                    default -> messageDelivery.sendMessage(
                        new Message(message.getChatId(), Messagers.GAME_CONTROLLER, Messagers.USER,
                            new Replica("Неизвестная операция: %s".formatted(executionString))));
                }
                return;
            }
            if (message.to(Messagers.PERSON)) {
                if (!actors.containsKey(message.getChatId())) {
                    messageDelivery.sendMessage(
                        new Message(message.getChatId(), Messagers.PERSON, Messagers.USER,
                            new Replica("Сначала надо начать игру")));
                } else {
                    Actor actor = actors.get(message.getChatId());
                    messageDelivery.sendMessage(
                        new Message(message.getChatId(), Messagers.PERSON, Messagers.USER,
                            Replica.of(actor.act(executionString))));
                    messageDelivery.sendMessage(
                        new Message(message.getChatId(), Messagers.PERSON, Messagers.USER,
                            Choice.of(actor.getChoices())));
                }
            }
        });

    }

    private void stopGame(long chatId) {
        messageDelivery.sendMessage(
            new Message(chatId, Messagers.GAME_CONTROLLER, Messagers.USER,
                new Replica("Ну и не надо, ну и пожалуйста!")));
    }

    private void initGame(long chatId) {
        if (!actors.containsKey(chatId)) {
            actors.put(chatId,
                gameController.getActorForUser(chatId).messageDelivery(messageDelivery).autogame(chatId));
            messageDelivery.sendMessage(
                new Message(chatId, Messagers.GAME_CONTROLLER, Messagers.USER,
                    new Replica("Начинается новая игра...")));
        } else {
            messageDelivery.sendMessage(
                new Message(chatId, Messagers.GAME_CONTROLLER, Messagers.USER,
                    new Replica("Продолжается сохраненная игра...")));
        }
        messageDelivery.sendMessage(
            new Message(chatId, Messagers.PERSON, Messagers.USER, Choice.of(actors.get(chatId).getChoices())));
    }


}
