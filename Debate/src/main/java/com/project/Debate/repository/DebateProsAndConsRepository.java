package com.project.Debate.repository;

import com.project.Debate.entity.DebatePostProsAndCons;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface DebateProsAndConsRepository extends JpaRepository<DebatePostProsAndCons,Long>, QuerydslPredicateExecutor<DebatePostProsAndCons> {
}
