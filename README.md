
# portfolioDebate
분쟁 해결 웹 사이트 개인 프로젝트 포트폴리오

# 소개
여러 문제들의 분쟁을 해결 할 수 있도록 작성자가 분쟁 내용을 정리해 게시하면 <BR>
사이트 이용자들이 판단해 A와 B 둘 중 누가 잘못했는지 투표 할 수 있는 사이트 입니다.


# 제작기간 & 참여 인원
<UL>
  <LI>2023.12.27 ~ 2024.01.15</LI>
  <LI>개인 프로젝트</LI>
</UL>



# 사용기술
![js](https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white)
![js](https://img.shields.io/badge/Java-FF0000?style=for-the-badge&logo=Java&logoColor=white)
![js](https://img.shields.io/badge/IntelliJ-004088?style=for-the-badge&logo=IntelliJ&logoColor=white)
![js](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=MariaDB&logoColor=white)
![js](https://img.shields.io/badge/security-6DB33F?style=for-the-badge&logo=security&logoColor=white)

![js](https://img.shields.io/badge/jquery-0769AD?style=for-the-badge&logo=jquery&logoColor=white)
![js](https://img.shields.io/badge/bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)
![js](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=JavaScript&logoColor=white)
![js](https://img.shields.io/badge/React-0769Ac?style=for-the-badge&logo=React&logoColor=white)
# E-R 다이어그램


![DebateERD](https://github.com/oals/portfolioDebateBoard/assets/136543676/64a7db24-534f-419c-a654-b51fad2eb7bf)


# 핵심 기능 및 페이지 소개

<details>
 <summary> Member Entity 
 
 </summary> 
 

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    @Entity
    @Table(name="member")
    public class Member {

    @Id
    @Column(nullable = false,length = 50,name="member_id")
    private String memberId;

    private String memberPswd;
    private String memberName;
    private String memberPhone;
    private String memberAddress;

    private int memberDebateWinCount;       //분쟁의 승리를 맞춘 수
    private int memberDebateLoseCount;      //분쟁의 승리를 맞추지 못한 수
    private int memberDebateDrawCount;      //분쟁의 무승부 수

    private Double memberWinningPercent;    //승률
    private String memberDate;

    @Enumerated(EnumType.STRING)
    private Role role;  //권한 설정


    public static Member createMember(MemberDTO memberDTO, PasswordEncoder passwordEncoder){

        Member member = new Member();
        member.setMemberId(memberDTO.getMemberId());
        member.setMemberName(memberDTO.getMemberName());
        member.setMemberPhone(memberDTO.getMemberPhone());
        member.setMemberAddress(memberDTO.getMemberAddress());
        member.setMemberDebateWinCount(memberDTO.getMemberDebateWinCount());
        member.setMemberDebateLoseCount(memberDTO.getMemberDebateLoseCount());
        member.setMemberDebateDrawCount(memberDTO.getMemberDebateDrawCount());
        member.setMemberWinningPercent(memberDTO.getMemberWinningPercent());
        member.setMemberDate(memberDTO.getMemberDate());
        member.setRole(Role.USER);

        // 암호화
        String password = passwordEncoder.encode(memberDTO.getMemberPswd());
        member.setMemberPswd(password);

        return member;
    }



    }


</details>


<details>
 <summary> DebatePost Entity
 
 </summary> 
 

    @Entity
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public class DebatePost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "debate_article_no")
    private Long debateArticleNo;
    private String debateTitle;     //제목

    @Column(length = 1000)
    private String debateSituation; //상황 설명
    private String debateCategory;  //카테고리
    @Column(length = 200)
    private String debateContentTextA;  //A의 논점
    @Column(length = 200)
    private String debateContentTextB;  //B의 논점
    private String debateStartDate;     //상황 발생일

    private String debateSelectTime;    //분쟁 게시글의 선택된 스레드 시간

    private String debateRunningTime;   //현재 스레드의 시간

    private int debatePostView;     //조회수
    private String debatePostDate;      //게시물 자성일
    private String debateResult;        //분쟁 결과
    private Boolean debateState;        //분쟁 상태

    private int aVotes;             //A의 득표수
    private int bVotes;             //B의 득표수
    private String debateWinner;       //승리한 논점

    @OneToOne(mappedBy = "debatePost", cascade=CascadeType.ALL, orphanRemoval=true , fetch = FetchType.LAZY)
    private DebatePostImage debatePostImage;    //포스트 이미지

    @OneToMany(mappedBy = "debatePost", cascade=CascadeType.ALL, orphanRemoval=true ,fetch = FetchType.LAZY)
    private List<DebateComment> debateComments = new ArrayList<>(); //포스트 댓글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;  //작성자


    @OneToMany(mappedBy = "debatePost", cascade=CascadeType.ALL, orphanRemoval=true ,fetch = FetchType.LAZY)
    private List<DebatePostProsAndCons> postProsAndCons = new ArrayList<>();    //포스트의 A 혹은 B 투표 현황




    }




</details>



<details>
 <summary> DebatePostImage Entity
 
 </summary> 
 


    @Entity
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @Setter
    public class DebatePostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long debatePostImageNo;

    private String debateContentVideoPath;  //영상 주소
    private String debateContentImagePath;  //이미지 주소
    private String debateContentImagePath2; //이미지 주소2
    private String debateContentImagePath3; //이미지 주소3

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "debate_article_no")
    private DebatePost debatePost;      //해당 포스트



    }



</details>



<details>
 <summary> DebatePostProsAndCons Entity


 
 </summary> 
 

    @Entity
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public class DebatePostProsAndCons {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pros_and_cons_no")
    private Long prosAndConsNo;


    private String debateVoteState; //사용자가 투표한 A 혹은 B

    private String debateVoteDate;  //투표 버튼을 누른 날짜


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;          //투표를 한 사용자


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "debate_article_no")
    private DebatePost debatePost;      //투표 된 게시물





    }



</details>




<details>
 <summary> DebateComment Entity
 
 </summary> 



    @Entity
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @Setter
    public class DebateComment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="debate_comment_no")
    private Long debateCommentNo;
    private String debatePostComment;   //댓글 내용
    @Nullable
    private Long debatePostCommentParent;   //부모 댓글 (사용하지 않았음)

    private String debatePostCommentDate;   //댓글 작성일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;                  //작성자


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "debate_article_no")
    private DebatePost debatePost;      //댓글이 작성된 포스트


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "pros_and_cons_no")
    private DebatePostProsAndCons debatePostProsAndCons;    //작성자의 투표 현황

    @OneToMany(mappedBy = "debateComment",cascade=CascadeType.ALL, orphanRemoval=true ,fetch = FetchType.LAZY)
    List<DebateCommentLike> debateCommentLikeList = new ArrayList<>();  //작성자가 받은 댓글 공감 수




    }


 
 

