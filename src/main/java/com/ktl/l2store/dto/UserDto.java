package com.ktl.l2store.dto;

import java.time.ZonedDateTime;
import java.util.Collection;

import com.ktl.l2store.entity.Role;
import com.ktl.l2store.provider.URIBuilder;

import lombok.Data;

@Data
public class UserDto {

    private String firstName;

    private String lastName;

    private String email;

    private boolean gender;

    private String avatar;

    private String address;

    private Collection<Role> roles;

    private ZonedDateTime dob;

    public void setAvatarUri(String avatarCode) {
        this.avatar = URIBuilder.generate("/api/file/" + avatarCode);
    }
}
