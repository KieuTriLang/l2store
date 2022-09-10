package com.ktl.l2store.dto;

import java.time.ZonedDateTime;

import com.ktl.l2store.provider.URIBuilder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EvaluateDto {

    private Long id;

    private int star;

    private String comment;

    private Long productId;

    private String userName;

    private String avatar;

    private ZonedDateTime postedTime;

    public void setAvatarUri(String avatarCode) {
        this.avatar = URIBuilder.generate("/api/file/" + avatarCode);
    }
}