</details>


<details>
 <summary> DebateCommentLike Entity
 
 </summary> 


    
    @Entity
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public class DebateCommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long debateCommentLikeNo;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;      //공감 버튼을 누른 유저


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="debate_comment_no")
    private DebateComment debateComment;    //해당 공감 버튼의 댓글


    }





 
 

</details>



<details>
 <summary> DebateBookMark Entity
 
 </summary> 



    @Entity
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public class DebateBookMark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long debateBookMarkNo;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;  //북마크한 회원 정보


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "debate_article_no")
    private DebatePost debatePost;  //북마크한 포스트




    }

 
</details>



<hr>

<h3>메인 페이지</h3>
<br>
<BR>



![메인페이지 (1)](https://github.com/oals/portfolioDebateBoard/assets/136543676/f6b97541-a254-400a-884e-b543b0c28dc8)


<UL>
  <LI>작성 되는 최신 글들을 카테고리 관계 없이 확인 할 수 있도록 설계 했습니다.</LI>
</UL>










<h3>분쟁 게시판</h3>
<br>


![board페이지](https://github.com/oals/portfolioDebateBoard/assets/136543676/0608b9d2-1085-4ec1-9112-561b99fafbb6)



<br>
<br>

<details>
 <summary> 분쟁 게시판 플로우 차트
 
 </summary> 
 

</details>



<details>
 <summary> 분쟁 게시판 Service 코드
 
 </summary> 



</details>





<UL>
  <LI>ㅁㄴㄻㄴㄹ </LI>

</UL>


<br>
<br>


<h3>검색</h3>
<br>

![검색](https://github.com/oals/portfolioDebateBoard/assets/136543676/1361ac8c-0274-4acf-a19c-cc19e18aa327)


<br>
<br>

<details>
 <summary> 
 검색 플로우 차트
 </summary> 
 
</details>


<details>
 <summary> 
 검색 Service
 </summary> 
 



    
</details>


<UL>
  <LI></LI>

</UL>

<br>
<br>


<h3>포스트 작성</h3>
<br>

<img src='https://github.com/oals/portfolioDebateBoard/assets/136543676/018a5c4c-b570-4513-ac06-5a12b89623a4' width='1000px'>


<br>
<br>

<details>
 <summary> 
 포스트 작성 플로우 차트
 </summary> 


 
</details>


<details> 
 <summary> 
 포스트 Service
 </summary> 
 



    
</details>


<UL>
  <LI></LI>

</UL>

<br>
<br>




<h3>포스트</h3>
<br>

![post페이지](https://github.com/oals/portfolioDebateBoard/assets/136543676/594c3148-4681-4753-b72c-e728d1cfefc7)


<br>
<br>

<details>
 <summary> 
 포스트 플로우 차트
 </summary> 
 
</details>


<details>
 <summary> 
 포스트 Service
 </summary> 
 



    
</details>


<UL>
  <LI></LI>

</UL>

<br>
<br>



<h3>포스트 (투표 종료)</h3>
<br>

![분쟁종료board페이지](https://github.com/oals/portfolioDebateBoard/assets/136543676/cb4d9374-a8de-4d0c-bd26-d0367fc9950c)

<br>
<br>

<details>
 <summary> 
 포스트 (투표 종료) 플로우 차트
 </summary> 
 
</details>


<details>
 <summary> 
  포스트 (투표 종료) Service
 </summary> 
 



    
</details>


<UL>
  <LI></LI>

</UL>

<br>
<br>


<h3>스레드</h3>
<br>

<br>
<br>
<details>
 <summary> 스레드 플로우 차트
 
 </summary> 
 

</details>


<details>
 <summary> 스레드 Service 코드
 
 </summary> 
 




</details>


<UL>
  <LI>다중 스레드를 사용하여 1분 간격으로 남은 이용 시간이 업데이트 되도록 구현했습니다. </LI>
 <LI>남은 이용 시간이 00:40분 보다 작을 때만 이용 시간 연장 기능이 가능하도록 구현했습니다.</LI>
 <LI>1인 1좌석을 사용 할 수 있도록 현재 사용 중인 좌석 엔티티를 생성하여 중복 좌석을 방지하였습니다.</LI>
</UL>


<br>
<br>


<h3>댓글</h3>
<br>

![댓글-작성-공감-삭제](https://github.com/oals/portfolioDebateBoard/assets/136543676/c127058c-29a1-492a-8cd9-1ab6529f4119)

<br>
<br>

<details>
 <summary> 
 댓글 플로우 차트
 </summary> 
 
</details>


<details>
 <summary> 
 댓글 Service
 </summary> 
 



    
</details>


<UL>
  <LI></LI>

</UL>

<br>
<br>





<h3>카테고리</h3>
<br>

![카테고리별-게시판](https://github.com/oals/portfolioDebateBoard/assets/136543676/ea345b91-c59d-4be1-af44-71471815cbfb)

<br>
<br>

<details>
 <summary> 
 카테고리 플로우 차트
 </summary> 
 
</details>


<details>
 <summary> 
 카테고리 Service 
 </summary> 
 



    
</details>


<UL>
  <LI></LI>

</UL>

<br>
<br>



<h3>마이 페이지</h3>
<br>

![마이페이지](https://github.com/oals/portfolioDebateBoard/assets/136543676/fab15567-44f8-4665-8634-83b7fef38ab8)

<br>
<br>

<details>
 <summary> 
 마이페이지 플로우 차트
 </summary> 
 
</details>


<details>
 <summary> 
 마이페이지 Service
 </summary> 
 



    
</details>


<UL>
  <LI></LI>

</UL>

<br>
<br>


<h3>북마크</h3>
<br>

![마이페이지-북마크-삭제](https://github.com/oals/portfolioDebateBoard/assets/136543676/3dc6fad1-6405-40ce-b4a9-dd498df86c5f)


<br>
<br>

<details>
 <summary> 
 북마크 플로우 차트
 </summary> 
 
</details>


<details>
 <summary> 
 북마크 Service
 </summary> 
 



    
</details>


<UL>
  <LI></LI>

</UL>

<br>
<br>



<h3>회원가입&로그인</h3>
<br>



<br>
<br>
<details>
 <summary> 회원가입 플로우 차트
 
 </summary> 
 

</details>


<UL>
  <LI>정규 표현식을 사용하여 생년월일과 전화번호의 잘못된 입력을 방지하였습니다. </LI>
 <LI>우편 번호 API를 사용하여 주소를 검색 할 수 있도록 구현했습니다. </LI>
</UL>


<br>
<br>

# 프로젝트를 통해 느낀 점과 소감














