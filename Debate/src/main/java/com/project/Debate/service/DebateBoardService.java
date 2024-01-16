package com.project.Debate.service;

import com.project.Debate.dto.*;

import java.io.IOException;
import java.util.List;

public interface DebateBoardService {

    Long GetDebateBoardArticleNo();


    void CreateDebatePost(DebatePostDTO debatePostDTO);

    void UpdateDebatePostViewCount(Long DebateArticleNo);

    List<DebatePostDTO> SelectSearchDebatePost(String debateSearchOpt,String debateSearchData,String debateCategoryName,PageRequestDTO pageRequestDTO);
    PageResponseDTO<DebatePostDTO> SelectSearchDebatePostPaging(String debateSearchOpt,String debateSearchData,String debateCategoryName,PageRequestDTO pageRequestDTO);


    List<DebatePostPreviewDTO> SelectDebatePostPreview();

    DebatePostDTO GetSelectDebatePost(Long DebateArticleNo);


    List<MyPageDebateListDTO> SelectMyPageDebatePost(String memberId,  PageRequestDTO pageRequestDTO);

    PageResponseDTO<MyPageDebateListDTO> SelectMyPageDebatePostPaging(String memberId,  PageRequestDTO pageRequestDTO);

    List<MyPageDebateListDTO>  SelectMyPageLatestDebate(String memberId,  PageRequestDTO pageRequestDTO);

    PageResponseDTO<MyPageDebateListDTO>  SelectMyPageLatestDebatePaging(String memberId,  PageRequestDTO pageRequestDTO);


    List<DebatePostDTO> SelectDebatePost(String DebateCategoryName, String debateResult, PageRequestDTO pageRequestDTO);

    PageResponseDTO<DebatePostDTO> SelectDebatePostPaging(String DebateCategoryName, String debateResult, PageRequestDTO pageRequestDTO);


    List<LatestDebateDTO> SelectLatestDebate(String memberId);




    List<PopularDebateDTO> SelectPopularDebate();

    List<PopularCategoryDebateDTO> SelectPopularCategoryDebate(String debateCategoryName);

    void deleteDebatePost(Long debateArticleNo);

    void updateDebatePost(DebatePostDTO debatePostDTO);

    void updateDebatePostImage(DebatePostDTO debatePostDTO) throws IOException;

    boolean ProsAndConsCheck(Long debateArticleNo,String memberId);


    void ProsAndCons(Long debateArticleNo,String DebateVoteState,String memberId);

}
