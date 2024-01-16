package com.project.Debate.repository;

import com.project.Debate.entity.DebateComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface DebateCommentRepository extends JpaRepository<DebateComment,Long>, QuerydslPredicateExecutor<DebateComment> {
}
