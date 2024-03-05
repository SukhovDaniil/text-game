package game.interaction.replicsdialog;

import static java.util.stream.Collectors.toMap;
import game.interaction.ActionController;
import game.npc.Human;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DialogController implements ActionController {

    private final Human interviewer;
    //private final Person from;
    private Map<String, DialogNode> availableChoices;

    public DialogController(Human interviewer, Set<DialogNode> dialogs) {
        //this.from = from;
        this.interviewer = interviewer;
        updateChoices(dialogs);
    }

    @Override
    public List<String> act(String action) {
        if (!availableChoices.containsKey(action)) {
            throw new IllegalArgumentException("Unknown action: [%s]".formatted(action));
        }
        DialogNode choice = availableChoices.get(action);
        List<String> result = new ArrayList<>();
        result.add(
            (choice.isTo(interviewer) ? "[%s] << %s" : "[%s] >> %s").formatted(interviewer.name(), choice.say()));
        updateChoices(choice.getChildren());

        if (this.availableChoices.isEmpty()) {
            return result;
        }

        if (hasAutoActableChoices()) {
            result.addAll(
                act(
                    this.availableChoices.entrySet().stream()
                        .findFirst().orElseThrow().getValue().getReplica().getDescription()
                )
            );
        }
        return result;
    }

    @Override
    public String getDescription() {
        return "Поговорить с [%s]".formatted(interviewer.name());
    }

    @Override
    public Set<String> getChoices() {
        return availableChoices.keySet();
    }

    private void updateChoices(Set<DialogNode> newChoices) {
        this.availableChoices = newChoices.stream().collect(toMap(d -> d.getReplica().getDescription(), d -> d));
    }

    private boolean hasAutoActableChoices() {
        //Если доступна одна реплика от собеседника - автоматически проговариваем её
        return this.availableChoices.size() == 1
            && this.availableChoices.entrySet().stream().noneMatch(e -> e.getValue().isTo(this.interviewer));
    }
}
