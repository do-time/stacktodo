package io.app.stacktodobe.utils.testfixture;

import io.app.stacktodobe.integration.member.support.MemberFixtures;
import io.app.stacktodobe.integration.workspace.command.support.WorkspaceFixtures;
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

    @Bean
    WorkspaceFixtures workspaceFixtures(MemberFixtures memberFixtures) {
        return WorkspaceFixtures.create(memberFixtures);
    }
}
