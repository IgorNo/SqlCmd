package ua.com.nov.model.persistence.metamodel;

import org.springframework.util.Assert;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;
import java.lang.reflect.Member;

/**
 * Models the commonality of the JPA {@link Attribute} hierarchy.
 *
 * @author Igor Novikov
 * @since 20.11.2017
 */
public abstract class AbstractAttribute<X, Y> implements Attribute<X, Y> {
    private final String name;
    private final Class<Y> javaType;
    private final AbstractManagedType<X> declaringType;
    private final PersistentAttributeType persistentAttributeType;
    private transient Member member;

    protected AbstractAttribute(AbstractManagedType<X> declaringType, Builder<Y> builder) {
        this.declaringType = declaringType;
        this.name = builder.name;
        this.javaType = builder.javaType;
        this.member = builder.member;
        this.persistentAttributeType = builder.persistentAttributeType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ManagedType<X> getDeclaringType() {
        return declaringType;
    }

    @Override
    public Class<Y> getJavaType() {
        return javaType;
    }

    @Override
    public Member getJavaMember() {
        return member;
    }

    @Override
    public PersistentAttributeType getPersistentAttributeType() {
        return persistentAttributeType;
    }

//    private Field getFieldFor(String fieldName, Class<Y> javaType) {
//        try {
//            Field field = javaType.getDeclaredField(fieldName);
//            field.setAccessible(true);
//            getJavaMember().getDeclaringClass();
//            return field;
//        } catch (NoSuchFieldException e) {
//            throw new MappingException("Unable to set up field: " + fieldName, e);
//        }
//    }

    protected abstract static class Builder<Y> {
        protected final Class<Y> javaType;
        private final String name;
        private final PersistentAttributeType persistentAttributeType;
        private transient Member member;

        public Builder(String name, Class<Y> javaType, Member member, PersistentAttributeType persistentAttributeType) {
            Assert.notNull(name);
            Assert.notNull(javaType);
            Assert.notNull(member);
            Assert.notNull(persistentAttributeType);
            this.name = name;
            this.javaType = javaType;
            this.member = member;
            this.persistentAttributeType = persistentAttributeType;
        }

        protected String getName() {
            return name;
        }

        protected Member getMember() {
            return member;
        }

        protected Class<Y> getJavaType() {
            return javaType;
        }

        protected abstract <X> AbstractAttribute<X, Y> build(AbstractManagedType<X> declaringType);
    }

}
