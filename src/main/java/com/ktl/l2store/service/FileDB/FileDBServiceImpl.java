package com.ktl.l2store.service.FileDB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ktl.l2store.entity.FileDB;
import com.ktl.l2store.exception.ItemNotfoundException;
import com.ktl.l2store.repo.FileDBRepo;

@Service
public class FileDBServiceImpl implements FileDBService {

    @Autowired
    private FileDBRepo fileDBRepo;

    @Override
    public FileDB getImageByFileCode(Long fileCode) {

        FileDB file = fileDBRepo.findByFileCode(fileCode)
                .orElseThrow(() -> new ItemNotfoundException("Not found file"));
        return file;
    }

}
