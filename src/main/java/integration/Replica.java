package integration;

import java.util.List;

public class Replica extends Printable {

    public Replica(String value) {
        super(value);
    }

    public static Replica of(String value) {
        return new Replica(value);
    }

    public static List<Replica> of(List<String> values) {
        return values.stream().map(Replica::new).toList();
    }

}
