package ua.com.nov.model.persistence.metamodel;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.Type;
import java.lang.reflect.Member;

/**
 * @author Igor Novikov
 * @since 07.12.2017
 */
public class SingularAttributeImpl<X, Y> extends AbstractAttribute<X, Y> implements SingularAttribute<X, Y> {
    private final boolean isOptional;
    private final Type<Y> attributeType;

    private SingularAttributeImpl(AbstractManagedType<X> declaringType, Builder<Y> builder) {
        super(declaringType, builder);
        this.isOptional = builder.isOptional;
        this.attributeType = builder.attributeType;
    }

    @Override
    public boolean isId() {
        return this.getClass() == SingularAttributeImpl.Identifier.class;
    }

    @Override
    public boolean isVersion() {
        return this.getClass() == SingularAttributeImpl.Identifier.class;
    }

    @Override
    public boolean isOptional() {
        return isOptional;
    }

    @Override
    public Type<Y> getType() {
        return attributeType;
    }

    @Override
    public boolean isAssociation() {
        return getPersistentAttributeType() == PersistentAttributeType.MANY_TO_ONE
                || getPersistentAttributeType() == PersistentAttributeType.ONE_TO_ONE;
    }

    @Override
    public boolean isCollection() {
        return false;
    }

    @Override
    public BindableType getBindableType() {
        return BindableType.SINGULAR_ATTRIBUTE;
    }

    @Override
    public Class<Y> getBindableJavaType() {
        return attributeType.getJavaType();
    }

    /**
     * Subclass used to simply instantiation of singular attributes representing an entity's
     * identifier.
     */
    public static class Identifier<X, Y> extends SingularAttributeImpl<X, Y> {
        private Identifier(AbstractManagedType<X> declaringType, SingularAttributeImpl.Builder<Y> builder) {
            super(declaringType, builder);
        }

        public static class Builder<Y> extends SingularAttributeImpl.Builder<Y> {
            public Builder(String name, Class<Y> javaType, Member member, PersistentAttributeType persistentAttributeType,
                           boolean isOptional) {
                super(name, javaType, member, persistentAttributeType, isOptional);
            }

            @Override
            public Builder attributeType(Type<Y> attributeType) {
                super.attributeType(attributeType);
                return this;
            }

            @Override
            public <X> Identifier<X, Y> build(AbstractManagedType<X> declaringType) {
                return new Identifier<>(declaringType, this);
            }
        }
    }

    /**
     * Subclass used to simply instantiation of singular attributes representing an entity's
     * version.
     */
    public static class Version<X, Y> extends SingularAttributeImpl<X, Y> {
        private Version(AbstractManagedType<X> declaringType, SingularAttributeImpl.Builder<Y> builder) {
            super(declaringType, builder);
        }

        public static class Builder<Y> extends SingularAttributeImpl.Builder<Y> {
            public Builder(String name, Class<Y> javaType, Member member, PersistentAttributeType persistentAttributeType,
                           boolean isOptional) {
                super(name, javaType, member, persistentAttributeType, isOptional);
            }

            @Override
            public Builder attributeType(Type<Y> attributeType) {
                super.attributeType(attributeType);
                return this;
            }

            @Override
            public <X> Version<X, Y> build(AbstractManagedType<X> declaringType) {
                return new Version<>(declaringType, this);
            }
        }

    }

    public static class Builder<Y> extends AbstractAttribute.Builder<Y> {
        private final boolean isOptional;
        private Type<Y> attributeType;

        public Builder(String name, Class<Y> javaType, Member member, PersistentAttributeType persistentAttributeType,
                       boolean isOptional) {
            super(name, javaType, member, persistentAttributeType);
            this.isOptional = isOptional;
        }

        public Builder attributeType(Type<Y> attributeType) {
            this.attributeType = attributeType;
            return this;
        }

        @Override
        public <X> SingularAttributeImpl<X, Y> build(AbstractManagedType<X> declaringType) {
            return new SingularAttributeImpl<>(declaringType, this);
        }

    }
}
