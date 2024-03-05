package game.interaction;

import java.util.List;
import java.util.Set;

public interface ActionController {

    List<String> act(String action);

    String getDescription();

    Set<String> getChoices();

}
