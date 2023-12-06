package com.example.member.entity;

import com.example.member.article.Article;
import com.example.member.dto.RoomDto;
import com.example.member.dto.UploadFileDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UploadFile extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String fileName;                //예류.jpg

    @Column
    private String saveFileName;            //uuid예류.jpg

    @Column
    private String filePath;                // D:/image/uuid예류.jpg

    @Column
    private String contentType;             // image/jpeg

    private long size;                      // 4476873 (byte)

    @JoinColumn(name = "article_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

//    @Column
//    private Long imgNumber;

    public static UploadFile toUploadFile(UploadFileDto uploadFileDto, Article article) {
        UploadFile uploadFile = new UploadFile();

        uploadFile.setFileName(uploadFileDto.getFileName());
        uploadFile.setSaveFileName(uploadFileDto.getSaveFileName());
        uploadFile.setFilePath(uploadFileDto.getFilePath());
        uploadFile.setContentType(uploadFileDto.getContentType());
        uploadFile.setSize(uploadFileDto.getSize());
        uploadFile.setArticle(article);

        return uploadFile;
    }

}
