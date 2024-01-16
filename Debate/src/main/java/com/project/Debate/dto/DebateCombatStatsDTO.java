package com.project.Debate.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DebateCombatStatsDTO {

    private int memberDebateWinCount;
    private int memberDebateLoseCount;
    private int memberDebateDrawCount;
    private Double memberWinningPercent ;



}
