package com.ktl.l2store.dto.req;

import java.io.Serializable;
import java.time.ZonedDateTime;

import lombok.Data;

@Data
public class ReqUserDto implements Serializable {

    private String displayName;

    private int gender;

    private String address;

    private ZonedDateTime dob;
}