package io.app.stacktodobe.member.support;

import io.app.stacktodobe.member.adapter.out.persistence.entity.MemberEntity;
import io.app.stacktodobe.member.adapter.out.persistence.repository.MemberRepository;
import io.app.stacktodobe.member.adapter.in.web.dto.MemberCreateCommand;
import io.app.stacktodobe.member.domain.Member;
import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;

import static io.app.stacktodobe.utils.TestSourceGenerator.*;
import static io.app.stacktodobe.utils.TestSourceGenerator.generatePhoneNumber;
import static io.app.stacktodobe.utils.TestSourceGenerator.generateProfileImage;

public class MemberFixtures {

    private final TestRestTemplate client;

    private MemberFixtures(TestRestTemplate client) {
        this.client = client;
    }

    public static Long persistedMemberId(MemberRepository repo) {
        var command = new MemberCreateCommand(
                generateEmail(),
                generatePassword(),
                generateNickname(),
                generateProfileImage(),
                generatePhoneNumber()
        );

        return repo.save(MemberEntity.of(Member.createMember(command), "")).getId();
    }

    public static MemberFixtures create(Environment environment) {
        var client = new TestRestTemplate();
        var uriTemplateRenderer = new LocalHostUriTemplateHandler(environment);
        client.setUriTemplateHandler(uriTemplateRenderer);
        return new MemberFixtures(client);
    }
}
