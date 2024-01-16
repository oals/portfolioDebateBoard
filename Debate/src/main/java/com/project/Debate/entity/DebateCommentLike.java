package com.project.Debate.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DebateCommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long debateCommentLikeNo;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;      //공감 버튼을 누른 유저


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="debate_comment_no")
    private DebateComment debateComment;    //해당 공감 버튼의 댓글


}
