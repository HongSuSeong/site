package com.water.site.service;

import com.water.site.dto.BoardResponse;
import com.water.site.repository.UserRepository;
import com.water.site.request.BoardCreateRequest;
import com.water.site.entity.BoardEntity;
import com.water.site.entity.UserEntity;
import com.water.site.repository.BoardRepository;
import com.water.site.request.BoardUpdateRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<BoardResponse> list(String q, Pageable pageable) {
        Page<BoardEntity> page = (q == null || q.isBlank())
                ? boardRepository.findAll(pageable)
                : boardRepository.searchAll(q.trim(), pageable);
        return page.map(this::toDto);
    }

    public BoardResponse get(Long id, boolean increaseView) {
        BoardEntity b = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        if (increaseView) {
            b.setViewCount(b.getViewCount() + 1);
        }
        return toDto(b);
    }

    public Long create(BoardCreateRequest req, String username) {
        UserEntity author = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        BoardEntity b = BoardEntity.builder()
                .title(req.getTitle())
                .content(req.getContent())
                .author(author)
                .password((req.getPassword() == null || req.getPassword().isBlank())
                        ? null
                        : passwordEncoder.encode(req.getPassword()))
                .build();
        return boardRepository.save(b).getId();
    }

    public void update(Long id, BoardUpdateRequest req, String username) throws AccessDeniedException {
        BoardEntity b = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 작성자 또는 관리자만
        boolean isAuthor = b.getAuthor().getId().equals(user.getId());
        boolean isAdmin = user.getRole() != null && user.getRole().contains("ROLE_ADMIN");
        if (!isAuthor && !isAdmin) {
            throw new AccessDeniedException("작성자만 수정할 수 있습니다.");
        }

        // 글 비밀번호가 설정돼 있다면 추가 검증(선택 정책)
        if (b.getPassword() != null && !b.getPassword().isBlank()) {
            String input = req.getPasswordForEdit();
            if (input == null || input.isBlank() || !passwordEncoder.matches(input, b.getPassword())) {
                throw new IllegalArgumentException("글 비밀번호가 일치하지 않습니다.");
            }
        }

        b.setTitle(req.getTitle());
        b.setContent(req.getContent());
        // updatedAt은 @PreUpdate로 처리
    }

    public void delete(Long id, String username, String passwordForDelete) throws AccessDeniedException {
        BoardEntity b = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        boolean isAuthor = b.getAuthor().getId().equals(user.getId());
        boolean isAdmin = user.getRole() != null && user.getRole().contains("ROLE_ADMIN");
        if (!isAuthor && !isAdmin) {
            throw new AccessDeniedException("작성자만 삭제할 수 있습니다.");
        }

        if (b.getPassword() != null && !b.getPassword().isBlank() && !isAdmin) {
            // 관리자 삭제 시 비밀번호 우회 허용(정책)
            if (passwordForDelete == null || passwordForDelete.isBlank() ||
                    !passwordEncoder.matches(passwordForDelete, b.getPassword())) {
                throw new IllegalArgumentException("글 비밀번호가 일치하지 않습니다.");
            }
        }

        boardRepository.delete(b);
    }

    private BoardResponse toDto(BoardEntity b) {
        return new BoardResponse(
                b.getId(),
                b.getTitle(),
                b.getContent(),
                b.getAuthor().getUsername(),
                b.getViewCount(),
                b.getLikeCount(),
                b.getCreatedAt(),
                b.getUpdatedAt()
        );
    }
}
