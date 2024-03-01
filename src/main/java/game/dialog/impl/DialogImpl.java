package game.dialog.impl;

import game.dialog.Dialog;
import game.dialog.Question;
import game.npc.Human;
import java.util.Optional;

public class DialogImpl<S extends Human> implements Dialog<S> {

    private Optional<AbstractQuestion<S, ?>> currentQuestion;

    private DialogImpl(AbstractQuestion<S, ?> startQuestion) {
        this.currentQuestion = Optional.of(startQuestion);
    }


    @Override
    public Question<S, ?> iterate() {
        AbstractQuestion<S, ?> current = this.currentQuestion.get();
        this.currentQuestion = current.getChild();
        return current;
    }

    @Override
    public boolean isFinish() {
        return currentQuestion.isEmpty();
    }

    public static class DialogBuilder<S extends Human> {

        private AbstractQuestion<S, ?> startQuestion;
        private AbstractQuestion<S, ?> lastQuestion;

        public DialogBuilder<S> addNext(AbstractQuestion<S, ?> question) {
            if (this.startQuestion != null) {
                this.lastQuestion.setChild(question);
                this.lastQuestion = question;
            } else {
                this.startQuestion = question;
                this.lastQuestion = this.startQuestion;
            }
            return this;
        }

        public Dialog<S> build() {
            return new DialogImpl<>(this.startQuestion);
        }

    }
}
