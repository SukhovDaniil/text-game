package game.items;


import lombok.Getter;

public abstract class Item {

    @Getter
    protected final String name;
    @Getter
    protected final Integer price;

    protected Item(String name, Integer price) {
        this.name = name;
        this.price = price;
    }
}
