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

    public static void main(String[] args) {
        MoveController moveController = new MoveController();
        World world = new WorldImpl(100, 100);
        Person person = new PersonImpl();
        Human seller = new Seller();
        Monster smile = new Smile("Слизняк", 0L);

        world.setOnPosition(((Positionable) person), new Position(10, 7));
        world.setOnPosition(((Positionable) seller), new Position(3, 5));
        world.setOnPosition(((Positionable) smile), new Position(90, 38));
        log.debug(world.toString());

        for (int i = 0; i < 100; i++) {
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
        }
    }
}
