package game;

import game.interaction.ActionController;
import game.interaction.Actionable;
import game.interaction.move.Move;
import game.interaction.move.MoveController;
import game.items.potion.HealthPotion;
import game.items.weapon.Sword;
import game.npc.humans.Seller;
import game.npc.humans.abstractions.AbstractSeller;
import game.word.Person;
import game.word.Positionable;
import game.word.World;
import integration.Message;
import integration.MessageDelivery;
import integration.Messagers;
import integration.printable.Choice;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
@Getter
public class Actor implements ActionController {

    private final MoveController moveController = new MoveController();

    private final Person person;
    private final World world;
    @Setter
    @Accessors(fluent = true)
    private MessageDelivery messageDelivery;

    private AbstractSeller seller = new Seller()
        .addItem(new Sword(10, "меч-леденец", 1))
        .addItem(new HealthPotion("вода жизни", 1000))
        .addItem(new Sword(66, "меч", 69));


    private Map<String, ActionController> availableChoices = new HashMap<>();
    private ActionController currentContext;

    @Override
    public List<String> act(String action) {
        if (currentContext == null) {
            if (availableChoices.containsKey(action)) {
                currentContext = availableChoices.get(action);
                return List.of();
            } else {
                throw new IllegalArgumentException("Неизвестная операция: %s".formatted(action));
            }
        }
        return currentContext.act(action);
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public Set<String> getChoices() {
        if (currentContext == null) {
            this.availableChoices = world.getSurroundingActionable(((Positionable) person))
                .stream()
                .flatMap(actionable -> actionable.getActions(person).stream())
                .collect(Collectors.toMap(ActionController::getDescription, c -> c));
            return availableChoices.keySet();
        }
        return currentContext.getChoices();
    }

    public Actor autogame(long chatId) {
        Thread mainThread = Thread.currentThread();
        new Thread(() -> {
            while (mainThread.isAlive()) {
                if (!person.isBusy()) {
                    List<Move> possibleMoves = world.getPossibleMove(((Positionable) person)).stream().toList();
                    if (!possibleMoves.isEmpty()) {
                        moveController.move(((Positionable) person),
                            possibleMoves.get(((int) (possibleMoves.size() * Math.random()))));
                    }
                }

                world.getSurroundingActionable(((Positionable) person))
                    .stream()
                    .filter(a -> !a.isBusy())
                    .findFirst().ifPresentOrElse(actionable -> {
                            actionable.occupy();
                            availableChoices = actionable.getActions(person).stream()
                                .collect(Collectors.toMap(ActionController::getDescription, c -> c));
                            messageDelivery.sendMessage(
                                new Message(chatId, Messagers.PERSON, Messagers.USER,
                                    Choice.of(getChoices())));

                            actionable.toFree();
                        },
                        () -> {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        });
            }
        }).start();

        world.getPositionable().forEach(positionable -> {
            new Thread(() -> {
                while (mainThread.isAlive()) {
                    if (!((Actionable) positionable).isBusy()) {
                        List<Move> possibleMoves = world.getPossibleMove((positionable)).stream().toList();
                        if (!possibleMoves.isEmpty()) {
                            moveController.move((positionable),
                                possibleMoves.get(((int) (possibleMoves.size() * Math.random()))));
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        });

        return this;
    }
}
