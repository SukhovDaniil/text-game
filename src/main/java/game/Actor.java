package game;

import game.interaction.ActionController;
import game.items.potion.HealthPotion;
import game.items.weapon.Sword;
import game.npc.humans.Seller;
import game.npc.humans.abstractions.AbstractSeller;
import game.word.Person;
import game.word.Word;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Actor implements ActionController {

    private final Person person;
    private final Word word;

    private AbstractSeller seller = new Seller()
        .addItem(new Sword(10, "меч-леденец", 1))
        .addItem(new HealthPotion("вода жизни", 1000))
        .addItem(new Sword(66, "меч", 69));


    private Map<String, ActionController> availableChoices = new HashMap<>();
    private ActionController currentContext;

    @Override
    public List<String> act(String action) {
        if (currentContext == null) {
            if (availableChoices.containsKey(action)) {
                currentContext = availableChoices.get(action);
                return List.of();
            } else {
                throw new IllegalArgumentException("Неизвестная операция: %s".formatted(action));
            }
        }
        return currentContext.act(action);
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public Set<String> getChoices() {
        if (currentContext == null) {
            this.availableChoices = seller.getActions(person).stream()
                .collect(Collectors.toMap(ActionController::getDescription, c -> c));
            return availableChoices.keySet();
        }
        return currentContext.getChoices();
    }
}
