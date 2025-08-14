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

    public void update(Long id, BoardUpdateRequest req) throws AccessDeniedException {
        BoardEntity b = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        // 작성자 또는 관리자 확인
        boolean isAuthor = b.getAuthor().getUsername().equals(currentUsername);
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAuthor && !isAdmin) {
            throw new AccessDeniedException("작성자만 수정이 가능합니다.");
        }

        // 글 비밀번호가 설정돼 있다면 추가 검증(선택 정책)
        if (b.getPassword() != null && !b.getPassword().isBlank()) {
            String input = req.getPassword();
            if (input == null || input.isBlank() || !passwordEncoder.matches(input, b.getPassword())) {
                throw new IllegalArgumentException("글 비밀번호가 일치하지 않습니다.");
            }
        }

        b.setTitle(req.getTitle());
        b.setContent(req.getContent());
        // updatedAt은 @PreUpdate로 처리
    }

    public void delete(Long id, String username, String passwordForDelete) throws AccessDeniedException {
        // 게시글 조회
        BoardEntity b = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        // 현재 로그인 사용자 확인 (username 파라미터와 SecurityContext 모두 가능)
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        // 작성자 또는 관리자 확인
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
                b.getUpdatedAt()
        );
    }
}
