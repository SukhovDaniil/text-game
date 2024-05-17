package data;

public interface GameProvider {

    void deleteUser(long userId);

    game.GameController getActorForUser(long userId);

}
