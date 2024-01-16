package com.project.Debate.repository;

import com.project.Debate.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;

public interface  MemberRepository extends JpaRepository<Member,String>, QuerydslPredicateExecutor<Member> {
}
