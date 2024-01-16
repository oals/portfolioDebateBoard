package com.project.Debate.entity;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DebatePost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "debate_article_no")
    private Long debateArticleNo;
    private String debateTitle;     //제목

    @Column(length = 1000)
    private String debateSituation; //상황 설명
    private String debateCategory;  //카테고리
    @Column(length = 200)
    private String debateContentTextA;  //A의 논점
    @Column(length = 200)
    private String debateContentTextB;  //B의 논점
    private String debateStartDate;     //상황 발생일

    private String debateSelectTime;    //분쟁 게시글의 선택된 스레드 시간

    private String debateRunningTime;   //현재 스레드의 시간

    private int debatePostView;     //조회수
    private String debatePostDate;      //게시물 자성일
    private String debateResult;        //분쟁 결과
    private Boolean debateState;        //분쟁 상태

    private int aVotes;             //A의 득표수
    private int bVotes;             //B의 득표수
    private String debateWinner;       //승리한 논점

    @OneToOne(mappedBy = "debatePost", cascade=CascadeType.ALL, orphanRemoval=true , fetch = FetchType.LAZY)
    private DebatePostImage debatePostImage;    //포스트 이미지

    @OneToMany(mappedBy = "debatePost", cascade=CascadeType.ALL, orphanRemoval=true ,fetch = FetchType.LAZY)
    private List<DebateComment> debateComments = new ArrayList<>(); //포스트 댓글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;  //작성자


    @OneToMany(mappedBy = "debatePost", cascade=CascadeType.ALL, orphanRemoval=true ,fetch = FetchType.LAZY)
    private List<DebatePostProsAndCons> postProsAndCons = new ArrayList<>();    //포스트의 A 혹은 B 투표 현황




}
