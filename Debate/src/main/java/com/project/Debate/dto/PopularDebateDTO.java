package com.project.Debate.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PopularDebateDTO {

    private Long debateArticleNo;
    private String debateCategory;
    private String debateTitle;
    private String debateContentImagePath;

}
