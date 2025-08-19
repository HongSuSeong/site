package com.water.site.repository;

import com.water.site.entity.BoardEntity;
import com.water.site.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByBoardIdOrderByCreatedAtDesc(Long boardId);

    List<CommentEntity> findByBoardIdOrderByCreatedAtAsc(Long boardId);

    List<CommentEntity> findAllByBoardOrderByCreatedAtAsc(BoardEntity board);
}

