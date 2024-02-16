import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.Configuration;
import game.GameController;

public class Application {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new Configuration());
        GameController controller = injector.getInstance(GameController.class);
    }
}
