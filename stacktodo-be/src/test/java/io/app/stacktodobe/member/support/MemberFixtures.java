package io.app.stacktodobe.member.support;

import io.app.stacktodobe.member.persistence.entity.Member;
import io.app.stacktodobe.member.persistence.repository.MemberRepository;
import io.app.stacktodobe.member.presentation.command.MemberCreateCommand;

import static io.app.stacktodobe.utils.TestSourceGenerator.*;
import static io.app.stacktodobe.utils.TestSourceGenerator.generatePhoneNumber;
import static io.app.stacktodobe.utils.TestSourceGenerator.generateProfileImage;

public final class MemberFixtures {
    public static Long persistedMemberId(MemberRepository repo) {
        var command = new MemberCreateCommand(
                generateEmail(),
                generatePassword(),
                generateNickname(),
                generateProfileImage(),
                generatePhoneNumber()
        );

        return repo.save(Member.createMember(command, "password")).getId();
    }
}
