package ua.com.nov.model.persistence.metamodel;

import javax.persistence.metamodel.IdentifiableType;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.Type;
import java.util.HashSet;
import java.util.Set;

/**
 * Defines commonality for the JPA {@link IdentifiableType} types.  JPA defines
 * identifiable types as entities or mapped-superclasses.  Basically things to which an
 * identifier can be attached.
 * <p/>
 *
 * @author Igor Novikov
 * @since 05.12.2017
 */
public abstract class AbstractIdentifiableType<X> extends AbstractManagedType<X> implements IdentifiableType<X> {

    private SingularAttribute<X, ?> id;
    private Set<SingularAttribute<X, ?>> idClassAttributes;
    private SingularAttribute<X, ?> version;

    @SuppressWarnings("unchecked")
    protected AbstractIdentifiableType(Builder<X> builder) {
        super(builder);
        if (builder.id != null) {
            this.id = (SingularAttribute<X, ?>) getSingularAttribute(builder.id);
        } else {
            idClassAttributes = new HashSet<>(builder.idClassAttributes.size());
            for (String idClassAttribute : builder.idClassAttributes) {
                idClassAttributes.add((SingularAttribute<X, ?>) getSingularAttribute(idClassAttribute));
            }
        }
        if (builder.version != null) version = (SingularAttribute<X, ?>) getSingularAttribute(builder.version);
    }

    @Override
    public boolean hasSingleIdAttribute() {
        return locateIdAttribute() != null;
    }


