package com.ktl.l2store.dto;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;

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

    private Date dob;

    public void setAvatarUri(String avatarCode) {
        this.avatar = URIBuilder.generate("/api/file/" + avatarCode);
    }

    public void convertDate(ZonedDateTime date) {
        this.dob = Date.from(date.toInstant());
    }
}
