package game.word.impl;

import game.interaction.Actionable;
import game.interaction.move.Direction;
import game.interaction.move.Move;
import game.word.Positionable;
import game.word.World;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class WorldImpl implements World {

    @Getter
    private final int xSize;
    @Getter
    private final int ySize;

    private final Set<Positionable> objects = new HashSet<>();

    @Override
    public boolean possibleSetTo(Position position) {
        return position.getX() >= 0 && position.getX() < xSize
            && position.getY() >= 0 && position.getY() < ySize
            && objects.stream().noneMatch(o -> o.getPosition().equals(position));
    }

    public void setOnPosition(@NotNull Positionable positionable, Position position) {
        positionable.setPosition(position);
        this.objects.add(positionable);
    }

    public void removePositionable(@NotNull Positionable positionable) {
        this.objects.remove(positionable);
    }

    public Set<Positionable> getPositionable() {
        return new HashSet<>(this.objects);
    }

    @Override
    public Set<Actionable> getSurroundingActionable(Positionable positionable) {
        return this.getPositionable().stream()
            .filter(p -> Math.abs(positionable.getPosition().getX() - p.getPosition().getX()) <= 1
                && Math.abs(positionable.getPosition().getY() - p.getPosition().getY()) <= 1)
            .filter(p -> p instanceof Actionable)
            .map(p -> ((Actionable) p))
            .collect(Collectors.toSet());
    }

    @Override
    public Set<Move> getPossibleMove(Positionable positionable) {
        if (!this.getPositionable().contains(positionable)) {
            return Collections.emptySet();
        }

        Set<Move> possibleMoves = new HashSet<>();
        Position currentPosition = positionable.getPosition();
        for (Direction direction : Direction.values()) {
            Move move = new Move(direction, 1);
            Position newPosition = currentPosition.shift(move);
            if (possibleSetTo(newPosition)) {
                possibleMoves.add(move);
            }
        }

        return possibleMoves;
    }

    @Override
    public String toString() {
        return "[" + String.join("; ", this.objects.stream().map(p -> "%s: %s".formatted(p, p.getPosition())).toList())
            + "]";
    }
}
