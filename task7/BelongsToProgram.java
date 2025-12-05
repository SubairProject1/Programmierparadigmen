import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Responsible(groupMember = "Catalin", description = "Implementation")
public @interface BelongsToProgram {
    String description();
    String[] components();
}
