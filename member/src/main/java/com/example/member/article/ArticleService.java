package com.example.member.article;

import com.example.member.entity.Member;
import com.example.member.entity.UploadFile;
import com.example.member.repository.MemberRepository;
import com.example.member.repository.UploadFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final UploadFileRepository uploadFileRepository;

//    public Board create(BoardDto boardDto) {
//        Board board = boardDto.toEntity();
//        if(board.getId() != null){
//            return null;
//        } //기존데이터 수정 방지
//        return boardRepository.save(board);
//    }

    public List<ArticleDto> articleDtoList () {
        List<Article> articleList = articleRepository.findAll();
        List<ArticleDto> articleDtoList =new ArrayList<>();

        for (Article article : articleList) {
            ArticleDto articleDto = ArticleDto.toArticleDto(article);
            articleDtoList.add(articleDto);
        }

        return articleDtoList;
    }

    public void saveArticle(ArticleDto articleDto, String email){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        Article article = Article.toArticle(member, articleDto);
        articleRepository.save(article);

        System.out.println("article id 확인");
        System.out.println(article.getId());

        List<Long> longList = new ArrayList<>();
        String[] imgNumber = article.getContent().split("\"|\\\"");

//        System.out.println("imgNumber1 결과");
        for(int i = 0; i < imgNumber.length; i++) {
//            System.out.println(imgNumber[i]);

            if(imgNumber[i].contains("/image/")) {
//                System.out.println("다시 확인");
//                System.out.println(imgNumber[i]);
                String helloNumber = imgNumber[i].replaceAll("/image/", "");

//                System.out.println("다다시 확인");
//                System.out.println(helloNumber);

                Long helloLong = Long.parseLong(helloNumber);

                longList.add(helloLong);

            }

        }

        List<UploadFile> uploadFileList = uploadFileRepository.findAll();

//        for(UploadFile uploadFile : uploadFileList) {
//            System.out.println(uploadFile.toString());
//        }

        for(Long Long : longList) {
            System.out.println("longlist 확인");
            System.out.println(Long);
        }

        for(int i=0; i<uploadFileList.size(); i++) {

            for(int l=0; l<longList.size(); l++) {

            if(uploadFileList.get(i).getId().equals(longList.get(l))) {
                uploadFileList.get(i).setArticle(article);
                System.out.println("메시지메시지메시지");
            }

            }

        }

        // uploadfile : id가 타겟이다.
        // longlist : uploadfile의 아이디가 같아야 한다.
        // uploadfile.id = longlist.값
        // -

    }



    public List<ArticleDto> articleDtoList(String email){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        Long member_id = member.getId();
        List<Article> articleList = articleRepository.findAllByMemberId(member_id);
        List<ArticleDto> articleDtoList = new ArrayList<>();
        for (Article article : articleList){
            ArticleDto articleDto= ArticleDto.toArticleDto(article);
            articleDtoList.add(articleDto);
        }
        return articleDtoList;

    }


    public ArticleDto findArticle(Long id) {
        Article article = articleRepository.findById(id).orElse(null);

        return ArticleDto.toArticleDto(article);

    }



    public void articleUpdate(ArticleDto articleDto) {
        Article article = articleRepository.findById(articleDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        article.setTitle(articleDto.getTitle());
        article.setContent(articleDto.getContent());

    }
    public void articleDelete(Long article_id)throws Exception{
        Article article = articleRepository.findById(article_id)
                .orElseThrow(EntityNotFoundException::new);
        articleRepository.delete(article);

        List<UploadFile> uploadFile = uploadFileRepository.findAllByArticleId(article_id);
    }


}