package com.example.member.repository;

import com.example.member.entity.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long>{

    @Query(nativeQuery = true, value = "select * from just_board.upload_file a where a.article_id= :article_id")
    List<UploadFile> findAllByArticleId(@Param("article_id") Long article_id);

    @Query(nativeQuery = true, value = "select * from Lodging l where l.lodging_id= :lodging_id")
    List<UploadFile> findAllByLodgingId(@Param("lodging_id") Long lodging_id);
}
