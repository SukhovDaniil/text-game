package game.interaction.replicsdialog;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;

public class DialogNode {

    private final Object interviewer;
    @Getter
    private final Replica replica;
    @Getter
    private Set<DialogNode> children = new HashSet<>();

    public DialogNode(Replica replica, Object interviewer) {
        this.replica = replica;
        this.interviewer = interviewer;
    }

    public DialogNode addChild(DialogNode child) {
        this.children.add(child); //todo null-check?
        return this;
    }

    //по сути - вставка поддерева
    DialogNode pasteChildren(Set<DialogNode> pastedChildren) {
        if (this.children.isEmpty()) {
            this.children = pastedChildren;
        } else {
            pastedChildren.forEach(child -> child.pasteChildren(this.children)); //сделать через глубокое копирование
            this.children = pastedChildren;
        }
        return this;
    }

    public String say() {
        this.replica.sayAs(this);
        return this.replica.getDescription();
    }

    public boolean isTo(Object to) {
        return this.interviewer == to;
    }

}
