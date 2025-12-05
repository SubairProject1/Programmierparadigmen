import java.lang.annotation.*;

/**
 * Specifies a precondition that must be true before the annotated method, constructor, or class is executed.
 * This is used to document assumptions or constraints that the caller must satisfy.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD})
@Responsible(groupMember = "Subair", description = "Author and maintainer of this annotation")
public @interface Precondition {
    String description();
}

