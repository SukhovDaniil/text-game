package game.dialog;

import game.npc.Human;

/**
 * Интерфейс вопроса.
 *
 * @param <S> тип объекта-собеседника.
 * @param <A> тип ответа.
 */
public interface Question<S extends Human, A> {

    String getQuestionString();

    String getAnswerString();

    void askTo(S speaker);
}
