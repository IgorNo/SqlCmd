package ua.com.nov.model.domain.supertype;

/**
 * This class is used to mark a deleted from the persistence storage object.
 */
public class DeletedState extends PObjectState {

    private static DeletedState instance = new DeletedState();

    private DeletedState() {
    }

    public static DeletedState getInstance() {
        return instance;
    }
}
