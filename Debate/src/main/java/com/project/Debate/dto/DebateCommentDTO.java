package com.project.Debate.dto;

import com.project.Debate.entity.DebateCommentLike;
import com.project.Debate.entity.DebatePost;
import com.project.Debate.entity.Member;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@ToString
public class DebateCommentDTO {

    private Long debateCommentNo;
    private String debatePostComment;
    @Nullable
    private Long debatePostCommentParent;
    private String debatePostCommentDate;
    private String memberId;
    private Long debateArticleNo;
    private String debateProsAndConsState;

    private int debateCommentLikeCount;



}
