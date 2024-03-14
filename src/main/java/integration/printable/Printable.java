package integration.printable;

public abstract class Printable {

    private final String value;

    public Printable(String value) {
        this.value = value;
    }

    public String get() {
        return this.value;
    }
}
