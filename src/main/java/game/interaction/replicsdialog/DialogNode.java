package game.interaction.replicsdialog;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;

public abstract class DialogNode {

    private final Object interviewer;
    @Getter
    private Set<DialogNode> children = new HashSet<>();

    public DialogNode(Object interviewer) {
        this.interviewer = interviewer;
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
