package ua.com.nov.model.persistence.metamodel;

import javax.persistence.metamodel.EntityType;

/**
 * Defines implementation of the JPA {@link EntityType} contract.
 *
 * @author Igor Novikov
 * @since 03.12.2017
 */
public abstract class EntityTypeImpl<X> extends AbstractIdentifiableType<X> implements EntityType<X> {
    private final String entityName;

    @SuppressWarnings("unchecked")
    public EntityTypeImpl(Builder<X> builder) {
        super(builder);
        this.entityName = builder.entityName;
    }

    @Override
    public String getName() {
        return entityName;
    }

    @Override
    public BindableType getBindableType() {
        return BindableType.ENTITY_TYPE;
    }

    @Override
    public Class<X> getBindableJavaType() {
        return getJavaType();
    }

    @Override
    public PersistenceType getPersistenceType() {
        return PersistenceType.ENTITY;
    }

    public abstract static class Builder<X> extends AbstractIdentifiableType.Builder<X> {
        private final String entityName;

        public Builder(Class<X> domainClass, String entityName) {
            super(domainClass);
            this.entityName = entityName;
        }

        @Override
        protected Builder superType(AbstractManagedType<? super X> superType) {
            super.superType(superType);
            return this;
        }
    }
}
