import game.interaction.Actionable;
import game.interaction.move.Move;
import game.interaction.move.MoveController;
import game.items.potion.HealthPotion;
import game.items.weapon.Sword;
import game.npc.Human;
import game.npc.humans.Seller;
import game.word.Person;
import game.word.Positionable;
import game.word.World;
import game.word.impl.PersonImpl;
import game.word.impl.Position;
import game.word.impl.WorldImpl;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Multi {

    public static void main(String[] args) throws InterruptedException {
        MoveController moveController = new MoveController();
        World world = new WorldImpl(10, 10);
        Person person = new PersonImpl();
        Human seller = new Seller().addItems(
            List.of(new Sword(10, "Не меч", 100), new HealthPotion("Байкальская водица", 789)));

        world.setOnPosition(((Positionable) person), new Position(10, 7));
        world.setOnPosition(((Positionable) seller), new Position(3, 5));
        log.debug(world.toString());

        Thread personThread = new Thread(() -> {
            for (int i = 0; i < 500; i++) {
                List<Move> possibleMoves = world.getPossibleMove(((Positionable) person)).stream().toList();
                if (!possibleMoves.isEmpty()) {
                    moveController.move(((Positionable) person),
                        possibleMoves.get(((int) (possibleMoves.size() * Math.random()))));
                }
                log.debug(world.toString());

                world.getSurroundingActionable(((Positionable) person))
                    .stream().findFirst().ifPresent(actionable -> {
                        actionable.occupy();
                        actionable.getActions(person)
                            .stream().findFirst().ifPresent(action -> {
                                Set<String> choices = action.getChoices();
                                while (!choices.isEmpty()) {
                                    String choice = choices.stream().findAny().get();
                                    action.act(choice).forEach(log::info);
                                    choices = action.getChoices();
                                }
                            });
                        actionable.toFree();
                    });

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread sellerThread = new Thread(() -> {
            while (personThread.isAlive()) {
                if (!((Actionable) seller).isBusy()) {
                    List<Move> possibleMoves = world.getPossibleMove(((Positionable) seller)).stream().toList();
                    if (!possibleMoves.isEmpty()) {
                        moveController.move(((Positionable) seller),
                            possibleMoves.get(((int) (possibleMoves.size() * Math.random()))));
                    }
                    log.debug(world.toString());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        sellerThread.start();
        personThread.start();
        personThread.join();
    }
}
