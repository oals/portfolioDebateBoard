package com.project.Debate.repository;

import com.project.Debate.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterRepository extends JpaRepository<Member,String> {
}
