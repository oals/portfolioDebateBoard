package com.project.Debate.repository;

import com.project.Debate.entity.DebateBookMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface DebateBookMarkRepository extends JpaRepository<DebateBookMark,Long> , QuerydslPredicateExecutor<DebateBookMark> {
}
