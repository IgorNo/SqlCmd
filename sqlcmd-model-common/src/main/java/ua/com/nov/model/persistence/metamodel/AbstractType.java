package ua.com.nov.model.persistence.metamodel;

import org.springframework.util.Assert;

import javax.persistence.metamodel.Type;

/**
 * Defines commonality for the JPA {@link Type} hierarchy of interfaces.
 *
 * @param <X> - The type of the represented object or attribute
 * @author Igor Novikov
 * @since 04.11.2017
 */
public abstract class AbstractType<X> implements Type<X> {
    private Class<X> domainClass;

    /**
     * Instantiates the type based on the given domain {@link Class}.
     *
     * @param domainClass must not be {@literal null}.
     * @throws IllegalArgumentException if {@code domainClass} is {@literal null}
     */
    protected AbstractType(Class<X> domainClass) {
        Assert.notNull(domainClass);
        this.domainClass = domainClass;
    }

    @Override
    public Class<X> getJavaType() {
        return domainClass;
    }

    /**
     * Obtains the type name.  See notes on {@link #getJavaType()} for details
     *
     * @return The type name
     */
    String getTypeName() {
        return domainClass.getName();
    }
}
