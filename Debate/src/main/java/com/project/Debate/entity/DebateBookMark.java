package com.project.Debate.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DebateBookMark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long debateBookMarkNo;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;  //북마크한 회원 정보


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "debate_article_no")
    private DebatePost debatePost;  //북마크한 포스트




}
