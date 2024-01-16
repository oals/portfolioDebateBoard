package com.project.Debate.service;

import com.project.Debate.dto.DebateCombatStatsDTO;
import com.project.Debate.dto.DebateCommentDTO;
import com.project.Debate.dto.MemberDTO;

import java.util.List;
import java.util.Map;

public interface MemberService {

    boolean searchId(String memberId);


    void updateMemberDebateWinningPercent(Map<String,String> memberNameList,String result);

    MemberDTO findByMember(String memberId);

    DebateCombatStatsDTO SelectMemberWinRate(String memberId);


}
