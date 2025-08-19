package com.water.site.service;

import com.water.site.entity.BoardEntity;
import com.water.site.entity.CommentEntity;
import com.water.site.entity.UserEntity;
import com.water.site.repository.BoardRepository;
import com.water.site.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public void addComment(Long boardId, String content, UserEntity user) {
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        CommentEntity comment = new CommentEntity();
        comment.setBoard(board);
        comment.setUser(user);
        comment.setContent(content);

        commentRepository.save(comment);
    }

    public List<CommentEntity> getComments(Long boardId) {
        return commentRepository.findByBoardIdOrderByCreatedAtDesc(boardId);
    }

    public List<CommentEntity> getCommentsByBoardId(Long boardId) {
        return commentRepository.findByBoardIdOrderByCreatedAtAsc(boardId);
    }

    public List<CommentEntity> getCommentsByBoard(BoardEntity board) {
        return commentRepository.findAllByBoardOrderByCreatedAtAsc(board);
    }
}

