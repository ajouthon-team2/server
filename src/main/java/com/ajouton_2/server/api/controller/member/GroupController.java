package com.ajouton_2.server.api.controller.member;

import com.ajouton_2.server.api.dto.group.GroupAddRequest;
import com.ajouton_2.server.api.dto.group.GroupAddResponse;
import com.ajouton_2.server.api.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class GroupController {

    private final GroupService groupService;

    @PostMapping("/group")
    public ResponseEntity<GroupAddResponse> createGroup(@RequestBody GroupAddRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(groupService.createGroup(request));
    }
}
