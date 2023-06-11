package apbase.env;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Profile;

@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
@Profile("asyncbatch")
public @interface AsyncBatch {

}
