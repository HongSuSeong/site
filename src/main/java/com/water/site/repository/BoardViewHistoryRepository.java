package com.water.site.repository;

import com.water.site.entity.BoardHistory;
import com.water.site.entity.BoardViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardViewHistoryRepository extends JpaRepository<BoardViewHistory, Long> {
}
