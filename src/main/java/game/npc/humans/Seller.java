package game.npc.humans;

import game.items.Item;
import game.npc.humans.abstractions.AbstractSeller;

public class Seller extends AbstractSeller {

    @Override
    public int getPrice(Item item) {
        if (!items.contains(item)) {
            //Если нет такого айтема в инвентаре - цена покупки
            return (int) (item.getPrice() * 0.8);
        }
        //цена продажи
        return (int) (item.getPrice() * 1.2);
    }

    @Override
    public String name() {
        return "Торговец";
    }

    @Override
    public String toString() {
        return name();
    }
}
