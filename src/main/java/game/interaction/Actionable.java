package game.interaction;

import java.util.Set;

public interface Actionable {

    Set<ActionController> getActions(Object to);

}
