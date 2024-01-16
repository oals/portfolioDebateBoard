package com.project.Debate.service;

import com.project.Debate.dto.DebateCommentDTO;
import com.project.Debate.dto.PageRequestDTO;
import com.project.Debate.dto.PageResponseDTO;

import java.util.List;

public interface DebateCommentService {


    void InsertDebateComment(DebateCommentDTO debateCommentDTO);


    List<DebateCommentDTO> SelectDebateComment(Long debateArticleNo, PageRequestDTO pageRequestDTO);

    PageResponseDTO<DebateCommentDTO> SelectDebateCommentPaging(Long debateArticleNo, PageRequestDTO pageRequestDTO);

    boolean updateCommentLike(Long debateCommentNo,String memberId);

    void deleteComment(Long debateCommentNo);

}
