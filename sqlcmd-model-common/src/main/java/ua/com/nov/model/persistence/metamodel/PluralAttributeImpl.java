package ua.com.nov.model.persistence.metamodel;

import javax.persistence.metamodel.*;
import java.lang.reflect.Member;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Igor Novikov
 * @since 07.12.2017
 */
public abstract class PluralAttributeImpl<X, C, E> extends AbstractAttribute<X, C> implements PluralAttribute<X, C, E> {
    private final Type<E> elementType;

    private PluralAttributeImpl(AbstractManagedType<X> declaringType, Builder<C, E, ?> builder) {
        super(declaringType, builder);
        this.elementType = builder.elementType;
    }

    @Override
    public Type<E> getElementType() {
        return elementType;
    }

    @Override
    public boolean isAssociation() {
        return true;
    }

    @Override
    public boolean isCollection() {
        return true;
    }

    @Override
    public BindableType getBindableType() {
        return BindableType.PLURAL_ATTRIBUTE;
    }

    @Override
    public Class<E> getBindableJavaType() {
        return elementType.getJavaType();
    }

    protected static class Builder<C, E, K> extends AbstractAttribute.Builder<C> {
        private final Type<E> elementType;
        private Type<K> keyType;

        public Builder(String name, Class<C> javaType, Member member, PersistentAttributeType persistentAttributeType,
                       Type<E> attrType) {
            super(name, javaType, member, persistentAttributeType);
            this.elementType = attrType;
        }

        public Builder<C, E, K> keyType(Type<K> keyType) {
            this.keyType = keyType;
            return this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <X> PluralAttributeImpl<X, C, E> build(AbstractManagedType<X> ownerType) {
            //apply strict spec rules first
            if (Map.class.equals(javaType)) {
                final Builder<Map<K, E>, E, K> builder = (Builder<Map<K, E>, E, K>) this;
                return (PluralAttributeImpl<X, C, E>) new MapAttributeImpl<>(ownerType, builder);
            } else if (Set.class.equals(getJavaType())) {
                final Builder<Set<E>, E, ?> builder = (Builder<Set<E>, E, ?>) this;
                return (PluralAttributeImpl<X, C, E>) new SetAttributeImpl<>(ownerType, builder);
            } else if (List.class.equals(getJavaType())) {
                final Builder<List<E>, E, ?> builder = (Builder<List<E>, E, ?>) this;
                return (PluralAttributeImpl<X, C, E>) new ListAttributeImpl<>(ownerType, builder);
            } else if (Collection.class.equals(getJavaType())) {
                final Builder<Collection<E>, E, ?> builder = (Builder<Collection<E>, E, ?>) this;
                return (PluralAttributeImpl<X, C, E>) new CollectionAttributeImpl<>(ownerType, builder);
            }

            //apply loose rules
            if (getJavaType().isArray()) {
                final Builder<List<E>, E, ?> builder = (Builder<List<E>, E, ?>) this;
                return (PluralAttributeImpl<X, C, E>) new ListAttributeImpl<>(ownerType, builder);
            }

            if (Map.class.isAssignableFrom(getJavaType())) {
                final Builder<Map<K, E>, E, K> builder = (Builder<Map<K, E>, E, K>) this;
                return (PluralAttributeImpl<X, C, E>) new MapAttributeImpl<>(ownerType, builder);
            } else if (Set.class.isAssignableFrom(getJavaType())) {
                final Builder<Set<E>, E, ?> builder = (Builder<Set<E>, E, ?>) this;
                return (PluralAttributeImpl<X, C, E>) new SetAttributeImpl<>(ownerType, builder);
            } else if (List.class.isAssignableFrom(getJavaType())) {
                final Builder<List<E>, E, ?> builder = (Builder<List<E>, E, ?>) this;
                return (PluralAttributeImpl<X, C, E>) new ListAttributeImpl<>(ownerType, builder);
            } else if (Collection.class.isAssignableFrom(getJavaType())) {
                final Builder<Collection<E>, E, ?> builder = (Builder<Collection<E>, E, ?>) this;
                return (PluralAttributeImpl<X, C, E>) new CollectionAttributeImpl<>(ownerType, builder);
            }
            throw new UnsupportedOperationException("Unkown collection: " + getJavaType());
        }
    }

    static class MapAttributeImpl<X, K, V> extends PluralAttributeImpl<X, Map<K, V>, V> implements MapAttribute<X, K, V> {
        private final Type<K> keyType;

        MapAttributeImpl(AbstractManagedType<X> ownerType, PluralAttributeImpl.Builder<Map<K, V>, V, K> builder) {
            super(ownerType, builder);
            this.keyType = builder.keyType;
        }

        @Override
        public CollectionType getCollectionType() {
            return CollectionType.MAP;
        }

        @Override
        public Class<K> getKeyJavaType() {
            return keyType.getJavaType();
        }

        @Override
        public Type<K> getKeyType() {
            return keyType;
        }
    }

    static class SetAttributeImpl<X, E> extends PluralAttributeImpl<X, Set<E>, E> implements SetAttribute<X, E> {
        SetAttributeImpl(AbstractManagedType<X> ownerType, PluralAttributeImpl.Builder<Set<E>, E, ?> builder) {
            super(ownerType, builder);
        }

        @Override
        public CollectionType getCollectionType() {
            return CollectionType.SET;
        }
    }

    static class CollectionAttributeImpl<X, E> extends PluralAttributeImpl<X, Collection<E>, E> implements CollectionAttribute<X, E> {
        CollectionAttributeImpl(AbstractManagedType<X> ownerType, PluralAttributeImpl.Builder<Collection<E>, E, ?> builder) {
            super(ownerType, builder);
        }

        @Override
        public CollectionType getCollectionType() {
            return CollectionType.COLLECTION;
        }
    }

    static class ListAttributeImpl<X, E> extends PluralAttributeImpl<X, List<E>, E> implements ListAttribute<X, E> {
        ListAttributeImpl(AbstractManagedType<X> ownerType, PluralAttributeImpl.Builder<List<E>, E, ?> builder) {
            super(ownerType, builder);
        }

        @Override
        public CollectionType getCollectionType() {
            return CollectionType.LIST;
        }
    }


}
