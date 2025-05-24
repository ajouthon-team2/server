package com.ajouton_2.server.api.dto.group;

import com.ajouton_2.server.domain.group.Group.Category;
import lombok.Getter;

@Getter
public class GroupAddRequest {
    private String name;
    private Category category; // SPORTS, STUDY, CLUB
}