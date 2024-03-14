import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.Configuration;
import game.Game;

public class Application {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new Configuration());
        Game controller = injector.getInstance(Game.class);
    }
}
