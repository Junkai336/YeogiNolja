package com.example.member.entity;

import com.example.member.article.Article;
import com.example.member.constant.EditingExceptionConsideration;
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

    @Enumerated(EnumType.STRING)
    private EditingExceptionConsideration editingExceptionConsideration; // 서머노트 업로드 이미지 편집시 예외 잡기용 필드 (평소에 N으로 둘 것)

    @JoinColumn(name = "article_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

    @JoinColumn(name = "lodging_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Lodging lodging;

    @JoinColumn(name = "room_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Room room;

//    @Column
//    private Long imgNumber;

    public static UploadFile toUploadFile(UploadFileDto uploadFileDto, Article article) {
        UploadFile uploadFile = new UploadFile();

        uploadFile.setFileName(uploadFileDto.getFileName());
        uploadFile.setSaveFileName(uploadFileDto.getSaveFileName());
        uploadFile.setFilePath(uploadFileDto.getFilePath());
        uploadFile.setContentType(uploadFileDto.getContentType());
        uploadFile.setSize(uploadFileDto.getSize());
        uploadFile.setEditingExceptionConsideration(uploadFileDto.getEditingExceptionConsideration());
        uploadFile.setArticle(article);

        return uploadFile;
    }

}
