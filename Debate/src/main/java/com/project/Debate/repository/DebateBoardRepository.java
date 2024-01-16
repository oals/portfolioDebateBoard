package com.project.Debate.repository;

import com.project.Debate.entity.DebatePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface DebateBoardRepository extends JpaRepository<DebatePost,Long>, QuerydslPredicateExecutor<DebatePost> {


    @Query("select max(d.debateArticleNo) + 1 from DebatePost d")
    Long GetDebatePostCount();



}
