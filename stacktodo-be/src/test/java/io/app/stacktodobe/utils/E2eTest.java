package io.app.stacktodobe.utils;


import io.app.stacktodobe.StacktodoBeApplication;
import io.app.stacktodobe.utils.testfixture.TestFixtureConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
        , classes = {StacktodoBeApplication.class, TestFixtureConfiguration.class}
)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
//@Import(TestSecurityConfig.class)
@EnableJpaRepositories(basePackages = "io.app.stacktodobe.**.persistence.repository")
public @interface E2eTest {
}
