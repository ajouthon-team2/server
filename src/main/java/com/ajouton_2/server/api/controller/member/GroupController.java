package com.ajouton_2.server.api.controller.member;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class GroupController {

    @PostMapping("/group")
    public ResponseEntity<GroupAddResponse> createGroup() {

    }
}
