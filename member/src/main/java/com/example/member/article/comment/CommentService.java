package com.example.member.article.comment;

import com.example.member.article.Article;
import com.example.member.article.ArticleRepository;
import com.example.member.entity.Member;
import com.example.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    public void createComment(Long articleId, CommentDto commentDto, String email) throws Exception{
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        Article article = articleRepository.findById(articleId)
                .orElseThrow(EntityNotFoundException::new);
        Comment comment = Comment.crateComment(commentDto, article);
        commentRepository.save(comment);

    }

    // 해당 게시글의 댓글 목록 출력
    public List<CommentDto> commentDtoList(Long article_id){
        List<Comment> commentList = commentRepository.findAllByArticleId(article_id);
        List<CommentDto> commentDtoList=new ArrayList<>();
        for (Comment comment: commentList){
            CommentDto commentDto = CommentDto.toCommentDto(comment);
            commentDtoList.add(commentDto);
        }
        return commentDtoList;
    }

}























