package game.npc.humans.abstractions;

import static java.lang.String.join;
import game.interaction.ActionController;
import game.interaction.Actionable;
import game.interaction.replicsdialog.DialogController;
import game.interaction.replicsdialog.DialogNode;
import game.items.Item;
import game.npc.Human;
import game.word.Person;
import game.word.Positionable;
import game.word.impl.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.Getter;

public abstract class AbstractSeller implements Human, Actionable, Positionable {

    @Getter
    private Position position;

    private boolean isBusy;

    @Override
    public Position setPosition(Position position) {
        this.position = position;
        return this.position;
    }

    @Override
    public Set<ActionController> getActions(Object to) {
        return Set.of(generateSellDialog(((Person) to)));
    }

    @Getter
    protected final List<Item> items;

    protected AbstractSeller() {
        this.items = new ArrayList<>();
    }

    public AbstractSeller addItem(Item item) {
        this.items.add(item);
        return this;
    }

    public AbstractSeller addItems(List<Item> items) {
        this.items.addAll(items);
        return this;
    }

    public abstract int getPrice(Item item);

    private DialogController generateSellDialog(Person person) {
        Function<Item, DialogNode> itemPriceReplica = item -> new DialogNode(person) {
            @Override
            public String getDescription() {
                return "[%s] стоит [%s]".formatted(item.getName(), item.getPrice());
            }
        };

        Function<Item, DialogNode> whatIsItemPriceReplica = item -> new DialogNode(this) {
            @Override
            public String getDescription() {
                return "Сколько стоит [%s]?".formatted(item.getName());
            }

            @Override
            public String say() {
                this.pasteChildren(Set.of(itemPriceReplica.apply(item)));
                return getDescription();
            }
        };

        Supplier<DialogNode> itemsInStockReplica = () -> new DialogNode(person) {

            @Override
            public String getDescription() {
                List<String> itemsNames = items.stream().map(Item::getName).toList();
                return """
                    В наличии:
                        %s
                    """.formatted(join("\n\t", itemsNames));
            }

            @Override
            public String say() {
                this.pasteChildren(items.stream()
                    .map(whatIsItemPriceReplica)
                    .collect(Collectors.toSet()));
                return getDescription();
            }
        };

        DialogNode showMeItems = new DialogNode(this) {
            @Override
            public String say() {
                this.pasteChildren(Set.of(itemsInStockReplica.get()));
                return getDescription();
            }

            @Override
            public String getDescription() {
                return "Покажи товар";
            }

        };

        return new DialogController(this, Set.of(showMeItems));
    }

    @Override
    public boolean isBusy() {
        return this.isBusy;
    }

    @Override
    public void occupy() {
        this.isBusy = true;
    }

    @Override
    public void toFree() {
        this.isBusy = false;
    }
}
