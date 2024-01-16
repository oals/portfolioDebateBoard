package com.project.Debate.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DebatePostPreviewDTO {

    private Long debateArticleNo;
    private String debateTitle;
    private String debateSituation;
    private String debateCategory;
    private int debatePostView;
}
