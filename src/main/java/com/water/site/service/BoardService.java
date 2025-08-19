package com.water.site.service;

import com.water.site.dto.BoardResponse;
import com.water.site.entity.BoardEntity;
import com.water.site.entity.UserEntity;
import com.water.site.repository.BoardRepository;
import com.water.site.repository.UserRepository;
import com.water.site.request.BoardCreateRequest;
import com.water.site.request.BoardUpdateRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

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
    public BoardEntity getEntity(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
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

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!b.getAuthor().getUsername().equals(currentUsername)) {
            throw new AccessDeniedException("본인만 수정할 수 있습니다.");
        }

        b.setTitle(req.getTitle());
        b.setContent(req.getContent());
    }
    public void delete(Long id, String username, String passwordForDelete) throws AccessDeniedException {
        // 게시글 조회
        BoardEntity b = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        boolean isAuthor = b.getAuthor().getUsername().equals(currentUsername);
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAuthor && !isAdmin) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }

        // 글 비밀번호 검증 (관리자는 우회 가능)
        if (b.getPassword() != null && !b.getPassword().isBlank() && !isAdmin) {
            if (passwordForDelete == null || passwordForDelete.isBlank() ||
                    !passwordEncoder.matches(passwordForDelete, b.getPassword())) {
                throw new IllegalArgumentException("글 비밀번호가 일치하지 않습니다.");
            }
        }

        // 삭제
        boardRepository.delete(b);
    }
    private BoardResponse toDto(BoardEntity b) {
        return new BoardResponse(
                b.getId(),
                b.getTitle(),
                b.getContent(),
                b.getAuthor().getName(),
                b.getViewCount(),
                b.getLikeCount(),
                b.getCreatedAt(),
                b.getUpdatedAt(),
                b.getComments() != null ? b.getComments().size() : 0
        );
    }
}
