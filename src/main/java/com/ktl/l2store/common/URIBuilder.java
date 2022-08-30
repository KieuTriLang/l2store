package com.ktl.l2store.common;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class URIBuilder {
    public static String generate(String path) {
        String uri = ServletUriComponentsBuilder.fromCurrentContextPath().path(path).toUriString();
        return uri;
    }
}
