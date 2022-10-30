package com.ktl.l2store.dto;

import java.time.ZonedDateTime;
import java.util.Date;

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

    private String content;

    private Long productId;

    private String productName;

    private String userName;

    private String avatar;

    private Date postedTime;

    public void setAvatarUri(String avatarCode) {
        this.avatar = URIBuilder.generate("/api/file/" + avatarCode);
    }

    public void convertDate(ZonedDateTime date) {
        this.postedTime = Date.from(date.toInstant());
    }
}
