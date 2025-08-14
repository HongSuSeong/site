package com.water.site.repository;


import com.water.site.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    Page<BoardEntity> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
            String title, String content, Pageable pageable);


    @Query("SELECT b FROM BoardEntity b JOIN b.author u " +
            "WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "   OR LOWER(b.contentSearch) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "   OR LOWER(u.name) LIKE LOWER(CONCAT('%', :q, '%'))")
    Page<BoardEntity> searchAll(@Param("q") String keyword, Pageable pageable);
}
