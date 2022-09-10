package com.ktl.l2store.api;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ktl.l2store.entity.FileDB;
import com.ktl.l2store.service.FileDB.FileDBService;

import org.springframework.web.bind.annotation.RequestMethod;

@RestController
@RequestMapping("/api/file")
public class FileApi {
    @Autowired
    private FileDBService fileDBService;

    @RequestMapping(value = "/{fileCode}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> getImage(@PathVariable("fileCode") Long fileCode) {
        FileDB file = fileDBService.getImageByFileCode(fileCode);
        InputStream is = new ByteArrayInputStream(file.getData());
        MediaType mediaType = MediaType.valueOf(file.getType());
        return ResponseEntity.ok().contentType(mediaType).body(new InputStreamResource(is));
    }

    @RequestMapping(value = "/download/{fileCode}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadImage(@PathVariable("fileCode") Long fileCode) {

        FileDB file = fileDBService.getImageByFileCode(fileCode);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(file.getData());
    }

}
