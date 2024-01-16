package com.project.Debate.service;

import com.project.Debate.dto.DebateBookMarkDTO;
import com.project.Debate.dto.PageRequestDTO;
import com.project.Debate.dto.PageResponseDTO;
import com.project.Debate.entity.DebateBookMark;

import java.util.List;

public interface DebateBookMarkService {


    void debatePostBookMark(String memberId,Long debateArticleNo);

    boolean debatePostBookMarkChk(String memberId,Long debateArticleNo);

    List<DebateBookMarkDTO> SelectDebateBookMArk(String memberId, PageRequestDTO pageRequestDTO);

    PageResponseDTO<DebateBookMarkDTO> SelectDebateBookMarkPaging(String memberId, PageRequestDTO pageRequestDTO);



    void AllDeleteBookMark(String memberId);
    void DeleteBookMark(Long ArticleNo);

}
