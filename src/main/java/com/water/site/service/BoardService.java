package com.water.site.service;

import com.water.site.entity.Board;
import com.water.site.repository.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Board findById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다."));
    }

    @Transactional
    public void update(Long id, Board updatedBoard) {
        Board board = findById(id);
        board.setTitle(updatedBoard.getTitle());
        board.setContent(updatedBoard.getContent());
    }
}
