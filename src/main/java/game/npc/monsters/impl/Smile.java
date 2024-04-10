package game.npc.monsters.impl;

import game.npc.monsters.AbstractMonster;

public class Smile extends AbstractMonster {

    public Smile(String name, long id) {
        super(name, id);
    }

    @Override
    public String toString() {
        return "Слизняк Генадий";
    }
}
