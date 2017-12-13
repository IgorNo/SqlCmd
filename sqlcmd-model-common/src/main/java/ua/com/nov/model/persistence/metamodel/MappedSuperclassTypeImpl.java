package ua.com.nov.model.persistence.metamodel;

import javax.persistence.metamodel.MappedSuperclassType;

/**
 * Defines implementation of the JPA {@link MappedSuperclassType} contract.
 *
 * @author Igor Novikov
 * @since 04.12.2017
 */
public class MappedSuperclassTypeImpl<X> extends AbstractIdentifiableType<X> implements MappedSuperclassType<X> {
    public MappedSuperclassTypeImpl(Builder<X> builder) {
        super(builder);
    }

    @Override
    public PersistenceType getPersistenceType() {
        return PersistenceType.MAPPED_SUPERCLASS;
    }
}
