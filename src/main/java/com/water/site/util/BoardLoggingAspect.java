package com.water.site.util;

import com.water.site.entity.BoardHistory;
import com.water.site.entity.BoardViewHistory;
import com.water.site.repository.BoardHistoryRepository;
import com.water.site.repository.BoardViewHistoryRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class BoardLoggingAspect {

    private final BoardHistoryRepository boardHistoryRepository;
    private final BoardViewHistoryRepository boardViewHistoryRepository;
    private final HttpServletRequest request;

    /** 게시글 CRUD 기록 */
    @AfterReturning(
            pointcut = "execution(* com.water.site.service.BoardService.create(..)) && args(req, username)",
            returning = "ret",
            argNames = "joinPoint,req,username,ret")
    public void recordCreate(JoinPoint joinPoint, Object req, Object username, Object ret) {
        if (!(ret instanceof Long boardId)) return; // 반환값 체크
        boardHistoryRepository.save(BoardHistory.builder()
                .boardId(boardId)
                .username(username.toString())
                .action("CREATE")
                .actionAt(LocalDateTime.now())
                .build());
    }

    @AfterReturning(
            pointcut = "execution(* com.water.site.service.BoardService.update(..)) && args(id, req)",
            returning = "ret",
            argNames = "joinPoint,id,req,ret")
    public void recordUpdate(JoinPoint joinPoint, Object id, Object req, Object ret) {
        boardHistoryRepository.save(BoardHistory.builder()
                .boardId(Long.parseLong(id.toString()))
                .username(SecurityContextHolder.getContext().getAuthentication().getName())
                .action("UPDATE")
                .actionAt(LocalDateTime.now())
                .build());
    }

    @AfterReturning(
            value = "execution(* com.water.site.service.BoardService.delete(..)) && args(id, username)",
            argNames = "joinPoint,id,username")
    public void recordDelete(JoinPoint joinPoint, Object id, Object username) {
        boardHistoryRepository.save(BoardHistory.builder()
                .boardId(Long.parseLong(id.toString()))
                .username(username.toString())
                .action("DELETE")
                .actionAt(LocalDateTime.now())
                .build());
    }

    /** 조회 기록 (반환값 없이 접근만 기록) */
    @Before("execution(* com.water.site.service.BoardService.get(..)) && args(id,..)")
    public void recordView(Long id) {
        BoardViewHistory view = new BoardViewHistory();
        view.setBoardId(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        view.setUsername(auth != null ? auth.getName() : "ANONYMOUS");
        // IP는 HttpServletRequest에서 받아서 setIpAddress 가능
        boardViewHistoryRepository.save(view);
    }
}