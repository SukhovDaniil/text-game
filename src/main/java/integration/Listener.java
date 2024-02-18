package integration;

@FunctionalInterface
public interface Listener<I> {

    void onMessage(I message);

}
