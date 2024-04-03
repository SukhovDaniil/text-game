package game.word.impl;

import game.word.Positionable;
import game.word.World;
import java.util.HashSet;
import java.util.Set;
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
    public boolean possibleToMove(Position position) {
        return position.getX() >= 0 && position.getX() < xSize
            && position.getY() >= 0 && position.getY() < ySize
            && objects.stream().noneMatch(o -> o.getPosition().equals(position));
    }

    public void addPositionable(@NotNull Positionable positionable) {
        this.objects.add(positionable);
    }

    public void removePositionable(@NotNull Positionable positionable) {
        this.objects.remove(positionable);
    }

    public Set<Positionable> getPositionable() {
        return new HashSet<>(this.objects);
    }
}
