package game;

import game.interaction.ActionController;
import game.interaction.Actionable;
import game.interaction.Actor;
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
import integration.printable.Replica;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Getter
@Slf4j
public class GameController implements ActionController {

    private final MoveController moveController = new MoveController();

    private final Person person;
    private final World world;

    private ScheduledExecutorService executorService;

    @Setter
    @Accessors(fluent = true)
    private MessageDelivery messageDelivery;

    private AbstractSeller seller = new Seller()
        .addItem(new Sword(10, "меч-леденец", 1))
        .addItem(new HealthPotion("вода жизни", 1000))
        .addItem(new Sword(66, "меч", 69));

    private Actor personActor;
    private Map<Positionable, Actor> npcActors = new HashMap<>();


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

    public GameController autogame(long chatId) {
        executorService = Executors.newScheduledThreadPool(world.getPositionable().size() + 2);
        this.personActor = new Actor(() -> {
            List<Move> possibleMoves = world.getPossibleMove(((Positionable) person)).stream().toList();
            if (!possibleMoves.isEmpty()) {
                moveController.move(((Positionable) person),
                    possibleMoves.get(((int) (possibleMoves.size() * Math.random()))));
            }

            world.getSurroundingActionable(((Positionable) person))
                .stream()
                .filter(a -> !a.isBusy())
                .findAny().ifPresent(p -> this.onRecieve(p, chatId));

        }, executorService);

        this.npcActors = world.getPositionable().stream()
            .filter(p -> !(p instanceof Person))
            .map(positionable ->
                new Pair<>(
                    positionable,
                    new Actor(() -> {
                        if (!((Actionable) positionable).isBusy()) {
                            List<Move> possibleMoves = world.getPossibleMove(positionable).stream().toList();
                            if (!possibleMoves.isEmpty()) {
                                moveController.move(positionable,
                                    possibleMoves.get(((int) (possibleMoves.size() * Math.random()))));
                            }
                        }
                    }, executorService)
                )
            )
            .collect(Collectors.toMap(Pair::getKey, Pair::getValue));

        personActor.run();
        npcActors.values().forEach(Actor::run);

        log.debug("[{}] autogame start", Thread.currentThread());

        return this;
    }

    private void onRecieve(Object object, long chatId) {
        if (!(object instanceof Actionable)) {
            throw new IllegalArgumentException();
        }

        executorService.execute(() -> {
            this.personActor.stop();
            this.npcActors.get(object).stop();

            availableChoices = ((Actionable) object).getActions(person).stream()
                .collect(Collectors.toMap(ActionController::getDescription, c -> c));

            while (!getChoices().isEmpty()) {
                messageDelivery.sendMessage(
                    new Message(chatId, Messagers.PERSON, Messagers.USER,
                        Choice.of(getChoices())));
                String choice = new ArrayList<>(getChoices()).get(((int) (getChoices().size() * Math.random())));
                messageDelivery.sendMessage(
                    new Message(chatId, Messagers.PERSON, Messagers.USER,
                        Replica.of(choice)));
                messageDelivery.sendMessage(
                    new Message(chatId, Messagers.PERSON, Messagers.USER,
                        Replica.of(act(choice))));
            }

            this.personActor.run();
            this.npcActors.get(object).run();
        });
    }

    @Getter
    @AllArgsConstructor
    private class Pair<K, V> {

        private final K key;
        private final V value;
    }
}
