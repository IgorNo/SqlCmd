package ua.com.nov.model.domain.supertype;

import org.springframework.util.Assert;

import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Superclass to denote a composite primary key that is an embeddable class.
 * The embeddable class must be annotated as Embeddable
 *
 * @param <ID> the type of the identifier
 * @author Igor Novikov
 * @since 05.11.2017
 */
@MappedSuperclass
public abstract class EmbeddedIdEntity<ID extends Serializable> extends PersistentObject<ID> {

    private static final String ID_MUST_NOT_BE_NULL = "The given id must not be null!";

    @EmbeddedId
    private ID id;

    /**
     * Create a new PersistentObject instance with the given identifier and the {@link NewState} state.
     *
     * @param id must not be {@literal null}.
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    protected EmbeddedIdEntity(ID id) {
        this(id, NewState.getInstance());
    }

    /**
     * Create a new EmbeddedIdEntity instance with the given state.
     *
     * @param id    object identifier
     * @param state state of the object
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     * @throws IllegalArgumentException if {@code state} is {@literal null}.
     */
    protected EmbeddedIdEntity(ID id, PObjectState state) {
        super(state);
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        this.id = id;
    }

    @Override
    public ID getId() {
        return id;
    }

    /**
     * Set the given object identifier and set object state in {@link OldCleanState}.
     * This method is used by DAO or Repository classes.
     *
     * @param id object identifier
     */
    protected final void setId(ID id) {
        this.id = id;
        setState(OldCleanState.getInstance());
    }

}
