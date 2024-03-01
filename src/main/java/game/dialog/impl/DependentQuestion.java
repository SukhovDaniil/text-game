package game.dialog.impl;

import game.npc.Human;
import java.util.function.BiFunction;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;

/**
 * Зависимый вопрос. Т.е. данный вопрос зависит от ответа на вопрос-родитель.
 *
 * @param <S> тип объекта-собеседника.
 * @param <A> тип ответа.
 * @param <D> тип ответа на вопрос-родитель.
 */
@RequiredArgsConstructor
public class DependentQuestion<S extends Human, A, D> extends AbstractQuestion<S, A> {

    private final BiFunction<S, D, A> answerSupplier;
    private final Function<D, String> questionStringSupplier;
    private final BiFunction<D, A, String> answerStringSupplier;
    private D parentAnswer;

    @Override
    public String getQuestionString() {
        return questionStringSupplier.apply(parentAnswer);
    }

    @Override
    public String getAnswerString() {
        return answerStringSupplier.apply(this.parentAnswer, this.answer);
    }

    @Override
    protected A getAnswerFrom(S speaker) {
        return answerSupplier.apply(speaker, parentAnswer);
    }


    @Override
    public void notify(Object notification) {
        if (notification != null) {
            this.setParentAnswer(((D) notification));
        }
    }

    private void setParentAnswer(D parentAnswer) {
        this.parentAnswer = parentAnswer;
    }
}
