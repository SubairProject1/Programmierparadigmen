import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies an invariant that must always be true during the lifetime of the annotated class.
 * This is used to ensure that an object remains in a consistent state throughout its lifetime.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD})
@Responsible(groupMember = "Subair", description = "Author and maintainer of this annotation")
public @interface Invariant {
    String description();
}
