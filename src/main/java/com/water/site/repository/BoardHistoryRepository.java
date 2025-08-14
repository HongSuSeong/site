package com.water.site.repository;

import com.water.site.entity.BoardHistory;
import com.water.site.entity.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardHistoryRepository extends JpaRepository<BoardHistory, Long> {
}
