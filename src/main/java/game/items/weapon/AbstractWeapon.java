package game.items.weapon;

import game.items.Item;
import lombok.Getter;

public abstract class AbstractWeapon extends Item {

    @Getter
    protected final int baseDamage;

    protected AbstractWeapon(int baseDamage, String name, int price) {
        super(name, price);
        this.baseDamage = baseDamage;
    }
}
