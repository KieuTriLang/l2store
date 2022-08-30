package com.ktl.l2store.dto.res;

import java.time.ZonedDateTime;
import java.util.Collection;

import com.ktl.l2store.common.URIBuilder;
import com.ktl.l2store.entity.Role;

import lombok.Data;

@Data
public class UserDto {

    private String displayName;

    private boolean gender;

    private String imageUrl;

    private String address;

    private Collection<Role> roles;

    private ZonedDateTime dob;

    public void setImageUrl(String imageCode) {
        this.imageUrl = URIBuilder.generate("/api/file/" + imageCode);
    }
}
