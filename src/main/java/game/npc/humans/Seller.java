package game.npc.humans;

import game.dialog.Dialog;
import game.dialog.Question;
import game.dialog.impl.DependentQuestion;
import game.dialog.impl.DialogImpl;
import game.dialog.impl.IndependentQuestion;
import game.items.Item;
import game.items.potion.HealthPotion;
import game.items.weapon.Sword;
import game.npc.humans.abstractions.AbstractSeller;
import java.util.List;

public class Seller extends AbstractSeller {

    public static void main(String[] args) {
        Seller testSeller = new Seller();
        testSeller.addItem(new Sword(10, "меч-леденец", 1));
        testSeller.addItem(new HealthPotion("вода жизни", 1000));

        Dialog<Seller> dialog = new DialogImpl.DialogBuilder<Seller>()
            .addNext(
                new IndependentQuestion<>(
                    AbstractSeller::getItems,
                    () -> "Покажи товар",
                    answer -> "В наличии:\n\t" + String.join("\n\t",
                        answer.stream().map(i -> "[%s]".formatted(i.getName())).toList()))
            )
            .addNext(
                new DependentQuestion<Seller, Integer, List<Item>>(
                    (seller, itemList) -> seller.getPrice(itemList.get(0)),
                    itemList -> "Сколько стоит [%s]".formatted(itemList.get(0).getName()),
                    (itemList, price) -> "[%s] стоит %s рубликов".formatted(itemList.get(0).getName(), price)
                )
            )
            .build();

        while (dialog.notFinish()) {
            Question<Seller, ?> question = dialog.iterate();
            System.out.println("%s << %s".formatted(testSeller.name(), question.getQuestionString()));
            question.askTo(testSeller);
            System.out.println("%s >> %s".formatted(testSeller.name(), question.getAnswerString()));
        }
    }

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
}
