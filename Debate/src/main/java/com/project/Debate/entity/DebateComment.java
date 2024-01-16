package com.project.Debate.entity;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DebateComment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="debate_comment_no")
    private Long debateCommentNo;
    private String debatePostComment;   //댓글 내용
    @Nullable
    private Long debatePostCommentParent;   //부모 댓글 (사용하지 않았음)

    private String debatePostCommentDate;   //댓글 작성일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;                  //작성자


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "debate_article_no")
    private DebatePost debatePost;      //댓글이 작성된 포스트


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "pros_and_cons_no")
    private DebatePostProsAndCons debatePostProsAndCons;    //작성자의 투표 현황

    @OneToMany(mappedBy = "debateComment",cascade=CascadeType.ALL, orphanRemoval=true ,fetch = FetchType.LAZY)
    List<DebateCommentLike> debateCommentLikeList = new ArrayList<>();  //작성자가 받은 댓글 공감 수




}
