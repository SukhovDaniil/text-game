package game.npc.humans.abstractions;

import game.items.Item;
import game.npc.Human;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public abstract class AbstractSeller implements Human {

    @Getter
    protected final List<Item> items;

    protected AbstractSeller() {
        this.items = new ArrayList<>();
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    public void addItems(List<Item> items) {
        this.items.addAll(items);
    }

    public abstract int getPrice(Item item);
}
