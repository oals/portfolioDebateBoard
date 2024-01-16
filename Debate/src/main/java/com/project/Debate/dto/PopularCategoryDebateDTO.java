package com.project.Debate.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PopularCategoryDebateDTO {

    private Long debateArticleNo;
    private String debateTitle;
    private int debatePostView;

    private String debateRunningTime;

    private String debateResult;

    private String memberId;


}
