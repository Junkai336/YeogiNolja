package com.example.member.article;

import com.example.member.entity.Member;
import com.example.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    public void createArticle(String email, ArticleDto articleDto) throws Exception{
        // 작성자의 정보를 가져온다.
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(EntityExistsException::new);
        Article article = Article.createArticle(articleDto, member);
        articleRepository.save(article);

    }

    public List<ArticleDto> showList() {
        List<Article> articleList = articleRepository.findAll();
        List<ArticleDto> articleDtoList = new ArrayList<>();

        for (Article article: articleList){
            ArticleDto articleDto = ArticleDto.toArticleDto(article);
            articleDtoList.add(articleDto);
        }

        return articleDtoList;

    }

    public ArticleDto articleDetail(Long articleId) throws Exception{
        Article article = articleRepository.findById(articleId)
                .orElseThrow(EntityNotFoundException::new);
        ArticleDto articleDto =ArticleDto.toArticleDto(article);
        return articleDto;

    }

    public void deleteArticle(Long articleId, String email) throws RuntimeException{
        // 삭제할 글의 찾는다.
        Article article = articleRepository.findById(articleId)
                .orElseThrow(EntityNotFoundException::new);
        // 삭제요청하는 멤버를 찾는다.
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        // 삭제요청하는 멤버의 이메일과 글을 게시한 멤버의 이메일이 다르면
        if (article.getMember().getEmail() != member.getEmail()){
            throw new RuntimeException("작성자가 아니므로 권한이 없습니다.");
        }
        // 위의 조건에 충족되지 않는다면 게시글을 삭제한다.
        articleRepository.delete(article);

    }
}