    @Override
    @SuppressWarnings("unchecked")
    public AbstractIdentifiableType<? super X> getSupertype() {
        // overridden simply to perform the cast
        return (AbstractIdentifiableType<? super X>) super.getSupertype();
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public <Y> SingularAttribute<? super X, Y> getId(Class<Y> javaType) {
        SingularAttribute id = locateIdAttribute();
        if (id != null) {
            checkType(id, javaType);
        } else {
            throw new IllegalArgumentException(
                    "Illegal call to IdentifiableType#getId for class [" + getTypeName() + "] defined with @IdClass");
        }
        return (SingularAttribute<? super X, Y>) id;
    }

    private SingularAttribute<? super X, ?> locateIdAttribute() {
        if (id != null) {
            return id;
        } else {
            if (getSupertype() != null) {
                SingularAttribute<? super X, ?> id = getSupertype().internalGetId();
                if (id != null) {
                    return id;
                }
            }
        }

        return null;
    }

    private SingularAttribute<? super X, ?> internalGetId() {
        if (id != null) {
            return id;
        } else {
            if (getSupertype() != null) {
                return getSupertype().internalGetId();
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private void checkType(SingularAttribute attribute, Class javaType) {
        if (!javaType.isAssignableFrom(attribute.getType().getJavaType())) {
            throw new IllegalArgumentException(
                    String.format(
                            "Attribute [%s#%s : %s] not castable to requested type [%s]",
                            getTypeName(),
                            attribute.getName(),
                            attribute.getType().getJavaType().getName(),
                            javaType.getName()
                    )
            );
        }
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public <Y> SingularAttribute<X, Y> getDeclaredId(Class<Y> javaType) {
        if (id == null) {
            throw new IllegalArgumentException("The id attribute is not declared on this type [" + getTypeName() + "]");
        }
        checkType(id, javaType);
        return (SingularAttribute<X, Y>) id;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Type<?> getIdType() {
        final SingularAttribute id = locateIdAttribute();
        if (id != null) {
            return id.getType();
        }

        Set<SingularAttribute<? super X, ?>> idClassAttributes = getIdClassAttributesSafely();
        if (idClassAttributes != null) {
            if (idClassAttributes.size() == 1) {
                return idClassAttributes.iterator().next().getType();
            }
        }

        return null;
    }

    /**
     * A form of {@link #getIdClassAttributes} which prefers to return {@code null} rather than throw exceptions
     *
     * @return IdClass attributes or {@code null}
     */
    private Set<SingularAttribute<? super X, ?>> getIdClassAttributesSafely() {
        if (hasSingleIdAttribute()) {
            return null;
        }
        final Set<SingularAttribute<? super X, ?>> attributes = new HashSet<>();
        internalCollectIdClassAttributes(attributes);

        if (attributes.isEmpty()) {
            return null;
        }

        return attributes;
    }

    @Override
    public Set<SingularAttribute<? super X, ?>> getIdClassAttributes() {
        if (hasSingleIdAttribute()) {
            throw new IllegalArgumentException("This class [" + getJavaType() + "] does not define an IdClass");
        }

        final Set<SingularAttribute<? super X, ?>> attributes = new HashSet<>();
        internalCollectIdClassAttributes(attributes);

        if (attributes.isEmpty()) {
            throw new IllegalArgumentException("Unable to locate IdClass attributes [" + getJavaType() + "]");
        }

        return attributes;
    }

    @SuppressWarnings("unchecked")
    private void internalCollectIdClassAttributes(Set attributes) {
        if (idClassAttributes != null) {
            attributes.addAll(idClassAttributes);
        } else if (getSupertype() != null) {
            getSupertype().internalCollectIdClassAttributes(attributes);
        }
    }

    @Override
    public boolean hasVersionAttribute() {
        return locateVersionAttribute() != null;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public <Y> SingularAttribute<? super X, Y> getVersion(Class<Y> javaType) {
        // todo : is return null allowed?
        if (!hasVersionAttribute()) {
            return null;
        }

        SingularAttribute<? super X, ?> version = locateVersionAttribute();
        if (version != null) {
            checkType(version, javaType);
        }
        return (SingularAttribute<? super X, Y>) version;
    }

    private SingularAttribute<? super X, ?> locateVersionAttribute() {
        if (version != null) {
            return version;
        } else {
            if (getSupertype() != null) {
                SingularAttribute<? super X, ?> version = getSupertype().internalGetVersion();
                if (version != null) {
                    return version;
                }
            }
        }
        return null;
    }

    private SingularAttribute<? super X, ?> internalGetVersion() {
        if (version != null) {
            return version;
        } else {
            if (getSupertype() != null) {
                return getSupertype().internalGetVersion();
            }
        }

        return null;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public <Y> SingularAttribute<X, Y> getDeclaredVersion(Class<Y> javaType) {
        checkDeclaredVersion();
        checkType(version, javaType);
        return (SingularAttribute<X, Y>) version;
    }

    private void checkDeclaredVersion() {
        if (version == null || (getSupertype() != null && getSupertype().hasVersionAttribute())) {
            throw new IllegalArgumentException(
                    "The version attribute is not declared by this type [" + getJavaType() + "]"
            );
        }
    }


    public static abstract class Builder<X> extends AbstractManagedType.Builder<X> {
        private String id;
        private Set<String> idClassAttributes;
        private String version;
        private Type<?> idClassType;


        public Builder(Class<X> domainClass) {
            super(domainClass);
        }

        public <Y> void addIdAttribute(SingularAttributeImpl.Builder<Y> idAttribute) {
            if (idClassAttributes != null
                    && ((AbstractIdentifiableType) getSuperType()).getIdClassAttributesSafely() != null) {
                throw new IllegalStateException("IdentifiableType can't have single id attribute and idClass together.");
            }
            this.id = idAttribute.getName();
            addAttribute(idAttribute);
        }

        public <Y> void addIdClassAttributes(Set<SingularAttributeImpl.Builder<Y>> idClassAttributes) {
            if (id != null && ((AbstractIdentifiableType) getSuperType()).locateIdAttribute() != null) {
                throw new IllegalStateException("IdentifiableType can't have single id attribute and idClass together.");
            }
            this.idClassAttributes = new HashSet<>();
            for (SingularAttributeImpl.Builder<Y> idClassAttribute : idClassAttributes) {
                addAttribute(idClassAttribute);
                this.idClassAttributes.add(idClassAttribute.getName());
            }
        }

        public <Y> void addVersionAttribute(SingularAttributeImpl.Builder<Y> versionAttribute) {
            this.version = versionAttribute.getName();
            addAttribute(versionAttribute);
        }
    }
}
