import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies a postcondition that must be true after the annotated method, constructor, or class has executed.
 * This is used to document guarantees provided by the method or constructor, given that the preconditions were met.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD})
@Responsible(groupMember = "Subair", description = "Author and maintainer of this annotation")
public @interface Postcondition {
    String description();
}
