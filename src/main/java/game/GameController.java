package game;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import integration.Message;
import integration.MessageDelivery;
import integration.Messagers;

@Singleton
public class GameController {

    private final MessageDelivery messageDelivery;

    @Inject
    public GameController(MessageDelivery messageDelivery) {
        this.messageDelivery = messageDelivery;
        this.messageDelivery.addListener(message -> {
            if (message.to(Messagers.GAME_CONTROLLER)) {
                switch (message.getMessage()) {
                    case "/start" -> newGame();
                    case "/exit" -> stopGame();
                    default -> messageDelivery.sendMessage(
                        new Message(Messagers.GAME_CONTROLLER, Messagers.USER, "Неизвестная операция"));
                }
            }
        });
    }

    private void newGame() {
        messageDelivery.sendMessage(new Message(Messagers.GAME_CONTROLLER, Messagers.USER, "Начинается новая игра..."));
    }

    private void stopGame() {
        messageDelivery.sendMessage(
            new Message(Messagers.GAME_CONTROLLER, Messagers.USER, "Ну и не надо, ну и пожалуйста!"));
    }

}
