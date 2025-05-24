package com.ajouton_2.server.api.controller;

import com.ajouton_2.server.api.dto.group.GroupAddRequest;
import com.ajouton_2.server.api.dto.group.GroupInviteCodeResponse;
import com.ajouton_2.server.api.dto.group.GroupListResponse;
import com.ajouton_2.server.api.dto.groupmember.GroupSignInRequest;
import com.ajouton_2.server.api.service.GroupMemberService;
import com.ajouton_2.server.api.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/group")
public class GroupController {

    private final GroupService groupService;
    private final GroupMemberService groupMemberService;

    @PostMapping
    public ResponseEntity<GroupInviteCodeResponse> createGroup(@RequestBody GroupAddRequest request) {
        return ResponseEntity.ok(groupService.createGroup(request));
    }

    @PostMapping("/signIn")
    public ResponseEntity<Void> signInGroup(
            @RequestBody GroupSignInRequest request) {
        groupMemberService.signInToGroup(request);
        return ResponseEntity.ok().build();
    }

//    @GetMapping
//    public ResponseEntity<List<GroupListResponse>> getGroups(){
//        return ResponseEntity.ok(groupService.getGroups());
//    }

//    @GetMapping("/{groupId}")
//    public ResponseEntity<GroupDetailResponse> getGroup(){}

    @GetMapping("/{groupId}/code")
    public ResponseEntity<GroupInviteCodeResponse>getGroupInviteCode(@PathVariable Long groupId){
        return ResponseEntity.status(HttpStatus.OK).body(groupService.getInviteCode(groupId));
    }
}
