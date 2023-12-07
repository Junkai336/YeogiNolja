package com.example.member.dto;

import com.example.member.article.Article;
import com.example.member.entity.ItemImg;
import com.example.member.entity.UploadFile;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
public class UploadFileDto {

    private Long id;

    private String fileName;                //예류.jpg

    private String saveFileName;            //uuid예류.jpg

    private String filePath;                // D:/image/uuid예류.jpg

    private String contentType;             // image/jpeg

    private long size;                      // 4476873 (byte)

    private Article article;



    public static UploadFileDto toUploadFileDto (UploadFile uploadFile, Article article) {

        UploadFileDto uploadFileDto = new UploadFileDto();
        uploadFileDto.setId(uploadFile.getId());
        uploadFileDto.setFileName(uploadFile.getFileName());
        uploadFileDto.setSaveFileName(uploadFile.getSaveFileName());
        uploadFileDto.setFilePath(uploadFile.getFilePath());
        uploadFileDto.setContentType(uploadFile.getContentType());
        uploadFileDto.setSize(uploadFile.getSize());
        uploadFileDto.setArticle(article);

        return uploadFileDto;
    }

}
