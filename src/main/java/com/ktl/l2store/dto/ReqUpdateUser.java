package com.ktl.l2store.dto;

import java.time.ZonedDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReqUpdateUser {
    private String firstName;
    private String lastName;
    private boolean gender;
    private String address;
    private Date dob;
}
