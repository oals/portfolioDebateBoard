package com.project.Debate.repository;

import com.project.Debate.entity.DebatePostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface DebatePostImageRepository extends JpaRepository<DebatePostImage,Long>, QuerydslPredicateExecutor<DebatePostImage> {
}
