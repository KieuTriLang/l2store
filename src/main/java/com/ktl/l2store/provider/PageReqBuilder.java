package com.ktl.l2store.provider;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class PageReqBuilder {

    public static PageRequest createReq(int page, int limitSize) {
        return PageRequest.of(page, limitSize);
    }

    public static Pageable createReq(int page, int limitSize, String sortTar, String sortDir) {

        Direction direction = sortDir.equals("desc") ? Direction.DESC : Direction.ASC;

        return PageRequest.of(page, limitSize, Sort.by(direction, sortTar));
    }
}
