import game.interaction.move.Move;
import game.interaction.move.MoveController;
import game.npc.Human;
import game.npc.Monster;
import game.npc.humans.Seller;
import game.npc.monsters.impl.Smile;
import game.word.Person;
import game.word.Positionable;
import game.word.World;
import game.word.impl.PersonImpl;
import game.word.impl.Position;
import game.word.impl.WorldImpl;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Multi {

    public static void main(String[] args) throws InterruptedException {
        MoveController moveController = new MoveController();
        World world = new WorldImpl(100, 100);
        Person person = new PersonImpl();
        Human seller = new Seller();
        Monster smile = new Smile("Слизняк", 0L);

        world.setOnPosition(((Positionable) person), new Position(10, 7));
        world.setOnPosition(((Positionable) seller), new Position(3, 5));
        world.setOnPosition(((Positionable) smile), new Position(90, 38));
        log.debug(world.toString());

        Thread personThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    List<Move> possibleMoves = world.getPossibleMove(((Positionable) person)).stream().toList();
                    if (!possibleMoves.isEmpty()) {
                        moveController.move(((Positionable) person),
                            possibleMoves.get(((int) (possibleMoves.size() * Math.random()))));
                    }
                    log.debug(world.toString());
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Thread sellerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    List<Move> possibleMoves = world.getPossibleMove(((Positionable) seller)).stream().toList();
                    if (!possibleMoves.isEmpty()) {
                        moveController.move(((Positionable) seller),
                            possibleMoves.get(((int) (possibleMoves.size() * Math.random()))));
                    }
                    log.debug(world.toString());
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Thread smileThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    List<Move> possibleMoves = world.getPossibleMove(((Positionable) smile)).stream().toList();
                    if (!possibleMoves.isEmpty()) {
                        moveController.move(((Positionable) smile),
                            possibleMoves.get(((int) (possibleMoves.size() * Math.random()))));
                    }
                    log.debug(world.toString());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        personThread.start();
        Thread.sleep(1000);
        sellerThread.start();
        Thread.sleep(1000);
        smileThread.start();

/*        for (int i = 0; i < 100; i++) {
            world.getPositionable().forEach(
                positionable -> {
                    List<Move> possibleMoves = world.getPossibleMove(positionable).stream().toList();
                    if (!possibleMoves.isEmpty()) {
                        moveController.move(positionable,
                            possibleMoves.get(((int) (possibleMoves.size() * Math.random()))));
                    }
                }
            );
            log.debug(world.toString());
        }*/
    }
}
