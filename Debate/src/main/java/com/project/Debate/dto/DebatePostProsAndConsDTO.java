package com.project.Debate.dto;

import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DebatePostProsAndConsDTO {

    private Long prosAndConsNo;

    private String DebateVoteState;

    private String DebateVoteDate;

    private String memberId;

    private Long debateArticleNo;


}
