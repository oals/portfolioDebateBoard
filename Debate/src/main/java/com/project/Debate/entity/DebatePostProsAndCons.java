package com.project.Debate.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DebatePostProsAndCons {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pros_and_cons_no")
    private Long prosAndConsNo;


    private String debateVoteState; //사용자가 투표한 A 혹은 B

    private String debateVoteDate;  //투표 버튼을 누른 날짜


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;          //투표를 한 사용자


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "debate_article_no")
    private DebatePost debatePost;      //투표 된 게시물





}
