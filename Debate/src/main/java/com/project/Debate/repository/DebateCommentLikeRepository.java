package com.project.Debate.repository;

import com.project.Debate.entity.DebateCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface DebateCommentLikeRepository extends JpaRepository<DebateCommentLike,Long>, QuerydslPredicateExecutor<DebateCommentLike> {
}
