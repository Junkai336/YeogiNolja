package com.example.member.service;

import com.example.member.article.Article;
import com.example.member.article.ArticleDto;
import com.example.member.constant.EditingExceptionConsideration;
import com.example.member.dto.LodgingDto;
import com.example.member.entity.Lodging;
import com.example.member.entity.Room;
import com.example.member.entity.UploadFile;
import com.example.member.repository.UploadFileRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UploadFileService {


    @Autowired
    private UploadFileRepository uploadFileRepository;


    private final Path rootLocation; // d:/image/

    public UploadFileService(String uploadPath) {
        this.rootLocation = Paths.get(uploadPath);
    }

    public UploadFile store(MultipartFile file) throws Exception {
        //		 fileName : 예류2.jpg
        //		 filePath : d:/images/uuid-예류2.jpg
        //		 saveFileName : uuid-예류2.png
        //		 contentType : image/jpeg
        //		 size : 4994942
        //		 registerDate : 2020-02-06 22:29:57.748
        try {
            if (file.isEmpty()) {
                throw new Exception("Failed to store empty file " + file.getOriginalFilename());
            }

            String saveFileName = fileSave(rootLocation.toString(), file);
            UploadFile saveFile = new UploadFile();
            saveFile.setFileName(file.getOriginalFilename());
            saveFile.setSaveFileName(saveFileName);
            saveFile.setContentType(file.getContentType());
            saveFile.setSize(file.getResource().contentLength());
            saveFile.setEditingExceptionConsideration(EditingExceptionConsideration.N);
            saveFile.setFilePath(rootLocation.toString().replace(File.separatorChar, '/') + '/' + saveFileName);
            uploadFileRepository.save(saveFile);
            System.out.println("이미지가 DB에 저장됩니다.");
            return saveFile;

        } catch (IOException e) {
            throw new Exception("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    public UploadFile load(Long fileId) throws RuntimeException {

        UploadFile uploadFile = uploadFileRepository.findById(fileId)
                .orElseThrow(EntityNotFoundException::new);
        //UploadFile uploadFile = uploadFileRepository.findById(fileId).orElse(null);
        return uploadFileRepository.saveAndFlush(uploadFile);
    }

    public String fileSave(String rootLocation, MultipartFile file) throws IOException {
        File uploadDir = new File(rootLocation);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // saveFileName 생성
        UUID uuid = UUID.randomUUID();
        String saveFileName = uuid.toString() + file.getOriginalFilename();
        File saveFile = new File(rootLocation, saveFileName);
        FileCopyUtils.copy(file.getBytes(), saveFile);

        return saveFileName;
    }


    public void fileDelete(UploadFile uploadFile) {
        Path path = Paths.get(uploadFile.getFilePath());
        try {
            Files.delete(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void summernoteFileDelete(Long fileName) {

        UploadFile uploadFile = uploadFileRepository.findById(fileName).orElseThrow(EntityNotFoundException::new);

        String filePath = uploadFile.getFilePath();

        Path path = Paths.get(filePath);
        try {
            Files.delete(path);
            uploadFileRepository.delete(uploadFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 편집시 서머노트에서 이미지를 삭제할 때
    public void summernoteFileDelete(Long fileName, UploadFile uploadFile) {

        // 원본은 이넘타입이 Y 상태가 된다.
        // 작성하거나 예외사항에서 N 상태가 되어야 하는데 그것은 다른 메소드에서 한다.
        uploadFile.setEditingExceptionConsideration(EditingExceptionConsideration.Y);
        uploadFileRepository.save(uploadFile);

    }

    public void uploadFileGrantedArticleId(Article article) {

        List<Long> longList = new ArrayList<>();
        String[] imgNumber = article.getContent().split("\"|\\\"");

        for (int i = 0; i < imgNumber.length; i++) {
            if (imgNumber[i].contains("/image/")) {
                String helloNumber = imgNumber[i].replaceAll("/image/", "");

                Long helloLong = Long.parseLong(helloNumber);

                longList.add(helloLong);
            }
        }

        List<UploadFile> uploadFileList = uploadFileRepository.findAll();

        for (int i = 0; i < uploadFileList.size(); i++) {
            for (int l = 0; l < longList.size(); l++) {
                if (uploadFileList.get(i).getId().equals(longList.get(l))) {
                    uploadFileList.get(i).setArticle(article);
                }
            }
        }

    }

    public void uploadFileGrantedLodgingId(Lodging lodging) {

        List<Long> longList = new ArrayList<>();
        String[] imgNumber = lodging.getDetail().split("\"|\\\"");

        for (int i = 0; i < imgNumber.length; i++) {
            if (imgNumber[i].contains("/image/")) {
                String helloNumber = imgNumber[i].replaceAll("/image/", "");

                Long helloLong = Long.parseLong(helloNumber);

                longList.add(helloLong);
            }
        }

        List<UploadFile> uploadFileList = uploadFileRepository.findAll();

        for (int i = 0; i < uploadFileList.size(); i++) {
            for (int l = 0; l < longList.size(); l++) {
                if (uploadFileList.get(i).getId().equals(longList.get(l))) {
                    uploadFileList.get(i).setLodging(lodging);
                }
            }
        }

    }

    // nullpointerException 에러 (이미지 지우고 새로운 이미지 넣고 뒤로가기)
    public void backwardUploadFileCheck(Long id) {
        List<UploadFile> uploadFileList = uploadFileRepository.findAll();

        for (UploadFile uploadFile : uploadFileList) {
            if (uploadFile.getArticle() != null && uploadFile.getArticle().getId().equals(id)) {
                uploadFile.setEditingExceptionConsideration(EditingExceptionConsideration.N);
            }
        }
    }

    public void backwardUploadFileCheck() {
        List<UploadFile> uploadFileList = uploadFileRepository.findAll();

        for (UploadFile uploadFile : uploadFileList) {
            uploadFile.setEditingExceptionConsideration(EditingExceptionConsideration.N);
        }

    }

    public void refreshUploadFileCheck(Long id) {
        List<UploadFile> uploadFileList = uploadFileRepository.findAll();

        for (UploadFile uploadFile : uploadFileList) {
            if (uploadFile.getEditingExceptionConsideration() == EditingExceptionConsideration.Y) {
                uploadFile.setEditingExceptionConsideration(EditingExceptionConsideration.N);
                // 숙소 이미지 이넘타입이 바로 안바뀌어서 추가
                uploadFileRepository.saveAndFlush(uploadFile);
            }
        }
    }

    public void refreshUploadFileCheck() {
        List<UploadFile> uploadFileList = uploadFileRepository.findAll();

        for (UploadFile uploadFile : uploadFileList) {
            if (uploadFile.getEditingExceptionConsideration() == EditingExceptionConsideration.Y) {
                uploadFile.setEditingExceptionConsideration(EditingExceptionConsideration.N);
            }
        }
    }

    public void emptyUploadFileCheck() {

        List<UploadFile> uploadFileList = uploadFileRepository.findAll();

        for (UploadFile uploadFile : uploadFileList) {
            if (uploadFile.getArticle() == null && uploadFile.getLodging() == null && uploadFile.getRoom() == null) {
                uploadFileRepository.delete(uploadFile);
                fileDelete(uploadFile);
            }
        }
    }

    public void havingIdDelete() {
        List<UploadFile> uploadFileList = uploadFileRepository.findAll();

        for (UploadFile uploadFile : uploadFileList) {
            if (uploadFile.getEditingExceptionConsideration() == EditingExceptionConsideration.Y) {
                uploadFileRepository.delete(uploadFile);
                fileDelete(uploadFile);

            }
        }
    }

    public UploadFile findById(Long fileName) throws Exception {

        UploadFile uploadFile = uploadFileRepository.findById(fileName).orElseThrow(EntityNotFoundException::new);

        return uploadFile;
    }

    public void uploadFileGrantedRoomId(Room room) {
        List<Long> longList = new ArrayList<>();
        String[] imgNumber = room.getDetail().split("\"|\\\"");

        for (int i = 0; i < imgNumber.length; i++) {
            if (imgNumber[i].contains("/image/")) {
                String helloNumber = imgNumber[i].replaceAll("/image/", "");

                Long helloLong = Long.parseLong(helloNumber);

                longList.add(helloLong);
            }
        }

        List<UploadFile> uploadFileList = uploadFileRepository.findAll();

        for (int i = 0; i < uploadFileList.size(); i++) {
            for (int l = 0; l < longList.size(); l++) {
                if (uploadFileList.get(i).getId().equals(longList.get(l))) {
                    uploadFileList.get(i).setRoom(room);
                }
            }
        }

    }
}