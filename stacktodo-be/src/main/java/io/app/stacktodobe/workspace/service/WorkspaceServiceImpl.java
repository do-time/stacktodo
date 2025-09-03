package io.app.stacktodobe.workspace.service;

import io.app.stacktodobe.member.adapter.out.persistence.entity.Member;
import io.app.stacktodobe.workspace.domain.usecase.WorkspaceCommandService;
import io.app.stacktodobe.workspace.entity.Workspace;
import io.app.stacktodobe.workspace.persistence.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//@Service
//@RequiredArgsConstructor
//public class WorkspaceServiceImpl implements WorkspaceCommandService {
//
//    private final WorkspaceRepository workspaceRepository;
//
//    @Transactional
//    public void createWorkspace() {
//        // memberService에서 받아올 데이터
//        Member member = new Member();
//
//        var newWorkspace = Workspace.builder()
//                .owner(member)
//                .name(member.getNickname())
//                .build();
//
//        workspaceRepository.save(newWorkspace);
//    }
//}
