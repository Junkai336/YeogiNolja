package com.example.member.controller;

import com.example.member.entity.ItemImg;
import com.example.member.entity.UploadFile;
import com.example.member.service.ItemImgService;
import com.example.member.service.UploadFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class UploadFileController {

    private final UploadFileService uploadFileService;

    private final ResourceLoader resourceLoader;

    @PostMapping("/images")
//    @PostMapping("D:/shop/item")
    public ResponseEntity<?> imageUpload(@RequestParam("file") MultipartFile file) {
        try {
            UploadFile uploadFile = uploadFileService.store(file);
            return ResponseEntity.ok().body("/images/" + uploadFile.getId());
//            return ResponseEntity.ok().body("/D:/shop/item/" + uploadFile.getId());
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/images/{fileId}")
//    @GetMapping("/D:/shop/item/{fileId}")
    public ResponseEntity<?> serveFile(@PathVariable Long fileId){
        try {
            UploadFile uploadFile = uploadFileService.load(fileId);
            Resource resource = resourceLoader.getResource("file:" + uploadFile.getFilePath());
            return ResponseEntity.ok().body(resource);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

    }

}
