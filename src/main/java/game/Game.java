package game;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import game.word.Person;
import game.word.Word;
import game.word.impl.PersonImpl;
import game.word.impl.WordImpl;
import integration.Message;
import integration.MessageDelivery;
import integration.Messagers;
import integration.printable.Choice;
import integration.printable.Replica;

@Singleton
public class Game {

    private final MessageDelivery messageDelivery;

    private Actor actor;
    private Person person;
    private Word word;

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
                    case "/start" -> newGame();
                    case "/exit" -> stopGame();
                    default -> messageDelivery.sendMessage(
                        new Message(Messagers.GAME_CONTROLLER, Messagers.USER,
                            new Replica("Неизвестная операция: %s".formatted(executionString))));
                }
                return;
            }
            if (message.to(Messagers.PERSON)) {
                if (actor == null) {
                    messageDelivery.sendMessage(
                        new Message(Messagers.PERSON, Messagers.USER, new Replica("Сначала надо начать игру")));
                } else {
                    messageDelivery.sendMessage(
                        new Message(Messagers.PERSON, Messagers.USER, Replica.of(actor.act(executionString))));
                    messageDelivery.sendMessage(
                        new Message(Messagers.PERSON, Messagers.USER, Choice.of(actor.getChoices())));
                }
            }
        });

    }

    private void newGame() {
        initGame();
        messageDelivery.sendMessage(
            new Message(Messagers.GAME_CONTROLLER, Messagers.USER, new Replica("Начинается новая игра...")));
        messageDelivery.sendMessage(new Message(Messagers.PERSON, Messagers.USER, Choice.of(actor.getChoices())));
    }

    private void stopGame() {
        messageDelivery.sendMessage(
            new Message(Messagers.GAME_CONTROLLER, Messagers.USER, new Replica("Ну и не надо, ну и пожалуйста!")));
    }

    private void initGame() {
        this.person = new PersonImpl();
        this.word = new WordImpl();
        this.actor = new Actor(this.person, this.word);
    }


}
