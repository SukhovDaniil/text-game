package game.interaction.replicsdialog;


public interface Replica {

    //todo избавиться от прокидывания DialogNode
    void sayAs(DialogNode dialog);

    String getDescription();
}
