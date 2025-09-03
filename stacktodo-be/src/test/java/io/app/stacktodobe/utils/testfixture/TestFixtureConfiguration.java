package io.app.stacktodobe.utils.testfixture;

import io.app.stacktodobe.member.support.MemberFixtures;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;

public class TestFixtureConfiguration {

    @Bean
    @Scope("prototype")
    TestFixture testFixture(Environment environment) {
        return TestFixture.create(environment);

    }

    @Bean
    @Scope("prototype")
    MemberFixtures memberFixtures(Environment enviroment) {
        return MemberFixtures.create(enviroment);
    }
}
