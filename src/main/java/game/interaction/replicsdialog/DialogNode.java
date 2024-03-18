package game.interaction.replicsdialog;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;

public abstract class DialogNode {

    @Getter
    private Set<DialogNode> children = new HashSet<>();

    private final Object interviewer;

    public DialogNode(Object interviewer) {
        this.interviewer = interviewer;
    }

    public DialogNode addChild(DialogNode child) {
        this.children.add(child); //todo null-check?
        return this;
    }

    //по сути - вставка поддерева
    public DialogNode pasteChildren(Set<DialogNode> pastedChildren) {
        if (this.children.isEmpty()) {
            this.children = pastedChildren;
        } else {
            pastedChildren.forEach(child -> child.pasteChildren(this.children)); //сделать через глубокое копирование
            this.children = pastedChildren;
        }
        return this;
    }

    public String say() {
        return getDescription();
    }

    public abstract String getDescription();

    public boolean isTo(Object to) {
        return this.interviewer == to;
    }

}
