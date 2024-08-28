package com.example.demo.repository;

import com.example.demo.repository.search.BoardSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.domain.Board;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {

	//Page<Board> findByTitleContainingOrderByBnoDesc(String keyword, Pageable pageable);
	
	@Query("select b from Board b where b.title like concat('%',:keyword,'%')")
	Page<Board> findKeyword(String keyword, Pageable pageable);

	@Query(value = "select now()", nativeQuery = true)
	String getTime();

	@EntityGraph(attributePaths = {"imageSet"})
	@Query("select b from Board b where b.bno =:bno")
	Optional<Board> findByIdWithImage(@Param("bno") Long bno);
}
