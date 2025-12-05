import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Responsible(groupMember = "Catalin", description = "Implementation")
public @interface Responsible {
    String groupMember();
    String description() default "";
}
