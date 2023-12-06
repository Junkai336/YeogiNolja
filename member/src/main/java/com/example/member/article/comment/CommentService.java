package com.example.member.article.comment;

import com.example.member.article.Article;
import com.example.member.article.ArticleRepository;
import com.example.member.entity.Member;
import com.example.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class CommentService {

    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    public void newComment(CommentDto commentDto, String email, Long article_id) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        Article article = articleRepository.findById(article_id)
                .orElseThrow(EntityNotFoundException::new);
        Comment comment = Comment.toComment(commentDto, member, article);
        try {
            commentRepository.save(comment);
        } catch (Exception e) {
            e.getStackTrace();
        }

    }

    public List<CommentDto> commentDtoList(Long article_id) {
        List<Comment> commentList = commentRepository.findAllByArticleId(article_id);
        List<CommentDto> commentDtoList = CommentDto.toCommentDtoList(commentList);
        for (Comment comment : commentList) {
            CommentDto commentDto = CommentDto.toCommentDto(comment);
        }
        return commentDtoList;

    }

    // 접근자 유효성 검사
    public void validation(Long commentId, String email) throws IllegalArgumentException {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(EntityNotFoundException::new);
        if (!StringUtils.equals(comment.getMember().getEmail(), email)) {
            throw new IllegalArgumentException("접근 관한이 없습니다.");
        }

    }


    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(EntityNotFoundException::new);
        commentRepository.delete(comment);
    }

    public CommentDto update(Long articleId, Long commentId, CommentDto commentDto) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(EntityNotFoundException::new);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(EntityNotFoundException::new);
        if (commentId.equals(commentDto.getId())) {
            throw new IllegalArgumentException("다시 시도해주시기 바랍니다.");

        }
        comment.setComment(commentDto.getComment());
        comment.setUpdateTime(LocalDateTime.now());
        CommentDto commentDto1 = CommentDto.toCommentDto(comment);
        return commentDto1;
    }
}










