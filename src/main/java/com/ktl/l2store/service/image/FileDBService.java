package com.ktl.l2store.service.image;

import com.ktl.l2store.entity.FileDB;

public interface FileDBService {
    FileDB getImageByFileCode(Long fileCode);
}
