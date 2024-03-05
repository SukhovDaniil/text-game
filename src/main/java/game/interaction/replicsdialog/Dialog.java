package game.interaction.replicsdialog;

import static java.lang.String.join;
import game.items.Item;
import game.items.potion.HealthPotion;
import game.items.weapon.Sword;
import game.npc.humans.Seller;
import game.word.Person;
import game.word.impl.PersonImpl;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;


public class Dialog {
    //Класс оставлен для целей тестирования

    public static void main(String[] args) {
        Seller testSeller = new Seller();
        testSeller.addItem(new Sword(10, "меч-леденец", 1));
        testSeller.addItem(new HealthPotion("вода жизни", 1000));
        testSeller.addItem(new Sword(66, "меч-хуец", 69));
        Person person = new PersonImpl();

        Function<Item, Replica> itemPriceReplica = item -> new Replica() {
            @Override
            public void sayAs(DialogNode dialog) {

            }

            @Override
            public String getDescription() {
                return "[%s] стоит [%s]".formatted(item.getName(), item.getPrice());
            }
        };

        Function<Item, Replica> whatIsItemPriceReplica = item -> new Replica() {
            @Override
            public void sayAs(DialogNode dialog) {
                dialog.pasteChildren(Set.of(new DialogNode(itemPriceReplica.apply(item), person)));
            }

            @Override
            public String getDescription() {
                return "Сколько стоит [%s]?".formatted(item.getName());
            }
        };

        Supplier<Replica> itemsInStockReplica = () -> new Replica() {
            @Override
            public void sayAs(DialogNode dialog) {
                dialog.pasteChildren(testSeller.getItems().stream()
                    .map(item -> new DialogNode(whatIsItemPriceReplica.apply(item), testSeller))
                    .collect(Collectors.toSet()));
            }

            @Override
            public String getDescription() {
                List<String> itemsNames = testSeller.getItems().stream().map(Item::getName).toList();
                return """
                    В наличии:
                        %s
                    """.formatted(join("\n\t", itemsNames));
            }
        };

        DialogNode showMeItems = new DialogNode(new Replica() {
            @Override
            public void sayAs(DialogNode dialog) {
                List<Item> items = testSeller.getItems();
                DialogNode answer = new DialogNode(itemsInStockReplica.get(), person);
                dialog.pasteChildren(Set.of(answer));
            }

            @Override
            public String getDescription() {
                return "Покажи товар";
            }
        },
            testSeller);

        Scanner scanner = new Scanner(System.in);
        DialogController dialogController = new DialogController(testSeller, Set.of(showMeItems));
        while (!dialogController.getChoices().isEmpty()) {
            String choicesString = "Доступен выбор:\n" + join("\n", dialogController.getChoices());
            System.out.println(choicesString);
            String choiceString = scanner.nextLine();
            if (choiceString.equalsIgnoreCase("exit")) {
                break;
            }

            List<String> act = dialogController.act(choiceString);

            act.forEach(System.out::println);
        }

    }

}
