package com.example.member.controller;

import com.example.member.constant.EditingExceptionConsideration;
import com.example.member.dto.ItemImgDto;
import com.example.member.entity.ItemImg;
import com.example.member.entity.UploadFile;
import com.example.member.repository.UploadFileRepository;
import com.example.member.service.ItemImgService;
import com.example.member.service.UploadFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;

@Controller
@RequiredArgsConstructor
public class UploadFileController {

    private final UploadFileService uploadFileService;
    private final UploadFileRepository uploadFileRepository;

    private final ResourceLoader resourceLoader;

//    	@Bean(name = "uploadPath")
//	public String uploadPath() {
////		return "d:/image/";
//		return "D:/shop/item";
//	}

//      @Value("${itemImgLocation}")
//    private String itemImgLocation;
//    // D:/shop/item

    @PostMapping("/image")
    public ResponseEntity<?> imageUpload(@RequestParam("file") MultipartFile file) {
        try {
            UploadFile uploadFile = uploadFileService.store(file);
            return ResponseEntity.ok().body("/image/" + uploadFile.getId());
//            return ResponseEntity.ok().body("/D:/shop/item/" + uploadFile.getId());
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/image/{fileId}")
    public ResponseEntity<?> serveFile(@PathVariable Long fileId){
        try {
            UploadFile uploadFile = uploadFileService.load(fileId);

            Resource resource = resourceLoader.getResource("file:" + uploadFile.getFilePath());

            return ResponseEntity.ok().body(resource);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

    }
    @PostMapping(value = "/imageDelete")
    @ResponseBody
    public void deleteSummernoteImageFile(@RequestParam("file") Long fileName) {
        try {

        UploadFile uploadFile = uploadFileService.findById(fileName);
        if(uploadFile.getArticle() != null) {
            uploadFileService.summernoteFileDelete(fileName, uploadFile);
        } else {
        // 해당 파일 삭제
        uploadFileService.summernoteFileDelete(fileName);
        }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }




    }

}
