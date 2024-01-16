package com.project.Debate.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyPageDebateListDTO {

    private String memberId;

    private Long debateArticleNo;
    private String debateTitle;
    private String  debateCategory;

    private String debateRunningTime;
    private int debatePostView;
    private String debateResult;
    private String debatePostDate;

    private String debateVoteDate;
}
