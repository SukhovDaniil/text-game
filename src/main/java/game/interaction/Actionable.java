package game.interaction;

import java.util.Set;

public interface Actionable {

    Set<ActionController> getActions(Object to);

    boolean isBusy();

    void occupy();

    void toFree();

}
