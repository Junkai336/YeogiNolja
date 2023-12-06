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

        String imgNumber[] = article.getContent().split("<img src=\"/image/");
        String imgNumbertoString = Arrays.toString(imgNumber);
//        System.out.println(Arrays.toString(imgNumber));
        String imgNumber2[] = imgNumbertoString.split("\" style=\"");
//        System.out.println("확인메시지" + Arrays.toString(imgNumber2));

        // 과연?
//        Integer[] IntegerNumber = new Integer[]

//        Integer[] newArr = Stream.of(imgNumber2).mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new);
//        System.out.println("인티저확인메시지" + Arrays.toString(newArr));


//        List<UploadFile> uploadFileList = uploadFileRepository.findAll();
//
//        for(UploadFile uploadFile : uploadFileList) {
//
//            if(article.getContent().("/image/" + 22).equals(uploadFile.getId())) {
//
//            }
//
//        }


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