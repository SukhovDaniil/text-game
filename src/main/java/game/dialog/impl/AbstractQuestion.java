package game.dialog.impl;

import game.dialog.Question;
import game.npc.Human;
import java.util.Optional;

public abstract class AbstractQuestion<S extends Human, A> implements Question<S, A> {

    protected A answer;

    private AbstractQuestion<S, ?> childQuestion;

    @Override
    public void askTo(S speaker) {
        this.answer = getAnswerFrom(speaker);
        if (childQuestion != null) {
            childQuestion.notify(answer);
        }
    }

    public Optional<AbstractQuestion<S, ?>> getChild() {
        return Optional.ofNullable(this.childQuestion);
    }

    public void setChild(AbstractQuestion<S, ?> child) {
        this.childQuestion = child;
    }

    abstract protected A getAnswerFrom(S speaker);

    abstract protected void notify(Object notification);
}
