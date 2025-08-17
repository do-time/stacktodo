package io.app.stacktodobe.utils;


import io.app.stacktodobe.StacktodoBeApplication;
import io.app.stacktodobe.config.TestSecurityConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
        , classes = {StacktodoBeApplication.class}
)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public @interface E2eTest {
}
