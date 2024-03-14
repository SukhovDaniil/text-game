package integration;

import java.util.List;
import java.util.Set;

public class Choice extends Printable {

    public Choice(String value) {
        super(value);
    }

    public static Choice of(String value) {
        return new Choice(value);
    }

    public static List<Choice> of(Set<String> values) {
        return values.stream().map(Choice::new).toList();
    }


}
