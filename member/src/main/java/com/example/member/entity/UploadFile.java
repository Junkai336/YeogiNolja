package com.example.member.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
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
}
