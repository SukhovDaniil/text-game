package game.dialog.impl;

import game.npc.Human;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;

/**
 * Независимый вопрос. Т.е. класс описывает вопрос, который не зависит от ответа на другой вопрос.
 *
 * @param <S> тип объекта-собеседника.
 * @param <A> тип ответа.
 */
@RequiredArgsConstructor
public class IndependentQuestion<S extends Human, A> extends AbstractQuestion<S, A> {

    private final Function<S, A> answerSupplier;

    private final Supplier<String> questionStringSupplier;

    private final Function<A, String> answerStringSupplier;

    @Override
    public String getQuestionString() {
        return questionStringSupplier.get();
    }

    @Override
    public String getAnswerString() {
        return answerStringSupplier.apply(this.answer);
    }

    @Override
    protected A getAnswerFrom(S speaker) {
        return answerSupplier.apply(speaker);
    }

    @Override
    public void notify(Object notification) {

    }
}
